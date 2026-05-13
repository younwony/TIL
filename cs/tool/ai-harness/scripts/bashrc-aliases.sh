# ~/.bashrc 에 추가할 alias 블록
# 사용법:
#   cat scripts/bashrc-aliases.sh >> ~/.bashrc
#   source ~/.bashrc

# === Harness 플러그인 활성화 ===
export CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1

# === ai 세션 alias ===
# 현재 cwd 기반으로 세션 이름 자동 결정 (git root > basename > "agents")
ai_session_name() {
  local root project safe_name
  root=$(git rev-parse --show-toplevel 2>/dev/null || echo "$PWD")
  project=$(basename "$root")
  if [ "$root" = "$HOME" ] || [ -z "$project" ] || [ "$project" = "/" ]; then
    echo "agents"
  else
    safe_name=$(echo "$project" | sed 's/[^a-zA-Z0-9_-]/-/g')
    echo "agents-$safe_name"
  fi
}

alias ai='bash ~/ai-team.sh'
alias aik='_s=$(ai_session_name); tmux kill-session -t "$_s" 2>/dev/null && echo "killed: $_s" || echo "no session: $_s"'
alias air='_s=$(ai_session_name); tmux kill-session -t "$_s" 2>/dev/null; bash ~/ai-team.sh'
alias ais='echo "현재 세션: $(ai_session_name)"; echo "전체 agents 세션:"; tmux ls 2>/dev/null | grep -E "^agents" || echo "  (없음)"'
alias aia='tmux a -t "$(ai_session_name)"'
alias aikall='tmux ls 2>/dev/null | grep -oE "^agents[^:]*" | xargs -I{} tmux kill-session -t {} 2>/dev/null; echo "all agents killed"'
alias ai-old='bash ~/ai-session.sh'
