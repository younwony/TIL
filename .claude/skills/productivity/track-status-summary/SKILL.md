---
name: track-status-summary
description: |
  Track 작업의 전반적인 내용을 시각적으로 요약한 대시보드 문서(MD + HTML)를 생성한다.
  "/track-status-summary", "작업 요약", "트랙 요약", "작업 리포트", "트랙 리포트",
  "작업 정리", "작업 현황 문서", "track summary", "작업 대시보드" 요청에 트리거된다.
  /track-status가 현재 상태 + 다음 단계를 빠르게 보여주는 CLI 출력이라면,
  이 스킬은 전체 작업 히스토리를 브라우저에서 시각적으로 확인할 수 있는 대시보드를 생성한다.
  트랙의 Phase별 작업 내역, 아키텍처 다이어그램, 변경 파일, Git 통계를 한눈에 파악할 수 있다.
---

# Track Status Summary

Track의 전체 작업 내용을 시각적 대시보드(HTML)와 마크다운 요약 문서로 생성한다.
`/track-status`가 "지금 어디까지 왔나?"를 답한다면, 이 스킬은 "전체적으로 무슨 작업을 했나?"를 답한다.

## 실행 방법

```
/track-status-summary                    # Active Track 자동 감지
/track-status-summary TECH-22403         # 특정 Track 지정
```

## 작업 절차

### 1단계: Track 감지

CLAUDE.md의 `DOC_DIR 결정 규칙`을 따른다:

1. `.claude/tracks/index.md` 또는 `.claude/tracks/` 하위 디렉토리를 스캔
2. `$ARGUMENTS`가 있으면 해당 Track 사용
3. 없으면 Active Track 자동 선택 (2개+ 시 AskUserQuestion)

Track 디렉토리 경로를 `{TRACK_DIR}`로 설정한다.

### 2단계: 데이터 수집

다음 소스에서 정보를 수집한다. 각 소스가 없으면 해당 섹션을 "정보 없음"으로 표시하고 넘어간다.

#### 2-1. Track 메타데이터
- `{TRACK_DIR}/metadata.json` 읽기
- Track ID, 설명, 상태, 브랜치, 생성일, Jira 이슈

#### 2-2. Phase별 작업 내역
- `{TRACK_DIR}/plan.md` 또는 `{TRACK_DIR}/*_PLAN.md` 읽기
- Phase 이름, 완료 상태, 하위 Task 목록
- 없으면 WORK-SPEC 파일들(`*_WORK-SPEC*.md`)에서 Phase 정보 추출

#### 2-3. 작업 명세서 요약
- `{TRACK_DIR}/*_WORK-SPEC*.md` 파일들 읽기
- 각 Phase의 목표, 주요 변경사항, 핵심 결정사항 추출
- 여러 WORK-SPEC이 있으면 Phase 번호순으로 정렬

#### 2-4. 아키텍처/다이어그램
- `{TRACK_DIR}/images/*.svg` 파일 목록
- SVG 파일 내용을 읽어 HTML에 인라인할 준비

#### 2-5. Git 변경 통계
- 브랜치명을 metadata.json에서 가져옴
- `git log` + `git diff --stat`으로 커밋 수, 변경 파일, 추가/삭제 라인 수집
- 브랜치가 없거나 git 정보를 못 가져오면 이 섹션 스킵

#### 2-6. 문서 현황
- Track 디렉토리 내 모든 파일을 스캔
- 워크플로우 문서 존재 여부 체크 (WORK-SPEC, PLAN, SELF-REVIEW, QA-SCENARIOS 등)

#### 2-7. HANDOFF 정보 (있는 경우)
- `{TRACK_DIR}/HANDOFF.md` 읽기
- 완료된 작업, 남은 작업, 주의사항 추출

### 3단계: 마크다운 요약 생성

`{TRACK_DIR}/SUMMARY.md`를 생성한다. 구조:

```markdown
# {Track ID} 작업 요약

> {description}
> 생성일: {created_at} | 상태: {status} | 브랜치: {branch}

## Phase별 작업 내역

### Phase 1: {Phase명} ✅
- **목표**: {Phase 목표}
- **주요 변경**: {핵심 변경사항 2-3줄 요약}
- **변경 파일**: {파일 목록}
- **핵심 결정**: {왜 이렇게 했는지}

### Phase 2: {Phase명} 🔄
...

## 아키텍처

{다이어그램 이미지 참조}

## 변경 통계
- 커밋: {N}개
- 변경 파일: {N}개
- 추가: +{N} / 삭제: -{N}

## 문서 현황
| 문서 | 상태 |
|------|------|
| WORK-SPEC | ✅ |
| SELF-REVIEW | ❌ |
...

## 현재 상태 & 다음 단계
{HANDOFF 기반 요약}
```

### 4단계: HTML 대시보드 생성

`{TRACK_DIR}/html/summary-dashboard.html`을 생성한다.

#### HTML 생성 방법

`assets/dashboard-template.html` 템플릿을 읽고, 플레이스홀더를 실제 데이터로 교체한다:

| 플레이스홀더 | 데이터 |
|-------------|--------|
| `__TRACK_ID__` | Track ID |
| `__TRACK_DESC__` | 설명 |
| `__TRACK_STATUS__` | 상태 배지 |
| `__TRACK_BRANCH__` | 브랜치명 |
| `__TRACK_CREATED__` | 생성일 |
| `__TRACK_JIRA__` | Jira 이슈 번호 |
| `__PHASES_HTML__` | Phase별 카드 HTML |
| `__DIAGRAMS_HTML__` | SVG 다이어그램 인라인 HTML |
| `__GIT_STATS_HTML__` | Git 통계 HTML |
| `__DOCS_STATUS_HTML__` | 문서 현황 체크리스트 HTML |
| `__HANDOFF_HTML__` | 현재 상태/다음 단계 HTML |
| `__GENERATED_AT__` | 생성 시각 |

#### Phase 카드 HTML 생성 규칙

각 Phase를 카드 형태로 생성한다:

```html
<div class="phase-card {status-class}">
  <div class="phase-header">
    <span class="phase-badge">{Phase N}</span>
    <h3>{Phase명}</h3>
    <span class="status-icon">{✅/🔄/⏳}</span>
  </div>
  <div class="phase-body">
    <p class="phase-goal">{Phase 목표}</p>
    <div class="phase-changes">
      <h4>주요 변경</h4>
      <ul>{변경사항 리스트}</ul>
    </div>
    <div class="phase-files">
      <h4>변경 파일</h4>
      <ul class="file-list">{파일 목록}</ul>
    </div>
  </div>
</div>
```

- `status-class`: `completed`, `in-progress`, `pending` 중 하나
- Phase가 완료되면 배경색이 연한 초록, 진행중이면 연한 파랑, 대기중이면 회색

#### SVG 다이어그램 인라인

SVG 파일을 직접 HTML 안에 삽입한다 (외부 참조 X):

```html
<div class="diagram-card">
  <h3>{SVG 파일명에서 추출한 제목}</h3>
  <div class="diagram-container">
    {SVG 파일 내용 그대로 삽입}
  </div>
</div>
```

### 5단계: 브라우저에서 열기

생성된 HTML 파일을 브라우저에서 연다:

```bash
# Windows
start "" "{TRACK_DIR}/html/summary-dashboard.html"

# macOS
# open "{TRACK_DIR}/html/summary-dashboard.html"
```

### 6단계: 결과 안내

사용자에게 생성된 파일 경로를 안내한다:

```
## 생성 완료

- 마크다운 요약: {TRACK_DIR}/SUMMARY.md
- HTML 대시보드: {TRACK_DIR}/html/summary-dashboard.html
  → 브라우저에서 열었습니다

대시보드에서 Phase별 작업 내역, 아키텍처 다이어그램, Git 통계를 확인할 수 있습니다.
```

## 주의사항

- HTML은 외부 CDN 의존 없이 standalone으로 동작해야 한다 (모든 CSS/JS 인라인)
- SVG가 너무 크면 (100KB+) 축소하거나 별도 탭으로 분리
- `.claude/` 하위 파일이므로 git add하지 않는다
- 기존 SUMMARY.md / summary-dashboard.html이 있으면 덮어쓴다
