# MCP (Model Context Protocol)

> `[2] 입문` · 선수 지식: AI Agent란

> `Trend` 2024-2025

> MCP는 **AI 모델(LLM)과 외부 시스템을 연결하는 표준 프로토콜**이다.

`#MCP` `#ModelContextProtocol` `#모델컨텍스트프로토콜` `#Anthropic` `#Claude` `#ClaudeCode` `#도구확장` `#ToolUse` `#FunctionCalling` `#AI프로토콜` `#표준프로토콜` `#MCPServer` `#MCPClient` `#Resources` `#Tools` `#Prompts` `#JSONRPC` `#stdio` `#SSE` `#AI통합` `#외부시스템연동` `#플러그인` `#확장성` `#오픈소스` `#MxN문제` `#SDK`

## 왜 알아야 하는가?

- **실무**: AI 에이전트가 GitHub, Slack, DB 등 외부 시스템과 상호작용해야 할 때 MCP로 구현한다. 2025년 현재 16,000+ MCP 서버가 존재하며, 직접 서버를 개발하거나 활용할 수 있다.
- **면접**: "AI 에이전트가 외부 도구를 어떻게 사용하나요?"에 대한 답이다. M x N 문제와 해결 방식을 설명할 수 있어야 한다.
- **기반 지식**: AI 에이전트 확장의 핵심 프로토콜이다. Skill, Hook 등 모든 Claude Code 확장 기능의 기반이 되며, 2025년 사실상 업계 표준으로 자리잡았다.

## 핵심 개념

- **MCP**: AI 모델과 외부 시스템을 연결하는 **표준 프로토콜** (Anthropic 2024년 11월 공개)
- **M x N 문제 해결**: M개 AI 모델 × N개 도구를 각각 연결하는 대신, 하나의 표준으로 통합
- **Host/Client/Server**: 3계층 아키텍처로 AI 애플리케이션과 도구를 분리
- **Tools/Resources/Prompts**: MCP 서버가 제공하는 3가지 핵심 기능

## 쉽게 이해하기

**MCP**를 **USB-C 포트**에 비유할 수 있습니다.

스마트폰 충전기를 생각해보세요:

**USB-C 이전**
- 삼성 폰 → 삼성 전용 충전기
- 아이폰 → 라이트닝 케이블
- LG 폰 → LG 전용 충전기
- 새 폰을 사면 충전기도 새로 사야 함

**USB-C 이후**
- 모든 폰 → USB-C 하나로 충전
- 충전기 하나로 모든 기기 사용 가능

AI 세계에서도 마찬가지입니다:

**MCP 이전**
- Claude + GitHub = 전용 연동 코드
- ChatGPT + GitHub = 또 다른 연동 코드
- 도구가 추가되면? 모든 AI에 대해 새 코드 작성

**MCP 이후**
- GitHub MCP 서버 1개 만들면
- Claude, ChatGPT, Gemini 모두 사용 가능
- **"AI를 위한 USB-C 포트"**

## 상세 설명

### M x N 문제

**왜 이 문제가 중요한가?**

기존 방식은 **통합 비용이 기하급수적으로 증가**합니다.

```
기존 방식: M x N 통합 필요
┌─────────┐     ┌─────────┐
│ Claude  │────▶│ GitHub  │  각각 별도 구현
└─────────┘     └─────────┘
      │              ▲
      │              │
      ▼              │
┌─────────┐     ┌────┴────┐
│ ChatGPT │────▶│ GitHub  │  또 별도 구현
└─────────┘     │ (다른   │
      │         │ 구현)   │
      ▼         └─────────┘
┌─────────┐
│ Gemini  │────▶ ... (계속 증가)
└─────────┘

3개 AI × 5개 도구 = 15개 통합 코드 필요
```

```
MCP 방식: M + N 통합만 필요
┌─────────┐
│ Claude  │───┐
└─────────┘   │
┌─────────┐   │    ┌─────────┐    ┌─────────┐
│ ChatGPT │───┼───▶│   MCP   │───▶│ GitHub  │
└─────────┘   │    │ 프로토콜│    │ Slack   │
┌─────────┐   │    └─────────┘    │ DB ...  │
│ Gemini  │───┘                   └─────────┘
└─────────┘

3개 AI 클라이언트 + 5개 MCP 서버 = 8개만 구현
```

**실제 효과**: 새 AI 모델이 추가되면 1개 클라이언트만 구현, 새 도구가 추가되면 1개 서버만 구현.

### 아키텍처

```
┌──────────────────────────────────────────────────────────────┐
│                        MCP Host                               │
│  (Claude Desktop, IDE, AI Application)                        │
│                                                               │
│  ┌─────────────────────────────────────────────────────┐     │
│  │                   MCP Client                         │     │
│  │  - 서버와의 연결 관리                                 │     │
│  │  - 프로토콜 메시지 송수신                             │     │
│  └─────────────────────────────────────────────────────┘     │
└──────────────────────────────────────────────────────────────┘
                            │
                            │ JSON-RPC 2.0
                            │ (stdio / HTTP+SSE)
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                        MCP Server                             │
│                                                               │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │    Tools    │  │  Resources  │  │   Prompts   │          │
│  │  (함수 호출)│  │ (데이터 제공)│  │  (템플릿)   │          │
│  └─────────────┘  └─────────────┘  └─────────────┘          │
└──────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                    External Systems                           │
│  (GitHub, Slack, Database, File System, API, etc.)           │
└──────────────────────────────────────────────────────────────┘
```

### 핵심 구성 요소

| 구성 요소 | 역할 | 예시 |
|----------|------|------|
| **Host** | MCP 클라이언트를 실행하는 애플리케이션 | Claude Desktop, VS Code |
| **Client** | 서버와 1:1 연결 유지, 프로토콜 통신 | Host 내부에 포함 |
| **Server** | 특정 기능을 제공하는 경량 프로그램 | GitHub MCP Server |
| **Tools** | LLM이 호출할 수 있는 **함수** | `create_issue()` |
| **Resources** | LLM에 제공되는 **읽기 전용 데이터** | 파일 내용, DB 스키마 |
| **Prompts** | 재사용 가능한 **프롬프트 템플릿** | 코드 리뷰 템플릿 |

**왜 Host/Client/Server로 분리하나?**

- **보안**: Server가 별도 프로세스로 실행되어 격리됨
- **재사용**: 한 번 만든 Server를 여러 Host에서 사용 가능
- **확장성**: 필요한 Server만 연결하여 리소스 절약

### 동작 원리

#### 연결 수립

```
Host (Claude Desktop)              MCP Server (GitHub)
        │                                  │
        │──── initialize ─────────────────▶│
        │     {protocolVersion, ...}       │
        │                                  │
        │◀─── initialize result ───────────│
        │     {capabilities, ...}          │
        │                                  │
        │──── initialized ────────────────▶│
        │                                  │
        │◀─── tools/list ─────────────────│
        │     (사용 가능한 도구 목록)       │
```

#### 도구 호출

```
User: "내 GitHub 레포지토리 목록 보여줘"
        │
        ▼
┌───────────────┐
│  LLM (Claude) │ ── 사용 가능한 도구 분석
└───────────────┘
        │
        │ tools/call: list_repositories
        ▼
┌───────────────┐
│  MCP Client   │
└───────────────┘
        │
        │ JSON-RPC 요청
        ▼
┌───────────────┐
│  MCP Server   │ ── GitHub API 호출
│   (GitHub)    │
└───────────────┘
        │
        │ 결과 반환
        ▼
┌───────────────┐
│  LLM (Claude) │ ── 결과를 자연어로 변환
└───────────────┘
        │
        ▼
User: "다음은 귀하의 GitHub 레포지토리 목록입니다: ..."
```

## 기존 기술과의 비교

### MCP vs 단순 API (REST API)

"그냥 REST API 호출하면 되는 거 아닌가?"라는 질문이 자연스럽습니다.

#### 핵심 차이: 도구 선택의 주체

**MCP의 가장 중요한 차이점은 "어떤 도구를 쓸지"의 결정권이 개발자 → LLM으로 이동하는 것입니다.**

```
단순 API: 개발자가 도구를 결정
┌──────────┐     ┌──────────┐     ┌──────────┐
│  사용자   │────▶│   LLM    │────▶│ 개발자가  │
│"이슈 만들어"│     │ 파라미터  │     │ 미리 연결한│
└──────────┘     │ 생성만   │     │ GitHub API│
                 └──────────┘     └──────────┘
                      │
              개발자가 프롬프트에
              "GitHub API 있어"라고
              미리 알려줘야 함
```

```
MCP: LLM이 도구를 선택
┌──────────┐     ┌──────────┐     ┌──────────┐
│  사용자   │────▶│   LLM    │────▶│ MCP 서버  │
│"이슈 만들어"│     │          │     │ (GitHub)  │
└──────────┘     │ 1. tools/list로│     └──────────┘
                 │    뭐가 있나 확인│
                 │ 2. create_issue│
                 │    선택        │
                 │ 3. 호출        │
                 └──────────┘
```

| 구분 | 단순 API | MCP |
|------|----------|-----|
| **도구 선택** | 개발자가 코드로 결정 | LLM이 상황 보고 결정 |
| **새 도구 추가** | 코드 + 프롬프트 수정 | MCP 서버만 업데이트 |
| **도구 발견** | 수동 (문서 읽고 구현) | 자동 (tools/list) |

#### 구체적인 흐름 비교

**단순 API 방식:**
1. 개발자가 GitHub API 문서를 읽는다
2. 필요한 엔드포인트를 선택한다
3. LLM 프롬프트에 "이런 API가 있어"라고 설명한다
4. LLM이 파라미터를 생성하면 개발자 코드가 API 호출
5. 새 API 추가 시 → 1~4 반복

**MCP 방식:**
1. MCP 서버 연결
2. LLM이 tools/list로 사용 가능한 도구 자동 조회
3. LLM이 도구 목록을 보고 스스로 선택
4. tools/call로 자동 호출
5. 새 도구 추가 시 → **서버만 업데이트하면 LLM이 자동 인식**

#### 예시: "버그 리포트해줘"

**API 방식**: 개발자가 미리 "버그 리포트 = GitHub create_issue"라고 코딩해둬야 함

```python
# 개발자가 모든 것을 하드코딩
def handle_bug_report(title, body):
    # 개발자가 "버그 리포트 → GitHub"라고 결정
    return call_github_api(title, body)

# LLM 프롬프트에 직접 설명
prompt = """
사용 가능한 함수:
- handle_bug_report(title, body): 버그 리포트 생성
"""
```

**MCP 방식**: LLM이 tools/list를 보고 "GitHub의 create_issue가 적합하겠다"고 **스스로 판단**

```json
// MCP 서버가 tools/list 응답
{
  "tools": [
    {"name": "create_issue", "description": "GitHub 이슈를 생성합니다"},
    {"name": "send_slack", "description": "Slack 메시지를 보냅니다"},
    {"name": "create_jira", "description": "Jira 티켓을 생성합니다"}
  ]
}
// LLM: "버그 리포트니까 create_issue가 적합하겠다" → 자동 선택
```

#### 비교표

| 항목 | MCP | 단순 API 호출 |
|------|-----|--------------|
| **도구 선택 주체** | LLM이 자동 선택 | 개발자가 미리 결정 |
| **도구 발견** | 자동 (tools/list) | 수동 (문서 읽고 구현) |
| **스키마 표준** | JSON Schema 통일 | API마다 다름 |
| **LLM 통합** | 프로토콜 레벨 지원 | 직접 프롬프트에 설명 필요 |
| **새 도구 추가** | 서버만 업데이트 | 코드 + 프롬프트 수정 |
| **인증 관리** | MCP 서버가 처리 | 매 API마다 별도 구현 |

#### 언제 뭘 쓰나?

| 상황 | 권장 방식 | 이유 |
|------|----------|------|
| LLM 없이 백엔드 간 통신 | REST API | MCP는 LLM 연동 목적 |
| 고정된 워크플로우 | REST API | 도구 선택이 항상 동일 |
| LLM이 동적으로 도구 선택 | MCP | 핵심 사용 사례 |
| 여러 AI 모델에서 같은 도구 사용 | MCP | 한 번 구현으로 재사용 |
| 도구가 자주 추가/변경 | MCP | 서버만 업데이트하면 됨 |

### MCP vs OpenAI Function Calling

| 항목 | MCP | OpenAI Function Calling |
|------|-----|------------------------|
| **표준화** | 오픈 프로토콜 (벤더 중립) | OpenAI 전용 |
| **서버 분리** | 별도 서버로 분리 가능 | 애플리케이션 내장 |
| **재사용성** | 한 번 구축 → 모든 LLM 사용 | OpenAI 모델에서만 사용 |
| **생태계** | 16,000+ MCP 서버 | 제한적 |

### MCP vs LangChain Tools

| 항목 | MCP | LangChain Tools |
|------|-----|-----------------|
| **아키텍처** | 클라이언트-서버 분리 | 라이브러리 통합 |
| **언어 지원** | Python, TS, Java 등 | 주로 Python |
| **프로세스** | 별도 프로세스 실행 | 동일 프로세스 |
| **보안** | 프로세스 격리로 보안 강화 | 동일 프로세스 내 실행 |

**왜 MCP가 선호되는가?**

- 벤더 중립적이라 AI 모델 교체 시 재작업 불필요
- 프로세스 격리로 보안 사고 시 영향 범위 제한
- 2025년 OpenAI, Google, Microsoft가 모두 채택하며 사실상 표준화

## 예제 코드

### Claude Desktop 설정

```json
// claude_desktop_config.json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "<your-token>"
      }
    }
  }
}
```

### Python MCP Server 구현

```python
from mcp.server import Server
from mcp.types import Tool, TextContent

server = Server("weather-server")

@server.list_tools()
async def list_tools():
    """서버가 제공하는 도구 목록 반환"""
    return [
        Tool(
            name="get_weather",
            description="지정된 도시의 날씨 정보를 가져옵니다",
            inputSchema={
                "type": "object",
                "properties": {
                    "city": {"type": "string", "description": "도시 이름"}
                },
                "required": ["city"]
            }
        )
    ]

@server.call_tool()
async def call_tool(name: str, arguments: dict):
    """도구 호출 처리"""
    if name == "get_weather":
        city = arguments["city"]
        weather = await fetch_weather_api(city)  # 실제 API 호출
        return [TextContent(type="text", text=f"{city}의 날씨: {weather}")]
```

### TypeScript MCP Server 구현

```typescript
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";

const server = new Server({
  name: "search-server",
  version: "1.0.0"
}, {
  capabilities: { tools: {} }
});

// 도구 목록 제공
server.setRequestHandler("tools/list", async () => ({
  tools: [{
    name: "search_database",
    description: "데이터베이스에서 검색합니다",
    inputSchema: {
      type: "object",
      properties: {
        query: { type: "string" }
      },
      required: ["query"]
    }
  }]
}));

// 도구 호출 처리
server.setRequestHandler("tools/call", async (request) => {
  const { name, arguments: args } = request.params;
  if (name === "search_database") {
    const results = await searchDB(args.query);
    return { content: [{ type: "text", text: JSON.stringify(results) }] };
  }
});

// 서버 시작
const transport = new StdioServerTransport();
await server.connect(transport);
```

## 보안 고려사항

### 주요 보안 위협

| 위협 | 설명 | 대응 방안 |
|------|------|----------|
| **Prompt Injection** | 악의적 입력으로 도구 오용 유도 | 입력 검증, 샌드박싱 |
| **Tool Poisoning** | 유사 이름 악성 도구로 대체 | 도구 서명 검증, 신뢰 소스만 사용 |
| **Over-permissioning** | 과도한 권한 부여 | 최소 권한 원칙, 읽기 전용 모드 |
| **Data Exfiltration** | 도구 조합으로 데이터 유출 | 도구 조합 모니터링, 네트워크 제한 |

**왜 MCP 보안이 중요한가?**

MCP 서버는 파일시스템, DB, 외부 API에 접근합니다. 보안 취약점이 있으면 민감한 데이터 유출이나 시스템 손상으로 이어질 수 있습니다.

### 권장 보안 설정

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "/safe/path"],
      "env": {
        "MCP_READ_ONLY": "true"  // 읽기 전용 모드
      }
    }
  }
}
```

## 생태계 현황 (2025년 12월)

| 지표 | 수치 |
|------|------|
| SDK 월간 다운로드 | 9,700만+ (Python, TypeScript) |
| 등록된 MCP 서버 | 16,000+ |
| 주요 채택 기업 | Anthropic, OpenAI, Google, Microsoft, AWS |
| 거버넌스 | Linux Foundation (Agentic AI Foundation) |

### 향후 전망

1. **Agent-to-Agent (A2A)**: AI 에이전트 간 직접 통신 표준화
2. **엔터프라이즈 보안**: OAuth 2.0 기반 인증, 감사 로깅
3. **멀티모달**: 이미지, 오디오 등 다양한 데이터 타입 처리
4. **실시간 스트리밍**: 대용량 데이터 실시간 처리

## 면접 예상 질문

### Q1: MCP가 해결하는 M x N 문제란?

A: M개의 AI 모델과 N개의 도구를 연결할 때 발생하는 **통합 복잡도 문제**입니다.

**왜냐하면** 기존 방식은 각 AI 모델마다 각 도구에 대한 별도 통합 코드가 필요합니다. 3개 AI × 5개 도구 = 15개 구현이 필요하죠.

MCP는 표준 프로토콜로 이를 해결합니다. 각 도구는 MCP 서버로 한 번만, 각 AI는 MCP 클라이언트로 한 번만 구현하면 됩니다. 결과적으로 3 + 5 = 8개만 구현하면 되고, 새 도구/AI 추가 시 1개만 추가하면 됩니다.

### Q2: MCP의 Host, Client, Server의 역할을 설명해주세요.

A: 3계층 아키텍처입니다.

- **Host**: MCP 클라이언트를 실행하는 **상위 애플리케이션**입니다. Claude Desktop, VS Code 등이 해당됩니다.
- **Client**: Host 내부에서 MCP 서버와 **1:1 연결을 유지**하는 프로토콜 구현체입니다. JSON-RPC로 통신합니다.
- **Server**: 특정 기능을 제공하는 **경량 프로그램**입니다. GitHub, Slack 등 각 서비스별로 존재합니다.

**왜 분리하나?** 보안(프로세스 격리), 재사용(한 Server를 여러 Host에서), 확장성(필요한 Server만 연결) 때문입니다.

### Q3: Tools와 Resources의 차이는?

A: **Tools는 액션**, **Resources는 데이터**입니다.

- **Tools**: LLM이 호출하는 **함수**입니다. `create_issue()`, `send_message()` 같은 부수효과가 있는 작업입니다.
- **Resources**: LLM에 **컨텍스트로 제공되는 읽기 전용 데이터**입니다. 파일 내용, DB 스키마 등입니다.

**왜 구분하나?** Tools는 외부 시스템을 변경할 수 있어 권한 관리가 필요하고, Resources는 안전하게 읽기만 가능하여 자유롭게 접근 가능합니다.

### Q4: MCP 서버의 보안 위협과 대응 방안은?

A: 주요 위협 4가지입니다:

1. **Prompt Injection**: 악의적 입력으로 도구 오용 → 입력 검증, 사용자 확인 요구
2. **Tool Poisoning**: 유사 이름 악성 도구 → 신뢰 소스만 사용, 서명 검증
3. **Over-permissioning**: 과도한 권한 → 최소 권한 원칙, 읽기 전용 모드
4. **Data Exfiltration**: 도구 조합 데이터 유출 → 모니터링, 네트워크 제한

**왜 중요한가?** MCP 서버는 파일시스템, DB, 외부 API에 접근하므로, 취약점이 있으면 민감 데이터 유출이나 시스템 손상으로 이어집니다.

### Q5: MCP와 단순 REST API 호출의 차이는?

A: 핵심 차이는 **"어떤 도구를 쓸지"의 결정권이 개발자 → LLM으로 이동**하는 것입니다.

- **REST API**: 개발자가 API 문서를 읽고, 어떤 API를 쓸지 **미리 결정**하고, LLM 프롬프트에 직접 설명해야 합니다.
- **MCP**: LLM이 `tools/list`로 사용 가능한 도구를 **자동 발견**하고, 상황에 맞게 **스스로 선택**합니다.

**왜냐하면** AI 에이전트는 동적으로 도구를 선택해야 하기 때문입니다.

예를 들어 "버그 리포트해줘"라고 하면:
- **API 방식**: 개발자가 미리 "버그 리포트 = GitHub API"라고 코딩해둬야 함
- **MCP 방식**: LLM이 tools/list를 보고 "create_issue가 적합하겠다"고 **스스로 판단**

새 도구가 추가되면 MCP 서버만 업데이트하면 LLM이 자동으로 인식합니다. 단, LLM 없이 백엔드 간 통신이나 고정된 워크플로우에서는 REST API가 더 적합합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| AI Agent란 (TODO) | AI 에이전트 기본 개념 | [1] 정의 |
| [Claude Code Skill](./claude-code-skill.md) | MCP 기반 기능 확장 | [3] 중급 |
| [Claude Code Hook](./claude-code-hook.md) | MCP 기반 자동화 | [3] 중급 |
| [Claude Code Slash Command](./claude-code-slash-command.md) | MCP 기반 명령어 | [3] 중급 |

## 참고 자료

- [Anthropic - Introducing the Model Context Protocol](https://www.anthropic.com/news/model-context-protocol)
- [MCP GitHub Repository](https://github.com/modelcontextprotocol)
- [MCP Specification](https://spec.modelcontextprotocol.io)
- [Wikipedia - Model Context Protocol](https://en.wikipedia.org/wiki/Model_Context_Protocol)
