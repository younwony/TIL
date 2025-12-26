# 컬렉션 프레임워크 (Collection Framework)

> `[3] 중급` · 선수 지식: [자료구조란](../data-structure/what-is-data-structure.md), [OOP](../programming/oop.md)

> 데이터를 효율적으로 저장하고 처리하기 위한 표준화된 자료구조 라이브러리

`#컬렉션` `#Collection` `#프레임워크` `#Framework` `#List` `#ArrayList` `#LinkedList` `#Set` `#HashSet` `#TreeSet` `#Map` `#HashMap` `#TreeMap` `#LinkedHashMap` `#ConcurrentHashMap` `#Queue` `#Deque` `#PriorityQueue` `#Iterator` `#Comparable` `#Comparator` `#Stream` `#동시성컬렉션`

## 왜 알아야 하는가?

컬렉션 선택은 성능에 직접적인 영향을 줍니다. ArrayList vs LinkedList, HashMap vs TreeMap 등 상황에 맞는 선택이 중요합니다. 면접에서 각 구현체의 특성과 시간 복잡도를 자주 질문합니다.

## 핵심 개념

- **Collection**: 객체들의 그룹을 다루는 인터페이스
- **List**: 순서 있는 중복 허용 컬렉션
- **Set**: 중복 허용하지 않는 컬렉션
- **Map**: 키-값 쌍의 컬렉션

## 쉽게 이해하기

**컬렉션**을 정리함에 비유할 수 있습니다.

- **List**: 책꽂이 (순서대로, 같은 책 여러 권 가능)
- **Set**: 주민등록 (중복 불가)
- **Map**: 사전 (단어 → 뜻)
- **Queue**: 대기줄 (먼저 온 순서)

## 상세 설명

### 컬렉션 계층 구조

```
                    Iterable
                       │
                   Collection
                       │
        ┌──────────────┼──────────────┐
        │              │              │
       List           Set           Queue
        │              │              │
   ┌────┴────┐    ┌────┴────┐   ┌────┴────┐
ArrayList LinkedList HashSet TreeSet PriorityQueue Deque


                     Map
                      │
        ┌─────────────┼─────────────┐
    HashMap      TreeMap      LinkedHashMap
```

### List 구현체 비교

| 구현체 | 내부 구조 | 접근 | 삽입/삭제 | 특징 |
|--------|---------|------|----------|------|
| ArrayList | 동적 배열 | O(1) | O(n) | 조회 빈번 |
| LinkedList | 이중 연결 리스트 | O(n) | O(1)* | 삽입/삭제 빈번 |
| Vector | 동적 배열 | O(1) | O(n) | 동기화 (레거시) |

*노드 접근 후 삽입/삭제

```java
// ArrayList: 인덱스 접근 빠름
List<String> arrayList = new ArrayList<>();
arrayList.get(100);  // O(1)

// LinkedList: 양 끝 삽입/삭제 빠름
LinkedList<String> linkedList = new LinkedList<>();
linkedList.addFirst("first");  // O(1)
linkedList.addLast("last");    // O(1)
```

### Set 구현체 비교

| 구현체 | 내부 구조 | 삽입/검색 | 순서 | 특징 |
|--------|---------|----------|------|------|
| HashSet | 해시 테이블 | O(1) | 없음 | 가장 빠름 |
| LinkedHashSet | 해시 + 연결 리스트 | O(1) | 삽입 순서 | 순서 유지 |
| TreeSet | 레드-블랙 트리 | O(log n) | 정렬 순서 | 정렬 필요 |

```java
Set<Integer> hashSet = new HashSet<>();
// {3, 1, 2} → 순서 보장 안 됨

Set<Integer> linkedHashSet = new LinkedHashSet<>();
// {1, 2, 3} → 삽입 순서 유지

Set<Integer> treeSet = new TreeSet<>();
// {1, 2, 3} → 정렬된 순서
```

### Map 구현체 비교

| 구현체 | 내부 구조 | get/put | 순서 | 특징 |
|--------|---------|---------|------|------|
| HashMap | 해시 테이블 | O(1) | 없음 | 가장 빠름 |
| LinkedHashMap | 해시 + 연결 리스트 | O(1) | 삽입/접근 순서 | LRU 캐시 |
| TreeMap | 레드-블랙 트리 | O(log n) | 키 정렬 | 범위 검색 |
| ConcurrentHashMap | 분할 락 해시 | O(1) | 없음 | 스레드 안전 |

```java
Map<String, Integer> map = new HashMap<>();
map.put("apple", 100);
map.get("apple");  // O(1)

// Null 처리
HashMap: null 키/값 허용
TreeMap: null 키 불가, null 값 허용
ConcurrentHashMap: null 키/값 모두 불가
```

### HashMap 동작 원리

```
put("apple", 100):

1. hashCode("apple") = 12345
2. index = 12345 % 배열크기 = 5
3. 버킷[5]에 저장

버킷 (배열):
┌─────┬─────┬─────┬─────┬─────┬───────────────┬─────┐
│  0  │  1  │  2  │  3  │  4  │       5       │ ... │
└─────┴─────┴─────┴─────┴─────┴───────────────┴─────┘
                                      │
                          "apple" → 100
                                      │
                          "grape" → 200  (충돌 시 연결)

Java 8+: 버킷에 8개 이상 → 연결 리스트 → 레드-블랙 트리
```

### Queue & Deque

```java
// Queue (FIFO)
Queue<String> queue = new LinkedList<>();
queue.offer("first");  // 삽입
queue.poll();          // 제거 및 반환

// PriorityQueue (우선순위)
Queue<Integer> pq = new PriorityQueue<>();  // 최소 힙
pq.offer(3); pq.offer(1); pq.offer(2);
pq.poll();  // 1 (가장 작은 값)

// Deque (양방향)
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("first");
deque.addLast("last");
deque.pollFirst();
deque.pollLast();
```

### 동시성 컬렉션

| 컬렉션 | 동기화 방식 | 특징 |
|--------|-----------|------|
| Vector | 전체 메서드 동기화 | 느림 (레거시) |
| Collections.synchronizedList() | 래퍼 동기화 | 느림 |
| CopyOnWriteArrayList | 쓰기 시 복사 | 읽기 많을 때 |
| ConcurrentHashMap | 분할 락 | 범용 |

```java
// 스레드 안전한 Map
Map<String, Integer> map = new ConcurrentHashMap<>();

// 원자적 연산
map.computeIfAbsent("key", k -> expensiveCompute());
map.merge("key", 1, Integer::sum);  // 있으면 합산
```

### 선택 가이드

```
순서 필요?
├─ Yes → 중복 허용?
│        ├─ Yes → ArrayList (조회 多) / LinkedList (삽입 多)
│        └─ No  → LinkedHashSet
└─ No  → 중복 허용?
         ├─ Yes → (List 사용)
         └─ No  → 정렬 필요?
                  ├─ Yes → TreeSet
                  └─ No  → HashSet

키-값?
├─ Yes → 정렬 필요?
│        ├─ Yes → TreeMap
│        └─ No  → 순서 필요?
│                 ├─ Yes → LinkedHashMap
│                 └─ No  → HashMap / ConcurrentHashMap (동시성)
└─ No  → 위 참조
```

## 트레이드오프

| 항목 | 트레이드오프 |
|------|-------------|
| ArrayList vs LinkedList | 접근 속도 vs 삽입 속도 |
| HashSet vs TreeSet | 속도 vs 정렬 |
| HashMap vs TreeMap | 속도 vs 정렬/범위검색 |

## 면접 예상 질문

### Q: ArrayList와 LinkedList의 차이는?

A: **ArrayList**: 배열 기반, 인덱스 접근 O(1), 중간 삽입/삭제 O(n). **LinkedList**: 노드 기반, 인덱스 접근 O(n), 양 끝 삽입/삭제 O(1). **선택**: 읽기 많으면 ArrayList, 삽입/삭제 많으면 LinkedList. **실무**: 대부분 ArrayList가 유리 (캐시 지역성).

### Q: HashMap의 시간 복잡도가 O(1)인 이유는?

A: 해시 함수로 키를 인덱스로 변환하여 배열에 직접 접근하기 때문입니다. **단, 충돌** 시 O(n)이 될 수 있습니다. Java 8+에서는 버킷에 8개 이상 충돌 시 레드-블랙 트리로 변환하여 O(log n)으로 개선합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [자료구조란](../data-structure/what-is-data-structure.md) | 기반 지식 | [1] 정의 |
| [해시 테이블](../data-structure/hash-table.md) | HashMap 원리 | [3] 중급 |

## 참고 자료

- [Java Collection Framework - Oracle](https://docs.oracle.com/javase/tutorial/collections/)
- Effective Java - Joshua Bloch
