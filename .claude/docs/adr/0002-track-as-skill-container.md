# ADR 0002 — Track을 Atomic Skill Container로 재정의

**Status**: Accepted
**Date**: 2026-05-14
**Related**: ADR 0001 (Skill Hard/Soft Dependency), [mattpocock/skills](https://github.com/mattpocock/skills)

---

## Context

기존 Track 시스템은 `.claude/tracks/{track_id}/` 디렉토리에 번호 매겨진 문서 8종(REQ-SNAPSHOT, WORK-SPEC, FEATURE-CHECKLIST, PLAN, ARCHITECTURE, SPEC, SELF-REVIEW, QA-SCENARIOS)을 모아두고, 사용자는 `/work-plan` → `/work-plan-start` → `/self-review` → `/qa-scenario` → `/pr` 흐름을 따라간다.

Matt Pocock의 skills 저장소는 정반대 철학을 가진다: 작은 atomic skill (grill-with-docs, tdd, diagnose, triage, to-prd, to-issues, prototype, zoom-out 등)을 자유롭게 조합하여 사용자 판단에 맡긴다.

전면 흡수 결정 (대화 2026-05-14) 시 두 시스템의 충돌이 surface 했다:
- Track은 "정해진 흐름이 있는 큰 작업"에 강함
- Atomic skill은 "즉흥적/실험적 작업"에 강함
- 사용자가 어느 한쪽만 쓰면 다른 쪽의 이점을 잃음

선택지:
1. Track 시스템 폐기, atomic skill만 (큰 작업의 일관성 손실)
2. Track 시스템 유지, atomic skill 별도 (두 시스템 모두 갖춤 — 혼란)
3. **Track을 atomic skill의 컨테이너로 재정의** ← 선택

## Decision

Track 디렉토리/문서는 그대로 유지하되, 의미를 재정의한다:

**Track = "여러 atomic skill 호출의 컨텍스트 컨테이너"**.

- `.claude/tracks/{track_id}/` 디렉토리 = 산출물 보관소 (변경 없음)
- 번호 매겨진 문서 8종 = atomic skill 산출물에 매핑 (변경 없음)
- `/work-plan` skill 내부 = atomic skill (`grill-with-docs` → `to-prd` → `to-issues`)을 호출하는 **wrapper**로 재작성
- `/work-plan-start` skill 내부 = atomic skill (`tdd`, `diagnose`)을 호출하는 wrapper로 재작성
- atomic skill 자체는 Track 외부에서도 직접 호출 가능 (사용자 자유)

### 결과 매핑

| Track 문서 | 산출 atomic skill |
|-----------|------------------|
| `1_REQ-SNAPSHOT.md` | `/work-plan` 시 원본 보존 (skill 산출 X) |
| `2_WORK-SPEC.md` | `to-prd` 산출물 |
| `3_FEATURE-CHECKLIST.md` | `to-issues` 산출물 (사용자/QA 관점 압축) |
| `4_PLAN.md` | `work-plan-start` Phase 진행률 |
| `5_ARCHITECTURE.md` | `work-plan-start` 구현 완료 시 |
| `6_SPEC.md` | `work-plan-start` 구현 완료 시 |
| `7_SELF-REVIEW.md` | `/self-review` skill |
| `8_QA-SCENARIOS.md` | `/qa-scenario` skill |

## Consequences

**Positive**
- 기존 사용자는 `/work-plan` → `/work-plan-start` 흐름 그대로 사용 가능 (호환성).
- 즉흥 작업에서 `/grill-with-docs`, `/tdd` 같은 atomic skill 단독 호출 가능 (유연성).
- `/work-plan` 내부 로직이 atomic skill로 위임되어 SKILL.md가 짧아짐 (work-plan 441→~120줄 예상).
- atomic skill 각각이 작아서 개선/실험이 쉬워짐.

**Negative**
- `/work-plan` 디버깅 시 atomic skill 체인을 추적해야 함 (간접 비용).
- atomic skill 호출 순서를 사용자가 직접 결정할 때 가이드 필요 (WORKFLOW-GUIDELINE.md 결정 트리).
- atomic skill의 산출물 양식이 Track 문서 양식과 정확히 매칭되어야 (양식 drift 위험).

## Implementation

1. **PR 1 (완료)**: skills/ 6-bucket 구조 도입 (ADR 0003).
2. **PR 2 (완료)**: 신규 atomic skill 6개 작성 (grill-with-docs, triage, improve-codebase-architecture, to-prd, to-issues, prototype).
3. **PR 5 (예정)**: `/work-plan`, `/work-plan-start` 재작성. atomic skill의 wrapper로 만들기. 구버전은 `deprecated/work-plan-legacy/`로 보존.
4. **PR 6**: WORKFLOW-GUIDELINE.md에 "atomic skill 직접 호출 결정 트리" 추가.

## Related

- ADR 0001 — Skill Hard/Soft Dependency 분류
- ADR 0003 — Bucket Structure
- [mattpocock/skills](https://github.com/mattpocock/skills) — atomic skill 철학의 원본
- `.claude/docs/mattpocock-skills-application.md` — 적용 분석
- `.claude/plans/happy-twirling-iverson.md` — 본 재구성 plan
