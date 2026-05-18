# 워크플로우 산출 문서 HTML 전환 — 설계 문서

> 이 문서는 무엇인가: Claude Code 스킬·커맨드가 생성하는 **워크플로우 산출 문서**의 형식을
> Markdown에서 HTML로 전환하기 위한 설계 명세다. `writing-plans`가 이 문서를 입력으로
> 상세 구현 계획을 만든다.

## 1. 배경

세 자료가 같은 결론을 가리킨다.

- **Thariq, "HTML effectiveness"** (thariqs.github.io/html-effectiveness): LLM이 생성하는
  산출물 문서는 Markdown의 선형 구조보다 HTML의 공간적 표현(비교표·다이어그램·타임라인·
  collapsible)이 효과적이다. 권장 산출물은 **단일 `.html` 자체 완결 파일**.
- **Claude Code 팀(Thorick), "HTML is the new Markdown"** + **Karpathy 지지**: Markdown은
  정보 밀도·가독성·상호작용의 천장이 있고, HTML이 CSS 레이아웃·SVG·양방향 흐름으로 그
  천장을 깬다.
- **YouTube "Anthropic 엔지니어가 마크다운 버린 5가지 이유"**: 위와 동일 맥락.

핵심 구분: 이 주장이 적용되는 대상은 **사람이 읽을 산출물 문서**다. LLM 컨텍스트에 로드되는
파일(`SKILL.md`, `CLAUDE.md`)이나 GitHub에서 렌더링되는 학습 콘텐츠(`cs/`)는 대상이 아니다.

## 2. 목표 / 비목표

### 목표
- 워크플로우 스킬이 생성하는 산출 문서(WORK-SPEC, ARCHITECTURE, SPEC, FEATURE-CHECKLIST,
  SELF-REVIEW, QA-SCENARIOS, 0_INDEX, 리뷰 보고서)를 HTML로 생성하도록 전환한다.
- 글로벌(`~/.claude/skills`)과 TIL 로컬(`.claude/skills`)에 동시 적용한다.
- 파일럿 → 확산의 2단계로 진행하여 검증 루프를 확보한다.

### 비목표
- 기존에 생성된 `.md` 산출물의 일괄 변환(마이그레이션) — 하지 않는다. 신규 생성분부터 적용.
- `SKILL.md`·`CLAUDE.md`·`README`·`cs/` 학습 콘텐츠의 HTML화 — 제외(md 유지).
- Confluence 산출물(`work-log`/`work-share`) — Confluence storage format은 별도 체계, 제외.
- 다이어그램 생성 방식 변경 — SVG 생성은 그대로, **임베드 방식만** 규정한다.

## 3. 설계

### 3.1 신규 스킬 `html-doc` (productivity 버킷)

자체 완결 단일 HTML 산출물의 **공통 템플릿과 작성 규칙**을 제공하는 deep module.

- 자체 완결 단일 `.html`: 인라인 `<style>`(라이트/다크·반응형·인쇄 미디어쿼리)
- 컴포넌트: `<details>` 네이티브 collapsible, 비교표, 상태 배지, 단계 타임라인, 콜아웃 박스
- SVG 다이어그램은 `<img>`가 아닌 **인라인 `<svg>`**로 임베드 → 단일 파일 자체 완결성 유지
- JavaScript 최소화(단순성 원칙). collapsible은 `<details>`로 충분. 불가피한 경우만 인라인 `<script>`
- `references/` 에 HTML skeleton 템플릿과 컴포넌트 스니펫 보관
- `svg-diagram` 스킬과 연계: 다이어그램 생성은 기존대로, html-doc은 임베드 규약만 정의

### 3.2 SSOT 개정 — `track-status/references/document-system.md`

문서 시스템의 단일 진실 출처. 여기를 바꾸면 일관성이 전파된다.

- 번호 체계 확장자 `.md` → `.html`: `0_INDEX.html`, `2_WORK-SPEC.html`,
  `5_ARCHITECTURE.html`, `7_SELF-REVIEW.html`, `8_QA-SCENARIOS.html` 등
- 파일 탐색 패턴: `*_PLAN.md` → `*_PLAN.html` (확장자 비의존 매칭 권장: `*_PLAN.*`)
- 문서 간 링크: 상대 경로 `.html`
- 다이어그램 섹션: `images/xxx.svg` 외부 참조 → 인라인 `<svg>` 임베드 규칙으로 개정
- `1_REQ-SNAPSHOT`: 원본 req가 md 텍스트이므로 HTML 안에 `<pre>` 블록으로 보존
- 작성 원칙(자기 완결성·비전문가 이해·배경 섹션)은 그대로 유지, HTML 표현으로 매핑

### 3.3 CLAUDE.md 동기화

- 글로벌 `~/.claude/CLAUDE.md`: "워크플로우 문서 시스템"은 SSOT 한 줄 링크 → 링크 유지,
  필요 시 문구만 보정
- TIL `.claude/CLAUDE.md`: "워크플로우 문서 시스템" 섹션이 **인라인 전문**으로 박혀 있음 →
  SSOT 개정 내용과 일치하도록 HTML 기준으로 갱신

### 3.4 ADR 작성

`.claude/docs/adr/`(또는 `cs/` ADR 체계)에 이 결정을 기록한다.
근거 자료 3건, 적용 범위, 제외 대상, 트레이드오프를 남긴다.

## 4. 실행 단계

### Phase 1 — 파일럿
1. `html-doc` 스킬 신설 (글로벌 `productivity/`) → verify: 샘플 HTML이 브라우저에서 열림
2. `document-system.md` SSOT를 HTML 기준으로 개정 → verify: 번호 체계·링크·다이어그램 규칙 일관
3. `work-plan`, `self-review` 2개 스킬을 HTML 산출로 전환 (SKILL.md + 해당 references 템플릿)
   → verify: 두 스킬 실행 시 `.html` 산출물 생성, 템플릿 컴포넌트 정상 렌더
4. ADR 작성 → verify: 근거·범위·트레이드오프 명시
5. `sync-global`로 TIL 로컬 반영 → verify: 글로벌/로컬 diff 없음

### Phase 2 — 확산
대상 스킬: `work-plan-start`, `qa-scenario`, `track-status`, `track-status-summary`,
`feature-check`, `design-spec-generate`, `to-prd`, `to-issues`, `triage`, `web-pipeline`,
`grill-with-docs`, `product-review`, `prototype` 및 리뷰 커맨드(`review-pr`, `team-review`).

각 스킬: SKILL.md의 산출물 형식 지시 + `references/` 템플릿(`workspec-template`,
`completion-report`, `qa-document-template`, `AGENT-BRIEF` 등)을 HTML로 전환.
→ verify: 각 스킬 실행 시 HTML 산출 + html-doc 템플릿 준수.

## 5. 트레이드오프

- **`git diff` 가독성 저하**: HTML은 diff가 어렵다. 단, tracks/ 문서는 검토용 산출물이라
  코드 리뷰처럼 라인 diff를 정밀히 볼 일이 적어 영향이 작다.
- **토큰**: HTML이 md보다 약간 길다. 산출물은 컨텍스트 재투입 빈도가 낮아 수용 가능.
- **기존 md 산출물 혼재**: 마이그레이션을 하지 않으므로 한동안 `.md`/`.html`이 공존한다.
  스킬의 파일 탐색은 확장자 비의존 패턴으로 처리한다.

## 6. 검증 기준

- Phase 1 종료 시: `work-plan`·`self-review` 실행 → 브라우저에서 열리는 자체 완결 HTML 생성,
  인라인 SVG·collapsible·비교표가 정상 렌더.
- Phase 2 종료 시: 모든 대상 스킬이 html-doc 템플릿을 따르는 HTML을 산출.
- 글로벌/로컬 `sync-global status` diff 없음.
