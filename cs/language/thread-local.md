# ThreadLocal

> 각 스레드가 독립적으로 자신만의 변수 복사본을 가질 수 있게 해주는 Java 클래스

## 핵심 개념

- **스레드 격리**: 같은 ThreadLocal 변수라도 각 스레드는 자신만의 독립적인 값을 가짐
- **암묵적 전달**: 메서드 파라미터 없이 스레드 내에서 데이터를 전달할 수 있음
- **Thread 객체 내부 저장**: 실제 값은 각 Thread 객체의 ThreadLocalMap에 저장됨
- **초기값 제공**: `withInitial()`로 Lazy 초기화 가능
- **메모리 누수 주의**: 사용 후 반드시 `remove()` 호출 필요

## 쉽게 이해하기

**ThreadLocal**을 사물함에 비유할 수 있습니다.

회사에 공용 사물함이 있다고 상상해보세요. 일반 변수는 모든 직원이 같은 사물함을 공유하는 것과 같습니다.
A 직원이 넣은 물건을 B 직원이 꺼내갈 수 있어 혼란이 생깁니다.

ThreadLocal은 **직원마다 개인 사물함**을 배정하는 것과 같습니다:
- 같은 "사물함 번호"(ThreadLocal 변수)를 말해도
- 각 직원(스레드)은 자신만의 사물함에 접근
- A 직원이 넣은 물건은 A만 꺼낼 수 있음

예를 들어, 웹 서버에서 각 요청을 처리하는 스레드가 있을 때:
- 사용자 A의 요청 → 스레드 1 → ThreadLocal에 "사용자 A 정보" 저장
- 사용자 B의 요청 → 스레드 2 → ThreadLocal에 "사용자 B 정보" 저장
- 스레드 1이 ThreadLocal을 읽으면 "사용자 A 정보"만 나옴

단, 실제로는 사물함과 달리 **스레드가 끝나도 자동으로 비워지지 않아** 직접 청소(remove)해야 합니다.

## 상세 설명

### 왜 ThreadLocal이 필요한가?

**문제 상황**: 멀티스레드 환경에서 각 스레드가 독립적인 상태를 가져야 할 때

```java
// Bad - 모든 스레드가 같은 변수 공유
public class UserContext {
    public static User currentUser;  // 위험! 스레드 간 덮어쓰기 발생
}
```

**왜 위험한가?**
- 스레드 A가 `currentUser = userA` 설정
- 스레드 B가 `currentUser = userB` 설정
- 스레드 A가 `currentUser` 읽으면 → userB가 나옴 (데이터 오염)

**해결책**: ThreadLocal 사용

```java
// Good - 각 스레드가 독립적인 값 보유
public class UserContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void set(User user) {
        currentUser.set(user);
    }

    public static User get() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}
```

### 언제 사용하나?

**사용 시점**:
- 요청별 사용자 인증 정보 저장 (Spring Security의 SecurityContextHolder)
- 트랜잭션 컨텍스트 전파 (Spring의 @Transactional)
- 요청별 로깅 컨텍스트 (MDC - Mapped Diagnostic Context)
- 데이터베이스 커넥션 관리

**왜 이 상황에서 사용하나?**

"메서드 파라미터로 전달하면 되지 않나요?" 라는 의문이 생길 수 있습니다.

파라미터 전달 방식의 문제:
- **깊은 호출 스택**: Controller → Service → Repository → Utility 모든 곳에 파라미터 추가 필요
- **시그니처 오염**: 비즈니스와 무관한 파라미터가 메서드 시그니처를 오염
- **레거시 코드**: 기존 메서드 시그니처 변경이 어려움

**이점**:
- 호출 스택 어디서든 `ThreadLocal.get()`으로 접근 가능
- 메서드 시그니처 변경 불필요
- 관심사 분리 (횡단 관심사를 비즈니스 로직에서 분리)

**사용하지 않으면?**
- 모든 메서드에 컨텍스트 파라미터 추가 → 코드 복잡도 증가
- 또는 전역 변수 사용 → 스레드 안전성 문제

### 동작 원리

```
┌─────────────────────────────────────────────────────────────┐
│                        JVM                                   │
│                                                              │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐ │
│  │   Thread 1   │     │   Thread 2   │     │   Thread 3   │ │
│  │              │     │              │     │              │ │
│  │ ThreadLocal- │     │ ThreadLocal- │     │ ThreadLocal- │ │
│  │    Map       │     │    Map       │     │    Map       │ │
│  │ ┌─────────┐  │     │ ┌─────────┐  │     │ ┌─────────┐  │ │
│  │ │ TL1: A  │  │     │ │ TL1: X  │  │     │ │ TL1: P  │  │ │
│  │ │ TL2: B  │  │     │ │ TL2: Y  │  │     │ │ TL2: Q  │  │ │
│  │ └─────────┘  │     │ └─────────┘  │     │ └─────────┘  │ │
│  └──────────────┘     └──────────────┘     └──────────────┘ │
│                                                              │
│  ThreadLocal 변수(TL1, TL2)는 하나지만,                        │
│  각 Thread의 ThreadLocalMap에 별도 값 저장                     │
└─────────────────────────────────────────────────────────────┘
```

**내부 구조**:

1. 각 `Thread` 객체는 `ThreadLocal.ThreadLocalMap` 필드를 가짐
2. `ThreadLocalMap`은 `ThreadLocal` 객체를 Key로, 실제 값을 Value로 저장
3. `threadLocal.get()` 호출 시:
   - 현재 스레드의 `ThreadLocalMap` 가져옴
   - `ThreadLocal` 객체를 Key로 값 조회

```java
// ThreadLocal.get()의 단순화된 로직
public T get() {
    Thread currentThread = Thread.currentThread();
    ThreadLocalMap map = currentThread.threadLocals;  // 현재 스레드의 Map
    if (map != null) {
        Entry e = map.getEntry(this);  // this = ThreadLocal 객체가 Key
        if (e != null) {
            return (T) e.value;
        }
    }
    return setInitialValue();
}
```

## 예제 코드

### 기본 사용법

```java
public class ThreadLocalExample {
    // 1. 기본 생성
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // 2. 초기값과 함께 생성 (권장)
    private static final ThreadLocal<Integer> counter =
        ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        // 값 설정
        threadLocal.set("Hello from main thread");
        counter.set(counter.get() + 1);

        // 값 조회
        System.out.println(threadLocal.get());  // "Hello from main thread"
        System.out.println(counter.get());       // 1

        // 새로운 스레드에서 확인
        new Thread(() -> {
            System.out.println(threadLocal.get());  // null (다른 스레드)
            System.out.println(counter.get());       // 0 (초기값)
        }).start();

        // 사용 완료 후 반드시 제거
        threadLocal.remove();
        counter.remove();
    }
}
```

### 실무 예제: 요청별 사용자 컨텍스트

```java
/**
 * 현재 요청의 사용자 정보를 저장하는 컨텍스트 홀더
 * Spring Security의 SecurityContextHolder와 유사한 패턴
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> contextHolder =
        ThreadLocal.withInitial(UserContext::new);

    public static UserContext getContext() {
        return contextHolder.get();
    }

    public static void setContext(UserContext context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();  // 메모리 누수 방지
    }
}

// 필터에서 설정
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            // 요청 시작: 사용자 정보 설정
            User user = extractUserFromRequest((HttpServletRequest) request);
            UserContext context = new UserContext(user);
            UserContextHolder.setContext(context);

            chain.doFilter(request, response);
        } finally {
            // 요청 종료: 반드시 정리 (finally에서!)
            UserContextHolder.clearContext();
        }
    }
}

// 서비스 어디서든 사용
@Service
public class OrderService {
    public void createOrder(OrderRequest request) {
        // 파라미터 없이 현재 사용자 정보 접근
        User currentUser = UserContextHolder.getContext().getUser();

        Order order = new Order(currentUser, request.getItems());
        orderRepository.save(order);
    }
}
```

### InheritableThreadLocal: 자식 스레드로 값 전파

```java
public class InheritableExample {
    // 일반 ThreadLocal: 자식 스레드로 전파 안 됨
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    // InheritableThreadLocal: 자식 스레드로 값 복사
    private static final InheritableThreadLocal<String> inheritableThreadLocal =
        new InheritableThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("Parent Value");
        inheritableThreadLocal.set("Inheritable Parent Value");

        new Thread(() -> {
            System.out.println("ThreadLocal: " + threadLocal.get());
            // null

            System.out.println("Inheritable: " + inheritableThreadLocal.get());
            // "Inheritable Parent Value"
        }).start();
    }
}
```

**주의**: `InheritableThreadLocal`은 스레드 풀 환경에서 문제 발생
- 스레드 풀의 스레드는 재사용되므로, 스레드 생성 시점의 부모 값이 계속 유지
- 해결책: Alibaba의 TransmittableThreadLocal 라이브러리 사용

## 주의사항

- **메모리 누수 (Memory Leak)**
  - 왜 문제인가: ThreadLocal의 Entry는 WeakReference로 Key(ThreadLocal 객체)를 참조하지만,
    Value는 Strong Reference로 참조함. 스레드 풀 환경에서 스레드가 재사용되면
    이전 요청의 Value가 GC되지 않고 남아있을 수 있음
  - 발생 상황:
    - 웹 서버의 스레드 풀 (Tomcat, Netty 등)
    - ExecutorService로 생성한 스레드 풀
    - 스레드가 종료되지 않고 재사용되는 모든 환경
  - 해결 방법:
    - **반드시 `finally` 블록에서 `remove()` 호출**
    - Spring의 경우 `RequestContextHolder`가 자동 정리해주지만, 커스텀 ThreadLocal은 직접 관리 필요
    - Filter, Interceptor의 `afterCompletion`에서 정리

```java
// Bad - 메모리 누수 발생 가능
public void processRequest(Request request) {
    threadLocal.set(extractData(request));
    doSomething();
    // remove() 호출 없음 → 스레드 재사용 시 이전 값 남아있음
}

// Good - finally에서 반드시 정리
public void processRequest(Request request) {
    try {
        threadLocal.set(extractData(request));
        doSomething();
    } finally {
        threadLocal.remove();  // 예외 발생해도 반드시 실행
    }
}
```

- **스레드 풀과의 호환성 문제**
  - 왜 문제인가: 스레드 풀의 스레드는 작업 완료 후 종료되지 않고 다음 작업을 위해 대기함.
    이전 작업에서 설정한 ThreadLocal 값이 다음 작업에 영향을 줄 수 있음
  - 발생 상황:
    - `@Async`로 비동기 처리 시 부모 스레드의 ThreadLocal 값 접근 불가
    - `CompletableFuture`, `parallelStream` 사용 시 값 전파 안 됨
  - 해결 방법:
    - 작업 제출 전 값 복사, 작업 시작 시 설정, 작업 종료 시 정리
    - TransmittableThreadLocal (alibaba/transmittable-thread-local) 사용
    - Spring의 경우 `DelegatingSecurityContextRunnable` 활용

- **InheritableThreadLocal의 한계**
  - 왜 문제인가: 스레드 **생성 시점**에만 부모 값을 복사함. 스레드 풀에서는 스레드가
    이미 생성되어 있으므로 부모 값이 전파되지 않음
  - 발생 상황: 스레드 풀 환경에서 `InheritableThreadLocal` 사용 시
  - 해결 방법: TransmittableThreadLocal 사용

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 스레드 안전성 보장 (락 없이) | 메모리 누수 위험 (remove 필수) |
| 메서드 시그니처 오염 방지 | 암묵적 의존성으로 코드 추적 어려움 |
| 횡단 관심사 분리 용이 | 테스트 시 설정/정리 필요 |
| 동기화 오버헤드 없음 | 스레드 풀 환경에서 주의 필요 |
| 전역 접근 가능 | 과도한 사용 시 전역 상태 남용 |

**결론**: 요청 컨텍스트, 트랜잭션 관리 등 **명확한 생명주기**가 있는 데이터에 적합.
남용하면 전역 변수와 같은 문제가 발생하므로 신중하게 사용해야 함.

## 면접 예상 질문

- Q: ThreadLocal은 내부적으로 어떻게 동작하나요?
  - A: 각 Thread 객체는 내부에 `ThreadLocalMap`이라는 Map을 가지고 있습니다.
    `ThreadLocal.set(value)` 호출 시, 현재 스레드의 ThreadLocalMap에
    ThreadLocal 객체를 Key로, value를 Value로 저장합니다.
    **왜 Thread 객체 안에 저장하나요?** 스레드가 종료되면 Thread 객체가 GC되면서
    ThreadLocalMap도 함께 정리되기 때문입니다. 다만 스레드 풀 환경에서는
    스레드가 재사용되므로 명시적 `remove()` 호출이 필요합니다.

- Q: ThreadLocal 사용 시 주의할 점은 무엇인가요?
  - A: 가장 중요한 것은 **메모리 누수 방지**입니다. 스레드 풀 환경에서는 스레드가
    재사용되므로 이전 요청의 데이터가 남아있을 수 있습니다.
    **왜냐하면** ThreadLocalMap의 Entry가 Value를 Strong Reference로 참조하기 때문입니다.
    **따라서** 반드시 `finally` 블록이나 Filter/Interceptor의 정리 메서드에서
    `remove()`를 호출해야 합니다. 추가로, 비동기 처리(`@Async`, `CompletableFuture`) 시
    값이 자동 전파되지 않으므로 별도 처리가 필요합니다.

- Q: ThreadLocal과 synchronized의 차이점은 무엇인가요?
  - A: 목적이 다릅니다. `synchronized`는 **여러 스레드가 같은 데이터에 안전하게 접근**하기 위한 것이고,
    `ThreadLocal`은 **각 스레드가 독립적인 데이터를 가지기 위한 것**입니다.
    **비유하자면** synchronized는 "화장실 하나를 여러 명이 순서대로 사용"이고,
    ThreadLocal은 "각자 개인 화장실 보유"입니다.
    **따라서** 데이터 공유가 목적이면 synchronized, 데이터 격리가 목적이면 ThreadLocal을 사용합니다.

- Q: Spring에서 ThreadLocal은 어디에 활용되나요?
  - A: 대표적으로 세 가지가 있습니다:
    1. **SecurityContextHolder**: 현재 인증된 사용자 정보 저장
    2. **TransactionSynchronizationManager**: 트랜잭션 컨텍스트 관리
    3. **RequestContextHolder**: 현재 HTTP 요청 정보 접근

    **왜 ThreadLocal을 사용하나요?** 웹 요청은 하나의 스레드가 처리하므로,
    요청 시작 시 ThreadLocal에 저장하면 해당 요청을 처리하는 모든 코드에서
    파라미터 전달 없이 접근할 수 있기 때문입니다.

## 참고 자료

- [Java ThreadLocal 공식 문서](https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html)
- [Baeldung - An Introduction to ThreadLocal in Java](https://www.baeldung.com/java-threadlocal)
- [TransmittableThreadLocal GitHub](https://github.com/alibaba/transmittable-thread-local)
