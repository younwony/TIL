---
description: To Issues - 계획/PRD를 vertical slice (tracer bullet) 이슈로 분해
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# To Issues

`to-issues` skill을 호출하여 계획/PRD를 issue tracker에 등록할 수 있는 vertical slice 이슈로 분해해줘.

각 slice = 모든 레이어(스키마, API, UI, 테스트)를 가로지르는 좁은 경로. horizontal slice X.

상세 절차: `engineering/to-issues/SKILL.md` 참조.

## 실행 방법

```
/to-issues                       # 현재 컨텍스트의 계획 분해
/to-issues #42                  # 기존 이슈의 PRD 분해
/to-issues path/to/PRD.md      # 파일 기준
```

## 산출

HITL/AFK 라벨, 의존성 그래프 포함. issue tracker에 새 이슈 발행 (사용자 승인 후).
