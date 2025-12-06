# 캐싱 알고리즘

## 핵심 정리

- 캐시는 자주 사용되는 데이터를 빠르게 접근할 수 있는 임시 저장소
- 캐시 교체 알고리즘은 캐시가 가득 찼을 때 어떤 데이터를 제거할지 결정
- 캐시 적중률(Hit Rate)을 높이는 것이 캐싱의 목표

## LRU Cache (Least Recently Used)

가장 오래 사용되지 않은 항목을 먼저 제거하는 알고리즘

### 동작 원리

1. 캐시에 데이터가 있으면 해당 데이터를 가장 최근 사용으로 갱신
2. 캐시에 데이터가 없으면 새로 추가
3. 캐시가 가득 찼을 때 가장 오래 사용되지 않은 데이터 제거

### 구현

```java
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> cache;
    private final DoublyLinkedList<K, V> list;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.list = new DoublyLinkedList<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        Node<K, V> node = cache.get(key);
        list.moveToHead(node);
        return node.value;
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            node.value = value;
            list.moveToHead(node);
        } else {
            if (cache.size() >= capacity) {
                Node<K, V> tail = list.removeTail();
                cache.remove(tail.key);
            }
            Node<K, V> newNode = new Node<>(key, value);
            list.addToHead(newNode);
            cache.put(key, newNode);
        }
    }
}
```

### Java의 LinkedHashMap 활용

```java
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

### 시간 복잡도

| 연산 | 복잡도 |
|------|--------|
| get | O(1) |
| put | O(1) |

## LFU Cache (Least Frequently Used)

가장 적게 사용된 항목을 먼저 제거하는 알고리즘

### 동작 원리

1. 각 데이터의 사용 횟수(frequency)를 추적
2. 캐시가 가득 찼을 때 사용 빈도가 가장 낮은 데이터 제거
3. 빈도가 같을 경우 가장 오래된 데이터 제거 (LRU 방식)

### 구현

```java
public class LFUCache<K, V> {
    private final int capacity;
    private int minFreq;
    private final Map<K, V> cache;
    private final Map<K, Integer> keyToFreq;
    private final Map<Integer, LinkedHashSet<K>> freqToKeys;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.keyToFreq = new HashMap<>();
        this.freqToKeys = new HashMap<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        increaseFreq(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (capacity == 0) return;

        if (cache.containsKey(key)) {
            cache.put(key, value);
            increaseFreq(key);
            return;
        }

        if (cache.size() >= capacity) {
            removeMinFreqKey();
        }

        cache.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    private void increaseFreq(K key) {
        int freq = keyToFreq.get(key);
        keyToFreq.put(key, freq + 1);
        freqToKeys.get(freq).remove(key);

        if (freqToKeys.get(freq).isEmpty()) {
            freqToKeys.remove(freq);
            if (minFreq == freq) minFreq++;
        }

        freqToKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    private void removeMinFreqKey() {
        LinkedHashSet<K> keys = freqToKeys.get(minFreq);
        K keyToRemove = keys.iterator().next();
        keys.remove(keyToRemove);

        if (keys.isEmpty()) {
            freqToKeys.remove(minFreq);
        }

        cache.remove(keyToRemove);
        keyToFreq.remove(keyToRemove);
    }
}
```

### 시간 복잡도

| 연산 | 복잡도 |
|------|--------|
| get | O(1) |
| put | O(1) |

## LRU vs LFU 비교

| 특성 | LRU | LFU |
|------|-----|-----|
| 기준 | 최근 사용 시간 | 사용 빈도 |
| 장점 | 구현이 간단, 지역성 활용 | 인기 있는 항목 유지 |
| 단점 | 빈번히 사용되는 항목도 제거될 수 있음 | 구현 복잡, 오래된 인기 항목 문제 |
| 사용 사례 | 일반적인 캐시 | 컨텐츠 캐싱 |

## 면접 예상 질문

1. **LRU 캐시를 O(1)에 구현하려면 어떤 자료구조를 사용해야 하나요?**
   - HashMap + Doubly Linked List 조합 사용
   - HashMap으로 O(1) 접근, LinkedList로 O(1) 순서 변경

2. **LRU와 LFU 중 어떤 상황에서 어떤 알고리즘이 적합한가요?**
   - LRU: 최근 접근 패턴이 중요한 경우 (시간적 지역성)
   - LFU: 특정 데이터가 지속적으로 인기 있는 경우

3. **캐시 적중률을 높이기 위한 전략은?**
   - 적절한 캐시 크기 설정
   - 워크로드 특성에 맞는 교체 알고리즘 선택
   - TTL(Time To Live) 설정으로 오래된 데이터 제거
