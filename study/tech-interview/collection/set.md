# Set 인터페이스

## 핵심 정리

- Set은 중복을 허용하지 않는 컬렉션
- 순서 보장 여부는 구현체에 따라 다름
- 주요 구현체: HashSet, LinkedHashSet, TreeSet

## HashSet

HashMap을 기반으로 한 Set 구현체

### 특징

- 내부적으로 HashMap 사용 (값은 더미 객체)
- 순서를 보장하지 않음
- null 값 허용 (하나만)
- O(1) 시간 복잡도로 조회/삽입/삭제

### 구현 원리

```java
public class HashSet<E> {
    private static final Object PRESENT = new Object();
    private final HashMap<E, Object> map;

    public HashSet() {
        map = new HashMap<>();
    }

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public int size() {
        return map.size();
    }
}
```

### 동등성 판단

```java
public class Person {
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

**중요**: HashSet에서 객체를 올바르게 비교하려면 `equals()`와 `hashCode()`를 반드시 함께 오버라이드해야 함

## LinkedHashSet

삽입 순서를 유지하는 HashSet

### 특징

- LinkedHashMap을 내부에 사용
- 삽입 순서대로 순회 가능
- HashSet보다 약간의 오버헤드

### 사용 예시

```java
Set<String> set = new LinkedHashSet<>();
set.add("C");
set.add("A");
set.add("B");

for (String s : set) {
    System.out.println(s); // C, A, B (삽입 순서)
}
```

## TreeSet

정렬된 순서를 유지하는 Set

### 특징

- 내부적으로 TreeMap(Red-Black Tree) 사용
- 요소들이 자동으로 정렬됨
- null 값 허용하지 않음
- O(log n) 시간 복잡도

### 사용 예시

```java
Set<Integer> set = new TreeSet<>();
set.add(5);
set.add(1);
set.add(3);

for (Integer i : set) {
    System.out.println(i); // 1, 3, 5 (정렬됨)
}

// 범위 검색
TreeSet<Integer> treeSet = new TreeSet<>(set);
System.out.println(treeSet.headSet(3));  // [1]
System.out.println(treeSet.tailSet(3));  // [3, 5]
System.out.println(treeSet.subSet(1, 5)); // [1, 3]
```

### Comparator 사용

```java
// 역순 정렬
Set<Integer> descSet = new TreeSet<>(Comparator.reverseOrder());

// 커스텀 정렬
Set<Person> personSet = new TreeSet<>(
    Comparator.comparing(Person::getAge)
              .thenComparing(Person::getName)
);
```

## Set 구현체 비교

| 특성 | HashSet | LinkedHashSet | TreeSet |
|------|---------|---------------|---------|
| 순서 | 없음 | 삽입 순서 | 정렬 순서 |
| null 허용 | O (1개) | O (1개) | X |
| 내부 구조 | HashMap | LinkedHashMap | TreeMap (RB-Tree) |
| add/remove | O(1) | O(1) | O(log n) |
| contains | O(1) | O(1) | O(log n) |
| 메모리 | 적음 | 중간 | 많음 |

## 사용 가이드

### HashSet 선택

- 순서가 필요 없는 경우
- 빠른 조회/삽입/삭제가 필요한 경우
- 중복 제거만 필요한 경우

### LinkedHashSet 선택

- 삽입 순서 유지가 필요한 경우
- 순회 시 일정한 순서 보장이 필요한 경우

### TreeSet 선택

- 정렬된 상태 유지가 필요한 경우
- 범위 검색이 필요한 경우
- 최솟값/최댓값 접근이 자주 필요한 경우

## Set 연산

```java
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3));
Set<Integer> set2 = new HashSet<>(Arrays.asList(2, 3, 4));

// 합집합
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2); // [1, 2, 3, 4]

// 교집합
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2); // [2, 3]

// 차집합
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2); // [1]
```

## 면접 예상 질문

1. **HashSet에 객체를 저장할 때 주의할 점은?**
   - equals()와 hashCode()를 반드시 함께 오버라이드해야 함
   - hashCode가 같아도 equals가 다르면 다른 객체로 처리

2. **TreeSet의 내부 구조는 무엇인가요?**
   - Red-Black Tree (자가 균형 이진 탐색 트리)
   - 삽입/삭제 시 트리 균형을 유지하여 O(log n) 보장

3. **HashSet과 TreeSet 중 어떤 것을 선택해야 하나요?**
   - 정렬이 필요 없고 빠른 연산이 필요하면 HashSet
   - 정렬이나 범위 검색이 필요하면 TreeSet
