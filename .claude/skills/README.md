# Skills — TIL 카탈로그

Matt Pocock 패턴을 차용한 6-bucket 구조. 각 skill은 atomic 단위로 설계되며, 큰 워크플로우는 atomic skill의 조합으로 구성된다.

TIL은 글로벌 설정과 거의 동일하되 **TIL 고유 skill**(`3ai-plan`, `ai-slop-detect`, `pencil-*`, `cs-*`, `weekly-retro`, `smart-session`)을 추가로 보유한다. 동기화는 [`sync-global`](./productivity/sync-global/SKILL.md)로 관리.

> 영감 출처: [mattpocock/skills](https://github.com/mattpocock/skills). 한국어 워크플로우와 Track 시스템에 맞춰 변형.

## 디렉토리 구조

```
.claude/skills/
├── engineering/    ← 일일 코드 작업
├── productivity/   ← 일일 워크플로우 도구
├── misc/           ← 가끔 사용
├── personal/       ← TIL 고유 / 회사 특화 (노출 X)
├── in-progress/    ← 드래프트 (노출 X)
└── deprecated/     ← 폐기 (노출 X)
```

## 4가지 실패 모드 매핑

| # | 실패 모드 | 대응 skill |
|---|----------|-----------|
| 1 | **Alignment** — 에이전트가 원하는 걸 잘못 이해 | [`product-review`](./engineering/product-review/SKILL.md) + grilling |
| 2 | **Verbosity** — 도메인 용어 모르고 장황 | `CONTEXT.md` + [`zoom-out`](./engineering/zoom-out/SKILL.md) + [`caveman`](./productivity/caveman/SKILL.md) |
| 3 | **Doesn't Work** — 피드백 루프 부족 | [`test-coverage-check`](./engineering/test-coverage-check/SKILL.md) + `debugger` 5-phase + [`ai-slop-detect`](./engineering/ai-slop-detect/SKILL.md) |
| 4 | **Ball of Mud** — 진흙 공 | [`zoom-out`](./engineering/zoom-out/SKILL.md) + `code-refactor` agent |

## Engineering (일일 코드 작업)

> 상세: [engineering/README.md](./engineering/README.md)

`work-plan`, `work-plan-start`, `qa-scenario`, `browser-debug`, `browser-debug-chrome`, `feature-check`, `test-coverage-check`, `product-review`, `zoom-out`, **`3ai-plan`**, **`ai-slop-detect`**, **`pencil-screen`**, **`pencil-to-code`**, **`pencil-update`**

(굵게: TIL 고유)

## Productivity (일일 워크플로우)

> 상세: [productivity/README.md](./productivity/README.md)

`caveman`, `work-log`, `skill-rebuild`, `sync-global`, `setup-til-skills`, `track-status`, `track-status-summary`, `mermaid-diagram`, `svg-diagram`, **`smart-session`**, **`weekly-retro`**

## Misc (가끔 사용)

> 상세: [misc/README.md](./misc/README.md)

`docker-up`, `ssh-server-inspect`, `security-audit`, `prod-db-inspect`

## 비공개 버킷

- **personal/** — TIL/CS 전용 + 회사 특화 (`cs-*`, `guhada-common-convention`, `java-code-rules`)
- **in-progress/** — 비어 있음
- **deprecated/** — 비어 있음

## 관련 문서

- TIL CLAUDE.md (`/CLAUDE.md`)
- 워크플로우 가이드 (`.claude/docs/WORKFLOW-GUIDELINE.md`)
- 도메인 용어집 (`.claude/CONTEXT.md`)
- ADR 0001 — Skill Hard/Soft Dependency (`.claude/docs/adr/0001-skill-dependency-classification.md`)
