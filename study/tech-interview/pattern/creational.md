# 생성 패턴 (Creational Patterns)

## 핵심 정리

- 생성 패턴은 객체 생성 메커니즘을 다루는 디자인 패턴
- 객체 생성의 유연성을 높이고 코드 재사용성을 향상
- 주요 패턴: Singleton, Factory Method, Abstract Factory, Builder, Prototype

## Singleton Pattern

클래스의 인스턴스가 오직 하나만 생성되도록 보장하는 패턴

### 기본 구현 (Thread-Unsafe)

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### Thread-Safe 구현 방법

#### 1. synchronized 메서드

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

- 단점: 매 호출마다 동기화 오버헤드 발생

#### 2. Double-Checked Locking (DCL)

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

- `volatile` 키워드 필수 (메모리 가시성 보장)

#### 3. Bill Pugh Singleton (권장)

```java
public class Singleton {
    private Singleton() {}

    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

- Lazy Initialization + Thread-Safe
- 클래스 로딩 시점에 초기화 보장

#### 4. Enum Singleton (권장)

```java
public enum Singleton {
    INSTANCE;

    public void doSomething() {
        // 비즈니스 로직
    }
}
```

- 직렬화/역직렬화에도 싱글톤 보장
- 리플렉션 공격 방지

### 사용 사례

- 설정 관리 (Configuration)
- 로깅 (Logging)
- 캐시 (Cache)
- 스레드 풀 (Thread Pool)
- 데이터베이스 커넥션 풀

## Factory Method Pattern

객체 생성을 서브클래스에 위임하는 패턴

### 구조

```java
// 제품 인터페이스
public interface Product {
    void use();
}

// 구체적인 제품
public class ConcreteProductA implements Product {
    @Override
    public void use() {
        System.out.println("Using Product A");
    }
}

public class ConcreteProductB implements Product {
    @Override
    public void use() {
        System.out.println("Using Product B");
    }
}

// 팩토리 인터페이스
public abstract class Creator {
    public abstract Product createProduct();

    public void doSomething() {
        Product product = createProduct();
        product.use();
    }
}

// 구체적인 팩토리
public class ConcreteCreatorA extends Creator {
    @Override
    public Product createProduct() {
        return new ConcreteProductA();
    }
}

public class ConcreteCreatorB extends Creator {
    @Override
    public Product createProduct() {
        return new ConcreteProductB();
    }
}
```

### Simple Factory (정적 팩토리 메서드)

```java
public class ProductFactory {
    public static Product createProduct(String type) {
        return switch (type) {
            case "A" -> new ConcreteProductA();
            case "B" -> new ConcreteProductB();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
```

### 실제 활용 예시

```java
// Calendar
Calendar calendar = Calendar.getInstance();

// NumberFormat
NumberFormat format = NumberFormat.getInstance();

// Java Collection Framework
List<String> list = List.of("a", "b", "c");
Set<Integer> set = Set.of(1, 2, 3);

// Spring Framework
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
MyService service = context.getBean(MyService.class);
```

### Factory Method vs Simple Factory

| 특성 | Simple Factory | Factory Method |
|------|----------------|----------------|
| 구조 | 정적 메서드 | 추상 클래스/인터페이스 |
| 확장성 | 기존 코드 수정 필요 | 새 서브클래스 추가 |
| 복잡도 | 단순 | 복잡 |
| OCP | 위반 가능 | 준수 |

## 패턴 비교

| 패턴 | 목적 | 사용 시점 |
|------|------|-----------|
| Singleton | 인스턴스 하나 보장 | 전역 상태 관리 |
| Factory Method | 객체 생성 위임 | 생성할 객체 타입이 런타임에 결정 |
| Abstract Factory | 관련 객체 군 생성 | 제품군이 함께 사용되어야 할 때 |
| Builder | 복잡한 객체 단계별 생성 | 생성자 매개변수가 많을 때 |
| Prototype | 기존 객체 복제 | 객체 생성 비용이 클 때 |

## 면접 예상 질문

1. **Singleton 패턴의 문제점은 무엇인가요?**
   - 전역 상태로 인한 테스트 어려움
   - 의존성 숨김
   - 멀티스레드 환경에서 구현 복잡
   - 단일 책임 원칙 위반 가능

2. **Bill Pugh Singleton이 Thread-Safe한 이유는?**
   - JVM의 클래스 로딩 메커니즘 활용
   - 내부 클래스는 외부 클래스 로딩 시 초기화되지 않음
   - getInstance() 호출 시 SingletonHolder 클래스 로딩 → 동기화 보장

3. **Factory Method 패턴을 사용하는 이유는?**
   - 객체 생성 로직을 캡슐화
   - 클라이언트 코드와 구체적인 클래스 분리
   - 새로운 제품 추가 시 기존 코드 수정 최소화 (OCP 준수)
