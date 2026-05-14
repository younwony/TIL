# 워크플로우 가이드라인 (Workflow Guideline)

> **이 문서는 모든 skill·command·도구의 단일 진실 출처(Single Source of Truth)입니다.**
> 헷갈릴 때마다 여기를 펼쳐서 보세요.
> `/track-status` 실행 시 항상 이 문서로 링크됩니다.

---

## 0. 빠른 시작 (Quick Reference)

가장 자주 쓰는 명령 5개:

| 명령 | 언제 |
|------|------|
| `/track-status` | 지금 어디까지 왔지? 다음 뭘 할까? |
| `/work-plan` | 새 작업 시작 (req.md 또는 구두 설명 → WORK-SPEC.md) |
| `/work-plan-start` | WORK-SPEC.md 만든 뒤 실제 구현 |
| `/self-review` | PR 만들기 전 자체 리뷰 |
| `/pr` | PR 자동 생성 |

**아무것도 모르겠을 때**: `/track-status` 한 번 치면 현재 위치 + 다음 단계 안내.

---

## 1. 6단계 워크플로우

```
[Step 0]   환경 점검          ← /setup-til-skills
[Step 0.5] 제품 검증           ← /product-review (선택)
[Step 1]   작업 명세            ← /work-plan
[Step 2]   구현                 ← /work-plan-start
[Step 3]   검증                 ← /self-review, /qa-scenario, /feature-check
[Step 4]   마무리               ← /pr, /work-log
[Sticky]   /caveman             ← 모든 단계 토글
```

### Step 0 — 환경 점검

| 항목 | 명령 | 산출물 |
|------|------|--------|
| 환경 자동 진단 | `/setup-til-skills` | `.claude/docs/setup-state.json` |

**진입 조건**: 새 PC에서 클론 직후 / 1주 이상 setup-state.json 갱신 안 됨 / Hard dependency 미충족

**확인할 것**:
- ATLASSIAN_API_TOKEN 환경변수
- Slack/Jira/Pencil/Figma MCP 연결
- mysqlsh, Docker, gh CLI, Node, Java 설치
- Hooks 실행 권한
- Codex/Gemini CLI (선택)

**다음**: 모두 OK → Step 0.5 또는 Step 1

---

### Step 0.5 — 제품 검증 (선택)

| 항목 | 명령 | 산출물 |
|------|------|--------|
| "왜 만드는가" 검증 | `/product-review` | 판정 결과 (Go / Go(축소) / Hold / No-Go) |

**진입 조건** (다음 중 하나):
- 요구사항이 모호함 ("~하면 좋겠다" 정도)
- 변경 규모가 5파일 이상 예상
- "이거 진짜 필요한가?" 자기 의심

**Grilling 모드 자동 진입 조건** (Step 1.5 내부):
- 페르소나/사용 시나리오가 비어 있음
- 사용자가 직접 grilling 요청 ("그릴미", "인터뷰해줘")

**판정 결과별 다음**:
- Go → Step 1
- Go(축소) → 범위 줄이고 Step 1
- Hold → 추가 정보 모집
- No-Go → 종료 또는 대안 검토

---

### Step 1 — 작업 명세

| 항목 | 명령 | 산출물 |
|------|------|--------|
| 요구사항 → 명세서 | `/work-plan` | `1_REQ-SNAPSHOT.md`, `2_WORK-SPEC.md`, `3_FEATURE-CHECKLIST.md` |

**입력**: req.md 또는 구두 설명 또는 Step 0.5 결과
**출력 검증 항목**:
- [ ] WORK-SPEC.md 의 **3-1. 변경 인터페이스 (Agent Brief)** 섹션 작성됨 (Matt Pocock의 AGENT-BRIEF 양식 — file path 대신 interface-level)
- [ ] FEATURE-CHECKLIST.md 의 사용자/QA 관점 항목 작성됨

**옵션 인자**:
- `/work-plan path/to/req.md` (특정 파일)
- `/work-plan --design-review` (설계 리뷰 에이전트 추가 디스패치)

---

### Step 2 — 구현

| 항목 | 명령 | 산출물 |
|------|------|--------|
| 명세 기반 구현 | `/work-plan-start` | `4_PLAN.md`, `5_ARCHITECTURE.md`, `6_SPEC.md` |

**모드 자동 선택** (변경 파일 수 기준):
- 1~2개: Solo (Main 단독)
- 3~4개: Standard (Main 조율 + 구현)
- 5개+: Coordinator (Main은 순수 조율, 워커에 위임)

**구현 중 사용 가능한 도구**:

| 상황 | 도구 |
|------|------|
| 에러 발생 | `debugger` 에이전트 — Phase 1 (재현 loop) 우선. 10가지 loop 옵션 progressive |
| 코드 영역 모르겠음 | `/zoom-out` — CONTEXT.md 어휘로 모듈+호출자 맵 |
| 테스트 누락 | `test-generator` 에이전트 |
| 코드 스멜 발견 | `code-refactor` 에이전트 |
| 응답 길어짐 | `/caveman` 토글 |

---

### Step 3 — 검증

| 항목 | 명령 | 산출물 |
|------|------|--------|
| 기능 코드 레벨 검증 | `/feature-check` | FEATURE-CHECKLIST.md 갱신 |
| 자체 코드 리뷰 | `/self-review` | `7_SELF-REVIEW.md` |
| QA 시나리오 | `/qa-scenario` | `8_QA-SCENARIOS.md` |
| 보안 감사 | `/security-audit` | (선택) STRIDE+OWASP 리포트 |
| AI Slop 탐지 | `/ai-slop-detect` | (선택) 과잉 추상화 등 검출 |
| 브라우저 QA | `/browser-debug` | (해당 시) Playwright + Chrome 2-Layer |

---

### Step 4 — 마무리

| 항목 | 명령 | 산출물 |
|------|------|--------|
| PR 자동 생성 | `/pr` | GitHub PR |
| Confluence 작업 로그 | `/work-log` | Confluence 페이지 |
| 공유용 작업 문서 | `/work-share` | Confluence 페이지 (공유 영역) |
| Track 상태 변경 | `/track-status {id}` 후 응답 | metadata.json 갱신 |

---

### Sticky — 단계 무관 도구 모드

| 명령 | 효과 | 해제 |
|------|------|------|
| `/caveman` | 응답에서 filler 제거, 토큰 75% 절약 | "stop caveman" / "동굴인 그만" |
| `/zoom-out` | 한 단계 추상화 맵 | (단발성) |

---

## 2. Matt Pocock 7가지 하네스 패턴 매핑

| 패턴 | 우리 자산 | 한 줄 |
|------|---------|------|
| ① Alignment | `/product-review` (grilling), `/work-plan` | 사용자-에이전트 정합성 |
| ② Context Compression | `.claude/CONTEXT.md`, `/caveman` | 토큰/단어 절약 |
| ③ Architecture Hygiene | `/zoom-out`, `code-refactor`, `ai-slop-detect` | 매일 design care |
| ④ Feedback Loop | `debugger` (5-phase), `test-coverage-check`, `/feature-check`, `/self-review` | 검증 신호 인프라 |
| ⑤ Workflow State Machine | `/track-status`, `/work-plan-start` (Phase) | 작업 라이프사이클 |
| ⑥ Guardrail | `block-dangerous*.sh` hooks, `/security-audit` | 위험 명령 차단 |
| ⑦ Composability | `/setup-til-skills`, ADR 0001, `/sync-global` | 한 번 setup → 여러 skill 소비 |

> 상세: [`cs/tool/mattpocock-skills-harness.md`](../../cs/tool/mattpocock-skills-harness.md)

---

## 3. 도구 모드 트리거 (헷갈리지 말 것)

### Grilling vs 6Q 검증 (둘 다 product-review 안)

| 단계 | 언제 | 무엇 |
|------|------|------|
| Step 1.5 (Grilling) | 요구사항 모호할 때만 | 페르소나/현재행태/빈도/유사요청/성공기준 5단계 인터뷰 |
| Step 2 (6Q 검증) | 항상 | Q1~Q6 (핵심문제/대상사용자/현재해결책/MVP/성공측정/유지비용) |

### Caveman 모드의 Auto-Clarity 예외

다음 상황에서는 caveman 자동 해제:
- 보안 경고
- 비가역 명령 (rm, drop, push --force)
- 멀티스텝 시퀀스 (3단계+)
- 에러 진단 시 가설 제시 ("약간/아마"가 의미 있음)

### Debugger의 5-Phase

```
Phase 1. 재현 (loop 만들기) ← 80% 의 작업
Phase 2. 최소화 (delta-debugging)
Phase 3. 가설 3~5개 ranked + 사용자 승인
Phase 4. 계측 (한 번에 한 변수)
Phase 5. fix + 회귀 테스트
```

**Loop 우선순위 10단계** (위에서부터 시도):
Failing test → Curl → CLI snapshot → Headless browser → Replay trace → Throwaway harness → Property/fuzz → Bisection → Differential → HITL bash

---

## 4. 인프라 파일 위치

### 워크플로우 외부 (인프라)

| 파일 | 역할 |
|------|------|
| `.claude/CONTEXT.md` | 프로젝트 도메인 용어집 (ubiquitous language) |
| `.claude/docs/adr/0001-skill-dependency-classification.md` | Hard/Soft dependency 분류 |
| `.claude/docs/adr/0002-track-as-skill-container.md` | Track = atomic skill 컨테이너 (Matt Pocock 통합) |
| `.claude/docs/adr/0003-bucket-structure.md` | skills/ 6-bucket 구조 (engineering/productivity/misc/personal/in-progress/deprecated) |
| `.claude/docs/agents/issue-tracker.md` | Jira 호출 패턴 (`/triage`, `/to-prd`, `/to-issues` 입력) |
| `.claude/docs/agents/triage-labels.md` | canonical role → Jira 라벨/상태 매핑 |
| `.claude/docs/agents/domain.md` | 도메인 문서 위치 + skill 읽기 규칙 |
| `.claude/docs/setup-state.json` | `/setup-til-skills` 산출물 — 환경 점검 결과 |
| `.claude/hooks/block-dangerous*.sh` | PreToolUse 차단 hook (rm, git push --force, .claude/ 보호 등) |
| `.claude/hooks/block-claude-dir-gitadd.sh` | `.claude/` git add 차단 hook (TIL 안에서는 자동 통과) |
| `.claude/settings.json` | hooks 등록, 권한 |

### 워크플로우 산출물 (Track별)

```
.claude/tracks/{track_id}/
├── metadata.json
├── 0_INDEX.md
├── 1_REQ-SNAPSHOT.md       (work-plan)
├── 2_WORK-SPEC.md          (work-plan)
├── 3_FEATURE-CHECKLIST.md  (work-plan)
├── 4_PLAN.md               (work-plan-start)
├── 5_ARCHITECTURE.md       (구현 완료)
├── 6_SPEC.md               (구현 완료)
├── 7_SELF-REVIEW.md        (self-review)
└── 8_QA-SCENARIOS.md       (qa-scenario)
```

---

## 5. "언제 무엇을 쓰는가" 결정 트리

```
새 작업 시작?
├─ YES → /track-status (현재 위치 확인)
│         └─ 환경 미점검 → /setup-til-skills
│         └─ 요구사항 모호 → /product-review (grilling 자동 진입)
│         └─ WORK-SPEC 없음 → /work-plan
│         └─ WORK-SPEC 있고 PLAN 없음 → /work-plan-start
│         └─ Phase 진행 중 → 현재 Phase 작업
│         └─ 모든 Phase 완료 → /self-review → /pr
└─ NO → 어떤 상황?

에러 발생?
└─ debugger 에이전트 (Phase 1 재현 loop 우선)

코드 한 영역 모르겠음?
└─ /zoom-out

응답이 너무 김?
└─ /caveman

테스트 누락?
└─ test-generator 에이전트

리팩토링하고 싶음?
└─ code-refactor 에이전트

AI가 만든 코드 과해 보임?
└─ /ai-slop-detect

도메인 용어가 흐릿하거나 요구사항 모호함?
└─ /grill-with-docs (CONTEXT.md/ADR 인라인 갱신)

지금까지 대화를 PRD/WORK-SPEC 양식으로 합성?
└─ /to-prd (단독 호출도 가능)

계획을 vertical slice (tracer bullet) 이슈로 분해?
└─ /to-issues

모듈 모양이 잘못된 거 같음 (shallow/leaky/untestable)?
└─ /improve-codebase-architecture (deletion test + deepening)

설계 검증용 throwaway 코드?
└─ /prototype (terminal app 또는 UI 변형)

Jira 이슈 분류 / AGENT-BRIEF 작성?
└─ /triage (5-state machine)

설계 리뷰 받고 싶음?
└─ /self-review (4명 에이전트 병렬) 또는 /team-review

보안 점검?
└─ /security-audit (STRIDE + OWASP)

브라우저 QA?
└─ /browser-debug (Playwright + Chrome 2-Layer)

서버 점검?
└─ /ssh-server-inspect

DB 조회?
└─ /db-inspect (read-only) / /db-tune (쿼리 튜닝)

작업 내용 Confluence 정리?
└─ /work-log (개인) / /work-share (공유)

Slack→Jira/Confluence?
└─ /slack-to-jira / /slack-to-confluence

이번 주 회고?
└─ /weekly-retro
```

---

## 6. Hard Dependency 분류 (요약)

> 상세: [`docs/adr/0001-skill-dependency-classification.md`](./adr/0001-skill-dependency-classification.md)

| 의존성 | 영향받는 skill |
|-------|--------------|
| `ATLASSIAN_API_TOKEN` | work-log, work-share, meeting-notes, slack-to-confluence |
| Slack MCP | slack-* (7종), meeting-notes |
| Jira MCP | jira-report, jira-notify |
| Figma MCP | figma-read |
| Pencil MCP | pencil-screen, pencil-update, pencil-to-code |
| Chrome MCP / Playwright | browser-debug, browser-debug-chrome |
| `mysqlsh` | db-inspect, db-tune, prod-db-inspect |
| Docker Desktop | docker-up/update/down/logs/status |
| `gh` CLI | pr, review-pr |
| Codex + Gemini CLI | 3ai-plan |

`/setup-til-skills`가 위 모든 의존성을 자동 점검합니다.

---

## 6-1. 외부 AI 크로스 체크 timeout 정책 ⏱️

work-plan, self-review, review-pr, team-review, 3ai-plan 등에서 Gemini/Codex를 호출할 때:

| 호출 유형 | Timeout | 초과 시 |
|---------|---------|---------|
| **Bash** (`gemini -p`, `codex exec`, `codex review`) | `timeout: 240000` (4분) | 자동 종료 + 다음 단계 진행 |
| **3ai-plan** (Claude + Gemini + Codex Bash CLI) | 위 정책 적용 | 한 AI라도 hung되면 즉시 중단 |

> **Codex Plugin Skill 사용 금지**: `/codex:rescue`, `/codex:review` 등은 Bash timeout 미적용으로 hang 위험. 반드시 Bash CLI(`codex exec -`, `codex review`)만 사용. CLAUDE.md "Codex 협업" 섹션 참조.

**원칙**: hung은 무한히 기다리지 않는다. 4분(Bash) / 5분(Plugin) 한도 후 강제 진행.

---

## 7. 자주 까먹는 규칙

### Git
- `git add` 까지만, commit은 사용자 명시 요청 시만
- `.claude/` 디렉토리는 **절대 git add 하지 않음** (hook으로 강제 차단)
- `git add -A`, `git add .` 도 차단됨 (위와 같은 이유)

### 문서 작성
- SVG 다이어그램 우선 (Mermaid는 빠른 프로토타이핑만)
- 워크플로우 문서는 `.claude/tracks/{track_id}/` 또는 `.claude/docs/`에만, 프로젝트 루트 X
- 파일명에 번호 접두사 (1_, 2_, ... 8_)

### 응답
- 한국어로 응답
- 기술 용어/식별자는 원문 유지
- caveman 모드 ON일 때만 filler 제거 적용

---

## 8. 최근 추가된 항목 (2026-04-29)

이 문서가 만들어진 계기:

| 항목 | 위치 |
|------|------|
| `.claude/CONTEXT.md` | 도메인 용어집 (Track, WORK-SPEC, Phase 등) |
| `.claude/docs/adr/0001-skill-dependency-classification.md` | Hard/Soft 의존성 분류 |
| `/setup-til-skills` skill | Hard dep 자동 점검 + setup-state.json 생성 |
| `/zoom-out` skill | Matt Pocock의 zoom-out 차용 |
| `/caveman` skill | Matt Pocock의 caveman 한국어 변형 |
| `product-review` 글로벌 승격 + grilling | Step 0.5 단계화 |
| `debugger` 5-phase + 10-loop 옵션 | 재현 loop 우선 진단 패턴 |
| WORK-SPEC 템플릿 3-1 (AGENT-BRIEF) | interface-level spec 양식 |
| `block-dangerous` hook 룰 | `.claude/` git add 차단 |
| `/track-status` 보강 | 환경 점검 + 다이어그램 + 도구 힌트 |

---

## 9. 추가 참고 문서

- [`cs/tool/mattpocock-skills-harness.md`](../../cs/tool/mattpocock-skills-harness.md) — Matt Pocock 7패턴 분석 가이드
- [`mattpocock-skills-application.md`](./mattpocock-skills-application.md) — 본 저장소 적용 분석
- [`track-status-redesign.md`](./track-status-redesign.md) — `/track-status` 재구성 설계 문서
- [`adr/0001-skill-dependency-classification.md`](./adr/0001-skill-dependency-classification.md) — 의존성 분류 ADR
- 글로벌 CLAUDE.md — 모든 skill/command/agent 표

---

> 마지막 갱신: 2026-04-29
> 새 skill/command 추가 시 이 문서도 업데이트하세요.
