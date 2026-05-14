# ADR 0003 — Skills 6-Bucket 디렉토리 구조

**Status**: Accepted
**Date**: 2026-05-14
**Related**: ADR 0002 (Track as Skill Container), [mattpocock/skills](https://github.com/mattpocock/skills)

---

## Context

`~/.claude/skills/` (글로벌)과 `TIL/.claude/skills/` (TIL)에 각각 37, 34개 skill이 평탄 구조(flat)로 있었다. 다음 문제가 surface:

1. **카탈로그 외부 공유 시 노출 정책 모호** — `guhada-common-convention` (회사 코드 컨벤션)이나 `cs-guide-writer` (TIL 한정)이 외부 README에 그대로 노출됨.
2. **드래프트/폐기 분리 부재** — 실험 중인 skill이 동작 가능한 것과 섞여 있음. `*-workspace` 변형 같은 구버전이 사용 가능한 것처럼 보임.
3. **사용 빈도 시각화 부재** — 일일 사용 skill과 가끔 쓰는 skill이 같은 레벨에 있음.

Matt Pocock의 skills 저장소는 6-bucket 구조로 위 문제를 해결한다.

## Decision

`skills/` 직하에 6개 버킷을 둔다:

```
skills/
├── engineering/    ← 일일 코드 작업 (외부 공개)
├── productivity/   ← 일일 워크플로우 (외부 공개)
├── misc/           ← 가끔 사용 (외부 공개)
├── personal/       ← 개인/회사 특화 (외부 노출 X)
├── in-progress/    ← 드래프트 (외부 노출 X)
└── deprecated/     ← 폐기 (외부 노출 X)
```

각 버킷에 `README.md`. top-level `README.md`는 4가지 실패 모드 매핑 + 공개 버킷 3종만 노출.

### 분류 기준

| Bucket | 기준 |
|--------|------|
| `engineering/` | 일일 코드 작업 (work-plan, qa-scenario, tdd 변형, browser-debug, atomic skill 등) |
| `productivity/` | 코드 외 일일 워크플로우 (work-log, track-status, caveman, sync-global 등) |
| `misc/` | 일일은 아니지만 보존할 만함 (docker-up, ssh-server-inspect, security-audit 등) |
| `personal/` | 개인 셋업 / 회사 한정 / TIL 한정 (cs-*, guhada-*, ai-harness-monitor 등) |
| `in-progress/` | 드래프트 (완성 후 적절한 버킷 이동) |
| `deprecated/` | 폐기 (즉시 폴백 필요 시 보관, 호출 비권장) |

### 노출 정책

- 공개 3 버킷(engineering/productivity/misc)의 skill은 top-level README에 등록.
- 비공개 3 버킷(personal/in-progress/deprecated)의 skill은 등록하지 않는다.
- skill discovery는 디렉토리 깊이 무관 (Claude Code 자체가 nested 인식).

## Consequences

**Positive**
- 카탈로그 외부 공유 시 personal/in-progress/deprecated 자동 제외 → 보안 + 명료성.
- 새 skill 작성 시 분류 강제 → 책임 범위 명확화.
- `in-progress/`로 드래프트를 격리 → 실험 안전 zone.
- `deprecated/`에 구버전 보존 → 회귀 시 즉시 폴백.

**Negative**
- 기존 평탄 구조 사용자에게는 학습 비용 (몇 개월 적응 필요).
- skill 1개의 분류가 모호한 경우 발생 (engineering vs productivity 경계 — 판단 필요).
- 동기화(sync-global)와 카탈로그 자동 생성 스크립트 갱신 필요.

## Implementation

PR 1 (완료):
- 글로벌 ~/.claude/skills/ + TIL .claude/skills/ 양쪽 적용
- 글로벌 37 skill 분류: engineering(14) + productivity(9) + misc(7) + personal(4) + deprecated(3)
- TIL 34 skill 분류: engineering(14) + productivity(11) + misc(4) + personal(5)
- 각 버킷 README.md + top-level README.md 작성

PR 2 (완료):
- 신규 atomic skill 6개 모두 `engineering/`에 추가

PR 6 (예정):
- `~/.claude/docs/skills-and-agents-catalog.md` 갱신
- `.claude/docs/skills-commands-agents-catalog.md` 갱신
- `WORKFLOW-GUIDELINE.md` 갱신

## 신규 skill 추가 시 분류 가이드

1. **공개해도 되나? (외부 PC, 다른 사람이 볼 수 있나?)**
   - No → `personal/`
   - Yes → 다음
2. **드래프트인가? (아직 평가 못 받음)**
   - Yes → `in-progress/`
   - No → 다음
3. **폐기된 것의 후속인가? 또는 폴백용 보존인가?**
   - Yes → `deprecated/`
   - No → 다음
4. **일일 코드 작업에 쓰나?**
   - Yes → `engineering/`
   - No → 다음
5. **일일 워크플로우 (코드 외)인가?**
   - Yes → `productivity/`
   - No → `misc/`

## Related

- ADR 0001 — Skill Hard/Soft Dependency 분류
- ADR 0002 — Track as Skill Container
- [mattpocock/skills](https://github.com/mattpocock/skills) — bucket 구조의 원본
- `.claude/plans/happy-twirling-iverson.md` — 본 재구성 plan
