# Item 4: 인스턴스화를 막으려거든 private 생성자를 사용하라

## 핵심 정리

정적 메서드와 정적 필드만을 담은 클래스를 만들고 싶을 때가 있다.

## 유틸리티 클래스의 예

- `java.lang.Math`, `java.util.Arrays`: 기본 타입 값이나 배열 관련 메서드들을 모아놓음
- `java.util.Collections`: 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(팩터리)를 모아놓음
- `final` 클래스와 관련한 메서드들을 모아놓을 때도 사용

## 문제점

추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.
- 하위 클래스를 만들어 인스턴스화하면 그만이다
- 사용자는 상속해서 쓰라는 뜻으로 오해할 수 있다

## 해결책: private 생성자

```java
public class UtilityClass {
    // 기본 생성자가 만들어지는 것을 막는다(인스턴스화 방지용)
    private UtilityClass() {
        throw new AssertionError();
    }

    // 나머지 코드는 생략
}
```

## 장점

1. 명시적 생성자가 private이니 클래스 바깥에서는 접근할 수 없다
2. AssertionError를 던지면 클래스 안에서 실수로라도 생성자를 호출하지 않도록 해준다
3. 어떤 환경에서도 클래스가 인스턴스화되는 것을 막아준다
4. 상속을 불가능하게 하는 효과도 있다

## 주의사항

- 생성자가 분명 존재하는데 호출할 수 없다니 직관적이지 않다
- 그러니 적절한 주석을 달아두자

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_4](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_4)
