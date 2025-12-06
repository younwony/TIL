# Week 10: 멀티스레드 프로그래밍 (Multithreading)

## 개요

Java 멀티스레드 프로그래밍에 대해 학습합니다.

## 스레드 생성 방법

### 1. Thread 클래스 상속

```java
public class ExtendsThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread 클래스 상속");
        System.out.println("현재 스레드: " + Thread.currentThread().getName());
    }
}

// 사용
Thread thread = new ExtendsThread();
thread.start();
```

### 2. Runnable 인터페이스 구현

```java
public class RunThread implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable 인터페이스 구현");
        System.out.println("현재 스레드: " + Thread.currentThread().getName());
    }
}

// 사용
Thread thread = new Thread(new RunThread());
thread.start();
```

### 3. 람다식 사용

```java
Thread thread = new Thread(() -> {
    System.out.println("람다식으로 스레드 생성");
});
thread.start();
```

## 주의사항

```java
Thread thread = new ExtendsThread();
thread.start();  // 정상
// thread.start();  // IllegalThreadStateException! 같은 스레드 재시작 불가
```

## 스레드 상태

| 상태 | 설명 |
|------|------|
| NEW | 스레드 생성, 아직 start() 호출 안됨 |
| RUNNABLE | 실행 중 또는 실행 대기 |
| BLOCKED | 모니터 락 대기 |
| WAITING | 다른 스레드 대기 |
| TIMED_WAITING | 지정 시간 대기 |
| TERMINATED | 실행 완료 |

## 동기화 (Synchronization)

### synchronized 메서드

```java
public class Calculator {
    private int memory;

    public synchronized void setMemory(int memory) {
        this.memory = memory;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        System.out.println(Thread.currentThread().getName() + ": " + this.memory);
    }
}
```

### synchronized 블록

```java
public void setMemory(int memory) {
    synchronized (this) {
        this.memory = memory;
        // 임계 영역
    }
}
```

## 스레드 협력

### wait()과 notify()

```java
public class SharedObject {
    public synchronized void methodA() {
        System.out.println("methodA 시작");
        notify();  // 대기 중인 스레드 깨우기
        try {
            wait();  // 대기 상태로 전환
        } catch (InterruptedException e) {}
        System.out.println("methodA 종료");
    }

    public synchronized void methodB() {
        System.out.println("methodB 시작");
        notify();
        try {
            wait();
        } catch (InterruptedException e) {}
        System.out.println("methodB 종료");
    }
}
```

## 스레드 풀 (Thread Pool)

```java
ExecutorService executor = Executors.newFixedThreadPool(3);

for (int i = 0; i < 10; i++) {
    final int taskNum = i;
    executor.execute(() -> {
        System.out.println("Task " + taskNum + " - " + Thread.currentThread().getName());
    });
}

executor.shutdown();
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy10_멀티쓰레드프로그래밍](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy10_%EB%A9%80%ED%8B%B0%EC%93%B0%EB%A0%88%EB%93%9C%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D)
