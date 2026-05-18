---
name: grill-with-docs
description: 계획을 기존 도메인 모델에 대조하여 검증하는 grilling 세션. CONTEXT.md와 ADR을 인라인으로 갱신한다. 요구사항 정렬·용어 정밀화·도메인 결정 기록이 필요할 때 사용한다. "그릴미", "도메인 검증", "용어 정리", "왜 만드는지 모르겠어" 같은 맥락에 트리거된다.
---

# Grill With Docs

당신은 사용자의 계획을 끝까지 인터뷰해 공유 이해에 도달시킨다. 결정 트리의 모든 분기를 의존성 순서로 풀어낸다. 결정이 굳어지는 시점에 `CONTEXT.md`와 `docs/adr/`를 인라인으로 갱신한다.

> 영감 출처: [mattpocock/skills/engineering/grill-with-docs](https://github.com/mattpocock/skills/blob/main/skills/engineering/grill-with-docs/SKILL.md). 한국어 워크플로우 + Track 시스템에 맞춰 변형.

## 진행 규칙

- **한 번에 한 질문**. 사용자 답변 후 다음 질문.
- 모든 질문에 **권장 답안을 함께 제시**한다. 사용자가 빨리 확인하거나 반박할 수 있게.
- 코드베이스 탐색으로 답할 수 있는 질문이면 **묻지 말고 직접 탐색**한다.
- 답변이 굳어지면 **즉시** `CONTEXT.md` / ADR 갱신. 일괄 배치 금지.

## 도메인 인지

### 파일 구조

대부분 단일 컨텍스트:

```
{프로젝트 루트}/
├── .claude/
│   ├── CONTEXT.md
│   └── docs/adr/
│       ├── 0001-skill-dependency-classification.md
│       └── 0002-track-as-skill-container.md
└── src/
```

멀티 컨텍스트 (monorepo)면 `CONTEXT-MAP.md`가 루트에 있다. 각 컨텍스트는 자체 `CONTEXT.md` + `docs/adr/`를 가진다.

CONTEXT.md/ADR이 없으면 **첫 용어 / 첫 결정이 굳어질 때 lazy 생성**한다.

## 세션 진행

### 1. 용어집 대조 (Challenge against the glossary)

사용자가 쓴 용어가 기존 `CONTEXT.md` 정의와 충돌하면 즉시 짚는다.

> "용어집에서 '취소(cancellation)'는 X로 정의되어 있는데, 지금 말씀하시는 건 Y에 가까워 보입니다 — 둘 중 어느 쪽인가요?"

### 2. 모호한 용어 정밀화 (Sharpen fuzzy language)

오버로딩된 용어가 나오면 정확한 표준 용어를 제안한다.

> "지금 '계정'이라고 하셨는데 Customer를 말씀하시는 건가요, User를 말씀하시는 건가요? 둘은 다른 개념입니다."

### 3. 구체 시나리오로 압박 (Discuss concrete scenarios)

도메인 관계가 논의될 때 구체적인 엣지 케이스 시나리오를 제시해서 경계를 명확히 한다.

### 4. 코드와 교차 검증 (Cross-reference with code)

사용자의 주장이 코드와 모순되면 surface한다.

> "지금 부분 취소(partial cancellation)가 가능하다고 하셨는데, 코드는 Order 전체만 취소합니다 — 어느 쪽이 맞는지요?"

### 5. CONTEXT.md 인라인 갱신

용어가 풀리면 그 자리에서 `CONTEXT.md`를 갱신한다. **배치 금지** — 결정이 잊히기 전에 기록.

형식: [`CONTEXT-FORMAT.md`](./CONTEXT-FORMAT.md) 참조.

`CONTEXT.md`는 **순수 용어집**이다. spec, scratchpad, 구현 결정 저장소가 아니다.

### 6. ADR은 인색하게 제안

ADR은 다음 **세 조건이 모두** 참일 때만 제안한다:

1. **되돌리기 어렵다** — 나중에 마음 바꾸면 비용이 크다
2. **컨텍스트 없으면 놀라움** — 미래 독자가 "왜 이렇게 했지?" 할 만하다
3. **진짜 트레이드오프의 결과** — 진정한 대안이 있었고 특정 이유로 골랐다

하나라도 빠지면 ADR 만들지 않는다.

형식: [`ADR-FORMAT.md`](./ADR-FORMAT.md) 참조.

## Track 시스템과의 통합

- 현재 Active Track이 있으면 `{TRACK_DIR}/CONTEXT-LOCAL.md`에 Track 전용 용어를 추가할 수 있다. 단 우선 글로벌 `.claude/CONTEXT.md`에 추가를 시도하라.
- `/work-plan`이 내부에서 본 skill을 호출하면 grilling 결과가 그대로 WORK-SPEC.html 초안의 입력이 된다.
- `1_REQ-SNAPSHOT.html`에 원본 요구사항을 보존하므로 grilling 후 변경된 점만 WORK-SPEC.html에 반영하면 된다.

## 종료 조건

- 모든 결정 분기가 풀렸을 때
- 사용자가 "OK, 충분합니다"라고 명시한 시점
- 더 답할 필요가 없을 때 (코드 탐색으로 자동 해결 가능한 것만 남음)
