# 결제 시스템 설계 (Payment System Design)

> `[4] 심화` · 선수 지식: [멱등성](./idempotency.md), [SAGA 패턴](./saga-pattern.md), [OAuth/JWT](../security/oauth-jwt.md)

> 안전하고 신뢰할 수 있는 결제 처리를 위한 시스템 설계: 중복 결제 방지, 결제 상태 관리, PG사 연동

`#결제시스템` `#PaymentSystem` `#PG` `#PaymentGateway` `#토스페이먼츠` `#TossPayments` `#아임포트` `#멱등성` `#Idempotency` `#중복결제` `#결제취소` `#환불` `#Refund` `#정산` `#Settlement` `#PCI-DSS` `#보안` `#Security` `#3DS` `#웹훅` `#Webhook` `#콜백` `#Callback` `#결제상태` `#PaymentStatus` `#트랜잭션` `#커머스` `#Ecommerce`

## 왜 알아야 하는가?

- **실무**: 결제는 돈과 직결. 중복 결제, 결제 누락은 곧 금전적 손실과 법적 문제
- **면접**: "결제 API 호출 중 타임아웃이 나면?" 핵심 질문
- **기반 지식**: 외부 API 연동, 분산 트랜잭션, 보안의 종합적 적용

## 핵심 개념

- **PG(Payment Gateway)**: 결제 대행사 (토스페이먼츠, NHN KCP, 아임포트 등)
- **멱등성 키**: 동일 결제 요청 중복 처리 방지
- **결제 상태 머신**: PENDING → APPROVED → COMPLETED / CANCELLED
- **Webhook**: PG사에서 결제 결과를 비동기로 통지

## 쉽게 이해하기

**편의점 결제**에 비유하면 이해가 쉽습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                     결제 플로우 비유                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  고객 (클라이언트)                                           │
│    │                                                        │
│    │ "카드로 결제할게요"                                     │
│    ▼                                                        │
│  점원 (우리 서버)                                            │
│    │                                                        │
│    │ "잠시만요, 카드사에 확인할게요"                         │
│    ▼                                                        │
│  카드 단말기 (PG사)                                          │
│    │                                                        │
│    │ "카드사에 승인 요청 중..."                              │
│    ▼                                                        │
│  카드사 (은행/카드사)                                        │
│    │                                                        │
│    │ "승인!"                                                │
│    ▼                                                        │
│  점원 → 고객: "결제 완료되었습니다"                          │
│                                                              │
│  우리가 구현해야 하는 부분: 점원 역할                        │
│  - 카드 단말기(PG) 연동                                      │
│  - 결제 상태 관리                                            │
│  - 실패 시 대응                                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 결제 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                     결제 시스템 아키텍처                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐  │
│  │ 클라이언트│ →  │ API서버  │ →  │ 결제서비스 │ →  │  PG사   │  │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘  │
│                        │               │               │        │
│                        │               │               │        │
│                        ▼               ▼               │        │
│                   ┌─────────┐    ┌─────────┐          │        │
│                   │주문 DB  │    │결제 DB  │          │        │
│                   └─────────┘    └─────────┘          │        │
│                                       ▲               │        │
│                                       │               │        │
│                                       └───── Webhook ─┘        │
│                                         (결제 결과 통지)        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 결제 상태 머신

```
┌─────────────────────────────────────────────────────────────────┐
│                       결제 상태 전이                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                      ┌────────────┐                             │
│                      │  CREATED   │ (결제 시작)                  │
│                      └─────┬──────┘                             │
│                            │                                    │
│                            ▼                                    │
│                      ┌────────────┐                             │
│           ┌─────────│   PENDING  │─────────┐                   │
│           │         │  (승인 대기)│         │                   │
│           │         └─────┬──────┘         │                   │
│           │               │                │                   │
│      사용자취소       PG 승인 완료       타임아웃                │
│           │               │                │                   │
│           ▼               ▼                ▼                   │
│    ┌────────────┐  ┌────────────┐  ┌────────────┐             │
│    │ CANCELLED  │  │  APPROVED  │  │   FAILED   │             │
│    │ (사용자취소)│  │  (PG승인)  │  │   (실패)   │             │
│    └────────────┘  └─────┬──────┘  └────────────┘             │
│                          │                                     │
│                     결제 확정                                   │
│                     (capture)                                  │
│                          │                                     │
│                          ▼                                     │
│                   ┌────────────┐                               │
│         ┌────────│ COMPLETED  │────────┐                      │
│         │        │ (결제완료) │        │                      │
│         │        └────────────┘        │                      │
│         │                              │                      │
│     전액환불                        부분환불                   │
│         │                              │                      │
│         ▼                              ▼                      │
│  ┌────────────┐              ┌────────────────┐              │
│  │  REFUNDED  │              │PARTIALLY_REFUND│              │
│  │ (전액환불) │              │   (부분환불)   │              │
│  └────────────┘              └────────────────┘              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 결제 엔티티 설계

```java
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentKey;  // PG사 결제 키

    @Column(nullable = false, unique = true)
    private String orderId;     // 주문 ID (멱등성 키 역할)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;  // CARD, VIRTUAL_ACCOUNT, TRANSFER

    @Column(nullable = false)
    private Long amount;           // 결제 금액

    private Long refundedAmount;   // 환불된 금액

    @Column(nullable = false)
    private String customerEmail;

    private String failReason;     // 실패 사유

    private LocalDateTime approvedAt;   // PG 승인 시간
    private LocalDateTime completedAt;  // 결제 완료 시간
    private LocalDateTime cancelledAt;  // 취소 시간

    @Version
    private Long version;

    // 결제 상태 전이 메서드
    public void approve(String paymentKey, LocalDateTime approvedAt) {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태가 아닙니다");
        }
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = approvedAt;
    }

    public void complete() {
        if (this.status != PaymentStatus.APPROVED) {
            throw new IllegalStateException("승인 상태가 아닙니다");
        }
        this.status = PaymentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void refund(Long refundAmount) {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("완료된 결제만 환불 가능");
        }
        this.refundedAmount += refundAmount;
        this.status = refundedAmount.equals(this.amount)
            ? PaymentStatus.REFUNDED
            : PaymentStatus.PARTIALLY_REFUNDED;
        this.cancelledAt = LocalDateTime.now();
    }
}

public enum PaymentStatus {
    CREATED,           // 결제 생성
    PENDING,           // 승인 대기
    APPROVED,          // PG 승인 완료
    COMPLETED,         // 결제 완료
    FAILED,            // 결제 실패
    CANCELLED,         // 사용자 취소
    REFUNDED,          // 전액 환불
    PARTIALLY_REFUNDED // 부분 환불
}
```

### PG사 연동 (토스페이먼츠 예시)

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class TossPaymentService {

    private static final String TOSS_API_URL = "https://api.tosspayments.com/v1";

    private final WebClient webClient;
    private final PaymentRepository paymentRepository;

    /**
     * 1단계: 결제 준비 (클라이언트에 전달할 정보 생성)
     */
    @Transactional
    public PaymentReadyResponse preparePayment(PaymentRequest request) {
        // 멱등성: 같은 orderId로 이미 결제 존재하면 반환
        Optional<Payment> existing = paymentRepository.findByOrderId(request.getOrderId());
        if (existing.isPresent()) {
            return PaymentReadyResponse.from(existing.get());
        }

        Payment payment = Payment.builder()
            .orderId(request.getOrderId())
            .amount(request.getAmount())
            .status(PaymentStatus.CREATED)
            .method(request.getMethod())
            .customerEmail(request.getCustomerEmail())
            .build();

        paymentRepository.save(payment);

        return PaymentReadyResponse.builder()
            .orderId(payment.getOrderId())
            .amount(payment.getAmount())
            .successUrl(request.getSuccessUrl())
            .failUrl(request.getFailUrl())
            .build();
    }

    /**
     * 2단계: 결제 승인 (PG사에 승인 요청)
     */
    @Transactional
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
            .orElseThrow(() -> new RuntimeException("결제 정보 없음"));

        // 이미 승인된 결제면 기존 결과 반환 (멱등성)
        if (payment.getStatus() == PaymentStatus.APPROVED ||
            payment.getStatus() == PaymentStatus.COMPLETED) {
            return PaymentConfirmResponse.from(payment);
        }

        // 금액 검증 (위변조 방지)
        if (!payment.getAmount().equals(request.getAmount())) {
            throw new PaymentAmountMismatchException(
                "금액 불일치: 요청=" + request.getAmount() + ", 저장=" + payment.getAmount()
            );
        }

        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        try {
            // PG사 승인 API 호출
            TossConfirmResponse response = webClient.post()
                .uri(TOSS_API_URL + "/payments/confirm")
                .header("Authorization", "Basic " + encodeSecretKey())
                .header("Idempotency-Key", request.getOrderId())  // 멱등성 키
                .bodyValue(Map.of(
                    "paymentKey", request.getPaymentKey(),
                    "orderId", request.getOrderId(),
                    "amount", request.getAmount()
                ))
                .retrieve()
                .bodyToMono(TossConfirmResponse.class)
                .block(Duration.ofSeconds(30));

            // 승인 성공
            payment.approve(response.getPaymentKey(), response.getApprovedAt());
            payment.complete();
            paymentRepository.save(payment);

            return PaymentConfirmResponse.success(payment);

        } catch (WebClientResponseException e) {
            // PG사 응답 오류
            log.error("결제 승인 실패: {}", e.getResponseBodyAsString());
            payment.fail(parseErrorMessage(e));
            paymentRepository.save(payment);
            throw new PaymentFailedException(payment.getFailReason());
        }
    }

    /**
     * 3단계: 결제 취소/환불
     */
    @Transactional
    public PaymentCancelResponse cancelPayment(String paymentKey, Long cancelAmount, String cancelReason) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
            .orElseThrow(() -> new RuntimeException("결제 정보 없음"));

        // 환불 가능 금액 검증
        Long refundableAmount = payment.getAmount() - payment.getRefundedAmount();
        if (cancelAmount > refundableAmount) {
            throw new InvalidRefundAmountException(
                "환불 가능 금액 초과: 요청=" + cancelAmount + ", 가능=" + refundableAmount
            );
        }

        // PG사 취소 API 호출
        TossCancelResponse response = webClient.post()
            .uri(TOSS_API_URL + "/payments/" + paymentKey + "/cancel")
            .header("Authorization", "Basic " + encodeSecretKey())
            .header("Idempotency-Key", paymentKey + "-cancel-" + cancelAmount)
            .bodyValue(Map.of(
                "cancelReason", cancelReason,
                "cancelAmount", cancelAmount
            ))
            .retrieve()
            .bodyToMono(TossCancelResponse.class)
            .block(Duration.ofSeconds(30));

        payment.refund(cancelAmount);
        paymentRepository.save(payment);

        return PaymentCancelResponse.from(payment, response);
    }
}
```

### Webhook 처리 (비동기 결제 결과 수신)

```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final PaymentService paymentService;
    private final WebhookVerifier webhookVerifier;

    @PostMapping("/webhook/toss")
    public ResponseEntity<Void> handleTossWebhook(
            @RequestHeader("Toss-Signature") String signature,
            @RequestBody String payload) {

        // 1. 서명 검증 (위변조 방지)
        if (!webhookVerifier.verify(signature, payload)) {
            log.warn("Webhook 서명 검증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TossWebhookEvent event = parseWebhookEvent(payload);

        // 2. 이벤트 타입별 처리
        switch (event.getEventType()) {
            case "PAYMENT_STATUS_CHANGED":
                handlePaymentStatusChanged(event);
                break;
            case "DEPOSIT_CALLBACK":
                handleVirtualAccountDeposit(event);
                break;
            case "REFUND_STATUS_CHANGED":
                handleRefundStatusChanged(event);
                break;
            default:
                log.info("처리하지 않는 이벤트 타입: {}", event.getEventType());
        }

        // 3. 200 OK 반환 (재시도 방지)
        return ResponseEntity.ok().build();
    }

    private void handlePaymentStatusChanged(TossWebhookEvent event) {
        String paymentKey = event.getData().getPaymentKey();
        String status = event.getData().getStatus();

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
            .orElse(null);

        if (payment == null) {
            log.warn("Webhook: 존재하지 않는 결제 키: {}", paymentKey);
            return;
        }

        // 멱등성: 이미 처리된 상태면 무시
        if (payment.getStatus().name().equals(status)) {
            log.info("Webhook: 이미 처리된 상태: {}", status);
            return;
        }

        switch (status) {
            case "DONE":
                payment.complete();
                break;
            case "CANCELED":
                payment.cancel("Webhook: 취소됨");
                break;
            case "EXPIRED":
                payment.fail("Webhook: 만료됨");
                break;
        }

        paymentRepository.save(payment);
    }

    // 가상계좌 입금 확인
    private void handleVirtualAccountDeposit(TossWebhookEvent event) {
        String orderId = event.getData().getOrderId();
        Long depositAmount = event.getData().getAmount();

        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow();

        if (!payment.getAmount().equals(depositAmount)) {
            log.error("입금 금액 불일치: orderId={}", orderId);
            // 수동 확인 필요 알림
            return;
        }

        payment.complete();
        paymentRepository.save(payment);

        // 주문 서비스에 알림
        orderService.completePayment(orderId);
    }
}
```

### 결제 실패 대응

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentRecoveryService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentService tossPaymentService;

    /**
     * 미완료 결제 복구 (스케줄러)
     */
    @Scheduled(fixedDelay = 300000)  // 5분마다
    @Transactional
    public void recoverPendingPayments() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

        List<Payment> pendingPayments = paymentRepository
            .findByStatusAndCreatedAtBefore(PaymentStatus.PENDING, threshold);

        for (Payment payment : pendingPayments) {
            try {
                // PG사에 결제 상태 조회
                TossPaymentStatus pgStatus = tossPaymentService.getPaymentStatus(payment.getPaymentKey());

                switch (pgStatus) {
                    case DONE:
                        payment.complete();
                        log.info("복구: 결제 완료 처리: {}", payment.getOrderId());
                        break;
                    case CANCELED:
                    case EXPIRED:
                        payment.fail("PG 상태: " + pgStatus);
                        log.info("복구: 결제 실패 처리: {}", payment.getOrderId());
                        break;
                    case IN_PROGRESS:
                        // 아직 진행 중, 다음 주기에 재확인
                        break;
                }

                paymentRepository.save(payment);
            } catch (Exception e) {
                log.error("결제 복구 실패: orderId={}", payment.getOrderId(), e);
            }
        }
    }

    /**
     * 결제-주문 정합성 검증 (일 배치)
     */
    @Scheduled(cron = "0 0 3 * * *")  // 매일 새벽 3시
    public void validatePaymentOrderConsistency() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Payment> completedPayments = paymentRepository
            .findByStatusAndCompletedAtBetween(
                PaymentStatus.COMPLETED,
                yesterday.atStartOfDay(),
                yesterday.plusDays(1).atStartOfDay()
            );

        for (Payment payment : completedPayments) {
            Order order = orderRepository.findByOrderId(payment.getOrderId())
                .orElse(null);

            if (order == null) {
                log.error("정합성 오류: 결제 완료됐으나 주문 없음: {}", payment.getOrderId());
                alertService.sendCriticalAlert("결제-주문 불일치: " + payment.getOrderId());
                continue;
            }

            if (order.getStatus() != OrderStatus.PAID) {
                log.error("정합성 오류: 결제 완료됐으나 주문 상태 불일치: {} - {}",
                    payment.getOrderId(), order.getStatus());
                // 자동 복구 또는 알림
            }
        }
    }
}
```

### 보안 고려사항

```java
@Configuration
public class PaymentSecurityConfig {

    /**
     * PG사 Secret Key 관리
     */
    @Value("${toss.secret-key}")
    private String secretKey;

    // Secret Key는 환경 변수 또는 Secret Manager에서 로드
    // 절대 코드에 하드코딩하지 않음

    /**
     * 금액 위변조 방지
     */
    public void validateAmount(String orderId, Long requestAmount) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow();

        if (!payment.getAmount().equals(requestAmount)) {
            log.error("금액 위변조 시도 감지: orderId={}, 원본={}, 요청={}",
                orderId, payment.getAmount(), requestAmount);
            throw new PaymentSecurityException("금액 위변조 감지");
        }
    }

    /**
     * Webhook 서명 검증
     */
    public boolean verifyWebhookSignature(String signature, String payload, String secret) {
        String expected = HmacUtils.hmacSha256Hex(secret, payload);
        return MessageDigest.isEqual(
            expected.getBytes(StandardCharsets.UTF_8),
            signature.getBytes(StandardCharsets.UTF_8)
        );
    }
}
```

## 동작 원리

### 전체 결제 플로우

```
┌─────────────────────────────────────────────────────────────────┐
│                       결제 전체 플로우                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  클라이언트        서버           결제 DB        PG사             │
│     │              │               │             │               │
│     │ 1. 결제 요청  │               │             │               │
│     │ ───────────→ │               │             │               │
│     │              │ 2. 결제 생성   │             │               │
│     │              │ ─────────────→│             │               │
│     │              │               │             │               │
│     │ 3. 결제 정보 │               │             │               │
│     │←─────────────│               │             │               │
│     │              │               │             │               │
│     │ 4. PG 결제창 │               │             │               │
│     │─────────────────────────────────────────→ │               │
│     │              │               │             │               │
│     │ 5. 결제 완료 (paymentKey)    │             │               │
│     │←─────────────────────────────────────────  │               │
│     │              │               │             │               │
│     │ 6. 승인 요청 │               │             │               │
│     │ ───────────→ │               │             │               │
│     │              │ 7. 상태=PENDING│             │               │
│     │              │ ─────────────→│             │               │
│     │              │               │             │               │
│     │              │ 8. 승인 API   │             │               │
│     │              │ ─────────────────────────→ │               │
│     │              │               │             │               │
│     │              │ 9. 승인 성공  │             │               │
│     │              │ ←────────────────────────── │               │
│     │              │               │             │               │
│     │              │ 10. 상태=COMPLETED           │               │
│     │              │ ─────────────→│             │               │
│     │              │               │             │               │
│     │ 11. 결제 완료│               │             │               │
│     │←─────────────│               │             │               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트레이드오프

| 방식 | 장점 | 단점 |
|------|------|------|
| 동기 승인 | 즉시 결과 확인 | 타임아웃 시 불확실 |
| Webhook 의존 | 안정적 | 지연 발생, 복잡 |
| 폴링 | 단순 | 리소스 낭비 |

## 트러블슈팅

### 사례 1: 결제 승인 타임아웃

#### 증상
```
고객: 결제 버튼 클릭 → 로딩 → 타임아웃 에러
실제: PG사에서는 승인 완료됨
결과: 고객 돈은 빠졌는데 주문 안 됨
```

#### 해결 방법
```java
@Transactional
public PaymentConfirmResponse confirmPaymentWithRecovery(PaymentConfirmRequest request) {
    try {
        return confirmPayment(request);
    } catch (TimeoutException e) {
        // 타임아웃 시 PG사 상태 조회
        TossPaymentStatus status = queryPaymentStatus(request.getPaymentKey());

        if (status == TossPaymentStatus.DONE) {
            // 실제로는 성공함 → 성공 처리
            Payment payment = paymentRepository.findByOrderId(request.getOrderId()).orElseThrow();
            payment.approve(request.getPaymentKey(), LocalDateTime.now());
            payment.complete();
            return PaymentConfirmResponse.success(payment);
        } else {
            // 실제로 실패 → 실패 처리
            throw new PaymentFailedException("결제 타임아웃");
        }
    }
}
```

### 사례 2: 중복 결제

#### 증상
```
고객이 빠르게 결제 버튼 2번 클릭
결과: 같은 주문에 2번 결제됨
```

#### 해결 방법
```java
// 1. 프론트엔드: 버튼 비활성화
// 2. 백엔드: orderId로 멱등성 보장
@Transactional
public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {
    // 이미 완료된 결제면 기존 결과 반환
    Optional<Payment> existing = paymentRepository.findByOrderId(request.getOrderId());
    if (existing.isPresent() && existing.get().getStatus() == PaymentStatus.COMPLETED) {
        return PaymentConfirmResponse.from(existing.get());
    }

    // 3. PG사 API에도 Idempotency-Key 전달
    // ...
}
```

## 면접 예상 질문

### Q: 결제 API 호출 중 타임아웃이 나면 어떻게 처리하나?

A: 타임아웃은 "성공인지 실패인지 모르는 상태"입니다. 처리 방법:
1. **PG사 조회 API**로 실제 결제 상태 확인
2. 성공이면 성공 처리, 실패면 실패 처리
3. 조회도 실패하면 PENDING 상태로 두고, 스케줄러가 주기적으로 복구
4. Webhook으로 최종 상태를 비동기 수신하여 동기화

### Q: PG사 Webhook이 누락되면?

A: Webhook은 "보조 수단"으로 취급합니다.
1. **주기적 폴링**: 스케줄러가 PENDING 상태 결제를 PG사에 직접 조회
2. **타임아웃 설정**: 일정 시간 후 미완료 결제 자동 취소 처리
3. **정합성 배치**: 일 배치로 결제-주문 정합성 검증
4. **알림**: 불일치 발생 시 즉시 알림

### Q: 부분 환불은 어떻게 구현하나?

A: `refundedAmount` 필드로 누적 환불 금액을 관리합니다. 환불 요청 시 `(원래금액 - 환불누적) >= 요청금액`을 검증합니다. 전액 환불되면 REFUNDED, 일부면 PARTIALLY_REFUNDED 상태로 변경합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [멱등성](./idempotency.md) | 선수 지식 | Intermediate |
| [SAGA 패턴](./saga-pattern.md) | 선수 지식 | Advanced |
| [OAuth/JWT](../security/oauth-jwt.md) | 선수 지식 | Intermediate |
| [재고 관리 시스템](./inventory-system.md) | 관련 주제 | Advanced |
| [웹 보안](../security/web-security.md) | 관련 개념 | Intermediate |

## 참고 자료

- [토스페이먼츠 개발자 문서](https://docs.tosspayments.com/)
- [PCI DSS 표준](https://www.pcisecuritystandards.org/)
- [결제 시스템 설계 - AWS](https://aws.amazon.com/solutions/implementations/payments-systems/)
