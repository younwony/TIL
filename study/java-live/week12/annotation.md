# Week 12: 애노테이션 (Annotation)

## 개요

Java 애노테이션에 대해 학습합니다.

## 기본 제공 애노테이션

### @Override
```java
class Parent {
    public void method() {}
}

class Child extends Parent {
    @Override
    public void method() {}  // 부모 메서드 재정의
}
```

### @Deprecated
```java
@Deprecated
public void oldMethod() {
    // 더 이상 사용하지 않는 메서드
}
```

### @SuppressWarnings
```java
@SuppressWarnings("unchecked")
public void genericMethod() {
    List list = new ArrayList();  // 경고 억제
}
```

### @FunctionalInterface
```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);  // 단 하나의 추상 메서드
}
```

## 커스텀 애노테이션 정의

```java
public @interface CustomAnnotation {
    // 마커 애노테이션 (멤버 없음)
}
```

### 요소가 있는 애노테이션

```java
public @interface MyAnnotation {
    String value() default "";
    int count() default 0;
    String[] tags() default {};
}

// 사용
@MyAnnotation(value = "test", count = 5, tags = {"a", "b"})
public void annotatedMethod() {}
```

## 메타 애노테이션

### @Target
```java
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MyAnnotation {}
```

| ElementType | 설명 |
|-------------|------|
| TYPE | 클래스, 인터페이스, 열거형 |
| FIELD | 필드 |
| METHOD | 메서드 |
| PARAMETER | 매개변수 |
| CONSTRUCTOR | 생성자 |
| LOCAL_VARIABLE | 지역 변수 |
| ANNOTATION_TYPE | 애노테이션 타입 |
| PACKAGE | 패키지 |

### @Retention
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {}
```

| RetentionPolicy | 설명 |
|-----------------|------|
| SOURCE | 소스 코드까지만 유지 |
| CLASS | 클래스 파일까지 유지 (기본값) |
| RUNTIME | 런타임까지 유지 |

### @Documented
```java
@Documented
public @interface MyAnnotation {}
```

### @Inherited
```java
@Inherited
public @interface MyAnnotation {}
```

## 리플렉션으로 애노테이션 읽기

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestAnnotation {
    String value();
}

public class AnnotationExample {
    @TestAnnotation("테스트 메서드")
    public void testMethod() {}

    public static void main(String[] args) throws Exception {
        Method method = AnnotationExample.class.getMethod("testMethod");

        if (method.isAnnotationPresent(TestAnnotation.class)) {
            TestAnnotation annotation = method.getAnnotation(TestAnnotation.class);
            System.out.println(annotation.value());  // "테스트 메서드"
        }
    }
}
```

## JavaDoc 관련 애노테이션

```java
/**
 * 클래스 설명
 * @author 작성자
 * @version 1.0
 * @since 2020
 */
public class JavaDocExample {

    /**
     * 메서드 설명
     * @param name 파라미터 설명
     * @return 반환값 설명
     * @throws Exception 예외 설명
     */
    public String method(String name) throws Exception {
        return name;
    }
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy12_Annotation](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy12_Annotation)
