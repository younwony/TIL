# Item 13: clone 재정의는 주의해서 진행하라

## 핵심 정리

Cloneable은 복제해도 되는 클래스임을 명시하는 용도의 믹스인 인터페이스지만, 아쉽게도 의도한 목적을 제대로 이루지 못했다.

## Cloneable 인터페이스의 문제점

1. clone 메서드가 선언된 곳이 Cloneable이 아닌 Object이다
2. 그마저도 protected이다
3. Cloneable을 구현하는 것만으로는 외부 객체에서 clone 메서드를 호출할 수 없다

## clone 메서드의 일반 규약

```
x.clone() != x                           // 참
x.clone().getClass() == x.getClass()     // 참
x.clone().equals(x)                      // 일반적으로 참, 필수는 아님
```

## 가변 상태를 참조하지 않는 클래스용 clone

```java
@Override
public PhoneNumber clone() {
    try {
        return (PhoneNumber) super.clone();
    } catch (CloneNotSupportedException e) {
        throw new AssertionError();  // 일어날 수 없는 일
    }
}
```

## 가변 상태를 참조하는 클래스용 clone

```java
public class Stack implements Cloneable {
    private Object[] elements;
    private int size = 0;

    @Override
    public Stack clone() {
        try {
            Stack result = (Stack) super.clone();
            result.elements = elements.clone();  // 배열의 clone 호출
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

## 복잡한 가변 상태를 갖는 클래스용 clone

```java
public class HashTable implements Cloneable {
    private Entry[] buckets = ...;

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        // 이 엔트리가 가리키는 연결 리스트를 재귀적으로 복사
        Entry deepCopy() {
            return new Entry(key, value,
                    next == null ? null : next.deepCopy());
        }
    }

    @Override
    public HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++)
                if (buckets[i] != null)
                    result.buckets[i] = buckets[i].deepCopy();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

## 더 나은 대안: 복사 생성자와 복사 팩터리

### 복사 생성자
```java
public Yum(Yum yum) { ... }
```

### 복사 팩터리
```java
public static Yum newInstance(Yum yum) { ... }
```

## 복사 생성자/팩터리의 장점

1. 언어 모순적이고 위험천만한 객체 생성 메커니즘(생성자를 쓰지 않는 방식)을 사용하지 않는다
2. 엉성하게 문서화된 규약에 기대지 않는다
3. 정상적인 final 필드 용법과도 충돌하지 않는다
4. 불필요한 검사 예외를 던지지 않는다
5. 형변환도 필요치 않다
6. 해당 클래스가 구현한 인터페이스 타입의 인스턴스를 인수로 받을 수 있다

## 참고

- 원본 코드: [effectiveJava/chapter_3/item_13](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_3/item_13)
