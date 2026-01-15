# 동시성 (Concurrency)

> `[3] 중급` · 선수 지식: [프로세스와 스레드](../../os/process-vs-thread.md), [동기화](../../os/synchronization.md)

> 여러 작업이 동시에 진행되는 것처럼 보이도록 관리하는 프로그래밍 기법

`#동시성` `#Concurrency` `#병렬성` `#Parallelism` `#스레드` `#Thread` `#synchronized` `#volatile` `#Atomic` `#Lock` `#ReentrantLock` `#ExecutorService` `#ThreadPool` `#CompletableFuture` `#경쟁조건` `#RaceCondition` `#교착상태` `#Deadlock` `#가시성` `#Visibility` `#원자성` `#Atomicity` `#메모리모델` `#JMM`

## 왜 알아야 하는가?

현대 애플리케이션은 멀티스레드 환경에서 동작합니다. 동시성 문제(Race Condition, Deadlock)를 이해하지 못하면 찾기 어려운 버그가 발생합니다. 올바른 동기화는 성능과 안정성의 핵심입니다.

## 핵심 개념

- **동시성 vs 병렬성**: 논리적 동시 vs 물리적 동시
- **원자성**: 연산이 중간에 끊기지 않음
- **가시성**: 한 스레드의 변경이 다른 스레드에 보임
- **순서성**: 명령 실행 순서 보장

## 쉽게 이해하기

**동시성 문제**를 공유 화이트보드에 비유할 수 있습니다.

- **경쟁 조건**: 두 사람이 동시에 같은 칸에 쓰기 (충돌)
- **가시성 문제**: 한 사람이 쓴 내용이 다른 사람에게 안 보임
- **교착 상태**: 서로 상대방의 펜을 기다림

## 상세 설명

### 동시성 vs 병렬성

```
동시성 (Concurrency):
단일 코어에서 번갈아 실행 (시분할)

    시간 →
    ┌──┬──┬──┬──┬──┬──┐
코어│A │B │A │B │A │B │
    └──┴──┴──┴──┴──┴──┘

병렬성 (Parallelism):
멀티 코어에서 실제 동시 실행

    시간 →
    ┌────────────────┐
코어1│   A            │
    ├────────────────┤
코어2│   B            │
    └────────────────┘
```

### 동시성 문제

#### 1. 경쟁 조건 (Race Condition)

```java
// 문제 코드
int count = 0;

void increment() {
    count++;  // 읽기 → 증가 → 쓰기 (3단계, 비원자적)
}

// 스레드 A: count = 0 읽기
// 스레드 B: count = 0 읽기
// 스레드 A: 0 + 1 = 1 쓰기
// 스레드 B: 0 + 1 = 1 쓰기
// 결과: count = 1 (2가 아님!)
```

#### 2. 가시성 문제 (Visibility)

```java
boolean running = true;

// 스레드 A
void stop() {
    running = false;  // 메인 메모리에 안 쓰일 수 있음
}

// 스레드 B
void run() {
    while (running) {  // 캐시된 값만 읽을 수 있음
        // 무한 루프 가능!
    }
}
```

### 동기화 도구

#### 1. synchronized

```java
class Counter {
    private int count = 0;

    // 메서드 전체 동기화
    public synchronized void increment() {
        count++;
    }

    // 블록 동기화
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }
}
```

**특징**: 암시적 락, 재진입 가능, 블로킹

#### 2. volatile

```java
volatile boolean running = true;

// 읽기/쓰기가 메인 메모리에서 직접 수행
// 가시성 보장, 원자성 미보장 (count++ 불가)
```

**사용 시점**: 플래그 변수, 단일 변수 읽기/쓰기

#### 3. Atomic 클래스

```java
AtomicInteger count = new AtomicInteger(0);

count.incrementAndGet();    // 원자적 증가
count.compareAndSet(5, 10); // CAS (Compare-And-Swap)
```

**CAS 원리**:
```
기대값 == 현재값? → 새 값으로 교체 (성공)
기대값 != 현재값? → 실패, 재시도
```

#### 4. Lock 인터페이스

```java
private final Lock lock = new ReentrantLock();

void update() {
    lock.lock();
    try {
        // 임계 영역
    } finally {
        lock.unlock();  // 반드시 해제
    }
}
```

| 특징 | synchronized | Lock |
|------|-------------|------|
| 획득/해제 | 자동 | 수동 |
| 시도 | 불가 | tryLock() |
| 인터럽트 | 불가 | lockInterruptibly() |
| 공정성 | 불가 | 설정 가능 |
| 조건 변수 | wait/notify | Condition |

#### 5. ExecutorService

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

// 작업 제출
Future<Integer> future = executor.submit(() -> {
    return compute();
});

int result = future.get();  // 결과 대기

executor.shutdown();
```

#### 6. CompletableFuture

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchData())           // 비동기 시작
    .thenApply(data -> process(data))         // 변환
    .thenApply(processed -> format(processed)) // 변환
    .exceptionally(ex -> "error");            // 예외 처리

String result = future.join();
```

### 동기화 전략 비교

| 도구 | 용도 | 성능 |
|------|------|------|
| synchronized | 간단한 동기화 | 중간 |
| volatile | 플래그, 가시성 | 높음 |
| Atomic | 단일 변수 원자 연산 | 높음 |
| Lock | 세밀한 제어 필요 | 중간 |
| ConcurrentHashMap | 동시성 Map | 높음 |

## 트레이드오프

| 항목 | 트레이드오프 |
|------|-------------|
| 락 범위 | 넓으면 안전↑, 병렬성↓ |
| 락 세분화 | 병렬성↑, 복잡도↑ |
| 락 없는 자료구조 | 성능↑, 구현 난이도↑ |

## 면접 예상 질문

### Q: synchronized와 Lock의 차이는?

A: **synchronized**: 암시적 락, 블록 벗어나면 자동 해제, 간단함. **Lock**: 명시적 락, 수동 해제 필요, tryLock()/lockInterruptibly() 지원, 조건 변수 여러 개 가능. **선택**: 간단한 경우 synchronized, 세밀한 제어(타임아웃, 인터럽트) 필요 시 Lock.

### Q: volatile과 Atomic의 차이는?

A: **volatile**: 가시성 보장, 원자성 미보장 (`count++` 불가). **Atomic**: 가시성 + 원자성 모두 보장, CAS 기반 락 없는 동기화. **사용**: 단순 플래그 → volatile, 증가/CAS 연산 → Atomic.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [동기화](../../os/synchronization.md) | OS 기초 | [3] 중급 |
| [프로세스와 스레드](../../os/process-vs-thread.md) | 선수 지식 | [2] 입문 |

## 참고 자료

- Java Concurrency in Practice - Brian Goetz
- [Java Memory Model - Oracle](https://docs.oracle.com/javase/specs/jls/se17/html/jls-17.html)
