# Item 11: equals를 재정의하려거든 hashCode도 재정의하라

## 핵심 정리

**equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다.** 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 HashMap이나 HashSet 같은 컬렉션의 원소로 사용할 때 문제를 일으킨다.

## Object 명세에서 발췌한 hashCode 규약

1. equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다.
2. **equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.**
3. equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.

## 문제가 되는 예

```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 867, 5309), "제니");

// hashCode를 재정의하지 않으면 null을 반환한다
m.get(new PhoneNumber(707, 867, 5309)); // null 반환!
```

## 좋은 hashCode 작성 요령

```java
@Override
public int hashCode() {
    int result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    return result;
}
```

### 왜 31인가?
- 31은 홀수이면서 소수(prime)다
- 31 * i는 (i << 5) - i로 최적화할 수 있다

## Objects.hash 사용

```java
@Override
public int hashCode() {
    return Objects.hash(lineNum, prefix, areaCode);
}
```

**주의:** 성능에 민감하지 않은 상황에서만 사용하자. 입력 인수를 담기 위한 배열이 만들어지고, 기본 타입은 박싱/언박싱도 거쳐야 한다.

## 해시코드 지연 초기화와 캐싱

```java
private int hashCode; // 자동으로 0으로 초기화된다

@Override
public int hashCode() {
    int result = hashCode;
    if (result == 0) {
        result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        hashCode = result;
    }
    return result;
}
```

## 주의사항

- 성능을 높인답시고 해시코드를 계산할 때 핵심 필드를 생략해서는 안 된다
- hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말자

## 참고

- 원본 코드: [effectiveJava/chapter_3/item_11](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_3/item_11)
