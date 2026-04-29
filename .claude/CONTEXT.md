# TIL Context — Ubiquitous Language

> 이 파일은 TIL 저장소의 **도메인 용어집(ubiquitous language)** 이다. 매 세션마다 같은 용어를 다시 설명하지 않도록, 프로젝트 고유 개념을 한 곳에 정의한다.
>
> 일반 프로그래밍 용어(timeout, retry 등)는 포함하지 않는다. **이 저장소 고유 개념만** 등록한다.

---

## Language

### 작업 추적

**Track**
하나의 Jira 이슈에 묶인 작업 추적 단위. `.claude/tracks/{track_id}/` 디렉토리에 관련 문서 묶음을 보관한다. 같은 Jira 이슈에 sub-track을 여러 개 둘 수 있다.
_Avoid_: 작업, 태스크, 워크플로우

**Sub-track**
같은 Jira 이슈 아래 둔 보조 Track. 병렬 작업이나 부분 분리가 필요할 때 사용. `.claude/tracks/{이슈번호}-{서브명}/` 형식.
_Avoid_: 부속 작업

**Track ID**
Track 디렉토리 이름. 보통 Jira 이슈 키 또는 `{이슈키}-{설명}` 형식 (kebab-case).

**Active Track**
`.claude/tracks/index.md` 에 등록된 현재 진행 중인 Track. `{DOC_DIR}` 결정 시 참조됨.

### 워크플로우 문서

**WORK-SPEC** (= `2_WORK-SPEC.md`)
`/work-plan` skill이 생성하는 작업 명세서. Matt Pocock의 PRD에 해당. 무엇을·왜 만드는지, 변경 인터페이스, 영향 범위를 기술한다.
_Avoid_: 스펙, 명세, requirements, PRD (한국어 문서에서는 WORK-SPEC으로 통일)

**FEATURE-CHECKLIST** (= `3_FEATURE-CHECKLIST.md`)
`/work-plan`이 함께 생성하는 사용자/QA 관점 기능 체크리스트. `/feature-check` skill이 코드 레벨에서 자동 검증할 때 입력으로 사용.

**REQ-SNAPSHOT** (= `1_REQ-SNAPSHOT.md`)
`/work-plan` 실행 시 원본 req.md를 Track에 보존한 스냅샷.

**Phase**
WORK-SPEC 내부의 단계. 보통 4~6개. `/work-plan-start`가 순차 실행. PLAN.md(`4_PLAN.md`)에 Phase별 진행률이 기록된다.
_Avoid_: 단계 (PLAN.md 외의 일반적 단계는 "스텝"으로)

**INDEX** (= `0_INDEX.md`)
Track 디렉토리에 문서 2개 이상 시 자동 생성되는 문서 인덱스.

### 워크플로우 단계

**Solo / Standard / Coordinator**
`/work-plan-start` 실행 모드 3종. 변경 파일 수에 따라 자동 선택 (1~2개 / 3~4개 / 5개+).

**HITL / AFK** (Human-In-The-Loop / Away-From-Keyboard)
HITL = 사람의 design 결정이 필요한 issue. AFK = 사양만 정해지면 에이전트가 끝까지 갈 수 있는 issue. 가능한 AFK 선호.

### 의존성 분류 (ADR 0001 참조)

**Hard dependency**
사전 조건 없이 작동 불가능한 skill의 의존성. 예: `work-plan-start`는 WORK-SPEC.md 없이 동작 불가, `work-log`는 ATLASSIAN_API_TOKEN 없이 동작 불가.

**Soft dependency**
없어도 graceful degrade 되는 의존성. 예: `code-refactor`는 CONTEXT.md 없어도 동작.

### 도구 분류

**Skill** (vs **Command**)
`{name}/SKILL.md` 양식의 자동 트리거 가능한 워크플로우. Command는 슬래시 호출 전용.

**Agent**
독립 컨텍스트에서 동작하는 sub-agent. 글로벌 11종(`code-refactor`, `debugger`, `test-generator`, `review-*` 4종, `cs-*` 2종, `jira-updater`, `design-reviewer`).

**Advisor**
Sonnet/Opus 모델로 위임되는 판단 전용 호출. 파일 탐색·수정 없이 결과만 반환. 비용 절감 목적.

### 플랫폼/환경

**`{DOC_DIR}`**
워크플로우 문서가 저장되는 디렉토리. Active Track 수에 따라 결정 (0개=`.claude/docs/`, 1개=Track 자동 선택, 2개+=사용자 선택).

**개인 스페이스** (Confluence)
키 `~645023757`, ID `1983741954`. `work-log`/`work-share` skill의 기본 저장소.

---

## Relationships

- 하나의 **Jira 이슈** → 1개 이상의 **Track** (sub-track으로 분리 가능)
- 하나의 **Track** → 0개 이상의 워크플로우 문서 (`{N}_*.md`) + `INDEX.md`
- `/work-plan` → 생성: `REQ-SNAPSHOT`, `WORK-SPEC`, `FEATURE-CHECKLIST`
- `/work-plan-start` → 입력: `WORK-SPEC` / 생성: `PLAN`, `ARCHITECTURE`, `SPEC`
- `/self-review` → 입력: 현재 브랜치 diff / 생성: `SELF-REVIEW`
- `/qa-scenario` → 입력: 변경 파일 / 생성: `QA-SCENARIOS`
- `/pr` → 입력: 위 모든 문서 / 생성: GitHub PR

표준 흐름: `product-review` → `work-plan` → `work-plan-start` → `self-review` → `qa-scenario` → `pr`

---

## Flagged ambiguities

- **"Plan"** 이 두 가지를 가리킴:
  - `4_PLAN.md` (Phase별 구현 진행률 문서)
  - Plan Mode (Claude Code의 작업 모드)
  → 문서 내에서는 항상 `PLAN.md` / "플랜 모드"로 풀어 쓴다.

- **"Spec"** 도 두 가지:
  - `WORK-SPEC.md` (작업 명세서, 작업 시작 전)
  - `6_SPEC.md` (기능 명세, 구현 완료 후)
  → 항상 풀네임으로.

- **"Track"** vs **"Workflow"**:
  - Track은 단위 (디렉토리 + 문서 묶음)
  - Workflow는 흐름 (skill 간 호출 순서)
  → 혼용 금지.

- **"리뷰"** 가 가리키는 대상이 다름:
  - `/self-review`: 현재 브랜치 자체 리뷰
  - `/review-pr`: 특정 PR 번호 대상
  - `/team-review`: 4명 에이전트 병렬
  - `product-review`: 코드가 아닌 제품 결정 검증
  → 슬래시명 그대로 사용해 구분.
