---
name: prototype
description: 설계 검증을 위한 일회성 프로토타입 만들기. 상태/비즈니스 로직은 runnable terminal app, UI 질문은 한 라우트에서 토글 가능한 여러 UI 변형. "프로토타입", "검증용 코드", "throwaway", "UI 변형 비교" 트리거.
---

# Prototype

설계의 한 측면을 살피기 위한 **throwaway** 프로토타입을 만든다. 두 종류:

1. **상태/비즈니스 로직 프로토타입** — runnable terminal app
2. **UI 변형 프로토타입** — 한 라우트에서 토글 가능한 여러 변형

> 영감 출처: [mattpocock/skills/engineering/prototype](https://github.com/mattpocock/skills/blob/main/skills/engineering/prototype/SKILL.md).

**중요**: prototype은 **버려진다**. 머지하지 않는다. 결정만 추출해서 PRD/ADR/spec에 반영.

## 언제 사용하나

- 모듈 인터페이스의 모양이 모호할 때
- state machine / reducer 동작을 확인하고 싶을 때
- 두세 UI 변형을 보고 결정하고 싶을 때
- "이게 진짜 동작할까?"라는 의심이 있을 때

**부적합**:
- 이미 명확한 구현 — 그냥 만들면 됨
- 일회성 스크립트 — `scripts/` 폴더에 그냥 작성
- 작은 버그 수정 — `/diagnose` (debugger agent 5-phase)

## 절차 — Logic Prototype

### 1. 책임 정의

다음 둘 중 하나에 답할 한 가지 질문 명확화:

- "이 state machine이 X 입력에 Y 행동을 하는가?"
- "이 비즈니스 규칙의 엣지 케이스는 어떻게 되는가?"

다른 질문 X. 한 prototype = 한 질문.

### 2. 최소 terminal app

다음만 포함:
- 입력을 stdin/argv로 받음
- 핵심 로직 실행
- 출력 stdout

UI X. DB X. 외부 의존 X. 단일 파일이 이상적.

### 3. 시나리오 실행

미리 정한 입력 시나리오 N개로 실행. 출력 캡쳐. 사용자와 review.

### 4. 결정 추출

prototype에서 얻은 결정 (state machine 도식, reducer 코드, 타입 시그니처)을 [WORK-SPEC.html]/[PRD]/[ADR]에 inline. trim해서 결정-rich 부분만.

### 5. Prototype 삭제

산출물 파일을 삭제한다. 결정은 이미 영구 문서에 들어갔다.

## 절차 — UI Prototype

### 1. 책임 정의

"X 화면을 위한 N개 UI 변형 중 어떤 것이 사용자 의도에 맞나?"

### 2. 단일 라우트, 토글 변형

한 페이지/라우트에 N개(보통 2~3) 변형을 만든다. 토글로 전환:

```html
<select id="variant-toggle">
  <option value="A">Variant A</option>
  <option value="B">Variant B</option>
  <option value="C">Variant C</option>
</select>
<div id="variant-A">... A 마크업 ...</div>
<div id="variant-B">... B 마크업 ...</div>
```

각 변형은 **다르게** 만들어라 — 비슷한 두 변형은 비교 가치가 없다.

### 3. 사용자 walkthrough

사용자가 각 변형을 보고 토론. 결정 + 이유 캡쳐.

### 4. 결정 추출

- 채택된 변형의 spec → SPEC.md
- 트레이드오프가 컸다면 ADR

### 5. Prototype 삭제

비채택 변형 삭제. 채택된 것도 prototype 형태(토글)는 삭제하고 production 양식으로 다시 작성.

## Throwaway 표시

prototype 파일은 명확히 표시:

- 파일명에 `prototype-` prefix
- 파일 상단 주석:
  ```
  /*
   * THROWAWAY PROTOTYPE
   * Purpose: {질문}
   * Decision captured in: {SPEC.md 또는 ADR 링크}
   * DELETE after decision is finalized.
   */
  ```
- `.prototype/` 디렉토리에 격리 (있으면)

## 안티 패턴

- ❌ Prototype을 "그냥 좀 다듬어서" production으로 가져가기 — 모든 throwaway 코드는 throwaway다. 좋은 패턴이 보이면 새로 작성.
- ❌ 한 prototype에 여러 질문 답하려 하기 — 책임 분산되면 가치 없음
- ❌ Prototype에 정성 들이기 — error handling, edge case 등은 production에서. prototype은 "동작을 보여주는" 정도.
- ❌ Prototype 삭제 안 하기 — 코드베이스 오염

## Track 시스템 통합

prototype 작업 자체는 별도 Track 안 만든다. 본격 작업 (`/work-plan`) 전 검증 단계로 사용. 결정은 `2_WORK-SPEC.html`의 "Implementation Decisions" 섹션에 inline.
