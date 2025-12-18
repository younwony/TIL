# 그래프 (Graph)

> 정점(Vertex)과 간선(Edge)으로 이루어진 비선형 자료구조로, 객체 간의 관계를 표현

## 핵심 개념

- **정점 (Vertex, Node)**: 데이터를 저장하는 노드
- **간선 (Edge)**: 정점 간의 연결 관계
- **방향성**: 방향 그래프(Directed) vs 무방향 그래프(Undirected)
- **가중치**: 간선에 비용, 거리 등의 값이 부여될 수 있음
- **사이클**: 시작 정점으로 돌아오는 경로가 존재 가능

## 쉽게 이해하기

**그래프**를 지하철 노선도에 비유할 수 있습니다.

지하철 노선도를 생각해보세요. 각 역(정점)은 선로(간선)로 연결되어 있습니다.

예를 들어:
- **정점 (Vertex)**: 강남역, 신사역, 홍대입구역 등
- **간선 (Edge)**: 역들을 연결하는 선로
- **가중치 (Weight)**: 역 사이의 거리나 소요 시간
- **경로 (Path)**: 강남역 → 신사역 → 홍대입구역

특징:
- 환승역에서 여러 노선이 만남 (한 정점에 여러 간선)
- 순환선은 사이클이 있음 (2호선)
- 일부 구간은 양방향 (무방향), 일부는 일방통행 (방향)
- 최단 경로 찾기가 중요함

이렇게 복잡한 네트워크 관계를 표현하는 것이 그래프입니다.

## 상세 설명

### 그래프 종류

#### 1. 방향 그래프 (Directed Graph, Digraph)

간선에 방향이 있는 그래프입니다.

```
A → B
↑   ↓
D ← C
```

**특징**:
- A → B: A에서 B로 가는 간선 (B → A와 다름)
- 진입 차수 (In-degree): 정점으로 들어오는 간선 수
- 진출 차수 (Out-degree): 정점에서 나가는 간선 수

**사용 사례**:
- 웹페이지 링크 (A → B: A에서 B로 링크)
- 작업 의존성 (Task B는 Task A 완료 후 실행)
- SNS 팔로우 (A → B: A가 B를 팔로우)

#### 2. 무방향 그래프 (Undirected Graph)

간선에 방향이 없는 그래프입니다.

```
A — B
|   |
D — C
```

**특징**:
- A — B: A ↔ B (양방향)
- 차수 (Degree): 정점에 연결된 간선 수

**사용 사례**:
- 친구 관계 (A와 B는 친구)
- 도로망 (양방향 도로)
- 네트워크 연결 (컴퓨터 간 연결)

#### 3. 가중치 그래프 (Weighted Graph)

간선에 가중치(비용, 거리)가 있는 그래프입니다.

```
    5
A ——— B
|  3   |
2      4
|      |
D ——— C
    6
```

**사용 사례**:
- 도로망 (거리, 시간)
- 네트워크 (대역폭, 지연 시간)
- 비용 (운송비, 통신료)

#### 4. 완전 그래프 (Complete Graph)

모든 정점 쌍이 간선으로 연결된 그래프입니다.

**특징**:
- N개 정점 → N(N-1)/2개 간선 (무방향 그래프)
- 모든 정점에서 모든 정점으로 직접 이동 가능

#### 5. 연결 그래프 (Connected Graph)

모든 정점 쌍 사이에 경로가 존재하는 그래프입니다.

**비연결 그래프 (Disconnected Graph)**:
```
A — B    D — E
    |
    C
```

A, B, C는 연결되어 있지만, D, E와는 분리되어 있습니다.

#### 6. 순환 그래프 (Cyclic Graph) vs 비순환 그래프 (Acyclic Graph)

**순환 (Cycle)**: 시작 정점으로 돌아오는 경로 존재

```
A → B
↑   ↓
D ← C
```

**비순환**: 사이클이 없음

**DAG (Directed Acyclic Graph)**:
- 방향 그래프 + 사이클 없음
- 위상 정렬 가능
- 사용 사례: 작업 스케줄링, 프로젝트 관리, Git 커밋 히스토리

### 그래프 표현 방법

#### 1. 인접 행렬 (Adjacency Matrix)

2차원 배열로 그래프를 표현합니다.

```
    A B C D
A [ 0 1 0 1 ]
B [ 1 0 1 0 ]
C [ 0 1 0 1 ]
D [ 1 0 1 0 ]
```

**구현**:
```java
class GraphMatrix {
    private int[][] matrix;
    private int vertices;

    public GraphMatrix(int vertices) {
        this.vertices = vertices;
        this.matrix = new int[vertices][vertices];
    }

    // 무방향 그래프 간선 추가 - O(1)
    public void addEdge(int from, int to) {
        matrix[from][to] = 1;
        matrix[to][from] = 1;  // 무방향이므로 양방향
    }

    // 방향 그래프 간선 추가 - O(1)
    public void addDirectedEdge(int from, int to) {
        matrix[from][to] = 1;
    }

    // 가중치 그래프 간선 추가 - O(1)
    public void addWeightedEdge(int from, int to, int weight) {
        matrix[from][to] = weight;
        matrix[to][from] = weight;
    }

    // 간선 존재 확인 - O(1)
    public boolean hasEdge(int from, int to) {
        return matrix[from][to] != 0;
    }

    // 특정 정점의 모든 인접 정점 찾기 - O(V)
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            if (matrix[vertex][i] != 0) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }
}
```

**장점**:
- 간선 존재 확인 O(1)
- 구현이 간단
- 간선 추가/삭제 O(1)

**단점**:
- 공간 복잡도 O(V²) - 정점 수가 많으면 메모리 낭비
- 모든 인접 정점 찾기 O(V) - 간선이 적어도 전체 순회
- 희소 그래프 (Sparse Graph)에 비효율적

**언제 사용하나?**

- 밀집 그래프 (Dense Graph): 간선이 많음 (E ≈ V²)
- 간선 존재 확인이 빈번함
- 정점 수가 적음

#### 2. 인접 리스트 (Adjacency List)

각 정점마다 인접한 정점들의 리스트를 저장합니다.

```
A → [B, D]
B → [A, C]
C → [B, D]
D → [A, C]
```

**구현**:
```java
class GraphList {
    private List<List<Integer>> adjList;
    private int vertices;

    public GraphList(int vertices) {
        this.vertices = vertices;
        this.adjList = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    // 무방향 그래프 간선 추가 - O(1)
    public void addEdge(int from, int to) {
        adjList.get(from).add(to);
        adjList.get(to).add(from);  // 무방향
    }

    // 방향 그래프 간선 추가 - O(1)
    public void addDirectedEdge(int from, int to) {
        adjList.get(from).add(to);
    }

    // 간선 존재 확인 - O(degree(v))
    public boolean hasEdge(int from, int to) {
        return adjList.get(from).contains(to);
    }

    // 특정 정점의 모든 인접 정점 찾기 - O(1)
    public List<Integer> getNeighbors(int vertex) {
        return adjList.get(vertex);
    }
}
```

**가중치 그래프 인접 리스트**:
```java
class Edge {
    int to;
    int weight;

    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

class WeightedGraph {
    private List<List<Edge>> adjList;

    public void addEdge(int from, int to, int weight) {
        adjList.get(from).add(new Edge(to, weight));
        adjList.get(to).add(new Edge(from, weight));  // 무방향
    }
}
```

**장점**:
- 공간 복잡도 O(V + E) - 실제 간선만 저장
- 모든 인접 정점 찾기 O(degree(v)) - 빠름
- 희소 그래프에 효율적

**단점**:
- 간선 존재 확인 O(degree(v)) - 리스트 순회 필요
- 구현이 약간 복잡

**언제 사용하나?**

- 희소 그래프 (Sparse Graph): 간선이 적음 (E << V²)
- 대부분의 실제 그래프 (웹 그래프, SNS)
- DFS, BFS 등 순회 알고리즘

**왜 대부분 인접 리스트를 사용하나?**

실제 그래프는 대부분 희소 그래프입니다. 예를 들어 SNS에서 사용자 100만 명이 있어도 각 사용자가 모든 사람과 친구는 아닙니다. 인접 리스트는 실제 간선만 저장하므로 메모리 효율이 훨씬 좋습니다.

### 그래프 순회 (Graph Traversal)

#### 1. DFS (깊이 우선 탐색, Depth-First Search)

한 경로를 끝까지 탐색한 후 다른 경로를 탐색합니다.

```java
class GraphDFS {
    private List<List<Integer>> adjList;
    private boolean[] visited;

    // 재귀 방식 - O(V + E)
    public void dfs(int vertex) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfs(neighbor);
            }
        }
    }

    // 스택 방식 - O(V + E)
    public void dfsIterative(int start) {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[adjList.size()];

        stack.push(start);

        while (!stack.isEmpty()) {
            int vertex = stack.pop();

            if (!visited[vertex]) {
                visited[vertex] = true;
                System.out.print(vertex + " ");

                // 인접 정점을 역순으로 push (재귀와 동일한 순서)
                List<Integer> neighbors = adjList.get(vertex);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    if (!visited[neighbors.get(i)]) {
                        stack.push(neighbors.get(i));
                    }
                }
            }
        }
    }
}
```

**특징**:
- 스택 또는 재귀 사용
- 한 경로를 끝까지 탐색
- 백트래킹에 활용

**사용 사례**:
- 경로 존재 여부 확인
- 사이클 탐지
- 위상 정렬
- 미로 찾기

#### 2. BFS (너비 우선 탐색, Breadth-First Search)

가까운 정점부터 레벨 순서로 탐색합니다.

```java
class GraphBFS {
    private List<List<Integer>> adjList;

    // O(V + E)
    public void bfs(int start) {
        boolean[] visited = new boolean[adjList.size()];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }

    // 최단 경로 거리 구하기
    public int[] bfsDistance(int start) {
        int[] distance = new int[adjList.size()];
        Arrays.fill(distance, -1);  // -1은 미방문

        Queue<Integer> queue = new LinkedList<>();
        distance[start] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int neighbor : adjList.get(vertex)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[vertex] + 1;
                    queue.offer(neighbor);
                }
            }
        }

        return distance;
    }
}
```

**특징**:
- 큐 사용
- 레벨 순서로 탐색
- 최단 경로 보장 (가중치 없는 그래프)

**사용 사례**:
- 최단 경로 찾기 (가중치 없음)
- 레벨별 탐색
- 연결 요소 찾기

**왜 BFS가 최단 경로를 보장하나?**

BFS는 가까운 정점부터 방문하므로, 처음 도달한 경로가 최단 경로입니다. 가중치가 없는 그래프에서는 간선 개수가 거리이므로 BFS가 최적입니다.

### DFS vs BFS

| 기준 | DFS | BFS |
|------|-----|-----|
| 자료구조 | 스택 (재귀) | 큐 |
| 탐색 방식 | 깊이 우선 | 너비 우선 |
| 경로 | 존재 여부 확인 | 최단 경로 (가중치 X) |
| 메모리 | O(H) - 높이 | O(W) - 너비 |
| 사용 사례 | 사이클 탐지, 백트래킹 | 최단 경로, 레벨 탐색 |

**언제 DFS를 사용하나?**

- 모든 경로 탐색
- 사이클 탐지
- 위상 정렬
- 메모리가 제한적일 때 (긴 경로, 좁은 그래프)

**언제 BFS를 사용하나?**

- 최단 경로 (가중치 없음)
- 레벨별 처리
- 가까운 노드부터 탐색
- 넓은 그래프

## 시간 복잡도

### 그래프 표현

| 연산 | 인접 행렬 | 인접 리스트 |
|------|----------|------------|
| 공간 | O(V²) | O(V + E) |
| 간선 추가 | O(1) | O(1) |
| 간선 삭제 | O(1) | O(E) - 리스트 탐색 |
| 간선 확인 | O(1) | O(degree(v)) |
| 모든 인접 정점 | O(V) | O(degree(v)) |
| 전체 순회 | O(V²) | O(V + E) |

### 그래프 순회

| 알고리즘 | 시간 복잡도 | 공간 복잡도 |
|---------|------------|------------|
| DFS | O(V + E) | O(V) - 방문 배열 + O(H) 재귀 스택 |
| BFS | O(V + E) | O(V) - 방문 배열 + 큐 |

## 트레이드오프

### 인접 행렬 vs 인접 리스트

| 기준 | 인접 행렬 | 인접 리스트 |
|------|----------|------------|
| 공간 효율 | 나쁨 (희소 그래프) | 좋음 |
| 간선 확인 | O(1) - 빠름 | O(degree(v)) - 느림 |
| 순회 | O(V²) - 느림 | O(V + E) - 빠름 |
| 구현 | 간단 | 약간 복잡 |
| 사용 | 밀집 그래프, 간선 확인 많음 | 희소 그래프, 순회 많음 |

## 면접 예상 질문

- Q: 그래프와 트리의 차이는 무엇인가요?
  - A: 트리는 사이클이 없는 연결 그래프입니다. 트리는 N개 정점에 N-1개 간선, 루트가 있고, 모든 정점이 하나의 부모와 연결됩니다. 그래프는 사이클 가능, 루트 개념 없음, 간선 개수 제한 없음 등 더 일반적인 구조입니다. 트리는 계층 구조, 그래프는 네트워크 관계를 표현합니다.

- Q: 인접 행렬과 인접 리스트 중 언제 무엇을 사용하나요?
  - A: 희소 그래프(간선이 적음)는 인접 리스트, 밀집 그래프(간선이 많음)는 인접 행렬을 사용합니다. 인접 리스트는 O(V + E) 공간으로 메모리 효율이 좋고 순회가 빠릅니다. 인접 행렬은 O(V²) 공간이지만 간선 존재 확인이 O(1)로 빠릅니다. 실제로는 대부분의 그래프가 희소하므로 인접 리스트를 주로 사용합니다.

- Q: DFS와 BFS의 차이는 무엇이고 언제 사용하나요?
  - A: DFS는 스택(재귀)으로 한 경로를 끝까지 탐색하고, BFS는 큐로 레벨 순서로 탐색합니다. DFS는 사이클 탐지, 백트래킹, 모든 경로 탐색에 사용하고, BFS는 최단 경로(가중치 없음), 레벨별 처리에 사용합니다. BFS가 최단 경로를 보장하는 이유는 가까운 정점부터 방문하므로 처음 도달한 경로가 최단이기 때문입니다.

- Q: 방향 그래프와 무방향 그래프의 차이는?
  - A: 방향 그래프는 간선에 방향이 있어 A → B와 B → A가 다릅니다. 진입/진출 차수가 존재하고, 웹 링크, 작업 의존성 등을 표현합니다. 무방향 그래프는 A — B가 양방향으로 친구 관계, 도로망 등을 표현합니다. 인접 리스트로 구현 시 무방향은 양쪽에 모두 추가해야 하고, 방향은 한쪽만 추가합니다.

- Q: 그래프에서 사이클을 탐지하는 방법은?
  - A: 무방향 그래프는 DFS로 탐지합니다. 방문한 정점을 다시 방문하면 사이클입니다 (단, 직전 부모는 제외). 방향 그래프는 DFS + 상태 추적(방문 중/완료)으로 탐지합니다. 방문 중인 정점을 다시 방문하면 사이클입니다. 또는 위상 정렬로 모든 정점을 처리할 수 없으면 사이클이 있는 것입니다.

- Q: DAG(Directed Acyclic Graph)는 무엇이고 어디에 사용되나요?
  - A: DAG는 방향 그래프이면서 사이클이 없는 그래프입니다. 위상 정렬이 가능하여 작업 순서, 의존성 관리에 사용됩니다. 예를 들어 대학 선수과목(A 수강 후 B), 프로젝트 작업(Task A 완료 후 Task B), Git 커밋 히스토리 등입니다. 사이클이 없어야 하는 이유는 순환 의존성이 있으면 실행 순서를 정할 수 없기 때문입니다.

- Q: 가중치 그래프에서 최단 경로를 찾는 방법은?
  - A: 가중치가 없으면 BFS로 O(V + E), 가중치가 있고 음수가 없으면 다익스트라(Dijkstra)로 O((V + E) log V), 음수 가중치가 있으면 벨만-포드(Bellman-Ford)로 O(VE), 모든 쌍의 최단 경로는 플로이드-워셜(Floyd-Warshall)로 O(V³)입니다. BFS는 가중치를 고려하지 않으므로 가중치 그래프에는 부적합합니다.

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 22 (Graph), 23-25 (최단 경로, MST)
- [Wikipedia: Graph](https://en.wikipedia.org/wiki/Graph_(abstract_data_type))
- [Visualgo: Graph Traversal](https://visualgo.net/en/dfsbfs)
- [Graph Theory Playlist - MIT OpenCourseWare](https://ocw.mit.edu/courses/mathematics/)
