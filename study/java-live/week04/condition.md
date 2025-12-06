# Week 4: 조건문 (Condition)

## 개요

Java의 조건문에 대해 학습합니다.

## If-Else 문

```java
public void conditionIf() {
    int a = 0;
    int b = 1;

    if (a == 0) {
        System.out.println("a is 0");
    } else if (b == 1) {
        System.out.println("b is 1");
    } else {
        System.out.println("none");
    }
}
```

## 삼항 연산자

```java
public void conditionIf3() {
    int a = 0;
    int b = 1;

    String result = (a == 0) ? "a is 0" : (b == 1) ? "b is 1" : "none";
    System.out.println(result);
}
```

**특징:**
- 중첩 삼항 연산자 사용 가능
- 간단한 조건에 적합
- 복잡한 로직에는 가독성이 떨어질 수 있음

## Switch 문

```java
public void conditionSwitch() {
    int value = 1;

    switch (value) {
        case 0:
            System.out.println("zero");
            break;
        case 1:
            System.out.println("one");
            break;
        default:
            System.out.println("default");
    }
}
```

## Switch Expression (Java 12+)

```java
public void conditionSwitchExpression() {
    int value = 1;

    String result = switch (value) {
        case 0 -> "zero";
        case 1 -> "one";
        default -> "default";
    };

    System.out.println(result);
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy4](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy4)
