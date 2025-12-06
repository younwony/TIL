# Item 25: 톱레벨 클래스는 한 파일에 하나만 담으라

## 핵심 정리

소스 파일 하나에 톱레벨 클래스를 여러 개 선언하더라도 자바 컴파일러는 불평하지 않는다. 하지만 아무런 득이 없을 뿐더러 심각한 위험을 감수해야 한다.

## 문제의 예

### Utensil.java

```java
// 두 클래스가 한 파일(Utensil.java)에 정의되었다 - 따라하지 말 것!
class Utensil {
    static final String NAME = "pan";
}

class Dessert {
    static final String NAME = "cake";
}
```

### Dessert.java

```java
// 두 클래스가 한 파일(Dessert.java)에 정의되었다 - 따라하지 말 것!
class Utensil {
    static final String NAME = "pot";
}

class Dessert {
    static final String NAME = "pie";
}
```

### Main.java

```java
public class Main {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}
```

## 컴파일 순서에 따른 결과 차이

| 컴파일 명령 | 결과 |
|------------|------|
| `javac Main.java Dessert.java` | 컴파일 오류 |
| `javac Main.java` 또는 `javac Main.java Utensil.java` | pancake |
| `javac Dessert.java Main.java` | potpie |

컴파일러에 어느 소스 파일을 먼저 건네느냐에 따라 동작이 달라지므로 반드시 바로잡아야 한다.

## 해결책

**톱레벨 클래스들을 서로 다른 소스 파일로 분리하라**

### Utensil.java

```java
public class Utensil {
    static final String NAME = "pan";
}
```

### Dessert.java

```java
public class Dessert {
    static final String NAME = "cake";
}
```

## 굳이 한 파일에 담고 싶다면: 정적 멤버 클래스

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(Utensil.NAME + Dessert.NAME);
    }

    private static class Utensil {
        static final String NAME = "pan";
    }

    private static class Dessert {
        static final String NAME = "cake";
    }
}
```

## 요약

- 소스 파일 하나에는 반드시 톱레벨 클래스(혹은 톱레벨 인터페이스)를 하나만 담자
- 이 규칙을 따르면 컴파일러가 한 클래스에 대한 정의를 여러 개 만들어내는 일은 사라진다
- 소스 파일을 어떤 순서로 컴파일하든 바이너리 파일이나 프로그램의 동작이 달라지는 일은 결코 일어나지 않을 것이다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_25](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_25)
