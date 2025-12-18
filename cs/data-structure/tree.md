# 트리 (Tree)

> 계층적 관계를 표현하는 비선형 자료구조로, 노드들이 간선으로 연결되어 사이클이 없는 구조

## 핵심 개념

- **계층 구조**: 부모-자식 관계로 데이터를 표현
- **루트 (Root)**: 최상위 노드, 트리의 시작점
- **사이클 없음**: 한 노드에서 출발하여 같은 노드로 돌아올 수 없음
- **N개 노드 → N-1개 간선**: 모든 노드가 하나의 부모와 연결 (루트 제외)
- **재귀적 구조**: 트리의 부분도 트리 (서브트리)

## 쉽게 이해하기

**트리**를 조직도에 비유할 수 있습니다.

회사 조직도를 떠올려 보세요. 맨 위에 대표이사(루트)가 있고, 그 아래 부서장들(자식 노드), 부서장 아래 팀장들, 팀장 아래 팀원들이 있습니다.

예를 들어:
```
          대표이사 (Root)
         /    |    \
    개발부  영업부  인사부
    /  \
  백엔드 프론트
```

특징:
- 대표이사는 부모가 없음 (루트)
- 각 직원은 정확히 한 명의 상사를 가짐 (부모)
- 팀원은 자식이 없음 (리프)
- 순환 보고 체계는 없음 (사이클 X)

이렇게 계층적이고 순환하지 않는 구조가 트리입니다.

## 상세 설명

### 트리 용어

```
        1 (Root)
       / \
      2   3
     / \   \
    4   5   6
       /
      7
```

| 용어 | 설명 | 예시 |
|------|------|------|
| **루트 (Root)** | 최상위 노드, 부모가 없는 유일한 노드 | 1 |
| **부모 (Parent)** | 특정 노드의 상위 노드 | 2의 부모는 1 |
| **자식 (Child)** | 특정 노드의 하위 노드 | 1의 자식은 2, 3 |
| **리프 (Leaf)** | 자식이 없는 노드 (단말 노드) | 4, 6, 7 |
| **내부 노드 (Internal Node)** | 자식이 있는 노드 | 1, 2, 3, 5 |
| **형제 (Sibling)** | 같은 부모를 가진 노드들 | 2와 3 |
| **조상 (Ancestor)** | 루트까지의 경로상에 있는 모든 노드 | 7의 조상은 5, 2, 1 |
| **자손 (Descendant)** | 특정 노드 아래의 모든 노드 | 2의 자손은 4, 5, 7 |
| **깊이 (Depth)** | 루트에서 특정 노드까지의 간선 수 | 7의 깊이는 3 |
| **높이 (Height)** | 리프까지의 최대 간선 수 | 5의 높이는 1, 트리 높이는 3 |
| **레벨 (Level)** | 깊이 + 1 (루트는 레벨 1) | 7의 레벨은 4 |
| **서브트리 (Subtree)** | 특정 노드를 루트로 하는 트리 | 2를 루트로 하는 서브트리 |

**왜 높이와 깊이를 구분하나?**

- **깊이**: 위에서 아래로 (루트 → 노드)
- **높이**: 아래에서 위로 (노드 → 가장 먼 리프)
- 트리의 높이 = 루트의 높이 = 최대 깊이

### 이진 트리 (Binary Tree)

**정의**: 각 노드가 최대 2개의 자식 (왼쪽, 오른쪽)을 가지는 트리

```java
class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    TreeNode(int data) {
        this.data = data;
    }
}
```

#### 이진 트리 종류

**1. 정 이진 트리 (Full Binary Tree)**

모든 노드가 0개 또는 2개의 자식을 가짐 (1개는 없음).

```
      1
     / \
    2   3
   / \
  4   5
```

**2. 완전 이진 트리 (Complete Binary Tree)**

마지막 레벨을 제외하고 모든 레벨이 꽉 차있고, 마지막 레벨은 왼쪽부터 채워짐.

```
      1
     / \
    2   3
   / \  /
  4  5 6
```

**왜 완전 이진 트리가 중요한가?**

- **배열 표현 가능**: 인덱스 i의 왼쪽 자식은 2i, 오른쪽 자식은 2i+1
- **힙(Heap) 구조**: 완전 이진 트리로 구현됨
- **공간 효율**: 빈 공간 없이 배열에 저장 가능

**3. 포화 이진 트리 (Perfect Binary Tree)**

모든 레벨이 꽉 차있음. 리프 노드가 모두 같은 레벨.

```
      1
     / \
    2   3
   / \ / \
  4  5 6  7
```

**특징**:
- 높이 h일 때 노드 개수: 2^(h+1) - 1
- 리프 노드 개수: 2^h

**4. 편향 트리 (Skewed Tree)**

모든 노드가 한쪽 자식만 가짐 (연결 리스트와 유사).

```
1
 \
  2
   \
    3
     \
      4
```

**문제점**: 트리의 장점 상실, 탐색 시간 O(N)

### 이진 트리 순회 (Tree Traversal)

트리의 모든 노드를 방문하는 방법입니다.

```
      1
     / \
    2   3
   / \
  4   5
```

#### 1. 전위 순회 (Preorder): Root → Left → Right

**순서**: 1 → 2 → 4 → 5 → 3

```java
void preorder(TreeNode node) {
    if (node == null) return;

    System.out.print(node.data + " ");  // 루트 먼저
    preorder(node.left);                // 왼쪽 서브트리
    preorder(node.right);               // 오른쪽 서브트리
}
```

**활용**: 트리 복사, 수식 트리의 전위 표기법

#### 2. 중위 순회 (Inorder): Left → Root → Right

**순서**: 4 → 2 → 5 → 1 → 3

```java
void inorder(TreeNode node) {
    if (node == null) return;

    inorder(node.left);                 // 왼쪽 서브트리
    System.out.print(node.data + " ");  // 루트
    inorder(node.right);                // 오른쪽 서브트리
}
```

**활용**: BST에서 오름차순 출력, 수식 트리의 중위 표기법

**왜 BST에서 중위 순회가 정렬된 결과를 주는가?**

BST의 성질상 "왼쪽 < 루트 < 오른쪽"이므로, Left → Root → Right 순서로 방문하면 오름차순이 됩니다.

#### 3. 후위 순회 (Postorder): Left → Right → Root

**순서**: 4 → 5 → 2 → 3 → 1

```java
void postorder(TreeNode node) {
    if (node == null) return;

    postorder(node.left);               // 왼쪽 서브트리
    postorder(node.right);              // 오른쪽 서브트리
    System.out.print(node.data + " ");  // 루트
}
```

**활용**: 트리 삭제, 수식 트리의 후위 표기법 계산, 디렉토리 크기 계산

**왜 후위 순회로 트리를 삭제하나?**

자식을 먼저 삭제하고 부모를 나중에 삭제해야 메모리 누수가 없기 때문입니다.

#### 4. 레벨 순회 (Level Order): BFS 방식

**순서**: 1 → 2 → 3 → 4 → 5

```java
void levelOrder(TreeNode root) {
    if (root == null) return;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        System.out.print(node.data + " ");

        if (node.left != null) queue.offer(node.left);
        if (node.right != null) queue.offer(node.right);
    }
}
```

**활용**: 레벨별 출력, 최단 경로 찾기, 트리의 너비 계산

### 이진 탐색 트리 (BST: Binary Search Tree)

**정의**: 왼쪽 서브트리의 모든 값 < 루트 < 오른쪽 서브트리의 모든 값

```
      8
     / \
    3   10
   / \    \
  1   6   14
     / \  /
    4  7 13
```

**특징**:
- 중위 순회 시 오름차순 정렬
- 탐색, 삽입, 삭제 평균 O(log N), 최악 O(N)

#### BST 탐색

```java
TreeNode search(TreeNode node, int key) {
    if (node == null || node.data == key) {
        return node;
    }

    if (key < node.data) {
        return search(node.left, key);   // 왼쪽 서브트리 탐색
    } else {
        return search(node.right, key);  // 오른쪽 서브트리 탐색
    }
}
```

**시간 복잡도**: O(h), h는 트리 높이
- 균형 트리: O(log N)
- 편향 트리: O(N)

#### BST 삽입

```java
TreeNode insert(TreeNode node, int key) {
    if (node == null) {
        return new TreeNode(key);
    }

    if (key < node.data) {
        node.left = insert(node.left, key);
    } else if (key > node.data) {
        node.right = insert(node.right, key);
    }

    return node;
}
```

#### BST 삭제

**경우의 수**:

1. **리프 노드**: 그냥 삭제
2. **자식 1개**: 자식을 부모에 연결
3. **자식 2개**: 후속자(오른쪽 서브트리의 최솟값) 또는 선행자(왼쪽 서브트리의 최댓값)로 대체

```java
TreeNode delete(TreeNode node, int key) {
    if (node == null) return null;

    if (key < node.data) {
        node.left = delete(node.left, key);
    } else if (key > node.data) {
        node.right = delete(node.right, key);
    } else {
        // 삭제할 노드 발견

        // Case 1, 2: 자식이 0개 또는 1개
        if (node.left == null) return node.right;
        if (node.right == null) return node.left;

        // Case 3: 자식이 2개
        // 후속자(오른쪽 서브트리의 최솟값) 찾기
        TreeNode successor = findMin(node.right);
        node.data = successor.data;  // 값 복사
        node.right = delete(node.right, successor.data);  // 후속자 삭제
    }

    return node;
}

TreeNode findMin(TreeNode node) {
    while (node.left != null) {
        node = node.left;
    }
    return node;
}
```

**왜 후속자를 사용하나?**

후속자는 현재 노드보다 크면서 가장 작은 값이므로, BST 성질을 유지하면서 대체할 수 있습니다.

### 균형 트리 (Balanced Tree)

**문제**: BST는 입력 순서에 따라 편향될 수 있음 (1, 2, 3, 4... 삽입 시)

**해결**: 자동으로 균형을 유지하는 트리 사용

#### AVL 트리

**정의**: 모든 노드에서 왼쪽과 오른쪽 서브트리의 높이 차이가 1 이하

**균형 인수 (Balance Factor)**: BF = Height(Left) - Height(Right)
- -1, 0, 1 중 하나여야 함
- BF가 2 또는 -2이면 회전 (Rotation)으로 균형 복구

**회전 종류**:
- LL Rotation (Right Rotation)
- RR Rotation (Left Rotation)
- LR Rotation (Left-Right Rotation)
- RL Rotation (Right-Left Rotation)

**시간 복잡도**: 탐색, 삽입, 삭제 모두 O(log N) 보장

**단점**: 삽입/삭제 시 회전이 빈번하여 오버헤드 발생

#### Red-Black 트리

**정의**: 각 노드에 색상(Red/Black)을 부여하여 균형을 유지

**규칙**:
1. 모든 노드는 Red 또는 Black
2. 루트는 Black
3. 모든 리프(NIL)는 Black
4. Red 노드의 자식은 모두 Black (Red 연속 불가)
5. 모든 경로의 Black 노드 개수는 동일

**특징**:
- AVL보다 느슨한 균형 (높이 차이 최대 2배)
- 삽입/삭제 시 회전 횟수 적음
- Java TreeMap, TreeSet의 내부 구조

**시간 복잡도**: O(log N)

**왜 AVL 대신 Red-Black을 사용하나?**

삽입/삭제가 빈번한 경우 Red-Black이 유리합니다. AVL은 더 엄격한 균형으로 탐색이 약간 빠르지만, 삽입/삭제 시 회전이 많아 오버헤드가 큽니다.

## 시간 복잡도

### 이진 탐색 트리 (BST)

| 연산 | 평균 (균형 트리) | 최악 (편향 트리) |
|------|-----------------|-----------------|
| 탐색 | O(log N) | O(N) |
| 삽입 | O(log N) | O(N) |
| 삭제 | O(log N) | O(N) |

### 균형 트리 (AVL, Red-Black)

| 연산 | 시간 복잡도 |
|------|------------|
| 탐색 | O(log N) |
| 삽입 | O(log N) |
| 삭제 | O(log N) |

## 트레이드오프

### BST vs 배열

| 기준 | BST (균형 트리) | 정렬 배열 |
|------|----------------|----------|
| 탐색 | O(log N) | O(log N) - 이진 탐색 |
| 삽입 | O(log N) | O(N) - 요소 이동 |
| 삭제 | O(log N) | O(N) - 요소 이동 |
| 메모리 | 많음 - 포인터 | 적음 - 데이터만 |
| 순차 접근 | O(N) - 중위 순회 | O(N) - 인덱스 순회 |

### AVL vs Red-Black

| 기준 | AVL 트리 | Red-Black 트리 |
|------|---------|---------------|
| 균형 | 엄격 (높이 차이 1) | 느슨 (높이 차이 2배) |
| 탐색 속도 | 약간 빠름 | 약간 느림 |
| 삽입/삭제 | 느림 (회전 많음) | 빠름 (회전 적음) |
| 사용 사례 | 조회가 많음 | 삽입/삭제가 많음 |
| 실제 사용 | Windows 커널 | Linux 커널, Java |

## 면접 예상 질문

- Q: 트리와 그래프의 차이는 무엇인가요?
  - A: 트리는 사이클이 없는 연결 그래프입니다. 트리는 N개 노드에 N-1개 간선, 루트가 있고, 모든 노드가 연결되어 있습니다. 그래프는 사이클 가능, 방향/무방향, 루트 개념 없음 등 더 일반적인 구조입니다. 트리는 계층 구조 표현에 적합하고, 그래프는 네트워크, 관계 등 다양한 관계를 표현합니다.

- Q: 이진 탐색 트리(BST)의 시간 복잡도가 O(log N)인 이유는?
  - A: BST는 각 단계마다 탐색 범위를 절반으로 줄입니다. 루트와 비교하여 작으면 왼쪽, 크면 오른쪽 서브트리만 탐색하기 때문입니다. 높이 h인 균형 트리는 2^h개의 노드를 가지므로 h = log N이 되어 O(log N)입니다. 단, 편향 트리는 높이가 N이 되어 O(N)으로 퇴화합니다.

- Q: BST에서 중위 순회를 하면 정렬된 결과가 나오는 이유는?
  - A: BST의 성질이 "왼쪽 < 루트 < 오른쪽"이기 때문입니다. 중위 순회는 Left → Root → Right 순서로 방문하므로, 작은 값부터 큰 값 순서대로 방문하게 됩니다. 예를 들어 `[4, 2, 5, 1, 3]` BST를 중위 순회하면 `[1, 2, 3, 4, 5]`로 정렬된 결과를 얻습니다.

- Q: BST에서 노드를 삭제할 때 자식이 2개인 경우는 어떻게 처리하나요?
  - A: 후속자(오른쪽 서브트리의 최솟값) 또는 선행자(왼쪽 서브트리의 최댓값)로 대체합니다. 후속자를 사용하는 경우, 삭제할 노드의 값을 후속자 값으로 바꾸고, 후속자 노드를 삭제합니다. 후속자는 현재 노드보다 크면서 가장 작은 값이므로 BST 성질을 유지할 수 있습니다. 후속자는 최대 1개의 자식(오른쪽)만 가지므로 삭제가 간단합니다.

- Q: 완전 이진 트리를 배열로 표현하면 어떤 장점이 있나요?
  - A: 포인터 없이 인덱스 계산만으로 부모/자식에 접근할 수 있습니다. 인덱스 i의 왼쪽 자식은 2i, 오른쪽 자식은 2i+1, 부모는 i/2입니다. 이를 통해 메모리 효율이 높고 캐시 성능이 우수합니다. 힙(Heap)이 배열로 구현되는 이유입니다. 단, 완전 이진 트리가 아니면 빈 공간이 생겨 메모리 낭비가 발생합니다.

- Q: AVL 트리와 Red-Black 트리 중 언제 무엇을 선택하나요?
  - A: 조회가 빈번하면 AVL, 삽입/삭제가 빈번하면 Red-Black을 선택합니다. AVL은 더 엄격한 균형으로 높이가 작아 탐색이 약간 빠르지만, 삽입/삭제 시 회전이 많아 오버헤드가 큽니다. Red-Black은 느슨한 균형으로 회전 횟수가 적어 삽입/삭제가 빠릅니다. 실무에서는 대부분 Red-Black을 사용합니다 (Java TreeMap, Linux 커널 등).

- Q: 트리의 높이와 깊이의 차이는?
  - A: 깊이(Depth)는 루트에서 특정 노드까지의 간선 수로, 위에서 아래 방향입니다. 높이(Height)는 특정 노드에서 가장 먼 리프까지의 간선 수로, 아래에서 위 방향입니다. 트리의 높이는 루트의 높이이며, 모든 노드 중 최대 깊이와 같습니다. 예를 들어 루트의 깊이는 0이고, 높이는 트리 전체의 최대 깊이입니다.

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 12 (BST), 13 (Red-Black Tree)
- [Wikipedia: Binary Tree](https://en.wikipedia.org/wiki/Binary_tree)
- [Wikipedia: AVL Tree](https://en.wikipedia.org/wiki/AVL_tree)
- [Wikipedia: Red-Black Tree](https://en.wikipedia.org/wiki/Red%E2%80%93black_tree)
- [Visualgo: Binary Search Tree](https://visualgo.net/en/bst)
