# LATERAL JOIN

> `[3] 중급` · 선수 지식: [SQL](./sql.md)

> 외부 쿼리의 각 행을 참조하여 서브쿼리를 실행할 수 있게 해주는 상관 조인(Correlated Join)

`#LATERAL` `#LateralJoin` `#측면조인` `#상관조인` `#CorrelatedJoin` `#CROSSAPPLY` `#OUTERAPPLY` `#서브쿼리` `#Subquery` `#상관서브쿼리` `#CorrelatedSubquery` `#TopN` `#그룹별최상위` `#TopNPerGroup` `#테이블함수` `#TableFunction` `#UNNEST` `#JSON펼치기` `#PostgreSQL` `#MySQL8` `#Oracle` `#SQLServer` `#APPLY` `#인라인뷰` `#파생테이블` `#DerivedTable` `#조인최적화` `#N+1대안`

## 왜 알아야 하는가?

- **실무**: 그룹별 Top-N 조회, JSON 배열 펼치기, 복잡한 집계 등 일반 JOIN으로 해결하기 어려운 패턴을 간결하게 처리
- **면접**: 상관 서브쿼리와의 차이, CROSS APPLY vs OUTER APPLY, 성능 최적화 질문 빈출
- **기반 지식**: SQL 고급 기능, 쿼리 최적화, ORM의 N+1 문제 해결에 활용

## 핵심 개념

- **LATERAL**: 서브쿼리가 외부 쿼리의 현재 행을 참조할 수 있게 허용하는 키워드
- **상관 조인**: 왼쪽 테이블의 각 행에 대해 오른쪽 서브쿼리가 독립적으로 평가됨
- **CROSS APPLY / OUTER APPLY**: SQL Server/Oracle에서 LATERAL과 동일한 기능

## 쉽게 이해하기

**일반 JOIN은 "미리 준비된 두 테이블을 합치는 것"**

마치 두 개의 완성된 엑셀 시트를 VLOOKUP으로 연결하는 것과 같습니다.

**LATERAL JOIN은 "각 행마다 맞춤 서브쿼리를 실행하는 것"**

식당에서 손님(외부 테이블)이 주문할 때마다, 그 손님의 상황(알레르기, 선호도)을 고려해서 맞춤 메뉴 목록(서브쿼리 결과)을 제공하는 것과 같습니다.

```
일반 JOIN:
┌─────────┐     ┌─────────┐
│ Table A │ ──▶ │ Table B │ ← 미리 고정된 결과
└─────────┘     └─────────┘

LATERAL JOIN:
┌─────────┐     ┌─────────────────┐
│ Row 1   │ ──▶ │ 서브쿼리(Row 1) │ ← Row 1 기준 동적 결과
├─────────┤     ├─────────────────┤
│ Row 2   │ ──▶ │ 서브쿼리(Row 2) │ ← Row 2 기준 동적 결과
├─────────┤     ├─────────────────┤
│ Row 3   │ ──▶ │ 서브쿼리(Row 3) │ ← Row 3 기준 동적 결과
└─────────┘     └─────────────────┘
```

## 상세 설명

### 기본 문법

#### PostgreSQL / MySQL 8.0+

```sql
SELECT *
FROM 외부테이블 t1
CROSS JOIN LATERAL (
    SELECT *
    FROM 내부테이블 t2
    WHERE t2.외래키 = t1.기본키  -- 외부 테이블 참조 가능!
    LIMIT 3
) sub;
```

#### SQL Server / Oracle

```sql
-- CROSS APPLY: INNER JOIN과 유사 (결과 없으면 제외)
SELECT *
FROM 외부테이블 t1
CROSS APPLY (
    SELECT TOP 3 *
    FROM 내부테이블 t2
    WHERE t2.외래키 = t1.기본키
) sub;

-- OUTER APPLY: LEFT JOIN과 유사 (결과 없어도 포함)
SELECT *
FROM 외부테이블 t1
OUTER APPLY (
    SELECT TOP 3 *
    FROM 내부테이블 t2
    WHERE t2.외래키 = t1.기본키
) sub;
```

### LATERAL vs 일반 서브쿼리

**왜 일반 서브쿼리로는 안 되는가?**

```sql
-- ❌ 오류: FROM 절 서브쿼리에서 외부 테이블 참조 불가
SELECT *
FROM departments d,
     (SELECT * FROM employees e
      WHERE e.dept_id = d.id  -- 오류!
      ORDER BY salary DESC LIMIT 3) top3;

-- ✅ LATERAL 사용: 외부 테이블 참조 가능
SELECT *
FROM departments d
CROSS JOIN LATERAL (
    SELECT * FROM employees e
    WHERE e.dept_id = d.id  -- 가능!
    ORDER BY salary DESC LIMIT 3
) top3;
```

### JOIN 유형별 LATERAL 조합

| 조합 | 동작 | SQL Server 대응 |
|------|------|----------------|
| `CROSS JOIN LATERAL` | 서브쿼리 결과 없으면 행 제외 | `CROSS APPLY` |
| `LEFT JOIN LATERAL ... ON true` | 서브쿼리 결과 없어도 NULL로 포함 | `OUTER APPLY` |
| `INNER JOIN LATERAL ... ON 조건` | 조건 만족하는 결과만 포함 | - |

```sql
-- LEFT JOIN LATERAL (결과 없어도 포함)
SELECT d.name, top3.employee_name, top3.salary
FROM departments d
LEFT JOIN LATERAL (
    SELECT e.name AS employee_name, e.salary
    FROM employees e
    WHERE e.dept_id = d.id
    ORDER BY e.salary DESC
    LIMIT 3
) top3 ON true;
```

## 대표 사용 사례

### 1. 그룹별 Top-N (Top-N per Group)

**각 부서별 급여 상위 3명 조회**

```sql
-- PostgreSQL / MySQL 8.0+
SELECT d.name AS dept_name, e.name, e.salary
FROM departments d
CROSS JOIN LATERAL (
    SELECT name, salary
    FROM employees
    WHERE dept_id = d.id
    ORDER BY salary DESC
    LIMIT 3
) e;
```

**왜 Window Function 대신 LATERAL을 쓰는가?**

```sql
-- Window Function 방식 (전체 스캔 필요)
SELECT * FROM (
    SELECT d.name AS dept_name, e.name, e.salary,
           ROW_NUMBER() OVER (PARTITION BY d.id ORDER BY e.salary DESC) rn
    FROM departments d
    JOIN employees e ON e.dept_id = d.id
) ranked
WHERE rn <= 3;

-- LATERAL 방식 (인덱스 활용 가능)
SELECT d.name AS dept_name, e.name, e.salary
FROM departments d
CROSS JOIN LATERAL (
    SELECT name, salary
    FROM employees
    WHERE dept_id = d.id
    ORDER BY salary DESC
    LIMIT 3
) e;
```

| 방식 | 장점 | 단점 |
|------|------|------|
| Window Function | 범용적, 모든 DBMS 지원 | 전체 정렬 후 필터링 |
| LATERAL | 인덱스로 조기 종료 가능 | DBMS 제한적 |

### 2. JSON 배열 펼치기

```sql
-- PostgreSQL: jsonb_array_elements
SELECT o.order_id, item->>'product_name' AS product, item->>'qty' AS qty
FROM orders o
CROSS JOIN LATERAL jsonb_array_elements(o.items) AS item;

-- MySQL 8.0+: JSON_TABLE
SELECT o.order_id, jt.product_name, jt.qty
FROM orders o
CROSS JOIN LATERAL JSON_TABLE(
    o.items,
    '$[*]' COLUMNS (
        product_name VARCHAR(100) PATH '$.product_name',
        qty INT PATH '$.qty'
    )
) jt;
```

### 3. 테이블 반환 함수 호출

```sql
-- 각 사용자의 최근 로그인 기록 3개
SELECT u.name, logs.login_time, logs.ip_address
FROM users u
CROSS JOIN LATERAL get_recent_logins(u.id, 3) AS logs;
```

### 4. 복잡한 계산 재사용

```sql
-- 계산 결과를 여러 컬럼에서 재사용
SELECT
    p.name,
    calc.base_price,
    calc.discount_amount,
    calc.base_price - calc.discount_amount AS final_price
FROM products p
CROSS JOIN LATERAL (
    SELECT
        p.price * (1 + p.tax_rate) AS base_price,
        p.price * p.discount_rate AS discount_amount
) calc;
```

## 동작 원리

```
┌─────────────────────────────────────────────────────────────┐
│                    LATERAL JOIN 실행 흐름                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│    외부 테이블 (departments)                                 │
│    ┌────────────────────────┐                               │
│    │ id=1, name='Engineering'│                              │
│    └───────────┬────────────┘                               │
│                │                                             │
│                ▼                                             │
│    ┌────────────────────────────────────────────┐          │
│    │ LATERAL 서브쿼리 실행 (dept_id = 1 조건)    │          │
│    │ → employees WHERE dept_id=1 ORDER BY salary│          │
│    │ → LIMIT 3                                   │          │
│    └────────────────────────────────────────────┘          │
│                │                                             │
│                ▼  결과: [Alice, Bob, Carol]                 │
│    ┌────────────────────────────────────────────┐          │
│    │ 결과 결합: (Engineering, Alice)             │          │
│    │           (Engineering, Bob)               │          │
│    │           (Engineering, Carol)             │          │
│    └────────────────────────────────────────────┘          │
│                │                                             │
│                ▼  다음 행                                    │
│    ┌────────────────────────┐                               │
│    │ id=2, name='Sales'     │                               │
│    └───────────┬────────────┘                               │
│                │                                             │
│                ▼                                             │
│    ┌────────────────────────────────────────────┐          │
│    │ LATERAL 서브쿼리 실행 (dept_id = 2 조건)    │          │
│    │ → employees WHERE dept_id=2 ORDER BY salary│          │
│    │ → LIMIT 3                                   │          │
│    └────────────────────────────────────────────┘          │
│                │                                             │
│                ▼  결과: [Dave, Eve]                         │
│    ┌────────────────────────────────────────────┐          │
│    │ 결과 결합: (Sales, Dave)                    │          │
│    │           (Sales, Eve)                     │          │
│    └────────────────────────────────────────────┘          │
│                                                              │
│    ... 모든 외부 행에 대해 반복 ...                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 그룹별 Top-N을 효율적으로 처리 | PostgreSQL, MySQL 8.0+, SQL Server 등 제한적 지원 |
| 인덱스 활용으로 조기 종료 가능 | 최적화 힌트 부족 시 N+1과 유사한 동작 |
| 복잡한 상관 서브쿼리 간결화 | 실행 계획 분석이 복잡해질 수 있음 |
| JSON/배열 펼치기에 필수 | 잘못 사용 시 Nested Loop 성능 문제 |

## 성능 고려사항

### 인덱스 필수

LATERAL 서브쿼리가 효율적으로 동작하려면 **서브쿼리 조건에 맞는 인덱스**가 필요합니다.

```sql
-- 필요한 인덱스: employees(dept_id, salary DESC)
CREATE INDEX idx_emp_dept_salary ON employees(dept_id, salary DESC);
```

### 실행 계획 확인

```sql
EXPLAIN ANALYZE
SELECT d.name, e.*
FROM departments d
CROSS JOIN LATERAL (
    SELECT * FROM employees
    WHERE dept_id = d.id
    ORDER BY salary DESC
    LIMIT 3
) e;
```

**좋은 실행 계획 예시:**
```
Nested Loop  (rows=30)
  -> Seq Scan on departments d  (rows=10)
  -> Limit  (rows=3)
       -> Index Scan using idx_emp_dept_salary on employees
            Index Cond: (dept_id = d.id)  ← 인덱스 활용!
```

## 면접 예상 질문

### Q: LATERAL JOIN과 상관 서브쿼리의 차이는?

A: **위치와 반환 형태**가 다릅니다.

- **상관 서브쿼리**: SELECT/WHERE 절에서 사용, 스칼라 값 반환
- **LATERAL JOIN**: FROM 절에서 사용, 테이블(여러 행/컬럼) 반환

LATERAL은 그룹별 Top-N처럼 **여러 행을 반환해야 하는 경우** 상관 서브쿼리로는 불가능한 패턴을 해결합니다.

### Q: CROSS APPLY와 OUTER APPLY의 차이는?

A: INNER JOIN과 LEFT JOIN의 차이와 동일합니다.

- **CROSS APPLY**: 서브쿼리 결과가 없으면 해당 외부 행 제외
- **OUTER APPLY**: 서브쿼리 결과가 없어도 NULL로 포함

### Q: LATERAL JOIN이 항상 효율적인가?

A: **인덱스가 있을 때 효율적**입니다. 인덱스 없이 사용하면 각 외부 행마다 Full Scan이 발생하여 N+1 문제와 유사한 성능 저하가 발생합니다. 반드시 서브쿼리 조건에 맞는 복합 인덱스를 생성해야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [SQL](./sql.md) | 선수 지식 - 기본 JOIN, 서브쿼리 | 입문 |
| [Index](./index.md) | LATERAL 최적화의 핵심 | 중급 |
| [실행 계획](./execution-plan.md) | 성능 분석 필수 | 심화 |
| [JPA](./jpa.md) | N+1 문제 해결 대안 | 심화 |

## 참고 자료

- [PostgreSQL Documentation - LATERAL Subqueries](https://www.postgresql.org/docs/current/queries-table-expressions.html#QUERIES-LATERAL)
- [MySQL 8.0 Reference - Lateral Derived Tables](https://dev.mysql.com/doc/refman/8.0/en/lateral-derived-tables.html)
- [SQL Server Documentation - APPLY](https://docs.microsoft.com/en-us/sql/t-sql/queries/from-transact-sql)
- [Use The Index, Luke - Nested Loops](https://use-the-index-luke.com/sql/join/nested-loops-join-n1-problem)
