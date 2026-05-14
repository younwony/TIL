# Issue Tracker — TIL 환경

`/triage`, `/to-prd`, `/to-issues` 같은 skill이 사용하는 이슈 추적 도구 정의.

## 사용 도구

**Jira** (회사 표준).

- 사이트: `https://temcolabs.atlassian.net`
- 호출 방식: **curl REST API (1순위) → MCP 폴백 (2순위)**
- 인증: `ATLASSIAN_API_TOKEN` 환경변수 + Basic Auth (이메일 + 토큰)

## curl 기본 패턴

```bash
# 이슈 조회
curl -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{ISSUE_KEY}"

# 이슈 검색 (JQL)
curl -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  --data-urlencode 'jql=project = TECH AND status = "To Do"' \
  "https://temcolabs.atlassian.net/rest/api/3/search"

# 이슈 코멘트 추가
curl -X POST -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"body": {"type": "doc", "version": 1, "content": [...]}}' \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{ISSUE_KEY}/comment"

# 이슈 transition (상태 변경)
curl -X POST -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"transition": {"id": "21"}}' \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{ISSUE_KEY}/transitions"
```

## MCP 폴백

curl 실패 시:
- `mcp__atlassian__getJiraIssue`
- `mcp__atlassian__searchJiraIssues`
- `mcp__atlassian__addJiraComment`
- `mcp__atlassian__transitionJiraIssue`

(정확한 도구명은 사용 시점에 ToolSearch로 확인)

## CONTEXT.md 용어와의 매핑

| Matt Pocock canonical | TIL 매핑 |
|----------------------|---------|
| Issue tracker | Jira (temcolabs.atlassian.net) |
| Issue | Jira issue (TECH-XXXXX 키) |
| AGENT-BRIEF 코멘트 | Jira issue의 일반 코멘트 (마크다운 → Atlassian Document Format 변환 필요) |
| `.out-of-scope/` | `.claude/out-of-scope/{TECH-XXXXX}-{kebab-제목}.md` |

## 이슈 키 패턴

- `TECH-XXXXX` — 기술 작업
- `OPS-XXXXX` — 운영
- 그 외 프로젝트 키는 사용 시점에 확인

## 자주 쓰는 JQL 쿼리

```sql
-- 내가 담당이고 미완료
assignee = currentUser() AND status != "Done"

-- 이번 스프린트
sprint in openSprints()

-- 어제 변경됨
updated >= -1d
```

## 관련 skill

- `/triage` — 이슈 5-state machine 분류
- `/to-prd` — 대화 → PRD → Jira 이슈 발행
- `/to-issues` — vertical slice 분해 → Jira 이슈 발행
- `/jira-report`, `/jira-notify` — 상태 보고/알림 (MCP 사용)
- `/slack-to-jira` — Slack 스레드 → Jira 이슈 변환
