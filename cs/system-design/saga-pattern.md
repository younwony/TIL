# SAGA 패턴 (Saga Pattern)

> `[4] 심화` · 선수 지식: [분산 트랜잭션](./distributed-transaction.md), [메시지 큐](./message-queue.md), [이벤트 드리븐 아키텍처](./event-driven-architecture.md)

> 마이크로서비스 환경에서 로컬 트랜잭션의 연쇄와 보상 트랜잭션으로 데이터 일관성을 유지하는 분산 트랜잭션 패턴

`#SAGA` `#사가패턴` `#SagaPattern` `#분산트랜잭션` `#보상트랜잭션` `#Compensation` `#Choreography` `#코레오그래피` `#Orchestration` `#오케스트레이션` `#EventualConsistency` `#최종일관성` `#마이크로서비스` `#MSA` `#이벤트소싱` `#Outbox` `#CDC` `#주문` `#Order` `#결제` `#Payment` `#재고` `#Inventory` `#롤백` `#Rollback` `#커머스` `#Ecommerce`

## 왜 알아야 하는가?

- **실무**: "주문 → 결제 → 재고 차감" 중 하나라도 실패 시 전체 롤백 필수
- **면접**: "MSA에서 분산 트랜잭션은 어떻게 처리하나요?" 핵심 질문
- **기반 지식**: 이벤트 드리븐 아키텍처, 최종 일관성 이해의 기초

## 핵심 개념

- **로컬 트랜잭션**: 각 서비스가 자신의 DB만 변경
- **보상 트랜잭션(Compensation)**: 이전 단계를 취소하는 역연산
- **Choreography**: 이벤트 기반 분산 조율 (중앙 조율자 없음)
- **Orchestration**: 중앙 Orchestrator가 순서 제어

## 쉽게 이해하기

**도미노**에 비유하면 이해가 쉽습니다.

```
SAGA = "넘어지는 도미노" + "다시 세우는 도미노"

정상 흐름 (순방향 도미노):
  [주문] → [결제] → [재고] → [배송] → 완료! ✅

실패 시 (역방향 도미노 = 보상 트랜잭션):
  [주문] → [결제] → [재고] ← 실패! ❌
                      ↓
           [결제 취소] ← [재고 복구]
                ↓
           [주문 취소]
           전체 원상복구! ✅
```

## 상세 설명

### SAGA vs 2PC 비교

| 항목 | 2PC | SAGA |
|------|-----|------|
| 락 방식 | 글로벌 락 (모든 참여자) | 락 없음 (로컬 트랜잭션만) |
| 일관성 | 강한 일관성 | 최종 일관성 |
| 성능 | 낮음 (락 대기) | 높음 |
| 장애 허용 | 코디네이터 SPOF | 개별 서비스 장애 허용 |
| 구현 복잡도 | 낮음 | 높음 (보상 로직 필요) |
| 적합한 환경 | 단일 DB, 짧은 트랜잭션 | MSA, 긴 비즈니스 프로세스 |

### Choreography vs Orchestration

```
┌─────────────────────────────────────────────────────────────────┐
│                       Choreography                               │
│              (이벤트 기반, 분산 조율)                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  주문서비스          결제서비스          재고서비스               │
│     │                   │                   │                   │
│     │ OrderCreated      │                   │                   │
│     │ ─────────────────→│                   │                   │
│     │                   │ PaymentCompleted  │                   │
│     │                   │ ─────────────────→│                   │
│     │                   │                   │ StockReserved     │
│     │ ←─────────────────────────────────────│                   │
│                                                                  │
│  장점: 느슨한 결합, 단일 장애점 없음                             │
│  단점: 흐름 추적 어려움, 순환 의존성 위험                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       Orchestration                              │
│              (중앙 제어, 명시적 흐름)                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                    주문 Orchestrator                             │
│                          │                                       │
│            ┌─────────────┼─────────────┐                        │
│            │             │             │                        │
│            ▼             ▼             ▼                        │
│       주문서비스     결제서비스     재고서비스                    │
│                                                                  │
│  1. 주문 생성 명령 → 2. 결제 명령 → 3. 재고 차감 명령            │
│                                                                  │
│  장점: 흐름 명확, 디버깅 용이, 복잡한 로직 처리                  │
│  단점: Orchestrator가 SPOF, 서비스 간 결합도 증가               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 실전 예제: 주문 SAGA

#### Orchestration 방식 구현

```java
// 1. Saga 상태 정의
public enum OrderSagaState {
    STARTED,
    ORDER_CREATED,
    PAYMENT_COMPLETED,
    STOCK_RESERVED,
    COMPLETED,
    // 보상 상태
    COMPENSATING,
    ORDER_CANCELLED,
    PAYMENT_REFUNDED,
    STOCK_RELEASED,
    COMPENSATION_COMPLETED,
    FAILED
}

// 2. Saga 엔티티
@Entity
public class OrderSaga {
    @Id
    private String sagaId;

    @Enumerated(EnumType.STRING)
    private OrderSagaState state;

    private Long orderId;
    private Long paymentId;
    private Long reservationId;

    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// 3. Orchestrator
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaOrchestrator {

    private final OrderSagaRepository sagaRepository;
    private final OrderService orderService;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderSaga startSaga(CreateOrderCommand command) {
        // Saga 시작
        OrderSaga saga = OrderSaga.builder()
            .sagaId(UUID.randomUUID().toString())
            .state(OrderSagaState.STARTED)
            .build();
        sagaRepository.save(saga);

        try {
            // Step 1: 주문 생성
            OrderCreatedEvent orderEvent = orderService.createOrder(command);
            saga.setOrderId(orderEvent.getOrderId());
            saga.setState(OrderSagaState.ORDER_CREATED);
            sagaRepository.save(saga);

            // Step 2: 결제 처리
            PaymentCompletedEvent paymentEvent = paymentClient.processPayment(
                new PaymentCommand(saga.getOrderId(), command.getAmount())
            );
            saga.setPaymentId(paymentEvent.getPaymentId());
            saga.setState(OrderSagaState.PAYMENT_COMPLETED);
            sagaRepository.save(saga);

            // Step 3: 재고 차감
            StockReservedEvent stockEvent = inventoryClient.reserveStock(
                new ReserveStockCommand(command.getProductId(), command.getQuantity())
            );
            saga.setReservationId(stockEvent.getReservationId());
            saga.setState(OrderSagaState.STOCK_RESERVED);
            sagaRepository.save(saga);

            // 완료
            saga.setState(OrderSagaState.COMPLETED);
            return sagaRepository.save(saga);

        } catch (Exception e) {
            log.error("Saga 실패, 보상 트랜잭션 시작: {}", e.getMessage());
            saga.setFailureReason(e.getMessage());
            compensate(saga);
            throw new SagaFailedException(saga.getSagaId(), e);
        }
    }

    @Transactional
    public void compensate(OrderSaga saga) {
        saga.setState(OrderSagaState.COMPENSATING);
        sagaRepository.save(saga);

        // 역순으로 보상 트랜잭션 실행
        try {
            // 재고 복구 (있으면)
            if (saga.getReservationId() != null) {
                inventoryClient.releaseStock(saga.getReservationId());
                saga.setState(OrderSagaState.STOCK_RELEASED);
                sagaRepository.save(saga);
            }

            // 결제 환불 (있으면)
            if (saga.getPaymentId() != null) {
                paymentClient.refundPayment(saga.getPaymentId());
                saga.setState(OrderSagaState.PAYMENT_REFUNDED);
                sagaRepository.save(saga);
            }

            // 주문 취소 (있으면)
            if (saga.getOrderId() != null) {
                orderService.cancelOrder(saga.getOrderId());
                saga.setState(OrderSagaState.ORDER_CANCELLED);
                sagaRepository.save(saga);
            }

            saga.setState(OrderSagaState.COMPENSATION_COMPLETED);
        } catch (Exception e) {
            log.error("보상 트랜잭션 실패! 수동 개입 필요: {}", e.getMessage());
            saga.setState(OrderSagaState.FAILED);
            // 알림 발송 (Slack, PagerDuty 등)
            eventPublisher.publishEvent(new SagaCompensationFailedEvent(saga));
        }
        sagaRepository.save(saga);
    }
}
```

#### Choreography 방식 구현

```java
// 1. 주문 서비스 - 이벤트 발행
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void createOrder(CreateOrderCommand command) {
        Order order = Order.builder()
            .userId(command.getUserId())
            .productId(command.getProductId())
            .quantity(command.getQuantity())
            .status(OrderStatus.PENDING)
            .build();
        orderRepository.save(order);

        // 이벤트 발행 → 결제 서비스가 구독
        kafkaTemplate.send("order-events", new OrderCreatedEvent(
            order.getId(),
            order.getUserId(),
            command.getAmount()
        ));
    }

    // 보상 이벤트 수신
    @KafkaListener(topics = "payment-events")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();
        order.cancel("결제 실패: " + event.getReason());
        orderRepository.save(order);
    }
}

// 2. 결제 서비스 - 이벤트 수신 및 발행
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-events")
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            Payment payment = processPayment(event);

            // 성공 이벤트 → 재고 서비스가 구독
            kafkaTemplate.send("payment-events", new PaymentCompletedEvent(
                payment.getId(),
                event.getOrderId()
            ));
        } catch (PaymentException e) {
            // 실패 이벤트 → 주문 서비스가 구독 (보상)
            kafkaTemplate.send("payment-events", new PaymentFailedEvent(
                event.getOrderId(),
                e.getMessage()
            ));
        }
    }
}

// 3. 재고 서비스 - 이벤트 수신
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "payment-events")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        try {
            reserveStock(event.getOrderId());

            // 성공 이벤트 → 배송 서비스가 구독
            kafkaTemplate.send("inventory-events", new StockReservedEvent(
                event.getOrderId()
            ));
        } catch (InsufficientStockException e) {
            // 실패 이벤트 → 결제, 주문 서비스가 구독 (보상)
            kafkaTemplate.send("inventory-events", new StockReservationFailedEvent(
                event.getOrderId(),
                e.getMessage()
            ));
        }
    }
}
```

### Outbox 패턴 (이벤트 발행 보장)

```java
// 문제: 트랜잭션 커밋 후 이벤트 발행 실패 시 불일치
@Transactional
public void createOrder(CreateOrderCommand command) {
    Order order = orderRepository.save(new Order(...));
    // 여기서 장애 발생하면? DB는 커밋, 이벤트는 미발행!
    kafkaTemplate.send("order-events", new OrderCreatedEvent(order));
}

// 해결: Outbox 테이블 활용
@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    private String id;
    private String aggregateType;  // "Order"
    private String aggregateId;     // orderId
    private String eventType;       // "OrderCreated"
    private String payload;         // JSON
    private LocalDateTime createdAt;
    private boolean published;
}

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;

    @Transactional  // 같은 트랜잭션으로 원자성 보장!
    public void createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order(...));

        // DB에 이벤트 저장 (같은 트랜잭션)
        OutboxEvent event = OutboxEvent.builder()
            .id(UUID.randomUUID().toString())
            .aggregateType("Order")
            .aggregateId(order.getId().toString())
            .eventType("OrderCreated")
            .payload(toJson(new OrderCreatedEvent(order)))
            .build();
        outboxRepository.save(event);
    }
}

// CDC(Change Data Capture)로 Outbox 테이블 변경 감지 → Kafka 발행
// Debezium 등 사용
```

## 동작 원리

### SAGA 실행 흐름

```
┌─────────────────────────────────────────────────────────────────┐
│                     SAGA 실행 흐름                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [정상 흐름]                                                     │
│                                                                  │
│   T1 (주문생성)                                                  │
│        │                                                        │
│        ▼ 성공                                                   │
│   T2 (결제처리)                                                  │
│        │                                                        │
│        ▼ 성공                                                   │
│   T3 (재고차감)                                                  │
│        │                                                        │
│        ▼ 성공                                                   │
│   T4 (배송요청) ───────→ SAGA 완료 ✅                            │
│                                                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [실패 + 보상 흐름]                                              │
│                                                                  │
│   T1 (주문생성) → 성공                                           │
│        │                                                        │
│        ▼                                                        │
│   T2 (결제처리) → 성공                                           │
│        │                                                        │
│        ▼                                                        │
│   T3 (재고차감) → 실패! ❌                                       │
│        │                                                        │
│        ▼ 보상 시작                                              │
│   C2 (결제환불) ← 역방향                                        │
│        │                                                        │
│        ▼                                                        │
│   C1 (주문취소) ← 역방향                                        │
│        │                                                        │
│        ▼                                                        │
│   SAGA 보상 완료 (원상복구) ✅                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 방식 | 장점 | 단점 |
|------|------|------|
| **Choreography** | 느슨한 결합, SPOF 없음 | 흐름 추적 어려움, 순환 의존 위험 |
| **Orchestration** | 흐름 명확, 복잡한 로직 처리 | Orchestrator SPOF, 결합도 증가 |

| 고려사항 | 권장 |
|----------|------|
| 서비스 수 적음 (2~3개) | Choreography |
| 서비스 수 많음 (5개+) | Orchestration |
| 복잡한 분기/조건 | Orchestration |
| 단순 직렬 흐름 | Choreography |

## 트러블슈팅

### 사례 1: 보상 트랜잭션 실패

#### 증상
```
T1 (주문) → T2 (결제) → T3 (재고) 실패!
C2 (결제 환불) → 외부 PG사 장애로 실패!
결과: 고객 돈만 빠지고 주문도 없음
```

#### 해결 방법
```java
@Service
public class CompensationService {

    private static final int MAX_RETRY = 5;

    @Retryable(
        value = CompensationException.class,
        maxAttempts = MAX_RETRY,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void compensatePayment(Long paymentId) {
        paymentClient.refund(paymentId);
    }

    @Recover
    public void compensationFailed(CompensationException e, Long paymentId) {
        // 1. 수동 처리 큐에 등록
        manualProcessingQueue.add(new ManualCompensation(paymentId, e));

        // 2. 알림 발송
        alertService.sendAlert(
            "보상 트랜잭션 실패! paymentId=" + paymentId,
            AlertLevel.CRITICAL
        );

        // 3. 상태 저장 (나중에 배치로 재시도)
        compensationLogRepository.save(new CompensationLog(
            paymentId,
            CompensationStatus.PENDING_MANUAL,
            e.getMessage()
        ));
    }
}
```

### 사례 2: 이벤트 순서 역전

#### 증상
```
Kafka 파티션 분산으로 인해:
- StockReservedEvent가 먼저 도착
- PaymentCompletedEvent가 나중에 도착
→ 상태 머신 오류
```

#### 해결 방법
```java
// 이벤트 버퍼링 + 순서 검증
@Service
public class OrderEventHandler {

    private final Map<String, List<Event>> eventBuffer = new ConcurrentHashMap<>();

    @KafkaListener(topics = "saga-events")
    public void handleEvent(SagaEvent event) {
        String orderId = event.getOrderId();

        // 버퍼에 추가
        eventBuffer.computeIfAbsent(orderId, k -> new ArrayList<>())
                   .add(event);

        // 순서 검증 후 처리
        processInOrder(orderId);
    }

    private void processInOrder(String orderId) {
        List<Event> events = eventBuffer.get(orderId);
        events.sort(Comparator.comparing(Event::getSequenceNumber));

        for (Event event : events) {
            if (canProcess(orderId, event)) {
                process(event);
            }
        }
    }
}
```

## 면접 예상 질문

### Q: SAGA 패턴에서 격리성(Isolation)은 어떻게 보장하나?

A: SAGA는 **격리성을 완전히 보장하지 않습니다**. 이것이 2PC와의 큰 차이점입니다. 중간 상태가 다른 트랜잭션에 노출될 수 있어 "Dirty Read"가 가능합니다. 이를 완화하기 위해 **Semantic Lock**(논리적 락), **Commutative Updates**(교환 가능한 업데이트), **버전 관리** 등의 대응책을 사용합니다.

### Q: Choreography와 Orchestration 중 언제 무엇을 쓰나?

A: **Choreography**는 서비스 수가 적고(2~3개), 흐름이 단순할 때 적합합니다. 느슨한 결합의 장점이 있습니다. **Orchestration**은 서비스 수가 많거나(5개+), 복잡한 분기/조건이 있을 때 적합합니다. 흐름이 명확해 디버깅이 쉽습니다. 실무에서는 복잡한 주문 프로세스에 Orchestration을 많이 사용합니다.

### Q: 보상 트랜잭션이 실패하면?

A: **재시도**(Exponential Backoff), **Dead Letter Queue**, **수동 처리 큐** 순으로 대응합니다. 최종적으로 실패하면 운영자 알림과 함께 수동 개입이 필요합니다. 이를 위해 모든 SAGA의 상태를 DB에 저장하고, 배치로 미완료 SAGA를 주기적으로 재시도하는 메커니즘을 구축합니다.

### Q: SAGA에서 이벤트 순서가 뒤바뀌면?

A: **Sequence Number**를 이벤트에 포함시키고, 수신 측에서 순서를 검증합니다. Kafka의 경우 **같은 파티션**에 같은 SAGA의 이벤트를 보내면 순서가 보장됩니다(Partition Key = sagaId). 또는 이벤트 버퍼링 후 순서대로 처리하는 방식도 사용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [분산 트랜잭션](./distributed-transaction.md) | 선수 지식 | Advanced |
| [메시지 큐](./message-queue.md) | 선수 지식 | Intermediate |
| [이벤트 드리븐 아키텍처](./event-driven-architecture.md) | 선수 지식 | Intermediate |
| [CQRS & 이벤트 소싱](./cqrs-event-sourcing.md) | 관련 개념 | Advanced |
| [결제 시스템 설계](./payment-system.md) | 실전 적용 | Advanced |

## 참고 자료

- [Microservices Patterns - Chris Richardson](https://microservices.io/patterns/data/saga.html)
- [Saga Pattern - Microsoft Docs](https://docs.microsoft.com/en-us/azure/architecture/reference-architectures/saga/saga)
- [Eventuate Tram Sagas](https://eventuate.io/docs/manual/eventuate-tram/latest/getting-started-eventuate-tram-sagas.html)
