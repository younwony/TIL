# 구조 패턴 (Structural Patterns)

## 핵심 정리

- 구조 패턴은 클래스와 객체를 조합하여 더 큰 구조를 만드는 패턴
- 인터페이스나 구현을 복합하여 새로운 기능 제공
- 주요 패턴: Adapter, Decorator, Facade, Composite, Bridge, Proxy, Flyweight

## Adapter Pattern

호환되지 않는 인터페이스를 연결하는 패턴

### 구조

```java
// 기존 인터페이스 (클라이언트가 기대하는 인터페이스)
public interface Target {
    void request();
}

// 적응 대상 (기존 클래스)
public class Adaptee {
    public void specificRequest() {
        System.out.println("Specific request");
    }
}

// 어댑터 (객체 어댑터)
public class Adapter implements Target {
    private final Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
    }
}

// 사용
Target target = new Adapter(new Adaptee());
target.request();
```

### 실제 활용 예시

```java
// Java I/O - InputStreamReader
InputStream is = new FileInputStream("file.txt");
Reader reader = new InputStreamReader(is); // InputStream → Reader

// Collections - Arrays.asList
List<String> list = Arrays.asList("a", "b", "c"); // Array → List

// Spring - HandlerAdapter
public interface HandlerAdapter {
    boolean supports(Object handler);
    ModelAndView handle(HttpServletRequest request,
                       HttpServletResponse response, Object handler);
}
```

### 클래스 어댑터 vs 객체 어댑터

| 특성 | 클래스 어댑터 | 객체 어댑터 |
|------|--------------|-------------|
| 구현 방식 | 상속 | 합성 |
| 유연성 | 낮음 | 높음 |
| Adaptee 접근 | protected 멤버 접근 가능 | public 멤버만 접근 |
| Java 지원 | 다중 상속 불가 | 권장 |

## Decorator Pattern

객체에 동적으로 새로운 책임을 추가하는 패턴

### 구조

```java
// 컴포넌트 인터페이스
public interface Coffee {
    double getCost();
    String getDescription();
}

// 구체적인 컴포넌트
public class Espresso implements Coffee {
    @Override
    public double getCost() {
        return 2.0;
    }

    @Override
    public String getDescription() {
        return "Espresso";
    }
}

// 데코레이터 추상 클래스
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// 구체적인 데코레이터
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.5;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }
}

public class WhipDecorator extends CoffeeDecorator {
    public WhipDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.7;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Whip";
    }
}

// 사용
Coffee coffee = new Espresso();
coffee = new MilkDecorator(coffee);
coffee = new WhipDecorator(coffee);
System.out.println(coffee.getDescription()); // Espresso, Milk, Whip
System.out.println(coffee.getCost());        // 3.2
```

### 실제 활용 예시

```java
// Java I/O
InputStream is = new FileInputStream("file.txt");
InputStream bis = new BufferedInputStream(is);
InputStream dis = new DataInputStream(bis);

// Java Collections
List<String> list = new ArrayList<>();
List<String> syncList = Collections.synchronizedList(list);
List<String> unmodifiableList = Collections.unmodifiableList(list);
```

## Facade Pattern

복잡한 서브시스템에 대한 단순화된 인터페이스를 제공하는 패턴

### 구조

```java
// 복잡한 서브시스템들
public class CPU {
    public void freeze() { System.out.println("CPU freeze"); }
    public void jump(long position) { System.out.println("CPU jump to " + position); }
    public void execute() { System.out.println("CPU execute"); }
}

public class Memory {
    public void load(long position, byte[] data) {
        System.out.println("Memory load at " + position);
    }
}

public class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("HardDrive read");
        return new byte[size];
    }
}

// 퍼사드
public class ComputerFacade {
    private static final long BOOT_ADDRESS = 0x00000000;
    private static final long BOOT_SECTOR = 0x00000000;
    private static final int SECTOR_SIZE = 512;

    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void start() {
        cpu.freeze();
        memory.load(BOOT_ADDRESS, hardDrive.read(BOOT_SECTOR, SECTOR_SIZE));
        cpu.jump(BOOT_ADDRESS);
        cpu.execute();
    }
}

// 사용 - 클라이언트는 복잡한 내부 동작을 알 필요 없음
ComputerFacade computer = new ComputerFacade();
computer.start();
```

### 실제 활용 예시

```java
// JDBC - DriverManager
Connection conn = DriverManager.getConnection(url, user, password);

// SLF4J - Logger
Logger logger = LoggerFactory.getLogger(MyClass.class);

// Spring - JdbcTemplate
JdbcTemplate template = new JdbcTemplate(dataSource);
List<User> users = template.query("SELECT * FROM users", new UserRowMapper());
```

## 패턴 비교

| 패턴 | 목적 | 특징 |
|------|------|------|
| Adapter | 인터페이스 변환 | 기존 클래스를 새 인터페이스에 맞춤 |
| Decorator | 기능 추가 | 동적으로 책임 추가 |
| Facade | 단순화된 인터페이스 | 서브시스템 복잡도 숨김 |
| Proxy | 접근 제어 | 대리 객체를 통한 접근 |
| Composite | 트리 구조 | 개별 객체와 복합 객체 동일 취급 |

## Adapter vs Decorator vs Facade

| 특성 | Adapter | Decorator | Facade |
|------|---------|-----------|--------|
| 목적 | 인터페이스 변환 | 기능 확장 | 단순화 |
| 인터페이스 | 변경됨 | 동일 유지 | 새로 정의 |
| 래핑 대상 | 하나의 객체 | 하나의 객체 | 여러 서브시스템 |
| 관계 | 1:1 | 1:1 | 1:N |

## 면접 예상 질문

1. **Adapter 패턴과 Facade 패턴의 차이점은?**
   - Adapter: 하나의 인터페이스를 다른 인터페이스로 변환
   - Facade: 여러 서브시스템에 대한 단순화된 인터페이스 제공

2. **Decorator 패턴의 장단점은?**
   - 장점: 상속보다 유연한 기능 확장, 단일 책임 원칙 준수
   - 단점: 작은 객체가 많이 생성됨, 데코레이터 순서에 주의 필요

3. **Java I/O에서 Decorator 패턴이 어떻게 사용되나요?**
   - InputStream을 감싸는 BufferedInputStream, DataInputStream 등
   - 기본 스트림에 버퍼링, 데이터 변환 등의 기능을 동적으로 추가
