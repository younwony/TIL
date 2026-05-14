#!/bin/bash
#
# .claude/ 디렉토리 git add 차단 PreToolUse Hook
# CLAUDE.md 규칙: .claude/ 하위 파일(skills, commands, hooks, settings, docs 등)은 git add 금지.
# 'git add' 명령의 인자로 .claude 경로 컴포넌트가 등장하면 차단한다.
# exit 0: 허용, exit 2: 차단 (BLOCKED)
#

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // ""')

# 빈 명령이면 통과
if [[ -z "$COMMAND" ]]; then
    exit 0
fi

# git add 명령이 아니면 통과
if ! echo "$COMMAND" | grep -qE 'git[[:space:]]+add\b'; then
    exit 0
fi

# 예외 1 — 환경변수 응급 우회 (SKIP_CLAUDE_DIR_BLOCK=1)
if [[ "$SKIP_CLAUDE_DIR_BLOCK" == "1" ]]; then
    exit 0
fi

# 예외 2 — TIL 저장소
# TIL은 글로벌 설정 공유 채널로 사용되어 .claude/ git add 허용 (TIL/CLAUDE.md 정책).
#   1) 명령이 TIL 경로 안에서 실행 (cd TIL && git add ...)
#   2) 작업 디렉토리(cwd)가 TIL 안
CWD=$(echo "$INPUT" | jq -r '.cwd // ""')
TIL_PATTERN='workspace[/\\]intellij[/\\]TIL'
if echo "$COMMAND" | grep -qiE "$TIL_PATTERN"; then
    exit 0
fi
if [[ -n "$CWD" ]] && echo "$CWD" | grep -qiE "$TIL_PATTERN"; then
    exit 0
fi
# fallback: hook 실행 시점의 pwd
if pwd 2>/dev/null | grep -qiE "$TIL_PATTERN"; then
    exit 0
fi

# .claude 경로 컴포넌트가 인자에 포함되면 차단
#   - "git add .claude"          → block
#   - "git add .claude/"         → block
#   - "git add foo .claude/bar"  → block
#   - "git add path/.claude"     → block
#   - "git add my.claude.md"     → pass (.claude 가 path 컴포넌트가 아님)
if echo "$COMMAND" | grep -qE '(^|[[:space:]/])\.claude(/|[[:space:]]|$)'; then
    echo "⛔ BLOCKED: .claude/ 디렉토리는 git add 금지 (CLAUDE.md 규칙)" >&2
    echo "  - skills, commands, hooks, settings, docs, tracks 등 .claude/ 하위는 모두 staging 제외" >&2
    echo "  - TIL 저장소(workspace/intellij/TIL/)에서는 자동 허용됨" >&2
    echo "  - 그 외 응급 우회: SKIP_CLAUDE_DIR_BLOCK=1 git add ..." >&2
    exit 2
fi

exit 0
