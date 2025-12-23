# OOP (객체지향 프로그래밍)

**난이도**: [2] 입문

> 현실 세계의 사물을 객체로 모델링하여, 객체 간의 상호작용으로 프로그램을 구성하는 프로그래밍 패러다임

`#OOP` `#객체지향` `#ObjectOriented` `#캡슐화` `#Encapsulation` `#상속` `#Inheritance` `#다형성` `#Polymorphism` `#추상화` `#Abstraction` `#SOLID` `#SRP` `#OCP` `#LSP` `#ISP` `#DIP` `#클래스` `#Class` `#인터페이스` `#Interface` `#추상클래스` `#AbstractClass` `#상속vs조합` `#Composition` `#오버라이딩` `#Override` `#오버로딩` `#Overload` `#접근제어자`

## 왜 알아야 하는가?

객체지향 프로그래밍은 Java, C++, Python 등 주요 언어의 기반 패러다임입니다. 현대 소프트웨어 개발의 필수 개념으로, 이를 이해해야 실무 코드를 읽고 작성할 수 있습니다.

- **실무 표준**: 대부분의 엔터프라이즈 시스템이 OOP로 설계됨
- **협업 기반**: 캡슐화, 인터페이스로 여러 개발자가 독립적으로 개발 가능
- **유지보수 핵심**: 상속, 다형성으로 코드 재사용과 확장성 확보
- **면접 필수**: SOLID 원칙, 디자인 패턴의 기반 개념

## 핵심 개념

- **캡슐화 (Encapsulation)**: 데이터와 메서드를 하나로 묶고, 외부에서 내부 구현을 숨김
- **상속 (Inheritance)**: 부모 클래스의 속성과 메서드를 자식 클래스가 물려받음
- **다형성 (Polymorphism)**: 같은 인터페이스로 다른 구현체를 다룰 수 있음
- **추상화 (Abstraction)**: 복잡한 내부 구현을 숨기고 필요한 기능만 노출
- **SOLID 원칙**: 유지보수성과 확장성을 높이는 5가지 설계 원칙

## 쉽게 이해하기

**OOP**를 레고 블록에 비유할 수 있습니다.

레고 블록(객체)은 각각 모양(속성)과 끼우는 방법(메서드)을 가지고 있습니다.
블록 내부가 어떻게 생겼는지 몰라도 위아래 돌기만 알면 조립할 수 있습니다(캡슐화).
기본 블록을 확장해서 바퀴 달린 블록, 창문 블록을 만들 수 있고(상속),
"블록"이라는 공통 규격 덕분에 어떤 블록이든 서로 끼울 수 있습니다(다형성).

예를 들어, 자동차를 만들 때:
- 엔진 블록, 바퀴 블록, 차체 블록을 조립 (객체 조합)
- 각 블록의 내부 구조는 몰라도 됨 (캡슐화)
- 스포츠카, 트럭 모두 "자동차" 설계도를 기반으로 함 (상속)
- "달리기" 기능은 스포츠카와 트럭이 다르게 동작 (다형성)

**왜 OOP를 사용하나요?**
- 현실 세계를 코드로 자연스럽게 표현
- 코드 재사용성 증가 (상속, 조합)
- 유지보수 용이 (캡슐화로 변경 영향 최소화)
- 대규모 협업에 적합 (인터페이스로 역할 분리)

---

## 상세 설명

### 1. 캡슐화 (Encapsulation)

데이터(필드)와 행위(메서드)를 하나의 단위로 묶고, 외부로부터 내부 구현을 숨기는 것

#### 왜 캡슐화가 필요한가?

```
문제 상황: 은행 계좌의 잔액을 누구나 직접 수정할 수 있다면?
→ account.balance = -1000000;  // 잔액이 음수가 됨!
→ 데이터 무결성 파괴, 버그 발생
```

#### 권장 패턴

**권장 (O)**: private 필드 + public 메서드로 접근 제어
**비권장 (X)**: public 필드로 직접 접근 허용

```java
// Bad: 외부에서 직접 수정 가능
public class BankAccount {
    public long balance;  // 누구나 수정 가능 → 위험!
}

// Good: 메서드를 통한 안전한 접근
public class BankAccount {
    private long balance;  // 외부에서 직접 접근 불가

    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("입금액은 0보다 커야 합니다");
        }
        this.balance += amount;
    }

    public void withdraw(long amount) {
        if (amount > balance) {
            throw new IllegalStateException("잔액이 부족합니다");
        }
        this.balance -= amount;
    }

    public long getBalance() {
        return balance;  // 읽기만 허용
    }
}
```

**왜 이렇게 하는가?**
1. **데이터 무결성 보장**: 메서드 내에서 유효성 검사 가능
2. **변경 영향 최소화**: 내부 구현이 바뀌어도 외부 코드는 그대로
3. **디버깅 용이**: 데이터 변경 지점이 메서드로 한정됨

**만약 지키지 않으면?**
- 잔액이 음수가 되는 등 비정상 상태 발생
- 버그 원인 추적이 어려움 (어디서든 수정 가능하니까)
- 내부 구현 변경 시 모든 외부 코드 수정 필요

---

### 2. 상속 (Inheritance)

기존 클래스의 속성과 메서드를 재사용하여 새로운 클래스를 정의

#### 왜 상속을 사용하는가?

```
문제 상황: Dog, Cat, Bird 클래스에 name, age, sleep() 메서드가 중복됨
→ 코드 중복 → 수정 시 모든 클래스를 고쳐야 함
→ DRY(Don't Repeat Yourself) 원칙 위반
```

#### 상속 예제

```java
public abstract class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 공통 기능: 모든 동물이 잠을 잠
    public void sleep() {
        System.out.println(name + "이(가) 잠을 잡니다");
    }

    // 추상 메서드: 동물마다 다르게 울음
    public abstract void speak();
}

public class Dog extends Animal {
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void speak() {
        System.out.println(name + ": 멍멍!");
    }

    // Dog만의 고유 기능
    public void fetch() {
        System.out.println(name + "이(가) 공을 가져옵니다");
    }
}
```

#### 상속 vs 조합 (Composition)

**권장 (O)**: 조합(Composition) - "has-a" 관계
**주의 (△)**: 상속(Inheritance) - "is-a" 관계가 명확할 때만

```java
// Bad: 기능 재사용 목적으로 상속 (잘못된 사용)
public class Stack extends ArrayList {  // Stack "is-a" ArrayList? NO!
    // ArrayList의 모든 메서드가 노출됨 (add, remove, get...)
    // Stack에서 중간 요소 접근이 가능해져 버림
}

// Good: 조합으로 필요한 기능만 사용
public class Stack<T> {
    private final List<T> elements = new ArrayList<>();  // has-a 관계

    public void push(T item) {
        elements.add(item);
    }

    public T pop() {
        if (elements.isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }
}
```

**왜 조합을 선호하는가?**

| 비교 | 상속 | 조합 |
|------|------|------|
| 결합도 | 강함 (부모 변경 시 자식 영향) | 약함 (내부 객체만 교체) |
| 유연성 | 컴파일 타임에 결정 | 런타임에 동적 교체 가능 |
| 다중 사용 | Java는 단일 상속만 | 여러 객체 조합 가능 |
| 캡슐화 | 부모 구현 노출 | 내부 구현 숨김 |

**상속을 사용해야 할 때:**
- 명확한 "is-a" 관계 (Dog is an Animal)
- 부모 클래스가 상속을 위해 설계됨 (abstract class)
- 다형성이 필요한 경우

**만약 잘못 사용하면?**
- 부모 클래스 수정 시 모든 자식에게 영향 (깨지기 쉬운 기반 클래스 문제)
- 불필요한 메서드가 자식에게 노출
- 상속 계층이 깊어지면 이해하기 어려움

---

### 3. 다형성 (Polymorphism)

같은 타입(인터페이스)으로 여러 구현체를 다룰 수 있는 능력

#### 왜 다형성이 필요한가?

```
문제 상황: 결제 수단이 추가될 때마다 if-else가 늘어남
→ if (type == "CARD") { ... }
→ else if (type == "CASH") { ... }
→ else if (type == "KAKAO_PAY") { ... }  // 계속 추가...
→ OCP(개방-폐쇄 원칙) 위반!
```

#### 다형성으로 해결

```java
// 인터페이스 정의
public interface PaymentProcessor {
    void pay(long amount);
    void refund(long amount);
}

// 각 결제 수단별 구현
public class CardPayment implements PaymentProcessor {
    @Override
    public void pay(long amount) {
        System.out.println("카드로 " + amount + "원 결제");
    }

    @Override
    public void refund(long amount) {
        System.out.println("카드로 " + amount + "원 환불");
    }
}

public class KakaoPayment implements PaymentProcessor {
    @Override
    public void pay(long amount) {
        System.out.println("카카오페이로 " + amount + "원 결제");
    }

    @Override
    public void refund(long amount) {
        System.out.println("카카오페이로 " + amount + "원 환불");
    }
}

// 사용: 어떤 결제 수단이든 동일하게 처리
public class PaymentService {
    public void processPayment(PaymentProcessor processor, long amount) {
        processor.pay(amount);  // 구현체가 무엇이든 상관없음
    }
}
```

**왜 이렇게 하는가?**
1. **확장에 열림**: 새 결제 수단 추가 시 새 클래스만 만들면 됨
2. **수정에 닫힘**: 기존 코드(PaymentService) 수정 불필요
3. **테스트 용이**: Mock 객체로 쉽게 교체 가능

#### 다형성의 종류

| 종류 | 설명 | 예시 |
|------|------|------|
| 서브타입 다형성 | 부모 타입으로 자식 객체 참조 | `Animal a = new Dog()` |
| 매개변수 다형성 | 제네릭으로 타입 파라미터화 | `List<String>`, `List<Integer>` |
| 메서드 오버로딩 | 같은 이름, 다른 파라미터 | `print(int)`, `print(String)` |
| 메서드 오버라이딩 | 부모 메서드 재정의 | `@Override` |

---

### 4. 추상화 (Abstraction)

복잡한 시스템에서 핵심 개념만 추출하여 단순화

#### 왜 추상화가 필요한가?

```
문제 상황: MySQL, PostgreSQL, Oracle 각각 다른 연결 코드 작성
→ 사용하는 쪽에서 DB 종류마다 다른 코드 필요
→ DB 변경 시 모든 코드 수정 필요
```

#### 추상화로 해결

```java
// 인터페이스: "무엇을 할 수 있는가" 정의 (계약)
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}

// 구현체 1: MySQL
public class MySQLUserRepository implements Repository<User, Long> {
    @Override
    public User findById(Long id) {
        // MySQL 전용 쿼리
    }
    // ...
}

// 구현체 2: MongoDB
public class MongoUserRepository implements Repository<User, Long> {
    @Override
    public User findById(Long id) {
        // MongoDB 전용 쿼리
    }
    // ...
}

// 사용: DB 종류를 몰라도 됨
public class UserService {
    private final Repository<User, Long> repository;

    public UserService(Repository<User, Long> repository) {
        this.repository = repository;  // 어떤 구현체든 OK
    }

    public User getUser(Long id) {
        return repository.findById(id);  // DB 종류 상관없이 동일 코드
    }
}
```

#### 추상 클래스 vs 인터페이스

| 구분 | 추상 클래스 | 인터페이스 |
|------|------------|-----------|
| 목적 | 공통 구현 + 일부 추상화 | 순수 계약 정의 |
| 상속 | 단일 상속 (extends) | 다중 구현 (implements) |
| 필드 | 인스턴스 변수 가능 | 상수(static final)만 |
| 메서드 | 구현 메서드 가능 | default, static만 구현 가능 |
| 생성자 | 있음 | 없음 |
| 관계 | "is-a" (종류) | "can-do" (능력) |

**선택 기준:**
- **인터페이스**: "~할 수 있다" (Comparable, Serializable, Runnable)
- **추상 클래스**: "~의 일종이다" + 공통 구현 공유 (AbstractList, AbstractMap)

```java
// 인터페이스: 능력을 정의
public interface Flyable {
    void fly();
}

public interface Swimmable {
    void swim();
}

// 다중 구현 가능
public class Duck implements Flyable, Swimmable {
    @Override
    public void fly() { System.out.println("오리가 날아갑니다"); }

    @Override
    public void swim() { System.out.println("오리가 수영합니다"); }
}
```

---

## SOLID 원칙

### SRP (Single Responsibility Principle) - 단일 책임 원칙

> 클래스는 하나의 책임만 가져야 한다. 변경의 이유가 하나뿐이어야 한다.

**권장 (O)**: 하나의 클래스는 하나의 역할만
**비권장 (X)**: 여러 책임을 한 클래스에 혼합

```java
// Bad: User 클래스가 3가지 책임을 가짐
public class User {
    public void saveToDatabase() { ... }   // 책임 1: 영속성
    public void sendEmail() { ... }        // 책임 2: 알림
    public void generateReport() { ... }   // 책임 3: 리포트
}

// Good: 책임별로 분리
public class User { /* 사용자 도메인 로직만 */ }
public class UserRepository { /* DB 저장 책임 */ }
public class EmailService { /* 이메일 발송 책임 */ }
public class UserReportGenerator { /* 리포트 생성 책임 */ }
```

**왜 분리하는가?**
- 변경 이유가 명확해짐 (이메일 로직 변경 → EmailService만 수정)
- 테스트가 쉬워짐 (각 클래스를 독립적으로 테스트)
- 재사용성 증가 (EmailService를 다른 곳에서도 사용)

**만약 지키지 않으면?**
- User 클래스가 비대해짐 (God Class)
- 한 부분 수정이 다른 기능에 영향
- 테스트 시 불필요한 의존성 설정 필요

---

### OCP (Open-Closed Principle) - 개방-폐쇄 원칙

> 확장에는 열려있고, 수정에는 닫혀있어야 한다.

**권장 (O)**: 새 기능은 새 클래스로 추가
**비권장 (X)**: 기존 코드에 if-else 추가

```java
// Bad: 새 할인 타입 추가 시 기존 코드 수정 필요
public class DiscountService {
    public long calculate(String type, long price) {
        if (type.equals("VIP")) return price * 80 / 100;
        else if (type.equals("GOLD")) return price * 90 / 100;
        else if (type.equals("SILVER")) return price * 95 / 100;  // 추가!
        // 새 타입마다 여기 수정... → OCP 위반
        return price;
    }
}

// Good: 새 할인 정책은 새 클래스로 추가 (기존 코드 수정 X)
public interface DiscountPolicy {
    long calculate(long price);
}

public class VipDiscount implements DiscountPolicy {
    @Override
    public long calculate(long price) { return price * 80 / 100; }
}

public class GoldDiscount implements DiscountPolicy {
    @Override
    public long calculate(long price) { return price * 90 / 100; }
}

// 새 정책 추가: 기존 코드 수정 없이 새 클래스만 추가
public class SilverDiscount implements DiscountPolicy {
    @Override
    public long calculate(long price) { return price * 95 / 100; }
}
```

**왜 이렇게 하는가?**
- 기존 코드 수정 없음 → 기존 기능 안정성 보장
- 새 기능 추가가 쉬움 → 새 클래스만 만들면 됨
- 테스트 범위 최소화 → 새 클래스만 테스트

---

### LSP (Liskov Substitution Principle) - 리스코프 치환 원칙

> 자식 클래스는 부모 클래스를 대체할 수 있어야 한다. 부모의 계약을 위반하면 안 된다.

**권장 (O)**: 자식이 부모의 기대 동작을 유지
**비권장 (X)**: 자식이 부모의 계약을 위반

```java
// Bad: Square가 Rectangle의 계약을 위반
public class Rectangle {
    protected int width, height;

    public void setWidth(int w) { this.width = w; }
    public void setHeight(int h) { this.height = h; }
    public int getArea() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(int w) {
        this.width = w;
        this.height = w;  // 정사각형이니까 높이도 변경 → 부모 계약 위반!
    }

    @Override
    public void setHeight(int h) {
        this.width = h;
        this.height = h;
    }
}

// 문제 상황
Rectangle rect = new Square();
rect.setWidth(5);
rect.setHeight(3);
// 기대: 넓이 = 5 * 3 = 15
// 실제: 넓이 = 3 * 3 = 9 (setHeight에서 width도 바뀜!)

// Good: 상속 대신 별도 구현
public interface Shape {
    int getArea();
}

public class Rectangle implements Shape {
    private final int width, height;
    // ...
}

public class Square implements Shape {
    private final int side;
    // ...
}
```

**왜 문제인가?**
- 부모 타입으로 사용할 때 예상과 다른 동작
- 다형성의 장점을 살릴 수 없음
- 버그 원인 파악이 어려움

---

### ISP (Interface Segregation Principle) - 인터페이스 분리 원칙

> 클라이언트가 사용하지 않는 메서드에 의존하면 안 된다. 인터페이스는 작게 분리하라.

**권장 (O)**: 역할별로 인터페이스 분리
**비권장 (X)**: 하나의 거대한 인터페이스

```java
// Bad: 거대한 인터페이스 (Fat Interface)
public interface Worker {
    void work();
    void eat();
    void sleep();
}

// 로봇은 eat, sleep이 필요 없는데 구현해야 함
public class Robot implements Worker {
    @Override public void work() { /* 일한다 */ }
    @Override public void eat() { /* 불필요 - 빈 구현 or 예외 */ }
    @Override public void sleep() { /* 불필요 */ }
}

// Good: 역할별로 인터페이스 분리
public interface Workable { void work(); }
public interface Eatable { void eat(); }
public interface Sleepable { void sleep(); }

public class Human implements Workable, Eatable, Sleepable {
    @Override public void work() { /* 일한다 */ }
    @Override public void eat() { /* 먹는다 */ }
    @Override public void sleep() { /* 잔다 */ }
}

public class Robot implements Workable {
    @Override public void work() { /* 일한다 */ }
    // 불필요한 메서드 구현 안 해도 됨!
}
```

**왜 분리하는가?**
- 불필요한 의존성 제거
- 구현 클래스가 필요한 메서드만 구현
- 인터페이스 변경 시 영향 범위 최소화

---

### DIP (Dependency Inversion Principle) - 의존성 역전 원칙

> 고수준 모듈이 저수준 모듈에 의존하면 안 된다. 둘 다 추상화에 의존해야 한다.

**권장 (O)**: 추상화(인터페이스)에 의존
**비권장 (X)**: 구체 클래스에 직접 의존

```java
// Bad: 고수준 모듈(OrderService)이 저수준 모듈(MySQLRepository)에 직접 의존
public class OrderService {
    private MySQLOrderRepository repository = new MySQLOrderRepository();
    // MySQL → PostgreSQL 변경 시 OrderService 코드 수정 필요!
}

// Good: 추상화(인터페이스)에 의존 + 외부에서 주입
public class OrderService {
    private final OrderRepository repository;  // 인터페이스에 의존

    // 생성자 주입: 외부에서 구현체 결정
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
}

// 사용 시
OrderService service = new OrderService(new MySQLOrderRepository());
// PostgreSQL로 변경 시 OrderService 수정 없이:
OrderService service = new OrderService(new PostgreSQLOrderRepository());
```

**왜 추상화에 의존하는가?**

```
Before (DIP 위반):
OrderService → MySQLOrderRepository (구체 클래스)
             → 변경 시 OrderService 수정 필요

After (DIP 준수):
OrderService → OrderRepository (인터페이스) ← MySQLOrderRepository
             → 구현체 교체해도 OrderService 수정 불필요
```

**만약 지키지 않으면?**
- DB 변경 시 서비스 계층까지 수정 필요
- 테스트 시 실제 DB 필요 (Mock 불가)
- 결합도가 높아 유연성 저하

---

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 코드 재사용성 증가 (상속, 조합) | 설계 복잡도 증가 |
| 유지보수 용이 (캡슐화, 모듈화) | 초기 개발 시간 증가 |
| 확장성 좋음 (다형성, OCP) | 과도한 추상화 위험 |
| 대규모 협업에 적합 | 성능 오버헤드 (가상 메서드 호출) |
| 현실 세계 모델링 자연스러움 | 모든 문제에 적합하지 않음 |

**OOP가 적합한 경우:**
- 대규모 애플리케이션
- 장기간 유지보수가 필요한 시스템
- 여러 개발자가 협업하는 프로젝트
- 도메인 모델이 복잡한 경우

**OOP가 과도할 수 있는 경우:**
- 간단한 스크립트, 일회성 코드
- 성능이 극도로 중요한 저수준 시스템
- 함수형 패러다임이 더 적합한 데이터 처리

---

## 면접 예상 질문

### Q: 캡슐화와 정보 은닉의 차이점은?

**A:** 캡슐화는 데이터와 메서드를 하나의 단위(클래스)로 묶는 것이고, 정보 은닉은 내부 구현을 외부에 숨기는 것입니다.

**왜 구분하는가?**
- 캡슐화가 더 넓은 개념: "묶는다" + "숨긴다"
- 정보 은닉은 캡슐화를 달성하기 위한 기법
- Java에서는 접근 제어자(private, protected, public)로 정보 은닉 구현

**만약 정보 은닉 없이 캡슐화만 하면?**
- 데이터와 메서드가 묶여있지만 외부에서 직접 접근 가능
- 데이터 무결성 보장 불가
- 캡슐화의 장점을 제대로 누릴 수 없음

---

### Q: 상속보다 조합(Composition)을 선호해야 하는 이유는?

**A:** 상속은 강한 결합을 만들고, 조합은 약한 결합을 만들기 때문입니다.

**구체적인 이유:**
1. **결합도**: 상속은 부모 변경 시 모든 자식에 영향. 조합은 내부 객체만 교체하면 됨
2. **유연성**: 상속은 컴파일 타임에 결정. 조합은 런타임에 동적 교체 가능
3. **다중 사용**: Java는 단일 상속만. 조합은 여러 객체를 필드로 가질 수 있음
4. **캡슐화**: 상속은 부모 구현이 자식에 노출. 조합은 내부 구현을 숨길 수 있음

**그럼 상속은 언제 쓰나요?**
- 명확한 "is-a" 관계일 때 (Dog is an Animal)
- 다형성이 필요할 때
- 프레임워크가 상속을 요구할 때 (ex: Android Activity)

---

### Q: 인터페이스와 추상 클래스 중 어떤 것을 선택해야 하나요?

**A:** "~할 수 있다"면 인터페이스, "~의 일종이다" + 공통 구현 공유면 추상 클래스입니다.

**선택 기준:**

| 상황 | 선택 | 이유 |
|------|------|------|
| 다중 구현 필요 | 인터페이스 | Java는 다중 상속 불가 |
| 공통 상태(필드) 공유 | 추상 클래스 | 인터페이스는 상수만 가능 |
| 공통 구현 공유 | 추상 클래스 | 코드 중복 방지 |
| 순수 계약 정의 | 인터페이스 | 구현 강제 없이 명세만 |

**실무 가이드:**
- 먼저 인터페이스로 설계
- 공통 구현이 필요하면 추상 클래스 추가 고려
- Java 8+ default 메서드로 인터페이스에서도 일부 구현 가능

---

### Q: SOLID 원칙 중 가장 중요한 것은?

**A:** 모두 중요하지만, 실무에서 가장 자주 위반되고 영향이 큰 것은 **SRP(단일 책임 원칙)**와 **DIP(의존성 역전 원칙)**입니다.

**왜 SRP가 중요한가?**
- God Class(모든 걸 다 하는 클래스)를 방지
- 변경의 영향 범위를 최소화
- 테스트 용이성 확보

**왜 DIP가 중요한가?**
- 구체 클래스 대신 인터페이스에 의존 → 교체 용이
- 테스트 시 Mock 객체 주입 가능
- 계층 간 결합도 감소

**실무에서 자주 보는 위반 사례:**
- SRP 위반: Service 클래스가 비즈니스 로직 + 이메일 발송 + 로깅까지 담당
- DIP 위반: Service가 `new MySQLRepository()`로 직접 생성

---

## 연관 문서

### 다음 학습 추천
- [클린 코드](./clean-code.md) - OOP 원칙을 실제 코드에 적용하는 방법
- [디자인 패턴](./design-pattern.md) - OOP 기반의 검증된 설계 패턴

### 관련 심화 주제
- [SOLID 원칙](./solid.md) - 객체지향 설계의 5가지 핵심 원칙 상세 설명
- [함수형 프로그래밍](./functional-programming.md) - OOP와 상호 보완적인 패러다임

### 실무 적용
- [API 설계](./api-design.md) - 객체지향 원칙을 API 설계에 적용
- [테스트](./test.md) - 객체지향 코드의 테스트 전략

## 참고 자료

- Clean Code - Robert C. Martin
- Effective Java 3rd Edition - Joshua Bloch
- Head First Design Patterns - Eric Freeman, Elisabeth Robson
- 객체지향의 사실과 오해 - 조영호
- 오브젝트 - 조영호
