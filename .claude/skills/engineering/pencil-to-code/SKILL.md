---
name: pencil-to-code
description: |
  Pencil MCP 디자인(.pen 파일)을 프로젝트 코드(HTML/CSS/JS)로 변환합니다. 디자인 토큰→CSS 변수 매핑, 시맨틱 HTML 구조, BEM 네이밍, mobile-first 반응형을 자동으로 적용합니다.
  "코드 생성", "디자인 적용", "코드로 변환", "Pencil to code", "HTML 변환" 요청에 트리거됩니다.
  .pen 파일의 디자인을 실제 동작하는 웹 페이지로 만들고 싶다면 이 스킬을 사용하세요.
---

# Pencil 디자인 → 코드 변환

## 개요

Pencil .pen 파일의 디자인을 프로젝트 코드로 변환한다.
프로젝트 스택(vanilla HTML/CSS/JS), BEM 네이밍, CSS 변수, mobile-first 반응형을 준수한다.

## 왜 이 워크플로우인가?

Pencil 디자인의 노드 구조를 그대로 HTML로 옮기면 시맨틱하지 않은 div 중첩이 됩니다.
이 워크플로우는 Pencil 컴포넌트를 시맨틱 HTML 태그로 변환하고, 하드코딩된 색상값 대신 CSS 변수를 사용하여 디자인 시스템과 일관성을 유지합니다.
Desktop과 Mobile 프레임을 모두 참조하여 mobile-first 반응형으로 구현합니다.

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

### Step 5: 매핑 레퍼런스 확인

## 매핑 레퍼런스

Pencil→CSS/HTML 매핑 테이블과 프로젝트 템플릿은 Read 도구로 `references/mappings.md`를 읽어서 참조하세요.

### Step 6: HTML/CSS 코드 작성

- `references/mappings.md`의 CSS 토큰 매핑과 HTML 시맨틱 매핑을 기준으로 구현
- 하드코딩된 색상값 대신 CSS 변수 사용
- 프로젝트 파일 구조와 HTML 페이지 템플릿 참조

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
