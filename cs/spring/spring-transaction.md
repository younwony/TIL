# Spring 트랜잭션 관리 (Transaction Management)

> `[3] 중급` · 선수 지식: [트랜잭션](../db/transaction.md), [Spring AOP](./spring-aop.md)

> 데이터 일관성을 보장하기 위해 여러 작업을 하나의 논리적 단위로 묶어 관리하는 기능

`#트랜잭션` `#Transaction` `#Spring` `#스프링` `#Transactional` `#ACID` `#원자성` `#Atomicity` `#일관성` `#Consistency` `#격리성` `#Isolation` `#지속성` `#Durability` `#Propagation` `#전파` `#IsolationLevel` `#격리수준` `#Rollback` `#롤백` `#Commit` `#커밋` `#PlatformTransactionManager` `#트랜잭션매니저` `#선언적트랜잭션` `#프로그래밍방식` `#ReadOnly` `#Timeout`

## 왜 알아야 하는가?

데이터 일관성은 비즈니스의 핵심입니다. 은행 이체 중 에러가 발생하면 돈이 사라질 수 있습니다. Spring의 @Transactional 하나로 이런 문제를 해결할 수 있지만, 동작 원리를 모르면 의도치 않게 데이터가 깨질 수 있습니다.

## 핵심 개념

- **@Transactional**: 선언적 트랜잭션 관리
- **Propagation**: 트랜잭션 전파 방식
- **Isolation**: 격리 수준
- **Rollback**: 예외 발생 시 롤백 정책

## 쉽게 이해하기

**트랜잭션**을 은행 이체에 비유할 수 있습니다.

```
트랜잭션 없이:
┌─────────────────────────────────────────────────────────────┐
│  1. A 계좌에서 100만원 출금 ✓                               │
│  2. 시스템 에러 발생! 💥                                    │
│  3. B 계좌에 100만원 입금 ✗                                 │
│  → A에서 돈은 빠졌는데 B에는 안 들어감 (돈 증발!)            │
└─────────────────────────────────────────────────────────────┘

트랜잭션 적용:
┌─────────────────────────────────────────────────────────────┐
│  BEGIN TRANSACTION                                          │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ 1. A 계좌에서 100만원 출금                              │ │
│  │ 2. 시스템 에러 발생! 💥                                │ │
│  │ 3. ROLLBACK! 1번도 취소됨                              │ │
│  └────────────────────────────────────────────────────────┘ │
│  → A 계좌 원상복구, 데이터 일관성 유지!                      │
└─────────────────────────────────────────────────────────────┘

ACID = 원자성, 일관성, 격리성, 지속성
모두 성공하거나, 모두 실패하거나!
```

## 상세 설명

### 기본 사용법

```java
// 1. 클래스 레벨
@Service
@Transactional  // 모든 public 메서드에 적용
public class OrderService {
    public void createOrder() { }
    public void cancelOrder() { }
}

// 2. 메서드 레벨
@Service
public class OrderService {

    @Transactional
    public void createOrder() {
        // 트랜잭션 시작
        orderRepository.save(order);
        paymentService.process(payment);
        inventoryService.decrease(item);
        // 트랜잭션 커밋 (또는 롤백)
    }

    public void readOrder() {  // 트랜잭션 없음
        // ...
    }
}

// 3. 메서드 레벨이 클래스 레벨보다 우선
@Service
@Transactional(readOnly = true)  // 기본: 읽기 전용
public class OrderService {

    @Transactional  // 이 메서드만 readOnly = false
    public void createOrder() { }

    public List<Order> findOrders() { }  // readOnly = true 유지
}
```

### Propagation (전파 옵션)

```java
// 기존 트랜잭션이 있을 때 어떻게 동작할지 결정

// 1. REQUIRED (기본값): 있으면 참여, 없으면 새로 생성
@Transactional(propagation = Propagation.REQUIRED)
public void methodA() {
    // 트랜잭션 시작 (없으면)
    methodB();  // 같은 트랜잭션 참여
}

// 2. REQUIRES_NEW: 항상 새 트랜잭션 생성
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void logAction() {
    // 새 트랜잭션 시작, 기존 트랜잭션 일시 중단
    // 독립적으로 커밋/롤백
}

// 활용 예: 로그는 메인 트랜잭션과 독립적으로 저장
@Transactional
public void createOrder() {
    orderRepository.save(order);
    logService.log("주문 생성");  // REQUIRES_NEW
    throw new RuntimeException();  // 롤백되어도 로그는 유지
}

// 3. NESTED: 중첩 트랜잭션 (세이브포인트)
@Transactional(propagation = Propagation.NESTED)
public void nestedMethod() {
    // 부모 롤백 → 자식도 롤백
    // 자식 롤백 → 자식만 롤백, 부모는 유지
}

// 4. SUPPORTS: 있으면 참여, 없으면 없이 실행
// 5. NOT_SUPPORTED: 트랜잭션 없이 실행 (있으면 일시 중단)
// 6. MANDATORY: 반드시 있어야 함 (없으면 예외)
// 7. NEVER: 트랜잭션 있으면 예외
```

```
┌─────────────────────────────────────────────────────────────┐
│                전파 옵션 비교                                │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  호출 시점에 트랜잭션이 있는 경우:                           │
│                                                              │
│  REQUIRED      │████████████████│  기존 트랜잭션 참여       │
│  REQUIRES_NEW  │    │████████│  │  새 트랜잭션 (기존 중단)  │
│  NESTED        │████│───────│███│  중첩 (세이브포인트)      │
│  SUPPORTS      │████████████████│  기존 트랜잭션 참여       │
│  NOT_SUPPORTED │    │        │  │  트랜잭션 없이 실행       │
│  MANDATORY     │████████████████│  기존 트랜잭션 참여       │
│  NEVER         │     예외 발생    │  트랜잭션 있으면 에러    │
│                                                              │
│  호출 시점에 트랜잭션이 없는 경우:                           │
│                                                              │
│  REQUIRED      │████████████████│  새 트랜잭션 생성         │
│  REQUIRES_NEW  │████████████████│  새 트랜잭션 생성         │
│  NESTED        │████████████████│  새 트랜잭션 생성         │
│  SUPPORTS      │                │  트랜잭션 없이 실행       │
│  NOT_SUPPORTED │                │  트랜잭션 없이 실행       │
│  MANDATORY     │     예외 발생    │  트랜잭션 필수           │
│  NEVER         │                │  트랜잭션 없이 실행       │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Isolation (격리 수준)

```java
// 동시 트랜잭션 간의 격리 수준

@Transactional(isolation = Isolation.DEFAULT)        // DB 기본값 사용
@Transactional(isolation = Isolation.READ_UNCOMMITTED)  // Dirty Read 허용
@Transactional(isolation = Isolation.READ_COMMITTED)    // Dirty Read 방지
@Transactional(isolation = Isolation.REPEATABLE_READ)   // Non-Repeatable Read 방지
@Transactional(isolation = Isolation.SERIALIZABLE)      // 완전 격리 (성능 저하)

// 실무 권장
@Transactional(isolation = Isolation.READ_COMMITTED)  // 대부분의 경우
public void normalOperation() { }

@Transactional(isolation = Isolation.REPEATABLE_READ)  // 정합성 중요
public void financialOperation() { }
```

### Rollback 정책

```java
// 기본: RuntimeException, Error → 롤백
//       Checked Exception → 커밋

// 1. 특정 예외에서 롤백
@Transactional(rollbackFor = Exception.class)  // 모든 예외에서 롤백
public void method1() throws Exception { }

@Transactional(rollbackFor = {CustomException.class, IOException.class})
public void method2() { }

// 2. 특정 예외에서 롤백 제외
@Transactional(noRollbackFor = CustomBusinessException.class)
public void method3() { }

// 3. 수동 롤백
@Transactional
public void method4() {
    try {
        // 작업
    } catch (Exception e) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        // 또는 RuntimeException throw
    }
}

// 주의: Checked Exception은 기본적으로 롤백 안 됨!
@Transactional
public void riskyMethod() throws IOException {
    orderRepository.save(order);
    throw new IOException();  // 롤백 안 됨! (Checked Exception)
}

// 해결
@Transactional(rollbackFor = IOException.class)
public void safeMethod() throws IOException { }
```

### ReadOnly 최적화

```java
// 읽기 전용 트랜잭션
@Transactional(readOnly = true)
public List<Order> findOrders() {
    return orderRepository.findAll();
}

// 장점:
// 1. JPA: 스냅샷 저장 안 함 → 메모리 절약
// 2. JPA: Dirty Checking 안 함 → 성능 향상
// 3. DB: 읽기 전용 힌트 전달 → 최적화 가능
// 4. Replication: Slave DB로 라우팅 가능

// 활용 패턴
@Service
@Transactional(readOnly = true)  // 기본 읽기 전용
public class OrderService {

    public List<Order> findOrders() { }  // readOnly = true

    @Transactional  // 쓰기 필요한 메서드만 오버라이드
    public void createOrder() { }
}
```

### Timeout 설정

```java
@Transactional(timeout = 5)  // 5초 내 완료되지 않으면 롤백
public void longRunningOperation() {
    // 5초 초과 시 TransactionTimedOutException
}

// 단위: 초 (seconds)
// -1: 타임아웃 없음 (기본값, DB 기본값 사용)
```

### 동작 원리

```
┌─────────────────────────────────────────────────────────────┐
│              @Transactional 동작 원리                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. 프록시 생성                                              │
│     ┌─────────────────────────────────────────┐             │
│     │  OrderService$$Proxy                    │             │
│     │  ┌─────────────────────────────────────┐│             │
│     │  │ 트랜잭션 시작                        ││             │
│     │  │ ───────────────────────────────────││             │
│     │  │ 실제 OrderService.createOrder() 호출││             │
│     │  │ ───────────────────────────────────││             │
│     │  │ 성공 → 커밋 / 실패 → 롤백           ││             │
│     │  └─────────────────────────────────────┘│             │
│     └─────────────────────────────────────────┘             │
│                                                              │
│  2. TransactionManager 연동                                  │
│     - DataSourceTransactionManager (JDBC)                   │
│     - JpaTransactionManager (JPA)                           │
│     - HibernateTransactionManager (Hibernate)               │
│                                                              │
│  3. 트랜잭션 동기화                                          │
│     ThreadLocal에 Connection 저장                           │
│     같은 트랜잭션 내 모든 작업이 같은 Connection 사용        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 주의사항 (함정)

```java
// 1. 내부 호출 문제 - AOP 프록시 우회
@Service
public class OrderService {

    @Transactional
    public void createOrder() {
        // ...
        this.sendNotification();  // 트랜잭션 적용 안 됨!
    }

    @Transactional
    public void sendNotification() { }
}

// 해결: 클래스 분리
@Service
public class NotificationService {
    @Transactional
    public void sendNotification() { }
}

// 2. public 메서드만 적용
@Transactional
private void privateMethod() { }  // 트랜잭션 적용 안 됨!

// 3. 예외 삼킴
@Transactional
public void method() {
    try {
        riskyOperation();
    } catch (Exception e) {
        log.error("에러 발생", e);
        // 예외를 삼키면 정상 커밋됨!
    }
}

// 해결
@Transactional
public void method() {
    try {
        riskyOperation();
    } catch (Exception e) {
        log.error("에러 발생", e);
        throw e;  // 다시 던지거나
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}

// 4. 트랜잭션 범위 과다
@Transactional  // 전체가 트랜잭션
public void processOrder() {
    Order order = createOrder();        // DB 작업
    sendEmail(order);                   // 외부 API (느림)
    generateReport(order);              // 파일 I/O (느림)
}

// 해결: 트랜잭션 범위 최소화
public void processOrder() {
    Order order = createOrderWithTx();  // 트랜잭션 범위 최소화
    sendEmail(order);                   // 트랜잭션 외부
    generateReport(order);              // 트랜잭션 외부
}

@Transactional
public Order createOrderWithTx() {
    return orderRepository.save(order);
}
```

### 테스트에서 트랜잭션

```java
// @Transactional 테스트: 자동 롤백
@SpringBootTest
@Transactional  // 테스트 후 자동 롤백
class OrderServiceTest {

    @Test
    void createOrder_success() {
        orderService.createOrder(request);
        // 검증 후 자동 롤백 → DB 깨끗하게 유지
    }

    @Test
    @Rollback(false)  // 롤백 비활성화 (데이터 확인 시)
    void createOrder_checkData() { }

    @Test
    @Commit  // 커밋 강제
    void createOrder_commit() { }
}

// 주의: 실제 서비스와 다르게 동작할 수 있음
// - 지연 쓰기가 flush 안 될 수 있음
// - REQUIRES_NEW 트랜잭션이 보이지 않을 수 있음
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 선언적 관리 (코드 간결) | 프록시 이해 필요 |
| 일관된 예외 처리 | 내부 호출 문제 |
| 다양한 설정 옵션 | 설정 복잡도 |
| 테스트 용이 | 성능 오버헤드 |

## 면접 예상 질문

### Q: @Transactional이 동작하지 않는 경우는?

A: (1) **내부 호출**: 같은 클래스의 메서드를 `this.method()`로 호출 → 프록시 우회. (2) **private 메서드**: public만 프록시 적용. (3) **예외 삼킴**: catch로 예외를 잡고 다시 던지지 않으면 커밋. (4) **Checked Exception**: 기본적으로 롤백 안 됨. **해결**: 클래스 분리, rollbackFor 설정, 예외 재발생.

### Q: REQUIRES_NEW와 NESTED의 차이는?

A: **REQUIRES_NEW**: 완전히 독립된 새 트랜잭션. 부모 롤백해도 자식 커밋 유지. **NESTED**: 부모 트랜잭션 내 세이브포인트. 부모 롤백 시 자식도 롤백, 자식 롤백 시 세이브포인트까지만 롤백. **사용 사례**: REQUIRES_NEW는 로그 저장, NESTED는 부분 롤백 필요 시.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [트랜잭션](../db/transaction.md) | 선수 지식 | [3] 중급 |
| [Spring AOP](./spring-aop.md) | 동작 원리 | [3] 중급 |
| [격리 수준](../db/isolation-level.md) | Isolation | [4] 심화 |

## 참고 자료

- [Spring Transaction Management](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)
- [Baeldung - Spring Transaction](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring)
