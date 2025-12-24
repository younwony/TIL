# IaC (Infrastructure as Code)

> `[3] 중급` · 선수 지식: [CI/CD](./ci-cd.md)

> 인프라를 코드로 정의하고 버전 관리하여 자동화된 프로비저닝과 일관된 환경을 보장하는 방법론

`#IaC` `#InfrastructureAsCode` `#코드형인프라` `#Terraform` `#Ansible` `#Pulumi` `#CloudFormation` `#ARM` `#Bicep` `#CDK` `#프로비저닝` `#Provisioning` `#구성관리` `#ConfigurationManagement` `#선언적` `#Declarative` `#명령적` `#Imperative` `#멱등성` `#Idempotent` `#상태관리` `#StateManagement` `#모듈` `#Module` `#GitOps` `#AWS` `#Azure` `#GCP` `#클라우드`

## 왜 알아야 하는가?

- **실무**: 수동 인프라 관리의 휴먼 에러 제거, 환경 간 일관성 보장, 빠른 재해 복구
- **면접**: "인프라 자동화 경험", "환경 구성 관리" 등 DevOps 역량 평가의 핵심 주제
- **기반 지식**: GitOps, 클라우드 네이티브, SRE 실천의 필수 기반

## 핵심 개념

- **선언적 vs 명령적**: 원하는 상태를 선언 vs 수행할 작업을 명령
- **멱등성(Idempotency)**: 여러 번 실행해도 같은 결과
- **상태 관리(State)**: 현재 인프라 상태를 추적하여 변경 계획 수립

## 쉽게 이해하기

**레시피 비유**

수동 인프라 관리: 요리사가 매번 기억에 의존해 요리
- 재료 양이 매번 다름
- 다른 요리사는 재현 불가
- 실수하면 처음부터 다시

IaC: 상세한 레시피북
- **선언적**: "완성된 요리 사진" (Terraform)
- **명령적**: "1. 양파 썰기 2. 기름 두르기..." (Ansible)
- 누가 만들어도 같은 결과
- 레시피 버전 관리 가능

## 상세 설명

### 선언적 vs 명령적 접근

```
┌─────────────────────────────────────────────────────────────┐
│                    IaC 접근 방식                             │
├─────────────────────────────┬───────────────────────────────┤
│       선언적 (Declarative)  │       명령적 (Imperative)     │
├─────────────────────────────┼───────────────────────────────┤
│ "원하는 상태"를 정의        │ "수행할 작업"을 순서대로 정의 │
│ 시스템이 방법을 결정        │ 사용자가 방법을 지정          │
├─────────────────────────────┼───────────────────────────────┤
│ Terraform, CloudFormation   │ Ansible, Chef, Puppet         │
│ Kubernetes YAML             │ Shell Scripts                 │
├─────────────────────────────┼───────────────────────────────┤
│ "EC2 인스턴스 3개 있어야 함"│ "EC2 인스턴스 3개 생성하라"   │
└─────────────────────────────┴───────────────────────────────┘
```

**왜 선언적이 선호되는가?**
- 현재 상태와 원하는 상태를 비교하여 필요한 작업만 수행
- 멱등성 자동 보장
- 드리프트(drift) 감지 가능

### 멱등성 (Idempotency)

같은 작업을 여러 번 수행해도 결과가 동일.

```bash
# 멱등성 없음 (위험)
echo "export PATH=/usr/local/bin:$PATH" >> ~/.bashrc
# 실행할 때마다 중복 추가됨

# 멱등성 있음 (안전)
grep -q "export PATH=/usr/local/bin" ~/.bashrc || \
  echo "export PATH=/usr/local/bin:$PATH" >> ~/.bashrc
```

```hcl
# Terraform은 자동으로 멱등성 보장
resource "aws_instance" "web" {
  count         = 3
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"
}
# 이미 3개 있으면 아무것도 하지 않음
# 2개 있으면 1개만 추가
# 4개 있으면 1개 삭제
```

### 상태 관리 (State Management)

Terraform의 상태 파일은 현재 인프라 상태를 추적.

```
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│   Desired State  │    │   Current State  │    │   Actual Infra   │
│   (*.tf 파일)    │    │  (terraform.tfstate)   │    │   (AWS/GCP/...)  │
└────────┬─────────┘    └────────┬─────────┘    └────────┬─────────┘
         │                       │                       │
         └───────────┬───────────┘                       │
                     │                                   │
                     ▼                                   │
              ┌─────────────┐                           │
              │   Plan      │ ◀─────────────────────────┘
              │ (변경 계획)  │       terraform refresh
              └─────────────┘
```

**상태 파일 관리 Best Practice:**

```hcl
# 원격 백엔드 사용 (팀 협업)
terraform {
  backend "s3" {
    bucket         = "my-terraform-state"
    key            = "prod/terraform.tfstate"
    region         = "ap-northeast-2"
    encrypt        = true
    dynamodb_table = "terraform-locks"  # 상태 잠금
  }
}
```

**왜 원격 백엔드인가?**
- 팀원 간 상태 공유
- 상태 파일 잠금으로 동시 수정 방지
- 버전 관리 및 암호화

### IaC 도구 비교

| 도구 | 접근 방식 | 상태 관리 | 주요 사용처 |
|------|----------|----------|------------|
| Terraform | 선언적 | 자체 상태 파일 | 멀티 클라우드 프로비저닝 |
| Ansible | 명령적/선언적 | 없음 (에이전트리스) | 구성 관리, 배포 |
| CloudFormation | 선언적 | AWS 관리 | AWS 전용 |
| Pulumi | 선언적 | 자체/클라우드 | 범용 언어로 IaC |
| CDK | 선언적 | CloudFormation | AWS, TypeScript/Python |

### 프로젝트 구조

```
infrastructure/
├── environments/
│   ├── dev/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── terraform.tfvars
│   ├── staging/
│   │   └── ...
│   └── prod/
│       └── ...
├── modules/
│   ├── vpc/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── ec2/
│   │   └── ...
│   └── rds/
│       └── ...
└── shared/
    └── backend.tf
```

**왜 모듈화하는가?**
- 코드 재사용
- 환경 간 일관성
- 변경 영향 범위 최소화
- 팀 간 표준화

## 예제 코드

### Terraform - AWS EC2 + RDS

```hcl
# variables.tf
variable "environment" {
  description = "Environment name"
  type        = string
}

variable "instance_count" {
  description = "Number of EC2 instances"
  type        = number
  default     = 2
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

# main.tf
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}

# VPC 모듈 사용
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  name = "${var.environment}-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["ap-northeast-2a", "ap-northeast-2c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = var.environment != "prod"

  tags = {
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}

# EC2 인스턴스
resource "aws_instance" "web" {
  count = var.instance_count

  ami           = data.aws_ami.amazon_linux.id
  instance_type = var.environment == "prod" ? "t3.medium" : "t3.micro"
  subnet_id     = module.vpc.private_subnets[count.index % 2]

  vpc_security_group_ids = [aws_security_group.web.id]

  tags = {
    Name        = "${var.environment}-web-${count.index + 1}"
    Environment = var.environment
  }
}

# RDS
resource "aws_db_instance" "main" {
  identifier = "${var.environment}-db"

  engine         = "mysql"
  engine_version = "8.0"
  instance_class = var.environment == "prod" ? "db.r5.large" : "db.t3.micro"

  allocated_storage     = 20
  max_allocated_storage = var.environment == "prod" ? 100 : 50

  db_name  = "myapp"
  username = "admin"
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.db.id]

  skip_final_snapshot = var.environment != "prod"
  deletion_protection = var.environment == "prod"

  tags = {
    Environment = var.environment
  }
}

# outputs.tf
output "web_instance_ids" {
  value = aws_instance.web[*].id
}

output "db_endpoint" {
  value     = aws_db_instance.main.endpoint
  sensitive = true
}
```

### Ansible - 서버 구성

```yaml
# playbook.yml
---
- name: Configure Web Servers
  hosts: webservers
  become: yes
  vars:
    app_user: myapp
    app_dir: /opt/myapp
    java_version: "17"

  tasks:
    - name: Update apt cache
      apt:
        update_cache: yes
        cache_valid_time: 3600

    - name: Install required packages
      apt:
        name:
          - openjdk-{{ java_version }}-jdk
          - nginx
          - certbot
        state: present

    - name: Create application user
      user:
        name: "{{ app_user }}"
        shell: /bin/bash
        home: "{{ app_dir }}"
        create_home: yes

    - name: Deploy application
      copy:
        src: "{{ playbook_dir }}/files/app.jar"
        dest: "{{ app_dir }}/app.jar"
        owner: "{{ app_user }}"
        mode: '0644'
      notify: Restart application

    - name: Configure systemd service
      template:
        src: templates/myapp.service.j2
        dest: /etc/systemd/system/myapp.service
      notify:
        - Reload systemd
        - Restart application

    - name: Configure nginx
      template:
        src: templates/nginx.conf.j2
        dest: /etc/nginx/sites-available/myapp
      notify: Reload nginx

    - name: Enable nginx site
      file:
        src: /etc/nginx/sites-available/myapp
        dest: /etc/nginx/sites-enabled/myapp
        state: link
      notify: Reload nginx

  handlers:
    - name: Reload systemd
      systemd:
        daemon_reload: yes

    - name: Restart application
      systemd:
        name: myapp
        state: restarted
        enabled: yes

    - name: Reload nginx
      systemd:
        name: nginx
        state: reloaded
```

### CI/CD 통합 (GitHub Actions + Terraform)

```yaml
# .github/workflows/terraform.yml
name: Terraform

on:
  push:
    branches: [main]
    paths:
      - 'infrastructure/**'
  pull_request:
    branches: [main]
    paths:
      - 'infrastructure/**'

env:
  TF_VERSION: '1.6.0'
  AWS_REGION: 'ap-northeast-2'

jobs:
  plan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ env.AWS_REGION }}

      - name: Setup Terraform
        uses: hashicorp/setup-terraform@v3
        with:
          terraform_version: ${{ env.TF_VERSION }}

      - name: Terraform Init
        working-directory: infrastructure/environments/prod
        run: terraform init

      - name: Terraform Validate
        working-directory: infrastructure/environments/prod
        run: terraform validate

      - name: Terraform Plan
        working-directory: infrastructure/environments/prod
        run: terraform plan -out=tfplan
        env:
          TF_VAR_db_password: ${{ secrets.DB_PASSWORD }}

      - name: Upload Plan
        uses: actions/upload-artifact@v4
        with:
          name: tfplan
          path: infrastructure/environments/prod/tfplan

  apply:
    needs: plan
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    environment: production
    steps:
      - uses: actions/checkout@v4

      - name: Download Plan
        uses: actions/download-artifact@v4
        with:
          name: tfplan
          path: infrastructure/environments/prod

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ${{ env.AWS_REGION }}

      - name: Setup Terraform
        uses: hashicorp/setup-terraform@v3
        with:
          terraform_version: ${{ env.TF_VERSION }}

      - name: Terraform Init
        working-directory: infrastructure/environments/prod
        run: terraform init

      - name: Terraform Apply
        working-directory: infrastructure/environments/prod
        run: terraform apply -auto-approve tfplan
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 버전 관리 및 변경 이력 추적 | 초기 학습 곡선 |
| 환경 간 일관성 보장 | 상태 파일 관리 복잡성 |
| 빠른 환경 복제/복구 | 레거시 인프라 마이그레이션 어려움 |
| 코드 리뷰를 통한 인프라 검증 | 도구별 DSL 학습 필요 |
| 재해 복구 시간 단축 | 시크릿 관리 주의 필요 |

## 트러블슈팅

### 사례 1: 상태 파일 불일치 (State Drift)

#### 증상
- `terraform plan`이 예상치 못한 변경을 표시
- 콘솔에서 수동으로 변경한 리소스 감지

#### 원인 분석
- 콘솔이나 CLI로 직접 인프라 수정
- 다른 도구/팀에서 동일 리소스 수정
- 상태 파일 손상

#### 해결 방법
```bash
# 현재 인프라 상태로 상태 파일 갱신
terraform refresh

# 특정 리소스만 import
terraform import aws_instance.web i-1234567890abcdef0

# 상태에서 리소스 제거 (리소스는 유지)
terraform state rm aws_instance.web

# 드리프트 무시하고 코드 상태로 강제 적용 (주의!)
terraform apply -refresh=false
```

#### 예방 조치
- 모든 인프라 변경은 IaC를 통해서만
- 정기적인 `terraform plan` 실행으로 드리프트 감지
- 쓰기 권한 제한 (IaC 파이프라인만 변경 가능)

### 사례 2: 순환 의존성

#### 증상
```
Error: Cycle: aws_security_group.a, aws_security_group.b
```

#### 원인 분석
- 보안 그룹 A가 B를 참조하고, B도 A를 참조
- 리소스 간 상호 참조 발생

#### 해결 방법
```hcl
# 문제: 순환 참조
resource "aws_security_group" "app" {
  ingress {
    security_groups = [aws_security_group.db.id]
  }
}

resource "aws_security_group" "db" {
  ingress {
    security_groups = [aws_security_group.app.id]
  }
}

# 해결: 별도의 규칙 리소스 사용
resource "aws_security_group" "app" {
  name = "app-sg"
}

resource "aws_security_group" "db" {
  name = "db-sg"
}

resource "aws_security_group_rule" "app_to_db" {
  type                     = "ingress"
  security_group_id        = aws_security_group.db.id
  source_security_group_id = aws_security_group.app.id
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
}

resource "aws_security_group_rule" "db_to_app" {
  type                     = "ingress"
  security_group_id        = aws_security_group.app.id
  source_security_group_id = aws_security_group.db.id
  from_port                = 8080
  to_port                  = 8080
  protocol                 = "tcp"
}
```

#### 예방 조치
- 의존성 그래프 시각화 (`terraform graph`)
- 인라인 규칙 대신 별도 리소스 사용
- 모듈 간 명확한 의존성 설계

## 면접 예상 질문

### Q: 선언적 방식과 명령적 방식의 차이점과 각각의 장단점은?

A: 선언적 방식은 "원하는 최종 상태"를 정의하고 도구가 방법을 결정합니다. Terraform이 대표적입니다. 자동으로 멱등성이 보장되고 드리프트 감지가 가능하지만, 복잡한 조건부 로직이 어렵습니다. 명령적 방식은 "수행할 작업"을 순서대로 정의합니다. Ansible이 예입니다. 유연한 로직 구현이 가능하지만, 멱등성을 수동으로 보장해야 합니다. 실무에서는 Terraform으로 인프라 프로비저닝, Ansible로 구성 관리를 하는 조합이 많습니다.

### Q: Terraform의 상태 파일은 왜 중요하고 어떻게 관리해야 하나요?

A: 상태 파일은 현재 인프라 상태를 추적하여 코드와 실제 인프라 간의 차이를 계산합니다. 이것이 없으면 매번 전체 인프라를 재생성해야 합니다. 관리 방법으로는 첫째, S3+DynamoDB 같은 원격 백엔드로 팀 협업과 잠금을 지원합니다. 둘째, 암호화하여 민감 정보를 보호합니다. 셋째, 버전 관리로 복구 가능성을 확보합니다. 상태 파일에 직접 접근하는 것은 피하고, `terraform state` 명령을 사용해야 합니다.

### Q: IaC 도입 시 기존 인프라는 어떻게 마이그레이션하나요?

A: 크게 세 가지 접근법이 있습니다. 첫째, `terraform import`로 기존 리소스를 상태 파일에 추가하고 코드를 작성합니다. 리소스가 많으면 시간이 오래 걸립니다. 둘째, Terraformer 같은 도구로 기존 인프라에서 코드를 자동 생성합니다. 생성된 코드 정리가 필요합니다. 셋째, 새 인프라를 IaC로 구축하고 점진적으로 마이그레이션합니다. 가장 안전하지만 시간이 많이 걸립니다. 실무에서는 중요도에 따라 세 방법을 혼용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | IaC 파이프라인 통합 | Beginner |
| [Docker](../system-design/docker.md) | 컨테이너 인프라 정의 | Intermediate |
| [Kubernetes](../system-design/kubernetes.md) | K8s 리소스 IaC | Advanced |
| [모니터링](./monitoring.md) | 인프라 모니터링 연계 | Intermediate |

## 참고 자료

- [Terraform Documentation](https://developer.hashicorp.com/terraform/docs)
- [Ansible Documentation](https://docs.ansible.com/)
- [Infrastructure as Code - Kief Morris](https://infrastructure-as-code.com/)
- [Terraform: Up & Running - Yevgeniy Brikman](https://www.terraformupandrunning.com/)
