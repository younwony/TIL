---
description: 특정 시간에 Slack 메시지를 예약 발송합니다.
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Slack Remind - 메시지 예약 발송

특정 시간에 Slack 채널 또는 사용자에게 메시지를 예약 발송해줘.

## 설정

| 항목 | 값 |
|------|------|
| **타임존** | Asia/Seoul (KST, UTC+9) |

## 1단계: 인자 파싱

`$ARGUMENTS`에서 예약 정보를 추출한다.

### 인자 형식 예시

```
/slack-remind #channel 2026-02-20 09:00 "스프린트 리뷰 회의 시작합니다"
/slack-remind @user 내일 10:00 "PR 리뷰 부탁드립니다"
/slack-remind #dev-team 30분후 "배포 예정입니다"
```

### 인자가 부족한 경우

AskUserQuestion으로 순차적으로 정보를 수집:

**1) 대상 선택**
- "메시지를 보낼 대상을 선택해주세요."
- 옵션:
  - "채널 지정" (채널명 입력)
  - "사용자 지정" (사용자명 입력)

**2) 예약 시간**
- "메시지 발송 시간을 입력해주세요."
- 옵션:
  - "30분 후"
  - "1시간 후"
  - "내일 09:00"
  - "직접 입력" (YYYY-MM-DD HH:MM 형식)

**3) 메시지 내용**
- "발송할 메시지 내용을 입력해주세요."
- (사용자가 직접 입력)

## 2단계: 채널/사용자 확인

### 채널 지정 시

도구: `mcp__claude_ai_Slack__slack_search_channels`
- 채널명으로 검색하여 channel_id 확보

### 사용자 지정 시

도구: `mcp__claude_ai_Slack__slack_search_users`
- 사용자명으로 검색하여 user_id 확보
- DM 채널로 메시지 예약

## 3단계: 예약 시간 계산

### 상대 시간 처리

| 입력 | 변환 |
|------|------|
| `30분후` / `30분 후` | 현재 시간 + 30분 |
| `1시간후` / `1시간 후` | 현재 시간 + 1시간 |
| `내일 HH:MM` | 내일 해당 시간 (KST) |
| `YYYY-MM-DD HH:MM` | 해당 시간 (KST) |

### Unix timestamp 변환

Slack API는 Unix timestamp (초 단위)를 요구한다:

```bash
# KST 시간을 Unix timestamp로 변환
date -d "2026-02-20 09:00:00" +%s
```

### 과거 시간 검증

- 계산된 시간이 현재보다 과거이면 안내하고 중단:
  "지정한 시간이 이미 지났습니다. 미래 시간을 입력해주세요."

## 4단계: 예약 확인

AskUserQuestion으로 최종 확인:

- "다음 내용으로 메시지를 예약할까요?"
- 미리보기:
  ```
  대상: #{채널명} 또는 @{사용자명}
  시간: {YYYY-MM-DD HH:MM} (KST)
  메시지: {메시지 내용}
  ```
- 옵션:
  - "예약 발송"
  - "시간 변경"
  - "메시지 수정"
  - "취소"

## 5단계: 메시지 예약

도구: `mcp__claude_ai_Slack__slack_schedule_message`

- `channel_id`: 2단계에서 확보한 채널/사용자 ID
- `post_at`: 3단계에서 계산한 Unix timestamp
- `text`: 메시지 내용

### 예약 실패 시

- "메시지 예약에 실패했습니다." 안내
- 오류 내용 표시
- "다시 시도하시겠습니까?" 확인

## 6단계: 결과 보고

```
## 메시지 예약 완료

- **대상**: #{채널명} 또는 @{사용자명}
- **예약 시간**: {YYYY-MM-DD HH:MM} (KST)
- **메시지**: {메시지 내용}
- **예약 ID**: {scheduled_message_id}
```

### 주의사항

- 예약된 메시지는 Slack에서 직접 취소할 수 있음을 안내
- 예약 시간은 KST 기준으로 표시
