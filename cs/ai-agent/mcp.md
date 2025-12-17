# MCP (Model Context Protocol)

> `Trend` 2024-2025

> AI 모델(LLM)과 외부 시스템을 연결하는 표준 프로토콜

## 핵심 개념

- **MCP**는 AI 모델(LLM)과 외부 시스템(도구, 데이터베이스, API 등)을 연결하는 **표준 프로토콜**
- Anthropic이 2024년 11월 오픈소스로 공개, "AI를 위한 USB-C 포트"로 비유됨
- **M x N 문제 해결**: M개의 AI 모델과 N개의 도구를 각각 연결하는 대신, 하나의 표준으로 통합
- 2025년 OpenAI, Google, Microsoft 등 주요 기업이 채택하며 사실상 업계 표준으로 자리잡음
- 2025년 12월 Linux Foundation 산하 Agentic AI Foundation에 기부됨

## 등장 배경

### 왜 MCP가 필요한가?

기존 AI 통합 방식의 문제점:

```
기존 방식 (M x N 문제)
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Claude  │────▶│ GitHub  │     │ Slack   │
└─────────┘     └─────────┘     └─────────┘
      │              ▲               ▲
      │              │               │
      ▼              │               │
┌─────────┐     ┌────┴────┐     ┌───┴─────┐
│ ChatGPT │────▶│ GitHub  │     │ Slack   │
└─────────┘     │ (다른   │     │ (다른   │
      │         │ 구현)   │     │ 구현)   │
      ▼         └─────────┘     └─────────┘
┌─────────┐
│ Gemini  │────▶ ... (각각 별도 구현 필요)
└─────────┘
```

```
MCP 방식 (표준화)
┌─────────┐
│ Claude  │───┐
└─────────┘   │
┌─────────┐   │    ┌─────────┐    ┌─────────┐
│ ChatGPT │───┼───▶│   MCP   │───▶│ GitHub  │
└─────────┘   │    │ Server  │    │ Slack   │
┌─────────┐   │    └─────────┘    │ DB      │
│ Gemini  │───┘                   │ ...     │
└─────────┘                       └─────────┘
```

- 각 AI 모델마다 도구별 커스텀 통합 코드 작성 필요 (비효율적)
- 도구가 추가될 때마다 모든 AI 모델에 대해 새 통합 코드 필요
- 보안, 인증 방식이 통일되지 않아 관리 어려움

## 아키텍처

### 핵심 구성 요소

```
┌──────────────────────────────────────────────────────────┐
│                      MCP Host                            │
│  (Claude Desktop, IDE, AI Application)                   │
│                                                          │
│  ┌─────────────────────────────────────────────────┐    │
│  │                  MCP Client                      │    │
│  │  - 서버와의 연결 관리                             │    │
│  │  - 프로토콜 메시지 송수신                         │    │
│  └─────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────┘
                           │
                           │ JSON-RPC 2.0
                           │ (stdio / HTTP+SSE)
                           ▼
┌──────────────────────────────────────────────────────────┐
│                      MCP Server                          │
│                                                          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐       │
│  │   Tools     │ │  Resources  │ │   Prompts   │       │
│  │ (함수 호출) │ │ (데이터 제공)│ │ (템플릿)    │       │
│  └─────────────┘ └─────────────┘ └─────────────┘       │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                   External Systems                       │
│  (GitHub, Slack, Database, File System, API, etc.)       │
└──────────────────────────────────────────────────────────┘
```

### 주요 개념

| 개념 | 설명 | 예시 |
|------|------|------|
| **Host** | MCP 클라이언트를 실행하는 애플리케이션 | Claude Desktop, VS Code, IDE |
| **Client** | 서버와 1:1 연결을 유지하는 프로토콜 클라이언트 | Host 내부에 포함 |
| **Server** | 특정 기능을 제공하는 경량 프로그램 | GitHub MCP Server, Slack MCP Server |
| **Tools** | LLM이 호출할 수 있는 함수 | `create_issue()`, `send_message()` |
| **Resources** | LLM에 컨텍스트로 제공되는 데이터 | 파일 내용, DB 스키마 |
| **Prompts** | 재사용 가능한 프롬프트 템플릿 | 코드 리뷰 템플릿 |

## 동작 원리

### 연결 수립 과정

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
        │     (사용 가능한 도구 목록)        │
        │                                  │
```

### 도구 호출 과정

```
User: "내 GitHub 레포지토리 목록 보여줘"
        │
        ▼
┌───────────────┐
│   LLM (Claude)│ ── 사용 가능한 도구 분석
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
│   LLM (Claude)│ ── 결과를 자연어로 변환
└───────────────┘
        │
        ▼
User: "다음은 귀하의 GitHub 레포지토리 목록입니다: ..."
```

## 기존 기술과의 비교

### MCP vs OpenAI Function Calling

| 항목 | MCP | OpenAI Function Calling |
|------|-----|------------------------|
| **표준화** | 오픈 프로토콜 (벤더 중립) | OpenAI 전용 |
| **서버 분리** | 별도 서버로 분리 가능 | 애플리케이션 내장 |
| **재사용성** | 한 번 구축하면 모든 LLM에서 사용 | OpenAI 모델에서만 사용 |
| **생태계** | 16,000+ MCP 서버 존재 | 제한적 |

### MCP vs LangChain Tools

| 항목 | MCP | LangChain Tools |
|------|-----|-----------------|
| **아키텍처** | 클라이언트-서버 분리 | 라이브러리 통합 |
| **언어 지원** | 다중 언어 (Python, TS, Java 등) | 주로 Python |
| **프로세스** | 별도 프로세스로 실행 | 동일 프로세스 |
| **보안** | 프로세스 격리로 보안 강화 | 동일 프로세스 내 실행 |

## 실제 활용 사례

### 1. Claude Desktop + GitHub MCP Server

```json
// Claude Desktop 설정 (claude_desktop_config.json)
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

### 2. 커스텀 MCP Server 구현 (Python)

```python
from mcp.server import Server
from mcp.types import Tool, TextContent

server = Server("my-custom-server")

@server.list_tools()
async def list_tools():
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
    if name == "get_weather":
        city = arguments["city"]
        # 실제 날씨 API 호출 로직
        weather = fetch_weather(city)
        return [TextContent(type="text", text=f"{city}의 날씨: {weather}")]
```

### 3. TypeScript MCP Server 구현

```typescript
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";

const server = new Server({
  name: "my-mcp-server",
  version: "1.0.0"
}, {
  capabilities: { tools: {} }
});

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

server.setRequestHandler("tools/call", async (request) => {
  const { name, arguments: args } = request.params;
  if (name === "search_database") {
    const results = await searchDB(args.query);
    return { content: [{ type: "text", text: JSON.stringify(results) }] };
  }
});

const transport = new StdioServerTransport();
await server.connect(transport);
```

## 보안 고려사항

### 알려진 보안 이슈 (2025년 기준)

| 위협 | 설명 | 대응 방안 |
|------|------|----------|
| **Prompt Injection** | 악의적 입력으로 도구 오용 유도 | 입력 검증, 샌드박싱 |
| **Tool Poisoning** | 유사 이름 도구로 신뢰 도구 대체 | 도구 서명 검증 |
| **Over-permissioning** | 과도한 권한 부여 | 최소 권한 원칙 적용 |
| **Data Exfiltration** | 도구 조합으로 데이터 유출 | 도구 조합 제한, 모니터링 |

### 권장 보안 설정

```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "/safe/path"],
      "env": {
        "MCP_READ_ONLY": "true"
      }
    }
  }
}
```

## 향후 전망

### 2025년 이후 예상 발전 방향

1. **Agent-to-Agent (A2A) 프로토콜**: AI 에이전트 간 직접 통신 표준화
2. **엔터프라이즈 보안 강화**: OAuth 2.0 기반 인증, 감사 로깅
3. **멀티모달 지원**: 이미지, 오디오 등 다양한 데이터 타입 처리
4. **실시간 스트리밍**: 대용량 데이터 실시간 처리

### 생태계 현황 (2025년 12월 기준)

- **SDK 다운로드**: 월 9,700만+ (Python, TypeScript)
- **MCP 서버 수**: 16,000+ 등록
- **주요 채택 기업**: Anthropic, OpenAI, Google, Microsoft, AWS

## 면접 예상 질문

### Q1. MCP가 해결하는 M x N 문제란 무엇인가요?

**모범 답안:**

M x N 문제는 M개의 AI 모델과 N개의 외부 도구/서비스를 연결할 때 발생하는 통합 복잡도 문제입니다.

기존 방식에서는 각 AI 모델마다 각 도구에 대한 별도의 통합 코드가 필요했습니다. 예를 들어 3개의 AI 모델(Claude, ChatGPT, Gemini)과 5개의 도구(GitHub, Slack, DB, 파일시스템, 이메일)를 연결하려면 3 x 5 = 15개의 커스텀 통합이 필요합니다.

MCP는 이 문제를 표준 프로토콜로 해결합니다. 각 도구는 MCP 서버로 한 번만 구현하면 되고, 각 AI 모델은 MCP 클라이언트만 구현하면 모든 도구를 사용할 수 있습니다. 결과적으로 M + N개의 구현만 필요하며, 새로운 도구나 모델 추가 시 단 1개의 구현만 추가하면 됩니다.

### Q2. MCP의 핵심 구성 요소(Host, Client, Server, Tools, Resources)를 설명해주세요.

**모범 답안:**

**Host**는 MCP 클라이언트를 실행하는 상위 애플리케이션입니다. Claude Desktop, VS Code 같은 IDE, 또는 커스텀 AI 애플리케이션이 해당됩니다. Host는 사용자와의 상호작용을 담당하며, 여러 MCP 서버와의 연결을 관리합니다.

**Client**는 Host 내부에서 MCP 서버와 1:1 연결을 유지하는 프로토콜 구현체입니다. JSON-RPC 2.0 기반으로 서버와 통신하며, 도구 호출, 리소스 접근 등의 요청을 처리합니다.

**Server**는 특정 기능이나 데이터 소스에 대한 접근을 제공하는 경량 프로그램입니다. GitHub MCP Server, Slack MCP Server 등이 예시입니다. 각 서버는 독립적으로 실행되어 보안과 격리를 보장합니다.

**Tools**는 LLM이 호출할 수 있는 함수로, 외부 시스템과의 상호작용을 수행합니다. `create_issue()`, `send_message()` 같은 액션성 기능이 해당됩니다.

**Resources**는 LLM에 컨텍스트로 제공되는 읽기 전용 데이터입니다. 파일 내용, 데이터베이스 스키마, 설정 정보 등이 해당됩니다.

### Q3. MCP 서버의 보안 위협과 대응 방안은 무엇인가요?

**모범 답안:**

주요 보안 위협은 다음과 같습니다:

**Prompt Injection**: 악의적인 사용자 입력이 LLM을 통해 도구 호출에 영향을 미치는 공격입니다. 대응 방안으로는 사용자 입력과 시스템 명령을 명확히 분리하고, 민감한 작업에 대해 사용자 확인을 요구합니다.

**Tool Poisoning**: 신뢰할 수 있는 도구와 유사한 이름의 악성 도구로 대체하는 공격입니다. 도구 서명 검증과 신뢰할 수 있는 소스의 MCP 서버만 사용하여 방지합니다.

**Over-permissioning**: MCP 서버에 과도한 권한을 부여하는 문제입니다. 최소 권한 원칙을 적용하고, 읽기 전용 모드 활용, 접근 가능한 경로 제한 등으로 대응합니다.

**Data Exfiltration**: 여러 도구를 조합하여 민감한 데이터를 외부로 유출하는 공격입니다. 도구 조합에 대한 모니터링, 네트워크 요청 제한, 민감 데이터 마스킹으로 방지합니다.

2025년 6월 MCP 인증 스펙 업데이트에서 OAuth 2.0 기반 Resource Server 분류가 도입되어 인증/인가 체계가 강화되었습니다.

## 참고 자료

- [Anthropic - Introducing the Model Context Protocol](https://www.anthropic.com/news/model-context-protocol)
- [MCP GitHub Repository](https://github.com/modelcontextprotocol)
- [MCP Specification](https://spec.modelcontextprotocol.io)
- [Wikipedia - Model Context Protocol](https://en.wikipedia.org/wiki/Model_Context_Protocol)
