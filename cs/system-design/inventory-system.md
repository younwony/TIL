# 재고 관리 시스템 설계 (Inventory Management System)

> `[4] 심화` · 선수 지식: [분산 락](./distributed-lock.md), [낙관적/비관적 락](../db/optimistic-pessimistic-lock.md), [Redis 캐싱](../db/redis-caching.md)

> 동시성 환경에서 재고의 정확성을 보장하면서 높은 성능을 유지하는 시스템 설계

`#재고관리` `#InventoryManagement` `#Stock` `#재고차감` `#동시성` `#Concurrency` `#분산락` `#DistributedLock` `#Redis` `#DECR` `#원자연산` `#AtomicOperation` `#초과판매` `#Overselling` `#예약재고` `#ReservedStock` `#가용재고` `#AvailableStock` `#커머스` `#Ecommerce` `#주문` `#Order` `#선착순` `#FlashSale` `#데이터정합성` `#Consistency` `#이벤트소싱` `#CQRS`

## 왜 알아야 하는가?

- **실무**: 재고 10개인데 11개 판매 = 초과판매 = 금전적 손실 + 고객 불만
- **면접**: "동시에 같은 상품을 100명이 주문하면?" 핵심 질문
- **기반 지식**: 분산 락, 동시성 제어, 이벤트 기반 아키텍처의 실전 적용

## 핵심 개념

- **가용 재고 (Available Stock)**: 실제 판매 가능한 재고
- **예약 재고 (Reserved Stock)**: 결제 대기 중인 재고 (장바구니, 미결제 주문)
- **실물 재고 (Physical Stock)**: 창고에 있는 실제 재고
- **초과판매 (Overselling)**: 재고보다 많이 판매하는 문제

## 쉽게 이해하기

**콘서트 좌석 예매**에 비유하면 이해가 쉽습니다.

```
재고 종류 = 좌석 상태
┌───────────────────────────────────────────────────┐
│                                                    │
│  실물 재고 (총 좌석)     = 100석                   │
│       │                                            │
│       ├── 예약 재고 (선택 중)  = 20석              │
│       │   (아직 결제 안 함)                        │
│       │                                            │
│       ├── 판매 완료           = 50석               │
│       │   (결제 완료)                              │
│       │                                            │
│       └── 가용 재고           = 30석               │
│           (구매 가능)                              │
│                                                    │
│  공식: 가용재고 = 실물재고 - 예약재고 - 판매완료   │
│        30 = 100 - 20 - 50                          │
│                                                    │
└───────────────────────────────────────────────────┘
```

## 상세 설명

### 재고 모델 설계

```java
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private int totalStock;        // 실물 재고

    @Column(nullable = false)
    private int reservedStock;     // 예약된 재고

    @Column(nullable = false)
    private int soldStock;         // 판매된 재고

    @Version
    private Long version;          // 낙관적 락

    // 가용 재고 계산
    public int getAvailableStock() {
        return totalStock - reservedStock - soldStock;
    }

    // 재고 예약 (장바구니 담기, 주문 생성)
    public void reserve(int quantity) {
        if (getAvailableStock() < quantity) {
            throw new InsufficientStockException(
                "가용 재고 부족: 요청=" + quantity + ", 가용=" + getAvailableStock()
            );
        }
        this.reservedStock += quantity;
    }

    // 예약 취소 (장바구니 삭제, 주문 취소)
    public void releaseReservation(int quantity) {
        this.reservedStock -= quantity;
    }

    // 판매 확정 (결제 완료)
    public void confirmSale(int quantity) {
        this.reservedStock -= quantity;
        this.soldStock += quantity;
    }

    // 재고 증가 (입고)
    public void addStock(int quantity) {
        this.totalStock += quantity;
    }
}
```

### 재고 차감 전략 비교

```
┌─────────────────────────────────────────────────────────────────┐
│                     재고 차감 전략 비교                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. DB 직접 차감 (낙관적 락)                                     │
│     ─────────────────────────────                               │
│     장점: 구현 단순, 추가 인프라 없음                            │
│     단점: 충돌 많으면 재시도 비용 증가                           │
│     적합: 일반 상품, 충돌 적은 경우                              │
│                                                                  │
│  2. DB 직접 차감 (비관적 락)                                     │
│     ─────────────────────────────                               │
│     장점: 충돌 없음, 정합성 보장                                 │
│     단점: 락 대기 시간, DB 부하                                  │
│     적합: 인기 상품, 충돌 빈번한 경우                            │
│                                                                  │
│  3. Redis 선차감 + DB 후기록                                     │
│     ─────────────────────────────                               │
│     장점: 고성능, DB 부하 분산                                   │
│     단점: 정합성 관리 복잡                                       │
│     적합: 선착순 이벤트, 초고 트래픽                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 전략 1: DB 직접 차감 (낙관적 락)

```java
@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final int MAX_RETRY = 3;

    private final InventoryRepository inventoryRepository;

    @Transactional
    @Retryable(
        value = ObjectOptimisticLockingFailureException.class,
        maxAttempts = MAX_RETRY,
        backoff = @Backoff(delay = 50, multiplier = 2)
    )
    public void reserveStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new RuntimeException("상품 없음"));

        inventory.reserve(quantity);
        // 커밋 시점에 version 체크
    }

    @Recover
    public void reserveStockFailed(
            ObjectOptimisticLockingFailureException e,
            Long productId,
            int quantity) {
        throw new StockReservationFailedException(
            "재고 예약 실패: 재시도 횟수 초과", e
        );
    }
}
```

### 전략 2: DB 직접 차감 (비관적 락)

```java
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId")
    Optional<Inventory> findByProductIdWithLock(@Param("productId") Long productId);
}

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void reserveStockPessimistic(Long productId, int quantity) {
        // SELECT ... FOR UPDATE
        Inventory inventory = inventoryRepository.findByProductIdWithLock(productId)
            .orElseThrow(() -> new RuntimeException("상품 없음"));

        inventory.reserve(quantity);
        // 트랜잭션 종료 시 락 해제
    }
}
```

### 전략 3: Redis 선차감 + DB 후기록 (고성능)

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisInventoryService {

    private static final String STOCK_KEY = "stock:";
    private static final String RESERVED_KEY = "reserved:";

    private final StringRedisTemplate redisTemplate;
    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Redis로 재고 선차감 (초고속)
     */
    public boolean tryReserveStock(Long productId, int quantity) {
        String stockKey = STOCK_KEY + productId;

        // Lua 스크립트로 원자적 차감
        String script =
            "local stock = tonumber(redis.call('GET', KEYS[1]) or 0) " +
            "if stock >= tonumber(ARGV[1]) then " +
            "  redis.call('DECRBY', KEYS[1], ARGV[1]) " +
            "  return 1 " +
            "else " +
            "  return 0 " +
            "end";

        Long result = redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            List.of(stockKey),
            String.valueOf(quantity)
        );

        if (result == 1) {
            // 비동기로 DB 반영 (이벤트 발행)
            kafkaTemplate.send("inventory-events",
                new StockReservedEvent(productId, quantity));
            return true;
        }
        return false;
    }

    /**
     * Redis → DB 동기화 (Kafka Consumer)
     */
    @KafkaListener(topics = "inventory-events")
    @Transactional
    public void syncToDatabase(StockReservedEvent event) {
        Inventory inventory = inventoryRepository
            .findByProductIdWithLock(event.getProductId())
            .orElseThrow();

        inventory.reserve(event.getQuantity());
        inventoryRepository.save(inventory);
    }

    /**
     * Redis 재고 초기화 (서버 시작 or 배치)
     */
    @PostConstruct
    public void initializeRedisStock() {
        List<Inventory> inventories = inventoryRepository.findAll();
        for (Inventory inv : inventories) {
            String stockKey = STOCK_KEY + inv.getProductId();
            redisTemplate.opsForValue()
                .set(stockKey, String.valueOf(inv.getAvailableStock()));
        }
    }
}
```

### 재고 상태 전이 (State Machine)

```
┌─────────────────────────────────────────────────────────────────┐
│                     재고 상태 전이                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                      ┌──────────────┐                           │
│                      │   가용 재고   │                           │
│                      │  (Available) │                           │
│                      └──────┬───────┘                           │
│                             │                                    │
│              ┌──────────────┼──────────────┐                    │
│              │              │              │                    │
│              ▼              │              │                    │
│       주문 생성             │              │                    │
│      (reserve)              │              │                    │
│              │              │              │                    │
│              ▼              │              │                    │
│       ┌──────────────┐      │              │                    │
│       │   예약 재고   │      │              │                    │
│       │  (Reserved)  │      │              │                    │
│       └──────┬───────┘      │              │                    │
│              │              │              │                    │
│     ┌────────┴────────┐     │              │                    │
│     │                 │     │              │                    │
│     ▼                 ▼     │              │                    │
│ 결제 완료         주문 취소  │              │                    │
│ (confirm)        (release) │              │                    │
│     │                 │     │              │                    │
│     ▼                 │     │              │                    │
│ ┌──────────────┐      │     │              │                    │
│ │   판매 재고   │      └────→│              │                    │
│ │    (Sold)    │            │              │                    │
│ └──────┬───────┘            │              │                    │
│        │                    │              │                    │
│        ▼                    │              │                    │
│    반품/환불  ──────────────→│              │                    │
│    (return)                                                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 예약 재고 타임아웃 처리

```java
@Entity
public class StockReservation {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;
    private Long orderId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime reservedAt;
    private LocalDateTime expiredAt;  // 예약 만료 시간
}

@Service
@RequiredArgsConstructor
public class ReservationTimeoutService {

    private static final int RESERVATION_TIMEOUT_MINUTES = 30;

    private final StockReservationRepository reservationRepository;
    private final InventoryService inventoryService;

    /**
     * 스케줄러: 만료된 예약 해제
     */
    @Scheduled(fixedDelay = 60000)  // 1분마다 실행
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();

        List<StockReservation> expiredReservations =
            reservationRepository.findByStatusAndExpiredAtBefore(
                ReservationStatus.RESERVED,
                now
            );

        for (StockReservation reservation : expiredReservations) {
            try {
                // 재고 복구
                inventoryService.releaseReservation(
                    reservation.getProductId(),
                    reservation.getQuantity()
                );

                // 예약 상태 변경
                reservation.expire();
                reservationRepository.save(reservation);

                log.info("예약 만료 처리: orderId={}, quantity={}",
                    reservation.getOrderId(), reservation.getQuantity());
            } catch (Exception e) {
                log.error("예약 만료 처리 실패: {}", e.getMessage());
            }
        }
    }
}
```

### 다중 창고 재고 관리

```java
@Entity
@Table(name = "warehouse_inventory")
public class WarehouseInventory {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;
    private Long warehouseId;

    private int totalStock;
    private int reservedStock;
    private int soldStock;

    @Version
    private Long version;
}

@Service
@RequiredArgsConstructor
public class MultiWarehouseInventoryService {

    private final WarehouseInventoryRepository warehouseInventoryRepository;

    /**
     * 가장 가까운 창고에서 재고 차감 (배송 최적화)
     */
    @Transactional
    public WarehouseInventory reserveFromNearestWarehouse(
            Long productId,
            int quantity,
            String deliveryZipCode) {

        // 배송지 기준 가까운 창고 순으로 조회
        List<WarehouseInventory> inventories =
            warehouseInventoryRepository.findByProductIdOrderByDistance(
                productId,
                deliveryZipCode
            );

        for (WarehouseInventory inventory : inventories) {
            if (inventory.getAvailableStock() >= quantity) {
                inventory.reserve(quantity);
                return inventory;
            }
        }

        throw new InsufficientStockException("전체 창고 재고 부족");
    }

    /**
     * 복수 창고에서 분할 출고
     */
    @Transactional
    public List<WarehouseAllocation> reserveFromMultipleWarehouses(
            Long productId,
            int quantity) {

        List<WarehouseAllocation> allocations = new ArrayList<>();
        int remainingQuantity = quantity;

        List<WarehouseInventory> inventories =
            warehouseInventoryRepository.findByProductIdWithStock(productId);

        for (WarehouseInventory inventory : inventories) {
            if (remainingQuantity <= 0) break;

            int available = inventory.getAvailableStock();
            int toReserve = Math.min(available, remainingQuantity);

            if (toReserve > 0) {
                inventory.reserve(toReserve);
                allocations.add(new WarehouseAllocation(
                    inventory.getWarehouseId(),
                    toReserve
                ));
                remainingQuantity -= toReserve;
            }
        }

        if (remainingQuantity > 0) {
            throw new InsufficientStockException(
                "전체 재고 부족: 부족량=" + remainingQuantity
            );
        }

        return allocations;
    }
}
```

## 동작 원리

### 고성능 재고 처리 흐름

```
┌─────────────────────────────────────────────────────────────────┐
│                  고성능 재고 처리 흐름                           │
│               (Redis 선차감 + DB 후기록)                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  주문 요청                                                       │
│     │                                                           │
│     ▼                                                           │
│  ┌──────────────────────┐                                       │
│  │ Redis DECR (원자적)   │ ← 수 마이크로초 소요                  │
│  └──────────┬───────────┘                                       │
│             │                                                   │
│    ┌────────┴────────┐                                         │
│    │                 │                                         │
│  성공               실패                                        │
│    │                 │                                         │
│    ▼                 ▼                                         │
│ 주문 진행         "재고 없음"                                   │
│    │              응답 반환                                     │
│    │                                                            │
│    ▼                                                            │
│ ┌─────────────────────────┐                                    │
│ │ Kafka 이벤트 발행        │ ← 비동기                           │
│ │ (StockReservedEvent)    │                                    │
│ └──────────┬──────────────┘                                    │
│            │                                                    │
│            ▼                                                    │
│ ┌─────────────────────────┐                                    │
│ │ DB 재고 동기화           │ ← Consumer가 처리                  │
│ │ (비관적 락)              │                                    │
│ └─────────────────────────┘                                    │
│                                                                  │
│  핵심: 고객 응답은 Redis에서 즉시,                               │
│        DB 정합성은 비동기로 보장                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 전략 | 성능 | 정합성 | 구현 복잡도 | 적합한 상황 |
|------|------|--------|-------------|-------------|
| 낙관적 락 | 중 | 높음 | 낮음 | 일반 상품, 충돌 적음 |
| 비관적 락 | 낮음 | 높음 | 낮음 | 인기 상품, 충돌 많음 |
| Redis + DB | 높음 | 중 | 높음 | 선착순, 초고 트래픽 |

## 트러블슈팅

### 사례 1: Redis-DB 재고 불일치

#### 증상
```
Redis: 재고 0개
DB: 재고 5개
원인: Kafka 메시지 유실 또는 Consumer 처리 실패
```

#### 해결 방법
```java
// 1. 정기 동기화 배치
@Scheduled(cron = "0 */10 * * * *")  // 10분마다
public void syncRedisWithDatabase() {
    List<Inventory> inventories = inventoryRepository.findAll();

    for (Inventory inv : inventories) {
        String redisStock = redisTemplate.opsForValue()
            .get(STOCK_KEY + inv.getProductId());

        int dbStock = inv.getAvailableStock();
        int redisStockInt = redisStock != null ? Integer.parseInt(redisStock) : 0;

        if (redisStockInt != dbStock) {
            log.warn("재고 불일치 감지: productId={}, redis={}, db={}",
                inv.getProductId(), redisStockInt, dbStock);

            // DB 값을 기준으로 Redis 업데이트
            redisTemplate.opsForValue()
                .set(STOCK_KEY + inv.getProductId(), String.valueOf(dbStock));
        }
    }
}

// 2. 실시간 불일치 알림
@Async
public void checkConsistency(Long productId) {
    String redisStock = redisTemplate.opsForValue()
        .get(STOCK_KEY + productId);
    int dbStock = inventoryRepository.findByProductId(productId)
        .map(Inventory::getAvailableStock)
        .orElse(0);

    int diff = Math.abs(Integer.parseInt(redisStock) - dbStock);
    if (diff > THRESHOLD) {
        alertService.send("재고 불일치 경고: productId=" + productId);
    }
}
```

### 사례 2: 초과판매 (Overselling)

#### 증상
```
재고 10개인데 12개 판매됨
DB에 sold_stock = 12, total_stock = 10
```

#### 원인 분석
- 낙관적 락 재시도 중 재고 재조회 없이 차감
- Redis 차감 후 DB 동기화 실패 + 재시도 시 Redis 재차감

#### 해결 방법
```java
// DB에 제약 조건 추가
@Entity
@Table(name = "inventory")
@Check(constraints = "sold_stock + reserved_stock <= total_stock")
public class Inventory {
    // ...
}

// 차감 시 조건 검증
@Modifying
@Query("UPDATE Inventory i " +
       "SET i.reservedStock = i.reservedStock + :quantity " +
       "WHERE i.productId = :productId " +
       "AND i.totalStock - i.reservedStock - i.soldStock >= :quantity")
int reserveStock(@Param("productId") Long productId, @Param("quantity") int quantity);
// 결과가 0이면 재고 부족
```

## 면접 예상 질문

### Q: 동시에 100명이 같은 상품(재고 10개)을 주문하면?

A: 재고보다 많은 주문이 들어오는 **초과판매(Overselling)** 문제가 발생할 수 있습니다. 해결 방법:
1. **낙관적 락**: @Version으로 커밋 시점 검증, 충돌 시 재시도
2. **비관적 락**: SELECT FOR UPDATE로 순차 처리
3. **Redis 원자 연산**: DECR로 원자적 차감, 음수면 실패 처리
선착순 이벤트처럼 동시성이 극심한 경우 Redis 원자 연산이 가장 효과적입니다.

### Q: Redis와 DB 재고가 불일치하면?

A: Redis를 캐시로, DB를 원본(Source of Truth)으로 삼습니다. 불일치 대응:
1. **정기 동기화 배치**: 10분마다 DB 기준으로 Redis 동기화
2. **Outbox 패턴**: 재고 변경을 DB와 같은 트랜잭션으로 Outbox 테이블에 기록
3. **모니터링**: 불일치 감지 시 알림 발송
4. **Fallback**: Redis 장애 시 DB로 직접 처리

### Q: 장바구니 재고 선점은 어떻게?

A: **예약 재고(Reserved Stock)** 개념을 사용합니다. 장바구니 담기 시 재고를 예약하고, 일정 시간(30분 등) 내 결제 미완료 시 자동 해제합니다. 스케줄러가 만료된 예약을 주기적으로 해제하고, 예약 만료 전 알림을 보내 전환율을 높입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [분산 락](./distributed-lock.md) | 선수 지식 | Intermediate |
| [낙관적/비관적 락](../db/optimistic-pessimistic-lock.md) | 선수 지식 | Intermediate |
| [Redis 캐싱](../db/redis-caching.md) | 선수 지식 | Intermediate |
| [선착순 시스템 설계](./flash-sale-system.md) | 관련 주제 | Advanced |
| [SAGA 패턴](./saga-pattern.md) | 관련 주제 | Advanced |

## 참고 자료

- [Designing Data-Intensive Applications - Martin Kleppmann](https://dataintensive.net/)
- [Redis DECR Command](https://redis.io/commands/decr/)
- [Inventory Management in E-commerce - AWS](https://aws.amazon.com/solutions/implementations/retail-demo-store/)
