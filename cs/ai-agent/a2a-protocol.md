# A2A (Agent-to-Agent) 프로토콜

> `[3] 중급` · 선수 지식: [MCP](./mcp.md), [AI Agent](./ai-agent.md)

> `Trend` 2025

> A2A는 **AI 에이전트 간의 통신과 협업을 위한 표준 프로토콜**이다.

`#A2A` `#Agent2Agent` `#에이전트간통신` `#Google` `#LinuxFoundation` `#AIAgent` `#에이전트협업` `#멀티에이전트` `#MultiAgent` `#AgentCard` `#에이전트카드` `#에이전트디스커버리` `#TaskManagement` `#JSONRPC` `#gRPC` `#SSE` `#OpenProtocol` `#오픈프로토콜` `#분산에이전트` `#AgenticAI` `#에이전틱AI` `#MCP` `#ACP` `#에이전트상호운용성` `#Interoperability` `#Apache2` `#오픈소스` `#2025트렌드`

## 왜 알아야 하는가?

- **실무**: AI 에이전트를 여러 개 운영하거나, 외부 에이전트와 협업해야 할 때 A2A로 통신한다. 2025년 Gartner가 선정한 **Top Tech Trend**이며, Google, IBM, Microsoft가 모두 채택했다.
- **면접**: "MCP와 A2A의 차이점은?"이라는 질문이 나온다. MCP는 에이전트↔도구, A2A는 에이전트↔에이전트 통신이라는 핵심 차이를 알아야 한다.
- **기반 지식**: 멀티 에이전트 시스템의 핵심 프로토콜이다. 2028년까지 33% 엔터프라이즈 소프트웨어가 Agentic AI를 포함할 것으로 예측되며, A2A는 그 기반이 된다.

## 핵심 개념

- **A2A**: AI 에이전트 간 통신 표준 프로토콜 (Google 2025년 4월 발표)
- **Agent Card**: 에이전트가 자신의 능력을 광고하는 JSON 메타데이터 (`/.well-known/agent.json`)
- **Task**: 에이전트 간 작업 요청/완료의 단위, 생명주기를 가짐
- **MCP와 보완 관계**: MCP는 에이전트↔도구, A2A는 에이전트↔에이전트

## 쉽게 이해하기

**A2A**를 **회사 간 업무 협업**에 비유할 수 있습니다.

**개인이 모든 것을 처리하는 경우 (단일 에이전트)**
- 개발자가 디자인, 마케팅, 법률 검토까지 혼자 처리
- 모든 도구(포토샵, 엑셀, 법률 DB)를 직접 사용
- 전문성 한계, 비효율적

**전문 회사들과 협업하는 경우 (멀티 에이전트 + A2A)**
- 디자인은 디자인 회사에 의뢰
- 법률 검토는 법무법인에 의뢰
- 각 회사가 자체 도구(MCP)로 작업 후 결과 전달
- **A2A = 회사 간 업무 요청서/보고서 표준 양식**

```
[나의 AI 에이전트] ─── A2A ───> [디자인 에이전트]
        │                              │
       MCP                            MCP
        │                              │
    [내 도구들]                   [디자인 도구들]
```

**핵심 비유**:
- **MCP** = 직원이 사무용품(도구)을 사용하는 방법
- **A2A** = 회사 간 공식 업무 요청/협업 프로토콜

## 상세 설명

### A2A vs MCP: 역할 분담

| 프로토콜 | 통신 대상 | 역할 | 비유 |
|----------|----------|------|------|
| **MCP** | 에이전트 ↔ 도구 | 에이전트가 도구를 사용 | 직원이 도구 사용 |
| **A2A** | 에이전트 ↔ 에이전트 | 에이전트 간 협업/위임 | 회사 간 업무 협업 |

**왜 둘 다 필요한가?**

복잡한 작업은 단일 에이전트로 해결하기 어렵습니다.
- **전문성**: 법률 검토 에이전트, 데이터 분석 에이전트 등 전문 분야별 에이전트
- **보안**: 민감 데이터를 다루는 에이전트를 격리
- **확장성**: 작업 부하를 여러 에이전트에 분산

### 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                      사용자 요청                                │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Client Agent (요청자)                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ 1. Agent Card 조회 (/.well-known/agent.json)              │   │
│  │ 2. 적합한 Remote Agent 선택                               │   │
│  │ 3. Task 생성 및 전송                                      │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │ MCP                              │
│                        [로컬 도구들]                            │
└─────────────────────────────────────────────────────────────────┘
                              │
                         A2A Protocol
                    (JSON-RPC 2.0 / gRPC)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Remote Agent (수행자)                         │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ 1. Task 수신                                              │   │
│  │ 2. MCP로 도구 사용하여 작업 수행                          │   │
│  │ 3. Artifact(결과물) 반환                                  │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │ MCP                              │
│                        [전문 도구들]                            │
└─────────────────────────────────────────────────────────────────┘
```

### 핵심 구성 요소

| 구성 요소 | 역할 | 예시 |
|----------|------|------|
| **Client Agent** | 작업을 요청하는 에이전트 | 사용자 요청을 받은 주 에이전트 |
| **Remote Agent** | 작업을 수행하는 에이전트 | 법률 검토, 데이터 분석 전문 에이전트 |
| **Agent Card** | 에이전트 능력 명세 (JSON) | 이름, 기술, 인증 방식 등 |
| **Task** | 작업 요청 단위 | 생명주기: created → working → completed |
| **Artifact** | 작업 결과물 | 텍스트, 파일, 구조화된 데이터 |
| **Message** | 에이전트 간 통신 메시지 | 컨텍스트, 지시사항, 진행 상황 |

### Agent Card

모든 A2A 에이전트는 `/.well-known/agent.json`에 자신의 정보를 공개합니다.

```json
{
  "name": "LegalReviewAgent",
  "description": "계약서 법률 검토 전문 에이전트",
  "url": "https://legal-agent.example.com",
  "version": "1.0.0",
  "capabilities": {
    "streaming": true,
    "pushNotifications": true,
    "stateTransitionHistory": true
  },
  "authentication": {
    "schemes": ["oauth2", "apiKey"]
  },
  "defaultInputModes": ["text"],
  "defaultOutputModes": ["text", "file"],
  "skills": [
    {
      "id": "contract-review",
      "name": "계약서 검토",
      "description": "계약서의 법적 위험 요소를 분석합니다",
      "tags": ["legal", "contract", "risk-analysis"]
    }
  ]
}
```

**왜 Agent Card가 필요한가?**
- **발견(Discovery)**: Client가 어떤 에이전트가 어떤 일을 할 수 있는지 파악
- **선택**: 여러 에이전트 중 가장 적합한 에이전트 선택
- **호환성 확인**: 인증 방식, 입출력 형식 등 사전 확인

### Task 생명주기

```
       submitted
           │
           ▼
        working ←────────────────┐
           │                     │
     ┌─────┴─────┐               │
     ▼           ▼               │
input-required  진행 중 ─────────┘
     │
     ▼
  completed / failed / canceled
```

| 상태 | 설명 |
|------|------|
| `submitted` | Task가 제출됨 |
| `working` | 에이전트가 작업 중 |
| `input-required` | 추가 입력 필요 |
| `completed` | 성공적으로 완료 |
| `failed` | 실패 |
| `canceled` | 취소됨 |

### 통신 방식

A2A는 다양한 통신 패턴을 지원합니다.

| 방식 | 설명 | 사용 사례 |
|------|------|----------|
| **동기 (Request/Response)** | 즉시 응답 | 간단한 조회 |
| **스트리밍 (SSE)** | 실시간 진행 상황 | 긴 작업의 중간 결과 |
| **비동기 (Push Notification)** | 완료 시 알림 | 장시간 작업 |

## 동작 원리

### 에이전트 발견 및 작업 위임

```
┌──────────────┐                                    ┌──────────────┐
│ Client Agent │                                    │ Remote Agent │
└──────┬───────┘                                    └──────┬───────┘
       │                                                   │
       │  1. GET /.well-known/agent.json                   │
       │ ─────────────────────────────────────────────────>│
       │                                                   │
       │  2. Agent Card 반환                               │
       │ <─────────────────────────────────────────────────│
       │     {name, skills, auth...}                       │
       │                                                   │
       │  3. tasks/send (Task 생성)                        │
       │ ─────────────────────────────────────────────────>│
       │     {id, message: "계약서 검토해줘"}               │
       │                                                   │
       │  4. Task 수신 확인                                │
       │ <─────────────────────────────────────────────────│
       │     {id, status: "working"}                       │
       │                                                   │
       │         [Remote Agent: MCP로 도구 사용하여 작업]    │
       │                                                   │
       │  5. tasks/get 또는 Push Notification              │
       │ <─────────────────────────────────────────────────│
       │     {status: "completed", artifact: {...}}        │
       │                                                   │
```

## 예제 코드

### Agent Card 조회 및 Task 생성 (Python)

```python
import httpx
import json

class A2AClient:
    """A2A 클라이언트 - 다른 에이전트와 통신"""

    async def discover_agent(self, agent_url: str) -> dict:
        """Agent Card를 조회하여 에이전트 능력 확인"""
        async with httpx.AsyncClient() as client:
            response = await client.get(
                f"{agent_url}/.well-known/agent.json"
            )
            return response.json()

    async def send_task(
        self,
        agent_url: str,
        message: str,
        skill_id: str = None
    ) -> dict:
        """원격 에이전트에 Task 전송"""
        task = {
            "jsonrpc": "2.0",
            "method": "tasks/send",
            "params": {
                "id": self._generate_task_id(),
                "message": {
                    "role": "user",
                    "parts": [{"type": "text", "text": message}]
                }
            },
            "id": 1
        }

        if skill_id:
            task["params"]["skillId"] = skill_id

        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{agent_url}/a2a",
                json=task
            )
            return response.json()

    async def get_task_status(
        self,
        agent_url: str,
        task_id: str
    ) -> dict:
        """Task 상태 조회"""
        request = {
            "jsonrpc": "2.0",
            "method": "tasks/get",
            "params": {"id": task_id},
            "id": 1
        }

        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{agent_url}/a2a",
                json=request
            )
            return response.json()


# 사용 예시
async def main():
    client = A2AClient()

    # 1. 법률 검토 에이전트 발견
    agent_card = await client.discover_agent(
        "https://legal-agent.example.com"
    )
    print(f"에이전트: {agent_card['name']}")
    print(f"기술: {[s['name'] for s in agent_card['skills']]}")

    # 2. 계약서 검토 Task 전송
    result = await client.send_task(
        "https://legal-agent.example.com",
        message="이 계약서의 법적 위험 요소를 분석해주세요: ...",
        skill_id="contract-review"
    )

    task_id = result["result"]["id"]
    print(f"Task 생성됨: {task_id}")

    # 3. 완료 대기 (실제로는 webhook이나 polling 사용)
    status = await client.get_task_status(
        "https://legal-agent.example.com",
        task_id
    )
    print(f"상태: {status['result']['status']}")
```

### A2A 서버 구현 (Python)

```python
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import uuid

app = FastAPI()

# Task 저장소 (실제로는 DB 사용)
tasks = {}

@app.get("/.well-known/agent.json")
async def get_agent_card():
    """Agent Card 반환 - 에이전트 능력 광고"""
    return {
        "name": "DataAnalysisAgent",
        "description": "데이터 분석 전문 에이전트",
        "url": "https://data-agent.example.com",
        "version": "1.0.0",
        "capabilities": {
            "streaming": True,
            "pushNotifications": False
        },
        "skills": [
            {
                "id": "analyze-csv",
                "name": "CSV 분석",
                "description": "CSV 데이터를 분석하고 인사이트 도출"
            }
        ]
    }

@app.post("/a2a")
async def handle_a2a(request: Request):
    """A2A JSON-RPC 요청 처리"""
    body = await request.json()
    method = body.get("method")
    params = body.get("params", {})

    if method == "tasks/send":
        return await handle_task_send(body["id"], params)
    elif method == "tasks/get":
        return await handle_task_get(body["id"], params)
    else:
        return JSONResponse({
            "jsonrpc": "2.0",
            "error": {"code": -32601, "message": "Method not found"},
            "id": body["id"]
        })

async def handle_task_send(request_id: int, params: dict):
    """새 Task 생성"""
    task_id = params.get("id") or str(uuid.uuid4())

    # Task 저장
    tasks[task_id] = {
        "id": task_id,
        "status": "working",
        "message": params.get("message"),
        "artifacts": []
    }

    # 비동기로 실제 작업 시작 (여기서는 생략)
    # asyncio.create_task(process_task(task_id))

    return JSONResponse({
        "jsonrpc": "2.0",
        "result": {
            "id": task_id,
            "status": "working"
        },
        "id": request_id
    })

async def handle_task_get(request_id: int, params: dict):
    """Task 상태 조회"""
    task_id = params.get("id")
    task = tasks.get(task_id)

    if not task:
        return JSONResponse({
            "jsonrpc": "2.0",
            "error": {"code": -32000, "message": "Task not found"},
            "id": request_id
        })

    return JSONResponse({
        "jsonrpc": "2.0",
        "result": task,
        "id": request_id
    })
```

## MCP + A2A 조합 패턴

실제 멀티 에이전트 시스템에서는 MCP와 A2A를 함께 사용합니다.

```
┌───────────────────────────────────────────────────────────────┐
│                         사용자                                │
│                  "주간 매출 보고서 작성해줘"                   │
└───────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌───────────────────────────────────────────────────────────────┐
│                   Orchestrator Agent                          │
│                                                               │
│   1. 작업 분해: 데이터 조회 → 분석 → 보고서 작성               │
│   2. 각 전문 에이전트에 A2A로 위임                            │
└───────────────────────────────────────────────────────────────┘
          │                    │                    │
         A2A                  A2A                  A2A
          │                    │                    │
          ▼                    ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  Data Agent     │  │ Analysis Agent  │  │  Writer Agent   │
│                 │  │                 │  │                 │
│  MCP: DB 조회   │  │  MCP: Python    │  │  MCP: 문서 생성 │
│        API 호출 │  │       통계 도구 │  │        템플릿   │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

**패턴별 역할**:
- **MCP**: 각 에이전트가 자신의 도구를 사용
- **A2A**: 에이전트 간 작업 위임 및 결과 전달

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 전문 에이전트로 역할 분리 | 네트워크 오버헤드 증가 |
| 벤더 중립적 오픈 표준 | 아직 초기 단계 (v0.3) |
| Linux Foundation 거버넌스 | 복잡도 증가 |
| MCP와 상호보완적 | 보안 고려사항 증가 |
| 확장성과 격리 보장 | 디버깅 어려움 |

## 보안 고려사항

### 주요 보안 위협

| 위협 | 설명 | 대응 방안 |
|------|------|----------|
| **Agent Spoofing** | 가짜 에이전트가 신뢰된 척 | Agent Card 서명 검증 |
| **Task Injection** | 악의적 Task로 에이전트 조작 | 입력 검증, 샌드박싱 |
| **Data Leakage** | 에이전트 간 민감 데이터 유출 | 암호화, 접근 제어 |
| **Unauthorized Access** | 권한 없는 에이전트 접근 | OAuth 2.0, mTLS |

### 권장 보안 설정

```json
{
  "authentication": {
    "schemes": ["oauth2"],
    "oauth2": {
      "flows": {
        "clientCredentials": {
          "tokenUrl": "https://auth.example.com/token",
          "scopes": {
            "task:read": "Task 조회",
            "task:write": "Task 생성"
          }
        }
      }
    }
  }
}
```

## 생태계 현황 (2025년)

| 지표 | 수치 |
|------|------|
| 프로토콜 버전 | v0.3 (2025년 업그레이드) |
| 거버넌스 | Linux Foundation |
| 초기 파트너 | 50+ 기술 기업 |
| 주요 채택 기업 | Google, IBM, Microsoft, Salesforce |
| 라이선스 | Apache 2.0 |

### 관련 프로토콜 비교

| 프로토콜 | 제공자 | 용도 | 관계 |
|----------|--------|------|------|
| **MCP** | Anthropic | 에이전트 ↔ 도구 | 보완 |
| **A2A** | Google → Linux Foundation | 에이전트 ↔ 에이전트 | 본 문서 |
| **ACP** | IBM | 에이전트 통신 | 대안/보완 |

### 향후 전망

- **2026년**: 멀티 에이전트 시스템 프로덕션 배포 본격화
- **2028년**: 33% 엔터프라이즈 소프트웨어가 Agentic AI 포함 (Gartner)
- **표준화**: MCP + A2A 조합이 사실상 업계 표준화

## 면접 예상 질문

### Q1: MCP와 A2A의 차이점을 설명해주세요.

A: **통신 대상이 다릅니다.**

- **MCP (Model Context Protocol)**: 에이전트와 **도구** 간의 통신입니다. 에이전트가 DB, API, 파일시스템 등 외부 도구를 사용할 때 MCP를 씁니다.
- **A2A (Agent-to-Agent)**: 에이전트와 **에이전트** 간의 통신입니다. 여러 전문 에이전트가 협업할 때 A2A를 씁니다.

**비유하면**, MCP는 "직원이 도구를 사용하는 방법"이고, A2A는 "회사 간 업무 협업 프로토콜"입니다.

**왜 둘 다 필요하냐면**, 복잡한 작업은 단일 에이전트로 해결하기 어렵기 때문입니다. 법률 검토는 법률 전문 에이전트에게, 데이터 분석은 분석 전문 에이전트에게 위임하는 것이 효율적입니다.

### Q2: Agent Card란 무엇인가요?

A: Agent Card는 에이전트가 자신의 **능력을 광고하는 JSON 메타데이터**입니다.

`/.well-known/agent.json` 경로에 위치하며, 다음 정보를 포함합니다:
- **name**: 에이전트 이름
- **skills**: 수행 가능한 작업 목록
- **authentication**: 지원하는 인증 방식
- **capabilities**: 스트리밍, 푸시 알림 등 지원 기능

**왜 필요하냐면**, Client Agent가 어떤 에이전트에게 작업을 위임할지 결정하려면 각 에이전트가 무엇을 할 수 있는지 알아야 하기 때문입니다. 마치 회사 홈페이지에서 제공 서비스를 확인하는 것과 같습니다.

### Q3: A2A의 Task 생명주기를 설명해주세요.

A: Task는 **submitted → working → completed** 순으로 진행됩니다.

1. **submitted**: Client가 Task를 제출
2. **working**: Remote Agent가 작업 중
3. **input-required**: 추가 입력이 필요한 경우 (선택적)
4. **completed/failed/canceled**: 최종 상태

**왜 이런 생명주기가 필요하냐면**, 에이전트 간 작업은 시간이 오래 걸릴 수 있기 때문입니다. Client는 상태를 폴링하거나 Push Notification을 받아 진행 상황을 추적할 수 있습니다.

### Q4: 멀티 에이전트 시스템에서 MCP와 A2A를 어떻게 조합하나요?

A: **A2A로 작업을 위임하고, 각 에이전트는 MCP로 도구를 사용**합니다.

예를 들어 "주간 매출 보고서 작성"을 요청하면:
1. **Orchestrator Agent**가 작업을 분해
2. **Data Agent**에게 A2A로 "매출 데이터 조회" 위임 → Data Agent는 MCP로 DB 조회
3. **Analysis Agent**에게 A2A로 "데이터 분석" 위임 → Analysis Agent는 MCP로 Python 통계 도구 사용
4. **Writer Agent**에게 A2A로 "보고서 작성" 위임 → Writer Agent는 MCP로 문서 템플릿 사용

**왜냐하면** 각 에이전트는 자신의 전문 도구(MCP)가 있고, 에이전트 간 협업(A2A)을 통해 복잡한 작업을 분담할 수 있기 때문입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [MCP (Model Context Protocol)](./mcp.md) | 에이전트↔도구 통신, 선수 지식 | [2] 입문 |
| [AI Agent](./ai-agent.md) | AI 에이전트 기본 개념, 선수 지식 | [1] 정의 |
| [Multi-Agent Systems](./multi-agent-systems.md) | 멀티 에이전트 아키텍처 | [3] 중급 |
| [Tool Use](./tool-use.md) | 도구 사용 패턴 | [2] 입문 |

## 참고 자료

- [Google Developers Blog - Announcing the Agent2Agent Protocol](https://developers.googleblog.com/en/a2a-a-new-era-of-agent-interoperability/)
- [A2A Protocol Official Documentation](https://a2a-protocol.org/latest/)
- [A2A Specification (DRAFT v1.0)](https://a2a-protocol.org/latest/specification/)
- [GitHub - a2aproject/A2A](https://github.com/a2aproject/A2A)
- [IBM - What Is Agent2Agent Protocol](https://www.ibm.com/think/topics/agent2agent-protocol)
- [Linux Foundation - A2A Protocol Project](https://www.linuxfoundation.org/press/linux-foundation-launches-the-agent2agent-protocol-project-to-enable-secure-intelligent-communication-between-ai-agents)
