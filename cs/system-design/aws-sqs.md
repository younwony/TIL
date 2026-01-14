# AWS SQS (Simple Queue Service)

> `[3] 중급` · 선수 지식: [메시지 큐](./message-queue.md), [Cloud Computing](./cloud-computing.md)

> AWS에서 제공하는 완전 관리형 메시지 큐 서비스로, 분산 시스템 간 비동기 통신을 지원한다

`#AWSSQS` `#SQS` `#SimpleQueueService` `#메시지큐` `#MessageQueue` `#AWS` `#AmazonWebServices` `#완전관리형` `#FullyManaged` `#StandardQueue` `#FIFOQueue` `#VisibilityTimeout` `#가시성타임아웃` `#DLQ` `#DeadLetterQueue` `#LongPolling` `#ShortPolling` `#비동기` `#Asynchronous` `#느슨한결합` `#LooseCoupling` `#서버리스` `#Serverless` `#Lambda` `#SNS` `#EventBridge` `#AtLeastOnce` `#ExactlyOnce` `#분산시스템`

## 왜 알아야 하는가?

- **실무**: AWS 기반 MSA에서 가장 많이 사용되는 메시지 큐. 인프라 관리 없이 바로 사용 가능
- **면접**: "SQS Standard vs FIFO 차이점", "Visibility Timeout 동작 원리" 등 AWS 아키텍처 필수 질문
- **기반 지식**: 서버리스 아키텍처, 이벤트 드리븐 설계의 핵심 구성 요소

## 핵심 개념

- **완전 관리형(Fully Managed)**: 인프라 프로비저닝, 패치, 스케일링을 AWS가 자동 처리
- **무제한 처리량**: Standard Queue는 초당 무제한 메시지 처리 가능
- **내구성**: 메시지를 여러 AZ에 중복 저장하여 데이터 손실 방지
- **보안**: IAM 정책, 암호화(SSE), VPC 엔드포인트 지원

## 쉽게 이해하기

### SQS = AWS가 운영하는 편의점 택배 보관함

**직접 메시지 큐 운영 (RabbitMQ, Kafka 자체 운영)**
아파트에 택배 보관함을 직접 설치하고 관리합니다. 고장 나면 직접 수리하고, 보관함이 부족하면 직접 추가 설치합니다.

**SQS 사용 (완전 관리형)**
편의점 택배 보관함을 이용합니다. 보관함 관리는 편의점(AWS)이 담당하고, 우리는 택배를 맡기고 찾기만 하면 됩니다.

| 비유 | SQS 개념 |
|------|----------|
| 편의점 | AWS |
| 택배 보관함 | SQS Queue |
| 택배 | Message |
| 택배 맡기는 사람 | Producer |
| 택배 찾는 사람 | Consumer |
| 보관 기한 | Message Retention (최대 14일) |
| "찾는 중" 표시 | Visibility Timeout |

### Standard vs FIFO = 일반 보관함 vs 번호표 보관함

| 비유 | Standard Queue | FIFO Queue |
|------|----------------|------------|
| 보관함 유형 | 빈 칸 아무 데나 | 번호 순서대로 |
| 찾는 순서 | 순서 상관없이 | 먼저 맡긴 것 먼저 |
| 중복 가능 | 간혹 같은 택배 2번 (드묾) | 중복 없음 |
| 속도 | 매우 빠름 | 상대적으로 느림 |

## Standard Queue vs FIFO Queue

### 비교표

| 특성 | Standard Queue | FIFO Queue |
|------|----------------|------------|
| **처리량** | 무제한 | 초당 3,000 메시지 (배치 시 30,000) |
| **순서 보장** | 최선 노력 (Best-effort) | 엄격한 순서 보장 |
| **중복 전달** | At-least-once (중복 가능) | Exactly-once (중복 제거) |
| **사용 사례** | 로그 처리, 알림, 작업 큐 | 주문 처리, 금융 거래 |
| **가격** | 더 저렴 | 약 20% 비쌈 |
| **큐 이름** | 자유롭게 | `.fifo` 접미사 필수 |

### Standard Queue

```
Producer → [메시지 A, B, C 전송]
                    ↓
            ┌──────────────┐
            │   Standard   │
            │    Queue     │
            └──────────────┘
                    ↓
Consumer ← [C, A, B 순서로 수신] (순서 변경 가능)
           [A 중복 수신 가능] (드묾)
```

**적합한 사용 사례**:
- 이메일/SMS 알림 발송
- 로그 수집 및 분석
- 배치 작업 처리
- 이미지/비디오 인코딩

### FIFO Queue

```
Producer → [메시지 A, B, C 전송]
                    ↓
            ┌──────────────┐
            │    FIFO      │
            │    Queue     │
            │   (.fifo)    │
            └──────────────┘
                    ↓
Consumer ← [A, B, C 순서로 수신] (순서 보장)
           [중복 없음] (Exactly-once)
```

**적합한 사용 사례**:
- 주문 처리 (순서 중요)
- 금융 거래
- 티켓 예매 시스템
- 재고 관리

### Message Group ID (FIFO 전용)

FIFO Queue에서 **병렬 처리**와 **순서 보장**을 동시에 달성하는 방법:

```
┌─────────────────────────────────────────────┐
│                FIFO Queue                    │
├─────────────────────────────────────────────┤
│  Group A: [A1] → [A2] → [A3]  (순서 보장)   │
│  Group B: [B1] → [B2]         (순서 보장)   │
│  Group C: [C1] → [C2] → [C3]  (순서 보장)   │
└─────────────────────────────────────────────┘
           ↓          ↓          ↓
      Consumer1  Consumer2  Consumer3
      (Group A)  (Group B)  (Group C)
```

- 같은 Message Group ID 내에서만 순서 보장
- 다른 그룹은 병렬 처리 가능
- **예**: 사용자 ID를 Group ID로 사용 → 같은 사용자의 주문은 순서대로, 다른 사용자는 병렬 처리

## 주요 개념

### Visibility Timeout (가시성 타임아웃)

Consumer가 메시지를 가져가면, 해당 메시지가 다른 Consumer에게 **일시적으로 보이지 않는 시간**:

```
시간 →
│
│  [메시지 수신]     [처리 완료 & 삭제]
│       ↓                  ↓
│  ┌────┼──────────────────┼────────────────┐
│  │    ████████████████████               │ ← 정상 처리
│  │    ← Visibility Timeout →              │
│  └────────────────────────────────────────┘
│
│  [메시지 수신]     [처리 실패]    [다시 보임]
│       ↓               ↓              ↓
│  ┌────┼───────────────┼──────────────┼────┐
│  │    ███████████████                │    │ ← 타임아웃 후 재처리
│  │    ← Visibility Timeout →    [재수신]  │
│  └────────────────────────────────────────┘
```

**기본값**: 30초
**범위**: 0초 ~ 12시간

```java
// Visibility Timeout 설정
@SqsListener(value = "my-queue",
             messageVisibilitySeconds = 60)  // 60초로 설정
public void handleMessage(String message) {
    // 처리 로직
}
```

**왜 필요한가?**
- Consumer가 메시지 처리 중 실패하면, 다른 Consumer가 재처리할 수 있도록 함
- 처리 시간보다 짧으면: 중복 처리 발생
- 처리 시간보다 길면: 실패한 메시지의 재처리가 지연됨

### Dead Letter Queue (DLQ)

처리 실패한 메시지를 별도 큐로 이동시켜 분석/재처리하는 패턴:

```
┌─────────────┐     처리 실패 (N회)    ┌─────────────┐
│   Source    │ ─────────────────────→ │    DLQ      │
│    Queue    │                        │             │
└─────────────┘                        └─────────────┘
       │                                      │
       ↓                                      ↓
  Consumer가                            관리자가 분석
  정상 처리                             또는 재처리
```

**설정 예시** (AWS Console / CloudFormation):

```yaml
# CloudFormation 예시
Resources:
  MainQueue:
    Type: AWS::SQS::Queue
    Properties:
      QueueName: order-queue
      RedrivePolicy:
        deadLetterTargetArn: !GetAtt DeadLetterQueue.Arn
        maxReceiveCount: 3  # 3번 실패 시 DLQ로 이동

  DeadLetterQueue:
    Type: AWS::SQS::Queue
    Properties:
      QueueName: order-queue-dlq
      MessageRetentionPeriod: 1209600  # 14일 보관
```

**maxReceiveCount**: 메시지가 몇 번 수신된 후 DLQ로 이동할지 설정

### Long Polling vs Short Polling

| 방식 | 동작 | 비용 | 지연 시간 |
|------|------|------|----------|
| **Short Polling** | 즉시 응답 (빈 응답 가능) | 높음 (빈 요청도 비용) | 낮음 |
| **Long Polling** | 메시지 올 때까지 대기 (최대 20초) | 낮음 | 약간 높음 |

```
Short Polling (WaitTimeSeconds = 0)
─────────────────────────────────────────
요청 → [빈 응답]
요청 → [빈 응답]
요청 → [메시지!]

Long Polling (WaitTimeSeconds = 20)
─────────────────────────────────────────
요청 ─────────────────────→ [메시지!]
      (메시지 올 때까지 대기)
```

**권장**: Long Polling 사용 (비용 절감 + 빈 응답 감소)

```java
// Spring Cloud AWS 설정
@Bean
public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
        SqsAsyncClient sqsAsyncClient) {
    return SqsMessageListenerContainerFactory.builder()
            .sqsAsyncClient(sqsAsyncClient)
            .configure(options -> options
                    .maxMessagesPerPoll(10)
                    .pollTimeout(Duration.ofSeconds(20)))  // Long Polling
            .build();
}
```

### Message Attributes

메시지 본문과 별도로 메타데이터를 전달:

```java
// 메시지 전송 시 속성 추가
SendMessageRequest request = SendMessageRequest.builder()
    .queueUrl(queueUrl)
    .messageBody("{\"orderId\": 123}")
    .messageAttributes(Map.of(
        "contentType", MessageAttributeValue.builder()
            .dataType("String")
            .stringValue("application/json")
            .build(),
        "priority", MessageAttributeValue.builder()
            .dataType("Number")
            .stringValue("1")
            .build()
    ))
    .build();
```

## 동작 원리

### 메시지 생명주기

```
┌──────────────────────────────────────────────────────────────────┐
│                        메시지 생명주기                            │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  [1] 전송          [2] 저장           [3] 수신                   │
│  Producer ───────→ SQS Queue ───────→ Consumer                   │
│                    (다중 AZ)          (Visibility Timeout 시작)   │
│                        │                    │                    │
│                        │                    ▼                    │
│                        │              [4] 처리                   │
│                        │                    │                    │
│                        │         ┌──────────┴──────────┐         │
│                        │         ▼                     ▼         │
│                        │    [5a] 삭제            [5b] 타임아웃   │
│                        │    (성공 시)            (실패 시)       │
│                        │         │                     │         │
│                        │         ▼                     ▼         │
│                        │      완료              재시도 또는 DLQ   │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

### 확장성 패턴

```
                    ┌─────────────┐
                    │   SQS       │
                    │   Queue     │
                    └─────────────┘
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
   ┌──────────┐    ┌──────────┐    ┌──────────┐
   │Consumer 1│    │Consumer 2│    │Consumer 3│
   │(EC2/ECS) │    │(Lambda)  │    │(ECS)     │
   └──────────┘    └──────────┘    └──────────┘

   ← 트래픽 증가 시 Consumer 자동 확장 →
```

## 아키텍처 패턴

### 1. 팬아웃 (Fan-out) - SNS + SQS

하나의 메시지를 여러 서비스에 동시 전달:

```
                    ┌─────────────┐
   Producer ──────→ │   SNS       │
                    │   Topic     │
                    └─────────────┘
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
   ┌──────────┐    ┌──────────┐    ┌──────────┐
   │ SQS #1   │    │ SQS #2   │    │ SQS #3   │
   │(알림서비스)│    │(분석서비스)│    │(로그서비스)│
   └──────────┘    └──────────┘    └──────────┘
```

### 2. Lambda 트리거

서버리스 이벤트 처리:

```
┌─────────────┐     트리거      ┌─────────────┐     호출      ┌─────────────┐
│   SQS       │ ──────────────→ │   Lambda    │ ────────────→ │   DynamoDB  │
│   Queue     │                 │   Function  │               │   / RDS     │
└─────────────┘                 └─────────────┘               └─────────────┘
```

```yaml
# SAM 템플릿
Resources:
  OrderProcessor:
    Type: AWS::Serverless::Function
    Properties:
      Handler: handler.process
      Runtime: java17
      Events:
        SQSEvent:
          Type: SQS
          Properties:
            Queue: !GetAtt OrderQueue.Arn
            BatchSize: 10
```

### 3. 비동기 작업 처리

```
┌─────────────┐   ①주문요청    ┌─────────────┐
│   Client    │ ────────────→ │   API       │
└─────────────┘               │   Server    │
       ↑                      └──────┬──────┘
       │                             │②메시지 전송
       │ ⑤응답                       ▼
       │                      ┌─────────────┐
       │                      │   SQS       │
       │                      │   Queue     │
       │                      └──────┬──────┘
       │                             │③폴링
       │                             ▼
       │                      ┌─────────────┐
       └──────────────────────│   Worker    │
                 ④결과 전달    │   Server    │
              (WebSocket/Poll) └─────────────┘
```

### 4. CDC (Change Data Capture) 연동

DB 변경 사항을 감지하여 특정 조건의 INSERT/UPDATE만 SQS로 전송하는 패턴.

#### 방식 1: DynamoDB Streams (가장 간단)

```
┌──────────┐    변경 감지     ┌──────────┐    필터링     ┌──────────┐
│ DynamoDB │ ──────────────→ │ DynamoDB │ ───────────→ │  Lambda  │
│  Table   │                 │ Streams  │              │ (Filter) │
└──────────┘                 └──────────┘              └────┬─────┘
                                                            │
                                                            ▼
                                                      ┌──────────┐
                                                      │   SQS    │
                                                      └──────────┘
```

#### 방식 2: EventBridge Pipes (권장, 코드 없이 필터링)

```
┌──────────┐              ┌─────────────────────────────────┐
│ DynamoDB │              │       EventBridge Pipes         │
│ Streams  │ ───────────→ │  ┌────────┐    ┌─────────────┐ │ ──→ SQS
└──────────┘              │  │ Filter │ →  │ Transformer │ │
                          │  └────────┘    └─────────────┘ │
                          └─────────────────────────────────┘
```

**EventBridge Pipes 필터 예시** (JSON, 코드 없이):

```json
{
  "dynamodb": {
    "NewImage": {
      "status": {
        "S": ["PAID", "SHIPPED"]
      }
    }
  },
  "eventName": ["INSERT", "MODIFY"]
}
```

#### 방식 3: RDS/Aurora + DMS + Kinesis

```
┌──────────┐    CDC Log     ┌──────────┐              ┌──────────┐
│   RDS    │ ─────────────→ │   DMS    │ ───────────→ │ Kinesis  │
│ (MySQL/  │   (binlog)     │  (CDC)   │              │ Streams  │
│ Postgres)│                └──────────┘              └────┬─────┘
└──────────┘                                               │
                                                           ▼
                                                     ┌──────────┐
                                                     │  Lambda  │
                                                     │ (Filter) │
                                                     └────┬─────┘
                                                          │
                                                          ▼
                                                     ┌──────────┐
                                                     │   SQS    │
                                                     └──────────┘
```

#### 방식 4: Debezium + MSK (Kafka)

```
┌──────────┐    binlog     ┌──────────┐              ┌──────────┐
│   RDS    │ ─────────────→│ Debezium │ ───────────→ │   MSK    │
│ (MySQL)  │               │ Connector│              │ (Kafka)  │
└──────────┘               └──────────┘              └────┬─────┘
                                                          │
                                              ┌───────────┴───────────┐
                                              ▼                       ▼
                                        ┌──────────┐           ┌──────────┐
                                        │  Lambda  │           │  다른    │
                                        │  → SQS   │           │ Consumer │
                                        └──────────┘           └──────────┘
```

#### Lambda 필터링 코드 예시

```java
@Service
@RequiredArgsConstructor
public class CdcEventProcessor {

    private final SqsTemplate sqsTemplate;
    private static final Set<String> TARGET_STATUS = Set.of("PAID", "SHIPPED");
    private static final Set<String> TARGET_EVENTS = Set.of("INSERT", "MODIFY");

    public void handleDynamoDBStream(DynamodbEvent event) {
        event.getRecords().stream()
            // INSERT, MODIFY만 처리
            .filter(record -> TARGET_EVENTS.contains(record.getEventName()))
            // 특정 상태만 필터링
            .filter(this::shouldProcess)
            .forEach(this::sendToSqs);
    }

    private boolean shouldProcess(DynamodbStreamRecord record) {
        Map<String, AttributeValue> newImage =
            record.getDynamodb().getNewImage();

        // status가 PAID 또는 SHIPPED인 경우만
        String status = newImage.get("status").getS();
        return TARGET_STATUS.contains(status);
    }

    private void sendToSqs(DynamodbStreamRecord record) {
        OrderEvent event = convertToEvent(record);
        sqsTemplate.send("order-events-queue", event);
    }
}
```

#### CDC 방식 비교

| 방식 | 지원 DB | 복잡도 | 지연 시간 | 비용 |
|------|---------|--------|----------|------|
| **DynamoDB Streams** | DynamoDB | 낮음 | ~수백ms | 낮음 |
| **EventBridge Pipes** | DynamoDB, Kinesis | 낮음 | ~수백ms | 중간 |
| **DMS + Kinesis** | RDS, Aurora | 중간 | ~1-3초 | 중간 |
| **Debezium + MSK** | MySQL, PostgreSQL | 높음 | ~수백ms | 높음 |

#### CDC 방식 선택 가이드

| 상황 | 권장 방식 |
|------|----------|
| DynamoDB 사용 중 | **EventBridge Pipes** (가장 간단) |
| RDS/Aurora + 간단한 요구 | **DMS + Kinesis + Lambda** |
| RDS + 복잡한 필터링/다중 Consumer | **Debezium + MSK** |
| 서버리스 우선 | **EventBridge Pipes** |

## Spring Boot 연동

### 의존성 추가

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.awspring.cloud</groupId>
    <artifactId>spring-cloud-aws-starter-sqs</artifactId>
    <version>3.0.4</version>
</dependency>
```

### 설정

```yaml
# application.yml
spring:
  cloud:
    aws:
      region:
        static: ap-northeast-2
      credentials:
        access-key: ${AWS_ACCESS_KEY}
        secret-key: ${AWS_SECRET_KEY}
      sqs:
        endpoint: http://localhost:4566  # LocalStack (개발용)
```

### Producer

```java
@Service
@RequiredArgsConstructor
public class OrderMessageProducer {

    private final SqsTemplate sqsTemplate;

    public void sendOrder(OrderEvent event) {
        sqsTemplate.send(to -> to
            .queue("order-queue")
            .payload(event)
            .header("eventType", "ORDER_CREATED")
        );
    }

    // FIFO Queue 전송
    public void sendOrderFifo(OrderEvent event) {
        sqsTemplate.send(to -> to
            .queue("order-queue.fifo")
            .payload(event)
            .header(SqsHeaders.SQS_GROUP_ID_HEADER,
                    event.getUserId())  // 사용자별 순서 보장
            .header(SqsHeaders.SQS_DEDUPLICATION_ID_HEADER,
                    event.getOrderId())  // 중복 제거
        );
    }
}
```

### Consumer

```java
@Service
@Slf4j
public class OrderMessageConsumer {

    @SqsListener("order-queue")
    public void handleOrder(OrderEvent event) {
        log.info("주문 처리: {}", event.getOrderId());
        // 비즈니스 로직
    }

    // 메시지 속성과 함께 처리
    @SqsListener("order-queue")
    public void handleOrderWithAttributes(
            @Payload OrderEvent event,
            @Header("eventType") String eventType,
            @Headers Map<String, Object> headers) {
        log.info("이벤트 타입: {}, 주문: {}", eventType, event);
    }

    // 수동 ACK (Visibility Timeout 제어)
    @SqsListener(value = "order-queue",
                 acknowledgementMode = ON_SUCCESS)
    public void handleOrderManualAck(
            OrderEvent event,
            Acknowledgement ack) {
        try {
            processOrder(event);
            ack.acknowledge();  // 성공 시 삭제
        } catch (Exception e) {
            // ACK 안 하면 Visibility Timeout 후 재처리
            throw e;
        }
    }
}
```

### 배치 처리

```java
@SqsListener(value = "order-queue",
             maxConcurrentMessages = 10,
             maxMessagesPerPoll = 10)
public void handleOrderBatch(List<Message<OrderEvent>> messages) {
    messages.forEach(msg -> {
        try {
            processOrder(msg.getPayload());
        } catch (Exception e) {
            log.error("처리 실패: {}", msg.getPayload(), e);
        }
    });
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 완전 관리형 - 운영 부담 없음 | Kafka 대비 낮은 처리량 |
| 무제한 확장 (Standard) | FIFO Queue 처리량 제한 |
| 서버리스 통합 (Lambda) | 메시지 크기 제한 (256KB) |
| 사용한 만큼 비용 | 복잡한 라우팅 불가 (RabbitMQ 대비) |
| 높은 가용성 (다중 AZ) | Long Polling 최대 20초 |

### SQS vs 다른 메시지 큐

| 상황 | 권장 |
|------|------|
| AWS 서버리스 아키텍처 | **SQS** |
| 간단한 작업 큐 | **SQS** |
| 이벤트 스트리밍, 로그 | Kafka / Kinesis |
| 복잡한 라우팅 | RabbitMQ |
| 순서 보장 + 높은 처리량 | Kafka |
| 온프레미스 | RabbitMQ / Kafka |

### SQS vs DB 기반 작업 큐

"DB 테이블에 작업을 쌓아두고 Worker가 가져가면 되지 않나?"라는 질문을 자주 받는다. 겉보기엔 비슷해 보이지만 **규모가 커질수록 차이가 명확**해진다.

#### DB 기반 작업 큐 방식

```
┌──────────┐    INSERT     ┌──────────┐    SELECT FOR UPDATE    ┌──────────┐
│ Producer │ ────────────→ │   DB     │ ←───────────────────── │ Worker   │
│          │               │ (Queue)  │                         │ (Poll)   │
└──────────┘               └──────────┘                         └──────────┘
                                │
                          UPDATE status
                          또는 DELETE
```

```sql
-- Worker가 작업 가져가기
SELECT * FROM task_queue
WHERE status = 'PENDING'
ORDER BY created_at
LIMIT 10
FOR UPDATE SKIP LOCKED;  -- 동시성 제어

UPDATE task_queue SET status = 'PROCESSING' WHERE id IN (...);
```

#### 비교

| 항목 | DB 기반 큐 | SQS |
|------|-----------|-----|
| **Polling** | 지속적 SELECT 쿼리 (DB 부하) | Long Polling (효율적) |
| **동시성 제어** | `FOR UPDATE` 락 직접 구현 | Visibility Timeout 자동 |
| **확장 한계** | DB 커넥션 풀이 병목 | 무제한 (Standard) |
| **장애 분리** | 큐 장애 = DB 장애 | 독립적 |
| **운영** | 락 경쟁, 데드락 모니터링 | 관리형 |

#### DB 방식의 실제 문제

```
┌─────────────────────────────────────────────────────────┐
│  Worker 10개가 동시에 Polling                           │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Worker1 → SELECT FOR UPDATE ─┐                       │
│   Worker2 → SELECT FOR UPDATE ──┼→ 락 경쟁!            │
│   Worker3 → SELECT FOR UPDATE ─┘   (대기 발생)         │
│   ...                                                   │
│                                                         │
│   초당 10번 × 10 Worker = 100 쿼리/초 (빈 결과 포함)   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**SQS는 이 문제를 해결:**
- Visibility Timeout으로 자동 락
- Long Polling으로 빈 요청 없음
- Worker가 100개여도 DB 부하 없음

#### 언제 DB 큐가 괜찮은가?

| 상황 | 권장 |
|------|------|
| 소규모 (초당 ~100건) | DB 큐도 OK |
| 트랜잭션 일관성 필수 (Outbox 패턴) | DB 큐 |
| 대규모 (초당 1000건+) | **SQS/Kafka** |
| DB 부하 분리 필요 | **SQS/Kafka** |
| 서버리스 연동 (Lambda) | **SQS** |

#### 핵심 차이

```
DB 기반: "범용 도구로 큐 흉내"
         └→ 락, 인덱스, 커넥션 풀 직접 관리

SQS:     "큐 전용 시스템"
         └→ 메시지 전달에 최적화된 인프라
```

**결론**: 작은 규모에선 DB 큐도 충분하지만, 트래픽이 늘면 **DB가 병목**이 된다. SQS는 "큐 전용"으로 설계되어 그 한계가 훨씬 높다.

## 트러블슈팅

### 사례 1: 메시지 중복 처리

#### 증상
Standard Queue에서 같은 주문이 2번 처리되어 중복 결제 발생

#### 원인 분석
Standard Queue는 At-least-once 전달을 보장하여 드물게 중복 전달 발생

#### 해결 방법

```java
// 1. FIFO Queue 사용 (Exactly-once)
@SqsListener("order-queue.fifo")
public void handleOrder(OrderEvent event) {
    // 중복 없이 처리
}

// 2. 멱등성 키 사용 (Standard Queue)
@SqsListener("order-queue")
public void handleOrder(OrderEvent event) {
    String idempotencyKey = event.getOrderId();
    if (idempotencyStore.exists(idempotencyKey)) {
        log.info("이미 처리된 주문: {}", idempotencyKey);
        return;
    }

    processOrder(event);
    idempotencyStore.save(idempotencyKey, Duration.ofDays(1));
}
```

#### 예방 조치
- 결제, 주문 등 중요 처리는 FIFO Queue 또는 멱등성 보장 필수
- 멱등성 키 저장소(Redis)의 TTL은 Message Retention Period보다 길게 설정

### 사례 2: Visibility Timeout 초과

#### 증상
처리 중인 메시지가 다른 Consumer에게 재전달되어 중복 처리

#### 원인 분석
처리 시간(5분)이 Visibility Timeout(30초 기본값)보다 긺

#### 해결 방법

```java
// 1. Visibility Timeout 늘리기
@SqsListener(value = "order-queue",
             messageVisibilitySeconds = 600)  // 10분
public void handleLongProcess(OrderEvent event) {
    // 긴 처리 로직
}

// 2. 동적으로 타임아웃 연장
@SqsListener("order-queue")
public void handleOrder(
        OrderEvent event,
        @Header(SqsHeaders.SQS_RECEIPT_HANDLE_HEADER) String receiptHandle,
        SqsAsyncClient sqsClient) {

    // 처리 중 타임아웃 연장
    sqsClient.changeMessageVisibility(req -> req
        .queueUrl("order-queue")
        .receiptHandle(receiptHandle)
        .visibilityTimeout(300));  // 5분 연장

    processOrder(event);
}
```

### 사례 3: DLQ 메시지 급증

#### 증상
DLQ에 메시지가 계속 쌓임

#### 원인 분석
외부 API 장애로 모든 메시지 처리 실패

#### 해결 방법

```java
// 1. 재시도 로직 추가
@SqsListener("order-queue")
@Retryable(maxAttempts = 3,
           backoff = @Backoff(delay = 1000, multiplier = 2))
public void handleOrder(OrderEvent event) {
    externalApiClient.call(event);  // 실패 시 재시도
}

// 2. DLQ 모니터링 및 알림
@SqsListener("order-queue-dlq")
public void handleDlq(OrderEvent event,
                      @Headers Map<String, Object> headers) {
    int receiveCount = (int) headers.get("ApproximateReceiveCount");

    // 알림 발송
    alertService.sendAlert(
        "DLQ 메시지 발생",
        "주문 ID: " + event.getOrderId() +
        ", 시도 횟수: " + receiveCount
    );

    // 수동 처리 큐에 저장
    manualProcessQueue.add(event);
}
```

## 면접 예상 질문

### Q: SQS Standard Queue와 FIFO Queue의 차이점은?

A: **Standard Queue**는 무제한 처리량과 At-least-once 전달을 제공하며, 메시지 순서가 보장되지 않고 드물게 중복 전달될 수 있습니다. 로그 처리, 알림 등 순서와 중복이 크리티컬하지 않은 경우에 적합합니다.

**FIFO Queue**는 초당 3,000개(배치 시 30,000개) 처리량 제한이 있지만, 엄격한 순서 보장과 Exactly-once 전달을 보장합니다. 주문 처리, 결제 등 순서와 중복 방지가 중요한 경우 사용합니다.

### Q: Visibility Timeout이란 무엇이고, 왜 필요한가?

A: **Visibility Timeout**은 Consumer가 메시지를 가져간 후 다른 Consumer에게 해당 메시지가 보이지 않는 시간입니다.

**필요한 이유**: Consumer가 메시지 처리 중 실패하거나 크래시될 경우, Visibility Timeout이 지나면 메시지가 다시 보이게 되어 다른 Consumer가 재처리할 수 있습니다. 이를 통해 메시지 손실 없이 안정적인 처리를 보장합니다.

타임아웃이 처리 시간보다 짧으면 중복 처리가 발생하고, 너무 길면 실패한 메시지의 재처리가 지연됩니다.

### Q: SQS를 사용할 때 메시지 중복 처리를 어떻게 방지하나?

A: 세 가지 방법이 있습니다:

1. **FIFO Queue 사용**: Exactly-once 전달 보장으로 중복 원천 차단
2. **멱등성 키 구현**: 각 메시지에 고유 ID를 부여하고 처리 전 중복 체크 (Redis 등 활용)
3. **데이터베이스 유니크 제약**: 처리 결과를 저장할 때 유니크 키로 중복 INSERT 방지

실무에서는 FIFO Queue 사용이 어려운 경우 멱등성 키 패턴을 가장 많이 사용합니다.

### Q: DLQ(Dead Letter Queue)는 어떤 상황에서 사용하나?

A: **DLQ**는 여러 번 처리 실패한 메시지를 별도 큐로 이동시켜 분석하고 수동 처리할 수 있게 합니다.

**사용 상황**:
- 잘못된 메시지 형식으로 파싱 실패
- 외부 API 장애로 처리 불가
- 비즈니스 로직 예외 (데이터 불일치 등)

maxReceiveCount를 설정하여 N번 실패 시 DLQ로 이동하고, DLQ 모니터링 알림을 통해 빠르게 대응합니다.

### Q: DB 테이블로 작업 큐를 구현하면 안 되나? SQS와 차이점은?

A: 소규모에선 DB 기반 큐도 가능하지만, 규모가 커지면 차이가 명확해집니다.

**DB 기반 문제점**:
- 지속적인 Polling으로 DB 부하 증가
- `FOR UPDATE` 락 경쟁으로 동시성 이슈
- DB 커넥션 풀이 병목
- 큐 장애가 곧 DB 장애

**SQS 장점**:
- Long Polling으로 빈 요청 없음
- Visibility Timeout으로 자동 동시성 제어
- DB와 독립적으로 무제한 확장
- 큐 전용 시스템으로 최적화

**결론**: 초당 100건 이하의 소규모는 DB 큐도 괜찮지만, 대규모나 DB 부하 분리가 필요하면 SQS가 적합합니다. Outbox 패턴처럼 트랜잭션 일관성이 필수인 경우에만 DB 큐를 고려합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [메시지 큐](./message-queue.md) | 선수 지식 - 메시지 큐 기본 개념 | 심화 |
| [Cloud Computing](./cloud-computing.md) | 선수 지식 - AWS 클라우드 기초 | 기초 |
| [Serverless](./serverless.md) | Lambda + SQS 연동 | 중급 |
| [Event-Driven Architecture](./event-driven-architecture.md) | SQS 기반 이벤트 드리븐 | 중급 |
| [멱등성](./idempotency.md) | 중복 처리 방지 패턴 | 중급 |

## 참고 자료

- [AWS SQS 공식 문서](https://docs.aws.amazon.com/sqs/)
- [Spring Cloud AWS SQS](https://docs.awspring.io/spring-cloud-aws/docs/current/reference/html/index.html#sqs-integration)
- [AWS SQS Best Practices](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-best-practices.html)
