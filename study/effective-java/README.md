# Effective Java Study

Effective Java 서적을 기반으로 한 스터디입니다.

> 원본 레포지토리: https://github.com/younwony/effectiveJava

## 프로젝트 정보

- **언어:** Java 1.8
- **빌드 도구:** Gradle
- **프레임워크:** Spring Boot 2.7.11
- **패키지:** `dev.wony.effectivejava`

## 목차

### Chapter 2: 객체 생성과 파괴

- [Item 1: 생성자 대신 정적 팩터리 메서드를 고려하라](chapter02/item01.md)
- [Item 2: 생성자에 매개변수가 많다면 빌더를 고려하라](chapter02/item02.md)
- [Item 3: private 생성자나 열거 타입으로 싱글턴임을 보증하라](chapter02/item03.md)
- [Item 4: 인스턴스화를 막으려거든 private 생성자를 사용하라](chapter02/item04.md)
- [Item 5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라](chapter02/item05.md)
- [Item 6: 불필요한 객체 생성을 피하라](chapter02/item06.md)
- [Item 7: 다 쓴 객체 참조를 해제하라](chapter02/item07.md)
- [Item 8: finalizer와 cleaner 사용을 피하라](chapter02/item08.md)
- [Item 9: try-finally보다는 try-with-resources를 사용하라](chapter02/item09.md)

### Chapter 3: 모든 객체의 공통 메서드

- [Item 10: equals는 일반 규약을 지켜 재정의하라](chapter03/item10.md)
- [Item 11: equals를 재정의하려거든 hashCode도 재정의하라](chapter03/item11.md)
- [Item 12: toString을 항상 재정의하라](chapter03/item12.md)
- [Item 13: clone 재정의는 주의해서 진행하라](chapter03/item13.md)
- [Item 14: Comparable을 구현할지 고려하라](chapter03/item14.md)

### Chapter 4: 클래스와 인터페이스

- [Item 15: 클래스와 멤버의 접근 권한을 최소화하라](chapter04/item15.md)
- [Item 16: public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라](chapter04/item16.md)
- [Item 17: 변경 가능성을 최소화하라](chapter04/item17.md)
- [Item 18: 상속보다는 컴포지션을 사용하라](chapter04/item18.md)
- [Item 19: 상속을 고려해 설계하고 문서화하라. 그러지 않았다면 상속을 금지하라](chapter04/item19.md)
- [Item 20: 추상 클래스보다는 인터페이스를 우선하라](chapter04/item20.md)
- [Item 21: 인터페이스는 구현하는 쪽을 생각해 설계하라](chapter04/item21.md)
- [Item 22: 인터페이스는 타입을 정의하는 용도로만 사용하라](chapter04/item22.md)
- [Item 23: 태그 달린 클래스보다는 클래스 계층구조를 활용하라](chapter04/item23.md)
- [Item 24: 멤버 클래스는 되도록 static으로 만들라](chapter04/item24.md)
- [Item 25: 톱레벨 클래스는 한 파일에 하나만 담으라](chapter04/item25.md)

### Chapter 5: 제네릭

- Item 26 ~ Item 33 (예정)

### Chapter 6: 열거 타입과 애너테이션

- Item 34 ~ Item 41 (예정)

### Chapter 7: 람다와 스트림

- Item 42 ~ Item 48 (예정)

### Chapter 8: 메서드

- Item 49 ~ Item 56 (예정)

### Chapter 9: 일반적인 프로그래밍 원칙

- Item 57 ~ Item 68 (예정)

### Chapter 10: 예외

- Item 69 ~ Item 77 (예정)

### Chapter 11: 동시성

- Item 78 ~ Item 84 (예정)

### Chapter 12: 직렬화

- Item 85 ~ Item 90 (예정)
