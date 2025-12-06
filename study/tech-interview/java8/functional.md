# 함수형 인터페이스 (Functional Interface)

## 핵심 정리

- 함수형 인터페이스는 단 하나의 추상 메서드만 가지는 인터페이스
- `@FunctionalInterface` 어노테이션으로 명시 (선택적이지만 권장)
- 람다 표현식과 메서드 참조의 타겟 타입으로 사용
- java.util.function 패키지에 주요 함수형 인터페이스 제공

## 주요 함수형 인터페이스

### Consumer<T>

입력을 받아서 소비만 하고 반환값이 없는 인터페이스

```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
```

#### 사용 예시

```java
// 기본 사용
Consumer<String> printer = s -> System.out.println(s);
printer.accept("Hello");  // Hello

// 메서드 참조
Consumer<String> printer2 = System.out::println;
printer2.accept("World"); // World

// andThen으로 체이닝
Consumer<String> greeting = s -> System.out.print("Hello, ");
Consumer<String> name = s -> System.out.println(s);
greeting.andThen(name).accept("Java"); // Hello, Java

// 컬렉션과 함께 사용
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(System.out::println);
```

#### 변형

| 인터페이스 | 시그니처 | 설명 |
|-----------|----------|------|
| Consumer<T> | void accept(T t) | 단일 입력 |
| BiConsumer<T, U> | void accept(T t, U u) | 두 개 입력 |
| IntConsumer | void accept(int value) | int 특화 |
| LongConsumer | void accept(long value) | long 특화 |
| DoubleConsumer | void accept(double value) | double 특화 |

### Supplier<T>

입력 없이 값을 반환하는 인터페이스

```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

#### 사용 예시

```java
// 기본 사용
Supplier<Double> randomSupplier = () -> Math.random();
System.out.println(randomSupplier.get()); // 0.xxx

// 객체 생성
Supplier<List<String>> listSupplier = ArrayList::new;
List<String> list = listSupplier.get();

// 지연 실행 (Lazy Evaluation)
Supplier<String> expensiveOperation = () -> {
    System.out.println("Computing...");
    return "Result";
};
// 실제 get() 호출 전까지 계산하지 않음

// Optional과 함께 사용
Optional<String> optional = Optional.empty();
String value = optional.orElseGet(() -> "Default Value");
```

#### 변형

| 인터페이스 | 시그니처 | 설명 |
|-----------|----------|------|
| Supplier<T> | T get() | 객체 반환 |
| BooleanSupplier | boolean getAsBoolean() | boolean 반환 |
| IntSupplier | int getAsInt() | int 반환 |
| LongSupplier | long getAsLong() | long 반환 |
| DoubleSupplier | double getAsDouble() | double 반환 |

### Function<T, R>

입력을 받아서 변환하여 반환하는 인터페이스

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```

#### 사용 예시

```java
// 기본 사용
Function<String, Integer> lengthFunc = s -> s.length();
int length = lengthFunc.apply("Hello"); // 5

// 메서드 참조
Function<String, Integer> lengthFunc2 = String::length;

// andThen으로 체이닝 (f.andThen(g) = g(f(x)))
Function<Integer, Integer> multiply = x -> x * 2;
Function<Integer, Integer> add = x -> x + 3;
Function<Integer, Integer> combined = multiply.andThen(add);
combined.apply(5); // (5 * 2) + 3 = 13

// compose로 체이닝 (f.compose(g) = f(g(x)))
Function<Integer, Integer> composed = multiply.compose(add);
composed.apply(5); // (5 + 3) * 2 = 16

// Stream map과 함께 사용
List<String> names = Arrays.asList("alice", "bob");
List<String> upperNames = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

#### 변형

| 인터페이스 | 시그니처 | 설명 |
|-----------|----------|------|
| Function<T, R> | R apply(T t) | 단일 입력 변환 |
| BiFunction<T, U, R> | R apply(T t, U u) | 두 입력 변환 |
| UnaryOperator<T> | T apply(T t) | 동일 타입 변환 |
| BinaryOperator<T> | T apply(T t1, T t2) | 두 동일 타입 연산 |
| IntFunction<R> | R apply(int value) | int → R |
| ToIntFunction<T> | int applyAsInt(T value) | T → int |

### Predicate<T>

입력을 받아서 boolean을 반환하는 인터페이스

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
```

#### 사용 예시

```java
// 기본 사용
Predicate<String> isEmpty = s -> s.isEmpty();
boolean result = isEmpty.test(""); // true

// 메서드 참조
Predicate<String> isEmpty2 = String::isEmpty;

// 논리 연산 (and, or, negate)
Predicate<Integer> isPositive = x -> x > 0;
Predicate<Integer> isEven = x -> x % 2 == 0;

Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);
isPositiveAndEven.test(4);  // true
isPositiveAndEven.test(-2); // false

Predicate<Integer> isPositiveOrEven = isPositive.or(isEven);
isPositiveOrEven.test(-2);  // true

Predicate<Integer> isNotPositive = isPositive.negate();
isNotPositive.test(-5);     // true

// Stream filter와 함께 사용
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList()); // [2, 4, 6]

// isEqual 정적 메서드
Predicate<String> equalToHello = Predicate.isEqual("Hello");
equalToHello.test("Hello"); // true
```

#### 변형

| 인터페이스 | 시그니처 | 설명 |
|-----------|----------|------|
| Predicate<T> | boolean test(T t) | 단일 조건 |
| BiPredicate<T, U> | boolean test(T t, U u) | 두 입력 조건 |
| IntPredicate | boolean test(int value) | int 특화 |
| LongPredicate | boolean test(long value) | long 특화 |
| DoublePredicate | boolean test(double value) | double 특화 |

## 함수 조합 예시

```java
// 여러 함수를 조합하여 파이프라인 구성
Function<String, String> trim = String::trim;
Function<String, String> toLowerCase = String::toLowerCase;
Function<String, Integer> length = String::length;
Predicate<Integer> isLongEnough = len -> len >= 5;

String input = "  HELLO WORLD  ";
boolean result = Optional.of(input)
    .map(trim)
    .map(toLowerCase)
    .map(length)
    .filter(isLongEnough)
    .isPresent(); // true
```

## 커스텀 함수형 인터페이스

```java
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

// 사용
TriFunction<Integer, Integer, Integer, Integer> sum = (a, b, c) -> a + b + c;
int result = sum.apply(1, 2, 3); // 6
```

## 면접 예상 질문

1. **Consumer와 Supplier의 차이점은?**
   - Consumer: 입력만 받고 반환 없음 (void accept(T))
   - Supplier: 입력 없이 값 반환 (T get())

2. **Function과 Predicate의 차이점은?**
   - Function: 입력을 받아 다른 타입으로 변환 (R apply(T))
   - Predicate: 입력을 받아 boolean 반환 (boolean test(T))

3. **함수형 인터페이스를 사용하는 이유는?**
   - 람다 표현식의 타겟 타입으로 사용
   - 코드의 간결성과 가독성 향상
   - 함수를 일급 객체처럼 다룰 수 있음
   - 지연 실행(Lazy Evaluation) 구현 가능
