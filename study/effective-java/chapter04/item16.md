# Item 16: public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

## 핵심 정리

인스턴스 필드들을 모아놓는 일 외에는 아무 목적도 없는 퇴보한 클래스를 작성하려 할 때가 있다.

## 나쁜 예: 이처럼 퇴보한 클래스는 public이어서는 안 된다

```java
class Point {
    public double x;
    public double y;
}
```

**문제점:**
- 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못한다
- API를 수정하지 않고는 내부 표현을 바꿀 수 없다
- 불변식을 보장할 수 없다
- 외부에서 필드에 접근할 때 부수 작업을 수행할 수도 없다

## 좋은 예: 접근자와 변경자(mutator) 메서드를 활용한 데이터 캡슐화

```java
class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
```

## package-private 클래스 또는 private 중첩 클래스라면?

패키지 바깥 코드는 전혀 손대지 않고도 데이터 표현 방식을 바꿀 수 있다.

```java
// package-private 클래스라면 데이터 필드를 노출해도 무방하다
class Point {
    public double x;
    public double y;
}
```

## 불변 필드라면?

```java
// 불변 필드를 노출한 public 클래스 - 과연 좋은가?
public final class Time {
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY)
            throw new IllegalArgumentException("시간: " + hour);
        if (minute < 0 || minute >= MINUTES_PER_HOUR)
            throw new IllegalArgumentException("분: " + minute);
        this.hour = hour;
        this.minute = minute;
    }
}
```

**단점:**
- API를 변경하지 않고는 표현 방식을 바꿀 수 없다
- 필드를 읽을 때 부수 작업을 수행할 수 없다

## 요약

- public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다
- 불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다
- 하지만 package-private 클래스나 private 중첩 클래스에서는 종종 (불변이든 가변이든) 필드를 노출하는 편이 나을 때도 있다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_16](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_16)
