#!/bin/bash
# ai-team.sh — AI 3에이전트 팀 영상 패턴 wrapper
#
# 사용법: 이 파일을 ~/ai-team.sh 로 복사한 후 alias 'ai'로 호출.
#
# 동작:
# 1. 현재 cwd 또는 git root 기반으로 세션 이름 자동 결정
#    예: /mnt/c/projects/myapp → agents-myapp
#        $HOME → agents (단일)
# 2. ai-harness-monitor 스킬의 team-layout.sh로 3분할 세션 생성
#    - 좌측: claude (메인)
#    - 우상: gemini dashboard (라이브 모니터)
#    - 우하: codex dashboard (라이브 모니터)
# 3. 좌측 패널에 claude 자동 시작
# 4. 세션 attach
#
# 이미 같은 이름 세션 있으면 그냥 attach.

set -e

LAYOUT_SCRIPT="$HOME/.claude/skills/ai-harness-monitor/scripts/team-layout.sh"

# nvm 환경 활성화 (claude/gemini/codex CLI 인식)
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"

# 세션 이름 결정
PROJECT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || echo "$PWD")
PROJECT=$(basename "$PROJECT_ROOT")

if [ "$PROJECT_ROOT" = "$HOME" ] || [ -z "$PROJECT" ] || [ "$PROJECT" = "/" ]; then
  SESSION="agents"
else
  SAFE_NAME=$(echo "$PROJECT" | sed 's/[^a-zA-Z0-9_-]/-/g')
  SESSION="agents-$SAFE_NAME"
fi

export REPO_DIR="$PWD"
echo "세션: $SESSION (cwd: $PWD)"

# 이미 세션 있으면 attach
if tmux has-session -t "$SESSION" 2>/dev/null; then
  echo "기존 '$SESSION' 세션 attach..."
  tmux attach -t "$SESSION"
  exit 0
fi

# 사전 체크: layout 스크립트 존재
if [ ! -x "$LAYOUT_SCRIPT" ]; then
  echo "ERROR: $LAYOUT_SCRIPT 가 없거나 실행 권한 없음" >&2
  echo "ai-harness-monitor 스킬을 ~/.claude/skills/에 설치하세요" >&2
  exit 1
fi

# 신규 세션 생성
bash "$LAYOUT_SCRIPT" -n "$SESSION" --no-attach

# 좌측 패널 찾아서 claude 자동 시작
LEFT_PANE=$(tmux list-panes -t "$SESSION:$SESSION" -F "#{pane_id} #{pane_left}" \
  | sort -k2 -n | head -1 | awk '{print $1}')

if [ -n "$LEFT_PANE" ]; then
  tmux send-keys -t "$LEFT_PANE" "clear && claude --permission-mode bypassPermissions" Enter
fi

tmux attach -t "$SESSION"
