# 디자인 패턴

> 소프트웨어 설계에서 자주 발생하는 문제에 대한 재사용 가능한 해결책

## 핵심 개념

- **디자인 패턴**: 특정 상황에서 반복적으로 발생하는 설계 문제의 검증된 해결책
- **GoF (Gang of Four)**: 디자인 패턴을 체계화한 4인의 저자, 23개 패턴 정의
- **생성 패턴**: 객체 생성 방식을 추상화하여 유연성 제공
- **구조 패턴**: 클래스/객체를 조합하여 더 큰 구조 형성
- **행위 패턴**: 객체 간 책임 분배와 알고리즘 캡슐화

## 쉽게 이해하기

**디자인 패턴**을 요리 레시피에 비유할 수 있습니다.

김치찌개를 처음 만드는 사람도 레시피를 따라하면 맛있게 만들 수 있습니다.
레시피는 수많은 요리사들이 시행착오를 거쳐 정립한 "검증된 방법"입니다.
디자인 패턴도 마찬가지로, 선배 개발자들이 경험으로 정립한 "검증된 설계 방법"입니다.

예를 들어:
- **Singleton**: "이 재료는 딱 하나만 써" (전역 상태 관리)
- **Factory**: "주문만 하면 요리사가 알아서 만들어줌" (객체 생성 위임)
- **Observer**: "새 메뉴 나오면 단골들한테 알림 보내기" (이벤트 구독)
- **Strategy**: "같은 재료로 한식/중식/양식 선택" (알고리즘 교체)

**왜 디자인 패턴을 배워야 하나요?**
- 바퀴를 재발명하지 않아도 됨 (검증된 해결책 재사용)
- 개발자 간 공통 어휘 제공 ("여기 Strategy 패턴 쓰자" → 즉시 이해)
- 유지보수하기 쉬운 코드 작성 가능

---

## 패턴 분류

| 분류 | 목적 | 대표 패턴 |
|------|------|----------|
| 생성 (Creational) | 객체 생성 방식 추상화 | Singleton, Factory, Builder, Prototype |
| 구조 (Structural) | 클래스/객체 조합 | Adapter, Decorator, Proxy, Facade |
| 행위 (Behavioral) | 객체 간 책임 분배 | Strategy, Observer, Template Method, State |

---

## 생성 패턴 (Creational Patterns)

### Singleton (싱글톤)

> 클래스의 인스턴스가 오직 하나만 존재하도록 보장하는 패턴

**언제 사용하나?**
- 설정 관리자, 로깅, 캐시, 커넥션 풀 (Connection Pool)
- 전역적으로 하나의 인스턴스만 필요한 경우

**왜 이 상황에서 사용하나?**

"하나의 인스턴스만 필요"한 이유:
- **리소스 효율**: DB 커넥션 풀을 여러 개 만들면 커넥션 낭비, 메모리 낭비
- **일관성**: 설정 관리자가 여러 개면 어떤 설정이 진짜인지 혼란
- **상태 공유**: 캐시가 여러 개면 데이터 동기화 문제 발생

**이점**:
- 인스턴스 생성 비용 절감 (한 번만 생성)
- 전역 접근점 제공 (어디서든 동일한 인스턴스 접근)
- 상태 일관성 보장 (단일 진실 공급원)

**사용하지 않으면?**
- 커넥션 풀이 여러 개 생성되어 DB 커넥션 고갈
- 설정값이 인스턴스마다 달라 예측 불가능한 동작
- 캐시 데이터 불일치로 잘못된 결과 반환

```java
public class DatabaseConnection {
    // volatile: 멀티스레드 환경에서 가시성 보장
    private static volatile DatabaseConnection instance;

    private DatabaseConnection() {
        // private 생성자로 외부 생성 차단
    }

    // DCL (Double-Checked Locking): 성능과 스레드 안전성 모두 확보
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
```

**왜 Double-Checked Locking인가?**
- 첫 번째 if: 이미 생성된 경우 synchronized 비용 회피
- synchronized: 동시 생성 방지
- 두 번째 if: 대기 중 다른 스레드가 생성했을 수 있음

**주의사항**

- **전역 상태이므로 테스트하기 어려움**
  - 왜 문제인가: Singleton은 애플리케이션 전체에서 하나의 인스턴스를 공유하므로, 테스트 A에서 변경한 상태가 테스트 B에 영향을 줌 (테스트 격리 실패)
  - 발생 상황: 단위 테스트 병렬 실행 시, 또는 테스트 순서에 따라 결과가 달라지는 Flaky Test 발생
  - 해결 방법: 의존성 주입(DI)으로 설계하여 테스트 시 Mock으로 교체 가능하게 구성

- **멀티스레드 환경에서 동기화 필요**
  - 왜 문제인가: `if (instance == null)` 체크와 `new Instance()` 사이에 다른 스레드가 끼어들면 인스턴스가 여러 개 생성됨 (Race Condition)
  - 발생 상황: 애플리케이션 시작 시 여러 스레드가 동시에 `getInstance()` 호출
  - 해결 방법: DCL + volatile, static inner class (Lazy Holder), enum 방식 중 선택

---

### Factory Method (팩토리 메서드)

> 객체 생성을 서브클래스에 위임하여 생성 로직을 캡슐화하는 패턴

**언제 사용하나?**
- 생성할 객체의 타입이 런타임에 결정될 때
- 객체 생성 로직이 복잡하거나 변경 가능성이 있을 때

**왜 이 상황에서 사용하나?**

"런타임에 타입 결정"이 필요한 이유:
- **유연성**: 사용자 선택(카드/카카오페이)에 따라 다른 객체 필요
- **확장성**: 새 결제 수단 추가 시 클라이언트 코드 수정 없이 팩토리만 수정
- **캡슐화**: 복잡한 생성 로직(의존성 주입, 초기화 등)을 한 곳에서 관리

**이점**:
- 클라이언트가 구체 클래스를 몰라도 됨 → 결합도 (coupling) 감소
- 새 타입 추가 시 OCP (개방-폐쇄 원칙) 준수
- 생성 로직 변경 시 영향 범위 최소화

**사용하지 않으면?**
- 클라이언트 코드에 `new ConcreteClass()`가 흩어져 있음
- 새 타입 추가 시 모든 생성 지점을 찾아 수정해야 함
- 생성 로직 변경 시 여러 곳 동시 수정 필요 → 버그 위험

```java
// 제품 인터페이스
public interface Notification {
    void send(String message);
}

// 구체적인 제품들
public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("이메일 발송: " + message);
    }
}

public class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS 발송: " + message);
    }
}

// 팩토리
public class NotificationFactory {

    public static Notification create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// 사용
Notification notification = NotificationFactory.create("email");
notification.send("안녕하세요!");
```

**왜 Factory를 사용하나?**
- 클라이언트가 구체 클래스를 몰라도 됨 → 결합도 (coupling) 감소
- 새로운 타입 추가 시 팩토리만 수정 → OCP (개방-폐쇄 원칙) 준수
- 생성 로직 중앙 집중화 → 유지보수 용이

---

### Builder (빌더)

> 복잡한 객체를 단계별로 생성하며, 생성 과정과 표현을 분리하는 패턴

**언제 사용하나?**
- 생성자 파라미터가 많을 때 (4개 이상)
- 선택적 파라미터가 많을 때
- 불변 객체 (Immutable Object)를 만들 때

**왜 이 상황에서 사용하나?**

"파라미터가 많을 때" Builder가 필요한 이유:
- **가독성**: `new User("홍길동", null, 25, null, "서울")` → 각 값이 뭔지 모름
- **안전성**: 파라미터 순서 착각으로 인한 버그 방지
- **유연성**: 필수/선택 파라미터를 명확히 구분 가능

**이점**:
- 메서드 체이닝으로 가독성 높은 객체 생성
- 불변 객체를 안전하게 생성 (생성 완료 후 변경 불가)
- `build()` 시점에 유효성 검사 가능

**사용하지 않으면?**
- 점층적 생성자: `User(name)`, `User(name, email)`, `User(name, email, age)`... 생성자 폭발
- JavaBeans (setter): 객체가 완전히 생성되기 전 불완전한 상태 노출, 불변 객체 불가
- 파라미터 순서 실수로 런타임 버그 발생

```java
public class User {
    private final String name;      // 필수
    private final String email;     // 필수
    private final int age;          // 선택
    private final String phone;     // 선택
    private final String address;   // 선택

    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    public static class Builder {
        // 필수 파라미터
        private final String name;
        private final String email;

        // 선택 파라미터 - 기본값
        private int age = 0;
        private String phone = "";
        private String address = "";

        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder age(int age) {
            this.age = age;
            return this;  // 메서드 체이닝
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

// 사용 - 가독성 높은 객체 생성
User user = new User.Builder("홍길동", "hong@example.com")
        .age(25)
        .phone("010-1234-5678")
        .build();
```

**왜 Builder를 사용하나?**

| 방식 | 문제점 |
|------|--------|
| 점층적 생성자 | 파라미터 순서 혼동, 가독성 저하 |
| JavaBeans (setter) | 객체 일관성 깨짐, 불변 객체 불가 |
| Builder | 가독성 좋음, 불변 객체 가능, 유효성 검사 가능 |

---

### Prototype (프로토타입)

> 기존 객체를 복제하여 새 객체를 생성하는 패턴

**언제 사용하나?**
- 객체 생성 비용이 클 때 (DB 조회, 네트워크 요청 등)
- 비슷한 객체를 많이 만들어야 할 때

**왜 이 상황에서 사용하나?**

"생성 비용이 클 때" 복제가 유리한 이유:
- **성능**: DB 조회 100ms vs 메모리 복사 0.1ms → 1000배 차이
- **네트워크 비용**: 외부 API 호출 없이 로컬에서 복제
- **일관성**: 동일한 초기 상태의 객체를 빠르게 여러 개 생성

**이점**:
- 복잡한 초기화 과정을 반복하지 않음
- 런타임에 동적으로 객체 타입 결정 가능
- 상속 계층 없이 객체 생성 가능

**사용하지 않으면?**
- 동일한 DB 조회를 객체 생성할 때마다 반복 → 성능 저하
- 게임에서 몬스터 1000마리 생성 시 각각 초기화 → 로딩 지연
- 네트워크 요청 반복으로 외부 API 호출 제한 초과 위험

```java
public class GameCharacter implements Cloneable {
    private String name;
    private int level;
    private List<String> skills;

    @Override
    public GameCharacter clone() {
        try {
            GameCharacter cloned = (GameCharacter) super.clone();
            // 깊은 복사 (Deep Copy): 참조 타입은 새로 생성
            cloned.skills = new ArrayList<>(this.skills);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// 사용
GameCharacter template = new GameCharacter("전사", 1, List.of("베기"));
GameCharacter warrior1 = template.clone();
GameCharacter warrior2 = template.clone();
```

**얕은 복사 vs 깊은 복사**

| 구분 | 얕은 복사 (Shallow Copy) | 깊은 복사 (Deep Copy) |
|------|------------------------|---------------------|
| 동작 | 참조값만 복사 | 참조 객체도 새로 생성 |
| 결과 | 원본과 복사본이 같은 객체 참조 | 완전히 독립된 복사본 |
| 문제 | 복사본 수정 시 원본도 변경됨 | 없음 (독립적) |

**왜 깊은 복사가 필요한가?**
- 얕은 복사 시 `cloned.skills.add("스킬")` 하면 원본의 skills도 변경됨
- **왜?** 둘이 같은 List 객체를 참조하기 때문
- 따라서 참조 타입 필드는 `new ArrayList<>(this.skills)`처럼 새로 생성해야 함

---

## 구조 패턴 (Structural Patterns)

### Adapter (어댑터)

> 호환되지 않는 인터페이스를 연결해주는 패턴

**언제 사용하나?**
- 기존 클래스를 수정 없이 다른 인터페이스와 함께 사용할 때
- 외부 라이브러리를 내부 인터페이스에 맞출 때

**왜 이 상황에서 사용하나?**

"기존 클래스 수정 없이"가 중요한 이유:
- **안정성**: 이미 검증된 코드를 수정하면 새로운 버그 유발 위험
- **불가능**: 외부 라이브러리, 레거시 시스템은 수정 자체가 불가능
- **OCP 준수**: 확장에는 열려있고, 수정에는 닫혀있어야 함

**이점**:
- 기존 코드 변경 없이 새로운 인터페이스와 통합
- 클라이언트 코드가 구체적인 구현에 의존하지 않음
- 외부 의존성 교체 시 어댑터만 수정하면 됨 (영향 범위 최소화)

**사용하지 않으면?**
- 인터페이스가 맞지 않아 기존 클래스를 직접 수정 → 버그 위험
- 외부 라이브러리에 직접 의존 → 라이브러리 변경 시 전체 코드 수정
- PG사 변경 시 결제 로직 전체 재작성 필요

```java
// 기존 시스템 - 110V만 지원
public interface KoreanPlug {
    void connect110V();
}

// 새로운 기기 - 220V 필요
public class EuropeanDevice {
    public void connect220V() {
        System.out.println("220V로 연결됨");
    }
}

// 어댑터 - 110V 인터페이스로 220V 기기 사용
public class VoltageAdapter implements KoreanPlug {
    private final EuropeanDevice device;

    public VoltageAdapter(EuropeanDevice device) {
        this.device = device;
    }

    @Override
    public void connect110V() {
        // 변환 로직
        System.out.println("전압 변환 중...");
        device.connect220V();
    }
}

// 사용
KoreanPlug plug = new VoltageAdapter(new EuropeanDevice());
plug.connect110V();  // 기존 인터페이스로 새 기기 사용
```

**실무 예시**
- `Arrays.asList()`: 배열을 List 인터페이스로 어댑팅
- `InputStreamReader`: InputStream을 Reader로 어댑팅
- 외부 결제 API를 내부 PaymentService 인터페이스에 맞추기

---

### Decorator (데코레이터)

> 객체에 동적으로 새로운 기능을 추가하는 패턴

**언제 사용하나?**
- 상속 없이 기능을 확장하고 싶을 때
- 기능 조합이 다양할 때 (상속으로는 클래스 폭발)

**왜 이 상황에서 사용하나?**

"상속 대신 Decorator"가 필요한 이유:
- **조합 폭발 방지**: 에스프레소+우유, 에스프레소+샷, 에스프레소+우유+샷... 상속으로는 클래스가 기하급수적 증가
- **런타임 유연성**: 상속은 컴파일 타임에 고정, Decorator는 런타임에 조합 가능
- **단일 책임**: 각 데코레이터가 하나의 기능만 담당 (SRP 준수)

**이점**:
- 기존 코드 수정 없이 기능 추가 (OCP 준수)
- 기능을 자유롭게 조합/제거 가능
- 작은 단위의 클래스로 유지보수 용이

**사용하지 않으면?**
- 커피 종류: Espresso, EspressoWithMilk, EspressoWithShot, EspressoWithMilkAndShot, EspressoWithMilkAndShotAndWhip... (클래스 폭발)
- 새 옵션 추가 시 기존 모든 조합 클래스에 영향
- 코드 중복 심화

```java
// 기본 인터페이스
public interface Coffee {
    String getDescription();
    int getCost();
}

// 기본 구현
public class Espresso implements Coffee {
    @Override
    public String getDescription() { return "에스프레소"; }

    @Override
    public int getCost() { return 3000; }
}

// 데코레이터 베이스
public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// 구체적인 데코레이터들
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + 우유";
    }

    @Override
    public int getCost() {
        return coffee.getCost() + 500;
    }
}

public class ShotDecorator extends CoffeeDecorator {
    public ShotDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + " + 샷추가";
    }

    @Override
    public int getCost() {
        return coffee.getCost() + 500;
    }
}

// 사용 - 기능 조합
Coffee order = new ShotDecorator(new MilkDecorator(new Espresso()));
System.out.println(order.getDescription());  // 에스프레소 + 우유 + 샷추가
System.out.println(order.getCost());         // 4000
```

**왜 상속 대신 Decorator인가?**
- 상속: 에스프레소, 우유에스프레소, 샷에스프레소, 우유샷에스프레소... (클래스 폭발)
- Decorator: 기본 클래스 + 데코레이터 조합 (유연한 확장)

**실무 예시**
- Java I/O: `BufferedInputStream(new FileInputStream(file))`
- Spring: `@Transactional`, `@Cacheable` (AOP 기반)

---

### Proxy (프록시)

> 실제 객체에 대한 대리자를 제공하여 접근을 제어하는 패턴

**언제 사용하나?**
- 객체 생성/접근 비용이 클 때 (지연 로딩)
- 접근 권한을 제어해야 할 때 (보호 프록시)
- 원격 객체를 로컬처럼 사용하고 싶을 때 (원격 프록시)

**왜 이 상황에서 사용하나?**

"지연 로딩"이 필요한 이유:
- **메모리 절약**: 사용하지 않을 수도 있는 대용량 객체를 미리 로딩하면 메모리 낭비
- **초기 로딩 속도**: 모든 객체를 미리 로딩하면 애플리케이션 시작이 느려짐
- **필요 시점 로딩**: 실제 사용 시점에 로딩하면 불필요한 리소스 사용 방지

**이점**:
- 실제 객체 생성을 필요한 시점까지 지연 (메모리, 시간 절약)
- 실제 객체에 대한 접근 전/후 처리 가능 (로깅, 캐싱, 권한 체크)
- 클라이언트는 프록시와 실제 객체를 구분하지 않음 (투명성)

**사용하지 않으면?**
- 게시글 목록 조회 시 각 게시글의 이미지를 모두 미리 로딩 → 느린 초기 로딩
- 모든 연관 엔티티를 즉시 로딩 → 메모리 부족 (N+1 문제 포함)
- 권한 체크 로직이 비즈니스 로직에 섞임 → 관심사 분리 실패

**프록시 종류**

| 종류 | 목적 | 예시 |
|------|------|------|
| 가상 프록시 (Virtual) | 지연 로딩 (Lazy Loading) | 이미지 로딩 |
| 보호 프록시 (Protection) | 접근 권한 제어 | 인증/인가 |
| 원격 프록시 (Remote) | 원격 객체 접근 | RPC, RMI |
| 캐싱 프록시 | 결과 캐싱 | DB 쿼리 캐시 |

```java
public interface Image {
    void display();
}

// 실제 객체 - 생성 비용이 큼
public class RealImage implements Image {
    private final String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();  // 무거운 작업
    }

    private void loadFromDisk() {
        System.out.println("디스크에서 로딩: " + filename);
    }

    @Override
    public void display() {
        System.out.println("표시: " + filename);
    }
}

// 가상 프록시 - 지연 로딩
public class ImageProxy implements Image {
    private final String filename;
    private RealImage realImage;  // 실제 객체 참조

    public ImageProxy(String filename) {
        this.filename = filename;
        // 아직 RealImage 생성 안 함
    }

    @Override
    public void display() {
        // 실제 필요할 때 생성 (Lazy Loading)
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}

// 사용
Image image = new ImageProxy("photo.jpg");  // 아직 로딩 안 됨
// ... 다른 작업 ...
image.display();  // 이때 로딩됨
```

**실무 예시**
- JPA `@ManyToOne(fetch = LAZY)`: 지연 로딩 프록시
- Spring AOP: 트랜잭션, 보안 프록시
- `Collections.unmodifiableList()`: 보호 프록시

---

### Facade (퍼사드)

> 복잡한 서브시스템에 대한 단순한 인터페이스를 제공하는 패턴

**언제 사용하나?**
- 복잡한 시스템을 단순화하고 싶을 때
- 서브시스템 간 결합도를 줄이고 싶을 때

**왜 이 상황에서 사용하나?**

"복잡한 시스템 단순화"가 필요한 이유:
- **학습 비용**: 클라이언트가 여러 서브시스템의 API를 모두 알아야 하면 진입 장벽 높음
- **결합도**: 클라이언트가 서브시스템에 직접 의존하면 변경 영향 범위 증가
- **일관성**: 동일한 작업을 여러 곳에서 다르게 호출하면 버그 발생 가능

**이점**:
- 클라이언트는 단일 진입점만 알면 됨 (복잡도 감소)
- 서브시스템 변경 시 Facade만 수정 (변경 영향 최소화)
- 공통 작업 흐름을 한 곳에서 관리 (일관성 보장)

**사용하지 않으면?**
- 주문 처리: 재고확인 → 결제 → 배송 → 알림을 클라이언트가 직접 호출
- 각 서브시스템 API 변경 시 모든 클라이언트 수정 필요
- 작업 순서 실수로 결제 후 재고 부족 발견 같은 버그 발생

```java
// 복잡한 서브시스템들
public class Inventory {
    public boolean checkStock(String item) { /* ... */ return true; }
}

public class Payment {
    public boolean process(int amount) { /* ... */ return true; }
}

public class Shipping {
    public void ship(String address) { /* ... */ }
}

public class Notification {
    public void sendEmail(String message) { /* ... */ }
}

// 퍼사드 - 단순한 인터페이스 제공
public class OrderFacade {
    private final Inventory inventory;
    private final Payment payment;
    private final Shipping shipping;
    private final Notification notification;

    public OrderFacade() {
        this.inventory = new Inventory();
        this.payment = new Payment();
        this.shipping = new Shipping();
        this.notification = new Notification();
    }

    // 복잡한 주문 과정을 하나의 메서드로 단순화
    public boolean placeOrder(String item, int amount, String address) {
        if (!inventory.checkStock(item)) {
            return false;
        }
        if (!payment.process(amount)) {
            return false;
        }
        shipping.ship(address);
        notification.sendEmail("주문이 완료되었습니다.");
        return true;
    }
}

// 사용 - 클라이언트는 복잡한 내부를 몰라도 됨
OrderFacade order = new OrderFacade();
order.placeOrder("노트북", 1500000, "서울시 강남구");
```

**Facade vs Service Layer**

| 구분 | Facade | Service Layer |
|------|--------|---------------|
| 목적 | 기존 복잡한 시스템을 단순화 | 비즈니스 로직 자체를 담당 |
| 대상 | 이미 존재하는 서브시스템들 | 새로 설계하는 비즈니스 로직 |
| 위치 | 서브시스템 위의 래퍼 | 아키텍처의 핵심 계층 |

**왜 비슷해 보이는가?**
- 둘 다 여러 작업을 하나의 메서드로 묶음
- 둘 다 클라이언트에게 단순한 인터페이스 제공

**차이점은?**
- Facade: "이미 있는 복잡한 것을 감싼다" (외부 라이브러리, 레거시 시스템)
- Service: "비즈니스 로직 자체를 구현한다" (도메인 규칙, 트랜잭션 관리)

---

## 행위 패턴 (Behavioral Patterns)

### Strategy (전략)

> 알고리즘을 캡슐화하여 런타임에 교체 가능하게 하는 패턴

**언제 사용하나?**
- 동일한 문제를 여러 알고리즘으로 해결할 수 있을 때
- if-else 분기가 많아질 때

**왜 이 상황에서 사용하나?**

"if-else 분기가 많을 때" Strategy가 필요한 이유:
- **OCP 위반**: 새 알고리즘 추가 시 기존 코드(if-else)를 수정해야 함
- **테스트 어려움**: 거대한 if-else 블록은 단위 테스트 작성이 어려움
- **코드 복잡도**: 분기가 늘어날수록 가독성 저하, 버그 발생 확률 증가

**이점**:
- 알고리즘별로 클래스 분리 → 단일 책임 원칙 (SRP)
- 새 알고리즘 추가 시 기존 코드 수정 없음 (OCP)
- 런타임에 알고리즘 교체 가능 (유연성)
- 각 알고리즘을 독립적으로 테스트 가능

**사용하지 않으면?**
- 결제 수단 추가할 때마다 if-else 분기 추가 → 코드 비대화
- 카드 결제 로직 수정 시 다른 결제 로직에 영향 줄 위험
- 결제 로직 테스트 시 모든 분기를 함께 테스트해야 함

```java
// 전략 인터페이스
public interface PaymentStrategy {
    void pay(int amount);
}

// 구체적인 전략들
public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + "원을 신용카드로 결제");
    }
}

public class KakaoPayPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println(amount + "원을 카카오페이로 결제");
    }
}

// 컨텍스트 - 전략을 사용하는 클래스
public class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}

// 사용 - 런타임에 전략 교체
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment("1234-5678"));
cart.checkout(50000);

cart.setPaymentStrategy(new KakaoPayPayment());
cart.checkout(30000);
```

**Strategy로 if-else 제거**

```java
// Before - if-else 지옥
public void pay(String type, int amount) {
    if (type.equals("card")) {
        // 카드 결제 로직
    } else if (type.equals("kakao")) {
        // 카카오페이 로직
    } else if (type.equals("naver")) {
        // 네이버페이 로직
    }
    // 새 결제 수단 추가할 때마다 분기 추가... OCP 위반
}

// After - Strategy 패턴
public void pay(PaymentStrategy strategy, int amount) {
    strategy.pay(amount);  // 새 결제 수단은 새 Strategy 클래스로
}
```

---

### Observer (옵저버)

> 객체 상태 변경 시 의존 객체들에게 자동으로 알림을 보내는 패턴

**언제 사용하나?**
- 이벤트 기반 시스템
- 일대다 (one-to-many) 의존 관계

**왜 이 상황에서 사용하나?**

"이벤트 기반 시스템"에서 Observer가 필요한 이유:
- **느슨한 결합**: 발행자가 구독자를 직접 알 필요 없음 → 의존성 감소
- **동적 구독**: 런타임에 구독자 추가/제거 가능
- **확장성**: 새 구독자 추가 시 발행자 코드 수정 불필요

**이점**:
- 발행자-구독자 간 결합도 최소화
- 구독자 추가가 기존 코드에 영향 없음 (OCP)
- 동일한 이벤트에 여러 반응을 독립적으로 구현

**사용하지 않으면?**
- 뉴스 발행 시 이메일 발송, 푸시 알림, SMS를 발행자가 직접 호출
- 알림 채널 추가 시 발행자 코드 수정 필요
- 발행자가 모든 알림 로직에 의존 → 단일 책임 위반

```java
// 옵저버 인터페이스
public interface Observer {
    void update(String message);
}

// Subject (발행자)
public class NewsPublisher {
    private final List<Observer> subscribers = new ArrayList<>();

    public void subscribe(Observer observer) {
        subscribers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        subscribers.remove(observer);
    }

    public void publish(String news) {
        System.out.println("새 뉴스 발행: " + news);
        notifySubscribers(news);
    }

    private void notifySubscribers(String news) {
        for (Observer subscriber : subscribers) {
            subscriber.update(news);
        }
    }
}

// 구체적인 옵저버
public class EmailSubscriber implements Observer {
    private final String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println(email + "로 이메일 발송: " + message);
    }
}

public class AppSubscriber implements Observer {
    @Override
    public void update(String message) {
        System.out.println("앱 푸시 알림: " + message);
    }
}

// 사용
NewsPublisher publisher = new NewsPublisher();
publisher.subscribe(new EmailSubscriber("user@example.com"));
publisher.subscribe(new AppSubscriber());

publisher.publish("속보: 디자인 패턴 완전 정복!");
// 출력:
// 새 뉴스 발행: 속보: 디자인 패턴 완전 정복!
// user@example.com로 이메일 발송: 속보: ...
// 앱 푸시 알림: 속보: ...
```

**실무 예시**
- Spring `ApplicationEventPublisher`: 이벤트 발행/구독
- JavaScript `addEventListener`: DOM 이벤트
- React 상태 관리: Redux, MobX

---

### Template Method (템플릿 메서드)

> 알고리즘의 골격을 정의하고, 일부 단계를 서브클래스에서 구현하게 하는 패턴

**언제 사용하나?**
- 알고리즘 구조는 같지만 세부 구현이 다를 때
- 코드 중복을 제거하면서 확장 포인트를 제공하고 싶을 때

**왜 이 상황에서 사용하나?**

"알고리즘 구조는 같지만 세부 구현이 다를 때" Template Method가 필요한 이유:
- **코드 중복 제거**: 데이터 읽기 → 처리 → 쓰기 흐름이 같다면 골격은 재사용
- **일관성 보장**: 알고리즘 순서가 부모에서 고정되어 실수 방지
- **확장성**: 변하는 부분만 서브클래스에서 구현 (헐리우드 원칙: "Don't call us, we'll call you")

**이점**:
- 공통 로직 중복 제거 (DRY 원칙)
- 알고리즘 골격을 한 곳에서 관리 → 변경 용이
- 서브클래스는 변하는 부분에만 집중

**사용하지 않으면?**
- CSV/JSON/XML 파서가 각각 읽기→파싱→쓰기 로직을 중복 구현
- 알고리즘 순서 변경 시 모든 파서 수정 필요
- 파서 추가 시 동일한 코드 복붙

```java
// 템플릿 클래스
public abstract class DataProcessor {

    // 템플릿 메서드 - 알고리즘 골격 정의
    public final void process() {
        readData();
        processData();
        writeData();
    }

    // 공통 구현
    private void readData() {
        System.out.println("데이터 읽기");
    }

    // 추상 메서드 - 서브클래스에서 구현
    protected abstract void processData();

    // Hook 메서드 - 선택적 오버라이드
    protected void writeData() {
        System.out.println("데이터 쓰기 (기본)");
    }
}

// 구체적인 구현
public class CsvProcessor extends DataProcessor {
    @Override
    protected void processData() {
        System.out.println("CSV 형식으로 처리");
    }
}

public class JsonProcessor extends DataProcessor {
    @Override
    protected void processData() {
        System.out.println("JSON 형식으로 처리");
    }

    @Override
    protected void writeData() {
        System.out.println("JSON 파일로 저장");
    }
}

// 사용
DataProcessor csv = new CsvProcessor();
csv.process();
// 출력: 데이터 읽기 → CSV 형식으로 처리 → 데이터 쓰기 (기본)

DataProcessor json = new JsonProcessor();
json.process();
// 출력: 데이터 읽기 → JSON 형식으로 처리 → JSON 파일로 저장
```

**Template Method vs Strategy**

| 구분 | Template Method | Strategy |
|------|----------------|----------|
| 방식 | 상속 기반 | 구성 (composition) 기반 |
| 결정 시점 | 컴파일 타임에 결정 | 런타임에 교체 가능 |
| 변경 범위 | 알고리즘 일부 변경 | 알고리즘 전체 교체 |

**언제 어떤 것을 선택하나?**

- **Template Method 선택**: 알고리즘 골격은 고정하고, 특정 단계만 변경하고 싶을 때
  - 예: 데이터 파싱 (읽기→파싱→쓰기 순서 고정, 파싱 방법만 다름)
- **Strategy 선택**: 전혀 다른 알고리즘으로 통째로 교체하고 싶을 때
  - 예: 결제 방법 (카드 로직과 계좌이체 로직은 완전히 다름)

**왜 상속보다 구성이 유연한가?**
- 상속: 클래스 정의 시점에 부모 결정 → 변경 불가
- 구성: 객체 생성 후에도 `setStrategy()`로 교체 가능

---

### State (상태)

> 객체의 상태에 따라 행동을 변경하는 패턴

**언제 사용하나?**
- 객체의 행동이 상태에 따라 달라질 때
- 상태 전이 로직이 복잡할 때

**왜 이 상황에서 사용하나?**

"상태에 따라 행동이 달라질 때" State 패턴이 필요한 이유:
- **복잡한 조건문 제거**: 상태별 if-else가 모든 메서드에 반복되면 코드 복잡도 폭발
- **상태 전이 명확화**: 각 상태가 다음 상태로의 전이를 스스로 관리
- **상태별 책임 분리**: 각 상태 클래스가 자신의 행동만 담당 (SRP)

**이점**:
- 상태별 행동이 클래스로 캡슐화되어 가독성 향상
- 새 상태 추가 시 기존 코드 수정 최소화 (OCP)
- 상태 전이 로직이 분산되지 않고 각 상태에서 관리

**사용하지 않으면?**
- 주문 상태(주문완료/배송중/배송완료)에 따른 분기가 모든 메서드에 중복
- 새 상태(환불중) 추가 시 모든 메서드의 if-else 수정 필요
- 상태 전이 조건이 여러 곳에 흩어져 버그 발생 위험

```java
// 상태 인터페이스
public interface OrderState {
    void next(Order order);
    void prev(Order order);
    String getStatus();
}

// 구체적인 상태들
public class OrderedState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new ShippedState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("첫 상태입니다.");
    }

    @Override
    public String getStatus() {
        return "주문완료";
    }
}

public class ShippedState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new DeliveredState());
    }

    @Override
    public void prev(Order order) {
        order.setState(new OrderedState());
    }

    @Override
    public String getStatus() {
        return "배송중";
    }
}

public class DeliveredState implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("마지막 상태입니다.");
    }

    @Override
    public void prev(Order order) {
        order.setState(new ShippedState());
    }

    @Override
    public String getStatus() {
        return "배송완료";
    }
}

// 컨텍스트
public class Order {
    private OrderState state = new OrderedState();

    public void setState(OrderState state) {
        this.state = state;
    }

    public void nextState() {
        state.next(this);
    }

    public void printStatus() {
        System.out.println("현재 상태: " + state.getStatus());
    }
}

// 사용
Order order = new Order();
order.printStatus();  // 주문완료
order.nextState();
order.printStatus();  // 배송중
order.nextState();
order.printStatus();  // 배송완료
```

**State로 if-else 제거**

```java
// Before
public String getStatus() {
    if (state == 1) return "주문완료";
    else if (state == 2) return "배송중";
    else if (state == 3) return "배송완료";
    // 상태 추가될 때마다...
}

// After - 각 상태가 자신의 행동을 캡슐화
order.getState().getStatus();
```

---

## 패턴 선택 가이드

| 문제 상황 | 추천 패턴 |
|----------|----------|
| 인스턴스가 하나만 필요 | Singleton |
| 객체 생성 로직 캡슐화 | Factory |
| 파라미터가 많은 객체 생성 | Builder |
| 호환되지 않는 인터페이스 연결 | Adapter |
| 동적으로 기능 추가 | Decorator |
| 객체 접근 제어, 지연 로딩 | Proxy |
| 복잡한 시스템 단순화 | Facade |
| 알고리즘 교체 가능하게 | Strategy |
| 상태 변경 알림 | Observer |
| 알고리즘 골격 정의 | Template Method |
| 상태에 따른 행동 변경 | State |

---

## 안티패턴 (Anti-pattern) 주의

> 안티패턴: 자주 사용되지만 실제로는 비효율적이거나 문제를 일으키는 패턴

| 안티패턴 | 문제점 | 왜 문제인가? | 대안 |
|----------|--------|-------------|------|
| God Class | 하나의 클래스가 너무 많은 책임 | 변경 시 영향 범위 예측 불가, 테스트 어려움, 재사용 불가 | 클래스 분리 (SRP) |
| Spaghetti Code | 구조 없이 얽힌 코드 | 흐름 파악 불가, 수정 시 사이드이펙트, 디버깅 지옥 | 패턴 적용, 리팩토링 |
| Golden Hammer | 익숙한 패턴만 남용 | 문제에 안 맞는 해결책 → 오히려 복잡도 증가 | 문제에 맞는 패턴 선택 |
| Copy-Paste | 코드 복붙 | 버그 수정 시 모든 복사본 찾아 수정 필요, 누락 위험 | Template Method, 추상화 |

---

## 면접 예상 질문

- **Q: 디자인 패턴을 왜 사용하나요?**
  - A: 첫째, 검증된 해결책을 재사용하여 시행착오를 줄일 수 있습니다. **왜냐하면** 패턴은 수많은 개발자들이 경험으로 검증한 "베스트 프랙티스"이기 때문입니다. 둘째, 개발자 간 공통 어휘를 제공합니다. "여기 Strategy 패턴을 쓰자"라고 하면 즉시 의도가 전달됩니다. 셋째, 유지보수하기 쉬운 코드를 작성할 수 있습니다. 패턴은 대부분 SOLID 원칙을 따르기 때문입니다.

- **Q: Strategy 패턴과 State 패턴의 차이점은 무엇인가요?**
  - A: 구조는 유사하지만 목적과 제어 주체가 다릅니다.
    - **Strategy**: 동일한 목적을 다른 방법으로 달성 (예: 결제 수단 선택)
      - 클라이언트가 전략을 선택하고 주입: `cart.setPaymentStrategy(new KakaoPay())`
      - 전략 간 관계 없음 (카드와 카카오페이는 서로 모름)
    - **State**: 상태에 따라 행동이 달라짐 (예: 주문 상태별 처리)
      - 객체 스스로 상태를 전이: `state.next(this)` → 내부에서 `setState(new ShippedState())`
      - 상태 간 전이 관계 존재 (주문완료 → 배송중 → 배송완료)
    - **왜 구분이 중요한가?** Strategy로 상태를 관리하면 클라이언트가 모든 상태 전이 규칙을 알아야 하므로 로직이 분산됨. State를 쓰면 각 상태가 자신의 전이 규칙을 캡슐화.

- **Q: Singleton 패턴의 문제점과 대안은 무엇인가요?**
  - A: 문제점은 세 가지입니다. 첫째, 전역 상태이므로 테스트하기 어렵습니다. **왜냐하면** 상태가 테스트 간에 공유되어 격리가 안 되기 때문입니다. 둘째, 의존성이 숨겨져 코드 파악이 어렵습니다. 셋째, 멀티스레드에서 동기화 문제가 발생할 수 있습니다. 대안으로는 **의존성 주입 (DI)**을 사용하여 스코프를 관리하는 방법이 있습니다. Spring의 `@Component`는 기본적으로 Singleton이지만 컨테이너가 관리하므로 테스트 시 Mock으로 교체할 수 있습니다.

---

## 참고 자료

- [GoF 디자인 패턴](https://en.wikipedia.org/wiki/Design_Patterns) - 원서
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- Head First Design Patterns - 에릭 프리먼
- [Java Design Patterns](https://java-design-patterns.com/)
