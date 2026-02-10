# Claude Code 설정 체계

> `[3] 중급` · 선수 지식: [Claude Code Hook](./claude-code-hook.md), [Claude Code Skill](./claude-code-skill.md)

> `Trend` 2025

> Claude Code의 다계층 설정 구조와 셸 별칭, 보안 Hook 등 실전 환경 구성 방법

`#ClaudeCode` `#Settings` `#설정` `#Configuration` `#CLAUDE.md` `#settings.json` `#Hook` `#PreToolUse` `#PostToolUse` `#셸별칭` `#ShellAlias` `#보안` `#Security` `#다계층설정` `#글로벌설정` `#프로젝트설정` `#로컬설정` `#Anthropic` `#CLI` `#개발환경`

## 왜 알아야 하는가?

- **실무**: 프로젝트별 규칙 자동 적용, 위험 명령 차단 등으로 안전하고 일관된 AI 협업 환경 구축
- **면접**: AI 도구의 체계적 설정과 보안 의식을 보여주는 실질적 역량
- **기반 지식**: Claude Code 고급 기능(Skill, Hook, MCP)을 효과적으로 조합하기 위한 필수 기반

## 핵심 개념

- **다계층 설정**: 글로벌(Global) → 프로젝트(Project) → 로컬(Local) 3단계로 설정을 계층화하여 범위별 규칙 적용
- **CLAUDE.md**: AI에게 주는 프로젝트 설명서. 규칙, 컨벤션, 금지사항을 정의하는 핵심 설정 파일
- **settings.json**: Hook, MCP 서버, 권한 등 기술적 설정을 JSON으로 관리하는 파일
- **PreToolUse Hook**: 도구 실행 **전에** 명령을 검사하여 위험한 작업을 사전 차단하는 안전장치
- **셸 별칭(Shell Alias)**: 자주 사용하는 CLI 명령어를 단축하여 작업 속도를 높이는 기법

## 쉽게 이해하기

**Claude Code 설정 체계**를 **회사의 보안 정책**에 비유할 수 있습니다.

- **글로벌 설정** (`~/.claude/`) = 전사 보안 정책 (모든 부서에 적용)
- **프로젝트 설정** (`.claude/`) = 부서별 규칙 (해당 프로젝트에만 적용)
- **로컬 설정** (`settings.local.json`) = 개인 설정 (Git에 올리지 않음)
- **CLAUDE.md** = 업무 매뉴얼 (이 프로젝트에서 뭘 하고, 뭘 하지 말아야 하는지)
- **PreToolUse Hook** = 출입 게이트 (위험물 반입 시 입구에서 차단)
- **PostToolUse Hook** = CCTV 기록 (작업 후 로그 기록)
- **셸 별칭** = 단축 다이얼 (자주 쓰는 번호를 한 번에 호출)

## 상세 설명

### 1. 설정 파일 계층 구조

Claude Code는 3계층 설정을 지원하며, **하위 계층이 상위를 오버라이드(Override)**합니다.

```
우선순위: 로컬 > 프로젝트 > 글로벌

~/.claude/                          ← 글로벌 (모든 프로젝트에 적용)
├── CLAUDE.md                       ← 글로벌 행동 규칙
├── settings.json                   ← 글로벌 기술 설정 (Hook, MCP 등)
└── hooks/                          ← 글로벌 Hook 스크립트

{project}/                          ← 프로젝트 (해당 프로젝트에만 적용)
├── CLAUDE.md                       ← 프로젝트 행동 규칙
└── .claude/
    ├── settings.json               ← 프로젝트 기술 설정 (Git 추적 O)
    ├── settings.local.json         ← 로컬 기술 설정 (Git 추적 X)
    ├── hooks/                      ← 프로젝트 Hook 스크립트
    └── skills/                     ← 프로젝트 Skill
```

**왜 이렇게 나누는가?**

| 계층 | 용도 | 예시 |
|------|------|------|
| 글로벌 | 개인 습관, 공통 안전장치 | 위험 명령 차단, 응답 언어 설정 |
| 프로젝트 | 팀 공유 규칙 | 코딩 컨벤션, 커밋 메시지 형식 |
| 로컬 | 개인 환경 (API 키 등) | 개인 MCP 서버, 실험 설정 |

### 2. CLAUDE.md 작성 원칙

CLAUDE.md는 AI의 행동을 결정하는 **가장 중요한 설정 파일**입니다.

**권장 (O)**: 간결한 규칙 중심
```markdown
## Authentication
- NextAuth.js with Credentials provider
- JWT session strategy
- **DO NOT**: Bypass auth checks, expose session secrets
```

**비권장 (X)**: 장황한 설명
```markdown
## Authentication
Our authentication system is built using NextAuth.js, which is a
complete authentication solution for Next.js applications. It provides
a flexible and secure way to add authentication...
```

**왜?**

Claude Code는 CLAUDE.md를 매 세션마다 컨텍스트에 로드합니다. 장황한 설명은 토큰을 낭비하고, 핵심 규칙이 묻히게 됩니다. **짧은 CLAUDE.md가 좋은 CLAUDE.md**입니다.

### 3. settings.json 구조

settings.json은 Hook, MCP 서버 등 기술적 설정을 관리합니다.

```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "command": "bash \"$HOME/.claude/hooks/block-dangerous.sh\"",
            "timeout": 5
          }
        ]
      }
    ],
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "bash \".claude/hooks/log-edits.sh\"",
            "timeout": 10
          }
        ]
      }
    ]
  },
  "mcpServers": {}
}
```

#### Hook 매처(Matcher) 패턴

| 매처 | 대상 도구 | 용도 |
|------|----------|------|
| `Bash` | 셸 명령 실행 | 위험 명령 차단 |
| `Edit\|Write` | 파일 편집/생성 | 편집 로그 기록 |
| `Read` | 파일 읽기 | 민감 파일 접근 감지 |
| `Glob\|Grep` | 파일 검색 | 검색 범위 제한 |

#### Exit Code 규칙

| Exit Code | 의미 | 동작 |
|-----------|------|------|
| `0` | 허용 | 도구 실행 진행 |
| `2` | 차단 (BLOCKED) | 도구 실행 중단, 에러 메시지 표시 |
| 그 외 | 오류 | Hook 자체 오류로 처리 |

### 4. PreToolUse Hook - 위험 명령 차단

도구 실행 **전에** 명령을 검사하여 위험한 작업을 사전 차단합니다.

```bash
#!/bin/bash
# block-dangerous.sh - 위험 명령 사전 차단 Hook

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // ""')

if [[ -z "$COMMAND" ]]; then
    exit 0
fi

# 위험 패턴 목록 (패턴|설명)
RULES=(
    "rm -rf /|루트 디렉토리 삭제"
    "rm -rf ~|홈 디렉토리 삭제"
    "git push.*--force.*main|main 브랜치 force push"
    "git push.*--force.*master|master 브랜치 force push"
    "git reset --hard|커밋 이력 강제 초기화"
    "git checkout \.|모든 변경사항 폐기"
    "git clean -fd|추적되지 않는 파일 강제 삭제"
    "git branch -D|브랜치 강제 삭제"
    "drop table|테이블 삭제"
    "drop database|데이터베이스 삭제"
    "truncate table|테이블 데이터 전체 삭제"
    "--no-verify|Git Hook 우회"
)

for rule in "${RULES[@]}"; do
    pattern="${rule%%|*}"
    description="${rule##*|}"

    if echo "$COMMAND" | grep -iqE "$pattern"; then
        echo "⛔ BLOCKED: ${description}"
        exit 2
    fi
done

exit 0
```

**왜 PreToolUse인가?**

PostToolUse는 실행 **후** 동작하므로 이미 피해가 발생한 뒤입니다. PreToolUse는 실행 **전에** 차단하여 피해 자체를 방지합니다.

```
[사용자] "git push --force origin main"
    ↓
[PreToolUse Hook] 패턴 매칭: "git push.*--force.*main"
    ↓
⛔ BLOCKED: main 브랜치 force push
    ↓
[Claude] "위험한 명령이 차단되었습니다. 일반 push를 사용하시겠습니까?"
```

### 5. PostToolUse Hook - 작업 기록

도구 실행 **후**에 로그를 기록하거나 알림을 표시합니다.

```bash
#!/bin/bash
# log-edits.sh - 마크다운 편집 로그 기록

INPUT=$(cat)
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // ""')
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // ""')

# 마크다운 파일만 로그
if [[ ! "$FILE_PATH" =~ \.(md|mdx)$ ]]; then
    exit 0
fi

TIMESTAMP=$(date "+%Y-%m-%d %H:%M:%S")
echo "[$TIMESTAMP] $TOOL_NAME: $FILE_PATH" >> "$HOME/.claude/edit-log.txt"

exit 0
```

### 6. 셸 별칭(Shell Alias) 설정

자주 사용하는 명령어를 단축하여 작업 속도를 높입니다.

#### Bash / Git Bash (`~/.bashrc`)

```bash
# Claude Code 별칭
alias c='claude'              # 기본 실행
alias cc='claude --continue'  # 이전 세션 이어서
alias cr='claude --resume'    # 특정 세션 복원

# Git 별칭
alias gs='git status'
alias gd='git diff'
alias gl='git log --oneline -10'
alias gb='git branch'
alias gco='git checkout'
```

#### PowerShell (`$PROFILE`)

```powershell
Set-Alias c claude
function cc { claude --continue }
function cr { claude --resume }

function gs { git status }
function gd { git diff }
function gl { git log --oneline -10 }
```

**왜 별칭을 쓰는가?**

| 명령어 | 타이핑 수 | 별칭 | 타이핑 수 | 절감 |
|--------|----------|------|----------|------|
| `claude --continue` | 18자 | `cc` | 2자 | 89% |
| `git status` | 10자 | `gs` | 2자 | 80% |
| `claude --resume` | 15자 | `cr` | 2자 | 87% |

하루 수십 번 사용하면 누적 시간 절감이 상당합니다.

### 7. 실전 설정 조합 예시

글로벌과 프로젝트 설정을 조합한 실전 구성입니다.

```
~/.claude/                          ← 글로벌 (전체 프로젝트 공통)
├── CLAUDE.md                       ← 언어: 한국어, Java 코딩 표준
├── settings.json                   ← PreToolUse: 위험 명령 차단
└── hooks/
    └── block-dangerous.sh          ← 차단 스크립트

{project}/.claude/                  ← 프로젝트 (TIL 전용)
├── settings.json                   ← PreToolUse + PostToolUse
├── hooks/
│   ├── block-dangerous.sh          ← 차단 (이중 안전장치)
│   ├── log-edits.sh                ← 편집 로그 기록
│   └── notify-cs-doc.sh            ← CS 문서 변경 알림
└── skills/
    ├── cs-guide-writer/            ← CS 문서 작성
    ├── cs-sync/                    ← CS 문서 동기화
    └── ...
```

**이 구성의 장점:**

| 계층 | 역할 | 효과 |
|------|------|------|
| 글로벌 PreToolUse | 어떤 프로젝트든 위험 명령 차단 | 기본 안전망 |
| 프로젝트 PreToolUse | 프로젝트 특화 차단 규칙 추가 가능 | 세밀한 제어 |
| 프로젝트 PostToolUse | 편집 로그 + CS 문서 알림 | 작업 추적 |
| Skills | 반복 워크플로우 자동화 | 생산성 향상 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 위험 명령을 코드로 강제 차단 | Hook 스크립트 유지보수 필요 |
| 프로젝트별 맞춤 설정 가능 | 설정 파일이 분산되어 전체 파악 어려움 |
| 셸 별칭으로 작업 속도 향상 | 새 환경에서 별칭 재설정 필요 |
| 글로벌 + 프로젝트 이중 안전장치 | 과도한 Hook은 실행 속도 저하 유발 |

## 트러블슈팅

### 사례 1: Hook이 동작하지 않음

#### 증상
settings.json에 Hook을 추가했는데 실행되지 않음

#### 원인 분석
- 스크립트 파일에 실행 권한이 없음
- `jq`가 설치되지 않음
- 경로 변수(`$HOME`, `$CLAUDE_PROJECT_DIR`)가 올바르지 않음

#### 해결 방법
```bash
# 실행 권한 부여
chmod +x .claude/hooks/block-dangerous.sh

# jq 설치 확인
jq --version

# 경로 확인
echo $HOME
echo $CLAUDE_PROJECT_DIR
```

#### 예방 조치
Hook 스크립트 작성 후 반드시 수동 테스트:
```bash
echo '{"tool_input":{"command":"rm -rf /"}}' | bash .claude/hooks/block-dangerous.sh
# 기대 결과: exit code 2, BLOCKED 메시지
```

### 사례 2: 셸 별칭이 적용되지 않음

#### 증상
`.bashrc`에 별칭을 추가했는데 새 터미널에서 동작하지 않음

#### 원인 분석
Git Bash는 `.bash_profile`을 먼저 로드합니다. `.bash_profile`이 없으면 `.bashrc`를 무시할 수 있음

#### 해결 방법
```bash
# .bash_profile에서 .bashrc 로드하도록 설정
echo 'source ~/.bashrc' >> ~/.bash_profile
```

## 면접 예상 질문

### Q1. Claude Code의 설정 계층 구조와 우선순위는?

글로벌(`~/.claude/`) → 프로젝트(`.claude/`) → 로컬(`settings.local.json`) 3계층이며, **하위 계층이 상위를 오버라이드**합니다. 글로벌은 모든 프로젝트에 적용되는 공통 규칙(위험 명령 차단, 언어 설정), 프로젝트는 팀이 공유하는 규칙(컨벤션, Hook), 로컬은 개인 환경(API 키, 실험 설정)을 관리합니다. 이 구조로 **공통 안전망 위에 프로젝트별 맞춤 규칙**을 유연하게 적용할 수 있습니다.

### Q2. PreToolUse Hook과 PostToolUse Hook의 차이와 사용 시나리오는?

**PreToolUse**는 도구 실행 **전**에 동작하여 위험한 작업을 **사전 차단**합니다. `exit 2`로 실행 자체를 막을 수 있어 `rm -rf /`, `git push --force main` 같은 파괴적 명령을 방지합니다. **PostToolUse**는 실행 **후**에 동작하여 **로그 기록, 알림** 등에 사용됩니다. 보안에는 PreToolUse, 모니터링에는 PostToolUse가 적합합니다.

### Q3. CLAUDE.md와 settings.json의 역할 차이는?

**CLAUDE.md**는 AI의 **행동 규칙**을 자연어로 정의합니다 (코딩 컨벤션, 금지사항, 작업 흐름). AI가 매 세션마다 읽고 따릅니다. **settings.json**은 **기술적 설정**을 JSON으로 관리합니다 (Hook 등록, MCP 서버 연결, 권한 설정). CLAUDE.md는 "무엇을 해야 하는가", settings.json은 "어떻게 동작할 것인가"를 정의합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Claude Code Hook](./claude-code-hook.md) | Hook 시스템의 상세 개념과 이벤트 종류 (선수 지식) | Intermediate |
| [Claude Code Skill](./claude-code-skill.md) | Skills로 반복 워크플로우 자동화 (선수 지식) | Intermediate |
| [Claude Code 실전 가이드](./claude-code-guide.md) | 70가지 팁 중 설정 관련 핵심 정리 | Intermediate |
| [Claude Code Workflow](./claude-code-workflow.md) | 설정을 활용한 실무 최적화 전략 | Intermediate |
| [AI Guardrails](./ai-guardrails.md) | AI 안전 운영의 넓은 관점 | Intermediate |

## 참고 자료

- [Claude Code 공식 문서 - Configuration](https://docs.anthropic.com/en/docs/claude-code/settings)
- [Claude Code 공식 문서 - Hooks](https://docs.anthropic.com/en/docs/claude-code/hooks)
- [Anthropic Engineering Blog - Claude Code Best Practices](https://www.anthropic.com/engineering/claude-code-best-practices)
- [ykdojo claude-code-tips](https://github.com/ykdojo/claude-code-tips) - 43개 실전 팁
- [cc-safe](https://github.com/nichochar/cc-safe) - Claude Code 보안 스캔 도구
