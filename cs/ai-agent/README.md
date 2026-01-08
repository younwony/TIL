# AI Agent

AI 에이전트 관련 기술을 정리하는 공간입니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] AI Agent란 + LLM 기초                                     │
│        - AI 에이전트의 정의와 핵심 개념                          │
│            │                                                     │
│            ▼                                                     │
│   [2] Tool Use + MCP                                            │
│        - 도구 사용 및 외부 시스템 연결                           │
│            │                                                     │
│            ├──────────────┬──────────────┐                      │
│            ▼              ▼              ▼                      │
│   [3] Skill       [3] Hook         [3] Prompt Engineering       │
│        - 기능 확장 및 커스터마이징                               │
│            │                                                     │
│            ▼                                                     │
│   [4] Multi-Agent & Sub Agent                                   │
│        - 다중 에이전트 협력 및 병렬 실행                         │
│            │                                                     │
│            ▼                                                     │
│   [5] Agent SDK                                                 │
│        - 커스텀 에이전트 빌드                                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 난이도별 목차

### [1] 정의/기초

AI 에이전트가 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [AI Agent란](./ai-agent.md) | AI 에이전트의 정의, 구성 요소, 동작 원리 | 25분 |
| [LLM 기초](./llm.md) | 대규모 언어 모델의 기본 개념 | 30분 |

### [2] 입문

AI 에이전트 기초를 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Tool Use](./tool-use.md) | LLM의 도구 사용 패턴 | AI Agent란 |
| [MCP](./mcp.md) | AI 에이전트와 외부 시스템 연결을 위한 표준 프로토콜 | AI Agent란 |

### [3] 중급

MCP 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [RAG](./rag.md) | 검색 증강 생성, 외부 지식 기반 LLM 응답 `Trend` | LLM 기초, AI Agent란 |
| [Vibe Coding](./vibe-coding.md) | AI 협업 코딩 패러다임 `Trend` | AI Agent란, LLM 기초 |
| [Codex MCP 연결](./codex-mcp.md) | Claude Code와 OpenAI Codex CLI 통합 | MCP |
| [Claude Code Skill](./claude-code-skill.md) | AI 에이전트 기능 모듈화, 능력 패키징 | MCP |
| [Claude Code Hook](./claude-code-hook.md) | 생명주기 자동 실행 셸 명령어 | MCP, 셸 스크립트 |
| [Claude Code Slash Command](./claude-code-slash-command.md) | 자주 사용하는 프롬프트 명령어화 | MCP |
| [Prompt Engineering](./prompt-engineering.md) | 효과적인 프롬프트 작성 | LLM 기초, AI Agent란 |

### [4] 심화 - Multi-Agent & Sub Agent

확장 기능을 익힌 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Multi-Agent Systems](./multi-agent-systems.md) | 다중 에이전트 협력 아키텍처 `Trend` | AI Agent란, MCP, Tool Use |
| [Claude Code Sub Agent](./claude-code-sub-agent.md) | 독립적 작업 위임, 병렬 실행, Resume | Skill, Hook |

### [5] 심화 - SDK

Sub Agent를 이해한 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Agent SDK](./agent-sdk.md) | 커스텀 에이전트 빌드 | Sub Agent, Skill, Hook, MCP |

## 전체 목차

### 기초 개념
- [AI Agent란](./ai-agent.md) - 복잡한 목표를 달성하기 위해 자율적으로 계획하고, 실행하고, 적응하는 인공지능 시스템
- [LLM 기초](./llm.md) - 대규모 텍스트 데이터로 학습하여 자연어를 이해하고 생성하는 딥러닝 모델

### 도구 & 프로토콜
- [Tool Use](./tool-use.md) - LLM이 외부 도구를 호출하여 실제 작업을 수행하는 능력
- [MCP (Model Context Protocol)](./mcp.md) - AI 에이전트와 외부 시스템 연결을 위한 표준 프로토콜

### AI 협업 & 코딩 패러다임
- [RAG (Retrieval-Augmented Generation)](./rag.md) - 외부 지식을 검색하여 LLM 응답을 강화하는 하이브리드 AI 아키텍처 `Trend 2025`
- [Vibe Coding](./vibe-coding.md) - AI와 협업하여 코드를 생성하고 검토하는 새로운 코딩 패러다임 `Trend 2025`

### 확장 & 커스터마이징
- [Codex MCP 연결](./codex-mcp.md) - Claude Code에서 OpenAI Codex CLI를 MCP로 연결하여 두 AI 에이전트를 통합하는 방법
- [Claude Code Skill](./claude-code-skill.md) - AI 에이전트의 기능을 모듈화하여 확장하는 능력 패키징 시스템
- [Claude Code Hook](./claude-code-hook.md) - AI 에이전트의 생명주기에서 자동 실행되는 사용자 정의 셸 명령어 시스템
- [Claude Code Slash Command](./claude-code-slash-command.md) - 자주 사용하는 프롬프트를 명령어로 만들어 빠르게 실행하는 시스템
- [Prompt Engineering](./prompt-engineering.md) - LLM에서 원하는 결과를 얻기 위한 프롬프트 설계 및 최적화 기술

### 심화 기능
- [Multi-Agent Systems](./multi-agent-systems.md) - 여러 전문화된 AI 에이전트가 협력하여 복잡한 작업을 수행하는 분산 시스템 아키텍처 `Trend 2025`
- [Claude Code Sub Agent](./claude-code-sub-agent.md) - 복잡한 작업을 독립적으로 처리하는 전문화된 AI 에이전트 시스템
- [Agent SDK](./agent-sdk.md) - 커스텀 AI 에이전트를 프로그래밍 방식으로 구축하기 위한 개발 도구

## 작성 예정

*(모든 예정 문서가 작성 완료되었습니다)*
