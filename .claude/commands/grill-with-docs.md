---
description: Grill With Docs - 도메인 모델 대조 grilling + CONTEXT.md/ADR 인라인 갱신
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# Grill With Docs

`grill-with-docs` skill을 호출하여 사용자 계획/요구사항을 도메인 모델(.claude/CONTEXT.md, ADR)에 대조 검증하고, 결정이 굳어지는 시점에 CONTEXT.md/ADR을 인라인 갱신해줘.

한 번에 한 질문씩 grilling. 권장 답안을 함께 제시. 코드로 답할 수 있으면 묻지 말고 탐색.

상세 절차: `engineering/grill-with-docs/SKILL.md` 참조.

## 실행 방법

```
/grill-with-docs                       # 현재 대화 컨텍스트로 grilling
/grill-with-docs path/to/req.md       # 특정 요구사항 파일 기준
```

## 종료 조건

- 모든 결정 분기가 풀렸을 때
- 사용자가 "OK, 충분" 명시
- 코드 탐색으로 자동 해결 가능한 것만 남음
