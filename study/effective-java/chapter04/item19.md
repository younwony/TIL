# Item 19: 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라

## 핵심 정리

상속용 클래스는 재정의할 수 있는 메서드들을 내부적으로 어떻게 이용하는지(자기사용) 문서로 남겨야 한다.

## 상속용 클래스 설계 시 주의점

### 1. 재정의 가능 메서드의 내부 동작 방식을 문서로 남겨라

```java
/**
 * {@inheritDoc}
 *
 * <p>이 구현은 컬렉션을 순회하며 주어진 원소와 일치하는 원소를 찾는다.
 * 주어진 원소와 일치하는 원소를 찾으면, iterator의 remove 메서드를 사용해
 * 해당 원소를 컬렉션에서 제거한다.
 *
 * <p><b>Implementation Requirements:</b> 이 메서드는
 * 이 컬렉션의 iterator 메서드가 반환한 iterator를 사용해 컬렉션을 순회한다.
 * 일치하는 각 원소는 Iterator.remove 메서드를 사용해 제거한다.
 * 이 컬렉션의 iterator 메서드가 반환한 iterator가 remove 메서드를 구현하지
 * 않았다면 UnsupportedOperationException을 던진다.
 */
public boolean remove(Object o) { ... }
```

### 2. 훅(hook)을 잘 선별하여 protected 메서드로 제공하라

```java
// java.util.AbstractList의 예
protected void removeRange(int fromIndex, int toIndex) { ... }
```

### 3. 상속용 클래스의 생성자는 직접적으로든 간접적으로든 재정의 가능 메서드를 호출해서는 안 된다

```java
// 잘못된 예 - 생성자가 재정의 가능 메서드를 호출한다
public class Super {
    public Super() {
        overrideMe();  // 위험!
    }

    public void overrideMe() { }
}

public class Sub extends Super {
    private final Instant instant;

    Sub() {
        instant = Instant.now();
    }

    @Override
    public void overrideMe() {
        System.out.println(instant);  // NullPointerException 발생!
    }
}
```

## 상속용으로 설계되지 않은 클래스의 상속을 금지하는 방법

1. **클래스를 final로 선언한다**
2. **모든 생성자를 private이나 package-private으로 선언하고 public 정적 팩터리를 만든다**

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_19](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_19)
