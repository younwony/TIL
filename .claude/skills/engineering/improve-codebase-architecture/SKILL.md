---
name: improve-codebase-architecture
description: 코드베이스의 깊어질 수 있는(deepening) 기회를 찾는다. CONTEXT.md의 도메인 언어와 docs/adr/의 결정을 입력으로 사용. 테스트성과 AI 네비게이션 성을 높이는 리팩터링 제안. "아키텍처 개선", "구조 정리", "shallow module 찾기", "deep module 만들기" 트리거.
---

# Improve Codebase Architecture

아키텍처 friction을 surface하고 **deepening opportunity**를 제안한다 — shallow module을 deep module로 바꾸는 리팩터링. 목표는 테스트성 + AI 네비게이션 성.

> 영감 출처: [mattpocock/skills/engineering/improve-codebase-architecture](https://github.com/mattpocock/skills/blob/main/skills/engineering/improve-codebase-architecture/SKILL.md). 한국어 + CONTEXT.md/ADR 통합 변형.

## 용어집

본 skill의 제안에서는 다음 용어를 **정확히** 사용한다. 일관된 언어가 핵심이다. "component", "service", "API", "boundary" 같은 다른 용어로 drift 금지. 정의는 [LANGUAGE.md](./LANGUAGE.md).

- **Module** — 인터페이스 + 구현이 있는 것 (함수, 클래스, 패키지, 슬라이스)
- **Interface** — caller가 알아야 할 모든 것 (타입, 불변식, 에러, 순서, config)
- **Implementation** — 모듈 안 코드
- **Depth** — 인터페이스의 leverage. 작은 인터페이스 뒤에 많은 행동. **Deep** = 고 leverage. **Shallow** = 인터페이스가 구현 만큼 복잡
- **Seam** — 인터페이스가 사는 곳. 행동을 in-place 수정 없이 바꿀 수 있는 지점 ("boundary" 대신 이 단어 사용)
- **Adapter** — seam에서 인터페이스를 만족하는 구체
- **Leverage** — depth로 caller가 얻는 것
- **Locality** — depth로 maintainer가 얻는 것. 변화/버그/지식이 한 곳에 모임

**핵심 원칙** ([LANGUAGE.md](./LANGUAGE.md)에 full list):

- **Deletion test**: 이 모듈을 지운다고 상상하라. 복잡도가 사라지면 pass-through였다. 복잡도가 N개 caller에 다시 나타나면 그건 제 몫을 했다.
- **인터페이스가 테스트 표면**이다.
- **Adapter 1개 = 가설 seam. Adapter 2개 = 진짜 seam.**

본 skill은 프로젝트의 도메인 모델에 **informed**된다. 도메인 언어가 좋은 seam에 이름을 주고, ADR은 이 skill이 재논의하지 말아야 할 결정을 기록한다.

## 절차

### 1. 탐색 (Explore)

먼저 `.claude/CONTEXT.md`와 건드릴 영역의 `docs/adr/`를 읽는다.

그 다음 `Agent` 도구 (`subagent_type=Explore`)로 코드베이스를 walking 한다. 엄격한 휴리스틱 따르지 말고 유기적으로 탐색하면서 friction 지점을 노트한다:

- 한 개념을 이해하는데 작은 모듈 여럿을 오가야 하는 곳?
- 모듈이 **shallow**인 곳 — 인터페이스가 구현만큼 복잡?
- pure function을 testability 위해서만 추출했는데 진짜 버그는 그걸 어떻게 호출하는지에 숨어있는 곳 (**locality** 없음)?
- 강결합 모듈이 seam을 넘어 leak하는 곳?
- 어떤 부분이 untested 또는 현재 인터페이스로는 테스트가 어려운가?

shallow로 의심되는 것은 **deletion test**: 지우면 복잡도가 한 곳에 모이는가, 그냥 이동하는가? "모인다"가 원하는 신호.

### 2. 후보 제시

deepening opportunity를 번호 매겨진 리스트로 제시. 각 후보:

- **파일** — 어떤 파일/모듈
- **문제** — 현재 아키텍처가 일으키는 friction
- **해결** — 무엇이 바뀔지 평이한 한국어 설명
- **이익** — locality + leverage 측면에서, 그리고 테스트가 어떻게 개선될지

**도메인은 CONTEXT.md 어휘, 아키텍처는 [LANGUAGE.md](./LANGUAGE.md) 어휘**. CONTEXT.md가 "Order"를 정의했다면 "Order 인테이크 모듈"이라 부른다 — "FooBarHandler" X, "Order 서비스" X.

**ADR 충돌**: 후보가 기존 ADR과 모순되면, friction이 ADR을 재방문할 만한지 확인. 명시: "ADR-0007과 모순 — 그러나 X 이유로 재오픈할 가치 있음." ADR이 금지하는 모든 이론적 리팩터링 다 나열하지 않음.

인터페이스 제안은 아직 X. 사용자에게 묻기: "어떤 걸 더 탐색해볼까요?"

### 3. Grilling 루프

사용자가 후보를 고르면 grilling 대화로 진입. 결정 트리를 같이 walk down — 제약, 의존성, deepened 모듈의 모양, seam 뒤 무엇이 사는지, 어떤 테스트가 살아남는지.

side effect는 인라인:

- **deepened 모듈을 CONTEXT.md에 없는 이름으로 부르려 하나?** → CONTEXT.md에 추가 (grill-with-docs와 동일 규율, [CONTEXT-FORMAT.md](../grill-with-docs/CONTEXT-FORMAT.md)). 파일 없으면 lazy 생성.
- **대화 중 모호한 용어를 sharpen?** → CONTEXT.md 즉시 갱신
- **사용자가 후보를 load-bearing reason으로 거절?** → ADR 제안: "이걸 ADR로 기록해서 다음 아키텍처 리뷰에서 다시 제안되지 않도록 할까요?" 미래 explorer가 재제안 회피를 위해 필요한 reason만. 임시적인 ("지금은 아니야") 또는 self-evident한 reason은 skip. [ADR-FORMAT.md](../grill-with-docs/ADR-FORMAT.md).
- **deepened 모듈의 대안 인터페이스를 탐색하고 싶나?** → [INTERFACE-DESIGN.md](./INTERFACE-DESIGN.md).

## Track 통합

본 skill은 코드 변경을 직접 하지 않는다. 산출물은 grilling 노트 + 제안된 인터페이스 + CONTEXT.md/ADR 갱신. 실제 리팩터링은 `/work-plan` + `/work-plan-start` 사이클로 넘긴다.

## 사용 빈도

Matt Pocock 권장: **며칠에 한 번씩** 코드베이스에 본 skill 실행. 진흙공이 되기 전에 design care.
