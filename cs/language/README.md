# Language

언어별 심화 주제를 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 프로그래밍 언어란                                          │
│        - 언어의 정의, 분류, 특성                                 │
│                    │                                             │
│          ┌────────┴────────┐                                     │
│          ▼                 ▼                                     │
│   [2] 예외 처리      [2] ThreadLocal                             │
│          │                 │                                     │
│          └────────┬────────┘                                     │
│                   ▼                                              │
│   [3] Java 핵심 (Stream, Generic, Annotation, Reflection)        │
│                   │                                              │
│                   ▼                                              │
│   [3] JVM, GC, 동시성, 컬렉션                                    │
│                   │                                              │
│                   ▼                                              │
│   [3] Java 버전별 기능 (8/11/17/21)                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 난이도별 목차

### [1] 정의/기초

프로그래밍 언어가 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [프로그래밍 언어란](./what-is-language.md) | 언어의 정의, 분류(컴파일/인터프리터), 패러다임 | 25분 |

### [2] 입문

OOP와 멀티스레딩 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [ThreadLocal](./thread-local.md) | 스레드별 독립 변수, 메모리 누수 방지 | 프로세스/스레드, OOP |
| [예외 처리](./exception-handling.md) | try-catch-finally, Checked/Unchecked | 프로그래밍 언어란 |

### [3] 중급

언어 심화 개념을 다룹니다.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [JVM 구조](./jvm.md) | 클래스로더, 런타임 영역, 실행 엔진 | 메모리 관리, OOP |
| [GC](./gc.md) | Serial, Parallel, G1, ZGC | JVM 구조 |
| [동시성](./concurrency.md) | synchronized, volatile, Atomic, Lock | 스레드 |
| [컬렉션 프레임워크](./collection-framework.md) | List, Set, Map 구현체 | 자료구조 |
| [Stream API](./stream-api.md) | map, filter, reduce, collect | 컬렉션, 람다 |
| [제네릭](./generics.md) | 타입 파라미터, PECS, 타입 소거 | 프로그래밍 언어란 |
| [어노테이션](./annotation.md) | 메타데이터, 커스텀 어노테이션 | 프로그래밍 언어란 |
| [직렬화](./serialization.md) | Serializable, JSON 직렬화 | 프로그래밍 언어란 |
| [Java 버전별 기능](./java-versions.md) | Java 8/11/17/21 주요 기능 | 프로그래밍 언어란 |
| [Null Safety](./null-safety.md) | Kotlin 널 안정성 | Kotlin 기초 |
| [Coroutine](./coroutine.md) | 비동기 프로그래밍 | 동시성 |

### [4] 심화

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [리플렉션](./reflection.md) | 런타임 클래스 분석/조작 | JVM 구조, 어노테이션 |

## 전체 목차

### Java

| 문서 | 설명 |
|------|------|
| [ThreadLocal](./thread-local.md) | 스레드별 독립 변수, 동작 원리, 메모리 누수 방지 |
| [예외 처리](./exception-handling.md) | Checked/Unchecked Exception, try-with-resources |
| [JVM 구조](./jvm.md) | 클래스로더, 런타임 영역, 실행 엔진 |
| [GC](./gc.md) | Serial, Parallel, G1, ZGC, STW |
| [동시성](./concurrency.md) | synchronized, volatile, Atomic, Lock, 동기화 |
| [컬렉션 프레임워크](./collection-framework.md) | List, Set, Map 구현체별 특성 |
| [Stream API](./stream-api.md) | 함수형 스트림, Collectors, 병렬 스트림 |
| [제네릭](./generics.md) | 타입 안전성, PECS, 와일드카드 |
| [어노테이션](./annotation.md) | 메타 어노테이션, 커스텀 어노테이션, 리플렉션 |
| [리플렉션](./reflection.md) | Class, Method, Field 동적 접근 |
| [직렬화](./serialization.md) | Serializable, transient, JSON 대안 |
| [Java 버전별 기능](./java-versions.md) | Lambda, Record, Virtual Thread 등 |

### Kotlin

| 문서 | 설명 |
|------|------|
| [Null Safety](./null-safety.md) | ?, !!, let, 엘비스 연산자 |
| [Coroutine](./coroutine.md) | suspend, async, Flow |
