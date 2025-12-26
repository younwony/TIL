# 응집도와 결합도 (Cohesion & Coupling)

> `[2] 입문` · 선수 지식: [OOP](./oop.md)

> 모듈 내부의 관련성(응집도)과 모듈 간 의존성(결합도)을 측정하는 소프트웨어 설계 지표

`#Cohesion` `#Coupling` `#응집도` `#결합도` `#모듈화` `#Modularization` `#SRP` `#단일책임원칙` `#HighCohesion` `#LowCoupling` `#의존성` `#Dependency`

## 왜 알아야 하는가?

응집도와 결합도는 **좋은 설계의 핵심 지표**입니다. "높은 응집도, 낮은 결합도"는 유지보수성, 테스트 용이성, 재사용성의 기반이며, 면접에서 "좋은 코드란 무엇인가?"라는 질문에 대한 핵심 답변입니다.

- **실무**: 변경 영향 범위를 최소화하고, 버그 수정 시 사이드 이펙트를 줄임
- **면접**: "SOLID 원칙", "좋은 설계"를 설명할 때 반드시 언급되는 개념
- **기반 지식**: 디자인 패턴, 클린 아키텍처, MSA 설계의 기초

## 핵심 개념

- **응집도 (Cohesion)**: 모듈 내부 요소들이 얼마나 밀접하게 관련되어 있는가
- **결합도 (Coupling)**: 모듈 간 의존성이 얼마나 강한가
- **목표**: 높은 응집도(High Cohesion) + 낮은 결합도(Low Coupling)

## 쉽게 이해하기

**응집도**를 회사 부서에 비유할 수 있습니다.

```
낮은 응집도: 마케팅부에 개발자, 회계사, 디자이너가 섞여 있음
           → 업무 협조가 어렵고, 부서 역할이 불명확

높은 응집도: 마케팅부에는 마케터만, 개발부에는 개발자만
           → 부서 역할이 명확하고, 내부 협업이 원활
```

**결합도**를 레고 블록에 비유할 수 있습니다.

```
높은 결합도: 블록이 접착제로 붙어 있음
           → 하나를 떼면 다른 것도 부서짐

낮은 결합도: 블록이 규격화된 돌기로 연결
           → 필요한 블록만 쉽게 교체 가능
```

## 상세 설명

### 응집도 유형 (낮음 → 높음)

| 유형 | 설명 | 예시 |
|------|------|------|
| **우연적 (Coincidental)** | 관련 없는 요소들의 모음 | Utils 클래스에 sendEmail(), calculateTax(), formatDate() |
| **논리적 (Logical)** | 논리적으로 비슷한 것들 | 모든 입력 처리를 하나의 클래스에 |
| **시간적 (Temporal)** | 같은 시점에 실행되는 것들 | 초기화 함수에 DB 연결 + 로깅 설정 + 캐시 초기화 |
| **절차적 (Procedural)** | 순서대로 실행되는 것들 | 파일 열기 → 읽기 → 닫기 |
| **통신적 (Communicational)** | 같은 데이터를 사용 | 같은 레코드를 읽고 쓰는 함수들 |
| **순차적 (Sequential)** | 한 함수의 출력이 다음 입력 | 데이터 변환 파이프라인 |
| **기능적 (Functional)** | 단일 기능 수행 | calculateTax() - 세금 계산만 |

**왜 기능적 응집도가 좋은가?**
- 함수/클래스의 목적이 명확함
- 변경 이유가 하나뿐 (SRP 준수)
- 테스트와 재사용이 쉬움

### 결합도 유형 (높음 → 낮음)

| 유형 | 설명 | 예시 |
|------|------|------|
| **내용 (Content)** | 다른 모듈 내부를 직접 접근 | 다른 클래스의 private 필드를 reflection으로 수정 |
| **공통 (Common)** | 전역 변수 공유 | 여러 클래스가 static 변수를 읽고 씀 |
| **외부 (External)** | 외부 포맷/프로토콜 공유 | 두 모듈이 같은 파일 포맷에 의존 |
| **제어 (Control)** | 다른 모듈의 로직을 제어 | flag를 전달해서 상대 동작 결정 |
| **스탬프 (Stamp)** | 복합 데이터 구조 전달 | 필요 이상의 데이터를 담은 객체 전달 |
| **자료 (Data)** | 필요한 데이터만 전달 | 원시 타입 또는 필요한 필드만 전달 |
| **메시지 (Message)** | 메시지로만 통신 | 이벤트, 메시지 큐 |

**왜 낮은 결합도가 좋은가?**
- 한 모듈 변경 시 다른 모듈에 영향이 적음
- 모듈을 독립적으로 테스트 가능
- 모듈 교체/재사용이 쉬움

### 코드 예시

```java
// ❌ 낮은 응집도 + 높은 결합도
class UserService {
    private UserRepository userRepository;
    private EmailSender emailSender;
    private PaymentGateway paymentGateway;
    private ReportGenerator reportGenerator;

    public void registerUser(User user) { /* 사용자 등록 */ }
    public void sendPromotionEmail() { /* 마케팅 이메일 */ }
    public void processPayment(Order order) { /* 결제 처리 */ }
    public void generateSalesReport() { /* 리포트 생성 */ }
}
// 문제: 하나의 클래스가 너무 많은 책임을 가짐
// 결제 로직 수정 시 UserService 전체를 재배포해야 함

// ✅ 높은 응집도 + 낮은 결합도
class UserService {
    private UserRepository userRepository;

    public void registerUser(User user) { /* 사용자 관련만 */ }
    public User findUser(Long id) { /* 사용자 관련만 */ }
}

class PaymentService {
    private PaymentGateway paymentGateway;

    public void processPayment(Order order) { /* 결제 관련만 */ }
}

class EmailService {
    private EmailSender emailSender;

    public void sendEmail(String to, String content) { /* 이메일 관련만 */ }
}
// 장점: 각 서비스가 단일 책임, 독립적으로 변경/테스트 가능
```

### 결합도를 낮추는 방법

```java
// ❌ 높은 결합도: 구체 클래스 직접 의존
class OrderService {
    private MySQLOrderRepository repository = new MySQLOrderRepository();

    public void createOrder(Order order) {
        repository.save(order);
    }
}
// 문제: MySQL → PostgreSQL 변경 시 OrderService 수정 필요

// ✅ 낮은 결합도: 인터페이스 의존 + DI
interface OrderRepository {
    void save(Order order);
}

class OrderService {
    private final OrderRepository repository;  // 인터페이스 의존

    public OrderService(OrderRepository repository) {  // DI
        this.repository = repository;
    }

    public void createOrder(Order order) {
        repository.save(order);
    }
}
// 장점: DB 변경해도 OrderService 수정 불필요
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 변경 영향 범위 최소화 | 초기 설계 비용 증가 |
| 테스트 용이성 향상 | 클래스/파일 수 증가 |
| 재사용성 향상 | 과도한 분리 시 복잡성 증가 |
| 병렬 개발 가능 | 추상화 레이어 추가 오버헤드 |

## 면접 예상 질문

### Q: 응집도와 결합도가 무엇이고, 왜 중요한가요?

A: **응집도**는 모듈 내부 요소들의 관련성, **결합도**는 모듈 간 의존성입니다. **높은 응집도 + 낮은 결합도**가 좋은 설계입니다. **왜냐하면** (1) 변경 시 영향 범위가 최소화되고 (2) 독립적으로 테스트 가능하며 (3) 모듈 재사용이 쉽기 때문입니다. 이는 SOLID의 SRP(단일 책임), DIP(의존 역전)와 직접 연결됩니다.

### Q: 결합도를 낮추는 방법은?

A: (1) **인터페이스 의존**: 구체 클래스가 아닌 추상화에 의존 (2) **의존성 주입(DI)**: 외부에서 의존성을 주입받음 (3) **이벤트 기반**: 직접 호출 대신 이벤트/메시지로 통신 (4) **중재자 패턴**: 모듈 간 직접 참조 대신 중재자를 통해 통신. **왜냐하면** 이 방법들은 모듈 간 직접 참조를 제거하고, 변경 영향을 격리하기 때문입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [OOP](./oop.md) | SOLID 원칙 (SRP, DIP) | [2] 입문 |
| [디자인 패턴](./design-pattern.md) | 결합도를 낮추는 패턴들 | [3] 중급 |
| [클린 코드](./clean-code.md) | 좋은 함수/클래스 설계 | [3] 중급 |
| [DDD](./ddd.md) | 바운디드 컨텍스트, 응집도 | [4] 심화 |

## 참고 자료

- Clean Architecture - Robert C. Martin
- [Cohesion and Coupling - Wikipedia](https://en.wikipedia.org/wiki/Cohesion_(computer_science))
- Structured Design - Larry Constantine
