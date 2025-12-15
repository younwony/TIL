# 캐싱 (Caching)

> 자주 사용되는 데이터를 빠른 저장소에 임시 저장하여 응답 시간을 단축하고 원본 데이터 소스의 부하를 줄이는 기술

## 핵심 개념

- **캐시 히트(Cache Hit)**: 요청한 데이터가 캐시에 존재하는 경우
- **캐시 미스(Cache Miss)**: 요청한 데이터가 캐시에 없어 원본 소스에서 가져와야 하는 경우
- **TTL(Time To Live)**: 캐시 데이터의 유효 기간
- **캐시 무효화(Cache Invalidation)**: 캐시된 데이터를 삭제하거나 갱신하는 것
- **캐시 적중률(Cache Hit Ratio)**: 전체 요청 중 캐시 히트 비율

## 쉽게 이해하기

**캐싱**을 자주 쓰는 물건을 책상 위에 두는 것에 비유할 수 있습니다.

### 캐시 = 책상 위 공간

**캐시 없이 (매번 창고에서 찾기)**
펜이 필요할 때마다 창고(DB)까지 가서 찾아와야 합니다. 시간이 오래 걸리죠.

**캐시 있을 때 (책상 위에 두기)**
자주 쓰는 펜은 책상 위(캐시)에 놔두면 바로 사용할 수 있습니다!

| 비유 | 캐시 개념 |
|------|----------|
| 책상 위 | 캐시 (빠른 접근) |
| 창고 | 데이터베이스 (느린 접근) |
| 책상 위에 펜이 있음 | 캐시 히트 (Cache Hit) |
| 창고까지 가야 함 | 캐시 미스 (Cache Miss) |
| 유통기한 라벨 | TTL (Time To Live) |

### 캐시 전략 비유

**Cache-Aside (Lazy Loading)**
- 필요할 때만 책상 위에 올림
- "펜 필요 → 책상에 없네 → 창고에서 가져옴 → 책상에 올려둠"

**Write-Through**
- 물건 살 때 창고랑 책상 둘 다 놓음
- "새 펜 샀다 → 창고에 보관 + 책상에도 올림"

**Write-Behind**
- 일단 책상에만 놓고, 나중에 창고로 정리
- "새 펜 → 일단 책상에 (빠름) → 퇴근 전에 창고 정리"

### 캐시 교체 정책 = 책상 정리

책상이 꽉 찼는데 새 물건을 올려야 한다면?

| 전략 | 비유 | 설명 |
|------|------|------|
| LRU | 가장 오래 안 쓴 것 버리기 | "이거 언제 썼더라..." → 버림 |
| LFU | 가장 적게 쓴 것 버리기 | "이건 총 2번밖에 안 썼네" → 버림 |
| FIFO | 가장 먼저 온 것 버리기 | "이건 제일 처음에 올려둔 거네" → 버림 |

### 캐시 스탬피드 = 점심시간 급식실

12시에 캐시가 만료되면, 모든 학생(요청)이 동시에 급식실(DB)로 몰립니다. 해결책: 한 명만 가서 밥 받아오고 나머지는 기다리기 (분산 락)

## 상세 설명

### 캐시 계층 (Cache Layers)

```
┌─────────────────────────────────────────────────────────────┐
│                      Client                                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Browser Cache                              │
│              (로컬 저장소, Service Worker)                    │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      CDN Cache                               │
│                (CloudFront, Cloudflare)                      │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                  Application Cache                           │
│                   (Redis, Memcached)                         │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Database Cache                             │
│               (Query Cache, Buffer Pool)                     │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Database                                │
└─────────────────────────────────────────────────────────────┘
```

### 캐시 전략 (Caching Strategies)

#### 1. Cache-Aside (Lazy Loading)

가장 일반적인 패턴. 애플리케이션이 캐시를 직접 관리한다.

```
읽기 흐름:
1. 애플리케이션이 캐시에서 데이터 조회
2. 캐시 히트 → 데이터 반환
3. 캐시 미스 → DB에서 조회 → 캐시에 저장 → 데이터 반환

┌─────────┐    1. GET    ┌─────────┐
│   App   │ ──────────▶  │  Cache  │
└─────────┘              └─────────┘
     │                        │
     │ 3. GET (miss)          │ 2. HIT/MISS
     ▼                        │
┌─────────┐                   │
│   DB    │ ◀─────────────────┘
└─────────┘    4. SET (miss)
```

**장점**: 필요한 데이터만 캐시, 캐시 장애 시 DB로 폴백 가능
**단점**: 첫 요청은 항상 느림 (Cold Start), 캐시-DB 간 불일치 가능

#### 2. Write-Through

데이터를 쓸 때 캐시와 DB에 동시에 기록한다.

```
쓰기 흐름:
1. 애플리케이션이 캐시에 데이터 쓰기
2. 캐시가 DB에 동기적으로 쓰기
3. 두 작업 모두 완료 후 응답

┌─────────┐    1. WRITE   ┌─────────┐    2. WRITE   ┌─────────┐
│   App   │ ───────────▶  │  Cache  │ ───────────▶  │   DB    │
└─────────┘               └─────────┘               └─────────┘
```

**장점**: 캐시와 DB 일관성 보장, 읽기 성능 좋음
**단점**: 쓰기 지연 발생, 사용되지 않는 데이터도 캐시됨

#### 3. Write-Behind (Write-Back)

데이터를 캐시에만 먼저 쓰고, 나중에 비동기로 DB에 기록한다.

```
쓰기 흐름:
1. 애플리케이션이 캐시에 데이터 쓰기 (즉시 응답)
2. 캐시가 일정 주기로 DB에 배치 쓰기

┌─────────┐    1. WRITE   ┌─────────┐   2. ASYNC    ┌─────────┐
│   App   │ ───────────▶  │  Cache  │ ─ ─ ─ ─ ─ ▶  │   DB    │
└─────────┘  (즉시 응답)   └─────────┘   (배치)      └─────────┘
```

**장점**: 쓰기 성능 우수, DB 부하 감소
**단점**: 캐시 장애 시 데이터 유실 가능, 복잡한 구현

#### 4. Write-Around

데이터를 DB에만 직접 쓰고, 캐시는 읽기 시에만 갱신한다.

```
쓰기 흐름:
1. 애플리케이션이 DB에 직접 쓰기
2. 캐시 갱신 없음 (읽기 시 Cache-Aside로 갱신)

┌─────────┐               ┌─────────┐
│   App   │               │  Cache  │
└─────────┘               └─────────┘
     │
     │ 1. WRITE (캐시 무시)
     ▼
┌─────────┐
│   DB    │
└─────────┘
```

**장점**: 쓰기 후 재읽기가 드문 데이터에 적합
**단점**: 최근 쓴 데이터 읽기 시 캐시 미스 발생

### 캐시 무효화 전략

| 전략 | 설명 | 사용 사례 |
|------|------|----------|
| TTL 기반 | 일정 시간 후 자동 만료 | 자주 변경되지 않는 데이터 |
| 이벤트 기반 | 데이터 변경 시 명시적 삭제 | 실시간 일관성 필요 시 |
| 버전 기반 | 데이터 버전으로 유효성 검증 | API 응답, 정적 자원 |

### 캐시 교체 정책 (Eviction Policy)

캐시 공간이 부족할 때 어떤 데이터를 제거할지 결정하는 정책

| 정책 | 설명 | 특징 |
|------|------|------|
| **LRU** (Least Recently Used) | 가장 오래 사용되지 않은 데이터 제거 | 가장 많이 사용됨 |
| **LFU** (Least Frequently Used) | 사용 빈도가 가장 낮은 데이터 제거 | 인기 데이터 보존에 유리 |
| **FIFO** (First In First Out) | 가장 먼저 들어온 데이터 제거 | 구현이 단순 |
| **Random** | 무작위 제거 | 오버헤드 최소 |

### Redis vs Memcached

| 특성 | Redis | Memcached |
|------|-------|-----------|
| 데이터 구조 | String, List, Set, Hash, Sorted Set 등 | String만 지원 |
| 영속성 | RDB, AOF 지원 | 미지원 (순수 캐시) |
| 클러스터링 | Redis Cluster | 클라이언트 측 샤딩 |
| 복제 | Master-Replica 지원 | 미지원 |
| 메모리 효율 | 상대적으로 낮음 | 높음 |
| 사용 사례 | 세션, 랭킹, 메시지 큐 | 단순 캐싱 |

## 예제 코드

### Spring Boot + Redis 캐시 적용

```java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))  // TTL 10분
            .serializeKeysWith(
                SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Cache-Aside 패턴: 캐시에서 먼저 조회, 없으면 DB 조회 후 캐시에 저장
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    // 데이터 변경 시 캐시 갱신
    @CachePut(value = "users", key = "#user.id")
    public User update(User user) {
        return userRepository.save(user);
    }

    // 데이터 삭제 시 캐시도 삭제
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // 조건부 캐싱
    @Cacheable(value = "users", key = "#id", condition = "#id > 0")
    public User findByIdWithCondition(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

### Cache-Aside 패턴 직접 구현

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RedisTemplate<String, Product> redisTemplate;
    private final ProductRepository productRepository;

    private static final String CACHE_PREFIX = "product:";
    private static final long CACHE_TTL_SECONDS = 3600; // 1시간

    public Product findById(Long id) {
        String cacheKey = CACHE_PREFIX + id;

        // 1. 캐시에서 조회
        Product cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;  // Cache Hit
        }

        // 2. Cache Miss - DB에서 조회
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        // 3. 캐시에 저장
        redisTemplate.opsForValue().set(
            cacheKey,
            product,
            CACHE_TTL_SECONDS,
            TimeUnit.SECONDS
        );

        return product;
    }

    public void evictCache(Long id) {
        redisTemplate.delete(CACHE_PREFIX + id);
    }
}
```

## 캐시 사용 시 주의사항

### 1. 캐시 스탬피드 (Cache Stampede)

캐시가 만료될 때 다수의 요청이 동시에 DB로 몰리는 현상

**해결 방법:**
- **Lock**: 한 요청만 DB 조회 허용
- **Early Expiration**: TTL 전에 미리 갱신
- **Probabilistic Early Expiration**: 확률적으로 미리 갱신

```java
// Lock을 활용한 캐시 스탬피드 방지
public Product findByIdWithLock(Long id) {
    String cacheKey = "product:" + id;
    String lockKey = "lock:" + cacheKey;

    Product cached = redisTemplate.opsForValue().get(cacheKey);
    if (cached != null) {
        return cached;
    }

    // 분산 락 획득 시도
    Boolean acquired = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);

    if (Boolean.TRUE.equals(acquired)) {
        try {
            // DB 조회 및 캐시 저장
            Product product = productRepository.findById(id).orElseThrow();
            redisTemplate.opsForValue().set(cacheKey, product, 1, TimeUnit.HOURS);
            return product;
        } finally {
            redisTemplate.delete(lockKey);
        }
    } else {
        // 다른 스레드가 처리 중 - 잠시 대기 후 재시도
        Thread.sleep(50);
        return findByIdWithLock(id);
    }
}
```

### 2. 캐시 일관성 문제

분산 환경에서 캐시와 DB 간 데이터 불일치 발생 가능

**해결 방법:**
- 짧은 TTL 설정
- 이벤트 기반 캐시 무효화
- 최종 일관성(Eventual Consistency) 허용

## 면접 예상 질문

- Q: Cache-Aside와 Write-Through 패턴의 차이점은 무엇인가요?
  - A: Cache-Aside는 애플리케이션이 캐시를 직접 관리하며 읽기 시에만 캐시를 갱신합니다. Write-Through는 쓰기 시 캐시와 DB를 동기적으로 함께 갱신합니다. Cache-Aside는 첫 요청이 느리지만 필요한 데이터만 캐시하고, Write-Through는 데이터 일관성이 좋지만 사용되지 않는 데이터도 캐시됩니다. **왜 이렇게 답해야 하나요?** Cache-Aside는 읽기가 많은 시스템에 적합합니다. 캐시 미스 시 DB 조회 후 캐시에 저장하므로 실제 사용되는 데이터만 캐싱되어 메모리를 효율적으로 사용합니다. Write-Through는 쓰기 시마다 캐시와 DB를 모두 업데이트하므로 일관성은 좋지만 쓰기 지연이 발생합니다.

- Q: 캐시 스탬피드(Cache Stampede)란 무엇이고 어떻게 해결하나요?
  - A: 캐시 스탬피드는 캐시가 만료되는 순간 다수의 요청이 동시에 DB로 몰려 과부하가 발생하는 현상입니다. 분산 락을 사용하여 한 요청만 DB를 조회하게 하거나, TTL 만료 전에 미리 갱신하는 방식으로 해결할 수 있습니다. **왜 이렇게 답해야 하나요?** 인기 상품 페이지 캐시가 정시에 만료되면 수만 건의 요청이 동시에 DB를 조회하여 DB가 다운될 수 있습니다. 분산 락으로 한 스레드만 DB 조회를 허용하거나, 확률적 조기 만료로 만료 시점을 분산시켜 DB를 보호해야 합니다.

- Q: Redis와 Memcached의 차이점은 무엇인가요?
  - A: Redis는 다양한 데이터 구조(List, Set, Hash 등)를 지원하고 영속성과 복제 기능을 제공합니다. Memcached는 단순 Key-Value만 지원하지만 메모리 효율이 높고 멀티스레드를 지원합니다. 단순 캐싱에는 Memcached가, 복잡한 데이터 처리나 영속성이 필요하면 Redis가 적합합니다. **왜 이렇게 답해야 하나요?** Redis의 Sorted Set은 실시간 랭킹, Pub/Sub은 실시간 알림에 활용되고, RDB/AOF 영속성으로 재시작 시 데이터를 복구할 수 있습니다. Memcached는 단순하지만 멀티코어를 잘 활용하고 메모리 오버헤드가 적어 대용량 캐싱에 유리합니다.

## 참고 자료

- [Redis Documentation](https://redis.io/documentation)
- [AWS ElastiCache Best Practices](https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/BestPractices.html)
- [Caching Strategies and How to Choose the Right One](https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/)
