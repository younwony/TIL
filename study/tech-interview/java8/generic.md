# 제네릭 (Generic)

## 핵심 정리

- 제네릭은 클래스나 메서드에서 사용할 타입을 매개변수화하는 기능
- 컴파일 시점에 타입 체크를 수행하여 타입 안전성 보장
- 불필요한 형변환 제거로 코드 간결성 향상
- 와일드카드를 통한 유연한 타입 처리 지원

## 제네릭 기본

### 제네릭 클래스

```java
// 제네릭 클래스 정의
public class Box<T> {
    private T item;

    public void set(T item) {
        this.item = item;
    }

    public T get() {
        return item;
    }
}

// 사용
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String value = stringBox.get(); // 형변환 불필요

Box<Integer> intBox = new Box<>();
intBox.set(100);
Integer number = intBox.get();
```

### 제네릭 메서드

```java
public class Utility {
    // 제네릭 메서드 정의
    public static <T> T getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    // 여러 타입 파라미터
    public static <K, V> Map<K, V> createMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}

// 사용
List<String> names = Arrays.asList("Alice", "Bob");
String first = Utility.getFirst(names); // 타입 추론

Map<String, Integer> map = Utility.createMap("age", 25);
```

### 제네릭 인터페이스

```java
public interface Comparable<T> {
    int compareTo(T o);
}

public class Person implements Comparable<Person> {
    private String name;
    private int age;

    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }
}
```

## 타입 파라미터 컨벤션

| 파라미터 | 의미 | 사용 예 |
|----------|------|---------|
| T | Type | 일반적인 타입 |
| E | Element | 컬렉션 요소 |
| K | Key | 맵의 키 |
| V | Value | 맵의 값 |
| N | Number | 숫자 타입 |
| R | Result | 반환 타입 |

## 제한된 타입 파라미터 (Bounded Type)

### 상한 제한 (Upper Bound)

```java
// T는 Number의 하위 클래스여야 함
public class NumberBox<T extends Number> {
    private T number;

    public double getDoubleValue() {
        return number.doubleValue(); // Number 메서드 사용 가능
    }
}

// 사용
NumberBox<Integer> intBox = new NumberBox<>();   // OK
NumberBox<Double> doubleBox = new NumberBox<>(); // OK
// NumberBox<String> stringBox = new NumberBox<>(); // 컴파일 에러

// 다중 제한
public <T extends Number & Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```

## 와일드카드 (Wildcard)

### 비한정 와일드카드 (Unbounded)

```java
// 모든 타입 허용
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// 사용
printList(Arrays.asList("a", "b", "c"));
printList(Arrays.asList(1, 2, 3));
```

### 상한 와일드카드 (Upper Bounded)

```java
// Number와 그 하위 타입만 허용
public double sum(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {
        sum += n.doubleValue();
    }
    return sum;
}

// 사용
List<Integer> intList = Arrays.asList(1, 2, 3);
List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
sum(intList);    // OK
sum(doubleList); // OK
```

### 하한 와일드카드 (Lower Bounded)

```java
// Integer와 그 상위 타입만 허용
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

// 사용
List<Integer> intList = new ArrayList<>();
List<Number> numList = new ArrayList<>();
List<Object> objList = new ArrayList<>();
addNumbers(intList); // OK
addNumbers(numList); // OK
addNumbers(objList); // OK
```

## PECS 원칙 (Producer Extends, Consumer Super)

### Producer - extends 사용

```java
// 컬렉션에서 데이터를 읽기만 할 때 (Producer)
public void copyElements(List<? extends T> source, List<T> destination) {
    for (T item : source) {
        destination.add(item);
    }
}
```

### Consumer - super 사용

```java
// 컬렉션에 데이터를 쓰기만 할 때 (Consumer)
public void addElements(List<? super T> destination, T element) {
    destination.add(element);
}
```

### 예시: Collections.copy()

```java
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (int i = 0; i < src.size(); i++) {
        dest.set(i, src.get(i));
    }
}
```

## 타입 소거 (Type Erasure)

컴파일 후 제네릭 타입 정보가 제거되는 현상

### 동작 방식

```java
// 컴파일 전
public class Box<T> {
    private T item;
    public void set(T item) { this.item = item; }
    public T get() { return item; }
}

// 컴파일 후 (타입 소거)
public class Box {
    private Object item;
    public void set(Object item) { this.item = item; }
    public Object get() { return item; }
}
```

### 제한 사항

```java
// 불가능한 것들
public class Example<T> {
    // 타입 파라미터로 인스턴스 생성 불가
    // T instance = new T();

    // 타입 파라미터로 배열 생성 불가
    // T[] array = new T[10];

    // 정적 필드에 타입 파라미터 사용 불가
    // private static T staticField;

    // instanceof에 타입 파라미터 사용 불가
    // if (obj instanceof T) { }
}
```

### 해결 방법

```java
// 클래스 토큰 사용
public class Box<T> {
    private Class<T> type;

    public Box(Class<T> type) {
        this.type = type;
    }

    public T createInstance() throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }

    public boolean isInstance(Object obj) {
        return type.isInstance(obj);
    }
}
```

## 실전 활용 예시

### 제네릭 DAO

```java
public interface GenericDao<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(ID id);
}

public class UserDao implements GenericDao<User, Long> {
    @Override
    public User findById(Long id) { /* ... */ }
    // ...
}
```

### 제네릭 응답 래퍼

```java
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}

// 사용
ApiResponse<User> response = ApiResponse.success(user);
ApiResponse<List<User>> listResponse = ApiResponse.success(users);
```

## 면접 예상 질문

1. **제네릭을 사용하는 이유는?**
   - 컴파일 시 타입 체크로 런타임 에러 방지
   - 불필요한 형변환 제거
   - 코드 재사용성 향상

2. **PECS 원칙이란?**
   - Producer Extends, Consumer Super
   - 읽기만 할 때는 extends, 쓰기만 할 때는 super 사용
   - 읽기/쓰기 모두 필요하면 와일드카드 사용 불가

3. **타입 소거(Type Erasure)란?**
   - 컴파일 시 제네릭 타입 정보가 제거됨
   - 하위 호환성을 위해 도입
   - 런타임에 타입 정보를 알 수 없는 제한 발생
