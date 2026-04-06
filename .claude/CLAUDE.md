# 설정

## 언어

- **모든 응답은 한글로 작성**하고, 설명도 한글로 진행
  - 코드, 명령어, 기술 용어 등 고유명사는 원어 그대로 사용 가능
  - 커밋 메시지, 주석, 변수명 등 코드 내부는 기존 규칙을 따름
  - 질문, 설명, 안내, 요약 등 사용자와의 모든 대화는 한글로 응답

## Git

- **모든 작업에서 git add까지만 진행**하고 commit은 하지 않음
  - 새로 생성된 파일: git add만
  - 수정된 파일: git add만
  - 삭제된 파일: git add만
  - 사용자가 명시적으로 commit을 요청할 때만 commit 실행
- **`.claude/` 디렉토리는 git add 하지 않음**
  - 신규 생성, 수정, 삭제 등 어떤 변경이든 `.claude/` 하위 파일은 staging 제외
  - 워크플로우 문서, skills, commands, settings 등 모두 해당

## Plan Mode

- **모든 비단순 작업은 플랜 모드(Plan Mode)로 진행**
  - 사용자 요청, 스킬 실행, 커맨드 실행 등 구현이 필요한 작업은 반드시 플랜 모드로 시작
  - 플랜 모드에서 충분히 탐색·분석한 후 실행 계획을 수립하고 승인받은 뒤 구현
  - 단순 질문 응답, 파일 읽기, 간단한 수정(오타, 1-2줄 변경)은 플랜 모드 불필요
  - 플랜 수립 시 **반드시 충분한 thinking을 거쳐** 요구사항 분석, 영향 범위 파악, 접근 방법 비교를 진행
  - 플랜에는 실행 순서, 대상 파일, 변경 내용, 검증 방법을 명확히 포함

# 작업 처리 워크플로우

모든 비단순 요청은 아래 단계를 순서대로 수행한다.

## Step 1: 요구사항 파악
- 사용자 요청을 정확히 이해
- 모호한 부분은 질문으로 명확화
- 관련 파일/설정/문서 확인

## Step 2: 리서치 & 탐색
- 기존 코드/설정/문서에서 관련 내용 검색
- 외부 리소스 필요 시 웹 검색 수행
- 기존 패턴과 컨벤션 파악

## Step 3: 플랜 수립
- 실행 순서, 대상 파일, 변경 내용 정리
- 영향 범위와 리스크 분석
- 대안 비교 후 최적 방안 선택

## Step 4: 구현
- 플랜 승인 후 순서대로 실행
- 각 단계 완료 시 중간 결과 확인
- 문제 발생 시 즉시 보고 및 대안 제시

## Step 5: 검증 & 완료
- 변경 사항 동작 확인 (테스트/실행)
- Java 프로젝트: 테스트 커버리지 체크 (`test-coverage-check` 스킬) 수행
- git add로 staging
- 결과 요약 보고

## 실전 팁

- **CLAUDE.md 지속 관리**: Claude가 잘못된 행동을 할 때마다 해당 규칙을 CLAUDE.md에 추가하여 같은 실수 반복 방지
- **검증 피드백 루프 필수**: 모든 변경 후 반드시 검증 방법 제시 (테스트 실행, 동작 확인, 빌드 체크)
- **서브에이전트 적극 활용**: 독립적인 작업은 서브에이전트에 위임하여 메인 컨텍스트 보존
- **"Don't implement yet" 원칙**: 복잡한 작업은 구현 전 계획 완성도를 충분히 높인 후 실행

# 세션 & 컨텍스트 관리

- **한 세션 = 한 피처**: 하나의 대화에서 하나의 기능/작업만 완료
- **에러는 원본 그대로**: 에러 로그/스택 트레이스는 가공 없이 전문 기반 분석
- **가정 변경 알림**: 기술 스택이나 아키텍처 가정이 변경될 때 사용자에게 먼저 확인
- **/compact 타이밍**: 응답이 느려지면 `/compact` 실행, 85% 초과 시 `/clear`
- **스크립트 오프로드**: 대량 파일 변환, 포맷팅 등 반복 작업은 셸 스크립트로 분리
- **세션 시작 시**: MEMORY.md, TODO.md, HANDOFF.md가 있으면 읽고 맥락 파악 후 시작

## 메모리 에이징

- MEMORY.md, HANDOFF.md, TODO.md 등 메모리 파일을 읽을 때, 파일의 수정 시각(mtime)을 확인
- **2일 이상 경과**된 파일의 정보는 "이 정보는 N일 전 것" 으로 인지하고 현재 사실과 다를 수 있음을 고려
- 특히 **파일:줄 번호 인용**, **브랜치 상태**, **작업 진행률** 등은 반드시 현재 코드/git 상태와 대조 후 사용
- ISO 날짜보다 "N일 전" 형태로 경과 시간을 인지하면 staleness 판단이 더 정확함

## 커맨드 사전 조건

아래 커맨드는 해당 조건이 충족될 때만 사용한다. 조건이 안 맞으면 사용자에게 안내하고 실행하지 않는다.

| 커맨드 그룹 | 사전 조건 | 해당 커맨드 |
|------------|----------|------------|
| Slack 연동 | Slack MCP 연결 필요 | `slack-to-jira`, `slack-to-confluence`, `slack-digest`, `slack-remind`, `standup-summary`, `meeting-notes`, `sprint-start-notify` |
| Jira 연동 | Jira MCP 연결 필요 | `jira-report`, `jira-notify` |
| Confluence 연동 | `ATLASSIAN_API_TOKEN` 환경변수 필요 | `work-log`, `work-share`, `slack-to-confluence`, `meeting-notes` |
| Figma 연동 | Figma MCP 연결 필요 | `figma-read` |
| 브라우저 QA | Chrome MCP 또는 Playwright 설치 필요 | `browser-debug`, `browser-debug-chrome` |

# 테스트

테스트 코드는 반드시 작성한다.
단위 테스트, 통합테스트 포함.

# 워크플로우 문서 시스템

> 문서 경로, 번호 체계, 작성 규칙, 다이어그램 설정을 한 곳에서 관리한다.
> 변경 시 이 섹션만 수정하면 된다.

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
| 0 | `0_INDEX.md` | - | 문서 인덱스 (읽는 순서, 요약) |
| 1 | `1_REQ-SNAPSHOT.md` | `/work-plan` | 요구사항 원본 스냅샷 |
| 2 | `2_WORK-SPEC.md` | `/work-plan` | 작업 명세서 |
| 3 | `3_FEATURE-CHECKLIST.md` | `/work-plan` | 기능 체크리스트 (사용자/QA 관점) |
| 4 | `4_PLAN.md` | `/work-plan-start` | Phase별 구현 진행률 |
| 5 | `5_ARCHITECTURE.md` | 구현 완료 | 시스템 아키텍처 |
| 6 | `6_SPEC.md` | 구현 완료 | 기능 명세 |
| 7 | `7_SELF-REVIEW.md` | `/self-review` | 셀프 코드 리뷰 결과 |
| 8 | `8_QA-SCENARIOS.md` | `/qa-scenario` | QA 시나리오 |
| 9+ | `9_*.md` | 기타 | 프로젝트별 추가 문서 |

### 번호 없는 참조 문서

워크플로우 순서와 무관한 참조 문서는 번호 없이 `{DOC_DIR}/`에 저장한다.
`0_INDEX.md`의 "참조 리소스" 섹션에 링크를 추가한다.

| 파일명 | 설명 |
|--------|------|
| `DATABASE.md` | DB 데이터 모델 (테이블 관계, 컬럼 정의, DDL, ER 다이어그램) |

## 문서화 규칙

### 요구사항 스냅샷 (`{DOC_DIR}/*_REQ-SNAPSHOT.md`)
- `/work-plan` 실행 시 원본 req.md를 Track에 보존
- 나중에 "어떤 요구사항이었는지" 추적 가능
- req.md 원본 + 메타데이터 헤더 (Track ID, 원본 경로, 스냅샷 일시)

### 아키텍처 문서 (`{DOC_DIR}/*_ARCHITECTURE.md`)
- 시스템 아키텍처, 데이터 흐름, 핵심 로직을 SVG 다이어그램으로 시각화
- 파일 구조, 스케줄러 타임라인, SQL 쿼리 등 포함
- 관련 이슈 번호 기재

### 기능 명세 문서 (`{DOC_DIR}/*_SPEC.md`)
- 기능 설명, 사용자 인터페이스, 데이터 흐름, 핵심 로직을 SVG 다이어그램으로 시각화
- 관련 이슈 번호 기재

## 다이어그램

- **SVG 직접 생성을 기본으로 사용** (Mermaid보다 우선)
- `svg-diagram` 스킬의 템플릿/팔레트 적용 (그라디언트, 드롭섀도우 등)
- `{DOC_DIR}/images/` 폴더에 저장, 마크다운에서 `![](images/xxx.svg)`로 참조
- DDL 스크립트는 `{DOC_DIR}/sql/` 폴더에 저장, `DATABASE.md`에서 `[sql/xxx.sql](sql/xxx.sql)`로 참조
- Mermaid는 빠른 프로토타이핑이나 간단한 확인용으로만 사용
- 문서 간 참조는 번호 접두사 포함한 파일명으로 링크 (예: `[*_SPEC.md](*_SPEC.md)`)

> **파일 탐색 규칙**: 스킬에서 Track 문서를 찾을 때 하드코딩된 번호가 아닌 `*_파일명.md` 패턴 매칭을 사용한다. 번호는 언제든 변경될 수 있으므로 `*_PLAN.md`, `*_WORK-SPEC.md` 등으로 탐색한다.

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

### 3. Track 개요 문서 (0_INDEX.md)
- Track 디렉토리에 문서 2개 이상 시 `0_INDEX.md` 자동 생성/업데이트
- 번호순 문서 목록, 워크플로우 단계, 한 줄 요약 포함

### 4. 문서 간 연결
- 다른 문서 참조 시 상대 경로 링크 + 어떤 내용인지 설명 함께 제공

# AI 코딩 보안

AI 코딩 도구 사용 시 반드시 지켜야 할 보안 원칙이다.

## 신뢰할 수 없는 소스 주의
- `git clone` 전 저장소의 CLAUDE.md, .cursorrules, AGENTS.md 등 컨텍스트 파일 확인
- 알 수 없는 출처의 MCP 서버 연결 금지
- 외부 PR/이슈의 코드를 그대로 실행하지 않음

## AI 생성 코드 보안 리뷰
- AI 생성 코드도 반드시 OWASP Top 10 관점 보안 리뷰
- 특히 외부 입력 처리, 인증/인가, 파일 I/O, SQL 쿼리 주의
- API 키, 시크릿, 자격 증명이 코드에 포함되지 않았는지 확인

## 권한 최소화
- AI에게 필요 최소한의 파일/디렉토리 접근만 허용
- 프로덕션 환경 접근 금지
- `.env`, `credentials.json` 등 민감 파일 커밋 방지

# Codex 협업

## Codex Plugin (우선)

Codex Plugin이 설치된 경우 네이티브 슬래시 커맨드로 사용한다. Bash CLI보다 우선 사용한다.

### 설치

```
/plugin marketplace add openai/codex-plugin-cc
/plugin install codex@openai-codex
/reload-plugins
/codex:setup
```

### 커맨드 매핑 (Plugin 우선, CLI fallback)

| 용도 | Plugin (우선) | CLI fallback |
|------|--------------|-------------|
| 코드 리뷰 | `/codex:review --base main` | `codex review --base main` |
| 적대적 리뷰 | `/codex:review --base main` | 없음 |
| 작업 위임 | `/codex:rescue 자연어 설명` | `codex exec -` |
| 작업 상태 | `/codex:status` | 없음 |
| 작업 결과 | `/codex:result` | 없음 |
| Review Gate | `/codex:setup --enable-review-gate` | 없음 |

### 적용 원칙

- **Plugin 우선**: Plugin이 설치되어 있으면 항상 Plugin 방식을 사용
- **Bash fallback**: Plugin 미설치 또는 실행 실패 시 기존 Bash CLI로 폴백
- **리뷰 시 adversarial-review 권장**: 일반 review보다 설계 결정, 가정, 실패 모드까지 검증
- **Review Gate 주의**: 토큰 소비가 크므로 핵심 로직 구현 시에만 선택적 활성화

## Codex CLI (fallback)

Plugin 미설치 시 Bash에서 직접 실행한다.

```bash
# 기본 실행 (기본 모델: gpt-5-codex)
codex "분석할 내용"

# 모델 지정
codex "분석할 내용" --model o3

# 파일 참조 (@ 문법)
codex "@src/main/java/Foo.java 이 코드 분석해줘"

# 파이프 입력
cat file.yaml | codex "이 스펙 분석해줘"
```

## 활용 상황

- **코드 생성/수정**: 복잡한 코드 생성 시 Codex에 위임 또는 협업
- **아이디어 필요**: 브레인스토밍, 대안 제시가 필요할 때
- **검증 필요**: 기술적 정확성 확인이 필요할 때
- **복잡한 작업**: Claude 설계 + Codex 구현 협업

## 협업 원칙

- **Plugin 우선**, CLI는 fallback으로만 사용
- 필요 시 **자체 판단**으로 Codex 활용 (모델 고정 X)
- Codex 연결 실패 시 Claude 단독으로 진행
- 결과는 Claude가 검토 후 최종 정리

# 글로벌 Skills

모든 프로젝트에서 사용 가능한 스킬 목록입니다.

| 스킬 | 호출 | 설명 |
|------|------|------|
| `work-plan` | `/work-plan`, `/work-plan path/to/req.md` | req.md 기반 WORK-SPEC.md 작업 명세서 + FEATURE-CHECKLIST.md 생성 |
| `work-plan-start` | `/work-plan-start` | WORK-SPEC.md 기반 실제 작업 수행 |
| `work-log` | `/work-log`, `/work-log --parent <pageId\|제목>` | 현재 브랜치 작업 내용 Confluence 문서화 (기본: WORK-LOG 하위) |
| `work-share` | `/work-share` | 현재 브랜치 작업 내용 공유 페이지 하위에 Confluence 문서화 |
| `pr` | `/pr` | 현재 브랜치 분석 후 PR 자동 생성 |
| `review-pr` | `/review-pr {PR번호}` | PR 코드 리뷰 및 개선 제안 |
| `self-review` | `/self-review` | PR 생성 전 자체 리뷰 및 SELF-REVIEW.md 생성 |
| `slack-to-jira` | `/slack-to-jira {Slack URL}` | Slack 스레드 읽어 Jira 이슈 자동 생성 |
| `jira-report` | `/jira-report [#채널]` | 현재 스프린트 현황을 Slack 채널에 공유 |
| `slack-to-confluence` | `/slack-to-confluence {Slack URL}` | Slack 스레드를 Confluence 페이지로 변환 |
| `meeting-notes` | `/meeting-notes {Slack URL}` | Slack 스레드를 회의록으로 변환 (Confluence 저장) |
| `slack-digest` | `/slack-digest [#채널]` | 특정 채널의 최근 대화 요약 |
| `standup-summary` | `/standup-summary [#채널]` | 스탠드업 채널 일일 요약 |
| `slack-remind` | `/slack-remind [대상] [시간] [메시지]` | 특정 시간에 Slack 메시지 예약 발송 |
| `sprint-start-notify` | `/sprint-start-notify [#채널]` | 스프린트 시작 시 팀 채널에 할당 이슈 공유 |
| `jira-notify` | `/jira-notify {이슈키} [#채널]` | Jira 이슈 상태를 Slack 채널에 알림 |
| `qa-scenario` | `/qa-scenario`, "QA 시나리오", "테스트 시나리오", "변경 영향 분석" | 변경사항 분석 → 영향도 매트릭스 → BDD QA 시나리오 문서 생성 |
| `browser-debug` | `/browser-debug`, "브라우저 QA", "웹 QA", "크롬 디버깅", "Playwright QA" | 2-Layer QA: Playwright 1차 검증 → FAIL건만 Chrome 정밀 디버깅 |
| `browser-debug-chrome` | `/browser-debug-chrome`, "Chrome-only QA" | Chrome MCP만 사용하는 레거시 QA (백업) |
| `figma-read` | `/figma-read {Figma URL}` | Figma URL 디자인을 figma-team MCP로 읽기 (개인계정 MCP 사용 안함) |
| `pencil-screen` | "새 화면 디자인", "스크린 추가", "화면 만들어줘" | Pencil MCP로 새 화면(Desktop+Mobile) 디자인 |
| `pencil-to-code` | "코드 생성", "디자인 적용", "코드로 변환" | Pencil 디자인을 HTML/CSS/JS 코드로 변환 |
| `pencil-update` | "디자인 수정", "화면 변경", "레이아웃 조정" | Pencil MCP 기존 화면 수정 |
| `test-coverage-check` | "테스트 커버리지", "커버리지 체크", "테스트 점검" | 변경 파일 커버리지 분석 + 누락 테스트 자동 생성 |
| `docker-up` | `/docker-up`, "도커 띄워줘", "도커 실행", "컨테이너 시작" | Docker Desktop 체크/시작 + docker-compose 실행/업데이트 자동화 |
| `feature-check` | `/feature-check`, "기능 체크", "기능 검증", "구현 확인" | FEATURE-CHECKLIST.md 기반 코드 레벨 기능 구현 자동 검증 |
| `skill-rebuild` | `/skill-rebuild {스킬명}`, "스킬 재구성", "스킬 리빌드" | 기존 스킬 개선 시 전체 파이프라인(evals, 벤치마크, description 최적화) 강제 |
| `track-status` | `/track-status [track_id]` | Track 작업 추적 현황 조회 (전체/상세) |
| `sync-global` | `/sync-global push\|pull\|status` | 글로벌 Claude 설정(skills, commands, hooks, rules, agents)을 프로젝트와 동기화 |

## 글로벌 Agents (Sub-agents)

독립 컨텍스트에서 자율적으로 작업을 수행하는 전문 에이전트입니다.

| 에이전트 | 트리거 키워드 | 모델 | 설명 |
|----------|--------------|------|------|
| `test-generator` | "테스트 생성", "테스트 작성" | Sonnet | 단위/통합 테스트 자동 생성 + 실행 + 수정 |
| `code-refactor` | "리팩토링", "코드 스멜" | Sonnet | CLAUDE.md 규칙 기반 코드 분석 + 개선 |
| `debugger` | "디버깅", "에러 분석" | Sonnet | 스택 트레이스 추적 + 원인 분석 + 수정 |
| `jira-updater` | "Jira 업데이트", "이슈 상태" | Haiku | 브랜치 기반 이슈 감지 + 상태 전환 + 코멘트 |

## 일반 요청 병렬 에이전트 디스패치

WORK-SPEC.md 없이도, 아래 패턴의 요청 시 에이전트를 병렬 디스패치한다.

### 구현 요청 ("구현해줘", "작업해줘", "추가해줘", "변경해줘")

변경 파일 3개 이상 예상 시:
```
[Main]            코드 구현 (foreground)
[test-generator]  테스트 자동 생성 (background, Main 구현 시작 후 디스패치)
```

### 리팩토링 요청 ("리팩토링해줘", "정리해줘", "코드 개선")

```
[code-refactor]   CLAUDE.md 규칙 기반 분석 → 개선안 도출 (foreground)
[Main]            분석 결과 기반 수정 적용
```

### 버그 수정 요청 ("버그 수정", "에러 수정", "안돼", "동작 안함")

```
[debugger]        스택 트레이스/로그 분석 (foreground, 결과 대기)
[Main]            분석 결과 기반 수정
[test-generator]  수정 후 회귀 테스트 생성 (background)
```

### 적용 조건

- **파일 1~2개 수정**: Main 단독 (에이전트 오버헤드 > 이득)
- **파일 3개+**: 위 패턴 적용
- **단순 삭제/이름 변경**: Main 단독 (Grep/Edit으로 즉시 처리)

### 에이전트 에러 처리 (Withhold-then-Recover)

에이전트 실패 시 **즉시 에러를 전파하지 않고, 보류(withhold) 후 자동 복구를 시도**한다.

| 시도 | 행동 |
|------|------|
| 1차 실패 | 에러 메시지를 분석하여 에이전트에게 수정 지시 (SendMessage) |
| 2차 실패 | 다른 접근 방식으로 재시도 |
| 3차 실패 | Main이 해당 작업을 직접 수행 |

- 컴파일 에러, 테스트 실패 등 **수정 가능한 에러**는 에이전트에게 재시도 기회를 준다
- 네트워크 에러, 권한 에러 등 **환경 문제**는 즉시 Main으로 전환
- 에이전트가 3회 실패 후에도 해결 못하면 사용자에게 보고

## 팀 에이전트 워크플로우

`/work-plan-start` 실행 시 변경 파일 수에 따라 실행 모드를 선택한다.

### 모드 선택

| 예상 변경 파일 수 | 모드 | Main 역할 |
|------------------|------|----------|
| 1~2개 | **Solo** | Main 단독 처리. 에이전트 오버헤드 불필요 |
| 3~4개 | **Standard** | Main이 조율 + 구현 + 문서화 |
| 5개+ | **Coordinator** | Main은 순수 조율자. 구현을 워커에 위임 |

> 각 모드의 상세 Phase 구성, 에이전트별 입력, 워커 분배 규칙은 `work-plan-start` 스킬(SKILL.md) 참조.

### 공통 디스패치 규칙

- Phase 간 의존성이 있으므로 Phase 순서는 반드시 순차 실행
- 각 Phase 내 에이전트는 최대한 병렬 실행
- WORK-SPEC.md가 존재할 때만 팀 워크플로우 적용

## 외부 도구 에러 타입 판별

Codex, Gemini, MCP 등 외부 도구 실패 시 에러 유형에 따라 다르게 처리한다.

| 에러 유형 | 판별 기준 | 처리 방법 |
|----------|----------|----------|
| **네트워크 에러** | timeout, connection refused, ECONNRESET | 5초 대기 후 1회 재시도 → 실패 시 스킵 |
| **인증 에러** | 401, 403, unauthorized, forbidden | 재시도 없이 즉시 사용자에게 안내 ("API 토큰 확인 필요") |
| **타임아웃** | 120초+ 무응답 | 대기 시간을 늘려 1회 재시도 (timeout: 180000ms) |
| **Rate Limit** | 429, rate limit, too many requests | 30초 대기 후 재시도 |
| **모델/서비스 에러** | 500, 502, 503, internal server error | 1회 재시도 → 실패 시 대체 도구로 전환 (Codex↔Gemini) |
| **입력 에러** | 400, invalid input, schema validation | 입력을 수정하여 재시도 (프롬프트 축소, 형식 변경) |

- `CODEX_FAIL`, `GEMINI_FAIL` 같은 단순 문자열 대신 위 분류에 따라 분기
- Codex/Gemini 둘 다 실패 시 "외부 크로스 체크 없이 Claude 단독으로 진행합니다" 안내

## 리뷰 에이전트 컨텍스트 최적화

리뷰 에이전트 4명이 동일한 diff를 반복 탐색하지 않도록, Main이 요약 컨텍스트를 한 번 생성하여 전달한다.

> 상세 절차는 각 커맨드(`self-review.md`, `review-pr.md`, `team-review.md`)의 2단계 참조.

## PR 설정

각 프로젝트의 CLAUDE.md에서 PR base branch를 설정할 수 있습니다.

```
PR_BASE_BRANCH: main-review
```

설정이 없으면 기본값 `main`을 사용합니다.

### 리뷰어 제외 목록

리뷰어 랜덤 선정 시 아래 계정은 항상 후보에서 제외합니다.

```
PR_REVIEWER_EXCLUDE: temcolabs, happyfridaycode
```

# Confluence 설정

작업 로그를 Confluence에 업로드할 때 사용하는 설정입니다.

| 항목 | 값 |
|------|------|
| **사이트 URL** | `https://temcolabs.atlassian.net` |
| **이메일** | `wonhee.youn@temco.io` |
| **개인 스페이스 키** | `~645023757` |
| **개인 스페이스 ID** | `1983741954` |
| **홈페이지 ID** | `1983742135` |
| **API 인증** | Basic Auth (이메일 + API 토큰) |

- API 토큰은 환경변수 `ATLASSIAN_API_TOKEN`으로 관리하거나 MCP 설정에서 참조
- Confluence REST API v2 엔드포인트: `{사이트URL}/wiki/api/v2/`

### Atlassian API 우선순위

**curl REST API를 기본으로 사용**한다. MCP는 실패율이 높아 폴백으로만 사용한다.

| 우선순위 | 방법 | 사용 조건 |
|---------|------|----------|
| **1순위** | `curl` REST API (Basic Auth) | `ATLASSIAN_API_TOKEN` 환경변수 존재 시 항상 |
| **2순위 (폴백)** | `mcp__atlassian__*` MCP 도구 | curl 실패 시 또는 MCP 전용 기능 필요 시 |

```bash
# curl 기본 사용 패턴
curl -s -u "wonhee.youn@temco.io:$ATLASSIAN_API_TOKEN" \
  -H "Content-Type: application/json" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

- MCP 먼저 시도하지 않는다. 바로 curl로 요청한다.
- curl 실패 시에만 MCP 폴백을 시도한다.