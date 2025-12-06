# Week 6: 상속 (Inheritance)

## 개요

Java 상속의 핵심 개념에 대해 학습합니다.

## 다형성과 타입

```java
// 부모 클래스
class Parent {
    String name = "parent";

    public void said() {
        System.out.println("I am Parent");
    }
}

// 자식 클래스
class Child extends Parent {
    String childName = "child";

    @Override
    public void said() {
        System.out.println("I am Child");
    }
}
```

### 다형성 활용

```java
// 부모 타입으로 자식 인스턴스 참조 가능
Parent p = new Child();
p.said();  // "I am Child" 출력

// 자식 타입으로 부모 인스턴스 참조 불가능
// Child c = new Parent();  // 컴파일 에러
```

## 캐스팅

### 업캐스팅 (Upcasting)

```java
Child child = new Child();
Parent parent = child;  // 암시적 업캐스팅
```

### 다운캐스팅 (Downcasting)

```java
Parent parent = new Child();
Child child = (Child) parent;  // 명시적 다운캐스팅

// 주의: 실제 타입이 맞지 않으면 ClassCastException 발생
Parent p = new Parent();
// Child c = (Child) p;  // ClassCastException!
```

### instanceof 연산자

```java
Parent p = new Child();

if (p instanceof Child) {
    Child c = (Child) p;
    // 안전하게 사용
}
```

## 메서드 오버라이딩

```java
class Parent {
    public void said() {
        System.out.println("I am Parent");
    }
}

class Child extends Parent {
    @Override
    public void said() {
        System.out.println("I am Child");
    }
}
```

**특징:**
- 런타임에 실제 객체 타입에 따라 메서드 결정
- `@Override` 어노테이션 권장

## super 키워드

```java
class Child extends Parent {
    @Override
    public void said() {
        super.said();  // 부모의 said() 호출
        System.out.println("I am Child");
    }

    public void printParentName() {
        System.out.println(super.name);  // 부모의 name 접근
    }
}
```

## 접근 제어자와 상속

| 접근 제어자 | 같은 클래스 | 같은 패키지 | 자식 클래스 | 다른 패키지 |
|------------|:---------:|:---------:|:---------:|:---------:|
| private    | O | X | X | X |
| default    | O | O | X | X |
| protected  | O | O | O | X |
| public     | O | O | O | O |

```java
class Parent {
    private void privateMethod() {}     // 자식에서 접근 불가
    void defaultMethod() {}              // 같은 패키지에서만 접근
    protected void protectedMethod() {}  // 자식 클래스에서 접근 가능
    public void publicMethod() {}        // 어디서나 접근 가능
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy6_상속](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy6_%EC%83%81%EC%86%8D)
