# ADR 작성 형식

ADR (Architecture Decision Record) — 되돌리기 어려운 결정을 기록한다.

## 파일명

`{N}_{kebab-case-제목}.md` 형식. N은 ADR 번호.

예: `0002-track-as-skill-container.md`

## 본문 구조

```markdown
# ADR {N} — {결정 요약 (제목)}

**Status**: Accepted | Proposed | Superseded by ADR {M}
**Date**: YYYY-MM-DD
**Related**: (있으면) 관련 ADR 또는 외부 자료 링크

---

## Context

이 결정이 필요한 이유. 어떤 문제 / 제약 / 트레이드오프가 있었는가.
독자가 결정의 배경을 빠르게 이해할 수 있도록 작성.

## Decision

채택한 결정 자체. 명확하고 짧게.

(필요 시 표나 코드 예시로 보조)

## Consequences

**Positive**
- 이 결정으로 좋아진 점

**Negative**
- 이 결정의 비용 / 트레이드오프

## Implementation

(선택) 구체적 적용 방식. 어디서 어떻게 사용되는지.

## Related

- 관련 ADR
- 외부 자료 (논문, 글, GitHub issue 등)
```

## 작성 규칙

1. **한 ADR = 한 결정**. 묶지 말 것.
2. **Status는 명시적으로**. "Accepted" / "Proposed" / "Superseded by ADR M".
3. **Context는 결정 직전 상태**. 결정 이후 일어난 일은 Consequences에.
4. **Negative consequences를 솔직히 적어라**. ADR이 "오, 우리 결정이 모두 좋았어요!" 톤이 되면 신뢰도 잃음.
5. **Superseded되어도 파일을 지우지 않는다**. Status만 바꾸고 새 ADR 링크를 추가.

## ADR을 만들 시점 (Matt Pocock의 3-조건 룰)

세 조건이 **모두** 참일 때만 만든다:

1. **Hard to reverse** — 마음 바꾸면 비용이 크다
2. **Surprising without context** — 미래 독자가 "왜?" 할 만하다
3. **Real trade-off** — 진정한 대안이 있었고 특정 이유로 골랐다

하나라도 빠지면 만들지 않는다. "그냥 하면 좋아 보이는 결정"은 ADR이 아니다.

## ADR이 아닌 것

- 일시적 task 결정 → TODO.md
- 코드 구현 세부 → ARCHITECTURE.html or SPEC.md
- 도메인 용어 정의 → CONTEXT.md
- 작업 진행 상태 → PLAN.md
