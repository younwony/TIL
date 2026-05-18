# 워크플로우 산출 문서 HTML 전환 — 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Claude Code 워크플로우 스킬·커맨드가 생성하는 사람용 산출 문서를 Markdown 대신 자체 완결 HTML로 생성하도록 전환한다.

**Architecture:** 공통 `html-doc` 스킬 1개가 HTML 템플릿·컴포넌트·작성 규칙을 제공한다. 문서 시스템 SSOT(`document-system.md`)를 HTML 기준으로 개정하면 일관성이 전파된다. 파일럿(self-review·qa-scenario) → 확산(나머지)의 2단계로 진행한다.

**Tech Stack:** Markdown 스킬 정의 파일, 자체 완결 HTML5(인라인 CSS, `<details>`, 인라인 SVG), PowerShell/Bash.

**입력 설계 문서:** `.claude/docs/2026-05-18-html-doc-migration-design.md`

---

## 공통 규칙 (모든 Task 적용)

- **Git:** TIL CLAUDE.md 규칙에 따라 각 Task 종료 시 `git add`까지만 한다. `git commit`은 하지 않는다(사용자 명시 요청 시에만).
- **수정 대상:** 글로벌 원본(`~/.claude/...`)을 수정한다. TIL 로컬(`C:\workspace\intellij\TIL\.claude\...`)은 Task 11의 `sync-global`로 일괄 반영한다. 단 CLAUDE.md 인라인 섹션(Task 10)은 글로벌·로컬·TIL루트 3곳을 직접 수정한다.
- **검증 도구:** HTML 렌더 확인은 `Start-Process <파일>`로 기본 브라우저에서 연다.
- **제외 대상(절대 변경 금지):** `SKILL.md`/`CLAUDE.md` 자체의 형식(내용 지시만 수정), `README`, `cs/` 학습 콘텐츠, `work-log`/`work-share`의 Confluence 산출물.

---

## Phase 1 — 파일럿

### Task 1: `html-doc` 스킬 신설 — SKILL.md

**Files:**
- Create: `~/.claude/skills/productivity/html-doc/SKILL.md`

- [ ] **Step 1: SKILL.md 작성**

아래 내용으로 파일을 생성한다.

````markdown
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
5. **다이어그램** — `svg-diagram` 스킬로 SVG를 생성하되, 별도 파일이 아닌 본문에 인라인 `<svg>...</svg>`로 임베드한다.
6. **문서 간 링크** — 상대 경로 `.html`로 링크한다 (예: `<a href="6_SPEC.html">`).
7. **접근성** — `<html lang="ko">`, 문서 첫 부분에 "이 문서는 무엇인가" 1~2문장, 다이어그램에 범례.

## 작성 절차

1. `references/template.html`을 산출 경로로 복사한다.
2. `<title>`, 문서 헤더(제목·메타·요약)를 채운다.
3. 각 섹션을 `<section>`으로 작성한다. 긴 보조 내용은 `<details>`로 접는다.
4. 비교·체크리스트·상태는 `<table>`/배지 컴포넌트로 표현한다.
5. 다이어그램이 필요하면 인라인 `<svg>`로 임베드한다.
6. 브라우저로 열어 렌더를 확인한다.

## 참조

- `references/template.html` — HTML skeleton (인라인 CSS 포함)
- `references/components.html` — 재사용 컴포넌트 스니펫 모음
- 문서 번호 체계·경로: `track-status/references/document-system.md` (SSOT)
````

- [ ] **Step 2: git add**

```
git add ~/.claude/skills/productivity/html-doc/SKILL.md
```

---

### Task 2: `html-doc` 스킬 — template.html

**Files:**
- Create: `~/.claude/skills/productivity/html-doc/references/template.html`

- [ ] **Step 1: template.html 작성**

아래 내용으로 파일을 생성한다. CSS 변수로 라이트/다크(`prefers-color-scheme`), 반응형, 인쇄 미디어쿼리를 처리한다.

```html
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>{문서 제목}</title>
<style>
  :root {
    --bg: #ffffff; --fg: #1a1a1a; --muted: #666; --border: #e2e2e2;
    --accent: #2563eb; --code-bg: #f4f4f5; --callout-bg: #f0f7ff;
    --ok: #16a34a; --warn: #d97706; --err: #dc2626;
  }
  @media (prefers-color-scheme: dark) {
    :root {
      --bg: #1a1a1a; --fg: #e8e8e8; --muted: #999; --border: #3a3a3a;
      --accent: #60a5fa; --code-bg: #2a2a2a; --callout-bg: #1e2a3a;
    }
  }
  * { box-sizing: border-box; }
  body {
    margin: 0; padding: 2rem 1rem; background: var(--bg); color: var(--fg);
    font-family: -apple-system, "Segoe UI", "Malgun Gothic", sans-serif;
    line-height: 1.7; max-width: 960px; margin-inline: auto;
  }
  h1, h2, h3 { line-height: 1.3; }
  h1 { font-size: 1.8rem; border-bottom: 2px solid var(--accent); padding-bottom: .4rem; }
  h2 { font-size: 1.35rem; margin-top: 2.5rem; }
  .doc-meta { color: var(--muted); font-size: .9rem; }
  .doc-summary {
    background: var(--callout-bg); border-left: 4px solid var(--accent);
    padding: .8rem 1rem; border-radius: 4px; margin: 1rem 0;
  }
  table { border-collapse: collapse; width: 100%; margin: 1rem 0; }
  th, td { border: 1px solid var(--border); padding: .5rem .7rem; text-align: left; }
  th { background: var(--code-bg); }
  code { background: var(--code-bg); padding: .1rem .3rem; border-radius: 3px; }
  pre { background: var(--code-bg); padding: 1rem; border-radius: 6px; overflow-x: auto; }
  details {
    border: 1px solid var(--border); border-radius: 6px;
    padding: .5rem .8rem; margin: .8rem 0;
  }
  summary { cursor: pointer; font-weight: 600; }
  .badge {
    display: inline-block; padding: .1rem .5rem; border-radius: 10px;
    font-size: .8rem; font-weight: 600; color: #fff;
  }
  .badge-ok { background: var(--ok); }
  .badge-warn { background: var(--warn); }
  .badge-err { background: var(--err); }
  .callout {
    background: var(--callout-bg); border-left: 4px solid var(--accent);
    padding: .8rem 1rem; border-radius: 4px; margin: 1rem 0;
  }
  svg { max-width: 100%; height: auto; }
  @media print {
    body { max-width: none; }
    details { open: true; }
    @page { margin: 1.5cm; }
  }
</style>
</head>
<body>
<h1>{문서 제목}</h1>
<p class="doc-meta">생성일: {날짜} · Track: {track_id} · 단계: {워크플로우 단계}</p>
<div class="doc-summary">{이 문서는 무엇인가 — 1~2문장}</div>

<!-- 섹션을 여기에 작성. 각 섹션은 <section><h2>...</h2>...</section> -->

</body>
</html>
```

- [ ] **Step 2: 브라우저 렌더 확인**

Run: `Start-Process ~/.claude/skills/productivity/html-doc/references/template.html`
Expected: 브라우저에서 제목·메타·요약 박스가 정상 렌더. OS 다크모드 시 다크 팔레트 적용.

- [ ] **Step 3: git add**

```
git add ~/.claude/skills/productivity/html-doc/references/template.html
```

---

### Task 3: `html-doc` 스킬 — components.html

**Files:**
- Create: `~/.claude/skills/productivity/html-doc/references/components.html`

- [ ] **Step 1: components.html 작성**

재사용 컴포넌트 스니펫 모음. 각 스킬이 복사해 쓴다.

```html
<!-- ===== 재사용 컴포넌트 스니펫 (template.html의 <style>에 의존) ===== -->

<!-- 1. Collapsible 섹션 -->
<details>
  <summary>{접는 제목}</summary>
  <p>{접힌 내용}</p>
</details>

<!-- 2. 상태 배지 -->
<span class="badge badge-ok">통과</span>
<span class="badge badge-warn">주의</span>
<span class="badge badge-err">실패</span>

<!-- 3. 비교표 -->
<table>
  <thead><tr><th>항목</th><th>A안</th><th>B안</th></tr></thead>
  <tbody>
    <tr><td>{기준}</td><td>{값}</td><td>{값}</td></tr>
  </tbody>
</table>

<!-- 4. 콜아웃 박스 -->
<div class="callout"><strong>참고:</strong> {강조 내용}</div>

<!-- 5. 체크리스트 -->
<ul>
  <li><span class="badge badge-ok">✓</span> {완료 항목}</li>
  <li><span class="badge badge-err">✗</span> {미완 항목}</li>
</ul>

<!-- 6. 단계 타임라인 -->
<ol>
  <li><strong>{단계명}</strong> — {설명}</li>
</ol>

<!-- 7. 인라인 SVG 다이어그램 (svg-diagram 스킬 산출물을 본문에 직접 임베드) -->
<figure>
  <svg viewBox="0 0 400 200" xmlns="http://www.w3.org/2000/svg"><!-- ... --></svg>
  <figcaption>그림 1. {범례 포함 설명}</figcaption>
</figure>
```

- [ ] **Step 2: git add**

```
git add ~/.claude/skills/productivity/html-doc/references/components.html
```

---

### Task 4: SSOT `document-system.md` 개정

**Files:**
- Modify: `~/.claude/skills/productivity/track-status/references/document-system.md`

- [ ] **Step 1: 번호 체계 표 확장자 변경**

"## 문서 번호 체계" 표에서 모든 파일명의 `.md` → `.html`로 변경:
`0_INDEX.html`, `1_REQ-SNAPSHOT.html`, `2_WORK-SPEC.html`, `3_FEATURE-CHECKLIST.html`, `4_PLAN.html`, `5_ARCHITECTURE.html`, `6_SPEC.html`, `7_SELF-REVIEW.html`, `8_QA-SCENARIOS.html`, `9_*.html`.

- [ ] **Step 2: 파일 탐색 규칙 갱신**

"파일 탐색 규칙" 문단(라인 87 부근)을 확장자 비의존 매칭으로 수정:

```
> **파일 탐색 규칙**: 스킬에서 Track 문서를 찾을 때 하드코딩된 번호·확장자가 아닌
> `*_파일명.*` 패턴 매칭을 사용한다. 전환기에 `.md`/`.html`이 공존하므로
> `*_PLAN.*`, `*_WORK-SPEC.*` 등으로 탐색한다.
```

- [ ] **Step 3: 다이어그램 섹션 개정**

"## 다이어그램" 섹션을 인라인 SVG 기준으로 수정:

```
## 다이어그램

- **SVG 직접 생성을 기본으로 사용** (Mermaid보다 우선)
- `svg-diagram` 스킬의 템플릿/팔레트 적용
- 산출 문서가 HTML이므로 SVG는 별도 파일이 아닌 본문에 **인라인 `<svg>`**로 임베드한다
  (자체 완결 단일 파일 원칙). `images/` 외부 참조를 쓰지 않는다.
- DDL 스크립트는 `{DOC_DIR}/sql/` 폴더에 저장, `DATABASE.html`에서 `<a href="sql/xxx.sql">`로 참조
- Mermaid는 빠른 프로토타이핑·확인용으로만 사용
- 문서 간 참조는 번호 접두사 포함 `.html` 파일명으로 링크 (예: `<a href="6_SPEC.html">`)
```

- [ ] **Step 4: 작성 원칙·참조 문서 명칭 갱신**

- "번호 없는 참조 문서" 표의 `DATABASE.md` → `DATABASE.html`
- "### 3. Track 개요 문서 (0_INDEX.md)" → `(0_INDEX.html)`
- "## 문서 저장 경로" 등 본문에서 산출 문서를 가리키는 `.md` 표기를 `.html`로 정정
  (단 `req.md`, `metadata.json`, `index.md` 등 입력·메타 파일명은 그대로 둔다)
- 문서 상단에 한 줄 추가: `> 산출 문서는 HTML로 작성한다. 템플릿·규칙은 html-doc 스킬을 따른다.`

- [ ] **Step 5: git add**

```
git add ~/.claude/skills/productivity/track-status/references/document-system.md
```

---

### Task 5: `self-review` 커맨드 HTML 전환

**Files:**
- Modify: `~/.claude/commands/self-review.md`

- [ ] **Step 1: frontmatter description 갱신**

`description:` 의 `SELF-REVIEW.md` → `SELF-REVIEW.html`.

- [ ] **Step 2: 본문 산출물 표기 변경**

- 라인 9 부근 `결과를 \`SELF-REVIEW.md\` 문서로 생성` → `\`SELF-REVIEW.html\` 문서로 생성`
- "## 4단계: 결과 통합" 제목 `SELF-REVIEW.md` → `SELF-REVIEW.html`
- 4단계 본문 `{DOC_DIR}/*_SELF-REVIEW.md` → `{DOC_DIR}/*_SELF-REVIEW.html`,
  폴백 `SELF-REVIEW.md` → `SELF-REVIEW.html`

- [ ] **Step 3: 4단계에 HTML 생성 지시 추가**

"## 4단계" 섹션 시작부에 문단 추가:

```
산출 문서는 `html-doc` 스킬의 규칙을 따라 자체 완결 HTML로 작성한다.
`html-doc/references/template.html`을 skeleton으로 사용하고, 리뷰어별 지적사항은
`<details>` collapsible과 상태 배지(badge-ok/warn/err)로, 심각도 분포는 비교표로 표현한다.
```

기존 4단계에 Markdown 산출 양식 예시가 있으면 HTML 구조(`<section>`, `<table>`, `<details>`)로 교체한다.

- [ ] **Step 4: git add 명령 갱신**

라인 282 부근 `git add SELF-REVIEW.md` → `git add SELF-REVIEW.html`.

- [ ] **Step 5: git add**

```
git add ~/.claude/commands/self-review.md
```

---

### Task 6: `qa-scenario` 스킬 HTML 전환 — SKILL.md

**Files:**
- Modify: `~/.claude/skills/engineering/qa-scenario/SKILL.md`

- [ ] **Step 1: 산출물 표기 변경**

SKILL.md 전체에서 산출 문서를 가리키는 `8_QA-SCENARIOS.md`/`QA-SCENARIOS.md`/`*_QA-SCENARIOS.md` → `.html` 확장자로 변경.

- [ ] **Step 2: HTML 생성 지시 추가**

산출물 생성 단계에 문단 추가:

```
QA 시나리오 문서는 `html-doc` 스킬 규칙을 따라 자체 완결 HTML로 작성한다.
`references/qa-document-template.html`을 사용한다. BDD 시나리오는 `<details>`로,
영향도 매트릭스는 비교표로, 우선순위는 상태 배지로 표현한다.
```

- [ ] **Step 3: git add**

```
git add ~/.claude/skills/engineering/qa-scenario/SKILL.md
```

---

### Task 7: `qa-scenario` 스킬 — qa-document-template HTML 전환

**Files:**
- Create: `~/.claude/skills/engineering/qa-scenario/references/qa-document-template.html`
- Delete: `~/.claude/skills/engineering/qa-scenario/references/qa-document-template.md`

- [ ] **Step 1: 기존 md 템플릿 내용 확인**

Read `~/.claude/skills/engineering/qa-scenario/references/qa-document-template.md` — 섹션 구성(영향도 매트릭스, BDD 시나리오, 우선순위 등)을 파악한다.

- [ ] **Step 2: HTML 템플릿 작성**

`qa-document-template.html`을 생성한다. Task 2의 `template.html` skeleton(인라인 `<style>` 포함)을 기반으로, 기존 md 템플릿의 섹션을 HTML 구조로 옮긴다:
- 영향도 매트릭스 → `<table>`
- 각 BDD 시나리오(Given/When/Then) → `<details>` 1개씩, `<summary>`에 시나리오명
- 우선순위(High/Mid/Low) → `badge-err`/`badge-warn`/`badge-ok`
- `{플레이스홀더}` 표기는 기존 템플릿과 동일하게 유지

- [ ] **Step 3: 기존 md 템플릿 삭제**

```
Remove-Item ~/.claude/skills/engineering/qa-scenario/references/qa-document-template.md
```

- [ ] **Step 4: 브라우저 렌더 확인**

Run: `Start-Process ~/.claude/skills/engineering/qa-scenario/references/qa-document-template.html`
Expected: 매트릭스 표·collapsible 시나리오·배지가 정상 렌더.

- [ ] **Step 5: git add**

```
git add ~/.claude/skills/engineering/qa-scenario/references/qa-document-template.html
git add ~/.claude/skills/engineering/qa-scenario/references/qa-document-template.md
```
(삭제 파일도 `git add`로 staging 됨)

---

### Task 8: ADR 작성

**Files:**
- Create: `C:\workspace\intellij\TIL\.claude\docs\adr\NNNN-html-doc-migration.md` (NNNN은 기존 adr 폴더의 다음 번호)

- [ ] **Step 1: 기존 ADR 번호 확인**

Run: `Get-ChildItem C:\workspace\intellij\TIL\.claude\docs\adr\`
다음 순번 NNNN을 정한다.

- [ ] **Step 2: ADR 작성**

기존 ADR 파일 1개의 형식을 따라 작성한다. 내용:
- **제목:** 워크플로우 산출 문서를 Markdown에서 HTML로 전환
- **상태:** 채택
- **맥락:** Thariq "HTML effectiveness", Claude Code 팀 "HTML is the new Markdown", Karpathy 지지 — LLM 산출 문서는 HTML의 공간적 표현이 효과적
- **결정:** 워크플로우 산출 문서를 자체 완결 HTML로 생성. `html-doc` 공통 스킬 + SSOT 개정
- **범위:** 산출 문서 전체. 제외 = SKILL.md·CLAUDE.md·README·cs/·Confluence
- **결과/트레이드오프:** git diff 가독성 저하(검토용이라 영향 작음), 토큰 소폭 증가, 기존 md는 마이그레이션 안 함

> ADR은 LLM·GitHub가 읽는 의사결정 기록이므로 **md 그대로 작성**한다(제외 대상 정신과 일치).

- [ ] **Step 3: git add**

```
git add C:\workspace\intellij\TIL\.claude\docs\adr\NNNN-html-doc-migration.md
```

---

### Task 9: Phase 1 통합 검증

- [ ] **Step 1: self-review 산출 확인**

TIL이 아닌 임의 작업 브랜치에서 `/self-review`를 멘탈 시뮬레이션하거나, 4단계 산출 로직을 읽어 `.html` 경로·html-doc 템플릿 참조가 일관됨을 확인한다.
Expected: 산출 경로가 `*_SELF-REVIEW.html`, html-doc 규칙 참조 존재.

- [ ] **Step 2: qa-scenario 산출 확인**

`qa-scenario` SKILL.md와 `qa-document-template.html`이 일관됨을 확인한다.
Expected: SKILL.md가 `.html` 템플릿을 참조, 표기 불일치 없음.

- [ ] **Step 3: SSOT 일관성 확인**

`document-system.md`에 `.md` 산출 문서 표기가 남아있지 않은지 Grep:
Run: `Select-String -Path ~/.claude/skills/productivity/track-status/references/document-system.md -Pattern '_INDEX\.md|WORK-SPEC\.md|SPEC\.md|SELF-REVIEW\.md|QA-SCENARIOS\.md|ARCHITECTURE\.md|PLAN\.md'`
Expected: 매치 없음.

---

## Phase 2 — 확산

> **공통 전환 절차** (Phase 2 모든 Task 공통):
> 1. 대상 스킬의 SKILL.md(또는 커맨드 .md)에서 산출 문서 표기 `*.md` → `*.html`로 변경.
> 2. 산출물 생성 단계에 "`html-doc` 스킬 규칙을 따라 자체 완결 HTML로 작성한다" 지시 추가.
> 3. `references/`에 md 템플릿이 있으면 `template.html` 기반 HTML로 전환하고 기존 md는 삭제.
> 4. 산출물 내 다이어그램은 인라인 `<svg>` 임베드로 지시.
> 5. `git add`까지.

### Task 10: `work-plan` 묶음 전환 (work-plan + to-prd + to-issues)

**Files:**
- Modify: `~/.claude/skills/engineering/work-plan/SKILL.md`
- Create/Delete: `~/.claude/skills/engineering/work-plan/references/workspec-template.html` (← `.md` 삭제)
- Modify: `~/.claude/skills/engineering/to-prd/SKILL.md`
- Modify: `~/.claude/skills/engineering/to-issues/SKILL.md`

- [ ] **Step 1:** `to-prd` SKILL.md — WORK-SPEC 산출 양식을 HTML 구조로 전환. `2_WORK-SPEC.md` → `.html`.
- [ ] **Step 2:** `to-issues` SKILL.md — FEATURE-CHECKLIST 산출을 HTML로. `3_FEATURE-CHECKLIST.md` → `.html`. 체크리스트는 배지 컴포넌트 사용.
- [ ] **Step 3:** `work-plan` SKILL.md — `1_REQ-SNAPSHOT.md`/`2_WORK-SPEC.md`/`3_FEATURE-CHECKLIST.md` 표기를 `.html`로. REQ-SNAPSHOT은 원본 req 텍스트를 `<pre>` 블록으로 보존하도록 지시. 완료 기준 체크리스트(라인 108~111)의 파일명도 갱신.
- [ ] **Step 4:** `workspec-template.md`를 `template.html` 기반 `workspec-template.html`로 전환. 기존 md 템플릿의 섹션(요구사항 요약·기술 스택·...)을 `<section>`/`<table>`로 옮긴다. 기존 md 삭제.
- [ ] **Step 5:** 브라우저 렌더 확인 (`Start-Process ...workspec-template.html`).
- [ ] **Step 6:** `git add` (4개 파일 + 삭제분).

### Task 11: `work-plan-start` 전환

**Files:**
- Modify: `~/.claude/skills/engineering/work-plan-start/SKILL.md`
- Create/Delete: `~/.claude/skills/engineering/work-plan-start/references/completion-report.html` (← `.md` 삭제)

- [ ] **Step 1:** SKILL.md — `4_PLAN.md`/`5_ARCHITECTURE.md`/`6_SPEC.md` 표기 → `.html`. ARCHITECTURE/SPEC의 다이어그램을 인라인 `<svg>`로 지시.
- [ ] **Step 2:** `completion-report.md` → `completion-report.html` 전환 (template.html 기반), 기존 md 삭제.
- [ ] **Step 3:** 브라우저 렌더 확인.
- [ ] **Step 4:** `git add`.

### Task 12: `track-status` + `track-status-summary` 전환

**Files:**
- Modify: `~/.claude/skills/productivity/track-status/SKILL.md`
- Modify: `~/.claude/skills/productivity/track-status/references/workflow-stages.md`
- Modify: `~/.claude/skills/productivity/track-status-summary/SKILL.md`

- [ ] **Step 1:** `track-status` SKILL.md — `0_INDEX.md` 생성 로직을 `0_INDEX.html`로. 문서 목록 링크를 `.html`로.
- [ ] **Step 2:** `workflow-stages.md` 내 산출 문서 표기 `.md` → `.html`.
- [ ] **Step 3:** `track-status-summary` — 이미 HTML 대시보드를 생성하므로, 링크 대상 문서 경로만 `.html`로 갱신. 이미 일관되면 변경 없음.
- [ ] **Step 4:** `git add`.

### Task 13: `feature-check` + `design-spec-generate` 전환

**Files:**
- Modify: `~/.claude/skills/engineering/feature-check/SKILL.md`
- Modify: `~/.claude/skills/engineering/design-spec-generate/SKILL.md`

- [ ] **Step 1:** `feature-check` — `3_FEATURE-CHECKLIST` 입력/산출 표기를 `.html`로. 체크 결과 보고서가 산출되면 HTML로.
- [ ] **Step 2:** `design-spec-generate` — 디자인 스펙 산출 문서를 HTML로.
- [ ] **Step 3:** `git add`.

### Task 14: `triage` 전환

**Files:**
- Modify: `~/.claude/skills/engineering/triage/SKILL.md`
- Modify: `~/.claude/skills/engineering/triage/AGENT-BRIEF.md`

- [ ] **Step 1:** `triage` SKILL.md — AGENT-BRIEF 산출 문서를 HTML로 지시.
- [ ] **Step 2:** `AGENT-BRIEF.md`가 산출물 템플릿이면 `AGENT-BRIEF.html`로 전환·삭제. LLM 컨텍스트 입력 정의면 md 유지 — Step 1에서 판별.
- [ ] **Step 3:** `git add`.

### Task 15: `web-pipeline` + `grill-with-docs` 검토

**Files:**
- Modify: `~/.claude/skills/engineering/web-pipeline/SKILL.md`
- Review: `~/.claude/skills/engineering/grill-with-docs/SKILL.md`, `CONTEXT-FORMAT.md`, `ADR-FORMAT.md`

- [ ] **Step 1:** `web-pipeline` — Track 산출 문서(0~8) 표기를 `.html`로. 단 `9_QA-REPORT`도 `.html`.
- [ ] **Step 2:** `grill-with-docs` — `CONTEXT.md`/ADR은 **LLM·GitHub가 읽는 문서이므로 md 유지**. SKILL.md를 읽어 산출물이 워크플로우 산출 문서에 해당하는지 판별 후, 해당 시에만 전환. 해당 없으면 변경 없음(plan에 "변경 없음" 기록).
- [ ] **Step 3:** `git add` (변경분만).

### Task 16: 리뷰 커맨드 전환 (`review-pr`, `team-review`)

**Files:**
- Modify: `~/.claude/commands/review-pr.md`
- Modify: `~/.claude/commands/team-review.md`

- [ ] **Step 1:** `review-pr` — 리뷰 결과 산출 문서를 HTML로. self-review(Task 5)와 동일 패턴(리뷰어별 `<details>`, 심각도 배지, 비교표).
- [ ] **Step 2:** `team-review` — 동일 패턴 적용.
- [ ] **Step 3:** `git add`.

### Task 17: `product-review` + `prototype` 검토

**Files:**
- Review/Modify: `~/.claude/skills/engineering/product-review/SKILL.md`
- Review/Modify: `~/.claude/skills/engineering/prototype/SKILL.md`

- [ ] **Step 1:** 각 SKILL.md를 읽어 산출물이 "사람용 워크플로우 산출 문서"인지 판별. `product-review`는 리뷰 보고서면 HTML 전환. `prototype`은 throwaway 코드 산출이면 대상 아님 — 문서 산출 부분만 전환.
- [ ] **Step 2:** 해당 시 전환, 미해당 시 plan에 "변경 없음" 기록.
- [ ] **Step 3:** `git add` (변경분만).

---

## Phase 3 — 동기화 및 마무리

### Task 18: CLAUDE.md 인라인 섹션 동기화

**Files:**
- Modify: `~/.claude/CLAUDE.md`
- Modify: `C:\workspace\intellij\TIL\CLAUDE.md`
- Modify: `C:\workspace\intellij\TIL\.claude\CLAUDE.md`

- [ ] **Step 1:** `~/.claude/CLAUDE.md` — "워크플로우 문서 시스템"은 SSOT 한 줄 링크다. 링크 유지, 주변 문구에 산출 문서 `.md` 표기가 있으면 정정.
- [ ] **Step 2:** `C:\workspace\intellij\TIL\CLAUDE.md` — "워크플로우 문서 시스템" 섹션이 SSOT 링크 형태. 링크 유지, 문구 정정.
- [ ] **Step 3:** `C:\workspace\intellij\TIL\.claude\CLAUDE.md` — "워크플로우 문서 시스템" 섹션이 **인라인 전문**으로 박혀 있음. Task 4에서 개정한 `document-system.md` 내용과 일치하도록 번호 체계 표·다이어그램 규칙·작성 원칙을 `.html` 기준으로 갱신.
- [ ] **Step 4:** `git add` 3개 파일.

### Task 19: 글로벌 → TIL 로컬 동기화

- [ ] **Step 1:** `sync-global` 스킬 status로 글로벌과 TIL 로컬의 차이 확인.

Run: `/sync-global status`
Expected: Task 1~17에서 수정한 글로벌 스킬·커맨드가 "로컬과 다름"으로 표시.

- [ ] **Step 2:** `/sync-global pull`로 글로벌 변경을 TIL 로컬에 반영.

Run: `/sync-global pull`
Expected: TIL `.claude/skills`·`.claude/commands`가 글로벌과 동일해짐.

- [ ] **Step 3:** 재확인.

Run: `/sync-global status`
Expected: diff 없음.

- [ ] **Step 4:** `git add`.

```
git add C:\workspace\intellij\TIL\.claude\skills C:\workspace\intellij\TIL\.claude\commands
```

### Task 20: 최종 검증

- [ ] **Step 1:** 글로벌 스킬 전체에서 산출 문서 `.md` 잔여 표기 스캔.

Run: `Select-String -Path ~/.claude/skills -Recurse -Include SKILL.md -Pattern '\d_[A-Z-]+\.md'`
Expected: 매치 없음 (입력 파일 `req.md`, `metadata.json` 등 제외).

- [ ] **Step 2:** 커맨드 전체 동일 스캔.

Run: `Select-String -Path ~/.claude/commands -Recurse -Pattern 'SELF-REVIEW\.md|WORK-SPEC\.md|QA-SCENARIOS\.md'`
Expected: 매치 없음.

- [ ] **Step 3:** 결과 요약 — 전환된 스킬·커맨드 목록, 변경 없음 처리된 항목, 남은 트레이드오프(md/html 공존)를 사용자에게 보고.

---

## 검증 기준 (전체 완료 조건)

- `html-doc` 스킬이 존재하고 `template.html`이 브라우저에서 정상 렌더(라이트/다크).
- `document-system.md` SSOT가 HTML 기준으로 개정됨.
- 파일럿 2종(self-review·qa-scenario)이 `.html` 산출 + html-doc 규칙 참조.
- Phase 2 대상 스킬이 모두 전환 또는 "변경 없음" 명시.
- 글로벌·CLAUDE.md 3종이 일관, `sync-global status` diff 없음.
- 산출 문서 `.md` 잔여 표기 없음.

## 트레이드오프 (재확인)

- 기존 생성된 `.md` 산출물은 마이그레이션하지 않음 → `.md`/`.html` 한동안 공존, 탐색은 `*_NAME.*` 패턴으로 처리.
- HTML은 `git diff` 가독성이 낮음 → tracks/ 산출물은 검토용이라 영향 작음.
