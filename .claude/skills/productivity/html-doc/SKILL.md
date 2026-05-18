---
name: html-doc
description: 워크플로우 산출 문서를 자체 완결 단일 HTML 파일로 작성하는 공통 템플릿·규칙. WORK-SPEC/ARCHITECTURE/SPEC/SELF-REVIEW/QA-SCENARIOS 등 사람이 읽을 산출물을 생성하는 모든 스킬이 이 스킬의 템플릿과 규칙을 따른다. "HTML 문서 생성", "산출 문서 작성" 시 참조.
---

# HTML 산출 문서 작성

워크플로우 스킬이 생성하는 **사람이 읽을 산출 문서**를 자체 완결 단일 `.html` 파일로 작성한다.

## 적용 대상 / 비대상

- **대상:** `{DOC_DIR}`에 생성되는 워크플로우 산출 문서 (0_INDEX, WORK-SPEC, ARCHITECTURE, SPEC, FEATURE-CHECKLIST, SELF-REVIEW, QA-SCENARIOS, 리뷰 보고서 등)
- **비대상:** `SKILL.md`·`CLAUDE.md`(LLM 컨텍스트용), `README`·`cs/` 학습 콘텐츠(GitHub 렌더링용), Confluence 산출물

## 핵심 규칙

1. **자체 완결 단일 파일** — 외부 CSS/JS/이미지 의존 금지. 스타일은 인라인 `<style>`, 다이어그램은 인라인 `<svg>`.
2. **템플릿 사용** — `references/template.html`을 skeleton으로 복사하고 내용을 채운다.
3. **컴포넌트 재사용** — collapsible·비교표·배지·콜아웃·타임라인은 `references/components.html` 스니펫을 사용한다.
4. **JavaScript 최소화** — collapsible은 `<details>` 네이티브로 처리한다. 불가피한 경우만 인라인 `<script>` 허용.
5. **시각화 필수** — 산출 문서마다 핵심 흐름·구조·비교를 **인라인 SVG 다이어그램으로 최소 1개 이상** 시각화한다. 텍스트만으로 채우지 않는다. `svg-diagram` 스킬로 생성하거나 `components.html`의 SVG 컴포넌트를 활용하고, 별도 파일이 아닌 본문에 인라인 `<svg>...</svg>`로 임베드한다.
6. **문서 간 링크** — 상대 경로 `.html`로 링크한다 (예: `<a href="6_SPEC.html">`).
7. **접근성** — `<html lang="ko">`, 문서 첫 부분에 "이 문서는 무엇인가" 1~2문장, 다이어그램에 범례(`<figcaption>`)와 `role="img"`/`aria-label`.

## 시각화 가이드 (문서 종류별 권장 다이어그램)

산출 문서는 가독성을 위해 아래 다이어그램을 1개 이상 포함한다. SVG는 CSS 변수(`var(--accent)` 등)를 상속하므로 다크모드에 자동 대응한다.

| 문서 | 권장 다이어그램 | components.html 컴포넌트 |
|------|----------------|--------------------------|
| ARCHITECTURE | 시스템·모듈 구조도 | 구조도 |
| SPEC | 데이터·처리 흐름도 | 플로우 다이어그램 |
| SELF-REVIEW / 리뷰 보고서 | 심각도별 이슈 분포 | 막대 차트 |
| QA-SCENARIOS | 영향도 매트릭스 | 비교표 + 막대 차트 |
| WORK-SPEC / PLAN | 작업 흐름·Phase 타임라인 | 플로우 다이어그램 + 타임라인 |
| 0_INDEX | 문서 관계도 | 구조도 |

## 작성 절차

1. `references/template.html`을 산출 경로로 복사한다.
2. `<title>`, 문서 헤더(제목·메타·요약)를 채운다.
3. 각 섹션을 `<section>`으로 작성한다. 긴 보조 내용은 `<details>`로 접는다.
4. 비교·체크리스트·상태는 `<table>`/배지 컴포넌트로 표현한다.
5. **핵심 흐름·구조를 인라인 SVG 다이어그램으로 1개 이상 시각화한다 (필수).** 위 시각화 가이드와 `components.html`의 SVG 컴포넌트를 활용한다.
6. 브라우저로 열어 렌더를 확인한다. (사람의 수동 확인 단계 — LLM은 산출 후 이 단계를 사용자에게 안내한다.)

## 참조

- `references/template.html` — HTML skeleton (인라인 CSS 포함)
- `references/components.html` — 재사용 컴포넌트 스니펫 모음
- 문서 번호 체계·경로: `track-status/references/document-system.md` (SSOT)
