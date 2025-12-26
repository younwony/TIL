# Java 어노테이션 (Annotation)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](../what-is-language.md)

> 코드에 메타데이터를 추가하여 컴파일러나 런타임에 정보를 제공하는 기능

`#어노테이션` `#Annotation` `#Java` `#메타데이터` `#Metadata` `#@Override` `#@Deprecated` `#@SuppressWarnings` `#@Retention` `#@Target` `#@Inherited` `#@Documented` `#커스텀어노테이션` `#CustomAnnotation` `#리플렉션` `#Reflection` `#Spring` `#JPA` `#Lombok` `#ElementType` `#RetentionPolicy` `#메타어노테이션`

## 왜 알아야 하는가?

현대 Java 프레임워크는 어노테이션 기반입니다. Spring의 @Autowired, JPA의 @Entity, Lombok의 @Data 등 어노테이션 없이는 개발이 어렵습니다. 커스텀 어노테이션을 만들면 반복 코드를 줄이고 선언적 프로그래밍이 가능합니다.

## 핵심 개념

- **어노테이션**: `@` 기호로 시작하는 메타데이터
- **메타 어노테이션**: 어노테이션을 정의하는 어노테이션
- **Retention**: 어노테이션 유지 범위 (소스/클래스/런타임)
- **Target**: 어노테이션 적용 대상 (클래스/메서드/필드 등)

## 쉽게 이해하기

**어노테이션**을 포스트잇에 비유할 수 있습니다.

```
코드에 붙이는 포스트잇 메모:

┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  @Override    ← "이 메서드는 부모 메서드를 재정의한 거야"     │
│  public void run() { }                                      │
│                                                              │
│  @Deprecated  ← "이 메서드는 더 이상 사용하지 마"            │
│  public void oldMethod() { }                                │
│                                                              │
│  @Autowired   ← "Spring아, 여기에 의존성 주입해줘"           │
│  private Service service;                                    │
│                                                              │
│  @Entity      ← "JPA야, 이건 데이터베이스 테이블이야"         │
│  public class User { }                                       │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 기본 제공 어노테이션

```java
public class Example {

    // @Override: 메서드 재정의 확인
    @Override
    public String toString() {
        return "Example";
    }

    // @Deprecated: 사용 자제 권고
    @Deprecated
    public void oldMethod() { }

    // @SuppressWarnings: 경고 무시
    @SuppressWarnings("unchecked")
    public void rawTypeMethod() {
        List list = new ArrayList();
    }

    // @FunctionalInterface: 함수형 인터페이스 확인
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    // @SafeVarargs: 가변인자 타입 안전성 보장
    @SafeVarargs
    public final <T> void process(T... items) { }
}
```

### 메타 어노테이션

```java
// @Retention: 어노테이션 유지 범위
@Retention(RetentionPolicy.SOURCE)   // 소스 코드에서만 (컴파일 후 제거)
@Retention(RetentionPolicy.CLASS)    // 클래스 파일까지 (런타임 접근 불가) - 기본값
@Retention(RetentionPolicy.RUNTIME)  // 런타임까지 (리플렉션으로 접근 가능)

// @Target: 어노테이션 적용 대상
@Target(ElementType.TYPE)            // 클래스, 인터페이스, enum
@Target(ElementType.FIELD)           // 필드
@Target(ElementType.METHOD)          // 메서드
@Target(ElementType.PARAMETER)       // 메서드 파라미터
@Target(ElementType.CONSTRUCTOR)     // 생성자
@Target(ElementType.LOCAL_VARIABLE)  // 지역 변수
@Target(ElementType.ANNOTATION_TYPE) // 어노테이션
@Target({ElementType.TYPE, ElementType.METHOD}) // 복수 지정

// @Inherited: 하위 클래스에 상속
@Inherited  // 부모 클래스의 어노테이션이 자식에게 전달

// @Documented: Javadoc에 포함
@Documented

// @Repeatable: 반복 적용 가능 (Java 8+)
@Repeatable(Schedules.class)
```

### 커스텀 어노테이션 정의

```java
// 기본 형태
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}

// 속성 포함
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";           // 기본값 설정
    int length() default 255;
    boolean nullable() default true;
}

// 사용
public class User {
    @Column(name = "user_name", length = 50, nullable = false)
    private String name;

    @Column  // 기본값 사용
    private String email;
}
```

### 어노테이션 처리 (리플렉션)

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    String message() default "값이 null일 수 없습니다";
}

public class User {
    @NotNull(message = "이름은 필수입니다")
    private String name;

    @NotNull
    private String email;
}

// 검증 처리
public class Validator {
    public static void validate(Object obj) throws Exception {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(NotNull.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value == null) {
                    NotNull annotation = field.getAnnotation(NotNull.class);
                    throw new IllegalArgumentException(annotation.message());
                }
            }
        }
    }
}

// 사용
User user = new User();
user.setName(null);
Validator.validate(user);  // 예외: "이름은 필수입니다"
```

### 실무 활용 예시

```java
// 1. 로깅 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Logged {
    String value() default "";
}

// AOP로 처리
@Aspect
public class LoggingAspect {
    @Around("@annotation(logged)")
    public Object log(ProceedingJoinPoint pjp, Logged logged) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(pjp.getSignature() + " 실행 시간: " + elapsed + "ms");
        return result;
    }
}

// 2. 권한 체크 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireRole {
    String[] value();
}

@RequireRole({"ADMIN", "MANAGER"})
public void deleteUser(Long userId) { }

// 3. API 버전 관리
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersion {
    int value();
}

@ApiVersion(2)
@GetMapping("/users")
public List<UserDto> getUsers() { }
```

### 반복 가능 어노테이션 (Java 8+)

```java
// 컨테이너 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedules {
    Schedule[] value();
}

// 반복 가능 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Schedules.class)
public @interface Schedule {
    String cron();
}

// 사용
public class Scheduler {
    @Schedule(cron = "0 0 * * *")     // 매일 자정
    @Schedule(cron = "0 12 * * *")    // 매일 정오
    public void runJob() { }
}

// 조회
Method method = Scheduler.class.getMethod("runJob");
Schedule[] schedules = method.getAnnotationsByType(Schedule.class);
for (Schedule schedule : schedules) {
    System.out.println(schedule.cron());
}
```

### 컴파일 타임 어노테이션 처리

```java
// Lombok 예시 - 컴파일 시 코드 생성
@Data  // getter, setter, toString, equals, hashCode 자동 생성
public class User {
    private String name;
    private int age;
}

// 컴파일 후 생성되는 코드
public class User {
    private String name;
    private int age;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    // toString, equals, hashCode...
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 선언적 프로그래밍 | 마법 같은 동작 (이해 어려움) |
| 보일러플레이트 제거 | 디버깅 어려움 |
| 표준화된 방식 | 리플렉션 성능 오버헤드 |
| IDE 지원 | 과도한 사용 시 가독성 저하 |

## 면접 예상 질문

### Q: @Retention의 세 가지 정책의 차이는?

A: **SOURCE**: 컴파일러가 사용하고 버림 (@Override, @SuppressWarnings). **CLASS**: 클래스 파일에 포함, 런타임에 접근 불가 (기본값). **RUNTIME**: 런타임에 리플렉션으로 접근 가능 (Spring, JPA 어노테이션). **선택 기준**: 런타임 처리 필요하면 RUNTIME, 컴파일 검증만 필요하면 SOURCE.

### Q: 커스텀 어노테이션을 만들어 본 경험이 있나요?

A: 예시 답변: "로깅/모니터링 어노테이션을 만들었습니다. @Logged 어노테이션을 메서드에 붙이면 AOP로 실행 시간을 측정하고 로그를 남깁니다. @Retention(RUNTIME), @Target(METHOD)로 정의하고, Spring AOP의 @Around로 처리했습니다. 기존 보일러플레이트 로깅 코드를 제거하고 선언적으로 바꿀 수 있었습니다."

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [리플렉션](./reflection.md) | 런타임 처리 | [4] 심화 |
| [Spring AOP](../spring/spring-aop.md) | 어노테이션 활용 | [3] 중급 |

## 참고 자료

- [Java Annotations - Oracle](https://docs.oracle.com/javase/tutorial/java/annotations/)
- [Effective Java - Item 39-41](https://www.oreilly.com/library/view/effective-java/9780134686097/)
