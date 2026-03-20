---
name: qa-scenario
description: |
  현재 브랜치의 변경사항을 분석하여 체계적인 QA 시나리오 문서(QA-SCENARIOS.md)를 자동 생성합니다.
  변경 파일을 카테고리별로 분류하고, 영향 범위를 파악한 뒤, BDD(Given-When-Then) 형식과
  Mermaid 다이어그램을 포함한 QA 시나리오를 제안합니다.

  Plan 모드에서 분석 결과를 보여주고, 사용자 승인 후 최종 QA-SCENARIOS.md를 생성합니다.
  browser-debug 스킬의 Chrome 자동화 전 단계로 사용하거나, 독립적으로 QA 문서를 만들 때 사용합니다.

  다음 키워드/문맥에서 트리거됩니다:
  - "QA 시나리오", "QA 시나리오 만들어", "QA 시나리오 생성"
  - "테스트 시나리오", "테스트 케이스 작성", "테스트 계획"
  - "변경 영향 분석", "영향 범위 파악", "변경사항 QA"
  - "QA 문서", "QA 체크리스트", "인수 테스트 문서"
  - "이번 작업 QA", "브랜치 QA", "PR QA 시나리오"
  - `/qa-scenario` 슬래시 커맨드로도 호출 가능

  코드를 변경한 후 QA가 필요할 때, PR 전 테스트 계획이 필요할 때,
  또는 팀에 변경 영향도를 공유할 때 이 스킬을 사용하세요.
---

# QA Scenario Generator

현재 브랜치의 변경사항을 분석하여 체계적인 QA 시나리오 문서를 생성한다.
반드시 아래 Phase를 순서대로 수행한다.

---

## Phase 1: 변경사항 수집 (자동)

### 1-1. Git 변경사항 수집

```bash
# staged + unstaged 변경 파일 목록
git diff --cached --name-status
git diff --name-status
git status --short

# 변경 내용 상세
git diff --cached
git diff

# 비교 브랜치 대비 전체 변경 (PR용)
git log --oneline main..HEAD
git diff main...HEAD --stat
```

### 1-2. 변경 파일 분류

수집된 파일을 아래 카테고리로 분류한다:

| 카테고리 | 파일 패턴 | 분석 포인트 |
|---------|----------|------------|
| **Controller** | `*Controller.java` | URL 엔드포인트, HTTP 메서드, 요청/응답 DTO |
| **Service** | `*Service.java`, `*ServiceImpl.java` | 비즈니스 로직 변경, 트랜잭션 경계 |
| **Repository** | `*Repository.java`, `*Mapper.xml` | 쿼리 변경, 데이터 모델 |
| **Entity/DTO** | `*Entity.java`, `*Dto.java`, `*Request.java`, `*Response.java` | 필드 변경, 유효성 검증 |
| **View** | `*.jsp`, `*.html`, `*.ftl`, `*.mustache` | UI 렌더링, 레이아웃, 폼 구조 |
| **JavaScript** | `*.js`, `*.ts`, `*.vue`, `*.jsx`, `*.tsx` | 이벤트 핸들러, API 호출, DOM 조작 |
| **CSS** | `*.css`, `*.scss`, `*.less` | 스타일링, 반응형, 레이아웃 |
| **설정** | `application.*`, `*.xml`, `build.gradle`, `pom.xml` | 설정 변경, 빈 등록, 의존성 |
| **테스트** | `*Test.java`, `*Spec.java`, `*.test.*` | 테스트 커버리지 변경 |
| **기타** | 위에 해당하지 않는 파일 | 간접 영향 분석 |

---

## Phase 2: 영향 범위 분석 (Plan 모드)

**EnterPlanMode**로 Plan 모드에 진입한 후, Explore 에이전트를 사용하여 변경 코드의 영향 범위를 "very thorough"로 분석한다.

### 2-1. 코드 흐름 추적

변경된 코드가 영향을 미치는 전체 흐름을 추적한다:

- **진입점 파악**: Controller의 URL 매핑 → 어떤 페이지/API에서 호출되는가
- **호출 체인**: Controller → Service → Repository → DB 순서로 추적
- **프론트엔드 연결**: JSP/HTML에서 해당 API를 호출하는 JS 코드, 폼, 버튼
- **사용자 인터랙션**: onclick, submit, jQuery handler, AJAX 호출 등
- **모달/팝업**: 동적으로 열리는 모달, confirm 다이얼로그
- **외부 연동**: 파일 업로드/다운로드, 외부 API 호출
- **상태 변화**: 데이터 상태 전이 (예: 대기→처리→완료)

### 2-2. 영향도 매트릭스

분석 결과를 영향도 매트릭스로 정리한다:

```markdown
| 변경 파일 | 직접 영향 | 간접 영향 | 위험도 |
|----------|----------|----------|--------|
| ItemController.java | /api/items CRUD | 목록 페이지, 상세 페이지 | 🔴 높음 |
| ItemService.java | 저장 로직 | 검증, 이벤트 발행 | 🟡 중간 |
| item-list.js | 그리드 렌더링 | 필터, 페이지네이션 | 🟡 중간 |
```

위험도 기준:
- 🔴 **높음**: 핵심 기능(CRUD, 인증, 결제) 또는 다수 사용자 영향
- 🟡 **중간**: 부가 기능 또는 특정 조건에서만 영향
- 🟢 **낮음**: UI 변경, 텍스트 수정, 설정 변경

### 2-3. 테스트 범위 결정

영향도 매트릭스를 기반으로 테스트 범위를 결정한다:

- 🔴 높음 → **P0 시나리오** (반드시 테스트)
- 🟡 중간 → **P1 시나리오** (주요 기능)
- 🟢 낮음 → **P2 시나리오** (부가 기능)

---

## Phase 3: QA 시나리오 초안 작성 (Plan 모드 유지)

### 3-1. 다이어그램 생성 (SVG 이미지)

변경 유형에 따라 적절한 다이어그램을 선택한다:

| 변경 유형 | 필수 다이어그램 | 선택 다이어그램 |
|----------|--------------|--------------|
| 페이지 신규/수정 | flowchart (유저 플로우) | journey (사용자 경험) |
| API 추가/변경 | sequenceDiagram | flowchart |
| 상태 전이 로직 | stateDiagram | flowchart |
| CRUD 전체 | flowchart + sequenceDiagram | journey |
| UI만 변경 | flowchart | - |

**flowchart 스타일 규칙:**
- 성공 경로: `style node fill:#c8e6c9` (초록)
- 실패 경로: `style node fill:#ffcdd2` (빨강)
- 시작점: `style node fill:#e1f5fe` (파랑)
- 분기점: 마름모 `{}` 사용

**SVG 생성 절차:**

다이어그램은 Mermaid 코드 블록이 아닌 **SVG 이미지 파일**로 생성한다.

1. 프로젝트 루트에 `qa-images/` 디렉토리를 생성한다
2. `cs-diagram-generator` 에이전트를 사용하여 각 다이어그램을 SVG로 생성한다:
   - 유저 플로우 → `qa-images/user-flow.svg`
   - API 시퀀스 → `qa-images/api-sequence.svg` (해당 시)
   - 상태 다이어그램 → `qa-images/state-diagram.svg` (해당 시)
3. QA-SCENARIOS.md에서는 이미지 참조로 포함한다:
   ```markdown
   ![유저 플로우](qa-images/user-flow.svg)
   ```

> `cs-diagram-generator` 에이전트가 실패하면, `mermaid-diagram` 스킬로 폴백한다.
> `mermaid-diagram`도 실패하면, Mermaid 코드 블록을 문서에 직접 포함한다.

### 3-2. 시나리오 작성 형식

모든 시나리오는 BDD Given-When-Then 형식으로 작성한다:

```markdown
#### S01. {기능명}
- **우선순위**: P0
- **테스트 URL**: {URL}
- **시나리오**:
  - **Given**: {사전 조건 - 로그인 상태, 데이터 존재 여부 등}
  - **When**: {사용자 액션 - 클릭, 입력, 선택 등}
  - **Then**: {기대 결과 - 화면 변화, API 응답, 데이터 변경 등}
- **검증 방법**: {screenshot / javascript / network / console}
- **검증 코드**:
  ```javascript
  // 자동화 검증에 사용할 구체적인 코드
  ```
- **결과**: ⬜ 미실행
```

### 3-3. 시나리오 분류 기준

| 우선순위 | ID 범위 | 대상 | 예시 |
|---------|---------|------|------|
| **P0** | S01~S04 | 핵심 기능 — 이것이 실패하면 배포 불가 | 페이지 로드, 메인 CRUD, 핵심 API |
| **P1** | S05~S09 | 주요 기능 — 사용성에 직접 영향 | UI 인터랙션, 필터, 정렬, 페이지네이션 |
| **P2** | S10~ | 부가 기능 — 엣지 케이스 | 엑셀, 빈 데이터, 외부 연동, 날짜 범위 |

### 3-4. 검증 방법 가이드

| 검증 유형 | 사용 상황 | 자동화 도구 |
|----------|----------|-----------|
| **screenshot** | UI 레이아웃, 스타일 확인 | Chrome 캡처 |
| **javascript** | DOM 상태, 변수 값, 그리드 데이터 | `javascript_tool` |
| **network** | API 호출 URL, 상태코드, 응답 데이터 | `read_network_requests` |
| **console** | JS 에러, 경고, 디버그 로그 | `read_console_messages` |

검증 코드는 `browser-debug` 스킬의 Chrome 자동화에서 바로 사용할 수 있도록 구체적으로 작성한다.

---

## Phase 4: 사용자 리뷰 & 승인

Plan 모드 내에서 사용자에게 다음을 보여준다:

### 4-1. 분석 결과 요약

```markdown
## 분석 결과

### 변경 파일 ({N}개)
| 카테고리 | 파일 | 변경 유형 |
|---------|------|----------|
| Controller | ItemController.java | 수정 (M) |
| ...

### 영향도 매트릭스
{2-2에서 작성한 매트릭스}

### 시나리오 요약
| ID | 우선순위 | 기능명 | 테스트 URL |
|----|---------|--------|-----------|
| S01 | P0 | 목록 페이지 로드 | /admin/items/page |
| S02 | P0 | 저장 API | /api/items |
| ...

### 다이어그램 미리보기
{Mermaid 코드 블록으로 유저 플로우 표시}
```

### 4-2. 사용자 확인 항목

사용자에게 다음을 묻는다:
1. **시나리오 추가/수정/삭제** 필요한가?
2. **우선순위 조정** 필요한가?
3. **누락된 엣지 케이스** 있는가?
4. **테스트 데이터/환경** 특이 사항 있는가?

### 4-3. 승인 후 진행

사용자가 승인하면:
1. **ExitPlanMode**로 Plan 모드 종료
2. Phase 5로 진행하여 최종 문서 생성

---

## Phase 5: QA-SCENARIOS.md 생성

프로젝트 루트에 `QA-SCENARIOS.md` 파일을 생성한다.
(`references/qa-document-template.md` 참조)

### 문서 구조

```markdown
# QA Scenarios - {브랜치명}

## 환경
- 브랜치: {branch_name}
- 비교 기준: {compare_branch}
- 테스트 URL: http://localhost:{port}
- 생성 일시: {YYYY-MM-DD HH:mm}
- 변경 파일: {N}개

## 변경 요약
{변경된 기능 1~2줄 요약}

## 영향도 매트릭스
{변경 파일별 직접/간접 영향, 위험도 테이블}

## 유저 플로우 다이어그램
![유저 플로우](qa-images/user-flow.svg)

## API 시퀀스 다이어그램
{Controller/API 변경 시에만 포함}
![API 시퀀스](qa-images/api-sequence.svg)

## 시나리오 목록

### P0 - 핵심 기능 (반드시 테스트)
{S01~S04}

### P1 - 주요 기능
{S05~S09}

### P2 - 부가 기능
{S10~}

## 결과 요약
{QA 실행 후 업데이트 예정}

## 발견된 버그
| # | 시나리오 | 심각도 | 문제 | 원인 | 수정 파일 | 수정 내용 |
|---|---------|--------|------|------|---------|---------|

## 미검증 항목
- {테스트 데이터 부족, UI 미구현 등으로 확인 불가한 항목}
```

### 결과 상태 아이콘

| 상태 | 아이콘 | 설명 |
|------|--------|------|
| 미실행 | ⬜ | 아직 테스트하지 않음 |
| 실행중 | 🔄 | 현재 테스트 진행중 |
| 통과 | ✅ | 기대 결과와 일치 |
| 실패 | ❌ | 기대 결과와 불일치 |
| 수정후통과 | 🔧 | 버그 발견 → 수정 → 재검증 통과 |
| 차단됨 | ⚠️ | 선행 조건 미충족으로 테스트 불가 |

---

## Phase 6: Track 연동 및 완료 보고

### Track 연동

`.claude/tracks/` 디렉토리에서 status가 `in_progress`인 활성 Track을 탐색한다.

활성 Track이 있는 경우:
1. Track의 `plan.md`에서 "브라우저 QA" Phase를 찾는다
2. "QA 시나리오 생성" Task를 `[x]`로 마킹한다
3. `metadata.json`의 `current_phase`를 QA Phase 번호로 갱신한다
4. `.claude/tracks/index.md`에서 해당 Track이 `[~]` 상태인지 확인한다

활성 Track이 없는 경우: Track 연동 없이 진행한다.

### 완료 보고

1. `QA-SCENARIOS.md` 생성 완료 알림
2. 시나리오 수/우선순위별 분포 요약
3. `browser-debug` 스킬로 자동 실행 가능함을 안내
4. `git add QA-SCENARIOS.md`는 하지 않음 (로컬 참조용)
5. Track 연동된 경우: Track ID와 QA Phase 상태를 함께 표시

---

## 활용 시나리오

### 독립 사용
`/qa-scenario` → QA 시나리오 문서 생성 → 팀 공유 / PR 첨부

### browser-debug 연계
`/qa-scenario` → QA 시나리오 생성 → `/browser-debug` → Chrome 자동화 실행

### PR 리뷰 첨부
`/qa-scenario` → QA 시나리오 생성 → PR description에 포함

---

## 주의사항

- Plan 모드에서 충분히 분석한 후 시나리오를 제안한다 (성급하게 문서 생성 X)
- 변경 코드를 직접 읽어 영향 범위를 파악한다 (파일명만 보고 추측 X)
- 검증 코드는 `browser-debug` 스킬에서 바로 실행 가능하도록 구체적으로 작성한다
- 시나리오가 너무 많으면 (15개 초과) P0/P1에 집중하고 P2는 간략하게 작성한다
- 테스트 데이터가 필요한 시나리오는 Given 조건에 명시한다

## 관련 스킬

- `browser-debug`: QA 시나리오를 Chrome 자동화로 실행
- `test-coverage-check`: 단위/통합 테스트 커버리지 분석
- `self-review`: PR 전 코드 자체 리뷰
