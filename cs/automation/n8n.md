# n8n

> `[2] 입문` · 선수 지식: 없음 (노코드), JavaScript 기초 (커스텀 로직 시)

> 셀프 호스팅이 가능한 오픈소스 워크플로우 자동화 플랫폼

`#n8n` `#워크플로우` `#Workflow` `#자동화` `#Automation` `#노코드` `#NoCode` `#로우코드` `#LowCode` `#오픈소스` `#OpenSource` `#Zapier대안` `#셀프호스팅` `#SelfHosted` `#Docker` `#API통합` `#Integration` `#트리거` `#Trigger` `#웹훅` `#Webhook` `#크론` `#Cron` `#노드기반` `#시각적워크플로우` `#업무자동화` `#iPaaS`

## 왜 알아야 하는가?

- **실무**: Zapier 비용 절감, 민감한 데이터의 사내 처리, 복잡한 워크플로우 구현
- **면접**: 자동화 도구 선택 기준, 오픈소스 활용 경험, 비용 최적화 사례
- **기반 지식**: 워크플로우 자동화 패턴, 이벤트 기반 아키텍처, API 연동 이해

## 핵심 개념

- **노드(Node)**: 워크플로우의 각 작업 단위 (트리거, 액션, 조건 등)
- **워크플로우(Workflow)**: 노드들의 연결로 구성된 자동화 흐름
- **트리거(Trigger)**: 워크플로우를 시작하는 이벤트 (웹훅, 스케줄, 수동)
- **크레덴셜(Credential)**: 외부 서비스 인증 정보 관리

## 쉽게 이해하기

**레고 블록으로 자동화 만들기**

```
┌─────────────────────────────────────────────────────────────┐
│                     n8n 워크플로우                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   [트리거]      [처리]       [조건]       [액션]            │
│   ┌─────┐      ┌─────┐      ┌─────┐      ┌─────┐           │
│   │ 📥  │ ──► │ 🔄  │ ──► │ ❓  │ ──► │ 📤  │           │
│   │웹훅 │      │변환 │      │분기 │      │전송 │           │
│   └─────┘      └─────┘      └─────┘      └─────┘           │
│                                    │                        │
│                                    └──► [다른 액션]         │
│                                                              │
│   각 블록(노드)을 드래그 & 연결하면 자동화 완성!            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**비유**: n8n은 "자동화의 레고"
- 블록(노드)을 조립하듯 연결
- 코드 없이 복잡한 흐름 구현
- 필요하면 코드 블록 추가 가능

## 상세 설명

### n8n 아키텍처

```
┌────────────────────────────────────────────────────────────────┐
│                      n8n 아키텍처                               │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────┐      │
│   │                    n8n Server                        │      │
│   │  ┌───────────┐  ┌───────────┐  ┌───────────┐       │      │
│   │  │  Editor   │  │  Worker   │  │  Webhook  │       │      │
│   │  │   (UI)    │  │ (실행기)  │  │ (수신기)  │       │      │
│   │  └───────────┘  └───────────┘  └───────────┘       │      │
│   └─────────────────────────────────────────────────────┘      │
│                              │                                  │
│              ┌───────────────┼───────────────┐                 │
│              ▼               ▼               ▼                 │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│   │   Database   │  │  Credential  │  │   외부 API   │        │
│   │  (SQLite/    │  │    Store     │  │  (Slack,     │        │
│   │   Postgres)  │  │  (암호화)    │  │   GitHub...) │        │
│   └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### 배포 옵션

| 옵션 | 설명 | 적합한 경우 |
|------|------|------------|
| **n8n.cloud** | 관리형 SaaS | 빠른 시작, 소규모 팀 |
| **Docker** | 컨테이너 배포 | 개발/테스트, 단일 서버 |
| **Kubernetes** | 오케스트레이션 | 대규모, 고가용성 필요 |
| **npm** | 직접 설치 | 커스텀 환경 |

### 노드 종류

```
┌────────────────────────────────────────────────────────────────┐
│                      노드 카테고리                              │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 트리거 노드 (Trigger)                                      │
│     - Webhook: HTTP 요청 수신                                  │
│     - Schedule: Cron 기반 스케줄                               │
│     - 앱별 트리거: Gmail, Slack, GitHub 등                     │
│                                                                 │
│  2. 일반 노드 (Regular)                                        │
│     - HTTP Request: API 호출                                   │
│     - 앱 노드: 400+ 서비스 연동                                │
│     - Function: JavaScript 커스텀 로직                         │
│                                                                 │
│  3. 흐름 제어 노드 (Flow)                                      │
│     - IF: 조건 분기                                            │
│     - Switch: 다중 분기                                        │
│     - Merge: 여러 흐름 합치기                                  │
│     - Split In Batches: 배치 처리                              │
│                                                                 │
│  4. 데이터 노드 (Transform)                                    │
│     - Set: 데이터 설정                                         │
│     - Code: JavaScript/Python 실행                             │
│     - Item Lists: 배열 조작                                    │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 예제 워크플로우

### 1. GitHub Issue → Slack 알림

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   GitHub     │    │     Set      │    │    Slack     │
│   Trigger    │───►│   (가공)     │───►│   (알림)     │
│              │    │              │    │              │
│ On Issue     │    │ 메시지 포맷  │    │ #dev 채널    │
│ Created      │    │ 생성         │    │ 전송         │
└──────────────┘    └──────────────┘    └──────────────┘
```

**Set 노드 설정 (JSON)**:
```json
{
  "message": "새 이슈: {{ $json.issue.title }}",
  "url": "{{ $json.issue.html_url }}",
  "author": "{{ $json.issue.user.login }}"
}
```

### 2. 스케줄 기반 데이터 수집

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Schedule   │    │    HTTP      │    │    Code      │    │   Google     │
│   Trigger    │───►│   Request    │───►│   (변환)     │───►│   Sheets     │
│              │    │              │    │              │    │              │
│ 매일 9시     │    │ API 호출     │    │ 데이터 가공  │    │ 행 추가      │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
```

**Code 노드 (JavaScript)**:
```javascript
// 입력 데이터 가공
const items = $input.all();

return items.map(item => {
  return {
    json: {
      date: new Date().toISOString().split('T')[0],
      value: item.json.data.value,
      change: ((item.json.data.value - item.json.data.previous) / item.json.data.previous * 100).toFixed(2) + '%'
    }
  };
});
```

### 3. 웹훅 → 조건 분기 → 다중 액션

```
                                    ┌──────────────┐
                               ┌───►│    Slack     │
                               │    │  (긴급 알림) │
┌──────────────┐    ┌─────┐    │    └──────────────┘
│   Webhook    │───►│ IF  │────┤
│   Trigger    │    │     │    │    ┌──────────────┐
│              │    │     │    └───►│    Email     │
│ POST /alert  │    │     │         │  (일반 알림) │
└──────────────┘    └─────┘         └──────────────┘
                       │
                 priority === 'high'
                    ? Slack
                    : Email
```

### 4. 에러 처리 패턴

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Trigger    │───►│    HTTP      │───►│   Success    │
│              │    │   Request    │    │   Action     │
└──────────────┘    └──────┬───────┘    └──────────────┘
                           │
                           │ Error
                           ▼
                    ┌──────────────┐    ┌──────────────┐
                    │    Error     │───►│   Slack      │
                    │   Trigger    │    │  (에러 알림) │
                    └──────────────┘    └──────────────┘
```

### 5. AI 연동: 고객 문의 자동 분류 및 응답

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   Webhook    │    │   OpenAI     │    │    Switch    │    │   Actions    │
│   Trigger    │───►│   (분류)     │───►│   (분기)     │───►│              │
│              │    │              │    │              │    │              │
│ POST /inquiry│    │ 카테고리 +   │    │ 카테고리별   │    │ Slack/Email/ │
│              │    │ 긴급도 판단  │    │ 라우팅       │    │ Notion 등    │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   OpenAI     │
                    │   (답변)     │
                    │              │
                    │ 초안 생성    │
                    └──────────────┘
```

**워크플로우 상세 흐름:**

```
┌────────────────────────────────────────────────────────────────────────┐
│                    AI 고객 문의 자동화 워크플로우                        │
├────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  1. Webhook 수신                                                        │
│     └─► { "name": "홍길동", "email": "...", "message": "결제 오류..." } │
│                                                                         │
│  2. OpenAI: 문의 분류                                                   │
│     └─► { "category": "payment", "urgency": "high", "sentiment": -0.8 }│
│                                                                         │
│  3. OpenAI: 답변 초안 생성                                              │
│     └─► "안녕하세요, 결제 오류에 대해 불편을 드려 죄송합니다..."        │
│                                                                         │
│  4. Switch: 카테고리별 분기                                             │
│     ├─► payment (결제)  → #payment-support 채널 + 담당자 멘션          │
│     ├─► technical (기술) → #tech-support 채널 + Jira 티켓 생성         │
│     ├─► general (일반)  → 이메일 자동 응답                              │
│     └─► urgent (긴급)   → PagerDuty 알림 + 전화 연락                   │
│                                                                         │
│  5. 결과 저장                                                           │
│     └─► Notion DB / Google Sheets에 기록                               │
│                                                                         │
└────────────────────────────────────────────────────────────────────────┘
```

**OpenAI 노드 설정 (분류):**

```json
{
  "model": "gpt-4o-mini",
  "messages": [
    {
      "role": "system",
      "content": "고객 문의를 분석하여 JSON으로 응답하세요.\n\n카테고리: payment, technical, general, account\n긴급도: high, medium, low\n감정점수: -1(매우 부정) ~ 1(매우 긍정)\n\n응답 형식:\n{\"category\": \"...\", \"urgency\": \"...\", \"sentiment\": 0.0, \"summary\": \"한 줄 요약\"}"
    },
    {
      "role": "user",
      "content": "{{ $json.message }}"
    }
  ],
  "response_format": { "type": "json_object" }
}
```

**OpenAI 노드 설정 (답변 생성):**

```json
{
  "model": "gpt-4o-mini",
  "messages": [
    {
      "role": "system",
      "content": "당신은 친절한 고객 지원 담당자입니다. 고객 문의에 대해 공감하며 전문적인 답변을 작성하세요. 150자 이내로 작성하세요."
    },
    {
      "role": "user",
      "content": "고객명: {{ $json.name }}\n문의 내용: {{ $json.message }}\n카테고리: {{ $('OpenAI_분류').item.json.category }}"
    }
  ]
}
```

**Code 노드 (결과 조합):**

```javascript
const webhook = $('Webhook').item.json;
const classification = JSON.parse($('OpenAI_분류').item.json.message.content);
const reply = $('OpenAI_답변').item.json.message.content;

return {
  json: {
    // 원본 데이터
    customer_name: webhook.name,
    customer_email: webhook.email,
    original_message: webhook.message,

    // AI 분석 결과
    category: classification.category,
    urgency: classification.urgency,
    sentiment: classification.sentiment,
    summary: classification.summary,

    // 생성된 답변
    draft_reply: reply,

    // 메타데이터
    processed_at: new Date().toISOString(),
    workflow_id: $workflow.id
  }
};
```

**Switch 노드 조건:**

| 출력 | 조건 |
|------|------|
| Payment | `{{ $json.category }}` equals `payment` |
| Technical | `{{ $json.category }}` equals `technical` |
| Urgent | `{{ $json.urgency }}` equals `high` |
| Default | 그 외 모든 경우 |

**Slack 알림 메시지 템플릿:**

```
🆕 *새 고객 문의* ({{ $json.urgency === 'high' ? '🔴 긴급' : '🟢 일반' }})

*고객*: {{ $json.customer_name }}
*카테고리*: {{ $json.category }}
*요약*: {{ $json.summary }}

> {{ $json.original_message }}

---
*AI 답변 초안:*
{{ $json.draft_reply }}

<{{ $json.customer_email }}|이메일 보내기>
```

## Docker로 시작하기

### 빠른 시작 (Linux/macOS)

```bash
# 단일 컨테이너 실행
docker run -it --rm \
  --name n8n \
  -p 5678:5678 \
  -v n8n_data:/home/node/.n8n \
  n8nio/n8n
```

### Windows에서 Docker로 실행하기

Windows 환경에서 Docker Desktop을 사용해 n8n을 셀프호스팅하는 방법입니다.

#### 1. 사전 준비

**Docker Desktop 설치**

1. [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/) 다운로드
2. 설치 후 WSL 2 백엔드 활성화 (권장)
3. Docker Desktop 실행 확인

```powershell
# PowerShell에서 Docker 설치 확인
docker --version
# Docker version 24.x.x, build xxxxx
```

#### 2. 빠른 시작 (PowerShell)

```powershell
# PowerShell에서 n8n 실행
docker run -it --rm `
  --name n8n `
  -p 5678:5678 `
  -v n8n_data:/home/node/.n8n `
  n8nio/n8n
```

> **참고**: PowerShell에서는 줄 연속 문자가 백틱(`)입니다.

브라우저에서 `http://localhost:5678` 접속

#### 3. 데이터 영구 저장 (Windows 경로)

```powershell
# Windows 로컬 폴더에 데이터 저장
docker run -d `
  --name n8n `
  --restart always `
  -p 5678:5678 `
  -v C:\Users\사용자명\.n8n:/home/node/.n8n `
  -e GENERIC_TIMEZONE=Asia/Seoul `
  -e TZ=Asia/Seoul `
  n8nio/n8n
```

#### 4. Docker Compose로 실행 (권장)

프로젝트 폴더에 `docker-compose.yml` 파일 생성:

```yaml
# docker-compose.yml (Windows용)
version: '3.8'

services:
  n8n:
    image: n8nio/n8n
    container_name: n8n
    restart: always
    ports:
      - "5678:5678"
    environment:
      # 기본 인증 설정
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=admin
      - N8N_BASIC_AUTH_PASSWORD=your_secure_password
      # 타임존 설정
      - GENERIC_TIMEZONE=Asia/Seoul
      - TZ=Asia/Seoul
      # 웹훅 URL (외부 접속 시)
      - WEBHOOK_URL=http://localhost:5678/
    volumes:
      # Windows 경로 예시
      - ./n8n-data:/home/node/.n8n
      # 또는 명명된 볼륨 사용
      # - n8n_data:/home/node/.n8n

volumes:
  n8n_data:
```

실행 명령:

```powershell
# docker-compose.yml 파일이 있는 폴더에서
docker-compose up -d

# 로그 확인
docker-compose logs -f n8n

# 중지
docker-compose down
```

#### 5. Windows 전용 트러블슈팅

| 문제 | 원인 | 해결 방법 |
|------|------|----------|
| `port already in use` | 5678 포트 사용 중 | `docker ps`로 확인 후 중지, 또는 포트 변경 `-p 5679:5678` |
| 볼륨 마운트 실패 | Docker Desktop 파일 공유 설정 | Settings → Resources → File Sharing에서 드라이브 추가 |
| 권한 오류 | WSL 권한 문제 | PowerShell 관리자 권한으로 실행 |
| 한글 깨짐 | 타임존/인코딩 | `GENERIC_TIMEZONE=Asia/Seoul` 환경변수 추가 |
| 컨테이너 재시작 반복 | 메모리 부족 | Docker Desktop → Settings → Resources에서 메모리 증가 |

#### 6. 외부 접속 설정 (ngrok / cloudflared)

로컬 n8n을 외부에서 접속 가능하게 하려면 터널링 도구를 사용합니다.

##### 방법 1: ngrok (빠른 테스트용)

```powershell
# ngrok 설치 (Chocolatey)
choco install ngrok

# 또는 직접 다운로드: https://ngrok.com/download

# n8n 포트 터널링
ngrok http 5678
```

ngrok에서 제공하는 URL을 `WEBHOOK_URL`에 설정:

```yaml
environment:
  - WEBHOOK_URL=https://xxxx-xxx-xxx.ngrok-free.app/
```

##### 방법 2: cloudflared (권장 - 무료, 고정 URL)

ngrok 무료 플랜의 제한(URL 변경, 대역폭 제한)을 피하려면 Cloudflare Tunnel 사용을 권장합니다.

> 상세 내용: [Cloudflared 문서](../devops/cloudflared.md)

**Quick Tunnel (임시 URL)**

```powershell
# cloudflared 설치 (winget)
winget install Cloudflare.cloudflared

# 또는 Chocolatey
choco install cloudflared

# n8n 포트 터널링
cloudflared tunnel --url http://localhost:5678
```

출력되는 `https://xxx-xxx.trycloudflare.com` URL을 `WEBHOOK_URL`에 설정

**Named Tunnel (고정 URL) - 프로덕션 권장**

```powershell
# 1. Cloudflare 로그인 (최초 1회)
cloudflared tunnel login

# 2. 터널 생성
cloudflared tunnel create n8n-tunnel

# 3. DNS 라우팅 설정 (Cloudflare에 등록된 도메인 필요)
cloudflared tunnel route dns n8n-tunnel n8n.yourdomain.com

# 4. 설정 파일 생성
```

`~/.cloudflared/config.yml` (또는 `C:\Users\사용자명\.cloudflared\config.yml`):

```yaml
tunnel: n8n-tunnel
credentials-file: C:\Users\사용자명\.cloudflared\<TUNNEL_ID>.json

ingress:
  - hostname: n8n.yourdomain.com
    service: http://localhost:5678
  - service: http_status:404
```

```powershell
# 5. 터널 실행
cloudflared tunnel run n8n-tunnel

# 6. 서비스로 등록 (Windows 시작 시 자동 실행)
cloudflared service install
```

**Docker Compose와 함께 사용**

```yaml
# docker-compose.yml
version: '3.8'

services:
  n8n:
    image: n8nio/n8n
    container_name: n8n
    restart: always
    ports:
      - "5678:5678"
    environment:
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=admin
      - N8N_BASIC_AUTH_PASSWORD=your_password
      - WEBHOOK_URL=https://n8n.yourdomain.com/
      - GENERIC_TIMEZONE=Asia/Seoul
    volumes:
      - n8n_data:/home/node/.n8n

  cloudflared:
    image: cloudflare/cloudflared:latest
    container_name: cloudflared
    restart: always
    command: tunnel --no-autoupdate run --token ${TUNNEL_TOKEN}
    depends_on:
      - n8n

volumes:
  n8n_data:
```

`.env` 파일:
```env
TUNNEL_TOKEN=your_tunnel_token_from_cloudflare_dashboard
```

> **토큰 얻기**: [Cloudflare Zero Trust](https://one.dash.cloudflare.com/) → Networks → Tunnels → Create → n8n 선택 → Docker 탭에서 토큰 복사

**ngrok vs cloudflared 비교**

| 항목 | ngrok (무료) | cloudflared |
|------|-------------|-------------|
| URL 고정 | ❌ 재시작 시 변경 | ✅ 고정 가능 |
| 커스텀 도메인 | 유료 | ✅ 무료 |
| 대역폭 | 제한 | 무제한 |
| 동시 터널 | 1개 | 무제한 |
| 설정 난이도 | 쉬움 | 중간 |
| 웹훅 안정성 | 낮음 (URL 변경) | ✅ 높음 |

#### 7. 프로덕션 설정 (PostgreSQL + Windows)

```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  n8n:
    image: n8nio/n8n
    container_name: n8n
    restart: always
    ports:
      - "5678:5678"
    environment:
      # 데이터베이스 설정
      - DB_TYPE=postgresdb
      - DB_POSTGRESDB_HOST=postgres
      - DB_POSTGRESDB_PORT=5432
      - DB_POSTGRESDB_DATABASE=n8n
      - DB_POSTGRESDB_USER=n8n
      - DB_POSTGRESDB_PASSWORD=${DB_PASSWORD:-n8n_secure_password}
      # 인증 설정
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=${N8N_USER:-admin}
      - N8N_BASIC_AUTH_PASSWORD=${N8N_PASSWORD:-change_this_password}
      # 실행 데이터 정리
      - EXECUTIONS_DATA_PRUNE=true
      - EXECUTIONS_DATA_MAX_AGE=168
      # 타임존
      - GENERIC_TIMEZONE=Asia/Seoul
      - TZ=Asia/Seoul
    volumes:
      - n8n_data:/home/node/.n8n
    depends_on:
      - postgres
    networks:
      - n8n-network

  postgres:
    image: postgres:15-alpine
    container_name: n8n-postgres
    restart: always
    environment:
      - POSTGRES_DB=n8n
      - POSTGRES_USER=n8n
      - POSTGRES_PASSWORD=${DB_PASSWORD:-n8n_secure_password}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - n8n-network
    # 헬스체크
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U n8n"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  n8n_data:
  postgres_data:

networks:
  n8n-network:
    driver: bridge
```

환경변수 파일 `.env` 생성:

```env
# .env
DB_PASSWORD=your_secure_db_password
N8N_USER=admin
N8N_PASSWORD=your_secure_n8n_password
```

실행:

```powershell
docker-compose -f docker-compose.prod.yml up -d
```

#### 8. Windows 시작 시 자동 실행

Docker Desktop 설정에서 "Start Docker Desktop when you log in" 활성화하면, `restart: always` 설정된 컨테이너가 자동으로 시작됩니다.

### docker-compose.yml

```yaml
version: '3.8'

services:
  n8n:
    image: n8nio/n8n
    restart: always
    ports:
      - "5678:5678"
    environment:
      - N8N_BASIC_AUTH_ACTIVE=true
      - N8N_BASIC_AUTH_USER=admin
      - N8N_BASIC_AUTH_PASSWORD=password
      - N8N_HOST=n8n.example.com
      - N8N_PROTOCOL=https
      - WEBHOOK_URL=https://n8n.example.com/
      - GENERIC_TIMEZONE=Asia/Seoul
    volumes:
      - n8n_data:/home/node/.n8n

volumes:
  n8n_data:
```

### 프로덕션 설정 (PostgreSQL)

```yaml
version: '3.8'

services:
  n8n:
    image: n8nio/n8n
    environment:
      - DB_TYPE=postgresdb
      - DB_POSTGRESDB_HOST=postgres
      - DB_POSTGRESDB_PORT=5432
      - DB_POSTGRESDB_DATABASE=n8n
      - DB_POSTGRESDB_USER=n8n
      - DB_POSTGRESDB_PASSWORD=n8n_password
      - EXECUTIONS_DATA_PRUNE=true
      - EXECUTIONS_DATA_MAX_AGE=168  # 7일
    depends_on:
      - postgres

  postgres:
    image: postgres:15
    environment:
      - POSTGRES_DB=n8n
      - POSTGRES_USER=n8n
      - POSTGRES_PASSWORD=n8n_password
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  n8n_data:
  postgres_data:
```

## 트레이드오프

### n8n vs 다른 자동화 도구

| 항목 | n8n | Zapier | Make | Apps Script |
|------|-----|--------|------|-------------|
| **비용** | 셀프호스팅 무료 | $20~/월 | $9~/월 | 무료 |
| **셀프호스팅** | 가능 | 불가 | 불가 | 불가 |
| **데이터 보안** | 내 서버 | 외부 | 외부 | Google |
| **노드/연동 수** | 400+ | 6000+ | 1500+ | Google 전용 |
| **복잡한 로직** | JavaScript | 제한적 | 제한적 | JavaScript |
| **학습 곡선** | 중간 | 낮음 | 낮음 | 중간 |
| **커뮤니티** | 오픈소스 | 상용 | 상용 | Google |

### 언제 n8n을 선택하는가?

```
┌─────────────────────────────────────────────────────────────┐
│                      선택 가이드                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  n8n 선택                          Zapier/Make 선택         │
│  ─────────                         ───────────────          │
│  ✓ 데이터 보안이 중요              ✓ 빠른 시작 필요         │
│  ✓ 비용 최소화 필요                ✓ 인프라 관리 불가       │
│  ✓ 복잡한 커스텀 로직              ✓ 간단한 자동화          │
│  ✓ 셀프호스팅 인프라 있음          ✓ SLA/지원 필요          │
│  ✓ 오픈소스 선호                   ✓ 비개발자 사용          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### n8n 제한사항

| 항목 | 제한 |
|------|------|
| 메모리 | 워크플로우당 ~256MB (기본) |
| 실행 시간 | 타임아웃 설정 가능 (기본 무제한) |
| 동시 실행 | 워커 수에 따라 제한 |
| 웹훅 | 인스턴스당 제한 없음 |

## 실무 활용 패턴

### 자주 사용되는 시나리오

```
┌────────────────────────────────────────────────────────────────┐
│                   실무 자동화 시나리오                          │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 리드 관리                                                  │
│     Form 제출 → CRM 저장 → 영업팀 Slack 알림 → 이메일 발송    │
│                                                                 │
│  2. 인시던트 대응                                              │
│     모니터링 웹훅 → 심각도 분류 → PagerDuty/Slack 알림        │
│                                                                 │
│  3. 콘텐츠 배포                                                │
│     RSS 피드 → 요약 생성 (AI) → 소셜 미디어 자동 포스팅       │
│                                                                 │
│  4. 데이터 동기화                                              │
│     DB 변경 감지 → 데이터 변환 → 외부 시스템 동기화           │
│                                                                 │
│  5. 보고서 자동화                                              │
│     스케줄 트리거 → 여러 API 데이터 수집 → 리포트 생성        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 면접 예상 질문

### Q: Zapier 대신 n8n을 선택한 이유는?

A: 세 가지 핵심 이유가 있습니다.

1. **데이터 보안**: 고객 데이터가 외부 SaaS로 나가면 안 되는 규정 준수 요구사항. n8n 셀프호스팅으로 데이터가 사내 서버에만 존재
2. **비용 효율**: Zapier Task 기반 과금은 규모가 커지면 월 수백만원. n8n은 EC2 비용만 발생
3. **복잡한 로직**: JavaScript Code 노드로 Zapier에서 불가능한 복잡한 데이터 변환 구현

**트레이드오프**: 인프라 관리 부담이 생겼지만, Docker + 모니터링으로 운영 안정화

### Q: n8n 워크플로우 에러 처리 전략은?

A: 세 가지 레벨로 처리합니다.

1. **노드 레벨**: 각 노드에 "Continue On Fail" 설정, 에러 발생해도 다음 노드 실행
2. **워크플로우 레벨**: Error Trigger 노드로 에러 발생 시 별도 흐름 실행 (알림 발송)
3. **시스템 레벨**: 실행 로그 모니터링, 실패율 임계치 초과 시 경고

```
// Error Trigger 활용 예시
Workflow Error → Error Trigger → Slack 알림 + DB 로깅
```

### Q: n8n 성능 최적화 방법은?

A: 주요 최적화 포인트입니다.

1. **배치 처리**: Split In Batches 노드로 대량 데이터 청크 단위 처리
2. **병렬 실행**: 독립적인 노드는 병렬로 실행되도록 워크플로우 설계
3. **데이터 필터링**: 불필요한 데이터 조기 필터링으로 메모리 사용 감소
4. **Queue 모드**: 대규모 처리 시 Redis + Worker 구조로 확장

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Google Apps Script](./google-apps-script.md) | 다른 자동화 도구 비교 | 입문 |
| CI/CD 자동화 (TODO) | n8n + GitHub Actions 연동 | 중급 |
| 웹훅 | 트리거 이해 | 입문 |

## 참고 자료

- [n8n 공식 문서](https://docs.n8n.io/)
- [n8n 커뮤니티 노드](https://n8n.io/integrations)
- [n8n GitHub](https://github.com/n8n-io/n8n)
- [n8n 워크플로우 템플릿](https://n8n.io/workflows)
