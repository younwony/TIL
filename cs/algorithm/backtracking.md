# 백트래킹 (Backtracking)

> `[3] 중급` · 선수 지식: [알고리즘이란](./what-is-algorithm.md), [시간 복잡도](./time-complexity.md)

> 해를 찾는 도중 해가 될 가능성이 없으면 되돌아가서 다른 경로를 탐색하는 기법

`#백트래킹` `#Backtracking` `#가지치기` `#Pruning` `#DFS` `#깊이우선탐색` `#상태공간트리` `#StateSpaceTree` `#N퀸` `#NQueen` `#부분집합` `#Subset` `#순열` `#Permutation` `#조합` `#Combination` `#스도쿠` `#Sudoku` `#제약충족` `#ConstraintSatisfaction`

## 왜 알아야 하는가?

백트래킹은 완전 탐색의 비효율을 개선하는 기법입니다. 불가능한 경로를 조기에 차단(가지치기)하여 탐색 공간을 줄입니다. N-Queen, 스도쿠, 순열/조합 등 코딩 테스트 단골 문제에 필수입니다.

## 핵심 개념

- **상태 공간 트리**: 모든 가능한 상태를 트리로 표현
- **유망 함수 (Promising)**: 현재 상태가 해로 이어질 가능성 판단
- **가지치기 (Pruning)**: 불가능한 경로를 조기에 차단
- **되돌아가기 (Backtrack)**: 막다른 길에서 이전 상태로 복귀

## 쉽게 이해하기

**백트래킹**을 미로 탈출에 비유할 수 있습니다.

- 갈림길에서 하나를 선택
- 막다른 길이면 되돌아가서 다른 길 선택
- 출구에 도달하거나 모든 길 탐색

**가지치기**: 벽으로 막힌 길은 들어가지도 않음 (불필요한 탐색 방지)

## 상세 설명

### 백트래킹 vs 완전 탐색

```
완전 탐색 (Brute Force):
모든 경로를 끝까지 탐색
        A
      / | \
     B  C  D
    /|  |  |\
   E F  G  H I
   모두 탐색 → 9개 노드

백트래킹:
가능성 없는 경로는 조기 차단
        A
      / | \
     B  C  D ← C에서 불가능 판단
    /|     |
   E F     H ← F에서 불가능 판단
   탐색 → 5개 노드 (가지치기)
```

### 백트래킹 템플릿

```java
void backtrack(상태) {
    if (정답 조건) {
        결과 저장;
        return;
    }

    for (선택지 : 가능한 선택지들) {
        if (유망하지 않음) continue;  // 가지치기

        선택;                         // 상태 변경
        backtrack(다음 상태);          // 재귀
        선택 취소;                     // 상태 복원 (백트래킹)
    }
}
```

### 예시 1: N-Queen 문제

```
N=4 체스판에 4개의 퀸을 서로 공격하지 않게 배치

    0 1 2 3
0 [ . Q . . ]
1 [ . . . Q ]
2 [ Q . . . ]
3 [ . . Q . ]

가지치기 조건:
- 같은 행 X
- 같은 열 X
- 같은 대각선 X
```

```java
int[] board = new int[N];  // board[row] = col

void nQueen(int row) {
    if (row == N) {
        count++;
        return;
    }

    for (int col = 0; col < N; col++) {
        if (isSafe(row, col)) {
            board[row] = col;
            nQueen(row + 1);
            // 자동 복원 (다음 반복에서 덮어씀)
        }
    }
}

boolean isSafe(int row, int col) {
    for (int i = 0; i < row; i++) {
        if (board[i] == col) return false;  // 같은 열
        if (Math.abs(board[i] - col) == row - i) return false;  // 대각선
    }
    return true;
}
```

### 예시 2: 부분집합 (Subset)

```
{1, 2, 3}의 모든 부분집합

          []
     /    |    \
   [1]   [2]   [3]
   /\     |
[1,2] [1,3] [2,3]
  |
[1,2,3]

결과: [], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]
```

```java
void subsets(int[] nums, int start, List<Integer> current) {
    result.add(new ArrayList<>(current));

    for (int i = start; i < nums.length; i++) {
        current.add(nums[i]);
        subsets(nums, i + 1, current);
        current.remove(current.size() - 1);  // 백트래킹
    }
}
```

### 예시 3: 순열 (Permutation)

```java
void permute(int[] nums, List<Integer> current, boolean[] used) {
    if (current.size() == nums.length) {
        result.add(new ArrayList<>(current));
        return;
    }

    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;  // 가지치기

        used[i] = true;
        current.add(nums[i]);
        permute(nums, current, used);
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

### 적용 문제들

| 문제 | 가지치기 조건 |
|------|-------------|
| N-Queen | 행, 열, 대각선 충돌 |
| 스도쿠 | 행, 열, 3x3 박스 중복 |
| 부분집합 합 | 현재 합이 목표 초과 |
| 미로 찾기 | 방문 여부, 벽 |
| 그래프 색칠 | 인접 노드 같은 색 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 완전 탐색보다 효율적 | 최악의 경우 완전 탐색과 동일 |
| 구현 비교적 단순 | 가지치기 조건 설계 필요 |
| 모든 해 탐색 가능 | 재귀 깊이 제한 |

## 면접 예상 질문

### Q: 백트래킹과 DFS의 차이는?

A: **DFS**는 그래프/트리의 모든 노드를 깊이 우선으로 방문하는 탐색 방법입니다. **백트래킹**은 DFS를 기반으로 하되, **가지치기**를 통해 불필요한 탐색을 조기 차단합니다. **핵심 차이**: 백트래킹은 "이 경로로 가면 해가 없다"고 판단되면 즉시 되돌아옵니다. DFS는 모든 경로를 끝까지 탐색합니다.

### Q: 가지치기 조건은 어떻게 설계하나요?

A: **문제의 제약 조건**에서 도출합니다. (1) 현재 상태가 이미 불가능한 경우 (N-Queen의 충돌) (2) 남은 선택으로 해를 만들 수 없는 경우 (부분집합 합에서 현재 합이 목표 초과) (3) 이미 방문한 상태 (순열에서 중복 사용). **팁**: 가능한 빨리 불가능을 판단할수록 효율적입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [분할 정복](./divide-and-conquer.md) | 알고리즘 설계 기법 | [3] 중급 |
| [그리디](./greedy.md) | 알고리즘 설계 기법 | [3] 중급 |

## 참고 자료

- Introduction to Algorithms (CLRS)
- [Backtracking - GeeksforGeeks](https://www.geeksforgeeks.org/backtracking-algorithms/)
