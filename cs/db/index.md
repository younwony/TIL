# Index (인덱스)

> 데이터베이스 테이블의 검색 속도를 향상시키기 위해 특정 컬럼에 대한 정렬된 별도의 자료구조

> `[3] 중급` · 선수 지식: [SQL](./sql.md)

## 왜 알아야 하는가?

인덱스는 데이터베이스 성능 최적화의 핵심입니다. 100만 건의 데이터에서 특정 레코드를 찾을 때, 인덱스가 없으면 10초가 걸리지만 인덱스를 사용하면 0.01초로 단축됩니다. 하지만 인덱스를 잘못 설계하면 오히려 성능이 저하되고, 너무 많이 만들면 INSERT/UPDATE/DELETE 성능이 떨어집니다. 백엔드 개발자라면 반드시 인덱스의 작동 원리와 설계 방법을 알아야 쿼리 성능 문제를 해결하고 효율적인 데이터베이스를 설계할 수 있습니다.

## 핵심 개념

- **B+Tree**: 데이터베이스 인덱스의 가장 일반적인 자료구조로, 균형 잡힌 트리 구조
- **클러스터드 인덱스 (Clustered Index)**: 테이블의 실제 데이터가 인덱스 순서대로 물리적으로 정렬됨
- **논클러스터드 인덱스 (Non-Clustered Index)**: 별도의 공간에 인덱스를 생성하고 데이터 위치를 가리킴
- **커버링 인덱스 (Covering Index)**: 쿼리에 필요한 모든 컬럼이 인덱스에 포함되어 테이블 접근 없이 처리 가능
- **인덱스 스캔**: 인덱스를 활용한 검색 방식으로 O(log N) 성능

## 쉽게 이해하기

**인덱스**를 책의 찾아보기(색인)에 비유할 수 있습니다.

"데이터베이스"라는 단어를 책에서 찾는다고 상상해보세요:

- **인덱스 없이 (풀 스캔)**: 책의 첫 페이지부터 마지막 페이지까지 한 장씩 넘기며 찾기 (1시간 소요)
- **인덱스 사용**: 뒤의 찾아보기를 보고 "데이터베이스 - 15페이지"를 확인 후 바로 이동 (10초 소요)

**클러스터드 인덱스**는 사전입니다. 단어(데이터) 자체가 가나다순으로 정렬되어 있습니다.

**논클러스터드 인덱스**는 소설책 뒤의 색인입니다. 색인은 알파벳순이지만, 실제 본문은 이야기 순서대로 되어 있습니다. 색인에서 "서울 - 47페이지"를 찾고, 다시 47페이지로 이동해야 합니다.

**커버링 인덱스**는 "인물 - 페이지 - 요약"이 모두 적힌 상세 색인입니다. 본문으로 가지 않아도 색인에서 필요한 정보를 모두 얻을 수 있습니다.

## 상세 설명

### 인덱스가 필요한 이유

**문제 상황**: 100만 건의 사용자 테이블에서 특정 이메일 검색

```sql
-- 인덱스 없음: 100만 건 전부 확인 (풀 테이블 스캔)
SELECT * FROM users WHERE email = 'test@example.com';  -- 10초
```

**해결책**: email 컬럼에 인덱스 생성

```sql
CREATE INDEX idx_email ON users(email);
SELECT * FROM users WHERE email = 'test@example.com';  -- 0.01초
```

**왜 1000배 빠른가?**

- 인덱스 없음: O(N) - 100만 건 모두 확인
- 인덱스 있음: O(log N) - 약 20번의 비교로 찾음 (log₂ 1,000,000 ≈ 20)

### B+Tree 구조

**왜 B+Tree를 사용하는가?**

```
              [50]
             /    \
        [20, 35]  [70, 90]
        /  |  \    /  |  \
     [10] [25] [40] [60] [80] [95] → 데이터 페이지 (리프 노드)
      ↓    ↓    ↓    ↓    ↓    ↓
     실제 데이터들 (연결 리스트로 연결)
```

**B+Tree의 3가지 장점**:

1. **균형 유지**: 모든 리프 노드가 같은 깊이 → 일관된 검색 성능
2. **범위 검색 효율**: 리프 노드가 연결 리스트로 연결 → `BETWEEN`, `>`, `<` 빠름
3. **디스크 I/O 최소화**: 높이가 3-4로 제한 (100만 건도 4번 읽기로 찾음)

**왜 다른 자료구조가 아닌가?**

| 자료구조 | 문제점 |
|---------|--------|
| Hash Table | 범위 검색 불가 (`age > 30` 불가), 정렬 불가 |
| Binary Search Tree | 균형 보장 안 됨 (편향 트리 시 O(N)) |
| B-Tree | 모든 노드에 데이터 저장 → 범위 검색 시 비효율 |

### 클러스터드 인덱스 (Clustered Index)

**특징**: 테이블당 1개만 가능, 테이블 자체가 인덱스 순서로 정렬됨

```sql
-- MySQL의 경우 Primary Key가 자동으로 클러스터드 인덱스
CREATE TABLE users (
    id INT PRIMARY KEY,      -- 클러스터드 인덱스
    name VARCHAR(100),
    email VARCHAR(100)
);
```

**데이터 저장 구조**:
```
[인덱스 페이지]
     1
    / \
   5   10
  / \  / \
[데이터 페이지] - 실제 데이터가 id 순서로 정렬되어 저장
1: {id:1, name:"Alice", email:"alice@..."}
2: {id:2, name:"Bob", email:"bob@..."}
...
```

**장점**:
- 범위 검색 매우 빠름 (데이터가 물리적으로 연속)
- 인덱스를 통해 데이터를 찾으면 바로 모든 컬럼 접근 가능

**단점**:
- INSERT/UPDATE/DELETE 시 데이터 재정렬 발생 (느림)
- 테이블당 1개만 가능

**왜 Primary Key를 클러스터드로 하는가?**

Primary Key는 중복이 없고 변경이 거의 없으며, 조인 시 자주 사용되므로 물리적 정렬의 이점이 큽니다.

### 논클러스터드 인덱스 (Non-Clustered Index)

**특징**: 테이블당 여러 개 생성 가능, 별도 공간에 인덱스 저장

```sql
CREATE INDEX idx_email ON users(email);  -- 논클러스터드 인덱스
```

**데이터 저장 구조**:
```
[인덱스 페이지]
"alice@..." → PK:1
"bob@..."   → PK:2
"charlie@..." → PK:3

[실제 테이블] - 물리적 순서는 id 순서 (클러스터드 인덱스)
{id:1, name:"Alice", email:"alice@..."}
{id:2, name:"Bob", email:"bob@..."}
{id:3, name:"Charlie", email:"charlie@..."}
```

**검색 과정**: 2단계
1. 논클러스터드 인덱스에서 "alice@..." → PK:1 찾기
2. 클러스터드 인덱스로 PK:1의 실제 데이터 찾기

**장점**:
- 여러 컬럼에 대해 생성 가능
- INSERT/UPDATE/DELETE 시 테이블 재정렬 없음

**단점**:
- 2단계 검색으로 클러스터드보다 느림
- 추가 저장 공간 필요 (테이블 크기의 10-20%)

### 복합 인덱스 (Composite Index)

**여러 컬럼을 조합한 인덱스**

```sql
CREATE INDEX idx_name_age ON users(name, age);
```

**왜 순서가 중요한가?**

인덱스는 **왼쪽부터 순차적으로** 정렬됩니다:

```
(Alice, 25)
(Alice, 30)
(Bob, 20)
(Bob, 35)
(Charlie, 40)
```

**사용 가능한 쿼리**:
```sql
-- O: name만 사용 (왼쪽부터 가능)
SELECT * FROM users WHERE name = 'Alice';

-- O: name + age 모두 사용
SELECT * FROM users WHERE name = 'Alice' AND age = 30;

-- X: age만 사용 (name 없이 불가)
SELECT * FROM users WHERE age = 30;  -- 인덱스 사용 불가
```

**왜 age만으로는 안 되는가?**

사전에서 "30세"를 찾는다고 상상해보세요. 사전은 "이름 → 나이" 순으로 정렬되어 있습니다. 이름 없이 나이만으로는 어디서 시작해야 할지 알 수 없습니다.

**복합 인덱스 설계 원칙**: 카디널리티(고유값 수)가 높은 컬럼을 앞에

```sql
-- Good: 카디널리티 높음(email) → 낮음(age)
CREATE INDEX idx_email_age ON users(email, age);

-- Bad: 카디널리티 낮음(age) → 높음(email)
CREATE INDEX idx_age_email ON users(age, email);
```

**왜?**
email은 고유값이 많아 초기 필터링으로 데이터를 크게 줄일 수 있지만, age는 몇 십 가지뿐이라 필터링 효과가 작습니다.

### 커버링 인덱스 (Covering Index)

**정의**: 쿼리에 필요한 모든 컬럼이 인덱스에 포함된 경우

```sql
-- 인덱스: (name, age)
CREATE INDEX idx_name_age ON users(name, age);

-- 커버링 인덱스 활용 (O)
SELECT name, age FROM users WHERE name = 'Alice';
-- 인덱스만으로 name, age를 얻을 수 있어 테이블 접근 불필요

-- 커버링 인덱스 활용 불가 (X)
SELECT name, age, email FROM users WHERE name = 'Alice';
-- email은 인덱스에 없으므로 테이블 접근 필요
```

**왜 빠른가?**

1. 인덱스 스캔만으로 끝 (테이블 접근 0회)
2. 인덱스는 테이블보다 크기가 작아 I/O 적음
3. 캐시 히트율 상승 (인덱스는 자주 사용되어 메모리에 상주)

**실행 계획에서 확인**:
```sql
EXPLAIN SELECT name, age FROM users WHERE name = 'Alice';
-- Extra: Using index (커버링 인덱스 사용)
```

**설계 전략**: 자주 조회되는 컬럼 조합을 인덱스에 포함

```sql
-- 자주 실행되는 쿼리
SELECT id, name, created_at FROM users WHERE status = 'active';

-- 커버링 인덱스 생성
CREATE INDEX idx_status_cover ON users(status, id, name, created_at);
```

### 인덱스 사용 불가 케이스

**1. 함수 사용**

```sql
-- Bad: 인덱스 사용 불가
SELECT * FROM users WHERE YEAR(created_at) = 2024;

-- Good: 인덱스 사용 가능
SELECT * FROM users WHERE created_at >= '2024-01-01' AND created_at < '2025-01-01';
```

**2. 암시적 형변환**

```sql
-- Bad: phone이 VARCHAR인데 숫자로 비교 → 형변환 발생
SELECT * FROM users WHERE phone = 1234567890;

-- Good: 문자열로 비교
SELECT * FROM users WHERE phone = '1234567890';
```

**3. LIKE 앞에 %**

```sql
-- Bad: 인덱스 사용 불가
SELECT * FROM products WHERE name LIKE '%phone%';

-- Good: 인덱스 사용 가능
SELECT * FROM products WHERE name LIKE 'phone%';
```

**4. OR 조건 (컬럼 다름)**

```sql
-- Bad: 인덱스 비효율
SELECT * FROM users WHERE name = 'Alice' OR email = 'alice@example.com';

-- Good: UNION 사용
SELECT * FROM users WHERE name = 'Alice'
UNION
SELECT * FROM users WHERE email = 'alice@example.com';
```

### 인덱스 유지보수

**인덱스 단편화 (Fragmentation)**

INSERT/UPDATE/DELETE 반복 시 인덱스의 물리적 순서가 논리적 순서와 달라져 성능 저하

**해결책**: 주기적 재구성

```sql
-- MySQL
ALTER TABLE users ENGINE=InnoDB;  -- 테이블 재구성

-- PostgreSQL
REINDEX INDEX idx_email;
```

**인덱스 통계 업데이트**

옵티마이저는 통계 정보를 기반으로 실행 계획 수립. 통계가 오래되면 잘못된 계획 선택

```sql
-- MySQL
ANALYZE TABLE users;

-- PostgreSQL
ANALYZE users;
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 조회 성능 대폭 향상 (O(N) → O(log N)) | INSERT/UPDATE/DELETE 성능 저하 (인덱스도 함께 갱신) |
| 정렬(ORDER BY) 비용 절감 | 추가 저장 공간 필요 (테이블의 10-20%) |
| 커버링 인덱스로 테이블 접근 제거 | 인덱스가 많으면 옵티마이저 선택 복잡 |
| WHERE, JOIN 성능 향상 | 잘못 설계된 인덱스는 오히려 성능 저하 |

**결론**:
- OLAP (분석, 조회 많음): 인덱스 적극 활용
- OLTP (트랜잭션, 쓰기 많음): 필수 인덱스만 선별적으로 생성

## 면접 예상 질문

- Q: 인덱스를 많이 만들면 좋은 거 아닌가요?
  - A: 아닙니다. 인덱스는 조회 성능을 높이지만 쓰기 성능을 저하시킵니다. **왜냐하면** INSERT/UPDATE/DELETE 시 테이블뿐만 아니라 모든 인덱스도 함께 갱신해야 하기 때문입니다. 예를 들어, 인덱스가 5개인 테이블에 INSERT하면 1번의 테이블 쓰기 + 5번의 인덱스 쓰기가 발생합니다. **또한** 인덱스는 추가 저장 공간을 차지합니다(테이블 크기의 10-20%). **따라서** 자주 조회되는 컬럼(WHERE, JOIN, ORDER BY에 사용)에만 선별적으로 생성해야 합니다.

- Q: 클러스터드 인덱스와 논클러스터드 인덱스의 차이는 무엇인가요?
  - A: 클러스터드 인덱스는 테이블의 실제 데이터가 인덱스 순서대로 물리적으로 정렬되며, 테이블당 1개만 가능합니다. 논클러스터드 인덱스는 별도 공간에 인덱스를 생성하고 데이터의 위치(포인터)를 저장하며, 여러 개 생성 가능합니다. **왜냐하면** 테이블은 한 가지 순서로만 정렬될 수 있기 때문에 클러스터드는 1개만 가능하고, 논클러스터드는 별도 공간을 사용하므로 여러 개 가능합니다. **성능 차이**: 클러스터드는 데이터에 바로 접근하지만, 논클러스터드는 인덱스 → 데이터의 2단계 검색이 필요합니다.

- Q: 복합 인덱스의 컬럼 순서가 왜 중요한가요?
  - A: 인덱스는 왼쪽 컬럼부터 순차적으로 정렬되기 때문입니다. `INDEX(name, age)`는 name으로 먼저 정렬하고, 같은 name 내에서 age로 정렬합니다. **따라서** `WHERE name = 'Alice'`는 인덱스를 사용하지만, `WHERE age = 30`은 name 없이는 인덱스를 사용할 수 없습니다. **비유**: 사전은 "가나다순 → 같은 단어는 품사순"으로 정렬됩니다. "명사"만으로는 어디서 찾아야 할지 모릅니다. **설계 원칙**: 카디널리티가 높은 컬럼(고유값이 많은)을 앞에 배치해야 초기 필터링으로 데이터를 크게 줄일 수 있습니다.

- Q: 커버링 인덱스가 무엇이고 왜 빠른가요?
  - A: 쿼리에 필요한 모든 컬럼이 인덱스에 포함되어 테이블에 접근하지 않고 인덱스만으로 결과를 반환하는 것입니다. **왜 빠른가?** 인덱스 스캔만으로 끝나므로 디스크 I/O가 절반 이하로 줄어듭니다. **예시**: `INDEX(name, age)`가 있을 때 `SELECT name, age WHERE name='Alice'`는 인덱스만 읽으면 되지만, `SELECT name, age, email WHERE name='Alice'`는 email을 얻기 위해 테이블까지 접근해야 합니다. **설계**: 자주 조회되는 컬럼 조합을 복합 인덱스로 만들되, 인덱스 크기와 쓰기 성능을 고려해야 합니다.

- Q: 인덱스가 있는데도 사용되지 않는 경우는 언제인가요?
  - A: 주요 케이스는 4가지입니다. (1) **함수 사용**: `WHERE YEAR(created_at) = 2024`처럼 컬럼에 함수를 적용하면 모든 행을 변환해야 해서 인덱스 사용 불가 (2) **암시적 형변환**: `WHERE phone = 1234567890` (phone이 VARCHAR일 때) 숫자를 문자로 변환하므로 인덱스 사용 불가 (3) **LIKE 앞 %**: `WHERE name LIKE '%phone%'`는 중간 매칭이라 인덱스 사용 불가 (4) **카디널리티 낮음**: `WHERE gender = 'M'`처럼 고유값이 적으면 옵티마이저가 풀 스캔이 더 빠르다고 판단. **해결**: 함수 제거, 형변환 방지, Full-text 인덱스 사용, 복합 인덱스로 카디널리티 높이기

## 연관 문서

- [MySQL 인덱스](./mysql-index.md): MySQL InnoDB의 구체적인 인덱스 구현
- [SQL](./sql.md): 인덱스를 활용하는 쿼리 작성법
- [Transaction](./transaction.md): 인덱스와 락의 관계

## 참고 자료

- [MySQL 공식 문서 - Optimization and Indexes](https://dev.mysql.com/doc/refman/8.0/en/optimization-indexes.html)
- [Use The Index, Luke - SQL 인덱싱 가이드](https://use-the-index-luke.com/)
- [PostgreSQL 공식 문서 - Indexes](https://www.postgresql.org/docs/current/indexes.html)
