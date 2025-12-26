# GC (Garbage Collection)

> `[3] 중급` · 선수 지식: [JVM 구조](./jvm.md)

> 더 이상 사용되지 않는 객체를 자동으로 메모리에서 해제하는 메커니즘

`#GC` `#GarbageCollection` `#가비지컬렉션` `#힙메모리` `#HeapMemory` `#MinorGC` `#MajorGC` `#FullGC` `#STW` `#StopTheWorld` `#SerialGC` `#ParallelGC` `#CMS` `#G1GC` `#ZGC` `#Shenandoah` `#Eden` `#Survivor` `#YoungGeneration` `#OldGeneration` `#MarkAndSweep` `#Compaction` `#GCTuning` `#메모리누수`

## 왜 알아야 하는가?

GC 이해는 Java 성능 튜닝의 핵심입니다. STW(Stop-The-World) 시간을 줄여 응답 시간을 개선하고, OOM을 방지합니다. GC 로그 분석으로 메모리 누수를 찾고, 적절한 GC 알고리즘을 선택할 수 있습니다.

## 핵심 개념

- **가비지**: 참조되지 않는 객체
- **Mark-Sweep-Compact**: 기본 GC 알고리즘
- **Generational Collection**: 세대별 수집
- **STW (Stop-The-World)**: GC 중 애플리케이션 정지

## 쉽게 이해하기

**GC**를 청소부에 비유할 수 있습니다.

- **힙**: 사무실 전체
- **객체**: 사무실의 물건들
- **참조**: 물건에 붙은 "사용 중" 스티커
- **GC**: 스티커 없는 물건을 치우는 청소부
- **STW**: 청소할 때 모두 자리 비우기

## 상세 설명

### 가비지 판단: 도달 가능성 분석

```
GC Roots ──► 도달 가능한 객체 = 살아있음
   │
   ├─ Stack의 지역 변수
   ├─ Method Area의 static 변수
   ├─ JNI 참조
   └─ 활성 스레드

예시:
GC Roots ──► A ──► B ──► C
             │
             └──► D

E ──► F (GC Roots와 연결 없음 → 가비지)
```

### Generational Collection

```
객체 생성 → Eden

Eden 가득 참 → Minor GC
  │
  ├─ 살아남음 → Survivor 0 (age = 1)
  │
  └─ 다시 Minor GC → age++ → Survivor 0 ↔ Survivor 1 이동
                      │
                      └─ age ≥ threshold (기본 15)
                               │
                               ▼
                         Old Generation
                               │
                               └─ Old 가득 참 → Major GC
```

**세대별 가설**:
- 대부분의 객체는 금방 죽는다 (Weak Generational Hypothesis)
- 오래 살아남은 객체는 계속 살아남는다

### GC 알고리즘

#### 1. Serial GC

```
단일 스레드로 GC 수행

┌──────────────────────────────────────────┐
│  App  │  App  │  GC (STW)  │  App  │    │
└──────────────────────────────────────────┘
                 ↑
               단일 스레드

-XX:+UseSerialGC
용도: 클라이언트 VM, 작은 힙
```

#### 2. Parallel GC

```
멀티 스레드로 GC 수행 (Throughput 중심)

┌──────────────────────────────────────────┐
│  App  │  App  │  GC (STW)  │  App  │    │
└──────────────────────────────────────────┘
                 ↑
               멀티 스레드

-XX:+UseParallelGC
용도: 배치 처리, 높은 처리량 필요
```

#### 3. G1 GC (Garbage-First)

```
힙을 Region으로 분할, 가비지가 많은 곳 우선 수집

┌───┬───┬───┬───┬───┬───┬───┬───┐
│ E │ S │ O │ O │ E │ H │ O │ E │  ← 균등 크기 Region
└───┴───┴───┴───┴───┴───┴───┴───┘
  │   │   │       │   │
  └───┴───┴───────┴───┴── 가비지 많은 순서로 수집

-XX:+UseG1GC (Java 9+ 기본)
용도: 큰 힙, 짧은 STW 필요
```

#### 4. ZGC

```
동시 수행, 극도로 짧은 STW (< 10ms)

┌──────────────────────────────────────────┐
│  App  │  App + GC (대부분 동시)  │  App  │
└──────────────────────────────────────────┘
                    ↑
              Colored Pointers + Load Barriers

-XX:+UseZGC
용도: 초대형 힙 (TB 단위), 저지연 필수
```

### GC 비교

| GC | STW 시간 | 처리량 | 힙 크기 | 적합 |
|-----|---------|-------|--------|-----|
| Serial | 길다 | 낮음 | 소형 | 클라이언트 |
| Parallel | 중간 | 높음 | 중형 | 배치 |
| G1 | 짧음 | 중간 | 대형 | 범용 |
| ZGC | 매우 짧음 | 중간 | 초대형 | 저지연 |

### GC 튜닝 옵션

```bash
# GC 선택
-XX:+UseG1GC
-XX:+UseZGC

# 힙 크기
-Xms4g -Xmx4g  # 동일하게 설정 권장

# G1 설정
-XX:MaxGCPauseMillis=200  # 목표 STW 시간
-XX:G1HeapRegionSize=16m  # Region 크기

# GC 로그
-Xlog:gc*:file=gc.log:time
```

### GC 로그 분석

```
[GC pause (G1 Evacuation Pause) (young), 0.0125648 secs]
   [Eden: 24.0M(24.0M)->0.0B(20.0M)]
   [Survivors: 0.0B->4.0M]
   [Heap: 24.0M(256.0M)->4.0M(256.0M)]
 [Times: user=0.04 sys=0.00, real=0.01 secs]
```

- **Eden**: 24M 사용 → 0 (수집됨)
- **Survivors**: 0 → 4M (살아남음)
- **STW**: 0.01초

## 트레이드오프

| 항목 | 트레이드오프 |
|------|-------------|
| STW vs 처리량 | 짧은 STW ↔ 높은 CPU 사용 |
| 힙 크기 | 큰 힙 = GC 빈도↓, 시간↑ |
| GC 스레드 | 많으면 GC 빠름, 앱 CPU↓ |

## 면접 예상 질문

### Q: GC의 동작 방식을 설명해주세요.

A: (1) **Mark**: GC Roots에서 도달 가능한 객체 표시 (2) **Sweep**: 표시 없는 객체 메모리 해제 (3) **Compact**: 단편화 방지를 위해 재배치. **세대별 수집**: Young(Eden → Survivor → Old)으로 이동하며, Minor GC(Young)와 Major GC(Old)로 구분합니다. **STW**: GC 중 애플리케이션 정지.

### Q: G1 GC의 특징은?

A: (1) **Region 기반**: 힙을 동일 크기 Region으로 분할 (2) **Garbage-First**: 가비지가 많은 Region 우선 수집 (3) **예측 가능한 STW**: `MaxGCPauseMillis` 목표 설정 (4) **Concurrent**: 대부분 작업이 애플리케이션과 동시 수행. **적합**: 4GB 이상 힙, 짧은 응답 시간 필요 시.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [JVM 구조](./jvm.md) | 선수 지식 | [3] 중급 |
| [메모리 관리](../os/memory-management.md) | OS 기반 | [3] 중급 |

## 참고 자료

- [Java Garbage Collection Basics](https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html)
- Java Performance - Scott Oaks
