# Language

프로그래밍 언어별 심화 주제를 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 프로그래밍 언어란                                          │
│        - 언어의 정의, 분류, 특성                                 │
│                    │                                             │
│          ┌────────┼────────┬────────────────┐                    │
│          ▼        ▼        ▼                ▼                    │
│   [Java]      [Kotlin]  [WebAssembly]   [기타]                   │
│   ├─ ThreadLocal  ├─ Null Safety  └─ Wasm, WASI                 │
│   ├─ 예외 처리     └─ Coroutine       브라우저/엣지              │
│   ├─ JVM, GC, 동시성                                             │
│   ├─ 컬렉션, Stream                                              │
│   └─ 리플렉션                                                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 하위 카테고리

### Java

Java 언어 심화 학습 문서입니다.

| 문서 | 설명 | 난이도 |
|------|------|--------|
| [ThreadLocal](./java/thread-local.md) | 스레드별 독립 변수, 메모리 누수 방지 | [2] 입문 |
| [예외 처리](./java/exception-handling.md) | Checked/Unchecked, try-with-resources | [2] 입문 |
| [JVM 구조](./java/jvm.md) | 클래스로더, 런타임 영역, 실행 엔진 | [3] 중급 |
| [GC](./java/gc.md) | Serial, Parallel, G1, ZGC | [3] 중급 |
| [동시성](./java/concurrency.md) | synchronized, volatile, Atomic, Lock | [3] 중급 |
| [컬렉션 프레임워크](./java/collection-framework.md) | List, Set, Map 구현체별 특성 | [3] 중급 |
| [Stream API](./java/stream-api.md) | 함수형 스트림, Collectors, 병렬 스트림 | [3] 중급 |
| [제네릭](./java/generics.md) | 타입 안전성, PECS, 와일드카드 | [3] 중급 |
| [어노테이션](./java/annotation.md) | 메타 어노테이션, 커스텀 어노테이션 | [3] 중급 |
| [직렬화](./java/serialization.md) | Serializable, transient, JSON | [3] 중급 |
| [Java 버전별 기능](./java/java-versions.md) | Lambda, Record, Virtual Thread | [3] 중급 |
| [리플렉션](./java/reflection.md) | Class, Method, Field 동적 접근 | [4] 심화 |

[Java 전체 문서 보기 →](./java/)

### Kotlin

Kotlin 언어 심화 학습 문서입니다.

| 문서 | 설명 | 난이도 |
|------|------|--------|
| [Null Safety](./kotlin/null-safety.md) | ?, !!, let, 엘비스 연산자 | [3] 중급 |
| [Coroutine](./kotlin/coroutine.md) | suspend, async, Flow | [3] 중급 |

[Kotlin 전체 문서 보기 →](./kotlin/)

### WebAssembly

브라우저를 넘어 서버/엣지로 확장되는 바이너리 실행 포맷입니다.

| 문서 | 설명 | 난이도 |
|------|------|--------|
| [WebAssembly](./webassembly.md) | Wasm, WASI, 샌드박싱, 엣지 컴퓨팅 | [2] 입문 |

## 난이도별 목차

### [1] 정의/기초

프로그래밍 언어가 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [프로그래밍 언어란](./what-is-language.md) | 언어의 정의, 분류(컴파일/인터프리터), 패러다임 | 25분 |

### [2] 입문

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [WebAssembly](./webassembly.md) | Wasm, WASI, 샌드박싱, 크로스플랫폼 | 프로그래밍 언어란 |
| [ThreadLocal](./java/thread-local.md) | 스레드별 독립 변수, 메모리 누수 방지 | 프로세스/스레드, OOP |
| [예외 처리](./java/exception-handling.md) | try-catch-finally, Checked/Unchecked | 프로그래밍 언어란 |

### [3] 중급

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [JVM 구조](./java/jvm.md) | 클래스로더, 런타임 영역, 실행 엔진 | 메모리 관리, OOP |
| [GC](./java/gc.md) | Serial, Parallel, G1, ZGC | JVM 구조 |
| [동시성](./java/concurrency.md) | synchronized, volatile, Atomic, Lock | 스레드 |
| [컬렉션 프레임워크](./java/collection-framework.md) | List, Set, Map 구현체 | 자료구조 |
| [Stream API](./java/stream-api.md) | map, filter, reduce, collect | 컬렉션, 람다 |
| [제네릭](./java/generics.md) | 타입 파라미터, PECS, 타입 소거 | 프로그래밍 언어란 |
| [어노테이션](./java/annotation.md) | 메타데이터, 커스텀 어노테이션 | 프로그래밍 언어란 |
| [직렬화](./java/serialization.md) | Serializable, JSON 직렬화 | 프로그래밍 언어란 |
| [Java 버전별 기능](./java/java-versions.md) | Java 8/11/17/21 주요 기능 | 프로그래밍 언어란 |
| [Null Safety](./kotlin/null-safety.md) | Kotlin 널 안정성 | Kotlin 기초 |
| [Coroutine](./kotlin/coroutine.md) | 비동기 프로그래밍 | 동시성 |

### [4] 심화

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [리플렉션](./java/reflection.md) | 런타임 클래스 분석/조작 | JVM 구조, 어노테이션 |
