# 유니온 파인드 (Union-Find)

> `[4] 심화` · 선수 지식: [트리](./tree.md), [그래프](./graph.md)

> 서로소 집합을 효율적으로 관리하는 자료구조

`#유니온파인드` `#UnionFind` `#서로소집합` `#DisjointSet` `#DSU` `#DisjointSetUnion` `#경로압축` `#PathCompression` `#랭크합치기` `#UnionByRank` `#크루스칼` `#Kruskal` `#MST` `#최소신장트리` `#연결성분` `#ConnectedComponent` `#사이클검출` `#CycleDetection` `#네트워크연결` `#그래프알고리즘`

## 왜 알아야 하는가?

유니온 파인드는 그래프에서 연결 여부를 빠르게 판단하는 자료구조입니다. 크루스칼 알고리즘(MST), 사이클 검출, 네트워크 연결 문제 등에 필수입니다. 코딩 테스트에서 자주 출제되는 핵심 자료구조입니다.

## 핵심 개념

- **Find**: 원소가 속한 집합(대표 원소) 찾기
- **Union**: 두 집합을 하나로 합치기
- **경로 압축**: Find 시 트리 높이를 줄여 최적화
- **랭크 합치기**: Union 시 작은 트리를 큰 트리에 붙여 최적화

## 쉽게 이해하기

**유니온 파인드**를 친구 그룹에 비유할 수 있습니다.

- 처음에는 모두 혼자 (각자가 자신의 대표)
- A와 B가 친구가 되면 같은 그룹으로 합침 (Union)
- "A와 C가 같은 그룹?"을 확인하려면 각자의 대표를 찾아 비교 (Find)

## 상세 설명

### 기본 구조

```
초기 상태: 각자 자신이 대표
[0] [1] [2] [3] [4]

Union(0, 1):
  1
  |
  0

Union(2, 3):
  1      3
  |      |
  0      2

Union(1, 3):
      3
    / |
   1  2
   |
   0
```

### 기본 구현 (비최적화)

```java
class UnionFind {
    int[] parent;

    UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;  // 자기 자신이 대표
        }
    }

    // 대표 찾기: O(n) 최악
    int find(int x) {
        if (parent[x] == x) return x;
        return find(parent[x]);
    }

    // 합치기: O(n) 최악
    void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }
}
```

### 최적화 1: 경로 압축 (Path Compression)

Find 시 경로상의 모든 노드를 직접 루트에 연결:

```java
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);  // 경로 압축
    }
    return parent[x];
}
```

```
경로 압축 전:           경로 압축 후:
    4                       4
    |                     / | \
    3                    0  1  3
    |                       |
    1                       2
   / \
  0   2

find(0) 호출 시 0→1→3→4 경로의 모든 노드가 4에 직접 연결됨
```

### 최적화 2: 랭크 합치기 (Union by Rank)

항상 작은 트리를 큰 트리에 붙임:

```java
class UnionFind {
    int[] parent, rank;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return;

        // 작은 랭크의 트리를 큰 랭크에 붙임
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }
}
```

### 시간 복잡도

| 연산 | 비최적화 | 경로 압축만 | 경로 압축 + 랭크 합치기 |
|------|---------|-----------|---------------------|
| Find | O(n) | O(log n) 상각 | O(α(n)) ≈ O(1) |
| Union | O(n) | O(log n) 상각 | O(α(n)) ≈ O(1) |

*α(n) = 역아커만 함수, 실질적으로 상수*

### 활용 예시: 크루스칼 MST

```java
// 간선을 가중치 순 정렬 후
for (Edge edge : edges) {
    if (find(edge.from) != find(edge.to)) {
        union(edge.from, edge.to);
        mst.add(edge);
        if (mst.size() == n - 1) break;
    }
}
```

### 활용 예시: 사이클 검출

```java
boolean hasCycle(int[][] edges) {
    for (int[] edge : edges) {
        int x = edge[0], y = edge[1];
        if (find(x) == find(y)) {
            return true;  // 사이클 발견
        }
        union(x, y);
    }
    return false;
}
```

### 활용 예시: 연결 요소 개수

```java
int countComponents(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);
    for (int[] edge : edges) {
        uf.union(edge[0], edge[1]);
    }

    Set<Integer> roots = new HashSet<>();
    for (int i = 0; i < n; i++) {
        roots.add(uf.find(i));
    }
    return roots.size();
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| Union/Find가 거의 O(1) | 집합 분리(분할) 불가 |
| 구현 간단 | 집합의 원소 열거 어려움 |
| 동적 연결성 판단에 최적 | 경로 정보 손실 |

## 면접 예상 질문

### Q: 유니온 파인드가 필요한 상황은?

A: (1) **연결 여부 판단**: 두 노드가 같은 그룹인지 (2) **크루스칼 MST**: 사이클 없이 간선 추가 (3) **네트워크 연결**: 컴퓨터/서버 연결 관리 (4) **이미지 분할**: 같은 영역 그룹핑. **핵심**: 동적으로 그룹이 합쳐지고 같은 그룹인지 확인이 필요할 때 사용합니다.

### Q: 경로 압축이 중요한 이유는?

A: 경로 압축 없이는 트리가 선형으로 길어져 Find가 O(n)이 됩니다. **경로 압축**은 Find 시 모든 노드를 루트에 직접 연결하여 다음 Find를 O(1)로 만듭니다. 랭크 합치기와 함께 사용하면 거의 상수 시간(O(α(n)))에 동작합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [그래프](./graph.md) | 선수 지식 | [4] 심화 |
| [트리](./tree.md) | 내부 구조 | [3] 중급 |

## 참고 자료

- Introduction to Algorithms (CLRS)
- [CP-Algorithms - DSU](https://cp-algorithms.com/data_structures/disjoint_set_union.html)
