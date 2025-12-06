# Item 2: 생성자에 매개변수가 많다면 빌더를 고려하라

## 핵심 정리

정적 팩터리와 생성자에는 똑같은 제약이 하나 있다. 선택적 매개변수가 많을 때 적절히 대응하기 어렵다는 점이다.

## 대안 1: 점층적 생성자 패턴 (Telescoping Constructor Pattern)

```java
public class NutritionFacts {
    private final int servingSize;   // 필수
    private final int servings;      // 필수
    private final int calories;      // 선택
    private final int fat;           // 선택
    private final int sodium;        // 선택
    private final int carbohydrate;  // 선택

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }
    // ... 생략
}
```

**단점:** 매개변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다.

## 대안 2: 자바빈즈 패턴 (JavaBeans Pattern)

```java
public class NutritionFacts {
    private int servingSize = -1;  // 필수
    private int servings = -1;     // 필수
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public NutritionFacts() { }

    public void setServingSize(int val) { servingSize = val; }
    public void setServings(int val) { servings = val; }
    public void setCalories(int val) { calories = val; }
    // ... 생략
}
```

**단점:**
- 객체 하나를 만들려면 메서드를 여러 개 호출해야 한다
- 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓인다
- 클래스를 불변으로 만들 수 없다

## 대안 3: 빌더 패턴 (Builder Pattern)

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
```

**사용법:**
```java
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
    .calories(100)
    .sodium(35)
    .carbohydrate(27)
    .build();
```

## 계층적 빌더 패턴

```java
public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        // 하위 클래스는 이 메서드를 재정의하여 "this"를 반환하도록 해야 한다
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}
```

## 장점

1. 빌더 패턴은 점층적 생성자 패턴의 안전성과 자바빈즈 패턴의 가독성을 겸비했다
2. 빌더 하나로 여러 객체를 순회하면서 만들 수 있다
3. 빌더에 넘기는 매개변수에 따라 다른 객체를 만들 수 있다
4. 객체마다 부여되는 일련번호와 같은 특정 필드는 빌더가 알아서 채우도록 할 수 있다

## 단점

1. 객체를 만들려면 빌더부터 만들어야 한다
2. 빌더 생성 비용이 크지는 않지만 성능에 민감한 상황에서는 문제가 될 수 있다
3. 점층적 생성자 패턴보다 코드가 장황해서 매개변수가 4개 이상은 되어야 값어치를 한다

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_2](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_2)
