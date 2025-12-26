# DDD (도메인 주도 설계)

> `[4] 심화` · 선수 지식: [OOP](./oop.md), [클린 코드](./clean-code.md), [디자인 패턴](./design-pattern.md)

> 복잡한 비즈니스 도메인을 소프트웨어 설계의 중심에 두는 방법론

`#DDD` `#DomainDrivenDesign` `#도메인주도설계` `#유비쿼터스언어` `#UbiquitousLanguage` `#바운디드컨텍스트` `#BoundedContext` `#애그리거트` `#Aggregate` `#엔티티` `#Entity` `#값객체` `#ValueObject` `#도메인이벤트` `#DomainEvent` `#리포지토리` `#Repository` `#도메인서비스` `#DomainService` `#전략적설계` `#StrategicDesign` `#전술적설계` `#TacticalDesign` `#컨텍스트맵` `#ContextMap`

## 왜 알아야 하는가?

DDD는 복잡한 비즈니스 로직을 효과적으로 모델링하는 방법론입니다. MSA에서 서비스 경계를 정하는 핵심 기준이 되며, 대규모 프로젝트에서 도메인 전문가와 개발자 간 소통을 개선합니다. 시니어 개발자에게 필수적인 설계 역량입니다.

## 핵심 개념

- **유비쿼터스 언어**: 도메인 전문가와 개발자가 공유하는 공통 언어
- **바운디드 컨텍스트**: 모델이 적용되는 명확한 경계
- **애그리거트**: 일관성을 유지하는 엔티티 묶음
- **도메인 이벤트**: 도메인에서 발생한 중요한 사건

## 쉽게 이해하기

**DDD**를 회사 조직에 비유할 수 있습니다.

- **유비쿼터스 언어**: 회사 내 공통 용어집 (모두가 같은 의미로 사용)
- **바운디드 컨텍스트**: 부서 (영업부의 "고객"과 CS부의 "고객"은 다를 수 있음)
- **애그리거트**: 팀 (팀장이 팀의 일관성을 책임)
- **도메인 이벤트**: 사내 공지 ("신규 계약 체결됨")

## 상세 설명

### 전략적 설계 (Strategic Design)

```
┌─────────────────────────────────────────────────────────────┐
│                      전체 시스템                              │
├───────────────────┬───────────────────┬─────────────────────┤
│                   │                   │                     │
│  ┌─────────────┐  │  ┌─────────────┐  │  ┌─────────────┐   │
│  │   주문      │  │  │   결제      │  │  │   배송      │   │
│  │  Context    │◄─┼─►│  Context    │◄─┼─►│  Context    │   │
│  └─────────────┘  │  └─────────────┘  │  └─────────────┘   │
│                   │                   │                     │
│  유비쿼터스 언어   │  유비쿼터스 언어   │  유비쿼터스 언어    │
│  - 주문           │  - 결제           │  - 배송            │
│  - 주문항목       │  - 청구           │  - 배송지          │
│  - 주문상태       │  - 환불           │  - 배송상태        │
│                   │                   │                     │
└───────────────────┴───────────────────┴─────────────────────┘

각 Bounded Context는 자신만의 언어와 모델을 가짐
```

### 컨텍스트 맵 (Context Map)

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│  주문    │     │  결제    │     │  배송    │
│ Context  │────►│ Context  │────►│ Context  │
└──────────┘     └──────────┘     └──────────┘
     │                │                │
     │    Upstream    │   Downstream   │
     └────────────────┴────────────────┘

관계 유형:
- 파트너십 (Partnership): 협력
- 공유 커널 (Shared Kernel): 일부 모델 공유
- 고객-공급자 (Customer-Supplier): 상하 관계
- 순응주의 (Conformist): 다운스트림이 업스트림에 맞춤
- 부패 방지 계층 (ACL): 외부 모델 변환
```

### 전술적 설계 (Tactical Design)

#### 엔티티 (Entity)

식별자로 구분되는 객체:

```java
public class Order {
    private OrderId id;  // 식별자
    private CustomerId customerId;
    private List<OrderLine> orderLines;
    private OrderStatus status;

    // 비즈니스 로직
    public void addItem(Product product, int quantity) {
        orderLines.add(new OrderLine(product, quantity));
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED) {
            throw new IllegalStateException("배송된 주문은 취소 불가");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
```

#### 값 객체 (Value Object)

속성으로 동등성 판단, 불변:

```java
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("통화가 다름");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object o) {
        // amount와 currency로 비교
    }
}
```

#### 애그리거트 (Aggregate)

```
┌─────────────────────────────────────────┐
│             Order (Aggregate Root)       │
│  ┌──────────────────────────────────┐   │
│  │  - id                            │   │
│  │  - status                        │   │
│  │  - orderLines[]                  │   │
│  │    └─ OrderLine (Entity)         │   │
│  │       - product                  │   │
│  │       - quantity                 │   │
│  │       - price (Value Object)     │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘

규칙:
1. 외부에서는 Root를 통해서만 접근
2. 트랜잭션 경계 = 애그리거트 경계
3. 다른 애그리거트는 ID로만 참조
```

```java
// 잘못된 예: 내부 엔티티 직접 접근
order.getOrderLines().get(0).setQuantity(5);  // X

// 올바른 예: Root를 통해 접근
order.changeQuantity(orderLineId, 5);  // O
```

#### 도메인 이벤트 (Domain Event)

```java
public class OrderPlacedEvent {
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money totalAmount;
    private final LocalDateTime occurredAt;

    // 과거형 명명: ~했다 (OrderPlaced, PaymentCompleted)
}

// 이벤트 발행
public class Order {
    public void place() {
        this.status = OrderStatus.PLACED;
        Events.publish(new OrderPlacedEvent(this.id, this.customerId, this.total()));
    }
}
```

### DDD 계층 구조

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│                    (Controller, DTO)                         │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                         │
│                    (Service, Use Case)                       │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer                              │
│      (Entity, Value Object, Aggregate, Domain Service)       │
├─────────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                       │
│               (Repository 구현, 외부 시스템)                   │
└─────────────────────────────────────────────────────────────┘

의존성: 위 → 아래 (Domain은 다른 계층에 의존하지 않음)
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 복잡한 비즈니스 로직 관리 | 학습 곡선 높음 |
| 도메인 전문가와 소통 개선 | 단순한 CRUD에는 과도함 |
| MSA 서비스 경계 정의 | 초기 설계 비용 |
| 유지보수성 향상 | 팀 전체 이해 필요 |

## 면접 예상 질문

### Q: 애그리거트란 무엇이고 왜 중요한가요?

A: **애그리거트**는 데이터 일관성을 유지하는 엔티티 묶음입니다. **중요한 이유**: (1) 트랜잭션 경계를 정의 (2) 불변식(invariant) 보장 (3) 복잡도 관리. **규칙**: 외부에서는 Root만 접근, 다른 애그리거트는 ID로 참조, 하나의 트랜잭션에서 하나의 애그리거트만 수정.

### Q: 바운디드 컨텍스트를 어떻게 정하나요?

A: (1) **유비쿼터스 언어가 달라지는 지점**을 찾음 (같은 용어가 다른 의미) (2) **팀/조직 경계**를 고려 (Conway's Law) (3) **비즈니스 역량**을 기준으로 분리 (4) 너무 크면 복잡, 너무 작으면 통신 오버헤드. **핵심**: 언어의 일관성이 유지되는 경계입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [OOP](./oop.md) | 기반 지식 | [2] 입문 |
| [디자인 패턴](./design-pattern.md) | 구현 패턴 | [3] 중급 |
| [MSA vs 모놀리식](../system-design/msa-vs-monolithic.md) | 아키텍처 | [3] 중급 |
| [CQRS & 이벤트 소싱](../system-design/cqrs-event-sourcing.md) | 패턴 | [4] 심화 |

## 참고 자료

- Domain-Driven Design - Eric Evans
- Implementing Domain-Driven Design - Vaughn Vernon
