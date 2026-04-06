---
description: Track 작업 요약 대시보드 생성 - Phase별 작업 내역 + 아키텍처 다이어그램을 HTML로 시각화
allowed-tools: Read, Write, Glob, Grep, Bash, Agent, AskUserQuestion
---

# Track Status Summary - 작업 요약 대시보드 생성

`.claude/tracks/` 하위 Track의 전체 작업 내역을 마크다운 + HTML 대시보드로 시각화해줘.

`track-status-summary` 스킬의 지시를 따라 실행해줘.

## 인자

```
/track-status-summary              # Active Track 자동 감지
/track-status-summary {track_id}   # 특정 Track 지정
```

$ARGUMENTS
