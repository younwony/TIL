# MySQL 인덱스

> 데이터베이스에서 검색 속도를 향상시키기 위한 자료구조로, MySQL InnoDB는 B+Tree 기반 인덱스를 사용한다.

## 핵심 개념

- **B+Tree 구조**: MySQL InnoDB의 기본 인덱스 자료구조로, 리프 노드에만 실제 데이터(또는 PK)가 저장됨
- **클러스터드 인덱스**: 테이블당 하나만 존재하며, 실제 데이터가 인덱스 순서대로 물리적으로 정렬됨 (PK 기준)
- **세컨더리 인덱스**: 클러스터드 인덱스 외의 모든 인덱스로, 리프 노드에 PK 값을 저장
- **커버링 인덱스**: 쿼리에 필요한 모든 컬럼이 인덱스에 포함되어 테이블 접근 없이 처리 가능
- **인덱스 선택도**: 중복도가 낮을수록(Cardinality가 높을수록) 인덱스 효율이 좋음

## 쉽게 이해하기

**인덱스**를 책의 목차와 색인에 비유할 수 있습니다.

### 인덱스 = 책 뒤쪽의 색인(찾아보기)

1000페이지짜리 책에서 "데이터베이스"라는 단어를 찾으려면 어떻게 할까요?

- **인덱스 없이**: 1페이지부터 끝까지 한 장씩 넘기며 찾기 (Full Table Scan)
- **인덱스 있으면**: 책 뒤쪽 색인에서 "데이터베이스 → 42, 156, 289p" 확인 후 바로 이동

### 클러스터드 인덱스 = 사전

사전은 단어가 **가나다순으로 정렬**되어 있습니다. "사과"를 찾으면 ㅅ 섹션으로 바로 가서 찾을 수 있죠. 데이터 자체가 정렬된 순서대로 저장되어 있습니다.

### 세컨더리 인덱스 = 도서관 카드 목록

도서관에서 책을 찾을 때:
1. 카드 목록(세컨더리 인덱스)에서 책 제목 검색 → "청구번호: A-123" 확인
2. 청구번호(PK)로 실제 책장 가서 책 찾기 (한 번 더 조회 필요)

### 복합 인덱스 = 전화번호부

전화번호부는 "성 → 이름" 순서로 정렬되어 있습니다.

- "김" 씨 찾기 → 빠름 (첫 번째 기준)
- "김철수" 찾기 → 빠름 (순서대로 검색)
- "철수" 찾기 → 느림! (이름만으로는 못 찾음, 처음부터 다 봐야 함)

이것이 복합 인덱스에서 **선행 컬럼이 중요한 이유**입니다.

| 비유 | 인덱스 개념 |
|------|------------|
| 책의 색인 | 인덱스 기본 개념 |
| 사전 | 클러스터드 인덱스 (데이터가 정렬됨) |
| 도서관 카드 목록 | 세컨더리 인덱스 (PK로 한 번 더 조회) |
| 전화번호부 | 복합 인덱스 (순서 중요) |

## 상세 설명

### B+Tree 인덱스 구조

```
                    [Root Node]
                   /     |     \
                  /      |      \
         [Branch]    [Branch]    [Branch]
          /   \        /   \        /   \
       [Leaf]-[Leaf]-[Leaf]-[Leaf]-[Leaf]-[Leaf]
         ↓      ↓      ↓      ↓      ↓      ↓
        Data   Data   Data   Data   Data   Data

    * 리프 노드들은 Linked List로 연결됨 (범위 검색에 유리)
```

**B+Tree 특징:**
- 모든 리프 노드가 같은 레벨에 위치 (균형 트리)
- 리프 노드끼리 연결되어 범위 검색에 효율적
- 검색, 삽입, 삭제 모두 O(log N) 시간복잡도

### 클러스터드 인덱스 (Clustered Index)

InnoDB에서 **테이블 = 클러스터드 인덱스**라고 볼 수 있다.

```
클러스터드 인덱스 구조:

    [PK: 1, 2, 3]          <- Root/Branch 노드
         |
    [Leaf 노드]
    ┌─────────────────────────────────────┐
    │ PK=1 │ name='Kim' │ age=25 │ ...   │  <- 실제 행 데이터
    │ PK=2 │ name='Lee' │ age=30 │ ...   │
    │ PK=3 │ name='Park'│ age=28 │ ...   │
    └─────────────────────────────────────┘
```

**클러스터드 인덱스 선정 기준 (우선순위):**
1. PRIMARY KEY가 있으면 PK를 클러스터드 인덱스로 사용
2. PK가 없으면 NOT NULL인 UNIQUE 인덱스 중 첫 번째를 사용
3. 둘 다 없으면 InnoDB가 내부적으로 6바이트 숨겨진 Row ID 생성

### 세컨더리 인덱스 (Secondary Index)

```
세컨더리 인덱스 (name 컬럼):

    [name 인덱스 트리]
         |
    [Leaf 노드]
    ┌─────────────────┐
    │ 'Kim'  → PK=1   │  <- PK 값을 저장
    │ 'Lee'  → PK=2   │
    │ 'Park' → PK=3   │
    └─────────────────┘
           ↓
    PK로 클러스터드 인덱스 다시 조회 (= "테이블 룩업")
```

**세컨더리 인덱스 조회 과정:**
1. 세컨더리 인덱스에서 조건에 맞는 PK 찾기
2. 찾은 PK로 클러스터드 인덱스 조회 (랜덤 I/O 발생)

이 때문에 **인덱스 범위 검색 후 많은 행을 조회하면 오히려 풀 테이블 스캔보다 느릴 수 있다.**

### 복합 인덱스 (Composite Index)

```sql
CREATE INDEX idx_name_age ON users(name, age);
```

```
복합 인덱스 정렬 순서:

    name='Kim', age=25  → PK=1
    name='Kim', age=30  → PK=4
    name='Lee', age=20  → PK=2
    name='Lee', age=35  → PK=3

    * 첫 번째 컬럼(name)으로 먼저 정렬 후, 두 번째 컬럼(age)으로 정렬
```

**복합 인덱스 사용 규칙:**
```sql
-- 인덱스: (name, age, city)

-- O 인덱스 사용
WHERE name = 'Kim'
WHERE name = 'Kim' AND age = 25
WHERE name = 'Kim' AND age = 25 AND city = 'Seoul'
WHERE name = 'Kim' AND age > 20

-- X 인덱스 사용 불가 (선행 컬럼 누락)
WHERE age = 25
WHERE city = 'Seoul'
WHERE age = 25 AND city = 'Seoul'
```

### 커버링 인덱스 (Covering Index)

쿼리에 필요한 모든 컬럼이 인덱스에 포함되어 **테이블 접근 없이** 인덱스만으로 결과 반환.

```sql
-- 인덱스: (name, age)

-- 커버링 인덱스 적용 (테이블 룩업 X)
SELECT name, age FROM users WHERE name = 'Kim';

-- 커버링 인덱스 미적용 (테이블 룩업 O)
SELECT name, age, email FROM users WHERE name = 'Kim';
```

EXPLAIN 결과에서 **Extra: Using index**가 표시되면 커버링 인덱스가 적용된 것.

### 인덱스가 사용되지 않는 경우

```sql
-- 1. 인덱스 컬럼에 함수/연산 적용
WHERE YEAR(created_at) = 2024     -- X
WHERE created_at >= '2024-01-01'  -- O

-- 2. 암시적 형변환
WHERE phone = 01012345678         -- X (숫자로 비교)
WHERE phone = '01012345678'       -- O

-- 3. LIKE 앞부분 와일드카드
WHERE name LIKE '%Kim'            -- X
WHERE name LIKE 'Kim%'            -- O

-- 4. OR 조건 (각 컬럼에 인덱스가 있어야 함)
WHERE name = 'Kim' OR age = 25    -- 상황에 따라 다름

-- 5. NOT, != 연산자
WHERE status != 'DELETED'         -- 대부분 풀스캔

-- 6. IS NULL (Nullable 컬럼)
WHERE deleted_at IS NULL          -- 상황에 따라 다름
```

## 실행 계획 (EXPLAIN)

```sql
EXPLAIN SELECT * FROM users WHERE name = 'Kim';
```

**주요 확인 항목:**

| 컬럼 | 설명 | 좋은 값 |
|------|------|---------|
| type | 접근 방식 | const > eq_ref > ref > range > index > ALL |
| key | 사용된 인덱스 | NULL이면 인덱스 미사용 |
| rows | 예상 검색 행 수 | 작을수록 좋음 |
| Extra | 추가 정보 | Using index (커버링), Using filesort (정렬), Using temporary (임시테이블) |

**type 상세:**
- `const`: PK 또는 UNIQUE로 1건 조회
- `eq_ref`: JOIN에서 PK/UNIQUE로 1건씩 매칭
- `ref`: 인덱스로 여러 건 조회
- `range`: 인덱스 범위 검색
- `index`: 인덱스 풀 스캔
- `ALL`: 테이블 풀 스캔 (최악)

## 예제 코드

```sql
-- 테이블 생성
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_email (email),
    INDEX idx_name_age (name, age)
) ENGINE=InnoDB;

-- 인덱스 확인
SHOW INDEX FROM users;

-- 인덱스 추가
CREATE INDEX idx_created_at ON users(created_at);

-- 인덱스 삭제
DROP INDEX idx_created_at ON users;

-- 실행 계획 확인
EXPLAIN SELECT * FROM users WHERE name = 'Kim' AND age > 20;

-- 인덱스 힌트 사용 (권장하지 않음, 옵티마이저 신뢰)
SELECT * FROM users USE INDEX (idx_name) WHERE name = 'Kim';
SELECT * FROM users FORCE INDEX (idx_name) WHERE name = 'Kim';
SELECT * FROM users IGNORE INDEX (idx_name) WHERE name = 'Kim';
```

## 인덱스 설계 가이드

### 인덱스를 생성해야 하는 경우
- WHERE 절에 자주 사용되는 컬럼
- JOIN 조건에 사용되는 컬럼
- ORDER BY, GROUP BY에 사용되는 컬럼
- Cardinality가 높은 컬럼 (중복이 적은)

### 인덱스를 피해야 하는 경우
- 테이블 데이터가 적은 경우 (수천 건 이하)
- INSERT/UPDATE/DELETE가 빈번한 컬럼
- Cardinality가 낮은 컬럼 (성별, 상태값 등)
- 거의 조회되지 않는 컬럼

### 복합 인덱스 컬럼 순서 결정
1. **동등 조건(=)** 컬럼을 앞에
2. **범위 조건(<, >, BETWEEN)** 컬럼을 뒤에
3. **Cardinality가 높은** 컬럼을 앞에
4. **정렬 기준** 컬럼 고려

```sql
-- WHERE status = 'ACTIVE' AND created_at > '2024-01-01' ORDER BY created_at
-- 좋은 인덱스: (status, created_at)
```

## 면접 예상 질문

- Q: 클러스터드 인덱스와 세컨더리 인덱스의 차이점은?
  - A: 클러스터드 인덱스는 테이블당 하나만 존재하며 리프 노드에 실제 행 데이터가 저장됩니다. 세컨더리 인덱스는 여러 개 생성 가능하고 리프 노드에 PK 값이 저장되어 추가적인 테이블 룩업이 필요합니다.

- Q: 복합 인덱스에서 컬럼 순서가 중요한 이유는?
  - A: 복합 인덱스는 첫 번째 컬럼으로 정렬 후 두 번째 컬럼으로 정렬되므로, 선행 컬럼이 조건에 없으면 인덱스를 효율적으로 사용할 수 없습니다. 또한 동등 조건 컬럼을 앞에, 범위 조건 컬럼을 뒤에 배치해야 인덱스를 최대한 활용할 수 있습니다.

- Q: 커버링 인덱스란 무엇이고 왜 성능이 좋은가요?
  - A: 쿼리에 필요한 모든 컬럼이 인덱스에 포함되어 테이블 데이터 페이지에 접근할 필요 없이 인덱스만으로 결과를 반환하는 것입니다. 디스크 랜덤 I/O를 줄여 성능이 향상됩니다.

## 참고 자료

- [MySQL 8.0 Reference Manual - InnoDB Indexes](https://dev.mysql.com/doc/refman/8.0/en/innodb-indexes.html)
- [MySQL 8.0 Reference Manual - EXPLAIN Output Format](https://dev.mysql.com/doc/refman/8.0/en/explain-output.html)
- Real MySQL 8.0 (위키북스)
