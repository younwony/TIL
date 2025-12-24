# 모니터링 (Observability)

> `[3] 중급` · 선수 지식: [CI/CD](./ci-cd.md)

> 시스템의 상태를 실시간으로 파악하고 문제를 조기에 감지하기 위한 로깅, 메트릭, 트레이싱의 통합 체계

`#모니터링` `#Monitoring` `#Observability` `#관측가능성` `#로깅` `#Logging` `#메트릭` `#Metrics` `#트레이싱` `#Tracing` `#DistributedTracing` `#분산추적` `#APM` `#ApplicationPerformanceMonitoring` `#Prometheus` `#Grafana` `#ELK` `#Elasticsearch` `#Logstash` `#Kibana` `#Jaeger` `#Zipkin` `#OpenTelemetry` `#OTEL` `#SRE` `#SLI` `#SLO` `#SLA` `#알림` `#Alert` `#대시보드` `#Dashboard`

## 왜 알아야 하는가?

- **실무**: 장애 발생 시 MTTR(평균 복구 시간) 단축의 핵심. 모니터링 없이는 문제의 원인을 파악할 수 없음
- **면접**: "시스템 장애 대응 경험", "성능 문제 해결 사례" 등 실무 역량을 판단하는 필수 주제
- **기반 지식**: SRE, 성능 최적화, 용량 계획 등 운영 전반의 기초

## 핵심 개념

- **Observability 3 Pillars**: 로그(Logs), 메트릭(Metrics), 트레이스(Traces)
- **SLI/SLO/SLA**: 서비스 수준을 정량적으로 측정하고 관리하는 지표
- **Alerting**: 이상 징후 감지 시 자동 알림

## 쉽게 이해하기

**자동차 계기판 비유**

자동차를 운전할 때 계기판이 없다면?
- 속도, 연료량, 엔진 온도를 알 수 없음
- 문제가 생겨도 차가 멈춰야 알게 됨

모니터링은 서버의 계기판:
- **메트릭**: 속도계, 연료계 (현재 상태 숫자)
- **로그**: 블랙박스 (무슨 일이 있었는지 기록)
- **트레이스**: 네비게이션 경로 (요청이 어디를 거쳤는지)

## 상세 설명

### Observability 3 Pillars

```
┌─────────────────────────────────────────────────────────────┐
│                    Observability                             │
├───────────────────┬───────────────────┬─────────────────────┤
│       Logs        │      Metrics      │       Traces        │
├───────────────────┼───────────────────┼─────────────────────┤
│ 이벤트 기록       │ 수치 시계열 데이터│ 요청 흐름 추적      │
│ "무슨 일이 있었나"│ "얼마나 빠른가"   │ "어디서 느린가"     │
├───────────────────┼───────────────────┼─────────────────────┤
│ ELK, Loki         │ Prometheus        │ Jaeger, Zipkin      │
│ CloudWatch Logs   │ Grafana           │ X-Ray               │
└───────────────────┴───────────────────┴─────────────────────┘
```

### 1. 로깅 (Logging)

시스템에서 발생하는 이벤트를 시간순으로 기록.

**로그 레벨:**

| 레벨 | 용도 | 예시 |
|------|------|------|
| ERROR | 즉시 대응 필요한 오류 | DB 연결 실패, 결제 실패 |
| WARN | 잠재적 문제 | 재시도 발생, 메모리 부족 경고 |
| INFO | 주요 비즈니스 이벤트 | 주문 완료, 사용자 로그인 |
| DEBUG | 개발/디버깅용 상세 정보 | 함수 호출, 변수 값 |
| TRACE | 매우 상세한 실행 흐름 | 메서드 진입/종료 |

**구조화된 로깅 (Structured Logging):**

```java
// Bad: 파싱하기 어려운 문자열
log.info("User 123 ordered product 456 for $99.99");

// Good: JSON 구조로 검색/분석 용이
log.info("Order placed", Map.of(
    "userId", 123,
    "productId", 456,
    "amount", 99.99,
    "currency", "USD"
));
```

**왜 구조화된 로깅인가?**
- 로그 검색/필터링 용이 (`userId:123`)
- 자동화된 분석 가능
- 대시보드/알림 설정 편리

### 2. 메트릭 (Metrics)

시간에 따른 수치 데이터의 집합.

**메트릭 유형:**

| 유형 | 설명 | 예시 |
|------|------|------|
| Counter | 증가만 하는 누적값 | 요청 수, 에러 수 |
| Gauge | 증감하는 현재값 | CPU 사용률, 메모리 |
| Histogram | 값의 분포 | 응답 시간 분포 |
| Summary | 백분위수 계산 | p50, p95, p99 응답 시간 |

**USE/RED 메트릭:**

```
┌─────────────────────────────────────────────────────────────┐
│  USE Method (인프라)          │  RED Method (서비스)        │
├───────────────────────────────┼─────────────────────────────┤
│  U: Utilization (사용률)      │  R: Rate (초당 요청 수)     │
│  S: Saturation (포화도)       │  E: Errors (에러율)         │
│  E: Errors (에러)             │  D: Duration (응답 시간)    │
└───────────────────────────────┴─────────────────────────────┘
```

**왜 USE/RED인가?**
- USE: 인프라(CPU, 메모리, 디스크) 문제 진단
- RED: 사용자 경험 관점의 서비스 상태 파악
- 두 방법론을 함께 사용하면 전체 시스템 파악 가능

### 3. 트레이싱 (Distributed Tracing)

마이크로서비스 환경에서 요청의 전체 흐름 추적.

```
사용자 요청
     │
     ▼
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│ Gateway │ →  │ Order   │ →  │ Payment │ →  │ Notify  │
│  50ms   │    │  120ms  │    │  800ms  │    │  30ms   │
└─────────┘    └─────────┘    └─────────┘    └─────────┘
                                   ↑
                              병목 지점!
```

**핵심 개념:**

- **Trace**: 하나의 요청에 대한 전체 여정
- **Span**: Trace 내의 개별 작업 단위
- **Trace ID**: 요청을 고유하게 식별하는 ID (서비스 간 전파)

**왜 분산 트레이싱인가?**
- 마이크로서비스에서 로그만으로는 전체 흐름 파악 불가
- 병목 지점을 시각적으로 확인
- 서비스 간 의존성 파악

### SLI/SLO/SLA

```
┌─────────────────────────────────────────────────────────────┐
│  SLA (Service Level Agreement)                               │
│  "99.9% 가용성 보장, 미달 시 크레딧 제공" - 계약              │
├─────────────────────────────────────────────────────────────┤
│  SLO (Service Level Objective)                               │
│  "99.95% 가용성 목표" - 내부 목표 (SLA보다 높게)              │
├─────────────────────────────────────────────────────────────┤
│  SLI (Service Level Indicator)                               │
│  "현재 가용성 99.97%" - 실제 측정값                          │
└─────────────────────────────────────────────────────────────┘
```

**Error Budget:**

SLO가 99.9%면, 한 달에 43분의 다운타임 허용 (에러 버짓).
- 에러 버짓 남음 → 새 기능 개발에 투자
- 에러 버짓 소진 → 안정화에 집중

## 예제 코드

### Spring Boot + Micrometer 메트릭

```java
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final MeterRegistry meterRegistry;
    private final Counter orderCounter;
    private final Timer orderTimer;

    public OrderController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.orderCounter = Counter.builder("orders.total")
            .description("Total number of orders")
            .tag("type", "online")
            .register(meterRegistry);
        this.orderTimer = Timer.builder("orders.duration")
            .description("Order processing time")
            .register(meterRegistry);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        return orderTimer.record(() -> {
            try {
                Order order = orderService.create(request);
                orderCounter.increment();
                meterRegistry.counter("orders.success").increment();
                return ResponseEntity.ok(order);
            } catch (Exception e) {
                meterRegistry.counter("orders.failure",
                    "reason", e.getClass().getSimpleName()).increment();
                throw e;
            }
        });
    }
}
```

### 구조화된 로깅 (Logback + JSON)

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeMdcKeyName>traceId</includeMdcKeyName>
            <includeMdcKeyName>spanId</includeMdcKeyName>
            <includeMdcKeyName>userId</includeMdcKeyName>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="JSON"/>
    </root>
</configuration>
```

```java
@Slf4j
@Service
public class OrderService {

    public Order create(OrderRequest request) {
        MDC.put("userId", request.getUserId());
        MDC.put("orderId", UUID.randomUUID().toString());

        log.info("Creating order", kv("productId", request.getProductId()),
                                   kv("quantity", request.getQuantity()));

        // 비즈니스 로직

        log.info("Order created successfully", kv("totalAmount", order.getTotal()));

        return order;
    }
}
```

### OpenTelemetry 트레이싱

```java
@Configuration
public class TracingConfig {

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault()
            .merge(Resource.create(Attributes.of(
                ResourceAttributes.SERVICE_NAME, "order-service"
            )));

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(
                OtlpGrpcSpanExporter.builder()
                    .setEndpoint("http://jaeger:4317")
                    .build()
            ).build())
            .setResource(resource)
            .build();

        return OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .buildAndRegisterGlobal();
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final Tracer tracer;

    public PaymentResult process(PaymentRequest request) {
        Span span = tracer.spanBuilder("process-payment")
            .setAttribute("payment.method", request.getMethod())
            .setAttribute("payment.amount", request.getAmount())
            .startSpan();

        try (Scope scope = span.makeCurrent()) {
            // 결제 처리 로직
            PaymentResult result = gateway.charge(request);

            span.setAttribute("payment.transactionId", result.getTransactionId());
            return result;

        } catch (Exception e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### Prometheus 알림 규칙

```yaml
# prometheus-rules.yaml
groups:
  - name: application
    rules:
      - alert: HighErrorRate
        expr: |
          sum(rate(http_requests_total{status=~"5.."}[5m]))
          /
          sum(rate(http_requests_total[5m])) > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
          description: "Error rate is {{ $value | humanizePercentage }}"

      - alert: SlowResponseTime
        expr: |
          histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "P95 latency is above 1 second"

      - alert: HighMemoryUsage
        expr: |
          (node_memory_MemTotal_bytes - node_memory_MemAvailable_bytes)
          / node_memory_MemTotal_bytes > 0.9
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Memory usage above 90%"
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 빠른 장애 감지 및 대응 | 인프라/저장 비용 발생 |
| 데이터 기반 의사결정 | 설정 및 운영 복잡도 |
| 성능 병목 지점 파악 | 과도한 로깅으로 인한 성능 영향 |
| 용량 계획 수립 가능 | 알림 피로(Alert Fatigue) 위험 |

## 트러블슈팅

### 사례 1: 알림 폭탄 (Alert Fatigue)

#### 증상
- 하루에 수백 개의 알림 발생
- 중요한 알림을 놓치기 시작
- 팀이 알림을 무시하는 습관

#### 원인 분석
- 임계값이 너무 낮게 설정
- 증상(symptom)이 아닌 원인(cause) 기반 알림
- 알림 중복/연쇄 발생

#### 해결 방법
```yaml
# 알림 그룹화 및 억제
alertmanager:
  route:
    group_by: ['alertname', 'service']
    group_wait: 30s
    group_interval: 5m
    repeat_interval: 4h

  inhibit_rules:
    - source_match:
        severity: 'critical'
      target_match:
        severity: 'warning'
      equal: ['alertname', 'service']
```

#### 예방 조치
- 증상 기반 알림 (사용자 영향 있는 것만)
- 알림 계층화 (critical/warning/info)
- 정기적인 알림 리뷰 및 정리

### 사례 2: 로그 저장소 용량 폭발

#### 증상
- Elasticsearch 디스크 사용률 급증
- 로그 검색 속도 저하
- 비용 급증

#### 원인 분석
- DEBUG 레벨 로그가 프로덕션에서 활성화
- 로그 보존 정책 미설정
- 불필요한 필드 인덱싱

#### 해결 방법
```yaml
# ILM (Index Lifecycle Management) 설정
PUT _ilm/policy/logs-policy
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "rollover": {
            "max_size": "50GB",
            "max_age": "1d"
          }
        }
      },
      "warm": {
        "min_age": "7d",
        "actions": {
          "shrink": { "number_of_shards": 1 },
          "forcemerge": { "max_num_segments": 1 }
        }
      },
      "delete": {
        "min_age": "30d",
        "actions": { "delete": {} }
      }
    }
  }
}
```

#### 예방 조치
- 환경별 로그 레벨 분리
- 샘플링 적용 (트래픽 높은 엔드포인트)
- 정기적인 용량 모니터링 알림

## 면접 예상 질문

### Q: Observability의 3가지 요소를 설명하고 각각 언제 사용하나요?

A: Logs, Metrics, Traces입니다. Logs는 이벤트의 상세 맥락을 기록하며 디버깅에 사용합니다. Metrics는 시계열 수치 데이터로 시스템 상태를 실시간 파악하고 알림 설정에 사용합니다. Traces는 분산 시스템에서 요청의 전체 흐름을 추적하여 병목 지점을 찾습니다. 장애 대응 시 Metrics로 이상 감지, Traces로 병목 위치 파악, Logs로 상세 원인 분석 순으로 사용합니다.

### Q: SLI, SLO, SLA의 차이점은?

A: SLI(Service Level Indicator)는 실제 측정된 서비스 품질 지표입니다. 예를 들어 현재 가용성 99.97%. SLO(Service Level Objective)는 내부 목표로, SLA보다 높게 설정합니다. 예: 99.95% 가용성 목표. SLA(Service Level Agreement)는 고객과의 계약으로 미달 시 보상이 따릅니다. 예: 99.9% 미달 시 크레딧 제공. SLO와 SLA 사이의 여유분이 에러 버짓이 되어, 새 기능 개발과 안정화 사이의 균형을 잡습니다.

### Q: 마이크로서비스에서 분산 트레이싱이 필요한 이유는?

A: 마이크로서비스는 하나의 요청이 여러 서비스를 거칩니다. 로그만으로는 어떤 서비스에서 문제가 생겼는지, 어떤 순서로 호출됐는지 파악하기 어렵습니다. 분산 트레이싱은 Trace ID를 통해 요청의 전체 여정을 추적하고, 각 Span의 소요 시간을 보여줘서 병목 지점을 시각적으로 파악할 수 있습니다. 또한 서비스 간 의존성 맵을 생성하여 아키텍처 이해에도 도움됩니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 배포 파이프라인과 연계 | Beginner |
| [Docker](../system-design/docker.md) | 컨테이너 로깅/메트릭 | Intermediate |
| [Kubernetes](../system-design/kubernetes.md) | K8s 모니터링 스택 | Advanced |
| [Circuit Breaker](../system-design/circuit-breaker.md) | 장애 대응 패턴 | Intermediate |

## 참고 자료

- [Google SRE Book - Monitoring Distributed Systems](https://sre.google/sre-book/monitoring-distributed-systems/)
- [OpenTelemetry Documentation](https://opentelemetry.io/docs/)
- [Prometheus Best Practices](https://prometheus.io/docs/practices/)
- [The USE Method - Brendan Gregg](https://www.brendangregg.com/usemethod.html)
