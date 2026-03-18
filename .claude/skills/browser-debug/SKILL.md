---
name: browser-debug
description: |
  Chrome in Claude 브라우저 자동화로 웹 프로젝트의 모든 페이지를 순회하며 기능을 점검합니다. 콘솔 에러, 인터랙션 동작, DOM 상태를 자동 검사하고 발견된 이슈를 TODO에 반영합니다.
  "브라우저 디버깅", "Chrome 디버깅", "페이지 점검", "UI 테스트", "기능 검증" 요청에 트리거됩니다.
  프로젝트 코드를 브라우저에서 직접 확인하고 싶거나, UI 버그를 체계적으로 찾고 싶을 때 사용하세요.
---

# Browser Debug - Chrome 디버깅 & TODO 업데이트

## 왜 브라우저 자동 점검인가?

수동으로 모든 페이지를 클릭하며 검증하면 빠뜨리는 항목이 생깁니다.
브라우저 자동화로 모든 페이지의 콘솔 에러, 인터랙티브 요소, DOM 상태를 체계적으로 검사하면 놓치는 버그를 최소화할 수 있습니다.

## 개요

Chrome in Claude 브라우저 자동화 도구를 사용하여 프로젝트의 모든 페이지를 순회하며 기능 동작을 점검한다.
발견된 버그와 미구현 기능을 `.claude/docs/TODO.md`에 자동으로 반영한다.

## 접속 URL

- **접속 URL**: 사용자가 지정한 URL을 사용합니다. 지정하지 않으면 프로젝트의 로컬 서버 URL을 자동 탐지합니다.

## 사용법

### Step 1: 브라우저 탭 준비

```
1. ToolSearch로 chrome 도구 로드 (tabs_context_mcp, navigate, computer, javascript_tool, read_page, read_console_messages)
2. tabs_context_mcp(createIfEmpty: true)로 탭 생성
3. navigate로 기본 URL 접속
```

### Step 2: 페이지별 순회 점검

프로젝트의 HTML 파일이나 라우트를 자동 탐지하여 순회합니다:
1. 프로젝트 디렉토리에서 `*.html` 파일 또는 라우트 설정 파일 탐색
2. 각 페이지에 대해 Step 3의 점검 수행
3. 페이지별 체크 항목은 페이지 유형(폼, 리스트, 대시보드 등)에 따라 자동 결정

### Step 3: 각 페이지 점검 방법

```
1. navigate로 페이지 이동
2. screenshot으로 화면 캡처
3. read_console_messages(onlyErrors: true)로 JS 에러 확인
4. read_page(filter: "interactive")로 인터랙티브 요소 목록 확인
5. 주요 버튼/링크 클릭하여 동작 검증 (computer: left_click)
6. javascript_tool로 DOM 상태 확인 (삭제 버튼 존재 여부, 폼 required 속성 등)
7. 발견된 이슈 기록
```

### Step 4: JS 소스 코드 분석

```
1. Read 도구로 js/*.js 파일 읽기
2. 스텁 구현 확인 (toast만 표시하고 실제 동작 없는 함수)
3. 이벤트 바인딩 누락 확인
4. Storage CRUD 메서드 중 UI에서 호출하지 않는 것 확인
```

### Step 5: TODO.md 업데이트

```
1. Read로 .claude/docs/TODO.md 현재 내용 확인
2. 기존 항목과 중복되지 않는 새 이슈만 추출
3. 적절한 Phase에 항목 추가:
   - Phase 1: 핵심 기능 누락 (삭제, 수정, 선택기 등)
   - Phase 2: UX/UI 개선 (에러 메시지, 포맷팅, 링크 등)
   - Phase 3+: 신규 기능
4. Edit 도구로 TODO.md 업데이트
5. git add
```

## 입출력

| 입력 | 출력 |
|------|------|
| 접속 URL (선택, 미지정 시 자동 탐지) | 업데이트된 `.claude/docs/TODO.md` |
| | 디버깅 결과 요약 (채팅 메시지) |

## 점검 체크리스트

### 공통 점검

- [ ] 콘솔 에러 없는지 확인
- [ ] 모든 네비게이션 링크 동작 확인
- [ ] 사이드바 메뉴 모든 항목 클릭 확인

### 기능 점검

- [ ] CRUD 동작 확인 (생성/조회/수정/삭제)
- [ ] 폼 유효성 검사 피드백 확인
- [ ] localStorage 저장/로드 확인
- [ ] 토글/스위치 동작 확인
- [ ] 검색/필터 동작 확인

### 결과 정리

- [ ] 기존 TODO 항목과 중복 제거
- [ ] 새 이슈를 적절한 Phase에 배치
- [ ] git add 완료

## 관련 스킬

- `pencil-to-code`: 디자인 → 코드 변환 후 브라우저 검증에 활용
- `pencil-update`: 디버깅에서 발견된 UI 이슈를 디자인에 먼저 반영할 때
