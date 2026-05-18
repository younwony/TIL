# ADR 0004 — 워크플로우 산출 문서를 Markdown에서 HTML로 전환

**Status**: Accepted
**Date**: 2026-05-18
**Related**: ADR 0002 (Track as Skill Container), `html-doc` 스킬, `track-status/references/document-system.md`

---

## Context

워크플로우 스킬(`work-plan`, `work-plan-start`, `self-review`, `qa-scenario`, `track-status` 등)이
생성하는 산출 문서는 모두 Markdown(`2_WORK-SPEC.md`, `5_ARCHITECTURE.md` 등)이었다.
다음 자료들이 공통된 결론을 가리켰다.

1. **Thariq, "HTML effectiveness"** (thariqs.github.io/html-effectiveness) — LLM이 생성하는
   산출물 문서는 Markdown의 선형 구조보다 HTML의 공간적 표현(비교표·다이어그램·타임라인·
   collapsible)이 효과적이다. 권장 산출물은 단일 `.html` 자체 완결 파일.
2. **Claude Code 팀(Thorick), "HTML is the new Markdown"** + **Karpathy 지지** — Markdown은
   정보 밀도·가독성·상호작용의 천장이 있고, HTML이 CSS 레이아웃·SVG·양방향 흐름으로 그
   천장을 깬다.

핵심 구분: 이 주장이 적용되는 대상은 **사람이 읽을 산출물 문서**다. LLM 컨텍스트에 로드되는
파일이나 GitHub에서 렌더링되는 학습 콘텐츠는 대상이 아니다.

## Decision

워크플로우 스킬이 생성하는 **사람이 읽을 산출 문서**를 자체 완결 단일 HTML 파일로 생성한다.

- **`html-doc` 공통 스킬 신설** (`productivity/`) — HTML 템플릿(`template.html`), 재사용
  컴포넌트(`components.html`: collapsible·배지·비교표·콜아웃·타임라인·SVG 다이어그램),
  작성 규칙을 제공한다. 산출 문서를 만드는 모든 스킬이 이 스킬을 따른다.
- **SSOT 개정** — `document-system.md`의 번호 체계를 `.html`로 전환
  (`0_INDEX.html` ~ `9_*.html`), 다이어그램을 인라인 `<svg>` 임베드로 규정.
- **시각화 필수화** — 산출 문서마다 핵심 흐름·구조를 인라인 SVG 다이어그램으로 최소 1개
  이상 포함한다. 텍스트만으로 채우지 않는다.

### 적용 범위

| 구분 | 대상 |
|------|------|
| **전환 (HTML)** | WORK-SPEC, ARCHITECTURE, SPEC, FEATURE-CHECKLIST, SELF-REVIEW, QA-SCENARIOS, REQ-SNAPSHOT, PLAN, 0_INDEX, 리뷰 보고서(PR-REVIEW, TEAM-REVIEW, PRODUCT-REVIEW), DATABASE 등 `{DOC_DIR}` 산출 문서 |
| **제외 (md 유지)** | `SKILL.md`·`CLAUDE.md`(LLM 컨텍스트용), `README`·`cs/` 학습 콘텐츠(GitHub 렌더링용), `CONTEXT.md`·ADR(LLM·GitHub 공용 기록), Confluence 산출물(`work-log`/`work-share`) |

## Consequences

**Positive**
- 산출 문서의 가독성·정보 밀도 향상 — 비교표·collapsible·인라인 SVG 다이어그램으로
  공간적 표현 가능.
- `html-doc` 공통 스킬로 템플릿·컴포넌트 중복 제거 (deep module).
- SVG가 CSS 변수를 상속하므로 다크모드·인쇄에 자동 대응.

**Negative**
- HTML은 `git diff` 가독성이 낮다 — 단, tracks/ 산출물은 검토용이라 라인 diff를 정밀히
  볼 일이 적어 영향이 작다.
- 산출물 토큰이 Markdown보다 소폭 증가 — 컨텍스트 재투입 빈도가 낮아 수용 가능.
- 전환기에 기존 `.md` 산출물과 신규 `.html`이 공존 — 스킬의 파일 탐색을 확장자 비의존
  패턴(`*_PLAN.*`)으로 처리한다.

## Implementation

- `html-doc` 스킬 신설 (`productivity/html-doc/` — SKILL.md, references/template.html,
  references/components.html).
- SSOT(`document-system.md`) HTML 기준 개정.
- 산출 스킬 전환: `self-review`, `qa-scenario`, `work-plan`(+`to-prd`/`to-issues`),
  `work-plan-start`, `track-status`, `feature-check`, `design-spec-generate`, `triage`,
  `web-pipeline`, `product-review`, 리뷰 커맨드(`review-pr`/`team-review`).
- 글로벌(`~/.claude/`) 원본 수정 후 `sync-global`로 TIL 로컬 반영.
- 기존 생성된 `.md` 산출물은 마이그레이션하지 않는다 (신규 생성분부터 적용).

## Related

- `.claude/docs/2026-05-18-html-doc-migration-design.md` — 설계 문서
- `.claude/docs/2026-05-18-html-doc-migration-plan.md` — 구현 계획
- ADR 0002 — Track as Skill Container
