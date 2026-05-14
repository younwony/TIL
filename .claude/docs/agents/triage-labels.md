# Triage Labels — TIL Jira 매핑

`/triage` skill이 사용하는 5종 canonical 역할 → 실제 Jira 상태/라벨 매핑.

> 참고: TIL은 Jira의 표준 워크플로우를 따른다. Status로 1차 매핑, 추가 분류는 Label로.

## 매핑 표

| Canonical role | Jira Status | Jira Label (보조) | 비고 |
|----------------|-------------|-------------------|------|
| `needs-triage` | `Selected for Development` | `needs-triage` | 메인테이너 평가 대기 |
| `needs-info` | `In Progress` (담당자 = 리포터) | `needs-info` | 리포터 응답 대기 |
| `ready-for-agent` | `Selected for Development` | `ready-for-agent` | AFK 픽업 가능 |
| `ready-for-human` | `Selected for Development` | `ready-for-human` | 사람 구현 필요 |
| `wontfix` | `Done` (resolution = `Won't Do`) | `wontfix` | 작업 안 함 |

## 카테고리 매핑

| Canonical | Jira issuetype |
|-----------|---------------|
| `bug` | `Bug` |
| `enhancement` | `Task` / `Story` |

## Transition ID

`/triage` 가 curl로 상태 변경 시 사용하는 ID. 실제 ID는 프로젝트마다 다르므로 사용 시 다음 명령으로 확인:

```bash
curl -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{ISSUE_KEY}/transitions"
```

## 라벨 추가 규칙

- 한 이슈에 **카테고리 1개 + 상태 1개**.
- `needs-info`는 상호배타적 (`ready-for-*`와 동시 X).
- `wontfix`는 종료 (다른 상태 라벨과 동시 X).

## TIL 고유 라벨

위 표준 외에 TIL에서만 쓰는 라벨:

- `tech-debt` — 기술 부채
- `breaking-change` — 호환성 깨짐
- `kaegi` — Kaegi (개발 환경) 관련
- `qa-blocker` — QA 진행 차단

이 라벨은 `/triage` skill이 자동 적용하지 않는다 — 메인테이너가 수동.

## 매핑 갱신 절차

Jira 워크플로우가 바뀌면 본 문서를 먼저 갱신, 그 다음 `/triage` skill의 호출 동작이 자동으로 반영된다. (`/triage`는 본 문서를 입력으로 사용)

## 관련

- `/triage` skill — 본 문서를 입력으로 사용
- [issue-tracker.md](./issue-tracker.md) — Jira API 호출 패턴
- [domain.md](./domain.md) — 도메인 문서 위치
