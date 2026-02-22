---
name: browser-debug
description: Chrome in Claude로 프로젝트 사이트를 디버깅하고 미구현 기능을 TODO에 추가할 때 사용
---

# Browser Debug - Chrome 디버깅 & TODO 업데이트

## 개요

Chrome in Claude 브라우저 자동화 도구를 사용하여 프로젝트의 모든 페이지를 순회하며 기능 동작을 점검한다.
발견된 버그와 미구현 기능을 `.claude/docs/TODO.md`에 자동으로 반영한다.

## 접속 URL

- **기본 URL**: `http://localhost:63342/travel/index.html`
- 사용자가 다른 URL을 지정하면 해당 URL 사용

## 사용법

### Step 1: 브라우저 탭 준비

```
1. ToolSearch로 chrome 도구 로드 (tabs_context_mcp, navigate, computer, javascript_tool, read_page, read_console_messages)
2. tabs_context_mcp(createIfEmpty: true)로 탭 생성
3. navigate로 기본 URL 접속
```

### Step 2: 페이지별 순회 점검

각 페이지에서 아래 체크를 수행한다:

| 페이지 | URL | 주요 체크 항목 |
|--------|-----|---------------|
| 랜딩 | `/index.html` | 네비게이션 링크, 기능 카드, 하트 클릭, 스크롤 |
| 대시보드 | `/dashboard.html` | 카드 렌더링, 검색 필터, 새 여행 버튼, 삭제 기능, 준비 현황 |
| 여행 계획 | `/plan.html` | 폼 유효성 검사, 저장/수정, 취소 버튼, 에러 피드백 |
| 체크리스트 | `/checklist.html` | 탭 전환, 체크박스 토글, 항목 추가/삭제/수정, 진행률, 여행 선택 |
| 설정 | `/settings.html` | 다크모드 토글, 기본 목적지, 알림, 데이터 내보내기/가져오기, 초기화 |

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
| 접속 URL (선택, 기본: localhost:63342) | 업데이트된 `.claude/docs/TODO.md` |
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
