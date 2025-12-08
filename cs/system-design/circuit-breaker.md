# 서킷브레이커 (Circuit Breaker)

> 외부 시스템 장애 시 연쇄적인 실패를 방지하고, 시스템의 안정성을 유지하기 위한 장애 격리 패턴

## 핵심 개념

- **장애 전파 방지**: 하나의 서비스 장애가 전체 시스템으로 확산되는 것을 차단
- **빠른 실패(Fail Fast)**: 장애 감지 시 즉시 실패를 반환하여 리소스 낭비 방지
- **자동 복구**: 일정 시간 후 자동으로 서비스 상태를 확인하고 정상화
- **세 가지 상태**: Closed, Open, Half-Open 상태로 동작
- **임계값 기반**: 실패율, 응답 시간 등의 임계값을 기준으로 상태 전환

## 상세 설명

### 서킷브레이커가 필요한 이유

MSA(Microservice Architecture) 환경에서는 서비스 간 의존성이 높다. 하나의 서비스가 장애를 일으키면:

1. 호출하는 서비스가 타임아웃까지 대기
2. 스레드 풀이 고갈되어 다른 요청 처리 불가
3. 연쇄적으로 다른 서비스까지 장애 전파 (Cascading Failure)

서킷브레이커는 이러한 장애 전파를 조기에 차단한다.

### 세 가지 상태

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│    ┌──────────┐      실패 임계값      ┌──────────┐         │
│    │          │      초과             │          │         │
│    │  CLOSED  │ ──────────────────▶   │   OPEN   │         │
│    │          │                       │          │         │
│    └──────────┘                       └──────────┘         │
│         ▲                                  │               │
│         │                                  │               │
│         │ 성공                    타임아웃 │               │
│         │                         만료     │               │
│         │                                  ▼               │
│         │                            ┌──────────┐          │
│         │                            │          │          │
│         └─────────────────────────── │HALF-OPEN │          │
│                  성공 시              │          │          │
│                                      └──────────┘          │
│                                           │                │
│                                           │ 실패 시        │
│                                           └──────▶ OPEN    │
└─────────────────────────────────────────────────────────────┘
```

| 상태 | 설명 |
|------|------|
| **Closed** | 정상 상태. 모든 요청이 통과하며, 실패를 카운트 |
| **Open** | 차단 상태. 모든 요청이 즉시 실패 처리 (Fallback 실행) |
| **Half-Open** | 테스트 상태. 제한된 요청만 허용하여 복구 여부 확인 |

### 주요 설정 값

| 설정 | 설명 | 예시 |
|------|------|------|
| `failureRateThreshold` | 실패율 임계값 (%) | 50% |
| `slowCallRateThreshold` | 느린 호출 비율 임계값 | 80% |
| `slowCallDurationThreshold` | 느린 호출 기준 시간 | 3초 |
| `waitDurationInOpenState` | Open 상태 유지 시간 | 60초 |
| `permittedNumberOfCallsInHalfOpenState` | Half-Open에서 허용 요청 수 | 10개 |
| `slidingWindowSize` | 실패율 계산 윈도우 크기 | 100 |

## 동작 원리

### 상태 전환 흐름

1. **Closed → Open**
   - 슬라이딩 윈도우 내 실패율이 임계값 초과
   - 예: 최근 100건 중 50건 이상 실패 시

2. **Open → Half-Open**
   - `waitDurationInOpenState` 시간 경과 후 자동 전환
   - 타이머 기반 자동 복구 시도

3. **Half-Open → Closed**
   - 허용된 테스트 요청이 성공하면 정상 복구

4. **Half-Open → Open**
   - 테스트 요청이 실패하면 다시 차단 상태로

### 슬라이딩 윈도우 방식

| 방식 | 설명 |
|------|------|
| Count-based | 최근 N개 요청 기준 (예: 최근 100건) |
| Time-based | 최근 N초 동안의 요청 기준 (예: 최근 60초) |

## 예제 코드

### Resilience4j를 이용한 구현 (Java/Spring)

```java
// 의존성: resilience4j-spring-boot2

// 설정 (application.yml)
/*
resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        registerHealthIndicator: true
        slidingWindowSize: 100
        failureRateThreshold: 50
        waitDurationInOpenState: 60000
        permittedNumberOfCallsInHalfOpenState: 10
        slowCallRateThreshold: 80
        slowCallDurationThreshold: 3000
*/

@Service
public class PaymentService {

    private final CircuitBreaker circuitBreaker;
    private final PaymentClient paymentClient;

    public PaymentService(CircuitBreakerRegistry registry, PaymentClient paymentClient) {
        this.circuitBreaker = registry.circuitBreaker("paymentService");
        this.paymentClient = paymentClient;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        // 서킷브레이커로 감싸서 호출
        return circuitBreaker.executeSupplier(() ->
            paymentClient.pay(request)
        );
    }

    // 어노테이션 방식
    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackPayment")
    public PaymentResponse processPaymentWithAnnotation(PaymentRequest request) {
        return paymentClient.pay(request);
    }

    // Fallback 메서드
    private PaymentResponse fallbackPayment(PaymentRequest request, Exception e) {
        // 대체 응답 또는 캐시된 데이터 반환
        return PaymentResponse.builder()
            .status("PENDING")
            .message("결제 서비스 일시 불가. 나중에 다시 시도해주세요.")
            .build();
    }
}
```

### 상태 모니터링

```java
@Component
public class CircuitBreakerEventListener {

    private static final Logger log = LoggerFactory.getLogger(CircuitBreakerEventListener.class);

    public CircuitBreakerEventListener(CircuitBreakerRegistry registry) {
        registry.circuitBreaker("paymentService")
            .getEventPublisher()
            .onStateTransition(event ->
                log.warn("CircuitBreaker 상태 변경: {} -> {}",
                    event.getStateTransition().getFromState(),
                    event.getStateTransition().getToState())
            )
            .onFailureRateExceeded(event ->
                log.error("실패율 임계값 초과: {}%", event.getFailureRate())
            );
    }
}
```

## 관련 패턴

| 패턴 | 설명 |
|------|------|
| **Retry** | 일시적 실패 시 재시도 (서킷브레이커와 함께 사용) |
| **Timeout** | 응답 시간 제한 설정 |
| **Bulkhead** | 리소스 격리로 장애 영향 범위 제한 |
| **Rate Limiter** | 요청 수 제한으로 과부하 방지 |
| **Fallback** | 장애 시 대체 응답 제공 |

## 서킷브레이커 구현체 비교

| 라이브러리 | 특징 |
|------------|------|
| **Resilience4j** | 경량, 함수형 스타일, Spring Boot 통합 우수 |
| **Hystrix** | Netflix OSS, 현재 유지보수 모드 (deprecated) |
| **Sentinel** | Alibaba, 대규모 트래픽 처리에 강점 |

## 면접 예상 질문

- Q: 서킷브레이커 패턴이란 무엇이며, 왜 필요한가요?
  - A: 서킷브레이커는 외부 서비스 장애 시 빠르게 실패를 반환하여 연쇄적인 장애 전파(Cascading Failure)를 방지하는 패턴입니다. MSA 환경에서 하나의 서비스 장애가 전체 시스템을 마비시키는 것을 막고, 시스템의 전체적인 안정성과 복원력을 높이기 위해 필요합니다.

- Q: 서킷브레이커의 세 가지 상태와 각 상태 전환 조건을 설명해주세요.
  - A: Closed(정상), Open(차단), Half-Open(테스트) 세 가지 상태가 있습니다. Closed에서 실패율이 임계값을 초과하면 Open으로 전환되어 모든 요청을 차단합니다. 일정 시간 후 Half-Open으로 전환되어 제한된 요청으로 복구 여부를 테스트하고, 성공하면 Closed로, 실패하면 다시 Open으로 돌아갑니다.

- Q: 서킷브레이커와 함께 사용하면 좋은 패턴은 무엇인가요?
  - A: Retry(재시도), Timeout(시간 제한), Bulkhead(격벽), Fallback(대체 응답) 패턴과 함께 사용합니다. 단, Retry는 서킷브레이커 바깥에 위치해야 하며, Open 상태에서는 재시도하지 않도록 구성해야 합니다. Fallback은 서킷이 열렸을 때 대체 응답을 제공하여 사용자 경험을 유지합니다.

## 참고 자료

- [Resilience4j 공식 문서](https://resilience4j.readme.io/docs)
- [Martin Fowler - Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)
- [Microsoft - Circuit Breaker Pattern](https://docs.microsoft.com/en-us/azure/architecture/patterns/circuit-breaker)
