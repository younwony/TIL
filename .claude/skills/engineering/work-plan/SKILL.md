---
name: work-plan
description: req.md 또는 구두 요구사항을 분석하여 Track에 WORK-SPEC.md + FEATURE-CHECKLIST.md + REQ-SNAPSHOT.md를 생성한다. 내부에서 grill-with-docs → to-prd → to-issues atomic skill을 조합하여 호출하는 wrapper. "/work-plan", "작업 계획", "구현 계획 세우기", "WORK-SPEC 생성", "req.md 분석", "스펙 정리", "설계해줘" 요청에 트리거.
---

# Work Plan (Atomic Wrapper)

req.md를 입력으로 받아 Track에 표준 문서 묶음(`1_REQ-SNAPSHOT.md`, `2_WORK-SPEC.md`, `3_FEATURE-CHECKLIST.md`)을 생성한다.

본 skill은 **wrapper**다. 실제 작업은 atomic skill들이 수행한다 — ADR 0002 (Track as Skill Container) 참조:

- `/grill-with-docs` — 요구사항 모호 시 grilling + CONTEXT.md/ADR 갱신
- `/to-prd` — 합성된 PRD를 WORK-SPEC.md 양식으로 출력
- `/to-issues` — vertical slice 분해를 FEATURE-CHECKLIST.md로 매핑

기존 호출 인터페이스(`/work-plan`, 플래그)는 그대로 유지된다.

> 구버전이 필요하면 `deprecated/work-plan-legacy/`. 단 호출 비권장.

## 실행 방법

```
/work-plan                          # 프로젝트 루트의 req.md 사용
/work-plan path/to/req.md           # 지정 경로의 req.md 사용
/work-plan --cross-check            # 크로스 체크 강제 실행
/work-plan --no-cross-check         # 크로스 체크 스킵
/work-plan --design-review          # design-reviewer 에이전트 활성화
/work-plan --skip-grilling          # grilling 단계 스킵 (요구사항 명확할 때)
```

## 절차

### Step 1 — req.md 읽기

`$ARGUMENTS`가 있으면 그 경로, 없으면 프로젝트 루트의 `req.md`. 둘 다 없으면 사용자에게 구두 요구사항을 받는다.

형식 무관 (자연어, 목록, 표).

### Step 2 — Track 설정

현재 git branch에서 Jira 번호 추출 → `.claude/tracks/{JIRA번호}-{sub-task-title}/` 생성/재사용.

- 같은 Jira 번호의 Track이 여럿이면 AskUserQuestion으로 sub-task title 선택
- Track 디렉토리 생성 + `metadata.json` 초기화 (`current_phase=0`, `total_phases` 미정)

### Step 3 — REQ-SNAPSHOT 보존

원본 req.md를 `1_REQ-SNAPSHOT.md`로 Track에 복사. 메타데이터 헤더 (Track ID, 원본 경로, 스냅샷 일시) 추가.

### Step 4 — 요구사항 grilling (`/grill-with-docs`)

`--skip-grilling`이 없고, 다음 신호 중 하나가 있으면 grilling 모드 진입:
- req.md에 "TBD" / "추후 결정" 같은 명시적 미정 영역
- 사용자가 구두로만 설명 (req.md 부재)
- 도메인 용어가 `.claude/CONTEXT.md`에 없는 신규 용어 다수 (>= 3개)

진입 시 `/grill-with-docs` skill을 호출. atomic skill 산출물:
- CONTEXT.md 신규 용어 추가
- 필요 시 새 ADR (`.claude/docs/adr/`)

grilling 종료 후 결정 사항을 Step 5의 입력으로 전달.

### Step 5 — WORK-SPEC.md 생성 (`/to-prd`)

`/to-prd` skill을 호출하여 PRD를 `2_WORK-SPEC.md` 양식으로 생성:

```markdown
## 문제 정의 (Problem Statement)
## 해결 (Solution)
## 사용자 스토리 (User Stories)
## 구현 결정 (Implementation Decisions)
   ### 3-1. 변경 인터페이스 (Agent Brief)
   (file path 대신 interface-level)
## 테스트 결정 (Testing Decisions)
## 범위 외 (Out of Scope)
## 추가 노트 (Further Notes)
```

Track의 `2_WORK-SPEC.md`로 저장. 헤더에 Track ID + 작성일 추가.

### Step 6 — FEATURE-CHECKLIST.md 생성 (`/to-issues`)

`/to-issues` skill을 호출하여 vertical slice 분해 → `3_FEATURE-CHECKLIST.md`로 매핑.

각 slice → 체크리스트 항목 1개:
- HITL slice는 `[HITL]` prefix
- AFK slice는 prefix 없음
- 차단 요소(Blocked by) 표시

### Step 7 — AI 크로스 체크 (Gemini/Codex)

복잡도 게이트 기본 동작:
- **Light 모드** (req.md ≤ 100 lines, 변경 파일 ≤ 5 예상): 크로스 체크 스킵
- **Full 모드** (그 외): 크로스 체크 실행

`--cross-check` / `--no-cross-check` 플래그로 강제 가능.

Gemini/Codex 호출은 `gemini-check` / `codex-check` 하네스에 위임 (정책: CLAUDE.md "AI 호출" 섹션). 두 호출은 병렬.

크로스 체크 결과는 WORK-SPEC.md 끝에 `## 크로스 체크 (Gemini/Codex)` 섹션으로 첨부. 중대한 누락/위험 발견 시 사용자에게 surface.

### Step 8 — design-reviewer (선택)

`--design-review` 플래그가 있을 때만 `design-reviewer` 에이전트 디스패치. SQL 쿼리 성능, 디자인 패턴, 로깅/롤백 Ops 관점 분석. 결과는 WORK-SPEC.md에 첨부.

### Step 9 — 산출물 검증

- [ ] `1_REQ-SNAPSHOT.md` 존재
- [ ] `2_WORK-SPEC.md` 존재 + AGENT-BRIEF 양식 (interface-level)
- [ ] `3_FEATURE-CHECKLIST.md` 존재 + 사용자/QA 관점 항목
- [ ] CONTEXT.md 갱신 (grilling 발생 시)
- [ ] 새 ADR 작성 (필요 시)

검증 후 사용자에게 다음 단계 안내: "`/work-plan-start`로 구현을 시작하세요."

## Atomic Skill 직접 호출 권장

Track 안에 들어가지 않는 즉흥 작업이라면 atomic skill을 직접 호출:

- 용어만 정밀화하고 싶음 → `/grill-with-docs` 단독
- 대화 → PRD만 → `/to-prd` 단독
- 계획 → 이슈 분해만 → `/to-issues` 단독

이 경우 Track 디렉토리는 생성하지 않는다.

## Track 시스템과의 관계 (ADR 0002)

- 본 skill은 **Track 컨테이너에 atomic skill 산출물을 배치하는** 책임만 가진다.
- atomic skill 자체의 동작은 본 skill이 모른다 — 변경 시 atomic skill의 SKILL.md만 수정.

## 관련

- `/work-plan-start` — 본 skill의 산출물을 입력으로 구현 단계 진행
- `/grill-with-docs`, `/to-prd`, `/to-issues` — 내부 호출되는 atomic skill
- ADR 0002 — Track-as-Skill-Container
- `.claude/CONTEXT.md` — ubiquitous language (Track, WORK-SPEC, Phase 등)
