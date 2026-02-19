---
description: Slack 스레드를 읽어 Jira 이슈를 자동 생성합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Slack to Jira - 스레드 기반 이슈 자동 생성

Slack 스레드의 대화 내용을 분석하여 Jira 이슈를 자동 생성해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Jira Cloud URL** | https://temcolabs.atlassian.net |
| **프로젝트 키** | TECH |
| **기본 이슈 타입** | 작업 (id: 10002) |
| **기본 담당자** | 윤원희 (accountId: 6212e9007580350068335108) |
| **기본 우선순위** | Medium (id: 3) |
| **기본 레이블** | GUHADA |
| **기본 스프린트** | 현재 활성 스프린트 자동 설정 |
| **스프린트 필드** | customfield_10008 |

## 1단계: Slack URL 파싱

`$ARGUMENTS`에서 Slack 스레드 URL을 추출한다.

### URL이 없는 경우

`$ARGUMENTS`가 비어있으면 AskUserQuestion으로 URL을 요청한다:
- "Jira 이슈로 변환할 Slack 스레드 URL을 입력해주세요."
- 예시 안내: `https://{workspace}.slack.com/archives/{channel}/p{timestamp}`

### URL 파싱 규칙

두 가지 URL 형식을 모두 지원:

**형식 1: 기본**
```
https://{workspace}.slack.com/archives/{channel_id}/p{timestamp}
```
- `channel_id`: `/archives/` 뒤의 값 (예: `C03D3F5DS02`)
- `message_ts`: `p` 뒤의 숫자에서 앞 10자리.뒤 6자리 (예: `p1771465781101669` → `1771465781.101669`)

**형식 2: thread_ts 파라미터 포함**
```
https://{workspace}.slack.com/archives/{channel_id}/p{timestamp}?thread_ts={ts}&cid={channel}
```
- `thread_ts` 쿼리 파라미터가 있으면 해당 값을 `message_ts`로 사용
- `thread_ts` 형식: `1771465781.101669` (이미 변환된 형태)

### URL 형식 오류 시

유효하지 않은 URL이면 다음을 안내하고 중단:
```
올바른 Slack 스레드 URL을 입력해주세요.
예시: https://workspace.slack.com/archives/C03D3F5DS02/p1771465781101669
```

## 2단계: Slack 스레드 읽기

도구: `mcp__claude_ai_Slack__slack_read_thread`

- `channel_id`: 1단계에서 추출한 값
- `message_ts`: 1단계에서 추출한 값
- `response_format`: "detailed"

### 스레드 읽기 실패 시

- "Slack 스레드를 읽을 수 없습니다. URL을 확인해주세요." 안내 후 중단
- 채널 접근 권한 문제일 수 있음을 안내

## 3단계: 내용 분석

스레드 대화를 분석하여 Jira 이슈 정보를 자동 생성한다.

### 자동 추출 항목

- **summary**: 스레드 핵심 주제를 1줄로 요약 (Jira 이슈 제목)
- **description**: 작업 내용 + Slack 링크 + 스레드 요약

### Description 템플릿

```markdown
## 작업 내용
{스레드에서 추출한 작업 설명 - 핵심 내용을 정리}

## Slack 스레드
- 채널: #{채널명}
- 작성자: {스레드 시작 작성자}
- 일시: {YYYY-MM-DD HH:MM}
- 링크: {원본 Slack URL}

## 스레드 요약
> {Parent 메시지 원문}

{Reply들을 시간순으로 정리}
- {작성자} ({시간}): {내용 요약}
- {작성자} ({시간}): {내용 요약}
```

## 4단계: 사용자 확인

### 제목 접두사 컨벤션

이슈 타입 선택 후, 해당하는 접두사를 summary 앞에 자동으로 붙인다:

| 접두사 | 적용 조건 |
|--------|----------|
| `[BE]` | 백엔드 작업 |
| `[FE]` | 프론트엔드 작업 |

- 접두사는 AskUserQuestion으로 사용자에게 확인받는다
- 예시: `[BE] 시즌 매핑 운영 업무 Settle 기능 구현`

### 첫 번째 질문 - 이슈 기본 정보

자동 생성된 summary와 description 미리보기를 보여준 후 AskUserQuestion으로 확인:

- "이슈 타입을 선택해주세요."
- 옵션:
  - "작업 (Task)" (기본, id: 10002)
  - "Task-User Story"
  - "버그 (Bug)"
  - "하위 작업 (Sub-task)"

### 제목 접두사 선택

- "제목 접두사를 선택해주세요."
- 옵션:
  - "[BE]" (백엔드 작업)
  - "[FE]" (프론트엔드 작업)
  - "없음" (접두사 없이 생성)

### 두 번째 질문 - 상세 필드 (multiSelect)

- "추가로 설정할 항목을 선택해주세요."
- 옵션 (multiSelect: true):
  - "스프린트 지정"
  - "레이블 추가"
  - "담당자 변경"
  - "우선순위 변경"
  - "이대로 생성"

### 선택에 따른 후속 질문

- **스프린트 지정**: 기본으로 현재 활성 스프린트에 자동 배치 (별도 질문 없이)
  - `searchJiraIssuesUsingJql` (jql: `project = TECH AND sprint in openSprints()`, fields: `["customfield_10008"]`)
  - 조회 결과에서 `state: "active"` 인 스프린트의 ID를 추출
  - 스프린트 필드: `customfield_10008` (Jira Agile Sprint 필드)
  - 이슈 생성 시 `additional_fields`에 `{"customfield_10008": 활성_스프린트_ID}` 전달 (숫자값 직접 전달)
  - 활성 스프린트가 여러 개면 AskUserQuestion으로 선택, 1개면 자동 설정
- **레이블 추가**: JQL로 TECH 프로젝트의 기존 레이블을 조회하여 목록으로 제시
  - `searchJiraIssuesUsingJql` (jql: `project = TECH AND labels is not EMPTY`, fields: `["labels"]`)
  - 조회 결과에서 고유 레이블을 추출하여 AskUserQuestion의 옵션으로 표시 (multiSelect)
  - 목록에 없는 레이블은 "기타" 옵션으로 직접 입력 가능
- **담당자 변경**: 담당자 이름/ID 입력 요청
- **우선순위 변경**: Highest / High / Medium / Low / Lowest 선택
- **이대로 생성**: 바로 5단계 진행

## 5단계: Jira 이슈 생성

### 1차 시도: `mcp__claude_ai_Atlassian__createJiraIssue`

- `cloudId`: https://temcolabs.atlassian.net
- `projectKey`: TECH
- `issueTypeName`: 4단계에서 선택한 이슈 타입
- `summary`: 3단계에서 생성한 제목
- `description`: 3단계에서 생성한 설명
- `assignee`: 6212e9007580350068335108 (기본값 또는 변경된 값)

### 1차 실패 시 2차 시도: `mcp__atlassian__create-issue`

- `projectKey`: TECH
- `issueTypeId`: 10002 (또는 선택한 타입 ID)
- `summary`, `description`, `assigneeId` 동일

### 둘 다 실패 시

수동 생성을 위한 정보를 출력:
```
Jira 이슈 자동 생성에 실패했습니다. 아래 정보로 수동 생성해주세요.

- 프로젝트: TECH
- 이슈 타입: {선택한 타입}
- 제목: {summary}
- 설명: {description}
- 담당자: {assignee}
- 링크: https://temcolabs.atlassian.net/jira/software/c/projects/TECH/issues
```

## 6단계: 결과 보고

### 이슈 생성 성공 시

다음 정보를 출력:
```
## Jira 이슈 생성 완료

- **이슈 키**: {TECH-XXXXX}
- **제목**: {summary}
- **링크**: https://temcolabs.atlassian.net/browse/{TECH-XXXXX}
- **이슈 타입**: {선택한 타입}
- **담당자**: {assignee}
```

### Slack 스레드에 이슈 링크 공유

AskUserQuestion으로 확인:
- "생성된 이슈 링크를 Slack 스레드에 공유할까요?"
- 옵션: "공유", "건너뛰기"

**공유 선택 시:**
도구: `mcp__claude_ai_Slack__slack_send_message`
- `channel_id`: 1단계에서 추출한 값
- `thread_ts`: 1단계에서 추출한 message_ts (스레드에 Reply)
- 메시지 내용:
```
Jira 이슈가 생성되었습니다: {TECH-XXXXX} - {summary}
https://temcolabs.atlassian.net/browse/{TECH-XXXXX}
```
