# 주문 처리 시스템 설계 (Order Processing System)

> `[4] 심화` · 선수 지식: [결제 시스템](./payment-system.md), [재고 관리](./inventory-system.md), [SAGA 패턴](./saga-pattern.md)

> 주문 생성부터 완료까지의 전체 라이프사이클을 관리하는 시스템: 상태 머신, 결제/재고 연동, 분산 트랜잭션

`#주문처리` `#OrderProcessing` `#주문시스템` `#OrderSystem` `#상태머신` `#StateMachine` `#주문상태` `#OrderStatus` `#커머스` `#Ecommerce` `#결제연동` `#재고연동` `#분산트랜잭션` `#DistributedTransaction` `#SAGA` `#보상트랜잭션` `#Compensation` `#주문취소` `#환불` `#Refund` `#주문번호` `#OrderNumber` `#이벤트소싱` `#EventSourcing` `#CQRS` `#도메인이벤트` `#DomainEvent`

## 왜 알아야 하는가?

- **실무**: 주문은 커머스의 핵심. 결제·재고·배송 모든 시스템과 연동됨
- **면접**: "주문 중 결제는 성공했는데 재고 차감이 실패하면?" 핵심 질문
- **기반 지식**: 상태 머신, 분산 트랜잭션, 이벤트 기반 아키텍처의 종합 적용

## 핵심 개념

- **주문 상태 머신**: CREATED → PAID → SHIPPED → COMPLETED / CANCELLED
- **주문 번호**: 고유하고 예측 불가능한 식별자 생성 전략
- **보상 트랜잭션**: 실패 시 이전 작업을 되돌리는 메커니즘
- **멱등성**: 동일 요청에 동일 결과 보장

## 쉽게 이해하기

**식당 주문**에 비유하면 이해가 쉽습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                     주문 처리 비유                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  손님 (고객)                                                  │
│    │ "이 메뉴 주문할게요"                                     │
│    ▼                                                        │
│  주문서 작성 (CREATED)                                        │
│    │                                                        │
│    ▼                                                        │
│  결제 확인 (PAID)                                             │
│    │ "카드 결제 완료"                                         │
│    ▼                                                        │
│  주방에 전달 (PROCESSING)                                     │
│    │ "재료 확인 → 조리 시작"                                  │
│    │                                                        │
│    ├─ 재료 없음? → 환불 + 취소 (보상 트랜잭션)                │
│    │                                                        │
│    ▼                                                        │
│  배달 출발 (SHIPPED)                                          │
│    │                                                        │
│    ▼                                                        │
│  배달 완료 (COMPLETED)                                        │
│                                                              │
│  핵심: 각 단계가 실패하면 이전 단계를 되돌려야 함              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 주문 상태 머신 (State Machine)

```
┌─────────────────────────────────────────────────────────────────┐
│                       주문 상태 전이                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                      ┌────────────┐                             │
│                      │  CREATED   │ (주문 생성)                  │
│                      └─────┬──────┘                             │
│                            │                                    │
│           ┌────────────────┼────────────────┐                  │
│           │                │                │                  │
│      결제 실패         결제 성공        사용자 취소              │
│           │                │                │                  │
│           ▼                ▼                ▼                  │
│    ┌────────────┐   ┌────────────┐   ┌────────────┐           │
│    │   FAILED   │   │    PAID    │   │ CANCELLED  │           │
│    │ (결제실패) │   │  (결제완료)│   │ (사용자취소)│           │
│    └────────────┘   └─────┬──────┘   └────────────┘           │
│                           │                                    │
│                      재고 차감                                  │
│                           │                                    │
│              ┌────────────┼────────────┐                      │
│              │            │            │                      │
│         재고 부족     재고 차감      결제 후 취소               │
│              │         성공           │                       │
│              ▼            │            ▼                       │
│       ┌────────────┐      │     ┌────────────┐               │
│       │  REFUNDED  │      │     │  REFUNDED  │               │
│       │ (환불처리) │      │     │ + 재고복구 │               │
│       └────────────┘      │     └────────────┘               │
│                           ▼                                    │
│                    ┌────────────┐                             │
│                    │ PROCESSING │ (상품준비)                   │
│                    └─────┬──────┘                             │
│                          │                                    │
│                     배송 시작                                  │
│                          │                                    │
│                          ▼                                    │
│                    ┌────────────┐                             │
│                    │  SHIPPED   │ (배송중)                     │
│                    └─────┬──────┘                             │
│                          │                                    │
│              ┌───────────┼───────────┐                       │
│              │           │           │                       │
│          배송 완료   배송 실패    반품 요청                    │
│              │           │           │                       │
│              ▼           ▼           ▼                       │
│       ┌────────────┐ ┌────────────┐ ┌────────────┐          │
│       │ COMPLETED  │ │SHIP_FAILED │ │  RETURNED  │          │
│       │ (주문완료) │ │ (배송실패) │ │  (반품)    │          │
│       └────────────┘ └────────────┘ └────────────┘          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 주문 엔티티 설계

```java
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;  // 주문 번호 (외부 노출용)

    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Embedded
    private ShippingAddress shippingAddress;

    @Column(nullable = false)
    private Long totalAmount;

    private Long discountAmount;

    @Column(nullable = false)
    private Long paymentAmount;  // 실제 결제 금액

    private String paymentKey;   // PG사 결제 키

    private LocalDateTime orderedAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;

    @Version
    private Long version;

    // 주문 생성 팩토리 메서드
    public static Order create(Long customerId, List<OrderItem> items,
                               ShippingAddress address, Long discountAmount) {
        Order order = new Order();
        order.orderNumber = OrderNumberGenerator.generate();
        order.customerId = customerId;
        order.status = OrderStatus.CREATED;
        order.shippingAddress = address;
        order.orderedAt = LocalDateTime.now();

        items.forEach(order::addItem);
        order.totalAmount = order.calculateTotalAmount();
        order.discountAmount = discountAmount;
        order.paymentAmount = order.totalAmount - discountAmount;

        return order;
    }

    // 상태 전이 메서드
    public void pay(String paymentKey) {
        validateStatus(OrderStatus.CREATED);
        this.paymentKey = paymentKey;
        this.status = OrderStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void startProcessing() {
        validateStatus(OrderStatus.PAID);
        this.status = OrderStatus.PROCESSING;
    }

    public void ship(String trackingNumber) {
        validateStatus(OrderStatus.PROCESSING);
        this.status = OrderStatus.SHIPPED;
        this.shippedAt = LocalDateTime.now();
        // 배송 정보 저장
    }

    public void complete() {
        validateStatus(OrderStatus.SHIPPED);
        this.status = OrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        if (this.status == OrderStatus.SHIPPED ||
            this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("배송 후에는 취소 불가");
        }
        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    private void validateStatus(OrderStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                "잘못된 상태 전이: 현재=" + this.status + ", 필요=" + expected
            );
        }
    }

    private Long calculateTotalAmount() {
        return orderItems.stream()
            .mapToLong(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}

public enum OrderStatus {
    CREATED,       // 주문 생성
    PAID,          // 결제 완료
    PROCESSING,    // 상품 준비 중
    SHIPPED,       // 배송 중
    COMPLETED,     // 주문 완료
    CANCELLED,     // 취소됨
    REFUNDED,      // 환불됨
    SHIP_FAILED,   // 배송 실패
    RETURNED       // 반품
}
```

### 주문 번호 생성 전략

```java
@Component
public class OrderNumberGenerator {

    private static final String PREFIX = "ORD";
    private static final int RANDOM_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 형식: ORD-{yyyyMMdd}-{8자리 랜덤}
     * 예: ORD-20250113-A3F8K2M9
     */
    public static String generate() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String random = generateRandomString(RANDOM_LENGTH);
        return String.format("%s-%s-%s", PREFIX, date, random);
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 대안 1: UUID 기반 (충돌 거의 없음, 길이 김)
     */
    public static String generateUUID() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 18);
    }

    /**
     * 대안 2: Snowflake ID (분산 환경, 시간순 정렬)
     */
    public static String generateSnowflake(SnowflakeIdGenerator generator) {
        return "ORD-" + generator.nextId();
    }

    /**
     * 대안 3: Redis INCR 기반 (순차적, 중앙화)
     */
    public static String generateSequential(RedisTemplate<String, String> redis) {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Long sequence = redis.opsForValue().increment("order:seq:" + date);
        return String.format("ORD-%s-%08d", date, sequence);
    }
}
```

### 주문 생성 서비스 (SAGA 패턴)

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final EventPublisher eventPublisher;

    /**
     * 주문 생성 (SAGA 패턴 - Choreography)
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 1. 주문 생성
        Order order = Order.create(
            request.getCustomerId(),
            toOrderItems(request.getItems()),
            request.getShippingAddress(),
            request.getDiscountAmount()
        );
        orderRepository.save(order);

        // 2. 이벤트 발행 (비동기 처리 시작)
        eventPublisher.publish(new OrderCreatedEvent(
            order.getOrderNumber(),
            order.getOrderItems(),
            order.getPaymentAmount()
        ));

        return OrderResponse.from(order);
    }

    /**
     * 주문 확정 (동기 처리 - Orchestration)
     */
    @Transactional
    public OrderResponse confirmOrder(String orderNumber, PaymentInfo paymentInfo) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        SagaContext context = new SagaContext();

        try {
            // Step 1: 재고 차감
            inventoryService.reserveStock(order.getOrderItems());
            context.addCompensation(() ->
                inventoryService.releaseStock(order.getOrderItems()));

            // Step 2: 쿠폰 사용 처리
            if (order.getCouponId() != null) {
                couponService.useCoupon(order.getCouponId(), order.getCustomerId());
                context.addCompensation(() ->
                    couponService.restoreCoupon(order.getCouponId()));
            }

            // Step 3: 결제 처리
            PaymentResult result = paymentService.processPayment(
                order.getOrderNumber(),
                order.getPaymentAmount(),
                paymentInfo
            );
            context.addCompensation(() ->
                paymentService.cancelPayment(result.getPaymentKey()));

            // Step 4: 주문 상태 변경
            order.pay(result.getPaymentKey());
            order.startProcessing();

            log.info("주문 확정 완료: {}", orderNumber);
            return OrderResponse.from(order);

        } catch (Exception e) {
            log.error("주문 확정 실패, 보상 트랜잭션 실행: {}", orderNumber, e);
            context.compensate();  // 역순으로 보상 실행
            order.cancel("주문 처리 실패: " + e.getMessage());
            throw new OrderConfirmFailedException(orderNumber, e);
        }
    }
}

/**
 * SAGA 컨텍스트 - 보상 트랜잭션 관리
 */
public class SagaContext {

    private final Deque<Runnable> compensations = new ArrayDeque<>();

    public void addCompensation(Runnable compensation) {
        compensations.push(compensation);  // 스택에 추가
    }

    public void compensate() {
        while (!compensations.isEmpty()) {
            try {
                compensations.pop().run();  // 역순 실행
            } catch (Exception e) {
                log.error("보상 트랜잭션 실패", e);
                // 실패해도 계속 진행 (최선의 노력)
            }
        }
    }
}
```

### 주문 취소 처리

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCancelService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final CouponService couponService;
    private final RefundRepository refundRepository;

    /**
     * 주문 취소 (상태에 따라 다른 처리)
     */
    @Transactional
    public CancelResponse cancelOrder(String orderNumber, CancelRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        return switch (order.getStatus()) {
            case CREATED -> cancelBeforePayment(order);
            case PAID, PROCESSING -> cancelAfterPayment(order, request);
            case SHIPPED -> throw new CannotCancelException("배송 중에는 취소 불가");
            case COMPLETED -> requestReturn(order, request);
            default -> throw new CannotCancelException("취소 불가 상태: " + order.getStatus());
        };
    }

    /**
     * 결제 전 취소 (단순 상태 변경)
     */
    private CancelResponse cancelBeforePayment(Order order) {
        order.cancel("고객 요청 - 결제 전 취소");
        return CancelResponse.cancelled(order);
    }

    /**
     * 결제 후 취소 (환불 + 재고 복구 + 쿠폰 복구)
     */
    private CancelResponse cancelAfterPayment(Order order, CancelRequest request) {
        // 1. 결제 취소/환불
        RefundResult refundResult = paymentService.refund(
            order.getPaymentKey(),
            order.getPaymentAmount(),
            request.getCancelReason()
        );

        // 2. 재고 복구
        inventoryService.releaseStock(order.getOrderItems());

        // 3. 쿠폰 복구
        if (order.getCouponId() != null) {
            couponService.restoreCoupon(order.getCouponId());
        }

        // 4. 환불 내역 저장
        Refund refund = Refund.create(
            order.getOrderNumber(),
            order.getPaymentAmount(),
            request.getCancelReason(),
            refundResult
        );
        refundRepository.save(refund);

        // 5. 주문 상태 변경
        order.cancel(request.getCancelReason());

        return CancelResponse.refunded(order, refund);
    }

    /**
     * 배송 완료 후 반품 요청
     */
    private CancelResponse requestReturn(Order order, CancelRequest request) {
        // 반품 신청만 생성 (실제 환불은 반품 확인 후)
        ReturnRequest returnRequest = ReturnRequest.create(
            order.getOrderNumber(),
            request.getCancelReason(),
            request.getReturnItems()
        );
        returnRequestRepository.save(returnRequest);

        return CancelResponse.returnRequested(order, returnRequest);
    }
}
```

### 이벤트 기반 주문 처리 (비동기)

```java
/**
 * 주문 이벤트 핸들러 (Choreography 방식)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    @EventListener
    @Async
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신: {}", event.getOrderNumber());

        Order order = orderRepository.findByOrderNumber(event.getOrderNumber())
            .orElseThrow();

        order.pay(event.getPaymentKey());

        // 재고 차감 이벤트 발행
        eventPublisher.publish(new ReserveStockEvent(
            event.getOrderNumber(),
            order.getOrderItems()
        ));
    }

    @EventListener
    @Async
    @Transactional
    public void handleStockReserved(StockReservedEvent event) {
        log.info("재고 차감 완료 이벤트 수신: {}", event.getOrderNumber());

        Order order = orderRepository.findByOrderNumber(event.getOrderNumber())
            .orElseThrow();

        order.startProcessing();

        // 주문 완료 알림
        eventPublisher.publish(new OrderConfirmedEvent(
            event.getOrderNumber(),
            order.getCustomerId()
        ));
    }

    /**
     * 재고 차감 실패 시 보상 처리
     */
    @EventListener
    @Async
    @Transactional
    public void handleStockReservationFailed(StockReservationFailedEvent event) {
        log.warn("재고 차감 실패: {}", event.getOrderNumber());

        Order order = orderRepository.findByOrderNumber(event.getOrderNumber())
            .orElseThrow();

        // 결제 취소 이벤트 발행
        eventPublisher.publish(new CancelPaymentEvent(
            event.getOrderNumber(),
            order.getPaymentKey(),
            "재고 부족"
        ));

        order.cancel("재고 부족으로 주문 취소");
    }
}
```

### 주문 조회 (CQRS 적용)

```java
/**
 * 주문 조회 전용 서비스 (Read Model)
 */
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderReadRepository orderReadRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ORDER_CACHE_KEY = "order:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    /**
     * 주문 상세 조회 (캐시 적용)
     */
    public OrderDetailResponse getOrderDetail(String orderNumber) {
        // 1. 캐시 확인
        String cacheKey = ORDER_CACHE_KEY + orderNumber;
        OrderDetailResponse cached = (OrderDetailResponse) redisTemplate
            .opsForValue().get(cacheKey);

        if (cached != null) {
            return cached;
        }

        // 2. DB 조회
        OrderReadModel order = orderReadRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        OrderDetailResponse response = OrderDetailResponse.from(order);

        // 3. 캐시 저장
        redisTemplate.opsForValue().set(cacheKey, response, CACHE_TTL);

        return response;
    }

    /**
     * 고객별 주문 목록 조회
     */
    public Page<OrderSummaryResponse> getCustomerOrders(
            Long customerId, Pageable pageable) {

        return orderReadRepository.findByCustomerId(customerId, pageable)
            .map(OrderSummaryResponse::from);
    }

    /**
     * 주문 상태별 조회 (관리자용)
     */
    public Page<OrderAdminResponse> getOrdersByStatus(
            OrderStatus status, LocalDate from, LocalDate to, Pageable pageable) {

        return orderReadRepository.findByStatusAndDateRange(
            status, from.atStartOfDay(), to.plusDays(1).atStartOfDay(), pageable
        ).map(OrderAdminResponse::from);
    }
}
```

## 동작 원리

### 주문 처리 전체 흐름

```
┌─────────────────────────────────────────────────────────────────┐
│                     주문 처리 전체 흐름                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  고객         주문서비스      결제서비스      재고서비스   알림    │
│   │              │              │              │          │     │
│   │ 1.주문생성   │              │              │          │     │
│   │────────────→│              │              │          │     │
│   │              │              │              │          │     │
│   │ 2.주문정보   │              │              │          │     │
│   │←────────────│              │              │          │     │
│   │              │              │              │          │     │
│   │ 3.결제요청   │              │              │          │     │
│   │────────────→│              │              │          │     │
│   │              │ 4.결제처리   │              │          │     │
│   │              │─────────────→│              │          │     │
│   │              │              │              │          │     │
│   │              │ 5.결제성공   │              │          │     │
│   │              │←─────────────│              │          │     │
│   │              │              │              │          │     │
│   │              │ 6.재고차감               │          │     │
│   │              │──────────────────────────→│          │     │
│   │              │              │              │          │     │
│   │              │ 7.차감성공               │          │     │
│   │              │←──────────────────────────│          │     │
│   │              │              │              │          │     │
│   │              │ 8.알림발송                           │     │
│   │              │─────────────────────────────────────→│     │
│   │              │              │              │          │     │
│   │ 9.주문완료   │              │              │          │     │
│   │←────────────│              │              │          │     │
│   │              │              │              │          │     │
│   │  [실패 시]   │              │              │          │     │
│   │              │ 재고실패     │              │          │     │
│   │              │←──────────────────────────│          │     │
│   │              │              │              │          │     │
│   │              │ 결제취소     │              │          │     │
│   │              │─────────────→│              │          │     │
│   │              │              │              │          │     │
│   │ 주문취소알림 │              │              │          │     │
│   │←────────────│              │              │          │     │
│   │              │              │              │          │     │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 방식 | 장점 | 단점 | 적합한 상황 |
|------|------|------|-------------|
| 동기 처리 | 즉시 결과, 구현 단순 | 응답 지연, 장애 전파 | 소규모, 단순 시스템 |
| SAGA Orchestration | 명확한 흐름, 디버깅 용이 | 중앙 집중 부하 | 복잡한 비즈니스 로직 |
| SAGA Choreography | 느슨한 결합, 확장성 | 흐름 추적 어려움 | MSA, 대규모 시스템 |

## 트러블슈팅

### 사례 1: 결제 성공 후 재고 차감 실패

#### 증상
```
고객: 결제됐는데 주문이 취소됐어요
로그: PaymentCompleted → StockReservationFailed
```

#### 원인 분석
- 결제와 재고 차감이 별도 트랜잭션
- 재고 부족 시 결제 환불 필요

#### 해결 방법
```java
// 1. 결제 전 재고 선예약 (권장)
@Transactional
public OrderResponse createOrder(CreateOrderRequest request) {
    // 재고 먼저 예약 (TTL 설정)
    inventoryService.reserveWithTTL(request.getItems(), Duration.ofMinutes(15));

    // 그 다음 주문 생성
    Order order = Order.create(...);
    return OrderResponse.from(order);
}

// 2. 결제 완료 후 예약 확정
@Transactional
public void confirmPayment(String orderNumber) {
    Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow();

    // 예약된 재고를 확정으로 변경
    inventoryService.confirmReservation(order.getOrderItems());

    order.pay(paymentKey);
}
```

### 사례 2: 중복 주문 생성

#### 증상
```
같은 장바구니로 주문이 2개 생성됨
고객이 빠르게 주문 버튼 2번 클릭
```

#### 해결 방법
```java
// 1. 멱등성 키 사용
@Transactional
public OrderResponse createOrder(CreateOrderRequest request) {
    // 멱등성 키로 중복 체크
    String idempotencyKey = request.getIdempotencyKey();

    Optional<Order> existing = orderRepository
        .findByIdempotencyKey(idempotencyKey);

    if (existing.isPresent()) {
        return OrderResponse.from(existing.get());  // 기존 주문 반환
    }

    Order order = Order.create(...);
    order.setIdempotencyKey(idempotencyKey);
    orderRepository.save(order);

    return OrderResponse.from(order);
}

// 2. Redis 분산 락
@Transactional
public OrderResponse createOrderWithLock(CreateOrderRequest request) {
    String lockKey = "order:create:" + request.getCustomerId();

    boolean acquired = redisLock.tryLock(lockKey, Duration.ofSeconds(5));
    if (!acquired) {
        throw new ConcurrentOrderException("주문 처리 중입니다");
    }

    try {
        return createOrder(request);
    } finally {
        redisLock.unlock(lockKey);
    }
}
```

## 면접 예상 질문

### Q: 주문 중 결제는 성공했는데 재고 차감이 실패하면?

A: SAGA 패턴의 **보상 트랜잭션**을 실행합니다.
1. 재고 차감 실패 감지
2. 결제 서비스에 환불 요청
3. 주문 상태를 CANCELLED로 변경
4. 고객에게 취소 알림 발송

실패를 줄이려면 **결제 전 재고 선예약** 패턴을 사용합니다. 장바구니 담기 시 재고를 TTL로 예약하고, 결제 완료 시 확정합니다.

### Q: 주문 번호는 어떻게 생성하나?

A: 여러 전략이 있습니다:
1. **날짜 + 랜덤**: `ORD-20250113-A3F8K2M9` (가독성 좋음)
2. **Snowflake ID**: 분산 환경, 시간순 정렬 가능
3. **UUID**: 충돌 없음, 길이가 긺
4. **Redis INCR**: 순차적, 중앙화 필요

보안상 **예측 불가능한** 번호를 사용해야 합니다 (순차 번호는 다른 주문 추측 가능).

### Q: 주문 상태는 어떻게 관리하나?

A: **상태 머신(State Machine)** 패턴을 사용합니다.
- 허용된 상태 전이만 가능하도록 제한
- 각 상태별 취소 가능 여부, 환불 정책 등 비즈니스 로직 적용
- 이벤트 소싱과 결합하면 모든 상태 변경 이력 추적 가능

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [결제 시스템](./payment-system.md) | 선수 지식 | Advanced |
| [재고 관리](./inventory-system.md) | 선수 지식 | Advanced |
| [SAGA 패턴](./saga-pattern.md) | 선수 지식 | Advanced |
| [멱등성](./idempotency.md) | 관련 개념 | Intermediate |
| [이벤트 기반 아키텍처](./event-driven-architecture.md) | 관련 개념 | Intermediate |
| [장바구니 설계](./shopping-cart-system.md) | 연계 시스템 | Intermediate |

## 참고 자료

- [Designing Data-Intensive Applications - Martin Kleppmann](https://dataintensive.net/)
- [SAGA Pattern - microservices.io](https://microservices.io/patterns/data/saga.html)
- [Order Management Best Practices - AWS](https://aws.amazon.com/solutions/implementations/order-management/)
