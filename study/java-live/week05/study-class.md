# Week 5: 클래스 (Class)

## 개요

Java 클래스와 변수의 종류에 대해 학습합니다.

## 변수의 종류

### 1. 클래스 변수 (Static Variable)

```java
public class StudyClass {
    static String school;

    static {
        school = "Default School";
        System.out.println("정적 블록 실행");
    }
}
```

**특징:**
- `static` 키워드로 선언
- 클래스 로드 시 한 번만 초기화
- 모든 인스턴스가 공유
- 정적 블록에서 초기화 가능

### 2. 인스턴스 변수 (Instance Variable)

```java
public class StudyClass {
    String name;
    int age;
    int grade;

    {
        // 인스턴스 초기화 블록
        name = "없음";
        age = 0;
        grade = 1;
        System.out.println("인스턴스 블록 실행");
    }
}
```

**특징:**
- 객체마다 독립적인 값을 가짐
- 인스턴스 생성 시 초기화
- 인스턴스 블록에서 초기화 가능

### 3. 지역 변수 (Local Variable)

```java
public void method() {
    int level = 1;  // 지역 변수
    System.out.println(level);
}
```

**특징:**
- 메서드 내에서 선언
- 메서드 실행 시 생성, 종료 시 소멸
- 반드시 초기화 후 사용

## 생성자 오버로딩

```java
public class StudyClass {
    String name;
    int age;
    int grade;

    // 기본 생성자
    public StudyClass() {
    }

    // 이름만 받는 생성자
    public StudyClass(String name) {
        this.name = name;
    }

    // 전체 정보를 받는 생성자
    public StudyClass(String name, int age, int grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}
```

## 초기화 순서

1. 정적 변수 기본값 할당
2. 정적 블록 실행
3. 인스턴스 변수 기본값 할당
4. 인스턴스 블록 실행
5. 생성자 실행

```java
public class InitOrder {
    static int staticVar = 1;           // 1. 정적 변수 초기화
    int instanceVar = 2;                // 3. 인스턴스 변수 초기화

    static {
        staticVar = 10;                 // 2. 정적 블록 실행
    }

    {
        instanceVar = 20;               // 4. 인스턴스 블록 실행
    }

    public InitOrder() {
        instanceVar = 30;               // 5. 생성자 실행
    }
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy5](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy5)
