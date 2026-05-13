#!/bin/bash
# ai-session.sh — 레거시 4분할 (인터랙티브 codex/gemini 패널)
#
# 사용법: ~/ai-session.sh 로 복사한 후 alias 'ai-old'로 호출.
#
# 패널 배치:
#   1 (좌상): claude (메인)
#   2 (우상): codex 인터랙티브
#   3 (좌하): gemini 인터랙티브
#   4 (우하): 빈 셸 (logs/htop/ssh 등 자유)
#
# 영상 패턴(dashboard 모니터)가 아닌 "사용자 직접 비교용" 4분할.

SESSION="ai"

# nvm 환경 활성화
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"

# 이미 세션 있으면 attach
tmux has-session -t $SESSION 2>/dev/null
if [ $? = 0 ]; then
  tmux attach -t $SESSION
  exit 0
fi

# 새 세션 + 4분할
tmux new-session -d -s $SESSION -n main
tmux split-window -h -t $SESSION:main
tmux split-window -v -t $SESSION:main.1
tmux split-window -v -t $SESSION:main.3

tmux set -g pane-border-status top
tmux set -g pane-border-format " #{pane_index}: #{pane_title} "

tmux select-pane -t $SESSION:main.1 -T "claude-main"
tmux select-pane -t $SESSION:main.2 -T "codex"
tmux select-pane -t $SESSION:main.3 -T "gemini"
tmux select-pane -t $SESSION:main.4 -T "shell"

tmux send-keys -t $SESSION:main.1 'claude --permission-mode bypassPermissions' C-m
tmux send-keys -t $SESSION:main.2 'codex' C-m
tmux send-keys -t $SESSION:main.3 'gemini' C-m

tmux select-pane -t $SESSION:main.1
tmux attach -t $SESSION
