# JVM 구조 (JVM Architecture)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](../what-is-language.md), [메모리 관리](../../os/memory-management.md)

> Java 바이트코드를 실행하는 가상 머신의 내부 구조

`#JVM` `#JavaVirtualMachine` `#자바가상머신` `#클래스로더` `#ClassLoader` `#런타임데이터영역` `#RuntimeDataArea` `#힙` `#Heap` `#스택` `#Stack` `#메서드영역` `#MethodArea` `#PC레지스터` `#PCRegister` `#네이티브스택` `#NativeStack` `#실행엔진` `#ExecutionEngine` `#JIT` `#JITCompiler` `#인터프리터` `#GC` `#GarbageCollector` `#바이트코드` `#Bytecode` `#핫스팟` `#HotSpot`

## 왜 알아야 하는가?

JVM 이해는 Java 성능 튜닝의 기본입니다. 메모리 구조를 알아야 OOM 해결, GC 튜닝이 가능합니다. 클래스 로딩 이해는 ClassLoader 관련 오류 해결에 필수입니다.

## 핵심 개념

- **클래스 로더**: .class 파일을 메모리에 적재
- **런타임 데이터 영역**: JVM이 사용하는 메모리 공간
- **실행 엔진**: 바이트코드를 해석하고 실행
- **네이티브 메서드 인터페이스**: C/C++ 코드 호출

## 쉽게 이해하기

**JVM**을 회사에 비유할 수 있습니다.

- **클래스 로더**: 신입사원(클래스)을 채용하여 자리(메모리) 배치
- **힙**: 공용 창고 (모든 부서가 공유)
- **스택**: 각 직원의 개인 책상 (스레드별 독립)
- **실행 엔진**: 작업 지시를 실제 업무로 변환

## 상세 설명

### JVM 전체 구조

```
┌─────────────────────────────────────────────────────────────┐
│                        JVM                                   │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                  Class Loader                         │   │
│  │  Bootstrap → Extension → Application                  │   │
│  └──────────────────────────────────────────────────────┘   │
│                           │                                  │
│                           ▼                                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │               Runtime Data Area                       │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────────────────────┐ │   │
│  │  │ Method  │ │   Heap  │ │   PC    Stack   Native  │ │   │
│  │  │  Area   │ │         │ │ Register       Stack   │ │   │
│  │  │(공유)   │ │(공유)   │ │ (스레드별)              │ │   │
│  │  └─────────┘ └─────────┘ └─────────────────────────┘ │   │
│  └──────────────────────────────────────────────────────┘   │
│                           │                                  │
│                           ▼                                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                 Execution Engine                      │   │
│  │  Interpreter ← → JIT Compiler ← → GC                  │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 1. 클래스 로더 (Class Loader)

```
클래스 로딩 과정:

Loading → Linking → Initialization
           │
           ├─ Verification (검증)
           ├─ Preparation (준비)
           └─ Resolution (해석)
```

**클래스 로더 계층**:

```
Bootstrap ClassLoader ← rt.jar (java.lang.*)
        ↓
Extension ClassLoader ← jre/lib/ext/*.jar
        ↓
Application ClassLoader ← classpath
        ↓
Custom ClassLoader ← 사용자 정의
```

```java
// 클래스 로더 확인
System.out.println(String.class.getClassLoader());     // null (Bootstrap)
System.out.println(MyClass.class.getClassLoader());    // AppClassLoader
```

**위임 모델 (Delegation Model)**: 클래스 로딩 시 부모에게 먼저 요청

### 2. 런타임 데이터 영역

#### 공유 영역 (모든 스레드)

| 영역 | 저장 내용 | 생명주기 |
|------|----------|---------|
| Method Area | 클래스 정보, static, 상수 | JVM 시작~종료 |
| Heap | 객체 인스턴스, 배열 | GC가 관리 |

#### 스레드별 영역

| 영역 | 저장 내용 | 생명주기 |
|------|----------|---------|
| PC Register | 현재 실행 중인 명령 주소 | 스레드 |
| JVM Stack | 메서드 호출 정보 (Frame) | 스레드 |
| Native Stack | 네이티브 메서드 정보 | 스레드 |

#### Heap 상세 구조

```
┌─────────────────────────────────────────────────────────┐
│                        Heap                              │
├─────────────────────────────────────────────────────────┤
│                                                          │
│   ┌─────────────────────────────────────────────────┐   │
│   │              Young Generation                    │   │
│   │   ┌─────────┐  ┌──────────┐  ┌──────────┐      │   │
│   │   │  Eden   │  │Survivor 0│  │Survivor 1│      │   │
│   │   │         │  │(From)    │  │(To)      │      │   │
│   │   └─────────┘  └──────────┘  └──────────┘      │   │
│   └─────────────────────────────────────────────────┘   │
│                           │                              │
│                           ▼ (Promotion)                  │
│   ┌─────────────────────────────────────────────────┐   │
│   │              Old Generation                      │   │
│   │                                                  │   │
│   └─────────────────────────────────────────────────┘   │
│                                                          │
└─────────────────────────────────────────────────────────┘

Minor GC: Young → Young 또는 Old
Major GC: Old 영역 정리
```

#### Stack Frame 구조

```
┌─────────────────────┐
│   Stack Frame       │
├─────────────────────┤
│ Local Variable      │ ← 지역 변수
│ Array               │
├─────────────────────┤
│ Operand Stack       │ ← 연산 중간 결과
├─────────────────────┤
│ Frame Data          │ ← 메서드 정보
│ (Constant Pool Ref) │
└─────────────────────┘
```

### 3. 실행 엔진

```
바이트코드 ──► 인터프리터 ──► 실행 (느림)
    │
    └──► 프로파일러 ──► "핫스팟" 발견 ──► JIT 컴파일러 ──► 네이티브 코드 캐싱
                                                              ↓
                                               다음 실행 시 빠르게 실행
```

**JIT 컴파일러 유형**:
- **C1 (Client)**: 빠른 컴파일, 적은 최적화
- **C2 (Server)**: 느린 컴파일, 많은 최적화
- **Tiered Compilation**: C1 → C2 단계적 적용 (기본값)

### 메모리 설정 옵션

```bash
# 힙 크기
-Xms512m   # 초기 힙 크기
-Xmx2g     # 최대 힙 크기

# 메타스페이스 (Method Area)
-XX:MetaspaceSize=128m
-XX:MaxMetaspaceSize=512m

# 스택 크기
-Xss1m     # 스레드당 스택 크기
```

## 트레이드오프

| 항목 | 트레이드오프 |
|------|-------------|
| 힙 크기 | 크면 GC 빈도↓, GC 시간↑ |
| JIT 레벨 | 높으면 성능↑, 웜업 시간↑ |
| 스택 크기 | 크면 깊은 재귀 가능, 스레드 수↓ |

## 면접 예상 질문

### Q: JVM 메모리 구조를 설명해주세요.

A: **공유 영역**: (1) Method Area - 클래스 정보, static 변수, 상수 풀 (2) Heap - 객체 인스턴스 (Young/Old Generation). **스레드별 영역**: (3) PC Register - 현재 명령 주소 (4) JVM Stack - 메서드 호출 스택 (5) Native Stack - 네이티브 메서드. **핵심**: Heap은 GC 대상, Stack은 스레드 종료 시 자동 해제.

### Q: 클래스 로더의 동작 방식은?

A: **위임 모델**: 클래스 로딩 요청 시 부모에게 먼저 위임하여 중복 로딩 방지. Bootstrap → Extension → Application 순으로 검색. **동작 단계**: Loading(파일 읽기) → Linking(검증/준비/해석) → Initialization(static 초기화). **장점**: 보안(핵심 클래스 보호), 일관성(같은 클래스 보장).

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [GC](./gc.md) | 힙 메모리 관리 | [3] 중급 |
| [프로그래밍 언어란](../what-is-language.md) | 선수 지식 | [1] 정의 |

## 참고 자료

- [JVM Specification](https://docs.oracle.com/javase/specs/jvms/se17/html/)
- Understanding the JVM - Advanced Features and Best Practices
