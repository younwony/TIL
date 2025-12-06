# List 인터페이스

## 핵심 정리

- List는 순서가 있고 중복을 허용하는 컬렉션
- 인덱스를 통한 요소 접근 가능
- 주요 구현체: ArrayList, LinkedList, Vector

## ArrayList

동적 배열 기반의 List 구현체

### 특징

- 내부적으로 Object[] 배열 사용
- 기본 용량(capacity) 10, 필요시 1.5배씩 증가
- 랜덤 접근이 빠름 (O(1))
- 삽입/삭제 시 요소 이동 필요 (O(n))

### 구현 원리

```java
public class ArrayList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elementData;
    private int size;

    public ArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    public void add(E element) {
        ensureCapacity(size + 1);
        elementData[size++] = element;
    }

    public E remove(int index) {
        rangeCheck(index);
        E oldValue = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1,
                elementData, index, numMoved);
        }
        elementData[--size] = null;
        return oldValue;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elementData.length) {
            int newCapacity = elementData.length + (elementData.length >> 1);
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }
}
```

### 시간 복잡도

| 연산 | 복잡도 |
|------|--------|
| get(index) | O(1) |
| add(E) (끝에 추가) | O(1) 평균 |
| add(index, E) | O(n) |
| remove(index) | O(n) |
| contains(E) | O(n) |

## LinkedList

이중 연결 리스트(Doubly Linked List) 기반 구현체

### 특징

- 각 노드가 이전/다음 노드의 참조를 가짐
- 메모리 오버헤드 있음 (노드당 추가 참조 2개)
- 삽입/삭제가 빠름 (노드 참조 변경만 필요)
- 랜덤 접근이 느림 (처음부터 순회 필요)

### 구현 원리

```java
public class LinkedList<E> {
    private Node<E> first;
    private Node<E> last;
    private int size;

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    public void addFirst(E element) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, element, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    public void addLast(E element) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, element, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public E get(int index) {
        Node<E> node = (index < size / 2) ?
            getFromFirst(index) : getFromLast(index);
        return node.item;
    }
}
```

### 시간 복잡도

| 연산 | 복잡도 |
|------|--------|
| get(index) | O(n) |
| addFirst(E) | O(1) |
| addLast(E) | O(1) |
| removeFirst() | O(1) |
| removeLast() | O(1) |
| remove(index) | O(n) |

## ArrayList vs LinkedList 비교

| 특성 | ArrayList | LinkedList |
|------|-----------|------------|
| 내부 구조 | 동적 배열 | 이중 연결 리스트 |
| 메모리 | 연속적 | 분산적 (노드당 추가 메모리) |
| 랜덤 접근 | O(1) | O(n) |
| 앞에 삽입 | O(n) | O(1) |
| 뒤에 삽입 | O(1) 평균 | O(1) |
| 중간 삽입 | O(n) | O(n) (탐색) + O(1) (삽입) |
| 캐시 효율 | 좋음 (연속 메모리) | 나쁨 (분산 메모리) |

## 사용 가이드

### ArrayList 선택

- 읽기 작업이 많은 경우
- 끝에만 추가/삭제하는 경우
- 요소 개수가 예측 가능한 경우

### LinkedList 선택

- 앞/뒤에서 빈번한 삽입/삭제
- Queue, Deque로 사용하는 경우
- 요소 개수 변동이 큰 경우

## 배열을 List로 변환

```java
// 고정 크기 리스트 (수정 불가)
List<String> list1 = Arrays.asList("a", "b", "c");

// 수정 가능한 리스트
List<String> list2 = new ArrayList<>(Arrays.asList("a", "b", "c"));

// Java 9+
List<String> list3 = List.of("a", "b", "c"); // 불변

// Stream 사용
List<String> list4 = Stream.of("a", "b", "c")
    .collect(Collectors.toList());
```

## 면접 예상 질문

1. **ArrayList와 LinkedList의 차이점은?**
   - ArrayList는 배열 기반으로 랜덤 접근이 빠르고, LinkedList는 연결 리스트 기반으로 삽입/삭제가 빠름

2. **ArrayList의 초기 용량과 확장 방식은?**
   - 기본 용량 10, 용량 초과 시 현재 용량의 1.5배로 확장

3. **Vector와 ArrayList의 차이는?**
   - Vector는 synchronized로 스레드 안전하지만 성능 저하
   - 멀티스레드 환경에서는 Collections.synchronizedList() 또는 CopyOnWriteArrayList 권장
