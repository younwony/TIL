# MySQL CLI 쿼리 튜닝

> `[3] 중급` · 선수 지식: [SQL](./sql.md), [MySQL 인덱스](./mysql-index.md), [실행 계획](./execution-plan.md)

> MySQL Shell을 활용하여 직접 DB에 접속하고, 인덱스 확인·실행 계획 분석·쿼리 프로파일링으로 성능을 최적화하는 실전 가이드

`#MySQL` `#MySQLShell` `#mysqlsh` `#CLI` `#쿼리튜닝` `#QueryTuning` `#EXPLAIN` `#실행계획` `#ExecutionPlan` `#인덱스` `#Index` `#SHOWINDEX` `#프로파일링` `#Profiling` `#SlowQuery` `#느린쿼리` `#성능최적화` `#PerformanceTuning` `#카디널리티` `#Cardinality` `#풀스캔` `#FullScan` `#옵티마이저` `#Optimizer` `#InnoDB` `#informationSchema` `#sysSchema` `#DBA`

## 왜 알아야 하는가?

- **실무**: 운영 중인 DB의 슬로우 쿼리를 즉시 분석하고 개선해야 할 때, CLI 도구로 빠르게 진단할 수 있다
- **면접**: "쿼리가 느릴 때 어떻게 분석하나요?"는 백엔드 면접 단골 질문이다
- **기반 지식**: 실행 계획, 인덱스 이론을 알아도 CLI에서 직접 확인하지 못하면 실무에서 활용할 수 없다

## 핵심 개념

- **MySQL Shell**: MySQL 공식 차세대 CLI 도구로 SQL/JavaScript/Python 모드를 지원한다
- **EXPLAIN**: 쿼리의 실행 계획을 분석하여 인덱스 사용 여부와 비용을 확인한다
- **SHOW INDEX**: 테이블에 설정된 인덱스 목록과 카디널리티를 확인한다
- **프로파일링**: 쿼리 실행 단계별 소요 시간을 측정하여 병목 구간을 찾는다
- **sys 스키마**: MySQL 성능 분석을 위한 뷰와 함수를 제공하는 내장 스키마이다

## 쉽게 이해하기

쿼리 튜닝을 **자동차 정비**에 비유할 수 있다.

```
자동차가 느려졌다 (슬로우 쿼리 발견)
    │
    ▼
계기판 확인 (EXPLAIN으로 실행 계획 분석)
    │
    ├─ 엔진 오일 부족? (인덱스 없음 → 인덱스 생성)
    ├─ 타이어 마모?    (잘못된 인덱스 → 인덱스 수정)
    └─ 짐이 너무 무거움? (SELECT * → 필요한 컬럼만 조회)
    │
    ▼
시운전으로 검증 (EXPLAIN 재확인 + 프로파일링)
```

**핵심**: 느린 쿼리를 "감"이 아니라 **데이터 기반**으로 진단하고 개선하는 것이다.

## MySQL Shell 셋업

### 설치

```shell
# Windows (winget)
winget install Oracle.MySQLShell

# macOS (Homebrew)
brew install mysql-shell

# Linux (apt)
sudo apt install mysql-shell
```

### 접속

```shell
# SQL 모드로 직접 접속
mysqlsh --sql -u 사용자명 -p -h 호스트 -P 3306 --schema=DB명

# 접속 후 모드 전환
\sql        # SQL 모드
\js         # JavaScript 모드
\py         # Python 모드

# 연결 URI 형식
mysqlsh mysql://user:password@host:3306/dbname
```

### 전통적인 mysql 클라이언트와 비교

| 항목 | mysql (전통) | mysqlsh (Shell) |
|------|-------------|-----------------|
| 모드 | SQL만 | SQL / JS / Python |
| 프로토콜 | Classic | Classic + X Protocol |
| 자동 완성 | 기본 | 향상된 자동 완성 |
| 출력 형식 | 테이블 | 테이블 / JSON / 탭 |
| AdminAPI | 미지원 | InnoDB Cluster 관리 |
| 설치 | MySQL Server 포함 | 독립 설치 가능 |

> **팁**: MySQL Shell은 서버 없이 **클라이언트만** 설치할 수 있어서, 원격 DB 접속용으로 가볍게 사용 가능하다.

![MySQL Shell 아키텍처](./images/mysql-shell-architecture.svg)

## 상세 설명

### 1. 인덱스 확인

테이블에 어떤 인덱스가 있는지 확인하는 것이 튜닝의 첫 번째 단계이다.

```sql
-- 테이블 인덱스 목록 확인
SHOW INDEX FROM orders;
```

**결과 주요 컬럼:**

| 컬럼 | 의미 | 확인 포인트 |
|------|------|------------|
| `Key_name` | 인덱스 이름 | PRIMARY, 세컨더리 구분 |
| `Column_name` | 인덱스 컬럼 | 복합 인덱스의 컬럼 순서 |
| `Seq_in_index` | 복합 인덱스 내 순서 | 1부터 시작, 순서가 성능에 영향 |
| `Cardinality` | 고유 값 개수 (추정) | 높을수록 인덱스 효율 좋음 |
| `Null` | NULL 허용 여부 | YES면 인덱스 성능 저하 가능 |

```sql
-- information_schema로 상세 인덱스 통계 확인
SELECT
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX,
    CARDINALITY,
    NULLABLE
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'mydb'
  AND TABLE_NAME = 'orders'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;
```

**왜 카디널리티가 중요한가?**

카디널리티(Cardinality)는 인덱스의 **선택도(Selectivity)**를 결정한다.

```sql
-- 카디널리티 확인: 높을수록 좋다
-- gender (M/F) → 카디널리티 2 → 인덱스 효율 낮음
-- email (고유값) → 카디널리티 100만 → 인덱스 효율 높음

SELECT
    COUNT(DISTINCT gender) AS gender_cardinality,
    COUNT(DISTINCT email) AS email_cardinality,
    COUNT(*) AS total_rows
FROM users;
```

### 2. EXPLAIN으로 실행 계획 분석

```sql
-- 기본 실행 계획
EXPLAIN SELECT * FROM orders WHERE user_id = 100;

-- JSON 포맷 (상세 비용 정보 포함)
EXPLAIN FORMAT=JSON SELECT * FROM orders WHERE user_id = 100;

-- ANALYZE: 실제 실행 후 측정값 포함 (MySQL 8.0.18+)
EXPLAIN ANALYZE SELECT * FROM orders WHERE user_id = 100;
```

**EXPLAIN 결과 핵심 컬럼:**

| 컬럼 | 의미 | 좋은 값 | 나쁜 값 |
|------|------|---------|---------|
| `type` | 접근 방식 | `const`, `eq_ref`, `ref` | `ALL` (풀스캔) |
| `key` | 사용된 인덱스 | 인덱스 이름 | `NULL` (인덱스 미사용) |
| `rows` | 예상 스캔 행 수 | 적을수록 좋음 | 테이블 전체 행 수 |
| `Extra` | 추가 정보 | `Using index` | `Using filesort`, `Using temporary` |
| `filtered` | 조건 필터 비율 | 100에 가까울수록 좋음 | 낮으면 불필요한 행 많이 스캔 |

**type 컬럼 성능 순서 (좋은 순):**

```
system > const > eq_ref > ref > range > index > ALL
  │        │        │       │      │       │      │
  │        │        │       │      │       │      └─ 풀 테이블 스캔 (최악)
  │        │        │       │      │       └─ 풀 인덱스 스캔
  │        │        │       │      └─ 범위 스캔 (BETWEEN, <, >)
  │        │        │       └─ 인덱스로 단일/다중 행 조회
  │        │        └─ 조인에서 PK/유니크 인덱스로 1행 조회
  │        └─ PK/유니크 인덱스로 상수 조건 1행 조회
  └─ 테이블에 1행만 존재
```

### 3. 쿼리 프로파일링

쿼리 실행의 **단계별 소요 시간**을 측정한다.

```sql
-- 프로파일링 활성화
SET profiling = 1;

-- 분석할 쿼리 실행
SELECT o.order_id, u.name, o.total_amount
FROM orders o
JOIN users u ON o.user_id = u.id
WHERE o.created_at >= '2025-01-01'
ORDER BY o.total_amount DESC
LIMIT 100;

-- 프로파일 결과 확인
SHOW PROFILES;

-- 특정 쿼리의 상세 프로파일
SHOW PROFILE FOR QUERY 1;

-- CPU, Block I/O 포함 상세 프로파일
SHOW PROFILE CPU, BLOCK IO FOR QUERY 1;
```

**프로파일 결과 주요 항목:**

| 단계 | 의미 | 병목 판단 |
|------|------|----------|
| `Sending data` | 스토리지 엔진에서 데이터 읽기 | 이 단계가 길면 인덱스/쿼리 개선 필요 |
| `Sorting result` | ORDER BY 정렬 | filesort 발생 시 인덱스로 정렬 커버 검토 |
| `Creating tmp table` | 임시 테이블 생성 | GROUP BY, DISTINCT 최적화 필요 |
| `Copying to tmp table` | 임시 테이블에 데이터 복사 | 메모리 → 디스크 전환 시 심각한 성능 저하 |

> **주의**: `SHOW PROFILE`은 MySQL 8.0에서 deprecated 상태이다. **Performance Schema**를 권장한다.

```sql
-- Performance Schema로 최근 쿼리 성능 확인 (권장 방식)
SELECT
    DIGEST_TEXT,
    COUNT_STAR AS exec_count,
    ROUND(AVG_TIMER_WAIT / 1000000000, 2) AS avg_ms,
    ROUND(SUM_TIMER_WAIT / 1000000000, 2) AS total_ms,
    SUM_ROWS_EXAMINED AS rows_examined,
    SUM_ROWS_SENT AS rows_sent
FROM performance_schema.events_statements_summary_by_digest
ORDER BY AVG_TIMER_WAIT DESC
LIMIT 10;
```

### 4. sys 스키마 활용

MySQL 5.7+의 sys 스키마는 성능 분석에 유용한 뷰를 제공한다.

```sql
-- 사용되지 않는 인덱스 확인 (불필요한 인덱스 제거 후보)
SELECT * FROM sys.schema_unused_indexes
WHERE object_schema = 'mydb';

-- 중복 인덱스 확인
SELECT * FROM sys.schema_redundant_indexes
WHERE table_schema = 'mydb';

-- 풀 테이블 스캔이 발생하는 테이블
SELECT * FROM sys.statements_with_full_table_scans
ORDER BY no_index_used_count DESC
LIMIT 10;

-- 테이블별 I/O 통계
SELECT * FROM sys.schema_table_statistics
WHERE table_schema = 'mydb'
ORDER BY io_read_latency DESC;

-- 인덱스별 사용 통계
SELECT * FROM sys.schema_index_statistics
WHERE table_schema = 'mydb'
ORDER BY rows_selected DESC;
```

### 5. 슬로우 쿼리 확인

```sql
-- 슬로우 쿼리 설정 확인
SHOW VARIABLES LIKE 'slow_query%';
SHOW VARIABLES LIKE 'long_query_time';

-- 슬로우 쿼리 활성화 (세션)
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;  -- 1초 이상 쿼리 기록

-- 슬로우 쿼리 로그 위치 확인
SHOW VARIABLES LIKE 'slow_query_log_file';
```

## 동작 원리

![쿼리 튜닝 워크플로우](./images/query-tuning-workflow.svg)

## 실전 튜닝 예제

### 사례 1: 풀 테이블 스캔 → 인덱스 추가

```sql
-- 느린 쿼리
SELECT * FROM orders WHERE status = 'PENDING' AND created_at >= '2025-01-01';

-- EXPLAIN 확인
EXPLAIN SELECT * FROM orders WHERE status = 'PENDING' AND created_at >= '2025-01-01';
-- type: ALL, key: NULL, rows: 1000000 → 풀 스캔

-- 복합 인덱스 생성 (카디널리티 높은 컬럼을 앞에)
CREATE INDEX idx_orders_status_created ON orders(status, created_at);

-- EXPLAIN 재확인
EXPLAIN SELECT * FROM orders WHERE status = 'PENDING' AND created_at >= '2025-01-01';
-- type: range, key: idx_orders_status_created, rows: 5000 → 대폭 개선
```

### 사례 2: SELECT * 제거 + 커버링 인덱스

```sql
-- 느린 쿼리: SELECT *는 모든 컬럼을 읽어야 함
SELECT * FROM users WHERE email LIKE 'john%';

-- 개선: 필요한 컬럼만 조회
SELECT id, email, name FROM users WHERE email LIKE 'john%';

-- 커버링 인덱스 생성
CREATE INDEX idx_users_email_cover ON users(email, id, name);

-- EXPLAIN 확인
-- Extra: Using index → 테이블 접근 없이 인덱스만으로 처리
```

### 사례 3: ORDER BY 최적화

```sql
-- 느린 쿼리: filesort 발생
SELECT order_id, total_amount
FROM orders
WHERE user_id = 100
ORDER BY created_at DESC;

-- EXPLAIN → Extra: Using filesort (정렬을 위한 추가 연산)

-- 인덱스로 정렬 커버
CREATE INDEX idx_orders_user_created ON orders(user_id, created_at DESC);

-- EXPLAIN 재확인 → Extra에 Using filesort 사라짐
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| CLI로 빠르게 진단 가능 | GUI 도구 대비 학습 곡선 |
| 서버에 직접 접속하여 분석 | 프로덕션 접속 시 보안 주의 필요 |
| 스크립트로 자동화 가능 | EXPLAIN 예측값과 실제 실행은 다를 수 있음 |
| 인덱스 통계 즉시 확인 | 통계가 오래되면 부정확 (ANALYZE TABLE 필요) |

## 트러블슈팅

### 사례 1: EXPLAIN에서 인덱스를 안 타는 경우

#### 증상
인덱스가 존재하는데 `EXPLAIN`의 `key`가 `NULL`이고 `type`이 `ALL`이다.

#### 원인 분석
옵티마이저가 인덱스 사용보다 풀 스캔이 더 효율적이라고 판단한 경우이다.

- 테이블의 대부분 행을 읽어야 할 때 (선택도가 낮은 조건)
- 인덱스 통계가 오래되어 부정확할 때
- 타입 불일치 (문자열 컬럼에 숫자로 비교)
- 함수/연산으로 인덱스 컬럼을 감싼 경우

#### 해결 방법

```sql
-- 1. 인덱스 통계 갱신
ANALYZE TABLE orders;

-- 2. 타입 불일치 확인
-- 나쁜 예: phone은 VARCHAR인데 숫자로 비교
EXPLAIN SELECT * FROM users WHERE phone = 01012345678;
-- 좋은 예: 문자열로 비교
EXPLAIN SELECT * FROM users WHERE phone = '01012345678';

-- 3. 함수 사용 제거
-- 나쁜 예: 인덱스 컬럼에 함수 적용
EXPLAIN SELECT * FROM orders WHERE YEAR(created_at) = 2025;
-- 좋은 예: 범위 조건으로 변환
EXPLAIN SELECT * FROM orders WHERE created_at >= '2025-01-01' AND created_at < '2026-01-01';

-- 4. 인덱스 힌트 (최후의 수단)
EXPLAIN SELECT * FROM orders FORCE INDEX (idx_status) WHERE status = 'PENDING';
```

#### 예방 조치
- 주기적으로 `ANALYZE TABLE` 실행 (통계 갱신)
- WHERE 조건의 컬럼 타입과 비교 값의 타입 일치 확인
- 인덱스 컬럼에 함수/연산 적용 금지

### 사례 2: Using temporary + Using filesort 동시 발생

#### 증상
`Extra` 컬럼에 `Using temporary; Using filesort`가 표시되며 쿼리가 느리다.

#### 원인 분석
GROUP BY와 ORDER BY의 컬럼이 다르거나, 인덱스로 커버할 수 없는 정렬/그룹핑이다.

#### 해결 방법

```sql
-- 느린 쿼리
SELECT status, COUNT(*) AS cnt
FROM orders
WHERE created_at >= '2025-01-01'
GROUP BY status
ORDER BY cnt DESC;

-- 개선: 서브쿼리로 분리하여 임시 테이블 크기 줄이기
SELECT status, cnt
FROM (
    SELECT status, COUNT(*) AS cnt
    FROM orders
    WHERE created_at >= '2025-01-01'
    GROUP BY status
) sub
ORDER BY cnt DESC;

-- 또는 커버링 인덱스로 GROUP BY 최적화
CREATE INDEX idx_orders_created_status ON orders(created_at, status);
```

## 면접 예상 질문

### Q: 쿼리가 느릴 때 어떤 순서로 분석하나요?

A: 먼저 `EXPLAIN`으로 실행 계획을 확인하여 `type`이 `ALL`(풀 스캔)인지, `key`가 `NULL`(인덱스 미사용)인지 확인한다. 그다음 `SHOW INDEX`로 해당 테이블의 인덱스 현황과 카디널리티를 확인하고, 필요하면 복합 인덱스를 생성한다. 개선 후 `EXPLAIN ANALYZE`로 실제 실행 시간을 비교 검증한다. **"왜"**: 감이 아닌 데이터 기반 의사결정이 정확한 튜닝으로 이어지기 때문이다.

### Q: EXPLAIN의 type 컬럼에서 ALL과 index의 차이는?

A: `ALL`은 테이블의 모든 데이터 행을 처음부터 끝까지 읽는 **풀 테이블 스캔**이다. `index`는 인덱스 트리의 모든 리프 노드를 읽는 **풀 인덱스 스캔**이다. `index`가 `ALL`보다 나은 이유는 인덱스가 테이블보다 크기가 작아 I/O가 적기 때문이다. 하지만 둘 다 전체를 읽는다는 점에서 `ref`나 `range`보다 비효율적이다.

### Q: 인덱스를 만들었는데 옵티마이저가 안 쓰는 경우는?

A: 주요 원인은 세 가지다. (1) **선택도가 낮을 때** — 예를 들어 성별 컬럼처럼 고유값이 2개뿐이면 인덱스를 타도 테이블의 50%를 읽어야 하므로 풀 스캔이 더 빠르다. (2) **타입 불일치** — VARCHAR 컬럼에 숫자를 넣으면 암묵적 형변환이 발생하여 인덱스를 사용할 수 없다. (3) **인덱스 컬럼에 함수 적용** — `WHERE YEAR(date_col) = 2025` 같은 조건은 인덱스를 무효화한다. 범위 조건으로 바꿔야 한다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [SQL](./sql.md) | 선수 지식 — SQL 기본 문법 | [2] 입문 |
| [MySQL 인덱스](./mysql-index.md) | 선수 지식 — B+Tree, 클러스터드/세컨더리 인덱스 | [4] 심화 |
| [실행 계획](./execution-plan.md) | 선수 지식 — EXPLAIN 이론 | [4] 심화 |
| [Index](./index.md) | 관련 — 인덱스 일반 개념 | [3] 중급 |
| [open_table_cache](./open-table-cache.md) | 관련 — MySQL 내부 캐시 구조 | [3] 중급 |

## 참고 자료

- MySQL 공식 문서 — MySQL Shell User Guide
- MySQL 공식 문서 — EXPLAIN Output Format
- MySQL 공식 문서 — Performance Schema
- MySQL 공식 문서 — sys Schema Reference
- Real MySQL 8.0 (백은빈, 이성욱)
