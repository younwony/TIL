---
description: Prototype - 설계 검증용 throwaway 프로토타입 작성
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# Prototype

`prototype` skill을 호출하여 설계의 한 측면을 검증할 throwaway 프로토타입을 만들어줘.

두 종류:
- **Logic prototype** — state machine/reducer/비즈니스 로직 검증용 terminal app
- **UI prototype** — 한 라우트에서 토글 가능한 여러 UI 변형

**throwaway**. 결정만 추출해서 SPEC.md/ADR에 inline 후 prototype 코드 삭제.

상세 절차: `engineering/prototype/SKILL.md` 참조.

## 실행 방법

```
/prototype logic "X 상태 머신 검증"
/prototype ui "결제 화면 3가지 변형 비교"
```

## 부적합

- 이미 명확한 구현 — 그냥 만들면 됨
- 일회성 스크립트 — `scripts/` 폴더
- 작은 버그 수정 — `debugger` agent
