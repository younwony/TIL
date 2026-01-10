# 분산 락 (Distributed Lock)

> `[3] 중급` · 선수 지식: [Redis 캐싱](../db/redis-caching.md), [트랜잭션](../db/transaction.md)

> 분산 환경에서 여러 서버/프로세스가 공유 자원에 동시 접근할 때 데이터 정합성을 보장하는 동기화 메커니즘

`#분산락` `#DistributedLock` `#동시성제어` `#Concurrency` `#Redis` `#Redisson` `#ZooKeeper` `#MySQL` `#락` `#Lock` `#상호배제` `#MutualExclusion` `#데드락` `#Deadlock` `#TTL` `#TimeToLive` `#Fencing` `#FencingToken` `#Redlock` `#SetNX` `#SETNX` `#분산시스템` `#재고관리` `#Inventory` `#선착순` `#FlashSale` `#RaceCondition` `#임계영역` `#CriticalSection`

## 왜 알아야 하는가?

- **실무**: 재고 차감, 쿠폰 발급, 포인트 처리 등 커머스 핵심 기능에서 동시성 문제 해결 필수
- **면접**: "동시에 같은 상품을 주문하면?" 질문에 분산 락 없이 답하기 어려움
- **기반 지식**: 분산 시스템의 동기화, CAP 정리, 장애 대응의 기초

## 핵심 개념

- **상호 배제(Mutual Exclusion)**: 한 시점에 하나의 클라이언트만 자원 접근
- **락 획득/해제**: 작업 전 락 획득, 작업 후 락 해제
- **TTL(Time To Live)**: 락 자동 만료로 데드락 방지
- **Fencing Token**: 락 유효성 검증으로 안전성 강화

## 쉽게 이해하기

**화장실 칸**에 비유하면 이해가 쉽습니다.

```
싱글 서버: 화장실 1개, 사람 1명씩 사용
┌─────────────────────────────────────┐
│  👤 → [🚻 잠금] → 사용 → [🚻 해제]  │
│  👤 대기...                          │
└─────────────────────────────────────┘

분산 서버: 화장실 1개, 여러 건물에서 동시에 달려옴
┌─────────────────────────────────────┐
│  건물A 👤 ─┐                         │
│  건물B 👤 ─┼→ [🚻 ???]              │
│  건물C 👤 ─┘                         │
│                                      │
│  → "중앙 잠금 시스템" 필요! (분산 락) │
└─────────────────────────────────────┘
```

**분산 락 = 여러 건물(서버)이 공유하는 중앙 잠금 시스템**

## 상세 설명

### 왜 일반 락으로는 안 되는가?

```java
// 단일 서버에서의 동기화 (synchronized)
public synchronized void decreaseStock(Long productId) {
    Product product = productRepository.findById(productId);
    product.decreaseStock(1);
    productRepository.save(product);
}
```

**문제**: `synchronized`는 JVM 내에서만 동작. 서버가 여러 대면 무용지물.

```
서버 A (JVM 1)          서버 B (JVM 2)
synchronized 락 획득    synchronized 락 획득
     ↓                       ↓
  재고 조회 (10개)        재고 조회 (10개)  ← 동시 조회!
     ↓                       ↓
  재고 차감 (9개)         재고 차감 (9개)   ← 둘 다 9개로 저장
     ↓                       ↓
  저장 완료               저장 완료

결과: 2번 주문했는데 재고 1개만 감소! (Race Condition)
```

### Redis 기반 분산 락

가장 널리 사용되는 방식입니다.

#### 기본 원리: SETNX

```bash
# SETNX = SET if Not eXists
SETNX lock:product:123 "server-a"  # 락 획득 시도
# 성공: 1 반환 (락 획득)
# 실패: 0 반환 (다른 서버가 이미 보유)

DEL lock:product:123  # 락 해제
```

#### Spring + Redisson 구현

```java
@Service
@RequiredArgsConstructor
public class StockService {

    private static final String LOCK_PREFIX = "lock:stock:";
    private static final long WAIT_TIME = 5L;    // 락 대기 시간
    private static final long LEASE_TIME = 3L;   // 락 유지 시간 (TTL)

    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;

    public void decreaseStock(Long productId, int quantity) {
        String lockKey = LOCK_PREFIX + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 (최대 5초 대기, 획득 후 3초 유지)
            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);

            if (!acquired) {
                throw new RuntimeException("락 획득 실패: 다른 요청 처리 중");
            }

            // 임계 영역 (Critical Section)
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));
            product.decreaseStock(quantity);
            productRepository.save(product);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 대기 중 인터럽트", e);
        } finally {
            // 락 해제 (자신이 획득한 락만 해제)
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
```

**왜 이렇게 하는가?**
- `tryLock(waitTime, leaseTime)`: 무한 대기 방지 + TTL로 데드락 방지
- `isHeldByCurrentThread()`: 다른 스레드의 락을 실수로 해제하는 것 방지
- TTL(leaseTime): 서버 장애 시에도 락이 자동 해제되어 시스템 복구 가능

### Redisson vs Lettuce 비교

| 항목 | Redisson | Lettuce (직접 구현) |
|------|----------|-------------------|
| 구현 복잡도 | 낮음 (추상화 제공) | 높음 (직접 SETNX 구현) |
| 락 갱신 | 자동 (Watchdog) | 수동 구현 필요 |
| Pub/Sub 기반 대기 | 지원 | 직접 구현 필요 |
| 성능 | 약간 낮음 | 높음 |
| 권장 상황 | 대부분의 경우 | 극한의 성능 필요 시 |

### MySQL 기반 분산 락

Redis가 없는 환경에서 대안으로 사용합니다.

```sql
-- Named Lock 방식
SELECT GET_LOCK('stock:123', 10);  -- 10초 대기
-- 작업 수행
SELECT RELEASE_LOCK('stock:123');
```

```java
@Repository
public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "SELECT GET_LOCK(:key, :timeout)", nativeQuery = true)
    Integer getLock(@Param("key") String key, @Param("timeout") int timeout);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    Integer releaseLock(@Param("key") String key);
}
```

**단점**: DB 커넥션 점유, 성능 저하 → 트래픽 높으면 Redis 권장

### ZooKeeper 기반 분산 락

강력한 일관성이 필요한 금융 시스템에서 사용합니다.

```java
@Service
@RequiredArgsConstructor
public class ZookeeperLockService {

    private final CuratorFramework client;

    public void executeWithLock(String lockPath, Runnable task) {
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);

        try {
            if (lock.acquire(5, TimeUnit.SECONDS)) {
                try {
                    task.run();
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ZK 락 실패", e);
        }
    }
}
```

**왜 ZooKeeper를 쓰는가?**
- **순차적 노드**: 공정한 락 순서 보장
- **임시 노드**: 클라이언트 연결 끊어지면 자동 삭제 (장애 복구)
- **강한 일관성**: 모든 노드가 같은 상태를 봄

## 동작 원리

### 분산 락 동작 흐름

```
┌─────────────────────────────────────────────────────────────┐
│                    분산 락 동작 흐름                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  서버 A              Redis              서버 B               │
│    │                  │                   │                  │
│    │ SETNX lock:123   │                   │                  │
│    │ ───────────────→ │                   │                  │
│    │     OK (획득)    │                   │                  │
│    │ ←─────────────── │                   │                  │
│    │                  │    SETNX lock:123 │                  │
│    │                  │ ←──────────────── │                  │
│    │                  │      FAIL         │                  │
│    │                  │ ─────────────────→│                  │
│    │  [작업 수행]     │                   │  [대기/재시도]    │
│    │                  │                   │                  │
│    │ DEL lock:123     │                   │                  │
│    │ ───────────────→ │                   │                  │
│    │                  │                   │                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Redisson Watchdog 메커니즘

```
┌───────────────────────────────────────────────────────┐
│              Redisson Watchdog 동작                    │
├───────────────────────────────────────────────────────┤
│                                                        │
│  락 획득 (TTL 30초)                                    │
│       │                                                │
│       ▼                                                │
│  [Watchdog 시작]                                       │
│       │                                                │
│       │  10초마다 체크                                  │
│       ▼                                                │
│  ┌─────────────────┐                                   │
│  │ 락 아직 필요? │                                     │
│  └────────┬────────┘                                   │
│           │                                            │
│     Yes   │   No                                       │
│     ▼     │   ▼                                        │
│  TTL 30초 │  자동 해제                                  │
│  갱신     │                                            │
│                                                        │
│  → 작업이 예상보다 오래 걸려도 락 유지!                 │
│  → leaseTime 미지정 시에만 동작                        │
│                                                        │
└───────────────────────────────────────────────────────┘
```

## 트레이드오프

| 방식 | 장점 | 단점 |
|------|------|------|
| **Redis (Redisson)** | 빠름, 구현 쉬움, Watchdog | Redis 장애 시 락 유실 가능 |
| **MySQL Named Lock** | 추가 인프라 불필요 | 느림, DB 부하, 커넥션 점유 |
| **ZooKeeper** | 강한 일관성, 공정성 | 복잡, 상대적으로 느림 |
| **Redlock** | Redis 장애 허용 | 복잡, 논란 있음 (Martin Kleppmann) |

## 트러블슈팅

### 사례 1: 락 획득 후 작업 시간이 TTL 초과

#### 증상
```
서버 A: 락 획득 (TTL 3초)
서버 A: 작업 중... (5초 소요)
서버 B: 락 획득 성공 (A의 TTL 만료)
서버 A: 작업 완료, DB 저장
서버 B: 작업 완료, DB 저장  ← 데이터 정합성 깨짐!
```

#### 원인 분석
- TTL이 실제 작업 시간보다 짧게 설정됨
- 네트워크 지연, GC 등으로 예상보다 작업이 오래 걸림

#### 해결 방법

**방법 1: Redisson Watchdog 사용**
```java
// leaseTime을 지정하지 않으면 Watchdog 자동 활성화
RLock lock = redissonClient.getLock(lockKey);
lock.lock();  // TTL 자동 갱신
try {
    // 작업 수행
} finally {
    lock.unlock();
}
```

**방법 2: Fencing Token 사용**
```java
public void decreaseStockWithFencing(Long productId, int quantity) {
    String lockKey = "lock:stock:" + productId;
    long fencingToken = redissonClient.getAtomicLong(lockKey + ":token")
                                      .incrementAndGet();

    RLock lock = redissonClient.getLock(lockKey);
    lock.lock();
    try {
        // DB에 fencingToken과 함께 저장
        stockRepository.decreaseWithToken(productId, quantity, fencingToken);
    } finally {
        lock.unlock();
    }
}

// Repository
@Modifying
@Query("UPDATE Stock s SET s.quantity = s.quantity - :qty " +
       "WHERE s.productId = :pid AND s.fencingToken < :token")
int decreaseWithToken(@Param("pid") Long productId,
                      @Param("qty") int quantity,
                      @Param("token") long token);
```

#### 예방 조치
- 작업 시간 모니터링 및 TTL 적절히 설정
- Watchdog 사용 또는 Fencing Token 적용
- 락 획득/해제 메트릭 수집

### 사례 2: 데드락 발생

#### 증상
```
서버 A: lock:product:1 획득, lock:product:2 대기
서버 B: lock:product:2 획득, lock:product:1 대기
→ 둘 다 영원히 대기 (Deadlock)
```

#### 해결 방법
```java
// 락 획득 순서 통일 (ID 오름차순)
public void transferStock(Long fromId, Long toId, int quantity) {
    Long firstId = Math.min(fromId, toId);
    Long secondId = Math.max(fromId, toId);

    RLock lock1 = redissonClient.getLock("lock:stock:" + firstId);
    RLock lock2 = redissonClient.getLock("lock:stock:" + secondId);

    // 순서대로 락 획득
    lock1.lock();
    try {
        lock2.lock();
        try {
            // 재고 이동 로직
        } finally {
            lock2.unlock();
        }
    } finally {
        lock1.unlock();
    }
}
```

## 면접 예상 질문

### Q: synchronized와 분산 락의 차이점은?

A: `synchronized`는 단일 JVM 내에서만 동작하는 모니터 기반 락입니다. 멀티 서버 환경에서는 각 서버가 독립적인 JVM을 사용하므로 synchronized로는 서버 간 동기화가 불가능합니다. 분산 락은 Redis, ZooKeeper 등 외부 저장소를 통해 여러 서버가 공유하는 락을 제공하여 분산 환경에서의 동시성 제어를 가능하게 합니다.

### Q: Redis 분산 락에서 TTL은 왜 필요한가?

A: TTL이 없으면 락을 획득한 서버가 장애로 락을 해제하지 못했을 때 영원히 락이 풀리지 않는 데드락이 발생합니다. TTL을 설정하면 일정 시간 후 락이 자동 만료되어 다른 요청이 처리될 수 있습니다. 다만 TTL이 작업 시간보다 짧으면 정합성 문제가 발생할 수 있어 Watchdog이나 Fencing Token으로 보완합니다.

### Q: Redisson의 Watchdog은 무엇인가?

A: Watchdog은 락 획득 시 TTL(기본 30초)을 자동으로 갱신해주는 백그라운드 스레드입니다. 작업이 예상보다 오래 걸려도 락이 만료되지 않도록 보호합니다. 단, `tryLock(waitTime, leaseTime)`에서 leaseTime을 명시하면 Watchdog이 비활성화되므로 주의해야 합니다.

### Q: Redis가 다운되면 분산 락은 어떻게 되나?

A: 단일 Redis 인스턴스 사용 시 Redis 다운 = 락 서비스 불가입니다. 이를 해결하기 위해:
1. **Redis Sentinel/Cluster**: 자동 페일오버로 가용성 확보
2. **Redlock 알고리즘**: 5대의 독립 Redis에서 과반수(3대) 락 획득 시 성공
3. **ZooKeeper**: 더 강한 일관성이 필요한 경우 대안

다만 Redlock은 Martin Kleppmann과 Redis 개발자 Salvatore Sanfilippo 간의 논쟁이 있어 신중히 검토해야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Redis 캐싱](../db/redis-caching.md) | 선수 지식 | Intermediate |
| [트랜잭션](../db/transaction.md) | 선수 지식 | Intermediate |
| [선착순 시스템 설계](./flash-sale-system.md) | 실전 적용 | Advanced |
| [Rate Limiting](./rate-limiting.md) | 관련 개념 | Intermediate |
| [낙관적/비관적 락](../db/optimistic-pessimistic-lock.md) | DB 레벨 락 | Intermediate |

## 참고 자료

- [Redisson 공식 문서](https://github.com/redisson/redisson/wiki/8.-Distributed-locks-and-synchronizers)
- [How to do distributed locking - Martin Kleppmann](https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html)
- [Is Redlock safe? - Salvatore Sanfilippo](http://antirez.com/news/101)
