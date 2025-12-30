# Vibe Coding

> `[3] 중급` · 선수 지식: [AI Agent란](./ai-agent.md), [LLM 기초](./llm.md)

> `Trend` 2025

> 개발자가 코드를 직접 작성하기보다 AI에게 의도를 전달하고 생성된 코드를 검토하는 AI 협업 코딩 패러다임

`#VibeCoding` `#바이브코딩` `#AI코딩` `#AICoding` `#AIAssisted` `#CopilotCoding` `#GitHubCopilot` `#Cursor` `#ClaudeCode` `#Windsurf` `#Cody` `#Tabnine` `#AgenticCoding` `#에이전틱코딩` `#자율코딩` `#AutonomousCoding` `#MultiAgent` `#멀티에이전트` `#Devin` `#ManusAI` `#PrompEngineering` `#CodeReview` `#AI페어프로그래밍` `#PairProgramming` `#HumanInTheLoop` `#AndrejKarpathy` `#코드생성` `#CodeGeneration`

## 왜 알아야 하는가?

- **실무**: 2025년 개발자의 82%가 AI 코딩 도구를 사용하며, 전체 코드의 41%가 AI 생성/지원으로 작성됨
- **면접**: "AI 도구를 어떻게 활용하시나요?"가 필수 면접 질문으로 자리잡음
- **기반 지식**: AI 에이전트, 프롬프트 엔지니어링, 코드 리뷰 역량과 직결

## 핵심 개념

- **의도 전달 (Intent Communication)**: 코드가 아닌 목표를 AI에게 설명
- **생성-검토 루프 (Generate-Review Loop)**: AI가 생성, 개발자가 검토
- **컨텍스트 관리 (Context Management)**: AI에게 적절한 맥락 제공
- **Human-in-the-Loop**: 최종 판단과 책임은 개발자에게

## 쉽게 이해하기

**요리사와 AI 어시스턴트 비유**

```
┌─────────────────────────────────────────────────────────────────┐
│                      기존 코딩 방식                              │
│                                                                  │
│  개발자(요리사): 직접 재료 손질, 조리, 플레이팅 모두 수행        │
│                                                                  │
│  [재료 준비] → [조리] → [플레이팅] → [완성]                     │
│       ↑          ↑         ↑                                    │
│    모두 직접 작성 (100% 수작업)                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                      Vibe Coding 방식                            │
│                                                                  │
│  개발자(요리사): "오늘의 메뉴 컨셉과 맛의 방향을 지시"           │
│  AI(수셰프): 재료 손질, 기본 조리 수행                           │
│  개발자: 맛보고 방향 수정, 최종 플레이팅                         │
│                                                                  │
│  [의도 전달] → [AI 생성] → [개발자 검토] → [수정 지시] → [완성] │
│       ↑            ↑           ↑             ↑                   │
│   "이런 기능"    코드 생성    코드 리뷰    피드백 반영            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

> "The hottest new programming language is English"
> — Andrej Karpathy (2023)

## 상세 설명

### Vibe Coding의 정의

**Andrej Karpathy**가 2023년 처음 사용한 용어로, AI와 함께 코딩하는 새로운 방식을 의미합니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                    Vibe Coding의 핵심                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  "코드를 직접 쓰지 않고, AI에게 바이브(의도)를 전달"             │
│                                                                  │
│  1. 개발자가 원하는 것을 자연어로 설명                          │
│  2. AI가 코드를 생성                                            │
│  3. 개발자가 결과를 검토하고 피드백                             │
│  4. AI가 피드백을 반영하여 수정                                 │
│  5. 반복...                                                     │
│                                                                  │
│  핵심: "어떻게(How)" 보다 "무엇(What)"에 집중                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### AI 코딩 도구의 진화 단계

```
┌─────────────────────────────────────────────────────────────────┐
│                  AI 코딩 도구 진화 (2021-2025)                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  [1세대] 자동완성 (2021)                                        │
│  └─ GitHub Copilot 초기: 다음 줄 예측                          │
│  └─ Tabnine: 코드 자동완성                                      │
│                     │                                            │
│                     ▼                                            │
│  [2세대] 대화형 코딩 (2023)                                     │
│  └─ ChatGPT, Claude: 코드 설명 및 생성                          │
│  └─ Cursor: 에디터 내 AI 대화                                   │
│                     │                                            │
│                     ▼                                            │
│  [3세대] 에이전틱 코딩 (2024-2025)                              │
│  └─ Claude Code: 파일 탐색, 편집, 테스트 자동화                 │
│  └─ Windsurf: 전체 프로젝트 컨텍스트 이해                       │
│  └─ Devin: 자율적 코딩 에이전트                                 │
│                     │                                            │
│                     ▼                                            │
│  [4세대] 멀티 에이전트 (2025~)                                  │
│  └─ Manus AI: 프론트/백엔드/DB 전문 에이전트 협업               │
│  └─ 코드 생성 + 리뷰 + 테스트 + 문서화 에이전트 분리            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 2025년 AI 코딩 현황

| 지표 | 수치 | 출처 |
|------|------|------|
| AI 코딩 도구 사용률 | 82% (일간/주간) | Second Talent |
| AI 생성/지원 코드 비율 | 41% | NetCorp |
| 생산성 향상 | 30-75% 시간 절약 | GitHub |
| Copilot 사용자 주간 프로젝트 완료율 | +126% | GitHub |
| AI 생성 코드 보안 취약점 포함률 | 48% | Stanford |

### 주요 AI 코딩 도구 비교

| 도구 | 특징 | 에이전틱 수준 | 가격 |
|------|------|-------------|------|
| **GitHub Copilot** | 가장 널리 사용, VS Code 통합 | 중 | $10-19/월 |
| **Cursor** | 전체 코드베이스 컨텍스트 | 중-상 | $20/월 |
| **Claude Code** | CLI 기반, 파일 편집/테스트 자동화 | 상 | API 사용량 |
| **Windsurf** | 대규모 프로젝트 특화 | 상 | $15/월 |
| **Devin** | 완전 자율 코딩 에이전트 | 최상 | $500/월 |
| **Manus AI** | 멀티 에이전트 협업 | 최상 | 비공개 |

## 동작 원리

### Vibe Coding 워크플로우

```
┌─────────────────────────────────────────────────────────────────┐
│                  Vibe Coding 워크플로우                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. 의도 전달 (Prompt)                                          │
│     ┌──────────────────────────────────────────────────────┐   │
│     │ "사용자 인증 기능을 구현해줘.                          │   │
│     │  - JWT 토큰 기반                                      │   │
│     │  - 리프레시 토큰 지원                                 │   │
│     │  - Spring Security 사용"                              │   │
│     └──────────────────────────────────────────────────────┘   │
│                            │                                     │
│                            ▼                                     │
│  2. 컨텍스트 수집                                               │
│     ┌──────────────────────────────────────────────────────┐   │
│     │ AI가 관련 파일 탐색:                                   │   │
│     │ - 기존 Security 설정                                  │   │
│     │ - User 엔티티 구조                                    │   │
│     │ - 프로젝트 의존성 (build.gradle)                      │   │
│     └──────────────────────────────────────────────────────┘   │
│                            │                                     │
│                            ▼                                     │
│  3. 코드 생성                                                   │
│     ┌──────────────────────────────────────────────────────┐   │
│     │ - JwtTokenProvider.java                               │   │
│     │ - SecurityConfig.java 수정                            │   │
│     │ - AuthController.java                                 │   │
│     │ - RefreshTokenRepository.java                         │   │
│     └──────────────────────────────────────────────────────┘   │
│                            │                                     │
│                            ▼                                     │
│  4. 개발자 검토                                                 │
│     ┌──────────────────────────────────────────────────────┐   │
│     │ "리프레시 토큰 만료 시간이 너무 긴 것 같아.            │   │
│     │  7일 → 1일로 변경하고, Redis에 저장하도록 수정해줘"   │   │
│     └──────────────────────────────────────────────────────┘   │
│                            │                                     │
│                            ▼                                     │
│  5. 피드백 반영 (반복)                                          │
│                            │                                     │
│                            ▼                                     │
│  6. 테스트 & 완료                                               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 효과적인 Vibe Coding 패턴

```
┌─────────────────────────────────────────────────────────────────┐
│                 효과적인 프롬프트 패턴                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ❌ 나쁜 예                                                     │
│  "로그인 기능 만들어줘"                                         │
│                                                                  │
│  ✅ 좋은 예                                                     │
│  "Spring Boot 3.x에서 JWT 기반 로그인 기능을 구현해줘.          │
│                                                                  │
│   요구사항:                                                     │
│   1. Access Token (15분) + Refresh Token (7일) 구조             │
│   2. Refresh Token은 Redis에 저장                               │
│   3. 로그아웃 시 토큰 블랙리스트 처리                           │
│                                                                  │
│   기존 코드:                                                    │
│   - User 엔티티: src/main/java/.../User.java                    │
│   - 현재 Security 설정: .../SecurityConfig.java                 │
│                                                                  │
│   제약사항:                                                     │
│   - 기존 User 엔티티 구조 유지                                  │
│   - 테스트 코드 함께 작성"                                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### CRAP 프롬프트 프레임워크

```
┌─────────────────────────────────────────────────────────────────┐
│                  CRAP 프롬프트 프레임워크                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  C - Context (맥락)                                             │
│      "Spring Boot 3.2 프로젝트, Kotlin 사용, Gradle 빌드"       │
│                                                                  │
│  R - Role (역할)                                                │
│      "시니어 백엔드 개발자처럼 작성해줘"                        │
│                                                                  │
│  A - Action (행동)                                              │
│      "JWT 인증 필터를 구현해줘"                                 │
│                                                                  │
│  P - Parameters (제약/조건)                                     │
│      "테스트 코드 포함, 기존 UserDetails 인터페이스 사용"       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 예제 코드

### Claude Code를 활용한 Vibe Coding

```bash
# 터미널에서 Claude Code 실행
$ claude

# 의도 전달
You: 사용자 회원가입 API를 만들어줘.
     - 이메일 중복 검사
     - 비밀번호 암호화 (BCrypt)
     - 검증 실패 시 상세 에러 메시지
     - 테스트 코드 포함

# Claude가 파일 탐색 → 코드 생성 → 테스트 작성까지 자동 수행

Claude: 프로젝트 구조를 분석하고 있습니다...
        기존 User 엔티티와 Repository를 확인했습니다.

        다음 파일들을 생성/수정하겠습니다:
        1. SignUpRequest.java (DTO)
        2. AuthController.java (API)
        3. AuthService.java (비즈니스 로직)
        4. AuthServiceTest.java (테스트)

        진행할까요?
```

### Cursor에서의 Vibe Coding

```javascript
// Cursor에서 Cmd+K로 AI에게 요청

// 개발자 의도:
// "이 함수가 너무 길어. 가독성 좋게 리팩토링해줘.
//  에러 처리도 추가하고, 각 단계를 명확히 분리해줘."

// Before
async function processOrder(order) {
  const user = await db.users.findById(order.userId);
  if (!user) return null;
  const items = await db.items.findByIds(order.itemIds);
  let total = 0;
  for (const item of items) {
    total += item.price * item.quantity;
  }
  if (user.balance < total) return null;
  await db.users.updateBalance(user.id, user.balance - total);
  const receipt = await db.orders.create({ ...order, total, status: 'completed' });
  await sendEmail(user.email, receipt);
  return receipt;
}

// After (AI 생성)
async function processOrder(order) {
  const user = await validateUser(order.userId);
  const orderDetails = await calculateOrderTotal(order.itemIds);

  await verifyUserBalance(user, orderDetails.total);
  await deductUserBalance(user, orderDetails.total);

  const receipt = await createOrderReceipt(order, orderDetails.total);
  await notifyUser(user, receipt);

  return receipt;
}

async function validateUser(userId) {
  const user = await db.users.findById(userId);
  if (!user) {
    throw new NotFoundError(`User not found: ${userId}`);
  }
  return user;
}

async function calculateOrderTotal(itemIds) {
  const items = await db.items.findByIds(itemIds);
  const total = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  return { items, total };
}

// ... 나머지 함수들
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 개발 속도 30-75% 향상 | 보안 취약점 48% 포함 가능 |
| 보일러플레이트 코드 자동화 | 코드 이해도 저하 위험 |
| 새로운 기술 학습 가속화 | AI 환각(Hallucination) 발생 |
| 문서화/테스트 자동 생성 | 디버깅 어려움 (AI 생성 코드) |
| 반복 작업 감소 | 비용 (API 사용량, 구독료) |
| 주니어 개발자 생산성 향상 | 기초 역량 약화 우려 |

### Vibe Coding의 주의사항

```
┌─────────────────────────────────────────────────────────────────┐
│                    Vibe Coding 주의사항                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ⚠️ 반드시 검토해야 할 것                                       │
│                                                                  │
│  1. 보안                                                        │
│     └─ SQL Injection, XSS 등 OWASP Top 10 취약점 확인           │
│     └─ 비밀키, 토큰 등 민감 정보 노출 여부                      │
│                                                                  │
│  2. 비즈니스 로직                                               │
│     └─ AI가 요구사항을 정확히 이해했는지 확인                   │
│     └─ 엣지 케이스 처리 여부                                    │
│                                                                  │
│  3. 성능                                                        │
│     └─ N+1 쿼리, 불필요한 반복 등                               │
│     └─ 메모리 누수 가능성                                       │
│                                                                  │
│  4. 의존성                                                      │
│     └─ 사용된 라이브러리 버전 호환성                            │
│     └─ 라이선스 확인                                            │
│                                                                  │
│  ✅ AI가 잘하는 것                                              │
│     └─ 보일러플레이트 코드                                      │
│     └─ 문서화, 테스트 코드                                      │
│     └─ 리팩토링 제안                                            │
│     └─ 일반적인 패턴 구현                                       │
│                                                                  │
│  ❌ AI가 못하는 것                                              │
│     └─ 도메인 특화 비즈니스 로직                                │
│     └─ 복잡한 시스템 간 상호작용                                │
│     └─ 성능 최적화 (프로파일링 필요)                            │
│     └─ 레거시 시스템 이해                                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 면접 예상 질문

### Q: Vibe Coding을 실무에서 어떻게 활용하고 계신가요?

A: 주로 **보일러플레이트 코드 생성**과 **테스트 코드 작성**에 활용합니다. 예를 들어 새로운 API 엔드포인트를 추가할 때 DTO, Controller, Service 기본 구조를 AI가 생성하고, 저는 비즈니스 로직에 집중합니다. 단, AI가 생성한 코드는 반드시 **보안 취약점 검토**와 **비즈니스 요구사항 일치 여부**를 확인합니다. 특히 SQL 쿼리나 인증 관련 코드는 더 꼼꼼히 검토합니다.

### Q: AI 코딩 도구의 한계는 무엇인가요?

A: 크게 세 가지입니다.

1. **보안 취약점**: 연구에 따르면 AI 생성 코드의 48%가 잠재적 보안 취약점을 포함합니다. 입력 검증, SQL 인젝션 등을 항상 확인해야 합니다.

2. **환각(Hallucination)**: 존재하지 않는 API나 잘못된 문법을 생성할 수 있습니다. 특히 최신 라이브러리나 마이너한 기술에서 자주 발생합니다.

3. **컨텍스트 제한**: 대규모 프로젝트의 전체 맥락을 이해하기 어렵습니다. 레거시 시스템이나 복잡한 도메인 로직에서 한계가 있습니다.

### Q: AI 코딩 도구 사용이 개발자의 성장에 방해가 되지 않나요?

A: **사용 방식에 따라 다릅니다**.

나쁜 사용: AI가 생성한 코드를 이해 없이 복사-붙여넣기하면 기초 역량이 약해집니다.

좋은 사용: AI를 **학습 도구**로 활용하면 오히려 성장이 빨라집니다. AI가 생성한 코드를 읽고 "왜 이렇게 작성했는지" 분석하면 새로운 패턴과 기술을 빠르게 배울 수 있습니다. 또한 AI가 처리하는 반복 작업에서 벗어나 **설계, 아키텍처, 문제 해결**에 더 집중할 수 있습니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [AI Agent란](./ai-agent.md) | 선수 지식 - 에이전트 기본 개념 | [1] 정의/기초 |
| [LLM 기초](./llm.md) | 선수 지식 - 언어 모델 이해 | [1] 정의/기초 |
| [MCP](./mcp.md) | 관련 개념 - 외부 도구 연결 | [2] 입문 |
| [Claude Code Skill](./claude-code-skill.md) | 관련 개념 - AI 기능 확장 | [3] 중급 |
| [Claude Code Sub Agent](./claude-code-sub-agent.md) | 심화 - 자율 에이전트 | [4] 심화 |

## 참고 자료

- [AI Coding Assistant Statistics & Trends 2025](https://www.secondtalent.com/resources/ai-coding-assistant-statistics/)
- [AI Engineering Trends in 2025: Agents, MCP and Vibe Coding](https://thenewstack.io/ai-engineering-trends-in-2025-agents-mcp-and-vibe-coding/)
- [AI Coding Trends: Developer Tools To Watch in 2025](https://thenewstack.io/ai-powered-coding-developer-tool-trends-to-monitor-in-2025/)
- [AI coding is now everywhere - MIT Technology Review](https://www.technologyreview.com/2025/12/15/1128352/rise-of-ai-coding-developers-2026/)
- [AI-Generated Code Statistics 2025](https://www.netcorpsoftwaredevelopment.com/blog/ai-generated-code-statistics)
