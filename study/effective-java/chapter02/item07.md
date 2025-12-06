# Item 7: 다 쓴 객체 참조를 해제하라

## 핵심 정리

자바에 가비지 컬렉터가 있다고 해서 메모리 관리에 더 이상 신경 쓰지 않아도 되는 것은 아니다.

## 메모리 누수 예제

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    // 메모리 누수가 일어나는 pop 메서드
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];  // 다 쓴 참조를 그대로 둠
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

## 해결책: 다 쓴 참조를 null 처리

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null;  // 다 쓴 참조 해제
    return result;
}
```

## null 처리의 이점

1. 실수로 null 처리한 참조를 사용하려 하면 프로그램은 즉시 `NullPointerException`을 던지며 종료한다
2. 프로그램 오류는 가능한 한 조기에 발견하는 게 좋다

## 언제 null 처리를 해야 하는가?

- 객체 참조를 null 처리하는 일은 예외적인 경우여야 한다
- **자기 메모리를 직접 관리하는 클래스**라면 프로그래머는 항시 메모리 누수에 주의해야 한다

## 메모리 누수의 주범

### 1. 자기 메모리를 직접 관리하는 클래스
- 위의 Stack 예제처럼 elements 배열로 저장소 풀을 만들어 원소들을 관리하는 경우

### 2. 캐시
- 객체 참조를 캐시에 넣고 그 객체를 다 쓴 뒤로도 한참을 그냥 놔두는 일
- 해결책:
  - `WeakHashMap`을 사용해 캐시를 만든다
  - 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식 (ScheduledThreadPoolExecutor 등)
  - 새 엔트리를 추가할 때 부수 작업으로 수행 (LinkedHashMap의 removeEldestEntry)

### 3. 리스너(listener) 혹은 콜백(callback)
- 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는 경우
- 해결책: 콜백을 약한 참조(weak reference)로 저장

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_7](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_7)
