# 로드 밸런싱 알고리즘

## 핵심 정리

- 로드 밸런싱은 여러 서버에 트래픽을 분산하여 성능과 가용성을 높이는 기술
- 적절한 알고리즘 선택은 서버 특성과 요청 패턴에 따라 달라짐
- 서버의 가중치, 상태, 연결 수 등을 고려한 다양한 알고리즘 존재

## Round Robin

요청을 순서대로 각 서버에 분배하는 가장 단순한 방식

### 동작 원리

1. 서버 목록을 순환하며 요청 분배
2. 마지막 서버 다음에는 첫 번째 서버로 돌아감
3. 모든 서버가 동일한 부하를 받는다고 가정

### 구현

```java
public class RoundRobinLoadBalancer {
    private final List<String> servers;
    private final AtomicInteger currentIndex;

    public RoundRobinLoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
        this.currentIndex = new AtomicInteger(0);
    }

    public String getNextServer() {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }
        int index = currentIndex.getAndUpdate(i -> (i + 1) % servers.size());
        return servers.get(index);
    }

    public void addServer(String server) {
        servers.add(server);
    }

    public void removeServer(String server) {
        servers.remove(server);
    }
}
```

### 특징

| 장점 | 단점 |
|------|------|
| 구현이 매우 간단 | 서버 성능 차이 고려 안 함 |
| 오버헤드가 적음 | 요청 처리 시간 차이 무시 |
| 균등한 분배 보장 | 서버 상태 고려 안 함 |

## Weighted Round Robin

서버별 가중치를 부여하여 분배하는 방식

### 구현

```java
public class WeightedRoundRobinLoadBalancer {
    private final List<ServerWeight> servers;
    private int currentIndex;
    private int currentWeight;
    private final int maxWeight;
    private final int gcdWeight;

    public WeightedRoundRobinLoadBalancer(Map<String, Integer> serverWeights) {
        this.servers = serverWeights.entrySet().stream()
            .map(e -> new ServerWeight(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
        this.maxWeight = servers.stream()
            .mapToInt(s -> s.weight).max().orElse(1);
        this.gcdWeight = calculateGCD(servers);
        this.currentIndex = -1;
        this.currentWeight = 0;
    }

    public synchronized String getNextServer() {
        while (true) {
            currentIndex = (currentIndex + 1) % servers.size();
            if (currentIndex == 0) {
                currentWeight -= gcdWeight;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                }
            }
            if (servers.get(currentIndex).weight >= currentWeight) {
                return servers.get(currentIndex).server;
            }
        }
    }

    private static class ServerWeight {
        String server;
        int weight;

        ServerWeight(String server, int weight) {
            this.server = server;
            this.weight = weight;
        }
    }
}
```

## Least Connections

현재 연결 수가 가장 적은 서버에 요청을 분배하는 방식

### 동작 원리

1. 각 서버의 활성 연결 수를 추적
2. 새 요청이 오면 연결 수가 가장 적은 서버 선택
3. 요청 완료 시 해당 서버의 연결 수 감소

### 구현

```java
public class LeastConnectionsLoadBalancer {
    private final Map<String, AtomicInteger> serverConnections;

    public LeastConnectionsLoadBalancer(List<String> servers) {
        this.serverConnections = new ConcurrentHashMap<>();
        servers.forEach(server ->
            serverConnections.put(server, new AtomicInteger(0)));
    }

    public String getNextServer() {
        return serverConnections.entrySet().stream()
            .min(Comparator.comparingInt(e -> e.getValue().get()))
            .map(Map.Entry::getKey)
            .orElseThrow(() -> new IllegalStateException("No servers available"));
    }

    public void onRequestStart(String server) {
        serverConnections.get(server).incrementAndGet();
    }

    public void onRequestComplete(String server) {
        serverConnections.get(server).decrementAndGet();
    }

    public void addServer(String server) {
        serverConnections.put(server, new AtomicInteger(0));
    }

    public void removeServer(String server) {
        serverConnections.remove(server);
    }
}
```

### 특징

| 장점 | 단점 |
|------|------|
| 서버 부하 실시간 반영 | 연결 수 추적 오버헤드 |
| 처리 시간이 다른 요청에 적합 | 구현이 상대적으로 복잡 |
| 동적 부하 분산 가능 | 연결 시작/종료 이벤트 필요 |

## 알고리즘 비교

| 알고리즘 | 복잡도 | 사용 사례 |
|----------|--------|-----------|
| Round Robin | O(1) | 동일 성능 서버, 균등한 요청 |
| Weighted RR | O(1) | 서버 성능이 다른 경우 |
| Least Connections | O(n) | 요청 처리 시간이 다양한 경우 |
| IP Hash | O(1) | 세션 유지가 필요한 경우 |

## 기타 알고리즘

### IP Hash

클라이언트 IP를 해시하여 항상 같은 서버로 라우팅

```java
public String getServer(String clientIp) {
    int hash = clientIp.hashCode();
    int index = Math.abs(hash) % servers.size();
    return servers.get(index);
}
```

### Random

무작위로 서버를 선택하는 방식

```java
public String getServer() {
    int index = random.nextInt(servers.size());
    return servers.get(index);
}
```

## 면접 예상 질문

1. **Least Connections가 Round Robin보다 좋은 상황은?**
   - 요청 처리 시간이 크게 다를 때
   - Long-polling이나 WebSocket 연결이 있을 때
   - 서버 성능에 차이가 있을 때

2. **세션 유지가 필요할 때 어떤 로드 밸런싱을 사용하나요?**
   - IP Hash 또는 Sticky Session 사용
   - 쿠키 기반 라우팅
   - Session Clustering과 함께 Round Robin

3. **Health Check는 왜 필요한가요?**
   - 장애 서버로의 요청 방지
   - 자동 장애 복구 지원
   - 서비스 가용성 보장
