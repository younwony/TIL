# Java 버전별 주요 기능 (8/11/17/21)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](../what-is-language.md)

> LTS 버전을 중심으로 Java의 주요 기능 변화와 발전 과정

`#Java버전` `#JavaVersion` `#LTS` `#LongTermSupport` `#Java8` `#Java11` `#Java17` `#Java21` `#Lambda` `#람다` `#Stream` `#스트림` `#Optional` `#var` `#타입추론` `#Record` `#레코드` `#SealedClass` `#봉인클래스` `#PatternMatching` `#패턴매칭` `#VirtualThread` `#가상스레드` `#TextBlock` `#Switch표현식` `#모듈시스템` `#JPMS` `#ZGC` `#G1GC`

## 왜 알아야 하는가?

Java는 LTS(Long Term Support) 버전을 중심으로 발전합니다. 실무에서 Java 8, 11, 17, 21이 주로 사용되며, 각 버전의 핵심 기능을 알아야 마이그레이션과 새 기능 활용이 가능합니다. 면접에서도 버전별 차이를 자주 묻습니다.

## 핵심 개념

- **LTS (Long Term Support)**: 장기 지원 버전 (8, 11, 17, 21)
- **Java 8**: Lambda, Stream, Optional (함수형 프로그래밍 도입)
- **Java 11**: var, HTTP Client, String 개선
- **Java 17**: Record, Sealed Class, Pattern Matching
- **Java 21**: Virtual Thread, Pattern Matching 완성

## 쉽게 이해하기

**Java 버전**을 스마트폰 OS 업데이트에 비유할 수 있습니다.

```
Java 버전 = 스마트폰 OS 버전

┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  Java 8 (2014)  = iOS 7 시대                                │
│  - 혁명적 변화: 람다, 스트림                                 │
│  - "드디어 함수형 프로그래밍!"                               │
│                                                              │
│  Java 11 (2018) = iOS 11 시대                               │
│  - 편의 기능: var, 새 API                                   │
│  - "이제 타입 안 써도 돼!"                                   │
│                                                              │
│  Java 17 (2021) = iOS 15 시대                               │
│  - 새로운 문법: Record, Sealed Class                        │
│  - "DTO가 한 줄로 끝나네!"                                  │
│                                                              │
│  Java 21 (2023) = iOS 17 시대                               │
│  - 성능 혁신: Virtual Thread                                │
│  - "수백만 스레드도 거뜬!"                                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### Java 8 (2014) - 함수형 프로그래밍 도입

```java
// 1. Lambda 표현식
// Before
Collections.sort(list, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// After (Lambda)
Collections.sort(list, (a, b) -> a.compareTo(b));

// 2. Stream API
List<String> filtered = users.stream()
    .filter(u -> u.getAge() > 20)
    .map(User::getName)
    .collect(Collectors.toList());

// 3. Optional
Optional<User> user = findById(id);
String name = user.map(User::getName).orElse("Unknown");

// 4. 메서드 참조
list.forEach(System.out::println);

// 5. 인터페이스 default 메서드
public interface Vehicle {
    default void start() {
        System.out.println("시동 걸기");
    }
}

// 6. 새로운 Date/Time API (java.time)
LocalDate today = LocalDate.now();
LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 10, 30);
Duration duration = Duration.between(start, end);
```

### Java 11 (2018) - 편의성 개선

```java
// 1. var (지역 변수 타입 추론) - Java 10부터
var list = new ArrayList<String>();  // ArrayList<String> 추론
var stream = list.stream();          // Stream<String> 추론

// 주의: 필드, 파라미터, 반환 타입에는 사용 불가
// class Example {
//     var field = 10;  // 컴파일 에러
// }

// 2. String 신규 메서드
"  hello  ".strip();         // "hello" (trim과 유사, Unicode 공백 지원)
"  hello  ".stripLeading();  // "hello  "
"  hello  ".stripTrailing(); // "  hello"
"hello".repeat(3);           // "hellohellohello"
"hello\nworld".lines();      // Stream<String>
"   ".isBlank();             // true

// 3. 컬렉션 -> 배열
List<String> list = List.of("a", "b", "c");
String[] array = list.toArray(String[]::new);

// 4. Files 신규 메서드
String content = Files.readString(Path.of("file.txt"));
Files.writeString(Path.of("file.txt"), "content");

// 5. HTTP Client (표준)
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com"))
    .GET()
    .build();
HttpResponse<String> response = client.send(request,
    HttpResponse.BodyHandlers.ofString());

// 6. Lambda에서 var 사용
list.stream()
    .map((var s) -> s.toUpperCase())  // 어노테이션 붙일 때 유용
    .collect(Collectors.toList());
```

### Java 17 (2021) - 현대적 문법

```java
// 1. Record (불변 데이터 클래스)
// Before: getter, equals, hashCode, toString 전부 작성
public class PersonOld {
    private final String name;
    private final int age;
    // 생성자, getter, equals, hashCode, toString...
}

// After: 한 줄로 끝
public record Person(String name, int age) { }

Person person = new Person("Alice", 25);
String name = person.name();  // getter (get 접두사 없음)

// 2. Sealed Class (상속 제한)
public sealed class Shape permits Circle, Rectangle, Triangle {
}

public final class Circle extends Shape { }
public final class Rectangle extends Shape { }
public non-sealed class Triangle extends Shape { }  // 자유 상속

// 3. Pattern Matching for instanceof
// Before
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// After
if (obj instanceof String s) {
    System.out.println(s.length());  // 바로 사용
}

// 4. Switch 표현식 (Java 14 정식)
// Before
String result;
switch (day) {
    case MONDAY:
    case FRIDAY:
        result = "출근";
        break;
    case SATURDAY:
    case SUNDAY:
        result = "휴식";
        break;
    default:
        result = "야근";
}

// After
String result = switch (day) {
    case MONDAY, FRIDAY -> "출근";
    case SATURDAY, SUNDAY -> "휴식";
    default -> "야근";
};

// 5. Text Block (Java 15 정식)
// Before
String json = "{\n" +
    "  \"name\": \"Alice\",\n" +
    "  \"age\": 25\n" +
    "}";

// After
String json = """
    {
      "name": "Alice",
      "age": 25
    }
    """;

// 6. NullPointerException 개선
// Before: NullPointerException
// After: Cannot invoke "String.length()" because "user.getName()" is null
```

### Java 21 (2023) - 성능과 문법 혁신

```java
// 1. Virtual Thread (가상 스레드)
// 기존 Platform Thread: OS 스레드와 1:1 매핑, 수천 개가 한계
// Virtual Thread: JVM이 관리, 수백만 개 가능

// 생성 방법 1
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual Thread!");
});

// 생성 방법 2: ExecutorService
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return "done";
        });
    }
}  // 자동 종료 대기

// 2. Record Pattern (레코드 패턴)
record Point(int x, int y) { }

// 분해해서 바로 사용
if (obj instanceof Point(int x, int y)) {
    System.out.println("x: " + x + ", y: " + y);
}

// switch에서도 사용
String describe(Object obj) {
    return switch (obj) {
        case Point(int x, int y) -> "Point at " + x + ", " + y;
        case String s -> "String: " + s;
        case null -> "null";
        default -> "Unknown";
    };
}

// 3. Pattern Matching for switch (완성)
String result = switch (obj) {
    case Integer i when i > 0 -> "양수: " + i;
    case Integer i -> "0 또는 음수: " + i;
    case String s -> "문자열: " + s;
    case null -> "null";
    default -> "기타";
};

// 4. Sequenced Collections
SequencedCollection<String> list = new ArrayList<>();
list.addFirst("first");
list.addLast("last");
list.getFirst();
list.getLast();
list.reversed();  // 역순 뷰

SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.firstEntry();
map.lastEntry();
map.pollFirstEntry();

// 5. String Templates (Preview)
// String message = STR."Hello, \{name}! You are \{age} years old.";

// 6. 향상된 switch null 처리
String result = switch (str) {
    case null -> "null 값";
    case "hello" -> "인사";
    default -> "기타";
};
```

### 버전별 GC 발전

```
┌─────────────────────────────────────────────────────────────┐
│                    GC 발전 과정                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Java 8: Parallel GC (기본), G1 GC (옵션)                   │
│          - 대용량 힙, STW 시간 단축                          │
│                                                              │
│  Java 11: G1 GC (기본), ZGC (실험적)                        │
│           - 예측 가능한 중단 시간                            │
│                                                              │
│  Java 17: ZGC (정식), Shenandoah                            │
│           - 밀리초 단위 중단 시간                            │
│                                                              │
│  Java 21: Generational ZGC                                  │
│           - 세대별 수집으로 더욱 효율적                      │
│                                                              │
│  ┌────────────────────────────────────────┐                 │
│  │ STW (Stop-The-World) 시간 비교         │                 │
│  │                                        │                 │
│  │ Parallel GC:  ████████████ (수초)      │                 │
│  │ G1 GC:        ██████ (수백 ms)         │                 │
│  │ ZGC:          █ (수 ms)                │                 │
│  └────────────────────────────────────────┘                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 마이그레이션 가이드

```java
// Java 8 → 11 주요 변경
// 1. Java EE 모듈 제거 (javax.xml.bind 등)
// 해결: 별도 의존성 추가
// implementation 'jakarta.xml.bind:jakarta.xml.bind-api:4.0.0'

// 2. 내부 API 접근 제한 (sun.misc.Unsafe 등)
// 해결: 표준 API 사용 또는 --add-opens 옵션

// Java 11 → 17 주요 변경
// 1. Sealed Class 도입에 따른 상속 검토
// 2. 강화된 캡슐화 (Reflection 제한)

// Java 17 → 21 주요 변경
// 1. Virtual Thread 활용 검토
// 2. Sequenced Collections 활용
// 3. Pattern Matching 적극 활용

// 버전 확인
Runtime.version();  // 21.0.1+12
System.getProperty("java.version");  // 21.0.1
```

### 버전별 주요 기능 비교표

| 기능 | Java 8 | Java 11 | Java 17 | Java 21 |
|------|--------|---------|---------|---------|
| Lambda | ✓ | ✓ | ✓ | ✓ |
| Stream | ✓ | ✓ | ✓ | ✓ |
| Optional | ✓ | ✓ | ✓ | ✓ |
| var | - | ✓ | ✓ | ✓ |
| HTTP Client | - | ✓ | ✓ | ✓ |
| Text Block | - | - | ✓ | ✓ |
| Record | - | - | ✓ | ✓ |
| Sealed Class | - | - | ✓ | ✓ |
| Pattern Matching | - | - | 일부 | ✓ |
| Virtual Thread | - | - | - | ✓ |
| Sequenced Collections | - | - | - | ✓ |
| 기본 GC | Parallel | G1 | G1 | G1 |

## 트레이드오프

| 버전 | 장점 | 단점 |
|------|------|------|
| Java 8 | 안정성, 풍부한 생태계 | 최신 기능 부재, 지원 종료 |
| Java 11 | 안정성 + 모던 API | 중간 버전 |
| Java 17 | 현대적 문법, 성능 | 생태계 이전 중 |
| Java 21 | 최신 기능, Virtual Thread | 신규 기능 검증 필요 |

## 면접 예상 질문

### Q: Java 8과 17의 가장 큰 차이점은?

A: **Java 8**: 함수형 프로그래밍 도입 (Lambda, Stream, Optional). 패러다임 변화. **Java 17**: 현대적 문법 도입 (Record, Sealed Class, Pattern Matching). 보일러플레이트 코드 대폭 감소. **실무 영향**: Record로 DTO 클래스가 한 줄, Pattern Matching으로 타입 체크 코드 간결화, Sealed Class로 상속 계층 명확화.

### Q: Virtual Thread란 무엇이고 언제 사용하나요?

A: **정의**: JVM이 관리하는 경량 스레드. OS 스레드와 M:N 매핑. **장점**: 수백만 개 생성 가능, 블로킹 I/O에서도 효율적. **사용 시점**: I/O 바운드 작업 (HTTP 요청, DB 쿼리). **주의**: CPU 바운드 작업에는 이점 없음, synchronized 블록 주의 (Pinning 문제).

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로그래밍 언어란](../what-is-language.md) | 선수 지식 | [1] 기초 |
| [JVM 구조](./jvm.md) | GC, 메모리 | [3] 중급 |
| [Stream API](./stream-api.md) | Java 8 핵심 | [3] 중급 |
| [프로세스와 스레드](../../os/process-vs-thread.md) | Virtual Thread | [2] 입문 |

## 참고 자료

- [Java Version History - Wikipedia](https://en.wikipedia.org/wiki/Java_version_history)
- [OpenJDK JEP Index](https://openjdk.org/jeps/0)
- [Oracle Java SE Documentation](https://docs.oracle.com/en/java/javase/)
