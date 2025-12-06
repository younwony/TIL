# Item 17: 변경 가능성을 최소화하라

## 핵심 정리

불변 클래스란 그 인스턴스의 내부 값을 수정할 수 없는 클래스다. 불변 클래스는 가변 클래스보다 설계하고 구현하고 사용하기 쉬우며, 오류가 생길 여지도 적고 훨씬 안전하다.

## 클래스를 불변으로 만들기 위한 다섯 가지 규칙

1. **객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다**
2. **클래스를 확장할 수 없도록 한다**
3. **모든 필드를 final로 선언한다**
4. **모든 필드를 private으로 선언한다**
5. **자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다**

## 불변 복소수 클래스 예제

```java
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                          re * c.im + im * c.re);
    }

    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
                          (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;
        Complex c = (Complex) o;
        return Double.compare(c.re, re) == 0
            && Double.compare(c.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
```

## 불변 클래스의 장점

1. **불변 객체는 단순하다** - 생성된 시점의 상태를 파괴될 때까지 그대로 간직한다
2. **불변 객체는 근본적으로 스레드 안전하여 따로 동기화할 필요 없다**
3. **불변 객체는 안심하고 공유할 수 있다**
4. **불변 객체끼리는 내부 데이터를 공유할 수 있다**
5. **객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다**
6. **불변 객체는 그 자체로 실패 원자성을 제공한다**

## 불변 클래스의 단점

- 값이 다르면 반드시 독립된 객체로 만들어야 한다
- 원하는 객체를 완성하기까지의 단계가 많고, 그 중간 단계에서 만들어진 객체들이 모두 버려진다면 성능 문제가 생길 수 있다

## 해결책

1. 다단계 연산들을 예측하여 기본 기능으로 제공
2. 가변 동반 클래스(companion class) 제공 (예: StringBuilder)

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_17](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_17)
