---
description: 현재 브랜치 변경사항 기반 Chrome 브라우저 QA (browser-debug 스킬 실행)
allowed-tools: Bash, Read, Glob, Grep, Edit, Write, ToolSearch, Agent, EnterPlanMode, ExitPlanMode, mcp__claude-in-chrome__tabs_context_mcp, mcp__claude-in-chrome__tabs_create_mcp, mcp__claude-in-chrome__navigate, mcp__claude-in-chrome__read_page, mcp__claude-in-chrome__get_page_text, mcp__claude-in-chrome__javascript_tool, mcp__claude-in-chrome__find, mcp__claude-in-chrome__computer, mcp__claude-in-chrome__form_input, mcp__claude-in-chrome__read_console_messages, mcp__claude-in-chrome__read_network_requests, mcp__claude-in-chrome__gif_creator, mcp__claude-in-chrome__resize_window, mcp__claude-in-chrome__upload_image
---

# Browser Debug & QA

현재 브랜치의 변경사항을 분석하여 Chrome 브라우저 자동화 QA를 수행합니다.

$ARGUMENTS

## 실행

`browser-debug` 스킬을 호출하여 실행한다.
이 커맨드는 스킬의 트리거 역할만 수행하며, 실제 로직은 스킬에 정의되어 있다.

스킬 워크플로우:
1. `*_QA-SCENARIOS.md` 확인 (없으면 `qa-scenario` 스킬로 생성)
2. 서버 상태 확인 및 자동 기동
3. Chrome 자동화로 시나리오 순차 실행
4. FAIL 시 즉시 수정 및 재검증
5. 결과 보고 및 git add
