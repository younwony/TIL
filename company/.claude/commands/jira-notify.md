---
description: Jira 이슈 상태를 Slack 채널에 알림합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Jira Notify - 이슈 상태 Slack 알림

특정 Jira 이슈의 현재 상태를 Slack 채널에 알림으로 공유해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Jira Cloud URL** | https://temcolabs.atlassian.net |
| **프로젝트 키** | TECH |

## 1단계: 이슈 결정

### `$ARGUMENTS`에 이슈 키가 있는 경우

- `$ARGUMENTS`에서 이슈 키를 추출
- 예: `/jira-notify TECH-22013` → 해당 이슈 조회
- 예: `/jira-notify TECH-22013 #dev-team` → 해당 이슈를 특정 채널에 알림

### 이슈 키가 없는 경우

AskUserQuestion으로 요청:
- "알림을 보낼 Jira 이슈 키를 입력해주세요."
- 예시: `TECH-22013`

### 이슈 키 형식 검증

- `{프로젝트키}-{숫자}` 형식이 아니면 안내하고 재입력 요청
- 예: `TECH-22013` (올바름), `22013` (잘못됨)

## 2단계: Jira 이슈 조회

도구: `mcp__claude_ai_Atlassian__getJiraIssue`

- `cloudId`: https://temcolabs.atlassian.net
- `issueKey`: 1단계에서 추출한 이슈 키

### 조회 실패 시

- "이슈를 찾을 수 없습니다. 이슈 키를 확인해주세요." 안내 후 중단

### 추출 정보

- 이슈 키, 제목 (summary)
- 상태 (status)
- 담당자 (assignee)
- 우선순위 (priority)
- 이슈 타입 (issuetype)
- 스프린트 (customfield_10008)
- 레이블 (labels)
- 생성일, 수정일

## 3단계: 알림 메시지 생성

### 상태별 이모지

| 상태 | 이모지 |
|------|--------|
| To Do / Open | :white_circle: |
| In Progress | :large_blue_circle: |
| In Review / QA Ready | :yellow_circle: |
| Done / Closed | :white_check_mark: |
| 기타 | :radio_button: |

### 메시지 템플릿

```
{상태 이모지} *[{이슈키}] {제목}*

*상태*: {상태}
*담당자*: {담당자}
*우선순위*: {우선순위}
*이슈 타입*: {타입}
{스프린트가 있으면}*스프린트*: {스프린트명}
{레이블이 있으면}*레이블*: {레이블 목록}

:link: https://temcolabs.atlassian.net/browse/{이슈키}
```

### 복수 이슈 지원

`$ARGUMENTS`에 여러 이슈 키가 있으면 (쉼표 또는 공백 구분) 각 이슈를 조회하여 한 메시지로 통합:

```
:clipboard: *Jira 이슈 현황*

{상태 이모지} *[{이슈키1}] {제목}* - {상태} ({담당자})
{상태 이모지} *[{이슈키2}] {제목}* - {상태} ({담당자})
{상태 이모지} *[{이슈키3}] {제목}* - {상태} ({담당자})
```

## 4단계: 채널 선택

### `$ARGUMENTS`에 채널 지정이 있는 경우

- `$ARGUMENTS`에서 `#채널명` 또는 채널 ID를 추출

### 채널 지정이 없는 경우

AskUserQuestion으로 채널 선택:
- "알림을 보낼 Slack 채널을 선택해주세요."
- 옵션:
  - "채널 검색"
  - "채널 ID 직접 입력"
  - "출력만" (Slack 전송 없이 텍스트 출력)

### 채널 검색 시

도구: `mcp__claude_ai_Slack__slack_search_channels`
- 검색어로 채널 목록 조회 후 선택

## 5단계: Slack 전송

도구: `mcp__claude_ai_Slack__slack_send_message`

- `channel_id`: 4단계에서 결정한 채널
- 메시지: 3단계에서 생성한 알림

### 전송 실패 시

- "Slack 전송에 실패했습니다." 안내
- 알림 내용을 텍스트로 출력

## 6단계: 결과 보고

```
## Jira 이슈 알림 완료

- **이슈**: {이슈키} - {제목}
- **상태**: {상태}
- **채널**: #{채널명}
- **링크**: https://temcolabs.atlassian.net/browse/{이슈키}
```
