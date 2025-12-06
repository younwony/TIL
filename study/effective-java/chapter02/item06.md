# Item 6: 불필요한 객체 생성을 피하라

## 핵심 정리

똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다.

## 예제 1: 문자열 객체 생성

```java
// 나쁜 예 - 실행될 때마다 String 인스턴스를 새로 만든다
String s = new String("bikini");

// 좋은 예 - 하나의 String 인스턴스를 재사용한다
String s = "bikini";
```

## 예제 2: 정적 팩터리 메서드 사용

```java
// 나쁜 예
Boolean b = new Boolean("true");

// 좋은 예 - 정적 팩터리 메서드를 사용
Boolean b = Boolean.valueOf("true");
```

## 예제 3: 비싼 객체는 캐싱하여 재사용

```java
// 나쁜 예 - 매번 Pattern 인스턴스를 생성한다
static boolean isRomanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}

// 좋은 예 - 값비싼 객체를 재사용해 성능을 개선한다
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

## 예제 4: 오토박싱 주의

```java
// 나쁜 예 - 오토박싱으로 인한 불필요한 객체 생성
private static long sum() {
    Long sum = 0L;  // Long으로 선언
    for (long i = 0; i <= Integer.MAX_VALUE; i++)
        sum += i;   // 매번 Long 인스턴스가 생성됨
    return sum;
}

// 좋은 예 - 기본 타입 사용
private static long sum() {
    long sum = 0L;  // long으로 선언
    for (long i = 0; i <= Integer.MAX_VALUE; i++)
        sum += i;
    return sum;
}
```

## 주의사항

- 이번 아이템을 "객체 생성은 비싸니 피해야 한다"로 오해하면 안 된다
- 요즘의 JVM에서는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다
- 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다
- 아주 무거운 객체가 아닌 다음에야 단순히 객체 생성을 피하고자 객체 풀(pool)을 만들지 말자

## 방어적 복사와의 균형

- 이 아이템은 "기존 객체를 재사용해야 한다면 새로운 객체를 만들지 마라"이다
- 아이템 50은 "새로운 객체를 만들어야 한다면 기존 객체를 재사용하지 마라"이다
- 방어적 복사가 필요한 상황에서 객체를 재사용했을 때의 피해가, 불필요한 객체를 반복 생성했을 때의 피해보다 훨씬 크다

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_6](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_6)
