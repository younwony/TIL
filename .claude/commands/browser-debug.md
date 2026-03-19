---
description: 현재 브랜치 변경사항 기반 Chrome 브라우저 QA (변경사항 분석 → QA 시나리오 MD 생성 → Plan 모드 실행 → 자동 점검)
allowed-tools: Bash, Read, Glob, Grep, Edit, Write, ToolSearch, Agent, EnterPlanMode, ExitPlanMode, mcp__claude-in-chrome__tabs_context_mcp, mcp__claude-in-chrome__tabs_create_mcp, mcp__claude-in-chrome__navigate, mcp__claude-in-chrome__read_page, mcp__claude-in-chrome__get_page_text, mcp__claude-in-chrome__javascript_tool, mcp__claude-in-chrome__find, mcp__claude-in-chrome__computer, mcp__claude-in-chrome__form_input, mcp__claude-in-chrome__read_console_messages, mcp__claude-in-chrome__read_network_requests, mcp__claude-in-chrome__gif_creator, mcp__claude-in-chrome__resize_window, mcp__claude-in-chrome__upload_image
---

# Browser Debug & QA

현재 브랜치의 변경사항을 분석하여 QA 시나리오 MD 파일을 생성하고, Plan 모드 기반으로 Chrome 자동화 점검을 수행합니다.

$ARGUMENTS

## 실행 워크플로우

반드시 아래 Phase를 순서대로 수행한다. Phase를 건너뛰지 않는다.

---

### Phase 1: 변경사항 분석 (Plan 모드 진입)

**EnterPlanMode**로 Plan 모드에 진입한 후 분석한다.

1. `git diff --cached --name-status` 및 `git status`로 변경된 파일 목록 수집
2. 변경 파일을 분류:
   - **Controller** (WebController, ApiController) → URL 엔드포인트 파악
   - **Service** → 비즈니스 로직 변경 파악
   - **JSP/JS/CSS** → UI 변경 파악
   - **Entity/DTO** → 데이터 모델 변경 파악
   - **Mapper XML** → 쿼리 변경 파악
3. Explore 에이전트를 사용하여 변경된 코드의 전체 기능을 "very thorough"로 분석:
   - 모든 사용자 인터랙션 (onclick, jQuery handler, form submit 등)
   - 모든 API 호출 (AJAX URL, 요청/응답 구조)
   - 모달/팝업
   - 엑셀 업로드/다운로드
   - 기존 버전과의 차이점

---

### Phase 2: QA 시나리오 MD 파일 생성

Phase 1 분석 결과를 기반으로 **프로젝트 루트에 `QA-SCENARIOS.md` 파일을 생성**한다.

시나리오 문서에 **Mermaid 다이어그램**과 **BDD Given-When-Then 형식**을 적용한다.
프로젝트에 `references/qa-scenario-guide.md`가 있으면 참조하여 다이어그램 종류를 결정한다.

#### QA-SCENARIOS.md 파일 구조

```markdown
# QA Scenarios - {브랜치명}

## 환경
- 브랜치: {branch_name}
- 비교 기준: {compare_branch}
- 테스트 URL: http://localhost:{port}
- 생성 일시: {datetime}
- 변경 파일: {N}개

## 변경 요약
{변경된 기능 1~2줄 요약}

## 유저 플로우 다이어그램

{Mermaid flowchart - 변경사항이 영향을 주는 전체 사용자 동선}
{성공 경로=초록, 실패 경로=빨강, 시작점=파랑}

## API 시퀀스 다이어그램

{Controller/API 변경이 있을 때만 포함}
{Mermaid sequenceDiagram - 프론트↔백엔드 통신 흐름}

## 시나리오 목록

### P0 - 핵심 기능 (반드시 테스트)

#### S01. {기능명}
- **테스트 URL**: {URL}
- **시나리오**:
  - **Given**: {사전 조건}
  - **When**: {사용자 액션}
  - **Then**: {기대 결과}
- **검증 방법**: screenshot / javascript_exec / network_request / console_log
- **검증 코드**:
  ```javascript
  // 자동화 검증에 사용할 코드
  ```
- **결과**: ⬜ 미실행

### P1 - 주요 기능
#### S05. ...

### P2 - 부가 기능
#### S10. ...

## 결과 요약
{QA 완료 후 Mermaid pie chart로 PASS/FAIL/FIX/BLOCKED 비율 표시}

## 발견된 버그

| # | 시나리오 | 심각도 | 문제 | 원인 | 수정 파일 | 수정 내용 |
|---|---------|--------|------|------|---------|---------|

## 미검증 항목
- {테스트 데이터 부족 등으로 확인 불가한 항목}
```

#### 다이어그램 포함 규칙

| 변경 유형 | 필수 다이어그램 | 선택 다이어그램 |
|----------|--------------|--------------|
| 페이지 신규/수정 | flowchart | journey |
| API 추가/변경 | sequenceDiagram | flowchart |
| 상태 전이 로직 | stateDiagram | flowchart |
| CRUD 전체 | flowchart + sequenceDiagram | journey |
| UI만 변경 | flowchart | - |

#### 시나리오 분류 기준

| 우선순위 | 범위 | 설명 |
|---------|------|------|
| **P0** | S01~S04 | 페이지 로드, Tiles 레이아웃, 핵심 API, 메인 CRUD |
| **P1** | S05~S09 | UI 인터랙션, 탭 전환, 필터 연동, 일괄변경 |
| **P2** | S10~ | 엣지 케이스, 엑셀, 외부 연동, 빈 데이터 |

#### 시나리오 작성 후 사용자 승인

`QA-SCENARIOS.md` 파일을 생성한 후:
1. 사용자에게 시나리오 요약 테이블을 보여준다
2. 추가/수정/삭제할 시나리오가 있는지 확인한다
3. 승인 후 **ExitPlanMode**로 Plan 모드를 종료하고 Phase 3으로 진행한다

---

### Phase 3: 서버 및 브라우저 준비

#### 서버 확인
1. `curl -s -o /dev/null -w "%{http_code}" http://localhost:8080` 로 서버 상태 확인
2. 서버 미기동 시 자체 기동:
   ```bash
   # Windows: 기존 프로세스 종료
   netstat -ano | findstr :8080
   taskkill /PID <pid> /F
   # 서버 기동
   cd <프로젝트루트> && ./gradlew bootRun &
   ```
3. Health check로 서버 ready 확인 (최대 60초 대기)

#### 서버 재기동이 필요한 경우
다음 파일이 변경된 경우 서버 재기동이 필요하다:
- `tiles*.xml` (Tiles 정의)
- `application*.yml` (설정)
- Java 소스 파일 (Controller, Service, Entity 등)
- `build.gradle`

서버 재기동이 필요하면 **사용자에게 묻지 않고 Bash로 자체 재기동**한다.

#### 브라우저 준비
1. Chrome 도구를 `ToolSearch`로 사전 로드 (tabs_context_mcp, navigate, computer, javascript_tool, read_console_messages, read_network_requests)
2. `tabs_context_mcp`로 현재 탭 상태 확인, 새 탭 생성
3. `read_console_messages` + `read_network_requests` 한 번 호출하여 **트래킹 활성화**
4. 로그인 페이지로 이동 → 자동 로그인 (CLAUDE.md의 로그인 정보 사용)

---

### Phase 4: QA 시나리오 순차 실행

`QA-SCENARIOS.md`의 시나리오를 **P0 → P1 → P2 순서대로** 실행한다.

#### 각 시나리오 실행 절차

1. **QA-SCENARIOS.md 업데이트**: 해당 시나리오 결과를 ⬜ → 🔄 실행중으로 변경
2. **사전 준비**: 필요한 페이지 이동, 데이터 준비
3. **액션 수행**: 사용자 액션을 Chrome 자동화로 실행
4. **검증 수행**:
   - `screenshot` → 화면 캡처 후 시각적 확인
   - `javascript_tool` → DOM 상태, 변수 값, 그리드 데이터 확인
   - `read_network_requests` → API 호출 URL, 상태코드, 응답 확인
   - `read_console_messages` → JS 에러, 경고 확인
5. **결과 판정 및 MD 업데이트**:
   - PASS → ✅ PASS
   - FAIL → ❌ FAIL (원인 기록)
   - BLOCKED → ⚠️ BLOCKED (사유 기록)
6. `QA-SCENARIOS.md`의 해당 시나리오 결과를 즉시 업데이트

#### FAIL 시 즉시 수정

- FAIL 발견 시 **원인 분석 → 코드 수정 → 재검증**을 즉시 수행
- 수정 후 해당 시나리오를 재실행하여 PASS 확인
- JS/CSS 수정은 브라우저 하드 리프레시(Ctrl+Shift+R)로 즉시 반영
- Java/설정 수정은 서버 재기동 후 재검증
- 수정 내용을 `QA-SCENARIOS.md`의 "발견된 버그" 테이블에 기록
- 결과를 🔧 FIX→PASS로 업데이트

#### P0 FAIL 시 중단 정책

- P0 시나리오가 FAIL이면 후속 시나리오 실행을 중단하고 수정에 집중
- P1/P2 FAIL은 기록 후 다음 시나리오 계속 진행

---

### Phase 5: 결과 보고

모든 시나리오 실행 후:

1. `QA-SCENARIOS.md`의 모든 시나리오 결과가 업데이트되었는지 확인
2. 미검증 항목이 있으면 사유와 함께 기록
3. QA 과정에서 수정한 파일을 `git add` (CLAUDE.md 규칙: commit은 하지 않음)
4. 사용자에게 최종 결과 요약을 보여준다:
   - 전체 시나리오 수 / PASS / FAIL / FIX / BLOCKED 카운트
   - 발견 및 수정한 버그 요약
   - 수정된 파일 목록

---

## 주의사항

- **모든 ToolSearch는 사전 실행**: Chrome 도구 사용 전 반드시 `ToolSearch`로 로드
- **Console/Network 트래킹 선 활성화**: 페이지 로드 전에 한 번 호출하여 트래킹 시작
- **서버 재기동 자체 처리**: 사용자에게 재기동 요청하지 않고 Bash로 직접 처리
- **QA-SCENARIOS.md 실시간 업데이트**: 각 시나리오 실행 즉시 결과 반영
- **CLAUDE.md 규칙 준수**: commit은 하지 않음, git add만 수행
- **QA-SCENARIOS.md는 git add 대상 제외**: QA 문서는 로컬 참조용
