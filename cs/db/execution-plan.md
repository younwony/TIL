# 실행 계획 (Execution Plan)

> `[4] 심화` · 선수 지식: [SQL](./sql.md), [Index](./index.md)

> 데이터베이스가 쿼리를 어떻게 실행할지 보여주는 분석 결과

`#실행계획` `#ExecutionPlan` `#EXPLAIN` `#QueryPlan` `#쿼리분석` `#쿼리최적화` `#QueryOptimization` `#옵티마이저` `#Optimizer` `#인덱스스캔` `#IndexScan` `#테이블스캔` `#TableScan` `#FullScan` `#풀스캔` `#NestedLoop` `#HashJoin` `#MergeJoin` `#비용` `#Cost` `#MySQL` `#PostgreSQL` `#Oracle` `#성능튜닝` `#PerformanceTuning` `#SlowQuery` `#느린쿼리`

## 왜 알아야 하는가?

같은 결과를 내는 쿼리도 실행 방식에 따라 성능이 수백 배 차이날 수 있습니다. 실행 계획을 읽으면 쿼리가 왜 느린지, 어떤 인덱스를 사용하는지, 어디를 개선해야 하는지 알 수 있습니다. 쿼리 성능 튜닝의 시작점입니다.

## 핵심 개념

- **EXPLAIN**: 실행 계획을 확인하는 명령어
- **Access Method**: 테이블/인덱스 접근 방법
- **Join Type**: 테이블 조인 방식
- **Cost**: 쿼리 실행에 예상되는 비용
- **Rows**: 처리될 예상 행 수

## 쉽게 이해하기

**실행 계획**을 네비게이션 경로에 비유할 수 있습니다.

```
목적지: 서울 → 부산

경로 A (고속도로):
서울 → 경부고속도로 → 부산
예상 시간: 4시간, 통행료: 30,000원

경로 B (국도):
서울 → 국도 1호선 → 부산
예상 시간: 8시간, 통행료: 0원

┌─────────────────────────────────────────────────────────────┐
│  EXPLAIN = 쿼리의 네비게이션                                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  SELECT * FROM orders WHERE user_id = 100;                  │
│                                                              │
│  경로 A (인덱스 사용):                                       │
│  idx_user_id 인덱스 → 해당 행만 조회                        │
│  예상 비용: 10, 행: 5개                                      │
│                                                              │
│  경로 B (풀 스캔):                                           │
│  orders 테이블 전체 스캔 → 조건 필터                         │
│  예상 비용: 10000, 행: 100만개                               │
│                                                              │
│  → 옵티마이저가 경로 A 선택                                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### EXPLAIN 기본 사용법

```sql
-- MySQL
EXPLAIN SELECT * FROM orders WHERE user_id = 100;

-- PostgreSQL
EXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = 100;

-- MySQL 상세 정보
EXPLAIN FORMAT=JSON SELECT * FROM orders WHERE user_id = 100;

-- MySQL 실제 실행
EXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = 100;
```

### MySQL EXPLAIN 출력 해석

```sql
EXPLAIN SELECT * FROM orders WHERE user_id = 100;

+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
| id | select_type | table  | type | possible_keys | key  | key_len | ref  | rows | Extra       |
+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
|  1 | SIMPLE      | orders | ref  | idx_user_id   |idx_user_id| 4 | const|    5 | Using index |
+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
```

```
┌─────────────────────────────────────────────────────────────┐
│                    EXPLAIN 컬럼 설명                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  id: 쿼리 실행 순서 (서브쿼리면 여러 개)                       │
│                                                              │
│  select_type: 쿼리 유형                                      │
│    - SIMPLE: 단순 SELECT                                     │
│    - PRIMARY: 가장 바깥 SELECT                               │
│    - SUBQUERY: 서브쿼리                                      │
│    - DERIVED: FROM 절의 서브쿼리                             │
│    - UNION: UNION의 두 번째 이후 SELECT                      │
│                                                              │
│  table: 접근하는 테이블                                      │
│                                                              │
│  type: 접근 방법 (중요! 성능에 직결)                         │
│    - system: 테이블에 1행만 존재                             │
│    - const: PRIMARY KEY로 1행 조회                           │
│    - eq_ref: 조인에서 PRIMARY KEY 사용                       │
│    - ref: 인덱스로 여러 행 조회                              │
│    - range: 인덱스 범위 스캔                                 │
│    - index: 인덱스 풀 스캔                                   │
│    - ALL: 테이블 풀 스캔 ⚠️                                  │
│                                                              │
│  possible_keys: 사용 가능한 인덱스                           │
│  key: 실제 사용된 인덱스                                     │
│  key_len: 사용된 인덱스 길이 (바이트)                        │
│  ref: 인덱스와 비교되는 컬럼/상수                            │
│  rows: 예상 조회 행 수                                       │
│                                                              │
│  Extra: 추가 정보                                            │
│    - Using index: 커버링 인덱스 (효율적)                     │
│    - Using where: WHERE 필터링                               │
│    - Using temporary: 임시 테이블 사용 ⚠️                    │
│    - Using filesort: 정렬 필요 ⚠️                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### type 컬럼 성능 순위

```
좋음 ←─────────────────────────────────────→ 나쁨

system > const > eq_ref > ref > range > index > ALL

┌─────────────────────────────────────────────────────────────┐
│  type별 설명                                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  const (최고):                                               │
│    SELECT * FROM users WHERE id = 1;                        │
│    PRIMARY KEY로 1행 조회                                    │
│                                                              │
│  eq_ref (조인에서 최고):                                     │
│    SELECT * FROM orders o                                    │
│    JOIN users u ON o.user_id = u.id;                        │
│    조인 시 PRIMARY KEY 사용                                  │
│                                                              │
│  ref (좋음):                                                 │
│    SELECT * FROM orders WHERE user_id = 100;                │
│    논클러스터드 인덱스로 여러 행 조회                         │
│                                                              │
│  range (괜찮음):                                             │
│    SELECT * FROM orders WHERE created_at BETWEEN ... ;      │
│    인덱스 범위 스캔                                          │
│                                                              │
│  index (주의):                                               │
│    SELECT COUNT(*) FROM orders;                             │
│    인덱스 전체 스캔                                          │
│                                                              │
│  ALL (나쁨 ⚠️):                                              │
│    SELECT * FROM orders WHERE description LIKE '%keyword%'; │
│    테이블 풀 스캔                                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 조인 알고리즘

```
┌─────────────────────────────────────────────────────────────┐
│                    조인 알고리즘                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Nested Loop Join                                        │
│     ─────────────────                                        │
│     for each row in outer_table:                            │
│         for each row in inner_table:                        │
│             if match: output                                 │
│                                                              │
│     적합: 작은 테이블 × 인덱스 있는 테이블                   │
│     복잡도: O(N × M) (인덱스 없으면)                         │
│                                                              │
│  2. Hash Join                                                │
│     ─────────                                                │
│     Step 1: 작은 테이블로 해시 테이블 생성                   │
│     Step 2: 큰 테이블 스캔하며 해시 조회                     │
│                                                              │
│     적합: 큰 테이블 조인, 동등 조인                          │
│     복잡도: O(N + M)                                         │
│                                                              │
│  3. Sort Merge Join                                          │
│     ───────────────                                          │
│     Step 1: 양쪽 테이블 정렬                                 │
│     Step 2: 병합하며 매칭                                    │
│                                                              │
│     적합: 이미 정렬된 데이터, 범위 조인                      │
│     복잡도: O(N log N + M log M)                             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### PostgreSQL EXPLAIN ANALYZE

```sql
EXPLAIN ANALYZE
SELECT u.name, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE u.status = 'active'
GROUP BY u.id;

                                    QUERY PLAN
-------------------------------------------------------------------------------
 HashAggregate  (cost=1234.56..1234.78 rows=22 width=40)
                (actual time=45.123..45.234 rows=20 loops=1)
   Group Key: u.id
   ->  Hash Right Join  (cost=10.50..1134.56 rows=10000 width=36)
                        (actual time=0.234..35.678 rows=9500 loops=1)
         Hash Cond: (o.user_id = u.id)
         ->  Seq Scan on orders o  (cost=0.00..850.00 rows=50000 width=8)
                                   (actual time=0.012..15.345 rows=50000 loops=1)
         ->  Hash  (cost=8.00..8.00 rows=200 width=36)
                   (actual time=0.156..0.157 rows=200 loops=1)
               Buckets: 1024  Batches: 1  Memory Usage: 16kB
               ->  Seq Scan on users u  (cost=0.00..8.00 rows=200 width=36)
                                        (actual time=0.005..0.089 rows=200 loops=1)
                     Filter: (status = 'active')
                     Rows Removed by Filter: 50
 Planning Time: 0.234 ms
 Execution Time: 45.456 ms
```

```
┌─────────────────────────────────────────────────────────────┐
│               PostgreSQL EXPLAIN 해석                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  cost=시작비용..총비용                                       │
│    - 시작비용: 첫 행 반환까지 비용                           │
│    - 총비용: 모든 행 반환까지 비용                           │
│                                                              │
│  rows: 예상 반환 행 수                                       │
│  width: 예상 행 크기 (바이트)                                │
│                                                              │
│  actual time=시작..종료: 실제 실행 시간 (ms)                 │
│  rows: 실제 반환 행 수                                       │
│  loops: 실행 횟수                                            │
│                                                              │
│  ⚠️ 예상(cost, rows) vs 실제(actual) 차이가 크면            │
│     → 통계 정보 갱신 필요: ANALYZE table_name;               │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 문제가 되는 패턴

```sql
-- 1. 테이블 풀 스캔 (type: ALL)
EXPLAIN SELECT * FROM orders WHERE YEAR(created_at) = 2024;
-- 해결: 인덱스 사용 가능하게 수정
SELECT * FROM orders
WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';

-- 2. Using filesort (정렬 비용)
EXPLAIN SELECT * FROM orders ORDER BY created_at;
-- 해결: ORDER BY 컬럼에 인덱스

-- 3. Using temporary (임시 테이블)
EXPLAIN SELECT DISTINCT user_id FROM orders;
-- 해결: 인덱스 활용 또는 쿼리 재구성

-- 4. 큰 rows 값
EXPLAIN SELECT * FROM orders WHERE status = 'pending';
-- 해결: 선택도 높은 조건 추가, 인덱스 재설계
```

### 쿼리 최적화 체크리스트

```
┌─────────────────────────────────────────────────────────────┐
│                    최적화 체크리스트                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  □ type이 ALL인가?                                          │
│    → 인덱스 추가 또는 쿼리 수정                              │
│                                                              │
│  □ possible_keys는 있는데 key가 NULL인가?                   │
│    → 옵티마이저가 인덱스 사용 안 함, 이유 분석               │
│                                                              │
│  □ rows가 예상보다 너무 큰가?                               │
│    → 조건 추가, 인덱스 최적화                                │
│                                                              │
│  □ Extra에 Using temporary, filesort가 있는가?              │
│    → 인덱스로 정렬/그룹핑 가능한지 확인                      │
│                                                              │
│  □ 서브쿼리가 DEPENDENT SUBQUERY인가?                       │
│    → JOIN으로 변환 검토                                      │
│                                                              │
│  □ 실제 실행 시간(ANALYZE)이 예상과 크게 다른가?             │
│    → ANALYZE table_name; 으로 통계 갱신                      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 쿼리 성능 문제 진단 | 읽기 학습 필요 |
| 인덱스 사용 여부 확인 | 예상치와 실제 차이 가능 |
| 최적화 방향 제시 | 데이터 분포에 따라 달라짐 |

## 면접 예상 질문

### Q: EXPLAIN 결과에서 가장 먼저 확인해야 할 것은?

A: (1) **type 컬럼**: ALL(풀스캔)인지 확인. ALL이면 인덱스 미사용. (2) **rows**: 예상 처리 행 수가 너무 크면 문제. (3) **key**: 실제 사용된 인덱스 확인. possible_keys는 있는데 key가 NULL이면 인덱스 미사용. (4) **Extra**: Using temporary, Using filesort는 추가 비용 발생 신호.

### Q: 인덱스가 있는데 사용되지 않는 경우는?

A: (1) **함수 사용**: `WHERE YEAR(date) = 2024` → `WHERE date BETWEEN ...`. (2) **타입 불일치**: 문자열 컬럼에 숫자 비교. (3) **선택도 낮음**: 너무 많은 행이 매칭되면 옵티마이저가 풀스캔 선택. (4) **복합 인덱스 순서**: `(a, b)` 인덱스에 `WHERE b = 1` → 인덱스 미사용. (5) **통계 부정확**: `ANALYZE TABLE` 필요.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Index](./index.md) | 인덱스 이해 | [3] 중급 |
| [SQL](./sql.md) | SQL 기초 | [2] 입문 |
| [MySQL 인덱스](./mysql-index.md) | MySQL 특화 | [4] 심화 |

## 참고 자료

- [MySQL EXPLAIN](https://dev.mysql.com/doc/refman/8.0/en/explain.html)
- [PostgreSQL EXPLAIN](https://www.postgresql.org/docs/current/using-explain.html)
- [Use The Index, Luke!](https://use-the-index-luke.com/)
