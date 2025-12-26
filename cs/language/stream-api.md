# Java Stream API

> `[3] 중급` · 선수 지식: [컬렉션 프레임워크](./collection-framework.md), [함수형 프로그래밍](../programming/functional-programming.md)

> 컬렉션 데이터를 선언적으로 처리하는 함수형 스타일의 API

`#StreamAPI` `#스트림` `#Stream` `#Java8` `#함수형프로그래밍` `#FunctionalProgramming` `#람다` `#Lambda` `#map` `#filter` `#reduce` `#collect` `#중간연산` `#IntermediateOperation` `#최종연산` `#TerminalOperation` `#지연평가` `#LazyEvaluation` `#병렬스트림` `#ParallelStream` `#Collectors` `#Optional` `#파이프라인` `#Pipeline` `#선언적프로그래밍`

## 왜 알아야 하는가?

Stream API는 Java 8의 핵심 기능으로, 컬렉션 데이터를 간결하고 선언적으로 처리할 수 있게 합니다. for 루프 대신 map, filter, reduce 같은 고차 함수를 사용해 가독성이 높고 유지보수하기 쉬운 코드를 작성할 수 있습니다. 현대 Java 개발의 필수 스킬입니다.

## 핵심 개념

- **Stream**: 데이터의 흐름, 원본 데이터를 변경하지 않음
- **중간 연산**: 스트림을 변환 (map, filter, sorted)
- **최종 연산**: 결과를 생성 (collect, reduce, forEach)
- **지연 평가**: 최종 연산 호출 전까지 실행되지 않음

## 쉽게 이해하기

**Stream**을 공장 컨베이어 벨트에 비유할 수 있습니다.

```
원재료(Collection) → 컨베이어 벨트(Stream) → 완제품(Result)

┌─────────────────────────────────────────────────────────────┐
│                    Stream 파이프라인                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  List<Person>                                                │
│       ↓                                                      │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐        │
│  │ filter  │→│   map   │→│ sorted  │→│ collect │        │
│  │ (조건)  │  │ (변환)  │  │ (정렬)  │  │ (수집)  │        │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘        │
│   중간 연산     중간 연산     중간 연산     최종 연산         │
│       ↓                                                      │
│  List<String>                                                │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 스트림 생성

```java
// 컬렉션에서 생성
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> stream = list.stream();

// 배열에서 생성
String[] array = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(array);

// 직접 생성
Stream<String> stream = Stream.of("a", "b", "c");

// 빈 스트림
Stream<String> empty = Stream.empty();

// 무한 스트림
Stream<Integer> infinite = Stream.iterate(0, n -> n + 2); // 0, 2, 4, 6...
Stream<Double> random = Stream.generate(Math::random);

// 범위 스트림
IntStream range = IntStream.range(1, 5);     // 1, 2, 3, 4
IntStream rangeClosed = IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5
```

### 중간 연산 (Intermediate Operations)

```java
List<Person> people = getPeople();

// filter: 조건에 맞는 요소만 선택
people.stream()
    .filter(p -> p.getAge() >= 18)

// map: 각 요소를 변환
people.stream()
    .map(Person::getName)  // Person → String

// flatMap: 중첩 구조를 평탄화
List<List<String>> nested = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d")
);
nested.stream()
    .flatMap(List::stream)  // Stream<List<String>> → Stream<String>
    // 결과: "a", "b", "c", "d"

// sorted: 정렬
people.stream()
    .sorted(Comparator.comparing(Person::getAge))

// distinct: 중복 제거
Stream.of(1, 2, 2, 3, 3, 3)
    .distinct()  // 1, 2, 3

// limit: 개수 제한
people.stream()
    .limit(5)

// skip: 처음 n개 건너뛰기
people.stream()
    .skip(2)

// peek: 디버깅용 (부작용 수행)
people.stream()
    .peek(p -> System.out.println("처리 중: " + p))
```

### 최종 연산 (Terminal Operations)

```java
List<Person> people = getPeople();

// collect: 결과를 컬렉션으로 수집
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// forEach: 각 요소에 대해 작업 수행
people.stream()
    .forEach(System.out::println);

// reduce: 요소들을 하나로 결합
int sum = IntStream.rangeClosed(1, 10)
    .reduce(0, Integer::sum);  // 55

// count: 개수 세기
long count = people.stream()
    .filter(p -> p.getAge() >= 18)
    .count();

// findFirst / findAny: 첫 번째 / 아무 요소
Optional<Person> first = people.stream()
    .filter(p -> p.getAge() >= 18)
    .findFirst();

// anyMatch / allMatch / noneMatch: 조건 검사
boolean hasAdult = people.stream()
    .anyMatch(p -> p.getAge() >= 18);

// min / max
Optional<Person> oldest = people.stream()
    .max(Comparator.comparing(Person::getAge));

// toArray: 배열로 변환
String[] nameArray = people.stream()
    .map(Person::getName)
    .toArray(String[]::new);
```

### Collectors 활용

```java
List<Person> people = getPeople();

// 리스트로 수집
List<String> list = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());

// Set으로 수집
Set<String> set = people.stream()
    .map(Person::getName)
    .collect(Collectors.toSet());

// Map으로 수집
Map<Long, String> map = people.stream()
    .collect(Collectors.toMap(
        Person::getId,
        Person::getName
    ));

// 그룹핑
Map<Integer, List<Person>> byAge = people.stream()
    .collect(Collectors.groupingBy(Person::getAge));

// 그룹핑 + 집계
Map<Integer, Long> countByAge = people.stream()
    .collect(Collectors.groupingBy(
        Person::getAge,
        Collectors.counting()
    ));

// 파티셔닝 (boolean 기준)
Map<Boolean, List<Person>> partition = people.stream()
    .collect(Collectors.partitioningBy(
        p -> p.getAge() >= 18
    ));

// 문자열 조인
String joined = people.stream()
    .map(Person::getName)
    .collect(Collectors.joining(", "));  // "Alice, Bob, Charlie"

// 통계
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
// stats.getAverage(), stats.getMax(), stats.getMin(), stats.getSum()
```

### 지연 평가 (Lazy Evaluation)

```java
// 중간 연산은 즉시 실행되지 않음
Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5)
    .filter(n -> {
        System.out.println("filter: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("map: " + n);
        return n * 2;
    });

System.out.println("아직 아무것도 출력 안 됨");

// 최종 연산 호출 시 실행
List<Integer> result = stream.collect(Collectors.toList());

// 출력:
// 아직 아무것도 출력 안 됨
// filter: 1
// filter: 2
// map: 2
// filter: 3
// filter: 4
// map: 4
// filter: 5
```

### 병렬 스트림 (Parallel Stream)

```java
// 병렬 스트림 생성
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.parallelStream()
    .forEach(System.out::println);  // 순서 보장 안 됨

// 순차 스트림을 병렬로 변환
numbers.stream()
    .parallel()
    .forEach(System.out::println);

// 병렬 처리에 적합한 경우
long sum = LongStream.rangeClosed(1, 10_000_000)
    .parallel()
    .sum();

// 주의: 병렬 스트림 사용 시 고려사항
// 1. 데이터 양이 충분히 많아야 함
// 2. 요소 처리 비용이 높아야 함
// 3. 순서가 중요하면 부적합
// 4. 공유 상태 변경하면 안 됨
```

### 실무 예제

```java
// 예제 1: 주문 데이터 처리
List<Order> orders = getOrders();

// 2024년 주문 중 총액 상위 5개
List<Order> top5 = orders.stream()
    .filter(o -> o.getYear() == 2024)
    .sorted(Comparator.comparing(Order::getTotal).reversed())
    .limit(5)
    .collect(Collectors.toList());

// 카테고리별 매출 합계
Map<String, Long> salesByCategory = orders.stream()
    .collect(Collectors.groupingBy(
        Order::getCategory,
        Collectors.summingLong(Order::getTotal)
    ));

// 예제 2: 중첩 데이터 처리
List<Department> departments = getDepartments();

// 모든 부서의 직원 이름 목록
List<String> allEmployeeNames = departments.stream()
    .flatMap(d -> d.getEmployees().stream())
    .map(Employee::getName)
    .distinct()
    .sorted()
    .collect(Collectors.toList());

// 예제 3: Optional과 함께 사용
Optional<Employee> highestPaid = departments.stream()
    .flatMap(d -> d.getEmployees().stream())
    .max(Comparator.comparing(Employee::getSalary));

highestPaid.ifPresent(e ->
    System.out.println("최고 연봉: " + e.getName())
);
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 가독성/간결성 | 디버깅 어려움 |
| 선언적 코드 | 학습 곡선 |
| 병렬화 용이 | 단순 반복에는 오버헤드 |
| 체이닝으로 표현력 향상 | 재사용 불가 (일회성) |

## 면접 예상 질문

### Q: Stream과 Collection의 차이는?

A: **Collection**: 데이터를 저장하는 자료구조, 모든 요소가 메모리에 존재. **Stream**: 데이터의 흐름, 요소를 필요할 때 계산(지연 평가). **주요 차이**: (1) Stream은 원본 데이터를 변경하지 않음 (2) Stream은 일회용 (소비 후 재사용 불가) (3) Stream은 지연 평가로 효율적.

### Q: 병렬 스트림은 언제 사용해야 하나요?

A: **적합한 경우**: (1) 데이터 양이 많음 (수만 건 이상) (2) 요소 처리 비용이 높음 (3) 순서가 중요하지 않음 (4) 공유 상태 없음. **부적합한 경우**: (1) 소량 데이터 (2) 순서 의존적 처리 (3) I/O 작업 (블로킹). **주의**: 무조건 빠르지 않음, 벤치마크 필수.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [컬렉션 프레임워크](./collection-framework.md) | 선수 지식 | [3] 중급 |
| [함수형 프로그래밍](../programming/functional-programming.md) | 개념 | [3] 중급 |
| [Java 버전별 기능](./java-versions.md) | Java 8 기능 | [3] 중급 |

## 참고 자료

- [Java Stream API - Oracle Docs](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Modern Java in Action](https://www.manning.com/books/modern-java-in-action)
