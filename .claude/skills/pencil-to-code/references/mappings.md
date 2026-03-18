# Pencil → CSS/HTML 매핑 레퍼런스

## CSS 토큰 매핑

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

## HTML 시맨틱 매핑

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
