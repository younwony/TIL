# Tool Use (도구 사용)

> `[2] 입문` · 선수 지식: [AI Agent란](./ai-agent.md)

> LLM이 외부 도구(함수)를 호출하여 실제 작업을 수행하는 능력

`#ToolUse` `#도구사용` `#FunctionCalling` `#함수호출` `#LLM` `#AI에이전트` `#AIAgent` `#Claude` `#GPT` `#API` `#JSON` `#JSONSchema` `#Tool` `#Function` `#Parameter` `#파라미터` `#InputSchema` `#OutputSchema` `#ToolResult` `#도구결과` `#Automation` `#자동화` `#Agentic` `#에이전틱` `#ReAct` `#ActionSpace`

## 왜 알아야 하는가?

LLM은 기본적으로 텍스트 생성만 가능합니다. Tool Use를 통해 LLM이 외부 세계와 상호작용할 수 있게 되어, 파일 읽기/쓰기, API 호출, 데이터베이스 조회 등 실제 작업을 수행할 수 있습니다. 이것이 AI 에이전트의 핵심 능력입니다.

## 핵심 개념

- **Tool**: LLM이 호출할 수 있는 함수/기능
- **Tool Definition**: 도구의 이름, 설명, 입력 스키마 정의
- **Tool Call**: LLM이 도구 사용을 요청하는 것
- **Tool Result**: 도구 실행 결과를 LLM에 반환

## 쉽게 이해하기

**Tool Use**를 비서의 업무 위임에 비유할 수 있습니다.

```
LLM = 뇌 (생각만 함, 손 없음)
Tool = 손 (실제 행동 수행)

┌─────────────────────────────────────────────────────┐
│                    Tool Use 흐름                     │
├─────────────────────────────────────────────────────┤
│                                                      │
│  사용자: "오늘 날씨 어때?"                             │
│            ↓                                         │
│  ┌────────────────────┐                              │
│  │    LLM (뇌)        │  "날씨를 알려면                │
│  │   - 생각/추론      │   weather_api를 호출해야겠다"  │
│  └────────────────────┘                              │
│            ↓ Tool Call                               │
│  ┌────────────────────┐                              │
│  │  Tool: weather_api │  → 실제 API 호출             │
│  │   (손)             │                              │
│  └────────────────────┘                              │
│            ↓ Tool Result                             │
│  ┌────────────────────┐                              │
│  │    LLM (뇌)        │  "맑음, 25도입니다"           │
│  │   - 결과 해석      │   (자연어로 변환)             │
│  └────────────────────┘                              │
│            ↓                                         │
│  사용자: "오늘은 맑고 25도예요!"                       │
│                                                      │
└─────────────────────────────────────────────────────┘
```

## 상세 설명

### Tool Definition (도구 정의)

```json
{
  "name": "get_weather",
  "description": "지정된 도시의 현재 날씨 정보를 조회합니다",
  "input_schema": {
    "type": "object",
    "properties": {
      "city": {
        "type": "string",
        "description": "날씨를 조회할 도시 이름"
      },
      "unit": {
        "type": "string",
        "enum": ["celsius", "fahrenheit"],
        "description": "온도 단위"
      }
    },
    "required": ["city"]
  }
}
```

**구성 요소**:
| 필드 | 설명 | 예시 |
|------|------|------|
| name | 도구 고유 식별자 | "get_weather" |
| description | 도구의 용도 설명 (LLM이 사용 판단) | "날씨 조회" |
| input_schema | JSON Schema로 입력 파라미터 정의 | { "city": string } |

### Tool Call 흐름

```
┌─────────────────────────────────────────────────────────────┐
│                    Tool Call 전체 흐름                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. 요청 (User → LLM)                                        │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ messages: [{ role: "user", content: "서울 날씨 알려줘" }] │ │
│  │ tools: [get_weather, search_web, ...]                   │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ↓                                  │
│  2. LLM 응답 (stop_reason: "tool_use")                       │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ content: [{                                             │ │
│  │   type: "tool_use",                                     │ │
│  │   id: "call_123",                                       │ │
│  │   name: "get_weather",                                  │ │
│  │   input: { city: "서울", unit: "celsius" }              │ │
│  │ }]                                                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ↓                                  │
│  3. 도구 실행 (개발자 코드)                                   │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ const result = await weatherAPI.get("서울");            │ │
│  │ // { temp: 25, condition: "맑음" }                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ↓                                  │
│  4. 결과 반환 (Tool Result → LLM)                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ messages: [...이전 메시지,                               │ │
│  │   { role: "user", content: [{                           │ │
│  │     type: "tool_result",                                │ │
│  │     tool_use_id: "call_123",                            │ │
│  │     content: "{ \"temp\": 25, \"condition\": \"맑음\" }"│ │
│  │   }]}                                                   │ │
│  │ ]                                                       │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ↓                                  │
│  5. 최종 응답 (LLM → User)                                   │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ "서울 날씨는 맑고 기온은 25도입니다."                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 다중 Tool 호출

LLM은 한 번에 여러 도구를 호출할 수 있습니다:

```
사용자: "서울과 도쿄 날씨 비교해줘"

LLM 응답 (병렬 호출):
┌─────────────────────────────────────────┐
│ content: [                               │
│   { type: "tool_use",                    │
│     id: "call_1",                        │
│     name: "get_weather",                 │
│     input: { city: "서울" } },           │
│   { type: "tool_use",                    │
│     id: "call_2",                        │
│     name: "get_weather",                 │
│     input: { city: "도쿄" } }            │
│ ]                                        │
└─────────────────────────────────────────┘
           ↓ 병렬 실행
┌───────────────┐  ┌───────────────┐
│ 서울: 25도 맑음 │  │ 도쿄: 28도 흐림 │
└───────────────┘  └───────────────┘
           ↓ 결과 취합
LLM: "서울은 25도로 맑고, 도쿄는 28도로 흐립니다."
```

### Tool Description 작성 요령

**좋은 예**:
```json
{
  "name": "search_database",
  "description": "사용자 데이터베이스에서 조건에 맞는 레코드를 검색합니다. 이름, 이메일, 가입일 기준으로 필터링 가능. 최대 100개 결과 반환.",
  "input_schema": {
    "properties": {
      "filter": {
        "type": "object",
        "description": "검색 필터 조건. 예: { \"name\": \"김철수\" }"
      },
      "limit": {
        "type": "integer",
        "description": "반환할 최대 결과 수 (기본: 10, 최대: 100)"
      }
    }
  }
}
```

**나쁜 예**:
```json
{
  "name": "search",
  "description": "검색함",
  "input_schema": {
    "properties": {
      "q": { "type": "string" }
    }
  }
}
```

**작성 원칙**:
1. **구체적 설명**: 도구가 무엇을 하는지 명확히
2. **제약 조건 명시**: 최대값, 필수 조건 등
3. **예시 포함**: 입력 예시로 이해도 향상
4. **의미있는 이름**: 약어 대신 명확한 이름

### 실제 구현 예시

```typescript
import Anthropic from '@anthropic-ai/sdk';

const client = new Anthropic();

// 1. 도구 정의
const tools = [
  {
    name: "calculate",
    description: "수학 계산을 수행합니다",
    input_schema: {
      type: "object",
      properties: {
        expression: {
          type: "string",
          description: "계산할 수식. 예: '2 + 3 * 4'"
        }
      },
      required: ["expression"]
    }
  },
  {
    name: "get_current_time",
    description: "현재 시각을 반환합니다",
    input_schema: {
      type: "object",
      properties: {
        timezone: {
          type: "string",
          description: "타임존. 예: 'Asia/Seoul'"
        }
      }
    }
  }
];

// 2. 도구 실행 핸들러
function executeTool(name: string, input: any): string {
  switch (name) {
    case "calculate":
      try {
        // 주의: 실제로는 안전한 수식 파서 사용
        const result = eval(input.expression);
        return String(result);
      } catch {
        return "계산 오류";
      }
    case "get_current_time":
      return new Date().toLocaleString('ko-KR', {
        timeZone: input.timezone || 'Asia/Seoul'
      });
    default:
      return "알 수 없는 도구";
  }
}

// 3. 에이전트 실행
async function runWithTools(userMessage: string) {
  const messages: any[] = [
    { role: "user", content: userMessage }
  ];

  while (true) {
    const response = await client.messages.create({
      model: "claude-sonnet-4-20250514",
      max_tokens: 1024,
      tools,
      messages
    });

    // 최종 응답
    if (response.stop_reason === "end_turn") {
      const text = response.content.find(b => b.type === "text");
      return text?.text;
    }

    // 도구 호출 처리
    if (response.stop_reason === "tool_use") {
      messages.push({ role: "assistant", content: response.content });

      const toolResults = [];
      for (const block of response.content) {
        if (block.type === "tool_use") {
          const result = executeTool(block.name, block.input);
          toolResults.push({
            type: "tool_result",
            tool_use_id: block.id,
            content: result
          });
        }
      }

      messages.push({ role: "user", content: toolResults });
    }
  }
}

// 사용
const answer = await runWithTools("2 + 3 * 4 계산하고 현재 시간도 알려줘");
console.log(answer);
// "2 + 3 * 4의 결과는 14이고, 현재 서울 시각은 2025년 1월 15일 오후 3시 42분입니다."
```

## 트레이드오프

| 관점 | 장점 | 단점 |
|------|------|------|
| 기능 | 실제 작업 수행 가능 | 보안 위험 (권한 관리 필요) |
| 비용 | 효율적 작업 완료 | 도구 호출당 추가 토큰 |
| 복잡도 | 유연한 확장 | 도구 정의/핸들러 구현 필요 |

## 면접 예상 질문

### Q: Tool Use와 일반 API 호출의 차이는?

A: **일반 API 호출**: 개발자가 직접 어떤 API를 언제 호출할지 결정. **Tool Use**: LLM이 상황을 판단하여 필요한 도구를 선택하고 파라미터를 결정. **장점**: 자연어로 복잡한 작업 요청 가능, LLM이 맥락에 맞게 도구 조합 사용. **단점**: LLM 판단 오류 가능, 토큰 비용 증가.

### Q: Tool Description을 잘 작성해야 하는 이유는?

A: LLM은 description을 보고 도구 사용 여부와 방법을 결정합니다. (1) **모호한 설명**: LLM이 잘못된 도구 선택 또는 잘못된 파라미터 전달. (2) **좋은 설명**: 용도, 제약조건, 예시 포함 → 정확한 도구 사용. (3) **실무 팁**: 도구 테스트 시 description 수정으로 성능 개선 가능.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [AI Agent란](./ai-agent.md) | 선수 지식 | [1] 기초 |
| [Agent SDK](./agent-sdk.md) | 고급 구현 | [5] 심화 |
| [MCP](./mcp.md) | 외부 도구 연결 | [2] 입문 |

## 참고 자료

- [Anthropic Tool Use Guide](https://docs.anthropic.com/en/docs/build-with-claude/tool-use/overview)
- [OpenAI Function Calling](https://platform.openai.com/docs/guides/function-calling)
- [JSON Schema](https://json-schema.org/)
