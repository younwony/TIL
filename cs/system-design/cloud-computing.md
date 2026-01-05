# Cloud Computing

> `[1] 정의/기초` · 선수 지식: 없음

> 인터넷을 통해 컴퓨팅 리소스(서버, 스토리지, 네트워크, 소프트웨어)를 온디맨드로 제공받는 기술

`#클라우드컴퓨팅` `#CloudComputing` `#IaaS` `#PaaS` `#SaaS` `#InfrastructureAsAService` `#PlatformAsAService` `#SoftwareAsAService` `#AWS` `#Azure` `#GCP` `#GoogleCloud` `#퍼블릭클라우드` `#프라이빗클라우드` `#하이브리드클라우드` `#멀티클라우드` `#온프레미스` `#OnPremise` `#가상화` `#Virtualization` `#온디맨드` `#종량제` `#PayAsYouGo` `#EC2` `#S3` `#Lambda` `#클라우드서비스` `#클라우드마이그레이션`

## 왜 알아야 하는가?

- **실무**: 대부분의 현대 서비스가 클라우드에서 운영됨. 개발자도 클라우드 기본 개념 필수
- **면접**: "IaaS, PaaS, SaaS 차이점", "클라우드 vs 온프레미스" 등 기초 질문 빈출
- **기반 지식**: Docker, Kubernetes, Serverless, DevOps 등 모든 클라우드 기술의 기반

## 핵심 개념

- **온디맨드(On-Demand)**: 필요할 때 즉시 리소스를 할당받고, 필요 없으면 반환
- **종량제(Pay-as-you-go)**: 사용한 만큼만 비용 지불
- **서비스 모델**: IaaS, PaaS, SaaS로 구분되는 추상화 수준

## 쉽게 이해하기

**피자 비유 (Pizza as a Service)**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      Pizza as a Service                                  │
├─────────────┬─────────────┬─────────────┬─────────────┬─────────────────┤
│  On-Premise │    IaaS     │    PaaS     │    SaaS     │   Delivered     │
│  (집에서)    │  (식재료)    │ (반조리)     │ (배달)      │                 │
├─────────────┼─────────────┼─────────────┼─────────────┼─────────────────┤
│ 테이블       │ 테이블       │ 테이블       │ 테이블       │ ← 사용자 관리    │
│ 접시        │ 접시         │ 접시         │ 접시         │                 │
│ 오븐        │ 오븐         │ 오븐         │ 오븐         │                 │
│ 가스        │ 가스         │ 가스         │ 가스         │                 │
│ 반죽        │ 반죽         │ 반죽         │ 반죽         │                 │
│ 토핑        │ 토핑         │ 토핑         │ 토핑         │ ← 제공자 관리    │
│ 치즈        │ 치즈         │ 치즈         │ 치즈         │                 │
│ 피자        │ 피자         │ 피자         │ 피자         │                 │
├─────────────┴─────────────┴─────────────┴─────────────┴─────────────────┤
│ █████ = 사용자 관리    ░░░░░ = 제공자 관리                               │
└─────────────────────────────────────────────────────────────────────────┘
```

- **On-Premise**: 모든 것을 직접 준비 (서버, 네트워크, OS, 앱 모두 관리)
- **IaaS**: 식재료 구매 (인프라만 제공, 나머지는 직접 구성)
- **PaaS**: 반조리 제품 (플랫폼 제공, 코드만 작성)
- **SaaS**: 배달 피자 (완제품 사용, 설정만)

## 상세 설명

### 클라우드 컴퓨팅의 5가지 특성 (NIST 정의)

| 특성 | 설명 | 예시 |
|------|------|------|
| **온디맨드 셀프서비스** | 필요할 때 직접 리소스 프로비저닝 | AWS 콘솔에서 EC2 인스턴스 생성 |
| **광범위한 네트워크 접근** | 인터넷을 통해 어디서든 접근 | 모바일, 태블릿, PC에서 접속 |
| **리소스 풀링** | 여러 사용자가 리소스를 공유 | 멀티테넌시 환경 |
| **신속한 탄력성** | 수요에 따라 자동 확장/축소 | Auto Scaling |
| **측정 가능한 서비스** | 사용량 모니터링 및 과금 | 시간당, 요청당 과금 |

### 서비스 모델 비교

#### IaaS (Infrastructure as a Service)

가상화된 컴퓨팅 리소스를 인터넷으로 제공.

**사용자 관리 영역:**
- 운영체제 (OS)
- 미들웨어
- 런타임
- 애플리케이션
- 데이터

**제공자 관리 영역:**
- 서버 (물리적)
- 스토리지
- 네트워크
- 가상화

**대표 서비스:**
| 제공자 | 서비스 |
|--------|--------|
| AWS | EC2, EBS, VPC |
| Azure | Virtual Machines, Blob Storage |
| GCP | Compute Engine, Persistent Disk |

**적합한 경우:**
- 인프라에 대한 완전한 제어 필요
- 레거시 애플리케이션 마이그레이션
- 특수한 OS/미들웨어 요구사항

#### PaaS (Platform as a Service)

애플리케이션 개발/실행 플랫폼을 제공.

**사용자 관리 영역:**
- 애플리케이션
- 데이터

**제공자 관리 영역:**
- 서버, 스토리지, 네트워크
- 가상화
- 운영체제
- 미들웨어
- 런타임

**대표 서비스:**
| 제공자 | 서비스 |
|--------|--------|
| AWS | Elastic Beanstalk, App Runner |
| Azure | App Service |
| GCP | App Engine, Cloud Run |
| 기타 | Heroku, Vercel, Netlify |

**적합한 경우:**
- 빠른 개발 및 배포 필요
- 인프라 관리 부담 최소화
- 표준화된 런타임 환경 사용

#### SaaS (Software as a Service)

완성된 소프트웨어를 인터넷으로 제공.

**사용자 관리 영역:**
- 설정
- 데이터 (일부)

**제공자 관리 영역:**
- 모든 인프라
- 애플리케이션

**대표 서비스:**
| 분야 | 서비스 |
|------|--------|
| 협업 | Google Workspace, Microsoft 365, Slack |
| CRM | Salesforce, HubSpot |
| 개발 | GitHub, Jira, Confluence |
| 모니터링 | Datadog, New Relic |

**적합한 경우:**
- 표준화된 비즈니스 애플리케이션 필요
- IT 관리 역량/리소스 부족
- 빠른 도입 필요

### 서비스 모델 비교 표

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         책임 분담 비교                                   │
├─────────────────┬─────────────┬─────────────┬─────────────┬─────────────┤
│                 │ On-Premise  │    IaaS     │    PaaS     │    SaaS     │
├─────────────────┼─────────────┼─────────────┼─────────────┼─────────────┤
│ Application     │   사용자     │   사용자     │   사용자     │   제공자    │
│ Data            │   사용자     │   사용자     │   사용자     │   공유      │
│ Runtime         │   사용자     │   사용자     │   제공자     │   제공자    │
│ Middleware      │   사용자     │   사용자     │   제공자     │   제공자    │
│ OS              │   사용자     │   사용자     │   제공자     │   제공자    │
│ Virtualization  │   사용자     │   제공자     │   제공자     │   제공자    │
│ Servers         │   사용자     │   제공자     │   제공자     │   제공자    │
│ Storage         │   사용자     │   제공자     │   제공자     │   제공자    │
│ Networking      │   사용자     │   제공자     │   제공자     │   제공자    │
└─────────────────┴─────────────┴─────────────┴─────────────┴─────────────┘
```

### 배포 모델

#### Public Cloud (퍼블릭 클라우드)

클라우드 제공자가 인터넷을 통해 일반에 공개하는 리소스.

```
┌─────────────────────────────────────────┐
│           Public Cloud                   │
│  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐     │
│  │ 기업A │  │ 기업B │  │ 기업C │  │ 개인  │     │
│  └─────┘  └─────┘  └─────┘  └─────┘     │
│       ↓       ↓       ↓       ↓         │
│  ┌─────────────────────────────────┐    │
│  │      공유 인프라 풀               │    │
│  │   (Multi-tenancy)                │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

**장점:** 비용 효율, 확장성, 유지보수 불필요
**단점:** 보안 우려, 규제 제약, 커스터마이징 제한

#### Private Cloud (프라이빗 클라우드)

단일 조직 전용으로 운영되는 클라우드 환경.

```
┌─────────────────────────────────────────┐
│          Private Cloud                   │
│  ┌─────────────────────────────────┐    │
│  │         단일 조직 전용            │    │
│  │  ┌─────┐  ┌─────┐  ┌─────┐     │    │
│  │  │ 부서A │  │ 부서B │  │ 부서C │     │    │
│  │  └─────┘  └─────┘  └─────┘     │    │
│  └─────────────────────────────────┘    │
│         On-Premise or Hosted            │
└─────────────────────────────────────────┘
```

**장점:** 보안/규제 준수, 완전한 제어, 커스터마이징
**단점:** 높은 초기 비용, 자체 운영 필요

#### Hybrid Cloud (하이브리드 클라우드)

퍼블릭과 프라이빗 클라우드를 연결하여 사용.

```
┌─────────────────────────────────────────────────────────────┐
│                    Hybrid Cloud                              │
│                                                              │
│  ┌─────────────────┐         ┌─────────────────────────┐   │
│  │  Private Cloud   │ ←────→ │      Public Cloud        │   │
│  │                  │  연결   │                          │   │
│  │  - 민감 데이터    │         │  - 웹 애플리케이션        │   │
│  │  - 핵심 시스템    │         │  - 개발/테스트 환경       │   │
│  │  - 규제 대상     │         │  - 버스트 트래픽 처리     │   │
│  └─────────────────┘         └─────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**사용 사례:**
- 민감 데이터는 프라이빗, 웹 서비스는 퍼블릭
- 평상시 프라이빗, 트래픽 폭증 시 퍼블릭으로 확장 (Cloud Bursting)

#### Multi-Cloud (멀티 클라우드)

여러 클라우드 제공자를 동시에 사용.

```
┌─────────────────────────────────────────────────────────────┐
│                    Multi-Cloud                               │
│                                                              │
│     ┌─────────┐     ┌─────────┐     ┌─────────┐           │
│     │   AWS   │     │  Azure  │     │   GCP   │           │
│     │         │     │         │     │         │           │
│     │ - 컴퓨팅  │     │ - AI/ML │     │ - 데이터 │           │
│     │ - 스토리지│     │ - AD통합 │     │ - 분석   │           │
│     └─────────┘     └─────────┘     └─────────┘           │
│           ↑               ↑               ↑                 │
│           └───────────────┼───────────────┘                 │
│                           │                                 │
│                    ┌──────┴──────┐                         │
│                    │  통합 관리 계층 │                         │
│                    └─────────────┘                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**장점:**
- Vendor Lock-in 방지
- 각 제공자의 강점 활용
- 가용성/재해 복구 향상

**단점:**
- 운영 복잡성 증가
- 통합 관리 도구 필요
- 데이터 이동 비용

### 주요 클라우드 제공자 비교

| 항목 | AWS | Azure | GCP |
|------|-----|-------|-----|
| **시장 점유율** | 1위 (32%) | 2위 (22%) | 3위 (10%) |
| **강점** | 서비스 다양성, 성숙도 | MS 생태계 통합, 하이브리드 | 데이터/AI, 가격 경쟁력 |
| **컴퓨팅** | EC2 | Virtual Machines | Compute Engine |
| **서버리스** | Lambda | Functions | Cloud Functions |
| **컨테이너** | EKS, ECS | AKS | GKE |
| **DB** | RDS, DynamoDB | SQL Database, Cosmos DB | Cloud SQL, Spanner |
| **스토리지** | S3 | Blob Storage | Cloud Storage |

## 예제 코드

### AWS SDK를 이용한 S3 접근 (Java)

```java
// AWS SDK v2
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

public class S3Example {

    private final S3Client s3Client;

    public S3Example() {
        this.s3Client = S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .build();
    }

    // 파일 업로드
    public void uploadFile(String bucket, String key, Path filePath) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3Client.putObject(request, filePath);
    }

    // 파일 다운로드
    public void downloadFile(String bucket, String key, Path destination) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        s3Client.getObject(request, destination);
    }
}
```

### 인프라 구성 (Terraform)

```hcl
# AWS EC2 인스턴스 생성
provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t3.micro"

  tags = {
    Name        = "web-server"
    Environment = "production"
  }
}

# Auto Scaling Group
resource "aws_autoscaling_group" "web" {
  name                = "web-asg"
  min_size            = 2
  max_size            = 10
  desired_capacity    = 2
  vpc_zone_identifier = var.subnet_ids

  launch_template {
    id      = aws_launch_template.web.id
    version = "$Latest"
  }

  target_group_arns = [aws_lb_target_group.web.arn]
}
```

## 트레이드오프

### 클라우드 vs 온프레미스

| 항목 | 클라우드 | 온프레미스 |
|------|---------|-----------|
| **초기 비용** | 낮음 (OPEX) | 높음 (CAPEX) |
| **확장성** | 즉시 확장/축소 | 하드웨어 구매 필요 |
| **유지보수** | 제공자 담당 | 자체 인력 필요 |
| **보안 제어** | 공유 책임 모델 | 완전한 제어 |
| **규제 준수** | 제약 있을 수 있음 | 유연한 대응 |
| **네트워크 지연** | 인터넷 의존 | 내부망 사용 |

### 서비스 모델별 트레이드오프

| 모델 | 유연성 | 관리 부담 | 비용 예측성 |
|------|--------|----------|------------|
| IaaS | 높음 | 높음 | 낮음 (사용량 변동) |
| PaaS | 중간 | 낮음 | 중간 |
| SaaS | 낮음 | 매우 낮음 | 높음 (정액제) |

## 면접 예상 질문

### Q: IaaS, PaaS, SaaS의 차이점을 설명해주세요.

A: 모두 클라우드 서비스 모델이지만 추상화 수준이 다릅니다. IaaS는 가상 서버, 스토리지, 네트워크 같은 인프라를 제공하고 OS부터 애플리케이션까지 사용자가 관리합니다. AWS EC2가 대표적입니다. PaaS는 런타임과 미들웨어까지 제공하여 개발자가 코드만 작성하면 됩니다. Heroku, AWS Elastic Beanstalk이 예시입니다. SaaS는 완성된 소프트웨어를 제공하여 설정만 하면 바로 사용할 수 있습니다. Gmail, Slack이 SaaS입니다.

### Q: 하이브리드 클라우드를 선택하는 이유는 무엇인가요?

A: 크게 세 가지 이유가 있습니다. 첫째, 규제 준수입니다. 금융, 의료 등 민감 데이터는 프라이빗에 두고 일반 서비스는 퍼블릭에 배치합니다. 둘째, 기존 투자 활용입니다. 온프레미스 인프라를 유지하면서 점진적으로 클라우드를 도입합니다. 셋째, Cloud Bursting입니다. 평상시에는 프라이빗을 사용하다가 트래픽 폭증 시 퍼블릭으로 확장하여 비용을 최적화합니다.

### Q: 클라우드의 공유 책임 모델(Shared Responsibility Model)이란?

A: 클라우드 보안에서 제공자와 사용자의 책임 범위를 정의한 모델입니다. 클라우드 제공자는 "클라우드의 보안"(물리 인프라, 하이퍼바이저, 글로벌 네트워크)을 책임지고, 사용자는 "클라우드 내 보안"(데이터, IAM, 네트워크 설정, 암호화)을 책임집니다. IaaS일수록 사용자 책임이 크고, SaaS일수록 제공자 책임이 큽니다. 예를 들어 EC2에서 OS 패치는 사용자 책임이지만, Lambda에서는 제공자가 런타임을 관리합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Cloud Native](./cloud-native.md) | 클라우드 네이티브 아키텍처 | Beginner |
| [Docker](./docker.md) | 컨테이너 기반 배포 | Intermediate |
| [Serverless](./serverless.md) | FaaS, 서버리스 아키텍처 | Intermediate |
| [IaC](../devops/iac.md) | 인프라 코드화 | Intermediate |
| [FinOps](../devops/finops.md) | 클라우드 비용 최적화 | Intermediate |

## 참고 자료

- [NIST Cloud Computing Definition](https://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-145.pdf)
- [AWS Well-Architected Framework](https://aws.amazon.com/architecture/well-architected/)
- [Azure Architecture Center](https://docs.microsoft.com/en-us/azure/architecture/)
- [Google Cloud Architecture Framework](https://cloud.google.com/architecture/framework)
