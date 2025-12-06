# Item 15: 클래스와 멤버의 접근 권한을 최소화하라

## 핵심 정리

잘 설계된 컴포넌트는 모든 내부 구현을 완벽히 숨겨, 구현과 API를 깔끔히 분리한다. 오직 API를 통해서만 다른 컴포넌트와 소통하며 서로의 내부 동작 방식에는 전혀 개의치 않는다. 이것이 **정보 은닉(캡슐화)**의 개념이다.

## 정보 은닉의 장점

1. 시스템 개발 속도를 높인다 (여러 컴포넌트를 병렬로 개발 가능)
2. 시스템 관리 비용을 낮춘다 (각 컴포넌트를 더 빨리 파악 가능)
3. 정보 은닉 자체가 성능을 높여주지는 않지만, 성능 최적화에 도움을 준다
4. 소프트웨어 재사용성을 높인다
5. 큰 시스템을 제작하는 난이도를 낮춘다

## 접근 제어 메커니즘

| 접근 수준 | 설명 |
|----------|------|
| private | 멤버를 선언한 톱레벨 클래스에서만 접근 가능 |
| package-private | 멤버가 소속된 패키지 안의 모든 클래스에서 접근 가능 (기본 접근 수준) |
| protected | package-private의 접근 범위를 포함하며, 이 멤버를 선언한 클래스의 하위 클래스에서도 접근 가능 |
| public | 모든 곳에서 접근 가능 |

## 핵심 원칙

- **모든 클래스와 멤버의 접근성을 가능한 한 좁혀야 한다**
- 톱레벨 클래스와 인터페이스에 부여할 수 있는 접근 수준은 package-private과 public 두 가지다
- public으로 선언하면 공개 API가 되므로 하위 호환을 위해 영원히 관리해야 한다
- package-private으로 선언하면 API가 아닌 내부 구현이 되므로 언제든 수정할 수 있다

## 주의사항

### public 클래스의 인스턴스 필드는 되도록 public이 아니어야 한다

```java
// 나쁜 예
public class Thing {
    public String name;
}

// 좋은 예
public class Thing {
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

### public static final 배열 필드를 두지 마라

```java
// 보안 허점이 있다
public static final Thing[] VALUES = { ... };

// 해결책 1: public 불변 리스트 추가
private static final Thing[] PRIVATE_VALUES = { ... };
public static final List<Thing> VALUES =
    Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

// 해결책 2: 복사본을 반환하는 public 메서드 추가
private static final Thing[] PRIVATE_VALUES = { ... };
public static final Thing[] values() {
    return PRIVATE_VALUES.clone();
}
```

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_15](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_15)
