# Cloud Security

> `[3] 중급` · 선수 지식: [보안이란](./what-is-security.md), [인증과 인가](./authentication-authorization.md), [Cloud Computing](../system-design/cloud-computing.md)

> 클라우드 환경에서 데이터, 애플리케이션, 인프라를 보호하기 위한 정책, 기술, 제어의 집합

`#클라우드보안` `#CloudSecurity` `#공유책임모델` `#SharedResponsibility` `#IAM` `#IdentityAccessManagement` `#보안그룹` `#SecurityGroup` `#NACL` `#NetworkACL` `#KMS` `#KeyManagementService` `#ZeroTrust` `#제로트러스트` `#CSPM` `#CloudSecurityPostureManagement` `#CWPP` `#CloudWorkloadProtection` `#암호화` `#Encryption` `#데이터보안` `#VPC` `#WAF` `#DDoS` `#Shield` `#GuardDuty` `#CloudTrail` `#SIEM`

## 왜 알아야 하는가?

- **실무**: 클라우드 환경에서의 보안 설계는 필수. 보안 사고 시 기업 신뢰도와 비용에 막대한 영향
- **면접**: "클라우드 보안의 공유 책임 모델 설명", "IAM 정책 설계 경험" 등 빈출 질문
- **기반 지식**: DevSecOps, 클라우드 아키텍처, 컴플라이언스 설계의 핵심 기반

## 핵심 개념

- **공유 책임 모델**: 클라우드 제공자와 사용자의 보안 책임 분담
- **IAM**: 누가(Who), 무엇을(What), 어디서(Where) 접근할 수 있는지 제어
- **심층 방어(Defense in Depth)**: 여러 계층의 보안 적용으로 단일 실패점 방지

## 쉽게 이해하기

**아파트 보안 비유**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      아파트 보안 = 클라우드 보안                           │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   건물 관리 (클라우드 제공자)              세입자 (클라우드 사용자)         │
│   ┌─────────────────────┐              ┌─────────────────────┐          │
│   │ • 외벽, 구조물 관리    │              │ • 현관문 잠금        │          │
│   │ • 공용 현관 보안      │              │ • 귀중품 금고 보관   │          │
│   │ • CCTV 설치/운영     │              │ • 창문 잠금 확인     │          │
│   │ • 경비원 배치        │              │ • 방문자 확인       │          │
│   └─────────────────────┘              └─────────────────────┘          │
│                                                                          │
│   ────────────────────────────────────────────────────────────          │
│                        공유 책임 모델                                     │
│   ────────────────────────────────────────────────────────────          │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

- **건물 관리**: 물리적 보안, 기반 인프라 → AWS/Azure/GCP가 담당
- **세입자**: 집 내부 보안, 개인 물품 관리 → 사용자가 담당
- **공용 공간**: 복도, 엘리베이터 → 함께 관리 (네트워크 설정 등)

## 상세 설명

### 공유 책임 모델 (Shared Responsibility Model)

클라우드 보안에서 가장 중요한 개념. 제공자와 사용자의 책임 범위를 명확히 정의합니다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      공유 책임 모델                                       │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │                     사용자 책임                                   │   │
│   │   "클라우드 내(IN) 보안"                                          │   │
│   ├─────────────────────────────────────────────────────────────────┤   │
│   │   • 데이터 암호화 및 무결성                                        │   │
│   │   • 서버 사이드 암호화 설정                                        │   │
│   │   • 네트워크 트래픽 보호 (방화벽, ACL)                             │   │
│   │   • 운영체제, 애플리케이션 패치                                    │   │
│   │   • IAM 정책, 사용자/역할 관리                                     │   │
│   │   • 보안 그룹 규칙 설정                                           │   │
│   └─────────────────────────────────────────────────────────────────┘   │
│                                                                          │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │                    제공자 책임                                    │   │
│   │   "클라우드의(OF) 보안"                                           │   │
│   ├─────────────────────────────────────────────────────────────────┤   │
│   │   • 물리적 데이터센터 보안                                         │   │
│   │   • 하드웨어 및 네트워크 인프라                                    │   │
│   │   • 하이퍼바이저 보안                                              │   │
│   │   • 글로벌 네트워크 보안                                           │   │
│   │   • 스토리지/DB 서비스 기반 보안                                   │   │
│   └─────────────────────────────────────────────────────────────────┘   │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**서비스 모델별 책임 범위:**

| 계층 | On-Premise | IaaS | PaaS | SaaS |
|------|:----------:|:----:|:----:|:----:|
| Data | 사용자 | 사용자 | 사용자 | 공유 |
| Application | 사용자 | 사용자 | 사용자 | 제공자 |
| Runtime | 사용자 | 사용자 | 제공자 | 제공자 |
| OS | 사용자 | 사용자 | 제공자 | 제공자 |
| Virtualization | 사용자 | 제공자 | 제공자 | 제공자 |
| Network | 사용자 | 제공자 | 제공자 | 제공자 |
| Storage | 사용자 | 제공자 | 제공자 | 제공자 |
| Physical | 사용자 | 제공자 | 제공자 | 제공자 |

**왜 이렇게 하는가?**
클라우드는 인프라를 공유하므로 책임 소재가 불분명해지기 쉽습니다. 공유 책임 모델을 통해 보안 사고 발생 시 책임을 명확히 하고, 각자의 영역에 집중할 수 있습니다.

### IAM (Identity and Access Management)

클라우드 리소스에 대한 접근을 제어하는 핵심 서비스입니다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           IAM 구성 요소                                   │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌──────────────┐    ┌──────────────┐    ┌──────────────┐             │
│   │    User      │    │    Group     │    │     Role     │             │
│   │   (사용자)    │    │    (그룹)    │    │    (역할)    │             │
│   └──────┬───────┘    └──────┬───────┘    └──────┬───────┘             │
│          │                   │                   │                      │
│          └───────────────────┼───────────────────┘                      │
│                              │                                          │
│                              ▼                                          │
│                    ┌──────────────────┐                                 │
│                    │     Policy       │                                 │
│                    │    (정책)        │                                 │
│                    │                  │                                 │
│                    │  Who + What +    │                                 │
│                    │  Where + When    │                                 │
│                    └────────┬─────────┘                                 │
│                             │                                           │
│                             ▼                                           │
│                    ┌──────────────────┐                                 │
│                    │    Resource      │                                 │
│                    │  (EC2, S3, RDS)  │                                 │
│                    └──────────────────┘                                 │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**IAM 정책 구조 (JSON):**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
      ],
      "Resource": "arn:aws:s3:::my-bucket/*",
      "Condition": {
        "IpAddress": {
          "aws:SourceIp": "192.168.1.0/24"
        }
      }
    }
  ]
}
```

| 요소 | 설명 | 예시 |
|------|------|------|
| **Effect** | 허용 또는 거부 | Allow, Deny |
| **Action** | 수행할 수 있는 작업 | s3:GetObject, ec2:StartInstances |
| **Resource** | 대상 리소스 | arn:aws:s3:::bucket/* |
| **Condition** | 조건부 접근 | IP 범위, 시간대, MFA 여부 |

**IAM 보안 베스트 프랙티스:**

| 원칙 | 설명 |
|------|------|
| **최소 권한 원칙** | 필요한 최소한의 권한만 부여 |
| **Root 계정 미사용** | 일상 작업에 Root 계정 사용 금지 |
| **MFA 활성화** | 모든 사용자에 다중 인증 필수 |
| **역할 기반 접근** | 사용자 대신 역할(Role) 사용 권장 |
| **정기 감사** | 미사용 자격 증명 주기적 검토 |

### 네트워크 보안

클라우드 네트워크 보안의 핵심 구성 요소입니다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        VPC 네트워크 보안 계층                              │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   Internet                                                               │
│       │                                                                  │
│       ▼                                                                  │
│   ┌───────────────────────────────────────────────────────────────┐     │
│   │  WAF (Web Application Firewall)                                │     │
│   │  - SQL Injection, XSS 방어                                     │     │
│   │  - Rate Limiting, IP 차단                                      │     │
│   └───────────────────────────────────────────────────────────────┘     │
│       │                                                                  │
│       ▼                                                                  │
│   ┌───────────────────────────────────────────────────────────────┐     │
│   │  DDoS Protection (Shield)                                      │     │
│   │  - L3/L4 DDoS 방어                                             │     │
│   │  - 자동 트래픽 스크러빙                                          │     │
│   └───────────────────────────────────────────────────────────────┘     │
│       │                                                                  │
│       ▼                                                                  │
│   ┌───────────────────────────────────────────────────────────────┐     │
│   │  VPC                                                           │     │
│   │  ┌─────────────────────────────────────────────────────────┐  │     │
│   │  │  NACL (Network ACL) - Subnet Level                       │  │     │
│   │  │  - Stateless 방화벽                                       │  │     │
│   │  │  - Inbound/Outbound 규칙 별도                             │  │     │
│   │  │  ┌─────────────────────────────────────────────────┐     │  │     │
│   │  │  │  Security Group - Instance Level                │     │  │     │
│   │  │  │  - Stateful 방화벽                               │     │  │     │
│   │  │  │  - Allow 규칙만 지원                             │     │  │     │
│   │  │  │  ┌───────────────────────────────────────┐      │     │  │     │
│   │  │  │  │           EC2 Instance               │      │     │  │     │
│   │  │  │  └───────────────────────────────────────┘      │     │  │     │
│   │  │  └─────────────────────────────────────────────────┘     │  │     │
│   │  └─────────────────────────────────────────────────────────┘  │     │
│   └───────────────────────────────────────────────────────────────┘     │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**Security Group vs NACL:**

| 항목 | Security Group | NACL |
|------|----------------|------|
| 적용 레벨 | 인스턴스(ENI) | 서브넷 |
| 상태 | Stateful | Stateless |
| 규칙 | Allow만 | Allow/Deny 모두 |
| 평가 순서 | 모든 규칙 평가 | 번호 순서대로 |
| 기본 동작 | 모두 거부 | 모두 허용 |

### 데이터 보안

클라우드에서 데이터를 보호하는 핵심 전략입니다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        데이터 보안 전략                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    │
│   │   저장 시 암호화   │    │  전송 중 암호화   │    │   사용 중 암호화   │    │
│   │   (At Rest)      │    │   (In Transit)   │    │    (In Use)      │    │
│   └────────┬────────┘    └────────┬────────┘    └────────┬────────┘    │
│            │                      │                      │              │
│            ▼                      ▼                      ▼              │
│   • S3 SSE (AES-256)     • TLS 1.2/1.3         • Confidential         │
│   • EBS 암호화            • HTTPS 강제             Computing           │
│   • RDS 암호화            • VPN/Direct Connect  • Enclave             │
│   • KMS 키 관리           • 인증서 관리          • Nitro Enclaves      │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**암호화 유형:**

| 유형 | 설명 | AWS 서비스 |
|------|------|-----------|
| **SSE-S3** | AWS 관리 키로 서버 측 암호화 | S3 기본 암호화 |
| **SSE-KMS** | KMS 고객 관리 키 사용 | KMS + S3/EBS/RDS |
| **SSE-C** | 고객 제공 키 사용 | 자체 키 관리 |
| **CSE** | 클라이언트 측 암호화 | SDK 활용 |

**KMS (Key Management Service):**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           KMS 키 계층                                     │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌─────────────────────────────────────────────────────────────────┐   │
│   │                    Customer Master Key (CMK)                     │   │
│   │                    - 키 정책으로 접근 제어                         │   │
│   │                    - 자동 키 순환 지원                             │   │
│   └───────────────────────────┬─────────────────────────────────────┘   │
│                               │                                          │
│              ┌────────────────┼────────────────┐                        │
│              ▼                ▼                ▼                        │
│   ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐          │
│   │    Data Key 1   │ │    Data Key 2   │ │    Data Key 3   │          │
│   │   (S3 객체용)    │ │   (EBS 볼륨용)   │ │   (RDS DB용)    │          │
│   └─────────────────┘ └─────────────────┘ └─────────────────┘          │
│                                                                          │
│   * Envelope Encryption: CMK로 Data Key를 암호화                        │
│   * Data Key로 실제 데이터 암호화                                        │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### Zero Trust 아키텍처

"절대 신뢰하지 말고, 항상 검증하라"는 원칙의 보안 모델입니다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       Zero Trust vs 전통적 보안                           │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   전통적 보안 (Castle and Moat)         Zero Trust                       │
│   ┌─────────────────────────────┐    ┌─────────────────────────────┐   │
│   │     ┌─────────────────┐     │    │     ┌─────────────────┐     │   │
│   │     │    신뢰 영역     │     │    │     │  🔒  🔒  🔒    │     │   │
│   │     │   (내부망)      │     │    │     │  리소스별 검증   │     │   │
│   │     │    안전!       │     │    │     │                 │     │   │
│   │     └─────────────────┘     │    │     └─────────────────┘     │   │
│   │            ▲                │    │      ▲    ▲    ▲           │   │
│   │     ┌──────┴──────┐        │    │      │    │    │           │   │
│   │     │   방화벽     │        │    │    ┌─┴────┴────┴─┐         │   │
│   │     └─────────────┘        │    │    │ 정책 엔진     │         │   │
│   │            ▲                │    │    │ (실시간 검증) │         │   │
│   │            │                │    │    └──────────────┘         │   │
│   │      외부 = 위험            │    │         ▲                   │   │
│   └─────────────────────────────┘    │    ┌────┴────┐              │   │
│                                       │    │ 모든 접근 │              │   │
│   문제점:                              │    │  = 검증  │              │   │
│   - 내부 침입 시 무방비                │    └─────────┘              │   │
│   - 경계 돌파 = 전체 위험              └─────────────────────────────┘   │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**Zero Trust 핵심 원칙:**

| 원칙 | 설명 |
|------|------|
| **최소 권한** | 필요한 최소한의 접근만 허용 |
| **명시적 검증** | 모든 요청에 대해 인증/인가 확인 |
| **침해 가정** | 이미 침해되었다고 가정하고 설계 |
| **마이크로 세그멘테이션** | 네트워크를 작은 단위로 분리 |
| **지속적 모니터링** | 모든 활동을 로깅하고 분석 |

### 클라우드 보안 서비스 (AWS 예시)

| 서비스 | 용도 | 설명 |
|--------|------|------|
| **GuardDuty** | 위협 탐지 | ML 기반 이상 행위 탐지 |
| **Security Hub** | 보안 현황 대시보드 | 통합 보안 상태 관리 |
| **Inspector** | 취약점 스캔 | EC2/ECR 취약점 자동 스캔 |
| **CloudTrail** | 감사 로깅 | 모든 API 호출 기록 |
| **Config** | 구성 관리 | 리소스 구성 변경 추적 |
| **Macie** | 데이터 보호 | 민감 데이터 자동 분류 |
| **WAF** | 웹 방화벽 | L7 공격 방어 |
| **Shield** | DDoS 방어 | L3/L4 DDoS 방어 |

## 예제 코드

### IAM 정책 예시 (Terraform)

```hcl
# 최소 권한 원칙을 적용한 S3 접근 정책
resource "aws_iam_policy" "s3_readonly" {
  name        = "S3ReadOnlyPolicy"
  description = "특정 버킷 읽기 전용 접근"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:ListBucket"
        ]
        Resource = [
          "arn:aws:s3:::my-app-bucket",
          "arn:aws:s3:::my-app-bucket/*"
        ]
        Condition = {
          StringEquals = {
            "aws:PrincipalTag/Department" = "Engineering"
          }
          Bool = {
            "aws:MultiFactorAuthPresent" = "true"
          }
        }
      }
    ]
  })
}

# 역할 생성 (EC2가 사용)
resource "aws_iam_role" "app_role" {
  name = "AppServerRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}
```

### 보안 그룹 설정 (Terraform)

```hcl
# 웹 서버 보안 그룹
resource "aws_security_group" "web" {
  name        = "web-sg"
  description = "Web server security group"
  vpc_id      = var.vpc_id

  # HTTPS만 허용
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 아웃바운드 제한 (필요한 것만)
  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS outbound"
  }

  egress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.db.id]
    description     = "PostgreSQL to DB"
  }

  tags = {
    Name = "web-sg"
  }
}

# DB 보안 그룹 - 웹 서버에서만 접근
resource "aws_security_group" "db" {
  name        = "db-sg"
  description = "Database security group"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.web.id]
    description     = "PostgreSQL from web"
  }

  # 아웃바운드 없음 (DB는 외부 연결 불필요)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = []
  }
}
```

### KMS 암호화 설정 (Java)

```java
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;
import java.util.Base64;

public class KmsEncryptionService {

    private final KmsClient kmsClient;
    private final String keyId;

    public KmsEncryptionService(String keyId) {
        this.kmsClient = KmsClient.builder().build();
        this.keyId = keyId;
    }

    // 데이터 암호화
    public String encrypt(String plaintext) {
        EncryptRequest request = EncryptRequest.builder()
            .keyId(keyId)
            .plaintext(SdkBytes.fromUtf8String(plaintext))
            .encryptionContext(Map.of(
                "purpose", "user-data",
                "service", "my-app"
            ))
            .build();

        EncryptResponse response = kmsClient.encrypt(request);
        return Base64.getEncoder()
            .encodeToString(response.ciphertextBlob().asByteArray());
    }

    // 데이터 복호화
    public String decrypt(String ciphertext) {
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);

        DecryptRequest request = DecryptRequest.builder()
            .ciphertextBlob(SdkBytes.fromByteArray(ciphertextBytes))
            .encryptionContext(Map.of(
                "purpose", "user-data",
                "service", "my-app"
            ))
            .build();

        DecryptResponse response = kmsClient.decrypt(request);
        return response.plaintext().asUtf8String();
    }
}
```

## 트레이드오프

| 보안 강화 | 트레이드오프 |
|----------|-------------|
| **엄격한 IAM 정책** | 개발 속도 저하, 권한 요청 빈번 |
| **모든 트래픽 암호화** | 지연 시간 증가, 비용 상승 |
| **세분화된 보안 그룹** | 관리 복잡도 증가 |
| **MFA 필수** | 사용자 불편, 복구 절차 필요 |
| **로깅 전체 활성화** | 스토리지 비용, 분석 부담 |

## 트러블슈팅

### 사례 1: EC2 인스턴스가 S3에 접근 불가

#### 증상
```
An error occurred (AccessDenied) when calling the GetObject operation
```

#### 원인 분석
1. IAM 역할이 EC2에 연결되지 않음
2. IAM 정책에 S3 권한 없음
3. S3 버킷 정책에서 거부
4. VPC Endpoint 미설정 (프라이빗 서브넷)

#### 해결 방법
```bash
# 1. EC2 인스턴스 프로파일 확인
aws ec2 describe-instances --instance-id i-xxx \
  --query 'Reservations[].Instances[].IamInstanceProfile'

# 2. IAM 역할 정책 확인
aws iam list-attached-role-policies --role-name MyRole
aws iam get-role-policy --role-name MyRole --policy-name MyPolicy

# 3. S3 버킷 정책 확인
aws s3api get-bucket-policy --bucket my-bucket

# 4. VPC Endpoint 추가 (프라이빗 서브넷인 경우)
aws ec2 create-vpc-endpoint \
  --vpc-id vpc-xxx \
  --service-name com.amazonaws.ap-northeast-2.s3 \
  --route-table-ids rtb-xxx
```

### 사례 2: 보안 그룹 규칙이 있는데 연결 안 됨

#### 증상
Connection timeout, Security Group 규칙은 정상

#### 원인 분석
1. NACL에서 차단
2. 라우팅 테이블 문제
3. 상대방 보안 그룹 미설정

#### 해결 방법
```bash
# NACL 규칙 확인
aws ec2 describe-network-acls --filters "Name=vpc-id,Values=vpc-xxx"

# VPC Flow Logs 분석
# REJECT 로그 확인으로 어느 계층에서 차단되는지 파악
```

## 면접 예상 질문

### Q: 클라우드 보안의 공유 책임 모델을 설명해주세요.

A: 공유 책임 모델은 클라우드 보안 책임을 제공자와 사용자가 분담하는 모델입니다. 클라우드 제공자는 "클라우드의 보안"을 담당합니다. 물리적 데이터센터, 하드웨어, 네트워크 인프라, 하이퍼바이저가 이에 해당합니다. 사용자는 "클라우드 내 보안"을 담당합니다. 데이터 암호화, IAM 설정, 네트워크 구성, OS 패치가 이에 해당합니다. IaaS일수록 사용자 책임이 크고, SaaS일수록 제공자 책임이 큽니다.

### Q: IAM 정책 설계 시 최소 권한 원칙을 어떻게 적용하나요?

A: 첫째, 와일드카드(*) 사용을 피하고 필요한 Action만 명시합니다. 둘째, Resource를 특정 리소스 ARN으로 제한합니다. 셋째, Condition을 활용해 IP, 시간대, MFA 여부 등 조건을 추가합니다. 넷째, IAM Access Analyzer를 사용해 실제 사용되지 않는 권한을 찾아 제거합니다. 마지막으로 정기적으로 권한을 검토하고 미사용 자격 증명을 비활성화합니다.

### Q: Security Group과 NACL의 차이점은 무엇인가요?

A: Security Group은 인스턴스(ENI) 레벨에서 동작하는 Stateful 방화벽입니다. Allow 규칙만 지원하고, 인바운드를 허용하면 아웃바운드는 자동 허용됩니다. NACL은 서브넷 레벨에서 동작하는 Stateless 방화벽입니다. Allow/Deny 모두 지원하고, 인바운드와 아웃바운드를 별도로 설정해야 합니다. 일반적으로 Security Group을 주로 사용하고, NACL은 서브넷 전체 차단이 필요할 때 보조적으로 사용합니다.

### Q: Zero Trust 보안 모델이란 무엇인가요?

A: "절대 신뢰하지 말고, 항상 검증하라"는 원칙의 보안 모델입니다. 전통적인 경계 기반 보안(Castle and Moat)과 달리, 내부/외부 구분 없이 모든 접근을 검증합니다. 핵심 원칙은 최소 권한, 명시적 검증, 침해 가정, 마이크로 세그멘테이션입니다. 클라우드 환경에서는 IAM으로 세분화된 접근 제어, Security Group으로 마이크로 세그멘테이션, 모든 트래픽 암호화, CloudTrail로 지속적 모니터링을 통해 구현합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [보안이란](./what-is-security.md) | 보안 기초 개념 | [1] 정의 |
| [인증과 인가](./authentication-authorization.md) | IAM의 기반 개념 | [3] 중급 |
| [Cloud Computing](../system-design/cloud-computing.md) | 클라우드 기초 | [1] 정의 |
| [VPC](../network/vpc.md) | 클라우드 네트워크 | [3] 중급 |
| [HTTPS와 TLS](./https-tls.md) | 전송 암호화 | [3] 중급 |
| [DevSecOps](../devops/devsecops.md) | 보안 자동화 | [3] 중급 |

## 참고 자료

- [AWS Shared Responsibility Model](https://aws.amazon.com/compliance/shared-responsibility-model/)
- [AWS Well-Architected Framework - Security Pillar](https://docs.aws.amazon.com/wellarchitected/latest/security-pillar/)
- [Azure Security Documentation](https://docs.microsoft.com/en-us/azure/security/)
- [GCP Security Best Practices](https://cloud.google.com/security/best-practices)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)
- [Zero Trust Architecture - NIST SP 800-207](https://nvlpubs.nist.gov/nistpubs/SpecialPublications/NIST.SP.800-207.pdf)
