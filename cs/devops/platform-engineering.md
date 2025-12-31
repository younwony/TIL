# Platform Engineering

> `[3] 중급` · 선수 지식: [CI/CD](./ci-cd.md), [IaC](./iac.md)

> `Trend` 2025

> 개발자가 셀프서비스로 인프라와 도구를 사용할 수 있도록 내부 개발자 플랫폼(IDP)을 구축하고 운영하는 엔지니어링 분야

`#플랫폼엔지니어링` `#PlatformEngineering` `#IDP` `#InternalDeveloperPlatform` `#내부개발자플랫폼` `#DevEx` `#DeveloperExperience` `#개발자경험` `#셀프서비스` `#SelfService` `#GoldenPath` `#골든패스` `#DevOps` `#데브옵스` `#Backstage` `#Port` `#Humanitec` `#Crossplane` `#Kubernetes` `#클라우드네이티브` `#CloudNative` `#InfrastructureAbstraction` `#인프라추상화` `#CNCF` `#ServiceCatalog` `#서비스카탈로그` `#Scaffolding` `#TemplateEngine`

## 왜 알아야 하는가?

- **실무**: 55%의 글로벌 조직이 이미 Platform Engineering을 채택했으며, 90%가 더 많은 팀으로 확대 중. 대규모 조직에서 개발 생산성 향상의 핵심 전략
- **면접**: DevOps의 진화 방향으로 주목받으며, "DevOps vs Platform Engineering" 비교 질문 빈출
- **기반 지식**: 클라우드 네이티브 환경에서 개발자 경험(DevEx)과 조직 확장성을 이해하는 필수 개념

## 핵심 개념

- **IDP(Internal Developer Platform)**: 개발자가 셀프서비스로 인프라, 배포, 모니터링 등을 수행할 수 있는 통합 플랫폼
- **Golden Path**: 검증된 모범 사례를 따르는 표준화된 개발/배포 경로
- **Developer Experience(DevEx)**: 개발자가 업무를 수행하는 데 있어 느끼는 전반적인 경험과 만족도

## 쉽게 이해하기

**쇼핑몰 비유**

기존 DevOps 환경은 마트에서 직접 재료를 사서 요리하는 것과 같습니다. Kubernetes 설정, CI/CD 파이프라인, 모니터링 구성 등 모든 것을 개발자가 직접 해야 합니다.

Platform Engineering은 밀키트(Meal Kit) 서비스와 같습니다. 필요한 재료가 레시피와 함께 준비되어 있고, 개발자는 조리만 하면 됩니다. "서비스 배포하고 싶어요" 버튼 하나로 인프라 프로비저닝, CI/CD 설정, 모니터링까지 자동으로 구성됩니다.

```
[기존 DevOps]
개발자 → Kubernetes YAML 작성 → CI/CD 설정 → 모니터링 구성 → 배포
        (각 단계마다 학습 필요, 팀마다 다른 방식)

[Platform Engineering]
개발자 → IDP 포털에서 "Spring Boot 서비스 생성" 클릭 → 완료
        (Golden Path를 따라 표준화된 방식으로 자동 구성)
```

## 상세 설명

### DevOps의 한계와 Platform Engineering의 등장

DevOps는 개발과 운영의 장벽을 허물었지만, 규모가 커지면서 새로운 문제가 발생했습니다.

```
[DevOps의 확장 문제]

팀 A: Kubernetes + Helm + ArgoCD + Prometheus
팀 B: Kubernetes + Kustomize + Jenkins + Datadog
팀 C: ECS + CloudFormation + GitHub Actions + CloudWatch
           ↓
┌─────────────────────────────────────────────────┐
│ 문제점:                                         │
│ - 팀마다 다른 도구와 방식                       │
│ - 인프라 지식이 필요한 모든 개발자              │
│ - 중복된 작업과 휠의 재발명                     │
│ - 보안/컴플라이언스 일관성 부재                 │
│ - 신규 입사자 온보딩에 수주 소요                │
└─────────────────────────────────────────────────┘
```

**왜 이렇게 되었는가?**

DevOps의 "You build it, you run it" 원칙은 개발자에게 전체 스택에 대한 책임을 부여했습니다. 하지만 클라우드 네이티브 환경이 복잡해지면서 개발자의 인지 부하(Cognitive Load)가 급격히 증가했습니다.

### Internal Developer Platform (IDP)

IDP는 개발자 셀프서비스를 가능하게 하는 플랫폼입니다.

```
┌─────────────────────────────────────────────────────────────┐
│                    Internal Developer Platform               │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Developer Portal (UI)                   │    │
│  │  - 서비스 카탈로그    - 문서/가이드                 │    │
│  │  - 템플릿 기반 생성   - 대시보드                    │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Orchestration Layer                     │    │
│  │  - 워크플로우 자동화  - 정책 적용                   │    │
│  │  - 리소스 프로비저닝  - 시크릿 관리                 │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Infrastructure Layer                    │    │
│  │  - Kubernetes        - Cloud Services               │    │
│  │  - CI/CD Pipelines   - Observability                │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Golden Path

Golden Path는 조직에서 검증된 모범 사례를 따르는 표준화된 개발 경로입니다.

```yaml
# Golden Path 예시: Spring Boot 서비스
golden_path:
  name: "spring-boot-service"
  includes:
    - repository:
        template: "spring-boot-starter"
        ci_cd: "github-actions"
    - infrastructure:
        runtime: "kubernetes"
        namespace: "auto-generated"
        resources: "standard-tier"
    - observability:
        logging: "elk-stack"
        metrics: "prometheus"
        tracing: "jaeger"
    - security:
        scanning: "snyk"
        secrets: "vault"
```

**왜 Golden Path인가?**

- 강제가 아닌 권장: 개발자가 표준을 벗어날 수 있지만, 표준을 따르면 지원을 받음
- 검증된 방식: 보안, 성능, 운영 측면에서 검증된 구성
- 빠른 시작: 신규 서비스를 몇 분 만에 프로덕션 준비 상태로 만듦

### 주요 IDP 도구

| 도구 | 특징 | 적합한 조직 |
|------|------|------------|
| **Backstage** | Spotify 개발, 오픈소스, 커스터마이징 유연 | 엔지니어링 역량 높은 조직 |
| **Port** | SaaS, 빠른 도입, 낮은 진입장벽 | 빠른 도입 원하는 조직 |
| **Humanitec** | 스코어 기반, 동적 구성 관리 | 복잡한 멀티클라우드 환경 |
| **Crossplane** | Kubernetes 네이티브 IaC | Kubernetes 중심 조직 |

## 동작 원리

### Platform Engineering 워크플로우

```
[개발자 요청]
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. Developer Portal 접속                                    │
│    - 서비스 카탈로그에서 "Spring Boot API" 템플릿 선택      │
│    - 서비스명, 팀, 환경 입력                                │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Orchestration Layer 처리                                 │
│    - 템플릿 기반 Git 리포지토리 생성                        │
│    - CI/CD 파이프라인 자동 구성                             │
│    - 정책(RBAC, 네트워크, 리소스 제한) 적용                 │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. Infrastructure Provisioning                              │
│    - Kubernetes Namespace 생성                              │
│    - 데이터베이스, 캐시 등 의존성 프로비저닝                │
│    - 모니터링/알림 설정                                     │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
[개발자: 비즈니스 로직 개발에 집중]
```

### Backstage 아키텍처 예시

```
┌────────────────────────────────────────────────────────────────┐
│                         Backstage                               │
├────────────────────────────────────────────────────────────────┤
│  Frontend (React)                                               │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │ Catalog  │ │ Scaffold │ │ TechDocs │ │ Search   │           │
│  │ Plugin   │ │ Plugin   │ │ Plugin   │ │ Plugin   │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
├────────────────────────────────────────────────────────────────┤
│  Backend (Node.js)                                              │
│  ┌──────────────────────────────────────────────────────┐      │
│  │                   Plugin System                       │      │
│  │  - Catalog Backend    - Scaffolder Backend           │      │
│  │  - Auth Backend       - Search Backend               │      │
│  └──────────────────────────────────────────────────────┘      │
├────────────────────────────────────────────────────────────────┤
│  Integrations                                                   │
│  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐        │
│  │ GitHub │ │  K8s   │ │ ArgoCD │ │PagerDuty│ │ Vault  │       │
│  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘        │
└────────────────────────────────────────────────────────────────┘
```

## 예제 코드

### Backstage 서비스 템플릿 (Software Template)

```yaml
# template.yaml - 새 서비스 생성 템플릿
apiVersion: scaffolder.backstage.io/v1beta3
kind: Template
metadata:
  name: spring-boot-service
  title: Spring Boot Service
  description: Spring Boot 마이크로서비스 생성
  tags:
    - java
    - spring-boot
    - recommended
spec:
  owner: platform-team
  type: service

  parameters:
    - title: 서비스 정보
      required:
        - serviceName
        - owner
      properties:
        serviceName:
          title: 서비스명
          type: string
          pattern: '^[a-z][a-z0-9-]*$'
        owner:
          title: 소유 팀
          type: string
          ui:field: OwnerPicker
        description:
          title: 설명
          type: string

    - title: 인프라 설정
      properties:
        database:
          title: 데이터베이스
          type: string
          enum:
            - none
            - postgresql
            - mysql
          default: none
        cacheEnabled:
          title: Redis 캐시 사용
          type: boolean
          default: false

  steps:
    - id: fetch-template
      name: 템플릿 가져오기
      action: fetch:template
      input:
        url: ./skeleton
        values:
          serviceName: ${{ parameters.serviceName }}
          owner: ${{ parameters.owner }}

    - id: create-repo
      name: GitHub 리포지토리 생성
      action: publish:github
      input:
        repoUrl: github.com?repo=${{ parameters.serviceName }}

    - id: create-argocd-app
      name: ArgoCD 애플리케이션 생성
      action: argocd:create-resources
      input:
        appName: ${{ parameters.serviceName }}

    - id: register-catalog
      name: 카탈로그 등록
      action: catalog:register
      input:
        repoContentsUrl: ${{ steps['create-repo'].output.repoContentsUrl }}

  output:
    links:
      - title: Repository
        url: ${{ steps['create-repo'].output.remoteUrl }}
      - title: Open in catalog
        icon: catalog
        entityRef: ${{ steps['register-catalog'].output.entityRef }}
```

### Crossplane으로 인프라 추상화

```yaml
# composition.yaml - 애플리케이션 인프라 구성
apiVersion: apiextensions.crossplane.io/v1
kind: Composition
metadata:
  name: app-infrastructure
spec:
  compositeTypeRef:
    apiVersion: platform.example.com/v1alpha1
    kind: Application
  resources:
    # Kubernetes Namespace
    - name: namespace
      base:
        apiVersion: kubernetes.crossplane.io/v1alpha1
        kind: Object
        spec:
          forProvider:
            manifest:
              apiVersion: v1
              kind: Namespace

    # PostgreSQL Database
    - name: database
      base:
        apiVersion: database.aws.crossplane.io/v1beta1
        kind: RDSInstance
        spec:
          forProvider:
            dbInstanceClass: db.t3.micro
            engine: postgres
            engineVersion: "14"

    # Redis Cache
    - name: cache
      base:
        apiVersion: cache.aws.crossplane.io/v1beta1
        kind: ReplicationGroup
        spec:
          forProvider:
            cacheNodeType: cache.t3.micro
            engine: redis
```

```yaml
# 개발자가 사용하는 간단한 리소스 정의
apiVersion: platform.example.com/v1alpha1
kind: Application
metadata:
  name: order-service
spec:
  team: commerce
  tier: standard  # standard, premium, enterprise
  database: true
  cache: true
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 개발자 생산성 향상 (71%가 time-to-market 가속) | 초기 구축에 상당한 투자 필요 (6-12개월) |
| 일관된 보안/컴플라이언스 적용 | Platform Team 인력 확보 필요 |
| 인프라 인지 부하 감소 | 과도한 추상화로 유연성 저하 위험 |
| 신규 입사자 온보딩 시간 단축 | Golden Path가 모든 케이스를 커버하지 못함 |
| 중복 작업 제거, 휠 재발명 방지 | 플랫폼 자체가 병목이 될 수 있음 |

## DevOps vs Platform Engineering

```
┌────────────────────┬─────────────────────┬─────────────────────┐
│       측면         │      DevOps         │ Platform Engineering│
├────────────────────┼─────────────────────┼─────────────────────┤
│ 초점              │ 문화와 협업          │ 제품과 셀프서비스    │
│ 인프라 책임       │ 개발팀 (분산)        │ 플랫폼팀 (중앙화)    │
│ 도구 선택         │ 팀별 자율            │ 표준화된 Golden Path│
│ 개발자 역할       │ 풀스택 책임          │ 비즈니스 로직 집중   │
│ 확장성            │ 팀 증가시 복잡도 증가│ 플랫폼이 복잡도 흡수│
│ 인지 부하         │ 높음                 │ 낮음                 │
└────────────────────┴─────────────────────┴─────────────────────┘
```

**주의**: Platform Engineering은 DevOps를 대체하는 것이 아니라 **진화**입니다. DevOps의 문화와 원칙 위에 플랫폼 제품화를 추가한 것입니다.

## 트러블슈팅

### 사례 1: 플랫폼 채택률이 낮음

#### 증상
- 개발팀이 IDP 대신 기존 방식 고수
- Golden Path 사용률 30% 미만
- "우리 팀 상황에는 안 맞아요" 피드백

#### 원인 분석
- 개발자 니즈를 반영하지 않은 플랫폼 설계
- Golden Path가 너무 제한적
- 탈출구(Escape Hatch) 부재

#### 해결 방법
```
1. 개발자 페르소나 정의 및 인터뷰 수행
2. MVP 먼저 출시 후 피드백 기반 개선
3. Golden Path 외 커스텀 옵션 제공 (단, 지원 수준 차등화)
4. 플랫폼팀에 Product Manager 역할 도입
```

#### 예방 조치
- 분기별 개발자 만족도 조사 (NPS)
- 플랫폼 사용량 메트릭 모니터링
- 정기적인 개발자 피드백 세션

### 사례 2: 플랫폼이 병목이 됨

#### 증상
- 새 기능 요청이 수주간 대기
- 플랫폼팀 번아웃
- 개발팀이 플랫폼 우회 시도

#### 원인 분석
- 플랫폼팀 인력 부족
- Self-service가 아닌 ticket-service
- 플랫폼 확장성 설계 미흡

#### 해결 방법
```yaml
# Self-service 비율 목표
metrics:
  self_service_ratio: 80%  # 티켓 없이 개발자가 직접 처리
  ticket_resolution_time: 24h  # 나머지 20% 케이스
  platform_team_ratio: 1:50  # 플랫폼 엔지니어 : 개발자
```

## 면접 예상 질문

### Q: DevOps와 Platform Engineering의 차이는 무엇인가요?

A: DevOps는 **문화와 협업**에 초점을 맞춰 개발팀이 인프라까지 책임지는 방식입니다. 하지만 조직이 커지면 각 팀이 서로 다른 도구와 방식을 사용하게 되고, 개발자의 인지 부하가 높아집니다.

Platform Engineering은 DevOps를 **제품화**한 것입니다. 전담 Platform Team이 IDP를 구축하여 개발자가 셀프서비스로 표준화된 방식으로 인프라를 사용할 수 있게 합니다. 이를 통해 개발자는 비즈니스 로직에 집중하고, 조직은 일관된 보안과 컴플라이언스를 유지할 수 있습니다.

### Q: Golden Path란 무엇이고 왜 중요한가요?

A: Golden Path는 조직에서 검증된 모범 사례를 따르는 표준화된 개발 경로입니다. "Spring Boot 서비스는 이렇게 만든다"는 레시피와 같습니다.

중요한 이유는:
1. **빠른 시작**: 신규 서비스를 분 단위로 프로덕션 준비 상태로
2. **검증된 구성**: 보안, 성능, 운영 관점에서 검증됨
3. **강제가 아닌 권장**: 개발자 자율성 보장하면서 표준 제공

단, Golden Path만 강제하면 유연성이 떨어지므로, 탈출구(Escape Hatch)를 제공하되 지원 수준을 차등화하는 것이 좋습니다.

### Q: Platform Engineering 도입 시 흔한 실패 요인은?

A: 가장 흔한 실패 요인은:

1. **제품 마인드셋 부재**: 내부 개발자를 고객으로 보지 않고 인프라팀 관점으로 접근
2. **과도한 추상화**: 개발자가 필요로 하는 유연성을 제거하여 채택률 저하
3. **Self-service 미달성**: 여전히 티켓 기반으로 운영되어 병목 발생
4. **개발자 피드백 무시**: 실제 사용자 니즈와 동떨어진 플랫폼 구축

성공을 위해서는 Platform Team에 Product Manager 역할을 두고, MVP로 시작해 피드백 기반으로 점진적 개선하는 것이 중요합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 선수 지식 - 지속적 통합/배포 기초 | 입문 |
| [IaC](./iac.md) | 선수 지식 - 인프라 코드화 | 중급 |
| [Docker](../system-design/docker.md) | 관련 기술 - 컨테이너 기초 | 입문 |
| [Kubernetes](../system-design/kubernetes.md) | 관련 기술 - 오케스트레이션 | 중급 |
| [MSA vs Monolithic](../system-design/msa-vs-monolithic.md) | 관련 개념 - 마이크로서비스 아키텍처 | 중급 |
| [모니터링](./monitoring.md) | 관련 기술 - 관찰 가능성 | 중급 |

## 참고 자료

- [Google Cloud - Platform Engineering Study (2025)](https://cloud.google.com/blog/products/application-development/platform-engineering-study)
- [CNCF - Platform Engineering White Paper](https://tag-app-delivery.cncf.io/whitepapers/platforms/)
- [Backstage Official Documentation](https://backstage.io/docs)
- [Gartner - Top Strategic Trends in Software Engineering 2025](https://www.gartner.com/en/newsroom/press-releases/2025-07-01-gartner-identifies-the-top-strategic-trends-in-software-engineering-for-2025-and-beyond)
- [InfoQ - Cloud and DevOps Trends 2025](https://www.infoq.com/articles/cloud-devops-trends-2025/)
- [Team Topologies - Platform as a Product](https://teamtopologies.com/key-concepts)
