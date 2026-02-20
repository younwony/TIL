# 데드락 (Deadlock)

> `[3] 중급` · 선수 지식: [프로세스와 스레드](./process-vs-thread.md), [동기화](./synchronization.md)

> 두 개 이상의 프로세스/스레드가 서로가 점유한 자원을 기다리며 무한히 대기하는 교착 상태

`#데드락` `#Deadlock` `#교착상태` `#상호배제` `#MutualExclusion` `#점유대기` `#HoldAndWait` `#비선점` `#NoPreemption` `#순환대기` `#CircularWait` `#기아` `#Starvation` `#라이브락` `#Livelock` `#락순서` `#LockOrdering` `#BankersAlgorithm` `#은행원알고리즘` `#자원할당그래프` `#ResourceAllocationGraph` `#tryLock` `#타임아웃` `#Timeout` `#락경쟁` `#LockContention` `#ReentrantLock` `#synchronized` `#DB데드락` `#분산락` `#DistributedLock`

## 왜 알아야 하는가?

데드락은 멀티스레드 프로그래밍과 분산 시스템에서 발생하는 가장 까다로운 동시성 문제 중 하나입니다. 한 번 발생하면 시스템이 완전히 멈추며, 재현이 어렵고 디버깅이 극도로 어렵습니다. DB 트랜잭션, 분산 락, 멀티스레드 서버 등 실무 곳곳에서 만날 수 있습니다.

- **실무**: DB 트랜잭션 데드락, 멀티스레드 서버 교착, 분산 락 교착 해결
- **면접**: 데드락 4가지 조건, 해결 전략은 CS 면접 단골 질문
- **기반 지식**: 동기화, 트랜잭션 격리 수준, 분산 시스템 이해의 기초

## 핵심 개념

- **데드락 발생 4가지 조건**: 상호 배제, 점유 대기, 비선점, 순환 대기 (모두 충족 시 발생)
- **예방 (Prevention)**: 4가지 조건 중 하나를 원천 차단
- **회피 (Avoidance)**: 자원 할당 전 안전 상태 여부 검사 (Banker's Algorithm)
- **탐지/복구 (Detection/Recovery)**: 발생 후 감지하여 처리
- **무시 (Ostrich Algorithm)**: 발생 확률이 낮으면 무시 (대부분의 OS)

## 쉽게 이해하기

**데드락**을 도로 교차로에 비유할 수 있습니다.

### 교차로 교착 상태

사거리에서 4대의 차가 동시에 진입하면:
- A차는 B차가 비켜야 갈 수 있고
- B차는 C차가 비켜야 갈 수 있고
- C차는 D차가 비켜야 갈 수 있고
- D차는 A차가 비켜야 갈 수 있음

→ 아무도 움직이지 못하는 **교착 상태**

```
        ┌───┐
        │ C │ → 대기
        └───┘
          ↑
┌───┐           ┌───┐
│ D │ → 대기 ←  │ B │
└───┘           └───┘
          ↓
        ┌───┐
        │ A │ → 대기
        └───┘
```

### 해결 방법 비유

| 방법 | 비유 | 설명 |
|------|------|------|
| **예방** | 교통 신호등 설치 | 순환 대기 자체를 불가능하게 |
| **회피** | 교차로 진입 전 확인 | 교착 가능성 있으면 진입 거부 |
| **탐지/복구** | 교통 경찰이 와서 한 대 후진시킴 | 교착 발생 후 처리 |
| **무시** | "별로 안 일어나니까 그냥 둬" | 비용 대비 효과가 낮으면 무시 |

## 상세 설명

### 데드락 발생 조건 (Coffman Conditions)

4가지 조건이 **모두 동시에** 성립해야 데드락이 발생합니다.

| 조건 | 설명 | 예시 |
|------|------|------|
| **상호 배제 (Mutual Exclusion)** | 자원을 한 번에 하나의 프로세스만 사용 | 프린터, 락 |
| **점유 대기 (Hold and Wait)** | 자원을 점유한 채로 다른 자원 대기 | 락 A 가진 채로 락 B 요청 |
| **비선점 (No Preemption)** | 점유한 자원을 강제로 뺏을 수 없음 | 스레드 A의 락을 B가 못 뺏음 |
| **순환 대기 (Circular Wait)** | 프로세스들이 원형으로 자원 대기 | A→B→C→A |

**왜 4가지 모두 필요한가?**

하나라도 성립하지 않으면 데드락이 발생하지 않습니다. 이것이 **예방** 전략의 핵심입니다.

### 자원 할당 그래프 (Resource Allocation Graph)

```
프로세스 → 자원: "요청" (대기 중)
자원 → 프로세스: "할당됨"

  P1 ──요청──→ R2
  ↑              │
  할당           할당
  │              ↓
  R1 ←──요청── P2

사이클 존재 → 데드락!
```

**사이클 판별법**:
- 자원 인스턴스가 1개인 경우: 사이클 = 데드락
- 자원 인스턴스가 여러 개인 경우: 사이클 ≠ 반드시 데드락 (추가 분석 필요)

### 데드락 예방 (Prevention)

4가지 조건 중 하나를 제거합니다.

#### 1. 상호 배제 제거

```java
// 공유 가능한 자원은 상호 배제 불필요
// 예: 읽기 전용 파일은 여러 프로세스가 동시 접근 가능
ReadWriteLock rwLock = new ReentrantReadWriteLock();

// 읽기는 동시 접근 허용
rwLock.readLock().lock();
// ... 읽기 작업 ...
rwLock.readLock().unlock();
```

**한계**: 대부분의 자원은 상호 배제가 필수 (프린터, 쓰기 작업 등)

#### 2. 점유 대기 제거

```java
// Bad: 점유 대기 가능 (데드락 위험)
synchronized (lockA) {
    // lockA 점유한 상태에서 lockB 대기
    synchronized (lockB) {
        // 작업
    }
}

// Good: 모든 자원을 한 번에 요청
boolean acquired = tryAcquireAll(lockA, lockB);
if (acquired) {
    try {
        // 작업
    } finally {
        releaseAll(lockA, lockB);
    }
}
```

**한계**: 자원 활용률 저하 (필요 없는 자원까지 미리 확보)

#### 3. 비선점 허용

```java
// tryLock으로 실패 시 기존 자원 반납 (선점 효과)
ReentrantLock lockA = new ReentrantLock();
ReentrantLock lockB = new ReentrantLock();

while (true) {
    if (lockA.tryLock()) {
        try {
            if (lockB.tryLock()) {
                try {
                    // 두 락 모두 획득 성공 → 작업 수행
                    return;
                } finally {
                    lockB.unlock();
                }
            }
        } finally {
            lockA.unlock(); // lockB 실패 시 lockA도 반납
        }
    }
    // 잠시 대기 후 재시도 (라이브락 방지)
    Thread.sleep(ThreadLocalRandom.current().nextInt(10));
}
```

**한계**: 재시도 로직 복잡, 라이브락 가능성

#### 4. 순환 대기 제거 (가장 실용적)

```java
// Bad: 스레드마다 다른 순서로 락 획득 → 순환 대기 가능
// Thread 1: lock(A) → lock(B)
// Thread 2: lock(B) → lock(A)

// Good: 락 순서 고정 (항상 A → B 순서)
// Thread 1: lock(A) → lock(B)
// Thread 2: lock(A) → lock(B)  ← 순서 통일!
```

**왜 가장 실용적인가?**
- 구현이 간단: 락에 번호를 부여하고 번호 순서대로 획득
- 런타임 오버헤드 없음: 코드 작성 시 규칙만 지키면 됨
- 정적 분석 가능: 코드 리뷰로 검증 가능

```java
// 락 순서를 보장하는 유틸리티
public class OrderedLock {
    // 락의 고유 ID로 순서 보장
    public static void lockInOrder(ReentrantLock lock1, ReentrantLock lock2) {
        int id1 = System.identityHashCode(lock1);
        int id2 = System.identityHashCode(lock2);

        if (id1 < id2) {
            lock1.lock();
            lock2.lock();
        } else if (id1 > id2) {
            lock2.lock();
            lock1.lock();
        } else {
            // 해시 충돌 시 tie-breaking 락 사용
            lock1.lock();
            lock2.lock();
        }
    }
}
```

### 데드락 회피 (Avoidance)

#### Banker's Algorithm (은행원 알고리즘)

자원 할당 전에 **안전 상태**인지 검사합니다.

```
5개의 자원이 있고, 3개의 프로세스가 경쟁:

프로세스   최대 요구   현재 할당   남은 필요
P0          10          5           5
P1           4          2           2
P2           9          2           7

현재 가용 자원: 3

안전 순서 존재 여부 확인:
1. P1 실행 가능 (필요 2 ≤ 가용 3) → 완료 후 가용 = 3+2 = 5
2. P0 실행 가능 (필요 5 ≤ 가용 5) → 완료 후 가용 = 5+5 = 10
3. P2 실행 가능 (필요 7 ≤ 가용 10) → 완료 후 가용 = 10+2 = 12

안전 순서: <P1, P0, P2> → 할당 승인!
```

**왜 실무에서 잘 안 쓰는가?**
- 프로세스가 최대 자원 요구량을 미리 알아야 함 (현실적으로 어려움)
- 매번 안전 상태 검사 비용이 큼
- 자원 종류가 많으면 계산 복잡도 증가

### 데드락 탐지/복구 (Detection/Recovery)

데드락 발생을 허용하되, 탐지하여 복구합니다.

**탐지 방법**: 자원 할당 그래프에서 사이클 탐색

**복구 방법**:

| 방법 | 설명 | 장단점 |
|------|------|--------|
| **프로세스 종료** | 데드락 프로세스 중 하나를 강제 종료 | 간단하지만 작업 손실 |
| **자원 선점** | 프로세스에서 자원을 강제 회수 | 롤백 필요 |
| **체크포인트/롤백** | 이전 상태로 되돌리기 | 안전하지만 복잡 |

### 라이브락 (Livelock)

데드락과 유사하지만, 프로세스가 상태를 계속 변경하면서도 진전이 없는 상태입니다.

```
좁은 복도에서 두 사람이 마주침:
- A: "왼쪽으로 비켜야지" → 왼쪽 이동
- B: "왼쪽으로 비켜야지" → 왼쪽 이동 (같은 방향!)
- A: "오른쪽으로 비켜야지" → 오른쪽 이동
- B: "오른쪽으로 비켜야지" → 오른쪽 이동 (또 같은 방향!)
→ 계속 움직이지만 진전이 없음
```

```java
// 라이브락 예시: 서로 양보하다가 진전 없음
while (true) {
    if (lockA.tryLock()) {
        if (lockB.tryLock()) {
            // 성공
            break;
        }
        lockA.unlock(); // 양보
    }
    // 두 스레드가 동시에 같은 패턴으로 양보 → 라이브락
}

// 해결: 랜덤 대기 시간 추가
while (true) {
    if (lockA.tryLock()) {
        if (lockB.tryLock()) {
            break;
        }
        lockA.unlock();
    }
    // 랜덤 대기로 동시성 깨뜨림
    Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10));
}
```

## 트러블슈팅

### 사례 1: Java 멀티스레드 데드락

#### 증상

```
애플리케이션이 응답 없음 (hang)
특정 API 호출이 무한 대기
CPU 사용률은 낮은데 스레드가 BLOCKED 상태
```

#### 원인 분석

```bash
# Java 스레드 덤프로 데드락 확인
jstack <PID>

# 출력 예시:
# Found one Java-level deadlock:
# "Thread-1":
#   waiting to lock monitor 0x00007f... (object 0x000000076...)
#   which is held by "Thread-2"
# "Thread-2":
#   waiting to lock monitor 0x00007f... (object 0x000000076...)
#   which is held by "Thread-1"
```

#### 해결 방법

```java
// Bad: 데드락 발생 코드
class TransferService {
    void transfer(Account from, Account to, int amount) {
        synchronized (from) {        // 락 순서가 호출마다 다름!
            synchronized (to) {
                from.debit(amount);
                to.credit(amount);
            }
        }
    }
}

// Good: 락 순서 고정으로 데드락 방지
class TransferService {
    void transfer(Account from, Account to, int amount) {
        Account first = from.getId() < to.getId() ? from : to;
        Account second = from.getId() < to.getId() ? to : from;

        synchronized (first) {       // 항상 ID가 작은 계좌 먼저 락
            synchronized (second) {
                from.debit(amount);
                to.credit(amount);
            }
        }
    }
}
```

#### 예방 조치

- `jstack`으로 주기적 스레드 덤프 수집
- 타임아웃 있는 `tryLock()` 사용
- 락 순서 규칙을 코드 리뷰에서 검증

### 사례 2: DB 트랜잭션 데드락

#### 증상

```
com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException:
Deadlock found when trying to get lock; try restarting transaction
```

#### 원인 분석

```sql
-- 트랜잭션 A
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;  -- Row 1 락
UPDATE accounts SET balance = balance + 100 WHERE id = 2;  -- Row 2 대기

-- 트랜잭션 B (동시 실행)
BEGIN;
UPDATE accounts SET balance = balance - 50 WHERE id = 2;   -- Row 2 락
UPDATE accounts SET balance = balance + 50 WHERE id = 1;   -- Row 1 대기 → 데드락!
```

#### 해결 방법

```sql
-- Good: 항상 id 순서대로 업데이트
-- 트랜잭션 A
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

-- 트랜잭션 B (같은 순서!)
BEGIN;
UPDATE accounts SET balance = balance + 50 WHERE id = 1;
UPDATE accounts SET balance = balance - 50 WHERE id = 2;
```

```java
// Spring에서 재시도 패턴
@Retryable(
    value = DeadlockLoserDataAccessException.class,
    maxAttempts = 3,
    backoff = @Backoff(delay = 100, multiplier = 2)
)
@Transactional
public void transfer(Long fromId, Long toId, int amount) {
    // ID 순서대로 락 획득
    Long firstId = Math.min(fromId, toId);
    Long secondId = Math.max(fromId, toId);

    Account first = accountRepository.findByIdForUpdate(firstId);
    Account second = accountRepository.findByIdForUpdate(secondId);

    // 이체 로직
}
```

#### 예방 조치

- DB 테이블 접근 순서를 팀 내에서 통일
- 트랜잭션 범위를 최소화
- 데드락 발생 시 재시도 로직 구현
- `SHOW ENGINE INNODB STATUS`로 데드락 로그 모니터링

## 예제 코드

### 데드락 발생 시뮬레이션

```java
public class DeadlockDemo {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread 1: LOCK_A 획득");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.out.println("Thread 1: LOCK_B 대기...");
                synchronized (LOCK_B) {
                    System.out.println("Thread 1: 작업 완료");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread 2: LOCK_B 획득");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.out.println("Thread 2: LOCK_A 대기...");
                synchronized (LOCK_A) {
                    System.out.println("Thread 2: 작업 완료");
                }
            }
        });

        t1.start();
        t2.start();
        // "작업 완료"가 출력되지 않음 → 데드락!
    }
}
```

### tryLock으로 데드락 방지

```java
public class SafeLockDemo {
    private static final ReentrantLock LOCK_A = new ReentrantLock();
    private static final ReentrantLock LOCK_B = new ReentrantLock();

    public static void safeOperation() throws InterruptedException {
        while (true) {
            boolean gotA = LOCK_A.tryLock(50, TimeUnit.MILLISECONDS);
            boolean gotB = false;
            try {
                if (gotA) {
                    gotB = LOCK_B.tryLock(50, TimeUnit.MILLISECONDS);
                }
            } finally {
                if (gotA && gotB) {
                    try {
                        // 두 락 모두 획득 → 작업 수행
                        System.out.println("작업 수행!");
                        return;
                    } finally {
                        LOCK_B.unlock();
                        LOCK_A.unlock();
                    }
                }
                if (gotA) { LOCK_A.unlock(); }
                if (gotB) { LOCK_B.unlock(); }
            }
            // 랜덤 대기 후 재시도 (라이브락 방지)
            Thread.sleep(ThreadLocalRandom.current().nextInt(10));
        }
    }
}
```

### JMX로 데드락 탐지

```java
public class DeadlockDetector {
    public static void detectDeadlock() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadBean.findDeadlockedThreads();

        if (deadlockedThreads != null) {
            ThreadInfo[] infos = threadBean.getThreadInfo(deadlockedThreads, true, true);
            for (ThreadInfo info : infos) {
                System.out.println("데드락 스레드: " + info.getThreadName());
                System.out.println("대기 중인 락: " + info.getLockName());
                System.out.println("락 소유자: " + info.getLockOwnerName());
            }
        }
    }
}
```

## 트레이드오프

| 전략 | 장점 | 단점 | 적합한 상황 |
|------|------|------|------------|
| **예방** | 데드락 원천 차단 | 자원 활용률 저하, 제약 조건 | 안전이 최우선인 시스템 |
| **회피** | 안전 상태 보장 | 사전 정보 필요, 계산 비용 | 자원 요구량 예측 가능 시 |
| **탐지/복구** | 자원 활용률 높음 | 탐지 비용, 복구 복잡 | DB 트랜잭션, 범용 OS |
| **무시** | 오버헤드 없음 | 데드락 시 수동 개입 | 발생 확률 극히 낮은 경우 |

### 실무 선택 가이드

| 상황 | 권장 전략 | 이유 |
|------|----------|------|
| Java 멀티스레드 | **예방** (락 순서 고정) | 구현 간단, 오버헤드 없음 |
| DB 트랜잭션 | **탐지/복구** (재시도) | DBMS가 탐지, 앱은 재시도만 |
| 분산 시스템 | **예방** + **타임아웃** | 탐지가 어려워 예방 우선 |
| 실시간 시스템 | **예방** (자원 순서) | 복구 시간 허용 불가 |

## 면접 예상 질문

### Q: 데드락의 4가지 발생 조건은?

A: **(1) 상호 배제**: 자원을 한 프로세스만 사용. **(2) 점유 대기**: 자원을 들고 다른 자원 대기. **(3) 비선점**: 자원을 강제로 못 뺏음. **(4) 순환 대기**: 프로세스들이 원형으로 대기. **왜 4가지 모두 필요한가?** 하나라도 없으면 데드락이 성립하지 않습니다. 이 원리를 이용해 **하나만 제거**하면 데드락을 예방할 수 있습니다.

### Q: 데드락을 어떻게 해결하나요?

A: 가장 실용적인 방법은 **락 순서 고정**으로 순환 대기를 제거하는 것입니다. 모든 스레드가 락 A → 락 B 순서를 지키면 순환 대기가 불가능합니다. DB에서는 DBMS가 자동으로 데드락을 **탐지**하고 한 트랜잭션을 롤백하므로, 애플리케이션에서 **재시도 로직**을 구현합니다. **왜 락 순서가 가장 좋은가?** 런타임 오버헤드가 없고, 코드 리뷰로 검증 가능하며, 구현이 간단합니다.

### Q: 데드락과 기아(Starvation)의 차이는?

A: **데드락**은 여러 프로세스가 서로를 기다리며 **모두 멈추는 상태**입니다. **기아**는 특정 프로세스가 다른 프로세스들에게 계속 밀려 **영원히 실행되지 못하는 상태**입니다. **차이**: 데드락은 관련 프로세스 **전부** 멈추고, 기아는 **일부만** 피해를 봅니다. **해결**: 기아는 에이징(대기 시간에 따라 우선순위 상승)으로 해결합니다.

### Q: 라이브락이란 무엇인가요?

A: 라이브락은 프로세스들이 데드락을 피하려고 **계속 상태를 변경**하지만, 결국 **아무 진전이 없는 상태**입니다. 예를 들어, 두 스레드가 동시에 `tryLock()` 실패 → 락 해제 → 재시도를 **같은 타이밍**에 반복합니다. **해결**: 재시도 시 **랜덤 대기 시간**을 추가하여 타이밍을 어긋나게 합니다.

### Q: DB 데드락은 어떻게 처리하나요?

A: MySQL(InnoDB)은 **Wait-for Graph**로 데드락을 자동 탐지하고, 비용이 적은 트랜잭션을 롤백합니다. 애플리케이션에서는 `DeadlockLoserDataAccessException`을 잡아 **재시도**합니다. **예방법**: (1) 테이블/행 접근 순서 통일 (2) 트랜잭션 범위 최소화 (3) 인덱스 활용으로 락 범위 축소.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로세스와 스레드](./process-vs-thread.md) | 선수 지식 - 실행 단위 기본 | [2] 입문 |
| [동기화](./synchronization.md) | 선수 지식 - 뮤텍스, 세마포어, 락 | [3] 중급 |
| [컨텍스트 스위칭](./context-switching.md) | 락 경쟁으로 인한 스위칭 오버헤드 | [3] 중급 |

## 참고 자료

- Operating System Concepts (Silberschatz) - Chapter 8: Deadlocks
- Java Concurrency in Practice (Brian Goetz) - Chapter 10: Avoiding Liveness Hazards
- [MySQL Deadlock Documentation](https://dev.mysql.com/doc/refman/8.0/en/innodb-deadlocks.html)
- The Art of Multiprocessor Programming (Herlihy, Shavit)
