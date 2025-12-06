# Item 22: 인터페이스는 타입을 정의하는 용도로만 사용하라

## 핵심 정리

인터페이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다. 달리 말해, 클래스가 어떤 인터페이스를 구현한다는 것은 자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 얘기해주는 것이다.

## 안티패턴: 상수 인터페이스

```java
// 상수 인터페이스 안티패턴 - 사용하지 말 것!
public interface PhysicalConstants {
    // 아보가드로 수 (1/몰)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    // 볼츠만 상수 (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    // 전자 질량 (kg)
    static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

**문제점:**
1. 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부 구현에 해당한다
2. 상수 인터페이스를 구현하는 것은 이 내부 구현을 클래스의 API로 노출하는 행위다
3. 다음 릴리스에서 이 상수들을 더는 쓰지 않게 되더라도 바이너리 호환성을 위해 여전히 상수 인터페이스를 구현하고 있어야 한다
4. final이 아닌 클래스가 상수 인터페이스를 구현한다면 모든 하위 클래스의 이름공간이 그 인터페이스가 정의한 상수들로 오염되어 버린다

## 대안 1: 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가

```java
// Integer와 Double에 정의된 MIN_VALUE와 MAX_VALUE 상수
Integer.MIN_VALUE
Integer.MAX_VALUE
```

## 대안 2: 열거 타입으로 나타내기 적합한 상수라면 열거 타입으로 만들어 공개

```java
public enum Planet {
    MERCURY(3.302e+23, 2.439e6),
    VENUS(4.869e+24, 6.052e6),
    // ...
}
```

## 대안 3: 인스턴스화할 수 없는 유틸리티 클래스에 담아 공개

```java
// 상수 유틸리티 클래스
public class PhysicalConstants {
    private PhysicalConstants() { }  // 인스턴스화 방지

    // 아보가드로 수 (1/몰)
    public static final double AVOGADROS_NUMBER = 6.022_140_857e23;

    // 볼츠만 상수 (J/K)
    public static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    // 전자 질량 (kg)
    public static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_22](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_22)
