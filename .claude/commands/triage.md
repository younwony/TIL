---
description: Triage - 이슈 5-state machine 분류 + AGENT-BRIEF 작성
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# Triage

`triage` skill을 호출하여 issue tracker(Jira/GitHub/.scratch)의 이슈를 5-state machine으로 분류하고 AFK 에이전트가 픽업 가능한 형태로 정리해줘.

상태: `needs-triage` → `needs-info` | `ready-for-agent` | `ready-for-human` | `wontfix`

상세 절차: `engineering/triage/SKILL.md` 참조.

## 실행 방법

```
/triage                       # 주의 필요 이슈 자동 표시 (미라벨/needs-triage/needs-info)
/triage #42                   # 특정 이슈 분류
/triage move #42 ready-for-agent   # 빠른 상태 override
```

## 사전 조건

`.claude/docs/agents/triage-labels.md`와 `issue-tracker.md`가 설정되어 있어야 함. 없으면 `/setup-til-skills` 먼저.
