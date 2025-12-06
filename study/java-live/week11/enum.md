# Week 11: 열거형 (Enum)

## 개요

Java 열거형(Enum)에 대해 학습합니다.

## 기본 열거형 정의

```java
public enum Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, NAVY, PURPLE
}
```

## 열거형 사용

```java
Color color = Color.RED;

switch (color) {
    case RED:
        System.out.println("빨간색");
        break;
    case BLUE:
        System.out.println("파란색");
        break;
    default:
        System.out.println("기타 색상");
}
```

## 열거형 메서드

```java
// 모든 상수 출력
for (Color c : Color.values()) {
    System.out.println(c.name() + " : " + c.ordinal());
}

// 문자열로 열거형 상수 얻기
Color red = Color.valueOf("RED");

// 비교
Color c1 = Color.RED;
Color c2 = Color.BLUE;
System.out.println(c1.compareTo(c2));  // 순서 비교
```

## 커스텀 열거형

```java
public enum CustomEnum {
    FIRST(1, "첫 번째"),
    SECOND(2, "두 번째"),
    THIRD(3, "세 번째");

    private final int number;
    private final String description;

    // private 생성자
    CustomEnum(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}
```

## 열거형에 메서드 추가

```java
public enum Operation {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double x, double y);

    @Override
    public String toString() {
        return symbol;
    }
}
```

## 열거형의 장점

1. **타입 안전성**: 정의된 상수만 사용 가능
2. **컴파일 타임 체크**: 잘못된 값 사용 방지
3. **가독성**: 명확한 의도 표현
4. **싱글톤 보장**: 각 상수는 하나의 인스턴스만 존재

## EnumSet과 EnumMap

```java
// EnumSet
EnumSet<Color> warmColors = EnumSet.of(Color.RED, Color.ORANGE, Color.YELLOW);
EnumSet<Color> allColors = EnumSet.allOf(Color.class);

// EnumMap
EnumMap<Color, String> colorMap = new EnumMap<>(Color.class);
colorMap.put(Color.RED, "#FF0000");
colorMap.put(Color.BLUE, "#0000FF");
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy11_Enum](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy11_Enum)
