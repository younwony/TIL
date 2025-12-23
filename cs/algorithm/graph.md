# 그래프 알고리즘 (Graph Algorithms)

> `[4] 심화` · 선수 지식: [시간 복잡도](./time-complexity.md), [탐색](./search.md), [자료구조](../data-structure/README.md)

> 노드(정점)와 간선으로 이루어진 그래프 자료구조에서 경로 탐색, 최단 거리 계산 등을 수행하는 알고리즘

`#그래프` `#Graph` `#DFS` `#깊이우선탐색` `#DepthFirstSearch` `#BFS` `#너비우선탐색` `#BreadthFirstSearch` `#다익스트라` `#Dijkstra` `#벨만포드` `#BellmanFord` `#플로이드워셜` `#FloydWarshall` `#최단경로` `#ShortestPath` `#위상정렬` `#TopologicalSort` `#MST` `#최소신장트리` `#크루스칼` `#Kruskal` `#프림` `#Prim` `#네트워크` `#정점` `#Vertex` `#간선` `#Edge` `#인접리스트` `#인접행렬`

## 왜 알아야 하는가?

- **실무**: 네트워크 라우팅, SNS 친구 추천, 지도 경로 탐색, 작업 스케줄링 등 실세계 연결 관계 문제 해결의 핵심입니다. 웹 크롤링, 의존성 분석, 추천 시스템의 기반이 됩니다.
- **면접**: DFS/BFS는 코딩 테스트 필수이며, 다익스트라, 위상 정렬 등은 고급 문제에서 자주 출제됩니다. 문제 모델링 능력을 평가하는 핵심 주제입니다.
- **기반 지식**: 탐색, 정렬, 동적 프로그래밍과 결합되어 복잡한 문제를 해결합니다. 트리, 힙, 우선순위 큐 등 자료구조 활용 능력을 종합적으로 요구합니다.

## 핵심 개념

- **그래프 표현**: 인접 행렬 vs 인접 리스트 - 공간과 탐색 속도의 트레이드오프
- **그래프 탐색**: DFS(깊이 우선), BFS(너비 우선) - 스택 vs 큐
- **최단 경로**: 다익스트라(가중치 양수), 벨만-포드(음수 가능), 플로이드-워셜(모든 쌍)
- **최소 신장 트리 (MST)**: 크루스칼(간선 정렬), 프림(정점 확장)
- **위상 정렬**: DAG(방향 비순환 그래프)에서 순서 결정

## 쉽게 이해하기

**그래프 알고리즘**을 지도와 길찾기에 비유할 수 있습니다.

도시(노드)들이 도로(간선)로 연결된 지도가 있다면:

- **DFS (깊이 우선 탐색)**: 한 길을 끝까지 가보고, 막히면 돌아와서 다른 길 시도. 미로 탈출 방식
- **BFS (너비 우선 탐색)**: 현재 위치에서 1칸 거리, 2칸 거리, 3칸 거리... 동심원처럼 퍼져나가며 탐색
- **다익스트라**: 출발지에서 가장 가까운 도시부터 차례로 방문하며 최단 거리 갱신 (네비게이션)
- **벨만-포드**: 모든 도로를 여러 번 확인하며 더 짧은 경로 발견 (음수 도로도 가능)
- **플로이드-워셜**: 모든 도시 쌍의 최단 거리를 한 번에 계산 (전체 거리표 작성)
- **크루스칼 MST**: 가장 짧은 도로부터 선택하되, 사이클(순환)이 생기지 않게 연결
- **위상 정렬**: 대학 선수과목처럼 "A를 들어야 B를 들을 수 있음" 같은 순서 결정

예를 들어, 서울에서 부산까지 가는 경로를 찾는다면:
- BFS: 최소 경유지 수(환승 최소)
- 다익스트라: 최소 거리 또는 최소 시간
- MST: 전국 모든 도시를 연결하는 최소 비용 도로망

## 상세 설명

### 1. 그래프 표현

#### 인접 행렬 (Adjacency Matrix)

2차원 배열로 간선 존재 여부를 표현합니다.

```java
// V개의 정점
int[][] adjMatrix = new int[V][V];

// 간선 (u, v) 추가
adjMatrix[u][v] = 1;
adjMatrix[v][u] = 1; // 무방향 그래프

// 가중치 그래프
adjMatrix[u][v] = weight;
```

**특징**:
- 공간 복잡도: O(V²)
- 간선 존재 확인: O(1)
- 모든 간선 순회: O(V²)

**언제 사용하나?**

- 간선이 많은 밀집 그래프 (E ≈ V²)
- 간선 존재 확인이 빈번할 때
- 플로이드-워셜 알고리즘 (모든 쌍 최단 경로)

#### 인접 리스트 (Adjacency List)

각 정점마다 인접한 정점들의 리스트를 저장합니다.

```java
// ArrayList 방식
List<List<Integer>> adjList = new ArrayList<>();
for (int i = 0; i < V; i++) {
    adjList.add(new ArrayList<>());
}

// 간선 (u, v) 추가
adjList.get(u).add(v);
adjList.get(v).add(u); // 무방향 그래프

// 가중치 그래프 (Pair 사용)
class Edge {
    int to, weight;
    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

List<List<Edge>> graph = new ArrayList<>();
```

**특징**:
- 공간 복잡도: O(V + E)
- 간선 존재 확인: O(degree) - 인접 노드 수
- 모든 간선 순회: O(V + E)

**언제 사용하나?**

- 간선이 적은 희소 그래프 (E << V²)
- DFS, BFS, 다익스트라 등 대부분의 그래프 알고리즘

**왜 인접 리스트가 더 많이 사용되나?**

실제 그래프는 대부분 희소 그래프입니다. 예를 들어 SNS 친구 관계에서 사람 100만 명이 있어도 한 사람의 친구는 수백~수천 명입니다. 인접 행렬은 100만 × 100만 = 1조 공간이 필요하지만, 인접 리스트는 100만 + 친구 수만큼만 필요합니다.

### 2. 그래프 탐색

#### DFS (Depth-First Search, 깊이 우선 탐색)

한 경로를 끝까지 탐색한 후 다른 경로를 탐색하는 방식입니다.

**왜 이렇게 하는가?**

스택(또는 재귀)을 사용하여 현재 경로를 끝까지 탐색합니다. 백트래킹, 사이클 감지, 위상 정렬 등에 활용됩니다.

```java
// 재귀 방식
public void dfs(int v, boolean[] visited, List<List<Integer>> graph) {
    visited[v] = true;
    System.out.print(v + " ");

    for (int next : graph.get(v)) {
        if (!visited[next]) {
            dfs(next, visited, graph);
        }
    }
}

// 스택 방식
public void dfsIterative(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Stack<Integer> stack = new Stack<>();

    stack.push(start);

    while (!stack.isEmpty()) {
        int v = stack.pop();

        if (visited[v]) continue;

        visited[v] = true;
        System.out.print(v + " ");

        // 스택이므로 역순으로 추가 (작은 번호부터 방문하려면)
        for (int i = graph.get(v).size() - 1; i >= 0; i--) {
            int next = graph.get(v).get(i);
            if (!visited[next]) {
                stack.push(next);
            }
        }
    }
}
```

**특징**:
- 시간 복잡도: O(V + E)
- 공간 복잡도: O(V) - 재귀 스택 또는 명시적 스택
- 경로 찾기, 사이클 감지, 위상 정렬에 사용

**언제 사용하나?**

- 모든 경로 탐색 (백트래킹)
- 사이클 감지
- 위상 정렬
- 연결 요소 찾기

#### BFS (Breadth-First Search, 너비 우선 탐색)

현재 정점에서 가까운 정점부터 탐색하는 방식입니다.

**왜 이렇게 하는가?**

큐를 사용하여 같은 거리의 정점들을 먼저 방문합니다. 최단 경로(가중치 없는 그래프)를 보장합니다.

```java
public void bfs(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Queue<Integer> queue = new LinkedList<>();

    visited[start] = true;
    queue.offer(start);

    while (!queue.isEmpty()) {
        int v = queue.poll();
        System.out.print(v + " ");

        for (int next : graph.get(v)) {
            if (!visited[next]) {
                visited[next] = true;
                queue.offer(next);
            }
        }
    }
}

// 최단 거리 계산
public int[] bfsDistance(int start, List<List<Integer>> graph) {
    int[] dist = new int[graph.size()];
    Arrays.fill(dist, -1);
    Queue<Integer> queue = new LinkedList<>();

    dist[start] = 0;
    queue.offer(start);

    while (!queue.isEmpty()) {
        int v = queue.poll();

        for (int next : graph.get(v)) {
            if (dist[next] == -1) {
                dist[next] = dist[v] + 1;
                queue.offer(next);
            }
        }
    }

    return dist;
}
```

**특징**:
- 시간 복잡도: O(V + E)
- 공간 복잡도: O(V) - 큐
- **가중치 없는 그래프의 최단 경로 보장**

**언제 사용하나?**

- 최단 경로 (가중치 없음 또는 모두 1)
- 최소 이동 횟수
- 레벨 순회 (트리의 레벨별 방문)

**DFS vs BFS 선택 기준**:

| 상황 | 선택 | 이유 |
|------|------|------|
| 최단 경로 (가중치 없음) | BFS | 가까운 정점부터 방문하므로 보장 |
| 모든 경로 탐색 | DFS | 백트래킹 가능 |
| 메모리 제약 | DFS | 스택 깊이만큼 (보통 BFS보다 적음) |
| 목표가 깊은 곳 | DFS | 빠르게 도달 |
| 목표가 가까운 곳 | BFS | 빠르게 발견 |

### 3. 최단 경로 알고리즘

#### 다익스트라 (Dijkstra)

출발점에서 모든 정점까지의 최단 거리를 구하는 알고리즘입니다.

**왜 이렇게 하는가?**

현재까지 확정된 최단 거리 중 가장 가까운 정점을 선택하고, 그 정점을 거쳐가는 경로를 확인하며 거리를 갱신합니다. **탐욕법(Greedy)** 전략입니다.

```java
class Node implements Comparable<Node> {
    int vertex, distance;

    Node(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
        return this.distance - other.distance;
    }
}

public int[] dijkstra(int start, List<List<Node>> graph) {
    int V = graph.size();
    int[] dist = new int[V];
    Arrays.fill(dist, Integer.MAX_VALUE);

    PriorityQueue<Node> pq = new PriorityQueue<>();
    dist[start] = 0;
    pq.offer(new Node(start, 0));

    while (!pq.isEmpty()) {
        Node current = pq.poll();
        int u = current.vertex;

        // 이미 처리된 정점 스킵
        if (current.distance > dist[u]) continue;

        // 인접 정점 거리 갱신
        for (Node edge : graph.get(u)) {
            int v = edge.vertex;
            int weight = edge.distance;

            if (dist[u] + weight < dist[v]) {
                dist[v] = dist[u] + weight;
                pq.offer(new Node(v, dist[v]));
            }
        }
    }

    return dist;
}
```

**특징**:
- 시간 복잡도: O((V + E) log V) - 우선순위 큐 사용
- **가중치가 모두 양수일 때만 동작**
- 단일 출발점 최단 경로

**왜 음수 가중치에서 실패하나?**

이미 확정된 정점의 거리가 나중에 음수 간선으로 더 짧아질 수 있습니다. 다익스트라는 한 번 확정된 정점을 다시 방문하지 않으므로 음수 가중치를 처리할 수 없습니다.

**언제 사용하나?**

- 네비게이션 (도로 길이, 소요 시간)
- 네트워크 라우팅
- 게임 경로 탐색

#### 벨만-포드 (Bellman-Ford)

음수 가중치가 있어도 최단 거리를 구하는 알고리즘입니다.

**왜 이렇게 하는가?**

모든 간선을 V-1번 반복하며 거리를 갱신합니다. V-1번이면 최단 경로(최대 V-1개 간선)가 반드시 발견됩니다.

```java
class Edge {
    int from, to, weight;

    Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

public int[] bellmanFord(int start, int V, List<Edge> edges) {
    int[] dist = new int[V];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;

    // V-1번 반복
    for (int i = 0; i < V - 1; i++) {
        for (Edge edge : edges) {
            if (dist[edge.from] != Integer.MAX_VALUE &&
                dist[edge.from] + edge.weight < dist[edge.to]) {
                dist[edge.to] = dist[edge.from] + edge.weight;
            }
        }
    }

    // 음수 사이클 검증
    for (Edge edge : edges) {
        if (dist[edge.from] != Integer.MAX_VALUE &&
            dist[edge.from] + edge.weight < dist[edge.to]) {
            throw new IllegalStateException("Negative cycle detected");
        }
    }

    return dist;
}
```

**특징**:
- 시간 복잡도: O(VE)
- 음수 가중치 처리 가능
- 음수 사이클 감지 가능

**왜 V-1번 반복하나?**

최단 경로는 최대 V-1개의 간선을 가집니다 (V개 정점을 거치므로). 각 반복마다 최소 1개 정점의 최단 거리가 확정되므로 V-1번이면 충분합니다.

**언제 사용하나?**

- 음수 가중치가 있는 그래프
- 음수 사이클 감지가 필요할 때
- 시간이 여유롭고 안정성이 중요할 때

#### 플로이드-워셜 (Floyd-Warshall)

모든 정점 쌍의 최단 거리를 구하는 알고리즘입니다.

**왜 이렇게 하는가?**

k번 정점을 경유지로 고려하며 모든 쌍 (i, j)의 최단 거리를 갱신합니다. 동적 프로그래밍입니다.

```java
public int[][] floydWarshall(int[][] graph) {
    int V = graph.length;
    int[][] dist = new int[V][V];

    // 초기화
    for (int i = 0; i < V; i++) {
        for (int j = 0; j < V; j++) {
            dist[i][j] = graph[i][j];
        }
    }

    // k를 경유지로 고려
    for (int k = 0; k < V; k++) {
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][k] != Integer.MAX_VALUE &&
                    dist[k][j] != Integer.MAX_VALUE &&
                    dist[i][k] + dist[k][j] < dist[i][j]) {
                    dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
    }

    return dist;
}
```

**특징**:
- 시간 복잡도: O(V³)
- 공간 복잡도: O(V²)
- 모든 쌍 최단 경로
- 음수 가중치 가능

**언제 사용하나?**

- 모든 정점 쌍의 최단 거리가 필요할 때
- 정점 수가 적을 때 (V ≤ 500)
- 거리 행렬이 이미 있을 때

**왜 다익스트라를 V번 하지 않고 플로이드-워셜을 사용하나?**

다익스트라 V번: O(V² log V + VE log V) - 희소 그래프에서 유리
플로이드-워셜: O(V³) - 밀집 그래프나 코드가 간결할 때 유리

### 4. 최소 신장 트리 (Minimum Spanning Tree, MST)

모든 정점을 연결하되 간선 가중치 합이 최소인 트리입니다.

**왜 필요한가?**

N개 도시를 모두 연결하는 최소 비용 도로망, 네트워크 배선 최소화 등에 사용됩니다.

#### 크루스칼 (Kruskal)

간선을 가중치 오름차순으로 정렬하여 사이클이 생기지 않게 선택합니다.

**왜 이렇게 하는가?**

가장 짧은 간선부터 선택하되, 사이클이 생기면 건너뜁니다. Union-Find로 사이클을 O(α(V))에 감지합니다.

```java
class UnionFind {
    int[] parent, rank;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 경로 압축
        }
        return parent[x];
    }

    boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return false; // 이미 같은 집합

        // Rank 기반 union
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        return true;
    }
}

public int kruskal(int V, List<Edge> edges) {
    Collections.sort(edges, (a, b) -> a.weight - b.weight);

    UnionFind uf = new UnionFind(V);
    int mstWeight = 0;
    int edgeCount = 0;

    for (Edge edge : edges) {
        if (uf.union(edge.from, edge.to)) {
            mstWeight += edge.weight;
            edgeCount++;

            if (edgeCount == V - 1) break; // MST 완성
        }
    }

    return mstWeight;
}
```

**특징**:
- 시간 복잡도: O(E log E) - 정렬
- 희소 그래프에 유리

#### 프림 (Prim)

하나의 정점에서 시작하여 트리를 확장해가는 방식입니다.

**왜 이렇게 하는가?**

현재 트리에 연결된 간선 중 가장 가중치가 작은 것을 선택합니다. 다익스트라와 유사하게 우선순위 큐를 사용합니다.

```java
public int prim(int start, List<List<Node>> graph) {
    int V = graph.size();
    boolean[] visited = new boolean[V];
    PriorityQueue<Node> pq = new PriorityQueue<>();

    int mstWeight = 0;
    pq.offer(new Node(start, 0));

    while (!pq.isEmpty()) {
        Node current = pq.poll();
        int u = current.vertex;

        if (visited[u]) continue;

        visited[u] = true;
        mstWeight += current.distance;

        for (Node edge : graph.get(u)) {
            if (!visited[edge.vertex]) {
                pq.offer(edge);
            }
        }
    }

    return mstWeight;
}
```

**특징**:
- 시간 복잡도: O(E log V)
- 밀집 그래프에 유리

**크루스칼 vs 프림**:

| 특성 | 크루스칼 | 프림 |
|------|---------|------|
| 전략 | 간선 중심 | 정점 중심 |
| 정렬 필요 | O | X |
| 희소 그래프 | 유리 | 불리 |
| 밀집 그래프 | 불리 | 유리 |

### 5. 위상 정렬 (Topological Sort)

방향 비순환 그래프(DAG)에서 정점들의 선형 순서를 결정합니다.

**왜 필요한가?**

선수 과목, 빌드 순서, 작업 스케줄링 등 의존 관계가 있는 작업의 순서를 결정합니다.

```java
// DFS 방식
public List<Integer> topologicalSort(List<List<Integer>> graph) {
    int V = graph.size();
    boolean[] visited = new boolean[V];
    Stack<Integer> stack = new Stack<>();

    for (int i = 0; i < V; i++) {
        if (!visited[i]) {
            topologicalSortDFS(i, visited, stack, graph);
        }
    }

    List<Integer> result = new ArrayList<>();
    while (!stack.isEmpty()) {
        result.add(stack.pop());
    }

    return result;
}

private void topologicalSortDFS(int v, boolean[] visited, Stack<Integer> stack,
                                List<List<Integer>> graph) {
    visited[v] = true;

    for (int next : graph.get(v)) {
        if (!visited[next]) {
            topologicalSortDFS(next, visited, stack, graph);
        }
    }

    stack.push(v); // 후위 순회 순서
}

// Kahn's 알고리즘 (진입 차수 이용)
public List<Integer> topologicalSortKahn(List<List<Integer>> graph) {
    int V = graph.size();
    int[] inDegree = new int[V];

    // 진입 차수 계산
    for (int u = 0; u < V; u++) {
        for (int v : graph.get(u)) {
            inDegree[v]++;
        }
    }

    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < V; i++) {
        if (inDegree[i] == 0) {
            queue.offer(i);
        }
    }

    List<Integer> result = new ArrayList<>();

    while (!queue.isEmpty()) {
        int u = queue.poll();
        result.add(u);

        for (int v : graph.get(u)) {
            inDegree[v]--;
            if (inDegree[v] == 0) {
                queue.offer(v);
            }
        }
    }

    // 사이클 검증
    if (result.size() != V) {
        throw new IllegalStateException("Cycle detected");
    }

    return result;
}
```

**특징**:
- 시간 복잡도: O(V + E)
- DAG에서만 가능 (사이클 있으면 불가능)
- 여러 해가 존재할 수 있음

**언제 사용하나?**

- 대학 선수 과목 순서
- 빌드 시스템 의존성 해결
- 프로젝트 작업 스케줄링

## 알고리즘 선택 가이드

| 목적 | 알고리즘 | 시간 복잡도 | 조건 |
|------|---------|------------|------|
| **그래프 탐색** | DFS | O(V + E) | 모든 경로, 사이클 감지 |
| | BFS | O(V + E) | 최단 경로 (가중치 없음) |
| **최단 경로 (단일 출발)** | 다익스트라 | O((V+E) log V) | 가중치 양수 |
| | 벨만-포드 | O(VE) | 음수 가중치 가능 |
| **최단 경로 (모든 쌍)** | 플로이드-워셜 | O(V³) | 작은 그래프, 음수 가능 |
| **최소 신장 트리** | 크루스칼 | O(E log E) | 희소 그래프 |
| | 프림 | O(E log V) | 밀집 그래프 |
| **위상 정렬** | DFS / Kahn | O(V + E) | DAG만 가능 |

## 면접 예상 질문

- Q: DFS와 BFS의 차이와 각각 언제 사용하나요?
  - A: DFS는 스택(재귀)으로 한 경로를 끝까지 탐색하고, BFS는 큐로 가까운 정점부터 탐색합니다. **따라서** BFS는 가중치 없는 그래프의 최단 경로를 보장하지만, DFS는 보장하지 않습니다. **언제 사용하나?** 최단 경로가 필요하면 BFS, 모든 경로 탐색(백트래킹)이나 사이클 감지는 DFS입니다. **메모리 측면**에서 DFS는 깊이만큼, BFS는 너비만큼 메모리를 사용하므로 트리가 넓고 얕으면 DFS가, 좁고 깊으면 BFS가 유리합니다. **실무 예시**: 미로 탈출은 BFS(최소 이동), 파일 시스템 탐색은 DFS입니다.

- Q: 다익스트라와 벨만-포드의 차이는?
  - A: 둘 다 단일 출발점 최단 경로 알고리즘이지만, 다익스트라는 O((V+E) log V)로 빠르지만 **양수 가중치만 가능**합니다. 벨만-포드는 O(VE)로 느리지만 **음수 가중치와 음수 사이클 감지가 가능**합니다. **왜 다익스트라는 음수를 못 다루나?** 이미 확정된 정점의 거리가 나중에 음수 간선으로 더 짧아질 수 있는데, 다익스트라는 확정된 정점을 재방문하지 않기 때문입니다. **실무 선택**: 네비게이션, 네트워크 라우팅 등 대부분은 양수이므로 다익스트라를 사용합니다.

- Q: 크루스칼과 프림 알고리즘의 차이는?
  - A: 둘 다 MST를 구하지만, 크루스칼은 **간선 중심**으로 가장 짧은 간선을 선택하고, 프림은 **정점 중심**으로 트리를 확장합니다. **시간 복잡도**는 크루스칼이 O(E log E) (정렬), 프림이 O(E log V) (우선순위 큐)입니다. **희소 그래프**(간선 적음)에서는 크루스칼이, **밀집 그래프**(간선 많음)에서는 프림이 유리합니다. **왜냐하면** 크루스칼은 간선 정렬 비용이 지배적이고, 프림은 간선마다 우선순위 큐 연산이 발생하기 때문입니다. **구현 난이도**는 크루스칼이 Union-Find가 필요하지만 간결하고, 프림은 다익스트라와 유사합니다.

- Q: 위상 정렬은 언제 사용하나요?
  - A: **방향 비순환 그래프(DAG)**에서 정점들의 선형 순서를 결정할 때 사용합니다. **대표 예시**는 대학 선수과목("자료구조를 들어야 알고리즘 수강 가능"), 빌드 시스템("A.java를 컴파일해야 B.java 컴파일 가능"), 프로젝트 작업 스케줄링입니다. **구현 방법**은 DFS 후위 순회 역순 또는 Kahn 알고리즘(진입 차수 0인 정점부터 제거)입니다. **사이클이 있으면?** 위상 정렬이 불가능합니다. 예를 들어 A→B→C→A 같은 순환 의존성은 순서를 정할 수 없습니다. **따라서** Kahn 알고리즘으로 사이클 감지가 가능합니다(결과 크기 < V이면 사이클 존재).

- Q: 플로이드-워셜 알고리즘은 언제 사용하나요?
  - A: **모든 정점 쌍의 최단 거리**가 필요할 때 사용합니다. 다익스트라를 V번 실행하는 것과 비교하면, 다익스트라 V번은 O(V(V+E) log V)이고 플로이드-워셜은 O(V³)입니다. **따라서** 밀집 그래프(E ≈ V²)나 정점 수가 적을 때(V ≤ 500) 플로이드-워셜이 유리합니다. **또한** 코드가 3중 for문으로 매우 간결하고, **음수 가중치도 처리 가능**합니다. **실무 예시**: 도시 간 거리표 작성, 네트워크 지연 시간 행렬 계산 등입니다. **주의**: 공간 O(V²)이 필요하므로 V가 크면 메모리 부족이 발생할 수 있습니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [시간 복잡도](./time-complexity.md) | 선수 지식: 그래프 알고리즘 성능 분석 기반 | [2] 입문 |
| [탐색](./search.md) | 선수 지식: DFS/BFS의 기본 개념 | [3] 중급 |
| [동적 프로그래밍](./dynamic-programming.md) | 관련 개념: 플로이드-워셜, 트리 DP | [4] 심화 |
| [정렬](./sort.md) | 관련 개념: 크루스칼 MST의 간선 정렬 | [3] 중급 |
| [자료구조](../data-structure/README.md) | 선수 지식: 우선순위 큐, 스택, 큐 활용 | [2] 입문 |

## 참고 자료

- Introduction to Algorithms (CLRS) - 22장(Graph Algorithms), 24장(Single-Source Shortest Paths), 25장(All-Pairs Shortest Paths)
- Algorithms 4th Edition (Robert Sedgewick) - 4장 Graphs
- [VisuAlgo](https://visualgo.net/en) - 그래프 알고리즘 시각화 (DFS, BFS, Dijkstra, MST 등)
- [Graph Theory Playlist by William Fiset](https://www.youtube.com/playlist?list=PLDV1Zeh2NRsDGO4--qE8yH72HFL1Km93P) - 상세한 그래프 이론 강의
