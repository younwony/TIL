# 힙 (Heap)

> `[3] 중급` · 선수 지식: [배열 (Array)](./array.md), [트리 (Tree)](./tree.md)

> 완전 이진 트리 기반으로 최댓값 또는 최솟값을 빠르게 찾기 위한 자료구조

`#힙` `#Heap` `#최대힙` `#MaxHeap` `#최소힙` `#MinHeap` `#우선순위큐` `#PriorityQueue` `#완전이진트리` `#CompleteBinaryTree` `#힙정렬` `#HeapSort` `#자료구조` `#DataStructure` `#Heapify` `#힙속성` `#HeapProperty` `#삽입O(logn)` `#삭제O(logn)` `#최솟값O(1)` `#최댓값O(1)` `#배열구현` `#다익스트라` `#Dijkstra` `#TopK` `#스케줄링`

## 왜 알아야 하는가?

- **실무**: 우선순위 큐 구현의 핵심 자료구조로, 작업 스케줄링, 이벤트 시뮬레이션, 데이터 스트림에서 상위 K개 찾기 등에 필수적입니다.
- **면접**: 힙 정렬, 최단 경로(다익스트라), K번째 요소 찾기 등 다양한 알고리즘 문제의 기반입니다. 힙의 동작 원리와 시간 복잡도는 자주 출제됩니다.
- **기반 지식**: 완전 이진 트리를 배열로 표현하는 방법을 이해하고, 우선순위 큐의 효율적인 구현 방법을 배울 수 있습니다.

## 핵심 개념

- **완전 이진 트리**: 마지막 레벨을 제외하고 모든 레벨이 꽉 차있고, 마지막 레벨은 왼쪽부터 채워짐
- **힙 속성 (Heap Property)**: 부모 노드가 자식 노드보다 크거나 (최대힙) 작음 (최소힙)
- **O(1) 최댓값/최솟값 조회**: 루트에 최댓값(최대힙) 또는 최솟값(최소힙)이 위치
- **O(log N) 삽입/삭제**: heapify 과정으로 힙 속성 유지
- **우선순위 큐 구현**: 힙으로 효율적인 우선순위 큐 구현 가능

## 쉽게 이해하기

**힙**을 회사 조직도에 비유할 수 있습니다.

최대힙을 회사 조직도라고 생각해보세요. 규칙은 "상사는 항상 부하직원보다 연봉이 높다"입니다.

```
        CEO (1억)
       /          \
  부장(8천)    부장(7천)
   /    \        /
과장(5천) 대리(4천) 과장(6천)
```

특징:
- 맨 위(루트)에 가장 높은 연봉 → O(1) 최댓값
- 부모는 항상 자식보다 높음 (힙 속성)
- 형제 간 순서는 상관없음 (부장 8천 vs 7천)
- 왼쪽부터 채워짐 (완전 이진 트리)

새 직원 채용 시:
1. 맨 아래 왼쪽에 추가
2. 상사보다 연봉이 높으면 자리 교체 (위로 올라감)
3. 상사보다 낮으면 멈춤

CEO 퇴사 시:
1. 맨 아래 직원을 CEO 자리로 이동
2. 부하보다 연봉이 낮으면 자리 교체 (아래로 내려감)
3. 부하보다 높으면 멈춤

이렇게 계층 구조를 유지하며 최댓값을 빠르게 찾는 것이 힙입니다.

## 상세 설명

### 힙의 종류

#### 1. 최대힙 (Max Heap)

**정의**: 부모 노드 ≥ 자식 노드

```
        9
       / \
      7   6
     / \ /
    3  5 4
```

**특징**:
- 루트에 최댓값
- 부모 ≥ 자식

**사용 사례**:
- 최댓값 빠르게 찾기
- 내림차순 정렬 (Heap Sort)
- 최대 우선순위 큐

#### 2. 최소힙 (Min Heap)

**정의**: 부모 노드 ≤ 자식 노드

```
        1
       / \
      3   4
     / \ /
    7  5 6
```

**특징**:
- 루트에 최솟값
- 부모 ≤ 자식

**사용 사례**:
- 최솟값 빠르게 찾기
- 오름차순 정렬
- 최소 우선순위 큐
- 다익스트라 알고리즘

### 힙의 배열 표현

**왜 배열을 사용하나?**

완전 이진 트리이므로 배열로 표현하면 빈 공간 없이 효율적으로 저장할 수 있습니다.

**인덱스 규칙** (인덱스 1부터 시작):
```
부모: i / 2
왼쪽 자식: 2 * i
오른쪽 자식: 2 * i + 1
```

**인덱스 0부터 시작 시**:
```
부모: (i - 1) / 2
왼쪽 자식: 2 * i + 1
오른쪽 자식: 2 * i + 2
```

**예시**:
```
트리:        9
           /   \
          7     6
         / \   /
        3   5 4

배열: [9, 7, 6, 3, 5, 4]
인덱스: 0  1  2  3  4  5
```

- `heap[1] = 7`의 부모: `heap[1/2] = heap[0] = 9`
- `heap[1] = 7`의 왼쪽 자식: `heap[2*1] = heap[2] = 6`
- `heap[1] = 7`의 오른쪽 자식: `heap[2*1+1] = heap[3] = 3`

**장점**:
- 포인터 없이 배열만 사용 → 메모리 효율
- 부모/자식 인덱스 계산 O(1)
- 캐시 친화적 (연속 메모리)

### 힙 연산

#### 1. 삽입 (Insert) - O(log N)

**과정**:
1. 마지막 위치에 삽입
2. 부모와 비교하며 위로 이동 (Heapify Up, Bubble Up)

**최대힙 삽입 예시**: 8 삽입

```
초기:     9           배열: [9, 7, 6, 3, 5, 4]
        /   \
       7     6
      / \   /
     3   5 4

Step 1: 마지막에 추가
         9           배열: [9, 7, 6, 3, 5, 4, 8]
       /   \
      7     6
     / \   / \
    3   5 4   8

Step 2: 부모(6)와 비교 → 8 > 6 → 교체
         9           배열: [9, 7, 8, 3, 5, 4, 6]
       /   \
      7     8
     / \   / \
    3   5 4   6

Step 3: 부모(9)와 비교 → 8 < 9 → 멈춤
```

**구현**:
```java
class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity + 1];  // 인덱스 1부터 사용
        this.size = 0;
    }

    // O(log N)
    public void insert(int value) {
        if (size == capacity) {
            throw new RuntimeException("Heap is full");
        }

        size++;
        heap[size] = value;  // 마지막에 삽입

        // Heapify Up (위로 이동)
        heapifyUp(size);
    }

    private void heapifyUp(int index) {
        while (index > 1) {
            int parent = index / 2;

            // 최대힙: 부모가 자식보다 크거나 같으면 멈춤
            if (heap[parent] >= heap[index]) {
                break;
            }

            // 부모와 교체
            swap(parent, index);
            index = parent;
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
```

**시간 복잡도**: O(log N)
- 최악의 경우 루트까지 올라감 → 트리 높이만큼 = log N

#### 2. 삭제 (Delete) - O(log N)

**과정**:
1. 루트 제거 (최댓값/최솟값)
2. 마지막 요소를 루트로 이동
3. 자식과 비교하며 아래로 이동 (Heapify Down, Bubble Down)

**최대힙 삭제 예시**: 루트(9) 삭제

```
초기:     9           배열: [9, 7, 6, 3, 5, 4]
        /   \
       7     6
      / \   /
     3   5 4

Step 1: 루트 제거, 마지막 요소(4)를 루트로
         4           배열: [4, 7, 6, 3, 5]
       /   \
      7     6
     / \
    3   5

Step 2: 자식(7, 6) 중 큰 값(7)과 비교 → 4 < 7 → 교체
         7           배열: [7, 4, 6, 3, 5]
       /   \
      4     6
     / \
    3   5

Step 3: 자식(3, 5) 중 큰 값(5)과 비교 → 4 < 5 → 교체
         7           배열: [7, 5, 6, 3, 4]
       /   \
      5     6
     / \
    3   4

Step 4: 리프 노드 도달 → 멈춤
```

**구현**:
```java
// O(log N)
public int delete() {
    if (size == 0) {
        throw new RuntimeException("Heap is empty");
    }

    int max = heap[1];  // 루트 (최댓값)
    heap[1] = heap[size];  // 마지막 요소를 루트로
    size--;

    // Heapify Down (아래로 이동)
    heapifyDown(1);

    return max;
}

private void heapifyDown(int index) {
    while (index * 2 <= size) {  // 왼쪽 자식이 존재하는 동안
        int left = index * 2;
        int right = index * 2 + 1;
        int largest = index;

        // 왼쪽 자식이 더 크면
        if (left <= size && heap[left] > heap[largest]) {
            largest = left;
        }

        // 오른쪽 자식이 더 크면
        if (right <= size && heap[right] > heap[largest]) {
            largest = right;
        }

        // 부모가 가장 크면 멈춤
        if (largest == index) {
            break;
        }

        // 자식과 교체
        swap(index, largest);
        index = largest;
    }
}
```

**시간 복잡도**: O(log N)
- 최악의 경우 리프까지 내려감 → 트리 높이만큼 = log N

#### 3. 최댓값/최솟값 조회 (Peek) - O(1)

```java
// O(1)
public int peek() {
    if (size == 0) {
        throw new RuntimeException("Heap is empty");
    }
    return heap[1];  // 루트
}
```

**왜 O(1)인가?**

힙 속성에 의해 루트가 항상 최댓값(최대힙) 또는 최솟값(최소힙)이므로 즉시 반환합니다.

### 힙 생성 (Heapify) - O(N)

**배열을 힙으로 변환하는 방법**:

**방법 1**: 삽입 반복 → O(N log N)
```java
for (int value : array) {
    insert(value);  // O(log N) × N = O(N log N)
}
```

**방법 2**: Bottom-up Heapify → O(N)

**왜 O(N)인가?**

리프 노드(N/2개)는 이미 힙 속성을 만족합니다 (자식 없음). 내부 노드(N/2개)만 heapifyDown하면 되고, 높이가 낮은 노드가 많으므로 평균적으로 O(N)입니다.

```java
// O(N)
public void buildHeap(int[] array) {
    this.heap = array;
    this.size = array.length - 1;

    // 마지막 내부 노드부터 루트까지 heapifyDown
    for (int i = size / 2; i >= 1; i--) {
        heapifyDown(i);
    }
}
```

**예시**: `[3, 9, 2, 1, 4, 5]`를 최대힙으로

```
초기 트리:    3
            /   \
           9     2
          / \   /
         1   4 5

Step 1: heapifyDown(3) → [3, 9, 5, 1, 4, 2]
           3
         /   \
        9     5
       / \   /
      1   4 2

Step 2: heapifyDown(2) → [3, 9, 5, 1, 4, 2] (변화 없음)

Step 3: heapifyDown(1) → [9, 4, 5, 1, 3, 2]
           9
         /   \
        4     5
       / \   /
      1   3 2
```

### 힙 정렬 (Heap Sort) - O(N log N)

**과정**:
1. 배열을 힙으로 변환 - O(N)
2. 루트(최댓값)를 마지막과 교체하고 heapifyDown 반복 - O(N log N)

```java
// O(N log N)
public void heapSort(int[] array) {
    // 1. Build Max Heap - O(N)
    buildHeap(array);

    // 2. 루트와 마지막 교체 후 heapifyDown - O(N log N)
    for (int i = size; i > 1; i--) {
        swap(1, i);  // 루트(최댓값)를 마지막으로
        size--;      // 힙 크기 감소
        heapifyDown(1);  // 힙 속성 복구
    }
}
```

**시간 복잡도**: O(N log N)
**공간 복잡도**: O(1) - 제자리 정렬

**특징**:
- 최악의 경우도 O(N log N) 보장 (Quick Sort는 O(N²))
- 제자리 정렬 (추가 메모리 불필요)
- 불안정 정렬 (동일 값의 순서 바뀔 수 있음)

### 우선순위 큐 (Priority Queue)

**정의**: 우선순위가 높은 요소가 먼저 나오는 큐

**힙으로 구현하는 이유**:
- 최댓값/최솟값 조회: O(1)
- 삽입/삭제: O(log N)
- 배열: 삽입 O(N), 삭제 O(1) 또는 삽입 O(1), 삭제 O(N)
- 정렬 배열: 삽입 O(N), 삭제 O(1)

**구현**:
```java
class PriorityQueue {
    private MaxHeap heap;

    public void enqueue(int value) {
        heap.insert(value);  // O(log N)
    }

    public int dequeue() {
        return heap.delete();  // O(log N)
    }

    public int peek() {
        return heap.peek();  // O(1)
    }
}
```

**사용 사례**:
- **작업 스케줄링**: 우선순위가 높은 작업 먼저 처리
- **다익스트라 알고리즘**: 최단 거리 노드 선택
- **이벤트 시뮬레이션**: 가장 빠른 이벤트 먼저 처리
- **허프만 코딩**: 빈도가 낮은 문자 우선 병합

**Java PriorityQueue**:
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();  // 최소힙
pq.offer(5);
pq.offer(3);
pq.offer(7);
System.out.println(pq.poll());  // 3 (최솟값)

// 최대힙
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
```

## 시간 복잡도

| 연산 | 시간 복잡도 |
|------|------------|
| 삽입 (Insert) | O(log N) |
| 삭제 (Delete) | O(log N) |
| 조회 (Peek) | O(1) |
| 힙 생성 (Heapify) | O(N) |
| 힙 정렬 (Heap Sort) | O(N log N) |
| 탐색 (Search) | O(N) - 힙은 탐색에 비효율 |

## 트레이드오프

### 힙 vs 정렬 배열 (우선순위 큐 구현)

| 기준 | 힙 | 정렬 배열 |
|------|-----|---------|
| 삽입 | O(log N) | O(N) - 정렬 유지 |
| 삭제 (최댓값) | O(log N) | O(1) - 마지막 요소 |
| 최댓값 조회 | O(1) | O(1) |
| 공간 | O(N) | O(N) |
| 사용 | 삽입 빈번 | 삭제만 빈번 |

### 힙 vs BST (우선순위 큐 구현)

| 기준 | 힙 | BST (균형) |
|------|-----|-----------|
| 삽입 | O(log N) | O(log N) |
| 삭제 (최댓값) | O(log N) | O(log N) |
| 최댓값 조회 | O(1) | O(log N) - 가장 오른쪽 |
| 구조 | 완전 이진 트리 | 균형 트리 |
| 배열 표현 | 가능 | 어려움 |
| 정렬 순서 | 부분 정렬 | 완전 정렬 (중위 순회) |

**왜 우선순위 큐에 힙을 사용하나?**

- 최댓값/최솟값 조회가 O(1)
- 배열로 구현 가능 → 캐시 친화적
- BST보다 구현 간단
- 완전 정렬 불필요 (부분 정렬로 충분)

## 면접 예상 질문

- Q: 힙의 삽입과 삭제가 O(log N)인 이유는?
  - A: 힙은 완전 이진 트리이므로 높이가 log N입니다. 삽입 시 마지막에 추가 후 부모와 비교하며 위로 올라가므로 최악의 경우 루트까지 log N번 비교합니다. 삭제 시 루트를 제거하고 마지막 요소를 루트로 이동한 후 자식과 비교하며 아래로 내려가므로 최악의 경우 리프까지 log N번 비교합니다. 따라서 둘 다 O(log N)입니다.

- Q: 배열로 힙을 표현할 때의 장점은?
  - A: 완전 이진 트리이므로 배열로 표현하면 빈 공간 없이 연속적으로 저장할 수 있습니다. 포인터가 필요 없어 메모리 효율적이고, 부모/자식 인덱스를 계산으로 O(1)에 구할 수 있습니다. 또한 연속 메모리로 캐시 친화적이어서 성능이 좋습니다. 인덱스 i의 왼쪽 자식은 2i, 오른쪽 자식은 2i+1, 부모는 i/2로 간단합니다.

- Q: 힙 생성(Heapify)이 O(N)인 이유는?
  - A: Bottom-up 방식으로 마지막 내부 노드부터 루트까지 heapifyDown을 수행합니다. 리프 노드(N/2개)는 이미 힙 속성을 만족하므로 처리 불필요합니다. 높이가 낮은 노드가 많고 높이가 높은 노드가 적으므로, 평균적으로 각 노드가 O(1) 작업만 하여 전체 O(N)이 됩니다. 삽입을 N번 반복하면 O(N log N)이지만 Heapify는 O(N)으로 더 효율적입니다.

- Q: 최대힙과 최소힙 중 언제 무엇을 사용하나요?
  - A: 최댓값을 빠르게 찾아야 하면 최대힙, 최솟값을 빠르게 찾아야 하면 최소힙을 사용합니다. 예를 들어 내림차순 정렬은 최대힙, 오름차순 정렬은 최소힙입니다. 다익스트라 알고리즘은 최단 거리(최솟값)를 찾으므로 최소힙을 사용합니다. 두 힙의 구현은 비교 연산자만 바꾸면 되므로 매우 유사합니다.

- Q: 힙으로 우선순위 큐를 구현하는 이유는?
  - A: 힙은 최댓값/최솟값 조회가 O(1)이고 삽입/삭제가 O(log N)으로 우선순위 큐에 최적입니다. 배열로 구현하면 삽입 O(N) 또는 삭제 O(N)이고, BST는 최댓값 조회가 O(log N)입니다. 힙은 배열로 표현 가능하여 캐시 친화적이고 구현도 간단합니다. 또한 완전 정렬이 필요 없고 부분 정렬만 필요한 우선순위 큐에 적합합니다.

- Q: 힙 정렬의 시간 복잡도와 특징은?
  - A: 시간 복잡도는 O(N log N)으로 최악의 경우도 보장됩니다. 힙 생성에 O(N), 루트 제거와 heapifyDown을 N번 반복하여 O(N log N)입니다. 제자리 정렬로 추가 메모리가 O(1)이지만 불안정 정렬입니다. Quick Sort는 평균 O(N log N)이지만 최악 O(N²)이고, Merge Sort는 O(N log N) 보장이지만 O(N) 추가 메모리가 필요합니다.

- Q: 힙에서 특정 값을 찾는 시간 복잡도는?
  - A: O(N)입니다. 힙은 부모-자식 관계만 정렬되어 있고 형제 간 순서는 없으므로 이진 탐색을 사용할 수 없습니다. 따라서 최악의 경우 전체 노드를 순회해야 합니다. 힙은 최댓값/최솟값 찾기에 특화되어 있고, 일반 탐색이 필요하면 해시 테이블이나 BST를 사용해야 합니다.

## 연관 문서

- [배열 (Array)](./array.md) - 힙의 내부 저장 방식
- [트리 (Tree)](./tree.md) - 힙은 완전 이진 트리 구조
- [스택과 큐 (Stack & Queue)](./stack-queue.md) - 힙으로 우선순위 큐 구현
- [그래프 (Graph)](./graph.md) - 다익스트라 알고리즘에서 힙 사용

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 6
- [Wikipedia: Heap](https://en.wikipedia.org/wiki/Heap_(data_structure))
- [Visualgo: Heap](https://visualgo.net/en/heap)
- Java PriorityQueue 소스 코드: [OpenJDK PriorityQueue.java](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/PriorityQueue.java)
