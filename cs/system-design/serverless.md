# Serverless Computing

> `[3] 중급` · 선수 지식: [Docker](./docker.md), [확장성](./scalability.md)

> `Trend` 2025

> 서버 관리 없이 코드 실행에만 집중할 수 있는 클라우드 실행 모델

`#Serverless` `#서버리스` `#FaaS` `#FunctionAsAService` `#BaaS` `#BackendAsAService` `#Lambda` `#AWSLambda` `#CloudFunctions` `#AzureFunctions` `#이벤트드리븐` `#EventDriven` `#AutoScaling` `#자동확장` `#PayPerUse` `#종량제` `#ColdStart` `#콜드스타트` `#WarmStart` `#ProvisionedConcurrency` `#APIGateway` `#Trigger` `#트리거` `#Firebase` `#Supabase` `#CloudflareWorkers` `#Knative` `#OpenFaaS` `#VendorLockin` `#NoOps`

## 왜 알아야 하는가?

- **실무**: 2025년 백엔드 전략의 핵심. 인프라 관리 부담 없이 빠른 개발과 배포 가능
- **면접**: 클라우드 아키텍처, MSA 설계 질문에서 Serverless 이해도 확인
- **기반 지식**: FaaS, BaaS, 이벤트 드리븐 아키텍처의 기초

## 핵심 개념

- **No Server Management**: 서버 프로비저닝, 패치, 스케일링을 클라우드가 처리
- **Pay-per-Use**: 실행 시간과 호출 횟수 기준 과금 (유휴 시간 비용 없음)
- **Auto Scaling**: 요청량에 따라 자동으로 인스턴스 확장/축소
- **Event-Driven**: 이벤트(HTTP 요청, DB 변경, 메시지 등)에 반응하여 실행

## 쉽게 이해하기

**택시 vs 렌터카 비유**

- **전통적 서버**: 렌터카를 빌려서 직접 운전. 사용하지 않아도 대여료 지불
- **Serverless**: 택시를 이용. 탈 때만 비용 지불, 운전도 기사(클라우드)가 담당

## 상세 설명

### Serverless의 두 가지 형태

#### 1. FaaS (Function as a Service)

개별 함수 단위로 코드를 배포하고 실행

```
┌─────────────────────────────────────────────────────────────┐
│                        FaaS 플랫폼                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   이벤트 소스           함수 실행           결과 반환         │
│                                                              │
│   ┌─────────┐       ┌─────────┐       ┌─────────┐           │
│   │ HTTP    │──────▶│ Function│──────▶│ Response│           │
│   │ Request │       │ (Lambda)│       │         │           │
│   └─────────┘       └─────────┘       └─────────┘           │
│                                                              │
│   ┌─────────┐       ┌─────────┐       ┌─────────┐           │
│   │ S3      │──────▶│ Function│──────▶│ DB Write│           │
│   │ Upload  │       │ (Lambda)│       │         │           │
│   └─────────┘       └─────────┘       └─────────┘           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**주요 서비스**
- AWS Lambda
- Google Cloud Functions
- Azure Functions
- Cloudflare Workers

#### 2. BaaS (Backend as a Service)

인증, 데이터베이스, 스토리지 등 백엔드 기능을 서비스로 제공

**주요 서비스**
- Firebase (Auth, Firestore, Storage)
- Supabase (PostgreSQL 기반 오픈소스 대안)
- AWS Amplify

### Serverless 동작 원리

```
요청 발생 → 컨테이너 생성(Cold Start) → 함수 실행 → 응답 반환 → 유휴 시 컨테이너 제거

┌──────────────────────────────────────────────────────────────┐
│                     Cold Start vs Warm Start                  │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  Cold Start (첫 요청 또는 스케일 아웃 시)                     │
│  ┌────────┐   ┌────────┐   ┌────────┐   ┌────────┐           │
│  │컨테이너│──▶│런타임  │──▶│코드    │──▶│함수    │           │
│  │생성    │   │초기화  │   │로드    │   │실행    │           │
│  └────────┘   └────────┘   └────────┘   └────────┘           │
│      ↑                                                        │
│      └── 지연 시간 발생 (100ms ~ 수 초)                       │
│                                                               │
│  Warm Start (컨테이너 재사용)                                  │
│  ┌────────┐                                                   │
│  │함수    │  ← 즉시 실행 (수 ms)                              │
│  │실행    │                                                   │
│  └────────┘                                                   │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

**왜 Cold Start가 발생하는가?**

클라우드 제공자는 비용 효율을 위해 유휴 컨테이너를 제거합니다. 새 요청이 오면 컨테이너를 다시 생성해야 하므로 지연이 발생합니다.

### 사용 사례

| 사용 사례 | 설명 | 예시 |
|----------|------|------|
| API 백엔드 | REST/GraphQL API 엔드포인트 | API Gateway + Lambda |
| 이벤트 처리 | 파일 업로드, DB 변경 시 트리거 | S3 이벤트 → Lambda |
| 스케줄 작업 | 주기적 배치 작업 | CloudWatch Events → Lambda |
| 웹훅 처리 | 외부 서비스 알림 처리 | Stripe 결제 웹훅 |
| 실시간 처리 | 스트림 데이터 처리 | Kinesis → Lambda |

## 예제 코드

### AWS Lambda (Node.js)

```javascript
// handler.js
export const hello = async (event) => {
  const name = event.queryStringParameters?.name || 'World';

  return {
    statusCode: 200,
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      message: `Hello, ${name}!`,
      timestamp: new Date().toISOString(),
    }),
  };
};
```

### AWS Lambda (Java)

```java
public class HelloHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent input,
            Context context) {

        String name = input.getQueryStringParameters()
            .getOrDefault("name", "World");

        String body = String.format(
            "{\"message\": \"Hello, %s!\", \"timestamp\": \"%s\"}",
            name,
            Instant.now().toString()
        );

        return new APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody(body);
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 인프라 관리 불필요 | Cold Start 지연 시간 |
| 사용한 만큼만 과금 | 실행 시간 제한 (Lambda: 15분) |
| 자동 스케일링 | 벤더 종속성 (Vendor Lock-in) |
| 빠른 개발과 배포 | 로컬 개발/디버깅 어려움 |
| 고가용성 기본 제공 | Stateless 제약 (상태 저장 불가) |

### 언제 Serverless를 선택해야 하는가?

```
┌─────────────────────────────────────────────────────────────┐
│                    Serverless 적합성 판단                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ✅ Serverless가 적합한 경우                                 │
│  ├─ 트래픽이 불규칙하거나 예측 불가                          │
│  ├─ 이벤트 기반 처리 (파일 업로드, 웹훅 등)                  │
│  ├─ 빠른 MVP 개발이 필요한 경우                              │
│  ├─ 운영 부담을 최소화하고 싶은 경우                         │
│  └─ 마이크로서비스의 개별 함수                               │
│                                                              │
│  ❌ Serverless가 부적합한 경우                               │
│  ├─ 일정한 고트래픽 (컨테이너/VM이 더 비용 효율적)           │
│  ├─ 장시간 실행 작업 (15분 초과)                             │
│  ├─ 극도로 낮은 지연 시간 요구 (Cold Start 민감)             │
│  ├─ 복잡한 상태 관리 필요                                    │
│  └─ 특정 하드웨어/OS 요구사항                                │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## Cold Start 최적화 전략

### 1. Provisioned Concurrency (AWS)

미리 인스턴스를 예약하여 Cold Start 제거

```yaml
# serverless.yml
functions:
  hello:
    handler: handler.hello
    provisionedConcurrency: 5  # 5개 인스턴스 상시 대기
```

### 2. 언어 선택

| 언어 | Cold Start 시간 | 특징 |
|------|----------------|------|
| Python, Node.js | 100~300ms | 가장 빠름 |
| Go, Rust | 200~500ms | 바이너리 배포 |
| Java, .NET | 1~5초 | JVM/CLR 초기화 |

### 3. 패키지 크기 최소화

```bash
# 불필요한 의존성 제거
npm prune --production

# 번들러 사용 (esbuild, webpack)
esbuild src/handler.js --bundle --minify --platform=node --outfile=dist/handler.js
```

## 면접 예상 질문

### Q: Serverless와 Container의 차이점은?

A: **추상화 수준**이 다릅니다.

| 항목 | Serverless | Container |
|------|-----------|-----------|
| 관리 대상 | 코드만 | 컨테이너 + 오케스트레이션 |
| 스케일링 | 자동 (요청 단위) | 수동 또는 HPA 설정 필요 |
| 과금 | 실행 시간 + 호출 수 | 인스턴스 가동 시간 |
| 실행 시간 | 제한 있음 (15분) | 제한 없음 |
| 상태 관리 | Stateless | Stateful 가능 |

**왜 이 차이가 중요한가?**

Serverless는 운영 부담이 적지만 제약이 있고, Container는 유연하지만 관리가 필요합니다. 워크로드 특성에 따라 선택해야 합니다.

### Q: Cold Start를 줄이는 방법은?

A:
1. **Provisioned Concurrency**: 인스턴스 사전 예약
2. **경량 런타임 선택**: Python, Node.js, Go 사용
3. **패키지 최소화**: 불필요한 의존성 제거, 번들링
4. **연결 재사용**: DB 연결을 핸들러 외부에서 초기화
5. **Warm-up 호출**: 주기적으로 함수 호출하여 컨테이너 유지

### Q: Serverless의 Vendor Lock-in을 어떻게 해결하는가?

A:
1. **Serverless Framework**: 멀티 클라우드 배포 지원
2. **Knative**: Kubernetes 위에서 동작하는 Serverless 플랫폼
3. **추상화 레이어**: 비즈니스 로직을 클라우드 SDK와 분리
4. **OpenFaaS**: 오픈소스 FaaS 플랫폼

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Docker](./docker.md) | 컨테이너 기초 (Serverless 내부 동작 이해) | 중급 |
| [확장성](./scalability.md) | 오토스케일링 개념 | 입문 |
| [메시지 큐](./message-queue.md) | 이벤트 드리븐 아키텍처 | 중급 |
| [Kubernetes](./kubernetes.md) | 컨테이너 오케스트레이션 비교 | 중급 |

## 참고 자료

- [AWS Lambda Documentation](https://docs.aws.amazon.com/lambda/)
- [Serverless Framework](https://www.serverless.com/)
- [GeeksforGeeks - Top 10 Backend Development Trends in 2025](https://www.geeksforgeeks.org/blogs/top-backend-development-trends/)
- [The Future of Backend Development: Key Trends for 2025](https://talent500.com/blog/future-of-backend-development-2025/)
