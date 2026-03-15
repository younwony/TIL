---
description: Slack 스레드를 회의록으로 변환하여 Confluence에 저장합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Meeting Notes - 스레드 기반 회의록 생성

Slack 스레드의 대화 내용을 분석하여 회의록을 자동 생성하고 Confluence에 저장해줘.

## 설정

| 항목 | 값 |
|------|------|
| **Confluence Cloud ID** | 1d77eaeb-5f74-4e36-8f0c-1d7ffc53faf9 |
| **개인 스페이스 ID** | 1983741954 |
| **홈페이지 ID** | 1983742135 |
| **Confluence URL** | https://temcolabs.atlassian.net |

## 1단계: Slack URL 파싱

`$ARGUMENTS`에서 Slack 스레드 URL을 추출한다.

### URL이 없는 경우

`$ARGUMENTS`가 비어있으면 AskUserQuestion으로 URL을 요청한다:
- "회의록으로 변환할 Slack 스레드 URL을 입력해주세요."
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

유효하지 않은 URL이면 안내하고 중단.

## 2단계: Slack 스레드 읽기

도구: `mcp__claude_ai_Slack__slack_read_thread`

- `channel_id`: 1단계에서 추출한 값
- `message_ts`: 1단계에서 추출한 값
- `response_format`: "detailed"

### 스레드 읽기 실패 시

- "Slack 스레드를 읽을 수 없습니다. URL을 확인해주세요." 안내 후 중단

## 3단계: 회의록 구조화

스레드 대화를 분석하여 회의록 형식으로 구조화한다.

### 자동 추출 항목

- **회의 제목**: 스레드 주제 기반 생성
- **일시**: 스레드 시작 시간
- **참석자**: 스레드에 메시지를 보낸 모든 사람
- **안건**: 대화에서 추출한 논의 주제들
- **결정사항**: 합의된 내용이나 결정
- **액션 아이템**: TODO, 후속 작업

### 회의록 템플릿

```markdown
# {회의 제목}

## 회의 정보

| 구분 | 내용 |
|------|------|
| **일시** | {YYYY-MM-DD HH:MM} |
| **채널** | #{채널명} |
| **참석자** | {참석자 목록} |
| **작성자** | Claude (자동 생성) |
| **Slack 원문** | {원본 URL} |

## 안건

1. {안건 1}
2. {안건 2}
3. {안건 3}

## 논의 내용

### 안건 1: {안건 제목}

**배경:**
{안건의 배경 설명}

**논의:**
- {참석자A}: {발언 요약}
- {참석자B}: {발언 요약}

**결론:**
{이 안건에 대한 결론}

### 안건 2: {안건 제목}

(동일 구조 반복)

## 결정사항

| 번호 | 결정사항 | 비고 |
|:----:|---------|------|
| 1 | {결정 내용} | {관련 안건} |

## 액션 아이템

| 번호 | 액션 아이템 | 담당자 | 기한 | 상태 |
|:----:|------------|--------|------|:----:|
| 1 | {할 일} | {담당자} | {기한 또는 미정} | 대기 |

## 기타 메모

{대화에서 나온 참고사항, 부가 정보}

---

> 이 회의록은 Slack 스레드를 기반으로 Claude가 자동 생성했습니다.
> 원본: {Slack URL}
```

> 관련 없는 섹션은 생략 가능

## 4단계: 사용자 확인

### 첫 번째 질문 - 회의록 확인

자동 생성된 회의록을 미리보기로 보여준 후 AskUserQuestion:

- "회의록 내용을 확인해주세요."
- 옵션:
  - "이대로 저장"
  - "제목 수정"
  - "내용 수정" (수정 사항 입력)

### 두 번째 질문 - 저장 방식

- "회의록을 어디에 저장할까요?"
- 옵션:
  - "Confluence 개인 스페이스" (기본)
  - "Confluence 다른 스페이스"
  - "텍스트 출력만" (Confluence 저장 없이)

## 5단계: Confluence 페이지 생성

### 1차 시도: MCP 도구

도구: `mcp__atlassian__create-page`

- `spaceId`: 1983741954 (또는 선택한 스페이스)
- `parentId`: 1983742135
- `title`: 회의록 제목 (예: `[회의록] {제목} - {YYYY-MM-DD}`)
- `bodyValue`: Confluence Storage Format (XML)

### 2차 시도: curl 폴백

MCP 도구 실패 시 curl로 REST API 직접 호출:

```bash
API_TOKEN=$(cat ~/.claude.json | grep -o '"ATLASSIAN_API_TOKEN":"[^"]*"' | cut -d'"' -f4)

curl -s -X POST -H "Content-Type: application/json" \
  -u "wonhee.youn@temco.io:${API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

### 둘 다 실패 시

회의록 내용을 텍스트로 출력하여 수동 복사 가능하도록.

## 6단계: 결과 보고

### 성공 시

```
## 회의록 생성 완료

- **제목**: {제목}
- **링크**: {페이지 URL}
- **참석자**: {N}명
- **액션 아이템**: {N}건
```

### Slack 스레드에 링크 공유

AskUserQuestion으로 확인:
- "생성된 회의록 링크를 Slack 스레드에 공유할까요?"
- 옵션: "공유", "건너뛰기"

**공유 선택 시:**
도구: `mcp__claude_ai_Slack__slack_send_message`
- `channel_id`: 1단계에서 추출한 값
- `thread_ts`: 1단계에서 추출한 message_ts
- 메시지 내용:
```
회의록이 생성되었습니다: {제목}
{페이지 URL}
```
