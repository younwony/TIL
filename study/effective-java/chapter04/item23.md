# Item 23: 태그 달린 클래스보다는 클래스 계층구조를 활용하라

## 핵심 정리

두 가지 이상의 의미를 표현할 수 있으며, 그중 현재 표현하는 의미를 태그 값으로 알려주는 클래스를 본 적이 있을 것이다. 이런 클래스는 단점이 많다.

## 태그 달린 클래스의 예

```java
// 태그 달린 클래스 - 클래스 계층구조보다 훨씬 나쁘다!
class Figure {
    enum Shape { RECTANGLE, CIRCLE };

    // 태그 필드 - 현재 모양을 나타낸다
    final Shape shape;

    // 다음 필드들은 모양이 사각형(RECTANGLE)일 때만 쓰인다
    double length;
    double width;

    // 다음 필드는 모양이 원(CIRCLE)일 때만 쓰인다
    double radius;

    // 원용 생성자
    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 사각형용 생성자
    Figure(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }

    double area() {
        switch(shape) {
            case RECTANGLE:
                return length * width;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
```

## 태그 달린 클래스의 단점

1. 열거 타입 선언, 태그 필드, switch 문 등 쓸데없는 코드가 많다
2. 여러 구현이 한 클래스에 혼합돼 있어서 가독성도 나쁘다
3. 다른 의미를 위한 코드도 언제나 함께 하니 메모리도 많이 사용한다
4. 필드들을 final로 선언하려면 해당 의미에 쓰이지 않는 필드들까지 생성자에서 초기화해야 한다
5. 다른 의미를 추가하려면 코드를 수정해야 한다
6. 인스턴스의 타입만으로는 현재 나타내는 의미를 알 길이 전혀 없다

**결론: 태그 달린 클래스는 장황하고, 오류를 내기 쉽고, 비효율적이다**

## 해결책: 클래스 계층구조

```java
// 클래스 계층구조로 변환
abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    final double radius;

    Circle(double radius) { this.radius = radius; }

    @Override
    double area() { return Math.PI * (radius * radius); }
}

class Rectangle extends Figure {
    final double length;
    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() { return length * width; }
}

// 클래스 계층구조는 확장도 쉽다
class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}
```

## 클래스 계층구조의 장점

1. 각 의미를 독립된 클래스에 담아 관련 없는 데이터 필드를 모두 제거했다
2. 살아남은 필드들은 모두 final이다
3. 각 클래스의 생성자가 모든 필드를 남김없이 초기화하고 추상 메서드를 모두 구현했는지 컴파일러가 확인해준다
4. 타입이 의미별로 존재하니 변수의 의미를 명시하거나 제한할 수 있다
5. 타입 사이의 자연스러운 계층 관계를 반영할 수 있어서 유연성은 물론 컴파일타임 타입 검사 능력을 높여준다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_23](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_23)
