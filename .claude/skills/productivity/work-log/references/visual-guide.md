# Work Log 시각화 가이드

work-log 페이지를 **글 위주가 아니라 다이어그램·패널·배지로 풍부하게** 구성하기 위한 표준이다.
SKILL.md / work-log.md 커맨드의 다이어그램 작성·Confluence 삽입 단계는 이 문서를 따른다.

## 핵심 원칙

- **글보다 그림** — 작업 흐름·상태 전이·아키텍처·분기 로직은 산문 대신 SVG 다이어그램으로 표현한다.
- **작업당 SVG 2~4개** — 전체 개요 1개는 필수. 그 외 핵심 흐름/상태 전이/분기 로직별로 추가.
- **HTML 매크로 금지** — `ac:structured-macro ac:name="html"`은 Confluence Cloud에서 기본 비활성화되어 렌더되지 않는다. **반드시 attachment + `<ac:image>` 방식**을 쓴다.

## 1. SVG 다이어그램 작성 규칙

직접 손으로 SVG를 작성한다(외부 다이어그램 스킬 의존 없음). 박스 + 화살표 다이어그램이 기본형.

### 기본 골격

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1120 500" font-family="Segoe UI, Malgun Gothic, sans-serif">
  <rect width="1120" height="500" fill="#f8fafc"/>
  <text x="560" y="38" text-anchor="middle" font-size="22" font-weight="700" fill="#0f172a">제목</text>
  <!-- 박스 -->
  <rect x="40" y="80" width="220" height="90" rx="8" fill="..." stroke="..." stroke-width="1.8"/>
  <text x="150" y="130" text-anchor="middle" font-size="13" font-weight="600" fill="...">
    <tspan x="150" dy="0">첫 줄</tspan><tspan x="150" dy="18">둘째 줄</tspan>
  </text>
  <!-- 화살표 -->
  <path d="M150 170 L150 210" stroke="#94a3b8" stroke-width="2.5"/>
  <polygon points="143,208 157,208 150,218" fill="#94a3b8"/>
</svg>
```

### 색상 팔레트 (의미 고정)

| 의미 | 채움(fill) | 테두리(stroke) | 용도 |
|------|-----------|---------------|------|
| 원인 / 위험 | `#fee2e2` | `#dc2626` | 장애 원인, Kill, 에러 |
| 문제 / 고착 | `#ffedd5` | `#ea580c` | stuck 상태, 개선 전 |
| 해결 / 복구 | `#dcfce7` | `#16a34a` | 복구 로직, 개선 후, 성공 |
| 진행 / 중간 | `#dbeafe` | `#3b82f6` | RUNNING 등 처리 중 |
| 대기 / 중립 | `#e2e8f0` | `#94a3b8` | WAIT, 초기 상태 |
| 분기 / 판단 | `#fef9c3` | `#ca8a04` | 다이아몬드 조건 분기 |
| 공통 / 강조 | `#e0e7ff` | `#6366f1` | 공통 처리, 핵심 노트 |

- 배경은 `#f8fafc`(연회색). 텍스트는 박스 색 계열의 진한 톤(`#991b1b`, `#9a3412`, `#166534` 등).
- 강조 흐름(복구 경로 등)은 화살표를 초록(`#16a34a`) + `stroke-width` 굵게.
- 텍스트 줄바꿈은 `<tspan x="..." dy="18">`로 처리(SVG는 자동 줄바꿈 없음).

### 권장 다이어그램 유형

| 유형 | 언제 | 구성 |
|------|------|------|
| 전체 개요도 | 항상(필수) | 원인 → N개 영역(문제→해결) 컬럼 그리드 |
| 상태 전이도 | 상태머신 변경 시 | 박스 가로 배치 + 라벨 화살표, 루프백은 곡선 path |
| 분기 흐름도 | 조건 분기 로직 | 다이아몬드 + YES/NO 두 갈래 + 공통 합류 |
| 아키텍처도 | 레이어/컴포넌트 추가 | Controller/Service/Repository 레이어 박스 |

## 2. Confluence 삽입 — attachment 방식

### 2-1. SVG를 페이지에 첨부 (curl)

페이지가 먼저 존재해야 한다(pageId 필요). v1 attachment API + `X-Atlassian-Token: nocheck`:

```bash
curl -s -u "$EMAIL:$ATLASSIAN_API_TOKEN" -X POST \
  -H "X-Atlassian-Token: nocheck" \
  -F "file=@01_overview.svg;type=image/svg+xml" \
  -F "file=@02_flow.svg;type=image/svg+xml" \
  "https://temcolabs.atlassian.net/wiki/rest/api/content/{pageId}/child/attachment"
```

여러 파일을 `-F`로 한 번에 첨부할 수 있다. 동일 파일명 재첨부 시 새 버전으로 갱신된다.

### 2-2. 본문에서 이미지 참조

storage format 본문에 `<ac:image>` + `<ri:attachment>`로 삽입한다(HTML 매크로 아님):

```xml
<p><ac:image ac:width="1100" ac:align="center"><ri:attachment ri:filename="01_overview.svg" /></ac:image></p>
```

- `ac:width`는 SVG viewBox 가로값 근처로(최대 1100~1280, wide 레이아웃 기준).
- 파일명은 `01_`, `02_` 숫자 prefix로 순서를 고정한다.

### 2-3. 생성/업데이트 순서

- **신규 페이지**: ① 페이지 생성(본문에 `<ac:image>` 참조 포함) → ② SVG 첨부. 첨부 전 잠깐 깨진 이미지로 보이나 첨부 후 정상 렌더된다.
- **기존 페이지 업데이트**: ① SVG 첨부(pageId 이미 존재) → ② 본문 PUT(`version.number` +1).

## 3. 시각 매크로 활용

글 블록을 패널·배지로 분해해 가독성을 높인다.

### 패널 매크로

```xml
<!-- 메타데이터 / 개요 -->
<ac:structured-macro ac:name="info"><ac:rich-text-body><p>...</p></ac:rich-text-body></ac:structured-macro>
<!-- 설계 의도 / 팁 -->
<ac:structured-macro ac:name="tip"><ac:rich-text-body><p>...</p></ac:rich-text-body></ac:structured-macro>
<!-- 핵심 결정 / 주의 -->
<ac:structured-macro ac:name="note"><ac:rich-text-body><ul><li>...</li></ul></ac:rich-text-body></ac:structured-macro>
<!-- 리스크 / 경고 -->
<ac:structured-macro ac:name="warning"><ac:rich-text-body><p>...</p></ac:rich-text-body></ac:structured-macro>
```

| 매크로 | 용도 |
|--------|------|
| `info` | 페이지 상단 메타데이터(Jira/Track/날짜/상태) |
| `tip` | 설계 의도, 권장 사항 |
| `note` | 핵심 설계 결정, 알아둘 점 |
| `warning` | 리스크, 후속 조치 필요 |

### status 배지

표 안의 라벨·구분·완료 상태는 컬러 배지로:

```xml
<ac:structured-macro ac:name="status">
  <ac:parameter ac:name="colour">Green</ac:parameter>
  <ac:parameter ac:name="title">BUILD SUCCESSFUL</ac:parameter>
</ac:structured-macro>
```

색상: `Green`(완료/성공), `Blue`(구분/라벨), `Yellow`(진행 중), `Red`(실패/위험), `Grey`(대기).

## 4. 레이아웃

본문 전체를 wide breakout으로 wrap하고, 모든 표에 `data-layout="wide"`:

```xml
<ac:layout><ac:layout-section ac:type="single" ac:breakout-mode="wide"><ac:layout-cell>
  <!-- 본문: info 패널 → h2 섹션 → ac:image → wide 표 → tip/note 패널 ... -->
</ac:layout-cell></ac:layout-section></ac:layout>
```

## 5. 페이지 구성 순서 (권장)

1. `info` 패널 — Jira/Track/날짜/상태
2. `## 개요` — `blockquote` 한 문단 + **전체 개요 SVG**
3. `## 작업 범위` — wide 표 (status 배지로 구분 컬럼)
4. 핵심 기능별 섹션 — 각 **흐름/상태 SVG** + 설명 목록 + `tip` 패널
5. `## 핵심 설계 결정` — `note` 패널
6. 보조 섹션(DB 튜닝/성능 등) — 비교 표
7. `## 테스트` — 표 + status 배지
8. `## 변경 파일 요약`

> 임시로 만든 SVG·payload 파일은 작업 후 삭제한다(첨부본은 Confluence 페이지에 보존됨).
