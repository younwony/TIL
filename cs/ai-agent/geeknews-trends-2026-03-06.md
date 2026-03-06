# GeekNews 주요 트렌드 정리 (2026-03-06)

> 출처: https://news.hada.io/

## 핵심 요약

2026년 3월 기준 GeekNews의 주요 화두는 **AI 에이전트 기반 개발 워크플로우의 본격화**다. 코딩 에이전트(Claude Code, Codex 등)가 일상 도구로 자리잡으면서, 에이전트와 협업하는 엔지니어링 패턴, 프로젝트 관리 자동화, CLI 기반 AI 통합 도구들이 빠르게 등장하고 있다.

---

## 1. 에이전틱 엔지니어링 패턴 (Agentic Engineering Patterns)

- **출처:** [simonwillison.net](https://simonwillison.net/guides/agentic-engineering-patterns/)
- **핵심:** Claude Code, Codex 같은 코딩 에이전트 시대의 개발 방식을 정리한 가이드

### 주요 패턴

| 영역 | 패턴 | 설명 |
|------|------|------|
| 원칙 | Writing code is cheap now | 코드 작성 비용이 거의 무료. 아이디어를 바로 코드로 시험 가능 |
| 원칙 | Hoard things you know how to do | 코드 실험/예제를 축적하여 에이전트 입력 자료로 재활용 |
| 테스트 | Red/Green TDD | 테스트 우선 개발이 에이전트와 가장 잘 맞는 패턴 |
| 테스트 | First run the tests | 에이전트 작업 시 자동화 테스트는 필수 |
| 이해 | Linear walkthroughs | 에이전트가 만든 코드를 순서대로 읽으며 학습 |
| 이해 | Interactive explanations | 에이전트와 대화하며 코드 구조 파악 |

### 안티패턴

- 에이전트가 생성한 코드를 리뷰 없이 PR 제출
- 에이전트가 작성한 PR 설명을 검증 없이 사용
- 테스트/검증 과정 없이 코드 공유

### 커뮤니티 인사이트

- **코드 리뷰 병목 문제:** 에이전트가 코드 생성 속도를 높이면 리뷰가 병목이 됨. 정적/동적 분석 자동화 투자가 필요
- **거부 로그(rejections.md):** "왜 이 접근을 버렸는가"를 기록하면 AI가 같은 실수를 반복하지 않음
- **자기충족적 테스트 경계:** LLM이 만드는 테스트가 실제로는 아무것도 검증하지 않는 경우 존재. Mutation testing으로 검증 필요
- **코드베이스 품질 = 에이전트 성능:** 강한 타입과 테스트가 있는 프로젝트에서 에이전트 성능이 극적으로 높아짐

### 활용 포인트

> 현재 TIL 프로젝트의 CLAUDE.md에서 이미 적용 중인 패턴들이 많다. 특히 `rejections.md` 패턴과 mutation testing은 추가로 도입할 가치가 있다.

---

## 2. OpenAI Symphony - 에이전트 기반 프로젝트 관리 자동화

- **출처:** [github.com/openai/symphony](https://github.com/openai/symphony)
- **핵심:** Linear 보드를 모니터링하여 작업을 자동으로 에이전트에게 할당하고, PR 생성·병합까지 자동화

### 아키텍처

```
Linear 보드 → Symphony 모니터링 → 에이전트 자동 생성/할당
                                    ↓
                              코드 구현 (격리된 run)
                                    ↓
                         작업 증거(CI, PR 리뷰, 영상) 제출
                                    ↓
                              승인 시 자동 PR 병합
```

### 특징

- SPEC.md 기반으로 자기 조직에 맞게 구현 가능한 오픈소스 방식
- Harness engineering을 채택한 코드베이스에서 최적 성능
- Elixir 기반 참조 구현체 제공
- Apache License 2.0

### 활용 포인트

> 현재 `/work-plan-start` 워크플로우와 유사한 접근. SPEC.md 기반 에이전트 디스패치 패턴을 참고하여 팀 워크플로우를 개선할 수 있다.

---

## 3. GPT-5.4 공개

- **출처:** [openai.com](https://openai.com/index/introducing-gpt-5-4/)
- **핵심:** ChatGPT, API, Codex 전반에 적용되는 최신 프런티어 모델

### 주요 개선점

| 항목 | 내용 |
|------|------|
| 컨텍스트 윈도우 | 최대 1M 토큰 |
| 컴퓨터 사용 | 네이티브 computer-use 기능 내장 (OSWorld 75.0%, 인간 72.4%) |
| 코딩 | SWE-Bench Pro 57.7% (GPT-5.3-Codex 56.8%) |
| Tool Search | 도구 정의를 동적 조회하여 토큰 사용량 47% 감소 |
| 환각 감소 | 개별 주장 허위 가능성 33% 감소 |
| 가격 | 입력 $2.50/M, 출력 $15/M (272K 초과 시 2배) |

### Tool Search 기능

- 기존: 모든 도구 정의를 프롬프트에 사전 포함 (수천~수만 토큰 소비)
- 개선: 경량 도구 목록만 제공하고 필요 시 정의를 동적 조회
- 36개 MCP 서버 기준 총 토큰 사용량 47% 감소, 정확도 동일

### 활용 포인트

> Tool Search 패턴은 MCP 서버가 많은 환경에서 토큰 효율성을 크게 높일 수 있다. 현재 Claude Code 설정에도 유사한 개념(deferred tools)이 이미 적용되어 있다.

---

## 4. gws - 구글 워크스페이스 CLI

- **출처:** [github.com/googleworkspace/cli](https://github.com/googleworkspace/cli)
- **핵심:** Google Workspace API 전체를 단일 CLI로 제어

### 주요 기능

- Drive, Gmail, Calendar, Sheets, Docs, Chat, Admin 등 지원
- Google Discovery Service 기반 동적 명령 생성 (새 API 추가 시 자동 반영)
- AI 에이전트용 스킬 100개 이상 포함
- MCP 서버 모드 지원 → Claude Desktop, VS Code 등에서 도구로 사용 가능
- Rust 기반 (99.5%)

### MCP 서버 활용 예시

```bash
# Drive, Gmail, Calendar를 MCP 도구로 노출
gws mcp -s drive,gmail,calendar
```

### 활용 포인트

> Claude Code의 MCP 서버로 연결하면 Google Workspace 자동화가 가능해진다. 특히 Gmail, Calendar, Drive 연동으로 업무 자동화 워크플로우를 구성할 수 있다.

---

## 5. mogcli - Microsoft 365용 에이전트 친화적 CLI

- **출처:** [github.com/jaredpalmer/mogcli](https://github.com/jaredpalmer/mogcli)
- **핵심:** Microsoft Graph API 기반 개인/기업용 계정 지원 CLI

### 주요 기능

- Mail, Calendar, Contacts, Groups, Tasks, OneDrive 등 주요 워크로드 제어
- 위임 사용자 인증과 앱 전용 인증(App-only) 지원
- `--json`, `--plain` 출력 모드로 자동화 스크립트 통합 용이
- `--dry-run`으로 쓰기 작업 미리보기 가능
- 다중 프로필 관리 (개인/기업용 계정 전환)
- 토큰과 비밀키는 OS Keychain/Keyring에 안전하게 저장
- MIT License, Go로 개발

### 명령 예시

```bash
mog mail list          # 메일 목록 조회
mog calendar list      # 캘린더 이벤트 조회
mog onedrive put file  # OneDrive 파일 업로드
```

### 활용 포인트

> Google Workspace 대신 Microsoft 365를 쓰는 환경에서 동일한 AI 에이전트 통합을 구현할 수 있다.

---

## 6. Anthropic Courses - 무료 온라인 강의

- **출처:** [anthropic.skilljar.com](https://anthropic.skilljar.com/)
- **핵심:** Claude 기본 사용법부터 API 활용, Claude Code 개발 워크플로, MCP 서버 구축, Agent Skills까지 포함

### 개발자 대상 과정

| 과정 | 내용 |
|------|------|
| Claude Code in Action | Claude Code를 개발 워크플로에 통합하는 실습 |
| Building with the Claude API | Anthropic 모델을 Claude API로 활용하는 전 과정 |
| Introduction to MCP | Python으로 MCP 서버/클라이언트 구축 (tools, resources, prompts) |
| MCP: Advanced Topics | 샘플링, 알림, 파일 시스템 접근, 트랜스포트 등 프로덕션 MCP 개발 |
| Introduction to Agent Skills | Claude Code에서 재사용 가능한 마크다운 기반 스킬 생성/구성/공유 |

### 일반 사용자 및 특화 과정

- **Claude 101:** 일상 업무에 Claude를 활용하는 방법
- **AI Fluency:** AI 시스템과 효과적/윤리적/안전하게 협업하는 프레임워크
- **AI Fluency for Educators/Students/Nonprofits:** 대상별 맞춤 AI 역량 과정
- **Claude with Amazon Bedrock / Google Cloud Vertex AI:** 클라우드 플랫폼별 통합 과정

### 활용 포인트

> Claude Code와 MCP를 실무에 활용하는 공식 가이드. 특히 Agent Skills 과정은 현재 TIL 프로젝트의 Skills 시스템 개선에 참고할 수 있다. Skilljar LMS에서 Anthropic 계정 없이 무료로 수강 가능.

---

## 7. Vercel agent-browser - AI 에이전트용 브라우저 자동화 CLI

- **출처:** [agent-browser.dev](https://agent-browser.dev/)
- **핵심:** AI 에이전트를 위한 헤드리스 브라우저 자동화

### 주요 특징

- 기존 Node.js + Playwright + CDP 구조에서 **Rust 바이너리가 CDP를 직접 호출**하는 `--native` 모드 도입
- Node.js 프로세스 없이 독립실행형 데몬으로 동작 → 메모리 사용량 대폭 감소
- `snapshot` 명령으로 접근성 트리 기반 고유 ref(@e1, @e2) 생성, 이를 기반으로 동작

### AI 친화적 워크플로우 명령

| 명령 | 기능 |
|------|------|
| `open`, `goto` | 페이지 탐색 |
| `click`, `fill`, `type`, `hover` | 사용자 인터랙션 |
| `screenshot`, `pdf` | 스크린샷/PDF 생성 |
| `get text`, `get attr` | 상태 조회 |
| `diff` | 스냅샷/스크린샷/URL 비교 |
| `network route` | 네트워크 요청 가로채기 및 모킹 |

### 세션 관리

- `--session`으로 격리된 브라우저 인스턴스 실행
- `--profile` 또는 `--session-name`으로 로그인/스토리지 상태 유지
- `--annotate` 옵션으로 요소 번호가 표시된 주석 스크린샷 생성
- macOS, Linux, Windows 전용 Rust 바이너리 제공, Apache-2.0 라이선스

---

## 8. 그 외 주목할 내용

### 클로드 코드 가이드 (전자책)
- **출처:** [wikidocs.net/book/19104](https://wikidocs.net/book/19104)
- 퀵 레퍼런스부터 Claude Code 창시자의 노하우까지 정리

### tutor-skills - Claude Code로 공부하기
- **출처:** [github.com/RoundTable02/tutor-skills](https://github.com/RoundTable02/tutor-skills)
- Claude Code Skills로 2주 만에 AWS 자격증 취득한 사례
- PDF 교재 또는 코드베이스를 넣으면 AI가 Obsidian 노트 변환 → 연습 문제 생성 → 퀴즈로 약점 탐지 → 반복 드릴
- 메타인지 기반 학습: "내가 무엇을 모르는지" 파악에 초점
- 설치: `npx skills add RoundTable02/tutor-skills`

### Claude Code로 자율 AI 마케팅 팀 운영
- **출처:** [snow.runbear.io](https://snow.runbear.io/how-i-built-an-ai-marketing-team-with-claude-code-and-cowork-f3405a53ee22)
- 7명 규모 AI SaaS 스타트업 CEO가 Claude Code Agent Teams로 5인 AI 마케팅 팀 구축
- CMO, 콘텐츠 작가, 소셜미디어 담당, HN 매니저, 성과 분석가 구성
- 코드 한 줄 없이 마크다운 파일 15~20개 + Claude Code만으로 동작
- Mac Mini M1에서 cron으로 매시간 자동 실행, 블로그·SNS·성과 분석까지 자율 수행
- **핵심 교훈:**
  - 에이전트를 "프롬프트"가 아닌 "직원 채용"으로 접근 (직무 범위, 스타일 가이드, 도구 권한 정의)
  - `.claude/agents/` 마크다운 파일로 에이전트 정의, `.claude/rules/`로 팀 공통 정책
  - 피드백 루프 구성이 가장 중요 (코드가 아님)
  - 규칙은 복리로 누적 — 한 번 추가한 규칙이 모든 에이전트를 영구 개선
  - 3시간 타임슬롯 시스템이 핵심 (없으면 에이전트가 너무 많이/적게 하는 문제)

### AI가 주니어 개발자를 쓸모없게 만들고 있다
- **출처:** [beabetterdev.com](https://beabetterdev.com/2026/03/01/ai-is-making-junior-devs-useless/)
- AI 도구가 얕은 역량(shallow competence)만 만들어주는 문제
- 코드를 빠르게 출력하지만 "왜 그런 접근을 택했는지" 설명하지 못하는 상황 빈번
- **시니어의 진정한 가치:** 코드 작성 속도가 아니라 수년간 실패를 통해 축적한 "실패 패턴 인식"
- **5가지 전략:**
  1. 기초를 제대로 학습 (좋은 코드가 뭔지 알아야 AI 결과물을 평가 가능)
  2. 장애 사례(post-mortem) 공부 (Cloudflare, AWS, Azure 등)
  3. 의도적 고군분투 (AI에 붙여넣기 전에 먼저 스택 트레이스를 읽고 가설 세우기)
  4. 이해하지 못한 코드를 절대 출시하지 말 것
  5. 답이 아니라 "왜"를 프롬프트 (여러 접근법의 장단점 학습)
- **커뮤니티 논쟁:** "AI 없이 배우는 기간이 필수" vs "AI로 더 빠르게 시니어급 성장 가능"

### 단순함으로는 승진하지 못한다
- **출처:** [terriblesoftware.org](https://terriblesoftware.org/2026/03/03/nobody-gets-promoted-for-simplicity/)
- 복잡한 시스템을 만드는 사람이 더 인정받는 구조적 문제
- 50줄로 기능을 구현한 Engineer A vs 추상화 레이어·pub/sub·설정 프레임워크로 3주 걸린 Engineer B → B가 승진 서류를 채우기 쉬움
- **"피한 복잡성"으로는 승진 불가** — 불필요한 복잡성(Unearned Complexity) 구분이 핵심
- **엔지니어 실천 방안:**
  - "기능 X 구현" 대신 "세 가지 접근법 평가 후 가장 단순한 것 선택, 2일 만에 출시, 6개월 무장애 운영"으로 기술
  - 디자인 리뷰에서 "미래 대비" 압박에 굴복하지 말고 비용 분석 제시
- **리더 실천 방안:**
  - "가장 단순한 출시 가능 버전은 무엇이고, 복잡한 것이 필요한 구체적 신호는?" 으로 질문 변경
  - 코드를 삭제한 엔지니어, "아직 필요 없다"고 맞았던 엔지니어를 인정
- **AI 시대 연결:** AI가 5분 만에 복잡한 아키텍처를 생성하므로 문제가 더 악화. AGENTS.md에 "KISS", "YAGNI" 명시 필요

### 도널드 커누스, Claude Opus 4.6 논문 공개
- **출처:** [stanford.edu (PDF)](https://www-cs-faculty.stanford.edu/~knuth/papers/claude-cycles.pdf)
- TAOCP 저자가 Claude Opus 4.6이 미해결 조합론 문제(해밀토니안 사이클 분해)를 해결한 과정을 논문으로 발표
- Claude가 31번의 Python 스크립트 실행과 자체 피드백 루프를 통해 일반화된 알고리즘 구조 발견
- **핵심 분기점:** 25번째 탐색에서 "시뮬레이티드 어닐링은 개별 해를 찾을 수 있지만 일반적 구조를 제시할 수 없다"고 스스로 한계를 분석하고 순수 수학적 접근으로 전환
- 커누스가 결과를 **'클로드형 분해(Claude-like decompositions)'**로 명명
- 기존 AI에 회의적이었던 커누스가 "자동 연역과 창의적 문제 해결의 극적인 진전"으로 평가
- **에이전틱 워크플로우의 핵심:** `plan.md`에 매 탐색 후 즉시 기록하도록 강제하는 프롬프트 제약

### 모든 신규 창업자가 추적해야 할 세 가지 핵심 지표
- **출처:** [dearstage2.com](https://www.dearstage2.com/p/three-metrics-every-new-founder-should)
- 초기 스타트업은 매출 확대 전에 "비즈니스가 작동하는지" 검증이 우선
- **3가지 핵심 지표:**
  1. **리텐션 선행 지표(LIR):** 고객이 남을 가능성을 시사하는 관찰 가능한 행동 (예: Slack - 팀 2,000건 이상 메시지 발송)
  2. **가치 도달 시간(Time-to-Value):** 신규 고객이 LIR 지점에 도달하는 속도
  3. **고객 건강도 요약:** 주간 스프레드시트로 건강/정체/위험 고객 분류
- CAC, LTV, 총마진 등은 가치 창출과 리텐션 확보 후에 다룰 것

---

## 트렌드 종합

### 핵심 흐름

1. **에이전트 = 팀원:** 코딩 에이전트가 단순 도구에서 팀원 수준으로 격상. 프로젝트 관리까지 에이전트가 참여
2. **CLI + MCP = AI 통합:** Google, Microsoft 등 대형 플랫폼들이 CLI + MCP 서버 형태로 AI 에이전트 통합을 지원
3. **테스트가 핵심 인프라:** 에이전트 시대에 테스트는 선택이 아닌 필수. TDD가 에이전트와 가장 잘 맞는 개발 패턴
4. **Tool Search 패턴:** MCP 도구가 많아지면서 도구 정의의 동적 로딩이 필수 최적화로 부상
5. **코드베이스 품질 = AI 생산성:** 타입 시스템과 테스트가 잘 갖춰진 코드베이스에서 AI 에이전트 성능이 극적으로 향상

### 실무 적용 제안

| 우선순위 | 항목 | 적용 방안 |
|---------|------|----------|
| 높음 | rejections.md 패턴 | 작업 시 거부한 접근법과 이유를 기록하여 에이전트 반복 실수 방지 |
| 높음 | gws MCP 서버 | Google Workspace CLI를 MCP로 연결하여 업무 자동화 |
| 중간 | Mutation Testing | 에이전트 생성 테스트의 품질 검증을 위해 도입 검토 |
| 중간 | Anthropic 공식 과정 | Agent Skills, MCP 서버 구축 과정 수강 |
| 낮음 | Symphony 패턴 참고 | SPEC.md 기반 에이전트 디스패치 워크플로우 개선 |
