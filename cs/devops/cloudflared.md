# Cloudflared (Cloudflare Tunnel)

> `[2] 입문` · 선수 지식: 없음 (기본 네트워크 개념 권장)

> 공인 IP 없이 로컬 서버를 인터넷에 안전하게 노출시키는 Cloudflare의 터널링 도구

`#Cloudflared` `#CloudflareTunnel` `#터널링` `#Tunneling` `#ZeroTrust` `#제로트러스트` `#리버스프록시` `#ReverseProxy` `#포트포워딩대안` `#PortForwarding` `#ngrok대안` `#셀프호스팅` `#SelfHosted` `#보안터널` `#SecureTunnel` `#Cloudflare` `#Argo` `#ArgoTunnel` `#인바운드차단` `#OutboundOnly` `#DDoS방어` `#무료터널` `#홈서버` `#HomeServer` `#원격접속` `#SSH터널` `#웹서버노출` `#동적IP`

## 왜 알아야 하는가?

- **실무**: 공인 IP 없이 개발 서버 외부 노출, 웹훅 테스트, 데모 환경 구축
- **면접**: Zero Trust 보안 모델, 네트워크 보안 이해도 확인
- **기반 지식**: 터널링, 리버스 프록시, 보안 아키텍처의 실제 적용

## 핵심 개념

- **Outbound-Only 연결**: 내부에서 외부로만 연결, 인바운드 포트 개방 불필요
- **Zero Trust**: 모든 접속을 신뢰하지 않고 검증하는 보안 모델
- **터널(Tunnel)**: cloudflared 데몬과 Cloudflare 네트워크 간의 암호화된 연결
- **커넥터(Connector)**: 로컬 서비스와 Cloudflare를 연결하는 cloudflared 프로세스

## 쉽게 이해하기

**비밀 통로 비유**

```
┌─────────────────────────────────────────────────────────────────┐
│                  기존 포트 포워딩 vs Cloudflare Tunnel            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [포트 포워딩] - 집 문을 열어두는 것                             │
│                                                                  │
│   인터넷 ──────────► 라우터 ──────────► 서버                    │
│              ⚠️ 포트 개방              (직접 노출)               │
│              ⚠️ 공인 IP 필요                                     │
│              ⚠️ 공격에 취약                                      │
│                                                                  │
│  [Cloudflare Tunnel] - 비밀 통로로 나가는 것                     │
│                                                                  │
│   인터넷 ◄──── Cloudflare ◄════════ 서버                        │
│          (보호)         (암호화된 터널)  (아웃바운드만)          │
│              ✓ 포트 개방 불필요                                  │
│              ✓ 공인 IP 불필요                                    │
│              ✓ DDoS 방어 포함                                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

**핵심 원리**: 서버가 먼저 Cloudflare에 "전화"를 걸어 연결을 맺습니다. 외부 요청은 이 연결을 통해 전달되므로, 서버는 인바운드 포트를 열 필요가 없습니다.

## 상세 설명

### 동작 원리

```
┌────────────────────────────────────────────────────────────────────┐
│                    Cloudflare Tunnel 동작 흐름                      │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  1. cloudflared 시작                                               │
│     서버 ═══════════════════════════════► Cloudflare Edge          │
│           (아웃바운드 연결, 포트 개방 X)                            │
│                                                                     │
│  2. 사용자 요청                                                    │
│     사용자 ──────────► Cloudflare Edge                             │
│              (HTTPS)   (DDoS 방어, WAF, 캐싱)                      │
│                                                                     │
│  3. 터널 통해 전달                                                 │
│     Cloudflare Edge ═══════════════════► 서버                      │
│                      (기존 연결 재사용)   (localhost:8080)         │
│                                                                     │
│  4. 응답 반환                                                      │
│     서버 ═══► Cloudflare ═══► 사용자                               │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### ngrok vs Cloudflare Tunnel

| 항목 | ngrok (무료) | Cloudflare Tunnel |
|------|-------------|-------------------|
| **비용** | 무료 (제한적) | 무료 (무제한) |
| **커스텀 도메인** | 유료 | 무료 (Cloudflare 도메인 필요) |
| **URL 고정** | 유료 | 무료 |
| **대역폭** | 제한 있음 | 무제한 |
| **동시 연결** | 1개 (무료) | 무제한 |
| **설정 복잡도** | 낮음 | 중간 |
| **DDoS 방어** | 제한적 | 포함 |
| **인증 옵션** | 기본 | Zero Trust 통합 |

### 사용 사례

```
┌────────────────────────────────────────────────────────────────┐
│                      주요 사용 사례                              │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 웹훅 테스트                                                 │
│     └─ Stripe, GitHub 웹훅을 로컬에서 수신                     │
│                                                                 │
│  2. 홈서버 외부 노출                                            │
│     └─ NAS, 미디어 서버, Home Assistant                        │
│                                                                 │
│  3. 개발 환경 공유                                              │
│     └─ 로컬 개발 서버를 팀원/클라이언트에게 공유               │
│                                                                 │
│  4. SSH 원격 접속                                               │
│     └─ 포트 22 개방 없이 안전한 SSH 접속                       │
│                                                                 │
│  5. 셀프호스팅 서비스                                           │
│     └─ n8n, Gitea, Nextcloud 등 외부 접속                      │
│                                                                 │
│  6. IoT 디바이스 접근                                           │
│     └─ 라즈베리파이, 스마트홈 기기 원격 관리                   │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 설치 및 설정

### 1. cloudflared 설치

**Windows (winget)**

```shell
winget install Cloudflare.cloudflared
```

**Windows (Chocolatey)**

```shell
choco install cloudflared
```

**macOS**

```bash
brew install cloudflared
```

**Linux (Debian/Ubuntu)**

```bash
# 저장소 추가
curl -fsSL https://pkg.cloudflare.com/cloudflare-main.gpg | sudo tee /usr/share/keyrings/cloudflare-main.gpg >/dev/null
echo 'deb [signed-by=/usr/share/keyrings/cloudflare-main.gpg] https://pkg.cloudflare.com/cloudflared focal main' | sudo tee /etc/apt/sources.list.d/cloudflared.list

# 설치
sudo apt update && sudo apt install cloudflared
```

**Docker**

```bash
docker pull cloudflare/cloudflared
```

### 2. Quick Tunnel (임시 터널)

가장 빠른 방법. 계정 없이 즉시 사용 가능 (URL은 랜덤, 재시작 시 변경)

```bash
# 로컬 8080 포트를 외부에 노출
cloudflared tunnel --url http://localhost:8080
```

출력 예시:
```
2025-01-15T10:00:00Z INF +----------------------------+
2025-01-15T10:00:00Z INF |  Your quick Tunnel URL is  |
2025-01-15T10:00:00Z INF |  https://random-words.trycloudflare.com  |
2025-01-15T10:00:00Z INF +----------------------------+
```

### 3. Named Tunnel (영구 터널) - 권장

고정 URL과 커스텀 도메인 사용 가능

#### Step 1: Cloudflare 로그인

```bash
cloudflared tunnel login
```

브라우저가 열리면 Cloudflare 계정으로 로그인하고 도메인 선택

#### Step 2: 터널 생성

```bash
# 터널 생성
cloudflared tunnel create my-tunnel

# 생성된 터널 확인
cloudflared tunnel list
```

#### Step 3: 설정 파일 작성

```yaml
# ~/.cloudflared/config.yml (Linux/macOS)
# C:\Users\사용자명\.cloudflared\config.yml (Windows)

tunnel: my-tunnel
credentials-file: /path/to/.cloudflared/<TUNNEL_ID>.json

ingress:
  # 서브도메인별 라우팅
  - hostname: app.example.com
    service: http://localhost:8080

  - hostname: api.example.com
    service: http://localhost:3000

  - hostname: ssh.example.com
    service: ssh://localhost:22

  # 기본 (매칭되지 않은 요청)
  - service: http_status:404
```

#### Step 4: DNS 레코드 생성

```bash
# Cloudflare DNS에 CNAME 레코드 자동 생성
cloudflared tunnel route dns my-tunnel app.example.com
```

#### Step 5: 터널 실행

```bash
# 포그라운드 실행
cloudflared tunnel run my-tunnel

# 또는 설정 파일 지정
cloudflared tunnel --config ~/.cloudflared/config.yml run
```

### 4. 서비스로 등록 (자동 시작)

**Windows**

```shell
# 관리자 권한으로 실행
cloudflared service install
```

**Linux (systemd)**

```bash
sudo cloudflared service install
sudo systemctl enable cloudflared
sudo systemctl start cloudflared
```

**macOS (launchd)**

```bash
sudo cloudflared service install
sudo launchctl start com.cloudflare.cloudflared
```

### 5. Docker Compose 설정

```yaml
# docker-compose.yml
version: '3.8'

services:
  # 터널을 통해 노출할 앱
  webapp:
    image: nginx
    container_name: webapp

  # Cloudflare Tunnel
  cloudflared:
    image: cloudflare/cloudflared:latest
    container_name: cloudflared
    restart: always
    command: tunnel --no-autoupdate run --token ${TUNNEL_TOKEN}
    environment:
      - TUNNEL_TOKEN=${TUNNEL_TOKEN}
    depends_on:
      - webapp
```

`.env` 파일:
```env
TUNNEL_TOKEN=your_tunnel_token_here
```

> **토큰 얻기**: Cloudflare Zero Trust 대시보드 → Networks → Tunnels → 터널 선택 → Install connector → 토큰 복사

## 고급 설정

### Access 정책 (인증 추가)

Cloudflare Zero Trust Access로 접근 제어 추가

```yaml
# config.yml에 access 설정 추가
ingress:
  - hostname: admin.example.com
    service: http://localhost:8080
    originRequest:
      # Access 정책 적용
      access:
        required: true
        teamName: my-team
```

### SSH 접속 설정

```yaml
# config.yml
ingress:
  - hostname: ssh.example.com
    service: ssh://localhost:22
  - service: http_status:404
```

클라이언트 측 설정 (`~/.ssh/config`):
```
Host ssh.example.com
  ProxyCommand cloudflared access ssh --hostname %h
```

### 로드밸런싱

```yaml
ingress:
  - hostname: app.example.com
    service: http://localhost:8080
    originRequest:
      # 여러 origin 서버 로드밸런싱
      connectTimeout: 30s
      noTLSVerify: true
```

## 트러블슈팅

| 문제 | 원인 | 해결 방법 |
|------|------|----------|
| `failed to connect` | 네트워크 차단 | 방화벽에서 아웃바운드 443 허용 |
| `tunnel not found` | 터널 ID 불일치 | `cloudflared tunnel list`로 확인 |
| `502 Bad Gateway` | 로컬 서비스 미실행 | 대상 서비스 실행 상태 확인 |
| `DNS error` | DNS 레코드 미등록 | `cloudflared tunnel route dns` 실행 |
| 느린 응답 | 리전 문제 | `--region` 옵션으로 가까운 리전 지정 |

### 디버깅

```bash
# 상세 로그 출력
cloudflared tunnel --loglevel debug run my-tunnel

# 연결 상태 확인
cloudflared tunnel info my-tunnel
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 포트 개방 불필요 (보안 향상) | Cloudflare 의존성 |
| 공인 IP/고정 IP 불필요 | 설정이 ngrok보다 복잡 |
| 무료로 무제한 사용 | Cloudflare 도메인 필요 |
| DDoS 방어 포함 | 일부 프로토콜 미지원 |
| Zero Trust 통합 가능 | 대역폭은 Cloudflare 경유 |

## 면접 예상 질문

### Q: Cloudflare Tunnel과 VPN의 차이점은?

A: **연결 방향과 목적**이 다릅니다.

| 항목 | Cloudflare Tunnel | VPN |
|------|-------------------|-----|
| 방향 | 아웃바운드 (서버→클라우드) | 양방향 |
| 목적 | 특정 서비스 노출 | 네트워크 전체 연결 |
| 클라이언트 | 불필요 (웹 접속) | 필요 |
| 인증 | Cloudflare Access | VPN 자체 인증 |

Tunnel은 "서비스 하나를 안전하게 노출"하는 것이고, VPN은 "네트워크 전체를 연결"하는 것입니다.

### Q: 왜 Outbound-Only 연결이 더 안전한가?

A: **공격 표면(Attack Surface) 감소** 때문입니다.

1. **인바운드 포트 없음**: 포트 스캔으로 발견 불가
2. **IP 비노출**: 서버 IP를 알 수 없어 직접 공격 불가
3. **중간자 보호**: 모든 트래픽이 Cloudflare 경유하며 암호화
4. **방화벽 단순화**: 아웃바운드 443만 허용하면 됨

기존 방식은 "문을 열어두고 누가 오나 기다리는 것"이고, Tunnel은 "내가 먼저 안전한 곳에 전화해서 연결하는 것"입니다.

### Q: Quick Tunnel과 Named Tunnel 중 언제 무엇을 사용하는가?

A:

| 상황 | 선택 |
|------|------|
| 임시 테스트, 데모 | Quick Tunnel |
| 웹훅 테스트 (일회성) | Quick Tunnel |
| 프로덕션 서비스 | Named Tunnel |
| 고정 URL 필요 | Named Tunnel |
| 커스텀 도메인 | Named Tunnel |
| 서비스 자동 시작 | Named Tunnel + Service |

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 배포 파이프라인에서 터널 활용 | 입문 |
| [Docker](../system-design/docker.md) | 컨테이너화된 터널 실행 | 중급 |
| [n8n](../automation/n8n.md) | 웹훅 수신을 위한 터널 활용 | 입문 |

## 참고 자료

- [Cloudflare Tunnel 공식 문서](https://developers.cloudflare.com/cloudflare-one/networks/connectors/cloudflare-tunnel/)
- [cloudflared GitHub](https://github.com/cloudflare/cloudflared)
- [Cloudflare Zero Trust](https://developers.cloudflare.com/cloudflare-one/)
- [I finally understand Cloudflare Zero Trust tunnels](https://david.coffee/cloudflare-zero-trust-tunnels/)
