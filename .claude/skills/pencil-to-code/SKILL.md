---
name: pencil-to-code
description: Pencil MCP 디자인을 프로젝트 코드(HTML/CSS/JS)로 변환할 때 사용
---

# Pencil 디자인 → 코드 변환

## 개요

Pencil .pen 파일의 디자인을 프로젝트 코드로 변환한다.
프로젝트 스택(vanilla HTML/CSS/JS), BEM 네이밍, CSS 변수, mobile-first 반응형을 준수한다.

## 사용법

### Step 1: 코드 가이드라인 확인

```
get_guidelines(topic: "code")
```

- Pencil의 코드 변환 가이드라인 확인

### Step 2: 화면 구조 파악

```
batch_get(nodeIds: [Desktop_ID, Mobile_ID], readDepth: 3)
```

- Desktop과 Mobile 프레임의 전체 구조 파악
- 컴포넌트 ID, 속성, 레이아웃 확인

### Step 3: 변수(토큰) 확인

```
get_variables(filePath: "파일.pen")
```

- 색상, 폰트, 간격 등 디자인 토큰 추출
- CSS 변수로 매핑

### Step 4: 스크린샷 참조

```
get_screenshot(nodeId: Desktop_ID)
get_screenshot(nodeId: Mobile_ID)
```

- 시각적 참조로 정확한 구현

### Step 5: CSS 변수 매핑

Pencil 속성 → CSS 변수 매핑:

| Pencil | CSS 변수 |
|--------|---------|
| `fill: "#FAF8F5"` | `var(--color-bg)` |
| `fill: "#7C9082"` | `var(--color-sage)` |
| `fill: "#D4845E"` | `var(--color-terracotta)` |
| `fill: "#2D2D2D"` | `var(--color-text)` |
| `fill: "#8A8A8A"` | `var(--color-text-muted)` |
| `fill: "#E8E4DF"` | `var(--color-border)` |
| `fontFamily: "Fraunces"` | `var(--font-display)` |
| `fontFamily: "Inter"` | `var(--font-body)` |
| `cornerRadius: 999` | `var(--radius-pill)` |
| `cornerRadius: 16` | `var(--radius-lg)` |

### Step 6: HTML 시맨틱 구조

Pencil 노드 타입 → HTML 태그:

| Pencil | HTML |
|--------|------|
| `frame` (최상위) | `<section>`, `<main>`, `<aside>` |
| `frame` (카드) | `<div class="card">`, `<article>` |
| `text` (제목) | `<h1>`~`<h4>` |
| `text` (본문) | `<p>`, `<span>` |
| `icon_font` | `<i data-lucide="이름">` |
| `ref` (버튼 컴포넌트) | `<button class="btn">`, `<a class="btn">` |
| `ref` (입력 컴포넌트) | `<div class="input-group">` + `<input>` |

### Step 7: JS 인터랙션

- 탭 전환, 체크박스 토글, 폼 유효성 등 동작 코드 작성
- DOM 선택자는 상수로 관리
- `'use strict'` 선언

## 입출력

| 입력 | 출력 |
|------|------|
| Pencil 화면 노드 ID | HTML 파일 |
| 디자인 토큰 | CSS 파일 (기존 CSS에 추가) |
| 인터랙션 요구사항 | JS 파일 |

## 프로젝트 파일 구조

```
travel/
├── css/
│   ├── variables.css   # 디자인 토큰
│   ├── base.css        # Reset + 타이포
│   ├── components.css  # 공유 컴포넌트
│   ├── landing.css     # Landing 전용
│   └── app.css         # App 레이아웃
├── js/
│   ├── common.js       # 공통 (Lucide, reveal)
│   ├── landing.js      # Landing 전용
│   ├── dashboard.js
│   ├── plan.js
│   └── checklist.js
└── *.html
```

## 핵심 규칙

- **프로젝트 스택 준수**: vanilla HTML/CSS/JS (프레임워크 없음)
- **Mobile-first**: 기본 스타일 → `@media (min-width: 768px)` 추가
- **BEM 네이밍**: `.block__element--modifier`
- **CSS 변수 사용**: 하드코딩 색상/크기 금지
- **기존 컴포넌트 재활용**: components.css의 클래스 우선 사용
- **Lucide 아이콘**: `<i data-lucide="icon-name">` + CDN 스크립트
- **시맨틱 HTML**: `<header>`, `<main>`, `<nav>`, `<section>`, `<article>`, `<footer>`
- **접근성**: `aria-label`, `role`, `alt` 속성

## 체크리스트

- [ ] Pencil 노드 구조 파악 (batch_get)
- [ ] 디자인 토큰 CSS 변수 매핑
- [ ] 시맨틱 HTML 구조 작성
- [ ] BEM 클래스 네이밍
- [ ] 반응형 (Desktop + Mobile)
- [ ] Lucide 아이콘 적용
- [ ] JS 인터랙션 구현
- [ ] 브라우저 시각 검증

## 관련 스킬

- `pencil-screen`: 새 화면 생성
- `pencil-update`: 기존 화면 수정
