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

### Step 2: 화면 구조 파악

```
batch_get(nodeIds: [Desktop_ID, Mobile_ID], readDepth: 3)
```

- Desktop과 Mobile 프레임의 전체 구조 파악

### Step 3: 변수(토큰) 확인

```
get_variables(filePath: "파일.pen")
```

### Step 4: 스크린샷 참조

```
get_screenshot(nodeId: Desktop_ID)
get_screenshot(nodeId: Mobile_ID)
```

### Step 5: CSS 토큰 매핑

| Pencil 속성 | CSS 변수 |
|------------|---------|
| `fill: "#FAF8F5"` | `var(--color-bg)` |
| `fill: "#7C9082"` | `var(--color-sage)` |
| `fill: "#D4845E"` | `var(--color-terracotta)` |
| `fill: "#2D2D2D"` | `var(--color-text)` |
| `fill: "#8A8A8A"` | `var(--color-text-muted)` |
| `fill: "#E8E4DF"` | `var(--color-border)` |
| `fill: "#F0EDE8"` | `var(--color-border-light)` |
| `fill: "#FFFFFF"` | `var(--color-surface)` |
| `fill: "#18181b"` | `var(--color-sidebar)` |
| `fontFamily: "Fraunces"` | `var(--font-display)` |
| `fontFamily: "Inter"` | `var(--font-body)` |
| `cornerRadius: 999` | `var(--radius-pill)` |
| `cornerRadius: 16` | `var(--radius-lg)` |
| `cornerRadius: 12` | `var(--radius-md)` |
| `cornerRadius: 8` | `var(--radius-sm)` |

### Step 6: HTML 시맨틱 매핑

| Pencil 노드 | HTML 태그 |
|-------------|----------|
| `frame` (최상위) | `<section>`, `<main>`, `<aside>` |
| `frame` (카드) | `<div class="card">`, `<article>` |
| `text` (제목) | `<h1>`~`<h4>` |
| `text` (본문) | `<p>`, `<span>` |
| `icon_font` | `<i data-lucide="이름">` |
| `ref` → `ZETEA` | `<button class="btn btn--primary">` |
| `ref` → `4x7RU` | `<button class="btn btn--outline">` |
| `ref` → `gKpi4` | `<div class="input-group">` + `<input>` |
| `ref` → `XhJWF` | `<div class="select-group">` + `<select>` |
| `ref` → `QFzE8` | `<div class="textarea-group">` + `<textarea>` |
| `ref` → `ERkuB` | `<div class="card">` |
| `ref` → `ksvfk` | `<article class="card-image">` |
| `ref` → `Kbr4h` | `<div class="tabs">` |
| `ref` → `W4YFH` | `<div class="progress">` |
| `ref` → `d5ZTS` | `<aside class="sidebar">` |

### Step 7: JS 인터랙션 작성

- 탭 전환, 체크박스 토글, 폼 검증 등
- `'use strict'` 선언, DOM 선택자 상수화
- `js/` 디렉토리에 페이지별 파일 생성

## 입출력

| 입력 | 출력 |
|------|------|
| Pencil 화면 노드 ID | HTML 파일 (페이지) |
| 디자인 토큰 | CSS 추가 (필요시) |
| 인터랙션 요구사항 | JS 파일 |

## 프로젝트 파일 구조

```
travel/
├── .claude/docs/
│   └── design.pen      # Pencil 디자인 파일 (프로젝트 전용)
├── css/
│   ├── variables.css   # 디자인 토큰 → 새 토큰 추가 가능
│   ├── base.css        # Reset + 타이포
│   ├── components.css  # 공유 컴포넌트 → 새 컴포넌트 추가 가능
│   ├── landing.css     # Landing 전용
│   └── app.css         # App 레이아웃
├── js/
│   ├── common.js       # 공통 (Lucide, reveal)
│   └── {page}.js       # 페이지별 JS
└── {page}.html         # 페이지별 HTML
```

## HTML 페이지 템플릿

```html
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
    <meta name="theme-color" content="#7C9082">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <title>페이지명 - 땅뽀 여행</title>
    <link rel="stylesheet" href="css/variables.css">
    <link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/components.css">
    <link rel="stylesheet" href="css/app.css">
    <script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js"></script>
</head>
<body>
    <div class="app">
        <!-- Sidebar (Desktop) -->
        <aside class="sidebar">...</aside>
        <!-- Mobile Header -->
        <header class="mobile-header">...</header>
        <!-- Main Content -->
        <main class="main">...</main>
        <!-- Bottom Navigation (Mobile) -->
        <nav class="bottom-nav">...</nav>
    </div>
    <script src="js/common.js"></script>
    <script src="js/{page}.js"></script>
</body>
</html>
```

## 핵심 규칙

- **vanilla HTML/CSS/JS** - 프레임워크 사용 금지
- **Mobile-first 반응형** - 기본 모바일, `@media (min-width: 768px)` 데스크톱
- **BEM 네이밍** - `.block__element--modifier`
- **CSS 변수 사용** - 하드코딩 금지, `css/variables.css` 참조
- **기존 컴포넌트 재활용** - `components.css`의 클래스 우선 사용
- **Lucide 아이콘** - `<i data-lucide="icon-name">` + CDN
- **시맨틱 HTML** - `<header>`, `<main>`, `<nav>`, `<section>`, `<article>`, `<footer>`
- **접근성** - `aria-label`, `role`, `alt`

## 체크리스트

- [ ] Pencil batch_get으로 노드 구조 파악
- [ ] 디자인 토큰 → CSS 변수 매핑
- [ ] 시맨틱 HTML 구조 작성
- [ ] BEM 클래스 네이밍
- [ ] 반응형 (Desktop + Mobile)
- [ ] Lucide 아이콘 적용
- [ ] JS 인터랙션 구현
- [ ] 브라우저 시각 검증

## 관련 스킬

- `pencil-screen`: 새 화면 생성
- `pencil-update`: 기존 화면 수정
