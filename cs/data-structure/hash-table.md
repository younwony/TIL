# 해시 테이블 (Hash Table)

> `[3] 중급` · 선수 지식: [배열 (Array)](./array.md), [연결 리스트 (Linked List)](./linked-list.md)

> 키(Key)를 해시 함수로 변환하여 값(Value)을 저장하고 조회하는 자료구조

## 왜 알아야 하는가?

- **실무**: 빠른 검색이 필요한 대부분의 상황에서 사용됩니다. 데이터베이스 인덱싱, 캐싱, 중복 검사, 빈도 계산 등 실무에서 가장 많이 사용되는 자료구조 중 하나입니다.
- **면접**: 해시 충돌 해결, 해시 함수 설계, 시간 복잡도 분석은 자주 출제되는 면접 주제입니다. "Two Sum" 같은 문제에서 O(1) 조회를 활용하는 능력을 평가합니다.
- **기반 지식**: HashMap, HashSet 등 표준 라이브러리의 내부 동작 원리를 이해하는 데 필수적이며, 성능 최적화의 핵심 개념입니다.

## 핵심 개념

- **키-값 쌍 (Key-Value Pair)**: 키를 통해 값을 저장하고 검색
- **해시 함수 (Hash Function)**: 키를 배열 인덱스로 변환하는 함수
- **O(1) 평균 시간**: 삽입, 삭제, 조회 모두 평균 O(1)
- **충돌 (Collision)**: 서로 다른 키가 같은 인덱스로 해싱되는 현상
- **충돌 해결**: 체이닝(Chaining), 개방 주소법(Open Addressing)

## 쉽게 이해하기

**해시 테이블**을 사물함 시스템에 비유할 수 있습니다.

학교나 헬스장의 사물함을 생각해보세요. 사물함 번호를 정하는 규칙이 있습니다.

예를 들어, 이름의 첫 글자로 번호를 정한다면:
- "김철수" → 'ㄱ' → 1번 사물함
- "이영희" → 'ㄹ' → 5번 사물함
- "박민수" → 'ㅂ' → 7번 사물함

장점:
- 이름만 알면 사물함 번호를 즉시 계산 (해시 함수)
- 사물함 번호로 바로 접근 → O(1)

문제:
- "김철수"와 "김영수" 모두 1번으로 배정됨 (충돌!)
- 해결: 1번에 리스트로 여러 명 저장 (체이닝)
- 또는 1번이 차있으면 2번, 3번 순서로 찾기 (개방 주소법)

이렇게 빠른 검색을 위해 키를 인덱스로 변환하는 것이 해시 테이블입니다.

## 상세 설명

### 해시 테이블 구조

```
Key → Hash Function → Index → Value

예: "apple" → hash("apple") → 3 → "사과"
```

**기본 구성**:
```java
class HashTable {
    private Entry[] table;  // 배열
    private int capacity;   // 크기

    class Entry {
        String key;
        String value;
        Entry next;  // 체이닝용

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

### 해시 함수 (Hash Function)

**정의**: 임의 크기의 데이터를 고정 크기의 값(해시값)으로 변환하는 함수

**좋은 해시 함수의 조건**:

1. **결정적 (Deterministic)**: 같은 입력은 항상 같은 출력
2. **균등 분포 (Uniform Distribution)**: 모든 인덱스에 골고루 분산
3. **빠른 계산**: O(1) 시간에 계산 가능
4. **눈사태 효과 (Avalanche Effect)**: 입력의 작은 변화가 출력에 큰 변화

**왜 균등 분포가 중요한가?**

불균등하면 특정 인덱스에 데이터가 몰려 충돌이 많아지고, 성능이 O(N)으로 퇴화됩니다.

#### 해시 함수 예제

**1. Division Method (나눗셈법)**

```java
int hash(String key) {
    int hashCode = key.hashCode();  // Java의 hashCode()
    return Math.abs(hashCode) % capacity;
}
```

**장점**: 간단하고 빠름
**단점**: capacity가 2의 거듭제곱이면 하위 비트만 사용되어 불균등

**개선**: capacity를 소수(prime number)로 설정

**2. Multiplication Method (곱셈법)**

```java
int hash(String key) {
    int hashCode = key.hashCode();
    double A = (Math.sqrt(5) - 1) / 2;  // 황금비
    double frac = (hashCode * A) % 1;    // 소수 부분
    return (int) (capacity * frac);
}
```

**3. Java String hashCode()**

```java
// "abc" → 'a' * 31^2 + 'b' * 31^1 + 'c' * 31^0
public int hashCode() {
    int hash = 0;
    for (int i = 0; i < length(); i++) {
        hash = 31 * hash + charAt(i);
    }
    return hash;
}
```

**왜 31을 사용하나?**

- 홀수 소수: 균등 분포
- 31 = 32 - 1: `31 * i = (i << 5) - i`로 최적화 가능 (비트 연산)
- 경험적으로 충돌이 적음

### 충돌 해결 (Collision Resolution)

**충돌**: 서로 다른 키가 같은 인덱스로 해싱

```
hash("apple") = 3
hash("banana") = 3  // 충돌!
```

#### 1. 체이닝 (Chaining, Separate Chaining)

**방법**: 각 인덱스에 연결 리스트로 여러 엔트리 저장

```
Index 0: []
Index 1: [(key1, val1)]
Index 2: []
Index 3: [(apple, 사과) → (banana, 바나나)]
Index 4: [(key4, val4)]
```

**구현**:
```java
class HashTableChaining {
    private LinkedList<Entry>[] table;
    private int capacity;
    private int size;

    class Entry {
        String key;
        String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashTableChaining(int capacity) {
        this.capacity = capacity;
        this.table = new LinkedList[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // O(1) 평균, O(N) 최악 (모든 키가 같은 인덱스)
    public void put(String key, String value) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        // 기존 키 업데이트
        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // 새 엔트리 추가
        bucket.add(new Entry(key, value));
        size++;

        // 로드 팩터 확인 후 리사이징
        if ((double) size / capacity > 0.75) {
            resize();
        }
    }

    // O(1) 평균, O(N) 최악
    public String get(String key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;  // 없음
    }

    // O(1) 평균, O(N) 최악
    public void remove(String key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        Iterator<Entry> it = bucket.iterator();
        while (it.hasNext()) {
            Entry entry = it.next();
            if (entry.key.equals(key)) {
                it.remove();
                size--;
                return;
            }
        }
    }

    // O(N) - 모든 엔트리 재해싱
    private void resize() {
        LinkedList<Entry>[] oldTable = table;
        capacity *= 2;
        table = new LinkedList[capacity];
        size = 0;

        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }

        // 모든 엔트리 재삽입
        for (LinkedList<Entry> bucket : oldTable) {
            for (Entry entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
}
```

**장점**:
- 구현이 간단
- 삭제가 쉬움
- 로드 팩터가 1 이상이어도 동작 (체인만 길어짐)

**단점**:
- 추가 포인터 메모리 필요
- 캐시 성능 낮음 (메모리 분산)
- 최악의 경우 O(N) (모든 키가 한 인덱스)

**로드 팩터 (Load Factor)**:
```
α = N / M
N: 저장된 엔트리 수
M: 테이블 크기
```

**왜 로드 팩터가 중요한가?**

α가 높을수록 충돌이 많아지고 체인이 길어져 성능 저하됩니다. 보통 α > 0.75이면 리사이징합니다.

#### 2. 개방 주소법 (Open Addressing)

**방법**: 충돌 시 빈 슬롯을 찾아 저장 (배열만 사용, 포인터 X)

**탐사 (Probing)**: 빈 슬롯을 찾는 방법

##### a. 선형 탐사 (Linear Probing)

충돌 시 다음 슬롯을 순차적으로 확인: `(h(k) + i) % M`

```
hash("apple") = 3 → table[3] 확인
table[3] 차있음 → table[4] 확인
table[4] 차있음 → table[5] 확인
table[5] 비어있음 → 저장
```

**구현**:
```java
class HashTableLinearProbing {
    private Entry[] table;
    private int capacity;
    private int size;
    private static final Entry DELETED = new Entry(null, null);

    class Entry {
        String key;
        String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // O(1) 평균, O(N) 최악 (클러스터링)
    public void put(String key, String value) {
        int index = hash(key);

        while (table[index] != null && table[index] != DELETED) {
            if (table[index].key.equals(key)) {
                table[index].value = value;  // 업데이트
                return;
            }
            index = (index + 1) % capacity;  // 다음 슬롯
        }

        table[index] = new Entry(key, value);
        size++;

        if ((double) size / capacity > 0.5) {
            resize();
        }
    }

    // O(1) 평균, O(N) 최악
    public String get(String key) {
        int index = hash(key);

        while (table[index] != null) {
            if (table[index] != DELETED && table[index].key.equals(key)) {
                return table[index].value;
            }
            index = (index + 1) % capacity;
        }

        return null;  // 없음
    }

    // O(1) 평균, O(N) 최악
    public void remove(String key) {
        int index = hash(key);

        while (table[index] != null) {
            if (table[index] != DELETED && table[index].key.equals(key)) {
                table[index] = DELETED;  // Lazy Deletion
                size--;
                return;
            }
            index = (index + 1) % capacity;
        }
    }
}
```

**왜 DELETED 마커를 사용하나?**

NULL로 바꾸면 탐사 체인이 끊겨서, 그 뒤에 있는 엔트리를 찾지 못합니다.

```
table[3] = apple
table[4] = banana  (hash도 3, 충돌로 4에 저장)
table[5] = cherry  (hash도 3, 충돌로 5에 저장)

apple 삭제 → table[3] = NULL
banana 검색 → table[3]이 NULL → "없음" (잘못!)
```

**장점**:
- 포인터 없이 배열만 사용 → 메모리 효율
- 캐시 친화적 (연속 메모리)

**단점**:
- **1차 클러스터링 (Primary Clustering)**: 연속된 슬롯이 차서 성능 저하
- 삭제 복잡 (Lazy Deletion 필요)
- 로드 팩터 0.5 이하 유지 필요

**1차 클러스터링이란?**

충돌 시 연속된 슬롯에 저장하므로, 긴 체인이 형성되어 탐사 시간이 증가합니다.

```
[A][][][][][][]  → 충돌 시 다음 슬롯
[A][B][][][][][]  → 또 충돌 시 다음 슬롯
[A][B][C][][][][]  → 클러스터 형성
```

##### b. 제곱 탐사 (Quadratic Probing)

충돌 시 제곱수만큼 이동: `(h(k) + i²) % M`

```
hash("apple") = 3
table[3] 차있음 → (3 + 1²) % M = table[4]
table[4] 차있음 → (3 + 2²) % M = table[7]
table[7] 비어있음 → 저장
```

**장점**: 1차 클러스터링 완화
**단점**: 2차 클러스터링 (같은 해시값은 같은 탐사 순서)

##### c. 이중 해싱 (Double Hashing)

두 번째 해시 함수 사용: `(h1(k) + i * h2(k)) % M`

```java
int hash1(String key) {
    return Math.abs(key.hashCode()) % capacity;
}

int hash2(String key) {
    // h2(k) ≠ 0, h2(k)와 M이 서로소
    return 7 - (Math.abs(key.hashCode()) % 7);
}

int probe(String key, int i) {
    return (hash1(key) + i * hash2(key)) % capacity;
}
```

**장점**: 클러스터링 최소화, 균등 분포
**단점**: 두 번째 해시 함수 계산 비용

### 리사이징 (Resizing)

**언제?** 로드 팩터가 임계값 초과 시 (보통 0.75)

**방법**:
1. 새 배열 생성 (보통 2배 크기)
2. 모든 엔트리 재해싱하여 삽입
3. 기존 배열 삭제

**시간 복잡도**: O(N) - 모든 엔트리 재삽입
**Amortized O(1)**: 리사이징이 드물게 발생하므로 평균적으로 O(1)

**왜 리사이징이 필요한가?**

로드 팩터가 높으면 충돌이 많아져 성능이 저하됩니다. 리사이징으로 공간을 늘려 충돌을 줄입니다.

## 시간 복잡도

| 연산 | 평균 | 최악 |
|------|------|------|
| 삽입 (Insert) | O(1) | O(N) |
| 삭제 (Delete) | O(1) | O(N) |
| 조회 (Search) | O(1) | O(N) |
| 리사이징 | O(N) | O(N) |

**최악의 경우**: 모든 키가 같은 인덱스로 해싱 (해시 함수 불량, 높은 로드 팩터)

**평균 O(1)인 이유**:
- 좋은 해시 함수 → 균등 분포
- 적절한 로드 팩터 → 충돌 최소화
- 리사이징 → 성능 유지

## 트레이드오프

### 체이닝 vs 개방 주소법

| 기준 | 체이닝 | 개방 주소법 |
|------|--------|------------|
| 메모리 | 포인터 추가 공간 | 배열만 사용 (효율적) |
| 캐시 성능 | 나쁨 (분산) | 좋음 (연속) |
| 로드 팩터 | > 1 가능 | < 1 필수 (보통 0.5) |
| 삭제 | 간단 | 복잡 (Lazy Deletion) |
| 클러스터링 | 없음 | 있음 (선형/제곱) |
| 구현 | 간단 | 복잡 |
| 사용 | Java HashMap | Python dict (< 3.6) |

### 해시 테이블 vs 다른 자료구조

| 기준 | 해시 테이블 | BST | 배열 (정렬) |
|------|-----------|-----|-----------|
| 조회 | O(1) 평균 | O(log N) | O(log N) - 이진 탐색 |
| 삽입 | O(1) 평균 | O(log N) | O(N) - 요소 이동 |
| 삭제 | O(1) 평균 | O(log N) | O(N) - 요소 이동 |
| 순서 | 없음 | 있음 (중위 순회) | 있음 |
| 범위 검색 | O(N) | O(log N + K) | O(log N + K) |
| 메모리 | 많음 (빈 공간) | 포인터 | 적음 |

**언제 해시 테이블을 사용하나?**

- 빠른 조회/삽입/삭제가 필요할 때
- 순서가 중요하지 않을 때
- 범위 검색이 필요 없을 때

**언제 BST를 사용하나?**

- 정렬된 순서가 필요할 때
- 범위 검색이 필요할 때
- 최악의 경우도 O(log N) 보장 필요 시

## 면접 예상 질문

- Q: 해시 테이블의 평균 시간 복잡도가 O(1)인 이유는?
  - A: 해시 함수로 키를 인덱스로 직접 변환하므로 배열 접근처럼 O(1)입니다. 단, 좋은 해시 함수와 적절한 로드 팩터가 전제됩니다. 해시 함수가 균등 분포를 만들고 로드 팩터가 낮으면 충돌이 적어 대부분 한 번에 접근 가능합니다. 최악의 경우(모든 키가 충돌)는 O(N)이지만 실제로는 거의 발생하지 않습니다.

- Q: 해시 충돌을 해결하는 방법은?
  - A: 체이닝과 개방 주소법 두 가지가 있습니다. 체이닝은 각 인덱스에 연결 리스트로 여러 엔트리를 저장합니다. 구현이 간단하고 로드 팩터 1 이상도 가능하지만 포인터 메모리와 캐시 성능 저하가 단점입니다. 개방 주소법은 충돌 시 빈 슬롯을 찾아 저장합니다. 메모리 효율적이고 캐시 친화적이지만 클러스터링과 복잡한 삭제가 단점입니다. Java HashMap은 체이닝을 사용합니다.

- Q: 좋은 해시 함수의 조건은?
  - A: 1) 결정적: 같은 입력은 항상 같은 출력. 2) 균등 분포: 모든 인덱스에 골고루 분산. 3) 빠른 계산: O(1) 시간. 4) 눈사태 효과: 입력의 작은 변화가 출력에 큰 변화. 균등 분포가 가장 중요한데, 불균등하면 특정 인덱스에 데이터가 몰려 충돌이 많아지고 성능이 O(N)으로 퇴화되기 때문입니다.

- Q: 로드 팩터는 무엇이고 왜 중요한가요?
  - A: 로드 팩터(α)는 저장된 엔트리 수 / 테이블 크기입니다. α가 높을수록 충돌이 많아져 성능이 저하됩니다. 보통 α > 0.75 (체이닝) 또는 α > 0.5 (개방 주소법)이면 리사이징합니다. 리사이징은 테이블 크기를 늘려 충돌을 줄입니다. 하지만 O(N) 비용이므로 자주 발생하지 않도록 임계값을 적절히 설정해야 합니다.

- Q: 해시 테이블에서 삭제 연산이 복잡한 이유는?
  - A: 개방 주소법에서는 단순히 NULL로 바꾸면 탐사 체인이 끊겨 뒤의 엔트리를 찾지 못합니다. 따라서 DELETED 마커를 사용하는 Lazy Deletion이 필요합니다. 체이닝에서는 연결 리스트에서 제거만 하면 되므로 간단합니다. 이것이 체이닝이 삭제에 유리한 이유입니다.

- Q: 해시 테이블과 BST를 언제 각각 사용하나요?
  - A: 해시 테이블은 빠른 조회/삽입/삭제(평균 O(1))가 필요하고 순서가 중요하지 않을 때 사용합니다. BST는 정렬된 순서가 필요하거나 범위 검색이 필요할 때 사용합니다. 예를 들어 "특정 키 조회"는 해시 테이블, "10~20 사이 값 검색"은 BST가 적합합니다. 또한 BST는 최악의 경우도 O(log N) 보장(균형 트리)하므로 안정적입니다.

- Q: Java HashMap과 Hashtable의 차이는?
  - A: HashMap은 동기화되지 않아 멀티스레드에 안전하지 않지만 빠릅니다. null 키/값 허용. Hashtable은 동기화되어 멀티스레드 안전하지만 느립니다. null 불허. 현재는 Hashtable 대신 ConcurrentHashMap을 사용합니다. HashMap은 Java 1.2부터 체이닝 + 트리(Java 8+)로 구현되어, 체인이 길어지면 Red-Black Tree로 변환하여 O(log N)을 보장합니다.

## 연관 문서

- [배열 (Array)](./array.md) - 해시 테이블의 내부 저장소
- [연결 리스트 (Linked List)](./linked-list.md) - 체이닝 방식의 충돌 해결에 사용
- [트리 (Tree)](./tree.md) - Java 8+ HashMap에서 긴 체인을 Red-Black Tree로 변환
- [그래프 (Graph)](./graph.md) - 인접 리스트 구현에 HashMap 활용

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 11
- [Wikipedia: Hash Table](https://en.wikipedia.org/wiki/Hash_table)
- [Java HashMap 소스 코드](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/HashMap.java)
- [Visualgo: Hash Table](https://visualgo.net/en/hashtable)
