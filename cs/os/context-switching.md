# 컨텍스트 스위칭 (Context Switching)

> `[3] 중급` · 선수 지식: [프로세스와 스레드](./process-vs-thread.md), [스케줄링](./scheduling.md)

> CPU가 현재 실행 중인 프로세스/스레드의 상태를 저장하고, 다른 프로세스/스레드의 상태를 복원하여 실행을 전환하는 과정

`#컨텍스트스위칭` `#ContextSwitching` `#ContextSwitch` `#PCB` `#ProcessControlBlock` `#TCB` `#ThreadControlBlock` `#레지스터` `#Register` `#프로그램카운터` `#ProgramCounter` `#스택포인터` `#StackPointer` `#TLB` `#TranslationLookasideBuffer` `#TLB플러시` `#TLBFlush` `#캐시미스` `#CacheMiss` `#캐시오염` `#CachePollution` `#오버헤드` `#Overhead` `#선점` `#Preemptive` `#인터럽트` `#Interrupt` `#시스템콜` `#SystemCall` `#커널모드` `#KernelMode` `#유저모드` `#UserMode`

## 왜 알아야 하는가?

컨텍스트 스위칭은 멀티태스킹 운영체제의 핵심 메커니즘입니다. CPU가 하나뿐인 시스템에서 여러 프로세스가 동시에 실행되는 것처럼 보이는 이유가 바로 컨텍스트 스위칭 덕분입니다. 이 개념을 이해하면 성능 최적화, 스레드 풀 설정, 시스템 튜닝에서 올바른 결정을 내릴 수 있습니다.

- **실무**: 스레드 풀 크기 결정, 과도한 스위칭으로 인한 성능 저하 진단
- **면접**: 프로세스 vs 스레드 비교 시 핵심 차이점으로 출제
- **기반 지식**: 스케줄링, 멀티스레딩, 가상 메모리 이해의 기초

## 핵심 개념

- **PCB (Process Control Block)**: 프로세스 상태 정보를 저장하는 커널 자료구조
- **TCB (Thread Control Block)**: 스레드 상태 정보를 저장하는 자료구조
- **TLB 플러시**: 프로세스 전환 시 주소 변환 캐시가 무효화되는 현상
- **캐시 오염 (Cache Pollution)**: 전환 후 이전 프로세스 데이터가 캐시에 남아 미스 발생
- **오버헤드**: 컨텍스트 스위칭에 소비되는 CPU 시간 (실제 작업이 아닌 관리 비용)

## 쉽게 이해하기

**컨텍스트 스위칭**을 독서에 비유할 수 있습니다.

### 프로세스 컨텍스트 스위칭 = 다른 책으로 교체

A 책을 읽다가 B 책으로 바꾸려면:
1. A 책에 **책갈피** 꽂기 (현재 상태 저장)
2. A 책을 **책장에 넣기** (메모리에서 내리기)
3. B 책을 **책장에서 꺼내기** (메모리에 올리기)
4. B 책의 **책갈피 위치**에서 읽기 시작 (상태 복원)
5. A 책 관련 **메모/노트 전부 치우기** (TLB 플러시, 캐시 무효화)

### 스레드 컨텍스트 스위칭 = 같은 책에서 다른 챕터로 이동

같은 책 안에서 챕터를 바꾸면:
1. 현재 위치에 **손가락 끼우기** (레지스터, 스택 포인터만 저장)
2. 다른 챕터로 **이동** (같은 메모리 공간)
3. 메모/노트는 **그대로 유지** (TLB, 캐시 유지)

| 비유 | 프로세스 전환 | 스레드 전환 |
|------|-------------|-----------|
| 해야 할 일 | 책 교체 + 메모 치우기 | 챕터 이동만 |
| 소요 시간 | 오래 걸림 | 빠름 |
| 기존 정보 | 전부 버림 | 대부분 유지 |

## 상세 설명

### 컨텍스트 스위칭이 발생하는 시점

| 원인 | 설명 | 예시 |
|------|------|------|
| **타이머 인터럽트** | 타임 슬라이스 소진 | Round Robin에서 할당 시간 만료 |
| **I/O 요청** | 디스크/네트워크 대기 | 파일 읽기, DB 쿼리 |
| **시스템 콜** | 커널 서비스 요청 | `fork()`, `read()`, `write()` |
| **더 높은 우선순위** | 선점 스케줄링 | 실시간 프로세스 도착 |
| **자발적 양보** | 프로세스가 스스로 양보 | `yield()`, `sleep()` |

**왜 이렇게 다양한가?**
- OS가 CPU를 효율적으로 사용하려면, 대기 중인 프로세스에게 낭비 없이 CPU를 넘겨야 함
- I/O 대기 중 CPU를 놀리면 자원 낭비

### PCB (Process Control Block)

프로세스의 모든 실행 정보를 담는 커널 자료구조입니다.

```
┌────────────────────────────────────┐
│          PCB (커널 메모리)          │
├────────────────────────────────────┤
│  프로세스 ID (PID)                 │
│  프로세스 상태 (Running/Ready/...)  │
│  프로그램 카운터 (PC)              │
│  CPU 레지스터 값들                 │
│  스택 포인터 (SP)                  │
│  메모리 관리 정보                  │
│    - 페이지 테이블 베이스 레지스터  │
│    - 세그먼트 테이블               │
│  I/O 상태 정보                     │
│    - 열린 파일 목록                │
│    - 할당된 I/O 장치               │
│  스케줄링 정보                     │
│    - 우선순위                      │
│    - 스케줄링 큐 포인터            │
│  CPU 사용 시간 통계                │
└────────────────────────────────────┘
```

**왜 PCB에 이 모든 정보가 필요한가?**
- 프로세스를 **정확히 그 지점**에서 재개하려면 실행 상태 전부가 필요
- 빠뜨리면 레지스터 값이 오염되어 프로세스 오동작

### 프로세스 컨텍스트 스위칭 과정

```
프로세스 A (실행 중)          커널              프로세스 B (Ready)
─────────────────          ──────           ─────────────────
     │                        │                     │
     │  인터럽트/시스템 콜     │                     │
     │ ──────────────────────→│                     │
     │                        │                     │
     │   ① A의 상태를 PCB_A에 저장                  │
     │    - 레지스터            │                    │
     │    - PC, SP             │                    │
     │    - 메모리 맵           │                    │
     │                         │                    │
     │   ② 스케줄러가 B 선택    │                   │
     │                         │                    │
     │   ③ 메모리 맵 전환       │                   │
     │    - 페이지 테이블 변경   │                   │
     │    - TLB 플러시          │                   │
     │                         │                    │
     │   ④ PCB_B에서 상태 복원  │                   │
     │    - 레지스터            │                    │
     │    - PC, SP             │                    │
     │                         │───────────────────→│
     │                         │                    │
     │                         │        프로세스 B 실행 재개
```

### 스레드 컨텍스트 스위칭 과정

같은 프로세스 내 스레드 전환:

```
스레드 A (실행 중)            커널              스레드 B (Ready)
─────────────────          ──────           ─────────────────
     │                        │                     │
     │  타이머 인터럽트        │                     │
     │ ──────────────────────→│                     │
     │                        │                     │
     │   ① A의 상태를 TCB_A에 저장                  │
     │    - 레지스터            │                    │
     │    - PC, SP             │                    │
     │                         │                    │
     │   ② 스케줄러가 B 선택    │                   │
     │                         │                    │
     │   ③ 메모리 전환 불필요!  │                   │
     │    - 같은 주소 공간      │                   │
     │    - TLB 유지            │                   │
     │                         │                    │
     │   ④ TCB_B에서 상태 복원  │                   │
     │    - 레지스터            │                    │
     │    - PC, SP             │                    │
     │                         │───────────────────→│
     │                         │                    │
     │                         │        스레드 B 실행 재개
```

### 프로세스 vs 스레드 컨텍스트 스위칭 비용

| 단계 | 프로세스 전환 | 스레드 전환 | 비용 차이 이유 |
|------|-------------|-----------|--------------|
| 레지스터 저장/복원 | O | O | 동일 |
| 스택 포인터 전환 | O | O | 동일 |
| 페이지 테이블 변경 | O | X | 같은 주소 공간 |
| TLB 플러시 | O | X | 같은 매핑 유지 |
| CPU 캐시 무효화 | 높음 | 낮음 | 공유 데이터 캐시 유지 |
| 메모리 맵 전환 | O | X | 같은 프로세스 |

**왜 TLB 플러시가 가장 비싼가?**

TLB(Translation Lookaside Buffer)는 가상 주소 → 물리 주소 변환 결과를 캐시합니다.

```
프로세스 A 실행 중:
  TLB: [가상 0x1000 → 물리 0x5000] (A의 매핑)

프로세스 B로 전환:
  TLB 플러시! (A의 매핑은 B에게 무의미)
  TLB: [비어있음]

  B의 첫 메모리 접근마다 TLB 미스 → 페이지 테이블 조회 필요
  점차 B의 매핑으로 TLB 채워짐 (워밍업)
```

- TLB 히트: ~1ns (캐시에서 바로 변환)
- TLB 미스: ~10~100ns (페이지 테이블 조회)
- 플러시 후 워밍업 동안 **모든 메모리 접근이 느려짐**

### 실제 비용 수치

| 측정 항목 | 프로세스 전환 | 스레드 전환 |
|----------|-------------|-----------|
| 직접 비용 (레지스터 등) | ~1-5 us | ~0.5-2 us |
| TLB 워밍업 | ~10-100 us | 0 |
| 캐시 워밍업 | ~100-1000 us | ~10-50 us |
| **총 비용** | **~수백 us** | **~수 us ~ 수십 us** |

> us = 마이크로초 (1/1,000,000초)

**왜 간접 비용이 직접 비용보다 훨씬 큰가?**
- 직접 비용: 레지스터 몇 개 복사 → 수십 바이트
- 간접 비용: 캐시 전체가 새로운 데이터로 채워져야 함 → 수 KB~MB
- 캐시 워밍업 동안 메모리 접근마다 지연 누적

## 트러블슈팅

### 사례 1: 과도한 컨텍스트 스위칭으로 인한 성능 저하

#### 증상

```bash
# vmstat으로 확인 (Linux)
$ vmstat 1
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
15  0      0 102400  51200 512000    0    0     0     0  5000 80000 30 45 25  0  0
                                                               ^^^^^ ^^ ^^
                                                               cs=80000 (과도)
                                                               sy=45% (커널 시간 높음)
                                                               id=25% (유휴 낮음)
```

- `cs` 값이 비정상적으로 높음 (수만~수십만/초)
- `sy` (시스템 시간) 비율이 높고, `us` (유저 시간)이 낮음
- 실제 작업보다 스위칭에 CPU를 더 많이 사용

#### 원인 분석

1. **스레드 수 >> CPU 코어 수**: 너무 많은 스레드가 경쟁
2. **짧은 타임 슬라이스**: 작업 완료 전 빈번한 전환
3. **락 경쟁**: 많은 스레드가 같은 락을 기다리며 대기/재개 반복
4. **빈번한 I/O**: I/O 대기와 완료가 반복되며 전환 유발

#### 해결 방법

```java
// Bad: 스레드 수가 과도
ExecutorService executor = Executors.newFixedThreadPool(1000);

// Good: CPU 코어 수 기반 스레드 풀
int cores = Runtime.getRuntime().availableProcessors();

// CPU 집중 작업: 코어 수 + 1
ExecutorService cpuPool = Executors.newFixedThreadPool(cores + 1);

// I/O 집중 작업: 코어 수 * 2 (I/O 대기 동안 다른 스레드 실행)
ExecutorService ioPool = Executors.newFixedThreadPool(cores * 2);
```

#### 예방 조치

- 스레드 풀 크기를 작업 특성에 맞게 설정
- `vmstat`, `pidstat`으로 컨텍스트 스위칭 모니터링
- 락 경쟁 최소화 (ConcurrentHashMap, Atomic 변수 활용)

### 사례 2: Java에서 자발적/비자발적 스위칭 구분

#### 증상

```bash
# pidstat으로 프로세스별 컨텍스트 스위칭 확인
$ pidstat -w -p <PID> 1
                      cswch/s  nvcswch/s
                      ^^^^^^^  ^^^^^^^^^
                      자발적    비자발적
```

#### 원인 분석

| 유형 | 의미 | 원인 |
|------|------|------|
| **자발적 (voluntary)** | 스스로 CPU 양보 | I/O 대기, sleep(), wait(), 락 대기 |
| **비자발적 (involuntary)** | 강제로 뺏김 | 타임 슬라이스 소진, 높은 우선순위 프로세스 등장 |

- **자발적 높음**: I/O 병목 가능성 → I/O 최적화 필요
- **비자발적 높음**: CPU 경쟁 심함 → 스레드 수 줄이기

## 예제 코드

### 컨텍스트 스위칭 영향 측정 (Java)

```java
public class ContextSwitchDemo {
    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) throws Exception {
        // 단일 스레드 실행
        long singleTime = measureSingleThread();
        System.out.println("단일 스레드: " + singleTime + "ms");

        // 멀티 스레드 실행 (컨텍스트 스위칭 발생)
        long multiTime = measureMultiThread();
        System.out.println("멀티 스레드: " + multiTime + "ms");

        System.out.println("오버헤드: " + (multiTime - singleTime) + "ms");
    }

    private static long measureSingleThread() {
        long start = System.currentTimeMillis();
        long sum = 0;
        for (int i = 0; i < ITERATIONS * 2; i++) {
            sum += i;
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureMultiThread() throws Exception {
        long start = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            long sum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                sum += i;
            }
        });

        Thread t2 = new Thread(() -> {
            long sum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                sum += i;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        return System.currentTimeMillis() - start;
    }
}
```

### 적절한 스레드 풀 크기 설정

```java
public class ThreadPoolConfig {
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();

    /**
     * CPU 집중 작업용 스레드 풀
     * - 코어 수 + 1: 하나의 스레드가 페이지 폴트 등으로 잠시 멈출 때 대비
     * - 더 많으면 컨텍스트 스위칭 오버헤드만 증가
     */
    public static ExecutorService cpuIntensivePool() {
        return Executors.newFixedThreadPool(CPU_CORES + 1);
    }

    /**
     * I/O 집중 작업용 스레드 풀
     * - 코어 수 * 2: I/O 대기 시간 동안 다른 스레드가 CPU 활용
     * - I/O 비율이 높으면 더 큰 값도 가능
     */
    public static ExecutorService ioIntensivePool() {
        return Executors.newFixedThreadPool(CPU_CORES * 2);
    }
}
```

### Linux에서 컨텍스트 스위칭 모니터링

```bash
# 시스템 전체 컨텍스트 스위칭 확인
vmstat 1

# 특정 프로세스의 컨텍스트 스위칭 확인
pidstat -w -p <PID> 1

# 스레드별 컨텍스트 스위칭 확인
pidstat -w -t -p <PID> 1

# perf로 상세 분석
perf stat -e context-switches,cpu-migrations -p <PID> sleep 10
```

## 트레이드오프

| 관점 | 컨텍스트 스위칭 적음 | 컨텍스트 스위칭 많음 |
|------|---------------------|---------------------|
| CPU 효율 | 높음 (실제 작업에 집중) | 낮음 (스위칭 오버헤드) |
| 응답성 | 낮음 (한 프로세스가 오래 점유) | 높음 (빠르게 교대) |
| 공정성 | 낮음 (특정 프로세스 독점) | 높음 (균등 배분) |
| 캐시 효율 | 높음 (워밍업 유지) | 낮음 (빈번한 무효화) |

### 최적화 전략

| 전략 | 방법 | 효과 |
|------|------|------|
| 적절한 스레드 수 | CPU 코어 수 기반 설정 | 불필요한 스위칭 방지 |
| 스레드 풀 | 스레드 재사용 | 생성/소멸 오버헤드 제거 |
| 락 최소화 | CAS, Lock-free 자료구조 | 락 대기로 인한 스위칭 감소 |
| CPU 친화도 | 특정 코어에 바인딩 | 캐시 효율 극대화 |
| 비동기 I/O | NIO, epoll | I/O 대기 스위칭 감소 |

## 면접 예상 질문

### Q: 컨텍스트 스위칭이란 무엇인가요?

A: CPU가 현재 실행 중인 프로세스/스레드의 상태(레지스터, PC, SP 등)를 PCB/TCB에 저장하고, 다른 프로세스/스레드의 상태를 복원하여 실행을 전환하는 과정입니다. **왜 필요한가?** CPU는 한 번에 하나의 작업만 실행할 수 있으므로, 멀티태스킹을 위해 빠르게 전환하면서 동시에 실행되는 것처럼 보이게 합니다.

### Q: 프로세스 컨텍스트 스위칭이 스레드보다 비싼 이유는?

A: 프로세스 전환은 레지스터 저장/복원 외에 **페이지 테이블 변경**과 **TLB 플러시**가 추가로 필요합니다. TLB가 무효화되면 전환 직후 모든 메모리 접근에서 TLB 미스가 발생하여 워밍업 비용이 큽니다. 스레드는 같은 프로세스 내에서 주소 공간을 공유하므로 이 비용이 없습니다. **실제 수치**: 프로세스 전환은 수백 us, 스레드 전환은 수~수십 us 수준입니다.

### Q: 컨텍스트 스위칭을 줄이려면 어떻게 해야 하나요?

A: (1) **스레드 수 최적화**: CPU 집중 작업은 코어 수 + 1, I/O 작업은 코어 수 * 2 정도로 설정합니다. (2) **Lock-free 자료구조 활용**: CAS 기반 AtomicInteger 등으로 락 경쟁을 줄입니다. (3) **비동기 I/O**: NIO나 epoll로 I/O 대기 중 스위칭을 방지합니다. (4) **CPU 친화도**: 프로세스를 특정 코어에 바인딩하여 캐시 효율을 높입니다. **왜 중요한가?** 과도한 스위칭은 실제 작업보다 관리 비용이 더 커지는 상황을 만듭니다.

### Q: 자발적 컨텍스트 스위칭과 비자발적 컨텍스트 스위칭의 차이는?

A: **자발적(voluntary)**: I/O 대기, sleep(), 락 대기 등 프로세스가 스스로 CPU를 양보하는 경우입니다. **비자발적(involuntary)**: 타임 슬라이스 만료나 높은 우선순위 프로세스 도착으로 OS가 강제로 CPU를 뺏는 경우입니다. **진단 기준**: 자발적이 높으면 I/O 병목, 비자발적이 높으면 CPU 경쟁 심화를 의미합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로세스와 스레드](./process-vs-thread.md) | 선수 지식 - 실행 단위의 기본 개념 | [2] 입문 |
| [스케줄링](./scheduling.md) | 컨텍스트 스위칭을 결정하는 알고리즘 | [3] 중급 |
| [가상 메모리](./virtual-memory.md) | TLB, 페이지 테이블과 스위칭 비용의 관계 | [3] 중급 |
| [동기화](./synchronization.md) | 락 경쟁으로 인한 컨텍스트 스위칭 | [3] 중급 |
| [시스템 콜](./system-call.md) | 시스템 콜 시 모드 전환과 컨텍스트 스위칭 | [2] 입문 |

## 참고 자료

- Operating System Concepts (Silberschatz) - Chapter 3: Processes
- Modern Operating Systems (Tanenbaum) - Chapter 2: Processes and Threads
- [Linux Kernel - Context Switching](https://www.kernel.org/doc/html/latest/scheduler/)
- Systems Performance (Brendan Gregg) - Chapter 6: CPUs
