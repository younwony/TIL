# Claude Code StatusLine 설정 가이드

> `[2] 입문` · 선수 지식: 없음

> 한 줄 정의: Claude Code 터미널 하단에 사용자, 브랜치, 모델, 비용 등 작업 상태를 실시간으로 표시하는 상태 표시줄

`#StatusLine` `#상태표시줄` `#ClaudeCode` `#CLI` `#터미널` `#ANSI` `#이스케이프코드` `#설정` `#커스터마이징` `#jq` `#printf` `#bash` `#셸스크립트` `#개발도구` `#DX` `#DeveloperExperience`

## 왜 알아야 하는가?

- **실무**: 현재 브랜치, 변경 파일 수, 컨텍스트 사용률, 비용 등을 한눈에 파악하여 작업 효율을 높임
- **기반 지식**: ANSI 이스케이프 코드, 셸 스크립트, JSON 파싱(jq) 등 기초 기술의 실전 활용 사례
- **커스터마이징**: 개인 워크플로우에 맞게 터미널 환경을 최적화하는 능력

## 핵심 개념

- StatusLine은 `~/.claude/settings.json`에서 셸 명령어로 정의
- Claude Code가 JSON 데이터를 stdin으로 전달하면, 명령어가 파싱하여 표시할 텍스트를 출력
- ANSI 이스케이프 코드로 색상 표현 가능
- `jq`로 JSON 필드를 추출하여 동적 정보 표시

## 쉽게 이해하기

StatusLine은 **게임의 HUD(Head-Up Display)** 와 같다.

게임에서 체력, 마나, 미니맵을 화면 한쪽에 항상 표시하듯이, StatusLine은 현재 사용자, 브랜치, 모델, 컨텍스트 사용률 등을 터미널 하단에 항상 표시한다.

```
┌─────────────────────────────────────────────┐
│  Claude Code 대화 영역                        │
│                                             │
│  > 사용자 입력...                              │
│  Claude 응답...                              │
│                                             │
├─────────────────────────────────────────────┤
│  user:TIL (main) [Claude Opus 4.6] | ctx: 45% | $1.23 | 15:32  │ ← StatusLine
└─────────────────────────────────────────────┘
```

## 상세 설명

### 설정 파일 위치

```
~/.claude/settings.json
```

### 기본 구조

```json
{
  "statusLine": {
    "type": "command",
    "command": "셸 명령어"
  }
}
```

- `type`: `"command"` 고정 (셸 명령어 실행 방식)
- `command`: stdin으로 JSON을 받아 처리하는 bash 명령어

### Claude Code가 전달하는 JSON 데이터

StatusLine 명령어는 stdin으로 다음과 같은 JSON을 수신한다:

```json
{
  "workspace": {
    "current_dir": "/workspace/intellij/TIL"
  },
  "model": {
    "display_name": "Claude Opus 4.6"
  },
  "context_window": {
    "used_percentage": 45.2
  },
  "cost_usd": 1.23
}
```

| 필드 | 설명 | 예시 |
|------|------|------|
| `workspace.current_dir` | 현재 작업 디렉토리 | `/workspace/intellij/TIL` |
| `model.display_name` | 사용 중인 모델명 | `Claude Opus 4.6` |
| `context_window.used_percentage` | 컨텍스트 윈도우 사용률 | `45.2` |
| `cost_usd` | 세션 누적 비용 (USD) | `1.23` |

### 명령어 작성 핵심 패턴

#### 1. JSON 파싱 (jq)

```bash
# stdin에서 JSON 읽기
input=$(cat)

# 필드 추출
dir=$(echo "$input" | jq -r '.workspace.current_dir // ""')
model=$(echo "$input" | jq -r '.model.display_name // ""')
used=$(echo "$input" | jq -r '.context_window.used_percentage // empty')
cost=$(echo "$input" | jq -r '.cost_usd // empty')
```

- `// ""`: 값이 없을 때 빈 문자열 반환
- `// empty`: 값이 없으면 출력 자체를 생략

#### 2. Git 정보 추출

```bash
# 현재 브랜치명
branch=$(git -C "$dir" --no-optional-locks symbolic-ref --short HEAD 2>/dev/null)

# 변경된 파일 수
changed=$(git -C "$dir" --no-optional-locks status --porcelain 2>/dev/null | wc -l | tr -d ' ')
```

- `--no-optional-locks`: 락 파일 생성 방지 (안전한 읽기 전용 조회)
- `2>/dev/null`: Git 저장소가 아닌 경우 에러 출력 억제

#### 3. ANSI 이스케이프 코드로 색상 적용

```bash
# 색상 코드 (printf 또는 printf '%b' 에서 사용)
\033[36m   # 청록색 (Cyan)
\033[33m   # 노란색 (Yellow)
\033[32m   # 초록색 (Green)
\033[31m   # 빨간색 (Red)
\033[35m   # 보라색 (Magenta)
\033[0m    # 색상 초기화 (Reset)
```

**주의**: ANSI 코드 사용 시 반드시 출력 방식에 맞는 처리가 필요하다.

| 출력 방식 | ANSI 해석 여부 | 비고 |
|----------|:-------------:|------|
| `printf` 포맷 문자열 | O | `printf "\033[36m%s\033[0m" "$var"` |
| `printf '%s'` 인자 | X | 리터럴로 출력됨 |
| `printf '%b'` 인자 | O | 이스케이프 시퀀스 해석 |
| `echo -e` | O | 이스케이프 시퀀스 해석 |

#### 4. 출력 (printf '%b')

```bash
# 문자열을 빌드한 후 '%b'로 출력 (이스케이프 코드 해석)
out="\033[36m${user}\033[0m:\033[33m${basename_dir}\033[0m"
printf '%b' "$out"
```

**왜 `%b`를 사용하는가?**

변수에 저장된 `\033`은 리터럴 문자열이다. `%s`는 이를 그대로 출력하지만, `%b`는 이스케이프 시퀀스로 해석하여 실제 색상으로 변환한다.

```bash
# 잘못된 방식 - \033[32m(main)\033[0m 이 그대로 보임
branch_info="\033[32m(main)\033[0m"
printf "%s" "$branch_info"    # ← %s는 해석하지 않음

# 올바른 방식 - 초록색 (main) 으로 표시됨
out="\033[32m(main)\033[0m"
printf '%b' "$out"            # ← %b가 \033을 ESC로 해석
```

### JSON 이스케이프 주의사항

`settings.json`에 명령어를 작성할 때는 JSON 이스케이프가 추가로 적용된다:

| 원하는 bash 코드 | JSON에 작성할 값 | 설명 |
|-----------------|-----------------|------|
| `\033` | `\\033` | JSON `\\` → bash `\` |
| `"$var"` | `\"$var\"` | JSON 내 따옴표 이스케이프 |
| `\$` | `\\$` | 달러 기호 리터럴 |

### 전체 설정 예시

```json
{
  "statusLine": {
    "type": "command",
    "command": "input=$(cat); user=$(whoami); dir=$(echo \"$input\" | jq -r '.workspace.current_dir // .cwd // \"\"'); basename_dir=$(basename \"$dir\"); model=$(echo \"$input\" | jq -r '.model.display_name // \"\"'); branch=$(git -C \"$dir\" --no-optional-locks symbolic-ref --short HEAD 2>/dev/null); out=\"\\033[36m${user}\\033[0m:\\033[33m${basename_dir}\\033[0m\"; if [ -n \"$branch\" ]; then changed=$(git -C \"$dir\" --no-optional-locks status --porcelain 2>/dev/null | wc -l | tr -d ' '); if [ \"$changed\" -gt 0 ]; then out=\"${out} \\033[31m(${branch} *${changed})\\033[0m\"; else out=\"${out} \\033[32m(${branch})\\033[0m\"; fi; fi; out=\"${out} [\\033[35m${model}\\033[0m]\"; used=$(echo \"$input\" | jq -r '.context_window.used_percentage // empty'); if [ -n \"$used\" ] && [ \"$used\" != \"null\" ]; then used_int=${used%.*}; out=\"${out} | ctx: ${used_int}%\"; fi; cost=$(echo \"$input\" | jq -r '.cost_usd // empty'); if [ -n \"$cost\" ] && [ \"$cost\" != \"null\" ]; then cost_formatted=$(printf '%.2f' \"$cost\"); out=\"${out} | \\$${cost_formatted}\"; fi; current_time=$(date +%H:%M); out=\"${out} | ${current_time}\"; printf '%b' \"$out\""
  }
}
```

### 표시 결과

```
youn.wonhee:TIL (main *3) [Claude Opus 4.6] | ctx: 45% | $1.23 | 15:32
 ────────── ─── ──── ──── ──────────────────   ────────   ──────   ─────
 청록색     노란  빨강  숫자  보라색              기본       기본    기본
            색    (변경有)
```

| 항목 | 색상 | 조건 |
|------|------|------|
| 사용자명 | 청록색 (Cyan) | 항상 표시 |
| 디렉토리 | 노란색 (Yellow) | 항상 표시 |
| 브랜치 (변경 없음) | 초록색 (Green) | Git 저장소일 때 |
| 브랜치 *N (변경 있음) | 빨간색 (Red) | 변경 파일 존재 시 |
| 모델명 | 보라색 (Magenta) | 항상 표시 |
| ctx 사용률 | 기본 | 컨텍스트 데이터 있을 때 |
| 비용 | 기본 | cost_usd 있을 때 |
| 현재 시간 | 기본 | 항상 표시 |

## 트러블슈팅

### 사례 1: ANSI 코드가 리터럴로 표시됨

#### 증상

```
\033[32m(main)\033[0m
```

색상 대신 이스케이프 코드가 그대로 보임.

#### 원인 분석

변수에 저장된 `\033`은 리터럴 문자열이다. `printf "%s"` 는 이를 해석하지 않고 그대로 출력한다.

#### 해결 방법

`printf '%b'`를 사용하여 출력한다. `%b` 포맷 지정자는 `\033` 같은 이스케이프 시퀀스를 해석한다.

```bash
# 변경 전
printf "\033[36m%s\033[0m %s" "$user" "$branch_info"

# 변경 후 - 전체를 하나의 문자열로 빌드 후 %b로 출력
out="\033[36m${user}\033[0m ${branch_info}"
printf '%b' "$out"
```

#### 예방 조치

- ANSI 코드를 변수에 저장할 때는 최종 출력에서 반드시 `printf '%b'` 또는 `echo -e` 사용
- `printf` 포맷 문자열 내 ANSI 코드는 직접 해석되므로 `%s` 사용 가능하지만, 변수 내 코드는 `%b` 필요

### 사례 2: Git 정보가 표시되지 않음

#### 증상

디렉토리와 모델명은 표시되지만 브랜치 정보가 없음.

#### 원인 분석

- `git` 명령어에 전달되는 디렉토리 경로가 잘못됨
- Git 저장소가 아닌 디렉토리에서 실행

#### 해결 방법

```bash
# -C 옵션으로 디렉토리를 명시적으로 지정
branch=$(git -C "$dir" --no-optional-locks symbolic-ref --short HEAD 2>/dev/null)
```

- `$dir` 이 실제 작업 디렉토리인지 확인
- `jq` 필드명이 올바른지 확인 (`.workspace.current_dir` 또는 `.cwd`)

### 사례 3: jq 관련 에러

#### 증상

StatusLine이 아예 표시되지 않거나 에러 메시지가 표시됨.

#### 원인 분석

`jq`가 설치되어 있지 않거나 PATH에 없음.

#### 해결 방법

```bash
# jq 설치 확인
jq --version

# 미설치 시
# macOS: brew install jq
# Ubuntu: sudo apt install jq
# Windows (Git Bash): 별도 설치 필요
```

## 면접 예상 질문

### Q: ANSI 이스케이프 코드란 무엇인가?

A: 터미널에서 텍스트 색상, 스타일(굵게, 밑줄), 커서 위치 등을 제어하기 위한 특수 문자 시퀀스이다. `ESC[` (= `\033[` = `\e[`)로 시작하며, 터미널 에뮬레이터가 이를 해석하여 화면에 반영한다. 원래 VT100 터미널에서 시작된 표준으로, 현재 대부분의 터미널이 지원한다.

### Q: printf의 %s와 %b의 차이는?

A: `%s`는 문자열을 **있는 그대로** 출력한다. `\033`이 변수에 있으면 리터럴 4글자(`\`, `0`, `3`, `3`)로 출력된다. 반면 `%b`는 C 스타일 이스케이프 시퀀스를 **해석**하여 `\033`을 실제 ESC 문자(0x1B)로 변환한 후 출력한다. 색상이 필요한 동적 문자열에는 `%b`를 사용해야 한다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Claude HUD 설정 가이드](./claude-hud-setup.md) | 플러그인 기반 StatusLine 확장 | 입문 |
| [Claude Code 릴리스 노트](./claude-code-release-notes.md) | Claude Code 기능 업데이트 | 입문 |

## 참고 자료

- [Claude Code 공식 문서 - Settings](https://docs.anthropic.com/en/docs/claude-code)
- [ANSI Escape Code - Wikipedia](https://en.wikipedia.org/wiki/ANSI_escape_code)
- [jq Manual](https://stedolan.github.io/jq/manual/)
