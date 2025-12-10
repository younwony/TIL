# Message Queue (메시지 큐)

> 서비스 간 비동기 통신을 위해 메시지를 임시 저장하고 전달하는 미들웨어

## 핵심 개념

- **비동기 통신**: Producer와 Consumer가 직접 연결되지 않고 큐를 통해 메시지 전달
- **느슨한 결합(Loose Coupling)**: 서비스 간 의존성을 줄여 독립적인 확장과 배포 가능
- **버퍼링**: 트래픽 급증 시 메시지를 큐에 저장하여 Consumer가 처리 가능한 속도로 소비
- **신뢰성**: 메시지 영속화와 재시도 메커니즘으로 메시지 손실 방지
- **확장성**: Consumer를 수평 확장하여 처리량 증가 가능

## 메시지 큐 아키텍처

```
┌──────────────┐     ┌─────────────────┐     ┌──────────────┐
│   Producer   │────▶│  Message Queue  │────▶│   Consumer   │
│   (생산자)    │     │   (메시지 큐)    │     │   (소비자)    │
└──────────────┘     └─────────────────┘     └──────────────┘
                            │
                     ┌──────┴──────┐
                     │  Exchange/  │
                     │   Topic     │
                     └─────────────┘
```

## 메시지 전달 보장 방식

| 방식 | 설명 | 사용 사례 |
|------|------|----------|
| **At-most-once** | 최대 한 번 전달, 손실 가능 | 로그 수집, 모니터링 |
| **At-least-once** | 최소 한 번 전달, 중복 가능 | 일반적인 비즈니스 로직 |
| **Exactly-once** | 정확히 한 번 전달 | 결제, 금융 거래 |

## 주요 메시지 큐 비교

### RabbitMQ vs Kafka vs AWS SQS

| 특성 | RabbitMQ | Kafka | AWS SQS |
|------|----------|-------|---------|
| **타입** | Message Broker | Event Streaming | Managed Queue |
| **프로토콜** | AMQP, MQTT, STOMP | 자체 프로토콜 | HTTP/HTTPS |
| **메시지 저장** | 소비 후 삭제 | 로그 기반 영구 저장 | 소비 후 삭제 |
| **처리량** | 중간 (~10K/s) | 높음 (~100K/s+) | 중간 |
| **순서 보장** | 큐 단위 보장 | 파티션 단위 보장 | FIFO 큐 사용 시 |
| **사용 사례** | 작업 큐, RPC | 이벤트 스트리밍, 로그 | 서버리스, 간단한 큐 |

---

## RabbitMQ

### 개요

AMQP(Advanced Message Queuing Protocol) 기반의 오픈소스 메시지 브로커

### 핵심 구성 요소

```
┌──────────┐    ┌──────────┐    ┌─────────┐    ┌──────────┐
│ Producer │───▶│ Exchange │───▶│  Queue  │───▶│ Consumer │
└──────────┘    └──────────┘    └─────────┘    └──────────┘
                     │
              Binding(Routing Key)
```

- **Exchange**: 메시지를 받아 라우팅 규칙에 따라 큐에 분배
- **Queue**: 메시지가 저장되는 버퍼
- **Binding**: Exchange와 Queue를 연결하는 규칙
- **Routing Key**: 메시지 라우팅에 사용되는 키

### Exchange 타입

| 타입 | 설명 | 사용 사례 |
|------|------|----------|
| **Direct** | Routing Key가 정확히 일치하는 큐에 전달 | 특정 작업 큐 |
| **Fanout** | 바인딩된 모든 큐에 브로드캐스트 | 알림, 로그 |
| **Topic** | 패턴 매칭으로 큐에 전달 (*, #) | 다중 구독 |
| **Headers** | 헤더 속성 기반 라우팅 | 복잡한 라우팅 |

### 예제 코드 (Spring AMQP)

```java
// Producer
@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(
            "order.exchange",    // exchange
            "order.created",     // routing key
            order                // message
        );
    }
}

// Consumer
@Service
public class OrderConsumer {

    @RabbitListener(queues = "order.queue")
    public void handleOrder(Order order) {
        // 주문 처리 로직
        processOrder(order);
    }
}

// Configuration
@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable("order.queue")
            .withArgument("x-dead-letter-exchange", "dlx.exchange")
            .build();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
            .to(orderExchange)
            .with("order.created");
    }
}
```

### RabbitMQ 특징

- **유연한 라우팅**: 다양한 Exchange 타입으로 복잡한 라우팅 가능
- **메시지 확인(Acknowledgment)**: Consumer가 처리 완료를 확인
- **Dead Letter Queue**: 실패한 메시지를 별도 큐로 이동
- **관리 UI**: 웹 기반 모니터링 및 관리 도구

---

## Apache Kafka

### 개요

분산 이벤트 스트리밍 플랫폼. 대용량 실시간 데이터 처리에 최적화

### 핵심 구성 요소

```
┌──────────────────────────────────────────────────────┐
│                    Kafka Cluster                      │
│  ┌─────────────────────────────────────────────────┐ │
│  │                    Topic                         │ │
│  │  ┌───────────┐ ┌───────────┐ ┌───────────┐     │ │
│  │  │Partition 0│ │Partition 1│ │Partition 2│     │ │
│  │  │  [0,1,2]  │ │  [0,1,2]  │ │  [0,1,2]  │     │ │
│  │  └───────────┘ └───────────┘ └───────────┘     │ │
│  └─────────────────────────────────────────────────┘ │
│                                                       │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐              │
│  │Broker 1 │  │Broker 2 │  │Broker 3 │              │
│  └─────────┘  └─────────┘  └─────────┘              │
└──────────────────────────────────────────────────────┘
         ▲                              │
         │                              ▼
    ┌─────────┐                   ┌──────────────┐
    │Producer │                   │Consumer Group│
    └─────────┘                   │  ┌────────┐  │
                                  │  │Consumer│  │
                                  │  │Consumer│  │
                                  │  └────────┘  │
                                  └──────────────┘
```

- **Topic**: 메시지를 카테고리별로 분류하는 단위
- **Partition**: Topic을 분할하여 병렬 처리 가능하게 함
- **Broker**: Kafka 서버 인스턴스
- **Consumer Group**: 같은 그룹의 Consumer들이 파티션을 나눠서 소비
- **Offset**: 파티션 내 메시지의 고유 위치

### Kafka의 메시지 저장 방식

```
Partition 0:
┌───┬───┬───┬───┬───┬───┬───┬───┐
│ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │  ← Offset
└───┴───┴───┴───┴───┴───┴───┴───┘
              ▲           ▲
              │           │
        Consumer A    Consumer B
         (offset 3)   (offset 6)
```

- 메시지는 추가만 가능(Append-only Log)
- 소비해도 메시지가 삭제되지 않음
- Retention 정책에 따라 일정 기간/용량 후 삭제

### 예제 코드 (Spring Kafka)

```java
// Producer
@Service
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event)
            .addCallback(
                result -> log.info("Sent: {}", result.getRecordMetadata()),
                ex -> log.error("Failed to send", ex)
            );
    }

    // 특정 파티션으로 전송
    public void sendToPartition(String topic, int partition, Object event) {
        kafkaTemplate.send(topic, partition, null, event);
    }
}

// Consumer
@Service
public class EventConsumer {

    @KafkaListener(
        topics = "order-events",
        groupId = "order-service",
        concurrency = "3"  // 3개의 Consumer 스레드
    )
    public void consume(
        @Payload OrderEvent event,
        @Header(KafkaHeaders.OFFSET) Long offset,
        @Header(KafkaHeaders.PARTITION) Integer partition
    ) {
        log.info("Received from partition {} at offset {}: {}",
            partition, offset, event);
        processEvent(event);
    }
}

// Configuration
@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");  // 모든 복제본 확인
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "order-service");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }
}
```

### Kafka 특징

- **높은 처리량**: 배치 처리, Zero-copy로 대용량 데이터 처리
- **내구성**: 복제(Replication)로 데이터 손실 방지
- **확장성**: 파티션 추가로 수평 확장
- **메시지 재처리**: Offset 기반으로 과거 메시지 다시 소비 가능

---

## 사용 사례별 선택 가이드

### RabbitMQ가 적합한 경우

- 복잡한 라우팅 로직이 필요한 경우
- 메시지 우선순위가 필요한 경우
- 요청-응답(RPC) 패턴 구현
- 메시지 단위의 ACK가 중요한 경우

### Kafka가 적합한 경우

- 대용량 실시간 데이터 스트리밍
- 이벤트 소싱(Event Sourcing) 패턴
- 로그 수집 및 분석 파이프라인
- 메시지 재처리가 필요한 경우
- 여러 Consumer가 같은 메시지를 소비해야 하는 경우

### AWS SQS가 적합한 경우

- AWS 환경에서 간단한 큐가 필요한 경우
- 운영 부담 없이 관리형 서비스 사용
- Lambda와 연동한 서버리스 아키텍처

---

## 메시지 큐 설계 시 고려사항

### 1. 멱등성(Idempotency)

At-least-once 전달로 인한 중복 메시지 처리 대비

```java
@Service
public class IdempotentConsumer {

    private final ProcessedMessageRepository repository;

    @Transactional
    public void handleMessage(Message message) {
        String messageId = message.getId();

        // 이미 처리된 메시지인지 확인
        if (repository.existsById(messageId)) {
            log.info("Duplicate message ignored: {}", messageId);
            return;
        }

        // 비즈니스 로직 처리
        processMessage(message);

        // 처리 완료 기록
        repository.save(new ProcessedMessage(messageId));
    }
}
```

### 2. 실패 처리 (Dead Letter Queue)

```java
// 재시도 후에도 실패하면 DLQ로 이동
@KafkaListener(topics = "orders")
@RetryableTopic(
    attempts = "3",
    backoff = @Backoff(delay = 1000, multiplier = 2),
    dltTopicSuffix = ".DLT"
)
public void processOrder(Order order) {
    // 처리 로직
}

@DltHandler
public void handleDlt(Order order) {
    log.error("Failed to process order after retries: {}", order);
    alertService.notifyFailure(order);
}
```

### 3. 순서 보장

```java
// Kafka: 같은 키의 메시지는 같은 파티션으로
kafkaTemplate.send("orders", order.getUserId(), order);

// RabbitMQ: 단일 큐 + 단일 Consumer로 순서 보장
@RabbitListener(queues = "sequential.queue", concurrency = "1")
public void handleSequentially(Message message) {
    // 순서대로 처리
}
```

## 면접 예상 질문

- **Q: Kafka와 RabbitMQ의 차이점은 무엇인가요?**
  - A: Kafka는 분산 로그 기반 스트리밍 플랫폼으로 대용량 처리와 메시지 재처리에 강점이 있고, RabbitMQ는 AMQP 기반 메시지 브로커로 복잡한 라우팅과 메시지 단위 ACK에 강점이 있습니다. Kafka는 Consumer가 Pull 방식으로 메시지를 가져가고, RabbitMQ는 Push 방식입니다.

- **Q: Consumer Group은 무엇이고 왜 필요한가요?**
  - A: Consumer Group은 Kafka에서 같은 토픽을 구독하는 Consumer들의 논리적 그룹입니다. 그룹 내 Consumer들은 파티션을 분담하여 병렬 처리하고, 그룹 간에는 같은 메시지를 독립적으로 소비합니다. 이를 통해 확장성과 내결함성을 제공합니다.

- **Q: 메시지 큐에서 메시지 순서를 보장하려면 어떻게 해야 하나요?**
  - A: Kafka에서는 같은 파티션 키를 사용하여 관련 메시지를 같은 파티션으로 보내면 순서가 보장됩니다. RabbitMQ에서는 단일 큐와 단일 Consumer를 사용하거나, Message Sequencing 패턴을 적용합니다. 단, 순서 보장은 처리량과 트레이드오프가 있습니다.

## 참고 자료

- [RabbitMQ 공식 문서](https://www.rabbitmq.com/documentation.html)
- [Apache Kafka 공식 문서](https://kafka.apache.org/documentation/)
- [AWS SQS 개발자 가이드](https://docs.aws.amazon.com/sqs/)
- Martin Kleppmann, "Designing Data-Intensive Applications"
