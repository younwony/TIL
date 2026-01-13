# 쿠폰/프로모션 시스템 설계 (Coupon & Promotion System)

> `[3] 중급` · 선수 지식: [트랜잭션](../db/transaction.md), [Redis 캐싱](../db/redis-caching.md)

> 쿠폰 발급/사용, 할인 계산, 중복 사용 방지, 프로모션 규칙 엔진을 위한 시스템 설계

`#쿠폰` `#Coupon` `#프로모션` `#Promotion` `#할인` `#Discount` `#커머스` `#Ecommerce` `#쿠폰코드` `#CouponCode` `#정률할인` `#정액할인` `#PercentDiscount` `#FixedDiscount` `#중복사용방지` `#쿠폰검증` `#Validation` `#쿠폰발급` `#Issuance` `#선착순` `#FirstCome` `#유효기간` `#Expiration` `#사용조건` `#Condition` `#규칙엔진` `#RuleEngine`

## 왜 알아야 하는가?

- **실무**: 쿠폰은 마케팅 핵심 도구. 잘못된 적용 = 금전적 손실
- **면접**: "쿠폰 중복 사용은 어떻게 방지하나?" 자주 나오는 질문
- **기반 지식**: 규칙 엔진, 동시성 제어, 상태 관리의 실전 적용

## 핵심 개념

- **쿠폰 정책**: 할인 유형, 조건, 유효기간 등을 정의
- **쿠폰 인스턴스**: 개별 사용자에게 발급된 쿠폰
- **할인 유형**: 정액(Fixed), 정률(Percent), 무료배송, 사은품
- **사용 조건**: 최소 주문금액, 특정 카테고리, 첫 구매 등

## 쉽게 이해하기

**종이 쿠폰**에 비유하면 이해가 쉽습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                     쿠폰 시스템 비유                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  [쿠폰 정책 = 쿠폰 설계도]                                    │
│  "신규 가입 10% 할인 쿠폰, 1만원 이상 구매 시"                │
│       │                                                     │
│       ▼                                                     │
│  [쿠폰 발급 = 쿠폰 인쇄해서 나눠줌]                           │
│  "회원가입하신 분께 쿠폰을 드립니다"                          │
│       │                                                     │
│       ▼                                                     │
│  [쿠폰 보유 = 지갑에 쿠폰 보관]                               │
│  "내 쿠폰함에 있는 쿠폰들"                                    │
│       │                                                     │
│       ▼                                                     │
│  [쿠폰 적용 = 계산대에서 쿠폰 제시]                           │
│  "이 쿠폰 사용할게요" → 조건 검증                             │
│       │                                                     │
│       ▼                                                     │
│  [쿠폰 사용 완료 = 쿠폰에 도장 찍음]                          │
│  "사용된 쿠폰은 재사용 불가"                                  │
│                                                              │
│  핵심: 쿠폰 정책(설계도)과 쿠폰 인스턴스(실물)를 분리          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 쿠폰 타입과 할인 유형

```
┌─────────────────────────────────────────────────────────────────┐
│                     쿠폰 할인 유형                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. 정액 할인 (Fixed Amount)                                     │
│     ────────────────────────                                    │
│     예: 5,000원 할인                                             │
│     계산: 주문금액 - 5,000원                                     │
│     주의: 주문금액보다 클 수 없음 (0원 처리)                     │
│                                                                  │
│  2. 정률 할인 (Percentage)                                       │
│     ──────────────────────                                      │
│     예: 10% 할인                                                 │
│     계산: 주문금액 × 0.1                                         │
│     주의: 최대 할인 금액 제한 필요 (cap)                         │
│                                                                  │
│  3. 무료 배송 (Free Shipping)                                    │
│     ──────────────────────                                      │
│     예: 배송비 무료                                              │
│     계산: 배송비 = 0                                             │
│                                                                  │
│  4. 사은품 (Free Gift)                                           │
│     ──────────────                                              │
│     예: 샘플 증정                                                │
│     처리: 주문에 사은품 자동 추가                                │
│                                                                  │
│  5. Buy X Get Y (BOGO)                                          │
│     ─────────────────                                           │
│     예: 2+1, 1+1                                                 │
│     계산: 특정 상품 N개 구매 시 M개 무료                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 쿠폰 엔티티 설계

```java
/**
 * 쿠폰 정책 (Coupon Policy)
 * - 쿠폰의 "설계도" 역할
 * - 어떤 조건으로, 어떤 할인을, 얼마나 발급할지 정의
 */
@Entity
@Table(name = "coupon_policies")
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;  // WELCOME2025, SUMMER_SALE

    @Column(nullable = false)
    private String name;  // "신규 가입 환영 쿠폰"

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private Long discountValue;  // 정액: 금액, 정률: 퍼센트(10 = 10%)

    private Long maxDiscountAmount;  // 최대 할인 금액 (정률 시)

    private Long minOrderAmount;  // 최소 주문 금액

    // 발급 제한
    private Integer totalQuantity;    // 총 발급 수량 (null = 무제한)
    private Integer perUserLimit;     // 1인당 발급 제한 (기본 1)

    // 유효 기간
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    // 사용 후 유효 기간 (발급일로부터 N일)
    private Integer validDaysAfterIssue;

    // 적용 범위
    @ElementCollection
    private List<Long> applicableCategoryIds;  // 특정 카테고리만

    @ElementCollection
    private List<Long> applicableProductIds;   // 특정 상품만

    @ElementCollection
    private List<Long> excludedProductIds;     // 제외 상품

    // 사용 조건
    private Boolean firstOrderOnly;  // 첫 구매만
    private Boolean stackable;       // 다른 쿠폰과 중복 사용 가능

    @Enumerated(EnumType.STRING)
    private CouponStatus status;  // ACTIVE, PAUSED, EXPIRED

    private LocalDateTime createdAt;

    @Version
    private Long version;

    // 발급된 수량 (동시성 고려)
    private Integer issuedCount = 0;

    public boolean canIssue() {
        if (status != CouponStatus.ACTIVE) return false;
        if (totalQuantity != null && issuedCount >= totalQuantity) return false;
        if (validUntil != null && LocalDateTime.now().isAfter(validUntil)) return false;
        return true;
    }

    public void incrementIssuedCount() {
        this.issuedCount++;
    }
}

/**
 * 사용자별 쿠폰 (Coupon Instance)
 * - 실제 발급된 쿠폰
 */
@Entity
@Table(name = "user_coupons",
       uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "coupon_policy_id", "issue_seq"}))
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy policy;

    @Column(nullable = false)
    private String couponCode;  // 개별 쿠폰 코드 (UUID)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserCouponStatus status;  // ISSUED, USED, EXPIRED, CANCELLED

    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;  // 개별 만료일
    private LocalDateTime usedAt;
    private String usedOrderId;       // 사용된 주문 ID

    private Integer issueSeq;  // 같은 정책 내 발급 순번

    @Version
    private Long version;

    public boolean isUsable() {
        if (status != UserCouponStatus.ISSUED) return false;
        if (expiredAt != null && LocalDateTime.now().isAfter(expiredAt)) return false;
        return true;
    }

    public void use(String orderId) {
        if (!isUsable()) {
            throw new CouponNotUsableException("사용 불가능한 쿠폰");
        }
        this.status = UserCouponStatus.USED;
        this.usedAt = LocalDateTime.now();
        this.usedOrderId = orderId;
    }

    public void restore() {
        if (this.status != UserCouponStatus.USED) {
            throw new IllegalStateException("사용된 쿠폰만 복구 가능");
        }
        this.status = UserCouponStatus.ISSUED;
        this.usedAt = null;
        this.usedOrderId = null;
    }
}

public enum DiscountType {
    FIXED_AMOUNT,    // 정액 할인
    PERCENTAGE,      // 정률 할인
    FREE_SHIPPING,   // 무료 배송
    FREE_GIFT,       // 사은품
    BUY_X_GET_Y      // N+M
}

public enum UserCouponStatus {
    ISSUED,    // 발급됨 (사용 가능)
    USED,      // 사용됨
    EXPIRED,   // 만료됨
    CANCELLED  // 취소됨
}
```

### 쿠폰 발급 서비스

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CouponIssueService {

    private static final String ISSUE_LOCK_KEY = "coupon:issue:lock:";
    private static final String ISSUE_COUNT_KEY = "coupon:issue:count:";

    private final CouponPolicyRepository policyRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;

    /**
     * 쿠폰 발급 (기본)
     */
    @Transactional
    public UserCoupon issueCoupon(Long customerId, String policyCode) {
        CouponPolicy policy = policyRepository.findByCode(policyCode)
            .orElseThrow(() -> new CouponNotFoundException(policyCode));

        // 1. 발급 가능 여부 확인
        validateIssue(policy, customerId);

        // 2. 발급 수량 증가 (비관적 락)
        CouponPolicy lockedPolicy = policyRepository.findByIdWithLock(policy.getId())
            .orElseThrow();

        if (!lockedPolicy.canIssue()) {
            throw new CouponIssueException("발급 불가: 수량 소진 또는 기간 만료");
        }

        lockedPolicy.incrementIssuedCount();

        // 3. 쿠폰 생성
        UserCoupon coupon = createUserCoupon(customerId, lockedPolicy);
        userCouponRepository.save(coupon);

        log.info("쿠폰 발급 완료: customerId={}, policyCode={}", customerId, policyCode);
        return coupon;
    }

    /**
     * 선착순 쿠폰 발급 (Redis + DB)
     */
    @Transactional
    public UserCoupon issueFirstComeCoupon(Long customerId, String policyCode) {
        String countKey = ISSUE_COUNT_KEY + policyCode;
        String lockKey = ISSUE_LOCK_KEY + customerId + ":" + policyCode;

        // 1. 중복 발급 방지 (분산 락)
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                throw new CouponIssueException("잠시 후 다시 시도해주세요");
            }

            // 2. 이미 발급받았는지 확인
            if (userCouponRepository.existsByCustomerIdAndPolicyCode(customerId, policyCode)) {
                throw new CouponAlreadyIssuedException("이미 발급받은 쿠폰입니다");
            }

            CouponPolicy policy = policyRepository.findByCode(policyCode)
                .orElseThrow();

            // 3. Redis로 수량 선차감
            Long currentCount = redisTemplate.opsForValue().increment(countKey);
            if (currentCount == null || currentCount > policy.getTotalQuantity()) {
                redisTemplate.opsForValue().decrement(countKey);
                throw new CouponSoldOutException("쿠폰 소진");
            }

            // 4. DB 저장
            UserCoupon coupon = createUserCoupon(customerId, policy);
            userCouponRepository.save(coupon);

            return coupon;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CouponIssueException("발급 처리 중 오류");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 자동 쿠폰 발급 (이벤트 기반)
     */
    @EventListener
    @Async
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            issueCoupon(event.getCustomerId(), "WELCOME_COUPON");
            log.info("신규 가입 쿠폰 발급: customerId={}", event.getCustomerId());
        } catch (Exception e) {
            log.error("신규 가입 쿠폰 발급 실패", e);
        }
    }

    private void validateIssue(CouponPolicy policy, Long customerId) {
        // 1인당 발급 제한 확인
        int issuedCount = userCouponRepository.countByCustomerIdAndPolicyId(
            customerId, policy.getId()
        );
        if (issuedCount >= policy.getPerUserLimit()) {
            throw new CouponIssueException("1인당 발급 제한 초과");
        }
    }

    private UserCoupon createUserCoupon(Long customerId, CouponPolicy policy) {
        LocalDateTime expiredAt = calculateExpiredAt(policy);

        return UserCoupon.builder()
            .customerId(customerId)
            .policy(policy)
            .couponCode(UUID.randomUUID().toString())
            .status(UserCouponStatus.ISSUED)
            .issuedAt(LocalDateTime.now())
            .expiredAt(expiredAt)
            .build();
    }

    private LocalDateTime calculateExpiredAt(CouponPolicy policy) {
        if (policy.getValidDaysAfterIssue() != null) {
            return LocalDateTime.now().plusDays(policy.getValidDaysAfterIssue());
        }
        return policy.getValidUntil();
    }
}
```

### 할인 계산 서비스

```java
@Service
@RequiredArgsConstructor
public class DiscountCalculationService {

    private final UserCouponRepository userCouponRepository;
    private final ProductService productService;

    /**
     * 쿠폰 적용 가능 여부 검증 + 할인 금액 계산
     */
    public DiscountResult calculateDiscount(Long customerId, String couponCode,
                                            OrderContext orderContext) {
        // 1. 쿠폰 조회
        UserCoupon coupon = userCouponRepository.findByCouponCode(couponCode)
            .orElseThrow(() -> new CouponNotFoundException(couponCode));

        // 2. 기본 검증
        validateCoupon(coupon, customerId);

        CouponPolicy policy = coupon.getPolicy();

        // 3. 적용 조건 검증
        validateConditions(policy, orderContext);

        // 4. 할인 금액 계산
        Long discountAmount = calculateDiscountAmount(policy, orderContext);

        return DiscountResult.builder()
            .couponCode(couponCode)
            .discountType(policy.getDiscountType())
            .discountAmount(discountAmount)
            .originalAmount(orderContext.getTotalAmount())
            .finalAmount(orderContext.getTotalAmount() - discountAmount)
            .build();
    }

    private void validateCoupon(UserCoupon coupon, Long customerId) {
        if (!coupon.getCustomerId().equals(customerId)) {
            throw new CouponOwnerMismatchException("본인 쿠폰이 아닙니다");
        }
        if (!coupon.isUsable()) {
            throw new CouponNotUsableException("사용 불가능한 쿠폰입니다");
        }
    }

    private void validateConditions(CouponPolicy policy, OrderContext context) {
        // 최소 주문금액
        if (policy.getMinOrderAmount() != null &&
            context.getTotalAmount() < policy.getMinOrderAmount()) {
            throw new MinOrderAmountNotMetException(
                "최소 주문금액: " + policy.getMinOrderAmount() + "원"
            );
        }

        // 첫 구매 전용
        if (Boolean.TRUE.equals(policy.getFirstOrderOnly()) &&
            !context.isFirstOrder()) {
            throw new FirstOrderOnlyException("첫 구매 전용 쿠폰입니다");
        }

        // 적용 가능 카테고리
        if (policy.getApplicableCategoryIds() != null &&
            !policy.getApplicableCategoryIds().isEmpty()) {
            boolean hasApplicableItem = context.getItems().stream()
                .anyMatch(item -> policy.getApplicableCategoryIds()
                    .contains(item.getCategoryId()));
            if (!hasApplicableItem) {
                throw new CategoryNotApplicableException("적용 가능한 상품이 없습니다");
            }
        }

        // 제외 상품
        if (policy.getExcludedProductIds() != null) {
            boolean allExcluded = context.getItems().stream()
                .allMatch(item -> policy.getExcludedProductIds()
                    .contains(item.getProductId()));
            if (allExcluded) {
                throw new ProductExcludedException("모든 상품이 쿠폰 적용 제외입니다");
            }
        }
    }

    private Long calculateDiscountAmount(CouponPolicy policy, OrderContext context) {
        Long applicableAmount = calculateApplicableAmount(policy, context);

        return switch (policy.getDiscountType()) {
            case FIXED_AMOUNT -> Math.min(policy.getDiscountValue(), applicableAmount);

            case PERCENTAGE -> {
                Long discount = applicableAmount * policy.getDiscountValue() / 100;
                if (policy.getMaxDiscountAmount() != null) {
                    discount = Math.min(discount, policy.getMaxDiscountAmount());
                }
                yield discount;
            }

            case FREE_SHIPPING -> context.getShippingFee();

            default -> 0L;
        };
    }

    /**
     * 적용 대상 금액 계산 (제외 상품 제거)
     */
    private Long calculateApplicableAmount(CouponPolicy policy, OrderContext context) {
        return context.getItems().stream()
            .filter(item -> !isExcluded(policy, item))
            .mapToLong(item -> item.getPrice() * item.getQuantity())
            .sum();
    }

    private boolean isExcluded(CouponPolicy policy, OrderItem item) {
        if (policy.getExcludedProductIds() != null &&
            policy.getExcludedProductIds().contains(item.getProductId())) {
            return true;
        }
        if (policy.getApplicableCategoryIds() != null &&
            !policy.getApplicableCategoryIds().isEmpty() &&
            !policy.getApplicableCategoryIds().contains(item.getCategoryId())) {
            return true;
        }
        return false;
    }
}
```

### 쿠폰 사용/복구 서비스

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CouponUsageService {

    private final UserCouponRepository userCouponRepository;
    private final DiscountCalculationService discountService;

    /**
     * 쿠폰 사용 (주문 확정 시)
     */
    @Transactional
    public void useCoupon(Long customerId, String couponCode, String orderId) {
        UserCoupon coupon = userCouponRepository.findByCouponCodeWithLock(couponCode)
            .orElseThrow(() -> new CouponNotFoundException(couponCode));

        // 소유자 검증
        if (!coupon.getCustomerId().equals(customerId)) {
            throw new CouponOwnerMismatchException("본인 쿠폰이 아닙니다");
        }

        // 사용 처리
        coupon.use(orderId);

        log.info("쿠폰 사용 완료: couponCode={}, orderId={}", couponCode, orderId);
    }

    /**
     * 쿠폰 복구 (주문 취소 시)
     */
    @Transactional
    public void restoreCoupon(String orderId) {
        UserCoupon coupon = userCouponRepository.findByUsedOrderId(orderId)
            .orElse(null);

        if (coupon == null) {
            log.info("복구할 쿠폰 없음: orderId={}", orderId);
            return;
        }

        // 만료 여부 확인
        if (coupon.getExpiredAt() != null &&
            LocalDateTime.now().isAfter(coupon.getExpiredAt())) {
            log.info("만료된 쿠폰은 복구하지 않음: couponCode={}", coupon.getCouponCode());
            return;
        }

        coupon.restore();
        log.info("쿠폰 복구 완료: couponCode={}, orderId={}", coupon.getCouponCode(), orderId);
    }

    /**
     * 쿠폰 만료 처리 (스케줄러)
     */
    @Scheduled(cron = "0 0 1 * * *")  // 매일 새벽 1시
    @Transactional
    public void expireCoupons() {
        LocalDateTime now = LocalDateTime.now();

        int expiredCount = userCouponRepository.expireOldCoupons(
            UserCouponStatus.ISSUED,
            UserCouponStatus.EXPIRED,
            now
        );

        log.info("쿠폰 만료 처리 완료: {}개", expiredCount);
    }
}
```

### 프로모션 규칙 엔진

```java
/**
 * 프로모션 규칙 엔진
 * - 장바구니 단위 자동 할인 적용
 */
@Service
@RequiredArgsConstructor
public class PromotionEngine {

    private final PromotionRuleRepository ruleRepository;
    private final List<PromotionStrategy> strategies;

    /**
     * 적용 가능한 모든 프로모션 계산
     */
    public PromotionResult applyPromotions(OrderContext context) {
        List<PromotionRule> activeRules = ruleRepository.findActiveRules(LocalDateTime.now());

        List<AppliedPromotion> appliedPromotions = new ArrayList<>();
        Long totalDiscount = 0L;

        for (PromotionRule rule : activeRules) {
            if (rule.isApplicable(context)) {
                PromotionStrategy strategy = findStrategy(rule.getType());
                AppliedPromotion applied = strategy.apply(rule, context);

                if (applied != null) {
                    appliedPromotions.add(applied);
                    totalDiscount += applied.getDiscountAmount();
                }
            }
        }

        return PromotionResult.builder()
            .appliedPromotions(appliedPromotions)
            .totalDiscount(totalDiscount)
            .build();
    }

    private PromotionStrategy findStrategy(PromotionType type) {
        return strategies.stream()
            .filter(s -> s.supports(type))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("지원하지 않는 프로모션 타입"));
    }
}

/**
 * 프로모션 규칙 엔티티
 */
@Entity
@Table(name = "promotion_rules")
public class PromotionRule {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PromotionType type;

    // 조건
    private Long minOrderAmount;
    private Integer minQuantity;
    private Long targetCategoryId;
    private Long targetProductId;

    // 할인
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private Long discountValue;
    private Long maxDiscount;

    // 기간
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Integer priority;  // 우선순위 (낮을수록 먼저 적용)
    private Boolean stackable; // 중복 적용 가능 여부

    public boolean isApplicable(OrderContext context) {
        // 기간 체크
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startAt) || now.isAfter(endAt)) {
            return false;
        }

        // 최소 주문금액
        if (minOrderAmount != null && context.getTotalAmount() < minOrderAmount) {
            return false;
        }

        // 최소 수량
        if (minQuantity != null && context.getTotalQuantity() < minQuantity) {
            return false;
        }

        // 카테고리/상품 체크
        if (targetCategoryId != null || targetProductId != null) {
            return context.getItems().stream()
                .anyMatch(item ->
                    (targetCategoryId == null || targetCategoryId.equals(item.getCategoryId())) &&
                    (targetProductId == null || targetProductId.equals(item.getProductId()))
                );
        }

        return true;
    }
}

public enum PromotionType {
    CART_DISCOUNT,      // 장바구니 할인
    CATEGORY_DISCOUNT,  // 카테고리 할인
    PRODUCT_DISCOUNT,   // 상품 할인
    BUNDLE_DISCOUNT,    // 묶음 할인
    SHIPPING_DISCOUNT   // 배송비 할인
}

/**
 * 장바구니 할인 전략 (예: 5만원 이상 구매 시 5% 할인)
 */
@Component
public class CartDiscountStrategy implements PromotionStrategy {

    @Override
    public boolean supports(PromotionType type) {
        return type == PromotionType.CART_DISCOUNT;
    }

    @Override
    public AppliedPromotion apply(PromotionRule rule, OrderContext context) {
        Long discount = switch (rule.getDiscountType()) {
            case PERCENTAGE -> {
                Long calculated = context.getTotalAmount() * rule.getDiscountValue() / 100;
                yield rule.getMaxDiscount() != null ?
                    Math.min(calculated, rule.getMaxDiscount()) : calculated;
            }
            case FIXED_AMOUNT -> Math.min(rule.getDiscountValue(), context.getTotalAmount());
            default -> 0L;
        };

        return AppliedPromotion.builder()
            .ruleName(rule.getName())
            .discountAmount(discount)
            .build();
    }
}
```

## 동작 원리

### 쿠폰 적용 플로우

```
┌─────────────────────────────────────────────────────────────────┐
│                     쿠폰 적용 플로우                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  고객         주문서비스        쿠폰서비스        DB              │
│   │              │               │              │               │
│   │ 쿠폰 적용    │               │              │               │
│   │────────────→│               │              │               │
│   │              │               │              │               │
│   │              │ 쿠폰 검증     │              │               │
│   │              │──────────────→│              │               │
│   │              │               │              │               │
│   │              │               │ 쿠폰 조회    │               │
│   │              │               │─────────────→│               │
│   │              │               │              │               │
│   │              │               │ 소유자 검증  │               │
│   │              │               │ 상태 검증    │               │
│   │              │               │ 조건 검증    │               │
│   │              │               │              │               │
│   │              │ 할인 금액     │              │               │
│   │              │←──────────────│              │               │
│   │              │               │              │               │
│   │ 적용 결과    │               │              │               │
│   │←────────────│               │              │               │
│   │              │               │              │               │
│   │ 결제 요청    │               │              │               │
│   │────────────→│               │              │               │
│   │              │               │              │               │
│   │              │ 쿠폰 사용     │              │               │
│   │              │──────────────→│              │               │
│   │              │               │              │               │
│   │              │               │ 상태 변경    │               │
│   │              │               │ USED         │               │
│   │              │               │─────────────→│               │
│   │              │               │              │               │
│   │ 결제 완료    │               │              │               │
│   │←────────────│               │              │               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 검증 시점 | 장점 | 단점 |
|----------|------|------|
| 장바구니 진입 시 | 빠른 피드백 | 실제 주문 시 상태 변경 가능 |
| 결제 직전 | 정확한 검증 | 늦은 피드백 |
| 양쪽 모두 | 안전 | 복잡, 성능 비용 |

## 트러블슈팅

### 사례 1: 쿠폰 중복 사용

#### 증상
```
동일 쿠폰으로 2건 주문됨
병렬 요청 시 발생
```

#### 해결 방법
```java
// 1. 비관적 락으로 쿠폰 조회
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT c FROM UserCoupon c WHERE c.couponCode = :code")
Optional<UserCoupon> findByCouponCodeWithLock(@Param("code") String code);

// 2. 사용 시 상태 재확인 + 유니크 제약
@Entity
@Table(name = "user_coupons",
       uniqueConstraints = @UniqueConstraint(columns = {"coupon_code", "used_order_id"}))
public class UserCoupon {
    // ...
}
```

### 사례 2: 선착순 쿠폰 초과 발급

#### 증상
```
1000개 한정인데 1050개 발급됨
동시 요청 시 Redis 카운터 초과
```

#### 해결 방법
```java
// Lua 스크립트로 원자적 발급
String script =
    "local count = tonumber(redis.call('GET', KEYS[1]) or 0) " +
    "local limit = tonumber(ARGV[1]) " +
    "if count < limit then " +
    "  redis.call('INCR', KEYS[1]) " +
    "  return 1 " +
    "else " +
    "  return 0 " +
    "end";

Long result = redisTemplate.execute(
    new DefaultRedisScript<>(script, Long.class),
    List.of(countKey),
    String.valueOf(policy.getTotalQuantity())
);

if (result == 0) {
    throw new CouponSoldOutException("쿠폰 소진");
}
```

## 면접 예상 질문

### Q: 쿠폰 중복 사용은 어떻게 방지하나?

A: **여러 레이어**에서 방지합니다.
1. **DB 레벨**: 상태 컬럼을 USED로 변경 + 낙관적/비관적 락
2. **유니크 제약**: `(coupon_code, used_order_id)` 복합 유니크
3. **분산 락**: Redis 분산 락으로 동시 요청 직렬화
4. **검증 시점**: 결제 직전에 최종 검증

### Q: 정률 할인과 정액 할인 중 어떤 게 유리한가?

A: **상황에 따라** 다릅니다.
- **정액 할인**: 고정 비용 예측 가능, 저가 상품에 유리
- **정률 할인**: 고가 상품에 효과적, 최대 할인 금액 설정 필수
- 보통 정률 할인에 **cap(최대 할인금액)**을 설정하여 리스크 관리

### Q: 쿠폰 정책과 쿠폰 인스턴스를 분리하는 이유?

A: **유연성과 관리 용이성** 때문입니다.
- 정책 변경 시 발급된 쿠폰에 영향 없음
- 개별 쿠폰의 만료일, 사용 이력 관리 가능
- 통계/분석 용이 (어떤 정책의 쿠폰이 얼마나 사용되었는지)
- 1인당 발급 제한 관리 가능

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [트랜잭션](../db/transaction.md) | 선수 지식 | Intermediate |
| [Redis 캐싱](../db/redis-caching.md) | 선수 지식 | Intermediate |
| [선착순 쿠폰 시스템](./flash-sale-system.md) | 관련 주제 | Advanced |
| [주문 처리 시스템](./order-processing-system.md) | 연계 시스템 | Advanced |
| [장바구니 시스템](./shopping-cart-system.md) | 연계 시스템 | Intermediate |

## 참고 자료

- [Discount Engine Design Pattern](https://martinfowler.com/articles/patterns-of-distributed-systems/)
- [E-commerce Promotion System - AWS](https://aws.amazon.com/solutions/retail/)
- [Redis Best Practices for Coupon Systems](https://redis.io/docs/manual/patterns/)
