# Codex CLI MCP 연결

> `[3] 중급` · 선수 지식: [MCP](./mcp.md)

> Claude Code에서 OpenAI Codex CLI를 MCP 서버로 연결하여 두 AI 에이전트를 통합하는 방법이다.

`#CodexCLI` `#Codex` `#OpenAI` `#MCP` `#ModelContextProtocol` `#ClaudeCode` `#AI통합` `#MCPServer` `#stdio` `#npx` `#Windows` `#설정파일` `#claude.json` `#AI협업` `#멀티에이전트` `#코딩어시스턴트` `#도구연동` `#CLI도구` `#자동화` `#개발생산성`

## 왜 알아야 하는가?

- **실무**: Claude Code와 Codex CLI를 통합하면 두 AI 에이전트의 강점을 결합하여 개발 생산성을 높일 수 있다. Codex의 코드 생성 능력과 Claude의 분석/추론 능력을 함께 활용할 수 있다.
- **면접**: "여러 AI 에이전트를 어떻게 통합하나요?"에 대한 실무 답변이 된다. MCP를 통한 도구 통합 패턴을 설명할 수 있어야 한다.
- **기반 지식**: MCP 설정의 실전 예제로, 다른 CLI 도구나 외부 서비스를 연동할 때 동일한 패턴을 적용할 수 있다.

## 핵심 개념

- **Codex CLI**: OpenAI가 제공하는 터미널 기반 AI 코딩 어시스턴트
- **MCP 서버**: 외부 도구를 Claude Code에 연결하는 표준 프로토콜 서버
- **stdio 방식**: 표준 입출력(stdin/stdout)으로 통신하는 로컬 프로세스 기반 MCP
- **npx**: Node.js 패키지를 임시 설치 후 실행하는 명령어

## 쉽게 이해하기

**통역사**에 비유할 수 있습니다.

Claude Code와 Codex CLI는 서로 다른 언어를 쓰는 전문가입니다:
- Claude: 분석, 설계, 문서화에 강함
- Codex: 코드 생성, 리팩토링에 강함

MCP 서버는 이 둘 사이의 **통역사** 역할을 합니다.

```
[Claude Code] ←→ [MCP 서버 (통역)] ←→ [Codex CLI]
```

통역사가 있으면 Claude가 "이 코드 리팩토링해줘"라고 말하면, Codex가 이해하고 작업한 결과를 Claude에게 전달할 수 있습니다.

## 상세 설명

### Codex CLI란?

OpenAI가 제공하는 터미널 기반 AI 코딩 어시스턴트입니다.

| 특징 | 설명 |
|------|------|
| 제공사 | OpenAI |
| 실행 환경 | 터미널/CLI |
| 주요 기능 | 코드 생성, 리팩토링, 설명, 디버깅 |
| 모델 | GPT 기반 |

### MCP 연결 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                    Claude Code (Host)                    │
│  ┌─────────────────────────────────────────────────┐    │
│  │              MCP Client                          │    │
│  │  - 프로토콜 통신 담당                            │    │
│  │  - JSON-RPC over stdio                          │    │
│  └─────────────┬───────────────────────────────────┘    │
└────────────────┼────────────────────────────────────────┘
                 │ stdin/stdout
                 ▼
┌─────────────────────────────────────────────────────────┐
│              MCP Server (@cexll/codex-mcp-server)        │
│  ┌─────────────────────────────────────────────────┐    │
│  │  - Codex CLI 래핑                                │    │
│  │  - 도구 목록 제공 (tools/list)                   │    │
│  │  - 도구 호출 처리 (tools/call)                   │    │
│  └─────────────┬───────────────────────────────────┘    │
└────────────────┼────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────┐
│                    Codex CLI                             │
│  - codex exec (비대화형 실행)                           │
│  - 코드 생성/수정 명령 처리                             │
└─────────────────────────────────────────────────────────┘
```

### Windows 환경 설정

Windows에서 MCP 서버를 설정할 때는 **cmd /c** 래퍼가 필요합니다.

**왜 cmd /c가 필요한가?**

Windows에서 `npx`는 실제로 `npx.cmd` 배치 파일입니다. MCP 클라이언트가 직접 실행하려면 `cmd.exe`를 통해 호출해야 합니다.

```
[MCP Client] → [cmd.exe] → [npx.cmd] → [Node.js 패키지]
```

### 설정 파일 구조

Claude Code의 MCP 설정은 `~/.claude.json` 파일에 저장됩니다.

```json
{
  "mcpServers": {
    "codex-cli": {
      "type": "stdio",
      "command": "cmd",
      "args": [
        "/c",
        "npx.cmd",
        "-y",
        "@cexll/codex-mcp-server"
      ]
    }
  }
}
```

| 필드 | 설명 |
|------|------|
| `type` | 통신 방식 (`stdio`: 표준 입출력, `http`: HTTP) |
| `command` | 실행할 명령어 (Windows: `cmd`) |
| `args` | 명령어 인자 배열 |
| `/c` | cmd.exe에서 명령 실행 후 종료 옵션 |
| `-y` | npx에서 설치 확인 없이 자동 진행 |

### 설정 방법

#### 방법 1: CLI 명령어 (권장)

```bash
# MCP 서버 추가
claude mcp add codex-cli -- cmd /c npx.cmd -y @cexll/codex-mcp-server

# 연결 상태 확인
claude mcp list

# 서버 제거 (필요 시)
claude mcp remove codex-cli
```

**주의**: Windows에서 `/c` 옵션이 파싱 오류를 일으킬 수 있습니다. 이 경우 방법 2를 사용하세요.

#### 방법 2: 설정 파일 직접 편집

`~/.claude.json` 파일을 직접 편집합니다.

```json
{
  "mcpServers": {
    "atlassian": {
      "type": "stdio",
      "command": "cmd",
      "args": ["/c", "npx.cmd", "-y", "mcp-remote", "https://mcp.atlassian.com/v1/sse"]
    },
    "codex-cli": {
      "type": "stdio",
      "command": "cmd",
      "args": ["/c", "npx.cmd", "-y", "@cexll/codex-mcp-server"]
    }
  }
}
```

### 사전 요구사항

| 요구사항 | 확인 명령어 | 최소 버전 |
|----------|-----------|----------|
| Node.js | `node --version` | v18.0.0+ |
| Codex CLI | `codex --version` | 최신 |
| Claude Code | `claude --version` | 최신 |

```bash
# Codex CLI 설치 (미설치 시)
npm install -g @openai/codex

# Codex CLI 인증
codex auth
```

### 연결 확인

```bash
# MCP 서버 목록 및 상태 확인
claude mcp list
```

출력 예시:
```
Checking MCP server health...

atlassian: cmd /c npx.cmd -y mcp-remote https://mcp.atlassian.com/v1/sse - ✓ Connected
codex-cli: cmd /c npx.cmd -y @cexll/codex-mcp-server - ✓ Connected
```

Claude Code 내에서도 `/mcp` 명령으로 확인 가능합니다.

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 두 AI 에이전트의 강점 결합 | 추가 설정 및 의존성 필요 |
| MCP 표준으로 일관된 통합 | Windows에서 설정이 복잡함 |
| 확장 가능한 아키텍처 | 네트워크 지연 발생 가능 |
| 도구 자동 발견 | Codex CLI 인증 필요 |

## 트러블슈팅

### 사례 1: "Connection closed" 또는 "Failed to connect"

#### 증상
```
codex-cli: cmd /c npx -y @cexll/codex-mcp-server - ✗ Failed to connect
```

#### 원인 분석
Windows에서 `npx`가 아닌 `npx.cmd`를 사용해야 합니다. MCP 클라이언트는 직접 실행 파일만 호출할 수 있어서, 배치 파일인 `npx`를 실행하려면 `cmd /c`를 통해야 합니다.

#### 해결 방법
설정에서 `npx`를 `npx.cmd`로 변경합니다.

```json
{
  "args": ["/c", "npx.cmd", "-y", "@cexll/codex-mcp-server"]
}
```

#### 예방 조치
Windows에서 Node.js 기반 MCP 서버 추가 시 항상 `cmd /c npx.cmd` 패턴을 사용합니다.

### 사례 2: CLI 명령어 파싱 오류

#### 증상
```bash
claude mcp add codex-cli -- cmd /c npx -y @cexll/codex-mcp-server
# 결과: cmd C:/ npx ... (잘못된 파싱)
```

#### 원인 분석
Windows의 `/c` 옵션이 경로로 해석되어 `C:/`로 변환됩니다.

#### 해결 방법
CLI 대신 설정 파일을 직접 편집합니다. `~/.claude.json`의 `mcpServers` 섹션에 직접 추가합니다.

### 사례 3: Codex CLI 인증 오류

#### 증상
MCP 서버는 연결되지만 도구 호출 시 인증 오류 발생.

#### 원인 분석
Codex CLI가 OpenAI API에 인증되지 않았습니다.

#### 해결 방법
```bash
# Codex CLI 인증
codex auth

# 또는 환경 변수로 API 키 설정
set OPENAI_API_KEY=your-api-key
```

## 제공 도구 (Tools)

Codex MCP 서버는 다음 도구들을 Claude Code에 제공합니다.

### 핵심 도구

| 도구 | 설명 | 주요 파라미터 |
|------|------|--------------|
| **ask-codex** | Codex에 질문하고 결과를 받는 핵심 기능 | `prompt`, `model`, `sandbox` |
| **brainstorm** | 구조화된 방식으로 아이디어 생성 | `topic`, `method` |
| **ping** | 연결 상태 테스트 | - |
| **help** | Codex CLI 도움말 표시 | - |

### ask-codex 도구 상세

가장 핵심적인 도구로, 다양한 옵션을 지원합니다.

```json
{
  "name": "ask-codex",
  "arguments": {
    "prompt": "@src/ 이 디렉토리의 구조를 분석해줘",
    "model": "gpt-5-codex",
    "sandbox": true
  }
}
```

**지원 모델:**

| 모델 | 설명 |
|------|------|
| `gpt-5-codex` | 기본 모델, 코드 특화 |
| `gpt-5` | 범용 GPT-5 |
| `o3` | 추론 특화 모델 |
| `o4-mini` | 경량 모델 |
| `codex-1` | 코드 생성 전용 |

**Sandbox 모드:**

| 모드 | 설명 |
|------|------|
| `read-only` | 분석만 가능 (안전) |
| `workspace-write` | 파일 수정 가능 |
| `danger-full-access` | 전체 시스템 접근 |

### @ 문법으로 파일 참조

Codex MCP는 `@` 문법으로 파일을 컨텍스트에 포함할 수 있습니다.

```
@src/main.js          # 특정 파일
@src/                 # 디렉토리 전체
@config.json          # 설정 파일
@package.json         # 의존성 파일
```

## 실제 사용법

### Claude Code에서 자연어로 사용

Claude Code 내에서 자연어로 Codex 도구를 호출할 수 있습니다.

**코드 분석:**
```
"codex로 @src/utils.js 분석해줘"
"@config.json의 의존성을 나열해줘"
```

**코드 생성:**
```
"codex를 사용해서 React 로그인 컴포넌트 만들어줘"
"gpt-5 모델로 @api/handlers.ts 리팩토링 제안해줘"
```

**디버깅:**
```
"codex로 @src/app.py의 버그를 찾아줘"
"이 에러 메시지 분석해줘: [에러 내용]"
```

### 직접 도구 호출

Claude가 자동으로 MCP 도구를 호출하거나, 명시적으로 요청할 수 있습니다.

```
"ask-codex 도구로 @src/ 디렉토리 요약해줘"
"brainstorm 도구로 캐싱 전략 아이디어 내줘"
"ping으로 codex 연결 상태 확인해줘"
```

### 승인 정책 설정

도구 실행 시 승인 방식을 설정할 수 있습니다.

| 정책 | 설명 | 사용 사례 |
|------|------|----------|
| `never` | 승인 없이 자동 실행 | 신뢰할 수 있는 작업 |
| `on-failure` | 실패 시에만 승인 요청 | 일반적인 사용 |
| `on-request` | 매번 승인 요청 | 민감한 작업 |

## 활용 예시

### 예시 1: 코드 리뷰 자동화

```
사용자: "codex로 @src/payment.js 코드 리뷰해줘"

Claude: ask-codex 도구를 호출합니다.
        프롬프트: "@src/payment.js 코드 리뷰: 보안 취약점, 성능 이슈,
                  코드 스타일 문제를 찾아서 개선안을 제시해줘"

Codex 응답:
1. 보안: SQL 인젝션 가능성 (Line 45)
2. 성능: N+1 쿼리 문제 (Line 78-82)
3. 스타일: 매직 넘버 사용 (Line 23)
[개선 코드 제안...]
```

### 예시 2: 테스트 코드 생성

```
사용자: "codex로 @src/calculator.ts에 대한 테스트 코드 만들어줘"

Claude: ask-codex 도구를 호출합니다.
        모델: gpt-5-codex
        sandbox: true

결과: Jest 테스트 코드 생성
- 단위 테스트 10개
- 엣지 케이스 5개
- 모킹 예시 포함
```

### 예시 3: 리팩토링 제안

```
사용자: "o3 모델로 @src/legacy/ 폴더를 현대적인 패턴으로 리팩토링 제안해줘"

Claude: ask-codex 도구를 호출합니다.
        모델: o3 (추론 특화)
        프롬프트: 레거시 코드 분석 및 현대화 제안

결과:
1. 콜백 → async/await 변환
2. var → const/let 교체
3. 클래스 컴포넌트 → 함수형 컴포넌트
[마이그레이션 계획 및 코드...]
```

### 예시 4: 아이디어 브레인스토밍

```
사용자: "brainstorm 도구로 API 성능 최적화 방법 아이디어 내줘"

Claude: brainstorm 도구를 호출합니다.
        topic: "API 성능 최적화"
        method: "SCAMPER"

결과:
- Substitute: REST → GraphQL
- Combine: 여러 엔드포인트 병합
- Adapt: 캐싱 레이어 추가
- Modify: 응답 압축 적용
- Put to other use: CDN 활용
- Eliminate: 불필요한 필드 제거
- Reverse: Pull → Push (WebSocket)
```

### 예시 5: 멀티 에이전트 워크플로우

Claude와 Codex의 강점을 결합한 복잡한 작업:

```
1단계 - Claude (설계):
"인증 시스템 아키텍처를 설계해줘"
→ JWT 기반 인증 흐름, 인터페이스 정의

2단계 - Codex (구현):
"codex로 위 설계를 기반으로 @src/auth/ 구현해줘"
→ 코드 생성

3단계 - Claude (리뷰):
"생성된 코드를 보안 관점에서 검토해줘"
→ 취약점 분석, 개선점 제안

4단계 - Codex (수정):
"codex로 보안 이슈를 수정해줘"
→ 코드 패치
```

## 사용 사례

### 사례 1: 코드 생성 위임

Claude Code에서 복잡한 코드 생성을 Codex에 위임합니다.

```
사용자: "React 컴포넌트로 데이터 테이블 만들어줘"
Claude: Codex CLI의 코드 생성 도구를 호출하여 컴포넌트 생성
```

### 사례 2: 멀티 에이전트 협업

Claude가 설계하고 Codex가 구현하는 협업 워크플로우.

```
1. Claude: 아키텍처 설계 및 인터페이스 정의
2. Codex: 구현 코드 생성
3. Claude: 코드 리뷰 및 개선점 제안
4. Codex: 리팩토링 실행
```

### 사례 3: 도구 통합 확장

동일한 패턴으로 다른 CLI 도구도 MCP로 연결할 수 있습니다.

```json
{
  "mcpServers": {
    "codex-cli": { "..." },
    "github": { "..." },
    "jira": { "..." },
    "slack": { "..." }
  }
}
```

## 면접 예상 질문

### Q1: Claude Code에서 외부 CLI 도구를 어떻게 연동하나요?

A: **MCP(Model Context Protocol)를 사용**합니다.

MCP 서버를 만들어 CLI 도구를 래핑하면, Claude Code가 표준 프로토콜로 해당 도구를 호출할 수 있습니다.

**왜냐하면** MCP는 AI 모델과 외부 도구를 연결하는 표준이기 때문입니다. CLI 도구를 MCP 서버로 감싸면:
1. Claude Code가 `tools/list`로 사용 가능한 도구를 자동 발견
2. `tools/call`로 도구를 호출
3. 결과를 표준 형식으로 수신

### Q2: Windows에서 MCP stdio 서버 설정 시 주의점은?

A: **cmd /c와 npx.cmd 사용**이 필요합니다.

**왜냐하면**:
1. `npx`는 Windows에서 실제로 `npx.cmd` 배치 파일입니다
2. MCP 클라이언트는 직접 실행 파일만 호출 가능합니다
3. 배치 파일을 실행하려면 `cmd.exe`를 통해야 합니다

올바른 설정:
```json
{
  "command": "cmd",
  "args": ["/c", "npx.cmd", "-y", "패키지명"]
}
```

### Q3: 여러 AI 에이전트를 통합하면 어떤 이점이 있나요?

A: **각 에이전트의 강점을 결합**할 수 있습니다.

예를 들어 Claude + Codex 조합:
- **Claude**: 분석, 설계, 문서화, 복잡한 추론
- **Codex**: 코드 생성, 리팩토링, 패턴 적용

**왜냐하면** 각 AI 모델은 학습 데이터와 최적화 방향이 다르기 때문입니다. MCP로 통합하면 하나의 인터페이스에서 여러 에이전트의 능력을 활용할 수 있습니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [MCP](./mcp.md) | MCP 기본 개념과 아키텍처 | [2] 입문 |
| [Claude Code Skill](./claude-code-skill.md) | MCP 기반 기능 확장 | [3] 중급 |
| [Multi-Agent Systems](./multi-agent-systems.md) | 다중 에이전트 협업 | [4] 심화 |

## 참고 자료

- [cexll/codex-mcp-server GitHub](https://github.com/cexll/codex-mcp-server)
- [OpenAI Codex MCP 문서](https://developers.openai.com/codex/mcp/)
- [MCP Specification](https://spec.modelcontextprotocol.io)
- [Claude Code MCP 가이드](https://code.claude.com/docs/en/mcp)
