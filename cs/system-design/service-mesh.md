# Service Mesh

> `[4] 심화` · 선수 지식: [API Gateway](./api-gateway.md), [MSA vs 모놀리식](./msa-vs-monolithic.md), [로드밸런싱](./load-balancing.md)

> 마이크로서비스 간 통신을 관리하는 전용 인프라 계층으로, 사이드카 프록시를 통해 트래픽 제어, 보안, 관찰성을 제공

`#ServiceMesh` `#서비스메시` `#Sidecar` `#사이드카` `#Istio` `#이스티오` `#Envoy` `#엔보이` `#Linkerd` `#mTLS` `#상호TLS` `#TrafficManagement` `#트래픽관리` `#Observability` `#관찰성` `#분산추적` `#DistributedTracing` `#Jaeger` `#Zipkin` `#Kiali` `#서킷브레이커` `#재시도` `#Retry` `#타임아웃` `#Timeout` `#카나리배포` `#CanaryDeployment` `#Kubernetes` `#쿠버네티스` `#DataPlane` `#ControlPlane`

## 왜 알아야 하는가?

- **실무**: 대규모 MSA 환경에서 서비스 간 통신 문제 해결의 표준. K8s 기반 시스템에서 Istio 도입 증가
- **면접**: "서비스 간 mTLS는 어떻게 구현하나요?", "분산 추적은 어떻게 하나요?" 질문의 핵심 답변
- **기반 지식**: 제로 트러스트 보안, Observability, 카나리 배포의 기술적 기반

## 핵심 개념

- 서비스 코드 수정 없이 사이드카 프록시가 모든 네트워크 통신을 가로채어 처리
- Data Plane (사이드카들) + Control Plane (중앙 관리) 구조
- 트래픽 관리, 보안 (mTLS), 관찰성을 투명하게 제공

## 쉽게 이해하기

**비서가 있는 임원실 비유**

```
┌─────────────────────────────────────────────────────────────────┐
│                        회 사                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   기존 (Service Mesh 없음)                                       │
│   ─────────────────────────────────                              │
│   ┌─────────┐                 ┌─────────┐                       │
│   │ 임원 A  │──── 직접 통화 ───│ 임원 B  │                       │
│   │         │    (보안 없음)   │         │                       │
│   └─────────┘                 └─────────┘                       │
│                                                                  │
│   Service Mesh 도입 후                                           │
│   ────────────────────                                           │
│   ┌─────────────────┐         ┌─────────────────┐               │
│   │ ┌─────────────┐ │         │ ┌─────────────┐ │               │
│   │ │   비서 A    │◄┼─────────┼►│   비서 B    │ │               │
│   │ │(Sidecar)   │ │  암호화  │ │(Sidecar)   │ │               │
│   │ └──────┬──────┘ │         │ └──────┬──────┘ │               │
│   │        │        │         │        │        │               │
│   │ ┌──────▼──────┐ │         │ ┌──────▼──────┐ │               │
│   │ │   임원 A    │ │         │ │   임원 B    │ │               │
│   │ │ (Service)   │ │         │ │ (Service)   │ │               │
│   │ └─────────────┘ │         │ └─────────────┘ │               │
│   └─────────────────┘         └─────────────────┘               │
│          Pod A                        Pod B                      │
│                                                                  │
│   비서의 역할:                                                   │
│   - 모든 통화를 대신 받고 전달 (프록시)                          │
│   - 통화 내용 암호화 (mTLS)                                      │
│   - 통화 기록 (로깅)                                             │
│   - 바쁘면 나중에 다시 연결 (재시도)                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 상세 설명

### API Gateway vs Service Mesh

```
┌────────────────────────────────────────────────────────────────┐
│                API Gateway vs Service Mesh                      │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   [API Gateway: North-South 트래픽]                             │
│                                                                 │
│           외부 클라이언트                                        │
│                 │                                               │
│                 ▼                                               │
│        ┌───────────────┐                                        │
│        │  API Gateway  │  ← 외부 → 내부 경계                    │
│        └───────────────┘                                        │
│                 │                                               │
│                 ▼                                               │
│         내부 서비스들                                            │
│                                                                 │
│   ────────────────────────────────────────────────────────────  │
│                                                                 │
│   [Service Mesh: East-West 트래픽]                              │
│                                                                 │
│   ┌─────────┐     ┌─────────┐     ┌─────────┐                  │
│   │Service A│◄───►│Service B│◄───►│Service C│                  │
│   │ + Proxy │     │ + Proxy │     │ + Proxy │                  │
│   └─────────┘     └─────────┘     └─────────┘                  │
│                                                                 │
│        ↑                                                        │
│        └── 서비스 간 내부 통신 관리                             │
│                                                                 │
│   ────────────────────────────────────────────────────────────  │
│                                                                 │
│   | 구분 | API Gateway | Service Mesh |                        │
│   |------|-------------|--------------|                        │
│   | 위치 | 경계 (Edge) | 내부 전체    |                        │
│   | 트래픽 | North-South | East-West  |                        │
│   | 배포 | 중앙 집중   | 분산 (사이드카)|                       │
│   | 주 용도 | 외부 노출 API | 서비스 간 통신 |                  │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

**왜 둘 다 필요한가?**

API Gateway는 외부 진입점에서 인증, Rate Limiting을 처리하고, Service Mesh는 내부 서비스 간 보안 통신, 트래픽 제어를 담당합니다. 상호 보완적입니다.

### Service Mesh 아키텍처

```
┌────────────────────────────────────────────────────────────────┐
│                  Service Mesh 아키텍처                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌────────────────────────────────────────────────────────┐   │
│   │                   Control Plane                         │   │
│   │  ┌──────────┐  ┌──────────┐  ┌──────────┐              │   │
│   │  │  Pilot   │  │ Citadel  │  │  Galley  │   (Istio)    │   │
│   │  │ (라우팅)  │  │ (인증서)  │  │ (설정)   │              │   │
│   │  └──────────┘  └──────────┘  └──────────┘              │   │
│   └────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                              │ 설정 배포                        │
│                              ▼                                  │
│   ┌────────────────────────────────────────────────────────┐   │
│   │                    Data Plane                           │   │
│   │                                                         │   │
│   │   ┌─────────────────┐       ┌─────────────────┐        │   │
│   │   │      Pod A      │       │      Pod B      │        │   │
│   │   │ ┌─────────────┐ │       │ ┌─────────────┐ │        │   │
│   │   │ │   Envoy     │◄┼───────┼►│   Envoy     │ │        │   │
│   │   │ │  (Sidecar)  │ │ mTLS  │ │  (Sidecar)  │ │        │   │
│   │   │ └──────┬──────┘ │       │ └──────┬──────┘ │        │   │
│   │   │        │        │       │        │        │        │   │
│   │   │ ┌──────▼──────┐ │       │ ┌──────▼──────┐ │        │   │
│   │   │ │  Service A  │ │       │ │  Service B  │ │        │   │
│   │   │ │ (localhost) │ │       │ │ (localhost) │ │        │   │
│   │   │ └─────────────┘ │       │ └─────────────┘ │        │   │
│   │   └─────────────────┘       └─────────────────┘        │   │
│   │                                                         │   │
│   └────────────────────────────────────────────────────────┘   │
│                                                                 │
│   Data Plane: 실제 트래픽 처리 (Envoy 프록시들)                 │
│   Control Plane: 정책, 설정, 인증서 관리                        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### 핵심 기능 상세

```
┌────────────────────────────────────────────────────────────────┐
│                   Service Mesh 핵심 기능                        │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. 트래픽 관리 (Traffic Management)                           │
│   ────────────────────────────────────                          │
│                                                                 │
│   ┌───────────────────────────────────────────────────────┐    │
│   │                                                        │    │
│   │   [카나리 배포]                                        │    │
│   │   요청 → 90% → v1 (안정 버전)                          │    │
│   │        → 10% → v2 (신규 버전)                          │    │
│   │                                                        │    │
│   │   [A/B 테스트]                                         │    │
│   │   헤더: x-user-group: beta → v2                        │    │
│   │   그 외                     → v1                        │    │
│   │                                                        │    │
│   │   [장애 주입 (Chaos Engineering)]                      │    │
│   │   5% 요청에 500 에러 주입하여 복원력 테스트            │    │
│   │                                                        │    │
│   └───────────────────────────────────────────────────────┘    │
│                                                                 │
│   2. 보안 (Security)                                            │
│   ────────────────────                                          │
│                                                                 │
│   ┌───────────────────────────────────────────────────────┐    │
│   │                                                        │    │
│   │   [mTLS - 상호 TLS]                                    │    │
│   │   Service A ◄──── 암호화된 통신 ────► Service B        │    │
│   │             양방향 인증서 검증                          │    │
│   │                                                        │    │
│   │   [인가 정책]                                          │    │
│   │   "Order Service만 Payment Service 호출 가능"         │    │
│   │                                                        │    │
│   └───────────────────────────────────────────────────────┘    │
│                                                                 │
│   3. 관찰성 (Observability)                                     │
│   ──────────────────────────                                    │
│                                                                 │
│   ┌───────────────────────────────────────────────────────┐    │
│   │                                                        │    │
│   │   [분산 추적] Jaeger, Zipkin                           │    │
│   │   요청 ID로 전체 호출 체인 추적                        │    │
│   │   A → B → C → D 각 구간별 지연 시간 확인              │    │
│   │                                                        │    │
│   │   [메트릭] Prometheus + Grafana                        │    │
│   │   요청 수, 지연 시간, 에러율 자동 수집                 │    │
│   │                                                        │    │
│   │   [서비스 맵] Kiali                                    │    │
│   │   서비스 간 의존성 시각화                              │    │
│   │                                                        │    │
│   └───────────────────────────────────────────────────────┘    │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### 주요 Service Mesh 솔루션 비교

| 솔루션 | 특징 | 장점 | 단점 |
|--------|------|------|------|
| **Istio** | 가장 기능 풍부, Envoy 기반 | 강력한 기능, 큰 커뮤니티 | 복잡도 높음, 리소스 사용량 |
| **Linkerd** | 경량, 심플 | 쉬운 설치, 낮은 오버헤드 | Istio 대비 기능 제한 |
| **Consul Connect** | HashiCorp, 멀티 플랫폼 | K8s + VM 혼합 환경 | 다른 HashiCorp 도구와 조합 시 최적 |
| **AWS App Mesh** | AWS 네이티브 | AWS 서비스 통합 | AWS 종속 |

## 구현 예시

### Istio 설치 및 기본 설정

```bash
# Istio 설치
istioctl install --set profile=demo -y

# 네임스페이스에 사이드카 자동 주입 활성화
kubectl label namespace default istio-injection=enabled

# 샘플 애플리케이션 배포 (사이드카 자동 주입됨)
kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml
```

### 트래픽 관리: 카나리 배포

```yaml
# VirtualService: 트래픽 분배 규칙
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: reviews
spec:
  hosts:
    - reviews
  http:
    - route:
        # 90%는 v1으로
        - destination:
            host: reviews
            subset: v1
          weight: 90
        # 10%는 v2로 (카나리)
        - destination:
            host: reviews
            subset: v2
          weight: 10

---
# DestinationRule: 버전별 서브셋 정의
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: reviews
spec:
  host: reviews
  subsets:
    - name: v1
      labels:
        version: v1
    - name: v2
      labels:
        version: v2
```

### 재시도 및 타임아웃 설정

```yaml
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: ratings
spec:
  hosts:
    - ratings
  http:
    - route:
        - destination:
            host: ratings
      # 타임아웃 설정
      timeout: 10s
      # 재시도 설정
      retries:
        attempts: 3          # 최대 3번 재시도
        perTryTimeout: 2s    # 각 시도당 2초 타임아웃
        retryOn: 5xx,reset,connect-failure,retriable-4xx
```

### 서킷브레이커 설정

```yaml
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: reviews
spec:
  host: reviews
  trafficPolicy:
    connectionPool:
      tcp:
        maxConnections: 100       # 최대 연결 수
      http:
        h2UpgradePolicy: UPGRADE
        http1MaxPendingRequests: 100  # 대기 요청 제한
        http2MaxRequests: 1000        # 최대 동시 요청
    outlierDetection:
      consecutive5xxErrors: 5      # 5번 연속 5xx 에러 시
      interval: 30s                # 30초 간격으로 체크
      baseEjectionTime: 30s        # 30초간 풀에서 제외
      maxEjectionPercent: 50       # 최대 50%까지 제외
```

### mTLS 정책 설정

```yaml
# 네임스페이스 전체에 mTLS 강제
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
  namespace: production
spec:
  mtls:
    mode: STRICT  # mTLS 필수

---
# 특정 서비스 간 통신 인가 정책
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: payment-policy
  namespace: production
spec:
  selector:
    matchLabels:
      app: payment-service
  action: ALLOW
  rules:
    - from:
        - source:
            principals: ["cluster.local/ns/production/sa/order-service"]
      to:
        - operation:
            methods: ["POST"]
            paths: ["/api/payments/*"]
```

### 장애 주입 (Chaos Engineering)

```yaml
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: ratings
spec:
  hosts:
    - ratings
  http:
    - fault:
        # 지연 주입: 10% 요청에 5초 지연
        delay:
          percentage:
            value: 10
          fixedDelay: 5s
        # 에러 주입: 5% 요청에 500 에러
        abort:
          percentage:
            value: 5
          httpStatus: 500
      route:
        - destination:
            host: ratings
```

### 분산 추적 설정

```java
// Spring Boot에서 trace ID 전파 (Istio와 연동)
@Component
public class TracingFilter implements Filter {

    private static final List<String> TRACE_HEADERS = List.of(
        "x-request-id",
        "x-b3-traceid",
        "x-b3-spanid",
        "x-b3-parentspanid",
        "x-b3-sampled",
        "x-b3-flags"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 헤더 값을 ThreadLocal에 저장
        Map<String, String> traceHeaders = new HashMap<>();
        for (String header : TRACE_HEADERS) {
            String value = httpRequest.getHeader(header);
            if (value != null) {
                traceHeaders.put(header, value);
            }
        }

        TraceContext.setHeaders(traceHeaders);

        try {
            chain.doFilter(request, response);
        } finally {
            TraceContext.clear();
        }
    }
}

// 다른 서비스 호출 시 헤더 전파
@Component
public class TracingRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                         ClientHttpRequestExecution execution)
            throws IOException {

        // 저장된 trace 헤더를 요청에 추가
        Map<String, String> headers = TraceContext.getHeaders();
        headers.forEach((key, value) ->
            request.getHeaders().add(key, value));

        return execution.execute(request, body);
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 서비스 코드 수정 없이 기능 추가 | 복잡도 증가 |
| mTLS 자동화 (제로 트러스트) | 리소스 오버헤드 (사이드카) |
| 풍부한 관찰성 | 학습 곡선 |
| 세밀한 트래픽 제어 | 운영 부담 |

### 도입 시 고려사항

```
┌────────────────────────────────────────────────────────────────┐
│                  Service Mesh 도입 체크리스트                   │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   [도입 권장]                                                   │
│   ✓ 서비스 수 10개 이상                                        │
│   ✓ 서비스 간 보안 통신 필수 (금융, 헬스케어)                  │
│   ✓ 정교한 트래픽 제어 필요 (카나리, A/B 테스트)               │
│   ✓ 분산 추적이 핵심 요구사항                                  │
│   ✓ K8s 환경에서 운영                                          │
│                                                                 │
│   [도입 보류]                                                   │
│   ✗ 서비스 수 5개 미만 (오버엔지니어링)                        │
│   ✗ 운영팀 K8s 경험 부족                                       │
│   ✗ 리소스 제약 (사이드카 오버헤드 10-20%)                     │
│   ✗ 단순한 요구사항 (기존 도구로 해결 가능)                    │
│                                                                 │
│   [단계적 도입 권장]                                            │
│   1단계: 관찰성만 활성화 (메트릭, 추적)                         │
│   2단계: mTLS 점진적 적용 (PERMISSIVE → STRICT)                 │
│   3단계: 트래픽 관리 기능 활용                                  │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 트러블슈팅

### 사례 1: 사이드카 주입 후 Pod 시작 실패

#### 증상
Pod가 `CrashLoopBackOff` 상태, 사이드카 주입 전에는 정상 동작

#### 원인 분석
- 애플리케이션이 사이드카보다 먼저 시작하여 네트워크 준비 전 요청 시도
- 또는 사이드카 리소스 부족

#### 해결 방법
```yaml
# Pod에 holdApplicationUntilProxyStarts 설정
apiVersion: v1
kind: Pod
metadata:
  annotations:
    proxy.istio.io/config: |
      holdApplicationUntilProxyStarts: true
spec:
  containers:
    - name: my-app
      # ...
```

```yaml
# 사이드카 리소스 조정
apiVersion: install.istio.io/v1alpha1
kind: IstioOperator
spec:
  values:
    global:
      proxy:
        resources:
          requests:
            cpu: 100m
            memory: 128Mi
          limits:
            cpu: 500m
            memory: 512Mi
```

### 사례 2: mTLS 전환 시 503 에러

#### 증상
mTLS STRICT 모드 전환 후 서비스 간 통신 실패, 503 Service Unavailable

#### 원인 분석
일부 서비스에 사이드카가 없거나, 외부 서비스와 통신 시 mTLS 불일치

#### 해결 방법
```yaml
# 단계적 전환: PERMISSIVE 모드 먼저 적용
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
  namespace: production
spec:
  mtls:
    mode: PERMISSIVE  # mTLS와 평문 모두 허용

---
# 외부 서비스는 mTLS 비활성화
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: external-service
spec:
  host: external-api.example.com
  trafficPolicy:
    tls:
      mode: DISABLE  # 외부는 평문
```

```bash
# 사이드카 주입 상태 확인
kubectl get pods -n production -o jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.spec.containers[*].name}{"\n"}{end}'
```

## 면접 예상 질문

### Q: Service Mesh와 API Gateway의 차이점은?

A: API Gateway는 클러스터 경계에서 외부 → 내부 트래픽(North-South)을 관리하고, 인증/Rate Limiting 등을 처리합니다. Service Mesh는 클러스터 내부에서 서비스 간 트래픽(East-West)을 관리하며, 사이드카 프록시로 mTLS, 재시도, 관찰성을 제공합니다. **왜 둘 다 필요하냐면**, API Gateway는 외부 노출 API를 보호하고, Service Mesh는 내부 서비스 간 보안과 통제를 담당하여 상호 보완적이기 때문입니다.

### Q: 사이드카 패턴의 장단점은?

A: **장점**: 서비스 코드 수정 없이 네트워크 기능 추가, 언어/프레임워크 독립적, 관심사 분리. **단점**: 각 Pod마다 추가 컨테이너로 메모리/CPU 오버헤드 (약 10-20%), 네트워크 홉 증가로 지연 시간 소폭 증가. **왜 이런 트레이드오프를 감수하냐면**, 서비스 코드에 보안/관찰성 로직을 넣으면 언어마다 구현해야 하고 일관성 유지가 어렵지만, 사이드카로 분리하면 인프라 레벨에서 일괄 적용할 수 있기 때문입니다.

### Q: mTLS는 왜 중요하고 어떻게 동작하나요?

A: mTLS(상호 TLS)는 클라이언트와 서버가 서로의 인증서를 검증하는 양방향 인증입니다. **중요한 이유**: 제로 트러스트 모델에서 네트워크 내부라도 모든 통신을 검증해야 합니다. 공격자가 네트워크에 침입해도 유효한 인증서 없이는 서비스 간 통신이 불가능합니다. **동작 방식**: Control Plane(Citadel)이 각 서비스에 인증서를 발급하고, 사이드카들이 통신 시 자동으로 TLS 핸드셰이크를 수행합니다. 애플리케이션 코드는 이를 인지하지 못합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [API Gateway](./api-gateway.md) | 선수 지식 - North-South 트래픽 | Intermediate |
| [MSA vs 모놀리식](./msa-vs-monolithic.md) | 선수 지식 - MSA 환경 이해 | Intermediate |
| [로드밸런싱](./load-balancing.md) | 선수 지식 - 트래픽 분산 | Intermediate |
| [서킷브레이커](./circuit-breaker.md) | 관련 - 장애 처리 패턴 | Intermediate |
| [Kubernetes](./kubernetes.md) | 관련 - 실행 환경 | Intermediate |

## 참고 자료

- [Istio Documentation](https://istio.io/latest/docs/)
- [Envoy Proxy Documentation](https://www.envoyproxy.io/docs/envoy/latest/)
- [Linkerd Documentation](https://linkerd.io/2.14/overview/)
- William Morgan, "The Service Mesh: What Every Software Engineer Needs to Know"
