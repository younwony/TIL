# Item 5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

## 핵심 정리

많은 클래스가 하나 이상의 자원에 의존한다. 사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.

## 잘못된 예 1: 정적 유틸리티를 잘못 사용한 예

```java
public class SpellChecker {
    private static final Lexicon dictionary = ...;

    private SpellChecker() {} // 객체 생성 방지

    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

## 잘못된 예 2: 싱글턴을 잘못 사용한 예

```java
public class SpellChecker {
    private final Lexicon dictionary = ...;

    private SpellChecker(...) {}
    public static SpellChecker INSTANCE = new SpellChecker(...);

    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

## 문제점

- 두 방식 모두 사전을 단 하나만 사용한다고 가정한다
- 실전에서는 사전이 언어별로 따로 있고, 특수 어휘용 사전이 별도로 있기도 하다
- 사전 하나로 이 모든 쓰임에 대응하기 어렵다

## 해결책: 의존 객체 주입

**인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식**

```java
public class SpellChecker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

## 장점

1. 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 작동한다
2. 불변을 보장하여 같은 자원을 사용하려는 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있다
3. 생성자, 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다

## 변형: 팩터리 메서드 패턴

생성자에 자원 팩터리를 넘겨주는 방식

```java
Mosaic create(Supplier<? extends Tile> tileFactory) { ... }
```

- `Supplier<T>` 인터페이스는 팩터리를 표현한 완벽한 예이다
- 이 방식을 사용해 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다

## 단점

의존성이 수천 개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다.
→ 대거(Dagger), 주스(Guice), 스프링(Spring) 같은 의존 객체 주입 프레임워크를 사용하면 해소할 수 있다.

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_5](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_5)
