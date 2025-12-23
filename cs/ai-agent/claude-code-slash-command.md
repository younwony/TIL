# Claude Code Slash Command

> `[3] 중급` · 선수 지식: [MCP](./mcp.md)

> `Trend` 2025

> 자주 사용하는 프롬프트를 명령어로 만들어 빠르게 실행할 수 있는 재사용 가능한 프롬프트 시스템

`#ClaudeCode` `#SlashCommand` `#슬래시명령어` `#SlashCommands` `#커스텀명령어` `#CustomCommand` `#프롬프트` `#Prompt` `#자동화` `#Automation` `#Anthropic` `#CLI` `#명령어` `#Command` `#마크다운` `#Markdown` `#재사용프롬프트` `#UserInvoked` `#명시적호출` `#commands폴더` `#프로젝트명령어` `#사용자명령어` `#단축키` `#워크플로우`

## 왜 알아야 하는가?

- **실무**: 반복적인 프롬프트 입력을 한 줄 명령어로 자동화하여 작업 효율 향상
- **면접**: AI 도구 활용 역량과 개발 자동화 경험 증명
- **기반 지식**: Claude Code 커스터마이징의 기본 단위

## 핵심 개념

- **재사용 가능한 프롬프트**: 마크다운 파일로 정의하고 `/명령어` 형식으로 호출
- **명시적 호출**: 사용자가 직접 `/review`처럼 명령어를 입력해야 실행
- **변수 지원**: `$ARGUMENTS`, `$1`, `$2` 등으로 동적 입력 처리
- **도구 접근 제어**: `allowed-tools`로 특정 도구만 사용하도록 제한
- **계층적 저장**: 프로젝트 레벨과 사용자 레벨로 분리하여 관리

## 쉽게 이해하기

**Slash Command**를 **단축키가 설정된 매크로**에 비유할 수 있습니다.

게임에서 복잡한 스킬 콤보를 매번 입력하기 귀찮으면 매크로로 등록합니다. 키 하나만 누르면 미리 정의한 동작이 실행됩니다.

Claude Code의 Slash Command도 마찬가지입니다:

```
# 매번 이렇게 길게 입력하는 대신...
"이 코드를 검토해줘. 보안 취약점, 성능 문제,
SOLID 원칙 위반 여부를 확인하고..."

# 한 번 정의해두면...
> /review src/app.js

# 끝! 미리 정의한 프롬프트가 자동으로 실행됩니다.
```

Skill이 "상황에 맞게 자동으로 선택"되는 것이라면, Slash Command는 "내가 원할 때 직접 호출"하는 것입니다.

## 상세 설명

### 내장 Slash Command

Claude Code에는 기본 제공되는 명령어가 있습니다.

| 명령어 | 설명 |
|--------|------|
| `/help` | 사용 가능한 모든 명령어 목록 표시 |
| `/clear` | 대화 기록 초기화 |
| `/config` | 설정 인터페이스 열기 |
| `/context` | 현재 컨텍스트 사용량 시각화 |
| `/cost` | 토큰 사용 통계 표시 |
| `/model` | AI 모델 변경 |
| `/exit` | Claude Code 종료 |
| `/export` | 현재 대화를 파일로 내보내기 |
| `/memory` | CLAUDE.md 메모리 편집 |
| `/init` | CLAUDE.md 초기화 |
| `/hooks` | Hook 설정 관리 |
| `/permissions` | 도구 권한 보기/수정 |
| `/rewind` | 대화 되감기 |

### 커스텀 명령어 저장 위치

| 레벨 | 위치 | 용도 | Git 공유 |
|------|------|------|---------|
| 프로젝트 | `.claude/commands/` | 팀 워크플로우 | O |
| 사용자 | `~/.claude/commands/` | 개인 명령어 | X |

**우선순위**: 동일 이름이면 프로젝트 명령어가 우선

**왜 두 가지로 나뉘는가?**

- **프로젝트 명령어**: 팀 전체가 같은 워크플로우를 사용해야 할 때. Git에 커밋하면 팀원 모두 동일한 명령어 사용 가능
- **사용자 명령어**: 개인 작업 스타일에 맞춘 명령어. 모든 프로젝트에서 사용 가능

### 명령어 파일 구조

커스텀 명령어는 마크다운 파일로 작성합니다.

```markdown
---
description: 명령어 설명 (필수 권장)
allowed-tools: Bash(git:*), Read, Write
argument-hint: [file-path]
model: sonnet
---

# 명령어 제목

Claude에게 전달될 지시사항을 작성합니다.
$ARGUMENTS 변수로 사용자 입력을 받을 수 있습니다.
```

### Frontmatter 옵션

| 옵션 | 설명 | 기본값 |
|------|------|--------|
| `description` | 명령어 설명 (`/help`에 표시) | 첫 줄 자동 사용 |
| `allowed-tools` | 사용 가능한 도구 제한 | 대화 설정 상속 |
| `argument-hint` | 인수 형식 힌트 (자동완성에 표시) | 없음 |
| `model` | 특정 모델 지정 | 대화 모델 상속 |

### 변수 시스템

명령어에서 사용자 입력을 동적으로 처리할 수 있습니다.

| 변수 | 설명 | 예시 |
|------|------|------|
| `$ARGUMENTS` | 모든 인수를 하나의 문자열로 | `/explain closure` → "closure" |
| `$1`, `$2`, ... | 개별 인수 (공백 구분) | `/fix app.js 123` → $1="app.js", $2="123" |

### 특수 문법

| 문법 | 설명 | 예시 |
|------|------|------|
| `@파일경로` | 파일 참조 | `@src/utils.js` |
| `!`백틱명령어`!` | Bash 명령어 실행 | `!git status!` |

## 동작 원리

### 명령어 실행 흐름

```
1. 사용자가 명령어 입력
   └─> /review src/app.js

2. Claude Code가 명령어 파일 검색
   ├─> .claude/commands/review.md (프로젝트)
   └─> ~/.claude/commands/review.md (사용자)

3. 변수 치환
   └─> $ARGUMENTS → "src/app.js"
   └─> $1 → "src/app.js"

4. Bash 명령어 실행 (있는 경우)
   └─> !`git status`! → 실제 출력으로 대체

5. 최종 프롬프트를 Claude에게 전달

6. Claude가 응답 생성
```

### 파일 검색 순서

```
1. 프로젝트 명령어 (.claude/commands/)
   └─> 매칭되면 사용, 아니면 다음 단계

2. 사용자 명령어 (~/.claude/commands/)
   └─> 매칭되면 사용, 아니면 에러

3. 내장 명령어
   └─> /help, /clear 등
```

## 예제 코드

### 기본 명령어: 코드 리뷰

`.claude/commands/review.md`:

```markdown
---
description: 코드 리뷰 요청
allowed-tools: Read, Grep, Glob
---

# 코드 리뷰

다음 항목을 기준으로 코드를 검토해주세요:

## 검토 항목
- 보안 취약점 (SQL Injection, XSS 등)
- 성능 문제
- 코드 스타일 위반
- SOLID 원칙 위반
- 테스트 커버리지

## 출력 형식
각 항목별로 발견된 문제와 개선 방안을 제시하세요.
```

사용:
```
> /review src/UserService.java
```

### 변수 활용: Git 커밋

`.claude/commands/commit.md`:

```markdown
---
description: 커밋 메시지 생성
allowed-tools: Bash(git:*)
argument-hint: [type: docs|feat|fix|refactor]
---

# Git 커밋 생성

## 현재 상태
!`git status`!

## 변경 내용
!`git diff --staged`!

## 작업
위 변경사항을 분석하여 커밋 메시지를 작성하세요.

타입: $1
형식:
```
$1: 간결한 설명

- 상세 변경 내용 1
- 상세 변경 내용 2
```
```

사용:
```
> /commit docs
```

### 다중 인수: 파일 비교

`.claude/commands/compare.md`:

```markdown
---
description: 두 파일 비교
allowed-tools: Read
argument-hint: [file1] [file2]
---

# 파일 비교

@$1과 @$2를 비교 분석해주세요.

## 비교 항목
- 구조적 차이
- 로직 차이
- 성능 차이
- 어떤 버전이 더 나은지 평가
```

사용:
```
> /compare src/v1/app.js src/v2/app.js
```

### Bash 명령어 실행: PR 분석

`.claude/commands/pr-analysis.md`:

```markdown
---
description: PR 변경사항 분석
allowed-tools: Bash(git:*), Read
argument-hint: [branch-name]
---

# PR 분석

## 브랜치 정보
- Source: $1
- Target: main

## 변경 통계
!`git diff main...$1 --stat`!

## 변경된 파일
!`git diff main...$1 --name-only`!

## 분석 항목
1. 코드 품질 평가
2. 잠재적 버그
3. 테스트 필요 여부
4. 문서화 상태
```

사용:
```
> /pr-analysis feature/new-api
```

### 네임스페이스: 카테고리별 명령어

디렉토리 구조로 명령어를 분류할 수 있습니다:

```
.claude/commands/
├── git/
│   ├── commit.md      # /git:commit
│   ├── pr.md          # /git:pr
│   └── review.md      # /git:review
├── test/
│   ├── unit.md        # /test:unit
│   └── e2e.md         # /test:e2e
└── docs/
    └── readme.md      # /docs:readme
```

사용:
```
> /git:commit
> /test:unit src/services/
```

## Skill과의 비교

| 구분 | Slash Command | Skill |
|------|---------------|-------|
| 호출 방식 | 사용자가 `/명령어`로 직접 호출 | Claude가 상황에 맞게 자동 선택 |
| 파일 구조 | 단일 `.md` 파일 | 디렉토리 + `SKILL.md` |
| 지원 파일 | 단일 파일만 | 스크립트, 템플릿 등 지원 |
| 적합한 상황 | 같은 프롬프트를 반복 실행 | 상황에 따라 자동 적용 |
| 복잡도 | 간단한 프롬프트 | 복잡한 워크플로우 |

**언제 무엇을 사용할까?**

- **Slash Command**: "코드 리뷰해줘", "커밋 만들어줘" 같이 **명확한 의도**가 있을 때
- **Skill**: "이 PDF 처리해줘" 같이 **맥락에 따라 적절한 도구가 달라질 때**

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 반복 작업을 한 줄로 단축 | 명령어 이름을 기억해야 함 |
| 팀 전체에 일관된 워크플로우 적용 | 복잡한 로직은 표현하기 어려움 |
| Git으로 버전 관리 및 공유 | 15,000자 제한 |
| 도구 접근 제어로 안전한 실행 | Skill처럼 자동 선택 불가 |
| 변수로 유연한 입력 처리 | 초기 설정 학습 곡선 |

## 면접 예상 질문

### Q: Slash Command와 Skill의 차이점은 무엇인가요?

A: 핵심 차이는 **호출 방식**입니다.

- **Slash Command**: 사용자가 `/review`처럼 명시적으로 호출합니다. "이 작업을 해라"라는 명확한 의도가 있을 때 사용합니다.
- **Skill**: Claude가 대화 맥락을 분석하여 자동으로 선택합니다. "PDF 처리해줘"라고 하면 알아서 적절한 Skill을 찾습니다.

**실무 가이드라인:**
- 자주 반복하는 명확한 작업 → Slash Command
- 상황에 따라 유연하게 적용해야 하는 워크플로우 → Skill

### Q: 프로젝트 명령어와 사용자 명령어는 언제 각각 사용하나요?

A: **공유 범위**에 따라 선택합니다.

- **프로젝트 명령어** (`.claude/commands/`): 팀 전체가 동일한 워크플로우를 따라야 할 때 사용합니다. Git에 커밋하면 `git pull`만으로 팀 전체가 같은 명령어를 사용할 수 있습니다.

- **사용자 명령어** (`~/.claude/commands/`): 개인 작업 스타일에 맞춘 명령어입니다. 모든 프로젝트에서 사용 가능하므로, 프로젝트와 무관하게 자주 쓰는 명령어를 등록합니다.

**우선순위**: 같은 이름이면 프로젝트 명령어가 우선입니다. 이를 통해 프로젝트별 커스터마이징이 가능합니다.

### Q: `$ARGUMENTS`와 `$1`, `$2`의 차이는?

A: **인수 처리 방식**이 다릅니다.

| 변수 | 동작 | 예시 (`/cmd hello world`) |
|------|------|---------------------------|
| `$ARGUMENTS` | 모든 인수를 하나의 문자열로 | "hello world" |
| `$1`, `$2` | 공백으로 구분된 개별 인수 | $1="hello", $2="world" |

**사용 가이드라인:**
- 자연어 입력 (설명, 질문 등) → `$ARGUMENTS`
- 구조화된 입력 (파일경로, 번호 등) → `$1`, `$2`

### Q: allowed-tools 옵션은 왜 필요한가요?

A: **안전성과 예측 가능성**을 위해 필요합니다.

예를 들어, 코드 리뷰 명령어에서 실수로 파일을 수정하면 안 됩니다:

```yaml
---
allowed-tools: Read, Grep, Glob  # Write, Edit 제외
---
```

이렇게 하면 Claude가 아무리 파일을 수정하려 해도 도구 접근이 차단됩니다. Hook의 Exit code 2처럼 강제로 막는 것입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [MCP](./mcp.md) | 선수 지식 - AI 에이전트 기초 | [2] 입문 |
| [Skill](./claude-code-skill.md) | 관련 개념 - 자동 발견 방식 | [3] 중급 |
| [Hook](./claude-code-hook.md) | 관련 개념 - 이벤트 기반 제어 | [3] 중급 |

## 참고 자료

- [Claude Code Slash Commands Documentation](https://docs.anthropic.com/en/docs/claude-code/slash-commands)
- [Claude Code Skills Documentation](https://docs.anthropic.com/en/docs/claude-code/skills)
