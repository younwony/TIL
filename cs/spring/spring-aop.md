# Spring AOP (관점 지향 프로그래밍)

> `[3] 중급` · 선수 지식: [Spring DI/IoC](./spring-di-ioc.md), [프록시 패턴](../programming/design-pattern.md)

> 횡단 관심사를 모듈화하여 핵심 비즈니스 로직과 분리하는 프로그래밍 패러다임

`#AOP` `#AspectOrientedProgramming` `#관점지향프로그래밍` `#Spring` `#스프링` `#Aspect` `#Advice` `#Pointcut` `#JoinPoint` `#Weaving` `#위빙` `#Proxy` `#프록시` `#CGLib` `#JDK동적프록시` `#횡단관심사` `#CrossCuttingConcern` `#Before` `#After` `#Around` `#AfterReturning` `#AfterThrowing` `#트랜잭션` `#로깅` `#보안` `#캐싱` `#AspectJ`

## 왜 알아야 하는가?

AOP는 로깅, 트랜잭션, 보안, 캐싱 등 여러 클래스에 공통으로 적용되는 기능을 분리합니다. Spring의 @Transactional, @Cacheable 등이 모두 AOP로 구현됩니다. 코드 중복 제거와 유지보수성 향상에 필수입니다.

## 핵심 개념

- **Aspect**: 횡단 관심사를 모듈화한 것
- **Advice**: 실제 수행할 로직 (언제, 무엇을)
- **Pointcut**: 어디에 적용할지 (표현식)
- **JoinPoint**: Advice가 적용될 수 있는 지점
- **Weaving**: Aspect를 대상 객체에 연결

## 쉽게 이해하기

**AOP**를 아파트 관리에 비유할 수 있습니다.

```
AOP 없이 (각자 관리):
┌─────────────────────────────────────────────────────────────┐
│  101호: 전기 검침 + 수도 검침 + 가스 검침 + 경비 + 청소    │
│  102호: 전기 검침 + 수도 검침 + 가스 검침 + 경비 + 청소    │
│  103호: 전기 검침 + 수도 검침 + 가스 검침 + 경비 + 청소    │
│  → 모든 집이 같은 일을 반복!                                │
└─────────────────────────────────────────────────────────────┘

AOP 적용 (관리소):
┌─────────────────────────────────────────────────────────────┐
│  관리소 (Aspect):                                           │
│  - 전기 검침팀 → 모든 호수에 적용                           │
│  - 수도 검침팀 → 모든 호수에 적용                           │
│  - 경비팀 → 모든 호수에 적용                                │
│                                                              │
│  101호, 102호, 103호: 자기 일(핵심 로직)만 집중!            │
└─────────────────────────────────────────────────────────────┘

횡단 관심사 = 여러 곳에 공통으로 적용되는 기능
핵심 관심사 = 각 클래스의 고유 비즈니스 로직
```

## 상세 설명

### AOP 용어

```
┌─────────────────────────────────────────────────────────────┐
│                    AOP 구성 요소                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Target Object (대상 객체)                                   │
│  ┌───────────────────────────────────────┐                  │
│  │  OrderService                         │                  │
│  │  ┌─────────────────────────────────┐  │                  │
│  │  │ createOrder()  ← JoinPoint      │  │                  │
│  │  │ cancelOrder()  ← JoinPoint      │  │                  │
│  │  │ getOrder()     ← JoinPoint      │  │                  │
│  │  └─────────────────────────────────┘  │                  │
│  └───────────────────────────────────────┘                  │
│                    ↑                                         │
│  Aspect (관점)     │                                         │
│  ┌─────────────────┴─────────────────────┐                  │
│  │  Pointcut: "createOrder*" 메서드      │                  │
│  │  Advice: 트랜잭션 시작/커밋           │                  │
│  └───────────────────────────────────────┘                  │
│                                                              │
│  Weaving: 컴파일/로드/런타임에 Aspect 적용                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Advice 종류

```java
@Aspect
@Component
public class LoggingAspect {

    // 1. @Before: 메서드 실행 전
    @Before("execution(* com.example.service.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info("메서드 시작: {}", joinPoint.getSignature().getName());
    }

    // 2. @After: 메서드 실행 후 (성공/실패 무관)
    @After("execution(* com.example.service.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
        log.info("메서드 종료: {}", joinPoint.getSignature().getName());
    }

    // 3. @AfterReturning: 정상 반환 후
    @AfterReturning(
        pointcut = "execution(* com.example.service.*.*(..))",
        returning = "result"
    )
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        log.info("반환값: {}", result);
    }

    // 4. @AfterThrowing: 예외 발생 후
    @AfterThrowing(
        pointcut = "execution(* com.example.service.*.*(..))",
        throwing = "ex"
    )
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
        log.error("예외 발생: {}", ex.getMessage());
    }

    // 5. @Around: 메서드 실행 전후 (가장 강력)
    @Around("execution(* com.example.service.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();  // 원본 메서드 실행
            return result;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info("실행 시간: {}ms", elapsed);
        }
    }
}
```

### Pointcut 표현식

```java
@Aspect
@Component
public class PointcutExamples {

    // 1. execution: 메서드 실행 시점
    // execution(접근제어자 반환타입 패키지.클래스.메서드(파라미터))

    // 모든 public 메서드
    @Pointcut("execution(public * *(..))")
    public void allPublicMethods() {}

    // OrderService의 모든 메서드
    @Pointcut("execution(* com.example.service.OrderService.*(..))")
    public void orderServiceMethods() {}

    // service 패키지의 모든 클래스의 모든 메서드
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceLayerMethods() {}

    // service 패키지 및 하위 패키지
    @Pointcut("execution(* com.example.service..*.*(..))")
    public void serviceAndSubPackages() {}

    // 특정 파라미터 타입
    @Pointcut("execution(* *..OrderService.*(Long, ..))")
    public void methodsWithLongFirstParam() {}

    // 2. within: 특정 타입 내의 모든 메서드
    @Pointcut("within(com.example.service.OrderService)")
    public void withinOrderService() {}

    // 3. @annotation: 특정 어노테이션이 붙은 메서드
    @Pointcut("@annotation(com.example.annotation.Logged)")
    public void loggedMethods() {}

    // 4. @within: 특정 어노테이션이 붙은 클래스의 메서드
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceBeans() {}

    // 5. bean: 특정 빈의 메서드
    @Pointcut("bean(orderService)")
    public void orderServiceBean() {}

    @Pointcut("bean(*Service)")  // Service로 끝나는 빈
    public void allServiceBeans() {}

    // 6. 조합
    @Pointcut("execution(* com.example.service.*.*(..)) && @annotation(Transactional)")
    public void transactionalServiceMethods() {}

    @Pointcut("serviceLayerMethods() || repositoryLayerMethods()")
    public void dataAccessMethods() {}

    @Pointcut("allPublicMethods() && !orderServiceMethods()")
    public void publicMethodsExceptOrderService() {}
}
```

### 실무 활용 예시

```java
// 1. 로깅 Aspect
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@annotation(Logged)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();

        log.info("[START] {} args={}", methodName, Arrays.toString(args));

        try {
            Object result = pjp.proceed();
            log.info("[END] {} return={}", methodName, result);
            return result;
        } catch (Exception e) {
            log.error("[ERROR] {} exception={}", methodName, e.getMessage());
            throw e;
        }
    }
}

// 2. 성능 측정 Aspect
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* com.example.service..*.*(..))")
    public Object measureTime(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return pjp.proceed();
        } finally {
            stopWatch.stop();
            if (stopWatch.getTotalTimeMillis() > 1000) {
                log.warn("느린 메서드 감지: {} ({}ms)",
                    pjp.getSignature(), stopWatch.getTotalTimeMillis());
            }
        }
    }
}

// 3. 권한 체크 Aspect
@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint jp, RequireRole requireRole) {
        String[] requiredRoles = requireRole.value();
        User currentUser = SecurityContext.getCurrentUser();

        boolean hasRole = Arrays.stream(requiredRoles)
            .anyMatch(role -> currentUser.hasRole(role));

        if (!hasRole) {
            throw new AccessDeniedException("권한이 없습니다");
        }
    }
}

// 커스텀 어노테이션
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String[] value();
}

// 사용
@Service
public class AdminService {
    @RequireRole({"ADMIN", "SUPER_ADMIN"})
    public void deleteUser(Long userId) {
        // ...
    }
}

// 4. 재시도 Aspect
@Aspect
@Component
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint pjp, Retry retry) throws Throwable {
        int maxAttempts = retry.maxAttempts();
        long delay = retry.delay();
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return pjp.proceed();
            } catch (Exception e) {
                lastException = e;
                log.warn("재시도 {}/{}: {}", attempt, maxAttempts, e.getMessage());

                if (attempt < maxAttempts) {
                    Thread.sleep(delay);
                }
            }
        }
        throw lastException;
    }
}
```

### 프록시 동작 원리

```
┌─────────────────────────────────────────────────────────────┐
│                  Spring AOP 프록시                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  호출: orderService.createOrder()                           │
│          │                                                   │
│          ▼                                                   │
│  ┌─────────────────────────────────────────┐                │
│  │  Proxy (대리자)                          │                │
│  │  ┌─────────────────────────────────────┐│                │
│  │  │ 1. Before Advice 실행               ││                │
│  │  │ 2. 실제 메서드 호출 ──────────────┐ ││                │
│  │  │ 3. After Advice 실행              │ ││                │
│  │  └───────────────────────────────────┼─┘│                │
│  └──────────────────────────────────────┼──┘                │
│                                         │                    │
│                                         ▼                    │
│  ┌──────────────────────────────────────────┐               │
│  │  Target (실제 OrderService)              │               │
│  │  createOrder() 실행                      │               │
│  └──────────────────────────────────────────┘               │
│                                                              │
│  프록시 종류:                                                │
│  - JDK 동적 프록시: 인터페이스 기반                          │
│  - CGLIB: 클래스 상속 기반 (기본값)                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

```java
// JDK 동적 프록시 vs CGLIB
// 인터페이스가 있으면: JDK 동적 프록시 (기본) 또는 CGLIB
// 인터페이스가 없으면: CGLIB만 가능

// 강제로 CGLIB 사용
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class AopConfig { }

// Spring Boot 2.0+: 기본적으로 CGLIB 사용
```

### 내부 호출 문제

```java
@Service
public class OrderService {

    @Transactional
    public void createOrder() {
        // ...
        this.sendNotification();  // 프록시 우회! AOP 미적용!
    }

    @Transactional
    public void sendNotification() {
        // AOP가 적용되지 않음
    }
}

// 해결 방법 1: 자기 자신 주입
@Service
public class OrderService {
    @Autowired
    private OrderService self;  // 프록시 주입

    public void createOrder() {
        self.sendNotification();  // 프록시 통해 호출
    }
}

// 해결 방법 2: AopContext (비권장)
@EnableAspectJAutoProxy(exposeProxy = true)
public class OrderService {
    public void createOrder() {
        ((OrderService) AopContext.currentProxy()).sendNotification();
    }
}

// 해결 방법 3: 클래스 분리 (권장)
@Service
public class OrderService {
    private final NotificationService notificationService;

    public void createOrder() {
        notificationService.sendNotification();  // 다른 빈 호출
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 횡단 관심사 분리 | 디버깅 어려움 |
| 코드 중복 제거 | 내부 호출 문제 |
| 유지보수성 향상 | 프록시 오버헤드 |
| OCP 원칙 준수 | 학습 곡선 |

## 면접 예상 질문

### Q: Spring AOP와 AspectJ의 차이는?

A: **Spring AOP**: 프록시 기반, 메서드 실행 JoinPoint만 지원, 런타임 위빙, Spring Bean에만 적용. **AspectJ**: 바이트코드 조작, 모든 JoinPoint 지원 (필드, 생성자 등), 컴파일/로드 타임 위빙, 모든 객체 적용 가능. **선택 기준**: 대부분 Spring AOP로 충분, 생성자/필드 AOP 필요 시 AspectJ.

### Q: @Transactional이 내부 호출에서 동작하지 않는 이유는?

A: Spring AOP는 **프록시 기반**이므로 외부에서 호출해야 프록시를 거칩니다. `this.method()` 호출은 프록시를 우회하여 실제 객체를 직접 호출합니다. **해결**: (1) 클래스 분리 (권장) (2) 자기 자신 주입 (3) AopContext.currentProxy() 사용.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Spring DI/IoC](./spring-di-ioc.md) | 선수 지식 | [3] 중급 |
| [프록시 패턴](../programming/design-pattern.md) | 동작 원리 | [3] 중급 |
| [트랜잭션 관리](./spring-transaction.md) | AOP 활용 | [3] 중급 |
| [어노테이션](../language/java/annotation.md) | 커스텀 AOP | [3] 중급 |

## 참고 자료

- [Spring AOP Documentation](https://docs.spring.io/spring-framework/reference/core/aop.html)
- [Baeldung - Spring AOP](https://www.baeldung.com/spring-aop)
