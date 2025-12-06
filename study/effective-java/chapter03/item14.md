# Item 14: Comparable을 구현할지 고려하라

## 핵심 정리

Comparable 인터페이스의 유일무이한 메서드인 compareTo는 Object의 메서드가 아니다. compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있으며, 제네릭하다.

## compareTo 메서드의 일반 규약

1. **반사성:** 이 객체와 주어진 객체의 순서를 비교한다. 이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환한다.
2. **대칭성:** sgn(x.compareTo(y)) == -sgn(y.compareTo(x))
3. **추이성:** (x.compareTo(y) > 0 && y.compareTo(z) > 0)이면 x.compareTo(z) > 0
4. **일관성:** (x.compareTo(y) == 0) == (x.equals(y))는 권장사항이다

## Comparable 구현 예제

```java
public final class PhoneNumber implements Comparable<PhoneNumber> {
    private final short areaCode, prefix, lineNum;

    @Override
    public int compareTo(PhoneNumber pn) {
        int result = Short.compare(areaCode, pn.areaCode);
        if (result == 0) {
            result = Short.compare(prefix, pn.prefix);
            if (result == 0)
                result = Short.compare(lineNum, pn.lineNum);
        }
        return result;
    }
}
```

## 비교자 생성 메서드를 활용한 비교자

```java
private static final Comparator<PhoneNumber> COMPARATOR =
        comparingInt((PhoneNumber pn) -> pn.areaCode)
            .thenComparingInt(pn -> pn.prefix)
            .thenComparingInt(pn -> pn.lineNum);

@Override
public int compareTo(PhoneNumber pn) {
    return COMPARATOR.compare(this, pn);
}
```

## 주의사항

### 해시코드 차이를 이용한 비교자 - 추이성 위배!

```java
// 해시코드 값의 차를 기준으로 하는 비교자 - 추이성을 위배한다!
static Comparator<Object> hashCodeOrder = new Comparator<>() {
    public int compare(Object o1, Object o2) {
        return o1.hashCode() - o2.hashCode();  // 오버플로우 가능!
    }
};
```

### 올바른 방법

```java
// 정적 compare 메서드를 활용한 비교자
static Comparator<Object> hashCodeOrder = new Comparator<>() {
    public int compare(Object o1, Object o2) {
        return Integer.compare(o1.hashCode(), o2.hashCode());
    }
};

// 비교자 생성 메서드를 활용한 비교자
static Comparator<Object> hashCodeOrder =
        Comparator.comparingInt(o -> o.hashCode());
```

## Comparable 구현의 이점

1. 손쉽게 컬렉션을 정렬할 수 있다: `Arrays.sort(a)`
2. 검색, 극단값 계산, 자동 정렬되는 컬렉션 관리도 쉽게 할 수 있다
3. 알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현하자

## 참고

- 원본 코드: [effectiveJava/chapter_3/item_14](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_3/item_14)
