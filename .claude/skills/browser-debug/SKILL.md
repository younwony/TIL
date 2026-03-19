---
name: browser-debug
description: |
  Chrome in Claude 브라우저 자동화로 웹 프로젝트를 점검합니다.
  기본 동작: 현재 브랜치 변경사항 분석 → QA 시나리오 생성 → 서버 자동 기동 → Chrome 자동화 점검.
  서버가 기동되지 않은 상태에서도 프로젝트 타입을 자동 탐지하여 서버를 기동한 뒤 점검을 수행합니다.
  콘솔 에러, 인터랙션 동작, DOM 상태, 네트워크 에러를 자동 검사하고 발견된 이슈를 정리합니다.

  다음 키워드/문맥에서 트리거됩니다:
  - "브라우저 디버깅", "Chrome 디버깅", "크롬 디버깅"
  - "브라우저 QA", "웹 QA", "QA 점검", "QA 테스트"
  - "웹 디버그", "웹 디버깅", "프론트 디버깅"
  - "페이지 점검", "UI 테스트", "기능 검증", "화면 확인"
  - "Claude in Chrome으로 확인", "브라우저에서 확인"
  - "프로젝트 QA", "전체 점검", "사이트 점검"
  - `/browser-debug` 슬래시 커맨드로도 호출 가능

  프로젝트 코드를 브라우저에서 직접 확인하고 싶거나, UI 버그를 체계적으로 찾고 싶을 때 사용하세요.
  서버가 꺼져 있어도 자동으로 기동합니다.
---

# Browser Debug & QA - Chrome 자동화 웹 점검

## 개요

Chrome in Claude 브라우저 자동화 도구를 사용하여 프로젝트의 페이지를 점검한다.
서버가 기동되지 않은 상태라면 프로젝트 타입을 자동 탐지하여 서버를 먼저 기동한다.
발견된 버그와 미구현 기능을 정리하여 보고한다.

## 기본 동작 (Default): 변경사항 기반 시나리오 QA

**URL이나 특정 페이지를 지정하지 않은 경우**, 현재 브랜치의 변경사항을 분석하여 유저 시나리오를 먼저 생성하고, 해당 시나리오대로 QA한다.

### Step D-1: 변경사항 분석

#### Compare Branch 결정

```
1. git rev-parse --verify main 2>/dev/null → 존재하면 main 사용
2. git rev-parse --verify master 2>/dev/null → 존재하면 master 사용
3. 모두 없으면 AskUserQuestion으로 비교 기준 브랜치를 입력받는다
4. 결정된 브랜치를 {COMPARE_BRANCH}로 사용
```

#### 변경사항 수집 (병렬 실행)

```
1. git branch --show-current → 현재 브랜치명
2. git log {COMPARE_BRANCH}..HEAD --oneline → 커밋 목록
3. git diff {COMPARE_BRANCH}...HEAD --stat → 변경된 파일 통계
4. git diff {COMPARE_BRANCH}...HEAD --name-only → 변경된 파일 목록
   (커밋 전이면 git diff --name-only + git diff --name-only --cached 추가)
```

#### 변경 파일 분류

| 카테고리 | 파일 패턴 | 디버깅 대상 |
|---------|----------|-----------|
| **HTML/JSP/Thymeleaf** | `*.html`, `*.jsp`, `*.th` | UI 렌더링, 레이아웃, 데이터 바인딩 |
| **JavaScript/TypeScript** | `*.js`, `*.ts`, `*.tsx` | 이벤트 핸들러, API 호출, DOM 조작 |
| **CSS** | `*.css`, `*.scss` | 스타일링, 레이아웃, 반응형 |
| **Controller/Router** | `*Controller.java`, `router.*` | URL 매핑 → 접근 가능한 URL 추출 |
| **Service** | `*Service.java` | 비즈니스 로직 (Controller 역추적) |
| **Repository/Mapper** | `*Repository.java`, `*Mapper.java` | 데이터 조회 (Controller 역추적) |
| **SQL/Query** | `*.xml` (MyBatis), `*.sql` | 쿼리 동작 |
| **설정/리소스** | `application.*`, `.env`, `*.json` | 영향받는 페이지 추정 |

#### URL 자동 추출

변경된 Controller 파일에서 `@RequestMapping`, `@GetMapping` 등의 어노테이션을 분석하여 접근 가능한 URL 목록을 추출한다.

### Step D-2: 유저 시나리오 파일 생성

변경사항 분석 결과를 기반으로 `.claude/docs/QA-SCENARIO.md` 파일을 생성한다.

```markdown
# QA 시나리오 - {날짜}

> 브랜치: `{브랜치명}` | 비교 기준: `{COMPARE_BRANCH}`

## 변경사항 요약
- {변경 파일 목록과 변경 내용 요약}

## 시나리오 목록

### 시나리오 1: {기능명}
- **대상 URL**: {URL}
- **사전 조건**: {필요한 상태나 데이터}
- **테스트 절차**:
  1. {URL}에 접속
  2. {요소}를 클릭
  3. {입력값}을 입력
  4. {결과}를 확인
- **기대 결과**: {정상 동작 설명}
- **확인 항목**: 콘솔 에러, 네트워크 에러, DOM 상태
- **결과**: (QA 후 기록)

### 시나리오 2: ...
```

시나리오 작성 규칙:
- 변경된 기능 단위로 시나리오를 분리
- 각 시나리오에 구체적인 **테스트 절차**와 **기대 결과** 포함
- 회귀 테스트가 필요한 기존 기능도 시나리오에 포함
- 시나리오 파일을 사용자에게 공유하고 승인 받은 후 Phase 0으로 진행

### Step D-3: 시나리오 기반 QA 진행

```
1. 시나리오 파일의 각 시나리오를 순서대로 실행
2. 각 시나리오의 테스트 절차를 Chrome 자동화로 수행
3. 기대 결과와 실제 결과를 비교
4. 시나리오별 PASS/FAIL 기록
5. 완료 후 QA-SCENARIO.md에 결과 업데이트
```

**URL을 지정한 경우**: 해당 URL 기준으로 전체 페이지 또는 지정 페이지를 QA한다 (시나리오 생성 생략 가능).

## Phase 0: 서버 준비

서버가 필요한 프로젝트인 경우, 점검 전에 서버 상태를 확인하고 필요 시 자동 기동한다.

### Step 0-1: URL 확인

```
1. 사용자가 URL을 지정했는가?
   ├─ YES → Step 0-2로 (health check)
   └─ NO  → Step 0-3으로 (프로젝트 타입 탐지)
```

### Step 0-2: Health Check

```
1. curl -s -o /dev/null -w "%{http_code}" {URL} 로 상태 확인
   ├─ 200~399 응답 → Phase 1로 진행 (서버 정상)
   └─ 응답 없음/에러 → Step 0-3으로 (서버 기동 시도)
```

### Step 0-3: 프로젝트 타입 탐지 & 서버 기동

`references/server-detection.md`를 참조하여 프로젝트 타입을 탐지한다.

```
1. 프로젝트 루트에서 빌드 파일 탐색 (Glob 도구 사용)
   ├─ build.gradle / build.gradle.kts → Spring Boot (Gradle)
   ├─ pom.xml → Spring Boot (Maven)
   ├─ package.json → Node.js
   ├─ docker-compose.yml → Docker Compose
   ├─ requirements.txt / pyproject.toml → Python
   └─ index.html만 존재 → 정적 HTML

2. 포트 탐지
   ├─ application.yml/properties → server.port
   ├─ package.json scripts → --port 플래그
   ├─ .env → PORT 변수
   └─ 기본값: 8080 (Spring Boot) / 3000 (Node.js) / 8000 (Python)

3. 서버 기동 (Bash, run_in_background: true)
   → 프로젝트 타입에 맞는 기동 커맨드 실행

4. Health check 폴링 (최대 30초, 3초 간격)
   → curl로 응답 확인
   → 기동 실패 시 사용자에게 보고 후 중단
```

**중요:** 서버 기동은 반드시 `run_in_background: true`로 실행한다. 기동 후 별도 Bash에서 health check를 수행한다.

## Phase 1: 브라우저 준비

```
1. ToolSearch로 Chrome MCP 도구 로드
   → "select:mcp__claude-in-chrome__tabs_context_mcp,mcp__claude-in-chrome__tabs_create_mcp,mcp__claude-in-chrome__navigate,mcp__claude-in-chrome__computer,mcp__claude-in-chrome__javascript_tool,mcp__claude-in-chrome__read_page,mcp__claude-in-chrome__read_console_messages,mcp__claude-in-chrome__read_network_requests,mcp__claude-in-chrome__get_page_text,mcp__claude-in-chrome__gif_creator,mcp__claude-in-chrome__form_input,mcp__claude-in-chrome__find"

2. tabs_context_mcp로 현재 탭 상태 확인
   → 기존에 대상 URL이 열려있으면 해당 탭 사용

3. tabs_create_mcp로 새 탭 생성 (기존 탭 없을 때)

4. navigate로 대상 URL 접속
   → 로딩 실패 시 사용자에게 서버 상태 확인 요청
```

## Phase 2: 페이지 탐지

점검 대상 페이지 목록을 자동으로 구성한다.

```
1. 기본 동작(변경사항 기반)인 경우
   → Step D-1에서 도출한 페이지 목록 사용 (이미 결정됨)

2. URL 지정 + 전체 점검인 경우
   ├─ Spring Boot → @Controller/@RequestMapping 매핑 탐색
   ├─ Node.js → router 설정, pages 디렉토리 탐색
   ├─ 정적 HTML → *.html 파일 목록
   └─ SPA → 라우터 설정 파일에서 경로 추출

3. 사용자가 특정 페이지를 지정한 경우 해당 페이지만 점검

4. 점검 대상 페이지 목록 작성 및 사용자에게 공유
```

## Phase 3: 페이지별 QA 점검

각 페이지를 순회하며 아래 항목을 **순서대로** 점검한다.

### A. 콘솔 에러 확인
- `read_console_messages`로 JavaScript 에러/경고 수집
- pattern: `error|Error|ERROR|warn|Warning` 필터 적용
- 에러 발견 시 변경된 JS 파일과 대조하여 원인 분석

### B. 네트워크 요청 확인
- `read_network_requests`로 API 호출 상태 확인
- 4xx, 5xx 응답 필터링
- 변경된 Controller/API와 매핑되는 요청이 정상인지 확인

### C. 페이지 렌더링 확인
- `read_page`로 현재 페이지 DOM 구조 확인
- 변경된 HTML/JSP/CSS에 해당하는 요소가 정상 렌더링되는지 확인
- 빈 영역, 깨진 레이아웃 등 시각적 이상 탐지

### D. 기능 동작 확인
- `read_page(filter: "interactive")`로 인터랙티브 요소 목록 확인
- 변경된 JS에서 정의한 이벤트 핸들러가 바인딩되어 있는지 확인
- `javascript_tool`로 주요 함수/변수의 존재 여부 확인
- 버튼 클릭, 폼 제출 등 주요 인터랙션 시뮬레이션 (`computer: left_click`)

### E. GIF 캡처 (이슈 발견 시)
- `gif_creator`로 문제 재현 과정을 GIF로 기록
- 파일명: `debug_{브랜치명}_{타임스탬프}.gif`

### 점검 체크리스트

**공통 점검:**
- [ ] 콘솔 에러 없는지 확인
- [ ] 네트워크 에러(4xx/5xx) 없는지 확인
- [ ] 모든 네비게이션 링크 동작 확인
- [ ] 사이드바/헤더 메뉴 모든 항목 클릭 확인

**기능 점검:**
- [ ] CRUD 동작 확인 (생성/조회/수정/삭제)
- [ ] 폼 유효성 검사 피드백 확인
- [ ] 데이터 저장/로드 확인 (localStorage, API 등)
- [ ] 토글/스위치 동작 확인
- [ ] 검색/필터 동작 확인

## Phase 4: 소스 코드 크로스체크

브라우저에서 발견된 이슈의 원인을 소스 코드에서 확인한다.

```
1. Read 도구로 JS/TS 소스 파일 읽기
2. 스텁 구현 확인 (toast만 표시하고 실제 동작 없는 함수)
3. 이벤트 바인딩 누락 확인
4. API 호출 누락 확인
5. 미구현 기능 식별
```

## Phase 5: 결과 보고

### 결과 출력 형식

```markdown
## 브라우저 QA 결과

> 브랜치: `{브랜치명}` | 대상 URL: `{URL}` | 일시: YYYY-MM-DD HH:MM

### 콘솔 상태
- 에러: {N}건 / 경고: {N}건 / 정상
- {에러 상세 내용}

### 네트워크 상태
- 총 요청: {N}건 | 실패: {N}건
- {실패한 요청 상세}

### 렌더링 상태
- {정상/이상} - {상세 설명}

### 기능 동작
- {테스트한 기능별 결과}

### 발견된 이슈
| # | 심각도 | 카테고리 | 설명 | 관련 파일 |
|---|--------|---------|------|----------|
| 1 | Critical/Major/Minor | JS/CSS/API/렌더링 | 이슈 설명 | 파일 경로 |

### GIF 캡처
- {캡처한 GIF 파일 경로 목록}
```

### 파일 업데이트

```
1. 시나리오 기반 QA인 경우
   → QA-SCENARIO.md에 각 시나리오별 PASS/FAIL 결과 기록
   → FAIL 시나리오에 실패 원인 및 GIF 경로 추가

2. TODO.md 업데이트 (프로젝트에 존재하는 경우)
   - 기존 항목과 중복 제거
   - 적절한 Phase에 항목 추가

3. git add
```

## Phase 6: 다음 액션

QA 완료 후 사용자에게 다음 액션을 제안:

- **이슈 수정**: 발견된 이슈를 심각도 순으로 바로 수정 → 수정 후 Phase 3으로 재검증
- **추가 디버깅**: 다른 페이지/기능을 추가로 확인 → Phase 2로 돌아감
- **특정 요소 상세 확인**: 특정 DOM 요소나 API를 집중 디버깅
- **종료**: QA 종료

## 입출력

| 입력 | 출력 |
|------|------|
| 없음 (기본) | `.claude/docs/QA-SCENARIO.md` (시나리오 + 결과) |
| 접속 URL (선택) | 이슈 목록 요약 (채팅 메시지) |
| 특정 페이지/기능 (선택) | 업데이트된 TODO.md (있는 경우) |

## 주의사항

- JavaScript `alert()`, `confirm()`, `prompt()` 등 모달 다이얼로그를 트리거하지 않는다
- 페이지 내 삭제/수정 등 실제 데이터에 영향을 주는 동작은 사용자 확인 후 진행
- 브라우저 확장 프로그램이 응답하지 않으면 2~3회 재시도 후 사용자에게 안내
- 로그인이 필요한 페이지는 사용자에게 로그인 완료를 요청한 후 진행

## 관련 스킬

- `pencil-to-code`: 디자인 → 코드 변환 후 브라우저 검증에 활용
- `pencil-update`: 디버깅에서 발견된 UI 이슈를 디자인에 먼저 반영할 때
