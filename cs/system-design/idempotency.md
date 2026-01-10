# 멱등성 (Idempotency)

> `[3] 중급` · 선수 지식: [REST API](../network/rest-api.md), [트랜잭션](../db/transaction.md)

> 동일한 요청을 여러 번 수행해도 결과가 한 번 수행한 것과 같은 성질

`#멱등성` `#Idempotency` `#IdempotencyKey` `#중복방지` `#결제` `#Payment` `#주문` `#Order` `#API설계` `#REST` `#POST` `#PUT` `#네트워크재시도` `#Retry` `#AtLeastOnce` `#ExactlyOnce` `#중복요청` `#DuplicateRequest` `#토큰` `#RequestId` `#결제중복` `#PaymentDuplication` `#분산시스템` `#장애복구` `#Timeout` `#커머스` `#Ecommerce`

## 왜 알아야 하는가?

- **실무**: 결제 중복, 주문 중복은 실제 금전적 손실과 고객 불만으로 이어짐
- **면접**: "결제 API 호출 중 타임아웃이 나면?" 질문에 멱등성 없이 답하기 어려움
- **기반 지식**: 분산 시스템에서 "Exactly-Once" 처리를 위한 핵심 개념

## 핵심 개념

- **멱등성(Idempotency)**: f(f(x)) = f(x), 같은 연산을 여러 번 해도 결과 동일
- **Idempotency Key**: 요청을 식별하는 고유 키 (중복 판별용)
- **At-Least-Once → Exactly-Once**: 재시도를 허용하면서도 중복 처리 방지

## 쉽게 이해하기

**엘리베이터 버튼**에 비유하면 이해가 쉽습니다.

```
멱등한 연산: 엘리베이터 버튼
┌────────────────────────────────────────┐
│  3층 버튼을 1번 누르든 100번 누르든     │
│  엘리베이터는 3층에 딱 1번만 선다        │
│                                         │
│  👆 → 3층 도착                          │
│  👆👆👆👆👆 → 3층 도착 (동일한 결과)     │
└────────────────────────────────────────┘

멱등하지 않은 연산: 자판기 버튼
┌────────────────────────────────────────┐
│  음료 버튼을 누를 때마다 음료가 나옴     │
│                                         │
│  👆 → 음료 1개                          │
│  👆👆👆 → 음료 3개 (매번 다른 결과)      │
└────────────────────────────────────────┘
```

**결제 API는 반드시 엘리베이터처럼 동작해야 합니다!**

## 상세 설명

### HTTP 메서드별 멱등성

| 메서드 | 멱등성 | 안전성 | 설명 |
|--------|--------|--------|------|
| GET | O | O | 조회만, 상태 변경 없음 |
| HEAD | O | O | GET과 동일, 바디 없음 |
| OPTIONS | O | O | 지원 메서드 조회 |
| PUT | O | X | 전체 교체, 여러 번 해도 동일 |
| DELETE | O | X | 삭제, 이미 없어도 동일 |
| **POST** | **X** | X | 생성, 매번 새 리소스 |
| **PATCH** | **△** | X | 부분 수정, 구현에 따라 다름 |

**문제**: POST로 결제/주문 생성 시 멱등성이 기본 보장되지 않음

### 왜 문제가 되는가?

```
클라이언트                서버                결제사
    │                      │                    │
    │ POST /orders         │                    │
    │ ───────────────────→ │                    │
    │                      │ 결제 요청           │
    │                      │ ──────────────────→ │
    │                      │     결제 성공       │
    │                      │ ←────────────────── │
    │                      │                    │
    │  (네트워크 타임아웃)  │                    │
    │ ←─────── X ───────── │                    │
    │                      │                    │
    │ 응답 못 받음... 재시도? │                    │
    │                      │                    │
    │ POST /orders (재시도) │                    │
    │ ───────────────────→ │                    │
    │                      │ 결제 요청 (또!)     │
    │                      │ ──────────────────→ │
    │                      │     결제 성공 (또!) │
    │                      │ ←────────────────── │
    │        200 OK        │                    │
    │ ←─────────────────── │                    │
    │                      │                    │
결과: 고객 카드에서 2번 결제됨! 💸💸
```

### 해결책: Idempotency Key

```
클라이언트                      서버                    Redis
    │                            │                       │
    │ POST /orders               │                       │
    │ Idempotency-Key: abc-123   │                       │
    │ ─────────────────────────→ │                       │
    │                            │ SETNX abc-123         │
    │                            │ ─────────────────────→│
    │                            │ OK (최초 요청)         │
    │                            │ ←─────────────────────│
    │                            │                       │
    │    (결제 처리 후 응답)       │                       │
    │    (네트워크 타임아웃)       │                       │
    │ ←─────── X ─────────────── │                       │
    │                            │                       │
    │ POST /orders (재시도)       │                       │
    │ Idempotency-Key: abc-123   │                       │
    │ ─────────────────────────→ │                       │
    │                            │ SETNX abc-123         │
    │                            │ ─────────────────────→│
    │                            │ FAIL (이미 존재)       │
    │                            │ ←─────────────────────│
    │                            │                       │
    │   이전 결과 반환 (중복 방지) │                       │
    │ ←─────────────────────────  │                       │

결과: 결제 1번만 됨! ✅
```

### 구현 방법

#### 1. Idempotency Key 기반 (권장)

```java
@RestController
@RequiredArgsConstructor
public class OrderController {

    private static final String IDEMPOTENCY_PREFIX = "idempotency:";
    private static final long IDEMPOTENCY_TTL = 24L; // 24시간

    private final RedisTemplate<String, String> redisTemplate;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody OrderRequest request) {

        String redisKey = IDEMPOTENCY_PREFIX + idempotencyKey;

        // 1. 이전 요청 결과 확인
        String cachedResult = redisTemplate.opsForValue().get(redisKey);
        if (cachedResult != null) {
            // 이미 처리된 요청 → 캐시된 결과 반환
            OrderResponse cached = objectMapper.readValue(cachedResult, OrderResponse.class);
            return ResponseEntity.ok(cached);
        }

        // 2. 락 획득 시도 (동시 요청 방지)
        Boolean acquired = redisTemplate.opsForValue()
            .setIfAbsent(redisKey + ":lock", "processing", Duration.ofSeconds(30));

        if (!acquired) {
            // 동일 키로 처리 중인 요청 존재
            return ResponseEntity.status(409)
                .body(new OrderResponse("처리 중인 요청이 있습니다"));
        }

        try {
            // 3. 실제 주문 처리
            OrderResponse response = orderService.createOrder(request);

            // 4. 결과 캐싱 (TTL 24시간)
            redisTemplate.opsForValue().set(
                redisKey,
                objectMapper.writeValueAsString(response),
                Duration.ofHours(IDEMPOTENCY_TTL)
            );

            return ResponseEntity.ok(response);
        } finally {
            // 5. 락 해제
            redisTemplate.delete(redisKey + ":lock");
        }
    }
}
```

**왜 이렇게 하는가?**
- **Idempotency Key**: 클라이언트가 생성한 UUID로 요청 식별
- **캐싱**: 동일 요청 시 DB 조회 없이 즉시 응답
- **락**: 동시에 같은 키로 요청 시 한 건만 처리
- **TTL**: 영구 저장이 아닌 일정 기간만 유지 (저장소 관리)

#### 2. DB Unique 제약 기반

```java
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    // ...
}

@Service
@Transactional
public class OrderService {

    public OrderResponse createOrder(String idempotencyKey, OrderRequest request) {
        // 이미 존재하면 기존 주문 반환
        return orderRepository.findByIdempotencyKey(idempotencyKey)
            .map(this::toResponse)
            .orElseGet(() -> {
                Order order = Order.builder()
                    .idempotencyKey(idempotencyKey)
                    .amount(request.getAmount())
                    .build();
                return toResponse(orderRepository.save(order));
            });
    }
}
```

**장점**: 추가 인프라 불필요
**단점**: 외부 결제 API 호출 전 중복 체크 어려움

#### 3. 결제사 API 활용

대부분의 PG사는 자체 멱등성 키를 지원합니다.

```java
// 토스페이먼츠 예시
@Service
public class TossPaymentService {

    public PaymentResponse processPayment(String orderId, int amount) {
        return webClient.post()
            .uri("/v1/payments/confirm")
            .header("Idempotency-Key", orderId)  // 주문 ID를 멱등성 키로
            .bodyValue(Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
            ))
            .retrieve()
            .bodyToMono(PaymentResponse.class)
            .block();
    }
}
```

### Idempotency Key 생성 전략

| 방식 | 예시 | 장점 | 단점 |
|------|------|------|------|
| UUID v4 | `550e8400-e29b-41d4-a716-446655440000` | 충돌 없음 | 의미 없는 값 |
| 조합 | `user:123:cart:456:ts:1699999999` | 디버깅 용이 | 복잡 |
| 해시 | `SHA256(userId + cartId + amount)` | 동일 요청 = 동일 키 | 해시 충돌 가능성 |
| 주문번호 | `ORD-20241115-001234` | 추적 용이 | 생성 로직 필요 |

**권장**: 클라이언트에서 UUID v4 생성 후 헤더로 전달

```javascript
// 클라이언트 (JavaScript)
const idempotencyKey = crypto.randomUUID();

fetch('/api/orders', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'Idempotency-Key': idempotencyKey
    },
    body: JSON.stringify(orderData)
});
```

## 동작 원리

### 멱등성 처리 흐름

```
┌─────────────────────────────────────────────────────────────────┐
│                     멱등성 처리 흐름                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  요청 수신 (Idempotency-Key: abc-123)                            │
│       │                                                          │
│       ▼                                                          │
│  ┌─────────────────────────┐                                     │
│  │ Redis에서 키 조회        │                                     │
│  └───────────┬─────────────┘                                     │
│              │                                                   │
│    ┌─────────┴─────────┐                                        │
│    │                   │                                        │
│  존재함              존재 안 함                                   │
│    │                   │                                        │
│    ▼                   ▼                                        │
│ ┌──────────┐    ┌─────────────────┐                             │
│ │캐시 반환  │    │ SETNX 락 획득   │                             │
│ │(중복요청) │    └────────┬────────┘                             │
│ └──────────┘             │                                      │
│                   ┌──────┴──────┐                               │
│                 성공           실패                              │
│                   │             │                               │
│                   ▼             ▼                               │
│             ┌──────────┐  ┌───────────┐                         │
│             │ 비즈니스  │  │ 409 반환   │                         │
│             │ 로직 실행 │  │ (처리 중)  │                         │
│             └─────┬────┘  └───────────┘                         │
│                   │                                              │
│                   ▼                                              │
│             ┌──────────┐                                         │
│             │ 결과 캐싱 │                                         │
│             │ TTL 설정 │                                         │
│             └─────┬────┘                                         │
│                   │                                              │
│                   ▼                                              │
│             ┌──────────┐                                         │
│             │ 200 반환 │                                         │
│             └──────────┘                                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 항목 | Redis 기반 | DB Unique | 결제사 위임 |
|------|-----------|-----------|------------|
| 구현 복잡도 | 중간 | 낮음 | 낮음 |
| 성능 | 높음 | 중간 | 높음 |
| 외부 API 보호 | O | △ | O |
| 추가 인프라 | Redis 필요 | 없음 | 없음 |
| 적용 범위 | 전체 | 단일 테이블 | 결제만 |

## 트러블슈팅

### 사례 1: Idempotency Key 없이 재시도 시 중복 결제

#### 증상
```
고객 A: 결제 버튼 클릭 → 응답 안 옴 → 다시 클릭
결과: 카드에서 2번 결제됨
CS: "왜 돈이 2번 빠졌나요?" 문의 폭주
```

#### 원인 분석
- 클라이언트에서 Idempotency Key 미사용
- 버튼 중복 클릭 방지 없음
- 서버에서 중복 요청 감지 불가

#### 해결 방법

**프론트엔드 (1차 방어)**
```javascript
const PaymentButton = () => {
    const [isProcessing, setIsProcessing] = useState(false);
    const idempotencyKeyRef = useRef(crypto.randomUUID());

    const handlePayment = async () => {
        if (isProcessing) return;  // 중복 클릭 방지

        setIsProcessing(true);
        try {
            await fetch('/api/payments', {
                method: 'POST',
                headers: {
                    'Idempotency-Key': idempotencyKeyRef.current
                },
                body: JSON.stringify(paymentData)
            });
        } finally {
            // 성공/실패 상관없이 같은 키 유지 (재시도 시 동일 키)
        }
    };

    return <button onClick={handlePayment} disabled={isProcessing}>결제</button>;
};
```

**백엔드 (2차 방어)**
```java
// 위의 Redis 기반 구현 적용
```

#### 예방 조치
- 결제 API에는 반드시 Idempotency Key 필수화
- 클라이언트 SDK 제공 시 자동 생성 포함
- 모니터링: 동일 사용자 짧은 시간 내 중복 요청 알림

### 사례 2: TTL 만료 후 재시도로 중복 처리

#### 증상
```
Day 1: 주문 요청 (Key: abc-123) → 성공
Day 2: 네트워크 이슈로 같은 Key로 재시도 (TTL 만료됨)
결과: 주문 2건 생성
```

#### 해결 방법
```java
// TTL 충분히 길게 설정 (최소 24시간, 권장 7일)
private static final long IDEMPOTENCY_TTL = 7L * 24L; // 7일

// DB에도 idempotencyKey 저장하여 이중 체크
@Service
public class OrderService {
    public OrderResponse createOrder(String idempotencyKey, OrderRequest request) {
        // 1. Redis 체크 (빠름)
        String cached = redisTemplate.opsForValue().get(idempotencyKey);
        if (cached != null) {
            return objectMapper.readValue(cached, OrderResponse.class);
        }

        // 2. DB 체크 (TTL 만료 대비)
        Optional<Order> existing = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        // 3. 새 주문 생성
        Order order = createNewOrder(idempotencyKey, request);
        return toResponse(order);
    }
}
```

## 면접 예상 질문

### Q: POST는 왜 멱등하지 않은가?

A: POST는 "새 리소스 생성"의 의미를 가지므로 매 요청마다 새로운 리소스가 생성됩니다. 예를 들어 `POST /orders`를 3번 호출하면 주문이 3건 생성됩니다. 반면 PUT은 "리소스 교체"이므로 `PUT /orders/123`을 여러 번 호출해도 123번 주문이 동일한 상태로 유지됩니다.

### Q: Idempotency Key는 누가 생성해야 하는가?

A: **클라이언트**가 생성해야 합니다. 이유는:
1. 요청 전에 키가 존재해야 네트워크 실패 시 동일 키로 재시도 가능
2. 서버 응답을 받기 전 타임아웃 발생 시 서버가 키를 줬는지 알 수 없음
3. UUID v4를 사용하면 충돌 확률이 무시할 수준 (2^122 중 1)

### Q: At-Least-Once와 Exactly-Once의 차이는?

A: **At-Least-Once**는 메시지가 최소 1번 이상 전달됨을 보장하지만 중복 가능합니다. **Exactly-Once**는 정확히 1번만 전달됨을 보장합니다. 분산 시스템에서 진정한 Exactly-Once는 매우 어려워서, 보통 "At-Least-Once + 멱등성"으로 Exactly-Once 효과를 달성합니다. Kafka도 Exactly-Once semantics를 트랜잭션과 멱등성 프로듀서로 구현합니다.

### Q: 멱등성 키의 TTL은 얼마가 적절한가?

A: 비즈니스 요구사항에 따라 다르지만, 일반적으로:
- **결제**: 최소 24시간 (고객이 다음날 문의할 수 있음)
- **주문**: 7일 이상 (배송 완료까지 추적)
- **API 일반**: 1시간~24시간
- DB에 영구 저장하면서 Redis는 캐시 역할로만 사용하는 것이 가장 안전합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [REST API](../network/rest-api.md) | 선수 지식 | Beginner |
| [트랜잭션](../db/transaction.md) | 선수 지식 | Intermediate |
| [분산 락](./distributed-lock.md) | 관련 개념 | Intermediate |
| [외부 API 통합](./external-api-integration.md) | 실전 적용 | Intermediate |
| [결제 시스템 설계](./payment-system.md) | 실전 적용 | Advanced |

## 참고 자료

- [Stripe Idempotent Requests](https://stripe.com/docs/api/idempotent_requests)
- [토스페이먼츠 API 문서](https://docs.tosspayments.com/reference)
- [Designing Data-Intensive Applications - Martin Kleppmann](https://dataintensive.net/)
