# 외부 API 통합 (External API Integration)

> `[3] 중급` · 선수 지식: [서킷브레이커](./circuit-breaker.md), [Rate Limiting](./rate-limiting.md)

> 외부 시스템과의 안정적인 통신을 위해 고려해야 할 설계 원칙과 패턴

`#ExternalAPI` `#API통합` `#APIIntegration` `#Retry` `#재시도` `#Backoff` `#ExponentialBackoff` `#Jitter` `#Timeout` `#타임아웃` `#Idempotency` `#멱등성` `#IdempotencyKey` `#CircuitBreaker` `#Fallback` `#폴백` `#GracefulDegradation` `#ConnectionPool` `#커넥션풀` `#KeepAlive` `#HTTPClient` `#Resilience` `#FaultTolerance` `#장애허용` `#APIGateway` `#RateLimiting` `#BulkheadPattern` `#429` `#503`

## 왜 알아야 하는가?

- **실무**: 대부분의 서비스는 결제, 알림, 인증 등 외부 API에 의존. 장애 대응 능력이 필수
- **면접**: "외부 API 장애 시 어떻게 대응하나요?" "재시도 전략은?" 빈출 질문
- **기반 지식**: MSA, 분산 시스템에서 서비스 간 통신의 기본 원칙

## 핵심 개념

- **Timeout**: 무한 대기 방지, 빠른 실패 유도
- **Retry + Backoff**: 일시적 장애 극복, 서버 부하 분산
- **Circuit Breaker**: 지속적 장애 시 빠른 차단
- **Idempotency**: 재시도해도 안전한 설계
- **Fallback**: 장애 시 대체 응답으로 서비스 유지

## 쉽게 이해하기

외부 API 호출을 **전화 통화**에 비유할 수 있습니다.

### 전화 통화 = API 호출

| 상황 | 전화 | API |
|------|------|-----|
| **Timeout** | 10초 신호 후 끊기 | 응답 없으면 연결 종료 |
| **Retry** | 통화중이면 다시 걸기 | 실패 시 재요청 |
| **Backoff** | 바로 걸지 말고 1분 후 다시 | 재시도 간격 점점 늘리기 |
| **Circuit Breaker** | 계속 안 받으면 나중에 | 연속 실패 시 당분간 시도 안 함 |
| **Fallback** | 문자로 대신 연락 | 캐시 데이터로 응답 |

### 왜 이렇게 복잡해야 하는가?

```
외부 API 없이 단순 호출하면?
┌─────────────────────────────────────────────────────────────┐
│ 1. 결제 API 3초 지연 → 우리 서버 스레드 3초 점유             │
│ 2. 동시 요청 1000개 → 3000초(50분)의 스레드 자원 점유        │
│ 3. 스레드 풀 고갈 → 다른 API도 응답 불가                     │
│ 4. 서비스 전체 다운!                                         │
└─────────────────────────────────────────────────────────────┘

적절한 설계가 있다면?
┌─────────────────────────────────────────────────────────────┐
│ 1. Timeout 1초 설정 → 1초 내 응답 없으면 빠르게 실패         │
│ 2. Circuit Breaker → 5번 실패 후 바로 실패 응답              │
│ 3. Fallback → "결제 처리 중, 잠시 후 확인해주세요"           │
│ 4. 다른 기능은 정상 동작!                                    │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 1. Timeout 전략

**왜 중요한가?**
- 외부 API가 응답하지 않으면 무한 대기 → 스레드 고갈
- 빠른 실패(Fail Fast)로 리소스 보호

#### Timeout 종류

| 종류 | 설명 | 권장값 |
|------|------|--------|
| **Connection Timeout** | TCP 연결 수립까지 대기 시간 | 1~3초 |
| **Read Timeout** | 응답 데이터 수신까지 대기 시간 | 3~10초 |
| **Write Timeout** | 요청 데이터 전송까지 대기 시간 | 3~10초 |

#### Timeout 설정 원칙

```
┌─────────────────────────────────────────────────────────────┐
│                 Timeout 설정 체크리스트                       │
├─────────────────────────────────────────────────────────────┤
│ □ 외부 API의 SLA 확인 (보통 p99 응답 시간)                   │
│ □ 우리 서비스의 SLA 고려 (전체 응답 시간 예산)               │
│ □ Retry 횟수 고려 (Timeout × Retry = 최대 대기 시간)         │
│ □ 사용자 경험 고려 (너무 길면 UX 저하)                       │
└─────────────────────────────────────────────────────────────┘
```

#### Java 코드 예시

```java
// RestTemplate 설정
@Bean
public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(3000);  // 연결 타임아웃 3초
    factory.setReadTimeout(5000);     // 읽기 타임아웃 5초
    return new RestTemplate(factory);
}

// WebClient 설정 (Spring WebFlux)
@Bean
public WebClient webClient() {
    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
        .responseTimeout(Duration.ofSeconds(5));

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
}

// OkHttp 설정
OkHttpClient client = new OkHttpClient.Builder()
    .connectTimeout(3, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .writeTimeout(5, TimeUnit.SECONDS)
    .build();
```

### 2. Retry 정책

**언제 재시도해야 하는가?**
- 일시적 네트워크 오류 (DNS 실패, 연결 리셋)
- 서버 과부하 (503, 429)
- 타임아웃 (간헐적 지연)

**언제 재시도하면 안 되는가?**
- 클라이언트 오류 (400, 401, 403, 404)
- 비즈니스 로직 오류 (잔액 부족, 중복 요청)
- 멱등하지 않은 요청 (POST로 생성 시 주의)

#### HTTP 상태 코드별 재시도 전략

| 상태 코드 | 의미 | 재시도 여부 | 이유 |
|-----------|------|-------------|------|
| **200** | 성공 | X | 성공 |
| **400** | Bad Request | X | 요청 수정 필요 |
| **401** | Unauthorized | △ | 토큰 갱신 후 1회 |
| **403** | Forbidden | X | 권한 없음 |
| **404** | Not Found | X | 리소스 없음 |
| **408** | Request Timeout | O | 일시적 문제 가능 |
| **429** | Too Many Requests | O | Retry-After 헤더 확인 |
| **500** | Internal Server Error | △ | 서버 버그일 수도 |
| **502** | Bad Gateway | O | 일시적 문제 가능 |
| **503** | Service Unavailable | O | 일시적 과부하 |
| **504** | Gateway Timeout | O | 일시적 문제 가능 |

#### 재시도 횟수 결정

```
┌─────────────────────────────────────────────────────────────┐
│ 재시도 횟수 = (허용 가능한 최대 지연 시간) / (Timeout + Backoff)│
│                                                              │
│ 예시: 최대 30초 허용, Timeout 5초, 평균 Backoff 5초          │
│      → 30 / (5 + 5) = 3회 재시도                            │
└─────────────────────────────────────────────────────────────┘
```

### 3. Backoff 전략

**왜 Backoff가 필요한가?**
- 즉시 재시도하면 아직 복구 안 된 서버에 부하 가중
- 여러 클라이언트가 동시에 재시도 → 더 큰 장애 유발 (Thundering Herd)

#### Backoff 알고리즘

| 알고리즘 | 공식 | 예시 (base=1초) |
|---------|------|-----------------|
| **Fixed** | base | 1초 → 1초 → 1초 |
| **Linear** | base × n | 1초 → 2초 → 3초 |
| **Exponential** | base × 2^n | 1초 → 2초 → 4초 → 8초 |
| **Exponential + Jitter** | (base × 2^n) + random | 1초 → 2.3초 → 4.7초 |

#### Exponential Backoff + Jitter

```
재시도 1: 1초 + random(0~1초) = 1.3초 대기
재시도 2: 2초 + random(0~2초) = 3.7초 대기
재시도 3: 4초 + random(0~4초) = 6.2초 대기
재시도 4: 8초 + random(0~8초) = 12.1초 대기 (max 도달)

왜 Jitter(랜덤)?
┌─────────────────────────────────────────────────────────────┐
│ Jitter 없이 Exponential만 사용하면:                          │
│   클라이언트 1000개가 동시에 실패                            │
│   → 1초 후 1000개 동시 재시도                               │
│   → 2초 후 1000개 동시 재시도 (Thundering Herd!)            │
│                                                              │
│ Jitter 추가하면:                                             │
│   각 클라이언트가 0.5~1.5초 사이 랜덤 시점에 재시도          │
│   → 부하가 분산됨                                           │
└─────────────────────────────────────────────────────────────┘
```

#### Java 코드 예시 (Resilience4j)

```java
// Retry 설정
RetryConfig retryConfig = RetryConfig.custom()
    .maxAttempts(3)
    .waitDuration(Duration.ofMillis(1000))
    .intervalFunction(IntervalFunction.ofExponentialBackoff(
        Duration.ofMillis(1000),  // 초기 대기 시간
        2.0,                      // 배수
        Duration.ofSeconds(10)    // 최대 대기 시간
    ))
    .retryOnResult(response -> response.getStatusCode().is5xxServerError())
    .retryExceptions(IOException.class, TimeoutException.class)
    .ignoreExceptions(BusinessException.class)
    .build();

Retry retry = Retry.of("externalApi", retryConfig);

// 사용
Supplier<Response> supplier = () -> externalApiClient.call(request);
Response response = retry.executeSupplier(supplier);
```

```java
// Spring Retry 어노테이션 방식
@Retryable(
    value = {RestClientException.class, TimeoutException.class},
    maxAttempts = 3,
    backoff = @Backoff(
        delay = 1000,      // 초기 대기 시간 1초
        multiplier = 2,    // 배수
        maxDelay = 10000   // 최대 대기 시간 10초
    )
)
public PaymentResponse processPayment(PaymentRequest request) {
    return paymentClient.pay(request);
}

@Recover
public PaymentResponse fallbackPayment(Exception e, PaymentRequest request) {
    return PaymentResponse.pending("일시적 오류, 나중에 확인해주세요");
}
```

### 4. 멱등성 (Idempotency)

**왜 중요한가?**
- 네트워크 오류로 응답을 못 받았지만, 실제로는 처리됨
- 재시도하면 중복 처리 발생 (결제 2번, 주문 2번)

#### HTTP 메서드별 멱등성

| 메서드 | 멱등성 | 안전성 | 설명 |
|--------|--------|--------|------|
| **GET** | O | O | 조회, 부수 효과 없음 |
| **HEAD** | O | O | GET과 동일, 본문 없음 |
| **PUT** | O | X | 전체 리소스 덮어쓰기 |
| **DELETE** | O | X | 삭제 (이미 없으면 무시) |
| **POST** | X | X | 생성, 재시도 시 중복 가능! |
| **PATCH** | X | X | 부분 수정, 상대적 변경 시 중복 가능 |

#### Idempotency Key 패턴

```
┌─────────────────────────────────────────────────────────────┐
│                    Idempotency Key 흐름                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Client                          Server                      │
│    │                               │                         │
│    │ POST /payments               │                         │
│    │ Idempotency-Key: abc123      │                         │
│    │───────────────────────────────>│                        │
│    │                               │ 키 확인: 처음 본 키     │
│    │                               │ → 처리 후 결과 저장     │
│    │<───────────────────────────────│                        │
│    │ 201 Created                  │                         │
│    │                               │                         │
│    │ (네트워크 오류로 응답 못 받음)   │                        │
│    │                               │                         │
│    │ POST /payments (재시도)       │                         │
│    │ Idempotency-Key: abc123      │                         │
│    │───────────────────────────────>│                        │
│    │                               │ 키 확인: 이미 있음!     │
│    │                               │ → 저장된 결과 반환      │
│    │<───────────────────────────────│                        │
│    │ 201 Created (동일 결과)       │                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

#### Java 코드 예시

```java
// 클라이언트 측: Idempotency Key 생성 및 전송
public PaymentResponse processPayment(PaymentRequest request) {
    String idempotencyKey = generateIdempotencyKey(request);

    return webClient.post()
        .uri("/payments")
        .header("Idempotency-Key", idempotencyKey)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(PaymentResponse.class)
        .block();
}

private String generateIdempotencyKey(PaymentRequest request) {
    // 요청의 고유 조합으로 키 생성
    return DigestUtils.sha256Hex(
        request.getUserId() +
        request.getOrderId() +
        request.getAmount()
    );
}
```

```java
// 서버 측: Idempotency Key 검증
@PostMapping("/payments")
public ResponseEntity<PaymentResponse> createPayment(
    @RequestHeader("Idempotency-Key") String idempotencyKey,
    @RequestBody PaymentRequest request) {

    // 1. 이미 처리된 요청인지 확인
    Optional<PaymentResponse> cached =
        idempotencyStore.get(idempotencyKey);
    if (cached.isPresent()) {
        return ResponseEntity.ok(cached.get());
    }

    // 2. 처리 중인 요청인지 확인 (분산 락)
    if (!lockService.tryLock(idempotencyKey)) {
        return ResponseEntity.status(409).build();
    }

    try {
        // 3. 실제 처리
        PaymentResponse response = paymentService.process(request);

        // 4. 결과 저장 (TTL: 24시간)
        idempotencyStore.save(idempotencyKey, response, Duration.ofHours(24));

        return ResponseEntity.status(201).body(response);
    } finally {
        lockService.unlock(idempotencyKey);
    }
}
```

### 5. Connection Pool 관리

**왜 중요한가?**
- HTTP 연결 수립 비용이 높음 (TCP 핸드셰이크, TLS 핸드셰이크)
- 연결 재사용으로 성능 향상

#### Connection Pool 설정

| 설정 | 설명 | 권장값 |
|------|------|--------|
| **maxConnections** | 최대 연결 수 | 외부 API 성능에 따라 |
| **maxConnectionsPerRoute** | 호스트당 최대 연결 | 20~50 |
| **connectionTTL** | 연결 유지 시간 | 5분 |
| **validateAfterInactivity** | 유휴 후 검증 시간 | 1초 |

```java
// Apache HttpClient Connection Pool
PoolingHttpClientConnectionManager connectionManager =
    new PoolingHttpClientConnectionManager();
connectionManager.setMaxTotal(100);           // 전체 최대 연결
connectionManager.setDefaultMaxPerRoute(20);  // 호스트당 최대 연결

CloseableHttpClient httpClient = HttpClients.custom()
    .setConnectionManager(connectionManager)
    .evictIdleConnections(5, TimeUnit.MINUTES)
    .build();
```

### 6. Fallback 전략

장애 시 대체 응답으로 서비스 연속성 유지

| 전략 | 설명 | 적용 예시 |
|------|------|----------|
| **캐시 반환** | 이전에 캐시된 데이터 | 환율, 날씨, 상품 정보 |
| **기본값 반환** | 미리 정의된 기본 응답 | 추천 시스템 → 인기 상품 |
| **Graceful Degradation** | 기능 축소 운영 | 실시간 재고 → "재고 확인 중" |
| **대체 서비스** | 백업 서비스 호출 | 결제사 A 장애 → 결제사 B |
| **큐잉** | 나중에 처리하도록 저장 | 알림 발송 → 큐에 저장 |

```java
@CircuitBreaker(name = "exchangeRate", fallbackMethod = "fallbackRate")
public ExchangeRate getExchangeRate(String currency) {
    return exchangeRateClient.getRate(currency);
}

// Fallback 우선순위
private ExchangeRate fallbackRate(String currency, Exception e) {
    // 1순위: 캐시된 데이터
    ExchangeRate cached = cacheService.get("rate:" + currency);
    if (cached != null && !cached.isExpired(Duration.ofHours(1))) {
        return cached.markAsCached();
    }

    // 2순위: 대체 서비스
    try {
        return backupExchangeRateClient.getRate(currency);
    } catch (Exception backupError) {
        // 3순위: 기본값
        return ExchangeRate.defaultRate(currency);
    }
}
```

### 7. 모니터링 및 알림

외부 API 상태를 실시간으로 파악하고 장애에 빠르게 대응

#### 필수 메트릭

| 메트릭 | 설명 | 알림 조건 |
|--------|------|----------|
| **응답 시간 (p99)** | 99% 요청의 응답 시간 | > 임계값의 2배 |
| **에러율** | 실패 요청 비율 | > 5% |
| **서킷 상태** | Open/Closed/Half-Open | Open 전환 시 |
| **재시도 횟수** | 재시도 발생 빈도 | 급증 시 |
| **Timeout 발생 수** | 타임아웃 빈도 | 급증 시 |

```java
// Micrometer를 이용한 메트릭 수집
@Component
public class ExternalApiMetrics {

    private final MeterRegistry registry;
    private final Timer apiTimer;
    private final Counter errorCounter;

    public ExternalApiMetrics(MeterRegistry registry) {
        this.registry = registry;
        this.apiTimer = Timer.builder("external.api.duration")
            .tag("api", "payment")
            .register(registry);
        this.errorCounter = Counter.builder("external.api.errors")
            .tag("api", "payment")
            .register(registry);
    }

    public <T> T recordApiCall(Supplier<T> apiCall) {
        return apiTimer.record(() -> {
            try {
                return apiCall.get();
            } catch (Exception e) {
                errorCounter.increment();
                throw e;
            }
        });
    }
}
```

### 8. 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         외부 API 호출 아키텍처                            │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌──────────┐                                                            │
│  │ 클라이언트 │                                                           │
│  └────┬─────┘                                                            │
│       │                                                                   │
│       ▼                                                                   │
│  ┌──────────┐                                                            │
│  │ API Layer │                                                           │
│  └────┬─────┘                                                            │
│       │                                                                   │
│       ▼                                                                   │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                    External API Client Layer                      │    │
│  │  ┌────────────────────────────────────────────────────────────┐  │    │
│  │  │                      Resilience Layer                        │  │    │
│  │  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │  │    │
│  │  │  │ Timeout │→ │  Retry  │→ │ Circuit │→ │Fallback │       │  │    │
│  │  │  │         │  │+Backoff │  │ Breaker │  │         │       │  │    │
│  │  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘       │  │    │
│  │  └────────────────────────────────────────────────────────────┘  │    │
│  │                              │                                    │    │
│  │  ┌────────────────────────────────────────────────────────────┐  │    │
│  │  │                    Connection Pool                          │  │    │
│  │  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐                  │  │    │
│  │  │  │conn1│ │conn2│ │conn3│ │conn4│ │conn5│ ...              │  │    │
│  │  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘                  │  │    │
│  │  └────────────────────────────────────────────────────────────┘  │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│       │                                                                   │
│       ▼                                                                   │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │                        외부 API 서버                                │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐                            │  │
│  │  │ 결제 API │  │ 알림 API │  │ 인증 API │                           │  │
│  │  └─────────┘  └─────────┘  └─────────┘                            │  │
│  └───────────────────────────────────────────────────────────────────┘  │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 항목 | 보수적 설정 | 공격적 설정 |
|------|------------|------------|
| **Timeout** | 짧음 (1초) | 길음 (10초) |
| | + 빠른 실패, 리소스 보호 | + 느린 API도 처리 가능 |
| | - 정상 요청도 실패 가능 | - 스레드 점유 시간 증가 |
| **Retry 횟수** | 적음 (1~2회) | 많음 (5회 이상) |
| | + 빠른 응답, 부하 적음 | + 일시적 장애 극복 |
| | - 복구 가능한 오류도 실패 | - 응답 지연, 부하 증가 |
| **Circuit Breaker 임계값** | 낮음 (30%) | 높음 (70%) |
| | + 빠른 장애 격리 | + 불필요한 차단 방지 |
| | - 일시적 오류에도 차단 | - 장애 전파 위험 |

## 트러블슈팅

### 사례 1: Thundering Herd로 인한 연쇄 장애

#### 증상
```
외부 결제 API 장애 복구 후 우리 서버까지 다운
로그: "Connection pool exhausted", "Thread pool rejected"
```

#### 원인 분석
- 결제 API 장애 시 모든 요청이 Retry
- 동일한 Backoff로 동시에 재시도 → 복구된 서버에 과부하
- Connection Pool 고갈

#### 해결 방법
```java
// Jitter 추가
.intervalFunction(IntervalFunction.ofExponentialRandomBackoff(
    Duration.ofMillis(1000),
    2.0,
    0.5  // 50% 랜덤 범위
))

// Bulkhead로 동시 요청 수 제한
Bulkhead bulkhead = Bulkhead.of("payment", BulkheadConfig.custom()
    .maxConcurrentCalls(10)
    .maxWaitDuration(Duration.ofMillis(500))
    .build());
```

#### 예방 조치
- Exponential Backoff + Jitter 필수 적용
- Bulkhead로 동시 요청 수 제한
- Circuit Breaker로 조기 차단

### 사례 2: 멱등성 미구현으로 중복 결제

#### 증상
```
고객: "결제가 2번 됐어요!"
로그: 동일 주문에 대해 2개의 결제 성공 기록
```

#### 원인 분석
- 첫 번째 요청: 결제 성공, 응답 전송 중 네트워크 오류
- 클라이언트: 응답 못 받음, 재시도
- 두 번째 요청: 새로운 결제로 처리됨

#### 해결 방법
```java
// Idempotency Key 필수 적용
@PostMapping("/payments")
public ResponseEntity<?> pay(
    @RequestHeader("Idempotency-Key") String idempotencyKey,
    @RequestBody PaymentRequest request) {
    // ... Idempotency Key로 중복 검증
}
```

#### 예방 조치
- POST 요청에 Idempotency Key 필수
- 서버에서 24시간 동안 Key-Response 매핑 저장
- 중복 요청 시 저장된 응답 반환

## 면접 예상 질문

### Q: 외부 API 호출 시 Timeout을 왜 설정해야 하나요?

A: 외부 API가 응답하지 않으면 스레드가 무한 대기하게 되고, 스레드 풀이 고갈되어 전체 서비스가 마비됩니다. Timeout을 설정하면 빠르게 실패하여 스레드를 반환하고, Circuit Breaker나 Fallback으로 대응할 수 있습니다. **실무에서는** Connection Timeout(1~3초)과 Read Timeout(3~10초)을 분리하여 설정하고, 외부 API의 SLA를 기준으로 결정합니다.

### Q: Exponential Backoff에 Jitter를 추가하는 이유는?

A: 여러 클라이언트가 동시에 실패하면 Exponential Backoff만으로는 동일한 시점에 재시도하게 됩니다(Thundering Herd). 예를 들어 1000개 요청이 모두 1초 후, 2초 후에 동시 재시도하면 복구된 서버에 더 큰 부하를 줍니다. Jitter(랜덤 지연)를 추가하면 재시도 시점이 분산되어 서버 부하가 완화됩니다.

### Q: 멱등성(Idempotency)이 왜 중요한가요?

A: 네트워크 오류로 응답을 못 받았지만 실제로는 처리된 경우, 재시도하면 중복 처리됩니다(결제 2번 등). 멱등성을 보장하면 같은 요청을 여러 번 보내도 한 번만 처리되어 안전하게 재시도할 수 있습니다. **구현 방법**은 Idempotency Key를 요청에 포함하고, 서버에서 키별로 처리 결과를 저장하여 중복 요청 시 저장된 결과를 반환합니다.

### Q: Circuit Breaker와 Retry를 함께 사용할 때 주의점은?

A: Retry는 Circuit Breaker **바깥**에 위치해야 합니다. Circuit Breaker가 Open 상태일 때는 재시도하지 않고 바로 Fallback으로 가야 합니다. 순서는 `Retry → Circuit Breaker → (실제 호출)` 입니다. 또한 Retry 횟수와 Backoff를 고려하여 전체 응답 시간이 사용자 경험에 영향 없는 범위 내인지 확인해야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [서킷브레이커](./circuit-breaker.md) | 장애 격리 패턴 상세 | [4] 심화 |
| [Rate Limiting](./rate-limiting.md) | 요청 제한 알고리즘 상세 | [3] 중급 |
| [가용성](./availability.md) | 고가용성 개념 | [2] 입문 |
| [메시지 큐](./message-queue.md) | 비동기 통신으로 장애 격리 | [3] 중급 |

## 참고 자료

- [AWS - Exponential Backoff and Jitter](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/)
- [Stripe - Idempotent Requests](https://stripe.com/docs/api/idempotent_requests)
- [Resilience4j 공식 문서](https://resilience4j.readme.io/docs)
- [Microsoft - Retry Pattern](https://docs.microsoft.com/en-us/azure/architecture/patterns/retry)
- [Martin Fowler - Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)
