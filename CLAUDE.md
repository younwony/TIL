# CLAUDE.md

이 문서는 Claude Code가 TIL 저장소에서 작업할 때 참고하는 설정 및 규칙입니다.
글로벌 `~/.claude/CLAUDE.md`를 베이스로 하되, TIL 고유 항목(저장소 개요, 폴더 구조, 자체 카탈로그, DB 접근 제어, 참고 문서)은 별도 보존합니다.

# 행동 원칙 (최우선)

> 이 문서의 다른 모든 규칙·워크플로우·스킬보다 이 4원칙이 우선한다.
> 출처: Andrej Karpathy CLAUDE.md — 핵심 4원칙 흡수.
>
> **트레이드오프:** 이 가이드는 속도보다 신중함에 편향되어 있다. 사소한 작업은 판단으로 처리한다.

## 1. 코딩 전에 생각한다 (Think Before Coding)

**가정하지 마라. 혼란을 숨기지 마라. 트레이드오프를 surface하라.**

구현 전에:
- 가정을 명시적으로 표현한다. 확신이 없으면 묻는다.
- 여러 해석이 가능하면 선택지를 제시한다 — silent하게 고르지 않는다.
- 더 단순한 접근이 있으면 말한다. 필요하면 푸시백한다.
- 불명확하면 멈춘다. 무엇이 혼란스러운지 명명하고 묻는다.

## 2. 단순함이 우선 (Simplicity First)

**문제를 푸는 최소 코드. 추측성 코드 금지.**

- 요청 외 기능 금지.
- 1회용 코드에 추상화 금지.
- 요청하지 않은 "유연성", "설정 가능성" 금지.
- 일어날 수 없는 시나리오에 대한 에러 핸들링 금지.
- 200줄을 썼는데 50줄로 가능하다면 다시 써라.

자체 점검: "시니어 엔지니어가 이걸 보면 과하다고 할까?" Yes면 단순화한다.

## 3. 외과적 변경 (Surgical Changes)

**필요한 것만 건드린다. 내가 만든 쓰레기만 치운다.**

기존 코드 수정 시:
- 인접 코드, 주석, 포매팅을 "개선"하지 않는다.
- 망가지지 않은 것을 리팩터링하지 않는다.
- 내 스타일과 다르더라도 기존 스타일을 따른다.
- 무관한 dead code를 발견하면 언급만 한다 — 삭제하지 않는다.

내 변경이 orphan을 만들었을 때:
- 내 변경으로 새로 미사용이 된 import/변수/함수만 제거한다.
- 사전에 존재하던 dead code는 요청 없이 제거하지 않는다.

테스트: 변경된 모든 라인이 사용자 요청에 직접 추적되어야 한다.

## 4. 목표 주도 실행 (Goal-Driven Execution)

**성공 기준을 정의한다. 검증될 때까지 루프한다.**

작업을 검증 가능한 목표로 변환한다:
- "검증 추가" → "잘못된 입력 테스트 작성 후 통과시키기"
- "버그 수정" → "재현 테스트 작성 후 통과시키기"
- "X 리팩터링" → "변경 전후 테스트 통과 보장"

다단계 작업은 간단한 plan을 명시한다:

    1. [단계] → verify: [확인 방법]
    2. [단계] → verify: [확인 방법]
    3. [단계] → verify: [확인 방법]

강한 성공 기준은 독립적으로 루프할 수 있게 한다. 약한 기준("동작하게 만들어")은 끊임없는 명확화를 요구한다.

---

**원칙이 작동하고 있다면:** diff에 불필요한 변경이 줄고, 과한 복잡도로 인한 재작성이 줄고, 명확화 질문이 실수 후가 아니라 구현 전에 나온다.

# 설정

## 언어

- **모든 응답은 한글로 작성**하고, 설명도 한글로 진행
  - 코드, 명령어, 기술 용어 등 고유명사는 원어 그대로 사용 가능
  - 커밋 메시지, 주석, 변수명 등 코드 내부는 기존 규칙을 따름
  - 질문, 설명, 안내, 요약 등 사용자와의 모든 대화는 한글로 응답

## Git

- **모든 작업에서 git add까지만 진행**하고 commit은 하지 않음
  - 새로 생성된 파일: git add만
  - 수정된 파일: git add만
  - 삭제된 파일: git add만
  - 사용자가 명시적으로 commit을 요청할 때만 commit 실행
- **TIL 한정 예외 — `.claude/` 디렉토리도 git add 허용**
  - 글로벌 룰은 `.claude/` git add 금지이나, TIL은 글로벌 설정 공유 채널로 사용하기 때문에 허용한다.
  - skills/commands/hooks/agents/rules/CLAUDE.md 모두 add 대상.
  - 다만 `.claude/.credentials.json`, `.claude/sessions/`, `.claude/cache/`, `.claude/projects/` 등 PC/세션 종속 자산은 `.gitignore`로 제외한다.

## Plan Mode

- **모든 비단순 작업은 플랜 모드(Plan Mode)로 진행**
  - 사용자 요청, 스킬 실행, 커맨드 실행 등 구현이 필요한 작업은 반드시 플랜 모드로 시작
  - 플랜 모드에서 충분히 탐색·분석한 후 실행 계획을 수립하고 승인받은 뒤 구현
  - 단순 질문 응답, 파일 읽기, 간단한 수정(오타, 1-2줄 변경)은 플랜 모드 불필요
  - 플랜 수립 시 **반드시 충분한 thinking을 거쳐** 요구사항 분석, 영향 범위 파악, 접근 방법 비교를 진행
  - 플랜에는 실행 순서, 대상 파일, 변경 내용, 검증 방법을 명확히 포함

# 저장소 개요

- **저장소 유형:** TIL (Today I Learned) - 학습 내용 정리
- **주요 언어:** Markdown, Java, Kotlin
- **구조:** CS 지식(마크다운) + Study(프로젝트 단위)

## 커밋 메시지 형식

```
<type>: <subject>

<body> (선택)
```

| Type | 설명 | 예시 |
|------|------|------|
| `docs` | 문서 추가/수정 | `docs: Effective Java Item 1 추가` |
| `feat` | 새로운 기능/내용 | `feat: 알고리즘 문제 풀이 추가` |
| `fix` | 오류 수정 | `fix: 오타 수정` |
| `refactor` | 구조 변경 | `refactor: 폴더 구조 정리` |
| `chore` | 기타 작업 | `chore: gitignore 수정` |

## 폴더 구조 규칙

```
TIL/
├── README.md              # 저장소 소개
├── CONVENTION.md          # 컨벤션 가이드
├── CLAUDE.md              # Claude 설정 (이 문서)
├── .claude/
│   ├── commands/          # Claude Code Commands
│   ├── skills/            # Claude Code Skills
│   ├── docs/              # 워크플로우 생성 문서 (Track 없을 때)
│   │   └── CODE-RULES.md
│   └── tracks/            # Track별 작업 추적 + 문서
│       └── {track_id}/    # WORK-SPEC, ARCHITECTURE, SPEC, QA-SCENARIOS, SELF-REVIEW 등
├── cs/                    # CS 지식 (마크다운 중심)
│   └── {category}/        # kebab-case
├── cs-web/                # CS 문서 웹 뷰어 (Spring Boot)
└── study/                 # 스터디 (프로젝트 단위)
    └── {study-name}/      # kebab-case
```

# 작업 처리 워크플로우

모든 비단순 요청은 아래 단계를 순서대로 수행한다.

## Step 1: 요구사항 파악
- 사용자 요청을 정확히 이해
- 모호한 부분은 질문으로 명확화
- 관련 파일/설정/문서 확인

## Step 2: 리서치 & 탐색
- 기존 코드/설정/문서에서 관련 내용 검색
- 외부 리소스 필요 시 웹 검색 수행
- 기존 패턴과 컨벤션 파악

## Step 3: 플랜 수립
- 실행 순서, 대상 파일, 변경 내용 정리
- 영향 범위와 리스크 분석
- 대안 비교 후 최적 방안 선택

## Step 4: 구현
- 플랜 승인 후 순서대로 실행
- 각 단계 완료 시 중간 결과 확인
- 문제 발생 시 즉시 보고 및 대안 제시

## Step 5: 검증 & 완료
- 변경 사항 동작 확인 (테스트/실행)
- Java 프로젝트: 테스트 커버리지 체크 (`test-coverage-check` 스킬) 수행
- git add로 staging
- 결과 요약 보고

## 실전 팁

- **CLAUDE.md 지속 관리**: Claude가 잘못된 행동을 할 때마다 해당 규칙을 CLAUDE.md에 추가하여 같은 실수 반복 방지
- **검증 피드백 루프 필수**: 모든 변경 후 반드시 검증 방법 제시 (테스트 실행, 동작 확인, 빌드 체크)
- **서브에이전트 적극 활용**: 독립적인 작업은 서브에이전트에 위임하여 메인 컨텍스트 보존
- **"Don't implement yet" 원칙**: 복잡한 작업은 구현 전 계획 완성도를 충분히 높인 후 실행

# 세션 & 컨텍스트 관리

- **한 세션 = 한 피처**: 하나의 대화에서 하나의 기능/작업만 완료
- **에러는 원본 그대로**: 에러 로그/스택 트레이스는 가공 없이 전문 기반 분석
- **가정 변경 알림**: 기술 스택이나 아키텍처 가정이 변경될 때 사용자에게 먼저 확인
- **/compact 타이밍**: 응답이 느려지면 `/compact` 실행, 85% 초과 시 `/clear`
- **스크립트 오프로드**: 대량 파일 변환, 포맷팅 등 반복 작업은 셸 스크립트로 분리
- **세션 시작 시**: MEMORY.md, TODO.md, HANDOFF.md가 있으면 읽고 맥락 파악 후 시작

## 메모리 에이징

- MEMORY.md, HANDOFF.md, TODO.md 등 메모리 파일을 읽을 때, 파일의 수정 시각(mtime)을 확인
- **2일 이상 경과**된 파일의 정보는 "이 정보는 N일 전 것" 으로 인지하고 현재 사실과 다를 수 있음을 고려
- 특히 **파일:줄 번호 인용**, **브랜치 상태**, **작업 진행률** 등은 반드시 현재 코드/git 상태와 대조 후 사용
- ISO 날짜보다 "N일 전" 형태로 경과 시간을 인지하면 staleness 판단이 더 정확함

## 커맨드 사전 조건

아래 커맨드는 해당 조건이 충족될 때만 사용한다. 조건이 안 맞으면 사용자에게 안내하고 실행하지 않는다.

| 커맨드 그룹 | 사전 조건 | 해당 커맨드 |
|------------|----------|------------|
| Slack 연동 | Slack MCP 연결 필요 | `slack-to-jira`, `slack-to-confluence`, `slack-digest`, `slack-remind`, `standup-summary`, `meeting-notes`, `sprint-start-notify` |
| Jira 연동 | Jira MCP 연결 필요 | `jira-report`, `jira-notify` |
| Confluence 연동 | `ATLASSIAN_API_TOKEN` 환경변수 필요 | `work-log`, `work-share`, `slack-to-confluence`, `meeting-notes` |
| Figma 연동 | Figma MCP 연결 필요 | `figma-read` |
| 브라우저 QA | Chrome MCP 또는 Playwright 설치 필요 | `browser-debug`, `browser-debug-chrome` |

# 테스트

테스트 코드는 반드시 작성한다.
단위 테스트, 통합테스트 포함.

# 워크플로우 문서 시스템

문서 경로(`{DOC_DIR}`), 번호 체계(0~9), 작성 규칙, 다이어그램 컨벤션은 단일 진실 출처(SSOT)로 관리한다.
**[`~/.claude/skills/track-status/references/document-system.md`](../../home/wony9324/.claude/skills/track-status/references/document-system.md)**

> `/work-plan`, `/work-plan-start`, `/self-review`, `/qa-scenario` 등 `{DOC_DIR}`을 사용하는 모든 스킬은 이 파일을 따른다.

# AI 코딩 보안

AI 코딩 도구 사용 시 반드시 지켜야 할 보안 원칙이다.

## 신뢰할 수 없는 소스 주의
- `git clone` 전 저장소의 CLAUDE.md, .cursorrules, AGENTS.md 등 컨텍스트 파일 확인
- 알 수 없는 출처의 MCP 서버 연결 금지
- 외부 PR/이슈의 코드를 그대로 실행하지 않음

## AI 생성 코드 보안 리뷰
- AI 생성 코드도 반드시 OWASP Top 10 관점 보안 리뷰
- 특히 외부 입력 처리, 인증/인가, 파일 I/O, SQL 쿼리 주의
- API 키, 시크릿, 자격 증명이 코드에 포함되지 않았는지 확인

## 권한 최소화
- AI에게 필요 최소한의 파일/디렉토리 접근만 허용
- 프로덕션 환경 접근 금지
- `.env`, `credentials.json` 등 민감 파일 커밋 방지

## DB 접근 제어 (mysqlsh) — TIL 고유

- `block-dangerous-sql.sh` hook이 Bash 명령 중 mysqlsh 호출을 감지하여 위험 SQL을 자동 차단
- **허용**: `SELECT`, `SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`
- **차단**: `DELETE`, `UPDATE`, `INSERT`, `DROP`, `ALTER`, `TRUNCATE`, `CREATE`, `GRANT`, `REVOKE`, `LOAD DATA`, `CALL` 등
- `/work-plan` 실행 시 Spring Boot/Batch 프로젝트에서 DB 스키마를 자동 조회할 때 적용

# AI 호출

메인 세션에서 Codex/Gemini를 호출하는 모든 경우에 `ai-harness-monitor` 래퍼를 사용한다 (단순 ad-hoc 질문 포함).

- 메인 세션: `~/.claude/skills/ai-harness-monitor/scripts/ask-gemini.sh` / `ask-codex.sh` / `ask-both.sh` / `route-codex.sh`
- Agent 내부(work-plan/self-review/review-pr/team-review 등이 디스패치한 reviewer 에이전트): `gemini-check` / `codex-check` 스킬
- **금지**: 메인 세션에서 `gemini -p ...` / `codex exec ...` / `codex review ...` 직접 호출 (dashboard 카드에 안 떠서 라우팅이 안 보임)
- **금지**: Codex Plugin Skill(`/codex:rescue`, `/codex:review` 등) — Bash timeout 미적용으로 hang 위험
- 상세 정책(timeout 240s, 에러 분류, 재시도, 로그 보존)은 각 스킬 SKILL.md 참조

## Codex CLI 사용법 (Agent 내부 또는 직접 디버깅용)

```bash
# Bash 도구 호출 시 timeout: 240000ms (4분) 필수 명시

# 파이프 입력 (실패 처리 포함)
cat file.yaml | codex exec - 2>&1 || echo "CODEX_FAIL"

# 코드 리뷰 (적대적 리뷰)
codex review --base main 2>&1 || echo "CODEX_FAIL"

# 모델 지정
codex "분석할 내용" --model o3
```

상세 가이드: [Codex Plugin 문서](./cs/tool/codex-plugin-claude-code.md)

# Claude Code Skills / Commands / Agents 카탈로그

전체 목록(Skills ~21개, Commands ~32개, Agents ~10개)은 분리되어 있다:
**[`.claude/docs/skills-commands-agents-catalog.md`](.claude/docs/skills-commands-agents-catalog.md)**

> 호출 자체는 시스템 프롬프트의 available skills 목록과 Agent 도구 description으로 이미 노출되므로,
> 매 세션에 카탈로그를 컨텍스트에 올릴 필요가 없다. 어떤 게 있는지 한눈에 보고 싶을 때만 Read.
>
> 에이전트 사용 가이드: [AGENT-GUIDE.md](./AGENT-GUIDE.md)
> 디스패치 규칙(언제 어떤 에이전트를 부를지)은 아래 섹션에 잔류한다.

## 일반 요청 병렬 에이전트 디스패치

WORK-SPEC.md 없이도, 아래 패턴의 요청 시 에이전트를 병렬 디스패치한다.

### 구현 요청 ("구현해줘", "작업해줘", "추가해줘", "변경해줘")

변경 파일 3개 이상 예상 시:
```
[Main]            코드 구현 (foreground)
[test-generator]  테스트 자동 생성 (background, Main 구현 시작 후 디스패치)
```

### 리팩토링 요청 ("리팩토링해줘", "정리해줘", "코드 개선")

```
[code-refactor]   CLAUDE.md 규칙 기반 분석 → 개선안 도출 (foreground)
[Main]            분석 결과 기반 수정 적용
```

### 버그 수정 요청 ("버그 수정", "에러 수정", "안돼", "동작 안함")

```
[debugger]        스택 트레이스/로그 분석 (foreground, 결과 대기)
[Main]            분석 결과 기반 수정
[test-generator]  수정 후 회귀 테스트 생성 (background)
```

### 적용 조건

- **파일 1~2개 수정**: Main 단독 (에이전트 오버헤드 > 이득)
- **파일 3개+**: 위 패턴 적용
- **단순 삭제/이름 변경**: Main 단독 (Grep/Edit으로 즉시 처리)

## 에이전트 에러 처리 (Withhold-then-Recover)

에이전트 실패 시 **즉시 에러를 전파하지 않고, 보류(withhold) 후 자동 복구를 시도**한다.

| 시도 | 행동 |
|------|------|
| 1차 실패 | 에러 메시지를 분석하여 에이전트에게 수정 지시 (SendMessage) |
| 2차 실패 | 다른 접근 방식으로 재시도 |
| 3차 실패 | Main이 해당 작업을 직접 수행 |

- 컴파일 에러, 테스트 실패 등 **수정 가능한 에러**는 에이전트에게 재시도 기회를 준다
- 네트워크 에러, 권한 에러 등 **환경 문제**는 즉시 Main으로 전환
- 에이전트가 3회 실패 후에도 해결 못하면 사용자에게 보고

## 팀 에이전트 워크플로우

`/work-plan-start` 실행 시 변경 파일 수에 따라 실행 모드를 선택한다.

### 모드 선택

| 예상 변경 파일 수 | 모드 | Main 역할 |
|------------------|------|----------|
| 1~2개 | **Solo** | Main 단독 처리. 에이전트 오버헤드 불필요 |
| 3~4개 | **Standard** | Main이 조율 + 구현 + 문서화 |
| 5개+ | **Coordinator** | Main은 순수 조율자. 구현을 워커에 위임 |

> 각 모드의 상세 Phase 구성, 에이전트별 입력, 워커 분배 규칙은 `work-plan-start` 스킬(SKILL.md) 참조.

### 공통 디스패치 규칙

- Phase 간 의존성이 있으므로 Phase 순서는 반드시 순차 실행
- 각 Phase 내 에이전트는 최대한 병렬 실행
- WORK-SPEC.md가 존재할 때만 팀 워크플로우 적용

## 외부 도구 에러 타입 판별

Codex, Gemini, MCP 등 외부 도구 실패 시 에러 유형에 따라 다르게 처리한다.

| 에러 유형 | 판별 기준 | 처리 방법 |
|----------|----------|----------|
| **네트워크 에러** | timeout, connection refused, ECONNRESET | 5초 대기 후 1회 재시도 → 실패 시 스킵 |
| **인증 에러** | 401, 403, unauthorized, forbidden | 재시도 없이 즉시 사용자에게 안내 ("API 토큰 확인 필요") |
| **타임아웃** | 응답 4분(240초) 초과 | 즉시 강제 종료 (Bash `timeout: 240000ms`). 재시도 X — 다른 AI/Claude 단독 진행 |
| **Rate Limit** | 429, rate limit, too many requests | 30초 대기 후 재시도 |
| **모델/서비스 에러** | 500, 502, 503, internal server error | 1회 재시도 → 실패 시 대체 도구로 전환 (Codex↔Gemini) |
| **입력 에러** | 400, invalid input, schema validation | 입력을 수정하여 재시도 (프롬프트 축소, 형식 변경) |

- `CODEX_FAIL`, `GEMINI_FAIL` 같은 단순 문자열 대신 위 분류에 따라 분기
- Codex/Gemini 둘 다 실패 시 "외부 크로스 체크 없이 Claude 단독으로 진행합니다" 안내

## 리뷰 에이전트 컨텍스트 최적화

리뷰 에이전트 4명이 동일한 diff를 반복 탐색하지 않도록, Main이 요약 컨텍스트를 한 번 생성하여 전달한다.

> 상세 절차는 각 커맨드(`self-review.md`, `review-pr.md`, `team-review.md`)의 2단계 참조.

## PR 설정

```
PR_BASE_BRANCH: main-review
```

설정이 없으면 기본값 `main`을 사용합니다.

### 리뷰어 제외 목록

리뷰어 랜덤 선정 시 아래 계정은 항상 후보에서 제외합니다.

```
PR_REVIEWER_EXCLUDE: temcolabs, happyfridaycode
```

# Confluence 설정

작업 로그를 Confluence에 업로드할 때 사용하는 설정입니다.

| 항목 | 값 |
|------|------|
| **사이트 URL** | `https://temcolabs.atlassian.net` |
| **이메일** | `wonhee.youn@temco.io` |
| **개인 스페이스 키** | `~645023757` |
| **개인 스페이스 ID** | `1983741954` |
| **홈페이지 ID** | `1983742135` |
| **API 인증** | Basic Auth (이메일 + API 토큰) |

- API 토큰은 환경변수 `ATLASSIAN_API_TOKEN`으로 관리하거나 MCP 설정에서 참조
- Confluence REST API v2 엔드포인트: `{사이트URL}/wiki/api/v2/`

## Atlassian API 우선순위

**curl REST API를 기본으로 사용**한다. MCP는 실패율이 높아 폴백으로만 사용한다.

| 우선순위 | 방법 | 사용 조건 |
|---------|------|----------|
| **1순위** | `curl` REST API (Basic Auth) | `ATLASSIAN_API_TOKEN` 환경변수 존재 시 항상 |
| **2순위 (폴백)** | `mcp__atlassian__*` MCP 도구 | curl 실패 시 또는 MCP 전용 기능 필요 시 |

```bash
# curl 기본 사용 패턴
curl -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

- MCP 먼저 시도하지 않는다. 바로 curl로 요청한다.
- curl 실패 시에만 MCP 폴백을 시도한다.

# 참고 문서

- [CONVENTION.md](./CONVENTION.md): 상세 컨벤션 가이드
- [cs/CS-GUIDE.md](./cs/CS-GUIDE.md): CS 문서 작성 가이드
- [study/README.md](./study/README.md): 스터디 목록
- [.claude/skills/](./.claude/skills/): Claude Code Skills (특정 워크플로우)
- [cs/tool/ai-harness/](./cs/tool/ai-harness/): AI 팀(Claude+Codex+Gemini) tmux 셋업 가이드
