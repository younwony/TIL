# Item 21: 인터페이스는 구현하는 쪽을 생각해 설계하라

## 핵심 정리

자바 8 전에는 기존 구현체를 깨뜨리지 않고는 인터페이스에 메서드를 추가할 방법이 없었다. 자바 8에 와서 기존 인터페이스에 메서드를 추가할 수 있도록 디폴트 메서드를 소개했지만, 위험이 완전히 사라진 것은 아니다.

## 디폴트 메서드의 위험성

### Collection의 removeIf 메서드

```java
default boolean removeIf(Predicate<? super E> filter) {
    Objects.requireNonNull(filter);
    boolean result = false;
    for (Iterator<E> it = iterator(); it.hasNext(); ) {
        if (filter.test(it.next())) {
            it.remove();
            result = true;
        }
    }
    return result;
}
```

**문제:** `org.apache.commons.collections4.collection.SynchronizedCollection`과 같은 동기화 컬렉션은 이 디폴트 메서드를 재정의하지 않으면 스레드 안전성이 깨진다.

## 핵심 원칙

1. **디폴트 메서드는 (컴파일에 성공하더라도) 기존 구현체에 런타임 오류를 일으킬 수 있다**
2. **기존 인터페이스에 디폴트 메서드로 새 메서드를 추가하는 일은 꼭 필요한 경우가 아니면 피해야 한다**
3. **디폴트 메서드라는 도구가 생겼더라도 인터페이스를 설계할 때는 여전히 세심한 주의를 기울여야 한다**

## 인터페이스 설계 시 테스트

- 새로운 인터페이스라면 릴리스 전에 반드시 테스트를 거쳐야 한다
- 서로 다른 방식으로 최소한 세 가지는 구현해봐야 한다
- 인터페이스를 릴리스한 후라도 결함을 수정하는 게 가능한 경우도 있겠지만, 절대 그 가능성에 기대서는 안 된다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_21](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_21)
