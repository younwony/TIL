# Claude Code 코드 리뷰 명령어 가이드

> `[3] 중급` · 선수 지식: [Slash Command](./claude-code-slash-command.md), [Skill](./claude-code-skill.md)

> Claude Code의 `/self-review`, `/review-pr`, `/pr` 슬래시 명령어를 활용한 코드 리뷰 자동화 워크플로우

`#ClaudeCode` `#CodeReview` `#SelfReview` `#PR` `#SlashCommand` `#GeminiCLI` `#CodexCLI` `#CrossReview` `#멀티에이전트리뷰` `#자동화` `#워크플로우` `#ghCLI` `#GitHubPR`

## 왜 알아야 하는가?

- **실무**: PR 생성 전 자체 코드 리뷰를 자동화하여 리뷰 품질 향상 및 리뷰어 부담 감소
- **면접**: AI 에이전트를 활용한 코드 리뷰 자동화 파이프라인 구축 역량 증명
- **기반 지식**: Claude + Gemini + Codex 멀티 에이전트 크로스 리뷰 패턴의 실전 구현

## 핵심 개념

- **`/self-review`**: PR 생성 전 현재 브랜치의 변경사항을 자체 리뷰하고 `SELF-REVIEW.md` 문서로 생성
- **`/review-pr`**: 이미 생성된 특정 PR에 대해 심층 코드 리뷰를 수행
- **`/pr`**: 현재 브랜치의 변경사항을 분석하여 GitHub PR을 자동 생성
- **크로스 리뷰(Cross Review)**: Claude의 리뷰에 Gemini CLI, Codex CLI의 리뷰를 추가하여 다각도 분석

## 전체 워크플로우

```
┌──────────────────────────────────────────────────────────────────┐
│                    코드 리뷰 자동화 워크플로우                       │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│   작업 브랜치에서 코드 작성 완료                                    │
│           │                                                       │
│           ▼                                                       │
│   ┌─────────────────┐                                            │
│   │  /self-review    │ ─── PR 생성 전 자체 리뷰                   │
│   │                  │     → SELF-REVIEW.md 생성                  │
│   └────────┬────────┘                                            │
│            │                                                      │
│            ├─── 필수 수정 사항 있음 → 코드 수정 후 재실행           │
│            │                                                      │
│            ▼                                                      │
│   ┌─────────────────┐                                            │
│   │  /pr             │ ─── PR 자동 생성                           │
│   │                  │     → GitHub PR 생성 + 리뷰어 배정          │
│   └────────┬────────┘                                            │
│            │                                                      │
│            ▼                                                      │
│   ┌─────────────────┐                                            │
│   │  /review-pr 123  │ ─── 생성된 PR 심층 리뷰 (선택)             │
│   │                  │     → 리뷰 결과 출력 + 코멘트 등록          │
│   └─────────────────┘                                            │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

## 사전 요구사항

### 필수 도구

| 도구 | 용도 | 설치 확인 | 설치 방법 |
|------|------|----------|----------|
| **Claude Code** | 슬래시 명령어 실행 환경 | `claude --version` | [공식 문서](https://docs.anthropic.com/en/docs/claude-code) |
| **GitHub CLI (gh)** | PR 생성/조회, diff 수집 | `gh --version` | `winget install GitHub.cli` |

```bash
# GitHub CLI 인증 (최초 1회)
gh auth login
```

### 선택 도구 (크로스 리뷰용)

| 도구 | 용도 | 설치 확인 | 설치 방법 |
|------|------|----------|----------|
| **Gemini CLI** | Google Gemini 크로스 리뷰 | `where gemini` | `npm install -g @anthropic-ai/gemini-cli` |
| **Codex CLI** | OpenAI Codex 크로스 리뷰 | `where codex` | `npm install -g @openai/codex` |

```bash
# Gemini CLI 인증
gemini auth

# Codex CLI 인증
codex auth
```

> 크로스 리뷰 도구가 없어도 Claude 단독 리뷰로 정상 동작합니다. 설치된 도구만 자동으로 활성화됩니다.

### 프로젝트 설정

프로젝트 루트의 `CLAUDE.md`에 PR 설정을 추가합니다.

```markdown
# CLAUDE.md (프로젝트 루트)

## PR 설정

PR_BASE_BRANCH: main-review
```

| 설정 | 설명 | 기본값 |
|------|------|--------|
| `PR_BASE_BRANCH` | PR을 생성할 대상 브랜치 | `main` |
| `PR_REVIEWER_EXCLUDE` | 리뷰어 후보에서 제외할 계정 | - |

---

## /self-review — 셀프 코드 리뷰

### 개요

PR 생성 전 현재 브랜치의 변경사항을 자체 분석하여 `SELF-REVIEW.md` 문서를 생성합니다.

### 사용법

```
/self-review
```

> 인자 없이 실행합니다. 현재 체크아웃된 브랜치를 자동으로 감지합니다.

### 실행 조건

- **작업 브랜치에서만 실행 가능** (main, master에서는 실행 불가)
- 비교 기준 브랜치는 자동 감지 (main → master → 사용자 입력 순)

### 실행 흐름

```
/self-review 실행
    │
    ├── 1. 사전 확인
    │       - 현재 브랜치가 작업 브랜치인지 확인
    │       - Gemini CLI / Codex CLI 설치 여부 확인
    │       - 비교 기준 브랜치 결정 (main → master)
    │
    ├── 2. 변경사항 수집 (병렬 실행)
    │       - git branch --show-current
    │       - git log {base}..HEAD --oneline
    │       - git diff {base}...HEAD --stat
    │       - git diff {base}...HEAD
    │       - git status
    │
    ├── 3. 전체 개요 분석
    │       - 브랜치 목적 파악
    │       - 주요 변경 파일 정리
    │       - 커밋 히스토리 요약
    │
    ├── 4. 파일별 심층 리뷰
    │       - 코드 품질 (네이밍, SRP, 중복, 매직넘버)
    │       - 보안 (OWASP Top 10, 입력 검증, 민감정보)
    │       - 성능 (N+1 쿼리, 고비용 객체, 컬렉션 최적화)
    │       - 프로젝트 컨벤션 준수 여부
    │
    ├── 5. 크로스 리뷰 (병렬 실행, 선택적)
    │       ├── Gemini CLI → 코드 리뷰 요청
    │       └── Codex CLI  → codex review --base {base}
    │
    ├── 6. 테스트 커버리지 확인
    │       - 테스트 파일 존재 여부
    │       - 부족한 테스트 케이스 제안
    │
    ├── 7. SELF-REVIEW.md 생성
    │       - 프로젝트 루트에 파일 생성 (덮어쓰기)
    │
    └── 8. 다음 액션 선택
            - PR 생성 진행 (/pr)
            - 개선 사항 직접 수정
            - 종료
```

### 출력 파일: SELF-REVIEW.md

프로젝트 루트에 생성되며, 다음 섹션을 포함합니다.

| 섹션 | 내용 |
|------|------|
| **변경 개요** | 커밋 수, 변경 파일 수, 추가/삭제 라인, 커밋 히스토리, 변경 파일 목록 |
| **리뷰 요약** | 코드 품질/보안/성능/테스트/컨벤션 상태를 테이블로 요약 |
| **파일별 상세 리뷰** | 각 파일의 변경 내용과 리뷰 의견 |
| **Gemini 크로스 리뷰** | Gemini CLI 리뷰 결과 (설치된 경우) |
| **Codex 크로스 리뷰** | Codex CLI 리뷰 결과 (설치된 경우) |
| **개선 제안** | 필수 수정(❌) / 권장 수정(⚠️) / 참고(💡) 우선순위별 정리 |
| **결론** | 모든 리뷰 결과를 종합한 최종 의견, PR 생성 가능 여부 |

### 리뷰 관점 상세

#### 코드 품질

| 체크 항목 | 설명 |
|----------|------|
| 네이밍 규칙 | PascalCase(클래스), camelCase(메서드/변수), UPPER_SNAKE_CASE(상수) |
| 단일 책임 원칙 | 클래스/메서드가 하나의 책임만 갖는지 |
| 중복 코드 | DRY 원칙 위반 여부 |
| 매직 넘버 | 하드코딩된 숫자/문자열 → 상수 추출 필요 여부 |

#### 보안

| 체크 항목 | 설명 |
|----------|------|
| 입력 검증 | 사용자 입력에 대한 유효성 검증 |
| SQL 인젝션 | 파라미터 바인딩 사용 여부 |
| XSS | 출력 인코딩 적용 여부 |
| 민감 정보 | 비밀번호, API 키 등 코드 내 노출 여부 |

#### 성능

| 체크 항목 | 설명 |
|----------|------|
| N+1 쿼리 | Fetch Join 또는 EntityGraph 활용 여부 |
| 고비용 객체 | Pattern, ObjectMapper 등 static 캐싱 여부 |
| 컬렉션 최적화 | 중첩 루프 → Map 변환, 초기 크기 지정 |
| 문자열 연산 | 반복문 내 String + 연산 → StringBuilder |

### 크로스 리뷰 동작 방식

```
                    ┌──────────────────┐
                    │   변경사항 수집     │
                    └────────┬─────────┘
                             │
                    diff 크기 확인
                             │
              ┌──────────────┴──────────────┐
              │                              │
        ≤ 1,000줄                      > 1,000줄
              │                              │
        전체 diff 전달              stat + 주요 파일만 전달
              │                              │
              └──────────────┬──────────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
         Claude 리뷰    Gemini 리뷰    Codex 리뷰
         (항상 실행)    (설치 시)      (설치 시)
              │              │              │
              └──────────────┼──────────────┘
                             │
                      결과 종합 정리
```

- 대용량 diff(1,000줄 초과)는 자동으로 요약 + 주요 파일만 크로스 리뷰에 전달
- Gemini/Codex 실행 실패 시 해당 섹션을 건너뛰고 계속 진행
- Bash timeout은 300초(5분)로 설정되어 크로스 리뷰 대기

---

## /review-pr — PR 코드 리뷰

### 개요

이미 생성된 GitHub PR에 대해 심층 코드 리뷰를 수행합니다.

### 사용법

```
/review-pr 123
```

| 인자 | 필수 | 설명 |
|------|------|------|
| PR 번호 | O | 리뷰할 PR 번호. 미입력 시 대화형으로 요청 |

### 실행 흐름

```
/review-pr 123 실행
    │
    ├── 1. PR 정보 수집 (병렬 실행)
    │       - gh pr view (제목, 본문, 작성자, 파일, 상태 등)
    │       - gh pr diff (전체 diff)
    │       - gh pr view --json comments (코멘트)
    │
    ├── 2. 전체 개요 분석
    │       - PR 목적, 주요 변경 파일, 커밋 히스토리, 현재 상태
    │
    ├── 3. 파일별 심층 리뷰
    │       - 코드 품질 / 보안 / 성능 / 컨벤션
    │
    ├── 4. 테스트 커버리지 확인
    │
    ├── 5. 크로스 리뷰 (병렬, 선택적)
    │       ├── Gemini CLI
    │       └── Codex CLI
    │
    ├── 6. 리뷰 결과 출력 (화면에 표시)
    │
    └── 7. 다음 액션 선택
            - 리뷰 코멘트 작성 (GitHub에 등록)
            - 특정 파일 상세 분석
            - 종료
```

### /self-review와의 차이점

| 항목 | /self-review | /review-pr |
|------|-------------|-----------|
| 대상 | 현재 브랜치 (로컬) | GitHub PR (원격) |
| 정보 소스 | `git diff`, `git log` | `gh pr view`, `gh pr diff` |
| 출력 | `SELF-REVIEW.md` 파일 생성 | 화면에 리뷰 결과 출력 |
| 후속 액션 | PR 생성 / 코드 수정 | GitHub 코멘트 등록 / 상세 분석 |
| 사용 시점 | PR 생성 **전** | PR 생성 **후** |
| 인자 | 없음 | PR 번호 |

### 리뷰 코멘트 등록

다음 액션에서 "리뷰 코멘트 작성"을 선택하면 `gh pr review` 명령으로 GitHub에 직접 리뷰를 등록합니다.

```bash
# 내부적으로 실행되는 명령어
gh pr review 123 --comment --body "리뷰 내용"
```

---

## /pr — PR 자동 생성

### 개요

현재 브랜치의 변경사항을 분석하여 GitHub PR을 자동 생성합니다.

### 사용법

```
/pr
```

> 인자 없이 실행합니다. 현재 브랜치와 CLAUDE.md 설정을 자동으로 참조합니다.

### 실행 흐름

```
/pr 실행
    │
    ├── 1. 브랜치 결정
    │       - Compare Branch: main → master → 사용자 입력
    │       - PR Base Branch: CLAUDE.md 설정 → main-review → master-review
    │
    ├── 2. 사전 확인
    │       - 작업 브랜치인지 확인
    │       - 커밋되지 않은 변경사항 경고
    │
    ├── 3. 변경사항 분석 (병렬 실행)
    │       - git branch, git log, git diff, git status
    │
    ├── 4. PR 제목/본문 작성
    │       - 제목: 70자 이내, <type>: <subject> 형식
    │       - 본문: pull_request_template.md 형식 준수
    │
    ├── 5. 사용자 확인
    │       - PR 제목/본문 미리보기
    │       - 생성 / 제목 수정 / 본문 수정 / 취소
    │
    ├── 6. 리뷰어 랜덤 선정
    │       - 저장소 collaborator 중 2명 랜덤 선정
    │       - PR_REVIEWER_EXCLUDE 계정 제외
    │       - 확인 / 다시 뽑기 / 직접 지정
    │
    └── 7. PR 생성
            - git push -u origin {branch}
            - gh pr create --base {base} --reviewer {reviewers}
            - PR URL, 번호, 변경 통계 출력
```

### PR 본문 형식

```markdown
### 변경 내용을 설명해 주세요. (자유롭게 기술해 주세요.)

{변경 내용 상세 분석 - 커밋별로 정리}

- **변경 파일**: {변경된 파일 목록과 각 파일의 변경 요약}
- **주요 변경사항**: {핵심 변경 내용을 bullet point로}

### (Optional) 리뷰어에게 남기실 말씀을 써 주세요.

{코드 리뷰 시 참고할 사항}
```

---

## 실전 사용 시나리오

### 시나리오 1: 기본 워크플로우 (Self Review → PR)

```
# 1. 작업 브랜치에서 코드 작성 완료
$ git checkout -b feature/user-auth

# 2. Claude Code에서 셀프 리뷰 실행
> /self-review

# 3. SELF-REVIEW.md 확인 → 필수 수정 사항 있으면 수정

# 4. 다음 액션에서 "PR 생성 진행" 선택 → 자동으로 /pr 실행
#    또는 직접 /pr 실행
> /pr

# 5. PR 제목/본문 확인 → 리뷰어 확인 → 생성
```

### 시나리오 2: 다른 사람의 PR 리뷰

```
# 1. 리뷰 요청받은 PR 번호 확인 (예: #42)

# 2. Claude Code에서 리뷰 실행
> /review-pr 42

# 3. 리뷰 결과 확인

# 4. 다음 액션에서 "리뷰 코멘트 작성" 선택
#    → GitHub PR에 리뷰 코멘트 자동 등록
```

### 시나리오 3: 크로스 리뷰 활용

```
# Gemini CLI + Codex CLI가 설치된 상태에서
> /self-review

# 자동으로 3개의 AI가 병렬로 코드 리뷰:
#   - Claude: 파일별 심층 분석
#   - Gemini: 독립적인 코드 리뷰 시각
#   - Codex: codex review --base 명령으로 리뷰
#
# SELF-REVIEW.md에 3개의 리뷰 결과가 모두 포함됨
```

---

## 슬래시 명령어 파일 구조

### 파일 위치

```
.claude/
└── commands/
    ├── self-review.md    # /self-review 명령어 정의
    ├── review-pr.md      # /review-pr 명령어 정의
    └── pr.md             # /pr 명령어 정의
```

### 명령어 파일 구조 (Frontmatter)

```yaml
---
description: 명령어 설명 (한 줄)
allowed-tools: Bash(git:*), Bash(gh:*), Bash(gemini:*), Bash(codex:*), Bash(where:*), Read, Glob, Grep
---
```

| 필드 | 설명 |
|------|------|
| `description` | 명령어 목록에 표시되는 설명 |
| `allowed-tools` | 명령어 실행 시 허용되는 도구 목록 |

### 허용 도구 설명

| 도구 | 용도 |
|------|------|
| `Bash(git:*)` | git 명령어 실행 (diff, log, branch 등) |
| `Bash(gh:*)` | GitHub CLI 실행 (PR 생성/조회) |
| `Bash(gemini:*)` | Gemini CLI 크로스 리뷰 |
| `Bash(codex:*)` | Codex CLI 크로스 리뷰 |
| `Bash(where:*)` | CLI 도구 설치 여부 확인 |
| `Read` | 소스 파일 읽기 |
| `Glob` | 파일 패턴 검색 |
| `Grep` | 코드 내용 검색 |

---

## 팀 프로젝트에 적용하기

### 1단계: 명령어 파일 복사

```bash
# 프로젝트 루트에 .claude/commands/ 디렉토리 생성
mkdir -p .claude/commands

# 명령어 파일 복사 (또는 직접 작성)
# self-review.md, review-pr.md, pr.md를 .claude/commands/에 배치
```

### 2단계: CLAUDE.md 설정

프로젝트 루트의 `CLAUDE.md`에 PR 관련 설정을 추가합니다.

```markdown
## PR 설정

PR_BASE_BRANCH: develop

## 리뷰어 제외 목록

PR_REVIEWER_EXCLUDE: bot-account, admin-account
```

### 3단계: Git에 커밋

```bash
git add .claude/commands/
git commit -m "chore: Claude Code 코드 리뷰 명령어 추가"
```

> `.claude/commands/` 디렉토리를 Git에 커밋하면 팀원 모두 동일한 명령어를 사용할 수 있습니다.

### 4단계: 팀원 안내

팀원에게 필요한 것:

| 항목 | 필수 여부 | 설명 |
|------|----------|------|
| Claude Code 설치 | 필수 | 슬래시 명령어 실행 환경 |
| GitHub CLI (gh) | 필수 | PR 생성/조회에 필요 |
| `gh auth login` | 필수 | GitHub 인증 |
| Gemini CLI | 선택 | 크로스 리뷰 활성화 |
| Codex CLI | 선택 | 크로스 리뷰 활성화 |

---

## 커스터마이징

### 리뷰 관점 수정

`.claude/commands/self-review.md`의 **3단계: 파일별 심층 리뷰** 섹션을 수정하여 리뷰 관점을 추가/변경할 수 있습니다.

```markdown
## 3단계: 파일별 심층 리뷰

### 코드 품질
- (기존 항목...)

### 접근성 (추가 예시)
- WCAG 2.1 준수 여부
- 시맨틱 HTML 사용 여부
- alt 텍스트 포함 여부
```

### 크로스 리뷰 프롬프트 수정

Gemini에게 전달하는 리뷰 프롬프트를 프로젝트에 맞게 커스터마이징할 수 있습니다.

```bash
# self-review.md 내 Gemini 리뷰 프롬프트 변경 예시
git diff {COMPARE_BRANCH}...HEAD | gemini -p "다음 코드를 리뷰해줘:

## 리뷰 관점
1. **Spring Boot 컨벤션**: Bean 설정, 의존성 주입 패턴
2. **JPA 최적화**: 쿼리 성능, 연관관계 설정
3. **API 설계**: RESTful 규칙, DTO 분리
..."
```

### PR 템플릿 수정

`/pr` 명령어의 PR 본문 형식은 `.claude/commands/pr.md`의 **PR 작성 규칙** 섹션에서 변경합니다.

---

## 트러블슈팅

### 사례 1: "셀프 리뷰는 작업 브랜치에서 실행해야 합니다"

#### 증상
`/self-review` 실행 시 즉시 중단

#### 원인
현재 브랜치가 `main` 또는 `master`

#### 해결
```bash
# 작업 브랜치로 이동
git checkout feature/my-feature
```

### 사례 2: Gemini/Codex 크로스 리뷰 타임아웃

#### 증상
크로스 리뷰 단계에서 오랜 시간 대기 후 실패

#### 원인
diff 크기가 매우 크거나 네트워크 지연

#### 해결
- 크로스 리뷰 실패 시 자동으로 건너뛰고 Claude 단독 리뷰로 계속 진행
- 지속적으로 발생하면 커밋 단위를 작게 나누어 diff 크기를 줄이는 것을 권장

### 사례 3: "gh: command not found"

#### 증상
`/pr` 또는 `/review-pr` 실행 시 gh 명령어를 찾을 수 없음

#### 해결
```bash
# GitHub CLI 설치
winget install GitHub.cli    # Windows
brew install gh              # macOS

# 인증
gh auth login
```

### 사례 4: PR Base Branch를 찾을 수 없음

#### 증상
`/pr` 실행 시 base branch를 찾지 못하고 사용자 입력 요청

#### 해결
`CLAUDE.md`에 `PR_BASE_BRANCH` 설정을 명시적으로 추가합니다.

```markdown
PR_BASE_BRANCH: develop
```

### 사례 5: 리뷰어 후보가 0명

#### 증상
`/pr` 실행 시 리뷰어 선정 단계에서 후보가 없음

#### 원인
- 저장소에 collaborator가 본인뿐
- `PR_REVIEWER_EXCLUDE`에 모든 collaborator가 포함

#### 해결
- "직접 지정" 옵션을 선택하여 리뷰어를 수동 입력
- `PR_REVIEWER_EXCLUDE` 목록을 확인하여 필요한 계정을 제외 목록에서 제거

---

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Claude Code Slash Command](./claude-code-slash-command.md) | 선수 지식 - 슬래시 명령어 작성법 | [3] 중급 |
| [Claude Code Skill](./claude-code-skill.md) | 관련 개념 - 자동 발견형 Skill과의 차이 | [3] 중급 |
| [Codex MCP 연결](./codex-mcp.md) | 크로스 리뷰 - Codex CLI 통합 방법 | [3] 중급 |
| [Claude Code Workflow](./claude-code-workflow.md) | 활용 - 효율적인 워크플로우 구성 | [3] 중급 |

## 참고 자료

- [Claude Code Slash Commands Documentation](https://docs.anthropic.com/en/docs/claude-code/slash-commands)
- [GitHub CLI Manual](https://cli.github.com/manual/)
- [Gemini CLI](https://github.com/anthropic-ai/gemini-cli)
- [OpenAI Codex CLI](https://github.com/openai/codex)
