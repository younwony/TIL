#!/usr/bin/env bash
# route-codex.sh — auto-route Codex NEED RESEARCH through Gemini.
#
# Workflow:
#   1. ask-codex.sh "focus"                     (1st pass)
#   2. parse latest-codex.log for "## NEED RESEARCH" block
#   3. if meaningful bullets found:
#      - ask-gemini.sh with stitched query
#      - copy latest-gemini.log to a research file
#      - ask-codex.sh --with-research <file> "focus"   (2nd pass)
#   4. else: 1st pass is the final response
#
# Usage:
#   route-codex.sh "focus or scope"
#   route-codex.sh --max-iter 2 "focus"     # allow up to N research rounds
#
# Logs: each pass uses ask-*.sh's own logging — dashboard auto-renders cards.
#
# 글로벌 스킬 ai-harness-monitor. 작성: 2026-05-11.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
AGENTS_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

# Team namespace — must match ask-*.sh detection logic to read the correct log dir.
detect_team() {
  if [ -n "${AGENT_TEAM:-}" ]; then echo "$AGENT_TEAM"; return; fi
  if [ -n "${TMUX:-}" ]; then
    local n
    n=$(tmux show-options -wqv -t "${TMUX_PANE:-}" '@team-name' 2>/dev/null) || n=""
    [ -n "$n" ] && { echo "$n"; return; }
    n=$(tmux display-message -p -t "${TMUX_PANE:-}" '#{session_name}' 2>/dev/null) || n=""
    [ -n "$n" ] && { echo "$n"; return; }
  fi
  echo default
}
TEAM=$(detect_team)
LOG_DIR="$AGENTS_DIR/log/$TEAM"

MAX_ITER=1
while [ "$#" -gt 0 ]; do
  case "$1" in
    --max-iter)
      if [ "$#" -lt 2 ]; then
        echo "error: --max-iter requires an integer argument" >&2
        exit 2
      fi
      if ! [[ "$2" =~ ^[0-9]+$ ]]; then
        echo "error: --max-iter must be a non-negative integer (got: $2)" >&2
        exit 2
      fi
      MAX_ITER="$2"
      shift 2
      ;;
    -h|--help)
      grep -E "^# " "$0" | sed 's/^# \?//'
      exit 0
      ;;
    --) shift; break ;;
    -*) echo "unknown option: $1" >&2; exit 2 ;;
    *) break ;;
  esac
done

if [ "$#" -lt 1 ]; then
  echo "usage: $0 [--max-iter N] \"focus or scope\"" >&2
  exit 2
fi

FOCUS="$1"
TMPDIR_R="$(mktemp -d)"
trap 'rm -rf "$TMPDIR_R"' EXIT

# Helper: extract NEED RESEARCH block from a codex log file.
# IMPORTANT: only scan the response section (between `=== RESPONSE ===` and `=== END`).
# Otherwise the FOCUS section (user-controlled review target) could spoof a
# NEED RESEARCH block and bypass the trust boundary, routing arbitrary text to Gemini.
extract_block() {
  local log="$1"
  awk '
    /^=== RESPONSE ===/  {response=1; next}
    !response            {next}
    /^=== END/           {response=0; exit}
    /^## NEED RESEARCH/  {capture=1; next}
    capture && /^## /    {capture=0}
    capture              {print}
  ' "$log"
}

# Helper: filter NEED RESEARCH bullets, drop non-actionable placeholders.
filter_questions() {
  grep -E '^- ' \
    | sed 's/^- //' \
    | grep -viE '^(not applicable|n/?a|none|nothing|no questions)\.?$' \
    | grep -v '^$' || true
}

echo "[route-codex] iter=0 — initial codex pass (focus: $FOCUS)" >&2
"$SCRIPT_DIR/ask-codex.sh" "$FOCUS"

ITER=0
while [ "$ITER" -lt "$MAX_ITER" ]; do
  ITER=$((ITER+1))
  CODEX_LOG="$LOG_DIR/latest-codex.log"
  if [ ! -f "$CODEX_LOG" ]; then
    echo "[route-codex] no codex log at $CODEX_LOG; stopping" >&2
    break
  fi

  BLOCK="$(extract_block "$CODEX_LOG")"
  QUESTIONS="$(printf '%s\n' "$BLOCK" | filter_questions)"

  if [ -z "$QUESTIONS" ]; then
    echo "[route-codex] iter=$ITER — no actionable NEED RESEARCH; stopping" >&2
    break
  fi

  Q_COUNT="$(printf '%s\n' "$QUESTIONS" | wc -l)"
  echo "[route-codex] iter=$ITER — $Q_COUNT research question(s); calling gemini" >&2

  GEMINI_QUERY="Research these factual questions to support a code review. Provide concise, sourced answers per question:

$QUESTIONS"

  # Run gemini; dashboard auto-renders its card. Discard stdout (already on dashboard)
  # and copy the structured log into the research file the codex 2nd pass will read.
  if ! "$SCRIPT_DIR/ask-gemini.sh" "$GEMINI_QUERY" >/dev/null; then
    echo "[route-codex] gemini call failed; stopping (partial response on dashboard)" >&2
    break
  fi

  RESEARCH_FILE="$TMPDIR_R/research-iter${ITER}.md"
  cp "$LOG_DIR/latest-gemini.log" "$RESEARCH_FILE"
  echo "[route-codex] iter=$ITER — research saved to $RESEARCH_FILE; re-invoking codex" >&2

  "$SCRIPT_DIR/ask-codex.sh" --with-research "$RESEARCH_FILE" "$FOCUS"
done

echo "[route-codex] done after $ITER iteration(s). Final response: $LOG_DIR/latest-codex.log" >&2
