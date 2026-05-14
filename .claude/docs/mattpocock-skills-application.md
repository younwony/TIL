# Matt Pocock 하네스 패턴을 내 Skill/Command에 적용하기

> 이 문서는 [cs/tool/mattpocock-skills-harness.md](../../cs/tool/mattpocock-skills-harness.md) 분석을 기반으로, **현재 내가 보유한 글로벌(`~/.claude/`) + 로컬(`.claude/`) skill/command/agent를 7가지 하네스 패턴 관점에서 진단**하고, 무엇을 어떻게 보강할지 정리한 개인 적용 노트다.

---

## 보유 자산 한눈에

| 구분 | 글로벌 | 로컬 | 비고 |
|------|------|------|------|
| Skills | 24개 | 31개 | 로컬 = 글로벌 + TIL 전용 7~10개 (CS/디자인/메타) |
| Commands | 31개 | 34개 | 로컬 = 글로벌 + (`release-notes-kr`, `ssh-server-inspect`, `today`) |
| Agents | 11개 | 11개 | 동일 (review-* 4종, code-refactor, debugger, test-generator, jira-updater, design-reviewer, cs-* 2종) |
| Hooks | 2개 | 5개 | 로컬에만 `block-dangerous-ssh`, `log-edits`, `notify-cs-doc` 추가 |

---

## 7가지 패턴 커버리지 매트릭스

| 패턴 | 점수 | 대표 자산 | 강점 | 약점 |
|------|------|---------|------|------|
| ① Alignment | △ 중 | `product-review`, `work-plan`, `3ai-plan` | 6Q 검증 + 3AI 크로스체크로 "왜"는 검증함 | grilling 부재 — 깊은 사용자 인터뷰 단계 없음 |
| ② Context Compression | △ 약 | `CLAUDE.md`, `sync-global` | 글로벌 규칙 중앙화 | **CONTEXT.md/ubiquitous language 전무**, caveman 토글 없음 |
| ③ Architecture Hygiene | △ 중 | `code-refactor`, `ai-slop-detect`, `security-audit` | 14가지 코드 스멜 + AI slop 6패턴 감지 | **ADR 없음**, deep module/seam 어휘 없음, 사후 리팩토링만 |
| ④ Feedback Loop | ○ 강 | `tdd`(`test-coverage-check`), `debugger`, `qa-scenario`, `feature-check`, `self-review`, `browser-debug` | 다층 검증 (커버리지/스택/QA/리뷰/UI) | 진단 5-phase + 10-loop 옵션 같은 **명시적 루프 구성 가이드 없음** |
| ⑤ Workflow State Machine | △ 중 | `work-plan-start`, `track-status`, `smart-session`, `jira-updater` | Track 기반 phase 관리, TaskCreate, jira 연동 | **AGENT-BRIEF 양식 없음**, triage role 5종 정의 없음 |
| ⑥ Guardrail / Safety | ○ 강 | `block-dangerous.sh`, `block-dangerous-sql.sh`, `block-dangerous-ssh.sh`, `security-audit` | rm/git/sql/ssh 위험 패턴 사전 차단 | git push/reset 차단은 있지만 **`.claude/` 보호 hook 없음**(CLAUDE.md 규칙은 있어도 강제 X) |
| ⑦ Composability / Setup | △ 약 | `sync-global`, `docker-up`, `skill-rebuild` | 글로벌-로컬 양방향 sync, plugin 매니페스트 일부 | **setup skill 없음**, hard/soft dependency ADR 없음, plugin.json 형식 manifest 없음 |

**점수표**: 강(○) 2 / 중(△) 4 / 약 1
- 가장 약한 영역: **Context Compression**, **Composability**

---

## Top 3 갭 — 가장 약한 패턴

### 🔴 1위: Context Compression (CONTEXT.md / ubiquitous language)

매 세션마다 "Track이 뭐야", "WORK-SPEC이 뭐야", "Phase가 뭐야" 를 다시 설명해야 한다. CLAUDE.md에 규칙은 잔뜩 있지만, **도메인 용어집**이 없다.

**가장 작은 변경**: `/CONTEXT.md` (또는 `.claude/CONTEXT.md`) 한 파일 생성. Track, WORK-SPEC, Phase, Sub-track, FEATURE-CHECKLIST 등 우리 vocabulary 정의.

### 🟠 2위: Composability / Setup

`/sync-global push|pull|status` 는 있지만, **새 PC에 클론한 직후** ATLASSIAN_API_TOKEN, Slack MCP, Jira MCP, hooks 활성화, Docker 등을 한 번에 묻고 세팅하는 skill 없음. ADR도 없어서 어떤 skill이 어떤 사전 조건을 요구하는지 흩어져 있음.

**가장 작은 변경**: `.claude/docs/adr/0001-skill-dependency-classification.md` 한 장 작성. Hard 의존(work-plan-start, feature-check) vs Soft 의존(나머지) 분리만이라도.

### 🟡 3위: Alignment의 Grilling 단계

`product-review` 가 6Q 검증을 하지만, **Matt의 grilling**(권장 답안을 함께 제시하면서 결정 트리 끝까지 인터뷰)은 없음. `work-plan`도 req.md를 "이미 있다"고 가정하고 시작.

**가장 작은 변경**: `product-review` Step 0 또는 Step 1.5에 "사용자 인터뷰" 섹션 추가 — 한 번에 한 질문 + 권장 답안 패턴.

---

## A. 즉시 개선 가능 — 기존 Skill 보강

### A1. `product-review` SKILL.md에 grilling 단계 추가
**위치**: 글로벌 `~/.claude/skills/product-review/SKILL.md`
**변경**: Step 0 또는 1.5에 다음 추가
```markdown
### Step X: 사용자 인터뷰 (Grilling, 선택적)

요구사항이 모호하거나 "왜"가 약하면 grilling 모드 진입.
한 번에 한 질문 + 권장 답안을 함께 제시. 사용자 확인 → 다음 분기.

질문 트리:
1. 누가 가장 자주 이 기능을 쓰나? (페르소나)
2. 그 사람이 지금은 어떻게 해결하고 있나? (현재 행태)
3. 이 기능 없이는 무엇이 불가능한가? (Job to be done)
4. 비슷한 요청 빈도는? (priority signal)
```
**예상**: 30분

### A2. `work-plan`에 CONTEXT.md 자동 갱신 단계
**위치**: 글로벌 `~/.claude/skills/work-plan/SKILL.md`
**변경**: WORK-SPEC.md 작성 후 Step에 "신규 도메인 용어 발견 시 `.claude/CONTEXT.md`에 추가 제안" 추가. req.md에서 반복되는 고유명사 자동 추출 → 사용자에게 등록 여부 확인.
**예상**: 20분

### A3. `debugger` 에이전트에 Matt의 5-phase + 10-loop 명시
**위치**: 글로벌 `~/.claude/agents/debugger.md`
**변경**: 현재는 "스택 트레이스 → 원인 → 수정" 단순 흐름. 다음으로 교체:
```
Phase 1. 재현 (deterministic loop 만들기) ← 모든 것의 전제
Phase 2. 최소화 (failing input 줄이기)
Phase 3. 가설 3~5개 ranked + 사용자 승인
Phase 4. 계측 (한 번에 한 변수)
Phase 5. fix + 회귀 테스트
```
+ Loop 옵션 우선순위 (failing test → curl → CLI → headless → ... → HITL bash) 명시.
**예상**: 40분

### A4. `self-review` 에이전트 실행 순서 명문화
**위치**: 글로벌 `~/.claude/commands/self-review.md`
**변경**: 4명 에이전트 (convention/performance/security/test-coverage) 가 현재 단순 병렬. 다음으로 정렬:
- Phase 1: convention (네이밍/구조 기초)
- Phase 2: performance + security (병렬)
- Phase 3: test-coverage (앞 결과 종합)
**예상**: 15분

### A5. `code-refactor` 에이전트에 Matt 어휘 흡수
**위치**: 글로벌 `~/.claude/agents/code-refactor.md`
**변경**: 코드 스멜 14종 그대로 두고, Matt의 6개 어휘(Module/Interface/Depth/Seam/Adapter/Leverage) 박스를 상단에 추가. **Deletion test** ("이 모듈 지우면 복잡도가 사라지나?")를 진단 도구로 추가.
**예상**: 30분

---

## B. 신규 Skill 후보

### B1. `/setup-til-skills` — Composability/Setup
**위치**: 로컬 `.claude/skills/setup-til-skills/` (TIL 전용)
**역할**: 새 PC에서 클론 직후 한 번 실행하면 다음을 묻고 세팅
1. ATLASSIAN_API_TOKEN 환경변수 확인 → 없으면 입력 안내
2. Slack/Jira MCP 연결 상태 점검
3. `.claude/hooks/*.sh` 실행 권한 부여
4. `block-dangerous-*.sh` settings.json 등록 확인
5. Docker Desktop / mysqlsh 설치 점검
6. Confluence 페이지 ID 입력 (work-log/work-share용)

산출물: `.claude/docs/setup-state.json` (어떤 hard dependency가 충족됐는지)
**예상**: 2시간

### B2. `/zoom-out` — Architecture Hygiene
**위치**: 글로벌 `~/.claude/skills/zoom-out/`
**역할**: Matt의 zoom-out 그대로 차용. `disable-model-invocation: true` 단순 명령.
프롬프트:
> "이 코드 영역을 잘 모릅니다. 한 단계 추상화를 올려서, 관련 모듈과 호출자(caller) 맵을 **CONTEXT.md의 도메인 용어**로 설명해주세요."

**예상**: 5분 (거의 한 줄 skill)

### B3. `/caveman` (또는 `/동굴인`) — Context Compression
**위치**: 글로벌 `~/.claude/skills/caveman/`
**역할**: 응답에서 filler/pleasantry 제거. 75% 토큰 절약 모드. Matt의 캐릭터 그대로 차용 가능.
한국어 변형 시 "사실은", "음", "아무튼", "그래서", "혹시" 등 한국어 filler도 drop 규칙 추가.
**Auto-clarity exception**: 보안/비가역 액션은 일시 해제.
**예상**: 30분

---

## C. 인프라/규약 변경

### C1. `.claude/CONTEXT.md` 한 장 작성 ★최우선
프로젝트 ubiquitous language 정의. 약 30줄로 충분.
```markdown
# TIL Context

## Language
**Track**: 하나의 Jira 이슈에 묶인 작업 추적 단위. `.claude/tracks/{id}/`에 문서 묶음.
_Avoid_: 작업, 태스크

**WORK-SPEC**: /work-plan이 생성하는 작업 명세서. Matt Pocock의 PRD에 해당.
_Avoid_: 스펙, 명세, requirements

**Phase**: WORK-SPEC 내부의 단계. 보통 4~6개. /work-plan-start가 순차 실행.

**Sub-track**: 같은 Jira 이슈 아래 둔 보조 track (병렬 작업 시).

## Relationships
- 한 Jira 이슈 → 하나 이상의 Track
- Track → WORK-SPEC + FEATURE-CHECKLIST + PLAN + ARCHITECTURE + SPEC + SELF-REVIEW + QA-SCENARIOS
- /work-plan → /work-plan-start → /self-review → /qa-scenario → /pr

## Flagged ambiguities
- "Plan"이 PLAN.md(번호 4)와 plan mode를 둘 다 가리킴 → PLAN.md 사용 시 항상 풀네임
```
**예상**: 20분

### C2. `.claude/docs/adr/0001-skill-dependency-classification.md`
Hard vs Soft dependency 명시. ADR 한 장.
```markdown
# ADR 0001: Skill Hard/Soft Dependency 분리

## Status: Accepted

## Context
일부 skill은 사전 조건 없이 작동 불가능하다. 그 외는 graceful degrade.

## Decision
| Skill | 분류 | 사전 조건 |
|-------|------|---------|
| work-plan-start | Hard | WORK-SPEC.md 존재 |
| feature-check | Hard | FEATURE-CHECKLIST.md 존재 |
| work-log/work-share/meeting-notes/slack-to-confluence | Hard | ATLASSIAN_API_TOKEN |
| jira-report/jira-notify | Hard | Jira MCP |
| slack-* (digest/remind/to-jira/to-confluence/...) | Hard | Slack MCP |
| figma-read | Hard | figma-team MCP |
| browser-debug | Hard | Chrome MCP 또는 Playwright |
| db-inspect/db-tune | Hard | mysqlsh + 로컬 DB |
| 그 외 | Soft | — |

Hard skill의 SKILL.md 첫 줄에 "Requires: X" 명시.
Soft skill은 명시하지 않음 (cargo culting 방지).

## Consequences
- 새 PC 클론 시 어떤 skill이 동작 가능한지 즉시 판별 가능
- /setup-til-skills 가 이 ADR을 읽고 setup 항목 결정
```
**예상**: 25분

### C3. `block-dangerous-claude-dir.sh` 추가
**위치**: `.claude/hooks/`
**역할**: CLAUDE.md 규칙 "**.claude/는 git add 하지 않음**"을 hook으로 강제. `git add .claude/...` 패턴 차단.
**예상**: 15분

### C4. AGENT-BRIEF 양식을 WORK-SPEC.md 양식에 흡수
**위치**: `~/.claude/skills/work-plan/SKILL.md`
**변경**: WORK-SPEC.md 템플릿의 "변경 대상" 섹션을 file path 기반 → interface-level 기반으로 수정.
- ❌ "변경 파일: src/main/java/.../FooService.java:line 142"
- ✅ "변경 인터페이스: `FooService.create(...)` 의 시그니처에 `String tenantId` 추가, `FooNotFoundException` 추가"

file path는 보조 정보로만.
**예상**: 30분

### C5. 워크플로우 다이어그램 (`.claude/docs/workflow-overview.md`)
모든 skill의 호출 관계를 SVG 1장으로. work-plan → work-plan-start → self-review → qa-scenario → pr 메인 라인 + 사이드 (debugger, code-refactor, test-generator) 표기.
**예상**: 1시간

---

## 글로벌 vs 로컬 분리 점검

### 현재 분리는 대체로 합리적

| 카테고리 | 위치 | 판단 |
|---------|------|------|
| workflow (work-plan, work-plan-start, track-status) | 글로벌 ✓ | 모든 프로젝트 공통 |
| QA/검증 (qa-scenario, browser-debug, feature-check) | 글로벌 ✓ | 일반화 가능 |
| 협업 (slack-*, jira-*, work-log) | 글로벌 ✓ | 회사 도구 공통 |
| 학습 (cs-*, weekly-retro) | 로컬 ✓ | TIL 특화 |
| 디자인 (pencil-*) | 로컬 ✓ | 디자인 프로젝트 시에만 |

### 승격/강등 검토 대상

| 현재 위치 | 대상 skill | 권장 | 근거 |
|---------|----------|------|------|
| 로컬만 | `product-review` | **글로벌로 승격** | 모든 팀이 "왜 만드는가"를 검증해야 함 |
| 로컬만 | `ai-slop-detect` | **글로벌로 승격** | AI 코드 생성이 보편화됨, 모든 프로젝트에 유효 |
| 로컬만 | `smart-session` | **글로벌로 승격** | 세션 관리는 프로젝트 무관 |
| 로컬만 | `3ai-plan` | 로컬 유지 | 토큰 비용 큼, 프로젝트별 선택 적절 |
| 글로벌 | `guhada-common-convention`, `java-code-rules` | 검토 | 회사 코드 컨벤션 — TIL에서는 거의 안 씀. 회사 프로젝트 전용으로 빼야 할 수 있음 |

---

## 실행 우선순위 Top 5

| # | 항목 | 카테고리 | 효과 | 소요 | 의존성 |
|---|------|---------|------|------|------|
| 1 | C1. `.claude/CONTEXT.md` 작성 | 인프라 | Context Compression 즉시 보강 | 20분 | 없음 |
| 2 | C2. ADR 0001 (Hard/Soft Dependency) | 인프라 | Composability 기반, B1의 입력 | 25분 | 없음 |
| 3 | A1. `product-review` grilling 단계 + 글로벌 승격 | 보강 + 재배치 | Alignment 강화 | 45분 | 없음 |
| 4 | A3. `debugger` 5-phase + 10-loop 명시 | 보강 | Feedback Loop 깊이 강화 | 40분 | 없음 |
| 5 | B1. `/setup-til-skills` 신규 | 신규 | Composability 완성 | 2시간 | C2 선행 |

총 4시간 정도. 1~4번은 의존성 없어 병렬 가능.

**그 다음 단계**: B2 `/zoom-out`(5분), B3 `/caveman`(30분), C3 git-add hook(15분), C4 AGENT-BRIEF 흡수(30분) — 모두 짧고 즉시 효과.

---

## 한눈에 보는 Before/After

```
[Before]
강함  ████  Feedback Loop · Guardrail
중간  ▓▓▓▓  Alignment · Architecture · Workflow State
약함  ▓▓░░  Context Compression · Composability

[After Top 5 적용]
강함  ████  Feedback Loop · Guardrail · Composability(setup skill 후)
중간  ████  Alignment(grilling) · Architecture · Workflow · Context Compression
약함  ░░░░  없음
```

---

## 참고

- 원본 분석: [cs/tool/mattpocock-skills-harness.md](../../cs/tool/mattpocock-skills-harness.md)
- 원본 저장소: https://github.com/mattpocock/skills
- 작성일: 2026-04-29