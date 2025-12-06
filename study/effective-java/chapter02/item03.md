# Item 3: private 생성자나 열거 타입으로 싱글턴임을 보증하라

## 핵심 정리

싱글턴(singleton)이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.

## 싱글턴을 만드는 방식

### 방식 1: public static final 필드 방식

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() { }

    public void leaveTheBuilding() { }
}
```

**장점:**
- 해당 클래스가 싱글턴임이 API에 명백히 드러난다
- 간결하다

### 방식 2: 정적 팩터리 방식

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();

    private Elvis() { }

    public static Elvis getInstance() { return INSTANCE; }

    public void leaveTheBuilding() { }
}
```

**장점:**
- API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다
- 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다
- 정적 팩터리의 메서드 참조를 공급자(Supplier)로 사용할 수 있다

### 방식 3: 열거 타입 방식 (권장)

```java
public enum Elvis {
    INSTANCE;

    public void leaveTheBuilding() { }
}
```

**장점:**
- 더 간결하다
- 추가 노력 없이 직렬화할 수 있다
- 복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아준다
- **대부분 상황에서 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다**

## 주의사항

### 리플렉션 공격 방어
```java
private Elvis() {
    if (INSTANCE != null) {
        throw new RuntimeException("생성자를 호출할 수 없습니다!");
    }
}
```

### 직렬화
싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하다.
모든 인스턴스 필드를 transient라고 선언하고 readResolve 메서드를 제공해야 한다.

```java
// 싱글턴임을 보장해주는 readResolve 메서드
private Object readResolve() {
    // 진짜 Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡긴다
    return INSTANCE;
}
```

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_3](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_3)
