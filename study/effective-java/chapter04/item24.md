# Item 24: 멤버 클래스는 되도록 static으로 만들라

## 핵심 정리

중첩 클래스(nested class)란 다른 클래스 안에 정의된 클래스를 말한다. 중첩 클래스는 자신을 감싼 바깥 클래스에서만 쓰여야 하며, 그 외의 쓰임새가 있다면 톱레벨 클래스로 만들어야 한다.

## 중첩 클래스의 종류

1. **정적 멤버 클래스**
2. **(비정적) 멤버 클래스**
3. **익명 클래스**
4. **지역 클래스**

첫 번째를 제외한 나머지는 내부 클래스(inner class)에 해당한다.

## 정적 멤버 클래스

```java
public class Calculator {
    public enum Operation {  // 열거 타입도 암시적 static
        PLUS, MINUS, TIMES, DIVIDE
    }

    public static class Builder {  // 정적 멤버 클래스
        // ...
    }
}
```

**특징:**
- 다른 클래스 안에 선언된다
- 바깥 클래스의 private 멤버에도 접근할 수 있다
- 바깥 인스턴스와 독립적으로 존재할 수 있다

## 비정적 멤버 클래스

```java
public class MySet<E> extends AbstractSet<E> {
    // ...

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();  // 비정적 멤버 클래스의 인스턴스
    }

    private class MyIterator implements Iterator<E> {
        // MySet.this로 바깥 인스턴스에 접근 가능
    }
}
```

**특징:**
- 비정적 멤버 클래스의 인스턴스는 바깥 클래스의 인스턴스와 암묵적으로 연결된다
- 비정적 멤버 클래스의 인스턴스 메서드에서 `바깥클래스.this`를 사용해 바깥 인스턴스의 메서드를 호출하거나 바깥 인스턴스의 참조를 가져올 수 있다
- 바깥 인스턴스 없이는 생성할 수 없다

## 정적 멤버 클래스 vs 비정적 멤버 클래스

| 구분 | 정적 멤버 클래스 | 비정적 멤버 클래스 |
|------|-----------------|-------------------|
| 바깥 인스턴스 접근 | 불가능 | 가능 |
| 메모리 사용 | 바깥 인스턴스 참조 없음 | 바깥 인스턴스 참조 저장 |
| 생성 | 바깥 인스턴스 없이 가능 | 바깥 인스턴스 필요 |
| 가비지 컬렉션 | 영향 없음 | 바깥 인스턴스 수거 방해 가능 |

## 핵심 규칙

**멤버 클래스에서 바깥 인스턴스에 접근할 일이 없다면 무조건 static을 붙여서 정적 멤버 클래스로 만들자**

이유:
1. static을 생략하면 바깥 인스턴스로의 숨은 외부 참조를 갖게 된다
2. 이 참조를 저장하려면 시간과 공간이 소비된다
3. 더 심각한 문제는 가비지 컬렉션이 바깥 클래스의 인스턴스를 수거하지 못하는 메모리 누수가 생길 수 있다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_24](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_24)
