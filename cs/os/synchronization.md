# 동기화 (Synchronization)

> `[3] 중급` · 선수 지식: [프로세스와 스레드](./process-vs-thread.md)

> 여러 프로세스/스레드가 공유 자원에 안전하게 접근하도록 조율하는 메커니즘

`#동기화` `#Synchronization` `#뮤텍스` `#Mutex` `#세마포어` `#Semaphore` `#데드락` `#Deadlock` `#임계영역` `#CriticalSection` `#경쟁조건` `#RaceCondition` `#상호배제` `#MutualExclusion` `#스핀락` `#Spinlock` `#모니터` `#Monitor` `#조건변수` `#ConditionVariable` `#원자연산` `#AtomicOperation` `#락` `#Lock` `#기아` `#Starvation` `#교착상태`

## 왜 알아야 하는가?

멀티스레드 프로그램에서 여러 스레드가 동시에 같은 변수를 수정하면 예상치 못한 결과가 발생합니다. 이를 경쟁 조건(Race Condition)이라 하며, 동기화로 해결합니다. 그러나 잘못된 동기화는 데드락, 기아 현상을 유발합니다. 백엔드 개발자라면 동기화 원리를 이해하고 안전한 코드를 작성할 수 있어야 합니다.

## 핵심 개념

- **임계 영역 (Critical Section)**: 공유 자원에 접근하는 코드 영역
- **상호 배제 (Mutual Exclusion)**: 한 번에 하나의 프로세스만 임계 영역 실행
- **뮤텍스 (Mutex)**: 상호 배제를 보장하는 락
- **세마포어 (Semaphore)**: 카운터 기반 동기화 도구
- **데드락 (Deadlock)**: 두 프로세스가 서로의 락을 기다리며 무한 대기

## 쉽게 이해하기

**동기화**를 화장실 사용에 비유할 수 있습니다.

- **임계 영역 = 화장실**: 한 번에 한 명만 사용
- **뮤텍스 = 화장실 잠금장치**: 사용 중이면 다른 사람 대기
- **세마포어 = 화장실 칸 3개**: 3명까지 동시 사용 가능
- **데드락 = 서로 다른 화장실에서 상대방 화장지 기다리기**: 둘 다 영원히 대기

## 상세 설명

### 경쟁 조건 (Race Condition)

**문제 상황**: 두 스레드가 동시에 count++ 실행

```java
// 공유 변수
int count = 0;

// 스레드 A                // 스레드 B
count++;                   count++;

// count++ 는 실제로 3단계:
// 1. 메모리에서 count 읽기 (LOAD)
// 2. 값 증가 (ADD)
// 3. 메모리에 저장 (STORE)
```

**실행 순서에 따른 결과**:

```
정상 케이스 (순차 실행):
A: LOAD (0) → ADD (1) → STORE (1)
B: LOAD (1) → ADD (2) → STORE (2)
결과: count = 2 ✓

문제 케이스 (동시 실행):
A: LOAD (0) → ADD (1) →
B: LOAD (0) → ADD (1) → STORE (1)
A:                      → STORE (1)
결과: count = 1 ✗ (Lost Update)
```

### 임계 영역 (Critical Section)

**정의**: 공유 자원에 접근하는 코드 영역

**3가지 요구사항**:

1. **상호 배제 (Mutual Exclusion)**: 한 프로세스가 임계 영역에 있으면 다른 프로세스 진입 불가
2. **진행 (Progress)**: 임계 영역이 비어있으면 대기 중인 프로세스 중 하나가 진입 가능
3. **한정 대기 (Bounded Waiting)**: 무한 대기 방지, 언젠가는 진입 가능

```java
// 임계 영역 구조
lock.acquire();        // 진입 영역 (Entry Section)
try {
    // 임계 영역 (Critical Section)
    sharedResource.update();
} finally {
    lock.release();    // 퇴출 영역 (Exit Section)
}
// 나머지 영역 (Remainder Section)
```

### 뮤텍스 (Mutex)

**정의**: Mutual Exclusion의 약자, 상호 배제를 보장하는 락

```java
// 뮤텍스 사용 예시
Mutex mutex = new Mutex();

void increment() {
    mutex.lock();          // 락 획득 (다른 스레드는 대기)
    try {
        count++;           // 임계 영역
    } finally {
        mutex.unlock();    // 락 해제 (대기 중인 스레드 깨움)
    }
}
```

**특징**:
- 소유권 개념: 락을 획득한 스레드만 해제 가능
- 바이너리: 잠김(1) 또는 열림(0)

### 세마포어 (Semaphore)

**정의**: 카운터 기반 동기화 도구

```java
// 세마포어 사용 예시 (동시에 3개까지 허용)
Semaphore semaphore = new Semaphore(3);

void accessResource() {
    semaphore.acquire();   // 카운터 감소, 0이면 대기
    try {
        useSharedResource();
    } finally {
        semaphore.release(); // 카운터 증가, 대기 스레드 깨움
    }
}
```

**종류**:

| 유형 | 카운터 값 | 용도 |
|------|----------|------|
| 바이너리 세마포어 | 0 또는 1 | 뮤텍스와 유사 |
| 카운팅 세마포어 | 0 ~ N | 제한된 자원 풀 (DB 커넥션 등) |

**뮤텍스 vs 세마포어**:

| 뮤텍스 | 세마포어 |
|--------|---------|
| 소유권 있음 | 소유권 없음 |
| 잠금한 스레드만 해제 | 아무나 해제 가능 |
| 한 번에 하나 | 한 번에 N개 |

### 스핀락 (Spinlock)

**정의**: 락을 획득할 때까지 반복 확인 (Busy Waiting)

```java
// 스핀락 구현
AtomicBoolean lock = new AtomicBoolean(false);

void lock() {
    while (!lock.compareAndSet(false, true)) {
        // 락 획득할 때까지 계속 시도 (spin)
    }
}

void unlock() {
    lock.set(false);
}
```

**언제 사용하는가?**

| 상황 | 스핀락 | 뮤텍스/세마포어 |
|------|--------|----------------|
| 대기 시간 짧음 | ✓ (컨텍스트 스위칭 비용 절약) | ✗ |
| 대기 시간 긴 경우 | ✗ (CPU 낭비) | ✓ (슬립 후 깨움) |
| 멀티코어 | ✓ | ✓ |
| 싱글코어 | ✗ | ✓ |

### 데드락 (Deadlock)

**정의**: 두 개 이상의 프로세스가 서로의 자원을 기다리며 무한 대기

```
스레드 A                    스레드 B
─────────                  ─────────
lock(A)                    lock(B)
   │                          │
   ▼                          ▼
lock(B) ←─── 대기 ────→ lock(A)
   ▲                          ▲
   └────── 데드락! ────────────┘
```

**데드락 발생 4가지 조건 (모두 충족 시 발생)**:

1. **상호 배제**: 자원을 한 번에 하나의 프로세스만 사용
2. **점유 대기**: 자원을 점유한 채로 다른 자원 대기
3. **비선점**: 다른 프로세스의 자원을 강제로 뺏지 못함
4. **순환 대기**: 프로세스들이 원형으로 자원 대기

**해결 방법**:

| 방법 | 설명 | 예시 |
|------|------|------|
| **예방** | 4가지 조건 중 하나 제거 | 락 순서 고정 |
| **회피** | 데드락 가능성 검사 후 자원 할당 | Banker's Algorithm |
| **탐지/복구** | 데드락 발생 후 처리 | 프로세스 강제 종료 |
| **무시** | 발생 확률 낮으면 무시 | 대부분의 OS |

**예방 - 락 순서 고정**:

```java
// Bad: 데드락 가능
Thread A: lock(a) → lock(b)
Thread B: lock(b) → lock(a)

// Good: 락 순서 일관성
Thread A: lock(a) → lock(b)
Thread B: lock(a) → lock(b)  // 항상 a 먼저
```

### 모니터 (Monitor)

**정의**: 상호 배제와 조건 동기화를 하나로 묶은 고수준 동기화 도구

```java
// Java의 synchronized는 모니터 구현
class BoundedBuffer {
    private Queue<Integer> buffer = new LinkedList<>();
    private int capacity = 10;

    public synchronized void produce(int item) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait();  // 버퍼 가득 참 → 대기
        }
        buffer.add(item);
        notifyAll();  // 소비자 깨움
    }

    public synchronized int consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();  // 버퍼 비어 있음 → 대기
        }
        int item = buffer.remove();
        notifyAll();  // 생산자 깨움
        return item;
    }
}
```

**구성요소**:
- **락 (Lock)**: 상호 배제 보장
- **조건 변수 (Condition Variable)**: 특정 조건이 될 때까지 대기

## 트레이드오프

| 동기화 도구 | 장점 | 단점 | 적합한 상황 |
|------------|------|------|------------|
| 뮤텍스 | 단순, 소유권 명확 | 오버헤드 | 짧은 임계 영역 |
| 세마포어 | N개 동시 접근 | 소유권 없음 | 자원 풀 관리 |
| 스핀락 | 컨텍스트 스위칭 없음 | CPU 낭비 | 매우 짧은 대기 |
| 모니터 | 고수준, 사용 편리 | 유연성 부족 | 일반적인 동기화 |

## 면접 예상 질문

### Q: 뮤텍스와 세마포어의 차이는?

A: **뮤텍스**는 소유권이 있어 락을 획득한 스레드만 해제할 수 있고, 한 번에 하나만 접근 가능합니다. **세마포어**는 소유권이 없어 아무나 해제 가능하고, 카운터로 N개까지 동시 접근을 허용합니다. **왜 중요한가?** 뮤텍스는 한 번에 하나의 스레드만 자원을 사용해야 할 때, 세마포어는 DB 커넥션 풀처럼 제한된 개수의 자원을 관리할 때 사용합니다.

### Q: 데드락의 4가지 조건과 해결 방법은?

A: **4가지 조건**: (1) 상호 배제 (2) 점유 대기 (3) 비선점 (4) 순환 대기. 모두 충족되어야 데드락 발생. **해결**: 조건 중 하나를 제거합니다. 가장 실용적인 방법은 **락 획득 순서 고정**으로 순환 대기를 방지하는 것입니다. 예: 항상 락 A를 먼저 획득하고 B를 획득. **왜 이 방법이 좋은가?** 구현이 단순하고 런타임 오버헤드가 없습니다.

### Q: 스핀락은 언제 사용하나요?

A: **짧은 시간 대기**가 예상될 때 사용합니다. **왜?** 뮤텍스는 대기 시 컨텍스트 스위칭(수천 사이클)이 발생하지만, 스핀락은 CPU를 점유하며 대기합니다. 대기 시간이 컨텍스트 스위칭 비용보다 짧으면 스핀락이 유리합니다. **주의**: 싱글코어에서는 스핀락이 의미 없고(자신이 점유해야 락 해제 가능), 멀티코어 환경에서만 유효합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로세스와 스레드](./process-vs-thread.md) | 선수 지식 | [2] 입문 |
| [IPC](./ipc.md) | 프로세스 간 통신 | [3] 중급 |

## 참고 자료

- Operating System Concepts (공룡책) - Silberschatz
- Java Concurrency in Practice - Brian Goetz
