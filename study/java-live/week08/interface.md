# Week 8: 인터페이스 (Interface)

## 개요

Java 인터페이스에 대해 학습합니다.

## 인터페이스 정의

```java
public interface Study {
    // 상수 (public static final 자동 적용)
    int MAX_SCORE = 100;

    // 추상 메서드 (public abstract 자동 적용)
    int sumFactorial(int a, int b);

    // 디폴트 메서드 (Java 8+)
    default void printInfo() {
        System.out.println("Study Interface");
    }

    // 정적 메서드 (Java 8+)
    static void staticMethod() {
        System.out.println("Static Method");
    }
}
```

## 인터페이스 구현

```java
public class StudyClass implements Study {
    @Override
    public int sumFactorial(int a, int b) {
        return factorial(a) + factorial(b);
    }

    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
}
```

## 사용 예제

```java
public class Main {
    public static void main(String[] args) {
        StudyClass instance = new StudyClass();
        int result = instance.sumFactorial(5, 4);
        System.out.println(result);  // 5! + 4! = 120 + 24 = 144
    }
}
```

## 다중 인터페이스 구현

```java
interface Runnable {
    void run();
}

interface Flyable {
    void fly();
}

class Bird implements Runnable, Flyable {
    @Override
    public void run() {
        System.out.println("Running");
    }

    @Override
    public void fly() {
        System.out.println("Flying");
    }
}
```

## 인터페이스 상속

```java
interface Parent {
    void parentMethod();
}

interface Child extends Parent {
    void childMethod();
}

// Child를 구현하면 parentMethod와 childMethod 모두 구현해야 함
class Implementation implements Child {
    @Override
    public void parentMethod() {}

    @Override
    public void childMethod() {}
}
```

## 함수형 인터페이스 (Java 8+)

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// 람다식으로 구현
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(5, 3));       // 8
System.out.println(multiply.calculate(5, 3));  // 15
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy8_인터페이스](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy8_%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4)
