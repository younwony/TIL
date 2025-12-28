# Edge Computing

> `[3] 중급` · 선수 지식: [확장성](./scalability.md), [캐싱](./caching.md)

> `Trend` 2025

> 데이터 처리를 중앙 클라우드가 아닌 사용자 가까이(엣지)에서 수행하는 분산 컴퓨팅 패러다임

`#EdgeComputing` `#엣지컴퓨팅` `#CDN` `#ContentDeliveryNetwork` `#PoP` `#PointOfPresence` `#엣지서버` `#EdgeServer` `#CloudflareWorkers` `#VercelEdge` `#AWSLambdaEdge` `#Deno` `#Bun` `#엣지함수` `#EdgeFunction` `#저지연` `#LowLatency` `#분산컴퓨팅` `#DistributedComputing` `#실시간처리` `#IoT` `#5G` `#AIatEdge` `#하이브리드아키텍처` `#HybridArchitecture` `#네트워크엣지` `#Fastly` `#Akamai` `#지역성` `#데이터로컬리티`

## 왜 알아야 하는가?

- **실무**: 2025년 백엔드 개발의 핵심 트렌드. 글로벌 서비스에서 저지연 응답을 위해 필수
- **면접**: CDN, 분산 시스템, 시스템 설계 질문에서 Edge Computing 이해도 확인
- **기반 지식**: Serverless, CDN, 분산 아키텍처의 확장 개념

## 핵심 개념

- **Edge Location**: 사용자와 가까운 물리적 위치에 배치된 서버 (PoP: Point of Presence)
- **Data Locality**: 데이터를 생성된 곳에서 처리하여 네트워크 지연 최소화
- **Edge Function**: 엣지 서버에서 실행되는 경량 함수 (Cloudflare Workers, Vercel Edge 등)
- **Hybrid Architecture**: 엣지와 클라우드를 결합한 하이브리드 구조

## 쉽게 이해하기

**편의점 vs 대형마트 비유**

- **중앙 클라우드**: 대형마트. 모든 상품이 있지만 멀리 가야 함
- **Edge Computing**: 동네 편의점. 자주 쓰는 상품은 가까운 곳에서 바로 구매

사용자 요청을 미국 버지니아(AWS us-east-1)까지 보내는 대신, 서울 근처 엣지 서버에서 바로 처리하면 응답 시간이 200ms → 20ms로 단축됩니다.

## 상세 설명

### Edge Computing 아키텍처 유형

```
┌─────────────────────────────────────────────────────────────────┐
│                    Edge Computing 계층 구조                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [1] Device Edge (디바이스 엣지)                                 │
│  ├─ 스마트폰, IoT 센서, 스마트 카메라                            │
│  ├─ 가장 낮은 지연, 제한된 연산 능력                             │
│  └─ 예: 스마트워치 건강 데이터 분석                              │
│            │                                                     │
│            ▼                                                     │
│  [2] Near Edge (니어 엣지)                                       │
│  ├─ 로컬 게이트웨이, 온프레미스 서버                             │
│  ├─ 시설 내 실시간 처리                                          │
│  └─ 예: 공장 IoT 데이터 집계                                     │
│            │                                                     │
│            ▼                                                     │
│  [3] Network Edge (네트워크 엣지)  ← 개발자가 주로 다루는 영역   │
│  ├─ CDN PoP, 통신사 엣지 서버                                    │
│  ├─ Cloudflare Workers, Vercel Edge, Lambda@Edge                 │
│  └─ 예: 글로벌 API 응답, 인증, 개인화                            │
│            │                                                     │
│            ▼                                                     │
│  [4] Cloud (중앙 클라우드)                                       │
│  ├─ AWS, GCP, Azure 데이터센터                                   │
│  ├─ 대규모 연산, 데이터 저장                                     │
│  └─ 예: AI 모델 학습, 배치 처리                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### CDN vs Edge Computing

| 항목 | CDN | Edge Computing |
|------|-----|----------------|
| **핵심 기능** | 정적 콘텐츠 캐싱 | 동적 코드 실행 |
| **처리 방식** | 저장 및 전달 | 연산 및 처리 |
| **사용 사례** | 이미지, JS, CSS 배포 | API 응답, 인증, 개인화 |
| **상태** | Stateless (캐시) | Stateless/Stateful 가능 |
| **예시** | CloudFront, Akamai | Cloudflare Workers, Deno Deploy |

```
┌─────────────────────────────────────────────────────────────────┐
│                CDN vs Edge Computing 비교                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  CDN (Content Delivery Network)                                  │
│  ┌─────┐     ┌─────┐     ┌─────┐                                │
│  │Origin│────▶│Cache│────▶│User │                                │
│  │Server│     │(PoP)│     │     │                                │
│  └─────┘     └─────┘     └─────┘                                │
│      └── 정적 콘텐츠를 캐싱하여 전달                             │
│                                                                  │
│  Edge Computing                                                  │
│  ┌─────┐     ┌─────────┐     ┌─────┐                            │
│  │Origin│◀───│Edge     │◀────│User │                            │
│  │Server│    │Function │     │     │                            │
│  └─────┘     └─────────┘     └─────┘                            │
│      └── 엣지에서 코드 실행, 필요시 Origin 호출                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**왜 CDN만으로 부족한가?**

CDN은 정적 콘텐츠만 캐싱합니다. 사용자별 개인화, 인증, 실시간 데이터 처리는 Origin 서버까지 요청이 가야 합니다. Edge Computing은 이런 동적 처리를 사용자 가까이에서 수행합니다.

### Edge Computing 사용 사례

| 사용 사례 | 설명 | 이점 |
|----------|------|------|
| **인증/인가** | JWT 검증, 세션 확인 | Origin 부하 감소, 빠른 응답 |
| **개인화** | 지역/언어별 콘텐츠 변환 | 사용자 경험 향상 |
| **A/B 테스팅** | 엣지에서 트래픽 분배 | 실시간 실험 |
| **API Gateway** | Rate Limiting, 요청 변환 | 보안 강화 |
| **AI 추론** | 경량 ML 모델 실행 | 실시간 AI 응답 |
| **IoT 데이터 집계** | 센서 데이터 사전 처리 | 대역폭 절감 |

### 주요 Edge Computing 플랫폼

| 플랫폼 | 특징 | 런타임 | Cold Start |
|--------|------|--------|------------|
| **Cloudflare Workers** | 300+ 엣지 로케이션, V8 Isolates | JavaScript, WASM | 0ms |
| **Vercel Edge Functions** | Next.js 통합, 글로벌 배포 | JavaScript, TypeScript | 0ms |
| **AWS Lambda@Edge** | CloudFront 통합 | Node.js, Python | 수백ms |
| **Deno Deploy** | Deno 런타임, TypeScript 네이티브 | Deno | 0ms |
| **Fastly Compute@Edge** | WASM 기반, 고성능 | Rust, JavaScript | 0ms |

## 예제 코드

### Cloudflare Workers

```javascript
// 엣지에서 지역별 인사말 반환
export default {
  async fetch(request) {
    const country = request.cf?.country || 'Unknown';

    const greetings = {
      KR: '안녕하세요!',
      JP: 'こんにちは!',
      US: 'Hello!',
      default: 'Welcome!'
    };

    const greeting = greetings[country] || greetings.default;

    return new Response(JSON.stringify({
      message: greeting,
      country: country,
      colo: request.cf?.colo,  // 처리한 엣지 로케이션
      timestamp: new Date().toISOString()
    }), {
      headers: { 'Content-Type': 'application/json' }
    });
  }
};
```

### Vercel Edge Functions (Next.js)

```typescript
// middleware.ts - 엣지에서 인증 검증
import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export const config = {
  matcher: '/api/:path*',
};

export function middleware(request: NextRequest) {
  const token = request.headers.get('authorization')?.split(' ')[1];

  if (!token) {
    return NextResponse.json(
      { error: 'Unauthorized' },
      { status: 401 }
    );
  }

  // 간단한 JWT 검증 (실제로는 더 철저히)
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));

    if (payload.exp < Date.now() / 1000) {
      return NextResponse.json(
        { error: 'Token expired' },
        { status: 401 }
      );
    }

    // 검증 성공 시 Origin으로 전달
    return NextResponse.next();
  } catch {
    return NextResponse.json(
      { error: 'Invalid token' },
      { status: 401 }
    );
  }
}
```

### 엣지에서 A/B 테스팅

```javascript
// Cloudflare Workers - 엣지에서 A/B 테스트 분배
export default {
  async fetch(request, env) {
    const url = new URL(request.url);

    // 사용자 식별자로 일관된 버킷 할당
    const userId = request.headers.get('x-user-id') ||
                   request.cf?.clientId ||
                   crypto.randomUUID();

    // 해시 기반 버킷 분배 (50:50)
    const bucket = hashToBucket(userId, 2);
    const variant = bucket === 0 ? 'control' : 'experiment';

    // 엣지에서 바로 다른 Origin으로 라우팅
    const origins = {
      control: 'https://v1.example.com',
      experiment: 'https://v2.example.com'
    };

    const response = await fetch(origins[variant] + url.pathname, request);

    // 응답에 variant 정보 추가
    const newResponse = new Response(response.body, response);
    newResponse.headers.set('x-ab-variant', variant);

    return newResponse;
  }
};

function hashToBucket(str, buckets) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = ((hash << 5) - hash) + str.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash) % buckets;
}
```

## 동작 원리

### V8 Isolates (Cloudflare Workers의 비밀)

```
┌─────────────────────────────────────────────────────────────────┐
│                      V8 Isolates vs Containers                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  전통적 Serverless (Lambda)                                      │
│  ┌──────────────────┐  ┌──────────────────┐                     │
│  │    Container A   │  │    Container B   │                     │
│  │  ┌────────────┐  │  │  ┌────────────┐  │                     │
│  │  │   런타임   │  │  │  │   런타임   │  │                     │
│  │  │  (Node.js) │  │  │  │  (Node.js) │  │                     │
│  │  └────────────┘  │  │  └────────────┘  │                     │
│  │  ┌────────────┐  │  │  ┌────────────┐  │                     │
│  │  │    코드    │  │  │  │    코드    │  │                     │
│  │  └────────────┘  │  │  └────────────┘  │                     │
│  └──────────────────┘  └──────────────────┘                     │
│     Cold Start: 100ms~수초                                       │
│                                                                  │
│  V8 Isolates (Cloudflare Workers)                                │
│  ┌──────────────────────────────────────┐                       │
│  │         단일 V8 런타임 프로세스        │                       │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ │                       │
│  │  │Isolate A│ │Isolate B│ │Isolate C│ │                       │
│  │  │ (코드)  │ │ (코드)  │ │ (코드)  │ │                       │
│  │  └─────────┘ └─────────┘ └─────────┘ │                       │
│  └──────────────────────────────────────┘                       │
│     Cold Start: 0ms (이미 실행 중인 V8에 Isolate 추가)           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**왜 V8 Isolates가 빠른가?**

- 컨테이너처럼 OS 프로세스를 새로 시작하지 않음
- 이미 실행 중인 V8 엔진에 격리된 실행 환경만 추가
- 메모리 효율적 (Isolate당 수 MB)
- 5ms 이내에 새 Isolate 생성 가능

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 극도로 낮은 지연 시간 (20ms 이하) | 제한된 실행 환경 (CPU 시간, 메모리) |
| Cold Start 거의 없음 | 일부 API 사용 불가 (파일 시스템 등) |
| 글로벌 분산으로 높은 가용성 | 디버깅 어려움 |
| Origin 서버 부하 감소 | Stateful 처리 제한 |
| 대역폭 비용 절감 | 벤더 종속성 |

### 엣지에서 처리해야 할 것 vs 하지 말아야 할 것

```
┌─────────────────────────────────────────────────────────────────┐
│                    Edge 적합성 판단 가이드                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ Edge에서 처리하면 좋은 것                                    │
│  ├─ 인증/인가 (JWT 검증, API Key 확인)                          │
│  ├─ 요청/응답 변환 (헤더 추가, 리다이렉트)                       │
│  ├─ 지역화 (언어, 통화, 날짜 형식)                               │
│  ├─ A/B 테스팅, 피처 플래그                                      │
│  ├─ Bot 탐지, Rate Limiting                                     │
│  └─ 정적 데이터 캐싱 (KV Store)                                  │
│                                                                  │
│  ❌ Edge에서 피해야 할 것                                        │
│  ├─ 복잡한 비즈니스 로직                                         │
│  ├─ 대용량 데이터 처리                                           │
│  ├─ 장시간 실행 작업 (10초 이상)                                 │
│  ├─ 관계형 DB 직접 연결 (연결 풀 관리 어려움)                    │
│  └─ 대용량 파일 처리                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 하이브리드 아키텍처 패턴

2025년의 주류 아키텍처는 Edge + Serverless + Cloud의 하이브리드입니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                   하이브리드 아키텍처 예시                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  사용자 요청                                                     │
│       │                                                          │
│       ▼                                                          │
│  ┌─────────────────┐                                             │
│  │   Edge Layer    │  ← 인증, 캐싱, 개인화, Rate Limit           │
│  │ (Cloudflare)    │     지연: ~20ms                             │
│  └────────┬────────┘                                             │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────┐                                             │
│  │ Serverless API  │  ← 비즈니스 로직, DB 쿼리                   │
│  │ (Lambda/Cloud   │     지연: ~100ms                            │
│  │  Functions)     │                                             │
│  └────────┬────────┘                                             │
│           │                                                      │
│           ▼                                                      │
│  ┌─────────────────┐                                             │
│  │   Cloud Core    │  ← 배치 처리, AI 학습, 분석                 │
│  │ (AWS/GCP/Azure) │     지연: 상관없음                          │
│  └─────────────────┘                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 면접 예상 질문

### Q: Edge Computing과 CDN의 차이점은?

A: **처리 방식**이 다릅니다.

- **CDN**: 정적 콘텐츠를 **캐싱**하여 전달. 연산 없이 저장된 데이터를 반환
- **Edge Computing**: 엣지 서버에서 **코드를 실행**. 동적 처리, 인증, 개인화 가능

CDN은 "저장소"이고, Edge Computing은 "컴퓨터"입니다. 실제로는 함께 사용하여 정적 콘텐츠는 CDN으로, 동적 처리는 Edge Function으로 처리합니다.

### Q: Edge Computing은 언제 사용해야 하는가?

A: **지연 시간이 중요하고, 간단한 처리가 필요한 경우**에 적합합니다.

1. **글로벌 서비스**: 전 세계 사용자에게 일관된 저지연 응답 필요
2. **인증/인가**: 모든 요청을 Origin까지 보내지 않고 엣지에서 빠르게 검증
3. **개인화**: 지역, 언어, 디바이스에 따른 콘텐츠 변환
4. **실시간 AI**: 추론 결과를 빠르게 반환해야 할 때

반면 복잡한 비즈니스 로직, 대용량 데이터 처리, DB 트랜잭션은 중앙 서버가 적합합니다.

### Q: Cloudflare Workers의 0ms Cold Start는 어떻게 가능한가?

A: **V8 Isolates** 기술 덕분입니다.

전통적인 Serverless(Lambda)는 요청마다 컨테이너를 시작하지만, Cloudflare Workers는 이미 실행 중인 V8 엔진에 **격리된 실행 환경(Isolate)**만 추가합니다.

- 컨테이너: OS 프로세스 시작 → 런타임 로드 → 코드 실행 (수백ms)
- Isolates: 기존 프로세스에 메모리 공간만 할당 (~5ms)

이 방식으로 수천 개의 Worker가 하나의 프로세스에서 격리되어 실행됩니다.

### Q: Edge에서 데이터베이스 접근은 어떻게 하는가?

A: **Edge-optimized 데이터베이스**를 사용합니다.

1. **KV Store**: Cloudflare KV, Vercel KV - 전역 복제된 키-값 저장소
2. **Edge SQL**: Cloudflare D1, PlanetScale - 엣지에서 접근 최적화된 SQL
3. **Global Cache**: Redis Global, Upstash - 전역 분산 캐시

기존 RDB는 연결 풀 관리가 어려워 직접 연결을 피하고, 이런 Edge-native 솔루션을 사용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [확장성](./scalability.md) | 수평 확장, 분산 시스템 기초 | 입문 |
| [캐싱](./caching.md) | CDN 캐싱, 캐시 전략 | 중급 |
| [Serverless](./serverless.md) | FaaS와 Edge Functions 비교 | 중급 |
| [Rate Limiting](./rate-limiting.md) | 엣지에서 Rate Limit 구현 | 중급 |

## 참고 자료

- [Cloudflare Workers Documentation](https://developers.cloudflare.com/workers/)
- [Vercel Edge Functions](https://vercel.com/docs/functions/edge-functions)
- [15 Edge Computing Trends to Watch in 2025](https://www.techtarget.com/searchcio/tip/Top-edge-computing-trends-to-watch-in-2020)
- [Edge Computing in 2025: New Frontiers for Developers](https://dev.to/karander/edge-computing-in-2025-new-frontiers-for-developers-obo)
- [CDN vs Edge Computing](https://www.belugacdn.com/cdn-vs-edge-computing/)
