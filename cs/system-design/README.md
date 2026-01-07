# System Design

시스템 설계 관련 학습 내용을 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 시스템 설계란                                               │
│        - 시스템 설계의 정의와 목표                               │
│            │                                                     │
│            ▼                                                     │
│   [2] 확장성 + 가용성 + 일관성                                   │
│        - 시스템 설계 3대 핵심 요소                               │
│                           │                                      │
│            ┌──────────────┼──────────────┐                      │
│            ▼              ▼              ▼                      │
│   [3] 캐싱         [3] 서킷브레이커  [3] 메시지 큐               │
│        - 패턴 & 전략                                             │
│            │              │              │                      │
│            └──────────────┴──────────────┘                      │
│                           │                                      │
│            ┌──────────────┴──────────────┐                      │
│            ▼                             ▼                      │
│   [4] 대규모 시스템 설계           [4] 선착순 쿠폰                │
│        - 사례 연구                                               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 난이도별 목차

### [1] 정의/기초

시스템 설계가 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [시스템 설계란](./what-is-system-design.md) | 시스템 설계의 정의, 목표, 접근 방법 | 25분 |
| [Cloud Computing](./cloud-computing.md) | IaaS/PaaS/SaaS, 클라우드 서비스 모델 | 30분 |

### [2] 입문

시스템 설계 기초를 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Cloud Native](./cloud-native.md) | 컨테이너, 마이크로서비스, 서비스 메시 `Trend` | 시스템 설계란 |
| [Layered Architecture](./layered-architecture.md) | 계층형 아키텍처, Controller-Service-Repository | 시스템 설계란 |
| [확장성](./scalability.md) | 수직/수평 확장, 스케일 아웃 전략 | 시스템 설계란 |
| [가용성](./availability.md) | 고가용성(HA), 이중화, 페일오버, SLA/SLO | 시스템 설계란 |
| [일관성](./consistency.md) | 강한/최종 일관성, CAP 정리 | 시스템 설계란 |

### [3] 중급

기초 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [로드밸런싱](./load-balancing.md) | L4/L7, Round Robin, Health Check | 확장성, 가용성 |
| [캐싱](./caching.md) | 캐시 전략, 캐시 무효화, Redis | 확장성 |
| [서킷브레이커](./circuit-breaker.md) | 장애 전파 방지 패턴 | 가용성 |
| [메시지 큐](./message-queue.md) | Kafka, RabbitMQ, 이벤트 드리븐 | 확장성, 가용성 |
| [외부 API 통합](./external-api-integration.md) | Retry, Backoff, Timeout, Idempotency | 서킷브레이커, Rate Limiting |
| [API Gateway](./api-gateway.md) | 인증, 라우팅, Rate Limiting | 로드밸런싱, MSA |
| [Docker](./docker.md) | 컨테이너, 이미지, Dockerfile | OS 기초 |
| [Kubernetes](./kubernetes.md) | 컨테이너 오케스트레이션, Pod, Service | Docker |
| [Serverless](./serverless.md) | FaaS, BaaS, 서버리스 아키텍처 `Trend` | Docker, 확장성 |
| [Edge Computing](./edge-computing.md) | 엣지 컴퓨팅, CDN, 분산 처리 `Trend` | 확장성, 캐싱 |
| [DTO-Entity 변환 설계](./dto-entity-conversion.md) | 의존성 방향, Clean Architecture | OOP, SOLID |
| [Green Software Engineering](./green-software.md) | 탄소 효율적 소프트웨어 설계 | 확장성 |
| [Hexagonal Architecture](./hexagonal-architecture.md) | 포트와 어댑터, 도메인 분리 | Layered Architecture, OOP |
| [Event-Driven Architecture](./event-driven-architecture.md) | 이벤트 기반 통신 | 메시지 큐, MSA |
| [SOA](./soa.md) | 서비스 지향 아키텍처, ESB | Layered Architecture |
| [12-Factor App](./12-factor-app.md) | 클라우드 네이티브 방법론 | Docker |

### [4] 심화

기본 개념을 모두 익힌 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Service Mesh](./service-mesh.md) | Istio, Envoy, 사이드카, mTLS | API Gateway, MSA, 로드밸런싱 |
| [DDD](./ddd.md) | Bounded Context, Aggregate, 도메인 모델 | OOP, MSA, Hexagonal Architecture |
| [대규모 시스템 설계 기초](./large-scale-system.md) | 확장성, 가용성, 일관성 종합 | 캐싱, 메시지 큐, CAP |
| [선착순 쿠폰 시스템](./flash-sale-system.md) | 대규모 동시 요청 처리, 분산 락 | 캐싱, 메시지 큐, 트랜잭션 |

## 전체 목차

### 기초 개념
- [시스템 설계란](./what-is-system-design.md) - 시스템 설계의 정의, 목표, 접근 방법
- [Cloud Computing](./cloud-computing.md) - IaaS/PaaS/SaaS, 클라우드 서비스 모델, 배포 모델
- [Cloud Native](./cloud-native.md) - 컨테이너, 마이크로서비스, 서비스 메시 `Trend 2025`
- [확장성 (Scalability)](./scalability.md) - 수직/수평 확장, 스케일 아웃 전략
- [가용성 (Availability)](./availability.md) - 고가용성(HA), 이중화, 페일오버, 헬스체크, SLA/SLO
- [일관성 (Consistency)](./consistency.md) - 강한 일관성 vs 최종 일관성, CAP 정리

### 패턴 & 전략
- [로드밸런싱 (Load Balancing)](./load-balancing.md) - L4/L7, Round Robin, Health Check, 세션 유지
- [캐싱 (Caching)](./caching.md) - 캐시 전략, 캐시 무효화, Redis/Memcached
- [서킷브레이커 (Circuit Breaker)](./circuit-breaker.md) - 장애 전파 방지 패턴
- [메시지 큐 (Message Queue)](./message-queue.md) - Kafka, RabbitMQ, 이벤트 드리븐 아키텍처
- [Rate Limiting](./rate-limiting.md) - 토큰 버킷, 슬라이딩 윈도우, API 제한
- [외부 API 통합](./external-api-integration.md) - Retry, Backoff, Timeout, Idempotency, Fallback
- [API Gateway](./api-gateway.md) - 인증, 라우팅, Rate Limiting, BFF 패턴

### 인프라
- [Docker](./docker.md) - 컨테이너 플랫폼, 이미지, Dockerfile, Docker Compose
- [Kubernetes](./kubernetes.md) - 컨테이너 오케스트레이션, Pod, Service, Deployment
- [Service Mesh](./service-mesh.md) - Istio, Envoy, 사이드카, mTLS, 분산 추적
- [Serverless](./serverless.md) - FaaS, BaaS, 서버리스 아키텍처 `Trend 2025`
- [Edge Computing](./edge-computing.md) - 엣지 컴퓨팅, CDN, 분산 처리 `Trend 2025`

### 설계
- [DTO-Entity 변환 설계](./dto-entity-conversion.md) - 의존성 방향, Clean Architecture, 도메인 순수성
- [Green Software Engineering](./green-software.md) - 탄소 효율적 소프트웨어 설계, SCI 지표

### 아키텍처
- [Layered Architecture](./layered-architecture.md) - 계층형 아키텍처, Controller-Service-Repository
- [Hexagonal Architecture](./hexagonal-architecture.md) - 포트와 어댑터, 도메인 분리
- [Event-Driven Architecture](./event-driven-architecture.md) - 이벤트 기반 통신, 비동기 처리
- [SOA](./soa.md) - 서비스 지향 아키텍처, ESB
- [12-Factor App](./12-factor-app.md) - 클라우드 네이티브 방법론
- [MSA vs 모놀리식](./msa-vs-monolithic.md) - 마이크로서비스 vs 모놀리식, 장단점, 전환 전략
- [DDD](./ddd.md) - Bounded Context, Aggregate, 도메인 모델, Ubiquitous Language
- [분산 트랜잭션](./distributed-transaction.md) - 2PC, Saga 패턴, 보상 트랜잭션
- [CQRS & 이벤트 소싱](./cqrs-event-sourcing.md) - 명령/조회 분리, 이벤트 스토어

### 사례 연구
- [대규모 시스템 설계 기초](./large-scale-system.md) - 확장성, 가용성, 일관성, 설계 체크리스트
- [선착순 쿠폰 시스템](./flash-sale-system.md) - 대규모 동시 요청 처리, Redis, Kafka, 분산 락

## 작성 예정

*(모든 예정 문서가 작성 완료되었습니다)*
