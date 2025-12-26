# 세그먼트 트리 (Segment Tree)

> `[4] 심화` · 선수 지식: [트리](./tree.md), [알고리즘이란](../algorithm/what-is-algorithm.md)

> 구간 쿼리와 업데이트를 효율적으로 처리하는 트리 기반 자료구조

`#세그먼트트리` `#SegmentTree` `#구간트리` `#RangeQuery` `#구간쿼리` `#구간합` `#RangeSum` `#구간최소` `#RangeMin` `#구간최대` `#RangeMax` `#LazyPropagation` `#느린갱신` `#펜윅트리` `#FenwickTree` `#BIT` `#BinaryIndexedTree` `#알고리즘대회` `#경쟁프로그래밍` `#CompetitiveProgramming`

## 왜 알아야 하는가?

세그먼트 트리는 구간에 대한 질의(합, 최소, 최대 등)와 업데이트를 O(log n)에 처리합니다. 알고리즘 대회와 코딩 테스트에서 구간 쿼리 문제의 핵심 자료구조입니다.

## 핵심 개념

- **구간 쿼리**: 특정 범위의 합/최소/최대 등을 빠르게 계산
- **포인트 업데이트**: 특정 인덱스 값 변경
- **구간 업데이트**: 특정 범위 값 일괄 변경 (Lazy Propagation)
- **완전 이진 트리**: 배열로 효율적 구현 가능

## 쉽게 이해하기

**세그먼트 트리**를 조직 구조에 비유할 수 있습니다.

팀원 10명의 성과를 관리한다면:
- **잎 노드**: 각 팀원의 성과
- **중간 노드**: 소그룹의 성과 합
- **루트**: 전체 팀 성과 합

"3~7번 팀원 성과 합"을 구하려면 전체를 다 더하지 않고, 미리 계산된 그룹 합을 활용합니다.

## 상세 설명

### 세그먼트 트리 구조

배열 `[1, 3, 5, 7, 9, 11]`의 구간 합 세그먼트 트리:

```
                [0-5: 36]
              /          \
        [0-2: 9]        [3-5: 27]
        /      \        /       \
    [0-1: 4]  [2: 5]  [3-4: 16] [5: 11]
    /    \            /     \
 [0: 1] [1: 3]    [3: 7]  [4: 9]

각 노드는 해당 구간의 합을 저장
```

### 기본 구현

```java
class SegmentTree {
    int[] tree;
    int n;

    SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];  // 충분한 크기 할당
        build(arr, 1, 0, n - 1);
    }

    // 트리 구축: O(n)
    void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, 2 * node, start, mid);
        build(arr, 2 * node + 1, mid + 1, end);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // 구간 합 쿼리: O(log n)
    int query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            return 0;  // 범위 밖
        }
        if (left <= start && end <= right) {
            return tree[node];  // 완전히 포함
        }
        int mid = (start + end) / 2;
        return query(2 * node, start, mid, left, right)
             + query(2 * node + 1, mid + 1, end, left, right);
    }

    // 포인트 업데이트: O(log n)
    void update(int node, int start, int end, int idx, int val) {
        if (start == end) {
            tree[node] = val;
            return;
        }
        int mid = (start + end) / 2;
        if (idx <= mid) {
            update(2 * node, start, mid, idx, val);
        } else {
            update(2 * node + 1, mid + 1, end, idx, val);
        }
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }
}
```

### 시간 복잡도 비교

| 연산 | 일반 배열 | 세그먼트 트리 |
|------|----------|--------------|
| 구축 | - | O(n) |
| 구간 쿼리 | O(n) | O(log n) |
| 포인트 업데이트 | O(1) | O(log n) |
| 구간 업데이트 | O(n) | O(log n)* |

*Lazy Propagation 사용 시

### Lazy Propagation (느린 갱신)

구간 업데이트를 효율적으로 처리:

```java
class LazySegmentTree {
    int[] tree, lazy;
    int n;

    // 구간에 값 더하기
    void updateRange(int node, int start, int end, int left, int right, int val) {
        propagate(node, start, end);  // 지연된 업데이트 적용

        if (right < start || end < left) return;

        if (left <= start && end <= right) {
            lazy[node] += val;
            propagate(node, start, end);
            return;
        }

        int mid = (start + end) / 2;
        updateRange(2 * node, start, mid, left, right, val);
        updateRange(2 * node + 1, mid + 1, end, left, right, val);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    void propagate(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += lazy[node] * (end - start + 1);
            if (start != end) {
                lazy[2 * node] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
            }
            lazy[node] = 0;
        }
    }
}
```

### 다양한 연산

| 연산 | 결합 함수 | 항등원 |
|------|----------|-------|
| 구간 합 | a + b | 0 |
| 구간 최소 | min(a, b) | ∞ |
| 구간 최대 | max(a, b) | -∞ |
| 구간 GCD | gcd(a, b) | 0 |
| 구간 XOR | a ^ b | 0 |

### 세그먼트 트리 vs 펜윅 트리

| 항목 | 세그먼트 트리 | 펜윅 트리 (BIT) |
|------|-------------|----------------|
| 공간 | O(4n) | O(n) |
| 구현 | 복잡 | 간단 |
| 연산 | 모든 결합 연산 | 가역 연산만 (합, XOR) |
| 속도 | 약간 느림 | 빠름 |
| 구간 업데이트 | Lazy로 가능 | 어려움 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 구간 쿼리 O(log n) | 메모리 4n 필요 |
| 다양한 연산 지원 | 구현 복잡 |
| 구간 업데이트 가능 | 정적 배열에선 과도함 |

## 면접 예상 질문

### Q: 세그먼트 트리를 언제 사용하나요?

A: **구간 쿼리**와 **업데이트**가 빈번할 때 사용합니다. (1) 구간 합/최소/최대 쿼리 (2) 배열 값이 동적으로 변경 (3) 여러 번의 쿼리와 업데이트가 섞여 있을 때. **주의**: 업데이트 없이 쿼리만 있으면 누적 합 배열이 더 효율적입니다.

### Q: Lazy Propagation이 필요한 이유는?

A: **구간 업데이트**를 O(log n)에 처리하기 위해서입니다. 일반 세그먼트 트리에서 구간 업데이트는 O(n)이지만, Lazy Propagation으로 업데이트를 "지연"시켜 필요할 때만 적용하면 O(log n)이 됩니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [트리](./tree.md) | 선수 지식 | [3] 중급 |
| [분할 정복](../algorithm/divide-and-conquer.md) | 구현 원리 | [3] 중급 |

## 참고 자료

- [CP-Algorithms - Segment Tree](https://cp-algorithms.com/data_structures/segment_tree.html)
- Introduction to Algorithms (CLRS)
