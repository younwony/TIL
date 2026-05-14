---
description: To PRD - 현재 대화를 WORK-SPEC/PRD 양식으로 합성
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# To PRD

`to-prd` skill을 호출하여 현재 대화 컨텍스트와 코드베이스 이해를 PRD/WORK-SPEC.md 양식으로 합성해줘.

**사용자에게 다시 묻지 않음** — 이미 논의된 것만 동기화한다. 모르겠으면 `/grill-with-docs` 먼저.

상세 절차: `engineering/to-prd/SKILL.md` 참조.

## 실행 방법

```
/to-prd                                # 현재 대화 → PRD (Track에 2_WORK-SPEC.md)
/to-prd --no-publish                  # 발행 없이 초안만 출력
```

## 산출 양식

Problem / Solution / User Stories / Implementation Decisions / Testing Decisions / Out of Scope / Further Notes.

구체 파일 경로/코드 스니펫 포함 X. interface-level로 작성.
