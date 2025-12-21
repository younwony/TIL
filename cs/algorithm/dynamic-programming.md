# 동적 프로그래밍 (Dynamic Programming)

> `[4] 심화` · 선수 지식: [시간 복잡도](./time-complexity.md), [재귀](../data-structure/recursion.md)

> 복잡한 문제를 작은 하위 문제로 나누어 해결하고, 그 결과를 저장하여 중복 계산을 피하는 알고리즘 설계 기법

## 왜 알아야 하는가?

- **실무**: 추천 시스템, 자원 최적화, 경로 탐색 등 복잡한 최적화 문제 해결에 핵심적입니다. 캐싱 전략의 이론적 기반이 되며, 데이터베이스 쿼리 최적화에도 활용됩니다.
- **면접**: 코딩 테스트의 고난이도 문제 대부분이 DP입니다. 문제 분해 능력과 최적화 사고력을 평가하는 핵심 주제입니다.
- **기반 지식**: 분할 정복, 그리디, 백트래킹 등 다른 알고리즘과의 차이를 이해하고, 최적 부분 구조와 중복 부분 문제 개념을 통해 알고리즘 설계 능력을 향상시킵니다.

## 핵심 개념

- **최적 부분 구조 (Optimal Substructure)**: 큰 문제의 최적해가 작은 문제의 최적해로 구성됨
- **중복 부분 문제 (Overlapping Subproblems)**: 동일한 하위 문제가 여러 번 반복 계산됨
- **메모이제이션 (Memoization)**: Top-Down 방식, 재귀 + 캐싱
- **타뷸레이션 (Tabulation)**: Bottom-Up 방식, 반복문 + 테이블
- **상태 정의**: 문제를 표현하는 변수(상태)를 정의하는 것이 DP의 핵심

## 쉽게 이해하기

**동적 프로그래밍**을 메모하며 공부하는 것에 비유할 수 있습니다.

### DP = 똑똑한 메모 습관

수학 시험에서 피보나치 수열 문제가 나왔습니다.

**메모 없이 풀기 (비효율적)**
```
fib(5) = fib(4) + fib(3)
       = fib(3) + fib(2) + fib(2) + fib(1)
       = fib(2) + fib(1) + fib(1) + fib(0) + fib(1) + fib(0) + 1
       = ... (같은 계산 반복!)
```
fib(2)를 3번이나 계산합니다. 숫자가 커지면 같은 계산을 수백 번 반복하게 됩니다.

**왜 비효율적인가?**
- fib(50) 계산 시 함수 호출 횟수: 약 200억 번
- 같은 계산을 수없이 반복 → 시간 낭비

**메모하며 풀기 (DP)**
```
fib(0) = 0 ← 메모!
fib(1) = 1 ← 메모!
fib(2) = fib(1) + fib(0) = 1 ← 메모!
fib(3) = fib(2) + fib(1) = 2 ← 이미 계산해둔 값 사용!
fib(4) = fib(3) + fib(2) = 3 ← 이미 계산해둔 값 사용!
fib(5) = fib(4) + fib(3) = 5 ← 이미 계산해둔 값 사용!
```
각 계산을 딱 한 번만 합니다!

**왜 빠른가?**
- fib(50) 계산 시: 50번만 계산
- O(2^n) → O(n)으로 개선

### 메모이제이션 vs 타뷸레이션

| 방식 | 비유 | 설명 |
|------|------|------|
| 메모이제이션 | 시험 중 필요할 때만 메모 | 큰 문제부터 시작, 필요한 것만 계산 |
| 타뷸레이션 | 미리 공식집 만들어두기 | 작은 문제부터 차례로 표 채우기 |

### 계단 오르기 문제로 이해하기

10층 계단을 오르는데, 한 번에 1칸 또는 2칸씩 올라갈 수 있습니다. 몇 가지 방법이 있을까요?

**DP적 사고**:
- 10층에 도착하려면? → 9층에서 1칸 또는 8층에서 2칸
- 즉, `방법(10) = 방법(9) + 방법(8)`
- 작은 문제의 답으로 큰 문제를 풀 수 있습니다!

```
방법(1) = 1가지
방법(2) = 2가지
방법(3) = 방법(2) + 방법(1) = 3가지
방법(4) = 방법(3) + 방법(2) = 5가지
...
```

**왜 이렇게 생각할 수 있나요?**
- **최적 부분 구조**: n층 방법 = (n-1층 방법) + (n-2층 방법)
- **중복 부분 문제**: 방법(8)은 방법(9)과 방법(10) 계산 시 모두 필요

---

## 상세 설명

### DP 적용 조건

DP를 적용하려면 두 가지 조건을 만족해야 한다:

| 조건 | 설명 | 예시 | 왜 필요한가? |
|------|------|------|-------------|
| 최적 부분 구조 | 부분 문제의 최적해로 전체 최적해 구성 가능 | 최단 경로: A→C 최단 = A→B 최단 + B→C 최단 | 작은 문제 해결로 큰 문제 해결 가능 |
| 중복 부분 문제 | 같은 부분 문제가 반복해서 등장 | 피보나치: fib(5) 계산 시 fib(2)가 여러 번 호출 | 저장해두면 재계산 불필요 |

**만약 조건을 만족하지 않으면?**
- 최적 부분 구조 없음 → DP 적용 불가, 다른 알고리즘 필요
- 중복 없음 → 분할 정복이 더 적합 (저장할 필요 없음)

---

### 메모이제이션 vs 타뷸레이션

| 구분 | 메모이제이션 (Top-Down) | 타뷸레이션 (Bottom-Up) |
|------|------------------------|----------------------|
| 방식 | 재귀 + 캐싱 | 반복문 + 테이블 |
| 진행 방향 | 큰 문제 → 작은 문제 | 작은 문제 → 큰 문제 |
| 장점 | 직관적, 필요한 부분만 계산 | 스택 오버플로우 없음, 일반적으로 더 빠름 |
| 단점 | 스택 오버플로우 위험 | 불필요한 부분도 계산할 수 있음 |
| 적합한 경우 | 모든 하위 문제가 필요하지 않을 때 | 모든 하위 문제가 필요할 때 |

**권장 (O): 상황에 맞는 방식 선택**

```java
// 메모이제이션 - 직관적, 점화식 그대로 구현
public int fibMemo(int n, int[] memo) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];
    return memo[n] = fibMemo(n-1, memo) + fibMemo(n-2, memo);
}

// 타뷸레이션 - 안전, 일반적으로 더 빠름
public int fibTab(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 0; dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i-1] + dp[i-2];
    }
    return dp[n];
}
```

**비권장 (X): 깊은 재귀에서 메모이제이션만 사용**

```java
// n이 크면 스택 오버플로우!
fibMemo(100000, memo);  // StackOverflowError
```

**왜 스택 오버플로우가 발생하는가?**
- Java 기본 스택 크기: 약 512KB ~ 1MB
- 재귀 호출마다 스택 프레임 생성
- n=100,000이면 10만 개의 스택 프레임 → 스택 초과

**만약 깊은 재귀가 필요하면?**
- 타뷸레이션으로 변환
- 꼬리 재귀 최적화 (Java는 지원 안 함)
- 반복적 깊이 우선 탐색 (스택 직접 관리)

---

### DP 문제 해결 단계

**권장 (O): 체계적인 접근**

```
1. 문제 분석: DP 적용 가능 여부 판단
   - 최적 부분 구조? 중복 부분 문제?

2. 상태 정의: dp[i]가 무엇을 의미하는지 명확히
   - "dp[i]는 i번째까지 고려했을 때의 최댓값"

3. 점화식 도출: dp[i]를 이전 상태들로 표현
   - dp[i] = max(dp[i-1], dp[i-2] + value[i])

4. 초기값 설정: 기저 사례(base case) 설정
   - dp[0] = 0, dp[1] = value[1]

5. 계산 순서 결정: 의존 관계에 따라 계산 순서 결정
   - dp[i]가 dp[i-1], dp[i-2]에 의존 → 작은 i부터

6. 최종 답 도출: 어떤 상태가 최종 답인지 결정
   - return dp[n]
```

**비권장 (X): 바로 코드부터 작성**

**왜 문제인가?**
- 상태 정의 불명확 → 점화식 오류
- 초기값 누락 → 잘못된 결과
- 계산 순서 오류 → 아직 계산 안 된 값 참조

---

### 대표적인 DP 문제 유형

| 유형 | 대표 문제 | 상태 정의 | 시간 복잡도 |
|------|----------|----------|------------|
| 선형 DP | 피보나치, 계단 오르기 | `dp[i]`: i번째 값 | O(n) |
| 구간 DP | 행렬 곱셈, 팰린드롬 | `dp[i][j]`: i~j 구간의 최적값 | O(n²) ~ O(n³) |
| 배낭 DP | 0/1 배낭, 동전 교환 | `dp[i][w]`: i번째까지 고려, 용량 w일 때 | O(n × W) |
| 문자열 DP | LCS, 편집 거리 | `dp[i][j]`: 두 문자열의 i, j 위치까지 | O(m × n) |
| 트리 DP | 트리 지름, 독립 집합 | `dp[node]`: 해당 노드 서브트리의 값 | O(n) |

---

## 예제 코드

### 피보나치 수열

```java
public class Fibonacci {

    // 비권장 (X): 순수 재귀 - O(2^n)
    public static long fibNaive(int n) {
        if (n <= 1) return n;
        return fibNaive(n - 1) + fibNaive(n - 2);  // 중복 계산!
    }

    // 메모이제이션 (Top-Down) - O(n)
    private static long[] memo;

    public static long fibMemo(int n) {
        if (memo == null) {
            memo = new long[n + 1];
            Arrays.fill(memo, -1);
        }

        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];  // 이미 계산했으면 재사용

        memo[n] = fibMemo(n - 1) + fibMemo(n - 2);
        return memo[n];
    }

    // 타뷸레이션 (Bottom-Up) - O(n), 권장
    public static long fibTab(int n) {
        if (n <= 1) return n;

        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    // 공간 최적화 - O(n) 시간, O(1) 공간
    public static long fibOptimized(int n) {
        if (n <= 1) return n;

        long prev2 = 0;  // dp[i-2]
        long prev1 = 1;  // dp[i-1]

        for (int i = 2; i <= n; i++) {
            long current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }
}
```

**왜 공간 최적화가 가능한가?**
- `dp[i]`는 `dp[i-1]`과 `dp[i-2]`만 사용
- 전체 배열 대신 변수 2개로 충분
- O(n) → O(1) 공간 복잡도 개선

---

### 0/1 배낭 문제 (Knapsack)

```java
public class Knapsack {

    /**
     * 0/1 배낭 문제
     *
     * 상태 정의: dp[i][w] = i번째 물건까지 고려했을 때, 용량 w에서의 최대 가치
     * 점화식: dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i])
     *         (안 넣는 경우)  (넣는 경우)
     */
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                // 현재 물건을 넣지 않는 경우
                dp[i][w] = dp[i - 1][w];

                // 현재 물건을 넣을 수 있고, 넣는 게 이득인 경우
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        dp[i][w],
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                }
            }
        }

        return dp[n][capacity];
    }

    // 공간 최적화 - O(W) 공간
    public static int knapsackOptimized(int[] weights, int[] values, int capacity) {
        int[] dp = new int[capacity + 1];

        for (int i = 0; i < weights.length; i++) {
            // 역순으로 순회해야 이전 행의 값을 사용!
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }

        return dp[capacity];
    }
}
```

**왜 역순으로 순회하는가?**
- 순방향: `dp[w]`를 갱신하면 `dp[w + weight]` 계산 시 이미 갱신된 값 사용
- 역방향: 큰 w부터 갱신 → 작은 w의 값은 아직 이전 행의 값

**만약 순방향으로 순회하면?**
- 같은 물건을 여러 번 넣는 결과 (Unbounded Knapsack 문제가 됨)

---

### 최장 공통 부분 수열 (LCS)

```java
public class LCS {

    /**
     * 최장 공통 부분 수열의 길이
     *
     * 상태 정의: dp[i][j] = s1의 i번째까지, s2의 j번째까지의 LCS 길이
     * 점화식:
     *   - s1[i] == s2[j]: dp[i][j] = dp[i-1][j-1] + 1
     *   - s1[i] != s2[j]: dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     */
    public static int lcsLength(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;  // 일치: 대각선 + 1
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);  // 불일치: 위 또는 왼쪽
                }
            }
        }

        return dp[m][n];
    }

    // LCS 문자열 복원
    public static String lcsString(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // dp 테이블 채우기
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 역추적으로 LCS 문자열 복원
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                lcs.append(s1.charAt(i - 1));
                i--; j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return lcs.reverse().toString();
    }
}
```

**왜 역추적이 가능한가?**
- dp 테이블에 각 위치에서의 선택 정보가 암묵적으로 저장됨
- 같으면 → 대각선에서 왔음 (LCS에 포함)
- 다르면 → 더 큰 값에서 왔음

---

### 최장 증가 부분 수열 (LIS)

```java
public class LIS {

    // O(n²) 방법 - 기본
    public static int lisBasic(int[] arr) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);  // 최소 길이는 1

        // dp[i]: arr[i]를 마지막 원소로 하는 LIS 길이
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        return Arrays.stream(dp).max().orElse(0);
    }

    // O(n log n) 방법 - 이진 탐색 활용
    public static int lisOptimized(int[] arr) {
        List<Integer> lis = new ArrayList<>();

        for (int num : arr) {
            int pos = Collections.binarySearch(lis, num);
            if (pos < 0) {
                pos = -(pos + 1);  // 삽입 위치
            }

            if (pos == lis.size()) {
                lis.add(num);  // 맨 뒤에 추가
            } else {
                lis.set(pos, num);  // 해당 위치 값 교체
            }
        }

        return lis.size();
    }
}
```

**왜 O(n log n)이 가능한가?**
- lis 배열: 길이 k인 증가 수열의 마지막 원소 중 최솟값
- 이진 탐색으로 위치 찾기: O(log n)
- 전체: O(n log n)

**주의: lis 배열은 실제 LIS가 아님!**
- 길이만 정확, 실제 수열은 다를 수 있음
- 실제 LIS가 필요하면 별도 역추적 필요

---

## 시간 복잡도 비교

| 문제 | 순수 재귀 | DP 적용 후 | 왜 개선되는가? |
|------|----------|-----------|---------------|
| 피보나치 | O(2^n) | O(n) | 중복 계산 제거 |
| 0/1 배낭 | O(2^n) | O(n × W) | 상태 수만큼만 계산 |
| LCS | O(2^(m+n)) | O(m × n) | 모든 (i,j) 쌍 한 번씩 |
| LIS | O(2^n) | O(n²) 또는 O(n log n) | 이전 결과 재사용 |

---

## 트레이드오프

### 시간 vs 공간

| 방식 | 시간 | 공간 | 적합한 경우 |
|------|------|------|-----------|
| 기본 DP | O(상태 수) | O(상태 수) | 역추적 필요 |
| 공간 최적화 | O(상태 수) | O(1) ~ O(n) | 최종 값만 필요 |
| 메모이제이션 | O(필요한 상태) | O(상태 수) + 스택 | 일부 상태만 필요 |

### DP vs 그리디

| 기준 | DP | 그리디 |
|------|-----|-------|
| 최적해 보장 | ✅ 항상 | ⚠️ 증명 필요 |
| 시간 복잡도 | 높음 | 낮음 |
| 구현 복잡도 | 복잡 | 간단 |
| 적용 조건 | 최적 부분 구조 + 중복 | 최적 부분 구조 + 탐욕 선택 속성 |

**언제 그리디를 쓰는가?**
- 탐욕 선택 속성 증명 가능
- 더 빠른 해결 필요
- 예: 활동 선택, 허프만 코딩, 최소 신장 트리

---

## 면접 예상 질문

### Q: DP와 분할 정복의 차이점은 무엇인가요?

**A:** 둘 다 문제를 작은 부분 문제로 나누지만, 핵심 차이는 **부분 문제의 중복 여부**입니다.

| 기준 | 분할 정복 | DP |
|------|----------|-----|
| 부분 문제 중복 | ❌ 없음 | ✅ 있음 |
| 결과 저장 | 불필요 | 필요 (메모) |
| 예시 | 병합 정렬, 퀵 정렬 | 피보나치, 배낭 문제 |

**왜 이 차이가 중요한가?**
- 분할 정복: 각 부분 문제가 독립적 → 저장 없이 바로 계산
- DP: 같은 부분 문제 반복 → 저장하지 않으면 지수 시간

---

### Q: 메모이제이션과 타뷸레이션 중 어떤 것을 선택해야 하나요?

**A:**

| 상황 | 권장 방식 | 이유 |
|------|----------|------|
| 모든 하위 문제 필요 | 타뷸레이션 | 재귀 오버헤드 없음 |
| 일부 하위 문제만 필요 | 메모이제이션 | 불필요한 계산 회피 |
| 점화식이 복잡 | 메모이제이션 | 재귀가 직관적 |
| 큰 입력 (n > 10000) | 타뷸레이션 | 스택 오버플로우 방지 |

**만약 잘못 선택하면?**
- 타뷸레이션으로 희소 상태 → 불필요한 계산, 메모리 낭비
- 메모이제이션으로 깊은 재귀 → 스택 오버플로우

---

### Q: DP 문제를 어떻게 접근해야 하나요?

**A:** 단계별 접근이 중요합니다.

**1단계: DP 적용 가능 확인**
- 최적 부분 구조: 작은 문제의 최적해로 큰 문제 해결 가능?
- 중복 부분 문제: 같은 계산이 반복?

**2단계: 상태 정의**
- "dp[i]가 무엇을 의미하는가?"를 명확히
- 좋은 상태 정의가 절반의 성공

**3단계: 점화식 도출**
- dp[i]를 이전 상태들로 표현
- "마지막 선택"을 기준으로 생각

**4단계: 구현**
- 초기값, 계산 순서, 최종 답 확인

**왜 상태 정의가 중요한가?**
- 상태가 불명확 → 점화식 유도 불가
- 상태가 너무 많음 → 시간/공간 복잡도 증가
- 상태가 부족 → 문제 해결 불가

---

### Q: 공간 복잡도를 최적화하는 방법은?

**A:** 의존 관계를 분석하여 필요한 상태만 유지합니다.

| 의존 관계 | 최적화 방법 | 예시 |
|----------|-----------|------|
| dp[i]가 dp[i-1]만 사용 | 변수 1개 | 피보나치 (2개 변수) |
| dp[i]가 dp[i-1], dp[i-2] 사용 | 변수 2개 | 계단 오르기 |
| dp[i][j]가 dp[i-1][...]만 사용 | 1차원 배열 | 배낭 문제 (역순 순회) |

**만약 역추적이 필요하면?**
- 전체 테이블 유지해야 함
- 또는 선택 정보만 별도 저장

---

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [시간 복잡도](./time-complexity.md) | 선수 지식: DP의 효율성 분석 기반 | [2] 입문 |
| [재귀](../data-structure/recursion.md) | 선수 지식: 메모이제이션 구현 기반 | [2] 입문 |
| [그래프](./graph.md) | 관련 개념: 최단 경로, 트리 DP | [4] 심화 |
| [탐색](./search.md) | 관련 개념: 이진 탐색 + DP 조합 | [3] 중급 |

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 15: Dynamic Programming
- 알고리즘 문제 해결 전략 (구종만) - Chapter 8: 동적 계획법
- [백준 온라인 저지 - DP 문제집](https://www.acmicpc.net/problemset?sort=ac_desc&algo=25)
- Competitive Programmer's Handbook (Antti Laaksonen)
