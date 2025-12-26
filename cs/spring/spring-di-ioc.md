# Spring DI/IoC (의존성 주입과 제어의 역전)

> `[3] 중급` · 선수 지식: [OOP](../programming/oop.md), [리플렉션](../language/reflection.md)

> 객체 간 의존 관계를 외부에서 주입하여 결합도를 낮추는 설계 원칙

`#DI` `#DependencyInjection` `#의존성주입` `#IoC` `#InversionOfControl` `#제어의역전` `#Spring` `#스프링` `#Bean` `#빈` `#Container` `#컨테이너` `#ApplicationContext` `#BeanFactory` `#Autowired` `#Component` `#ComponentScan` `#Configuration` `#생성자주입` `#ConstructorInjection` `#필드주입` `#FieldInjection` `#Setter주입` `#SetterInjection` `#Scope` `#Singleton` `#Prototype` `#Qualifier` `#Primary`

## 왜 알아야 하는가?

Spring Framework의 핵심은 DI/IoC입니다. 이를 이해하지 않으면 Spring을 사용만 할 뿐 제대로 활용할 수 없습니다. DI는 테스트 용이성, 유연한 설계, 느슨한 결합을 가능하게 합니다. 모든 Spring 면접에서 필수로 묻는 주제입니다.

## 핵심 개념

- **IoC (Inversion of Control)**: 객체 생성과 생명주기 관리를 프레임워크에 위임
- **DI (Dependency Injection)**: 필요한 의존 객체를 외부에서 주입
- **Container**: 빈을 생성하고 관리하는 Spring의 핵심 컴포넌트
- **Bean**: Spring이 관리하는 객체

## 쉽게 이해하기

**DI/IoC**를 레스토랑에 비유할 수 있습니다.

```
DI 없이 (직접 요리):
┌─────────────────────────────────────────────────────────────┐
│  손님 (내 코드)                                              │
│  - 재료 직접 구매 (new 키워드)                               │
│  - 요리 직접 수행                                            │
│  - 설거지 직접 처리                                          │
│  → 모든 책임이 나에게!                                       │
└─────────────────────────────────────────────────────────────┘

DI 사용 (레스토랑):
┌─────────────────────────────────────────────────────────────┐
│  손님 (내 코드)                                              │
│  - 메뉴만 선택 (인터페이스)                                  │
│  - 요리사가 재료 준비 (Container가 Bean 생성)                │
│  - 웨이터가 음식 제공 (DI가 의존성 주입)                     │
│  → 먹기만 하면 됨!                                           │
└─────────────────────────────────────────────────────────────┘

IoC = 제어가 역전됨
- 내가 요리한다 → 레스토랑이 요리한다
- 내가 객체 생성 → Spring이 객체 생성
```

## 상세 설명

### IoC란?

```java
// IoC 없이: 직접 제어
public class OrderService {
    // 내가 직접 생성 (강한 결합)
    private OrderRepository repository = new JdbcOrderRepository();
    private PaymentService payment = new KakaoPaymentService();
}

// IoC 적용: 제어 역전
public class OrderService {
    // 외부에서 주입받음 (느슨한 결합)
    private final OrderRepository repository;
    private final PaymentService payment;

    public OrderService(OrderRepository repository, PaymentService payment) {
        this.repository = repository;
        this.payment = payment;
    }
}

// Spring Container가 객체 생성과 주입 담당
// → 내 코드는 "무엇을" 사용할지만 정의
// → "어떻게" 생성할지는 Spring이 결정
```

### DI 방식 비교

```java
// 1. 생성자 주입 (권장)
@Service
public class OrderService {
    private final OrderRepository repository;
    private final PaymentService payment;

    // @Autowired 생략 가능 (생성자가 하나일 때)
    public OrderService(OrderRepository repository, PaymentService payment) {
        this.repository = repository;
        this.payment = payment;
    }
}

// 장점:
// - 불변성 보장 (final)
// - 필수 의존성 명확
// - 테스트 용이 (Mock 주입 쉬움)
// - 순환 참조 컴파일 시점 감지

// 2. 필드 주입 (비권장)
@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private PaymentService payment;
}

// 단점:
// - 불변성 미보장
// - 의존성 숨김
// - 테스트 어려움 (Reflection 필요)
// - 순환 참조 런타임에 발생

// 3. Setter 주입 (선택적 의존성)
@Service
public class OrderService {
    private OrderRepository repository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }
}

// 사용 시점:
// - 선택적 의존성
// - 런타임에 의존성 변경 필요 시
```

### Bean 등록 방법

```java
// 1. @Component 계열 어노테이션
@Component          // 일반 컴포넌트
@Service            // 서비스 레이어
@Repository         // 데이터 접근 레이어
@Controller         // 웹 컨트롤러
@RestController     // REST API 컨트롤러

@Service
public class OrderService {
    // ...
}

// 2. @Configuration + @Bean (명시적 등록)
@Configuration
public class AppConfig {

    @Bean
    public OrderRepository orderRepository() {
        return new JdbcOrderRepository(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}

// 언제 @Bean 사용?
// - 외부 라이브러리 클래스 등록
// - 조건부 빈 생성
// - 복잡한 초기화 로직

// 3. @ComponentScan
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig { }

// Spring Boot는 @SpringBootApplication에 포함
```

### Bean Scope

```java
// 1. Singleton (기본값) - 애플리케이션에 하나
@Service
@Scope("singleton")  // 생략 가능
public class SingletonService { }

// 2. Prototype - 요청마다 새로 생성
@Component
@Scope("prototype")
public class PrototypeBean { }

// 3. Request - HTTP 요청마다 하나 (웹)
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean { }

// 4. Session - HTTP 세션마다 하나 (웹)
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedBean { }

// Singleton + Prototype 주의점
@Service
public class SingletonService {
    private final PrototypeBean prototypeBean;  // 항상 같은 인스턴스!

    // 해결: ObjectProvider 사용
    private final ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public void logic() {
        PrototypeBean newBean = prototypeBeanProvider.getObject();  // 매번 새로
    }
}
```

### 여러 빈 중 선택

```java
// 같은 타입의 빈이 여러 개일 때
public interface PaymentService { }

@Service
public class KakaoPaymentService implements PaymentService { }

@Service
public class NaverPaymentService implements PaymentService { }

// 1. @Primary - 기본 빈 지정
@Service
@Primary
public class KakaoPaymentService implements PaymentService { }

// 2. @Qualifier - 특정 빈 지정
@Service
public class OrderService {
    public OrderService(@Qualifier("naverPaymentService") PaymentService payment) {
        // NaverPaymentService 주입
    }
}

// 3. 필드명 매칭
@Service
public class OrderService {
    private final PaymentService kakaoPaymentService;  // 이름으로 매칭
}

// 4. List로 모두 주입
@Service
public class OrderService {
    private final List<PaymentService> paymentServices;  // 모든 구현체

    public void pay(String type) {
        paymentServices.stream()
            .filter(p -> p.supports(type))
            .findFirst()
            .orElseThrow()
            .pay();
    }
}
```

### Container 동작 원리

```
┌─────────────────────────────────────────────────────────────┐
│                Spring Container 동작                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. 설정 정보 읽기                                           │
│     @Configuration, @ComponentScan                          │
│          │                                                   │
│          ▼                                                   │
│  2. BeanDefinition 생성                                      │
│     빈 메타데이터 (클래스, 스코프, 의존성 등)                │
│          │                                                   │
│          ▼                                                   │
│  3. Bean 생성                                                │
│     ┌──────────────────────────────────────────┐            │
│     │ 1) 인스턴스 생성                          │            │
│     │ 2) 의존성 주입                            │            │
│     │ 3) 초기화 콜백 (@PostConstruct)           │            │
│     └──────────────────────────────────────────┘            │
│          │                                                   │
│          ▼                                                   │
│  4. Bean 사용 (Singleton Pool에서 반환)                      │
│          │                                                   │
│          ▼                                                   │
│  5. 소멸 콜백 (@PreDestroy)                                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

```java
// 빈 생명주기 콜백
@Service
public class OrderService {

    @PostConstruct
    public void init() {
        // 초기화 로직 (의존성 주입 완료 후)
    }

    @PreDestroy
    public void destroy() {
        // 정리 로직 (컨테이너 종료 시)
    }
}

// InitializingBean, DisposableBean 인터페이스도 가능
// @Bean의 initMethod, destroyMethod도 가능
```

### 순환 참조 문제

```java
// 순환 참조: A → B → A
@Service
public class ServiceA {
    private final ServiceB serviceB;  // ServiceB 필요

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;  // ServiceA 필요

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}

// 생성자 주입: 애플리케이션 시작 시 에러 (좋음)
// 필드 주입: 런타임에 문제 발생 (나쁨)

// 해결 방법:
// 1. 설계 재검토 (가장 좋음)
// 2. @Lazy 사용 (임시방편)
@Service
public class ServiceA {
    public ServiceA(@Lazy ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
```

### 테스트에서 DI 활용

```java
// DI 덕분에 Mock 주입이 쉬움
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_생성_테스트() {
        // given
        when(paymentService.pay(any())).thenReturn(true);

        // when
        Order order = orderService.createOrder(request);

        // then
        assertThat(order).isNotNull();
        verify(orderRepository).save(any());
    }
}

// DI 없이는?
// new OrderService(new FakeOrderRepository(), new FakePaymentService())
// 모든 의존성에 대해 Fake 구현 필요
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 느슨한 결합 | 런타임에 객체 결정 (디버깅 어려움) |
| 테스트 용이 | 학습 곡선 |
| 유연한 구성 | 설정 복잡도 |
| 코드 재사용성 | 오버 엔지니어링 가능 |

## 면접 예상 질문

### Q: 생성자 주입을 권장하는 이유는?

A: (1) **불변성**: final 필드로 변경 불가. (2) **필수 의존성 명확**: 생성자 파라미터로 표현. (3) **테스트 용이**: new로 Mock 주입 가능. (4) **순환 참조 감지**: 컴파일/시작 시점에 에러. (5) **NPE 방지**: 주입 실패 시 객체 생성 불가. **필드 주입**은 리플렉션 필요, 의존성 숨김, 테스트 어려움.

### Q: @Autowired와 @Inject의 차이는?

A: **@Autowired**: Spring 전용, required 속성으로 필수 여부 지정. **@Inject**: JSR-330 표준, Spring 외 컨테이너에서도 사용. **차이**: @Autowired는 required=false 가능, @Inject는 불가 (대신 Optional 사용). **권장**: 생성자 주입 시 둘 다 생략 가능, Spring 프로젝트에선 @Autowired 관례.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [OOP](../programming/oop.md) | 설계 원칙 | [2] 입문 |
| [리플렉션](../language/reflection.md) | 동작 원리 | [4] 심화 |
| [Spring AOP](./spring-aop.md) | 함께 사용 | [3] 중급 |
| [Spring MVC](./spring-mvc.md) | 활용 | [3] 중급 |

## 참고 자료

- [Spring Framework Documentation - Core](https://docs.spring.io/spring-framework/reference/core.html)
- [Baeldung - Dependency Injection](https://www.baeldung.com/spring-dependency-injection)
