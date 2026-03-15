---
description: 현재 스프린트 현황을 Slack 채널에 공유합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Jira Report - 스프린트 현황 Slack 공유

현재 활성 스프린트의 이슈 현황을 분석하여 Slack 채널에 리포트를 공유해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Jira Cloud URL** | https://temcolabs.atlassian.net |
| **프로젝트 키** | TECH |
| **보드 ID** | 2 |
| **스프린트 필드** | customfield_10008 |
| **기본 담당자** | 윤원희 (accountId: 6212e9007580350068335108) |

## 1단계: 활성 스프린트 조회

도구: `mcp__claude_ai_Atlassian__searchJiraIssuesUsingJql`

```
jql: project = TECH AND sprint in openSprints()
fields: ["summary", "status", "assignee", "priority", "customfield_10008"]
maxResults: 100
```

- 조회 결과에서 `state: "active"` 인 스프린트 정보를 추출
- 스프린트 이름, ID, 시작일, 종료일 파악

## 2단계: 이슈 분류 및 통계

조회된 이슈들을 상태별로 분류한다:

### 상태별 분류

| 상태 그룹 | 포함 상태 |
|-----------|----------|
| **완료** | Done, Closed, 완료 |
| **진행중** | In Progress, In Review, QA Ready |
| **대기** | To Do, Open, Backlog |

### 통계 항목

- 전체 이슈 수
- 상태별 이슈 수 및 비율
- 담당자별 이슈 수
- 우선순위별 이슈 수
- 완료율 (완료 / 전체 * 100)

## 3단계: 리포트 생성

### 리포트 메시지 템플릿

```
:bar_chart: *{스프린트명} 현황 리포트*
기간: {시작일} ~ {종료일}

:clipboard: *전체 현황*
- 전체: {전체}건
- 완료: {완료}건 ({완료율}%)
- 진행중: {진행중}건
- 대기: {대기}건

:chart_with_upwards_trend: *진행률*
{프로그레스 바 ASCII}  {완료율}%

:bust_in_silhouette: *담당자별 현황*
- {담당자1}: 완료 {N}건 / 진행중 {N}건 / 대기 {N}건
- {담당자2}: 완료 {N}건 / 진행중 {N}건 / 대기 {N}건

:rotating_light: *주요 이슈* (우선순위 High 이상)
- [{TECH-XXXXX}] {제목} ({상태}) - {담당자}
- [{TECH-XXXXX}] {제목} ({상태}) - {담당자}
```

### 프로그레스 바 생성 규칙

완료율에 따라 ASCII 프로그레스 바를 생성:
- 10% 단위로 블록 표시
- 예: `[████████░░]  80%`
- `█` = 완료 비율, `░` = 남은 비율

## 4단계: 채널 선택

### `$ARGUMENTS`에 채널 지정이 있는 경우

- `$ARGUMENTS`에서 채널명 또는 채널 ID를 추출
- 예: `/jira-report #dev-team` → `dev-team` 채널에 전송

### 채널 지정이 없는 경우

AskUserQuestion으로 채널을 선택:
- "리포트를 공유할 Slack 채널을 선택해주세요."
- 옵션:
  - "채널 검색" (채널명 입력)
  - "현재 채널에 공유" (기본)
  - "클립보드에 복사" (Slack 전송 없이 텍스트만 출력)

### 채널 검색 선택 시

도구: `mcp__claude_ai_Slack__slack_search_channels`
- 검색어로 채널 목록을 조회하여 AskUserQuestion으로 선택

## 5단계: Slack 전송

도구: `mcp__claude_ai_Slack__slack_send_message`

- `channel_id`: 4단계에서 결정한 채널
- 메시지: 3단계에서 생성한 리포트

### 전송 실패 시

- "Slack 전송에 실패했습니다. 리포트 내용을 텍스트로 출력합니다." 안내
- 리포트 내용을 그대로 출력하여 수동 복사 가능하도록

## 6단계: 결과 보고

```
## 스프린트 리포트 공유 완료

- **스프린트**: {스프린트명}
- **채널**: #{채널명}
- **전체 이슈**: {N}건
- **완료율**: {완료율}%
```
