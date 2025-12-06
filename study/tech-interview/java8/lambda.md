# 람다 표현식 (Lambda Expression)

## 핵심 정리

- 람다 표현식은 익명 함수를 간결하게 표현하는 문법
- 함수형 인터페이스의 인스턴스를 생성하는 방법
- `(매개변수) -> { 실행문 }` 형태의 문법 사용
- 메서드 참조(Method Reference)로 더 간결하게 표현 가능

## 람다 표현식 문법

### 기본 형태

```java
// 기본 형태
(parameters) -> { statements }

// 매개변수가 하나일 때 괄호 생략 가능
parameter -> { statements }

// 실행문이 하나일 때 중괄호와 return 생략 가능
(parameters) -> expression

// 예시
(int a, int b) -> { return a + b; }
(a, b) -> a + b
a -> a * 2
() -> System.out.println("Hello")
```

### 다양한 예시

```java
// Runnable
Runnable runnable = () -> System.out.println("Hello");

// Comparator
Comparator<String> comparator = (s1, s2) -> s1.compareTo(s2);

// Consumer
Consumer<String> consumer = s -> System.out.println(s);

// Function
Function<String, Integer> function = s -> s.length();

// Predicate
Predicate<Integer> predicate = n -> n > 0;

// BiFunction
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
```

## 메서드 참조 (Method Reference)

람다 표현식을 더 간결하게 표현하는 방법

### 종류

| 종류 | 문법 | 람다 표현식 |
|------|------|-------------|
| 정적 메서드 | ClassName::staticMethod | x -> ClassName.staticMethod(x) |
| 인스턴스 메서드 | instance::method | x -> instance.method(x) |
| 특정 타입의 메서드 | ClassName::method | (obj, x) -> obj.method(x) |
| 생성자 | ClassName::new | () -> new ClassName() |

### 예시

```java
// 정적 메서드 참조
Function<String, Integer> parseInt = Integer::parseInt;
// 동일: s -> Integer.parseInt(s)

// 인스턴스 메서드 참조
String str = "Hello";
Supplier<Integer> length = str::length;
// 동일: () -> str.length()

// 특정 타입의 인스턴스 메서드 참조
Function<String, String> toUpperCase = String::toUpperCase;
// 동일: s -> s.toUpperCase()

BiPredicate<String, String> contains = String::contains;
// 동일: (s1, s2) -> s1.contains(s2)

// 생성자 참조
Supplier<ArrayList<String>> listSupplier = ArrayList::new;
// 동일: () -> new ArrayList<>()

Function<Integer, int[]> arrayCreator = int[]::new;
// 동일: size -> new int[size]
```

## 람다와 익명 클래스의 차이

### 비교

```java
// 익명 클래스
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// 람다 표현식
Runnable r2 = () -> System.out.println("Hello");
```

### 차이점

| 특성 | 익명 클래스 | 람다 표현식 |
|------|------------|-------------|
| this 참조 | 익명 클래스 자신 | 바깥 클래스 |
| 스코프 | 새로운 스코프 생성 | 바깥 스코프와 공유 |
| 컴파일 | 별도 클래스 파일 생성 | invokedynamic 사용 |
| 인터페이스 제한 | 다중 메서드 가능 | 함수형 인터페이스만 |

### this 참조 예시

```java
public class Example {
    private String message = "Hello";

    public void test() {
        // 익명 클래스 - this는 익명 클래스 인스턴스
        Runnable r1 = new Runnable() {
            private String message = "World";

            @Override
            public void run() {
                System.out.println(this.message); // World
            }
        };

        // 람다 - this는 Example 인스턴스
        Runnable r2 = () -> {
            System.out.println(this.message); // Hello
        };
    }
}
```

## 변수 캡처 (Variable Capture)

람다에서 외부 변수를 사용할 때의 규칙

### 규칙

- 지역 변수는 final이거나 effectively final이어야 함
- 인스턴스 변수와 정적 변수는 자유롭게 사용 가능

```java
public void example() {
    int localVar = 10;      // effectively final
    final int finalVar = 20; // final

    // 가능
    Consumer<Integer> consumer1 = x -> {
        System.out.println(localVar);  // OK
        System.out.println(finalVar);  // OK
    };

    // 컴파일 에러
    // localVar = 15; // localVar가 더 이상 effectively final이 아님
    // Consumer<Integer> consumer2 = x -> System.out.println(localVar);
}
```

### 이유

- 람다는 지역 변수의 복사본을 사용함 (Capture)
- 값이 변경되면 복사본과 원본 간 불일치 발생 가능
- 멀티스레드 환경에서의 안전성 보장

## 실전 활용 예시

### Stream API와 함께

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// 필터링, 변환, 수집
List<String> result = names.stream()
    .filter(name -> name.length() > 4)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
// [ALICE, CHARLIE, DAVID]

// 통계
int totalLength = names.stream()
    .mapToInt(String::length)
    .sum();
```

### Comparator 작성

```java
List<Person> people = Arrays.asList(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Charlie", 35)
);

// 단일 조건
people.sort(Comparator.comparing(Person::getAge));

// 다중 조건
people.sort(Comparator
    .comparing(Person::getAge)
    .thenComparing(Person::getName));

// 역순
people.sort(Comparator.comparing(Person::getAge).reversed());
```

### 이벤트 핸들러

```java
button.addActionListener(e -> System.out.println("Button clicked"));

// 메서드 참조
button.addActionListener(this::handleClick);

private void handleClick(ActionEvent e) {
    System.out.println("Button clicked");
}
```

## 면접 예상 질문

1. **람다 표현식과 익명 클래스의 차이점은?**
   - this 참조: 람다는 바깥 클래스, 익명 클래스는 자신
   - 변수 섀도잉: 람다는 바깥 스코프와 공유
   - 컴파일: 람다는 invokedynamic, 익명 클래스는 별도 클래스 파일

2. **람다에서 지역 변수가 effectively final이어야 하는 이유는?**
   - 람다는 변수의 복사본을 사용함
   - 변경 가능하면 원본과 복사본 간 불일치 발생
   - 멀티스레드 환경에서 안전성 보장

3. **메서드 참조의 종류와 각각의 사용 시점은?**
   - 정적 메서드: 유틸리티 메서드 호출 (Integer::parseInt)
   - 인스턴스 메서드: 특정 객체의 메서드 호출 (str::length)
   - 특정 타입의 메서드: 첫 번째 매개변수가 수신 객체 (String::toUpperCase)
   - 생성자: 객체 생성 (ArrayList::new)
