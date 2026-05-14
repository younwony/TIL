---
name: triage
description: 이슈를 5-state machine을 거쳐 분류한다. Jira/GitHub/.scratch 등의 issue tracker에 라벨을 적용하고, AGENT-BRIEF로 AFK 에이전트가 픽업 가능하게 만든다. "트리아지", "이슈 분류", "Jira 정리", "백로그 정리", "/triage" 트리거.
---

# Triage

이슈를 작은 상태 머신을 거쳐 분류한다. AFK 에이전트가 픽업할 수 있는 형태로 명세를 정돈하는 게 목적.

> 영감 출처: [mattpocock/skills/engineering/triage](https://github.com/mattpocock/skills/blob/main/skills/engineering/triage/SKILL.md). 한국어 + Jira 환경에 맞춰 변형.

**모든 코멘트와 라벨 변경은 다음 면책으로 시작**:

```
> *이 코멘트는 triage 중 AI가 생성했습니다.*
```

## 역할 (Roles)

### 카테고리 (2종)

- `bug` — 동작 망가짐
- `enhancement` — 신규 기능 / 개선

### 상태 (5종)

- `needs-triage` — 메인테이너 평가 대기
- `needs-info` — 리포터 추가 정보 대기
- `ready-for-agent` — 완전 명세, AFK 에이전트 픽업 가능
- `ready-for-human` — 사람이 직접 구현해야 함
- `wontfix` — 작업 안 함

이슈마다 카테고리 1개 + 상태 1개. 충돌 시 메인테이너에게 먼저 확인.

상태 전이: 미라벨 → `needs-triage` → (`needs-info` / `ready-for-agent` / `ready-for-human` / `wontfix`). `needs-info`는 리포터가 답하면 `needs-triage`로 복귀.

이 역할 이름은 canonical이다. 실제 issue tracker에서 쓰는 라벨 문자열은 다를 수 있다 — 매핑은 `docs/agents/triage-labels.md` 참조 (없으면 `/setup-til-skills` 실행).

## 호출 방식

메인테이너가 자연어로 의도를 표현하면 해석해서 실행.

- "내 주의 필요한 거 보여줘"
- "#42 보자"
- "#42를 ready-for-agent로 옮겨줘"
- "에이전트가 가져갈 수 있는 거 뭐 있어?"

## 주의 필요 이슈 표시

issue tracker에서 다음 3개 버킷을 조회하여 오래된 순으로 표시:

1. **Unlabeled** — 한 번도 triage 안 됨
2. **`needs-triage`** — 평가 진행 중
3. **`needs-info`** — 리포터가 마지막 triage 노트 이후 활동했음 (재평가 필요)

각 이슈에 카운트 + 한 줄 요약 표시. 메인테이너가 고르게 함.

## 특정 이슈 트리아지

1. **컨텍스트 수집** — 이슈 본문, 코멘트, 라벨, 리포터, 날짜 모두 읽기. 이전 triage 노트 파싱. CONTEXT.md / ADR 확인. `.out-of-scope/*.md`에 유사 거절 사례가 있는지 확인.

2. **추천** — 카테고리/상태 추천 + 이유 + 관련 코드베이스 요약. 메인테이너 지시 대기.

3. **재현 (버그만)** — Grilling 전에 재현 시도. 리포터 단계 따라가서 코드 추적, 테스트/명령 실행. 결과 보고:
   - 성공: 코드 경로 명시
   - 실패: 어떤 점이 안 맞는지
   - 정보 부족: 강한 `needs-info` 신호

4. **Grilling (필요 시)** — 이슈가 추상적이면 `/grill-with-docs` 세션 실행.

5. **결과 적용**:
   - `ready-for-agent` → 에이전트 브리프 코멘트 작성 ([AGENT-BRIEF.md](./AGENT-BRIEF.md))
   - `ready-for-human` → 같은 구조지만 위임 못 하는 이유 명시 (판단 결정, 외부 접근, 매뉴얼 테스트 등)
   - `needs-info` → 아래 템플릿
   - `wontfix` (bug) → 정중한 설명 + 종료
   - `wontfix` (enhancement) → `.out-of-scope/`에 기록 ([OUT-OF-SCOPE.md](./OUT-OF-SCOPE.md)) + 코멘트로 링크 + 종료

## 빠른 상태 override

메인테이너가 "#42를 ready-for-agent로"라고 하면 그대로 적용. 라벨 변경 + 코멘트 + 종료를 확정 후 실행. Grilling 생략. `ready-for-agent`로 옮기는데 grilling 세션이 없었다면 에이전트 브리프 작성 여부 물어봄.

## Needs-info 템플릿

```markdown
## Triage Notes

**지금까지 확인된 것**:

- 항목 1
- 항목 2

**리포터(@reporter)에게 필요한 것**:

- 질문 1
- 질문 2
```

Grilling으로 풀린 내용은 "지금까지 확인된 것"에 모두 보존. 질문은 구체적이고 actionable해야 함 ("더 정보 주세요" 금지).

## 이전 세션 이어가기

이슈에 이전 triage 노트가 있으면 읽고, 리포터가 미해결 질문에 답했는지 확인 후 갱신된 그림 제시. 이미 풀린 질문 다시 묻지 않음.

## Track 시스템 통합

`ready-for-agent`로 결정되고 메인테이너 승인 시 새 Track을 자동 생성할 수 있다 (`.claude/tracks/{이슈키}-{설명}/`). `1_REQ-SNAPSHOT.md`에 이슈 본문 + AGENT-BRIEF 저장.
