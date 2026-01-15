# DevOps

개발과 운영의 통합을 통해 소프트웨어 개발 라이프사이클을 단축하고 품질을 향상시키는 방법론입니다.

## 목차

### [1] 정의/기초

- [DevOps란](./what-is-devops.md) - DevOps의 정의, CALMS 원칙, DORA 메트릭

### [2] 입문

- [CI/CD](./ci-cd.md) - 지속적 통합과 지속적 배포
- [Cloudflared](./cloudflared.md) - Cloudflare Tunnel, 보안 터널링

### [3] 중급

- [모니터링](./monitoring.md) - 로깅, 메트릭, 트레이싱 (선수: CI/CD)
- [IaC](./iac.md) - Infrastructure as Code (선수: CI/CD)
- [배포 전략](./deployment-strategy.md) - Blue-Green, Canary, Rolling Update (선수: CI/CD, Docker)
- [FinOps](./finops.md) - 클라우드 비용 최적화, 단위 비용 관리 `Trend` (선수: CI/CD, 클라우드 기초)
- [DevSecOps](./devsecops.md) - 개발-보안-운영 통합, Shift-Everywhere `Trend` (선수: CI/CD)

### [4] 심화

- [Platform Engineering](./platform-engineering.md) - 내부 개발자 플랫폼(IDP), 셀프서비스 인프라 `Trend` (선수: CI/CD, IaC)
- [GitOps](./gitops.md) - Git 기반 선언적 배포, ArgoCD, FluxCD `Trend` (선수: CI/CD, IaC)
- [SRE](./sre.md) - Site Reliability Engineering, SLI/SLO/SLA, Error Budget (선수: 모니터링)

## 관련 문서

| 카테고리 | 문서 | 연관성 |
|----------|------|--------|
| System Design | [Docker](../system-design/docker.md) | 컨테이너 기술 |
| System Design | [Kubernetes](../system-design/kubernetes.md) | 오케스트레이션 |
| Git | [Git Branch Strategy](../git/git-branch-strategy.md) | 브랜치 전략 |
