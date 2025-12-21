# SQL

> 데이터베이스에서 데이터를 정의, 조작, 제어하기 위한 표준 언어

> `[2] 입문` · 선수 지식: 없음

## 왜 알아야 하는가?

SQL은 백엔드 개발자가 반드시 알아야 할 가장 기본적인 기술입니다. 웹 서비스의 대부분의 데이터는 관계형 데이터베이스에 저장되며, 데이터를 조회하고 조작하려면 SQL이 필수입니다. 단순히 SELECT, INSERT, UPDATE, DELETE를 아는 것을 넘어 JOIN, 서브쿼리, 실행 계획 분석 등을 이해해야 효율적인 쿼리를 작성하고 성능 문제를 해결할 수 있습니다. SQL은 한 번 배우면 평생 사용하는 기술로, 프로그래밍 언어가 바뀌어도 SQL은 변하지 않습니다.

## 핵심 개념

- **DML (Data Manipulation Language)**: 데이터 조작 - SELECT, INSERT, UPDATE, DELETE
- **DDL (Data Definition Language)**: 데이터 정의 - CREATE, ALTER, DROP
- **DCL (Data Control Language)**: 데이터 제어 - GRANT, REVOKE
- **JOIN**: 여러 테이블의 데이터를 연결하여 조회
- **실행 계획 (Execution Plan)**: 쿼리가 실제로 어떻게 실행되는지 보여주는 정보

## 쉽게 이해하기

**SQL**을 도서관에서 책을 찾는 것에 비유할 수 있습니다.

- **SELECT**: "컴퓨터 관련 책 찾아줘" - 조건에 맞는 책(데이터)을 검색
- **INSERT**: "새 책을 서가에 추가" - 새로운 데이터 입력
- **UPDATE**: "책 위치를 변경" - 기존 데이터 수정
- **DELETE**: "오래된 책을 폐기" - 데이터 삭제

**JOIN**은 여러 서가의 정보를 조합하는 것과 같습니다. 예를 들어, "책 정보" 서가와 "저자 정보" 서가를 연결해서 "이 책의 저자는 누구인가?"를 찾을 수 있습니다.

**실행 계획**은 사서가 "어떤 순서로 어느 서가를 뒤질지" 계획을 세우는 것입니다. 색인(인덱스)을 먼저 보고 찾을지, 아니면 서가를 전부 뒤질지 결정합니다.

## 상세 설명

### SELECT 기본 구조

```sql
SELECT column1, column2
FROM table_name
WHERE condition
GROUP BY column1
HAVING group_condition
ORDER BY column1 DESC
LIMIT 10;
```

**왜 이 순서인가?**

SQL은 위와 같이 작성하지만, 실제 실행 순서는 다릅니다:
1. **FROM**: 테이블 선택
2. **WHERE**: 행 필터링
3. **GROUP BY**: 그룹화
4. **HAVING**: 그룹 필터링
5. **SELECT**: 컬럼 선택
6. **ORDER BY**: 정렬
7. **LIMIT**: 개수 제한

이 순서를 이해해야 "SELECT 절에서 정의한 별칭을 WHERE에서 사용할 수 없는 이유"를 알 수 있습니다. **왜냐하면** WHERE는 SELECT보다 먼저 실행되기 때문입니다.

### JOIN 종류

**INNER JOIN (내부 조인)**

```sql
SELECT u.name, o.order_date
FROM users u
INNER JOIN orders o ON u.id = o.user_id;
```

**왜 사용하나?**
양쪽 테이블에 **모두 존재하는 데이터만** 필요할 때 사용합니다. 주문하지 않은 사용자는 결과에 포함되지 않습니다.

**LEFT JOIN (왼쪽 조인)**

```sql
SELECT u.name, o.order_date
FROM users u
LEFT JOIN orders o ON u.id = o.user_id;
```

**왜 사용하나?**
왼쪽 테이블(users)의 **모든 데이터를 유지**하면서 오른쪽 테이블(orders)과 매칭합니다. 주문하지 않은 사용자도 결과에 포함되며, order_date는 NULL로 표시됩니다.

**사용 시나리오**: "모든 사용자의 목록과 각 사용자의 주문 내역(있다면)"을 조회할 때

### JOIN 성능 최적화

**왜 JOIN 순서가 중요한가?**

```sql
-- Bad: 큰 테이블부터 JOIN
SELECT *
FROM large_table (100만 건)
JOIN small_table (100건) ON large_table.id = small_table.id;

-- Good: 작은 테이블부터 필터링
SELECT *
FROM small_table (100건)
JOIN large_table (100만 건) ON small_table.id = large_table.id;
```

옵티마이저가 자동으로 최적화하지만, 복잡한 쿼리에서는 명시적으로 순서를 조정해야 합니다. **왜냐하면** 작은 결과 집합을 먼저 만들어야 후속 JOIN에서 처리할 데이터가 줄어들기 때문입니다.

### 서브쿼리 (Subquery)

**스칼라 서브쿼리 (Scalar Subquery)**

```sql
SELECT name,
       (SELECT COUNT(*) FROM orders WHERE user_id = users.id) as order_count
FROM users;
```

**왜 문제인가?**
각 행마다 서브쿼리가 실행되어 N+1 문제 발생. users 테이블이 1000건이면 서브쿼리도 1000번 실행됩니다.

**해결책**: JOIN으로 변경

```sql
SELECT u.name, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.name;
```

**왜 이게 더 나은가?**
JOIN은 한 번의 실행으로 모든 데이터를 가져오고, GROUP BY로 집계합니다. 실행 횟수: 1회

**IN 서브쿼리**

```sql
-- 서브쿼리 방식
SELECT * FROM users
WHERE id IN (SELECT user_id FROM orders WHERE order_date > '2024-01-01');

-- JOIN 방식 (보통 더 빠름)
SELECT DISTINCT u.*
FROM users u
INNER JOIN orders o ON u.id = o.user_id
WHERE o.order_date > '2024-01-01';
```

**언제 서브쿼리를 사용하나?**
- 가독성이 중요하고 성능 차이가 크지 않을 때
- 서브쿼리 결과가 매우 작을 때 (10-100건 이하)

### 실행 계획 (EXPLAIN)

```sql
EXPLAIN SELECT * FROM users WHERE email = 'test@example.com';
```

**주요 확인 항목**

| 항목 | 의미 | 좋음 | 나쁨 |
|------|------|------|------|
| type | 접근 방식 | const, eq_ref, ref | ALL (풀 스캔) |
| possible_keys | 사용 가능 인덱스 | 여러 개 | NULL |
| key | 실제 사용 인덱스 | 인덱스 이름 | NULL |
| rows | 검사할 행 수 | 적을수록 | 많을수록 |
| Extra | 추가 정보 | Using index | Using filesort |

**왜 실행 계획을 확인해야 하나?**

동일한 결과를 반환하는 쿼리라도 실행 방식에 따라 성능이 100배 이상 차이 날 수 있습니다.

**예시**: 100만 건 테이블에서
- 인덱스 스캔: 0.01초 (log N)
- 풀 테이블 스캔: 10초 (N)

### 쿼리 최적화 원칙

**1. SELECT * 지양**

```sql
-- Bad
SELECT * FROM users WHERE id = 1;

-- Good
SELECT id, name, email FROM users WHERE id = 1;
```

**왜?**
- 불필요한 데이터 전송으로 네트워크 대역폭 낭비
- 커버링 인덱스(covering index) 활용 불가
- 애플리케이션에서 실제로 사용하지 않는 컬럼까지 메모리에 로드

**2. WHERE 절에 함수 사용 지양**

```sql
-- Bad: 인덱스 사용 불가
SELECT * FROM users WHERE YEAR(created_at) = 2024;

-- Good: 인덱스 사용 가능
SELECT * FROM users WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';
```

**왜 함수 사용이 문제인가?**
인덱스는 원본 컬럼 값으로 정렬되어 있습니다. `YEAR(created_at)`처럼 함수를 적용하면 모든 행의 created_at을 함수로 변환해야 하므로 인덱스를 활용할 수 없습니다.

**3. LIKE 패턴 최적화**

```sql
-- Bad: 인덱스 사용 불가
SELECT * FROM products WHERE name LIKE '%phone%';

-- Good: 인덱스 사용 가능
SELECT * FROM products WHERE name LIKE 'phone%';
```

**왜 앞에 %가 문제인가?**
B+Tree 인덱스는 앞글자부터 순차적으로 비교합니다. `'%phone%'`은 중간에 'phone'이 있는 모든 단어를 찾아야 하므로 전체 스캔이 필요합니다.

**해결책**: Full-text 인덱스 사용 또는 ElasticSearch 같은 검색 엔진 활용

**4. OR 대신 UNION 고려**

```sql
-- Bad: 인덱스 비효율
SELECT * FROM products WHERE category_id = 1 OR status = 'active';

-- Good: 각각 인덱스 활용
SELECT * FROM products WHERE category_id = 1
UNION
SELECT * FROM products WHERE status = 'active';
```

**왜?**
OR 조건은 양쪽 조건을 모두 확인해야 하므로 인덱스를 효율적으로 사용하기 어렵습니다. UNION은 각 쿼리가 독립적으로 인덱스를 활용하고 결과를 합칩니다.

**주의**: UNION은 중복 제거, UNION ALL은 중복 허용 (더 빠름)

### 페이징 최적화

**일반적인 페이징 (문제 발생)**

```sql
SELECT * FROM products ORDER BY id LIMIT 10000, 10;
```

**왜 느린가?**
10010개 행을 읽어서 앞의 10000개를 버리고 10개만 반환합니다. 페이지가 뒤로 갈수록 느려집니다.

**최적화: 커서 기반 페이징**

```sql
-- 첫 페이지
SELECT * FROM products WHERE id > 0 ORDER BY id LIMIT 10;

-- 다음 페이지 (마지막 id가 10이었다면)
SELECT * FROM products WHERE id > 10 ORDER BY id LIMIT 10;
```

**왜 빠른가?**
인덱스를 활용해 시작 위치를 직접 찾아가므로 앞의 데이터를 읽지 않습니다.

**트레이드오프**: 특정 페이지 번호로 바로 이동 불가 (이전/다음만 가능)

## 트레이드오프

### 서브쿼리 vs JOIN

| 항목 | 서브쿼리 | JOIN |
|------|---------|------|
| 가독성 | 논리적으로 명확 | 복잡할 수 있음 |
| 성능 | 스칼라 서브쿼리는 느림 | 보통 더 빠름 |
| 사용 시점 | 단순하고 데이터 작을 때 | 대용량, 성능 중요 시 |

### 정규화 vs 반정규화

| 항목 | 정규화 | 반정규화 |
|------|---------|---------|
| 중복 | 최소화 | 의도적 중복 허용 |
| 조회 성능 | JOIN 필요로 느림 | JOIN 없이 빠름 |
| 쓰기 성능 | 빠름 | 여러 곳 업데이트 필요 |
| 데이터 일관성 | 높음 | 관리 필요 |
| 사용 시점 | OLTP (트랜잭션 많음) | OLAP (분석, 조회 많음) |

## 면접 예상 질문

- Q: INNER JOIN과 LEFT JOIN의 차이는 무엇인가요?
  - A: INNER JOIN은 양쪽 테이블에 매칭되는 데이터만 반환하고, LEFT JOIN은 왼쪽 테이블의 모든 데이터를 유지하면서 오른쪽 테이블과 매칭합니다. **왜냐하면** INNER JOIN은 교집합, LEFT JOIN은 왼쪽 테이블 전체 + 매칭되는 오른쪽 데이터이기 때문입니다. 예를 들어, 모든 사용자와 그들의 주문 내역을 보려면 LEFT JOIN을 사용해야 주문이 없는 사용자도 포함됩니다.

- Q: WHERE 절에서 함수를 사용하면 왜 성능이 나빠지나요?
  - A: 인덱스를 활용할 수 없기 때문입니다. **왜냐하면** 인덱스는 원본 컬럼 값으로 정렬되어 있는데, `YEAR(created_at) = 2024`처럼 함수를 사용하면 모든 행의 created_at을 함수로 변환한 후 비교해야 하므로 풀 테이블 스캔이 발생합니다. **따라서** `created_at >= '2024-01-01' AND created_at < '2025-01-01'`처럼 원본 컬럼에 직접 조건을 걸어야 인덱스를 사용할 수 있습니다.

- Q: 실행 계획(EXPLAIN)에서 가장 먼저 확인해야 할 것은 무엇인가요?
  - A: type 컬럼과 rows 컬럼입니다. **왜냐하면** type이 'ALL'이면 풀 테이블 스캔을 의미하여 성능이 매우 나쁘고, rows가 크면 검사할 행이 많다는 의미이기 때문입니다. 이상적으로는 type이 'const', 'eq_ref', 'ref'이고 rows가 작아야 합니다. 또한 Extra에 'Using filesort'나 'Using temporary'가 있으면 추가 정렬이나 임시 테이블 생성이 발생하므로 인덱스 추가를 고려해야 합니다.

- Q: 서브쿼리를 항상 JOIN으로 바꿔야 하나요?
  - A: 아닙니다. **경우에 따라 다릅니다**. 스칼라 서브쿼리(SELECT 절의 서브쿼리)는 N+1 문제로 성능이 나쁘므로 JOIN으로 변경해야 합니다. 하지만 IN 서브쿼리는 서브쿼리 결과가 작고(10-100건) 가독성이 중요하다면 그대로 사용해도 됩니다. **왜냐하면** 최신 DB 옵티마이저는 단순한 IN 서브쿼리를 자동으로 JOIN으로 변환하기 때문입니다. **따라서** 실행 계획으로 확인 후 성능 차이가 크지 않으면 가독성을 우선하는 것도 좋습니다.

- Q: LIMIT 10000, 10이 왜 느린가요?
  - A: 10010개 행을 모두 읽어서 앞의 10000개를 버리고 10개만 반환하기 때문입니다. **왜냐하면** LIMIT은 OFFSET만큼 건너뛰는 것이 아니라 실제로 읽은 후 버리는 방식이기 때문입니다. 페이지 번호가 1000 (LIMIT 10000, 10)이면 10010개를 읽어야 하므로 매우 느립니다. **해결책**: 커서 기반 페이징(`WHERE id > last_id LIMIT 10`)을 사용하면 인덱스로 시작 위치를 직접 찾아가므로 빠릅니다. 단, 특정 페이지로 바로 이동할 수 없다는 트레이드오프가 있습니다.

## 연관 문서

- [Index](./index.md): SQL 성능 최적화를 위한 인덱스
- [Transaction](./transaction.md): SQL과 트랜잭션 관리
- [Normalization](./normalization.md): 효율적인 테이블 설계
- [MySQL Index](./mysql-index.md): MySQL 특화 인덱스 최적화
- [JPA](./jpa.md): SQL 대신 ORM 사용하기

## 참고 자료

- [MySQL 공식 문서 - Optimization](https://dev.mysql.com/doc/refman/8.0/en/optimization.html)
- [PostgreSQL 공식 문서 - Performance Tips](https://www.postgresql.org/docs/current/performance-tips.html)
- [Use The Index, Luke - SQL 인덱싱 가이드](https://use-the-index-luke.com/)
