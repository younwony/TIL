# 함수형 프로그래밍 (Functional Programming)

## 핵심 개념

- **함수형 프로그래밍(FP)**은 순수 함수와 불변 데이터를 기반으로 프로그램을 구성하는 패러다임
- 부수 효과(Side Effect)를 최소화하여 예측 가능하고 테스트하기 쉬운 코드 작성
- 핵심 원칙: **순수 함수**, **불변성**, **선언적 프로그래밍**, **고차 함수**
- Java 8+, Kotlin, JavaScript 등 현대 언어에서 적극 지원
- OOP와 대립 개념이 아닌 **상호 보완적** 관계

## 순수 함수 (Pure Function)

### 정의

순수 함수는 두 가지 조건을 만족하는 함수:
1. **동일 입력 → 동일 출력**: 같은 인자로 호출하면 항상 같은 결과 반환
2. **부수 효과 없음**: 외부 상태를 변경하지 않음

```
┌─────────────────────────────────────────────────────────┐
│                      순수 함수                           │
│  ┌─────────┐                         ┌─────────┐       │
│  │ Input A │ ────▶ f(x) = x + 1 ────▶│ Output B│       │
│  └─────────┘                         └─────────┘       │
│                                                         │
│  • 외부 상태 참조 X                                      │
│  • 외부 상태 변경 X                                      │
│  • 항상 동일한 결과                                      │
└─────────────────────────────────────────────────────────┘
```

### 예시

```java
// 순수 함수
public int add(int a, int b) {
    return a + b;  // 항상 동일 입력 → 동일 출력
}

public String toUpperCase(String str) {
    return str.toUpperCase();  // 원본 변경 없음
}

// 비순수 함수 (피해야 함)
private int count = 0;

public int incrementAndGet() {
    return ++count;  // 외부 상태 변경 (부수 효과)
}

public int addWithRandom(int a) {
    return a + new Random().nextInt();  // 동일 입력 → 다른 출력
}

public void saveToDatabase(User user) {
    database.save(user);  // I/O 부수 효과
}
```

### 순수 함수의 장점

| 장점 | 설명 |
|------|------|
| **테스트 용이** | 외부 의존성 없이 입출력만 검증 |
| **캐싱 가능** | 동일 입력 → 동일 출력이므로 메모이제이션 적용 가능 |
| **병렬 처리 안전** | 공유 상태가 없어 동시성 문제 없음 |
| **추론 용이** | 함수 시그니처만으로 동작 예측 가능 |

## 불변성 (Immutability)

### 정의

데이터가 생성된 후 변경되지 않는 특성

```
┌─────────────────────────────────────────────────────────┐
│                    가변 vs 불변                          │
│                                                         │
│  가변 (Mutable)              불변 (Immutable)           │
│  ┌─────────┐                 ┌─────────┐               │
│  │ [1,2,3] │                 │ [1,2,3] │ ─── 원본 유지  │
│  └────┬────┘                 └─────────┘               │
│       │ add(4)                    │ add(4)             │
│       ▼                           ▼                    │
│  ┌─────────┐                 ┌─────────┐               │
│  │[1,2,3,4]│ ─── 원본 변경   │[1,2,3,4]│ ─── 새 객체   │
│  └─────────┘                 └─────────┘               │
└─────────────────────────────────────────────────────────┘
```

### Java에서 불변 객체 만들기

```java
// 불변 클래스 설계
public final class Money {
    private final int amount;
    private final String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // 값을 변경하는 대신 새 객체 반환
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("통화가 다릅니다");
        }
        return new Money(this.amount + other.amount, this.currency);
    }

    public int getAmount() { return amount; }
    public String getCurrency() { return currency; }
}

// 사용
Money price = new Money(1000, "KRW");
Money tax = new Money(100, "KRW");
Money total = price.add(tax);  // 새 객체 생성, price와 tax는 그대로
```

### Java Record (Java 14+)

```java
// Record는 기본적으로 불변
public record Point(int x, int y) {
    public Point move(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
}

Point p1 = new Point(0, 0);
Point p2 = p1.move(3, 4);  // p1은 변경되지 않음
```

### 불변 컬렉션

```java
// Java 9+ 불변 컬렉션 팩토리
List<String> immutableList = List.of("a", "b", "c");
Set<Integer> immutableSet = Set.of(1, 2, 3);
Map<String, Integer> immutableMap = Map.of("one", 1, "two", 2);

// immutableList.add("d");  // UnsupportedOperationException

// 기존 컬렉션을 불변으로 변환
List<String> mutableList = new ArrayList<>();
mutableList.add("a");
List<String> unmodifiable = Collections.unmodifiableList(mutableList);
```

## 고차 함수 (Higher-Order Function)

### 정의

함수를 인자로 받거나 함수를 반환하는 함수

```
┌─────────────────────────────────────────────────────────┐
│                     고차 함수                            │
│                                                         │
│  1. 함수를 인자로 받음                                   │
│     ┌─────────┐     ┌─────────┐                        │
│     │ 데이터   │────▶│ map(f)  │────▶ 변환된 데이터     │
│     └─────────┘     └────┬────┘                        │
│                          │                              │
│                     ┌────┴────┐                        │
│                     │ f(x)    │ ← 함수를 인자로         │
│                     └─────────┘                        │
│                                                         │
│  2. 함수를 반환                                          │
│     ┌─────────┐                                        │
│     │ adder(5)│────▶ (x) -> x + 5  ← 함수 반환         │
│     └─────────┘                                        │
└─────────────────────────────────────────────────────────┘
```

### 예시

```java
// 함수를 인자로 받는 고차 함수
public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
    List<R> result = new ArrayList<>();
    for (T item : list) {
        result.add(mapper.apply(item));
    }
    return result;
}

// 사용
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
List<Integer> doubled = map(numbers, x -> x * 2);  // [2, 4, 6, 8, 10]

// 함수를 반환하는 고차 함수
public static Function<Integer, Integer> adder(int n) {
    return x -> x + n;
}

// 사용
Function<Integer, Integer> add5 = adder(5);
int result = add5.apply(10);  // 15
```

### 커링 (Currying)

여러 인자를 받는 함수를 단일 인자 함수들의 체인으로 변환

```java
// 일반 함수
public int add(int a, int b, int c) {
    return a + b + c;
}

// 커링된 함수
public Function<Integer, Function<Integer, Integer>> curriedAdd(int a) {
    return b -> c -> a + b + c;
}

// 사용
int result = curriedAdd(1).apply(2).apply(3);  // 6

// 부분 적용
Function<Integer, Function<Integer, Integer>> add1 = curriedAdd(1);
Function<Integer, Integer> add1and2 = add1.apply(2);
int finalResult = add1and2.apply(3);  // 6
```

## 함수 합성 (Function Composition)

### 정의

여러 함수를 조합하여 새로운 함수를 만드는 기법

```
┌─────────────────────────────────────────────────────────┐
│                    함수 합성                             │
│                                                         │
│  f(x) = x + 1                                           │
│  g(x) = x * 2                                           │
│                                                         │
│  g ∘ f = g(f(x)) = (x + 1) * 2                         │
│                                                         │
│  입력: 3 ──▶ f(3)=4 ──▶ g(4)=8 ──▶ 출력: 8             │
└─────────────────────────────────────────────────────────┘
```

### Java에서 함수 합성

```java
Function<Integer, Integer> addOne = x -> x + 1;
Function<Integer, Integer> multiplyTwo = x -> x * 2;

// andThen: f.andThen(g) = g(f(x))
Function<Integer, Integer> addThenMultiply = addOne.andThen(multiplyTwo);
int result1 = addThenMultiply.apply(3);  // (3 + 1) * 2 = 8

// compose: f.compose(g) = f(g(x))
Function<Integer, Integer> multiplyThenAdd = addOne.compose(multiplyTwo);
int result2 = multiplyThenAdd.apply(3);  // (3 * 2) + 1 = 7
```

### 실용적인 함수 합성

```java
// 문자열 처리 파이프라인
Function<String, String> trim = String::trim;
Function<String, String> toLowerCase = String::toLowerCase;
Function<String, String> removeSpaces = s -> s.replace(" ", "");

Function<String, String> normalize = trim
    .andThen(toLowerCase)
    .andThen(removeSpaces);

String result = normalize.apply("  Hello World  ");  // "helloworld"
```

## Java Stream API

### 선언적 프로그래밍

```java
// 명령형 (Imperative) - HOW를 기술
List<Integer> evenNumbers = new ArrayList<>();
for (Integer num : numbers) {
    if (num % 2 == 0) {
        evenNumbers.add(num);
    }
}

// 선언적 (Declarative) - WHAT을 기술
List<Integer> evenNumbers = numbers.stream()
    .filter(num -> num % 2 == 0)
    .collect(Collectors.toList());
```

### 주요 스트림 연산

```java
List<User> users = List.of(
    new User("Alice", 25),
    new User("Bob", 30),
    new User("Charlie", 35)
);

// filter: 조건에 맞는 요소만 선택
List<User> adults = users.stream()
    .filter(user -> user.getAge() >= 30)
    .collect(Collectors.toList());

// map: 요소 변환
List<String> names = users.stream()
    .map(User::getName)
    .collect(Collectors.toList());

// flatMap: 중첩 구조 평탄화
List<List<Integer>> nested = List.of(
    List.of(1, 2),
    List.of(3, 4)
);
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());  // [1, 2, 3, 4]

// reduce: 요소들을 하나로 결합
int sum = List.of(1, 2, 3, 4, 5).stream()
    .reduce(0, Integer::sum);  // 15

// sorted, distinct, limit, skip
List<Integer> processed = List.of(3, 1, 4, 1, 5, 9, 2, 6).stream()
    .distinct()      // 중복 제거
    .sorted()        // 정렬
    .limit(5)        // 상위 5개만
    .collect(Collectors.toList());  // [1, 2, 3, 4, 5]
```

### Collectors 활용

```java
// toMap
Map<String, Integer> nameToAge = users.stream()
    .collect(Collectors.toMap(User::getName, User::getAge));

// groupingBy
Map<Integer, List<User>> byAge = users.stream()
    .collect(Collectors.groupingBy(User::getAge));

// partitioningBy
Map<Boolean, List<User>> partitioned = users.stream()
    .collect(Collectors.partitioningBy(u -> u.getAge() >= 30));

// joining
String allNames = users.stream()
    .map(User::getName)
    .collect(Collectors.joining(", "));  // "Alice, Bob, Charlie"

// 통계
IntSummaryStatistics stats = users.stream()
    .collect(Collectors.summarizingInt(User::getAge));
// stats.getAverage(), stats.getMax(), stats.getMin(), stats.getSum()
```

## Optional

### null 대신 Optional 사용

```java
// Bad - null 반환
public User findById(Long id) {
    return userRepository.findById(id);  // null일 수 있음
}

// Good - Optional 반환
public Optional<User> findById(Long id) {
    return Optional.ofNullable(userRepository.findById(id));
}
```

### Optional 활용

```java
Optional<User> userOpt = findById(1L);

// 값 존재 여부 확인
if (userOpt.isPresent()) {
    User user = userOpt.get();
}

// orElse: 기본값 제공
User user = userOpt.orElse(new User("Unknown", 0));

// orElseGet: 지연 평가 (값이 없을 때만 실행)
User user = userOpt.orElseGet(() -> createDefaultUser());

// orElseThrow: 예외 발생
User user = userOpt.orElseThrow(() -> new UserNotFoundException());

// map: 값 변환
Optional<String> nameOpt = userOpt.map(User::getName);

// flatMap: Optional 반환 함수와 사용
Optional<String> email = userOpt
    .flatMap(User::getEmail);  // getEmail()이 Optional<String> 반환 시

// filter: 조건 필터링
Optional<User> adultOpt = userOpt
    .filter(user -> user.getAge() >= 18);

// ifPresent: 값이 있을 때만 실행
userOpt.ifPresent(user -> sendWelcomeEmail(user));

// ifPresentOrElse (Java 9+)
userOpt.ifPresentOrElse(
    user -> System.out.println("Found: " + user.getName()),
    () -> System.out.println("User not found")
);
```

### Optional 체이닝

```java
// 중첩된 null 체크를 Optional로 대체
// Before
String cityName = null;
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        City city = address.getCity();
        if (city != null) {
            cityName = city.getName();
        }
    }
}

// After
String cityName = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .map(City::getName)
    .orElse("Unknown");
```

## 재귀와 꼬리 재귀 (Tail Recursion)

### 일반 재귀 vs 꼬리 재귀

```java
// 일반 재귀 - 스택 프레임 누적
public int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);  // 곱셈이 재귀 호출 "후"에 수행
}

// 호출 스택:
// factorial(5)
//   5 * factorial(4)
//     4 * factorial(3)
//       3 * factorial(2)
//         2 * factorial(1)
//           1
//         2 * 1 = 2
//       3 * 2 = 6
//     4 * 6 = 24
//   5 * 24 = 120

// 꼬리 재귀 - 마지막 연산이 재귀 호출
public int factorialTail(int n, int accumulator) {
    if (n <= 1) return accumulator;
    return factorialTail(n - 1, n * accumulator);  // 재귀 호출이 마지막
}

public int factorial(int n) {
    return factorialTail(n, 1);
}
```

### 꼬리 재귀 최적화 (TCO)

```
일반 재귀                    꼬리 재귀 (TCO 적용 시)
┌─────────────┐             ┌─────────────┐
│ factorial(5)│             │ fact(5, 1)  │
├─────────────┤             │     ↓       │
│ factorial(4)│             │ fact(4, 5)  │ ← 같은 스택 프레임 재사용
├─────────────┤             │     ↓       │
│ factorial(3)│             │ fact(3, 20) │
├─────────────┤             │     ↓       │
│ factorial(2)│             │ fact(2, 60) │
├─────────────┤             │     ↓       │
│ factorial(1)│             │ fact(1,120) │
└─────────────┘             └─────────────┘
  O(n) 스택 공간              O(1) 스택 공간
```

> **Note**: Java는 TCO를 지원하지 않지만, Kotlin/Scala는 `tailrec` 키워드로 지원

### Java에서 재귀를 반복문으로 변환

```java
// 재귀 대신 Stream reduce 사용
public int factorial(int n) {
    return IntStream.rangeClosed(1, n)
        .reduce(1, (a, b) -> a * b);
}
```

## 모나드 (Monad) 기초

### 모나드란?

값을 감싸는 컨텍스트 + 값을 변환하는 연산을 제공하는 디자인 패턴

```
┌─────────────────────────────────────────────────────────┐
│                      모나드                              │
│                                                         │
│  ┌─────────────────┐                                   │
│  │    Context      │  ← 값을 감싸는 컨텍스트            │
│  │  ┌───────────┐  │                                   │
│  │  │   Value   │  │  ← 실제 값                        │
│  │  └───────────┘  │                                   │
│  └─────────────────┘                                   │
│                                                         │
│  연산:                                                  │
│  • of(value)     : 값을 모나드로 감싸기 (unit/return)   │
│  • flatMap(f)    : 모나드 안의 값에 함수 적용 (bind)    │
│  • map(f)        : 값 변환 후 다시 감싸기               │
└─────────────────────────────────────────────────────────┘
```

### Java의 모나드 예시

```java
// Optional은 "값이 있거나 없음"이라는 컨텍스트를 가진 모나드
Optional<Integer> opt = Optional.of(5);  // of: 값을 감싸기

Optional<Integer> result = opt
    .map(x -> x * 2)      // map: 값 변환
    .flatMap(x -> x > 5   // flatMap: Optional 반환 함수와 연결
        ? Optional.of(x)
        : Optional.empty());

// Stream도 모나드 (여러 값의 컨텍스트)
Stream<Integer> stream = Stream.of(1, 2, 3);

List<Integer> result = stream
    .map(x -> x * 2)
    .flatMap(x -> Stream.of(x, x + 1))
    .collect(Collectors.toList());  // [2, 3, 4, 5, 6, 7]

// CompletableFuture도 모나드 (비동기 컨텍스트)
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 5);

CompletableFuture<String> result = future
    .thenApply(x -> x * 2)           // map에 해당
    .thenCompose(x ->                // flatMap에 해당
        CompletableFuture.supplyAsync(() -> "Result: " + x));
```

## for문 vs Stream 비교

### 기본 비교

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// === 짝수만 필터링하여 제곱한 후 합계 구하기 ===

// for문 (명령형)
int sum = 0;
for (int num : numbers) {
    if (num % 2 == 0) {
        sum += num * num;
    }
}

// Stream (선언적)
int sum = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .reduce(0, Integer::sum);
```

### 상세 비교표

| 관점 | for문 (명령형) | Stream (함수형) |
|------|---------------|-----------------|
| **관심사** | HOW (어떻게 할 것인가) | WHAT (무엇을 할 것인가) |
| **상태 관리** | 외부 변수로 상태 관리 | 상태 없음 (Stateless) |
| **가독성** | 로직이 길어지면 복잡 | 파이프라인으로 명확 |
| **병렬 처리** | 직접 구현 필요 | `.parallelStream()` 한 줄 |
| **디버깅** | 브레이크포인트 설정 쉬움 | 중간 결과 확인 어려움 |
| **성능 (소량)** | 더 빠름 (오버헤드 없음) | 오버헤드 존재 |
| **성능 (대량)** | 비슷하거나 느림 | 지연 평가로 최적화 가능 |
| **조기 종료** | break로 간단 | takeWhile, findFirst 등 |
| **중첩 루프** | 들여쓰기 깊어짐 | flatMap으로 평탄화 |

### 실전 예제 비교

#### 1. 필터링 + 변환 + 수집

```java
List<User> users = getUsers();

// for문
List<String> adultNames = new ArrayList<>();
for (User user : users) {
    if (user.getAge() >= 18) {
        adultNames.add(user.getName().toUpperCase());
    }
}

// Stream
List<String> adultNames = users.stream()
    .filter(user -> user.getAge() >= 18)
    .map(user -> user.getName().toUpperCase())
    .collect(Collectors.toList());
```

#### 2. 중첩 컬렉션 처리

```java
List<Order> orders = getOrders();

// for문 - 중첩 루프
List<Product> allProducts = new ArrayList<>();
for (Order order : orders) {
    for (Product product : order.getProducts()) {
        if (product.getPrice() > 1000) {
            allProducts.add(product);
        }
    }
}

// Stream - flatMap으로 평탄화
List<Product> allProducts = orders.stream()
    .flatMap(order -> order.getProducts().stream())
    .filter(product -> product.getPrice() > 1000)
    .collect(Collectors.toList());
```

#### 3. 그룹핑

```java
List<Employee> employees = getEmployees();

// for문
Map<String, List<Employee>> byDepartment = new HashMap<>();
for (Employee emp : employees) {
    String dept = emp.getDepartment();
    if (!byDepartment.containsKey(dept)) {
        byDepartment.put(dept, new ArrayList<>());
    }
    byDepartment.get(dept).add(emp);
}

// Stream
Map<String, List<Employee>> byDepartment = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

#### 4. 조기 종료 (Early Termination)

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// for문 - break 사용
Integer firstEven = null;
for (Integer num : numbers) {
    if (num % 2 == 0) {
        firstEven = num;
        break;
    }
}

// Stream - findFirst (지연 평가로 자동 최적화)
Optional<Integer> firstEven = numbers.stream()
    .filter(n -> n % 2 == 0)
    .findFirst();
```

#### 5. 인덱스가 필요한 경우

```java
List<String> items = List.of("a", "b", "c", "d");

// for문 - 인덱스 직접 사용
for (int i = 0; i < items.size(); i++) {
    System.out.println(i + ": " + items.get(i));
}

// Stream - IntStream으로 인덱스 생성
IntStream.range(0, items.size())
    .forEach(i -> System.out.println(i + ": " + items.get(i)));
```

### 성능 비교

```java
// 테스트 데이터: 100만 개 정수
List<Integer> largeList = IntStream.rangeClosed(1, 1_000_000)
    .boxed()
    .collect(Collectors.toList());

// === 필터링 + 합계 ===

// for문: ~15ms
long sum = 0;
for (int n : largeList) {
    if (n % 2 == 0) sum += n;
}

// Stream: ~25ms (약간의 오버헤드)
long sum = largeList.stream()
    .filter(n -> n % 2 == 0)
    .mapToLong(n -> n)
    .sum();

// Parallel Stream: ~10ms (멀티코어 활용)
long sum = largeList.parallelStream()
    .filter(n -> n % 2 == 0)
    .mapToLong(n -> n)
    .sum();
```

### 언제 무엇을 사용할까?

#### for문을 선택하는 경우

```java
// 1. 단순한 반복 (오버헤드 최소화)
for (int i = 0; i < 10; i++) {
    doSomething(i);
}

// 2. 인덱스 기반 접근이 빈번한 경우
for (int i = 0; i < list.size(); i++) {
    if (i > 0) compare(list.get(i-1), list.get(i));
}

// 3. 외부 상태 변경이 필요한 경우 (부수 효과)
int count = 0;
for (Item item : items) {
    if (item.isValid()) {
        count++;
        item.process();  // 객체 상태 변경
    }
}

// 4. checked exception 처리가 필요한 경우
for (String path : paths) {
    try {
        Files.readAllLines(Path.of(path));  // IOException
    } catch (IOException e) {
        handleError(e);
    }
}
```

#### Stream을 선택하는 경우

```java
// 1. 데이터 변환 파이프라인
List<OrderDto> dtos = orders.stream()
    .filter(Order::isCompleted)
    .sorted(Comparator.comparing(Order::getDate))
    .map(OrderDto::from)
    .collect(Collectors.toList());

// 2. 집계 연산
Map<Category, Long> countByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::getCategory,
        Collectors.counting()
    ));

// 3. 병렬 처리가 필요한 대용량 데이터
long total = hugeList.parallelStream()
    .filter(this::expensiveFilter)
    .mapToLong(this::expensiveCalculation)
    .sum();

// 4. 복잡한 중첩 구조 처리
List<String> allTags = posts.stream()
    .flatMap(post -> post.getTags().stream())
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

### 주의사항

```java
// ❌ Bad: Stream 내에서 외부 상태 변경
List<Integer> result = new ArrayList<>();
numbers.stream()
    .filter(n -> n > 0)
    .forEach(n -> result.add(n));  // 부수 효과!

// ✅ Good: collect로 수집
List<Integer> result = numbers.stream()
    .filter(n -> n > 0)
    .collect(Collectors.toList());

// ❌ Bad: 무한 스트림 주의
Stream.iterate(0, n -> n + 1)
    .filter(n -> n > 100)  // 무한 루프!
    .forEach(System.out::println);

// ✅ Good: limit 또는 takeWhile 사용
Stream.iterate(0, n -> n + 1)
    .takeWhile(n -> n <= 100)  // Java 9+
    .forEach(System.out::println);
```

## OOP vs FP 비교

| 관점 | OOP | FP |
|------|-----|-----|
| **기본 단위** | 객체 (상태 + 행위) | 함수 (입력 → 출력) |
| **상태 관리** | 객체 내부에 캡슐화 | 불변 데이터 + 순수 함수 |
| **데이터 흐름** | 객체 간 메시지 전달 | 함수 파이프라인 |
| **다형성** | 상속, 인터페이스 | 고차 함수, 합성 |
| **부수 효과** | 허용 (캡슐화로 관리) | 최소화 (경계로 분리) |

### 상호 보완적 사용

```java
// OOP + FP 결합 예시
public class OrderService {
    private final OrderRepository repository;  // OOP: 의존성 주입

    // FP: 순수 함수로 비즈니스 로직 구현
    public Money calculateTotal(List<OrderItem> items) {
        return items.stream()
            .map(OrderItem::getPrice)
            .reduce(Money.ZERO, Money::add);
    }

    // OOP: 부수 효과는 서비스 레이어 경계에서 처리
    public Order createOrder(List<OrderItem> items) {
        Money total = calculateTotal(items);  // 순수 함수 호출
        Order order = new Order(items, total);
        return repository.save(order);  // 부수 효과 (I/O)
    }
}
```

## 면접 예상 질문

### Q1. 순수 함수란 무엇이며, 왜 중요한가요?

**모범 답안:**

순수 함수는 두 가지 특성을 만족하는 함수입니다.

**첫째, 참조 투명성(Referential Transparency)**입니다. 동일한 입력에 대해 항상 동일한 출력을 반환합니다. `add(2, 3)`은 언제 어디서 호출해도 항상 `5`를 반환합니다.

**둘째, 부수 효과 없음**입니다. 함수 외부의 상태를 읽거나 변경하지 않습니다. 전역 변수 수정, 파일 I/O, 데이터베이스 접근 등이 없습니다.

순수 함수가 중요한 이유는 다음과 같습니다:

1. **테스트 용이성**: 외부 의존성이 없어 모킹 없이 입출력만 검증하면 됩니다.

2. **병렬 처리 안전**: 공유 상태가 없어 동시성 문제(Race Condition)가 발생하지 않습니다. 여러 스레드에서 동시에 호출해도 안전합니다.

3. **캐싱(메모이제이션)**: 동일 입력 → 동일 출력이 보장되므로 결과를 캐싱하여 성능을 최적화할 수 있습니다.

4. **추론 용이성**: 함수 시그니처만 보고 동작을 예측할 수 있어 코드 이해가 쉽습니다.

실무에서는 모든 함수를 순수하게 만들 수 없지만, 비즈니스 로직은 순수 함수로 작성하고 부수 효과(I/O, DB)는 애플리케이션 경계로 밀어내는 것이 좋습니다.

### Q2. Java Stream의 map과 flatMap의 차이를 설명해주세요.

**모범 답안:**

**map**은 스트림의 각 요소를 1:1로 변환합니다. 함수가 반환하는 값이 그대로 새 스트림의 요소가 됩니다.

```java
List<String> names = List.of("alice", "bob");
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());  // ["ALICE", "BOB"]
```

**flatMap**은 각 요소를 스트림으로 변환한 후 평탄화(flatten)합니다. 중첩 구조를 풀어낼 때 사용합니다.

```java
List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4));

// map 사용 시: Stream<List<Integer>> 반환 (중첩 유지)
List<List<Integer>> mapped = nested.stream()
    .map(list -> list)
    .collect(Collectors.toList());  // [[1,2], [3,4]]

// flatMap 사용 시: Stream<Integer> 반환 (평탄화)
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());  // [1, 2, 3, 4]
```

실무 사용 예시로, 주문 목록에서 모든 상품을 추출할 때 flatMap을 사용합니다:

```java
List<Product> allProducts = orders.stream()
    .flatMap(order -> order.getProducts().stream())
    .collect(Collectors.toList());
```

Optional에서도 동일한 개념이 적용됩니다. `map`은 `Optional<Optional<T>>`를 만들 수 있지만, `flatMap`은 중첩을 풀어 `Optional<T>`를 반환합니다.

### Q3. 불변성(Immutability)의 장단점과 Java에서 불변 객체를 만드는 방법을 설명해주세요.

**모범 답안:**

**불변성의 장점:**

1. **스레드 안전**: 상태가 변경되지 않으므로 동기화 없이 여러 스레드에서 안전하게 공유할 수 있습니다.

2. **예측 가능성**: 객체가 생성된 후 상태가 변하지 않아 디버깅과 추론이 쉽습니다. 어디선가 값이 변경되었는지 추적할 필요가 없습니다.

3. **캐싱 및 재사용**: 상태가 고정되어 있어 해시 값이 변하지 않으므로 HashMap의 키로 안전하게 사용할 수 있습니다.

4. **방어적 복사 불필요**: getter로 내부 상태를 반환해도 외부에서 수정할 수 없습니다.

**단점:**

1. **메모리 사용**: 값을 변경할 때마다 새 객체를 생성하므로 메모리 사용량이 증가할 수 있습니다.

2. **성능 오버헤드**: 빈번한 객체 생성은 GC 부담을 증가시킬 수 있습니다.

**Java에서 불변 객체 만드는 방법:**

```java
public final class ImmutableUser {
    private final String name;
    private final List<String> roles;

    public ImmutableUser(String name, List<String> roles) {
        this.name = name;
        this.roles = List.copyOf(roles);  // 방어적 복사
    }

    public String getName() { return name; }
    public List<String> getRoles() { return roles; }  // 이미 불변
}
```

핵심 규칙은: 클래스를 `final`로 선언, 모든 필드를 `private final`로, 생성자에서만 초기화, setter 제공 안 함, 가변 객체 참조 시 방어적 복사입니다. Java 14+에서는 `record`를 사용하면 간편하게 불변 객체를 만들 수 있습니다.

## 참고 자료

- [Java 8 in Action](https://www.manning.com/books/java-8-in-action)
- [Functional Programming in Java](https://www.oreilly.com/library/view/functional-programming-in/9781941222690/)
- [Oracle - Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
- [Oracle - Stream API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
