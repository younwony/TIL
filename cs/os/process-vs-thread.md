# 프로세스 vs 스레드

> 프로세스는 실행 중인 프로그램의 독립적인 인스턴스이고, 스레드는 프로세스 내에서 실행되는 작업의 단위이다.

## 핵심 개념

- **프로세스**: 독립된 메모리 공간(Code, Data, Stack, Heap)을 가진 실행 단위
- **스레드**: 프로세스 내에서 Stack만 독립적으로 할당받고, 나머지는 공유하는 실행 단위
- **컨텍스트 스위칭**: 프로세스 전환이 스레드 전환보다 비용이 큼
- **멀티프로세스 vs 멀티스레드**: 안정성 vs 효율성의 트레이드오프

## 상세 설명

### 프로세스 (Process)

프로세스는 운영체제로부터 자원을 할당받는 작업의 단위이다.

```
┌─────────────────────────────┐
│         Process             │
├─────────────────────────────┤
│  Code   │ 실행할 프로그램 코드 │
├─────────────────────────────┤
│  Data   │ 전역 변수, 정적 변수 │
├─────────────────────────────┤
│  Heap   │ 동적 할당 메모리    │
├─────────────────────────────┤
│  Stack  │ 지역 변수, 함수 호출 │
└─────────────────────────────┘
```

**특징:**
- 각 프로세스는 독립된 메모리 공간을 가짐
- 프로세스 간 통신(IPC)이 필요: 파이프, 소켓, 공유 메모리 등
- 하나의 프로세스가 죽어도 다른 프로세스에 영향 없음

### 스레드 (Thread)

스레드는 프로세스 내에서 실행되는 흐름의 단위이다.

```
┌─────────────────────────────────────┐
│              Process                │
├─────────────────────────────────────┤
│  Code (공유)  │  Data (공유)  │  Heap (공유)  │
├─────────────────────────────────────┤
│  Thread 1   │  Thread 2   │  Thread 3   │
│  ┌───────┐  │  ┌───────┐  │  ┌───────┐  │
│  │ Stack │  │  │ Stack │  │  │ Stack │  │
│  └───────┘  │  └───────┘  │  └───────┘  │
└─────────────────────────────────────┘
```

**특징:**
- Stack만 독립적으로 할당, Code/Data/Heap은 공유
- 스레드 간 통신이 간단 (메모리 공유)
- 하나의 스레드 문제가 전체 프로세스에 영향

### 프로세스 vs 스레드 비교

| 구분 | 프로세스 | 스레드 |
|------|---------|--------|
| 메모리 | 독립적 | Stack만 독립, 나머지 공유 |
| 생성 비용 | 높음 | 낮음 |
| 컨텍스트 스위칭 | 무거움 (메모리 전환) | 가벼움 (Stack만 전환) |
| 통신 | IPC 필요 | 메모리 공유로 간단 |
| 안정성 | 높음 (격리) | 낮음 (공유 자원 문제) |
| 동기화 | 불필요 | 필요 (Race Condition) |

### 컨텍스트 스위칭 (Context Switching)

CPU가 현재 작업을 멈추고 다른 작업으로 전환하는 과정.

**프로세스 컨텍스트 스위칭:**
1. 현재 프로세스 상태를 PCB(Process Control Block)에 저장
2. 다음 프로세스의 PCB에서 상태 복원
3. 메모리 맵, 캐시 등 전체 전환 필요

**스레드 컨텍스트 스위칭:**
1. 현재 스레드의 레지스터, Stack 포인터 저장
2. 다음 스레드의 레지스터, Stack 포인터 복원
3. 같은 프로세스 내라면 메모리 전환 불필요

### 멀티프로세스 vs 멀티스레드

**멀티프로세스:**
- 장점: 안정성 (하나가 죽어도 다른 프로세스 정상)
- 단점: 메모리 사용량 증가, IPC 오버헤드
- 예시: Chrome 브라우저 (탭별 프로세스)

**멀티스레드:**
- 장점: 자원 효율적, 통신 간단
- 단점: 동기화 문제, 하나의 스레드 오류가 전체 영향
- 예시: 웹 서버의 요청 처리

## 예제 코드

### Java에서 스레드 생성

```java
// 1. Thread 클래스 상속
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}

// 2. Runnable 인터페이스 구현
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable: " + Thread.currentThread().getName());
    }
}

// 실행
public class ThreadExample {
    public static void main(String[] args) {
        // Thread 상속
        new MyThread().start();

        // Runnable 구현
        new Thread(new MyRunnable()).start();

        // Lambda (Java 8+)
        new Thread(() -> {
            System.out.println("Lambda: " + Thread.currentThread().getName());
        }).start();
    }
}
```

### 스레드 동기화

```java
class Counter {
    private int count = 0;

    // synchronized로 동기화
    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

## 면접 예상 질문

- **Q: 프로세스와 스레드의 차이점은?**
  - A: 프로세스는 독립된 메모리 공간을 가진 실행 단위이고, 스레드는 프로세스 내에서 Stack만 독립적으로 갖고 Code/Data/Heap을 공유하는 실행 단위입니다. 스레드는 생성 비용과 컨텍스트 스위칭 비용이 적지만, 동기화 문제가 발생할 수 있습니다.

- **Q: 멀티프로세스 대신 멀티스레드를 사용하는 이유는?**
  - A: 스레드는 메모리를 공유하므로 자원 효율이 좋고, 컨텍스트 스위칭 비용이 적습니다. 또한 스레드 간 통신이 간단합니다. 단, 동기화에 주의해야 합니다.

- **Q: 컨텍스트 스위칭이란?**
  - A: CPU가 현재 실행 중인 작업의 상태를 저장하고, 다른 작업의 상태를 복원하여 전환하는 과정입니다. 프로세스 전환은 메모리 맵 전체를 바꿔야 하므로 스레드 전환보다 비용이 큽니다.

## 참고 자료

- Operating System Concepts (Silberschatz)
- [Oracle Java Thread Documentation](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
