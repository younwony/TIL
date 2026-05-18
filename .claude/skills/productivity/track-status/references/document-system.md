# 워크플로우 문서 시스템

> 문서 경로, 번호 체계, 작성 규칙, 다이어그램 설정을 한 곳에서 관리한다.
> 변경 시 이 파일만 수정하면 된다.
>
> 산출 문서는 HTML로 작성한다. 템플릿·규칙은 html-doc 스킬을 따른다.
>
> **참조 위치**: 글로벌 `~/.claude/CLAUDE.md`에서 이 파일을 한 줄 링크로 가리킨다.
> `/work-plan`, `/work-plan-start`, `/self-review`, `/qa-scenario` 등 `{DOC_DIR}`을 사용하는
> 모든 스킬·커맨드는 이 파일을 단일 진실 출처(SSOT)로 따른다.

## 문서 저장 경로 (`{DOC_DIR}`)

워크플로우 문서는 프로젝트 루트가 아닌 `.claude/` 하위에 저장한다.
**프로젝트 루트에 워크플로우 문서를 생성하지 않는다. git add도 하지 않는다.**

### DOC_DIR 결정 규칙

`{DOC_DIR}`을 사용하는 모든 스킬(`/work-plan`, `/work-plan-start`, `/self-review`, `/qa-scenario` 등)은 아래 규칙을 공통으로 따른다.

1. **`.claude/tracks/index.md`**를 읽어 Active Track 목록을 확인한다.
2. 분기:

| Active Track 수 | 동작 | `{DOC_DIR}` |
|-----------------|------|-------------|
| **0개** | 폴백 | `.claude/docs/` |
| **1개** | 자동 선택 | `.claude/tracks/{track_id}/` |
| **2개+** | AskUserQuestion으로 사용자에게 선택 요청 | `.claude/tracks/{선택된_track_id}/` |

3. **다중 Track 선택 UI** (2개+ 일 때):
   - 각 Track의 `metadata.json`에서 `description`, `current_phase`, `total_phases`를 읽어 표시
   - 형식: `{track_id} — {description} (Phase {current}/{total})`
   - 사용자가 선택한 Track의 디렉토리를 `{DOC_DIR}`로 설정

4. 스킬의 인자(`$ARGUMENTS`)로 경로가 직접 지정된 경우 이 규칙을 건너뛴다.

## 문서 번호 체계

파일명 앞에 `/track-status` 워크플로우 순서에 대응하는 번호를 붙인다.
번호만 보고도 작업 흐름과 문서 역할을 파악할 수 있도록 한다.

| # | 파일명 | 워크플로우 단계 | 설명 |
|---|--------|---------------|------|
| 0 | `0_INDEX.html` | - | 문서 인덱스 (읽는 순서, 요약) |
| 1 | `1_REQ-SNAPSHOT.html` | `/work-plan` | 요구사항 원본 스냅샷 |
| 2 | `2_WORK-SPEC.html` | `/work-plan` | 작업 명세서 |
| 3 | `3_FEATURE-CHECKLIST.html` | `/work-plan` | 기능 체크리스트 (사용자/QA 관점) |
| 4 | `4_PLAN.html` | `/work-plan-start` | Phase별 구현 진행률 |
| 5 | `5_ARCHITECTURE.html` | 구현 완료 | 시스템 아키텍처 |
| 6 | `6_SPEC.html` | 구현 완료 | 기능 명세 |
| 7 | `7_SELF-REVIEW.html` | `/self-review` | 셀프 코드 리뷰 결과 |
| 8 | `8_QA-SCENARIOS.html` | `/qa-scenario` | QA 시나리오 |
| 9+ | `9_*.html` | 기타 | 프로젝트별 추가 문서 |

### 번호 없는 참조 문서

워크플로우 순서와 무관한 참조 문서는 번호 없이 `{DOC_DIR}/`에 저장한다.
`0_INDEX.html`의 "참조 리소스" 섹션에 링크를 추가한다.

| 파일명 | 설명 |
|--------|------|
| `DATABASE.html` | DB 데이터 모델 (테이블 관계, 컬럼 정의, DDL, ER 다이어그램) |

## 문서화 규칙

### 요구사항 스냅샷 (`{DOC_DIR}/*_REQ-SNAPSHOT.html`)
- `/work-plan` 실행 시 원본 req.md를 Track에 보존
- 나중에 "어떤 요구사항이었는지" 추적 가능
- req.md 원본 + 메타데이터 헤더 (Track ID, 원본 경로, 스냅샷 일시)

### 아키텍처 문서 (`{DOC_DIR}/*_ARCHITECTURE.html`)
- 시스템 아키텍처, 데이터 흐름, 핵심 로직을 SVG 다이어그램으로 시각화
- 파일 구조, 스케줄러 타임라인, SQL 쿼리 등 포함
- 관련 이슈 번호 기재

### 기능 명세 문서 (`{DOC_DIR}/*_SPEC.html`)
- 기능 설명, 사용자 인터페이스, 데이터 흐름, 핵심 로직을 SVG 다이어그램으로 시각화
- 관련 이슈 번호 기재

## 다이어그램

- **산출 문서마다 핵심 흐름·구조를 인라인 SVG로 1개 이상 시각화한다 (필수).** 상세 규칙·컴포넌트는 `html-doc` 스킬을 따른다.
- **SVG 직접 생성을 기본으로 사용** (Mermaid보다 우선)
- `svg-diagram` 스킬의 템플릿/팔레트 적용
- 산출 문서가 HTML이므로 SVG는 별도 파일이 아닌 본문에 **인라인 `<svg>`**로 임베드한다
  (자체 완결 단일 파일 원칙). `images/` 외부 참조를 쓰지 않는다.
- DDL 스크립트는 `{DOC_DIR}/sql/` 폴더에 저장, `DATABASE.html`에서 `<a href="sql/xxx.sql">`로 참조
- Mermaid는 빠른 프로토타이핑·확인용으로만 사용
- 문서 간 참조는 번호 접두사 포함 `.html` 파일명으로 링크 (예: `<a href="6_SPEC.html">`)

> **파일 탐색 규칙**: 스킬에서 Track 문서를 찾을 때 하드코딩된 번호·확장자가 아닌
> `*_파일명.*` 패턴 매칭을 사용한다. 전환기에 `.md`/`.html`이 공존하므로
> `*_PLAN.*`, `*_WORK-SPEC.*` 등으로 탐색한다.

## 작성 원칙

### 1. 자기 완결적 문서 (Self-contained)
- 각 문서 맨 위에 **"이 문서는 무엇인가"를 1~2문장으로 명시**
- 다른 문서를 읽지 않아도 이 문서만으로 이해 가능해야 함
- 약어, 프로젝트 고유 용어는 **첫 등장 시 풀네임 + 간단한 설명** 포함

### 2. 도메인 비전문가도 이해 가능
- **배경 지식(Background) 섹션**을 문서 초반에 포함 ("왜 필요한지")
- 비즈니스 용어와 기술 용어를 분리하여 설명
- 다이어그램에 **범례(Legend)** 포함 (기호/색상/화살표 의미 명시)
- 복잡한 흐름은 **단계별 번호**로 순서 명확화

### 3. Track 개요 문서 (0_INDEX.html)
- Track 디렉토리에 문서 2개 이상 시 `0_INDEX.html` 자동 생성/업데이트
- 번호순 문서 목록, 워크플로우 단계, 한 줄 요약 포함

### 4. 문서 간 연결
- 다른 문서 참조 시 상대 경로 링크 + 어떤 내용인지 설명 함께 제공
