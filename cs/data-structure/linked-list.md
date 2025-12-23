# 연결 리스트 (Linked List)

> `[2] 입문` · 선수 지식: 없음

> 노드들이 포인터로 연결되어 데이터를 저장하는 선형 자료구조

`#연결리스트` `#LinkedList` `#단일연결리스트` `#SinglyLinkedList` `#이중연결리스트` `#DoublyLinkedList` `#원형연결리스트` `#CircularLinkedList` `#포인터` `#Pointer` `#노드` `#Node` `#자료구조` `#DataStructure` `#동적할당` `#DynamicAllocation` `#Head` `#Tail` `#Next` `#Prev` `#삽입O(1)` `#삭제O(1)` `#탐색O(n)` `#사이클탐지` `#역순` `#LRU캐시`

## 왜 알아야 하는가?

- **실무**: 동적 크기 조정이 필요한 상황에서 사용되며, 특히 중간 삽입/삭제가 빈번한 경우에 효율적입니다. LRU 캐시, 브라우저 히스토리 등에서 실제로 활용됩니다.
- **면접**: 연결 리스트 조작(역순, 사이클 감지, 중간 지점 찾기) 문제는 면접에서 매우 자주 출제됩니다. 포인터 조작 능력을 평가하는 좋은 수단이기 때문입니다.
- **기반 지식**: 포인터와 메모리 관리의 기초를 이해하는 데 필수적이며, 스택, 큐, 그래프 등 다른 자료구조의 구현에도 사용됩니다.

## 핵심 개념

- **노드 기반 구조**: 각 노드는 데이터와 다음 노드를 가리키는 포인터로 구성
- **비연속 메모리**: 배열과 달리 메모리상에서 분산되어 저장됨
- **동적 크기**: 실행 시간에 노드 추가/삭제가 자유로움
- **O(1) 삽입/삭제**: 포인터만 변경하면 되므로 빠름 (위치를 알고 있을 때)
- **O(N) 접근**: 특정 위치 접근을 위해 처음부터 순차 탐색 필요

## 쉽게 이해하기

**연결 리스트**를 보물찾기 쪽지에 비유할 수 있습니다.

보물찾기를 할 때, 첫 번째 쪽지에는 "다음 쪽지는 나무 밑에 있음"이라는 힌트가 적혀있습니다. 나무 밑 쪽지에는 또 "다음은 벤치 아래"라고 적혀있죠. 이렇게 각 쪽지가 다음 쪽지의 위치를 알려주는 구조입니다.

예를 들어:
- 첫 번째 쪽지 (HEAD) → "나무 밑"
- 나무 밑 쪽지 → "벤치 아래"
- 벤치 아래 쪽지 → "화단 옆"
- 화단 옆 쪽지 → "끝" (NULL)

중간에 새 쪽지를 끼워넣기는 쉽습니다. "나무 밑" 쪽지의 내용을 "우체통"으로 바꾸고, 우체통에 새 쪽지를 놓으면 됩니다. 하지만 3번째 쪽지를 찾으려면 처음부터 차례대로 따라가야 합니다.

## 상세 설명

### 노드 구조

**단일 연결 리스트 노드**:
```java
class Node {
    int data;      // 데이터
    Node next;     // 다음 노드를 가리키는 포인터
}
```

**왜 이 구조인가?**

- **data**: 실제 저장할 값
- **next**: 다음 노드의 메모리 주소를 저장하여 연결
- 배열과 달리 연속 메모리가 필요 없으므로, 메모리 어디든 노드 생성 가능

### 단일 연결 리스트 (Singly Linked List)

**구조**:
```
HEAD → [1|next] → [2|next] → [3|next] → NULL
```

**특징**:
- 각 노드는 다음 노드만 가리킴
- 단방향 순회만 가능
- 이전 노드로 돌아갈 수 없음

**삽입 연산 (중간)**:
```java
// node1 → node2 사이에 newNode 삽입
newNode.next = node1.next;  // newNode → node2
node1.next = newNode;        // node1 → newNode
```

**왜 순서가 중요한가?**

순서를 바꾸면 node2를 잃어버립니다:
```java
// 잘못된 순서
node1.next = newNode;        // node1 → newNode (node2 연결 끊김!)
newNode.next = node1.next;   // newNode → newNode (잘못된 연결)
```

**삭제 연산**:
```java
// node1 → node2 → node3에서 node2 삭제
node1.next = node2.next;  // node1 → node3 (node2 건너뜀)
// node2는 더 이상 참조되지 않으므로 가비지 컬렉션 대상
```

### 이중 연결 리스트 (Doubly Linked List)

**구조**:
```
NULL ← [prev|1|next] ⇄ [prev|2|next] ⇄ [prev|3|next] → NULL
```

**노드 구조**:
```java
class Node {
    int data;
    Node prev;  // 이전 노드 포인터
    Node next;  // 다음 노드 포인터
}
```

**왜 이중 연결이 필요한가?**

- **양방향 순회**: 앞뒤로 자유롭게 이동 가능
- **삭제 용이**: 이전 노드를 찾기 위해 처음부터 탐색할 필요 없음
- **역순 순회**: 끝에서부터 거꾸로 탐색 가능

**삽입 연산 (중간)**:
```java
// node1 ⇄ node2 사이에 newNode 삽입
newNode.next = node2;
newNode.prev = node1;
node1.next = newNode;
node2.prev = newNode;
```

**단점**:
- 포인터 2개로 메모리 사용량 증가
- 삽입/삭제 시 포인터 조작이 더 복잡

### 원형 연결 리스트 (Circular Linked List)

**구조**:
```
     ┌───────────────┐
     ↓               ↑
[1|next] → [2|next] → [3|next]
```

**특징**:
- 마지막 노드의 next가 NULL이 아닌 HEAD를 가리킴
- 끝이 없으므로 계속 순회 가능
- 리스트의 모든 노드에서 전체 순회 가능

**왜 원형 구조를 사용하나?**

- **라운드 로빈 스케줄링**: 프로세스를 순환하며 실행
- **버퍼**: 원형 큐 구현
- **멀티플레이어 게임**: 턴제 게임에서 플레이어 순환

**주의사항**:
- 종료 조건을 명확히 해야 무한 루프 방지
- 보통 HEAD로 돌아왔는지 체크: `current.next == HEAD`

### 연결 리스트 연산의 시간 복잡도

| 연산 | 단일 연결 리스트 | 이중 연결 리스트 | 이유 |
|------|----------------|----------------|------|
| 접근 (Access) | O(N) | O(N) | 인덱스로 바로 접근 불가, 순차 탐색 필요 |
| 탐색 (Search) | O(N) | O(N) | 최악의 경우 전체 순회 |
| 삽입 (Insert) - 맨 앞 | O(1) | O(1) | HEAD 포인터만 변경 |
| 삽입 - 중간 | O(1)* | O(1)* | 포인터만 변경 (*위치를 알고 있을 때) |
| 삽입 - 맨 뒤 | O(N) | O(1)** | 끝까지 순회 필요 (**TAIL 포인터 있을 때) |
| 삭제 (Delete) - 맨 앞 | O(1) | O(1) | HEAD 포인터만 변경 |
| 삭제 - 중간 | O(N) | O(1)* | 이전 노드 찾기 필요 (*노드 참조 있을 때) |

**중요**: 삽입/삭제가 O(1)인 것은 **대상 위치를 이미 알고 있을 때**입니다. 위치를 찾는 데 O(N)이 걸립니다.

## 예제 코드

### 단일 연결 리스트 구현

```java
class SinglyLinkedList {
    private Node head;
    private int size;

    class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // O(1) - 맨 앞 삽입
    public void addFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // O(N) - 맨 뒤 삽입 (끝까지 순회)
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // O(N) - 특정 인덱스에 삽입
    public void add(int index, int data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addFirst(data);
            return;
        }

        Node newNode = new Node(data);
        Node current = head;

        // index-1 위치까지 이동
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    // O(1) - 맨 앞 삭제
    public void removeFirst() {
        if (head == null) {
            throw new RuntimeException("List is empty");
        }
        head = head.next;
        size--;
    }

    // O(N) - 특정 인덱스 삭제
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            removeFirst();
            return;
        }

        Node current = head;
        // index-1 위치까지 이동
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        current.next = current.next.next;
        size--;
    }

    // O(N) - 탐색
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // O(N) - 순회
    public void print() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " → ");
            current = current.next;
        }
        System.out.println("NULL");
    }
}
```

### 연결 리스트 최적화: TAIL 포인터

```java
class OptimizedLinkedList {
    private Node head;
    private Node tail;  // 끝 노드 참조
    private int size;

    // O(1) - TAIL 포인터로 맨 뒤 삽입 최적화
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;  // TAIL 업데이트
        }
        size++;
    }
}
```

**왜 TAIL 포인터를 사용하나?**

맨 뒤 삽입이 O(N)에서 O(1)로 개선됩니다. 큐(Queue) 구현 시 필수입니다.

### 이중 연결 리스트 구현

```java
class DoublyLinkedList {
    private Node head;
    private Node tail;

    class Node {
        int data;
        Node prev;
        Node next;

        Node(int data) {
            this.data = data;
        }
    }

    // O(1) - 맨 앞 삽입
    public void addFirst(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
    }

    // O(1) - 맨 뒤 삽입 (TAIL 포인터 덕분)
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // O(1) - 특정 노드 삭제 (노드 참조를 알고 있을 때)
    public void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;  // 첫 노드 삭제
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;  // 마지막 노드 삭제
        }
    }
}
```

## 트레이드오프

### 연결 리스트 vs 배열

| 기준 | 연결 리스트 | 배열 |
|------|------------|------|
| 접근 속도 | O(N) - 순차 탐색 | O(1) - 인덱스로 즉시 |
| 중간 삽입/삭제 | O(1) - 포인터만 변경 (위치 알 때) | O(N) - 요소 이동 필요 |
| 메모리 사용 | 많음 - 포인터 추가 공간 | 적음 - 데이터만 저장 |
| 메모리 할당 | 동적 - 필요할 때 노드 생성 | 정적 (고정) 또는 재할당 필요 (동적) |
| 캐시 성능 | 나쁨 - 메모리 분산 | 우수 - 연속 메모리 |
| 크기 변경 | 쉬움 - 노드 추가만 | 어려움 (정적) / 비용 (동적) |

### 단일 vs 이중 연결 리스트

| 기준 | 단일 연결 리스트 | 이중 연결 리스트 |
|------|----------------|----------------|
| 메모리 사용 | 적음 (포인터 1개) | 많음 (포인터 2개) |
| 역방향 순회 | 불가능 | 가능 |
| 노드 삭제 | 이전 노드 찾기 O(N) | 즉시 삭제 O(1) (노드 참조 있을 때) |
| 구현 복잡도 | 간단 | 복잡 (포인터 2개 관리) |

**언제 이중 연결 리스트를 사용하나?**

- 양방향 순회가 필요할 때 (예: 브라우저 앞/뒤로 가기)
- 빈번한 삭제 연산 (예: LRU 캐시)
- 덱(Deque) 구현

## 주의사항

**메모리 누수**:
- C/C++에서는 노드 삭제 시 `free()` 또는 `delete` 필수
- Java/Python은 가비지 컬렉션이 자동 처리

**포인터 조작 실수**:
- 잘못된 순서로 포인터 변경 시 노드 유실
- 항상 연결을 끊기 전에 새 연결 먼저 만들기

**순환 참조**:
- 원형 리스트에서 종료 조건 누락 시 무한 루프
- 삭제 시 순환 끊기

## 면접 예상 질문

- Q: 연결 리스트의 중간 삽입/삭제가 O(1)이라고 하는데, 실제로는 언제 그런가요?
  - A: 삽입/삭제할 위치의 **노드 참조를 이미 가지고 있을 때** O(1)입니다. 포인터만 변경하면 되기 때문입니다. 하지만 위치를 찾는 과정은 순차 탐색이 필요하므로 O(N)이 걸립니다. 따라서 "찾기 + 삭제" 전체 연산은 O(N)입니다. 예를 들어 LRU 캐시에서는 해시맵으로 노드 참조를 O(1)에 찾을 수 있으므로, 삭제도 O(1)이 됩니다.

- Q: 배열과 연결 리스트 중 언제 무엇을 선택해야 하나?
  - A: 접근이 많고 크기가 고정적이면 배열을 선택합니다. 배열은 O(1) 인덱스 접근과 우수한 캐시 성능을 제공하기 때문입니다. 반면 중간 삽입/삭제가 빈번하고 크기가 동적으로 변한다면 연결 리스트를 선택합니다. 연결 리스트는 포인터만 변경하면 되므로 O(1)에 삽입/삭제가 가능하고, 메모리 재할당이 필요 없기 때문입니다.

- Q: 연결 리스트에서 중간 지점을 찾는 효율적인 방법은?
  - A: **투 포인터(Two Pointer)** 또는 **Runner 기법**을 사용합니다. 느린 포인터는 한 칸씩, 빠른 포인터는 두 칸씩 이동합니다. 빠른 포인터가 끝에 도달하면 느린 포인터는 중간에 위치합니다. 이는 O(N) 시간, O(1) 공간으로 해결됩니다. 리스트 길이를 알지 못해도 한 번의 순회로 찾을 수 있기 때문입니다.

- Q: 연결 리스트에 사이클이 있는지 어떻게 감지하나요?
  - A: **플로이드의 순환 감지 알고리즘 (Floyd's Cycle Detection)** 또는 **토끼와 거북이 알고리즘**을 사용합니다. 느린 포인터(거북이)는 한 칸씩, 빠른 포인터(토끼)는 두 칸씩 이동합니다. 사이클이 있으면 두 포인터가 언젠가 만나고, 없으면 빠른 포인터가 NULL에 도달합니다. O(N) 시간, O(1) 공간으로 감지 가능합니다.

- Q: 단일 연결 리스트를 역순으로 뒤집는 방법은?
  - A: 반복문을 사용한 방법이 가장 효율적입니다. 포인터 3개(prev, current, next)를 사용하여 각 노드의 next를 이전 노드로 변경합니다. 시간 O(N), 공간 O(1)입니다. 재귀로도 가능하지만 스택 공간 O(N)이 필요합니다. 왜냐하면 각 노드마다 재귀 호출이 스택에 쌓이기 때문입니다.

- Q: 이중 연결 리스트를 사용하는 실제 사례는?
  - A: LRU 캐시가 대표적입니다. 해시맵으로 O(1) 접근, 이중 연결 리스트로 O(1) 삽입/삭제를 구현합니다. 가장 최근 사용된 항목을 맨 앞으로 이동하고, 캐시가 꽉 차면 맨 뒤(가장 오래된) 항목을 제거합니다. 이중 연결이 필요한 이유는 중간 노드를 삭제할 때 이전 노드를 O(1)에 접근해야 하기 때문입니다.

## 연관 문서

- [배열 (Array)](./array.md) - 연결 리스트와 비교되는 연속 메모리 기반 자료구조
- [스택과 큐 (Stack & Queue)](./stack-queue.md) - 연결 리스트로 구현 가능한 자료구조
- [트리 (Tree)](./tree.md) - 연결 리스트와 유사한 노드 기반 구조
- [해시 테이블 (Hash Table)](./hash-table.md) - 체이닝 방식에서 연결 리스트 사용

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 10.2
- [Wikipedia: Linked List](https://en.wikipedia.org/wiki/Linked_list)
- [LeetCode Linked List Problems](https://leetcode.com/tag/linked-list/)
- Java LinkedList 소스 코드: [OpenJDK LinkedList.java](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/LinkedList.java)
