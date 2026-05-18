---
name: to-issues
description: 계획/PRD를 issue tracker의 독립 픽업 가능한 이슈로 분해한다. tracer bullet vertical slice 사용. "이슈로 분해", "PRD 쪼개기", "vertical slice", "FEATURE-CHECKLIST 생성" 트리거.
---

# To Issues

계획을 vertical slice (tracer bullet)로 분해한다. 각 slice는 모든 통합 레이어를 가로지르는 좁지만 완전한 경로.

> 영감 출처: [mattpocock/skills/engineering/to-issues](https://github.com/mattpocock/skills/blob/main/skills/engineering/to-issues/SKILL.md). Track 시스템 + FEATURE-CHECKLIST 통합.

issue tracker 및 triage 라벨이 설정되어 있어야 한다. 없으면 `/setup-til-skills` 먼저.

## 절차

### 1. 컨텍스트 수집

대화 컨텍스트에 이미 있는 것 사용. 사용자가 이슈 참조 (이슈 번호/URL/path)를 인자로 주면 issue tracker에서 가져와서 본문 + 코멘트 전체 읽기.

### 2. 코드베이스 탐색 (선택)

아직 탐색 안 했으면 현재 상태 이해. 이슈 제목/설명에 **CONTEXT.md 도메인 어휘**. 건드릴 영역의 ADR 존중.

### 3. Vertical slice 초안

계획을 **tracer bullet** 이슈로 분해. 각 이슈 = 모든 레이어(스키마, API, UI, 테스트)를 끝까지 가로지르는 얇은 vertical slice. 한 레이어의 horizontal slice X.

Slice는 'HITL' 또는 'AFK':
- **HITL** — 사람 상호작용 필요 (아키텍처 결정, 디자인 리뷰 등)
- **AFK** — 사람 개입 없이 구현 + 머지 가능

가능하면 AFK 선호.

**Vertical slice 규칙**:
- 각 slice는 모든 레이어를 가로지르는 좁지만 **완전한** 경로
- 완료된 slice는 그 자체로 demo 또는 검증 가능
- 두꺼운 slice 몇 개보다 얇은 slice 여러 개 선호

### 4. 사용자 퀴즈

제안된 분해를 번호 매겨진 리스트로 제시. 각 slice:

- **Title**: 짧은 설명적 이름
- **Type**: HITL / AFK
- **Blocked by**: 어떤 slice가 먼저 완료되어야 하나
- **User stories covered**: 어떤 user story가 다뤄지나 (원자료에 있으면)

사용자에게 묻기:
- granularity가 적절한가? (너무 굵음 / 너무 잘음)
- 의존성 관계가 맞나?
- 합칠/쪼갤 slice 있나?
- HITL/AFK 라벨이 맞나?

사용자가 승인할 때까지 반복.

### 5. 발행

승인된 slice마다 issue tracker에 새 이슈 발행. 의존성 순서대로 발행해서 "Blocked by"에 실제 이슈 ID 참조.

### 이슈 템플릿

```markdown
## Parent

부모 이슈 참조 (원자료가 기존 이슈면. 아니면 생략)

## 무엇을 만드는가 (What to build)

이 vertical slice의 end-to-end 동작 설명. 레이어별 구현 X.

특정 파일 경로/코드 스니펫 회피 — 빠르게 stale 됨.
예외: prototype 산출물 (state machine, reducer, schema, type shape) → inline + "prototype에서 왔음" 노트.

## 수용 기준 (Acceptance criteria)

- [ ] 기준 1
- [ ] 기준 2
- [ ] 기준 3

## 차단 요소 (Blocked by)

- 차단 이슈 #N 참조 (있으면)

또는 "차단 없음 - 즉시 시작 가능"
```

부모 이슈를 close하거나 수정하지 않는다.

## Track 시스템 통합

`/work-plan` 내부에서 호출되면:

- 산출물은 `3_FEATURE-CHECKLIST.html`로 매핑 (사용자/QA 관점 체크리스트로 압축)
- 각 slice → FEATURE-CHECKLIST 항목 1개
- HITL slice는 별도 표시 (`[HITL]` prefix)

산출 HTML 문서는 `html-doc` 스킬의 규칙을 따라 자체 완결 HTML로 작성한다. 체크리스트 항목은 `html-doc/references/components.html`의 배지 컴포넌트(badge-ok/warn/err)로, HITL/AFK 구분은 상태 배지로 표현한다. 산출 HTML 문서에는 html-doc 스킬의 시각화 가이드에 따라 작업 흐름·구조를 인라인 SVG 다이어그램으로 1개 이상 포함한다.

대형 Track의 경우 sub-track으로 slice 그룹화 가능:
- Track `TECH-22386-shipment/`
  - sub-track `TECH-22386-shipment-tracking/` (slice 1~3)
  - sub-track `TECH-22386-shipment-cancellation/` (slice 4~6)

## 안티 패턴

- ❌ Horizontal slice ("DB 스키마만", "UI만")
- ❌ "epic" 하나에 모든 걸 묶기
- ❌ "구현 디테일" 이슈 ("FooService.java 수정")
- ❌ HITL/AFK 미분류
- ❌ 차단 요소 미명시 (의존 그래프 손실)
