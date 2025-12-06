# Item 18: 상속보다는 컴포지션을 사용하라

## 핵심 정리

상속은 코드를 재사용하는 강력한 수단이지만, 항상 최선은 아니다. 잘못 사용하면 오류를 내기 쉬운 소프트웨어를 만들게 된다.

## 상속의 문제점

### 메서드 호출과 달리 상속은 캡슐화를 깨뜨린다

```java
// 잘못된 예 - 상속을 잘못 사용했다!
public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    public InstrumentedHashSet() { }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);  // 내부적으로 add를 호출!
    }

    public int getAddCount() {
        return addCount;
    }
}
```

**문제:** `addAll`을 호출하면 `add`가 내부적으로 호출되어 addCount가 중복 계산된다.

## 해결책: 컴포지션(composition)

기존 클래스를 확장하는 대신, 새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조하게 하자.

```java
// 래퍼 클래스 - 상속 대신 컴포지션을 사용했다
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

// 재사용할 수 있는 전달 클래스
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    public ForwardingSet(Set<E> s) { this.s = s; }

    public void clear() { s.clear(); }
    public boolean contains(Object o) { return s.contains(o); }
    public boolean isEmpty() { return s.isEmpty(); }
    public int size() { return s.size(); }
    public Iterator<E> iterator() { return s.iterator(); }
    public boolean add(E e) { return s.add(e); }
    public boolean remove(Object o) { return s.remove(o); }
    public boolean containsAll(Collection<?> c) { return s.containsAll(c); }
    public boolean addAll(Collection<? extends E> c) { return s.addAll(c); }
    public boolean removeAll(Collection<?> c) { return s.removeAll(c); }
    public boolean retainAll(Collection<?> c) { return s.retainAll(c); }
    public Object[] toArray() { return s.toArray(); }
    public <T> T[] toArray(T[] a) { return s.toArray(a); }
    @Override public boolean equals(Object o) { return s.equals(o); }
    @Override public int hashCode() { return s.hashCode(); }
    @Override public String toString() { return s.toString(); }
}
```

## 컴포지션의 장점

1. 한 번만 구현해두면 어떠한 Set 구현체라도 계측할 수 있다
2. 기존 생성자들과도 함께 사용할 수 있다

```java
Set<Instant> times = new InstrumentedSet<>(new TreeSet<>(cmp));
Set<E> s = new InstrumentedSet<>(new HashSet<>(INIT_CAPACITY));
```

## 상속을 사용해도 괜찮은 경우

- 클래스 B가 클래스 A와 is-a 관계일 때만 클래스 A를 상속해야 한다
- "B가 정말 A인가?"를 자문해보자
- 확실하지 않다면 B는 A를 상속해서는 안 된다

## 참고

- 원본 코드: [effectiveJava/chapter_4/item_18](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_4/item_18)
