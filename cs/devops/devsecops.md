# DevSecOps

> `[3] 중급` · 선수 지식: [CI/CD](./ci-cd.md)

> `Trend` 2025

> 개발(Dev), 보안(Sec), 운영(Ops)을 통합하여 소프트웨어 개발 전 과정에 보안을 내재화하는 방법론

`#DevSecOps` `#개발보안운영` `#ShiftLeft` `#ShiftEverywhere` `#보안자동화` `#SecurityAutomation` `#SAST` `#DAST` `#SCA` `#IaC보안` `#컨테이너보안` `#ContainerSecurity` `#SBOM` `#소프트웨어공급망` `#SupplyChainSecurity` `#SecureByDesign` `#보안게이트` `#SecurityGate` `#취약점스캔` `#VulnerabilityScanning` `#시크릿관리` `#SecretManagement` `#컴플라이언스` `#Compliance` `#OWASP` `#Snyk` `#Trivy` `#SonarQube` `#Checkov`

## 왜 알아야 하는가?

- **실무**: 2025년 기업 75%가 사이버보안 예산을 증가시키며, 보안은 개발자의 필수 역량으로 자리잡음
- **면접**: "보안 취약점을 어떻게 예방하는가", "시큐어 코딩 경험" 등 보안 역량 확인 질문 증가
- **기반 지식**: 클라우드 네이티브, 컨테이너, 마이크로서비스 환경에서 보안은 설계 단계부터 고려해야 함

## 핵심 개념

- **Shift-Left**: 보안을 개발 초기 단계로 앞당김 (코드 작성 시점에 보안 검증)
- **Shift-Everywhere**: 개발 전 과정에 보안을 내재화 (2025년 패러다임)
- **Security as Code**: 보안 정책을 코드로 정의하고 버전 관리

## 쉽게 이해하기

**건물 건축 비유**

전통적 보안: 건물 완공 후 보안 시스템 설치 → 구조적 결함 발견 시 비용 막대

DevSecOps: 설계 단계부터 보안을 고려
- **설계(Design)**: 보안 요구사항 정의, 위협 모델링
- **시공(Build)**: 보안 자재 사용 (시큐어 코딩)
- **검수(Test)**: 각 층 완성 시마다 보안 점검 (자동화된 스캔)
- **운영(Operate)**: 24시간 모니터링, 침입 탐지

## 상세 설명

### Shift-Left에서 Shift-Everywhere로

기존 "Shift-Left"는 보안을 개발 초기로 앞당기는 것이었지만, 2025년에는 "Shift-Everywhere" 패러다임으로 진화했습니다.

| 패러다임 | 설명 | 한계 |
|----------|------|------|
| **전통적 보안** | 배포 직전 보안 점검 | 발견 시점이 너무 늦음 |
| **Shift-Left** | 개발 초기에 보안 통합 | 후반 단계 보안 소홀 가능 |
| **Shift-Everywhere** | 전 과정에 보안 내재화 | - |

**왜 Shift-Everywhere인가?**
- 보안 위협은 특정 단계에만 존재하지 않음
- CI/CD 파이프라인 자체가 공격 대상이 될 수 있음
- 런타임 환경에서 새로운 취약점이 발견될 수 있음

### DevSecOps 파이프라인

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           DevSecOps Pipeline                                 │
├───────────┬───────────┬───────────┬───────────┬───────────┬─────────────────┤
│   Plan    │   Code    │   Build   │   Test    │  Deploy   │    Operate      │
├───────────┼───────────┼───────────┼───────────┼───────────┼─────────────────┤
│ • 위협     │ • IDE     │ • SAST    │ • DAST    │ • 이미지   │ • 런타임 보안   │
│   모델링   │   보안    │ • SCA     │ • IAST    │   스캔    │ • SIEM/SOAR     │
│ • 보안    │   플러그인 │ • 시크릿   │ • 침투    │ • IaC     │ • 침입 탐지     │
│   요구사항 │ • Pre-    │   스캔    │   테스트   │   보안    │ • 취약점        │
│ • 컴플라   │   commit  │ • SBOM    │           │   스캔    │   모니터링      │
│   이언스   │   hooks   │   생성    │           │           │                 │
└───────────┴───────────┴───────────┴───────────┴───────────┴─────────────────┘
```

### 핵심 보안 도구 유형

#### SAST (Static Application Security Testing)

소스 코드를 분석하여 보안 취약점 탐지. 빌드 전 또는 빌드 시점에 실행.

```yaml
# GitHub Actions에서 SonarQube SAST
- name: SonarQube Scan
  uses: sonarsource/sonarqube-scan-action@master
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
    SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
```

**탐지 가능한 취약점:**
- SQL Injection, XSS 패턴
- 하드코딩된 비밀번호
- 안전하지 않은 암호화 사용

#### DAST (Dynamic Application Security Testing)

실행 중인 애플리케이션을 대상으로 외부 공격 시뮬레이션.

```yaml
# OWASP ZAP을 이용한 DAST
- name: OWASP ZAP Scan
  uses: zaproxy/action-baseline@v0.10.0
  with:
    target: 'https://staging.example.com'
    rules_file_name: '.zap/rules.tsv'
```

**탐지 가능한 취약점:**
- 인증/인가 우회
- 세션 관리 문제
- 런타임 인젝션 공격

#### SCA (Software Composition Analysis)

오픈소스 의존성의 알려진 취약점(CVE) 탐지.

```yaml
# Snyk을 이용한 SCA
- name: Snyk Security Scan
  uses: snyk/actions/gradle@master
  env:
    SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
  with:
    args: --severity-threshold=high
```

**왜 중요한가?**
- 현대 애플리케이션의 70-90%가 오픈소스 코드
- Log4Shell 같은 대규모 취약점 위험
- 라이선스 컴플라이언스 문제

#### IaC 보안 스캔

Terraform, Kubernetes YAML 등 인프라 코드의 보안 설정 점검.

```yaml
# Checkov을 이용한 IaC 스캔
- name: Checkov IaC Scan
  uses: bridgecrewio/checkov-action@master
  with:
    directory: ./terraform
    framework: terraform
```

**점검 항목:**
- 공개 S3 버킷
- 과도한 IAM 권한
- 암호화되지 않은 스토리지

### 컨테이너 보안

```yaml
# Trivy를 이용한 컨테이너 이미지 스캔
- name: Container Image Scan
  uses: aquasecurity/trivy-action@master
  with:
    image-ref: 'myapp:${{ github.sha }}'
    format: 'sarif'
    output: 'trivy-results.sarif'
    severity: 'CRITICAL,HIGH'
```

**컨테이너 보안 체크리스트:**
1. 베이스 이미지 취약점 스캔
2. 비root 사용자로 실행
3. 읽기 전용 파일시스템
4. 리소스 제한 설정
5. 시크릿을 환경 변수가 아닌 시크릿 관리자 사용

### SBOM (Software Bill of Materials)

소프트웨어 구성 요소 목록을 문서화하여 공급망 보안 강화.

```bash
# Syft를 이용한 SBOM 생성
syft myapp:latest -o spdx-json > sbom.spdx.json
```

**SBOM이 필요한 이유:**
- 미국 정부 계약 시 SBOM 의무화 (Executive Order 14028)
- 취약점 발견 시 영향 범위 신속 파악
- 라이선스 컴플라이언스 증빙

## 예제 코드

### 통합 DevSecOps 파이프라인 (GitHub Actions)

```yaml
name: DevSecOps Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  security-scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      # 시크릿 스캔
      - name: Secret Scanning
        uses: trufflesecurity/trufflehog@main
        with:
          path: ./
          base: ${{ github.event.repository.default_branch }}

      # SAST - 정적 분석
      - name: SAST Scan
        uses: sonarsource/sonarqube-scan-action@master
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}

      # SCA - 의존성 취약점 스캔
      - name: Dependency Scan
        uses: snyk/actions/gradle@master
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}

  build:
    needs: security-scan
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Build Application
        run: ./gradlew build

      - name: Build Container Image
        run: docker build -t myapp:${{ github.sha }} .

      # 컨테이너 이미지 스캔
      - name: Container Scan
        uses: aquasecurity/trivy-action@master
        with:
          image-ref: 'myapp:${{ github.sha }}'
          severity: 'CRITICAL,HIGH'
          exit-code: '1'  # 심각한 취약점 발견 시 빌드 실패

      # SBOM 생성
      - name: Generate SBOM
        uses: anchore/sbom-action@v0
        with:
          image: myapp:${{ github.sha }}
          artifact-name: sbom.spdx.json

  deploy-staging:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to Staging
        run: ./deploy.sh staging

      # DAST - 동적 분석
      - name: DAST Scan
        uses: zaproxy/action-baseline@v0.10.0
        with:
          target: 'https://staging.example.com'

  deploy-production:
    needs: deploy-staging
    runs-on: ubuntu-latest
    environment: production  # 수동 승인 필요
    steps:
      - name: Deploy to Production
        run: ./deploy.sh production
```

### Pre-commit Hook 설정

```yaml
# .pre-commit-config.yaml
repos:
  # 시크릿 탐지
  - repo: https://github.com/Yelp/detect-secrets
    rev: v1.4.0
    hooks:
      - id: detect-secrets
        args: ['--baseline', '.secrets.baseline']

  # 보안 린팅
  - repo: https://github.com/PyCQA/bandit
    rev: 1.7.5
    hooks:
      - id: bandit
        args: ["-c", "pyproject.toml"]

  # Terraform 보안
  - repo: https://github.com/antonbabenko/pre-commit-terraform
    rev: v1.83.5
    hooks:
      - id: terraform_tfsec
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 취약점 조기 발견으로 수정 비용 절감 | 초기 파이프라인 구축 비용 |
| 자동화로 일관된 보안 수준 유지 | 빌드 시간 증가 |
| 컴플라이언스 요구사항 충족 | 오탐(False Positive) 관리 필요 |
| 개발자 보안 인식 향상 | 도구 학습 곡선 |
| 보안 사고 발생 시 빠른 대응 | 라이선스 비용 (상용 도구) |

## 트러블슈팅

### 사례 1: 오탐(False Positive) 과다로 개발 생산성 저하

#### 증상
- 보안 스캔 결과의 80% 이상이 오탐
- 개발자들이 보안 경고를 무시하기 시작
- 빌드 파이프라인 실패 빈번

#### 원인 분석
- 보안 도구 규칙이 프로젝트에 맞지 않음
- 레거시 코드에 대한 예외 처리 없음
- 테스트 코드까지 스캔 대상에 포함

#### 해결 방법
```yaml
# SonarQube 규칙 커스터마이징
sonar.exclusions=**/test/**,**/generated/**
sonar.issue.ignore.multicriteria=e1,e2

# e1: 테스트 코드에서 하드코딩된 비밀번호 허용
sonar.issue.ignore.multicriteria.e1.ruleKey=java:S2068
sonar.issue.ignore.multicriteria.e1.resourceKey=**/test/**
```

```yaml
# Snyk 심각도 임계값 조정
- name: Snyk Scan
  uses: snyk/actions/gradle@master
  with:
    args: --severity-threshold=high  # low, medium 제외
```

#### 예방 조치
- 초기에 규칙을 점진적으로 적용 (Critical → High → Medium)
- 정기적인 룰셋 리뷰 및 최적화
- 개발팀과 보안팀 간 피드백 루프 구축

### 사례 2: 의존성 취약점으로 배포 차단

#### 증상
- 중요 배포가 CVE로 인해 블로킹
- 즉시 업그레이드가 불가능한 의존성

#### 원인 분석
- 취약점이 있지만 패치 버전이 아직 없음
- 메이저 버전 업그레이드 필요 (Breaking Change)
- 해당 취약점이 실제로는 영향 없음

#### 해결 방법
```yaml
# Snyk ignore 파일로 예외 처리
# .snyk
version: v1.25.0
ignore:
  SNYK-JAVA-ORGAPACHELOGGING-2314720:
    - '*':
        reason: 'Log4j 취약점이지만 해당 코드 경로 미사용'
        expires: 2025-03-01T00:00:00.000Z  # 만료일 필수
```

#### 예방 조치
- 의존성 업데이트 자동화 (Dependabot, Renovate)
- 분기별 의존성 리뷰 프로세스 수립
- 취약점 영향도 분석 프로세스 정립

## 면접 예상 질문

### Q: DevSecOps에서 "Shift-Left"와 "Shift-Everywhere"의 차이점은?

A: Shift-Left는 보안을 개발 초기 단계로 앞당기는 것입니다. 코드 작성 시점에 SAST를 실행하고, 디자인 단계에서 위협 모델링을 수행합니다. 하지만 이것만으로는 충분하지 않습니다. Shift-Everywhere는 2025년 진화된 패러다임으로, 개발 전 과정(Plan-Code-Build-Test-Deploy-Operate)에 보안을 내재화합니다. 런타임 보안, 컨테이너 보안, 공급망 보안까지 포함하여 어떤 단계에서든 보안이 보장되도록 합니다.

### Q: SAST와 DAST의 차이점과 각각 언제 사용하나요?

A: SAST(정적 분석)는 소스 코드를 실행하지 않고 분석하여 SQL Injection, XSS 패턴 같은 코드 레벨 취약점을 찾습니다. 빌드 단계에서 빠르게 실행 가능하지만 런타임 동작은 알 수 없습니다. DAST(동적 분석)는 실행 중인 애플리케이션을 대상으로 실제 공격을 시뮬레이션합니다. 인증 우회, 세션 관리 문제 같은 런타임 취약점을 찾지만 시간이 오래 걸립니다. 둘은 상호 보완적이므로 SAST는 PR/빌드 시, DAST는 스테이징 배포 후 실행하는 것이 좋습니다.

### Q: SBOM이 왜 중요한가요?

A: SBOM(Software Bill of Materials)은 소프트웨어에 포함된 모든 구성 요소 목록입니다. Log4Shell 같은 대규모 취약점 발생 시, SBOM이 있으면 영향받는 시스템을 즉시 파악할 수 있습니다. 미국 정부는 Executive Order 14028을 통해 정부 계약 시 SBOM을 의무화했고, 이는 글로벌 표준이 되어가고 있습니다. 또한 라이선스 컴플라이언스 증빙과 공급망 보안 강화에도 필수적입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 선수 지식 - 파이프라인 기초 | Beginner |
| [웹 보안](../security/web-security.md) | OWASP Top 10, 취약점 이해 | Intermediate |
| [Docker](../system-design/docker.md) | 컨테이너 보안 기초 | Intermediate |
| [IaC](./iac.md) | Infrastructure as Code 보안 | Intermediate |

## 참고 자료

- [OWASP DevSecOps Guideline](https://owasp.org/www-project-devsecops-guideline/)
- [NIST Secure Software Development Framework](https://csrc.nist.gov/Projects/ssdf)
- [Snyk - State of Open Source Security 2024](https://snyk.io/reports/open-source-security/)
- [GitLab DevSecOps Survey 2024](https://about.gitlab.com/developer-survey/)
