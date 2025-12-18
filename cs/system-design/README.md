# System Design

시스템 설계 관련 학습 내용을 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 시스템 설계란 (TODO)                                       │
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
| 시스템 설계란 (TODO) | 시스템 설계의 정의, 목표, 접근 방법 | 25분 |

### [2] 입문

시스템 설계 기초를 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [확장성](./scalability.md) | 수직/수평 확장, 스케일 아웃 전략 | 시스템 설계란 |
| [가용성](./availability.md) | 고가용성(HA), 이중화, 페일오버, SLA/SLO | 시스템 설계란 |
| [일관성](./consistency.md) | 강한/최종 일관성, CAP 정리 | 시스템 설계란 |

### [3] 중급

기초 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [캐싱](./caching.md) | 캐시 전략, 캐시 무효화, Redis | 확장성 |
| [서킷브레이커](./circuit-breaker.md) | 장애 전파 방지 패턴 | 가용성 |
| [메시지 큐](./message-queue.md) | Kafka, RabbitMQ, 이벤트 드리븐 | 확장성, 가용성 |
| [Docker](./docker.md) | 컨테이너, 이미지, Dockerfile | OS 기초 |
| [Kubernetes](./kubernetes.md) | 컨테이너 오케스트레이션, Pod, Service | Docker |
| [DTO-Entity 변환 설계](./dto-entity-conversion.md) | 의존성 방향, Clean Architecture | OOP, SOLID |
| [Green Software Engineering](./green-software.md) | 탄소 효율적 소프트웨어 설계 | 확장성 |

### [4] 심화

기본 개념을 모두 익힌 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [대규모 시스템 설계 기초](./large-scale-system.md) | 확장성, 가용성, 일관성 종합 | 캐싱, 메시지 큐, CAP |
| [선착순 쿠폰 시스템](./flash-sale-system.md) | 대규모 동시 요청 처리, 분산 락 | 캐싱, 메시지 큐, 트랜잭션 |

## 전체 목차

### 기초 개념
- [확장성 (Scalability)](./scalability.md) - 수직/수평 확장, 스케일 아웃 전략
- [가용성 (Availability)](./availability.md) - 고가용성(HA), 이중화, 페일오버, 헬스체크, SLA/SLO
- [일관성 (Consistency)](./consistency.md) - 강한 일관성 vs 최종 일관성, CAP 정리

### 패턴 & 전략
- [캐싱 (Caching)](./caching.md) - 캐시 전략, 캐시 무효화, Redis/Memcached
- [서킷브레이커 (Circuit Breaker)](./circuit-breaker.md) - 장애 전파 방지 패턴
- [메시지 큐 (Message Queue)](./message-queue.md) - Kafka, RabbitMQ, 이벤트 드리븐 아키텍처

### 인프라
- [Docker](./docker.md) - 컨테이너 플랫폼, 이미지, Dockerfile, Docker Compose
- [Kubernetes](./kubernetes.md) - 컨테이너 오케스트레이션, Pod, Service, Deployment

### 설계
- [DTO-Entity 변환 설계](./dto-entity-conversion.md) - 의존성 방향, Clean Architecture, 도메인 순수성
- [Green Software Engineering](./green-software.md) - 탄소 효율적 소프트웨어 설계, SCI 지표

### 사례 연구
- [대규모 시스템 설계 기초](./large-scale-system.md) - 확장성, 가용성, 일관성, 설계 체크리스트
- [선착순 쿠폰 시스템](./flash-sale-system.md) - 대규모 동시 요청 처리, Redis, Kafka, 분산 락

## 작성 예정

- [ ] 시스템 설계란 - 시스템 설계의 정의, 목표, 접근 방법
- [ ] Rate Limiting - 토큰 버킷, 슬라이딩 윈도우
- [ ] 분산 트랜잭션 - 2PC, Saga 패턴
- [ ] MSA vs 모놀리식 - 장단점, 전환 전략
- [ ] CQRS & 이벤트 소싱 - 명령/조회 분리
