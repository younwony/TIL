# Transaction (트랜잭션)

> 데이터베이스에서 하나의 논리적 작업 단위로 실행되는 일련의 연산들. 모두 성공하거나 모두 실패해야 함 (All or Nothing)

> `[3] 중급` · 선수 지식: [SQL](./sql.md)

`#트랜잭션` `#Transaction` `#ACID` `#원자성` `#Atomicity` `#일관성` `#Consistency` `#격리성` `#Isolation` `#지속성` `#Durability` `#격리수준` `#IsolationLevel` `#ReadUncommitted` `#ReadCommitted` `#RepeatableRead` `#Serializable` `#락` `#Lock` `#SharedLock` `#ExclusiveLock` `#MVCC` `#커밋` `#Commit` `#롤백` `#Rollback` `#동시성제어` `#ConcurrencyControl` `#데드락` `#Deadlock`

## 왜 알아야 하는가?

트랜잭션은 데이터 무결성을 보장하는 핵심 메커니즘입니다. 은행 송금, 재고 차감, 결제 처리 등 실무의 거의 모든 비즈니스 로직에서 트랜잭션이 필요합니다. ACID 원칙, 격리 수준, 락, MVCC를 이해하지 못하면 데이터 불일치, 동시성 문제, 데드락 등 심각한 버그가 발생합니다. 특히 격리 수준에 따라 발생하는 이상 현상(Dirty Read, Non-Repeatable Read, Phantom Read)과 이를 방지하는 방법을 알아야 안정적인 시스템을 개발할 수 있습니다. 백엔드 개발자라면 트랜잭션의 동작 원리를 정확히 이해해야 합니다.

## 핵심 개념

- **ACID**: 트랜잭션이 보장해야 할 4가지 속성 (원자성, 일관성, 격리성, 지속성)
- **격리 수준 (Isolation Level)**: 동시 실행되는 트랜잭션 간의 격리 정도
- **락 (Lock)**: 동시성 제어를 위해 데이터에 대한 접근을 제한하는 메커니즘
- **MVCC (Multi-Version Concurrency Control)**: 락 없이 동시성을 제어하는 기법
- **데드락 (Deadlock)**: 두 개 이상의 트랜잭션이 서로 상대방의 락을 기다리며 무한 대기하는 상태

## 쉽게 이해하기

**트랜잭션**을 은행 송금에 비유할 수 있습니다.

A가 B에게 100만원을 송금할 때, 두 가지 작업이 일어납니다:
1. A 계좌에서 100만원 출금
2. B 계좌에 100만원 입금

이 두 작업은 **반드시 둘 다 성공하거나, 둘 다 취소**되어야 합니다.

**만약 트랜잭션이 없다면?**
- 출금만 성공하고 입금 실패 → 100만원이 공중에서 사라짐
- 출금 실패하고 입금 성공 → 100만원이 무에서 생김

**트랜잭션이 있으면?**
송금 중 시스템 장애가 발생해도:
- 출금과 입금 중 하나라도 실패하면 → 둘 다 취소되어 원래대로 복구 (Rollback)
- 둘 다 성공하면 → 변경사항 확정 (Commit)

**격리 수준**은 화장실 칸막이와 같습니다:
- Read Uncommitted: 칸막이 없음 (옆 사람이 하는 일 다 보임)
- Read Committed: 낮은 칸막이 (발은 보임)
- Repeatable Read: 높은 칸막이 (안 보임)
- Serializable: 한 번에 한 명만 사용 (가장 안전하지만 느림)

## 상세 설명

### ACID 속성

#### 1. Atomicity (원자성)

**정의**: 트랜잭션의 모든 연산이 완전히 수행되거나, 전혀 수행되지 않아야 함

```sql
START TRANSACTION;
UPDATE accounts SET balance = balance - 100000 WHERE id = 1;  -- A 출금
UPDATE accounts SET balance = balance + 100000 WHERE id = 2;  -- B 입금
COMMIT;  -- 둘 다 성공 시 확정

-- 만약 중간에 오류 발생 시
ROLLBACK;  -- 모든 변경사항 취소
```

**왜 필요한가?**

부분 실패를 허용하면 데이터 불일치가 발생합니다. **예를 들어**, 출금만 성공하고 입금이 실패하면 돈이 사라지는 심각한 문제가 발생합니다.

**구현 방법**: Write-Ahead Logging (WAL)
- 변경사항을 먼저 로그에 기록
- 장애 발생 시 로그를 이용해 복구(Redo) 또는 취소(Undo)

#### 2. Consistency (일관성)

**정의**: 트랜잭션 실행 전후로 데이터베이스가 일관된 상태를 유지해야 함

```sql
-- 제약 조건: balance >= 0 (음수 잔액 불가)

START TRANSACTION;
UPDATE accounts SET balance = balance - 200000 WHERE id = 1;
-- 만약 id=1의 잔액이 10만원이라면?
-- 제약 조건 위반 → 트랜잭션 롤백
ROLLBACK;
```

**왜 필요한가?**

비즈니스 규칙과 제약 조건을 위반하면 데이터 무결성이 깨집니다. **예를 들어**, 잔액이 음수가 되면 실제 세계의 논리와 맞지 않습니다.

**보장 방법**:
- CHECK 제약 조건
- Foreign Key 제약 조건
- Trigger를 통한 비즈니스 로직 검증

#### 3. Isolation (격리성)

**정의**: 동시 실행되는 트랜잭션들이 서로 영향을 주지 않아야 함

```sql
-- Transaction A
START TRANSACTION;
SELECT balance FROM accounts WHERE id = 1;  -- 100만원 읽음
-- (이 시점에 Transaction B가 개입)
UPDATE accounts SET balance = balance - 50000 WHERE id = 1;
COMMIT;

-- Transaction B (동시 실행)
START TRANSACTION;
UPDATE accounts SET balance = balance + 20000 WHERE id = 1;
COMMIT;
```

**왜 필요한가?**

격리가 없으면 **Race Condition**이 발생합니다:
- A가 100만원을 읽음
- B가 100만원을 읽고 +20만원 → 120만원으로 업데이트
- A가 100만원에서 -50만원 → 50만원으로 업데이트
- **결과**: 120만원이어야 하는데 50만원 (B의 +20만원이 사라짐)

**해결 방법**: 격리 수준 설정 + 락 메커니즘

#### 4. Durability (지속성)

**정의**: 커밋된 트랜잭션의 결과는 시스템 장애가 발생해도 영구 보존되어야 함

```sql
START TRANSACTION;
UPDATE accounts SET balance = balance + 100000 WHERE id = 1;
COMMIT;  -- 이 시점 이후 정전이 발생해도 변경사항은 보존됨
```

**왜 필요한가?**

커밋 후 데이터가 사라지면 사용자는 시스템을 신뢰할 수 없습니다.

**구현 방법**:
- Write-Ahead Logging: 커밋 전에 로그를 디스크에 기록
- Checkpoint: 주기적으로 메모리의 변경사항을 디스크에 반영
- RAID: 디스크 이중화

### 격리 수준 (Isolation Level)

격리 수준이 높을수록 동시성은 낮아지고(성능 저하), 낮을수록 동시성은 높아지지만 이상 현상이 발생할 수 있습니다.

#### 1. Read Uncommitted (레벨 0)

**특징**: 커밋되지 않은 데이터도 읽을 수 있음

```sql
-- Transaction A
START TRANSACTION;
UPDATE accounts SET balance = 200000 WHERE id = 1;
-- (아직 커밋 안 함)

-- Transaction B
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
START TRANSACTION;
SELECT balance FROM accounts WHERE id = 1;  -- 200000 읽음 (Dirty Read)
COMMIT;

-- Transaction A
ROLLBACK;  -- 200000 취소! 실제로는 100000
```

**문제: Dirty Read (오손 읽기)**

B가 읽은 200000은 A가 롤백되면서 사라짐. B는 존재하지 않는 데이터를 본 것입니다.

**사용처**: 거의 사용 안 함 (데이터 정합성 보장 불가)

#### 2. Read Committed (레벨 1) - PostgreSQL, Oracle 기본값

**특징**: 커밋된 데이터만 읽을 수 있음

```sql
-- Transaction A
START TRANSACTION;
UPDATE accounts SET balance = 200000 WHERE id = 1;
-- (아직 커밋 안 함)

-- Transaction B
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
START TRANSACTION;
SELECT balance FROM accounts WHERE id = 1;  -- 100000 읽음 (커밋된 값)
-- (이 시점에 A가 커밋)
SELECT balance FROM accounts WHERE id = 1;  -- 200000 읽음 (새로 커밋된 값)
COMMIT;
```

**문제: Non-Repeatable Read (반복 불가능 읽기)**

같은 트랜잭션 내에서 같은 쿼리를 두 번 실행했는데 결과가 다름 (100000 → 200000)

**사용처**: 일반적인 웹 애플리케이션 (대부분 괜찮음)

#### 3. Repeatable Read (레벨 2) - MySQL InnoDB 기본값

**특징**: 트랜잭션 시작 시점의 스냅샷을 읽음 (같은 쿼리는 항상 같은 결과)

```sql
-- Transaction A
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;
SELECT balance FROM accounts WHERE id = 1;  -- 100000 읽음

-- Transaction B
START TRANSACTION;
UPDATE accounts SET balance = 200000 WHERE id = 1;
COMMIT;  -- 커밋함

-- Transaction A (계속)
SELECT balance FROM accounts WHERE id = 1;  -- 여전히 100000 읽음 (스냅샷)
COMMIT;
```

**왜 같은 값을 읽는가?**

MVCC (Multi-Version Concurrency Control)로 트랜잭션 시작 시점의 스냅샷을 유지하기 때문입니다.

**문제: Phantom Read (유령 읽기)**

```sql
-- Transaction A
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
START TRANSACTION;
SELECT COUNT(*) FROM accounts WHERE balance > 100000;  -- 5건

-- Transaction B
START TRANSACTION;
INSERT INTO accounts (id, balance) VALUES (10, 150000);
COMMIT;

-- Transaction A (계속)
SELECT COUNT(*) FROM accounts WHERE balance > 100000;  -- MySQL: 5건 (Phantom Read 방지됨)
                                                        -- PostgreSQL: 5건 (방지됨)
COMMIT;
```

**MySQL InnoDB의 특징**: Gap Lock으로 Phantom Read도 방지 (사실상 Serializable에 가까움)

**사용처**: 금융, 재고 관리 등 정합성이 중요한 시스템

#### 4. Serializable (레벨 3)

**특징**: 완전한 격리. 트랜잭션을 순차적으로 실행한 것과 동일한 결과 보장

```sql
-- Transaction A
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
START TRANSACTION;
SELECT * FROM accounts WHERE balance > 100000;
-- 이 범위에 락이 걸림

-- Transaction B
START TRANSACTION;
INSERT INTO accounts (id, balance) VALUES (10, 150000);
-- A가 커밋될 때까지 대기
COMMIT;
```

**왜 느린가?**

읽기 작업도 락을 걸어 다른 트랜잭션의 쓰기를 차단합니다. 동시성이 크게 저하됩니다.

**사용처**: 극도로 높은 정합성이 필요한 경우 (거의 사용 안 함)

**격리 수준별 이상 현상 정리**

| 격리 수준 | Dirty Read | Non-Repeatable Read | Phantom Read |
|----------|-----------|-------------------|-------------|
| Read Uncommitted | O | O | O |
| Read Committed | X | O | O |
| Repeatable Read | X | X | O (MySQL은 X) |
| Serializable | X | X | X |

### 락 (Lock)

#### 공유락 (Shared Lock, S-Lock)

**정의**: 읽기 락. 다른 트랜잭션도 읽기 가능하지만 쓰기는 불가

```sql
-- Transaction A
START TRANSACTION;
SELECT * FROM accounts WHERE id = 1 LOCK IN SHARE MODE;  -- 공유락 획득
-- 다른 트랜잭션도 읽기 가능

-- Transaction B
SELECT * FROM accounts WHERE id = 1;  -- 가능
UPDATE accounts SET balance = 200000 WHERE id = 1;  -- 대기 (A가 커밋할 때까지)
```

**왜 필요한가?**

읽는 동안 데이터가 변경되지 않도록 보장하기 위해.

#### 배타락 (Exclusive Lock, X-Lock)

**정의**: 쓰기 락. 다른 트랜잭션의 읽기/쓰기 모두 불가

```sql
-- Transaction A
START TRANSACTION;
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;  -- 배타락 획득

-- Transaction B
SELECT * FROM accounts WHERE id = 1;  -- 대기 (격리 수준에 따라 다름)
UPDATE accounts SET balance = 200000 WHERE id = 1;  -- 대기
```

**언제 자동으로 걸리나?**

INSERT, UPDATE, DELETE는 자동으로 배타락을 획득합니다.

#### 락 호환성

|  | 공유락 (S) | 배타락 (X) |
|---|----------|----------|
| **공유락 (S)** | 호환 | 불가 |
| **배타락 (X)** | 불가 | 불가 |

**읽기-읽기**: 동시 가능
**읽기-쓰기**: 불가
**쓰기-쓰기**: 불가

#### 데드락 (Deadlock)

**발생 상황**: 두 트랜잭션이 서로의 락을 기다리며 무한 대기

```sql
-- Transaction A
START TRANSACTION;
UPDATE accounts SET balance = balance - 10000 WHERE id = 1;  -- id=1 락 획득
-- (잠시 대기)
UPDATE accounts SET balance = balance + 10000 WHERE id = 2;  -- id=2 락 대기

-- Transaction B (동시 실행)
START TRANSACTION;
UPDATE accounts SET balance = balance - 10000 WHERE id = 2;  -- id=2 락 획득
-- (잠시 대기)
UPDATE accounts SET balance = balance + 10000 WHERE id = 1;  -- id=1 락 대기

-- 결과: A는 B의 id=2 락을 기다리고, B는 A의 id=1 락을 기다림 → 데드락
```

**DB의 해결 방법**:
1. 데드락 감지 (일정 시간 후 확인)
2. 한 트랜잭션을 강제 롤백 (희생양 선택)
3. 애플리케이션에 오류 반환

**예방 방법**:
- 트랜잭션을 짧게 유지
- 락 획득 순서를 일관되게 (항상 id 오름차순으로 락 획득)
- 타임아웃 설정

```sql
-- Good: 락 획득 순서 일관성
-- 항상 id가 작은 것부터 락 획득
UPDATE accounts SET balance = balance - 10000 WHERE id = LEAST(1, 2);
UPDATE accounts SET balance = balance + 10000 WHERE id = GREATEST(1, 2);
```

### MVCC (Multi-Version Concurrency Control)

**정의**: 데이터의 여러 버전을 유지하여 락 없이 읽기 작업을 처리

**동작 원리**:

```
트랜잭션 100 시작
트랜잭션 101 시작

[데이터 버전]
balance = 10만원 (트랜잭션 ID: 99)

트랜잭션 100: UPDATE balance = 20만원 커밋
→ balance = 20만원 (트랜잭션 ID: 100) 생성
→ 이전 버전 (10만원, ID: 99)은 유지

트랜잭션 101: SELECT balance
→ 트랜잭션 101 시작 시점의 스냅샷 조회
→ 트랜잭션 ID 99 버전 읽음 → 10만원 반환
```

**왜 MVCC를 사용하는가?**

- **읽기는 쓰기를 차단하지 않음**: 읽기 작업이 락을 걸지 않으므로 동시성 향상
- **쓰기는 읽기를 차단하지 않음**: 쓰기 중에도 이전 버전을 읽을 수 있음
- **일관된 읽기**: 트랜잭션 시작 시점의 스냅샷을 보장

**단점**:
- 메모리 사용량 증가 (여러 버전 저장)
- Undo 로그 관리 오버헤드
- 주기적인 정리(Purge) 필요

**사용 DB**: MySQL InnoDB, PostgreSQL, Oracle

### 트랜잭션 로그

#### Undo Log

**목적**: 롤백 시 이전 상태로 복구

```
트랜잭션 시작
UPDATE accounts SET balance = 20만원 WHERE id = 1;
→ Undo Log: id=1의 이전 값 10만원 저장

ROLLBACK 시:
→ Undo Log를 읽어 10만원으로 복구
```

**추가 역할**: MVCC에서 이전 버전 제공

#### Redo Log

**목적**: 장애 복구 시 커밋된 트랜잭션 재실행

```
트랜잭션 시작
UPDATE accounts SET balance = 20만원 WHERE id = 1;
→ Redo Log에 기록: "id=1을 20만원으로 변경"
COMMIT
→ Redo Log를 디스크에 강제 기록 (fsync)
→ 실제 데이터 페이지는 나중에 기록 (성능 향상)

시스템 장애 발생 후 재시작:
→ Redo Log를 읽어 커밋된 변경사항 재실행
```

**왜 Redo Log가 필요한가?**

성능을 위해 데이터를 즉시 디스크에 쓰지 않고 메모리(버퍼 풀)에 유지합니다. 커밋 시점에 모든 데이터를 디스크에 쓰면 너무 느리므로, **작은 Redo Log만 디스크에 쓰고** 실제 데이터는 나중에 씁니다.

## 트레이드오프

### 격리 수준

| 격리 수준 | 동시성 | 정합성 | 사용처 |
|----------|--------|--------|--------|
| Read Uncommitted | 높음 | 매우 낮음 | 거의 사용 안 함 |
| Read Committed | 높음 | 중간 | 일반 웹 애플리케이션 |
| Repeatable Read | 중간 | 높음 | 금융, 재고 관리 |
| Serializable | 낮음 | 매우 높음 | 극도로 중요한 경우 |

### 락 vs MVCC

| 항목 | 락 (Lock) | MVCC |
|------|---------|------|
| 읽기 성능 | 낮음 (락 대기) | 높음 (락 없음) |
| 쓰기 성능 | 중간 | 중간 |
| 메모리 사용 | 적음 | 많음 (버전 관리) |
| 구현 복잡도 | 낮음 | 높음 |

## 면접 예상 질문

- Q: ACID가 무엇인가요?
  - A: 트랜잭션이 보장해야 할 4가지 속성입니다. (1) **원자성(Atomicity)**: 모두 성공하거나 모두 실패. 부분 실패를 허용하면 데이터 불일치 발생. (2) **일관성(Consistency)**: 제약 조건을 만족하는 일관된 상태 유지. 비즈니스 규칙 위반 방지. (3) **격리성(Isolation)**: 동시 실행 트랜잭션 간 간섭 방지. Race Condition 방지. (4) **지속성(Durability)**: 커밋된 변경은 영구 보존. 장애 발생해도 복구 가능. **왜냐하면** 이 4가지를 보장해야 데이터베이스를 신뢰할 수 있기 때문입니다.

- Q: Read Committed와 Repeatable Read의 차이는 무엇인가요?
  - A: Read Committed는 커밋된 데이터만 읽지만, 같은 트랜잭션 내에서 같은 쿼리를 두 번 실행하면 다른 결과가 나올 수 있습니다(Non-Repeatable Read). **왜냐하면** 첫 번째 읽기 후 다른 트랜잭션이 데이터를 변경하고 커밋하면, 두 번째 읽기에서 새로 커밋된 값을 읽기 때문입니다. Repeatable Read는 트랜잭션 시작 시점의 스냅샷을 읽으므로 같은 쿼리는 항상 같은 결과를 반환합니다. **MySQL InnoDB의 경우** MVCC와 Gap Lock으로 Phantom Read까지 방지하여 사실상 Serializable에 가깝습니다. **트레이드오프**: Read Committed가 더 높은 동시성, Repeatable Read가 더 높은 정합성.

- Q: 데드락은 왜 발생하고 어떻게 해결하나요?
  - A: 두 개 이상의 트랜잭션이 서로 상대방이 보유한 락을 기다리며 무한 대기하는 상태입니다. **예시**: 트랜잭션 A가 레코드 1을 락하고 레코드 2를 기다림. 트랜잭션 B가 레코드 2를 락하고 레코드 1을 기다림 → 데드락. **발생 원인**: 락 획득 순서가 일관되지 않거나, 트랜잭션이 너무 길 때. **DB의 해결**: 데드락을 감지하고 한 트랜잭션을 강제 롤백. **예방 방법**: (1) 락 획득 순서를 일관되게 (항상 id 오름차순) (2) 트랜잭션을 짧게 유지 (3) 타임아웃 설정 (4) 낙관적 락 사용 고려.

- Q: MVCC가 무엇이고 왜 사용하나요?
  - A: Multi-Version Concurrency Control로, 데이터의 여러 버전을 유지하여 락 없이 읽기 작업을 처리하는 기법입니다. **동작 원리**: 트랜잭션이 시작되면 그 시점의 스냅샷을 유지하고, 쓰기 작업 시 새 버전을 생성하되 이전 버전도 보존합니다. **왜 사용하는가?** (1) 읽기가 쓰기를 차단하지 않음 → 동시성 향상 (2) 쓰기가 읽기를 차단하지 않음 → 일관된 읽기 보장 (3) Repeatable Read 격리 수준을 효율적으로 구현. **단점**: 메모리 사용량 증가 (여러 버전 저장), Undo 로그 관리 오버헤드. **사용 DB**: MySQL InnoDB, PostgreSQL, Oracle.

- Q: 트랜잭션에서 SELECT ... FOR UPDATE는 언제 사용하나요?
  - A: 조회한 데이터를 곧바로 수정할 예정이고, 조회 시점과 수정 시점 사이에 다른 트랜잭션의 변경을 막아야 할 때 사용합니다. **왜냐하면** 일반 SELECT는 공유락도 걸지 않아(MVCC) 조회 후 다른 트랜잭션이 데이터를 변경할 수 있기 때문입니다. **예시**: 재고 차감 시 `SELECT stock FROM products WHERE id=1 FOR UPDATE`로 배타락을 걸어 다른 트랜잭션의 접근을 막고, 안전하게 재고를 차감합니다. **주의**: 락을 오래 보유하면 동시성 저하 및 데드락 위험 증가. **대안**: 낙관적 락(버전 컬럼) 고려.

## 연관 문서

- [SQL](./sql.md): 트랜잭션 내에서의 SQL 실행
- [Index](./index.md): 트랜잭션과 인덱스 락
- [MySQL Index](./mysql-index.md): InnoDB의 락 메커니즘
- [JPA](./jpa.md): JPA에서의 트랜잭션 관리
- [NoSQL](./nosql.md): NoSQL의 트랜잭션과 CAP 정리

## 참고 자료

- [MySQL 공식 문서 - InnoDB Locking](https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html)
- [PostgreSQL 공식 문서 - Transaction Isolation](https://www.postgresql.org/docs/current/transaction-iso.html)
- [Database Internals - Alex Petrov](https://www.databass.dev/)
