# Map 인터페이스

## 핵심 정리

- Map은 키-값 쌍으로 데이터를 저장하는 자료구조
- 키는 중복 불가, 값은 중복 허용
- 주요 구현체: HashMap, LinkedHashMap, TreeMap, Hashtable, ConcurrentHashMap

## HashMap

해시 테이블 기반의 Map 구현체

### 특징

- 내부적으로 배열 + 연결 리스트(또는 트리) 사용
- null 키와 null 값 모두 허용
- 순서를 보장하지 않음
- 스레드 안전하지 않음

### 내부 동작 원리

```java
// 해시 버킷 구조
transient Node<K,V>[] table;

static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}

// 키의 해시값으로 버킷 인덱스 결정
int index = (n - 1) & hash(key);
```

### 해시 충돌 해결

1. **Separate Chaining**: 같은 버킷에 연결 리스트로 저장
2. **Tree 변환**: Java 8+에서 버킷의 노드가 8개 이상이면 Red-Black Tree로 변환

```java
// 트리 변환 임계값
static final int TREEIFY_THRESHOLD = 8;
// 리스트 변환 임계값
static final int UNTREEIFY_THRESHOLD = 6;
```

### 시간 복잡도

| 연산 | 평균 | 최악 (충돌 시) |
|------|------|----------------|
| get | O(1) | O(log n) |
| put | O(1) | O(log n) |
| remove | O(1) | O(log n) |
| containsKey | O(1) | O(log n) |

## LinkedHashMap

삽입 순서를 유지하는 HashMap

### 특징

- 이중 연결 리스트로 순서 관리
- 삽입 순서 또는 접근 순서 유지 가능
- LRU 캐시 구현에 활용 가능

```java
// 접근 순서 유지 (accessOrder = true)
Map<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);

// LRU 캐시 구현
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
```

## TreeMap

정렬된 순서를 유지하는 Map

### 특징

- Red-Black Tree 기반
- 키가 자동으로 정렬됨
- null 키 허용하지 않음
- 범위 검색 가능

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(3, "C");
map.put(1, "A");
map.put(2, "B");

map.firstKey();           // 1
map.lastKey();            // 3
map.headMap(2);           // {1=A}
map.tailMap(2);           // {2=B, 3=C}
map.subMap(1, 3);         // {1=A, 2=B}
map.floorKey(2);          // 2 (이하 중 최대)
map.ceilingKey(2);        // 2 (이상 중 최소)
```

## Hashtable

동기화된 해시 테이블

### 특징

- 모든 메서드가 synchronized
- null 키, null 값 모두 허용하지 않음
- 레거시 클래스 (Java 1.0부터 존재)

```java
// 사용 비권장 - 성능 이슈
Map<String, Integer> hashtable = new Hashtable<>();

// 대안: ConcurrentHashMap
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

## ConcurrentHashMap

스레드 안전한 고성능 Map

### 특징

- 세그먼트 단위 잠금 (Java 7)
- 노드 단위 잠금 (Java 8+)
- null 키, null 값 허용하지 않음

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 원자적 연산
map.putIfAbsent("key", 1);
map.computeIfAbsent("key", k -> expensiveComputation(k));
map.merge("key", 1, Integer::sum);
```

## Map 구현체 비교

| 특성 | HashMap | LinkedHashMap | TreeMap | Hashtable | ConcurrentHashMap |
|------|---------|---------------|---------|-----------|-------------------|
| 순서 | 없음 | 삽입/접근 순서 | 정렬 순서 | 없음 | 없음 |
| null 키 | O | O | X | X | X |
| null 값 | O | O | O | X | X |
| 스레드 안전 | X | X | X | O | O |
| get/put | O(1) | O(1) | O(log n) | O(1) | O(1) |

## HashMap 주요 메서드

```java
Map<String, Integer> map = new HashMap<>();

// 기본 연산
map.put("a", 1);
map.get("a");                    // 1
map.getOrDefault("b", 0);        // 0
map.containsKey("a");            // true
map.containsValue(1);            // true

// Java 8+ 메서드
map.putIfAbsent("a", 2);         // 키가 없을 때만 삽입
map.computeIfAbsent("b", k -> 2); // 키가 없을 때 계산하여 삽입
map.computeIfPresent("a", (k, v) -> v + 1); // 키가 있을 때 계산
map.merge("a", 1, Integer::sum); // 있으면 합치고, 없으면 삽입

// 순회
map.forEach((k, v) -> System.out.println(k + ": " + v));
map.entrySet().stream()
   .filter(e -> e.getValue() > 0)
   .forEach(e -> System.out.println(e));
```

## 면접 예상 질문

1. **HashMap의 동작 원리를 설명해주세요.**
   - 키의 hashCode()로 버킷 인덱스 결정
   - 충돌 시 Separate Chaining으로 해결
   - Java 8+에서 8개 이상이면 Red-Black Tree로 변환

2. **HashMap과 Hashtable의 차이점은?**
   - HashMap은 비동기, Hashtable은 동기화
   - HashMap은 null 허용, Hashtable은 불허
   - 멀티스레드에서는 ConcurrentHashMap 권장

3. **HashMap의 시간 복잡도가 O(1)인 이유는?**
   - 해시 함수로 직접 버킷 위치 계산
   - 충돌이 적으면 상수 시간에 접근 가능
   - 최악의 경우(모든 키가 같은 버킷) O(n)이지만, 트리 변환으로 O(log n) 보장
