# Item 8: finalizer와 cleaner 사용을 피하라

## 핵심 정리

자바는 두 가지 객체 소멸자를 제공한다: **finalizer**와 **cleaner**. 하지만 이들은 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.

## finalizer와 cleaner의 문제점

### 1. 즉시 수행된다는 보장이 없다
- 객체에 접근할 수 없게 된 후 finalizer나 cleaner가 실행되기까지 얼마나 걸릴지 알 수 없다
- 따라서 **제때 실행되어야 하는 작업은 절대 할 수 없다**
- 예: 파일 닫기를 finalizer나 cleaner에 맡기면 시스템이 동시에 열 수 있는 파일 개수에 한계가 있어 중대한 오류를 일으킬 수 있다

### 2. 수행 여부조차 보장되지 않는다
- 상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 cleaner에 의존해서는 안 된다
- 예: 데이터베이스 같은 공유 자원의 영구 락(lock) 해제

### 3. 심각한 성능 문제
- finalizer를 사용한 객체를 생성하고 파괴하는 데 걸리는 시간이 try-with-resources를 사용하는 것보다 약 50배 느리다

### 4. finalizer 공격에 노출
- 생성자나 직렬화 과정에서 예외가 발생하면, 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있다
- 이 finalizer는 정적 필드에 자신의 참조를 할당하여 가비지 컬렉터가 수집하지 못하게 막을 수 있다

## 대안: AutoCloseable

```java
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    // 청소가 필요한 자원. 절대 Room을 참조해서는 안 된다!
    private static class State implements Runnable {
        int numJunkPiles;  // 방(Room) 안의 쓰레기 수

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // close 메서드나 cleaner가 호출한다
        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }

    private final State state;
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
```

## 사용법

```java
// 바람직한 사용법
try (Room myRoom = new Room(7)) {
    System.out.println("안녕~");
}

// 결과:
// 안녕~
// 방 청소
```

## cleaner의 적절한 용도

1. 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할
2. 네이티브 피어(native peer)와 연결된 객체에서 사용

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_8](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_8)
