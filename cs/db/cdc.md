# CDC (Change Data Capture)

> `[4] 심화` · 선수 지식: [Transaction](./transaction.md), [레플리케이션](./replication.md)

> 데이터베이스의 변경 사항(INSERT, UPDATE, DELETE)을 실시간으로 감지하고 캡처하여 다른 시스템에 전달하는 기술

`#CDC` `#ChangeDataCapture` `#변경데이터캡처` `#Debezium` `#DMS` `#AWSDatabaseMigrationService` `#binlog` `#WAL` `#트랜잭션로그` `#TransactionLog` `#이벤트소싱` `#EventSourcing` `#이벤트드리븐` `#EventDriven` `#데이터동기화` `#DataSync` `#ETL` `#데이터파이프라인` `#Kafka` `#KafkaConnect` `#OutboxPattern` `#실시간스트리밍` `#Replication` `#MySQL` `#PostgreSQL` `#DynamoDBStreams` `#EventBridge`

## 왜 알아야 하는가?

- **실무**: MSA 환경에서 서비스 간 데이터 동기화, 실시간 분석 파이프라인 구축의 핵심
- **면접**: "CDC vs Polling 차이점", "Outbox 패턴과 CDC" 등 분산 시스템 설계 필수 질문
- **기반 지식**: 이벤트 드리븐 아키텍처, 데이터 파이프라인, CQRS의 핵심 기술

## 핵심 개념

- **변경 감지**: 트랜잭션 로그(binlog, WAL)를 읽어 데이터 변경 사항을 실시간 캡처
- **비침투적(Non-invasive)**: 애플리케이션 코드 수정 없이 DB 레벨에서 변경 감지
- **순서 보장**: 트랜잭션 로그 순서대로 이벤트 전달
- **최소 지연**: Polling 방식 대비 밀리초 단위 지연

## 쉽게 이해하기

### CDC = CCTV 녹화 시스템

**Polling 방식 (주기적 조회)**
경비원이 5분마다 건물을 순찰하며 변화를 체크합니다. 순찰 사이에 일어난 일은 놓칠 수 있고, 아무 일 없어도 계속 순찰해야 합니다.

**CDC 방식 (실시간 감지)**
CCTV가 24시간 녹화하며 움직임이 감지되면 즉시 알림을 보냅니다. 모든 변화가 기록되고, 변화가 있을 때만 알림이 갑니다.

| 비유 | CDC 개념 |
|------|----------|
| CCTV | CDC 시스템 |
| 녹화 테이프 | 트랜잭션 로그 (binlog/WAL) |
| 움직임 감지 | 변경 이벤트 캡처 |
| 알림 수신자 | Consumer (Kafka, SQS 등) |

### 트랜잭션 로그 = 은행 거래 내역서

```
┌────────────────────────────────────────────┐
│            트랜잭션 로그 (binlog)            │
├────────────────────────────────────────────┤
│  10:01:23  INSERT orders (id=1001, ...)    │
│  10:01:24  UPDATE orders SET status='PAID' │
│  10:01:25  INSERT payments (...)           │
│  10:01:26  UPDATE inventory SET qty=99     │
│  ...                                       │
└────────────────────────────────────────────┘
         │
         ▼  CDC가 로그를 읽어서
┌────────────────────────────────────────────┐
│  { "op": "INSERT", "table": "orders", ...} │
│  { "op": "UPDATE", "table": "orders", ...} │
│  → Kafka/SQS로 전달                         │
└────────────────────────────────────────────┘
```

## CDC vs Polling

### 비교

```
Polling 방식
─────────────────────────────────────────────────────────
              5분        5분        5분
  ┌───────────┼──────────┼──────────┼──────────┐
  │  [조회]      [조회]      [조회]      [조회]  │
  │    ↓          ↓          ↓          ↓      │
  │  변경없음   변경있음!   변경없음   변경있음! │
  └───────────────────────────────────────────────────────┘
  문제: 빈 조회도 DB 부하, 조회 간격 동안 변경 감지 불가

CDC 방식
─────────────────────────────────────────────────────────
  ┌───────────────────────────────────────────────────────┐
  │  DB 로그를 실시간 모니터링                              │
  │                                                        │
  │  [변경 발생] ──→ [즉시 캡처] ──→ [이벤트 전달]         │
  │  [변경 발생] ──→ [즉시 캡처] ──→ [이벤트 전달]         │
  └───────────────────────────────────────────────────────┘
  장점: DB 부하 최소, 실시간 감지, 모든 변경 캡처
```

### 상세 비교표

| 항목 | Polling | CDC |
|------|---------|-----|
| **지연 시간** | 폴링 주기 (초~분) | 밀리초 단위 |
| **DB 부하** | 높음 (반복 쿼리) | 낮음 (로그 읽기) |
| **변경 누락** | 가능 (주기 사이) | 없음 (로그 기반) |
| **삭제 감지** | 어려움 | 쉬움 |
| **이전 값** | 불가능 | 가능 (before/after) |
| **구현 복잡도** | 낮음 | 중간~높음 |
| **인프라** | 없음 | CDC 시스템 필요 |

## CDC 구현 방식

### 1. 로그 기반 CDC (Log-based)

트랜잭션 로그를 직접 읽어 변경 사항 캡처. 가장 효율적이고 정확한 방식.

```
┌──────────────┐     binlog/WAL      ┌──────────────┐
│   Database   │ ──────────────────→ │  CDC Tool    │
│              │                     │ (Debezium)   │
└──────────────┘                     └──────┬───────┘
                                            │
                                            ▼
                                     ┌──────────────┐
                                     │    Kafka     │
                                     └──────────────┘
```

**지원 로그 형식:**

| DB | 로그 형식 | 설정 |
|-----|----------|------|
| MySQL | Binary Log (binlog) | `binlog_format=ROW` |
| PostgreSQL | Write-Ahead Log (WAL) | `wal_level=logical` |
| Oracle | Redo Log | LogMiner |
| SQL Server | Transaction Log | CT/CDC 활성화 |

### 2. 트리거 기반 CDC (Trigger-based)

DB 트리거로 변경 시 별도 테이블에 기록.

```sql
-- 변경 이력 테이블
CREATE TABLE order_changes (
    id BIGINT AUTO_INCREMENT,
    operation VARCHAR(10),  -- INSERT/UPDATE/DELETE
    order_id BIGINT,
    old_data JSON,
    new_data JSON,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- UPDATE 트리거
CREATE TRIGGER orders_update_trigger
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    INSERT INTO order_changes (operation, order_id, old_data, new_data)
    VALUES ('UPDATE', NEW.id,
            JSON_OBJECT('status', OLD.status),
            JSON_OBJECT('status', NEW.status));
END;
```

**단점**: DB 성능 저하, 트리거 관리 복잡

### 3. 타임스탬프 기반 CDC (Timestamp-based)

`updated_at` 컬럼으로 변경된 레코드 조회 (Polling의 일종).

```sql
-- 마지막 동기화 이후 변경된 데이터 조회
SELECT * FROM orders
WHERE updated_at > :last_sync_time
ORDER BY updated_at;
```

**단점**: DELETE 감지 불가, 정확한 순서 보장 어려움

## 주요 CDC 도구

### 1. Debezium (오픈소스, 가장 인기)

```
┌──────────┐    binlog     ┌──────────┐              ┌──────────┐
│  MySQL   │ ────────────→ │ Debezium │ ───────────→ │  Kafka   │
│          │               │ Connector│              │          │
└──────────┘               └──────────┘              └──────────┘
```

**Debezium 이벤트 형식:**

```json
{
  "before": {
    "id": 1001,
    "status": "PENDING",
    "amount": 50000
  },
  "after": {
    "id": 1001,
    "status": "PAID",
    "amount": 50000
  },
  "source": {
    "connector": "mysql",
    "db": "ecommerce",
    "table": "orders",
    "ts_ms": 1705123456789
  },
  "op": "u",  // c=create, u=update, d=delete, r=read(snapshot)
  "ts_ms": 1705123456800
}
```

**Debezium 설정 예시:**

```json
{
  "name": "mysql-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "dbz",
    "database.server.id": "184054",
    "database.server.name": "ecommerce",
    "database.include.list": "ecommerce",
    "table.include.list": "ecommerce.orders,ecommerce.payments",
    "include.schema.changes": "true"
  }
}
```

### 2. AWS DMS (Database Migration Service)

```
┌──────────┐              ┌──────────┐              ┌──────────┐
│   RDS    │ ───────────→ │   DMS    │ ───────────→ │ Kinesis  │
│ (Source) │              │   Task   │              │ (Target) │
└──────────┘              └──────────┘              └──────────┘
```

**특징:**
- 완전 관리형
- 다양한 소스/타겟 지원
- 마이그레이션 + CDC 동시 지원

### 3. DynamoDB Streams

```
┌──────────┐    자동 활성화   ┌──────────┐
│ DynamoDB │ ─────────────→ │ DynamoDB │
│  Table   │                │ Streams  │
└──────────┘                └────┬─────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
              ┌──────────┐             ┌──────────┐
              │  Lambda  │             │ Kinesis  │
              │ Trigger  │             │ Consumer │
              └──────────┘             └──────────┘
```

### 4. Maxwell (MySQL 전용, 경량)

```bash
# 간단한 실행
maxwell --user='maxwell' --password='xxx' \
        --host='127.0.0.1' \
        --producer=kafka \
        --kafka.bootstrap.servers='kafka:9092'
```

### 도구 비교

| 도구 | 지원 DB | 타겟 | 복잡도 | 관리 |
|------|---------|------|--------|------|
| **Debezium** | MySQL, PostgreSQL, MongoDB, Oracle, SQL Server | Kafka | 중간 | 자체 |
| **AWS DMS** | RDS, Aurora, 온프레미스 | Kinesis, S3, Redshift | 낮음 | AWS |
| **Maxwell** | MySQL | Kafka, Kinesis, SQS | 낮음 | 자체 |
| **DynamoDB Streams** | DynamoDB | Lambda, Kinesis | 낮음 | AWS |

## 아키텍처 패턴

### 1. 이벤트 기반 동기화

서비스 간 데이터 일관성 유지:

```
┌──────────────┐                    ┌──────────────┐
│ Order Service│                    │ Search Service│
│   (MySQL)    │                    │  (ES Index)  │
└──────┬───────┘                    └──────▲───────┘
       │                                   │
       │ binlog                            │ 인덱싱
       ▼                                   │
┌──────────────┐    CDC Event       ┌──────────────┐
│   Debezium   │ ────────────────→ │    Kafka     │
│              │                    │              │
└──────────────┘                    └──────────────┘
```

### 2. Outbox 패턴 + CDC

트랜잭션 일관성과 이벤트 발행을 동시에 보장:

```
┌────────────────────────────────────────────────────┐
│                    Order Service                    │
│                                                     │
│  ┌─────────────┐      ┌─────────────┐              │
│  │   Orders    │      │   Outbox    │              │
│  │   Table     │      │   Table     │              │
│  └─────────────┘      └──────┬──────┘              │
│                              │                     │
│  BEGIN TRANSACTION           │                     │
│    INSERT orders(...)        │                     │
│    INSERT outbox(event)  ────┘                     │
│  COMMIT                                            │
└────────────────────────────────────────────────────┘
                               │
                               │ CDC (Debezium)
                               ▼
                        ┌──────────────┐
                        │    Kafka     │
                        └──────────────┘
```

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        // 1. 주문 저장
        Order order = orderRepository.save(new Order(request));

        // 2. Outbox에 이벤트 저장 (같은 트랜잭션)
        OutboxEvent event = OutboxEvent.builder()
            .aggregateType("Order")
            .aggregateId(order.getId())
            .eventType("OrderCreated")
            .payload(toJson(order))
            .build();
        outboxRepository.save(event);

        // 3. CDC가 Outbox 테이블 변경을 감지하여 Kafka로 전달
        return order;
    }
}
```

### 3. CQRS (Command Query Responsibility Segregation)

```
                    ┌───────────────────────────────────────┐
                    │              API Gateway               │
                    └───────────────┬───────────────────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
             ┌──────────────┐               ┌──────────────┐
             │   Command    │               │    Query     │
             │   Service    │               │   Service    │
             │   (Write)    │               │   (Read)     │
             └──────┬───────┘               └──────▲───────┘
                    │                              │
                    ▼                              │
             ┌──────────────┐               ┌──────────────┐
             │    MySQL     │               │Elasticsearch │
             │   (Master)   │               │   (읽기용)    │
             └──────┬───────┘               └──────────────┘
                    │                              ▲
                    │ CDC                          │ 동기화
                    └──────────────────────────────┘
```

### 4. 실시간 분석 파이프라인

```
┌──────────────┐    CDC      ┌──────────────┐    Stream     ┌──────────────┐
│  Production  │ ─────────→ │    Kafka     │ ───────────→ │    Flink     │
│     DB       │            │              │               │  (실시간 처리) │
└──────────────┘            └──────────────┘               └──────┬───────┘
                                                                  │
                                                   ┌──────────────┴──────────────┐
                                                   ▼                             ▼
                                            ┌──────────────┐              ┌──────────────┐
                                            │   Redshift   │              │    Redis     │
                                            │   (분석용)   │              │   (실시간)   │
                                            └──────────────┘              └──────────────┘
```

## CDC + AWS SQS 연동

### EventBridge Pipes (권장)

코드 없이 필터링 + 변환 + SQS 전달:

```
┌──────────────┐              ┌─────────────────────────────────┐
│  DynamoDB    │              │       EventBridge Pipes         │
│   Streams    │ ───────────→ │  ┌────────┐    ┌─────────────┐ │ ──→ SQS
└──────────────┘              │  │ Filter │ →  │ Transformer │ │
                              │  └────────┘    └─────────────┘ │
                              └─────────────────────────────────┘
```

**필터 예시 (특정 상태 변경만):**

```json
{
  "dynamodb": {
    "NewImage": {
      "status": { "S": ["PAID", "SHIPPED"] }
    }
  },
  "eventName": ["INSERT", "MODIFY"]
}
```

### Lambda + SQS

```java
@Service
@RequiredArgsConstructor
public class CdcToSqsHandler {

    private final SqsTemplate sqsTemplate;

    public void handleDynamoDBStream(DynamodbEvent event) {
        event.getRecords().stream()
            .filter(this::isTargetEvent)
            .map(this::toOrderEvent)
            .forEach(this::sendToSqs);
    }

    private boolean isTargetEvent(DynamodbStreamRecord record) {
        // INSERT, MODIFY만 처리
        if (!Set.of("INSERT", "MODIFY").contains(record.getEventName())) {
            return false;
        }

        // 특정 상태만 필터링
        String status = record.getDynamodb()
            .getNewImage().get("status").getS();
        return Set.of("PAID", "SHIPPED").contains(status);
    }

    private void sendToSqs(OrderEvent event) {
        sqsTemplate.send("order-events-queue", event);
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 실시간 동기화 (밀리초 지연) | 인프라 복잡도 증가 |
| DB 부하 최소 | 로그 보관 필요 (디스크) |
| 모든 변경 캡처 (삭제 포함) | 초기 스냅샷 처리 필요 |
| 이전/이후 값 제공 | 스키마 변경 관리 필요 |
| 애플리케이션 비침투적 | CDC 시스템 장애 대비 필요 |

## 트러블슈팅

### 사례 1: binlog 보관 기간 초과

#### 증상
CDC 재시작 시 "binlog not found" 에러

#### 원인 분석
MySQL binlog 보관 기간(기본 7일)이 지나 CDC가 읽어야 할 로그가 삭제됨

#### 해결 방법

```sql
-- binlog 보관 기간 확인
SHOW VARIABLES LIKE 'binlog_expire_logs_seconds';

-- 보관 기간 늘리기 (30일)
SET GLOBAL binlog_expire_logs_seconds = 2592000;
```

```yaml
# Debezium 설정 - 스냅샷부터 재시작
"snapshot.mode": "when_needed"
```

#### 예방 조치
- binlog 보관 기간을 CDC 최대 다운타임보다 길게 설정
- CDC 상태 모니터링 및 알림 설정

### 사례 2: 대용량 테이블 초기 스냅샷

#### 증상
10억 건 테이블 CDC 시작 시 스냅샷이 24시간 이상 소요

#### 원인 분석
초기 스냅샷 시 전체 테이블 스캔 발생

#### 해결 방법

```json
{
  "snapshot.mode": "schema_only",  // 스키마만 스냅샷, 데이터는 이후 변경분만

  // 또는 증분 스냅샷 (Debezium 1.6+)
  "snapshot.mode": "initial",
  "snapshot.fetch.size": 10000,
  "incremental.snapshot.chunk.size": 1024
}
```

### 사례 3: Consumer 처리 지연

#### 증상
CDC 이벤트가 쌓이고 Consumer가 따라가지 못함

#### 해결 방법

```java
// 1. 파티션 늘리기 (병렬 처리)
// Kafka topic 파티션 수 증가

// 2. Consumer 그룹 인스턴스 추가

// 3. 배치 처리
@KafkaListener(topics = "orders-cdc",
               containerFactory = "batchFactory")
public void handleBatch(List<ConsumerRecord<String, String>> records) {
    // 배치로 처리
}
```

## 면접 예상 질문

### Q: CDC와 Polling 방식의 차이점은?

A: **Polling**은 주기적으로 DB를 조회하여 변경 사항을 확인하는 방식입니다. 구현은 간단하지만 폴링 간격 동안 변경을 놓칠 수 있고, 빈 조회도 DB 부하를 발생시킵니다.

**CDC**는 트랜잭션 로그(binlog, WAL)를 읽어 실시간으로 변경을 감지합니다. 밀리초 단위 지연, 모든 변경 캡처(삭제 포함), 이전/이후 값 제공이 가능하고 DB 부하가 최소화됩니다. 단, 별도 인프라가 필요합니다.

### Q: Outbox 패턴이란 무엇이고, CDC와 어떻게 연동하나?

A: **Outbox 패턴**은 비즈니스 데이터와 이벤트를 같은 트랜잭션에서 저장하여 데이터 일관성을 보장하는 패턴입니다.

1. 주문 테이블 INSERT와 Outbox 테이블 INSERT를 같은 트랜잭션으로 처리
2. CDC가 Outbox 테이블 변경을 감지하여 Kafka로 전달
3. 트랜잭션 실패 시 둘 다 롤백되어 일관성 보장

이 방식으로 "DB 저장은 됐는데 이벤트 발행 실패" 문제를 해결합니다.

### Q: CDC 시스템 장애 시 데이터 손실을 어떻게 방지하나?

A: 세 가지 방법으로 대비합니다:

1. **binlog/WAL 보관**: CDC 최대 다운타임보다 길게 로그 보관
2. **Offset 저장**: Kafka Connect는 offset을 저장하여 재시작 시 이어서 처리
3. **스냅샷 모드**: 필요 시 전체 스냅샷부터 다시 시작 가능 (`snapshot.mode=when_needed`)

중요한 것은 로그가 삭제되기 전에 CDC가 복구되어야 한다는 점입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Transaction](./transaction.md) | 선수 지식 - 트랜잭션 로그 이해 | 중급 |
| [레플리케이션](./replication.md) | 선수 지식 - binlog 기반 복제 | 심화 |
| [AWS SQS](../system-design/aws-sqs.md) | CDC + SQS 연동 | 중급 |
| [메시지 큐](../system-design/message-queue.md) | Kafka 연동 | 심화 |
| [Event-Driven Architecture](../system-design/event-driven-architecture.md) | CDC 활용 아키텍처 | 중급 |
| [SAGA 패턴](../system-design/saga-pattern.md) | Outbox 패턴 연계 | 심화 |

## 참고 자료

- [Debezium 공식 문서](https://debezium.io/documentation/)
- [AWS DMS CDC 가이드](https://docs.aws.amazon.com/dms/latest/userguide/CHAP_Task.CDC.html)
- [DynamoDB Streams](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)
- [Martin Kleppmann - Designing Data-Intensive Applications](https://dataintensive.net/)
