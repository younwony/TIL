# Item 10: equals는 일반 규약을 지켜 재정의하라

## 핵심 정리

equals 메서드는 재정의하기 쉬워 보이지만 곳곳에 함정이 도사리고 있어서 자칫하면 끔찍한 결과를 초래한다.

## equals를 재정의하지 않아야 하는 경우

1. **각 인스턴스가 본질적으로 고유하다** - 값이 아닌 동작하는 개체를 표현하는 클래스 (예: Thread)
2. **인스턴스의 '논리적 동치성'을 검사할 일이 없다**
3. **상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다**
4. **클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다**

## equals를 재정의해야 하는 경우

객체 식별성(두 객체가 물리적으로 같은가)이 아니라 **논리적 동치성**을 확인해야 하는데, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때

## equals 메서드의 일반 규약

### 1. 반사성(reflexivity)
- null이 아닌 모든 참조 값 x에 대해, `x.equals(x)`는 true다

### 2. 대칭성(symmetry)
- null이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`가 true면 `y.equals(x)`도 true다

```java
// 대칭성 위배 예제
public final class CaseInsensitiveString {
    private final String s;

    @Override
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        if (o instanceof String)  // 한 방향으로만 작동!
            return s.equalsIgnoreCase((String) o);
        return false;
    }
}
```

### 3. 추이성(transitivity)
- null이 아닌 모든 참조 값 x, y, z에 대해, `x.equals(y)`가 true이고 `y.equals(z)`도 true면 `x.equals(z)`도 true다

### 4. 일관성(consistency)
- null이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다

### 5. null-아님
- null이 아닌 모든 참조 값 x에 대해, `x.equals(null)`은 false다

## 양질의 equals 메서드 구현 방법

1. **== 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다**
2. **instanceof 연산자로 입력이 올바른 타입인지 확인한다**
3. **입력을 올바른 타입으로 형변환한다**
4. **입력 객체와 자기 자신의 대응되는 '핵심' 필드들이 모두 일치하는지 하나씩 검사한다**

```java
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber) o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }
}
```

## 주의사항

- equals를 재정의할 땐 hashCode도 반드시 재정의하자 (아이템 11)
- 너무 복잡하게 해결하려 들지 말자
- Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자

## 참고

- 원본 코드: [effectiveJava/chapter_3/item_10](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_3/item_10)
