# 서킷브레이커 (Circuit Breaker)

> `[4] 심화` · 선수 지식: [가용성](./availability.md), MSA 개념

> 외부 시스템 장애 시 연쇄적인 실패를 방지하고, 시스템의 안정성을 유지하기 위한 장애 격리 패턴

`#서킷브레이커` `#CircuitBreaker` `#장애격리` `#FaultIsolation` `#Resilience4j` `#Hystrix` `#MSA` `#FailFast` `#빠른실패` `#장애허용` `#FaultTolerance` `#폴백` `#Fallback` `#Closed` `#Open` `#HalfOpen` `#상태전환` `#슬라이딩윈도우` `#SlidingWindow` `#임계값` `#Threshold` `#Bulkhead` `#격벽` `#Retry` `#재시도` `#Timeout` `#CascadingFailure` `#연쇄장애` `#Sentinel`

## 왜 알아야 하는가?

- **실무**: MSA 환경에서 필수 패턴. 한 서비스 장애가 전체 시스템으로 번지는 것을 방지
- **면접**: "외부 API 장애 시 어떻게 대응하나요?" 질문의 핵심 답변
- **기반 지식**: Resilience4j, Hystrix 등 장애 허용 라이브러리 이해의 기초

## 핵심 개념

- **장애 전파 방지**: 하나의 서비스 장애가 전체 시스템으로 확산되는 것을 차단
- **빠른 실패(Fail Fast)**: 장애 감지 시 즉시 실패를 반환하여 리소스 낭비 방지
- **자동 복구**: 일정 시간 후 자동으로 서비스 상태를 확인하고 정상화
- **세 가지 상태**: Closed, Open, Half-Open 상태로 동작
- **임계값 기반**: 실패율, 응답 시간 등의 임계값을 기준으로 상태 전환

## 쉽게 이해하기

**서킷브레이커**를 가정집의 차단기(두꺼비집)에 비유할 수 있습니다.

### 서킷브레이커 = 두꺼비집 (전기 차단기)

전기 합선이나 과부하가 발생하면 두꺼비집이 내려가면서 전기를 차단합니다. 왜 그럴까요?

- **차단 안 하면**: 전선이 타고 → 화재 발생 → 집 전체가 위험
- **차단하면**: 해당 회로만 끊김 → 나머지는 안전

소프트웨어도 마찬가지입니다:
- 결제 서비스가 응답이 없을 때, 계속 기다리면?
- 스레드가 쌓이고 → 메모리 부족 → 전체 시스템 다운

### 세 가지 상태 비유

| 상태 | 비유 | 설명 |
|------|------|------|
| **Closed** | 차단기 ON | 정상 운영, 전기(요청) 통과 |
| **Open** | 차단기 OFF | 차단됨, 전기(요청) 즉시 거부 |
| **Half-Open** | 테스트 모드 | 살짝 올려서 확인 중 |

### 동작 흐름 = 차단기 동작

```
1. 평소 (Closed): 전기 정상 공급
         ↓
2. 과부하 발생: 5번 중 3번 합선! (실패율 60%)
         ↓
3. 차단 (Open): "위험해! 일단 전기 끊어!"
         ↓
4. 시간 경과: "60초 지났으니 한번 확인해볼까?"
         ↓
5. 테스트 (Half-Open): 전기 조금만 보내봄
         ↓
   성공? → Closed (복구)
   실패? → Open (다시 차단)
```

### 왜 "빠른 실패"가 중요한가?

**서킷브레이커 없이**
```
손님 100명이 결제 시도
→ 결제 서버 다운
→ 100명 모두 30초씩 대기 (타임아웃)
→ 3000초의 시간 낭비 + 서버 자원 점유
```

**서킷브레이커 있으면**
```
손님 5명 실패 후 서킷 Open
→ 나머지 95명은 즉시 "나중에 다시 시도해주세요" 응답
→ 서버 자원 보호, 빠른 사용자 응답
```

### Fallback = 비상 전력

정전 시 비상 발전기가 작동하듯, 서킷이 열리면 대체 응답을 제공합니다.

```
정상: 실시간 재고 조회
비상: "현재 재고 확인이 어렵습니다. 잠시 후 다시 시도해주세요."
```

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
  - A: 서킷브레이커는 외부 서비스 장애 시 빠르게 실패를 반환하여 연쇄적인 장애 전파(Cascading Failure)를 방지하는 패턴입니다. MSA 환경에서 하나의 서비스 장애가 전체 시스템을 마비시키는 것을 막고, 시스템의 전체적인 안정성과 복원력을 높이기 위해 필요합니다. **왜 이렇게 답해야 하나요?** 결제 서비스가 다운되면 주문 서비스의 스레드가 타임아웃까지 대기하며 쌓이고, 스레드 풀이 고갈되어 다른 API도 응답할 수 없게 됩니다. 서킷브레이커로 즉시 실패 반환하면 스레드를 보호하고 시스템 전체가 다운되는 것을 막습니다.

- Q: 서킷브레이커의 세 가지 상태와 각 상태 전환 조건을 설명해주세요.
  - A: Closed(정상), Open(차단), Half-Open(테스트) 세 가지 상태가 있습니다. Closed에서 실패율이 임계값을 초과하면 Open으로 전환되어 모든 요청을 차단합니다. 일정 시간 후 Half-Open으로 전환되어 제한된 요청으로 복구 여부를 테스트하고, 성공하면 Closed로, 실패하면 다시 Open으로 돌아갑니다. **왜 이렇게 답해야 하나요?** 슬라이딩 윈도우로 최근 N건의 요청 성공/실패를 추적하여 실시간으로 상태를 판단합니다. Half-Open 상태로 자동 복구를 시도하여 수동 개입 없이 서비스가 정상화될 수 있습니다. 만약 즉시 복구 시도하면 아직 불안정한 서비스에 부하를 주어 더 악화시킬 수 있습니다.

- Q: 서킷브레이커와 함께 사용하면 좋은 패턴은 무엇인가요?
  - A: Retry(재시도), Timeout(시간 제한), Bulkhead(격벽), Fallback(대체 응답) 패턴과 함께 사용합니다. 단, Retry는 서킷브레이커 바깥에 위치해야 하며, Open 상태에서는 재시도하지 않도록 구성해야 합니다. Fallback은 서킷이 열렸을 때 대체 응답을 제공하여 사용자 경험을 유지합니다. **왜 이렇게 답해야 하나요?** Timeout으로 무한 대기를 방지하고, Retry로 일시적 장애를 극복하며, Circuit Breaker로 지속적 장애를 차단합니다. Bulkhead는 스레드 풀을 분리하여 한 서비스 장애가 다른 서비스까지 영향주는 것을 막습니다. 이들을 조합하면 견고한 분산 시스템을 구축할 수 있습니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [가용성](./availability.md) | 장애 격리로 가용성 향상 | 입문 |
| [메시지 큐](./message-queue.md) | 비동기 통신으로 장애 격리 | 중급 |
| [대규모 시스템 설계](./large-scale-system.md) | 신뢰성 확보 패턴의 일부 | 심화 |

## 참고 자료

- [Resilience4j 공식 문서](https://resilience4j.readme.io/docs)
- [Martin Fowler - Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)
- [Microsoft - Circuit Breaker Pattern](https://docs.microsoft.com/en-us/azure/architecture/patterns/circuit-breaker)
