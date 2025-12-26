# Coroutine (코루틴)

> `[4] 심화` · 선수 지식: [동시성](./concurrency.md), [Null Safety](./null-safety.md)

> 비동기 프로그래밍을 동기식 코드처럼 작성할 수 있게 하는 경량 동시성 기법

`#코루틴` `#Coroutine` `#비동기` `#Async` `#suspend` `#launch` `#async` `#await` `#runBlocking` `#CoroutineScope` `#CoroutineContext` `#Dispatcher` `#Job` `#Deferred` `#Flow` `#Channel` `#구조화된동시성` `#StructuredConcurrency` `#취소` `#Cancellation` `#예외처리` `#SupervisorJob`

## 왜 알아야 하는가?

코루틴은 Kotlin의 핵심 비동기 솔루션입니다. 콜백 지옥 없이 비동기 코드를 순차적으로 작성할 수 있습니다. Android, Spring 등 현대 Kotlin 개발에서 필수적이며, 스레드보다 효율적인 동시성을 제공합니다.

## 핵심 개념

- **suspend 함수**: 일시 중단 가능한 함수
- **CoroutineScope**: 코루틴의 수명 범위
- **Dispatcher**: 코루틴이 실행될 스레드 지정
- **Job**: 코루틴의 핸들 (취소, 대기)

## 쉽게 이해하기

**코루틴**을 식당에 비유할 수 있습니다.

- **스레드**: 웨이터 1명이 한 테이블만 담당 (비효율)
- **코루틴**: 웨이터 1명이 여러 테이블 담당
  - 주문받고 → 다른 테이블로 (suspend)
  - 음식 나오면 → 다시 그 테이블로 (resume)

**핵심**: 기다리는 동안 다른 일을 함

## 상세 설명

### 코루틴 vs 스레드

```
스레드:
┌──────────────────────────────────────────┐
│Thread 1: [──작업A──][──대기──][──작업A──] │
│Thread 2: [──작업B──][──대기──][──작업B──] │
│Thread 3: [──작업C──][──대기──][──작업C──] │
└──────────────────────────────────────────┘
각 스레드가 대기 중에도 메모리 점유

코루틴:
┌──────────────────────────────────────────┐
│Thread 1: [A][B][C][─대기─][A][B][C]      │
└──────────────────────────────────────────┘
하나의 스레드에서 작업 번갈아 실행
```

| 항목 | 스레드 | 코루틴 |
|------|-------|-------|
| 메모리 | ~1MB/스레드 | ~수KB/코루틴 |
| 생성 비용 | 높음 | 낮음 |
| 컨텍스트 스위칭 | OS 레벨 | 사용자 레벨 |
| 수량 | 수천 개 한계 | 수십만 개 가능 |

### 기본 사용법

```kotlin
// suspend 함수 정의
suspend fun fetchUser(): User {
    delay(1000)  // 비동기 대기 (스레드 블로킹 X)
    return User("John")
}

// 코루틴 실행
fun main() = runBlocking {  // 메인 스레드 블로킹
    val user = fetchUser()  // suspend 함수 호출
    println(user)
}
```

### 코루틴 빌더

```kotlin
// launch: 결과 없이 실행 (Job 반환)
val job: Job = launch {
    println("Hello")
}
job.join()  // 완료 대기

// async: 결과 반환 (Deferred<T> 반환)
val deferred: Deferred<Int> = async {
    delay(1000)
    42
}
val result = deferred.await()  // 결과 대기

// runBlocking: 현재 스레드 블로킹 (테스트용)
runBlocking {
    delay(1000)
}
```

### Dispatcher (실행 스레드)

```kotlin
launch(Dispatchers.Main) {
    // UI 스레드 (Android)
}

launch(Dispatchers.IO) {
    // I/O 작업 (네트워크, 파일)
}

launch(Dispatchers.Default) {
    // CPU 집약적 작업
}

launch(Dispatchers.Unconfined) {
    // 호출 스레드에서 시작, suspend 후 변경 가능
}
```

### CoroutineScope

```kotlin
// GlobalScope: 앱 수명과 동일 (권장 안 함)
GlobalScope.launch { ... }

// 구조화된 동시성: 부모-자식 관계
class MyViewModel : ViewModel() {
    // viewModelScope: ViewModel 수명과 동일
    fun loadData() {
        viewModelScope.launch {
            val data = fetchData()
            // ViewModel 종료 시 자동 취소
        }
    }
}

// 커스텀 스코프
val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
scope.launch { ... }
scope.cancel()  // 모든 자식 취소
```

### 구조화된 동시성

```kotlin
suspend fun fetchAll() = coroutineScope {
    val user = async { fetchUser() }
    val posts = async { fetchPosts() }

    UserWithPosts(user.await(), posts.await())
}

// 하나가 실패하면 모두 취소
// 모든 자식이 완료되어야 종료
```

### 취소 처리

```kotlin
val job = launch {
    repeat(1000) { i ->
        if (!isActive) return@launch  // 취소 확인
        // 또는
        ensureActive()  // 취소 시 CancellationException

        println("Working $i")
        delay(100)  // 취소 가능 지점
    }
}

delay(500)
job.cancel()  // 취소 요청
job.join()    // 완료 대기
```

### 예외 처리

```kotlin
// try-catch
launch {
    try {
        riskyOperation()
    } catch (e: Exception) {
        println("Error: $e")
    }
}

// CoroutineExceptionHandler
val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught $exception")
}

launch(handler) {
    throw RuntimeException("Error")
}

// SupervisorJob: 자식 실패가 다른 자식에 영향 없음
val scope = CoroutineScope(SupervisorJob())
```

### Flow (비동기 스트림)

```kotlin
// Cold Stream: 수집 시 실행
fun numbers(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)  // 값 방출
    }
}

// 수집
numbers()
    .map { it * 2 }
    .filter { it > 2 }
    .collect { println(it) }

// StateFlow: 상태 홀더 (Hot)
private val _state = MutableStateFlow(0)
val state: StateFlow<Int> = _state.asStateFlow()

// SharedFlow: 이벤트 브로드캐스트 (Hot)
private val _events = MutableSharedFlow<Event>()
val events: SharedFlow<Event> = _events.asSharedFlow()
```

### 패턴: 순차 vs 동시

```kotlin
// 순차 실행: 2초
suspend fun sequential() {
    val a = fetchA()  // 1초
    val b = fetchB()  // 1초
    println(a + b)
}

// 동시 실행: 1초
suspend fun concurrent() = coroutineScope {
    val a = async { fetchA() }  // 동시 시작
    val b = async { fetchB() }  // 동시 시작
    println(a.await() + b.await())
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 경량 (수십만 개 가능) | 학습 곡선 |
| 동기식 코드 스타일 | 디버깅 어려움 |
| 구조화된 동시성 | suspend 전파 |
| 취소 자동 전파 | 블로킹 코드와 혼용 주의 |

## 면접 예상 질문

### Q: 코루틴과 스레드의 차이는?

A: **스레드**: OS 수준, 높은 생성 비용(~1MB), 컨텍스트 스위칭 비용 큼. **코루틴**: 사용자 수준, 경량(~수KB), 중단점에서만 전환. **핵심**: 코루틴은 "협력적 멀티태스킹"으로 suspend 지점에서 자발적으로 양보하여 하나의 스레드에서 많은 작업을 처리합니다.

### Q: launch와 async의 차이는?

A: **launch**: 결과 없음, `Job` 반환, 예외 즉시 전파. **async**: 결과 있음, `Deferred<T>` 반환, 예외는 `await()` 시 전파. **사용**: 결과가 필요하면 async, 단순 실행이면 launch.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [동시성](./concurrency.md) | 선수 지식 | [3] 중급 |
| [Null Safety](./null-safety.md) | Kotlin 기초 | [3] 중급 |

## 참고 자료

- [Kotlin Coroutines - Official](https://kotlinlang.org/docs/coroutines-overview.html)
- Kotlin Coroutines: Deep Dive - Marcin Moskala
