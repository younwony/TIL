# CI/CD

> `[2] 입문` · 선수 지식: 없음

> 코드 변경사항을 자동으로 빌드, 테스트, 배포하여 소프트웨어 출시 주기를 단축하는 개발 방법론

`#CI` `#CD` `#ContinuousIntegration` `#ContinuousDelivery` `#ContinuousDeployment` `#지속적통합` `#지속적배포` `#자동화` `#Automation` `#파이프라인` `#Pipeline` `#빌드` `#Build` `#테스트` `#Test` `#배포` `#Deploy` `#Jenkins` `#GitHubActions` `#GitLabCI` `#CircleCI` `#ArgoCD` `#DevOps` `#GitOps` `#BlueGreen` `#Canary` `#RollingUpdate` `#Rollback`

## 왜 알아야 하는가?

- **실무**: 모든 현대 소프트웨어 개발 조직에서 CI/CD는 필수 인프라. 수동 배포의 휴먼 에러를 제거하고 배포 주기를 단축
- **면접**: "CI/CD 파이프라인 구축 경험", "배포 전략" 등 DevOps 역량 확인 필수 질문
- **기반 지식**: 모니터링, IaC, GitOps 등 모든 DevOps 실천의 기반

## 핵심 개념

- **CI (Continuous Integration)**: 코드 변경을 자주 통합하고 자동 빌드/테스트 수행
- **CD (Continuous Delivery)**: 프로덕션 배포 가능한 상태를 항상 유지
- **CD (Continuous Deployment)**: 테스트 통과 시 자동으로 프로덕션 배포

## 쉽게 이해하기

**자동차 공장 비유**

전통적 개발: 모든 부품을 다 만든 후 한 번에 조립 → 문제 발생 시 원인 파악 어려움

CI/CD: 부품 하나 만들 때마다 바로 조립하고 테스트
- **CI**: 새 부품(코드)이 들어올 때마다 기존 자동차에 맞는지 검사
- **CD**: 검사 통과한 자동차를 전시장(프로덕션)에 바로 배치

## 상세 설명

### CI (Continuous Integration)

개발자가 코드를 공유 저장소에 자주 통합하는 실천 방법.

```
개발자 코드 Push → 자동 빌드 → 자동 테스트 → 결과 알림
```

**핵심 원칙:**
1. 하루에 여러 번 메인 브랜치에 통합
2. 모든 통합에 자동화된 빌드와 테스트 실행
3. 빌드 실패 시 즉시 수정 (빌드는 항상 통과 상태 유지)

**왜 자주 통합하는가?**
- 통합 간격이 길수록 충돌 해결 비용 증가
- 작은 변경은 문제 원인 파악이 쉬움
- 빠른 피드백으로 버그 조기 발견

### CD: Delivery vs Deployment

| 구분 | Continuous Delivery | Continuous Deployment |
|------|--------------------|-----------------------|
| 배포 결정 | 수동 승인 필요 | 자동 배포 |
| 적합한 경우 | 규제 산업, 보수적 조직 | 빠른 피드백 필요, 스타트업 |
| 배포 빈도 | 주 1회 ~ 일 1회 | 일 수회 ~ 수십 회 |

### CI/CD 파이프라인 구조

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  Code   │ →  │  Build  │ →  │  Test   │ →  │ Deploy  │ →  │ Monitor │
│  Commit │    │         │    │         │    │ (Stage) │    │         │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
                                                  │
                                                  ↓
                                            ┌─────────┐
                                            │ Deploy  │
                                            │ (Prod)  │
                                            └─────────┘
```

**각 단계 설명:**

1. **Code**: 개발자가 코드 커밋/푸시
2. **Build**: 소스 코드 컴파일, 의존성 설치, 아티팩트 생성
3. **Test**: 단위 테스트, 통합 테스트, E2E 테스트
4. **Deploy (Stage)**: 스테이징 환경에 배포, QA 테스트
5. **Deploy (Prod)**: 프로덕션 환경에 배포
6. **Monitor**: 배포 후 모니터링, 이상 시 롤백

## 배포 전략

### Blue-Green Deployment

두 개의 동일한 환경을 유지하여 무중단 배포.

```
           ┌──────────────────┐
           │   Load Balancer  │
           └────────┬─────────┘
                    │
        ┌───────────┴───────────┐
        ↓                       ↓
  ┌───────────┐           ┌───────────┐
  │   Blue    │           │   Green   │
  │  (Active) │           │  (Idle)   │
  │   v1.0    │           │   v1.1    │
  └───────────┘           └───────────┘
```

1. Green에 새 버전 배포
2. Green 테스트 통과 확인
3. 로드밸런서를 Green으로 전환
4. 문제 시 즉시 Blue로 롤백

**장점**: 즉각적인 롤백, 무중단 배포
**단점**: 인프라 비용 2배

### Canary Deployment

일부 트래픽만 새 버전으로 보내 점진적 배포.

```
           ┌──────────────────┐
           │   Load Balancer  │
           │   (95% : 5%)     │
           └────────┬─────────┘
                    │
        ┌───────────┴───────────┐
        ↓ 95%                   ↓ 5%
  ┌───────────┐           ┌───────────┐
  │  Stable   │           │  Canary   │
  │   v1.0    │           │   v1.1    │
  └───────────┘           └───────────┘
```

1. 5% 트래픽을 새 버전으로
2. 메트릭 모니터링 (에러율, 응답시간)
3. 정상이면 비율 증가 (5% → 25% → 50% → 100%)
4. 이상 시 즉시 롤백

**장점**: 리스크 최소화, 실제 트래픽으로 검증
**단점**: 구현 복잡, 배포 시간 증가

### Rolling Update

인스턴스를 순차적으로 교체.

```
시작:    [v1.0] [v1.0] [v1.0] [v1.0]
Step 1:  [v1.1] [v1.0] [v1.0] [v1.0]
Step 2:  [v1.1] [v1.1] [v1.0] [v1.0]
Step 3:  [v1.1] [v1.1] [v1.1] [v1.0]
완료:    [v1.1] [v1.1] [v1.1] [v1.1]
```

**장점**: 추가 인프라 불필요, 점진적 배포
**단점**: 롤백 시간 소요, 배포 중 버전 혼재

## 예제 코드

### GitHub Actions 예시

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Build with Gradle
        run: ./gradlew build

      - name: Run Tests
        run: ./gradlew test

      - name: Upload Test Results
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: build/reports/tests/

  deploy:
    needs: build-and-test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Deploy to Production
        run: |
          echo "Deploying to production..."
          # 실제 배포 스크립트
```

### Jenkins Pipeline 예시

```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/example/repo.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                sh './deploy.sh staging'
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            input {
                message "Deploy to production?"
                ok "Deploy"
            }
            steps {
                sh './deploy.sh production'
            }
        }
    }

    post {
        failure {
            slackSend channel: '#alerts', message: "Build Failed: ${env.JOB_NAME}"
        }
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 빠른 피드백 루프 | 초기 구축 비용과 시간 |
| 버그 조기 발견 | 테스트 커버리지 의존성 |
| 배포 리스크 감소 | 파이프라인 유지보수 필요 |
| 개발자 생산성 향상 | 러닝 커브 존재 |
| 일관된 배포 프로세스 | 인프라 복잡도 증가 |

## 트러블슈팅

### 사례 1: 빌드가 자주 깨지는 문제

#### 증상
- 메인 브랜치 빌드 실패 빈도 높음
- 개발자들이 빌드 상태를 무시하기 시작

#### 원인 분석
- 로컬에서 테스트하지 않고 푸시
- 브랜치 보호 규칙 미설정
- 빌드 시간이 너무 길어 피드백 지연

#### 해결 방법
```yaml
# 브랜치 보호 규칙 설정
# GitHub Settings > Branches > Branch protection rules
- Require status checks to pass before merging
- Require branches to be up to date before merging
```

```bash
# pre-commit hook으로 로컬 검증
#!/bin/sh
./gradlew test --fail-fast
```

#### 예방 조치
- PR 필수화 및 CI 통과 조건 설정
- 빌드 시간 최적화 (캐싱, 병렬화)
- 빌드 실패 시 즉시 알림 설정

### 사례 2: 배포 후 장애 발생

#### 증상
- 프로덕션 배포 후 에러율 급증
- 롤백에 시간 소요

#### 원인 분석
- 스테이징과 프로덕션 환경 차이
- Feature Flag 없이 Big Bang 배포
- 모니터링 부재

#### 해결 방법
```yaml
# Canary 배포 적용
deployment:
  strategy:
    canary:
      steps:
        - setWeight: 5
        - pause: {duration: 5m}
        - setWeight: 25
        - pause: {duration: 10m}
        - setWeight: 50
        - pause: {duration: 10m}
        - setWeight: 100
```

#### 예방 조치
- 환경 동등성 유지 (IaC 활용)
- Feature Flag로 점진적 기능 활성화
- 배포 후 자동화된 smoke test
- 메트릭 기반 자동 롤백 설정

## 면접 예상 질문

### Q: CI와 CD의 차이점을 설명해주세요.

A: CI(Continuous Integration)는 개발자들이 코드를 자주 통합하고 자동으로 빌드/테스트하는 것입니다. CD는 두 가지 의미가 있는데, Continuous Delivery는 항상 배포 가능한 상태를 유지하되 배포는 수동 승인을 받는 것이고, Continuous Deployment는 테스트 통과 시 자동으로 프로덕션에 배포하는 것입니다. CI가 없으면 CD도 의미가 없으며, CI는 CD의 필수 전제 조건입니다.

### Q: Blue-Green과 Canary 배포의 차이점과 각각 언제 사용하나요?

A: Blue-Green은 두 환경을 유지하며 한 번에 전체 트래픽을 전환합니다. 롤백이 즉각적이지만 인프라 비용이 2배입니다. Canary는 일부 트래픽만 새 버전으로 보내 점진적으로 검증합니다. Blue-Green은 빠른 전환이 필요하고 충분한 테스트가 완료된 경우에, Canary는 실제 트래픽으로 검증이 필요하고 리스크를 최소화해야 할 때 사용합니다.

### Q: CI/CD 파이프라인을 설계할 때 고려해야 할 점은?

A: 첫째, 빌드 시간 최적화입니다. 캐싱과 병렬화로 피드백 루프를 단축해야 합니다. 둘째, 테스트 전략입니다. 단위/통합/E2E 테스트의 균형과 실행 순서를 고려합니다. 셋째, 환경 동등성입니다. 스테이징과 프로덕션 환경 차이를 최소화해야 합니다. 넷째, 보안입니다. 시크릿 관리와 권한 제어가 필요합니다. 마지막으로 롤백 전략입니다. 문제 발생 시 신속하게 이전 버전으로 복구할 수 있어야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git Branch Strategy](../git/git-branch-strategy.md) | 브랜치 전략 | Beginner |
| [Docker](../system-design/docker.md) | 컨테이너 빌드 | Intermediate |
| [Kubernetes](../system-design/kubernetes.md) | 컨테이너 오케스트레이션 | Advanced |
| [모니터링](./monitoring.md) | 배포 후 모니터링 | Intermediate |

## 참고 자료

- [Continuous Delivery - Martin Fowler](https://martinfowler.com/bliki/ContinuousDelivery.html)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [The DevOps Handbook](https://itrevolution.com/the-devops-handbook/)
