# DDD (Domain-Driven Design)

> `[4] 심화` · 선수 지식: [OOP](../programming/oop.md), [MSA vs 모놀리식](./msa-vs-monolithic.md), [Hexagonal Architecture](./hexagonal-architecture.md)

> 복잡한 비즈니스 도메인을 소프트웨어로 효과적으로 모델링하기 위한 설계 접근 방식으로, 도메인 전문가와 개발자 간의 공통 언어(Ubiquitous Language)를 기반으로 함

`#DDD` `#도메인주도설계` `#DomainDrivenDesign` `#도메인` `#Domain` `#BoundedContext` `#바운디드컨텍스트` `#Aggregate` `#애그리거트` `#Entity` `#엔티티` `#ValueObject` `#값객체` `#Repository` `#리포지토리` `#DomainService` `#도메인서비스` `#ApplicationService` `#UbiquitousLanguage` `#유비쿼터스언어` `#ContextMap` `#컨텍스트맵` `#AntiCorruptionLayer` `#ACL` `#EventStorming` `#이벤트스토밍` `#전략적설계` `#전술적설계` `#DomainEvent` `#도메인이벤트`

## 왜 알아야 하는가?

- **실무**: 복잡한 비즈니스 로직을 체계적으로 설계하는 표준 방법론. 대규모 시스템에서 필수
- **면접**: "도메인 모델은 어떻게 설계하나요?", "MSA에서 서비스 경계는 어떻게 나누나요?" 질문의 핵심 답변
- **기반 지식**: MSA 서비스 분리, Clean Architecture, Event Sourcing의 이론적 기반

## 핵심 개념

- **Ubiquitous Language**: 도메인 전문가와 개발자가 공유하는 공통 언어
- **Bounded Context**: 특정 도메인 모델이 적용되는 명확한 경계
- **Aggregate**: 데이터 변경의 단위가 되는 객체 클러스터

## 쉽게 이해하기

**회사 조직도 비유**

```
┌─────────────────────────────────────────────────────────────────┐
│                        회 사                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [전략적 설계: Bounded Context = 부서]                         │
│                                                                  │
│   ┌─────────────────┐    ┌─────────────────┐                    │
│   │    영업부       │    │    개발부        │                    │
│   │   (Sales)      │    │ (Development)   │                    │
│   │                │    │                 │                    │
│   │  "고객" = 매출  │    │ "고객" = 사용자  │ ← 같은 단어,       │
│   │       원천     │    │      경험       │   다른 의미!        │
│   └─────────────────┘    └─────────────────┘                    │
│                                                                  │
│   각 부서는 자신만의 언어와 관점을 가짐                          │
│   (Bounded Context마다 Ubiquitous Language가 다름)              │
│                                                                  │
│   ─────────────────────────────────────────────────────────────  │
│                                                                  │
│   [전술적 설계: Aggregate = 팀]                                  │
│                                                                  │
│   ┌───────────────────────────────────────────────────────┐     │
│   │                    개발팀 (Aggregate)                  │     │
│   │                                                        │     │
│   │    ┌─────────────┐                                    │     │
│   │    │   팀장      │ ← Aggregate Root                   │     │
│   │    │ (책임자)    │   모든 의사결정은 팀장을 통해       │     │
│   │    └─────────────┘                                    │     │
│   │          │                                            │     │
│   │    ┌─────┴─────┐                                      │     │
│   │    ▼           ▼                                      │     │
│   │  ┌─────┐   ┌─────┐                                    │     │
│   │  │팀원A│   │팀원B│ ← Entity (개별 식별)               │     │
│   │  └─────┘   └─────┘                                    │     │
│   │                                                        │     │
│   │  외부에서 팀원에게 직접 지시 ✗                         │     │
│   │  팀장을 통해서만 소통 ✓                                │     │
│   │                                                        │     │
│   └───────────────────────────────────────────────────────┘     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 전략적 설계 vs 전술적 설계

```
┌────────────────────────────────────────────────────────────────┐
│                DDD 설계의 두 가지 레벨                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   [전략적 설계 - Strategic Design]                              │
│   ────────────────────────────────                              │
│   "큰 그림" - 시스템을 어떻게 나눌 것인가?                      │
│                                                                 │
│   • Bounded Context: 도메인 모델의 경계                         │
│   • Context Map: 컨텍스트 간 관계                               │
│   • Ubiquitous Language: 공통 언어 정의                         │
│                                                                 │
│   ────────────────────────────────────────────────────────────  │
│                                                                 │
│   [전술적 설계 - Tactical Design]                               │
│   ─────────────────────────────────                             │
│   "세부 구현" - 각 컨텍스트 내부를 어떻게 설계할 것인가?        │
│                                                                 │
│   • Aggregate: 일관성 경계                                      │
│   • Entity: 식별 가능한 객체                                    │
│   • Value Object: 값으로 비교되는 객체                          │
│   • Domain Service: 엔티티에 속하지 않는 도메인 로직            │
│   • Repository: 영속성 추상화                                   │
│   • Domain Event: 도메인에서 발생한 사건                        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### Bounded Context와 Ubiquitous Language

```
┌────────────────────────────────────────────────────────────────┐
│              Bounded Context 예시: 이커머스                     │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                    Order Context                         │  │
│   │                                                          │  │
│   │  "Product" = 주문 항목 (수량, 가격 스냅샷)               │  │
│   │  "Customer" = 주문자 정보 (배송 주소)                    │  │
│   │                                                          │  │
│   │  class OrderItem {                                       │  │
│   │      ProductSnapshot product;  // 주문 시점 상품 정보    │  │
│   │      int quantity;                                       │  │
│   │      Money price;                                        │  │
│   │  }                                                       │  │
│   └─────────────────────────────────────────────────────────┘  │
│                              │                                  │
│              Anti-Corruption Layer (변환 계층)                  │
│                              │                                  │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                   Catalog Context                        │  │
│   │                                                          │  │
│   │  "Product" = 상품 마스터 (설명, 이미지, 카테고리)        │  │
│   │                                                          │  │
│   │  class Product {                                         │  │
│   │      String name;                                        │  │
│   │      String description;                                 │  │
│   │      List<Image> images;                                 │  │
│   │      Category category;                                  │  │
│   │      Price currentPrice;  // 현재 가격 (변동 가능)       │  │
│   │  }                                                       │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                  Shipping Context                        │  │
│   │                                                          │  │
│   │  "Product" = 배송 물품 (무게, 크기, 취급 주의)           │  │
│   │  "Customer" = 수령인 (연락처, 배송지)                    │  │
│   │                                                          │  │
│   │  class Shipment {                                        │  │
│   │      Address destination;                                │  │
│   │      List<Package> packages;  // 박스 단위               │  │
│   │      ShippingMethod method;                              │  │
│   │  }                                                       │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   핵심: 같은 "Product"도 컨텍스트마다 의미와 속성이 다름        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

**왜 컨텍스트를 나누는가?**

하나의 거대한 모델로 모든 것을 표현하려 하면 모델이 복잡해지고, 변경의 영향 범위가 커집니다. 각 컨텍스트가 자신의 책임에 집중하면 모델이 단순해지고 독립적으로 발전할 수 있습니다.

### Context Map: 컨텍스트 간 관계

```
┌────────────────────────────────────────────────────────────────┐
│                      Context Map 패턴                           │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. Shared Kernel (공유 커널)                                  │
│   ─────────────────────────────                                 │
│   ┌───────────┐         ┌───────────┐                          │
│   │ Context A │◄───────►│ Context B │                          │
│   └───────────┘   공유   └───────────┘                          │
│         └────── 모델 ──────┘                                    │
│   양쪽이 공유 모델에 의존. 변경 시 양쪽 협의 필요               │
│                                                                 │
│   2. Customer-Supplier (고객-공급자)                            │
│   ────────────────────────────────                              │
│   ┌───────────┐         ┌───────────┐                          │
│   │ Upstream  │────────►│Downstream │                          │
│   │ (Supplier)│  제공   │ (Customer)│                          │
│   └───────────┘         └───────────┘                          │
│   Upstream이 API를 제공, Downstream이 사용                      │
│                                                                 │
│   3. Anti-Corruption Layer (부패 방지 계층)                     │
│   ─────────────────────────────────────                         │
│   ┌───────────┐  ┌─────┐  ┌───────────┐                        │
│   │  Legacy   │──│ ACL │──│    New    │                        │
│   │  System   │  │변환 │  │  Context  │                        │
│   └───────────┘  └─────┘  └───────────┘                        │
│   레거시/외부 시스템의 모델이 침투하지 않도록 변환 계층         │
│                                                                 │
│   4. Published Language (공표된 언어)                           │
│   ─────────────────────────────────                             │
│   ┌───────────┐   JSON/  ┌───────────┐                         │
│   │ Context A │──Schema──│ Context B │                         │
│   └───────────┘  표준화  └───────────┘                         │
│   표준화된 스키마로 통신 (OpenAPI, Protobuf 등)                 │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### Aggregate 설계

```
┌────────────────────────────────────────────────────────────────┐
│                    Aggregate 상세 구조                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                  Order Aggregate                         │  │
│   │  ─────────────────────────────────────────────────────  │  │
│   │                                                          │  │
│   │       ┌─────────────────────────────────┐               │  │
│   │       │      Order (Aggregate Root)     │               │  │
│   │       │  ─────────────────────────────  │               │  │
│   │       │  - orderId: OrderId             │               │  │
│   │       │  - status: OrderStatus          │               │  │
│   │       │  - orderedAt: LocalDateTime     │               │  │
│   │       │                                 │               │  │
│   │       │  + addItem(product, qty)        │               │  │
│   │       │  + removeItem(itemId)           │               │  │
│   │       │  + confirm()                    │               │  │
│   │       │  + cancel()                     │               │  │
│   │       └───────────────┬─────────────────┘               │  │
│   │                       │                                  │  │
│   │              ┌────────┴────────┐                        │  │
│   │              ▼                 ▼                        │  │
│   │   ┌─────────────────┐   ┌─────────────────┐            │  │
│   │   │   OrderItem     │   │   OrderItem     │            │  │
│   │   │    (Entity)     │   │    (Entity)     │            │  │
│   │   │ ─────────────── │   │ ─────────────── │            │  │
│   │   │ - itemId        │   │ - itemId        │            │  │
│   │   │ - productId     │   │ - productId     │            │  │
│   │   │ - quantity      │   │ - quantity      │            │  │
│   │   │ - unitPrice     │   │ - unitPrice     │            │  │
│   │   └─────────────────┘   └─────────────────┘            │  │
│   │                                                          │  │
│   │   ┌─────────────────┐   ┌─────────────────┐            │  │
│   │   │  ShippingAddr   │   │     Money       │            │  │
│   │   │ (Value Object)  │   │ (Value Object)  │            │  │
│   │   │ ─────────────── │   │ ─────────────── │            │  │
│   │   │ - street        │   │ - amount        │            │  │
│   │   │ - city          │   │ - currency      │            │  │
│   │   │ - zipCode       │   │                 │            │  │
│   │   │                 │   │ 불변, 값 비교   │            │  │
│   │   └─────────────────┘   └─────────────────┘            │  │
│   │                                                          │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   규칙:                                                         │
│   ✓ 외부에서는 Aggregate Root(Order)를 통해서만 접근           │
│   ✓ 하나의 트랜잭션에서 하나의 Aggregate만 수정                │
│   ✓ Aggregate 간 참조는 ID로만                                 │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### Entity vs Value Object

```
┌────────────────────────────────────────────────────────────────┐
│                 Entity vs Value Object                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   [Entity: 식별자로 구별]                                       │
│   ─────────────────────────                                     │
│   - 고유 ID로 식별                                              │
│   - 생명주기를 가짐 (생성, 변경, 삭제)                          │
│   - 속성이 바뀌어도 같은 엔티티                                 │
│                                                                 │
│   예시:                                                         │
│   • User(id=1, name="Kim") → name이 "Lee"로 바뀌어도 같은 User  │
│   • Order, Product, Customer 등                                 │
│                                                                 │
│   ────────────────────────────────────────────────────────────  │
│                                                                 │
│   [Value Object: 값으로 구별]                                   │
│   ───────────────────────────                                   │
│   - 속성 값의 조합으로 식별                                     │
│   - 불변 (Immutable)                                            │
│   - 교체 가능 (같은 값이면 교환 가능)                           │
│                                                                 │
│   예시:                                                         │
│   • Money(1000, KRW) == Money(1000, KRW) → 같음                 │
│   • Address, DateRange, Email 등                                │
│                                                                 │
│   ────────────────────────────────────────────────────────────  │
│                                                                 │
│   [왜 구분하는가?]                                              │
│                                                                 │
│   Value Object를 사용하면:                                      │
│   ✓ 불변성으로 부수효과 방지                                   │
│   ✓ 자체 검증 로직 캡슐화                                      │
│   ✓ 도메인 개념 명확화                                         │
│                                                                 │
│   // Bad: 원시 타입 사용                                        │
│   void transfer(String fromAccount, String toAccount, int amt); │
│                                                                 │
│   // Good: Value Object 사용                                    │
│   void transfer(AccountNumber from, AccountNumber to, Money m); │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 구현 예시

### Aggregate Root 구현

```java
// Order Aggregate Root
public class Order {

    private final OrderId id;
    private OrderStatus status;
    private CustomerId customerId;
    private List<OrderItem> items;
    private ShippingAddress shippingAddress;
    private Money totalAmount;
    private LocalDateTime orderedAt;

    // 도메인 이벤트 수집
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // 생성자: 팩토리 메서드로 생성 강제
    private Order(OrderId id, CustomerId customerId) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
        this.items = new ArrayList<>();
        this.totalAmount = Money.ZERO;
        this.orderedAt = LocalDateTime.now();
    }

    // 팩토리 메서드
    public static Order create(CustomerId customerId) {
        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId, customerId);
        order.registerEvent(new OrderCreatedEvent(orderId, customerId));
        return order;
    }

    // 비즈니스 로직: 아이템 추가
    public void addItem(ProductId productId, int quantity, Money unitPrice) {
        validateModifiable();

        OrderItem existingItem = findItemByProductId(productId);
        if (existingItem != null) {
            existingItem.increaseQuantity(quantity);
        } else {
            OrderItem newItem = new OrderItem(
                OrderItemId.generate(), productId, quantity, unitPrice);
            items.add(newItem);
        }

        recalculateTotalAmount();
    }

    // 비즈니스 로직: 주문 확정
    public void confirm() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalOrderStateException(
                "Only CREATED orders can be confirmed");
        }
        if (items.isEmpty()) {
            throw new EmptyOrderException("Order must have at least one item");
        }

        this.status = OrderStatus.CONFIRMED;
        registerEvent(new OrderConfirmedEvent(id, customerId, totalAmount));
    }

    // 비즈니스 로직: 주문 취소
    public void cancel(String reason) {
        if (!status.isCancellable()) {
            throw new IllegalOrderStateException(
                "Order in status " + status + " cannot be cancelled");
        }

        this.status = OrderStatus.CANCELLED;
        registerEvent(new OrderCancelledEvent(id, reason));
    }

    // 불변식 검증
    private void validateModifiable() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalOrderStateException(
                "Cannot modify order in status: " + status);
        }
    }

    private void recalculateTotalAmount() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.ZERO, Money::add);
    }

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    // ... getters (setters는 없음 - 불변성)
}
```

### Value Object 구현

```java
// Money Value Object
public final class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.KRW);

    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency are required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount.setScale(currency.getDecimalPlaces(), RoundingMode.HALF_UP);
        this.currency = currency;
    }

    public static Money of(long amount, Currency currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientMoneyException("Insufficient amount");
        }
        return new Money(result, this.currency);
    }

    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(
                "Cannot operate on different currencies: " +
                this.currency + " and " + other.currency);
        }
    }

    // equals & hashCode: 값 기반 비교
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 &&
               currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return currency.getSymbol() + " " + amount;
    }
}
```

### Domain Service 구현

```java
// 주문 금액 계산 (여러 Aggregate에 걸친 로직)
@DomainService
public class OrderPricingService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final ShippingFeeCalculator shippingFeeCalculator;

    public OrderPricing calculate(Order order, Customer customer) {
        Money itemsTotal = order.getItemsTotal();

        // 할인 적용
        List<DiscountPolicy> applicablePolicies =
            discountPolicyRepository.findApplicable(customer.getMembership(), itemsTotal);

        Money discount = applicablePolicies.stream()
            .map(policy -> policy.calculate(itemsTotal))
            .reduce(Money.ZERO, Money::add);

        // 배송비 계산
        Money shippingFee = shippingFeeCalculator.calculate(
            order.getShippingAddress(),
            order.getTotalWeight()
        );

        Money finalAmount = itemsTotal.subtract(discount).add(shippingFee);

        return new OrderPricing(itemsTotal, discount, shippingFee, finalAmount);
    }
}
```

### Repository 인터페이스

```java
// 도메인 레이어의 Repository 인터페이스
public interface OrderRepository {

    Order findById(OrderId id);

    Optional<Order> findByIdOptional(OrderId id);

    List<Order> findByCustomerId(CustomerId customerId);

    void save(Order order);

    void delete(Order order);
}

// 인프라 레이어의 구현체
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    @Override
    public Order findById(OrderId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomain)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        jpaRepository.save(entity);

        // 도메인 이벤트 발행
        order.getDomainEvents().forEach(eventPublisher::publish);
        order.clearDomainEvents();
    }
}
```

### Application Service

```java
// Application Service: 유스케이스 조율
@Service
@Transactional
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderPricingService pricingService;

    public OrderId createOrder(CreateOrderCommand command) {
        // 1. 고객 조회
        Customer customer = customerRepository.findById(command.getCustomerId());

        // 2. 주문 생성 (도메인 로직)
        Order order = Order.create(customer.getId());

        // 3. 상품 추가 (도메인 로직)
        for (OrderItemCommand item : command.getItems()) {
            Product product = productRepository.findById(item.getProductId());
            order.addItem(product.getId(), item.getQuantity(), product.getPrice());
        }

        // 4. 배송 주소 설정
        order.setShippingAddress(command.getShippingAddress());

        // 5. 저장
        orderRepository.save(order);

        return order.getId();
    }

    public void confirmOrder(ConfirmOrderCommand command) {
        Order order = orderRepository.findById(command.getOrderId());

        // 도메인 로직 호출
        order.confirm();

        orderRepository.save(order);
    }
}
```

### Domain Event

```java
// 도메인 이벤트 정의
public class OrderConfirmedEvent implements DomainEvent {

    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money totalAmount;
    private final LocalDateTime occurredAt;

    public OrderConfirmedEvent(OrderId orderId, CustomerId customerId, Money totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.occurredAt = LocalDateTime.now();
    }

    // getters...
}

// 이벤트 핸들러 (다른 Bounded Context)
@Component
public class OrderConfirmedEventHandler {

    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderConfirmedEvent event) {
        // 결제 처리 시작
        paymentService.initiatePayment(event.getOrderId(), event.getTotalAmount());

        // 알림 발송
        notificationService.sendOrderConfirmation(event.getCustomerId(), event.getOrderId());
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 비즈니스 로직이 도메인 객체에 응집 | 학습 곡선이 높음 |
| 도메인 전문가와 소통 용이 | 작은 프로젝트에는 오버엔지니어링 |
| 변경에 강한 설계 | 초기 설계에 시간 소요 |
| MSA 서비스 경계 도출에 유용 | 잘못된 경계는 수정 비용 큼 |

### DDD 도입 시 고려사항

```
┌────────────────────────────────────────────────────────────────┐
│                    DDD 도입 체크리스트                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   [DDD 권장]                                                    │
│   ✓ 복잡한 비즈니스 로직이 핵심                                │
│   ✓ 도메인 전문가와 협업 가능                                  │
│   ✓ 장기 유지보수가 중요한 시스템                              │
│   ✓ MSA 전환을 계획 중                                         │
│                                                                 │
│   [DDD 보류]                                                    │
│   ✗ CRUD 위주의 단순한 시스템                                  │
│   ✗ 도메인 전문가 부재                                         │
│   ✗ 단기 프로젝트, 프로토타입                                  │
│   ✗ 팀의 DDD 경험 부족 + 학습 시간 부족                        │
│                                                                 │
│   [단계적 도입]                                                 │
│   1단계: Ubiquitous Language부터 (용어 정리)                   │
│   2단계: Bounded Context 식별                                   │
│   3단계: 핵심 도메인에 전술적 패턴 적용                         │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 트러블슈팅

### 사례 1: Aggregate 경계 잘못 설정

#### 증상
트랜잭션에서 여러 Aggregate를 수정해야 하는 상황이 자주 발생

#### 원인 분석
Aggregate 경계가 비즈니스 불변식과 맞지 않게 설계됨

#### 해결 방법
```
[잘못된 설계]
Order Aggregate + Payment Aggregate 분리
→ 주문 확정 시 둘 다 수정 필요 (트랜잭션 문제)

[개선된 설계 옵션 1: Aggregate 병합]
Order Aggregate에 Payment 포함 (같은 트랜잭션)

[개선된 설계 옵션 2: 이벤트 기반]
Order 확정 → OrderConfirmedEvent 발행
→ Payment 서비스가 비동기로 처리
→ 최종 일관성 허용
```

### 사례 2: 빈약한 도메인 모델 (Anemic Domain Model)

#### 증상
Entity가 getter/setter만 있고, 비즈니스 로직은 Service에 있음

#### 원인 분석
DDD 전술적 패턴 미적용, 절차적 프로그래밍 습관

#### 해결 방법
```java
// Bad: Anemic Domain Model
class Order {
    private OrderStatus status;
    public void setStatus(OrderStatus status) { this.status = status; }
}

class OrderService {
    public void confirm(Order order) {
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException();
        }
        order.setStatus(OrderStatus.CONFIRMED);
    }
}

// Good: Rich Domain Model
class Order {
    private OrderStatus status;

    public void confirm() {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalOrderStateException("...");
        }
        this.status = OrderStatus.CONFIRMED;
        registerEvent(new OrderConfirmedEvent(this.id));
    }
}
```

## 면접 예상 질문

### Q: Bounded Context를 어떻게 식별하나요?

A: 주로 Event Storming을 통해 식별합니다. 도메인 전문가와 함께 비즈니스 이벤트를 나열하고, 이벤트가 발생하는 맥락을 그룹화합니다. 또한 같은 용어가 다른 의미로 사용되는 지점, 조직 구조(콘웨이 법칙), 기존 시스템 경계를 참고합니다. **왜 이게 중요하냐면**, 잘못된 경계는 서비스 간 과도한 결합이나 빈번한 통신을 유발하여 MSA의 이점을 상쇄하기 때문입니다.

### Q: Aggregate 설계 시 주의할 점은?

A: 첫째, Aggregate는 트랜잭션 일관성 경계입니다. 하나의 트랜잭션에서 하나의 Aggregate만 수정합니다. 둘째, 가능한 작게 유지합니다. 큰 Aggregate는 동시성 충돌과 성능 문제를 유발합니다. 셋째, Aggregate 간 참조는 ID로만 합니다. **왜 이런 규칙이 있냐면**, Aggregate 간 직접 참조는 경계를 무너뜨리고 변경 영향 범위를 예측할 수 없게 만들기 때문입니다.

### Q: Domain Service와 Application Service의 차이는?

A: Domain Service는 특정 Entity에 속하지 않는 도메인 로직을 담습니다. 예: 환율 계산, 할인 정책 적용. Application Service는 유스케이스를 조율합니다. 트랜잭션 관리, 여러 도메인 객체 조합, 외부 서비스 호출 등을 담당합니다. **핵심 차이는**, Domain Service는 비즈니스 규칙을, Application Service는 작업 흐름을 담당한다는 것입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [OOP](../programming/oop.md) | 선수 지식 - 객체지향 기본 | Beginner |
| [MSA vs 모놀리식](./msa-vs-monolithic.md) | 선수 지식 - 아키텍처 이해 | Intermediate |
| [Hexagonal Architecture](./hexagonal-architecture.md) | 관련 - 도메인 중심 설계 | Intermediate |
| [Event-Driven Architecture](./event-driven-architecture.md) | 관련 - 도메인 이벤트 | Intermediate |
| [CQRS & 이벤트 소싱](./cqrs-event-sourcing.md) | 후속 - 심화 패턴 | Advanced |

## 참고 자료

- Eric Evans, "Domain-Driven Design: Tackling Complexity in the Heart of Software", Addison-Wesley
- Vaughn Vernon, "Implementing Domain-Driven Design", Addison-Wesley
- Vaughn Vernon, "Domain-Driven Design Distilled", Addison-Wesley
- [DDD Community](https://www.domainlanguage.com/)
