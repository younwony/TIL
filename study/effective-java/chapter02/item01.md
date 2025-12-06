# Item 1: 생성자 대신 정적 팩터리 메서드를 고려하라

## 핵심 정리

클래스의 인스턴스를 얻는 전통적인 수단은 public 생성자다. 하지만 클래스는 생성자와 별도로 정적 팩터리 메서드(static factory method)를 제공할 수 있다.

## 정적 팩터리 메서드의 장점

### 1. 이름을 가질 수 있다
- 생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못한다
- 정적 팩터리는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다

### 2. 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다
- 불변 클래스(immutable class)는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용할 수 있다
- 플라이웨이트 패턴(Flyweight pattern)도 이와 비슷한 기법이다

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다
- 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 유연성을 제공한다

### 4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다
- 반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다

### 5. 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다
- 서비스 제공자 프레임워크를 만드는 근간이 된다

## 정적 팩터리 메서드의 단점

### 1. 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다

### 2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다

## 정적 팩터리 메서드 명명 규약

| 명명 규약 | 설명 | 예시 |
|----------|------|------|
| `from` | 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드 | `Date.from(instant)` |
| `of` | 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드 | `EnumSet.of(JACK, QUEEN, KING)` |
| `valueOf` | from과 of의 더 자세한 버전 | `BigInteger.valueOf(Integer.MAX_VALUE)` |
| `instance` / `getInstance` | 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않음 | `StackWalker.getInstance(options)` |
| `create` / `newInstance` | instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장 | `Array.newInstance(classObject, arrayLen)` |
| `getType` | getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용 | `Files.getFileStore(path)` |
| `newType` | newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용 | `Files.newBufferedReader(path)` |
| `type` | getType과 newType의 간결한 버전 | `Collections.list(legacyLitany)` |

## 예제 코드

```java
// Boolean의 정적 팩터리 메서드
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}

// EnumSet의 정적 팩터리 메서드
public static <E extends Enum<E>> EnumSet<E> of(E e) {
    EnumSet<E> result = noneOf(e.getDeclaringClass());
    result.add(e);
    return result;
}
```

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_1](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_1)
