---
name: to-prd
description: 현재 대화 컨텍스트를 PRD로 합성한다. 사용자 인터뷰 X — 이미 논의된 것만 동기화. WORK-SPEC.html 양식과 호환. "PRD 만들어", "지금까지 정리해줘", "스펙 합성" 트리거.
---

# To PRD

현재 대화의 컨텍스트와 코드베이스 이해를 입력으로 PRD를 만든다. **사용자 인터뷰 X** — 이미 알고 있는 것만 종합한다.

> 영감 출처: [mattpocock/skills/engineering/to-prd](https://github.com/mattpocock/skills/blob/main/skills/engineering/to-prd/SKILL.md). WORK-SPEC.html 양식과 호환되도록 변형.

issue tracker와 triage 라벨이 설정되어 있어야 한다. 아니면 `/setup-til-skills` 먼저.

## 절차

### 1. 코드베이스 탐색

아직 탐색 안 했다면 현재 상태 이해. PRD 전체에 **CONTEXT.md 도메인 어휘** 사용. 건드릴 영역의 ADR 존중.

### 2. 모듈 스케치

구현을 위해 새로 만들거나 수정할 주요 module 스케치. **deep module**로 추출할 수 있는 기회 적극 탐색.

> Deep module = 단순하고 거의 변하지 않는 인터페이스 뒤에 많은 기능을 캡슐화한 것.

사용자에게 확인:
- 이 module들이 사용자의 기대와 일치하는가?
- 어떤 module에 테스트를 작성할까?

### 3. PRD 작성 + 발행

아래 템플릿 사용. issue tracker에 `ready-for-agent` 라벨로 발행. (추가 triage 불필요)

Track 시스템 사용 시: `.claude/tracks/{이슈키}-{설명}/2_WORK-SPEC.html`로 저장.

산출 HTML 문서는 `html-doc` 스킬의 규칙을 따라 자체 완결 HTML로 작성한다. `work-plan/references/workspec-template.html`을 skeleton으로 사용하고, PRD 섹션은 `<section>` 구조로, 사용자 스토리 목록은 `<ol>`로, 구현 결정 표는 `<table>`로 표현한다. 산출 HTML 문서에는 html-doc 스킬의 시각화 가이드에 따라 작업 흐름·구조를 인라인 SVG 다이어그램으로 1개 이상 포함한다.

## PRD 템플릿

```markdown
## 문제 정의 (Problem Statement)

사용자가 직면한 문제, 사용자 관점.

## 해결 (Solution)

문제 해결책, 사용자 관점.

## 사용자 스토리 (User Stories)

긴 번호 매겨진 리스트. 각 스토리 형식:

1. {actor}로서, {feature}를 원한다, {benefit}을 위해.

<예시>
1. 모바일 뱅킹 고객으로서, 내 계좌 잔액을 보고 싶다, 더 정보 있는 지출 결정을 위해.
</예시>

이 리스트는 매우 확장적이어야 하고 기능의 모든 측면을 다뤄야 한다.

## 구현 결정 (Implementation Decisions)

- 만들/수정될 module
- 변경될 인터페이스
- 개발자가 제공한 기술 명확화
- 아키텍처 결정
- 스키마 변경
- API 계약
- 특정 상호작용

**구체 파일 경로나 코드 스니펫 포함 X**. 빠르게 stale 됨.

예외: prototype이 prose보다 정확하게 결정을 인코딩한 경우 (state machine, reducer, schema, type shape) 해당 결정 안에 inline. trim해서 결정-rich 부분만 — working demo X.

## 테스트 결정 (Testing Decisions)

- 좋은 테스트의 설명 (외부 동작만 테스트, 구현 디테일 X)
- 테스트될 module
- 테스트의 prior art (코드베이스의 유사 테스트)

## 범위 외 (Out of Scope)

PRD에 포함되지 않는 것들.

## 추가 노트 (Further Notes)

기타.
```

## Track 시스템 통합

`/work-plan` skill이 내부에서 본 skill을 호출하면:

- 산출물 PRD는 `2_WORK-SPEC.html`로 저장됨
- 추가 분기: req.md → grill-with-docs → to-prd 순서로 wrap
- `3_FEATURE-CHECKLIST.html`는 PRD의 "User Stories"에서 자동 파생

## 안티 패턴

- ❌ 사용자에게 다시 물어보기. 본 skill은 합성, grilling 아님. 모르겠으면 `/grill-with-docs` 먼저.
- ❌ 파일 경로/코드 스니펫 박기. 인터페이스 레벨로.
- ❌ "TBD" / "TODO" 박기. 모르면 grill-with-docs 다시.
