# 동적 프로그래밍 (Dynamic Programming)

> 복잡한 문제를 작은 하위 문제로 나누어 해결하고, 그 결과를 저장하여 중복 계산을 피하는 알고리즘 설계 기법

## 핵심 개념

- **최적 부분 구조 (Optimal Substructure)**: 큰 문제의 최적해가 작은 문제의 최적해로 구성됨
- **중복 부분 문제 (Overlapping Subproblems)**: 동일한 하위 문제가 여러 번 반복 계산됨
- **메모이제이션 (Memoization)**: Top-Down 방식, 재귀 + 캐싱
- **타뷸레이션 (Tabulation)**: Bottom-Up 방식, 반복문 + 테이블
- **상태 정의**: 문제를 표현하는 변수(상태)를 정의하는 것이 DP의 핵심

## 상세 설명

### DP 적용 조건

DP를 적용하려면 두 가지 조건을 만족해야 한다:

| 조건 | 설명 | 예시 |
|------|------|------|
| 최적 부분 구조 | 부분 문제의 최적해로 전체 최적해 구성 가능 | 최단 경로: A→C 최단 = A→B 최단 + B→C 최단 |
| 중복 부분 문제 | 같은 부분 문제가 반복해서 등장 | 피보나치: fib(5) 계산 시 fib(2)가 여러 번 호출 |

### 메모이제이션 vs 타뷸레이션

| 구분 | 메모이제이션 (Top-Down) | 타뷸레이션 (Bottom-Up) |
|------|------------------------|----------------------|
| 방식 | 재귀 + 캐싱 | 반복문 + 테이블 |
| 진행 방향 | 큰 문제 → 작은 문제 | 작은 문제 → 큰 문제 |
| 장점 | 직관적, 필요한 부분만 계산 | 스택 오버플로우 없음, 일반적으로 더 빠름 |
| 단점 | 스택 오버플로우 위험 | 불필요한 부분도 계산할 수 있음 |
| 적합한 경우 | 모든 하위 문제가 필요하지 않을 때 | 모든 하위 문제가 필요할 때 |

### DP 문제 해결 단계

1. **문제 분석**: DP 적용 가능 여부 판단
2. **상태 정의**: `dp[i]`가 무엇을 의미하는지 정의
3. **점화식 도출**: `dp[i]`를 이전 상태들로 표현
4. **초기값 설정**: 기저 사례(base case) 설정
5. **계산 순서 결정**: 의존 관계에 따라 계산 순서 결정
6. **최종 답 도출**: 어떤 상태가 최종 답인지 결정

### 대표적인 DP 문제 유형

| 유형 | 대표 문제 | 상태 정의 |
|------|----------|----------|
| 선형 DP | 피보나치, 계단 오르기 | `dp[i]`: i번째 값 |
| 구간 DP | 행렬 곱셈, 팰린드롬 | `dp[i][j]`: i~j 구간의 최적값 |
| 배낭 DP | 0/1 배낭, 동전 교환 | `dp[i][w]`: i번째까지 고려, 용량 w일 때 최적값 |
| 문자열 DP | LCS, 편집 거리 | `dp[i][j]`: 두 문자열의 i, j 위치까지 비교 |
| 트리 DP | 트리 지름, 독립 집합 | `dp[node]`: 해당 노드를 루트로 하는 서브트리의 값 |

## 예제 코드

### 피보나치 수열

```java
public class Fibonacci {

    // 메모이제이션 (Top-Down)
    private static long[] memo;

    public static long fibMemo(int n) {
        if (memo == null) {
            memo = new long[n + 1];
            Arrays.fill(memo, -1);
        }

        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];

        memo[n] = fibMemo(n - 1) + fibMemo(n - 2);
        return memo[n];
    }

    // 타뷸레이션 (Bottom-Up)
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

    // 공간 최적화 (O(1) 공간)
    public static long fibOptimized(int n) {
        if (n <= 1) return n;

        long prev2 = 0;
        long prev1 = 1;

        for (int i = 2; i <= n; i++) {
            long current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }
}
```

### 0/1 배낭 문제 (Knapsack)

```java
public class Knapsack {

    /**
     * 0/1 배낭 문제
     * @param weights 물건의 무게 배열
     * @param values 물건의 가치 배열
     * @param capacity 배낭 용량
     * @return 최대 가치
     */
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // dp[i][w]: i번째 물건까지 고려했을 때, 용량 w에서의 최대 가치
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
}
```

### 최장 공통 부분 수열 (LCS)

```java
public class LCS {

    /**
     * 최장 공통 부분 수열의 길이
     * @param s1 첫 번째 문자열
     * @param s2 두 번째 문자열
     * @return LCS 길이
     */
    public static int lcsLength(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // dp[i][j]: s1의 i번째까지, s2의 j번째까지의 LCS 길이
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }
}
```

### 최장 증가 부분 수열 (LIS)

```java
public class LIS {

    // O(n^2) 방법
    public static int lisBasic(int[] arr) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

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
                pos = -(pos + 1);
            }

            if (pos == lis.size()) {
                lis.add(num);
            } else {
                lis.set(pos, num);
            }
        }

        return lis.size();
    }
}
```

## 시간 복잡도 비교

| 문제 | 순수 재귀 | DP 적용 후 |
|------|----------|-----------|
| 피보나치 | O(2^n) | O(n) |
| 0/1 배낭 | O(2^n) | O(n × W) |
| LCS | O(2^(m+n)) | O(m × n) |
| LIS | O(2^n) | O(n²) 또는 O(n log n) |

## 면접 예상 질문

- **Q: DP와 분할 정복의 차이점은 무엇인가요?**
  - A: 둘 다 문제를 작은 부분 문제로 나누지만, 분할 정복은 부분 문제가 중복되지 않고, DP는 부분 문제가 중복됩니다. DP는 중복되는 부분 문제의 결과를 저장하여 재사용합니다.

- **Q: 메모이제이션과 타뷸레이션 중 어떤 것을 선택해야 하나요?**
  - A: 모든 하위 문제를 풀어야 하면 타뷸레이션이 효율적입니다(오버헤드가 적음). 일부 하위 문제만 필요하거나 점화식이 복잡하면 메모이제이션이 구현하기 쉽습니다.

- **Q: DP 문제를 어떻게 접근해야 하나요?**
  - A: 먼저 최적 부분 구조와 중복 부분 문제가 있는지 확인합니다. 그 다음 상태를 정의하고(`dp[i]`가 무엇을 의미하는지), 점화식을 세웁니다. 초기값과 계산 순서를 정하고 구현합니다.

## 참고 자료

- Introduction to Algorithms (CLRS) - Chapter 15: Dynamic Programming
- 알고리즘 문제 해결 전략 (구종만) - Chapter 8: 동적 계획법
- [백준 온라인 저지 - DP 문제집](https://www.acmicpc.net/problemset?sort=ac_desc&algo=25)
