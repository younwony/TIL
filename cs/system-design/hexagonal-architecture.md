# Hexagonal Architecture (헥사고날 아키텍처)

> `[3] 중급` · 선수 지식: [Layered Architecture](./layered-architecture.md), [OOP](../programming/oop.md)

> 애플리케이션 핵심 로직을 외부 시스템으로부터 분리하여 포트와 어댑터로 연결하는 아키텍처 패턴

`#HexagonalArchitecture` `#헥사고날` `#PortsAndAdapters` `#포트와어댑터` `#CleanArchitecture` `#DomainDrivenDesign` `#DIP` `#의존성역전`

## 왜 알아야 하는가?

Hexagonal Architecture는 **도메인 로직의 순수성**을 보장하는 아키텍처입니다. 프레임워크, DB, 외부 API 변경에도 비즈니스 로직이 영향받지 않도록 설계합니다. Clean Architecture의 기반이 되며, 테스트 용이성과 유지보수성이 뛰어납니다.

- **실무**: 복잡한 도메인 로직을 가진 시스템에서 변경 영향 최소화
- **면접**: "Clean Architecture와 Hexagonal Architecture의 차이는?"
- **기반 지식**: DDD, 클린 아키텍처 이해의 전제

## 핵심 개념

- **Port (포트)**: 애플리케이션과 외부 세계의 인터페이스 (추상화)
- **Adapter (어댑터)**: 포트를 구현하여 외부 시스템과 연결
- **의존성 역전**: 외부가 내부에 의존 (내부가 외부를 모름)

## 쉽게 이해하기

**Hexagonal Architecture**를 콘센트와 플러그에 비유할 수 있습니다.

```
┌─────────────────────────────────────────────────────┐
│                                                      │
│   [220V 콘센트]  ←──→  [가전제품]  ←──→  [110V 콘센트]  │
│     (포트)              (핵심)           (포트)       │
│       ↑                                    ↑         │
│   [한국 플러그]                        [미국 플러그]   │
│    (어댑터)                             (어댑터)      │
│                                                      │
└─────────────────────────────────────────────────────┘

가전제품(도메인)은 콘센트 규격(포트)만 알면 됨
어느 나라에서든 어댑터만 바꾸면 동작
```

## 상세 설명

### 구조

```
              ┌─────────────────────────────────────┐
              │                                      │
   Driving    │         ┌─────────────┐              │    Driven
   (Primary)  │         │   Domain    │              │   (Secondary)
              │         │   (Core)    │              │
  ┌────────┐  │  ┌────┐ │             │ ┌────┐       │  ┌────────────┐
  │ Web UI │──┼─→│Port│─┼─→ Service ──┼→│Port│──────┼─→│  Database  │
  └────────┘  │  └────┘ │             │ └────┘      │  └────────────┘
              │    ↑    │             │    ↑        │
  ┌────────┐  │    │    │             │    │        │  ┌────────────┐
  │  CLI   │──┼────┘    │  Business   │    └────────┼─→│ External   │
  └────────┘  │         │   Logic     │             │  │    API     │
              │         └─────────────┘             │  └────────────┘
              │                                      │
              └─────────────────────────────────────┘
                            Hexagon
```

### Port 종류

| 종류 | 설명 | 방향 | 예시 |
|------|------|------|------|
| **Driving Port (Primary)** | 외부가 애플리케이션을 호출 | 외부 → 내부 | REST API, CLI, 메시지 컨슈머 |
| **Driven Port (Secondary)** | 애플리케이션이 외부를 호출 | 내부 → 외부 | DB, 외부 API, 메시지 발행 |

### 코드 예시

```java
// === Domain (Core) ===
// 순수한 비즈니스 로직, 외부 의존성 없음
public class Order {
    private OrderId id;
    private List<OrderItem> items;
    private OrderStatus status;

    public void complete() {
        if (items.isEmpty()) {
            throw new EmptyOrderException();
        }
        this.status = OrderStatus.COMPLETED;
    }
}

// === Port (Interface) ===
// Driving Port: 외부가 호출하는 인터페이스
public interface OrderUseCase {
    OrderId createOrder(CreateOrderCommand command);
    void completeOrder(OrderId orderId);
}

// Driven Port: 애플리케이션이 사용하는 인터페이스
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId id);
}

public interface PaymentGateway {
    PaymentResult process(Payment payment);
}

// === Application Service ===
// 유스케이스 구현, 포트 조합
@Service
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;  // Driven Port
    private final PaymentGateway paymentGateway;    // Driven Port

    @Override
    public void completeOrder(OrderId orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(OrderNotFoundException::new);

        order.complete();  // 도메인 로직

        paymentGateway.process(order.createPayment());
        orderRepository.save(order);
    }
}

// === Adapter ===
// Driving Adapter: 외부 요청을 포트로 전달
@RestController
public class OrderController {
    private final OrderUseCase orderUseCase;  // Port 의존

    @PostMapping("/orders/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id) {
        orderUseCase.completeOrder(new OrderId(id));
        return ResponseEntity.ok().build();
    }
}

// Driven Adapter: 포트 구현, 실제 외부 시스템 연결
@Repository
public class JpaOrderRepository implements OrderRepository {
    private final JpaOrderEntityRepository jpaRepository;

    @Override
    public void save(Order order) {
        jpaRepository.save(OrderEntity.from(order));
    }
}

@Component
public class StripePaymentGateway implements PaymentGateway {
    private final StripeClient stripeClient;

    @Override
    public PaymentResult process(Payment payment) {
        // Stripe API 호출
    }
}
```

### 의존성 방향

```
┌─────────────────────────────────────────────────────────┐
│                                                          │
│  Controller ──→ UseCase(Port) ←── Service               │
│  (Adapter)         ↑               (구현)                │
│                    │                  │                  │
│                    │                  ↓                  │
│                    │        Repository(Port) ←── JpaRepo │
│                    │              ↑             (Adapter)│
│                    │              │                      │
│                    └──────────────┘                      │
│                      Domain Layer                        │
│                                                          │
└─────────────────────────────────────────────────────────┘

핵심: 화살표가 도메인(중심)을 향함 = 외부가 내부에 의존
```

### Layered vs Hexagonal

| 항목 | Layered | Hexagonal |
|------|---------|-----------|
| 의존성 방향 | 상위 → 하위 (Controller → DB) | 외부 → 내부 (Infrastructure → Domain) |
| DB 의존성 | 비즈니스가 DB를 직접 의존 | 비즈니스가 Port(인터페이스)만 의존 |
| 테스트 | DB Mock 필요 | Port만 Mock하면 됨 |
| 교체 용이성 | DB 교체 시 Service 수정 | Adapter만 교체 |
| 복잡도 | 단순 | 상대적으로 복잡 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 도메인 로직이 프레임워크와 분리 | 초기 설계 복잡도 증가 |
| 테스트 용이 (Port만 Mock) | 인터페이스와 구현 클래스 증가 |
| 외부 시스템 교체 용이 | 학습 곡선 존재 |
| 비즈니스 로직 순수성 보장 | 단순 CRUD에는 과도한 설계 |

### 언제 사용하나?

**적합한 경우**:
- 복잡한 도메인 로직
- 외부 시스템(DB, API) 변경 가능성이 높음
- 테스트 커버리지가 중요
- 장기 유지보수 프로젝트

**부적합한 경우**:
- 단순 CRUD 애플리케이션
- 빠른 프로토타이핑
- 작은 규모의 프로젝트

## 면접 예상 질문

### Q: Hexagonal Architecture의 핵심 원칙은?

A: **의존성 역전(DIP)**입니다. 도메인(핵심)이 외부(DB, API)를 모르고, 외부가 도메인에 의존합니다. **왜냐하면** 도메인 로직이 프레임워크나 인프라 변경에 영향받지 않아야 하기 때문입니다. 이를 위해 Port(인터페이스)를 도메인에 정의하고, Adapter(구현체)는 인프라 계층에 둡니다.

### Q: Layered Architecture와 비교해서 장단점은?

A: **Hexagonal 장점**: (1) 도메인이 DB/프레임워크와 완전 분리 (2) Adapter만 교체하면 DB/외부API 변경 가능 (3) Port만 Mock하면 되어 테스트 용이. **단점**: (1) 인터페이스와 클래스가 많아짐 (2) 단순 CRUD에는 과도한 설계. **사용 기준**: 복잡한 도메인 로직이 있고 장기 유지보수가 필요하면 Hexagonal, 단순 CRUD면 Layered가 적합합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Layered Architecture](./layered-architecture.md) | 선수 지식, 비교 | [2] 입문 |
| [OOP](../programming/oop.md) | DIP 원칙 | [2] 입문 |
| [DDD](../programming/ddd.md) | 도메인 중심 설계 | [4] 심화 |
| [DTO-Entity 변환](./dto-entity-conversion.md) | 계층 간 변환 | [3] 중급 |

## 참고 자료

- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- Clean Architecture - Robert C. Martin
- Get Your Hands Dirty on Clean Architecture - Tom Hombergs
