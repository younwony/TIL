# 자료구조 (Stack, Queue, Deque, PriorityQueue)

## 핵심 정리

- Stack: LIFO (Last In First Out)
- Queue: FIFO (First In First Out)
- Deque: 양쪽 끝에서 삽입/삭제 가능
- PriorityQueue: 우선순위에 따라 요소 정렬

## Stack

후입선출(LIFO) 자료구조

### 특징

- Java의 Stack 클래스는 Vector를 상속 (레거시)
- Deque 인터페이스 사용 권장

```java
// 레거시 방식 (비권장)
Stack<Integer> stack = new Stack<>();

// 권장 방식
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);
stack.push(2);
stack.pop();    // 2
stack.peek();   // 1
```

### 주요 연산

| 연산 | 설명 | 시간 복잡도 |
|------|------|-------------|
| push(E) | 요소 추가 | O(1) |
| pop() | 요소 제거 및 반환 | O(1) |
| peek() | 최상위 요소 조회 | O(1) |
| isEmpty() | 비어있는지 확인 | O(1) |

### 활용 사례

```java
// 괄호 유효성 검사
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character, Character> pairs = Map.of(')', '(', '}', '{', ']', '[');

    for (char c : s.toCharArray()) {
        if (pairs.containsValue(c)) {
            stack.push(c);
        } else if (pairs.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                return false;
            }
        }
    }
    return stack.isEmpty();
}
```

## Queue

선입선출(FIFO) 자료구조

### 특징

- Queue는 인터페이스로, 구현체를 선택하여 사용
- LinkedList, ArrayDeque 등으로 구현 가능

```java
Queue<Integer> queue = new LinkedList<>();
// 또는
Queue<Integer> queue = new ArrayDeque<>();

queue.offer(1);    // 추가 (성공 시 true)
queue.offer(2);
queue.poll();      // 제거 및 반환 (1)
queue.peek();      // 조회 (2)
```

### 주요 메서드

| 연산 | 예외 발생 | 특수 값 반환 |
|------|-----------|--------------|
| 삽입 | add(e) | offer(e) → boolean |
| 제거 | remove() | poll() → null |
| 조회 | element() | peek() → null |

### 활용 사례

```java
// BFS (너비 우선 탐색)
public void bfs(Node start) {
    Queue<Node> queue = new ArrayDeque<>();
    Set<Node> visited = new HashSet<>();

    queue.offer(start);
    visited.add(start);

    while (!queue.isEmpty()) {
        Node current = queue.poll();
        process(current);

        for (Node neighbor : current.getNeighbors()) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.offer(neighbor);
            }
        }
    }
}
```

## Deque (Double-Ended Queue)

양쪽 끝에서 삽입/삭제가 가능한 자료구조

### 특징

- Stack과 Queue 모두로 사용 가능
- ArrayDeque, LinkedList로 구현 가능
- ArrayDeque가 일반적으로 더 빠름

```java
Deque<Integer> deque = new ArrayDeque<>();

// Stack처럼 사용
deque.push(1);      // 앞에 추가
deque.pop();        // 앞에서 제거

// Queue처럼 사용
deque.offerLast(1); // 뒤에 추가
deque.pollFirst();  // 앞에서 제거

// 양방향 연산
deque.offerFirst(1);  // 앞에 추가
deque.offerLast(2);   // 뒤에 추가
deque.pollFirst();    // 앞에서 제거
deque.pollLast();     // 뒤에서 제거
deque.peekFirst();    // 앞 조회
deque.peekLast();     // 뒤 조회
```

### 시간 복잡도 비교

| 연산 | ArrayDeque | LinkedList |
|------|------------|------------|
| addFirst/addLast | O(1) | O(1) |
| removeFirst/removeLast | O(1) | O(1) |
| get(index) | O(n) | O(n) |
| 메모리 효율 | 좋음 | 나쁨 |

## PriorityQueue

우선순위 기반의 큐

### 특징

- 힙(Heap) 자료구조 기반
- 기본적으로 최소 힙 (작은 값이 높은 우선순위)
- null 요소 허용하지 않음

```java
// 최소 힙 (기본)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(3);
minHeap.offer(1);
minHeap.offer(2);
minHeap.poll();  // 1 (가장 작은 값)

// 최대 힙
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.offer(3);
maxHeap.offer(1);
maxHeap.offer(2);
maxHeap.poll();  // 3 (가장 큰 값)

// 커스텀 정렬
PriorityQueue<int[]> pq = new PriorityQueue<>(
    Comparator.comparingInt(a -> a[0])
);
```

### 시간 복잡도

| 연산 | 복잡도 |
|------|--------|
| offer(E) | O(log n) |
| poll() | O(log n) |
| peek() | O(1) |
| contains(E) | O(n) |
| remove(E) | O(n) |

### 활용 사례

```java
// K번째 큰 요소 찾기
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll();
        }
    }

    return minHeap.peek();
}

// 작업 스케줄링
class Task implements Comparable<Task> {
    int priority;
    String name;

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}

PriorityQueue<Task> taskQueue = new PriorityQueue<>();
```

## 자료구조 선택 가이드

| 상황 | 추천 자료구조 |
|------|---------------|
| LIFO 필요 | ArrayDeque (Stack처럼) |
| FIFO 필요 | ArrayDeque 또는 LinkedList |
| 양방향 접근 | ArrayDeque |
| 우선순위 처리 | PriorityQueue |
| 스레드 안전 필요 | ConcurrentLinkedQueue, LinkedBlockingQueue |

## 면접 예상 질문

1. **Stack 클래스 대신 ArrayDeque를 권장하는 이유는?**
   - Stack은 Vector를 상속하여 불필요한 동기화 오버헤드
   - ArrayDeque가 더 빠르고 메모리 효율적

2. **PriorityQueue의 내부 구조는?**
   - 배열 기반의 이진 힙(Binary Heap)
   - 완전 이진 트리 형태로 유지

3. **ArrayDeque와 LinkedList 중 어떤 것을 선택해야 하나요?**
   - 대부분의 경우 ArrayDeque가 빠름 (캐시 효율)
   - 중간 삽입/삭제가 필요하면 LinkedList
