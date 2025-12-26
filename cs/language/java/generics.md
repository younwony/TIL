# Java 제네릭 (Generics)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](../what-is-language.md), [컬렉션 프레임워크](./collection-framework.md)

> 타입을 파라미터화하여 컴파일 시점에 타입 안전성을 보장하는 기능

`#제네릭` `#Generics` `#Java` `#타입파라미터` `#TypeParameter` `#타입안전성` `#TypeSafety` `#컴파일타임` `#CompileTime` `#타입소거` `#TypeErasure` `#와일드카드` `#Wildcard` `#extends` `#super` `#공변` `#Covariant` `#반공변` `#Contravariant` `#PECS` `#ProducerExtends` `#ConsumerSuper` `#제네릭메서드` `#제네릭클래스` `#바운드` `#Bounded`

## 왜 알아야 하는가?

제네릭은 컴파일 시점에 타입 오류를 잡아내어 런타임 ClassCastException을 방지합니다. 컬렉션, 라이브러리 API, 프레임워크 모두 제네릭을 사용하므로 Java 개발에 필수입니다. 또한 코드 재사용성을 높여줍니다.

## 핵심 개념

- **타입 파라미터**: 타입을 변수처럼 사용 (`<T>`, `<E>`, `<K, V>`)
- **타입 소거 (Type Erasure)**: 컴파일 후 제네릭 정보 제거
- **와일드카드**: 알 수 없는 타입 (`<?>`)
- **경계 (Bound)**: 타입 범위 제한 (`<T extends Number>`)

## 쉽게 이해하기

**제네릭**을 만능 상자에 비유할 수 있습니다.

```
제네릭 없이 (Object 사용):
┌──────────────────────────────────────────────┐
│  Box (무엇이든 담는 상자)                      │
│  - 넣을 때: 아무거나 OK                        │
│  - 꺼낼 때: 뭐가 나올지 모름 → 캐스팅 필요     │
│  - 위험: ClassCastException 가능             │
└──────────────────────────────────────────────┘

제네릭 사용:
┌──────────────────────────────────────────────┐
│  Box<Apple> (사과 전용 상자)                   │
│  - 넣을 때: Apple만 가능                       │
│  - 꺼낼 때: Apple 확정 → 캐스팅 불필요         │
│  - 안전: 컴파일러가 타입 검사                  │
└──────────────────────────────────────────────┘
```

## 상세 설명

### 제네릭 클래스

```java
// 제네릭 없이
public class BoxOld {
    private Object item;

    public void set(Object item) {
        this.item = item;
    }

    public Object get() {
        return item;
    }
}

// 사용
BoxOld box = new BoxOld();
box.set("Hello");
String s = (String) box.get();  // 캐스팅 필요
box.set(123);
String s2 = (String) box.get(); // RuntimeException!

// 제네릭 사용
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
Box<String> box = new Box<>();
box.set("Hello");
String s = box.get();  // 캐스팅 불필요
box.set(123);          // 컴파일 에러!
```

### 제네릭 메서드

```java
public class Util {
    // 제네릭 메서드: 메서드 레벨에서 타입 파라미터 정의
    public static <T> T getFirst(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    // 여러 타입 파라미터
    public static <K, V> V getValue(Map<K, V> map, K key) {
        return map.get(key);
    }
}

// 사용
List<String> names = Arrays.asList("Alice", "Bob");
String first = Util.getFirst(names);  // 타입 추론
String first2 = Util.<String>getFirst(names);  // 명시적 지정
```

### 경계가 있는 타입 파라미터 (Bounded Type)

```java
// 상한 경계 (Upper Bound): T는 Number 또는 그 하위 타입
public class Calculator<T extends Number> {
    private T value;

    public double doubleValue() {
        return value.doubleValue();  // Number 메서드 사용 가능
    }
}

Calculator<Integer> calc = new Calculator<>();  // OK
Calculator<String> calc2 = new Calculator<>();  // 컴파일 에러

// 다중 경계
public <T extends Comparable<T> & Serializable> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### 와일드카드 (Wildcard)

```java
// 무제한 와일드카드: 모든 타입
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// 상한 경계 와일드카드: Number 또는 하위 타입
public double sum(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {
        sum += n.doubleValue();
    }
    return sum;
}

// 하한 경계 와일드카드: Integer 또는 상위 타입
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}
```

### PECS 원칙

```
┌─────────────────────────────────────────────────────────────┐
│  PECS: Producer Extends, Consumer Super                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Producer (데이터를 제공할 때): <? extends T>                │
│  ────────────────────────────────────────                   │
│  읽기만 가능, 쓰기 불가                                       │
│                                                              │
│  List<? extends Number> producer = new ArrayList<Integer>();│
│  Number n = producer.get(0);  // OK (읽기)                  │
│  producer.add(1);             // 컴파일 에러 (쓰기 불가)     │
│                                                              │
│  Consumer (데이터를 소비할 때): <? super T>                  │
│  ────────────────────────────────────────                   │
│  쓰기 가능, 읽기 시 Object만                                 │
│                                                              │
│  List<? super Integer> consumer = new ArrayList<Number>();  │
│  consumer.add(1);             // OK (쓰기)                  │
│  Object o = consumer.get(0);  // Object로만 읽기             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

```java
// PECS 실제 적용 예: Collections.copy()
public static <T> void copy(List<? super T> dest,
                            List<? extends T> src) {
    for (T item : src) {      // src에서 읽기 (Producer)
        dest.add(item);        // dest에 쓰기 (Consumer)
    }
}
```

### 타입 소거 (Type Erasure)

```java
// 컴파일 전
public class Box<T> {
    private T item;
    public T get() { return item; }
}

// 컴파일 후 (타입 소거)
public class Box {
    private Object item;
    public Object get() { return item; }
}

// 제약 사항
public class Example<T> {
    // 불가능한 것들
    private T[] array = new T[10];           // 컴파일 에러
    if (obj instanceof T) { }                 // 컴파일 에러
    T instance = new T();                     // 컴파일 에러

    // 가능한 것들
    @SuppressWarnings("unchecked")
    private T[] array = (T[]) new Object[10]; // 우회 (주의 필요)
}

// 런타임에 제네릭 타입 정보 없음
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
System.out.println(strings.getClass() == integers.getClass()); // true
```

### 제네릭과 상속

```java
// 제네릭 타입은 공변(covariant)이 아님
List<Object> objects = new ArrayList<String>();  // 컴파일 에러!

// 왜?
List<String> strings = new ArrayList<>();
List<Object> objects = strings;  // 만약 허용된다면...
objects.add(123);                // Integer 추가
String s = strings.get(0);       // ClassCastException!

// 배열은 공변 (런타임 에러)
Object[] objects = new String[10];  // 컴파일 OK
objects[0] = 123;                   // ArrayStoreException (런타임)

// 와일드카드로 공변/반공변 표현
List<? extends Number> covariant;   // 공변 (읽기 전용)
List<? super Integer> contravariant; // 반공변 (쓰기 전용)
```

### 실무 패턴

```java
// 팩토리 패턴
public interface Factory<T> {
    T create();
}

public class PersonFactory implements Factory<Person> {
    @Override
    public Person create() {
        return new Person();
    }
}

// 빌더 패턴
public class Builder<T extends Builder<T>> {
    protected String name;

    @SuppressWarnings("unchecked")
    public T name(String name) {
        this.name = name;
        return (T) this;
    }
}

public class PersonBuilder extends Builder<PersonBuilder> {
    private int age;

    public PersonBuilder age(int age) {
        this.age = age;
        return this;
    }
}

// 타입 토큰
public <T> T getService(Class<T> type) {
    return type.cast(services.get(type));
}

PersonService service = container.getService(PersonService.class);
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 컴파일 타임 타입 체크 | 타입 소거로 런타임 정보 제한 |
| 캐스팅 불필요 | 복잡한 문법 |
| 코드 재사용성 | 원시 타입 사용 불가 |
| API 표현력 향상 | 디버깅 어려움 |

## 면접 예상 질문

### Q: 제네릭의 타입 소거(Type Erasure)란?

A: 제네릭 타입 정보는 컴파일 시점에만 존재하고, 런타임에는 제거됩니다. `List<String>`과 `List<Integer>`는 런타임에 모두 `List`로 동일합니다. **이유**: 하위 호환성 (Java 5 이전 코드와 호환). **제약**: `new T()`, `instanceof T`, 제네릭 배열 생성 불가. **우회**: 타입 토큰(`Class<T>`), 수퍼 타입 토큰.

### Q: `List<?>`와 `List<Object>`의 차이는?

A: **`List<?>`**: 어떤 타입인지 모르는 리스트. 읽기만 가능(Object로), 쓰기 불가(null 제외). **`List<Object>`**: Object 타입의 리스트. 읽기/쓰기 모두 가능. **차이**: `List<String>`을 `List<?>`에 할당 가능, `List<Object>`에는 불가. **사용**: 읽기 전용이면 `?`, 쓰기 필요하면 구체적 타입.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [컬렉션 프레임워크](./collection-framework.md) | 제네릭 활용 | [3] 중급 |
| [리플렉션](./reflection.md) | 타입 정보 | [4] 심화 |

## 참고 자료

- [Java Generics Tutorial - Oracle](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Effective Java - Item 26-33](https://www.oreilly.com/library/view/effective-java/9780134686097/)
