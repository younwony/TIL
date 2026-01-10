# 낙관적 락 vs 비관적 락 (Optimistic vs Pessimistic Lock)

> `[3] 중급` · 선수 지식: [트랜잭션](./transaction.md), [인덱스](./index.md)

> 동시에 같은 데이터를 수정할 때 충돌을 방지하는 두 가지 전략: "충돌이 없을 거야" vs "충돌할 거야"

`#낙관적락` `#OptimisticLock` `#비관적락` `#PessimisticLock` `#동시성제어` `#Concurrency` `#버전관리` `#Version` `#충돌` `#Conflict` `#JPA` `#Hibernate` `#SELECT FOR UPDATE` `#DeadLock` `#데드락` `#LostUpdate` `#갱신손실` `#재고관리` `#Inventory` `#Race Condition` `#@Version` `#ObjectOptimisticLockingFailureException` `#트랜잭션` `#Transaction` `#격리수준` `#IsolationLevel` `#커머스` `#Ecommerce`

## 왜 알아야 하는가?

- **실무**: 재고 차감, 좌석 예약, 포인트 차감 등 동시성 이슈 해결 필수
- **면접**: "두 사용자가 동시에 같은 상품을 주문하면?" 핵심 질문
- **기반 지식**: 분산 락, 트랜잭션 격리 수준 이해의 기초

## 핵심 개념

- **낙관적 락**: 충돌이 드물다고 가정, 커밋 시점에 검증 (버전 체크)
- **비관적 락**: 충돌이 많다고 가정, 조회 시점에 락 획득 (SELECT FOR UPDATE)
- **Lost Update**: 두 트랜잭션이 동시에 수정하여 한쪽 변경이 사라지는 현상

## 쉽게 이해하기

**화장실 사용**에 비유하면 이해가 쉽습니다.

```
낙관적 락: "노크하고 들어가기"
┌─────────────────────────────────────────────┐
│  1. 문 열고 들어감 (락 없이 작업 시작)       │
│  2. 사용 완료 후 나오려는데...               │
│  3. 누군가 이미 사용했던 흔적 발견!          │
│  4. "아, 충돌이네" → 다시 시도               │
│                                              │
│  장점: 대기 시간 없음                        │
│  단점: 충돌 시 재시도 비용                   │
└─────────────────────────────────────────────┘

비관적 락: "문 잠그고 들어가기"
┌─────────────────────────────────────────────┐
│  1. 문 잠그고 들어감 (락 획득)              │
│  2. 다른 사람은 문 앞에서 대기               │
│  3. 사용 완료 후 문 열고 나옴 (락 해제)      │
│  4. 다음 사람 입장                           │
│                                              │
│  장점: 충돌 없음                            │
│  단점: 대기 시간 발생                        │
└─────────────────────────────────────────────┘
```

## 상세 설명

### 문제 상황: Lost Update

```
시간    트랜잭션 A (주문 1)        트랜잭션 B (주문 2)        재고
────────────────────────────────────────────────────────────────
T1      SELECT stock              -                         10
        FROM product
        WHERE id = 1
        → 재고 10개 확인

T2      -                         SELECT stock              10
                                  FROM product
                                  WHERE id = 1
                                  → 재고 10개 확인

T3      UPDATE product            -                         10
        SET stock = 9
        WHERE id = 1
        → 재고 9개로 변경

T4      -                         UPDATE product            9 → 9
                                  SET stock = 9
                                  WHERE id = 1
                                  → 재고 9개로 변경 (B도!)

결과: 2개 주문했는데 재고 1개만 감소! (Lost Update)
```

### 낙관적 락 (Optimistic Lock)

#### 원리
1. 조회 시 **버전(version)** 함께 조회
2. 수정 시 **버전 조건** 포함하여 UPDATE
3. 버전 불일치 시 **예외 발생** → 재시도

#### JPA 구현

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int stock;

    @Version  // 낙관적 락 핵심!
    private Long version;

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final int MAX_RETRY = 3;

    private final ProductRepository productRepository;

    @Transactional
    public void order(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("상품 없음"));

        product.decreaseStock(quantity);
        // 커밋 시점에 version 체크
        // version 불일치 시 ObjectOptimisticLockingFailureException 발생
    }

    // 재시도 로직 포함
    public void orderWithRetry(Long productId, int quantity) {
        int retryCount = 0;
        while (retryCount < MAX_RETRY) {
            try {
                order(productId, quantity);
                return;  // 성공
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= MAX_RETRY) {
                    throw new RuntimeException("주문 실패: 재시도 횟수 초과", e);
                }
                // 잠시 대기 후 재시도
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
```

#### 실제 실행되는 SQL

```sql
-- 조회
SELECT id, name, stock, version
FROM product
WHERE id = 1;
-- 결과: id=1, stock=10, version=1

-- 수정 (version 조건 포함!)
UPDATE product
SET stock = 9, version = 2
WHERE id = 1 AND version = 1;
-- affected rows = 1 → 성공
-- affected rows = 0 → 다른 트랜잭션이 먼저 수정함 (충돌!)
```

#### 동작 흐름

```
트랜잭션 A                    트랜잭션 B                    DB
    │                            │                         │
    │ SELECT (version=1)         │                         │
    │ ←──────────────────────────────────────────────────── │
    │                            │ SELECT (version=1)       │
    │                            │ ←──────────────────────── │
    │                            │                         │
    │ UPDATE WHERE version=1     │                         │
    │ ─────────────────────────────────────────────────────→│
    │ OK (version=2로 변경)      │                         │
    │ ←────────────────────────────────────────────────────│
    │                            │                         │
    │                            │ UPDATE WHERE version=1   │
    │                            │ ─────────────────────────→│
    │                            │ FAIL (version≠1)         │
    │                            │ ←─────────────────────────│
    │                            │                         │
    │                            │ 예외 발생 → 재시도       │
```

### 비관적 락 (Pessimistic Lock)

#### 원리
1. 조회 시점에 **락 획득** (SELECT FOR UPDATE)
2. 다른 트랜잭션은 **대기**
3. 커밋/롤백 시 **락 해제**

#### JPA 구현

```java
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 비관적 락 - 쓰기 락 (배타적)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);

    // 비관적 락 - 읽기 락 (공유)
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithSharedLock(@Param("id") Long id);
}
```

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    @Transactional
    public void orderWithPessimisticLock(Long productId, int quantity) {
        // SELECT ... FOR UPDATE 실행
        Product product = productRepository.findByIdWithPessimisticLock(productId)
            .orElseThrow(() -> new RuntimeException("상품 없음"));

        product.decreaseStock(quantity);
        // 트랜잭션 종료 시 락 자동 해제
    }
}
```

#### 실제 실행되는 SQL

```sql
-- MySQL (InnoDB)
SELECT id, name, stock, version
FROM product
WHERE id = 1
FOR UPDATE;  -- 행 락 획득

-- 다른 트랜잭션의 SELECT FOR UPDATE는 대기
-- 일반 SELECT는 가능 (MVCC)
```

#### 동작 흐름

```
트랜잭션 A                    트랜잭션 B                    DB
    │                            │                         │
    │ SELECT FOR UPDATE          │                         │
    │ ─────────────────────────────────────────────────────→│
    │ OK (락 획득)               │                         │
    │ ←────────────────────────────────────────────────────│
    │                            │                         │
    │                            │ SELECT FOR UPDATE       │
    │                            │ ─────────────────────────→│
    │                            │ (대기...)               │
    │                            │                         │
    │ UPDATE                     │                         │
    │ ─────────────────────────────────────────────────────→│
    │ COMMIT (락 해제)           │                         │
    │ ←────────────────────────────────────────────────────│
    │                            │                         │
    │                            │ OK (락 획득)             │
    │                            │ ←─────────────────────────│
    │                            │ 최신 데이터로 작업       │
```

### 비교표

| 항목 | 낙관적 락 | 비관적 락 |
|------|----------|----------|
| **충돌 가정** | 드물다 | 자주 발생 |
| **락 시점** | 커밋 시점 | 조회 시점 |
| **대기** | 없음 | 있음 |
| **충돌 시** | 예외 → 재시도 | 대기 후 처리 |
| **성능** | 충돌 적을 때 좋음 | 충돌 많을 때 좋음 |
| **데드락** | 없음 | 가능 |
| **구현** | @Version | SELECT FOR UPDATE |
| **적합한 상황** | 읽기 많음, 충돌 적음 | 쓰기 많음, 충돌 많음 |

### 언제 무엇을 쓸까?

```
┌─────────────────────────────────────────────────────────────────┐
│                    락 전략 선택 가이드                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  충돌이 자주 발생하는가?                                         │
│       │                                                          │
│    No │   Yes                                                    │
│       │     │                                                    │
│       ▼     ▼                                                    │
│  ┌─────────┐ ┌─────────────────────────────────────┐            │
│  │낙관적 락 │ │      재시도 비용이 큰가?             │            │
│  └─────────┘ └──────────────┬──────────────────────┘            │
│                              │                                   │
│                         No   │   Yes                             │
│                           │   │                                  │
│                           ▼   ▼                                  │
│                     ┌─────────┐ ┌─────────┐                     │
│                     │낙관적 락 │ │비관적 락 │                     │
│                     │+ 재시도  │ └─────────┘                     │
│                     └─────────┘                                  │
│                                                                  │
│  예시:                                                           │
│  - 상품 정보 수정 (관리자만) → 낙관적 락                         │
│  - 인기 상품 재고 (초당 수백 건) → 비관적 락                     │
│  - 일반 상품 재고 (가끔 충돌) → 낙관적 락 + 재시도              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 실전 예제: 재고 관리

```java
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    /**
     * 일반 상품: 낙관적 락 (충돌 드묾)
     */
    @Transactional
    @Retryable(
        value = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void decreaseStockOptimistic(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow();
        product.decreaseStock(quantity);
    }

    /**
     * 인기 상품/이벤트: 비관적 락 (충돌 빈번)
     */
    @Transactional
    public void decreaseStockPessimistic(Long productId, int quantity) {
        Product product = productRepository.findByIdWithPessimisticLock(productId)
            .orElseThrow();
        product.decreaseStock(quantity);
    }

    /**
     * 선착순 이벤트: 분산 락 (멀티 서버 환경)
     * @see DistributedLockService
     */
}
```

## 트레이드오프

| 상황 | 낙관적 락 | 비관적 락 |
|------|----------|----------|
| 읽기 많음, 쓰기 적음 | ✅ 최적 | ❌ 과도한 락 |
| 쓰기 많음, 충돌 빈번 | ❌ 재시도 비용 | ✅ 최적 |
| 긴 트랜잭션 | ✅ 락 유지 안 함 | ❌ 긴 락 대기 |
| 외부 API 호출 포함 | ✅ 적합 | ❌ 락 시간 증가 |
| 데드락 가능성 | 없음 | 있음 (주의 필요) |

## 트러블슈팅

### 사례 1: 비관적 락 데드락

#### 증상
```
트랜잭션 A: product 1 락 → product 2 락 시도 (대기)
트랜잭션 B: product 2 락 → product 1 락 시도 (대기)
→ 둘 다 영원히 대기 (Deadlock!)
```

#### 해결 방법
```java
// 락 획득 순서 통일 (ID 오름차순)
@Transactional
public void transferStock(Long fromId, Long toId, int quantity) {
    Long firstId = Math.min(fromId, toId);
    Long secondId = Math.max(fromId, toId);

    Product first = productRepository.findByIdWithPessimisticLock(firstId)
        .orElseThrow();
    Product second = productRepository.findByIdWithPessimisticLock(secondId)
        .orElseThrow();

    // 재고 이동 로직
    if (fromId.equals(firstId)) {
        first.decreaseStock(quantity);
        second.increaseStock(quantity);
    } else {
        second.decreaseStock(quantity);
        first.increaseStock(quantity);
    }
}
```

### 사례 2: 낙관적 락 무한 재시도

#### 증상
```
인기 상품 재고 10개, 동시 주문 100건
→ 대부분 충돌 → 재시도 폭발 → 서버 부하
```

#### 해결 방법
```java
// 재시도 제한 + 비관적 락 전환
@Transactional
public void decreaseStockSmart(Long productId, int quantity) {
    int retryCount = 0;
    while (retryCount < MAX_RETRY) {
        try {
            // 낙관적 락 시도
            Product product = productRepository.findById(productId).orElseThrow();
            product.decreaseStock(quantity);
            productRepository.flush();  // 즉시 version 체크
            return;
        } catch (ObjectOptimisticLockingFailureException e) {
            retryCount++;
        }
    }

    // 재시도 실패 시 비관적 락으로 전환
    Product product = productRepository.findByIdWithPessimisticLock(productId)
        .orElseThrow();
    product.decreaseStock(quantity);
}
```

## 면접 예상 질문

### Q: 낙관적 락과 비관적 락의 차이점은?

A: **낙관적 락**은 충돌이 드물다고 가정하고 커밋 시점에 버전을 검증합니다. 충돌 시 예외가 발생하고 재시도해야 합니다. **비관적 락**은 충돌이 자주 발생한다고 가정하고 조회 시점에 DB 락을 획득합니다. 다른 트랜잭션은 락이 해제될 때까지 대기합니다.

### Q: @Version은 어떻게 동작하는가?

A: JPA의 `@Version` 어노테이션이 붙은 필드는 UPDATE 시 자동으로 WHERE 조건에 포함됩니다. `UPDATE ... SET version = version + 1 WHERE version = ?` 형태로 실행되어, affected rows가 0이면 다른 트랜잭션이 먼저 수정한 것으로 판단하고 `ObjectOptimisticLockingFailureException`을 발생시킵니다.

### Q: SELECT FOR UPDATE와 일반 SELECT의 차이는?

A: 일반 SELECT는 MVCC로 인해 락 없이 스냅샷을 읽습니다. `SELECT FOR UPDATE`는 해당 행에 배타적 락(X-lock)을 획득하여 다른 트랜잭션의 수정이나 FOR UPDATE 조회를 차단합니다. 단, 일반 SELECT는 차단하지 않습니다(MVCC).

### Q: 인기 상품 재고 관리에는 어떤 락이 적합한가?

A: 동시 주문이 많은 인기 상품은 **비관적 락** 또는 **분산 락(Redis)**이 적합합니다. 낙관적 락은 재시도가 폭발적으로 증가하여 오히려 성능이 저하됩니다. 선착순 이벤트 같은 극단적인 경우는 Redis 원자 연산(DECR)을 사용하는 것이 가장 효율적입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [트랜잭션](./transaction.md) | 선수 지식 | Intermediate |
| [인덱스](./index.md) | 선수 지식 | Intermediate |
| [분산 락](../system-design/distributed-lock.md) | 확장 개념 | Intermediate |
| [선착순 시스템 설계](../system-design/flash-sale-system.md) | 실전 적용 | Advanced |

## 참고 자료

- [JPA Locking - Hibernate Docs](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#locking)
- [MySQL InnoDB Locking](https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html)
- [Optimistic vs Pessimistic Locking - Martin Fowler](https://martinfowler.com/eaaCatalog/optimisticOfflineLock.html)
