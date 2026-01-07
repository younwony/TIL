# GitOps

> `[4] 심화` · 선수 지식: [CI/CD](./ci-cd.md), [IaC](./iac.md)

> `Trend` 2025

> Git을 단일 진실 공급원(Single Source of Truth)으로 사용하여 인프라와 애플리케이션 배포를 선언적으로 관리하는 운영 패러다임

`#GitOps` `#깃옵스` `#ArgoCD` `#FluxCD` `#Flux` `#Kubernetes` `#K8s` `#쿠버네티스` `#선언적배포` `#DeclarativeDeployment` `#SSOT` `#SingleSourceOfTruth` `#Git` `#IaC` `#InfrastructureAsCode` `#지속적배포` `#ContinuousDeployment` `#풀기반배포` `#PullBasedDeployment` `#Reconciliation` `#자동동기화` `#Drift` `#드리프트` `#WeaveWorks` `#CNCF` `#클라우드네이티브` `#CloudNative` `#Helm` `#Kustomize` `#매니페스트`

## 왜 알아야 하는가?

- **실무**: Kubernetes 환경의 표준 배포 방식. 2025년 기업의 75%가 GitOps 도입 검토 중
- **면접**: "배포 파이프라인 설계", "인프라 관리 방식" 질문에서 GitOps 이해도 평가
- **기반 지식**: 클라우드 네이티브, Platform Engineering, SRE의 핵심 요소

## 핵심 개념

- **단일 진실 공급원 (SSOT)**: Git 저장소가 인프라/앱의 원하는 상태(Desired State)를 정의
- **선언적 구성**: "어떻게"가 아닌 "무엇을" 원하는지 명시 (YAML/Helm/Kustomize)
- **자동 동기화 (Reconciliation)**: Git 상태와 클러스터 상태를 자동으로 일치시킴
- **Pull 기반 배포**: CI가 클러스터에 직접 배포하지 않고, 클러스터가 Git을 감시하여 변경 감지

## 쉽게 이해하기

**Git = 설계도면, Kubernetes = 공장**

전통적인 방식은 설계도면을 들고 공장에 직접 가서 "이렇게 만들어"라고 지시합니다.
GitOps는 설계도면을 게시판에 붙여두면, 공장이 주기적으로 게시판을 확인하고 스스로 설계대로 제품을 만듭니다.

- 설계가 바뀌면? → 게시판만 업데이트 → 공장이 알아서 변경
- 누군가 공장에서 임의로 수정해도? → 게시판과 다르면 다시 원래대로 복구

## 상세 설명

### 기존 CI/CD vs GitOps

```
┌─────────────────────────────────────────────────────────────────┐
│              Push-based (기존 CI/CD) vs Pull-based (GitOps)       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [Push-based] 기존 CI/CD                                         │
│  ┌──────┐     ┌──────┐     ┌──────────────┐                     │
│  │ Dev  │────▶│  CI  │────▶│  Kubernetes  │                     │
│  │      │     │Server│     │   Cluster    │                     │
│  └──────┘     └──────┘     └──────────────┘                     │
│                   │                                              │
│              kubectl apply                                       │
│              (CI가 직접 배포)                                     │
│                                                                  │
│  문제점:                                                         │
│  - CI 서버에 클러스터 접근 권한 필요 (보안 위험)                   │
│  - 클러스터 상태와 Git 상태 불일치 가능                           │
│  - 수동 변경 시 추적 어려움                                       │
│                                                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [Pull-based] GitOps                                             │
│  ┌──────┐     ┌──────┐     ┌──────────────────────────────┐     │
│  │ Dev  │────▶│ Git  │◀────│  Kubernetes Cluster          │     │
│  │      │     │ Repo │     │  ┌────────────────────────┐  │     │
│  └──────┘     └──────┘     │  │  GitOps Operator       │  │     │
│                            │  │  (ArgoCD / FluxCD)     │  │     │
│                            │  └────────────────────────┘  │     │
│                            └──────────────────────────────┘     │
│                                      │                           │
│                             주기적으로 Git 확인                   │
│                             (Operator가 Pull)                    │
│                                                                  │
│  장점:                                                           │
│  - CI 서버에 클러스터 권한 불필요                                 │
│  - Git이 유일한 진실 (감사 추적 완벽)                             │
│  - 자동 복구 (Drift Detection)                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**왜 Pull 기반이 더 안전한가?**

Push 기반은 CI 서버가 해킹되면 공격자가 클러스터에 직접 접근할 수 있습니다.
Pull 기반은 클러스터 내부의 Operator만 Git을 읽으므로, 외부에서 클러스터로의 접근 경로가 없습니다.

### GitOps 4대 원칙

| 원칙 | 설명 | 예시 |
|------|------|------|
| **선언적 (Declarative)** | 원하는 상태를 코드로 선언 | YAML, Helm Chart, Kustomize |
| **버전 관리 (Versioned)** | 모든 변경은 Git 커밋으로 추적 | git log로 변경 이력 확인 |
| **자동 적용 (Automated)** | 승인된 변경은 자동으로 클러스터에 반영 | ArgoCD Sync |
| **자동 복구 (Self-healing)** | 실제 상태가 Git과 다르면 자동 복구 | Drift Detection |

### 주요 GitOps 도구

| 도구 | 특징 | CNCF 상태 | 적합한 환경 |
|------|------|-----------|-------------|
| **ArgoCD** | Web UI 우수, 직관적 | Graduated | 중대규모 팀, 시각화 중시 |
| **FluxCD** | 경량, CLI 중심 | Graduated | 소규모 팀, 자동화 중시 |
| **Jenkins X** | Jenkins 기반 | - | 기존 Jenkins 사용 팀 |
| **Rancher Fleet** | 멀티 클러스터 특화 | - | 대규모 멀티 클러스터 |

### GitOps 저장소 구조

```
┌─────────────────────────────────────────────────────────────────┐
│                    권장 저장소 구조 (모노레포)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  gitops-config/                                                  │
│  ├── apps/                    # 애플리케이션 매니페스트            │
│  │   ├── production/                                             │
│  │   │   ├── api-server/                                        │
│  │   │   │   ├── deployment.yaml                                │
│  │   │   │   ├── service.yaml                                   │
│  │   │   │   └── kustomization.yaml                             │
│  │   │   └── web-frontend/                                      │
│  │   │       └── ...                                            │
│  │   └── staging/                                                │
│  │       └── ...                                                │
│  ├── infrastructure/          # 인프라 리소스                     │
│  │   ├── cert-manager/                                          │
│  │   ├── ingress-nginx/                                         │
│  │   └── monitoring/                                            │
│  └── clusters/                # 클러스터별 설정                   │
│      ├── production/                                             │
│      │   └── kustomization.yaml                                 │
│      └── staging/                                                │
│          └── kustomization.yaml                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**왜 앱 코드와 배포 설정을 분리하는가?**

- **보안**: 앱 개발자에게 인프라 저장소 권한을 주지 않아도 됨
- **속도**: 배포 설정 변경 시 앱 빌드 불필요
- **감사**: 인프라 변경 이력을 독립적으로 추적

## 동작 원리

### ArgoCD 동기화 플로우

```
┌─────────────────────────────────────────────────────────────────┐
│                    ArgoCD 동기화 플로우                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. 개발자가 Git에 변경 Push                                      │
│     ┌──────┐     ┌──────────┐                                   │
│     │ Dev  │────▶│   Git    │                                   │
│     └──────┘     │   Repo   │                                   │
│                  └────┬─────┘                                   │
│                       │                                          │
│  2. ArgoCD가 변경 감지 (3분 주기 또는 Webhook)                    │
│                       ▼                                          │
│     ┌─────────────────────────────────────────┐                 │
│     │              ArgoCD                      │                 │
│     │  ┌─────────────────────────────────┐    │                 │
│     │  │     Application Controller      │    │                 │
│     │  │  - Git 상태 (Desired State)     │    │                 │
│     │  │  - 클러스터 상태 (Actual State) │    │                 │
│     │  │  - 비교 (Diff)                  │    │                 │
│     │  └─────────────────────────────────┘    │                 │
│     └─────────────────┬───────────────────────┘                 │
│                       │                                          │
│  3. 상태 불일치 시 동기화                                         │
│                       ▼                                          │
│     ┌─────────────────────────────────────────┐                 │
│     │          Kubernetes Cluster             │                 │
│     │  ┌─────────┐  ┌─────────┐  ┌─────────┐ │                 │
│     │  │   Pod   │  │ Service │  │ Ingress │ │                 │
│     │  └─────────┘  └─────────┘  └─────────┘ │                 │
│     └─────────────────────────────────────────┘                 │
│                                                                  │
│  4. 상태 보고 (OutOfSync / Synced / Healthy)                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Drift Detection (드리프트 감지)

```
┌─────────────────────────────────────────────────────────────────┐
│                    Drift Detection 시나리오                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  시나리오: 운영자가 kubectl로 직접 replica 수정                    │
│                                                                  │
│  Git (Desired State)          Cluster (Actual State)             │
│  ┌────────────────┐           ┌────────────────┐                │
│  │ replicas: 3    │           │ replicas: 5    │  ← 수동 변경    │
│  └────────────────┘           └────────────────┘                │
│           │                            │                         │
│           └──────────┬─────────────────┘                         │
│                      ▼                                           │
│              ┌───────────────┐                                   │
│              │   ArgoCD      │                                   │
│              │ Drift 감지!   │                                   │
│              └───────┬───────┘                                   │
│                      │                                           │
│         ┌────────────┴────────────┐                             │
│         ▼                         ▼                             │
│  [자동 복구 모드]          [알림만 모드]                          │
│  replicas: 5 → 3          "OutOfSync" 경고                       │
│  (Self-Healing)           (수동 확인 후 결정)                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 예제 코드

### ArgoCD Application 정의

```yaml
# argocd-application.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: api-server
  namespace: argocd
spec:
  project: default

  # Git 소스 설정
  source:
    repoURL: https://github.com/myorg/gitops-config.git
    targetRevision: main
    path: apps/production/api-server

  # 배포 대상 클러스터
  destination:
    server: https://kubernetes.default.svc
    namespace: production

  # 동기화 정책
  syncPolicy:
    automated:
      prune: true          # Git에서 삭제된 리소스 자동 삭제
      selfHeal: true       # 수동 변경 시 자동 복구
    syncOptions:
      - CreateNamespace=true
    retry:
      limit: 5
      backoff:
        duration: 5s
        factor: 2
        maxDuration: 3m
```

### Kustomize 기반 환경별 배포

```yaml
# base/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api-server
spec:
  replicas: 1  # base 값
  selector:
    matchLabels:
      app: api-server
  template:
    metadata:
      labels:
        app: api-server
    spec:
      containers:
        - name: api-server
          image: myorg/api-server:latest
          resources:
            requests:
              memory: "256Mi"
              cpu: "100m"
```

```yaml
# overlays/production/kustomization.yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
namespace: production
resources:
  - ../../base
patches:
  - patch: |-
      - op: replace
        path: /spec/replicas
        value: 5
    target:
      kind: Deployment
      name: api-server
images:
  - name: myorg/api-server
    newTag: v1.2.3  # 프로덕션 버전
```

### FluxCD HelmRelease

```yaml
# helm-release.yaml
apiVersion: helm.toolkit.fluxcd.io/v2beta1
kind: HelmRelease
metadata:
  name: redis
  namespace: cache
spec:
  interval: 5m
  chart:
    spec:
      chart: redis
      version: "17.x"
      sourceRef:
        kind: HelmRepository
        name: bitnami
        namespace: flux-system
  values:
    architecture: replication
    auth:
      enabled: true
      existingSecret: redis-secret
    replica:
      replicaCount: 3
```

### 이미지 자동 업데이트 (FluxCD Image Automation)

```yaml
# image-update-automation.yaml
apiVersion: image.toolkit.fluxcd.io/v1beta1
kind: ImageUpdateAutomation
metadata:
  name: api-server-automation
  namespace: flux-system
spec:
  interval: 1m
  sourceRef:
    kind: GitRepository
    name: gitops-config
  git:
    checkout:
      ref:
        branch: main
    commit:
      author:
        email: fluxbot@example.com
        name: FluxBot
      messageTemplate: |
        chore: update image {{range .Updated.Images}}{{println .}}{{end}}
    push:
      branch: main
  update:
    path: ./apps
    strategy: Setters
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| Git이 유일한 진실 (감사 추적 완벽) | 학습 곡선 (ArgoCD, Flux 등 새 도구) |
| 롤백이 `git revert`로 간단 | 시크릿 관리 복잡 (Sealed Secrets 등 필요) |
| Pull Request로 변경 검토 | Git 저장소 장애 시 배포 불가 |
| CI 서버에 클러스터 권한 불필요 | 초기 설정 복잡 |
| 드리프트 자동 감지/복구 | 빠른 핫픽스 배포 시 절차 필요 |

### 시크릿 관리 전략

```
┌─────────────────────────────────────────────────────────────────┐
│                   GitOps 시크릿 관리 옵션                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. Sealed Secrets (Bitnami)                                     │
│     - 공개키로 암호화된 시크릿을 Git에 저장                        │
│     - 클러스터 내 컨트롤러가 복호화                                │
│                                                                  │
│  2. External Secrets Operator                                    │
│     - AWS Secrets Manager, HashiCorp Vault 등 외부 참조          │
│     - Git에는 참조만 저장                                        │
│                                                                  │
│  3. SOPS (Mozilla)                                               │
│     - 파일 일부만 암호화 (키:값 중 값만)                          │
│     - 가독성 유지하면서 보안                                      │
│                                                                  │
│  권장: External Secrets Operator + AWS Secrets Manager           │
│        (Git에 시크릿 자체를 저장하지 않음)                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 트러블슈팅

### 사례 1: Sync Failed - OutOfSync 상태 지속

#### 증상
```
Application: api-server
Status: OutOfSync
Sync Result: Failed
Message: one or more objects failed to apply
```

#### 원인 분석
- YAML 문법 오류 또는 유효하지 않은 Kubernetes 리소스
- 권한 부족 (RBAC)
- 이미 존재하는 리소스와 충돌

#### 해결 방법
```bash
# 1. ArgoCD에서 상세 로그 확인
argocd app get api-server --show-operation

# 2. 매니페스트 유효성 검사
kubectl apply --dry-run=client -f deployment.yaml

# 3. 강제 동기화 (주의: 기존 리소스 덮어쓰기)
argocd app sync api-server --force
```

#### 예방 조치
- CI에서 `kubectl apply --dry-run=server` 검증 추가
- ArgoCD Webhook 설정으로 빠른 피드백

### 사례 2: 이미지 업데이트가 반영되지 않음

#### 증상
새 이미지를 레지스트리에 푸시했지만 클러스터에 배포되지 않음

#### 원인 분석
- Git 저장소의 이미지 태그가 업데이트되지 않음
- Image Automation이 설정되지 않음

#### 해결 방법
```bash
# 방법 1: 수동으로 Git에 태그 업데이트 후 Push
# 방법 2: FluxCD Image Automation 설정
# 방법 3: ArgoCD Image Updater 사용

# ArgoCD Image Updater annotation
kubectl annotate app api-server \
  argocd-image-updater.argoproj.io/image-list=api=myorg/api-server \
  argocd-image-updater.argoproj.io/api.update-strategy=latest
```

## 면접 예상 질문

### Q: GitOps와 기존 CI/CD의 차이점은?

A: **배포 방식(Push vs Pull)**과 **진실의 원천**이 다릅니다.

기존 CI/CD는 CI 서버가 클러스터에 직접 배포(Push)합니다. GitOps는 클러스터 내 Operator가 Git을 주기적으로 확인하고 변경을 가져옵니다(Pull).

GitOps의 핵심은 Git이 유일한 진실의 원천이라는 점입니다. 클러스터 상태가 Git과 다르면 자동으로 Git 상태로 복구합니다. 이로 인해 감사 추적이 완벽하고, 롤백이 `git revert`만으로 가능합니다.

### Q: Pull 기반이 Push 기반보다 안전한 이유는?

A: **공격 표면(Attack Surface)**이 줄어들기 때문입니다.

Push 기반은 CI 서버가 클러스터에 대한 쓰기 권한을 가져야 합니다. CI 서버가 해킹되면 클러스터도 위험합니다.

Pull 기반은 클러스터 내부의 Operator만 Git을 읽습니다. 외부에서 클러스터로 들어오는 경로 자체가 없습니다. Git 저장소 접근 권한만 관리하면 됩니다.

### Q: GitOps에서 시크릿은 어떻게 관리하는가?

A: **Git에 평문 시크릿을 저장하면 안 됩니다.** 세 가지 전략이 있습니다:

1. **Sealed Secrets**: 공개키로 암호화하여 Git에 저장, 클러스터에서 복호화
2. **External Secrets Operator**: AWS Secrets Manager 등 외부 저장소 참조만 Git에 저장
3. **SOPS**: 파일 내 값만 암호화, 키는 평문으로 가독성 유지

권장하는 방식은 External Secrets Operator입니다. 시크릿 자체가 Git에 없으므로 가장 안전합니다.

### Q: ArgoCD와 FluxCD 중 어떤 것을 선택해야 하는가?

A: **팀 규모와 선호도**에 따라 다릅니다.

- **ArgoCD**: Web UI가 직관적, 시각적 모니터링 중시, 중대규모 팀에 적합
- **FluxCD**: CLI 중심, 경량, 완전 자동화 선호, 소규모 팀에 적합

둘 다 CNCF Graduated 프로젝트로 안정성은 검증되었습니다. ArgoCD는 "보여주기" 좋고, FluxCD는 "자동화"에 강합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 선수 지식, Push 기반 배포 이해 | 입문 |
| [IaC](./iac.md) | 선수 지식, 인프라 코드화 | 중급 |
| [배포 전략](./deployment-strategy.md) | Blue-Green, Canary와 결합 | 중급 |
| [Kubernetes](../system-design/kubernetes.md) | GitOps 배포 대상 | 중급 |

## 참고 자료

- [ArgoCD Documentation](https://argo-cd.readthedocs.io/)
- [FluxCD Documentation](https://fluxcd.io/docs/)
- [GitOps Principles - OpenGitOps](https://opengitops.dev/)
- [Weaveworks - Guide To GitOps](https://www.weave.works/technologies/gitops/)
- [CNCF GitOps Working Group](https://github.com/cncf/tag-app-delivery/tree/main/gitops-wg)
