# Multi-Agent Systems (다중 에이전트 시스템)

> `[4] 심화` · 선수 지식: [AI Agent란](./ai-agent.md), [MCP](./mcp.md), [Tool Use](./tool-use.md)

> `Trend` 2025

> 여러 전문화된 AI 에이전트가 협력하여 복잡한 작업을 수행하는 분산 시스템 아키텍처

`#다중에이전트` `#MultiAgent` `#MAS` `#에이전트오케스트레이션` `#AgentOrchestration` `#분산AI` `#DistributedAI` `#Supervisor` `#OrchestratorWorkers` `#AdaptiveNetwork` `#CrewAI` `#LangGraph` `#AutoGen` `#SemanticKernel` `#BedrockAgents` `#VertexAI` `#AzureAI` `#Agent2Agent` `#A2A` `#협업AI` `#에이전트협업` `#병렬처리` `#Parallelization` `#PromptChaining` `#Routing` `#에이전트프레임워크` `#엔터프라이즈AI` `#AIArchitecture`

## 왜 알아야 하는가?

2025년은 "에이전트의 해"입니다. 단일 AI가 모든 것을 처리하던 시대에서, 여러 전문화된 에이전트가 **오케스트라**처럼 협력하는 시대로 전환되었습니다.

- **실무**: 72%의 엔터프라이즈 AI 프로젝트가 Multi-Agent 아키텍처를 채택 (2024년 23%에서 급증)
- **면접**: "복잡한 AI 시스템을 어떻게 설계하시겠습니까?" 질문에 대한 핵심 답변
- **기반 지식**: Agent SDK, 분산 시스템, 마이크로서비스 아키텍처의 교차점

## 핵심 개념

- **전문화된 에이전트**: 각 에이전트가 특정 역할에 집중 (코딩, 리서치, 검증 등)
- **오케스트레이션**: 에이전트 간 조율, 작업 위임, 결과 통합
- **프로토콜 표준**: MCP(도구 사용) + A2A(에이전트 간 통신)

## 쉽게 이해하기

**오케스트라 비유**

| 요소 | 오케스트라 | Multi-Agent System |
|------|-----------|-------------------|
| 지휘자 | 지휘자 | Orchestrator Agent |
| 연주자 | 바이올린, 첼로, 플루트 | Coder, Researcher, Reviewer Agent |
| 악보 | 악보 | Task Specification |
| 협연 | 파트별 조화 | 에이전트 간 통신 (A2A) |
| 악기 | 각자의 악기 | 각 에이전트의 도구 (MCP) |

단일 피아니스트가 모든 파트를 연주하는 것보다, 전문 연주자들이 협력하면 더 풍성한 음악이 만들어집니다.

## 오케스트레이션 패턴

### 1. Supervisor Pattern (감독자 패턴)

```
┌─────────────────────────────────────────┐
│           Supervisor Agent              │
│    (중앙 제어, 작업 분배, 결과 통합)      │
└─────────────┬───────────────────────────┘
              │
    ┌─────────┼─────────┐
    ▼         ▼         ▼
┌───────┐ ┌───────┐ ┌───────┐
│Agent A│ │Agent B│ │Agent C│
│(코딩) │ │(검색) │ │(검증) │
└───────┘ └───────┘ └───────┘
```

**특징**
- 중앙 집중식 의사결정
- 명확한 책임 분리
- 복잡한 워크플로우 제어 용이

**적합한 경우**
- 명확한 작업 분해가 가능한 경우
- 순차적 처리가 필요한 경우
- 품질 관리가 중요한 경우

### 2. Adaptive Agent Network (적응형 네트워크)

```
┌───────┐     ┌───────┐
│Agent A│◄───►│Agent B│
└───┬───┘     └───┬───┘
    │             │
    │   ┌───────┐ │
    └──►│Agent C│◄┘
        └───┬───┘
            │
        ┌───────┐
        │Agent D│
        └───────┘
```

**특징**
- 분산 협업, 중앙 제어 없음
- 에이전트가 자율적으로 작업 위임/전달
- 저지연, 고상호작용 환경에 최적화

**적합한 경우**
- 실시간 응답이 필요한 경우
- 동적으로 변하는 작업 흐름
- 에이전트 전문성에 따른 유연한 라우팅

### 3. Orchestrator-Workers Pattern

Anthropic의 Research 시스템이 채택한 패턴입니다.

```
┌─────────────────────────────────────────┐
│         Lead Agent (Orchestrator)       │
│    - 전체 프로세스 조율                   │
│    - 작업 분해 및 할당                    │
│    - 결과 통합                           │
└─────────────────┬───────────────────────┘
                  │ 병렬 위임
    ┌─────────────┼─────────────┐
    ▼             ▼             ▼
┌─────────┐ ┌─────────┐ ┌─────────┐
│Worker 1 │ │Worker 2 │ │Worker 3 │
│(독립 실행)│ │(독립 실행)│ │(독립 실행)│
└─────────┘ └─────────┘ └─────────┘
```

**특징**
- Lead Agent가 조율, Worker가 병렬 실행
- 각 Worker는 독립적으로 작업 수행
- 확장성과 효율성의 균형

## 핵심 워크플로우 패턴

### Workflow Tier의 5가지 핵심 패턴

| 패턴 | 설명 | 사용 사례 |
|------|------|----------|
| **Prompt Chaining** | 순차적 프롬프트 연결 | 문서 분석 → 요약 → 번역 |
| **Routing** | 입력에 따른 에이전트 선택 | 질문 유형별 전문 에이전트 라우팅 |
| **Parallelization** | 독립 작업 동시 실행 | 여러 소스 동시 검색 |
| **Evaluator-Optimizer** | 결과 평가 및 개선 반복 | 코드 생성 → 리뷰 → 수정 |
| **Orchestrator-Workers** | 조율자 + 작업자 분리 | 복잡한 리서치 태스크 |

### Prompt Chaining 예시

```python
# 순차적 체이닝
result1 = agent_analyzer.run("문서 분석: {document}")
result2 = agent_summarizer.run(f"요약: {result1}")
result3 = agent_translator.run(f"번역: {result2}")
```

### Parallelization 예시

```python
import asyncio

async def parallel_search():
    tasks = [
        agent_web.search("키워드"),
        agent_db.query("SELECT ..."),
        agent_api.fetch("/endpoint")
    ]
    results = await asyncio.gather(*tasks)
    return merge_results(results)
```

## 프로토콜 표준

### MCP + A2A 협력

2025년, Anthropic의 MCP와 Google의 A2A가 Linux Foundation에 기부되며 오픈 표준으로 자리잡았습니다.

| 프로토콜 | 역할 | 비유 |
|----------|------|------|
| **MCP** (Model Context Protocol) | 에이전트가 **도구**를 사용하는 방법 | 연주자가 악기를 다루는 방법 |
| **A2A** (Agent2Agent) | 에이전트가 **서로 통신**하는 방법 | 연주자들이 서로 신호를 주고받는 방법 |

```
┌─────────┐                    ┌─────────┐
│ Agent A │◄── A2A Protocol ──►│ Agent B │
└────┬────┘                    └────┬────┘
     │                              │
     │ MCP                          │ MCP
     ▼                              ▼
┌─────────┐                    ┌─────────┐
│ Tool 1  │                    │ Tool 2  │
└─────────┘                    └─────────┘
```

## 프레임워크 분류

### 2025년 프레임워크 지형

| 카테고리 | 프레임워크 | 특징 |
|----------|-----------|------|
| **Visual/Low-code** | n8n, Flowise, Zapier Agents | 비개발자 친화적, 빠른 프로토타이핑 |
| **Code-first SDK** | LangGraph, CrewAI, OpenAI Agents SDK, Google ADK, Semantic Kernel | 세밀한 제어, 커스터마이징 |
| **Enterprise** | Amazon Bedrock Agents, Vertex AI Agent Builder, Azure AI Agent Service | 규모, 보안, 규정 준수 |

### 주요 프레임워크 비교

| 프레임워크 | 회사 | 강점 | 적합 케이스 |
|-----------|------|------|------------|
| **CrewAI** | CrewAI | 역할 기반 설계, 직관적 API | 팀 시뮬레이션, 협업 워크플로우 |
| **LangGraph** | LangChain | 그래프 기반, 상태 관리 | 복잡한 조건부 워크플로우 |
| **AutoGen** | Microsoft | 대화형 에이전트, 그룹 채팅 | 다자간 협업, 토론 시뮬레이션 |
| **Semantic Kernel** | Microsoft | 엔터프라이즈 기능, 타입 안전성 | 프로덕션 배포, 대규모 시스템 |

## 인프라 아키텍처

### 분산 시스템으로서의 Multi-Agent

> "Multi-Agent 시스템 확장은 프롬프트 엔지니어링 문제가 아니라 **인프라 설계 문제**입니다."

```
┌─────────────────────────────────────────────────────────┐
│                    Kubernetes Cluster                    │
├─────────────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │ Agent Pod│  │ Agent Pod│  │ Agent Pod│  (Auto-scale)│
│  │ (Coder)  │  │(Reviewer)│  │(Deployer)│              │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘              │
│       │             │             │                     │
│       └─────────────┼─────────────┘                     │
│                     ▼                                   │
│            ┌────────────────┐                           │
│            │  Message Bus   │ (Kafka/RabbitMQ)          │
│            └────────────────┘                           │
├─────────────────────────────────────────────────────────┤
│  Observability: Logging | Metrics | Tracing            │
└─────────────────────────────────────────────────────────┘
```

### 핵심 인프라 컴포넌트

| 컴포넌트 | 기술 | 역할 |
|----------|------|------|
| **컨테이너화** | Docker | 에이전트별 독립 실행 환경 |
| **오케스트레이션** | Kubernetes | 오토스케일링, 복구, 배포 |
| **GPU 스케줄링** | NVIDIA Operator | 추론 에이전트용 GPU 할당 |
| **메시지 버스** | Kafka, RabbitMQ | 에이전트 간 이벤트 전달 |
| **관측성** | OpenTelemetry | 에이전트 동작 모니터링 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 전문화로 품질 향상 | 복잡성 증가 |
| 병렬 처리로 속도 향상 | 디버깅 어려움 |
| 개별 에이전트 업데이트 용이 | 에이전트 간 조율 오버헤드 |
| 확장성 확보 | 비용 관리 필요 (토큰 소모) |
| 장애 격리 | 네트워크 지연 |

## 설계 원칙

### 성공적인 배포를 위한 원칙

1. **단순하고 조합 가능한 아키텍처** - 복잡한 프레임워크보다 단순한 구조 우선
2. **관측성 내장** - 에이전트 동작 모니터링 필수
3. **보안 제어** - 감사 추적, 권한 관리
4. **비용 규율** - 토큰 사용량, 리소스 소비 모니터링

### 안티패턴

| 안티패턴 | 문제점 | 해결책 |
|----------|--------|--------|
| 과도한 에이전트 수 | 조율 복잡성 폭발 | 최소 에이전트로 시작 |
| 중앙 집중 병목 | 단일 장애점 | Supervisor 분산 |
| 무한 루프 위험 | 에이전트 간 순환 호출 | 최대 반복 횟수 제한 |
| 비용 무제한 | 토큰 소비 폭주 | 예산 제한, 모니터링 |

## 면접 예상 질문

### Q: Multi-Agent System에서 Supervisor 패턴과 Adaptive Network 패턴의 차이점은?

A: **Supervisor 패턴**은 중앙 에이전트가 모든 작업을 분배하고 결과를 통합하는 중앙 집중식 구조입니다. 명확한 워크플로우와 품질 관리가 필요할 때 적합합니다. 반면 **Adaptive Network 패턴**은 중앙 제어 없이 에이전트들이 자율적으로 협력하고 작업을 위임합니다. 실시간 응답과 유연한 라우팅이 필요한 저지연 환경에 적합합니다. 선택 기준은 **제어 필요성 vs 유연성**, **일관성 vs 속도** 트레이드오프입니다.

### Q: Multi-Agent 시스템의 확장성을 어떻게 확보하나요?

A: Multi-Agent 확장은 **인프라 설계 문제**입니다. 핵심 전략은: (1) 에이전트를 Docker로 컨테이너화하여 독립 배포, (2) Kubernetes로 수요에 따른 오토스케일링, (3) Kafka/RabbitMQ 같은 메시지 버스로 에이전트 간 비동기 통신, (4) OpenTelemetry로 관측성 확보. 분산 시스템 원칙(관측 가능, 조합 가능, 규정 준수)을 에이전트에 적용해야 합니다.

### Q: MCP와 A2A 프로토콜의 역할 차이는?

A: **MCP(Model Context Protocol)**는 에이전트가 **도구를 사용하는 방법**을 정의합니다. 파일 읽기, API 호출, 데이터베이스 쿼리 등 외부 시스템과의 상호작용입니다. **A2A(Agent2Agent)**는 에이전트가 **서로 통신하는 방법**을 정의합니다. 작업 위임, 결과 전달, 상태 공유 등 에이전트 간 협력입니다. 두 프로토콜은 상호 보완적으로 설계되어 함께 작동합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [AI Agent란](./ai-agent.md) | 선수 지식 - 에이전트 기본 개념 | [1] 정의 |
| [MCP](./mcp.md) | 선수 지식 - 도구 사용 프로토콜 | [2] 입문 |
| [Tool Use](./tool-use.md) | 선수 지식 - 도구 호출 패턴 | [2] 입문 |
| [Claude Code Sub Agent](./claude-code-sub-agent.md) | 관련 - 서브에이전트 실습 | [4] 심화 |
| [Agent SDK](./agent-sdk.md) | 후속 - 커스텀 에이전트 구축 | [5] 심화 |
| [메시지 큐](../system-design/message-queue.md) | 관련 - 에이전트 간 통신 | [3] 중급 |
| [Kubernetes](../system-design/kubernetes.md) | 관련 - 에이전트 오케스트레이션 | [3] 중급 |

## 참고 자료

- [Anthropic - How we built our multi-agent research system](https://www.anthropic.com/engineering/multi-agent-research-system)
- [Microsoft Azure - AI Agent Orchestration Patterns](https://learn.microsoft.com/en-us/azure/architecture/ai-ml/guide/ai-agent-design-patterns)
- [Kore.ai - Choosing the right orchestration pattern](https://www.kore.ai/blog/choosing-the-right-orchestration-pattern-for-multi-agent-systems)
- [IBM - AI Agents in 2025: Expectations vs Reality](https://www.ibm.com/think/insights/ai-agents-2025-expectations-vs-reality)
- [The New Stack - 5 Key Trends Shaping Agentic Development in 2026](https://thenewstack.io/5-key-trends-shaping-agentic-development-in-2026/)
- [Microsoft Agent Framework Overview](https://learn.microsoft.com/en-us/agent-framework/overview/agent-framework-overview)
