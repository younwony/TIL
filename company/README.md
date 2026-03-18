# Claude Code 회사 적용 키트

치트시트 기반 통합 워크플로우, 스킬, 훅, 커맨드, 에이전트를 회사 프로젝트에 적용하기 위한 전체 파일 모음입니다.

---

## 전체 파일 구성

```
company/
├── README.md                                 # 이 문서 (적용 가이드)
├── GLOBAL-CLAUDE.md                          # 글로벌 설정 (~/.claude/CLAUDE.md)
├── CLAUDE.md                                 # 프로젝트 CLAUDE.md 추가 내용
│
└── .claude/
    ├── settings.json                         # Hook 설정
    │
    ├── hooks/                                # 안전장치 스크립트
    │   ├── block-dangerous.sh                # 위험 명령 차단 (PreToolUse)
    │   └── log-edits.sh                      # 편집 로그 기록 (PostToolUse)
    │
    ├── commands/                             # 슬래시 명령 (20개)
    │   ├── handoff.md                        # /handoff — 작업 인수인계
    │   ├── pr.md                             # /pr — PR 자동 생성
    │   ├── review-pr.md                      # /review-pr — 팀 코드 리뷰
    │   ├── self-review.md                    # /self-review — 셀프 리뷰
    │   ├── team-review.md                    # /team-review — 4인 병렬 리뷰
    │   ├── work-plan.md                      # /work-plan — 작업 명세서 생성
    │   ├── work-plan-start.md                # /work-plan-start — 명세서 기반 실행
    │   ├── work-log.md                       # /work-log — Confluence 작업 문서화
    │   ├── work-share.md                     # /work-share — 공유용 Confluence 문서화
    │   ├── today.md                          # /today — 오늘 작업 현황
    │   ├── api-doc.md                        # /api-doc — API 문서 생성
    │   ├── slack-to-jira.md                  # /slack-to-jira — Slack→Jira 이슈
    │   ├── slack-to-confluence.md            # /slack-to-confluence — Slack→Confluence
    │   ├── jira-report.md                    # /jira-report — 스프린트 현황 공유
    │   ├── jira-notify.md                    # /jira-notify — Jira 상태 알림
    │   ├── meeting-notes.md                  # /meeting-notes — 회의록 변환
    │   ├── slack-digest.md                   # /slack-digest — 채널 대화 요약
    │   ├── standup-summary.md                # /standup-summary — 스탠드업 요약
    │   ├── slack-remind.md                   # /slack-remind — 메시지 예약
    │   └── sprint-start-notify.md            # /sprint-start-notify — 스프린트 알림
    │
    ├── agents/                               # 서브 에이전트 (4개)
    │   ├── code-refactor.md                  # 코드 리팩토링 전문
    │   ├── debugger.md                       # 디버깅 전문
    │   ├── test-generator.md                 # 테스트 자동 생성
    │   └── jira-updater.md                   # Jira 이슈 상태 관리
    │
    ├── skills/                               # 자동 트리거 스킬 (4개)
    │   ├── smart-session/SKILL.md            # 통합 워크플로우 오케스트레이터
    │   ├── mermaid-diagram/                  # Mermaid 다이어그램 생성
    │   │   ├── SKILL.md                      # 핵심 워크플로우 (~120줄)
    │   │   └── references/                   # 상세 레퍼런스 (Lazy Loading)
    │   │       ├── syntax-guide.md           # 20가지 다이어그램 유형별 문법
    │   │       └── cli-usage.md              # mmdc CLI 사용법, 테마, 스타일링
    │   ├── svg-diagram/                      # SVG 다이어그램 생성
    │   │   ├── SKILL.md                      # 핵심 워크플로우 (~80줄)
    │   │   └── references/
    │   │       └── templates.md              # 5가지 SVG 템플릿 + 색상 팔레트
    │   └── 3ai-plan/SKILL.md                 # 3-AI 협업 플랜
    │
    └── docs/
        └── CODE-RULES.md                    # 코드 작성 원칙 (Lazy Loading)
```

---

## 적용 순서

### Step 1: 글로벌 설정 (개인 PC에 1회)

```bash
# 백업
cp ~/.claude/CLAUDE.md ~/.claude/CLAUDE.md.bak

# 적용 (상단 안내 블록 제외, "## 설정"부터)
cp GLOBAL-CLAUDE.md ~/.claude/CLAUDE.md
```

### Step 2: 프로젝트에 .claude 디렉토리 복사

```bash
# 회사 프로젝트 루트에서 실행
cp -r .claude/ <회사-프로젝트>/.claude/
```

### Step 3: 프로젝트 CLAUDE.md 업데이트

`CLAUDE.md` 파일의 내용을 회사 프로젝트의 기존 CLAUDE.md에 병합합니다.

### Step 4: 커스터마이징 (아래 가이드 참조)

---

## 카테고리별 상세 가이드

### 1. Hooks — 안전장치

| Hook | 타입 | 역할 |
|------|------|------|
| `block-dangerous.sh` | PreToolUse (Bash) | `rm -rf /`, `git push --force main`, `drop table` 등 40개 위험 패턴 차단 |
| `log-edits.sh` | PostToolUse (Edit/Write) | 마크다운 편집 시 `~/.claude/edit-log.txt`에 로그 기록 |

**settings.json 설정:**

```jsonc
{
  "hooks": {
    "PreToolUse": [{
      "matcher": "Bash",
      "hooks": [{
        "type": "command",
        "command": "bash \"$CLAUDE_PROJECT_DIR/.claude/hooks/block-dangerous.sh\"",
        "timeout": 5
      }]
    }],
    "PostToolUse": [{
      "matcher": "Edit|Write",
      "hooks": [{
        "type": "command",
        "command": "bash \"$CLAUDE_PROJECT_DIR/.claude/hooks/log-edits.sh\"",
        "timeout": 10
      }]
    }]
  }
}
```

**커스터마이징:**
- `block-dangerous.sh`: 회사 환경에 맞는 위험 패턴 추가 (예: 프로덕션 DB 접근 명령)
- `log-edits.sh`: 로그 저장 경로 변경, 필요시 Slack 알림 추가
- `notify-cs-doc.sh`는 TIL 전용이라 **제외함** (필요시 프로젝트 전용 Hook 작성)

---

### 2. Commands — 슬래시 명령 (20개)

#### 핵심 워크플로우 (필수 적용 권장)

| 커맨드 | 호출 | 설명 | 필요 조건 |
|--------|------|------|----------|
| `handoff` | `/handoff` | 작업 인수인계 문서 자동 생성 | 없음 |
| `pr` | `/pr` | PR 자동 생성 (변경 분석 + 리뷰어 선정) | GitHub CLI (`gh`) |
| `review-pr` | `/review-pr {번호}` | 4인 팀 + AI 크로스 리뷰 | GitHub CLI |
| `self-review` | `/self-review` | PR 전 셀프 리뷰 → SELF-REVIEW.md | 없음 |
| `team-review` | `/team-review` | 4인 전문 리뷰어 병렬 실행 | 없음 |
| `work-plan` | `/work-plan` | req.md 기반 작업 명세서 생성 | 없음 |
| `work-plan-start` | `/work-plan-start` | WORK-SPEC.md 기반 실행 | work-plan 선행 |
| `today` | `/today` | 오늘 작업 현황 확인 | 없음 |
| `api-doc` | `/api-doc {대상}` | API 문서 자동 생성 | 없음 |

#### Slack/Jira 연동 (MCP 필요)

| 커맨드 | 호출 | 설명 | 필요 MCP |
|--------|------|------|---------|
| `slack-to-jira` | `/slack-to-jira {URL}` | Slack 스레드 → Jira 이슈 | Slack, Atlassian |
| `slack-to-confluence` | `/slack-to-confluence {URL}` | Slack → Confluence 페이지 | Slack, Atlassian |
| `jira-report` | `/jira-report [#채널]` | 스프린트 현황 Slack 공유 | Slack, Atlassian |
| `jira-notify` | `/jira-notify {이슈키}` | Jira 상태 알림 | Slack, Atlassian |
| `meeting-notes` | `/meeting-notes {URL}` | 회의록 자동 생성 | Slack, Atlassian |
| `slack-digest` | `/slack-digest [#채널]` | 채널 대화 요약 | Slack |
| `standup-summary` | `/standup-summary [#채널]` | 스탠드업 일일 요약 | Slack |
| `slack-remind` | `/slack-remind` | 메시지 예약 발송 | Slack |
| `sprint-start-notify` | `/sprint-start-notify` | 스프린트 시작 알림 | Slack, Atlassian |

#### Confluence 문서화

| 커맨드 | 호출 | 설명 | 필요 MCP |
|--------|------|------|---------|
| `work-log` | `/work-log` | 작업 내용 Confluence 문서화 | Atlassian |
| `work-share` | `/work-share` | 공유용 Confluence 문서화 | Atlassian |

**커스터마이징:**
- `pr.md`: `PR_BASE_BRANCH` 값을 프로젝트 CLAUDE.md에서 설정
- `work-log.md` / `work-share.md`: Confluence 페이지 ID를 회사 스페이스에 맞게 변경
- `jira-report.md`: Jira 프로젝트 키를 회사 프로젝트에 맞게 변경

---

### 3. Agents — 서브 에이전트 (4개)

| 에이전트 | 트리거 | 모델 | 역할 |
|----------|--------|------|------|
| `code-refactor` | "리팩토링", "코드 스멜" | Sonnet | 14개 안티패턴 검사 + 자동 리팩토링 |
| `debugger` | "디버깅", "에러 분석" | Sonnet | 스택 트레이스 추적 → 원인 분석 → 자동 수정 (3회 재시도) |
| `test-generator` | "테스트 생성" | Sonnet | 변경 파일 대상 테스트 생성 + 실행 + 수정 (3회 재시도) |
| `jira-updater` | "Jira 업데이트" | Haiku | 브랜치명에서 이슈 감지 → 상태 전환 + 코멘트 |

**커스터마이징:**
- `code-refactor.md`: 회사 코딩 규칙에 맞는 안티패턴 추가/수정
- `test-generator.md`: 테스트 프레임워크(JUnit5, Mockito 등) 및 패턴 조정
- `debugger.md`: 회사 프로젝트의 주요 에러 타입 추가
- `jira-updater.md`: 이슈 키 형식(TECH-1234 등) 변경

---

### 4. Skills — 자동 트리거 스킬 (4개)

| 스킬 | 트리거 | 설명 |
|------|--------|------|
| `smart-session` | "스마트 세션", "세션 시작", "세션 정리" | 치트시트 9기법 통합 워크플로우 (Phase 1→2→3) |
| `mermaid-diagram` | "다이어그램", "흐름도", "시퀀스", "ER" 등 | Mermaid CLI로 20가지 다이어그램 생성 |
| `svg-diagram` | "구조도", "패킷 구조", "계층 다이어그램" 등 | 정밀 레이아웃이 필요한 SVG 다이어그램 |
| `3ai-plan` | "3AI 플랜", "3AI 협업", "멀티 AI 리뷰" | Claude+Gemini+Codex 3-AI 협업 플랜 생성 |

#### 스킬 구조 (Progressive Disclosure)

스킬은 3단계 로딩 구조를 사용합니다:

1. **Metadata** (name + description) — 항상 컨텍스트에 로드 (~100 words)
2. **SKILL.md 본문** — 스킬 트리거 시 로드 (500줄 이내)
3. **references/** — 필요 시 Read 도구로 로드 (무제한)

이 구조로 불필요한 토큰 소비를 줄이면서도, 상세 레퍼런스가 필요할 때는 references/에서 가져옵니다.

| 스킬 | SKILL.md | references/ | 설명 |
|------|----------|-------------|------|
| `mermaid-diagram` | ~120줄 | syntax-guide.md, cli-usage.md | 20개 다이어그램 문법 + CLI 사용법 분리 |
| `svg-diagram` | ~80줄 | templates.md | 5가지 SVG 템플릿 + 색상 팔레트 분리 |
| `smart-session` | ~180줄 | — | 3 Phase 워크플로우 전체 포함 |
| `3ai-plan` | ~130줄 | — | Gemini/Codex 실행 가이드 전체 포함 |

#### 개선 내역 (v2, 2026-03-18)

| 항목 | 변경 전 | 변경 후 |
|------|---------|---------|
| **description** | 1줄 요약 | 2-3줄, 트리거 상황/키워드/사용 맥락 포함 |
| **트리거 키워드 섹션** | 본문에 중복 나열 | 삭제 (description이 유일한 트리거) |
| **WHY 설명** | MUST/NEVER 대문자 남발 | 이유 기반 설명으로 전환 |
| **mermaid-diagram** | 840줄 (한 파일) | 120줄 + references/ 2개 파일 |
| **svg-diagram** | 300줄 (한 파일) | 80줄 + references/ 1개 파일 |
| **smart-session** | Phase별 WHY 없음 | 각 Phase에 WHY 1줄씩 추가 |
| **3ai-plan** | WHY 없음 | "왜 3-AI 협업인가?" 섹션 추가 |

**제외된 스킬 (TIL 전용):**
- `cs-guide-writer`, `cs-sync`, `cs-link-sync` — CS 문서 작성용
- `pencil-screen`, `pencil-update`, `pencil-to-code` — Pencil MCP 전용
- `browser-debug` — Chrome in Claude 디버깅용

---

### 5. Docs — 참조 문서

| 문서 | 용도 | Lazy Loading |
|------|------|-------------|
| `CODE-RULES.md` | 코드 작성 원칙 상세 (TDD, 체크리스트, 스킬 생성 규칙) | 코드 작업 시에만 로드 |

---

## 필요 외부 도구

| 도구 | 용도 | 필수 여부 |
|------|------|----------|
| GitHub CLI (`gh`) | PR 생성/리뷰 | `/pr`, `/review-pr` 사용 시 |
| Slack MCP | Slack 읽기/쓰기 | Slack 관련 커맨드 사용 시 |
| Atlassian MCP | Jira/Confluence 연동 | Jira/Confluence 커맨드 사용 시 |
| Gemini CLI (`gemini`) | 크로스 리뷰 | `/review-pr`, `/self-review` 선택 기능 |
| Codex CLI (`codex`) | 크로스 리뷰 | `/review-pr`, `/self-review` 선택 기능 |

---

## 단계별 적용 전략 (권장)

### Phase 1: 기본 (1일차)

```
적용 대상:
├── GLOBAL-CLAUDE.md          → ~/.claude/CLAUDE.md
├── .claude/hooks/             → 안전장치 (block-dangerous, log-edits)
├── .claude/settings.json      → Hook 연결
├── .claude/docs/CODE-RULES.md → Lazy Loading 참조
└── .claude/skills/smart-session/ → 세션 관리
```

### Phase 2: 개발 워크플로우 (2~3일차)

```
적용 대상:
├── /handoff, /today           → 기본 세션 관리
├── /work-plan, /work-plan-start → 설계 우선 워크플로우
├── /pr, /self-review          → PR 자동화
├── /team-review               → 팀 코드 리뷰
└── agents/ (4개)              → 자동 리팩토링/디버깅/테스트
```

### Phase 3: Slack/Jira 연동 (1주차)

```
적용 대상 (MCP 설정 필요):
├── /slack-to-jira             → Slack에서 이슈 생성
├── /jira-report               → 스프린트 현황 공유
├── /meeting-notes             → 회의록 자동화
├── /work-log, /work-share     → Confluence 문서화
└── 나머지 Slack/Jira 커맨드
```

### Phase 4: 고급 (2주차~)

```
적용 대상:
├── /review-pr + Gemini/Codex  → 크로스 AI 리뷰
├── 3ai-plan 스킬              → 3-AI 협업 플랜
└── 커스텀 Hook/에이전트 추가
```

---

## 팀 공유 시 주의사항

1. **settings.json**: Hook 경로가 `$CLAUDE_PROJECT_DIR` 기반이므로 팀원 모두 동일 동작
2. **GLOBAL-CLAUDE.md**: 각 팀원이 개인 PC에 직접 적용해야 함 (git으로 공유 불가)
3. **MCP 설정**: Slack/Atlassian MCP는 각 팀원이 개별 인증 필요
4. **Confluence 페이지 ID**: `work-log.md`, `work-share.md`의 페이지 ID를 팀 스페이스에 맞게 변경
5. **.gitignore**: `settings.local.json`은 개인 설정이므로 `.gitignore`에 추가 권장
