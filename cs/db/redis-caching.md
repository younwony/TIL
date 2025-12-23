# Redis 캐싱 전략

> `[3] 중급` · 선수 지식: [NoSQL](./nosql.md)

> 인메모리 데이터 저장소 Redis를 활용하여 데이터베이스 부하를 줄이고 응답 속도를 개선하는 캐싱 기법

`#Redis` `#레디스` `#캐싱` `#Caching` `#Cache` `#인메모리` `#InMemory` `#CacheAside` `#LookAside` `#WriteThrough` `#WriteBehind` `#WriteBack` `#ReadThrough` `#TTL` `#TimeToLive` `#캐시무효화` `#CacheInvalidation` `#분산캐시` `#DistributedCache` `#캐시스탬피드` `#CacheStampede` `#핫키` `#HotKey` `#Pub/Sub` `#세션저장소` `#SessionStore` `#Lettuce` `#Jedis`

## 왜 알아야 하는가?

- **실무**: 대부분의 서비스에서 Redis 캐싱을 사용하며, 적절한 전략 선택이 성능과 비용에 직접적 영향을 미침
- **면접**: "캐시 무효화는 어떻게 하나요?", "캐시 일관성은 어떻게 유지하나요?" 등 자주 출제
- **기반 지식**: 분산 시스템, 마이크로서비스 아키텍처 이해의 핵심

## 핵심 개념

- **Cache-Aside (Lazy Loading)**: 애플리케이션이 캐시와 DB를 직접 관리
- **Write-Through**: 쓰기 시 캐시와 DB를 동시에 업데이트
- **Write-Behind (Write-Back)**: 캐시에 먼저 쓰고 나중에 DB에 반영
- **TTL (Time To Live)**: 캐시 만료 시간 설정
- **캐시 무효화 (Invalidation)**: 데이터 변경 시 캐시 삭제/갱신

## 쉽게 이해하기

캐싱을 **도서관 사서**에 비유해보겠습니다.

**Cache-Aside (Lazy Loading)** = 요청할 때 찾아오기
- 손님: "자바의 정석 책 있나요?"
- 사서: 먼저 책상 위(캐시) 확인 → 없으면 서고(DB)에서 가져옴 → 책상 위에 놔둠
- 다음 손님이 같은 책 요청 시 바로 전달

**Write-Through** = 바로바로 정리하기
- 새 책이 들어오면 책상 위(캐시)에도 놓고, 서고(DB)에도 바로 정리
- 항상 책상과 서고가 동일한 상태

**Write-Behind** = 나중에 몰아서 정리하기
- 새 책이 들어오면 책상 위(캐시)에만 놓음
- 한가할 때 서고(DB)에 정리 (비동기)
- 빠르지만, 정전 시 책상 위 책 유실 위험

**TTL** = 유통기한
- "이 책은 2시간만 책상에 둔다" (인기 없으면 자동 반납)
- 너무 짧으면: 자주 서고 왔다갔다 (캐시 미스)
- 너무 길면: 오래된 정보 제공 (stale data)

## 상세 설명

### 1. Cache-Aside (Lazy Loading)

**가장 일반적인 패턴**입니다. 애플리케이션이 캐시와 DB를 직접 관리합니다.

```
읽기 흐름:
1. 캐시에서 데이터 조회
2. 캐시 히트 → 데이터 반환
3. 캐시 미스 → DB 조회 → 캐시에 저장 → 데이터 반환
```

```java
public User getUser(Long userId) {
    String key = "user:" + userId;

    // 1. 캐시에서 조회
    User cachedUser = redisTemplate.opsForValue().get(key);
    if (cachedUser != null) {
        return cachedUser;  // 캐시 히트
    }

    // 2. 캐시 미스 → DB 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    // 3. 캐시에 저장 (TTL 1시간)
    redisTemplate.opsForValue().set(key, user, Duration.ofHours(1));

    return user;
}
```

**왜 이렇게 하는가?**

- 필요한 데이터만 캐싱 (메모리 효율)
- 캐시 장애 시 DB에서 직접 조회 가능 (장애 대응)
- 구현이 단순함

**장점**:
- 실제로 사용되는 데이터만 캐싱
- 캐시 서버 장애 시에도 서비스 가능

**단점**:
- 첫 요청은 항상 느림 (Cold Start)
- 캐시와 DB 간 일관성 문제 발생 가능

### 2. Write-Through

**쓰기 시 캐시와 DB를 동시에 업데이트**합니다.

```
쓰기 흐름:
1. 캐시에 데이터 쓰기
2. DB에 데이터 쓰기
3. 둘 다 성공해야 완료
```

```java
public User updateUser(Long userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    user.update(request);

    // 1. DB 저장
    User savedUser = userRepository.save(user);

    // 2. 캐시 갱신 (동기)
    String key = "user:" + userId;
    redisTemplate.opsForValue().set(key, savedUser, Duration.ofHours(1));

    return savedUser;
}
```

**왜 이렇게 하는가?**

캐시와 DB의 일관성을 항상 유지하기 위해서입니다.

**장점**:
- 캐시 일관성 보장
- 읽기 시 항상 캐시 히트 (데이터가 있다면)

**단점**:
- 쓰기 지연 (캐시 + DB 두 번 쓰기)
- 사용되지 않는 데이터도 캐싱될 수 있음

### 3. Write-Behind (Write-Back)

**캐시에 먼저 쓰고, 나중에 비동기로 DB에 반영**합니다.

```
쓰기 흐름:
1. 캐시에 데이터 쓰기 → 즉시 응답
2. 백그라운드에서 DB에 쓰기 (배치/큐)
```

```java
public void updateUserAsync(Long userId, UserUpdateRequest request) {
    String key = "user:" + userId;

    // 1. 캐시에 즉시 반영
    User updatedUser = new User(userId, request);
    redisTemplate.opsForValue().set(key, updatedUser);

    // 2. 비동기로 DB 저장 (메시지 큐 활용)
    kafkaTemplate.send("user-updates", userId, request);
}

// Consumer에서 DB 저장
@KafkaListener(topics = "user-updates")
public void handleUserUpdate(Long userId, UserUpdateRequest request) {
    User user = userRepository.findById(userId).orElseThrow();
    user.update(request);
    userRepository.save(user);
}
```

**왜 이렇게 하는가?**

쓰기 성능을 극대화하고, DB 부하를 분산하기 위해서입니다.

**장점**:
- 쓰기 성능 극대화
- DB 부하 분산 (배치 처리)

**단점**:
- 데이터 유실 위험 (캐시 장애 시)
- 복잡한 구현 (큐, 재시도 로직)
- 일시적 불일치 발생

### 4. 캐시 무효화 전략

데이터가 변경될 때 캐시를 어떻게 처리할지 결정합니다.

#### 삭제 (Invalidation)

```java
public void updateUser(Long userId, UserUpdateRequest request) {
    // DB 업데이트
    userRepository.update(userId, request);

    // 캐시 삭제 (다음 조회 시 갱신)
    redisTemplate.delete("user:" + userId);
}
```

**왜 삭제인가?**
- 단순함: 갱신보다 구현이 쉬움
- 안전함: 불필요한 캐시 적재 방지

#### 갱신 (Update)

```java
public void updateUser(Long userId, UserUpdateRequest request) {
    User user = userRepository.update(userId, request);

    // 캐시 갱신
    redisTemplate.opsForValue().set("user:" + userId, user);
}
```

**왜 갱신인가?**
- 캐시 미스 방지: 삭제 후 조회 시 DB 히트 없음
- 성능: 자주 조회되는 데이터에 적합

### 5. 캐시 키 설계

좋은 캐시 키는 **고유하고, 예측 가능하며, 관리하기 쉬워야** 합니다.

```java
// 나쁜 예
"user_123"           // 충돌 가능
"data"               // 범위 불명확

// 좋은 예
"user:123"           // 명확한 네임스페이스
"user:123:profile"   // 계층 구조
"post:456:comments"  // 관계 표현
"session:abc123"     // 타입 명시

// 패턴 기반 삭제가 가능한 구조
"user:123:*"         // user:123 관련 모든 캐시 삭제 가능
```

### 6. TTL 설정 전략

| 데이터 유형 | TTL 예시 | 이유 |
|------------|----------|------|
| 세션 | 30분 ~ 2시간 | 보안, 메모리 절약 |
| 사용자 프로필 | 1시간 ~ 1일 | 자주 변경되지 않음 |
| 상품 목록 | 5분 ~ 1시간 | 가격/재고 변동 |
| 검색 결과 | 1분 ~ 5분 | 실시간성 필요 |
| 설정 값 | 1일 ~ 7일 | 거의 변경되지 않음 |

```java
// TTL 설정 예시
redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(30));

// 동적 TTL (랜덤 요소 추가 - 캐시 스탬피드 방지)
int baseTtl = 3600; // 1시간
int jitter = new Random().nextInt(600); // 0~10분 랜덤
redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(baseTtl + jitter));
```

## 동작 원리

### Cache-Aside 패턴 흐름

```
┌─────────────┐
│  클라이언트  │
└──────┬──────┘
       │ 1. 데이터 요청
       ▼
┌─────────────┐
│ 애플리케이션 │
└──────┬──────┘
       │ 2. 캐시 조회
       ▼
┌─────────────┐
│   Redis     │ ─── 캐시 히트 → 3a. 데이터 반환 ─→ 완료
└──────┬──────┘
       │ 캐시 미스
       ▼
┌─────────────┐
│   Database  │ ← 3b. DB 조회
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Redis     │ ← 4. 캐시 저장
└─────────────┘
       │
       ▼
    5. 데이터 반환
```

### 캐시 갱신 시점 비교

```
Write-Through:
요청 → [캐시 쓰기] → [DB 쓰기] → 응답
        동기          동기

Write-Behind:
요청 → [캐시 쓰기] → 응답
        동기          ↓
                 [백그라운드]
                      ↓
                 [DB 쓰기]
                    비동기
```

## 예제 코드

### Spring Boot + Redis 캐싱 구현

```java
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()));

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

    // 캐시 조회 (없으면 메서드 실행 후 캐싱)
    @Cacheable(value = "users", key = "#userId")
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // 캐시 갱신
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // 캐시 삭제
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 전체 캐시 삭제
    @CacheEvict(value = "users", allEntries = true)
    public void clearAllUserCache() {
        // 캐시만 삭제
    }
}
```

### 캐시 스탬피드 방지

```java
@Service
@RequiredArgsConstructor
public class UserServiceWithLock {

    private static final String LOCK_PREFIX = "lock:";
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(10);

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        String key = "user:" + userId;
        String lockKey = LOCK_PREFIX + key;

        // 1. 캐시 조회
        User cachedUser = (User) redisTemplate.opsForValue().get(key);
        if (cachedUser != null) {
            return cachedUser;
        }

        // 2. 분산 락 획득 시도
        Boolean acquired = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, "locked", LOCK_TIMEOUT);

        if (Boolean.TRUE.equals(acquired)) {
            try {
                // 3. Double-check (다른 스레드가 이미 캐싱했을 수 있음)
                cachedUser = (User) redisTemplate.opsForValue().get(key);
                if (cachedUser != null) {
                    return cachedUser;
                }

                // 4. DB 조회 및 캐싱
                User user = userRepository.findById(userId).orElseThrow();
                redisTemplate.opsForValue().set(key, user, Duration.ofHours(1));
                return user;

            } finally {
                // 5. 락 해제
                redisTemplate.delete(lockKey);
            }
        } else {
            // 6. 락 획득 실패 → 잠시 대기 후 재시도
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return getUser(userId);  // 재귀 호출
        }
    }
}
```

## 트레이드오프

### 캐싱 전략 비교

| 전략 | 읽기 성능 | 쓰기 성능 | 일관성 | 복잡도 | 사용처 |
|------|----------|----------|--------|--------|--------|
| Cache-Aside | 보통 (미스 시 느림) | 빠름 | 낮음 | 낮음 | 일반적인 읽기 중심 |
| Write-Through | 빠름 | 느림 | 높음 | 중간 | 일관성 중요 |
| Write-Behind | 빠름 | 매우 빠름 | 낮음 | 높음 | 쓰기 집중 |

### 캐시 무효화 vs 갱신

| 방식 | 장점 | 단점 | 적합한 경우 |
|------|------|------|------------|
| 삭제 | 단순, 안전 | 다음 조회 시 캐시 미스 | 쓰기 빈도 > 읽기 빈도 |
| 갱신 | 항상 캐시 히트 | 복잡, 사용 안 되는 데이터 캐싱 | 읽기 빈도 > 쓰기 빈도 |

## 트러블슈팅

### 사례 1: Redis 메모리 부족으로 인한 BGSAVE 실패

#### 증상

Redis 서버에서 RDB 스냅샷 저장(BGSAVE) 또는 AOF rewrite 시 다음과 같은 에러 발생:

```
Can't save in background: fork: Cannot allocate memory
```

#### 원인 분석

**Redis 메모리 상태 확인**:

```bash
redis-cli INFO memory
```

```
# Memory
used_memory:360101992
used_memory_human:343.42M
used_memory_rss:453132288
used_memory_rss_human:432.14M
used_memory_peak:648691288
used_memory_peak_human:618.64M
used_memory_peak_perc:55.51%
total_system_memory:2050015232
total_system_memory_human:1.91G
maxmemory:0
maxmemory_human:0B
maxmemory_policy:noeviction
mem_fragmentation_ratio:1.26
```

**서버 메모리 상태**:

```bash
free -h
```

```
              total        used        free      shared  buff/cache   available
Mem:           1.9G        554M        453M        596K        946M        1.2G
Swap:            0B          0B          0B
```

**왜 발생하는가?**

1. **fork() 동작 원리**: Redis는 BGSAVE/AOF rewrite 시 `fork()`로 자식 프로세스 생성
2. **Copy-on-Write (COW)**: fork 직후에는 부모/자식이 메모리를 공유하지만, 쓰기 발생 시 페이지 복사
3. **Linux 기본 정책**: `overcommit_memory=0`은 보수적으로 메모리 할당 (최악의 경우 대비)
4. **결과**: 실제로는 COW로 적은 메모리만 필요하지만, OS가 "최대 필요량"을 미리 확보하려 함

```
현재 Redis 메모리: 343MB
fork 시 OS 요구량: 343MB x 2 = 686MB (최악의 경우)
가용 메모리: 453MB
→ 메모리 부족으로 fork 실패
```

#### 해결 방법

**방법 1: overcommit_memory 설정 변경 (즉시 적용)**

```bash
# 현재 설정 확인
cat /proc/sys/vm/overcommit_memory

# 임시 변경 (재부팅 시 초기화)
sudo sysctl vm.overcommit_memory=1

# 영구 변경
echo "vm.overcommit_memory=1" | sudo tee -a /etc/sysctl.conf
sudo sysctl -p
```

| 값 | 동작 | 설명 |
|---|------|------|
| 0 | 휴리스틱 | 기본값. 보수적으로 메모리 할당 판단 |
| 1 | 항상 허용 | 메모리 할당 항상 허용 (Redis 권장) |
| 2 | 제한적 허용 | swap + RAM의 일정 비율까지만 허용 |

**왜 overcommit_memory=1인가?**

- Redis의 fork는 COW를 사용하므로 실제 추가 메모리 사용량이 적음
- 값이 1이면 OS가 "일단 허용"하고, 실제로 메모리가 부족하면 OOM Killer 동작
- Redis 공식 문서에서도 권장하는 설정

**방법 2: maxmemory 설정 (근본적 해결)**

```bash
# redis.conf 수정
maxmemory 512mb
maxmemory-policy allkeys-lru
```

```bash
# 런타임에서 변경
redis-cli CONFIG SET maxmemory 512mb
redis-cli CONFIG SET maxmemory-policy allkeys-lru
```

| 정책 | 동작 |
|------|------|
| noeviction | 메모리 부족 시 에러 반환 (기본값) |
| allkeys-lru | 모든 키 중 LRU 기반 삭제 |
| volatile-lru | TTL 설정된 키 중 LRU 기반 삭제 |
| allkeys-random | 모든 키 중 랜덤 삭제 |
| volatile-ttl | TTL 짧은 키부터 삭제 |

**방법 3: Swap 추가 (임시 방편)**

```bash
# 2GB swap 파일 생성
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# 영구 설정
echo "/swapfile swap swap defaults 0 0" | sudo tee -a /etc/fstab
```

#### 예방 조치

**1. 메모리 모니터링 설정**

```bash
# Redis 메모리 사용률 모니터링
redis-cli INFO memory | grep used_memory_human
redis-cli INFO memory | grep mem_fragmentation_ratio
```

**2. 단편화 비율 관리**

```
mem_fragmentation_ratio 해석:
- 1.0 ~ 1.5: 정상
- 1.5 이상: 단편화 발생, 메모리 낭비
- 1.0 미만: Swap 사용 중 (성능 저하)
```

```bash
# 단편화 해소 (Redis 4.0+)
redis-cli MEMORY DOCTOR
redis-cli CONFIG SET activedefrag yes
```

**3. 정기적인 메모리 점검 스크립트**

```bash
#!/bin/bash
# redis-memory-check.sh

USED=$(redis-cli INFO memory | grep used_memory_human | cut -d: -f2)
PEAK=$(redis-cli INFO memory | grep used_memory_peak_human | cut -d: -f2)
FRAG=$(redis-cli INFO memory | grep mem_fragmentation_ratio | cut -d: -f2)

echo "현재 사용: $USED"
echo "피크 사용: $PEAK"
echo "단편화율: $FRAG"

# 단편화율 1.5 초과 시 알림
if (( $(echo "$FRAG > 1.5" | bc -l) )); then
    echo "경고: 메모리 단편화율이 높습니다!"
fi
```

### 사례 2: 캐시 키 폭발 (Key Explosion)

#### 증상

- Redis 메모리 사용량이 지속적으로 증가
- TTL 없는 키가 대량으로 쌓임

#### 원인

```java
// 문제 코드: 사용자별 검색어마다 캐시 생성
String key = "search:" + userId + ":" + searchQuery;
redisTemplate.opsForValue().set(key, results);  // TTL 없음!
```

#### 해결

```java
// 해결 1: TTL 필수 설정
redisTemplate.opsForValue().set(key, results, Duration.ofMinutes(30));

// 해결 2: 키 개수 제한 (Sorted Set 활용)
String userSearchKey = "user:" + userId + ":searches";
redisTemplate.opsForZSet().add(userSearchKey, searchQuery, System.currentTimeMillis());
redisTemplate.opsForZSet().removeRange(userSearchKey, 0, -101);  // 최근 100개만 유지
```

### 사례 3: 직렬화 문제로 인한 메모리 낭비

#### 증상

- 예상보다 Redis 메모리 사용량이 높음
- `used_memory_dataset` 비율이 낮음

#### 원인

```java
// Java 기본 직렬화 사용 시 메타데이터가 많이 포함됨
redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
```

#### 해결

```java
// JSON 직렬화로 변경 (더 효율적)
redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

// 또는 더 효율적인 직렬화 사용
redisTemplate.setValueSerializer(new StringRedisSerializer());
// 직접 JSON 변환
String json = objectMapper.writeValueAsString(user);
redisTemplate.opsForValue().set(key, json);
```

## 면접 예상 질문

### Q: 캐시 스탬피드(Cache Stampede)가 무엇이고 어떻게 해결하나요?

A: **캐시 스탬피드**는 캐시가 만료되는 순간 다수의 요청이 동시에 DB를 조회하는 현상입니다.

**왜 발생하는가?**
인기 데이터의 TTL이 만료되면, 동시에 100개의 요청이 캐시 미스 → 100개 모두 DB 조회 → DB 과부하.

**해결 방법**:
1. **분산 락**: 한 스레드만 DB 조회, 나머지는 대기
2. **TTL 랜덤화**: 모든 캐시가 동시에 만료되지 않도록 jitter 추가
3. **캐시 워밍**: 만료 전에 미리 갱신
4. **Stale-while-revalidate**: 오래된 데이터 반환하며 백그라운드 갱신

### Q: 캐시와 DB 간 데이터 일관성은 어떻게 유지하나요?

A: **완벽한 일관성은 불가능**하며, 상황에 맞는 전략을 선택해야 합니다.

**방법 1: Write-Through**
- 쓰기 시 캐시와 DB 동시 업데이트
- 일관성 높음, 쓰기 성능 저하

**방법 2: 캐시 삭제 + 짧은 TTL**
- 변경 시 캐시 삭제, 다음 조회 시 DB에서 가져옴
- 일시적 불일치 허용 (TTL 동안)

**방법 3: 이벤트 기반 갱신**
- DB 변경 시 이벤트 발행 → 캐시 갱신
- 비동기로 처리되어 지연 발생 가능

**실무**: 대부분 "일시적 불일치 허용 + 짧은 TTL" 조합 사용.

### Q: Redis 캐시 서버가 다운되면 어떻게 되나요?

A: **Cache-Aside 패턴**이면 DB에서 직접 조회하므로 서비스는 유지됩니다. 단, 모든 요청이 DB로 몰려 부하가 급증합니다.

**대응 방안**:
1. **서킷 브레이커**: 캐시 장애 감지 시 DB 직접 조회로 전환
2. **Redis Sentinel/Cluster**: 자동 장애 복구
3. **로컬 캐시 병행**: Caffeine 등 로컬 캐시로 1차 방어
4. **Rate Limiting**: DB 부하 제한

```java
@CircuitBreaker(name = "redis", fallbackMethod = "getFromDbDirectly")
public User getUser(Long userId) {
    return redisTemplate.opsForValue().get("user:" + userId);
}

public User getFromDbDirectly(Long userId, Exception e) {
    return userRepository.findById(userId).orElseThrow();
}
```

### Q: 어떤 데이터를 캐싱해야 하나요?

A: **읽기 빈도가 높고, 변경 빈도가 낮으며, 계산 비용이 큰 데이터**를 캐싱합니다.

| 캐싱 적합 | 캐싱 부적합 |
|----------|------------|
| 사용자 프로필 | 실시간 재고 (일관성 필수) |
| 상품 상세 정보 | 결제 정보 (보안) |
| 검색 결과 | 자주 변경되는 데이터 |
| 설정 값 | 1회성 데이터 |

**경험 법칙**: 같은 데이터를 10번 이상 조회한다면 캐싱 고려.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [NoSQL](./nosql.md) | 선수 지식 (Redis 기초) | [4] 심화 |
| [Transaction](./transaction.md) | 캐시 일관성 이해 | [3] 중급 |

## 참고 자료

- [Redis 공식 문서](https://redis.io/docs/)
- [AWS ElastiCache Best Practices](https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/best-practices.html)
- [Caching Strategies - AWS](https://aws.amazon.com/caching/best-practices/)
- [Cache Stampede 해결 - Martin Kleppmann](https://dataintensive.net/)
