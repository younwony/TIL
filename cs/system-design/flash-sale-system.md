# 선착순 쿠폰 시스템 설계 (Flash Sale System)

> `[4] 심화` · 선수 지식: [Redis](./caching.md), [메시지 큐](./message-queue.md), 동시성 제어

> 대규모 동시 요청(만 명 이상)이 발생하는 선착순 이벤트에서 데이터 정합성과 시스템 안정성을 보장하는 설계 전략

## 왜 알아야 하는가?

- **실무**: 쿠폰, 티켓팅, 한정판 판매 등 실제 비즈니스 문제. 실패 시 직접적인 손실
- **면접**: "동시성 제어", "대규모 트래픽 처리" 역량을 종합적으로 평가하는 단골 질문
- **기반 지식**: 분산 락, Redis 원자 연산, 비동기 처리 등 고급 기술의 실전 활용

## 핵심 개념

- **동시성 제어**: 수만 건의 동시 요청에서 쿠폰 초과 발급 방지
- **분산 락**: 여러 서버에서 동일 자원에 대한 동시 접근 제어 (Redis, Redisson)
- **비동기 처리**: 메시지 큐로 요청을 버퍼링하여 DB 부하 분산
- **원자적 연산**: Redis DECR로 재고 감소의 원자성 보장
- **Rate Limiting**: 트래픽 급증 시 시스템 보호

## 쉽게 이해하기

**선착순 쿠폰 시스템**을 인기 콘서트 티켓팅에 비유할 수 있습니다.

### 문제 상황: 동시에 만 명이 달려든다

```
10:00:00 이벤트 시작!
┌────────────────────────────────────────────────┐
│     👤👤👤👤👤👤👤👤👤👤... (10,000명)         │
│              ↓ 동시 요청                        │
│         ┌─────────┐                            │
│         │ 쿠폰 100장 │                          │
│         └─────────┘                            │
└────────────────────────────────────────────────┘
```

### 잘못된 설계: 번호표 없는 매장

```
손님 A: "재고 확인" → 1개 남음
손님 B: "재고 확인" → 1개 남음 (동시에 조회)
손님 A: "구매!" → 성공
손님 B: "구매!" → 성공 ← 재고 없는데 팔림! (초과 발급)
```

### 올바른 설계: 번호표 시스템

| 비유 | 실제 구현 | 역할 |
|------|----------|------|
| 번호표 발급기 | Redis Queue | 요청 순서 보장 |
| "한 명씩 입장" | 분산 락 | 동시 접근 방지 |
| 대기열 안내판 | WebSocket | 실시간 순번 알림 |
| VIP 별도 입장 | Rate Limiting | 트래픽 제어 |
| 재고 실시간 표시 | Redis 원자 연산 | 정확한 재고 관리 |

### 왜 일반 DB로는 안 되나?

```
일반 DB (느림):
1. SELECT count → 2. 비즈니스 로직 → 3. UPDATE
   ↑ 이 사이에 다른 요청이 끼어듦

Redis (빠름 + 원자적):
1. DECR coupon_count → 즉시 결과 반환
   (읽기 + 수정 + 쓰기가 한 번에)
```

---

## 문제 분석

### 발생 가능한 문제들

| 문제 | 원인 | 결과 |
|------|------|------|
| **초과 발급** | Race Condition | 100장인데 150장 발급 |
| **시스템 다운** | DB 과부하 | 전체 서비스 장애 |
| **느린 응답** | 동기 처리 병목 | 사용자 이탈 |
| **불공정 배분** | 순서 미보장 | 먼저 클릭해도 실패 |
| **중복 발급** | 재시도 처리 미흡 | 한 사람이 여러 장 |

### 트래픽 패턴

```
         요청 수
           │
    10,000 │    ██
           │    ██
           │    ██
     1,000 │    ██
           │    ██ ██
       100 │ ██ ██ ██ ██
           └────────────────→ 시간
              ↑
           이벤트 시작
           (스파이크)
```

**특징:**
- 특정 시점에 트래픽 급증 (스파이크)
- 짧은 시간 내 승부 (수 초 ~ 수 분)
- 실패 시 재시도 폭증

---

## 시스템 아키텍처

### 전체 구조

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              사용자 요청                                 │
│                         (10,000+ 동시 접속)                              │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                              CDN / WAF                                   │
│                    (정적 자원 캐싱, DDoS 방어)                            │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           Load Balancer                                  │
│                    (L7, Rate Limiting 1차)                               │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
            ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
            │   API GW    │ │   API GW    │ │   API GW    │
            │ (인증,Rate  │ │ (인증,Rate  │ │ (인증,Rate  │
            │  Limit 2차) │ │  Limit 2차) │ │  Limit 2차) │
            └─────────────┘ └─────────────┘ └─────────────┘
                    │               │               │
                    └───────────────┼───────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    ▼               ▼               ▼
            ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
            │  Coupon     │ │  Coupon     │ │  Coupon     │
            │  Service    │ │  Service    │ │  Service    │
            │  (Pod 1)    │ │  (Pod 2)    │ │  (Pod N)    │
            └─────────────┘ └─────────────┘ └─────────────┘
                    │               │               │
                    └───────────────┼───────────────┘
                                    │
            ┌───────────────────────┼───────────────────────┐
            │                       │                       │
            ▼                       ▼                       ▼
    ┌───────────────┐      ┌───────────────┐      ┌───────────────┐
    │ Redis Cluster │      │ Message Queue │      │   Database    │
    │ ───────────── │      │ ───────────── │      │ ───────────── │
    │ • 재고 관리    │      │ • Kafka       │      │ • 쿠폰 발급   │
    │ • 분산 락     │      │ • 비동기 처리  │      │   이력 저장   │
    │ • 중복 체크   │      │ • 순서 보장    │      │ • 최종 정합성 │
    │ • 대기열      │      │               │      │               │
    └───────────────┘      └───────────────┘      └───────────────┘
```

### 요청 처리 흐름

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         쿠폰 발급 요청 흐름                               │
└─────────────────────────────────────────────────────────────────────────┘

[1단계: 진입 제어]
    │
    ├─ Rate Limiting ─────────────────→ 초과 시 429 Too Many Requests
    │
    ├─ 중복 요청 체크 (Redis) ─────────→ 이미 발급 시 거절
    │
    └─ 이벤트 시간 검증 ──────────────→ 시간 외 요청 거절

[2단계: 재고 확보 (핵심)]
    │
    ├─ Redis DECR (원자적 재고 감소)
    │   │
    │   ├─ 결과 >= 0 ──────────────────→ 재고 확보 성공
    │   │
    │   └─ 결과 < 0 ───────────────────→ 재고 소진 (INCR로 복구 후 실패 반환)

[3단계: 비동기 발급]
    │
    ├─ Kafka에 발급 이벤트 발행
    │
    └─ 즉시 "발급 진행 중" 응답

[4단계: 실제 발급 (Consumer)]
    │
    ├─ DB 트랜잭션으로 쿠폰 저장
    │
    ├─ 실패 시 Redis 재고 복구
    │
    └─ 사용자 알림 (Push/WebSocket)
```

---

## 핵심 구현 전략

### 1. Redis 원자적 재고 관리

**왜 Redis인가?**
- 단일 스레드: 원자적 연산 보장
- 인메모리: 초고속 응답 (수십만 TPS)
- DECR: 읽기 + 수정 + 쓰기를 한 번에

```
일반 접근 (문제 발생):
Thread A: GET stock → 100
Thread B: GET stock → 100
Thread A: SET stock 99
Thread B: SET stock 99  ← 둘 다 성공, 재고는 99 (1개 누락)

Redis DECR (안전):
Thread A: DECR stock → 99 (원자적)
Thread B: DECR stock → 98 (원자적)
```

### 2. 분산 락 (Redisson)

**언제 필요한가?**
- 같은 사용자의 동시 요청 방지
- 복잡한 비즈니스 로직 보호

```
사용자 A의 동시 요청:
요청 1: 락 획득 → 처리 중...
요청 2: 락 획득 실패 → 대기 또는 거절
요청 1: 처리 완료 → 락 해제
요청 2: (대기했다면) 락 획득 → 이미 발급됨 확인 → 거절
```

### 3. 메시지 큐 비동기 처리

**왜 비동기인가?**
- DB 부하 분산: 순간 만 건 → 초당 1,000건으로 평탄화
- 응답 속도 향상: DB 대기 없이 즉시 응답
- 장애 격리: DB 장애 시에도 요청 유실 방지

```
동기 처리 (문제):
10,000 요청 → DB → 10,000 동시 트랜잭션 → 타임아웃/다운

비동기 처리 (해결):
10,000 요청 → Redis로 재고 확보 → Kafka 큐잉 → 순차 DB 저장
     └── 즉시 응답 ──┘
```

### 4. 대기열 시스템

**왜 대기열인가?**
- 공정성: 먼저 요청한 사람이 먼저 처리
- 사용자 경험: 무한 대기 대신 순번 안내
- 부하 제어: 동시 처리량 제한

```
대기열 없이:
"504 Gateway Timeout" → 재시도 → 트래픽 2배 → 악순환

대기열 있으면:
"현재 1,234번째입니다. 예상 대기 시간: 2분" → 대기 → 처리
```

---

## 예제 코드

### Redis 재고 관리 (Lua Script)

```java
@Service
@RequiredArgsConstructor
public class CouponStockService {

    private final StringRedisTemplate redisTemplate;

    private static final String STOCK_KEY = "coupon:stock:";
    private static final String ISSUED_KEY = "coupon:issued:";

    // Lua 스크립트: 원자적 재고 확인 + 감소 + 중복 체크
    private static final String ISSUE_SCRIPT = """
        local stockKey = KEYS[1]
        local issuedKey = KEYS[2]
        local userId = ARGV[1]

        -- 중복 발급 체크
        if redis.call('SISMEMBER', issuedKey, userId) == 1 then
            return -2  -- 이미 발급됨
        end

        -- 재고 감소 (원자적)
        local stock = redis.call('DECR', stockKey)

        if stock < 0 then
            redis.call('INCR', stockKey)  -- 복구
            return -1  -- 재고 부족
        end

        -- 발급 기록
        redis.call('SADD', issuedKey, userId)
        return stock  -- 남은 재고
        """;

    public CouponIssueResult tryIssueCoupon(String couponId, String userId) {
        List<String> keys = List.of(
            STOCK_KEY + couponId,
            ISSUED_KEY + couponId
        );

        Long result = redisTemplate.execute(
            new DefaultRedisScript<>(ISSUE_SCRIPT, Long.class),
            keys,
            userId
        );

        return switch (result.intValue()) {
            case -2 -> CouponIssueResult.ALREADY_ISSUED;
            case -1 -> CouponIssueResult.OUT_OF_STOCK;
            default -> CouponIssueResult.SUCCESS;
        };
    }

    // 재고 초기화 (이벤트 시작 전)
    public void initializeStock(String couponId, int quantity) {
        redisTemplate.opsForValue().set(STOCK_KEY + couponId, String.valueOf(quantity));
        redisTemplate.delete(ISSUED_KEY + couponId);
    }
}
```

### Kafka를 통한 비동기 발급

```java
@Service
@RequiredArgsConstructor
public class CouponIssueFacade {

    private final CouponStockService stockService;
    private final KafkaTemplate<String, CouponIssueEvent> kafkaTemplate;

    private static final String TOPIC = "coupon-issue";

    @Transactional(readOnly = true)
    public CouponIssueResponse issueCoupon(CouponIssueRequest request) {
        String couponId = request.couponId();
        String userId = request.userId();

        // 1. Redis에서 재고 확보 (동기, 빠름)
        CouponIssueResult result = stockService.tryIssueCoupon(couponId, userId);

        if (result != CouponIssueResult.SUCCESS) {
            return CouponIssueResponse.fail(result);
        }

        // 2. Kafka로 발급 이벤트 발행 (비동기)
        CouponIssueEvent event = new CouponIssueEvent(
            couponId,
            userId,
            LocalDateTime.now()
        );
        kafkaTemplate.send(TOPIC, userId, event);

        // 3. 즉시 응답 (DB 대기 없음)
        return CouponIssueResponse.pending("쿠폰 발급이 진행 중입니다.");
    }
}
```

### Kafka Consumer (실제 발급)

```java
@Service
@RequiredArgsConstructor
public class CouponIssueConsumer {

    private final CouponRepository couponRepository;
    private final CouponStockService stockService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "coupon-issue", groupId = "coupon-consumer")
    @Transactional
    public void consume(CouponIssueEvent event) {
        try {
            // DB에 쿠폰 발급 저장
            Coupon coupon = Coupon.create(
                event.couponId(),
                event.userId(),
                event.requestedAt()
            );
            couponRepository.save(coupon);

            // 사용자 알림
            notificationService.sendCouponIssued(event.userId(), coupon);

        } catch (Exception e) {
            // 실패 시 Redis 재고 복구
            stockService.rollbackStock(event.couponId(), event.userId());

            // 실패 알림
            notificationService.sendCouponFailed(event.userId());

            throw e;  // 재시도 또는 DLQ 처리
        }
    }
}
```

### 분산 락 (Redisson)

```java
@Service
@RequiredArgsConstructor
public class CouponServiceWithLock {

    private final RedissonClient redissonClient;
    private final CouponIssueFacade couponIssueFacade;

    private static final String LOCK_PREFIX = "lock:coupon:user:";

    public CouponIssueResponse issueCouponWithLock(CouponIssueRequest request) {
        String lockKey = LOCK_PREFIX + request.userId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 3초 대기, 5초 후 자동 해제
            boolean acquired = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!acquired) {
                return CouponIssueResponse.fail(CouponIssueResult.TOO_MANY_REQUESTS);
            }

            return couponIssueFacade.issueCoupon(request);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CouponIssueResponse.fail(CouponIssueResult.SYSTEM_ERROR);

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
```

### Rate Limiting (Bucket4j + Redis)

```java
@Component
@RequiredArgsConstructor
public class RateLimitFilter implements WebFilter {

    private final ProxyManager<String> proxyManager;

    private static final int REQUESTS_PER_SECOND = 10;
    private static final int BURST_CAPACITY = 20;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = extractUserId(exchange);
        String key = "rate-limit:" + userId;

        Bucket bucket = proxyManager.builder()
            .build(key, () -> bucketConfiguration());

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    private BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.builder()
                .capacity(BURST_CAPACITY)
                .refillGreedy(REQUESTS_PER_SECOND, Duration.ofSeconds(1))
                .build())
            .build();
    }
}
```

### 대기열 시스템 (Sorted Set)

```java
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final StringRedisTemplate redisTemplate;

    private static final String QUEUE_KEY = "coupon:waiting:";

    // 대기열 등록
    public long enterQueue(String couponId, String userId) {
        String key = QUEUE_KEY + couponId;
        double score = System.currentTimeMillis();

        // 이미 대기열에 있으면 기존 순번 반환
        Double existingScore = redisTemplate.opsForZSet().score(key, userId);
        if (existingScore != null) {
            return getPosition(couponId, userId);
        }

        redisTemplate.opsForZSet().add(key, userId, score);
        return getPosition(couponId, userId);
    }

    // 현재 순번 조회
    public long getPosition(String couponId, String userId) {
        Long rank = redisTemplate.opsForZSet().rank(QUEUE_KEY + couponId, userId);
        return rank != null ? rank + 1 : -1;
    }

    // 대기열에서 N명 꺼내기 (스케줄러에서 호출)
    public List<String> pollUsers(String couponId, int count) {
        String key = QUEUE_KEY + couponId;

        Set<String> users = redisTemplate.opsForZSet().range(key, 0, count - 1);
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        List<String> result = new ArrayList<>(users);
        redisTemplate.opsForZSet().removeRange(key, 0, count - 1);

        return result;
    }
}
```

---

## 동작 원리 상세

### 정상 흐름 시퀀스

```
User          API GW        Coupon Service      Redis           Kafka         Consumer        DB
 │              │                │                │               │              │             │
 │─ 쿠폰 요청 ──▶│                │                │               │              │             │
 │              │─ Rate Limit ───▶                │               │              │             │
 │              │◀─ OK ──────────│                │               │              │             │
 │              │─ 발급 요청 ─────▶                │               │              │             │
 │              │                │─ DECR stock ──▶│               │              │             │
 │              │                │◀─ 99 (성공) ───│               │              │             │
 │              │                │─ SADD issued ─▶│               │              │             │
 │              │                │◀─ OK ──────────│               │              │             │
 │              │                │─ 이벤트 발행 ───────────────────▶              │             │
 │              │◀─ "발급 진행중" ─│                │               │              │             │
 │◀─ 202 ───────│                │                │               │              │             │
 │              │                │                │               │─ consume ────▶             │
 │              │                │                │               │              │─ INSERT ────▶
 │              │                │                │               │              │◀─ OK ───────│
 │◀──── Push 알림: "쿠폰 발급 완료" ───────────────────────────────────────────────│             │
```

### 재고 소진 시 흐름

```
User          Coupon Service      Redis
 │                │                │
 │─ 쿠폰 요청 ────▶│                │
 │                │─ DECR stock ──▶│
 │                │◀─ -1 (부족) ───│
 │                │─ INCR stock ──▶│  (복구)
 │                │◀─ 0 ───────────│
 │◀─ 409 소진 ────│                │
```

### 장애 복구 흐름

```
Consumer                    DB                Redis
   │                        │                   │
   │─ INSERT coupon ────────▶                   │
   │◀─ ERROR (timeout) ─────│                   │
   │                        │                   │
   │─ INCR stock (복구) ─────────────────────────▶
   │─ SREM issued (복구) ────────────────────────▶
   │                        │                   │
   │─ 재시도 큐(DLQ)로 이동 ─▶                   │
```

---

## 트레이드오프

| 전략 | 장점 | 단점 | 적합한 상황 |
|------|------|------|------------|
| **Redis DECR** | 초고속, 원자적 | 영속성 약함 | 모든 선착순 시스템 |
| **분산 락** | 정합성 보장 | 성능 저하 | 복잡한 비즈니스 로직 |
| **메시지 큐** | 부하 분산, 장애 격리 | 복잡성 증가, 지연 | 대규모 트래픽 |
| **대기열** | 공정성, UX 개선 | 구현 복잡 | 티켓팅, 한정판 |
| **DB 낙관적 락** | 단순, 충돌 적을 때 효율 | 충돌 많으면 재시도 폭증 | 동시성 낮은 경우 |
| **DB 비관적 락** | 충돌 방지 확실 | 성능 병목, 데드락 위험 | 정합성이 최우선 |

### 언제 무엇을 선택?

| 상황 | 권장 조합 |
|------|----------|
| 소규모 (100명 이하) | DB 낙관적 락만으로 충분 |
| 중규모 (1,000명) | Redis + 동기 DB 처리 |
| 대규모 (10,000명+) | Redis + Kafka + 대기열 |
| 초대규모 (100,000명+) | 위 + 샤딩 + 지역 분산 |

---

## 성능 최적화 팁

### 1. 정적 자원 분리

```
이벤트 페이지:
- 정적 HTML/CSS/JS → CDN에서 서빙
- API 호출만 서버로 → 서버 부하 최소화
```

### 2. 커넥션 풀 튜닝

```yaml
# Redis 커넥션 풀
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 100   # 동시 연결 수
          max-idle: 50
          min-idle: 10
          max-wait: 1000ms  # 대기 시간

# DB 커넥션 풀 (HikariCP)
  datasource:
    hikari:
      maximum-pool-size: 50
      minimum-idle: 10
      connection-timeout: 3000
```

### 3. 타임아웃 설정

```java
// Redis 타임아웃
@Bean
public LettuceClientConfiguration lettuceClientConfiguration() {
    return LettuceClientConfiguration.builder()
        .commandTimeout(Duration.ofMillis(500))
        .shutdownTimeout(Duration.ofSeconds(2))
        .build();
}
```

### 4. 서킷브레이커 적용

```java
@CircuitBreaker(name = "couponService", fallbackMethod = "fallback")
public CouponIssueResponse issueCoupon(CouponIssueRequest request) {
    return couponIssueFacade.issueCoupon(request);
}

private CouponIssueResponse fallback(CouponIssueRequest request, Exception e) {
    return CouponIssueResponse.fail(CouponIssueResult.SERVICE_UNAVAILABLE);
}
```

---

## 면접 예상 질문

### Q: 동시에 만 명이 쿠폰을 요청하면 어떻게 처리하나요?

**A:** 3단계로 처리합니다.

1. **Rate Limiting**: API Gateway에서 초당 요청 수 제한
2. **Redis 원자 연산**: DECR로 재고를 원자적으로 감소, 초과 발급 방지
3. **비동기 처리**: Kafka로 실제 발급을 비동기화하여 DB 부하 분산

**왜 Redis인가?**
- 초당 10만+ 연산 가능 (DB는 수천 수준)
- DECR이 원자적이라 락 없이도 안전
- 메모리 기반이라 지연 최소

**만약 Redis 없이 DB만 쓴다면?**
- 동시 트랜잭션으로 DB 락 경쟁 발생
- 타임아웃 증가, 커넥션 풀 고갈
- 시스템 전체 장애로 확산

---

### Q: Redis 장애 시 어떻게 대응하나요?

**A:**

1. **Redis Cluster**: 3개 이상 노드로 고가용성 확보
2. **Fallback**: Redis 장애 시 DB 직접 처리 (성능 저하 감수)
3. **서킷브레이커**: 연속 실패 시 빠른 실패 반환

```java
@CircuitBreaker(name = "redis")
public CouponIssueResult tryIssue(String couponId, String userId) {
    return stockService.tryIssueCoupon(couponId, userId);
}

// Fallback: DB 비관적 락으로 처리
private CouponIssueResult fallback(String couponId, String userId, Exception e) {
    return couponService.tryIssueWithDbLock(couponId, userId);
}
```

**왜 DB Fallback인가?**
- Redis 장애는 일시적 (보통 수 초 ~ 수 분)
- 느리더라도 서비스 지속이 중요
- 정합성은 DB가 보장

---

### Q: 이미 발급받은 사용자가 다시 요청하면?

**A:** Redis Set으로 O(1) 중복 체크합니다.

```
SISMEMBER coupon:issued:{couponId} {userId}
→ 1이면 이미 발급, -2 반환
→ 0이면 미발급, 발급 진행
```

**왜 DB가 아닌 Redis인가?**
- DB 조회: 네트워크 + 디스크 I/O (수 ms)
- Redis 조회: 네트워크 + 메모리 (수십 μs)
- 만 건 중복 체크 시 차이: 수십 초 vs 수백 ms

**Redis와 DB 불일치 시?**
- 최종 정합성: Consumer에서 DB INSERT 시 중복 체크
- Unique 제약조건: (user_id, coupon_id)로 DB 레벨 보장

---

### Q: 쿠폰 100장인데 101번째 요청은 어떻게 되나요?

**A:** Redis DECR 결과가 음수면 즉시 복구하고 실패 반환합니다.

```
Thread 100: DECR → 0 (성공, 마지막 1장)
Thread 101: DECR → -1 (실패)
          : INCR → 0 (복구)
          : 반환 "재고 소진"
```

**왜 INCR로 복구하나?**
- DECR 후 음수면 이미 재고 초과 시도
- INCR로 복구해야 다음 요청에 정확한 재고 반영
- 복구 안 하면 재고가 -1, -2... 계속 감소

---

### Q: Kafka Consumer가 실패하면 쿠폰은 어떻게 되나요?

**A:** 3단계 복구 전략을 적용합니다.

1. **자동 재시도**: Consumer에서 3회 재시도
2. **재고 복구**: 실패 확정 시 Redis INCR + SREM
3. **DLQ 처리**: Dead Letter Queue로 이동, 수동 처리

```java
@KafkaListener(topics = "coupon-issue")
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 1000, multiplier = 2),
    dltTopicSuffix = "-dlt"
)
public void consume(CouponIssueEvent event) {
    // 처리 로직
}

@DltHandler
public void handleDlt(CouponIssueEvent event) {
    stockService.rollbackStock(event.couponId(), event.userId());
    alertService.notifyFailure(event);
}
```

---

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [캐싱](./caching.md) | Redis 원자 연산, 분산 락 | 중급 |
| [메시지 큐](./message-queue.md) | Kafka 비동기 처리 | 중급 |
| [확장성](./scalability.md) | 대규모 트래픽 대응 | 입문 |
| [대규모 시스템 설계](./large-scale-system.md) | 전체 아키텍처 관점 | 심화 |

## 참고 자료

- [Redis Documentation - Transactions](https://redis.io/docs/interact/transactions/)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Redisson - Distributed Locks](https://github.com/redisson/redisson/wiki/8.-Distributed-locks-and-synchronizers)
- [우아한형제들 기술블로그 - 선착순 이벤트 서버 생존기](https://techblog.woowahan.com/)
- Designing Data-Intensive Applications (Martin Kleppmann)
