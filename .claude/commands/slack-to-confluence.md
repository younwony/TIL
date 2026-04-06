---
description: Slack 스레드를 Confluence 페이지로 변환합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Slack to Confluence - 스레드 기반 문서 생성

Slack 스레드의 대화 내용을 분석하여 Confluence 페이지를 자동 생성해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Confluence Cloud ID** | 1d77eaeb-5f74-4e36-8f0c-1d7ffc53faf9 |
| **개인 스페이스 ID** | 1983741954 |
| **개인 스페이스 키** | ~645023757 |
| **홈페이지 ID** | 1983742135 |
| **기본 스페이스** | 개인 스페이스 |
| **Confluence URL** | https://temcolabs.atlassian.net |

## 1단계: Slack URL 파싱

`$ARGUMENTS`에서 Slack 스레드 URL을 추출한다.

### URL이 없는 경우

`$ARGUMENTS`가 비어있으면 AskUserQuestion으로 URL을 요청한다:
- "Confluence 페이지로 변환할 Slack 스레드 URL을 입력해주세요."
- 예시 안내: `https://{workspace}.slack.com/archives/{channel}/p{timestamp}`

### URL 파싱 규칙

두 가지 URL 형식을 모두 지원:

**형식 1: 기본**
```
https://{workspace}.slack.com/archives/{channel_id}/p{timestamp}
```
- `channel_id`: `/archives/` 뒤의 값
- `message_ts`: `p` 뒤의 숫자에서 앞 10자리.뒤 6자리

**형식 2: thread_ts 파라미터 포함**
```
https://{workspace}.slack.com/archives/{channel_id}/p{timestamp}?thread_ts={ts}&cid={channel}
```
- `thread_ts` 쿼리 파라미터가 있으면 해당 값을 `message_ts`로 사용

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

## 3단계: 내용 분석 및 문서 구조화

스레드 대화를 분석하여 Confluence 문서를 생성한다.

### 자동 추출 항목

- **제목**: 스레드 핵심 주제를 1줄로 요약
- **카테고리**: 기술 논의 / 의사결정 / 장애 대응 / 업무 요청 / 기타
- **참여자**: 스레드에 참여한 모든 사람
- **핵심 내용**: 대화에서 도출된 결론, 액션 아이템

### Confluence 페이지 템플릿

```markdown
# {제목}

## 한눈에 보기

| 구분 | 내용 |
|------|------|
| **카테고리** | {카테고리} |
| **일시** | {YYYY-MM-DD HH:MM} |
| **채널** | #{채널명} |
| **참여자** | {참여자 목록} |
| **Slack 링크** | {원본 URL} |

## 배경 및 목적

{스레드가 시작된 배경과 목적을 정리}

## 논의 내용

### 주요 포인트

{대화에서 나온 주요 포인트를 bullet point로 정리}

### 상세 대화 요약

> {Parent 메시지 원문}

{Reply들을 시간순으로 정리}
- **{작성자}** ({시간}): {내용 요약}
- **{작성자}** ({시간}): {내용 요약}

## 결론 및 액션 아이템

| 번호 | 액션 아이템 | 담당자 | 기한 |
|:----:|------------|--------|------|
| 1 | {액션 아이템} | {담당자} | {기한 또는 미정} |

## 관련 링크

| 구분 | 링크 |
|------|------|
| **Slack 스레드** | {원본 URL} |
```

> 관련 없는 섹션은 생략 가능 (예: 액션 아이템이 없으면 생략)

## 4단계: 사용자 확인

### 첫 번째 질문 - 페이지 제목 확인

자동 생성된 제목과 내용 미리보기를 보여준 후 AskUserQuestion:

- "페이지 제목을 확인해주세요."
- 옵션:
  - "이대로 생성" (자동 생성된 제목 사용)
  - "제목 수정" (직접 입력)

### 두 번째 질문 - 저장 위치

- "Confluence 저장 위치를 선택해주세요."
- 옵션:
  - "개인 스페이스 (기본)" (spaceId: 1983741954)
  - "다른 스페이스" (스페이스 검색)

### 다른 스페이스 선택 시

도구: `mcp__atlassian__get-spaces`
- 스페이스 목록을 조회하여 AskUserQuestion으로 선택

## 5단계: Confluence 페이지 생성

> **Atlassian API 우선순위**: curl REST API를 1순위로 사용한다 (CLAUDE.md 참조). MCP는 실패율이 높아 폴백으로만 사용.

### 1차 시도: curl REST API

curl을 사용하여 Atlassian REST API를 직접 호출한다:

- `spaceId`: 4단계에서 선택한 스페이스 ID (기본: 1983741954)
- `parentId`: 1983742135 (홈페이지, 개인 스페이스인 경우)
- `title`: 자동 생성 또는 사용자 수정 제목
- `bodyValue`: Confluence Storage Format (XML)

### 2차 시도: MCP 폴백

curl 실패 시 MCP 도구로 폴백:

```bash
# API 토큰 조회
API_TOKEN=$(cat ~/.claude.json | grep -o '"ATLASSIAN_API_TOKEN":"[^"]*"' | cut -d'"' -f4)

# 페이지 생성
curl -s -X POST -H "Content-Type: application/json" \
  -u "wonhee.youn@temco.io:${API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

JSON 구조:
```json
{
  "spaceId": "1983741954",
  "parentId": "1983742135",
  "status": "current",
  "title": "{제목}",
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

### 둘 다 실패 시

수동 생성을 위한 정보를 출력:
```
Confluence 페이지 자동 생성에 실패했습니다. 아래 내용을 직접 복사하여 생성해주세요.

- 스페이스: 개인 스페이스
- 제목: {제목}
- 링크: https://temcolabs.atlassian.net/wiki/spaces/~645023757/overview
```

## 6단계: 결과 보고

### 페이지 생성 성공 시

```
## Confluence 페이지 생성 완료

- **제목**: {제목}
- **스페이스**: {스페이스명}
- **링크**: {페이지 URL}
- **카테고리**: {카테고리}
- **참여자**: {참여자 수}명
```

### Slack 스레드에 페이지 링크 공유

AskUserQuestion으로 확인:
- "생성된 Confluence 페이지 링크를 Slack 스레드에 공유할까요?"
- 옵션: "공유", "건너뛰기"

**공유 선택 시:**
도구: `mcp__claude_ai_Slack__slack_send_message`
- `channel_id`: 1단계에서 추출한 값
- `thread_ts`: 1단계에서 추출한 message_ts
- 메시지 내용:
```
Confluence 페이지가 생성되었습니다: {제목}
{페이지 URL}
```
