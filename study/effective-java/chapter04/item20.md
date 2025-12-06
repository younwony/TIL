# Item 20: 추상 클래스보다는 인터페이스를 우선하라

## 핵심 정리

자바가 제공하는 다중 구현 메커니즘은 인터페이스와 추상 클래스, 이렇게 두 가지다. 자바 8부터 인터페이스도 디폴트 메서드를 제공할 수 있게 되어, 이제는 두 메커니즘 모두 인스턴스 메서드를 구현 형태로 제공할 수 있다.

## 인터페이스의 장점

### 1. 기존 클래스에도 손쉽게 새로운 인터페이스를 구현해넣을 수 있다

```java
public class MyClass extends SomeClass implements Comparable, Iterable { ... }
```

### 2. 인터페이스는 믹스인(mixin) 정의에 안성맞춤이다

믹스인: 클래스가 구현할 수 있는 타입으로, 원래의 '주된 타입' 외에도 특정 선택적 행위를 제공한다고 선언하는 효과를 준다.

```java
public class MyClass implements Comparable<MyClass> { ... }
```

### 3. 인터페이스로는 계층구조가 없는 타입 프레임워크를 만들 수 있다

```java
public interface Singer {
    AudioClip sing(Song s);
}

public interface Songwriter {
    Song compose(int chartPosition);
}

// 두 인터페이스를 모두 구현
public interface SingerSongwriter extends Singer, Songwriter {
    AudioClip strum();
    void actSensitive();
}
```

## 디폴트 메서드

```java
public interface Collection<E> extends Iterable<E> {
    // 디폴트 메서드
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
}
```

## 템플릿 메서드 패턴: 골격 구현 클래스

```java
// 골격 구현을 사용해 완성한 구체 클래스
static List<Integer> intArrayAsList(int[] a) {
    Objects.requireNonNull(a);

    return new AbstractList<>() {
        @Override
        public Integer get(int i) {
            return a[i];  // 오토박싱
        }

        @Override
        public Integer set(int i, Integer val) {
            int oldVal = a[i];
            a[i] = val;  // 오토언박싱
            return oldVal;  // 오토박싱
        }

        @Override
        public int size() {
            return a.length;
        }
    };
}
```

## 골격 구현 작성 방법

1. 인터페이스를 잘 살펴 다른 메서드들의 구현에 사용되는 기반 메서드들을 선정한다
2. 기반 메서드들을 사용해 직접 구현할 수 있는 메서드를 모두 디폴트 메서드로 제공한다
3. 기반 메서드나 디폴트 메서드로 만들지 못한 메서드가 남아 있다면, 이 인터페이스를 구현하는 골격 구현 클래스를 하나 만들어 남은 메서드들을 작성해 넣는다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_20](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_20)
