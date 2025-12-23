# 스택과 큐 (Stack & Queue)

> `[2] 입문` · 선수 지식: [배열 (Array)](./array.md) 또는 [연결 리스트 (Linked List)](./linked-list.md)

> 데이터의 삽입과 삭제 순서가 정해진 선형 자료구조

`#스택` `#Stack` `#큐` `#Queue` `#LIFO` `#LastInFirstOut` `#FIFO` `#FirstInFirstOut` `#덱` `#Deque` `#DoubleEndedQueue` `#자료구조` `#DataStructure` `#DFS` `#BFS` `#Push` `#Pop` `#Enqueue` `#Dequeue` `#Peek` `#Top` `#재귀` `#Recursion` `#콜스택` `#CallStack` `#괄호검사` `#실행취소` `#Undo` `#원형큐` `#CircularQueue`

## 왜 알아야 하는가?

- **실무**: 스택은 함수 호출, 실행 취소, 괄호 검사 등에 사용되며, 큐는 작업 스케줄링, 메시지 처리, 이벤트 시스템에 필수적입니다. 실무에서 매우 자주 사용되는 자료구조입니다.
- **면접**: DFS(스택), BFS(큐) 등 그래프 탐색 알고리즘의 기반이며, "두 스택으로 큐 구현" 같은 응용 문제도 자주 출제됩니다.
- **기반 지식**: LIFO와 FIFO 개념은 운영체제(프로세스 스케줄링), 컴파일러(구문 분석), 네트워크(패킷 처리) 등 다양한 분야의 기초입니다.

## 핵심 개념

- **스택 (Stack)**: LIFO (Last In First Out) - 마지막에 들어온 데이터가 먼저 나감
- **큐 (Queue)**: FIFO (First In First Out) - 먼저 들어온 데이터가 먼저 나감
- **덱 (Deque)**: 양쪽 끝에서 삽입/삭제가 모두 가능한 자료구조
- **제한된 접근**: 특정 위치(끝 또는 앞)에서만 삽입/삭제 가능
- **O(1) 연산**: push, pop, enqueue, dequeue 모두 O(1) 시간 복잡도

## 쉽게 이해하기

### 스택 (Stack)

**스택**을 접시 쌓기에 비유할 수 있습니다.

식당에서 깨끗한 접시를 쌓아놓는 상황을 생각해보세요. 새로 씻은 접시는 맨 위에 올려놓고 (push), 사용할 때는 맨 위 접시를 꺼냅니다 (pop). 맨 아래 접시를 꺼내려면 위에 있는 접시들을 전부 들어야 하므로 불가능합니다.

- 첫 번째 쌓은 접시 → 마지막에 사용
- 마지막에 쌓은 접시 → 먼저 사용

이것이 LIFO (Last In First Out)입니다.

### 큐 (Queue)

**큐**를 줄서기에 비유할 수 있습니다.

은행 창구에 줄을 서는 상황을 생각해보세요. 먼저 온 사람이 맨 앞에 서고 (enqueue), 창구가 열리면 맨 앞 사람이 먼저 업무를 봅니다 (dequeue). 새로 온 사람은 맨 뒤에 줄을 서야 하고, 중간에 끼어들 수 없습니다.

- 먼저 온 사람 → 먼저 나감
- 나중에 온 사람 → 나중에 나감

이것이 FIFO (First In First Out)입니다.

### 덱 (Deque)

**덱**을 양쪽 문이 있는 지하철에 비유할 수 있습니다.

양쪽 문이 있는 지하철은 앞문과 뒷문 모두에서 승객이 타고 내릴 수 있습니다. 마찬가지로 덱은 앞과 뒤 모두에서 삽입/삭제가 가능합니다.

## 상세 설명

## 스택 (Stack)

### 기본 연산

| 연산 | 설명 | 시간 복잡도 |
|------|------|------------|
| push(item) | 스택 맨 위에 요소 추가 | O(1) |
| pop() | 스택 맨 위 요소 제거 및 반환 | O(1) |
| peek() / top() | 스택 맨 위 요소 조회 (제거 X) | O(1) |
| isEmpty() | 스택이 비어있는지 확인 | O(1) |
| size() | 스택에 저장된 요소 개수 | O(1) |

**왜 O(1)인가?**

맨 위(top) 위치만 관리하면 되므로, 모든 연산이 즉시 처리됩니다. 중간 요소를 이동할 필요가 없기 때문입니다.

### 스택 구현 방법

#### 배열 기반 스택

```java
class ArrayStack {
    private int[] data;
    private int top;      // 다음 삽입 위치
    private int capacity;

    public ArrayStack(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
        this.top = 0;
    }

    // O(1)
    public void push(int item) {
        if (top == capacity) {
            throw new RuntimeException("Stack Overflow");
        }
        data[top++] = item;
    }

    // O(1)
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow");
        }
        return data[--top];
    }

    // O(1)
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return data[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }
}
```

**장점**: 간단한 구현, 캐시 친화적
**단점**: 고정 크기, Stack Overflow 가능

#### 연결 리스트 기반 스택

```java
class LinkedStack {
    private Node top;
    private int size;

    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    // O(1)
    public void push(int item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // O(1)
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow");
        }
        int item = top.data;
        top = top.next;
        size--;
        return item;
    }

    // O(1)
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
```

**장점**: 동적 크기, 메모리 효율적
**단점**: 포인터 추가 공간, 캐시 성능 낮음

### 스택 활용 사례

**1. 함수 호출 스택 (Call Stack)**

프로그램 실행 시 함수 호출 관리에 사용됩니다.

```java
void A() {
    B();  // B 호출 정보를 스택에 push
}

void B() {
    C();  // C 호출 정보를 스택에 push
}

void C() {
    return;  // 스택에서 pop, B로 복귀
}
```

**왜 스택을 사용하나?**

함수는 호출된 역순으로 종료되므로 (C → B → A), LIFO 구조가 적합합니다.

**2. 괄호 검사**

수식의 괄호가 올바르게 쌍을 이루는지 검사합니다.

```java
boolean isValid(String s) {
    Stack<Character> stack = new Stack<>();

    for (char c : s.toCharArray()) {
        if (c == '(' || c == '{' || c == '[') {
            stack.push(c);  // 여는 괄호는 push
        } else {
            if (stack.isEmpty()) return false;
            char open = stack.pop();
            // 쌍이 맞는지 확인
            if ((c == ')' && open != '(') ||
                (c == '}' && open != '{') ||
                (c == ']' && open != '[')) {
                return false;
            }
        }
    }

    return stack.isEmpty();  // 모든 괄호가 쌍을 이뤄야 함
}
```

**3. 브라우저 뒤로 가기**

방문한 페이지를 스택에 저장하여 뒤로 가기를 구현합니다.

```
페이지 A 방문 → push(A)
페이지 B 방문 → push(B)
페이지 C 방문 → push(C)
뒤로 가기 → pop() → C 제거, B로 복귀
```

**4. 후위 표기법 (Postfix) 계산**

`3 4 + 5 *` → `(3 + 4) * 5 = 35`

```java
int evaluatePostfix(String expr) {
    Stack<Integer> stack = new Stack<>();

    for (String token : expr.split(" ")) {
        if (isOperator(token)) {
            int b = stack.pop();
            int a = stack.pop();
            stack.push(calculate(a, b, token));
        } else {
            stack.push(Integer.parseInt(token));
        }
    }

    return stack.pop();
}
```

**5. DFS (깊이 우선 탐색)**

그래프 탐색에서 스택을 사용합니다 (재귀 대신 반복문 사용 시).

## 큐 (Queue)

### 기본 연산

| 연산 | 설명 | 시간 복잡도 |
|------|------|------------|
| enqueue(item) | 큐 맨 뒤에 요소 추가 | O(1) |
| dequeue() | 큐 맨 앞 요소 제거 및 반환 | O(1) |
| peek() / front() | 큐 맨 앞 요소 조회 (제거 X) | O(1) |
| isEmpty() | 큐가 비어있는지 확인 | O(1) |
| size() | 큐에 저장된 요소 개수 | O(1) |

### 큐 구현 방법

#### 배열 기반 원형 큐 (Circular Queue)

**왜 원형 큐를 사용하나?**

일반 배열 큐는 dequeue 시 맨 앞 요소를 제거하면 빈 공간이 생기고, 이를 재사용할 수 없습니다. 원형 큐는 배열을 원형으로 간주하여 공간을 재사용합니다.

```java
class CircularQueue {
    private int[] data;
    private int front;    // 첫 번째 요소 위치
    private int rear;     // 마지막 요소 다음 위치 (다음 삽입 위치)
    private int size;
    private int capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity + 1;  // 빈 공간 하나 필요 (full 판별용)
        this.data = new int[this.capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    // O(1)
    public void enqueue(int item) {
        if (isFull()) {
            throw new RuntimeException("Queue is full");
        }
        data[rear] = item;
        rear = (rear + 1) % capacity;  // 원형 이동
        size++;
    }

    // O(1)
    public int dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        int item = data[front];
        front = (front + 1) % capacity;  // 원형 이동
        size--;
        return item;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity - 1;
    }
}
```

**왜 `capacity + 1`인가?**

front == rear 조건을 빈 큐와 꽉 찬 큐를 구분하기 위해 빈 공간 하나를 남겨둡니다.

#### 연결 리스트 기반 큐

```java
class LinkedQueue {
    private Node front;  // 맨 앞 (dequeue 위치)
    private Node rear;   // 맨 뒤 (enqueue 위치)
    private int size;

    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    // O(1)
    public void enqueue(int item) {
        Node newNode = new Node(item);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // O(1)
    public int dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        int item = front.data;
        front = front.next;

        if (front == null) {  // 마지막 요소 제거 시
            rear = null;
        }

        size--;
        return item;
    }

    public boolean isEmpty() {
        return front == null;
    }
}
```

### 큐 활용 사례

**1. BFS (너비 우선 탐색)**

그래프의 레벨 순서 탐색에 사용됩니다.

```java
void bfs(Node start) {
    Queue<Node> queue = new LinkedList<>();
    queue.offer(start);
    visited.add(start);

    while (!queue.isEmpty()) {
        Node current = queue.poll();
        System.out.println(current.data);

        for (Node neighbor : current.neighbors) {
            if (!visited.contains(neighbor)) {
                queue.offer(neighbor);
                visited.add(neighbor);
            }
        }
    }
}
```

**2. 작업 스케줄링**

프로세스, 프린터 작업 등을 순서대로 처리합니다.

```
작업 A 도착 → enqueue(A)
작업 B 도착 → enqueue(B)
작업 C 도착 → enqueue(C)
처리 → dequeue() → A 먼저 처리
```

**3. 캐시 구현 (FIFO)**

가장 오래된 캐시부터 제거합니다.

**4. 메시지 큐 (Message Queue)**

분산 시스템에서 비동기 메시지 전달에 사용됩니다 (Kafka, RabbitMQ).

## 덱 (Deque - Double Ended Queue)

### 기본 연산

| 연산 | 설명 | 시간 복잡도 |
|------|------|------------|
| addFirst(item) | 맨 앞에 추가 | O(1) |
| addLast(item) | 맨 뒤에 추가 | O(1) |
| removeFirst() | 맨 앞 제거 | O(1) |
| removeLast() | 맨 뒤 제거 | O(1) |
| peekFirst() | 맨 앞 조회 | O(1) |
| peekLast() | 맨 뒤 조회 | O(1) |

**특징**:
- 스택과 큐의 기능을 모두 수행 가능
- 양쪽 끝에서 삽입/삭제가 모두 O(1)

### 덱 구현 (이중 연결 리스트 기반)

```java
class Deque {
    private Node front;
    private Node rear;

    class Node {
        int data;
        Node prev;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    // O(1)
    public void addFirst(int item) {
        Node newNode = new Node(item);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
    }

    // O(1)
    public void addLast(int item) {
        Node newNode = new Node(item);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            newNode.prev = rear;
            rear = newNode;
        }
    }

    // O(1)
    public int removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }

        int item = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        } else {
            front.prev = null;
        }

        return item;
    }

    // O(1)
    public int removeLast() {
        if (isEmpty()) {
            throw new RuntimeException("Deque is empty");
        }

        int item = rear.data;
        rear = rear.prev;

        if (rear == null) {
            front = null;
        } else {
            rear.next = null;
        }

        return item;
    }

    public boolean isEmpty() {
        return front == null;
    }
}
```

### 덱 활용 사례

**1. 슬라이딩 윈도우 (Sliding Window)**

배열에서 크기 k의 윈도우를 이동하며 최댓값/최솟값을 찾는 문제에 사용됩니다.

**2. 팰린드롬 검사**

양쪽 끝에서 동시에 비교하며 확인합니다.

```java
boolean isPalindrome(String s) {
    Deque<Character> deque = new ArrayDeque<>();

    for (char c : s.toCharArray()) {
        deque.addLast(c);
    }

    while (deque.size() > 1) {
        if (deque.removeFirst() != deque.removeLast()) {
            return false;
        }
    }

    return true;
}
```

**3. 작업 스케줄러 (우선순위 조정)**

긴급 작업은 맨 앞에, 일반 작업은 맨 뒤에 추가합니다.

## 시간 복잡도

| 연산 | 스택 | 큐 | 덱 |
|------|------|-----|-----|
| 삽입 | O(1) | O(1) | O(1) (양쪽) |
| 삭제 | O(1) | O(1) | O(1) (양쪽) |
| 조회 (끝) | O(1) | O(1) | O(1) (양쪽) |
| 탐색 | O(N) | O(N) | O(N) |

## 트레이드오프

### 배열 vs 연결 리스트 구현

| 기준 | 배열 기반 | 연결 리스트 기반 |
|------|----------|----------------|
| 공간 효율 | 고정 크기, 메모리 낭비 가능 | 동적 크기, 포인터 추가 공간 |
| 캐시 성능 | 우수 (연속 메모리) | 나쁨 (메모리 분산) |
| 크기 제한 | 있음 (Overflow 가능) | 없음 (메모리 허용 범위) |
| 구현 복잡도 | 원형 큐 구현 복잡 | 간단 |

## 면접 예상 질문

- Q: 스택과 큐의 차이는 무엇인가요?
  - A: 스택은 LIFO (Last In First Out) 구조로 마지막에 들어온 데이터가 먼저 나갑니다. 함수 호출 스택, 뒤로 가기 기능 등에 사용됩니다. 큐는 FIFO (First In First Out) 구조로 먼저 들어온 데이터가 먼저 나갑니다. BFS, 작업 스케줄링 등에 사용됩니다. 스택은 한쪽 끝에서만, 큐는 양쪽 끝에서 연산이 일어나는 점이 다릅니다.

- Q: 배열로 큐를 구현할 때 왜 원형 큐를 사용하나요?
  - A: 일반 배열 큐는 dequeue 시 맨 앞 공간이 비지만 재사용할 수 없어 메모리가 낭비됩니다. 원형 큐는 배열을 원형으로 간주하여 front와 rear를 순환시키므로, 빈 공간을 재사용할 수 있습니다. 이를 통해 고정된 크기의 배열로 계속 동작할 수 있습니다.

- Q: 두 개의 스택으로 큐를 구현할 수 있나요?
  - A: 가능합니다. 스택 2개(inbox, outbox)를 사용합니다. enqueue는 inbox에 push하고, dequeue는 outbox에서 pop합니다. outbox가 비어있으면 inbox의 모든 요소를 pop해서 outbox에 push합니다. 이렇게 하면 순서가 역전되어 FIFO가 됩니다. enqueue는 O(1), dequeue는 평균 O(1) (Amortized)입니다.

- Q: 스택을 사용하는 실제 사례는 무엇인가요?
  - A: 1) **함수 호출 스택**: 함수 호출 시 복귀 주소와 지역 변수를 스택에 저장합니다. 2) **괄호 검사**: 여는 괄호를 push하고, 닫는 괄호 시 pop하여 쌍을 검사합니다. 3) **실행 취소(Undo)**: 작업 히스토리를 스택에 저장하여 역순으로 취소합니다. 4) **후위 표기법 계산**: 피연산자를 push하고, 연산자 만나면 pop하여 계산 후 결과를 push합니다. 5) **DFS**: 그래프 깊이 우선 탐색에 사용됩니다.

- Q: 큐를 사용하는 실제 사례는 무엇인가요?
  - A: 1) **BFS**: 레벨 순서로 그래프를 탐색합니다. 2) **프로세스 스케줄링**: CPU 작업을 순서대로 처리합니다. 3) **프린터 큐**: 인쇄 작업을 순서대로 처리합니다. 4) **메시지 큐**: Kafka, RabbitMQ 등에서 비동기 메시지 전달에 사용됩니다. 5) **캐시 (FIFO)**: 가장 오래된 데이터부터 제거합니다. 모두 "먼저 온 것을 먼저 처리"하는 공정성이 필요하기 때문입니다.

- Q: 덱(Deque)은 언제 사용하나요?
  - A: 양쪽 끝에서 삽입/삭제가 모두 필요할 때 사용합니다. 1) **슬라이딩 윈도우**: 윈도우 앞뒤로 요소를 추가/제거하며 최댓값을 추적합니다. 2) **작업 스케줄러**: 긴급 작업은 맨 앞에, 일반 작업은 맨 뒤에 추가합니다. 3) **팰린드롬 검사**: 양쪽 끝에서 동시에 비교합니다. 덱은 스택과 큐의 기능을 모두 제공하므로 유연성이 필요할 때 유용합니다.

## 연관 문서

- [배열 (Array)](./array.md) - 스택과 큐 구현의 기반
- [연결 리스트 (Linked List)](./linked-list.md) - 스택과 큐의 또 다른 구현 방법
- [트리 (Tree)](./tree.md) - DFS(스택), BFS(큐)를 사용하는 순회 알고리즘
- [그래프 (Graph)](./graph.md) - 스택과 큐를 사용하는 탐색 알고리즘

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 10.1
- [Wikipedia: Stack](https://en.wikipedia.org/wiki/Stack_(abstract_data_type))
- [Wikipedia: Queue](https://en.wikipedia.org/wiki/Queue_(abstract_data_type))
- Java Stack/Queue 소스 코드: [OpenJDK ArrayDeque.java](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/ArrayDeque.java)
