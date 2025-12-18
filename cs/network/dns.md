# DNS (Domain Name System)

> 사람이 읽을 수 있는 도메인 이름을 컴퓨터가 이해하는 IP 주소로 변환하는 분산 데이터베이스 시스템

## 핵심 개념

- **이름 해석(Name Resolution)**: 도메인 → IP 주소 변환
- **분산 계층 구조**: Root → TLD → Authoritative 네임서버
- **캐싱**: TTL 기반 캐시로 응답 속도 향상 및 부하 분산

## 쉽게 이해하기

**DNS는 인터넷의 전화번호부**

- 친구 이름(도메인)으로 전화번호(IP)를 찾는 것과 같음
- 모든 번호를 외울 수 없으니 전화번호부(DNS)를 사용
- 자주 쓰는 번호는 단축번호(캐시)로 저장

```
"google.com 어디야?"

┌─────────┐    도메인     ┌─────────┐    IP 주소
│ Browser │ ──────────→ │   DNS   │ ──────────→ 142.250.196.110
└─────────┘  google.com  └─────────┘
```

## 상세 설명

### DNS 계층 구조

```
                    ┌─────────────────┐
                    │   Root DNS (.)  │  ← 전 세계 13개 루트 서버
                    └────────┬────────┘
                             │
         ┌───────────────────┼───────────────────┐
         ▼                   ▼                   ▼
   ┌──────────┐        ┌──────────┐        ┌──────────┐
   │ .com TLD │        │ .org TLD │        │ .kr TLD  │  ← Top Level Domain
   └────┬─────┘        └────┬─────┘        └────┬─────┘
        │                   │                   │
   ┌────▼─────┐        ┌────▼─────┐        ┌────▼─────┐
   │ google   │        │ wikipedia│        │ naver    │  ← Authoritative NS
   │  .com    │        │  .org    │        │  .co.kr  │
   └──────────┘        └──────────┘        └──────────┘
```

| 계층 | 역할 | 예시 |
|------|------|------|
| **Root** | 최상위, TLD 서버 위치 알려줌 | `.` (13개 루트 서버) |
| **TLD** | 도메인 확장자별 관리 | `.com`, `.org`, `.kr` |
| **Authoritative** | 실제 도메인-IP 매핑 보유 | `ns1.google.com` |

**왜 계층 구조인가?**
- 단일 서버로는 전 세계 도메인 처리 불가능
- 분산 처리로 부하 분산 및 장애 격리
- 각 계층이 하위 계층 정보만 관리하여 효율적

### DNS 질의 과정

```
┌──────────┐                                              ┌──────────────┐
│  Client  │                                              │ Root DNS (.) │
└────┬─────┘                                              └──────┬───────┘
     │                                                           │
     │  1. www.example.com?                                      │
     ▼                                                           │
┌──────────────┐                                                 │
│ Local DNS    │  2. Root에 질의 ─────────────────────────────→ │
│ (Resolver)   │  ←──────────────────────────── 3. .com TLD 주소 │
│              │                                                 │
│              │  4. TLD에 질의 ─────────────────────────────→ ┌─────────────┐
│              │  ←──────────────────────── 5. Authoritative 주소│  .com TLD   │
│              │                                               └─────────────┘
│              │  6. Authoritative에 질의 ───────────────────→ ┌─────────────┐
│              │  ←──────────────────────────── 7. IP 주소 반환 │ example.com │
└──────┬───────┘                                               │     NS      │
       │                                                       └─────────────┘
       │  8. IP 주소 반환
       ▼
┌──────────┐
│  Client  │  → 142.250.196.110 으로 접속
└──────────┘
```

### Recursive vs Iterative 질의

| 방식 | 설명 | 사용 주체 |
|------|------|----------|
| **Recursive** | Resolver가 대신 모든 질의 수행 후 최종 결과 반환 | 클라이언트 → Resolver |
| **Iterative** | 각 서버가 다음 서버 주소만 알려줌, 직접 질의 | Resolver → DNS 서버들 |

**왜 이렇게 나누는가?**
- 클라이언트는 단순하게 유지 (Recursive)
- DNS 서버 간에는 부하 분산 (Iterative)

### DNS 레코드 타입

| 타입 | 용도 | 예시 |
|------|------|------|
| **A** | 도메인 → IPv4 | `example.com → 93.184.216.34` |
| **AAAA** | 도메인 → IPv6 | `example.com → 2606:2800:220:1:...` |
| **CNAME** | 도메인 별칭 | `www.example.com → example.com` |
| **MX** | 메일 서버 | `example.com → mail.example.com` |
| **NS** | 네임서버 지정 | `example.com → ns1.example.com` |
| **TXT** | 텍스트 정보 (SPF, 도메인 인증) | `v=spf1 include:_spf.google.com` |
| **PTR** | IP → 도메인 (역방향) | `34.216.184.93 → example.com` |

### DNS 캐싱과 TTL

```
┌─────────────────────────────────────────────────────────┐
│                    DNS 캐싱 계층                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Browser Cache → OS Cache → Router → ISP DNS → Root    │
│       ↑             ↑          ↑         ↑              │
│    수 분         수 분~시간   시간~일    시간~일          │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**TTL (Time To Live)**

```
; DNS 레코드 예시
example.com.    3600    IN    A    93.184.216.34
                 ↑
              TTL: 3600초 (1시간) 동안 캐시 유지
```

| TTL 설정 | 장점 | 단점 |
|----------|------|------|
| **짧은 TTL** (60s) | 변경사항 빠른 반영 | DNS 질의 증가, 부하 증가 |
| **긴 TTL** (86400s) | 캐시 적중률 높음, 빠른 응답 | 변경 반영 지연 |

**왜 TTL이 중요한가?**
- 서버 IP 변경 시 TTL 만큼 기다려야 전파
- 장애 대응 시 낮은 TTL이 유리
- 평소에는 높은 TTL로 성능 최적화

### DNS 보안

#### 기존 DNS의 문제점

```
일반 DNS (UDP 53)

┌────────┐  평문 질의  ┌─────────┐
│ Client │ ─────────→ │   DNS   │
│        │ ←───────── │ Server  │
└────────┘  평문 응답  └─────────┘
      ↑
   도청/변조 가능 (DNS Spoofing)
```

#### 보안 강화 방법

| 기술 | 설명 | 포트 |
|------|------|------|
| **DNSSEC** | DNS 응답에 디지털 서명 추가 | UDP 53 |
| **DoH** (DNS over HTTPS) | HTTPS로 DNS 질의 암호화 | TCP 443 |
| **DoT** (DNS over TLS) | TLS로 DNS 질의 암호화 | TCP 853 |

```java
// Java에서 DNS 조회
InetAddress[] addresses = InetAddress.getAllByName("google.com");
for (InetAddress addr : addresses) {
    System.out.println(addr.getHostAddress());
}

// Spring에서 커스텀 DNS Resolver 설정
@Bean
public DnsResolver customDnsResolver() {
    return new SystemDefaultDnsResolver() {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            if ("custom.local".equals(host)) {
                return new InetAddress[] { InetAddress.getByName("127.0.0.1") };
            }
            return super.resolve(host);
        }
    };
}
```

### DNS 장애 대응

#### 1. DNS Failover

```
┌─────────────┐
│ DNS Server  │
└──────┬──────┘
       │
       │  Health Check 실패 시
       ▼
┌──────────────────────────────────┐
│ Primary: 10.0.0.1 (unhealthy)   │ → 제외
│ Secondary: 10.0.0.2 (healthy)   │ → 응답
└──────────────────────────────────┘
```

#### 2. GeoDNS (지역 기반)

```
사용자 위치에 따라 가장 가까운 서버 IP 반환

한국 사용자 → 서울 서버 (10.0.1.1)
미국 사용자 → 버지니아 서버 (10.0.2.1)
```

#### 3. 라운드 로빈

```
; 여러 IP를 순환하며 반환
example.com.  IN  A  10.0.0.1
example.com.  IN  A  10.0.0.2
example.com.  IN  A  10.0.0.3
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 사람이 기억하기 쉬운 도메인 사용 | DNS 장애 시 전체 서비스 영향 |
| 부하 분산, 장애 대응 가능 | 전파 지연 (TTL) |
| 캐싱으로 빠른 응답 | 보안 취약점 (Spoofing) |
| 유연한 서버 이전 | 추가 네트워크 홉 발생 |

## 실무 체크리스트

### DNS 설정 시

- [ ] 적절한 TTL 설정 (평소 높게, 변경 전 낮게)
- [ ] 최소 2개 이상 네임서버 등록 (이중화)
- [ ] SPF/DKIM/DMARC 레코드 설정 (메일 보안)
- [ ] CNAME vs A 레코드 선택 검토

### 장애 대응 시

- [ ] `nslookup`, `dig` 명령어로 DNS 확인
- [ ] TTL 확인 후 전파 시간 계산
- [ ] 브라우저/OS DNS 캐시 클리어

```bash
# DNS 조회 명령어
nslookup google.com
dig google.com +trace    # 전체 경로 추적
dig google.com ANY       # 모든 레코드 조회

# DNS 캐시 클리어 (Windows)
ipconfig /flushdns

# DNS 캐시 클리어 (macOS)
sudo dscacheutil -flushcache
```

## 면접 예상 질문

### Q: DNS 동작 과정을 설명해주세요.

A: 브라우저가 도메인을 입력받으면 먼저 **로컬 캐시**를 확인합니다. 없으면 **Local DNS(Resolver)**에 질의하고, Resolver는 **Root → TLD → Authoritative** 순으로 Iterative 질의를 수행합니다. 각 서버는 다음 서버 주소를 알려주고, 최종적으로 Authoritative NS가 IP 주소를 반환합니다. 결과는 TTL 동안 캐싱되어 이후 질의 속도가 빨라집니다.

### Q: TTL이 짧으면 어떤 장단점이 있나요?

A: **장점**: 서버 IP 변경이나 장애 대응 시 빠르게 반영됩니다. **단점**: 캐시 만료가 빨라 DNS 질의가 증가하고, 응답 지연과 DNS 서버 부하가 높아집니다. 실무에서는 평소에 긴 TTL(1시간~1일)을 사용하다가 **서버 마이그레이션 전에 TTL을 낮추는** 전략을 씁니다.

### Q: DNS Spoofing이란 무엇이고 어떻게 방어하나요?

A: 공격자가 **위조된 DNS 응답**을 보내 사용자를 악성 사이트로 유도하는 공격입니다. 기존 DNS는 UDP 기반 평문 통신이라 취약합니다. 방어 방법으로는:
1. **DNSSEC**: DNS 응답에 디지털 서명 추가
2. **DoH/DoT**: HTTPS/TLS로 DNS 통신 암호화
3. 신뢰할 수 있는 DNS 서버 사용 (8.8.8.8, 1.1.1.1)

## 참고 자료

- [RFC 1035 - Domain Names](https://datatracker.ietf.org/doc/html/rfc1035)
- [Cloudflare - What is DNS?](https://www.cloudflare.com/learning/dns/what-is-dns/)
- [AWS Route 53 Documentation](https://docs.aws.amazon.com/route53/)
