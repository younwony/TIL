# 장바구니 시스템 설계 (Shopping Cart System)

> `[3] 중급` · 선수 지식: [Redis 캐싱](../db/redis-caching.md), [세션 관리](../security/authentication-authorization.md)

> 회원/비회원 장바구니 저장, 상품 추가/삭제, 재고 연동, 만료 처리를 위한 시스템 설계

`#장바구니` `#ShoppingCart` `#Cart` `#커머스` `#Ecommerce` `#Redis` `#세션` `#Session` `#비회원장바구니` `#GuestCart` `#장바구니병합` `#CartMerge` `#TTL` `#만료` `#Expiration` `#재고연동` `#가격변동` `#PriceChange` `#쿠키` `#Cookie` `#LocalStorage` `#영속성` `#Persistence`

## 왜 알아야 하는가?

- **실무**: 장바구니는 구매 전환율에 직접 영향. 데이터 유실 = 매출 손실
- **면접**: "비회원 장바구니는 어디에 저장하나?" 자주 나오는 질문
- **기반 지식**: 세션 관리, 캐시 전략, 데이터 영속성의 실전 적용

## 핵심 개념

- **회원 장바구니**: DB + Redis 캐시 (영구 보관)
- **비회원 장바구니**: Redis + 쿠키 식별자 (TTL 기반)
- **장바구니 병합**: 비회원 → 회원 로그인 시 병합
- **재고 연동**: 장바구니 조회 시 실시간 재고/가격 반영

## 쉽게 이해하기

**마트 장바구니**에 비유하면 이해가 쉽습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                     장바구니 비유                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  실물 마트                      온라인 쇼핑몰                 │
│  ────────                      ─────────────                │
│                                                              │
│  카트를 밀고 다님               세션/쿠키로 식별              │
│  (내 카트 = 나만 씀)           (내 장바구니 = 나만 보임)     │
│                                                              │
│  상품을 카트에 담음             상품 추가 API 호출           │
│  (물리적 이동)                 (DB/Redis에 저장)            │
│                                                              │
│  카트 버리고 나감               로그아웃/브라우저 닫음        │
│  (상품 제자리)                 (비회원: TTL 후 삭제)         │
│                                                              │
│  다음날 와서 카트 없음          회원: 다음날도 유지           │
│  (비영구)                      (영구 저장)                  │
│                                                              │
│  핵심 차이점:                                                │
│  - 온라인은 "어디에 저장하느냐"가 설계 포인트                │
│  - 회원/비회원 구분 필요                                     │
│  - 재고/가격 실시간 동기화 필요                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 저장 전략 비교

```
┌─────────────────────────────────────────────────────────────────┐
│                     장바구니 저장 전략 비교                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. 클라이언트 저장 (Cookie/LocalStorage)                        │
│     ─────────────────────────────────────                       │
│     장점: 서버 부하 없음, 구현 단순                              │
│     단점: 용량 제한, 기기간 공유 불가, 보안 취약                  │
│     적합: 소규모, 비회원 전용, 보조 저장소                       │
│                                                                  │
│  2. 세션 저장 (서버 메모리)                                      │
│     ────────────────────────                                    │
│     장점: 빠른 접근, 구현 단순                                   │
│     단점: 서버 재시작 시 유실, 스케일 아웃 어려움                 │
│     적합: 단일 서버, 프로토타입                                  │
│                                                                  │
│  3. Redis 저장                                                   │
│     ─────────────                                               │
│     장점: 빠름, TTL 지원, 서버 무관                              │
│     단점: 휘발성 (설정에 따라), 비용                             │
│     적합: 비회원 장바구니, 캐시 레이어                           │
│                                                                  │
│  4. DB 저장                                                      │
│     ──────────                                                  │
│     장점: 영구 보관, 분석 가능, 안정성                           │
│     단점: 상대적 느림, DB 부하                                   │
│     적합: 회원 장바구니, 영구 보관 필요                          │
│                                                                  │
│  5. Redis + DB (권장)                                            │
│     ────────────────                                            │
│     Redis: 빠른 읽기/쓰기 캐시                                   │
│     DB: 영구 저장, 백업                                          │
│     적합: 대규모 서비스, 회원 장바구니                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 장바구니 엔티티 설계

```java
/**
 * 회원 장바구니 (DB 저장)
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private LocalDateTime lastModifiedAt;

    @Version
    private Long version;

    // 상품 추가
    public void addItem(Long productId, int quantity, Long price) {
        // 이미 있는 상품이면 수량 증가
        Optional<CartItem> existing = findItem(productId);
        if (existing.isPresent()) {
            existing.get().addQuantity(quantity);
        } else {
            CartItem item = new CartItem(this, productId, quantity, price);
            this.items.add(item);
        }
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 상품 수량 변경
    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = findItem(productId)
            .orElseThrow(() -> new CartItemNotFoundException(productId));

        if (quantity <= 0) {
            this.items.remove(item);
        } else {
            item.setQuantity(quantity);
        }
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 상품 삭제
    public void removeItem(Long productId) {
        this.items.removeIf(item -> item.getProductId().equals(productId));
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 전체 삭제
    public void clear() {
        this.items.clear();
        this.lastModifiedAt = LocalDateTime.now();
    }

    // 총 금액 계산
    public Long calculateTotal() {
        return items.stream()
            .mapToLong(item -> item.getPrice() * item.getQuantity())
            .sum();
    }

    private Optional<CartItem> findItem(Long productId) {
        return items.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();
    }
}

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long price;  // 담을 당시 가격 (참고용)

    private LocalDateTime addedAt;
}
```

### Redis 장바구니 DTO

```java
/**
 * Redis 저장용 장바구니 (비회원 + 캐시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisCart {

    private String cartId;  // 비회원: UUID, 회원: customerId
    private List<RedisCartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static RedisCart createEmpty(String cartId) {
        return RedisCart.builder()
            .cartId(cartId)
            .items(new ArrayList<>())
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .build();
    }

    public void addItem(Long productId, int quantity, Long price, String productName) {
        Optional<RedisCartItem> existing = items.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();

        if (existing.isPresent()) {
            existing.get().addQuantity(quantity);
        } else {
            items.add(RedisCartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .productName(productName)
                .addedAt(LocalDateTime.now())
                .build());
        }
        this.lastModifiedAt = LocalDateTime.now();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisCartItem {

    private Long productId;
    private String productName;
    private int quantity;
    private Long price;
    private LocalDateTime addedAt;

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
```

### 장바구니 서비스 구현

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private static final String CART_KEY_PREFIX = "cart:";
    private static final Duration GUEST_CART_TTL = Duration.ofDays(7);
    private static final Duration MEMBER_CART_CACHE_TTL = Duration.ofHours(1);

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    /**
     * 장바구니 조회 (회원/비회원 통합)
     */
    public CartResponse getCart(CartContext context) {
        RedisCart redisCart;

        if (context.isLoggedIn()) {
            // 회원: 캐시 → DB → 캐시 저장
            redisCart = getMemberCart(context.getCustomerId());
        } else {
            // 비회원: Redis only
            redisCart = getGuestCart(context.getGuestCartId());
        }

        // 실시간 재고/가격 반영
        return enrichCartWithProductInfo(redisCart);
    }

    /**
     * 회원 장바구니 조회 (캐시 + DB)
     */
    private RedisCart getMemberCart(Long customerId) {
        String cacheKey = CART_KEY_PREFIX + "member:" + customerId;

        // 1. 캐시 확인
        RedisCart cached = getFromRedis(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 2. DB 조회
        Cart cart = cartRepository.findByCustomerId(customerId)
            .orElse(null);

        if (cart == null) {
            return RedisCart.createEmpty(customerId.toString());
        }

        // 3. Redis 캐시 저장
        RedisCart redisCart = toRedisCart(cart);
        saveToRedis(cacheKey, redisCart, MEMBER_CART_CACHE_TTL);

        return redisCart;
    }

    /**
     * 비회원 장바구니 조회 (Redis only)
     */
    private RedisCart getGuestCart(String guestCartId) {
        String cacheKey = CART_KEY_PREFIX + "guest:" + guestCartId;

        RedisCart cart = getFromRedis(cacheKey);
        if (cart == null) {
            cart = RedisCart.createEmpty(guestCartId);
            saveToRedis(cacheKey, cart, GUEST_CART_TTL);
        }

        return cart;
    }

    /**
     * 상품 추가
     */
    @Transactional
    public CartResponse addItem(CartContext context, AddItemRequest request) {
        // 1. 상품 정보 조회
        Product product = productService.getProduct(request.getProductId());

        // 2. 재고 확인
        int availableStock = inventoryService.getAvailableStock(request.getProductId());
        if (availableStock < request.getQuantity()) {
            throw new InsufficientStockException(
                "재고 부족: 요청=" + request.getQuantity() + ", 가용=" + availableStock
            );
        }

        // 3. 장바구니에 추가
        if (context.isLoggedIn()) {
            addItemToMemberCart(context.getCustomerId(), request, product);
        } else {
            addItemToGuestCart(context.getGuestCartId(), request, product);
        }

        return getCart(context);
    }

    private void addItemToMemberCart(Long customerId, AddItemRequest request, Product product) {
        // DB 업데이트
        Cart cart = cartRepository.findByCustomerId(customerId)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setCustomerId(customerId);
                return cartRepository.save(newCart);
            });

        cart.addItem(request.getProductId(), request.getQuantity(), product.getPrice());
        cartRepository.save(cart);

        // 캐시 무효화
        String cacheKey = CART_KEY_PREFIX + "member:" + customerId;
        redisTemplate.delete(cacheKey);
    }

    private void addItemToGuestCart(String guestCartId, AddItemRequest request, Product product) {
        String cacheKey = CART_KEY_PREFIX + "guest:" + guestCartId;

        RedisCart cart = getGuestCart(guestCartId);
        cart.addItem(
            request.getProductId(),
            request.getQuantity(),
            product.getPrice(),
            product.getName()
        );

        saveToRedis(cacheKey, cart, GUEST_CART_TTL);
    }

    /**
     * 실시간 재고/가격 반영
     */
    private CartResponse enrichCartWithProductInfo(RedisCart cart) {
        List<CartItemResponse> enrichedItems = new ArrayList<>();

        for (RedisCartItem item : cart.getItems()) {
            Product product = productService.getProduct(item.getProductId());
            int availableStock = inventoryService.getAvailableStock(item.getProductId());

            CartItemResponse response = CartItemResponse.builder()
                .productId(item.getProductId())
                .productName(product.getName())
                .productImage(product.getImageUrl())
                .quantity(item.getQuantity())
                .originalPrice(item.getPrice())
                .currentPrice(product.getPrice())
                .priceChanged(!item.getPrice().equals(product.getPrice()))
                .availableStock(availableStock)
                .outOfStock(availableStock == 0)
                .insufficientStock(availableStock < item.getQuantity())
                .build();

            enrichedItems.add(response);
        }

        return CartResponse.builder()
            .items(enrichedItems)
            .totalAmount(calculateTotal(enrichedItems))
            .itemCount(enrichedItems.size())
            .hasOutOfStock(enrichedItems.stream().anyMatch(CartItemResponse::isOutOfStock))
            .hasPriceChanged(enrichedItems.stream().anyMatch(CartItemResponse::isPriceChanged))
            .build();
    }

    /**
     * 장바구니 병합 (비회원 → 회원)
     */
    @Transactional
    public CartResponse mergeCart(Long customerId, String guestCartId) {
        RedisCart guestCart = getGuestCart(guestCartId);

        if (guestCart.getItems().isEmpty()) {
            return getCart(CartContext.forMember(customerId));
        }

        // 회원 장바구니 조회/생성
        Cart memberCart = cartRepository.findByCustomerId(customerId)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setCustomerId(customerId);
                return cartRepository.save(newCart);
            });

        // 비회원 장바구니 아이템을 회원 장바구니에 병합
        for (RedisCartItem guestItem : guestCart.getItems()) {
            memberCart.addItem(
                guestItem.getProductId(),
                guestItem.getQuantity(),
                guestItem.getPrice()
            );
        }

        cartRepository.save(memberCart);

        // 비회원 장바구니 삭제
        String guestCacheKey = CART_KEY_PREFIX + "guest:" + guestCartId;
        redisTemplate.delete(guestCacheKey);

        // 회원 장바구니 캐시 무효화
        String memberCacheKey = CART_KEY_PREFIX + "member:" + customerId;
        redisTemplate.delete(memberCacheKey);

        log.info("장바구니 병합 완료: customerId={}, 병합 아이템 수={}",
            customerId, guestCart.getItems().size());

        return getCart(CartContext.forMember(customerId));
    }

    // Redis 헬퍼 메서드
    private RedisCart getFromRedis(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;
        return objectMapper.convertValue(value, RedisCart.class);
    }

    private void saveToRedis(String key, RedisCart cart, Duration ttl) {
        redisTemplate.opsForValue().set(key, cart, ttl);
    }
}
```

### 비회원 장바구니 식별자 관리

```java
@Component
public class GuestCartIdManager {

    private static final String GUEST_CART_COOKIE = "guest_cart_id";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;  // 7일

    /**
     * 요청에서 비회원 장바구니 ID 추출 또는 생성
     */
    public String getOrCreateGuestCartId(HttpServletRequest request,
                                          HttpServletResponse response) {
        // 1. 쿠키에서 조회
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (GUEST_CART_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // 2. 없으면 새로 생성
        String guestCartId = UUID.randomUUID().toString();

        Cookie cookie = new Cookie(GUEST_CART_COOKIE, guestCartId);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // HTTPS only
        response.addCookie(cookie);

        return guestCartId;
    }

    /**
     * 비회원 장바구니 ID 쿠키 삭제 (로그인 후 병합 완료 시)
     */
    public void clearGuestCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(GUEST_CART_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
```

### 장바구니 만료 처리

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CartCleanupService {

    private static final int INACTIVE_DAYS_THRESHOLD = 30;

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 장기 미사용 회원 장바구니 정리 (일 배치)
     */
    @Scheduled(cron = "0 0 4 * * *")  // 매일 새벽 4시
    @Transactional
    public void cleanupInactiveCarts() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(INACTIVE_DAYS_THRESHOLD);

        // 30일 이상 미사용 장바구니 조회
        List<Cart> inactiveCarts = cartRepository
            .findByLastModifiedAtBefore(threshold);

        int count = 0;
        for (Cart cart : inactiveCarts) {
            // 이메일 알림 후 삭제 (선택적)
            // notificationService.sendCartExpirationWarning(cart.getCustomerId());

            cart.clear();
            count++;
        }

        log.info("장바구니 정리 완료: {}개 정리됨", count);
    }

    /**
     * 비회원 장바구니는 Redis TTL로 자동 삭제됨
     * 추가로 수동 정리가 필요한 경우
     */
    public void cleanupExpiredGuestCarts() {
        // Redis TTL이 자동 처리하므로 별도 로직 불필요
        // 필요시 SCAN 명령으로 패턴 매칭 삭제 가능
        Set<String> keys = redisTemplate.keys("cart:guest:*");
        // keys는 TTL이 설정되어 있으므로 자동 만료됨
    }
}
```

## 동작 원리

### 장바구니 데이터 흐름

```
┌─────────────────────────────────────────────────────────────────┐
│                  장바구니 데이터 흐름                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [비회원 흐름]                                                   │
│                                                                  │
│  브라우저        서버         Redis                              │
│     │            │            │                                 │
│     │ 상품 담기   │            │                                 │
│     │───────────→│            │                                 │
│     │            │            │                                 │
│     │            │ 쿠키에서    │                                 │
│     │            │ guestId 추출│                                 │
│     │            │            │                                 │
│     │            │ 저장       │                                 │
│     │            │───────────→│ cart:guest:{uuid}               │
│     │            │            │ TTL: 7일                        │
│     │            │            │                                 │
│     │ 응답       │            │                                 │
│     │←───────────│            │                                 │
│                                                                  │
│  [회원 흐름]                                                     │
│                                                                  │
│  브라우저        서버         Redis         DB                   │
│     │            │            │            │                    │
│     │ 상품 담기   │            │            │                    │
│     │───────────→│            │            │                    │
│     │            │            │            │                    │
│     │            │         DB 저장          │                    │
│     │            │────────────────────────→│                    │
│     │            │            │            │                    │
│     │            │ 캐시 무효화 │            │                    │
│     │            │───────────→│ DELETE     │                    │
│     │            │            │            │                    │
│     │ 응답       │            │            │                    │
│     │←───────────│            │            │                    │
│                                                                  │
│  [로그인 시 병합]                                                │
│                                                                  │
│     │ 로그인     │            │            │                    │
│     │───────────→│            │            │                    │
│     │            │            │            │                    │
│     │            │ 비회원 조회 │            │                    │
│     │            │───────────→│            │                    │
│     │            │            │            │                    │
│     │            │         병합 후 저장    │                    │
│     │            │────────────────────────→│                    │
│     │            │            │            │                    │
│     │            │ 비회원 삭제 │            │                    │
│     │            │───────────→│ DELETE     │                    │
│     │            │            │            │                    │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 저장 방식 | 성능 | 영속성 | 구현 복잡도 | 비용 |
|----------|------|--------|-------------|------|
| LocalStorage | 최고 | 브라우저 한정 | 낮음 | 무료 |
| Redis only | 높음 | 휘발 가능 | 중간 | 중간 |
| DB only | 중간 | 영구 | 낮음 | 낮음 |
| Redis + DB | 높음 | 영구 | 높음 | 높음 |

## 트러블슈팅

### 사례 1: 로그인 후 장바구니 비어 있음

#### 증상
```
비회원 상태에서 상품 5개 담음
→ 로그인 후 장바구니가 비어 있음
```

#### 원인 분석
- 비회원 → 회원 장바구니 병합 누락
- 쿠키 guestCartId 조회 실패

#### 해결 방법
```java
@Service
public class AuthService {

    private final CartService cartService;
    private final GuestCartIdManager guestCartIdManager;

    @Transactional
    public LoginResponse login(LoginRequest request,
                               HttpServletRequest httpRequest,
                               HttpServletResponse httpResponse) {
        // 1. 로그인 처리
        User user = authenticate(request);

        // 2. 비회원 장바구니 ID 확인
        String guestCartId = guestCartIdManager
            .getOrCreateGuestCartId(httpRequest, httpResponse);

        // 3. 장바구니 병합
        cartService.mergeCart(user.getId(), guestCartId);

        // 4. 비회원 쿠키 삭제
        guestCartIdManager.clearGuestCartCookie(httpResponse);

        return LoginResponse.success(user);
    }
}
```

### 사례 2: 장바구니 상품 가격 변동

#### 증상
```
어제 10,000원에 담은 상품
→ 오늘 보니 12,000원으로 올랐음
→ 어떤 가격으로 결제해야 하나?
```

#### 해결 방법
```java
// 조회 시 실시간 가격 표시 + 변동 알림
public CartResponse getCart(...) {
    // ...
    CartItemResponse response = CartItemResponse.builder()
        .originalPrice(item.getPrice())      // 담을 당시 가격
        .currentPrice(product.getPrice())    // 현재 가격
        .priceChanged(!item.getPrice().equals(product.getPrice()))
        .priceIncreased(product.getPrice() > item.getPrice())
        .build();

    // 프론트엔드에서 "가격이 변경되었습니다" 알림 표시
}

// 주문 시 현재 가격 기준 결제
public OrderResponse createOrder(CreateOrderRequest request) {
    // 장바구니 상품의 "현재" 가격으로 주문 금액 계산
    Long totalAmount = 0L;
    for (CartItem item : cart.getItems()) {
        Product product = productService.getProduct(item.getProductId());
        totalAmount += product.getPrice() * item.getQuantity();  // 현재 가격
    }
    // ...
}
```

## 면접 예상 질문

### Q: 비회원 장바구니는 어디에 저장하나?

A: **Redis + 쿠키 조합**을 사용합니다.
- **식별자**: UUID를 생성하여 HttpOnly 쿠키에 저장
- **데이터**: Redis에 `cart:guest:{uuid}` 키로 저장
- **만료**: TTL 7일 설정, 이후 자동 삭제
- DB 저장은 불필요 (비회원은 영구 보관 기대 없음)

### Q: 로그인 시 비회원 장바구니는 어떻게 하나?

A: **병합(Merge)** 처리합니다.
1. 로그인 성공 시 쿠키에서 guestCartId 추출
2. Redis에서 비회원 장바구니 조회
3. 회원 장바구니에 아이템 추가 (중복은 수량 합산)
4. 비회원 장바구니 삭제, 쿠키 삭제
5. 회원 장바구니 캐시 갱신

### Q: 장바구니 상품의 재고가 소진되면?

A: **실시간 조회** 시점에 반영합니다.
- 장바구니 조회 시 상품별 재고 확인
- 품절 상품은 `outOfStock: true` 플래그로 표시
- 주문 버튼 클릭 시 재검증 후 품절 상품 제외 처리
- **재고 선점은 하지 않음** (장바구니에 담는다고 재고 차감 안 함)

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Redis 캐싱](../db/redis-caching.md) | 선수 지식 | Intermediate |
| [세션 관리](../security/authentication-authorization.md) | 선수 지식 | Intermediate |
| [주문 처리 시스템](./order-processing-system.md) | 연계 시스템 | Advanced |
| [재고 관리 시스템](./inventory-system.md) | 연계 시스템 | Advanced |
| [쿠폰 시스템](./coupon-promotion-system.md) | 연계 시스템 | Intermediate |

## 참고 자료

- [Redis Data Types - redis.io](https://redis.io/docs/data-types/)
- [E-commerce Shopping Cart Design - AWS](https://aws.amazon.com/solutions/retail/)
- [Cookie vs Session vs LocalStorage](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies)
