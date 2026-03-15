---
description: 스프린트 시작 시 팀 채널에 할당 이슈를 공유합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Sprint Start Notify - 스프린트 시작 알림

현재 활성 스프린트의 할당 이슈 목록을 팀 Slack 채널에 공유해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Jira Cloud URL** | https://temcolabs.atlassian.net |
| **프로젝트 키** | TECH |
| **보드 ID** | 2 |
| **스프린트 필드** | customfield_10008 |

## 1단계: 활성 스프린트 조회

도구: `mcp__claude_ai_Atlassian__searchJiraIssuesUsingJql`

```
jql: project = TECH AND sprint in openSprints()
fields: ["summary", "status", "assignee", "priority", "issuetype", "customfield_10008"]
maxResults: 100
```

- 조회 결과에서 `state: "active"` 인 스프린트 정보를 추출
- 스프린트 이름, 시작일, 종료일 파악

### 활성 스프린트가 없는 경우

- "현재 활성 스프린트가 없습니다." 안내 후 중단

## 2단계: 이슈 분류

조회된 이슈들을 담당자별로 그룹화한다.

### 분류 기준

- **담당자별 그룹**: 각 담당자에게 할당된 이슈 목록
- **미할당 이슈**: 담당자가 없는 이슈
- **이슈 타입별**: Task, Bug, Story 등

### 통계 항목

- 전체 이슈 수
- 담당자별 이슈 수
- 이슈 타입별 수
- 우선순위별 수

## 3단계: 알림 메시지 생성

### 메시지 템플릿

```
:rocket: *{스프린트명} 시작!*
기간: {시작일} ~ {종료일}

:clipboard: *스프린트 현황*
- 전체 이슈: {N}건
- Task: {N}건 | Bug: {N}건 | Story: {N}건

:bust_in_silhouette: *담당자별 이슈*

*{담당자1}* ({N}건)
- [{TECH-XXXXX}] {제목} ({우선순위})
- [{TECH-XXXXX}] {제목} ({우선순위})

*{담당자2}* ({N}건)
- [{TECH-XXXXX}] {제목} ({우선순위})

{미할당 이슈가 있는 경우}
:warning: *미할당 이슈* ({N}건)
- [{TECH-XXXXX}] {제목}

:link: *보드 링크*
https://temcolabs.atlassian.net/jira/software/c/projects/TECH/boards/2
```

## 4단계: 채널 선택

### `$ARGUMENTS`에 채널 지정이 있는 경우

- `$ARGUMENTS`에서 채널명 또는 채널 ID를 추출

### 채널 지정이 없는 경우

AskUserQuestion으로 채널 선택:
- "스프린트 시작 알림을 보낼 채널을 선택해주세요."
- 옵션:
  - "채널 검색"
  - "채널 ID 직접 입력"

### 채널 검색 시

도구: `mcp__claude_ai_Slack__slack_search_channels`
- 검색어로 채널 목록 조회 후 선택

## 5단계: 전송 확인

AskUserQuestion으로 최종 확인:
- "다음 내용으로 스프린트 시작 알림을 보낼까요?"
- 메시지 미리보기 표시
- 옵션:
  - "전송"
  - "수정" (내용 수정 요청)
  - "취소"

## 6단계: Slack 전송

도구: `mcp__claude_ai_Slack__slack_send_message`

- `channel_id`: 4단계에서 결정한 채널
- 메시지: 3단계에서 생성한 알림

### 전송 실패 시

- "Slack 전송에 실패했습니다. 알림 내용을 텍스트로 출력합니다." 안내
- 내용을 그대로 출력

## 7단계: 결과 보고

```
## 스프린트 시작 알림 완료

- **스프린트**: {스프린트명}
- **채널**: #{채널명}
- **전체 이슈**: {N}건
- **담당자**: {N}명
- **기간**: {시작일} ~ {종료일}
```
