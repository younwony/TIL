# 행동 패턴 (Behavioral Patterns)

## 핵심 정리

- 행동 패턴은 객체들 간의 상호작용과 책임 분배를 다루는 패턴
- 객체 간의 통신을 유연하게 하고 결합도를 낮춤
- 주요 패턴: Strategy, Template Method, Command, Observer, State, Iterator

## Strategy Pattern

알고리즘을 캡슐화하고 교체 가능하게 만드는 패턴

### 구조

```java
// 전략 인터페이스
public interface PaymentStrategy {
    void pay(int amount);
}

// 구체적인 전략들
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

public class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

// 컨텍스트
public class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}

// 사용
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment("1234-5678"));
cart.checkout(100);

cart.setPaymentStrategy(new PayPalPayment("user@email.com"));
cart.checkout(200);
```

### 람다를 활용한 간소화 (Java 8+)

```java
@FunctionalInterface
public interface PaymentStrategy {
    void pay(int amount);
}

// 사용
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(amount -> System.out.println("Paid " + amount));
cart.checkout(100);
```

### 실제 활용 예시

```java
// Java Comparator
List<String> list = Arrays.asList("c", "a", "b");
Collections.sort(list, Comparator.naturalOrder());
Collections.sort(list, Comparator.reverseOrder());
Collections.sort(list, (a, b) -> a.length() - b.length());

// Spring - Resource Loading
Resource resource = resourceLoader.getResource("classpath:file.txt");
Resource resource = resourceLoader.getResource("file:/path/to/file.txt");
```

## Template Method Pattern

알고리즘의 골격을 정의하고 세부 단계를 서브클래스에서 구현하는 패턴

### 구조

```java
// 추상 클래스 (템플릿)
public abstract class DataProcessor {

    // 템플릿 메서드 (final로 오버라이드 방지)
    public final void process() {
        readData();
        processData();
        writeData();
    }

    // 추상 메서드 (서브클래스에서 구현)
    protected abstract void readData();
    protected abstract void processData();

    // 훅 메서드 (선택적 오버라이드)
    protected void writeData() {
        System.out.println("Writing data to default output");
    }
}

// 구체적인 구현
public class CSVProcessor extends DataProcessor {
    @Override
    protected void readData() {
        System.out.println("Reading CSV data");
    }

    @Override
    protected void processData() {
        System.out.println("Processing CSV data");
    }
}

public class XMLProcessor extends DataProcessor {
    @Override
    protected void readData() {
        System.out.println("Reading XML data");
    }

    @Override
    protected void processData() {
        System.out.println("Processing XML data");
    }

    @Override
    protected void writeData() {
        System.out.println("Writing to XML file");
    }
}

// 사용
DataProcessor processor = new CSVProcessor();
processor.process();
```

### 실제 활용 예시

```java
// Java - AbstractList
public abstract class AbstractList<E> implements List<E> {
    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    abstract public E get(int index);
    abstract public int size();
}

// Spring - JdbcTemplate
jdbcTemplate.execute(new PreparedStatementCallback<Void>() {
    @Override
    public Void doInPreparedStatement(PreparedStatement ps) {
        // 사용자 정의 로직
        return null;
    }
});
```

## Command Pattern

요청을 객체로 캡슐화하여 매개변수화, 큐잉, 로깅, 취소 등을 지원하는 패턴

### 구조

```java
// 커맨드 인터페이스
public interface Command {
    void execute();
    void undo();
}

// 수신자 (Receiver)
public class Light {
    public void on() {
        System.out.println("Light is ON");
    }

    public void off() {
        System.out.println("Light is OFF");
    }
}

// 구체적인 커맨드
public class LightOnCommand implements Command {
    private final Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

public class LightOffCommand implements Command {
    private final Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}

// 호출자 (Invoker)
public class RemoteControl {
    private Command command;
    private final Deque<Command> history = new ArrayDeque<>();

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
        history.push(command);
    }

    public void pressUndo() {
        if (!history.isEmpty()) {
            Command lastCommand = history.pop();
            lastCommand.undo();
        }
    }
}

// 사용
Light light = new Light();
Command lightOn = new LightOnCommand(light);
Command lightOff = new LightOffCommand(light);

RemoteControl remote = new RemoteControl();
remote.setCommand(lightOn);
remote.pressButton();   // Light is ON
remote.pressUndo();     // Light is OFF
```

### 실제 활용 예시

```java
// Java - Runnable
Runnable task = () -> System.out.println("Task executed");
new Thread(task).start();

// Java - Callable
Callable<String> callable = () -> "Result";
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(callable);
```

## 패턴 비교

| 패턴 | 목적 | 특징 |
|------|------|------|
| Strategy | 알고리즘 교체 | 컴포지션으로 동적 변경 |
| Template Method | 알고리즘 골격 정의 | 상속으로 단계 구현 |
| Command | 요청 객체화 | 실행/취소, 큐잉, 로깅 |
| Observer | 상태 변화 통지 | 일대다 의존관계 |
| State | 상태에 따른 행동 변화 | 상태를 객체로 표현 |

## Strategy vs Template Method

| 특성 | Strategy | Template Method |
|------|----------|-----------------|
| 구현 방식 | 컴포지션 (위임) | 상속 |
| 유연성 | 런타임에 변경 가능 | 컴파일 타임에 결정 |
| 알고리즘 변경 | 전체 알고리즘 교체 | 알고리즘 일부만 변경 |
| 결합도 | 느슨함 | 강함 |

## 면접 예상 질문

1. **Strategy 패턴과 Template Method 패턴의 차이점은?**
   - Strategy: 컴포지션을 사용하여 알고리즘 전체를 교체
   - Template Method: 상속을 사용하여 알고리즘의 일부를 변경

2. **Command 패턴을 사용하는 이유는?**
   - 요청의 발신자와 수신자를 분리
   - 명령의 실행/취소/재실행 지원
   - 명령 큐잉, 로깅, 트랜잭션 구현 가능

3. **Strategy 패턴의 실제 사용 예는?**
   - Java Comparator: 정렬 전략을 객체로 전달
   - Spring Security: 인증 전략 (AuthenticationProvider)
   - 결제 시스템: 결제 방법에 따른 다른 처리
