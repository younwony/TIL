# Java 리플렉션 (Reflection)

> `[4] 심화` · 선수 지식: [JVM 구조](./jvm.md), [어노테이션](./annotation.md)

> 런타임에 클래스의 구조를 분석하고 조작할 수 있는 기능

`#리플렉션` `#Reflection` `#Java` `#런타임` `#Runtime` `#Class` `#Method` `#Field` `#Constructor` `#메타프로그래밍` `#Metaprogramming` `#동적로딩` `#DynamicLoading` `#프레임워크` `#Framework` `#Spring` `#JPA` `#Jackson` `#Proxy` `#invoke` `#setAccessible` `#Introspection`

## 왜 알아야 하는가?

Spring, JPA, Jackson 등 대부분의 Java 프레임워크는 리플렉션을 기반으로 동작합니다. DI, ORM 매핑, JSON 직렬화 모두 리플렉션을 사용합니다. 프레임워크를 이해하고 라이브러리를 개발하려면 리플렉션을 알아야 합니다.

## 핵심 개념

- **Class 객체**: 클래스의 메타데이터를 담는 객체
- **Field/Method/Constructor**: 클래스 멤버 접근
- **invoke()**: 메서드를 동적으로 호출
- **setAccessible(true)**: private 멤버 접근

## 쉽게 이해하기

**리플렉션**을 건물 설계도에 비유할 수 있습니다.

```
일반 프로그래밍:
  - 설계도(Class) → 건물 짓기(new) → 사용
  - 컴파일 시점에 모든 것이 결정됨

리플렉션:
  - 이미 지어진 건물을 보고 설계도 역분석
  - 런타임에 구조 파악, 동적 조작

┌─────────────────────────────────────────────────────────────┐
│                    리플렉션 비유                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  일반 코드:                                                  │
│  Person person = new Person();                              │
│  person.setName("Alice");      // 컴파일 시 메서드 확정      │
│                                                              │
│  리플렉션:                                                   │
│  Class<?> clazz = Class.forName("Person");                  │
│  Object person = clazz.newInstance();                       │
│  Method method = clazz.getMethod("setName", String.class);  │
│  method.invoke(person, "Alice"); // 런타임에 메서드 결정    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### Class 객체 얻기

```java
// 방법 1: 클래스 리터럴
Class<Person> clazz = Person.class;

// 방법 2: 인스턴스에서
Person person = new Person();
Class<?> clazz = person.getClass();

// 방법 3: 문자열로 (동적 로딩)
Class<?> clazz = Class.forName("com.example.Person");

// 기본 타입
Class<Integer> intClass = int.class;
Class<Integer> integerClass = Integer.class;
System.out.println(intClass == integerClass);  // false
```

### 클래스 정보 조회

```java
Class<Person> clazz = Person.class;

// 기본 정보
String name = clazz.getName();           // com.example.Person
String simpleName = clazz.getSimpleName(); // Person
Package pkg = clazz.getPackage();         // com.example
Class<?> superClass = clazz.getSuperclass(); // Object
Class<?>[] interfaces = clazz.getInterfaces();

// 수정자 (Modifier)
int modifiers = clazz.getModifiers();
boolean isPublic = Modifier.isPublic(modifiers);
boolean isAbstract = Modifier.isAbstract(modifiers);
```

### 필드 접근

```java
public class Person {
    private String name;
    public int age;
}

Class<Person> clazz = Person.class;

// public 필드만 (상속 포함)
Field[] publicFields = clazz.getFields();

// 선언된 모든 필드 (private 포함, 상속 제외)
Field[] allFields = clazz.getDeclaredFields();

// 특정 필드
Field nameField = clazz.getDeclaredField("name");

// private 필드 접근
Person person = new Person();
nameField.setAccessible(true);  // private 접근 허용
nameField.set(person, "Alice"); // 값 설정
String name = (String) nameField.get(person); // 값 조회
```

### 메서드 호출

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    private int multiply(int a, int b) {
        return a * b;
    }
}

Class<Calculator> clazz = Calculator.class;
Calculator calc = new Calculator();

// public 메서드 조회
Method addMethod = clazz.getMethod("add", int.class, int.class);

// 메서드 호출
Object result = addMethod.invoke(calc, 3, 5);  // 8

// private 메서드 호출
Method multiplyMethod = clazz.getDeclaredMethod("multiply", int.class, int.class);
multiplyMethod.setAccessible(true);
Object result2 = multiplyMethod.invoke(calc, 3, 5);  // 15

// 정적 메서드 호출
Method staticMethod = clazz.getMethod("staticMethod");
staticMethod.invoke(null);  // 인스턴스 불필요
```

### 생성자 호출

```java
public class Person {
    private String name;

    public Person() {}
    public Person(String name) { this.name = name; }
}

Class<Person> clazz = Person.class;

// 기본 생성자
Person person1 = clazz.newInstance();  // Deprecated (Java 9+)
Person person2 = clazz.getDeclaredConstructor().newInstance();  // 권장

// 파라미터가 있는 생성자
Constructor<Person> constructor = clazz.getConstructor(String.class);
Person person3 = constructor.newInstance("Alice");

// private 생성자
Constructor<Person> privateConstructor = clazz.getDeclaredConstructor(String.class);
privateConstructor.setAccessible(true);
Person person4 = privateConstructor.newInstance("Bob");
```

### 어노테이션 처리

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
}

public class User {
    @Column(name = "user_name")
    private String name;
}

// 어노테이션 조회
Field field = User.class.getDeclaredField("name");
if (field.isAnnotationPresent(Column.class)) {
    Column column = field.getAnnotation(Column.class);
    String columnName = column.name();  // "user_name"
}
```

### 동적 프록시

```java
public interface Service {
    void execute();
}

// 프록시 생성
Service proxy = (Service) Proxy.newProxyInstance(
    Service.class.getClassLoader(),
    new Class<?>[] { Service.class },
    (proxyObj, method, args) -> {
        System.out.println("Before: " + method.getName());
        Object result = method.invoke(realService, args);
        System.out.println("After: " + method.getName());
        return result;
    }
);

proxy.execute();
// 출력:
// Before: execute
// (실제 실행)
// After: execute
```

### 프레임워크 활용 예시

```java
// Spring DI 원리 (간소화)
public class DIContainer {
    public <T> T getBean(Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Object dependency = getBean(field.getType());
                field.setAccessible(true);
                field.set(instance, dependency);
            }
        }
        return instance;
    }
}

// Jackson JSON 바인딩 원리 (간소화)
public <T> T fromJson(String json, Class<T> clazz) throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();
    Map<String, Object> map = parseJson(json);

    for (Field field : clazz.getDeclaredFields()) {
        field.setAccessible(true);
        Object value = map.get(field.getName());
        field.set(instance, value);
    }
    return instance;
}
```

### 성능 최적화

```java
// 매번 조회하면 느림
for (int i = 0; i < 1000000; i++) {
    Method method = clazz.getMethod("add", int.class, int.class);
    method.invoke(calc, 1, 2);
}

// 캐싱하면 빠름
Method method = clazz.getMethod("add", int.class, int.class);
for (int i = 0; i < 1000000; i++) {
    method.invoke(calc, 1, 2);
}

// MethodHandle (Java 7+) - 더 빠름
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle handle = lookup.findVirtual(Calculator.class, "add",
    MethodType.methodType(int.class, int.class, int.class));
for (int i = 0; i < 1000000; i++) {
    int result = (int) handle.invoke(calc, 1, 2);
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 동적/유연한 코드 | 성능 오버헤드 |
| 프레임워크 구현 가능 | 타입 안전성 상실 |
| 런타임 조작 | 컴파일 타임 검증 불가 |
| private 접근 가능 | 캡슐화 위반 |

## 면접 예상 질문

### Q: 리플렉션의 단점과 주의점은?

A: (1) **성능**: 일반 호출보다 느림, Method/Field 캐싱 권장. (2) **타입 안전성 상실**: 컴파일 타임 검증 불가, 런타임 에러 가능. (3) **캡슐화 위반**: setAccessible(true)로 private 접근 가능. (4) **유지보수 어려움**: IDE 지원 제한, 리팩토링 시 문제. **사용 지침**: 프레임워크/라이브러리 개발에만 사용, 일반 비즈니스 로직에선 지양.

### Q: Spring은 리플렉션을 어떻게 활용하나요?

A: (1) **DI (의존성 주입)**: @Autowired 필드를 리플렉션으로 찾아서 주입. (2) **AOP**: 프록시 생성 시 메서드 가로채기. (3) **@Transactional**: 메서드 호출 전후에 트랜잭션 시작/커밋. (4) **@Controller**: 메서드 파라미터 타입 분석, 요청 매핑. **성능 대응**: 메타데이터 캐싱, CGLIB 바이트코드 생성으로 최적화.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [JVM 구조](./jvm.md) | 클래스 로딩 | [3] 중급 |
| [어노테이션](./annotation.md) | 함께 사용 | [3] 중급 |
| [Spring DI/IoC](../spring/spring-di-ioc.md) | 활용 | [3] 중급 |

## 참고 자료

- [Java Reflection Tutorial - Oracle](https://docs.oracle.com/javase/tutorial/reflect/)
- [Effective Java - Item 65](https://www.oreilly.com/library/view/effective-java/9780134686097/)
