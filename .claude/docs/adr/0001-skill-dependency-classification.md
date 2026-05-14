# ADR 0001 — Skill Hard/Soft Dependency 분류

**Status**: Accepted
**Date**: 2026-04-29
**Inspired by**: [mattpocock/skills ADR 0001](https://github.com/mattpocock/skills/blob/main/docs/adr/0001-explicit-setup-pointer-only-for-hard-dependencies.md)

---

## Context

이 저장소(`~/.claude/` 글로벌 + `TIL/.claude/` 로컬)에는 skill·command·agent가 60개 이상 있다. 일부는 사전 조건 없이 동작 불가하지만(`work-plan-start`는 WORK-SPEC.md 필수, `work-log`는 ATLASSIAN_API_TOKEN 필수), 다른 일부는 환경이 부족해도 graceful degrade 한다(`code-refactor`는 CONTEXT.md 없어도 동작).

이 둘이 구분되지 않아 다음 문제가 발생한다:

1. **새 PC 클론 직후 무엇을 setup해야 하는지 불명확** — 사용자가 모든 skill을 일일이 시도하다가 실패 메시지로 사전 조건을 학습.
2. **모든 skill SKILL.md에 setup 안내가 cargo cult로 박힐 위험** — 토큰 낭비 + 사용자 혼란.
3. **`/setup-til-skills` 같은 자동 setup skill을 만들 때 입력이 없음** — 어떤 skill이 무엇을 요구하는지 정리된 곳이 없음.

## Decision

모든 skill·command를 **Hard / Soft / None** 3분류로 명시한다.

### Hard Dependency (사전 조건 없이 동작 불가)

해당 skill의 SKILL.md frontmatter 또는 첫 줄에 `**Requires**: ...` 명시.

| Skill / Command | 사전 조건 |
|----------------|---------|
| `work-plan-start` | `WORK-SPEC.md` 존재 (= Active Track의 `*_WORK-SPEC.md`) |
| `feature-check` | `FEATURE-CHECKLIST.md` 존재 |
| `work-log`, `work-share`, `slack-to-confluence`, `meeting-notes` | `ATLASSIAN_API_TOKEN` 환경변수 |
| `jira-report`, `jira-notify` | Jira MCP 연결 |
| `slack-to-jira`, `slack-to-confluence`, `slack-digest`, `slack-remind`, `standup-summary`, `meeting-notes`, `sprint-start-notify` | Slack MCP 연결 |
| `figma-read` | figma-team MCP 연결 |
| `browser-debug` | Chrome MCP 또는 Playwright 설치 |
| `browser-debug-chrome` | Chrome MCP 연결 |
| `db-inspect`, `db-tune` | `mysqlsh` 설치 + 로컬 DB 접근 가능 |
| `prod-db-inspect` | `mysqlsh` + production DB 접근 권한 |
| `ssh-server-inspect` | SSH 키 설정 + 서버 별칭 등록 |
| `pr`, `review-pr` | `gh` CLI 인증 |
| `docker-up`, `docker-update`, `docker-down`, `docker-logs`, `docker-status` | Docker Desktop 설치 |
| `excel-reader` | `openpyxl` 또는 호환 도구 |
| `cs-guide-writer`, `cs-sync`, `cs-link-sync` | TIL 저장소(`cs/` 디렉토리) 안에서만 |
| `pencil-screen`, `pencil-update`, `pencil-to-code` | Pencil MCP 연결 |
| `3ai-plan` | Codex CLI/Plugin + Gemini CLI 둘 다 설치 |
| `work-plan` | (Soft) Codex/Gemini 있으면 크로스체크, 없어도 동작 |

### Soft Dependency (없어도 graceful degrade)

SKILL.md에 사전 조건을 **명시하지 않는다** (cargo cult 방지).

대표: `code-refactor`, `debugger`, `test-generator`, `qa-scenario`, `self-review`, `team-review`, `security-audit`, `ai-slop-detect`, `weekly-retro`, `product-review`, `track-status`, `track-status-summary`, `handoff`, `today`, `mermaid-diagram`, `svg-diagram`, `skill-rebuild`, `smart-session`.

이들은 CONTEXT.md / 도메인 문서 / Codex/Gemini가 있으면 활용하지만, 없어도 기본 동작 보장.

### None (전제 조건 없음)

거의 모든 Soft가 여기 해당. 별도 표시 안 함.

## Consequences

**Positive**
- 새 PC 클론 시 `/setup-til-skills` 한 번 실행으로 Hard 의존만 setup → 즉시 사용 가능한 skill 목록 자동 산출.
- SKILL.md의 token weight 감소 (Soft에 setup 안내 박지 않음).
- 신규 skill 작성 시 분류 강제로 사전 조건 누락 방지.

**Negative**
- 분류 유지보수 비용 — 새 skill 추가/변경 시 이 표 업데이트 필요. → `/sync-global` 또는 `/setup-til-skills`에 검증 단계 추가로 완화.
- "Soft인데 사실 Hard였다"는 회색지대 발견 시 표 갱신 필요.

## Implementation

1. 본 ADR을 단일 진실의 원천(single source of truth)으로 사용.
2. `/setup-til-skills` skill이 Hard 의존을 순회하며 환경 점검 + 미충족 시 안내.
3. 새 skill 추가 시 `/skill-rebuild` 또는 작성자가 본 ADR 표에 한 줄 추가.
4. 분기마다 (또는 skill 5개 이상 추가 시) 본 ADR 갱신 점검.

## Related

- `mattpocock/skills` 의 ADR 0001 (영감 출처)
- `.claude/CONTEXT.md` (의존성 표에서 사용하는 도메인 용어 정의)
- `cs/tool/mattpocock-skills-harness.md` (이 분류 철학의 배경)
