#!/usr/bin/env bash
# dashboard.sh — live, formatted summary view of gemini/codex wrapper runs.
#
# Usage:
#   dashboard.sh gemini    # render Gemini researcher status
#   dashboard.sh codex     # render Codex reviewer status
#
# Run this in a side tmux pane. Re-renders only when the source log changes
# (no flicker), and shows distilled key points — the full raw output stays
# in the Claude (PM) pane and on disk.
#
# Controls:
#   l       open full log in less (q to return)
#   space   toggle pause (auto-refresh on/off)
#   q       quit
#   Ctrl-C  also quits
#
# 원본: pandas-studio/agent-harness-tutorial (ep_a_demo/.agents-dev/scripts/dashboard.sh)
# 글로벌 스킬로 채택. 변경 없음.
set -uo pipefail

ROLE="${1:?usage: $0 gemini|codex}"

case "$ROLE" in
  gemini)
    ICON="🔍"; TITLE="GEMINI · researcher"
    HEADER_COLOR=$'\033[1;36m'   # bright cyan
    LABEL="Query"
    ;;
  codex)
    ICON="🧐"; TITLE="CODEX · reviewer"
    HEADER_COLOR=$'\033[1;35m'   # bright magenta
    LABEL="Focus"
    ;;
  *)
    echo "usage: $0 gemini|codex" >&2; exit 2 ;;
esac

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
AGENTS_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

# Team namespace — must match what wrappers use.
# Priority: $AGENT_TEAM env > tmux @team-name window option > tmux session name > "default"
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
LATEST="$LOG_DIR/latest-$ROLE.log"

# Metrics — count total runs, failures, and (codex only) cumulative tokens.
# Cheap: ls + grep over filenames; fast enough at <1000 logs to run every tick.
# Gemini CLI doesn't emit token counts → TOKENS stays 0 for gemini.
compute_metrics() {
  TOTAL=0
  FAIL=0
  TOKENS=0
  # nullglob to avoid the literal pattern being counted when no matches
  shopt -s nullglob
  local logs=("$LOG_DIR/${ROLE}-"*.log)
  shopt -u nullglob
  TOTAL=${#logs[@]}
  [ "$TOTAL" -eq 0 ] && return
  local f rc t
  for f in "${logs[@]}"; do
    rc=$(grep '^=== END ' "$f" 2>/dev/null | tail -1 | sed -nE 's/.*rc=([0-9]+).*/\1/p')
    [ -n "$rc" ] && [ "$rc" != "0" ] && FAIL=$((FAIL+1))
    if [ "$ROLE" = "codex" ]; then
      # codex CLI prints "tokens used\n<NUMBER>" near the end of each response.
      t=$(awk '/^tokens used$/ { getline tt; gsub(/,/,"",tt); if (tt ~ /^[0-9]+$/) { print tt; exit } }' "$f" 2>/dev/null)
      [ -n "$t" ] && TOKENS=$((TOKENS + t))
    fi
  done
}

# Format a token count compactly: 88_777 → "88k", 1_234_567 → "1.2M"
format_tokens() {
  local n="$1"
  if [ "$n" -ge 1000000 ]; then
    # one decimal for M, integer otherwise
    printf '%d.%dM' $((n / 1000000)) $(( (n % 1000000) / 100000 ))
  elif [ "$n" -ge 1000 ]; then
    printf '%dk' $((n / 1000))
  else
    printf '%d' "$n"
  fi
}

# Recent N — render up to N most recent runs as one-line summaries.
# Each line: <mark> <timestamp> "<truncated query/focus>"
format_recent() {
  local n="${1:-3}"
  shopt -s nullglob
  local logs=("$LOG_DIR/${ROLE}-"*.log)
  shopt -u nullglob
  [ "${#logs[@]}" -eq 0 ] && return
  # Sort by filename descending (filenames embed timestamp YYYYMMDD-HHMMSS → lexicographic = chronological).
  local sorted
  sorted=$(printf '%s\n' "${logs[@]}" | sort -r | head -"$n")
  local f base ts rc mark q
  while IFS= read -r f; do
    [ -z "$f" ] && continue
    base=$(basename "$f")
    ts=${base#${ROLE}-}
    ts=${ts%.log}
    rc=$(grep '^=== END ' "$f" 2>/dev/null | tail -1 | sed -nE 's/.*rc=([0-9]+).*/\1/p')
    if [ -z "$rc" ]; then
      mark="${YELLOW}⏳${RESET}"
    elif [ "$rc" = "0" ]; then
      mark="${GREEN}✓${RESET}"
    else
      mark="${RED}✗${RESET}"
    fi
    if [ "$ROLE" = "gemini" ]; then
      q=$(awk '/^=== QUERY ===$/{flag=1; next} /^=== /{flag=0} flag' "$f" 2>/dev/null | head -1)
    else
      q=$(awk '/^=== FOCUS ===$/{flag=1; next} /^=== /{flag=0} flag' "$f" 2>/dev/null | head -1)
    fi
    # Truncate to ~50 chars
    if [ "${#q}" -gt 50 ]; then q="${q:0:47}..."; fi
    [ -z "$q" ] && q="(no query)"
    printf '    %s  %s  %s\n' "$mark" "$ts" "$q"
  done <<< "$sorted"
}

RESET=$'\033[0m'
DIM=$'\033[2m'
BOLD=$'\033[1m'
GREEN=$'\033[1;32m'
YELLOW=$'\033[1;33m'
RED=$'\033[1;31m'
CYAN=$'\033[1;36m'

cleanup() { printf '\033[?25h\033[H\033[2J'; exit 0; }   # show cursor + clear
trap cleanup INT TERM
printf '\033[?25l'   # hide cursor

get_wrap_width() {
  local c
  c=$(tput cols 2>/dev/null || echo 80)
  [ "$c" -lt 40 ] && c=40
  echo $((c - 6))
}

LAST_HASH=""
PAUSED=0

while true; do
  if [ "$PAUSED" = "0" ]; then
    WRAP_W=$(get_wrap_width)
    BUF=""
    # Compact header: 1 title line + 1 separator. Keeps narrow panes (gemini) viewable.
    # ~/ prefix + truncate-from-left so home-anchored long paths stay readable.
    CWD_DISPLAY="${PWD/#$HOME/\~}"
    if [ "${#CWD_DISPLAY}" -gt 32 ]; then
      CWD_DISPLAY="...${CWD_DISPLAY: -29}"
    fi
    BUF+="  ${HEADER_COLOR}${ICON} ${TITLE}${RESET}  ${DIM}[${TEAM} · ${CWD_DISPLAY}]${RESET}"$'\n'
    BUF+="${HEADER_COLOR}═══════════════════════════════════════════════${RESET}"$'\n'

    # Metrics line (always shown, even before first run for this role).
    compute_metrics
    if [ "$TOTAL" -gt 0 ]; then
      FAIL_PCT=$(( FAIL * 100 / TOTAL ))
      TOKEN_SEG=""
      if [ "$ROLE" = "codex" ] && [ "$TOKENS" -gt 0 ]; then
        TOKEN_SEG=" · $(format_tokens "$TOKENS") tokens"
      fi
      if [ "$FAIL" -gt 0 ]; then
        BUF+="  ${DIM}Metrics:${RESET} ${TOTAL} runs · ${RED}${FAIL} failed${RESET} (${FAIL_PCT}%)${TOKEN_SEG}"$'\n\n'
      else
        BUF+="  ${DIM}Metrics:${RESET} ${TOTAL} runs · ${GREEN}0 failed${RESET}${TOKEN_SEG}"$'\n\n'
      fi
    else
      BUF+="  ${DIM}Metrics: 0 runs${RESET}"$'\n\n'
    fi

    if [ ! -e "$LATEST" ]; then
      BUF+="  ${DIM}(no runs yet — waiting for first call)${RESET}"$'\n'
      BUF+="  ${DIM}path: $LATEST${RESET}"$'\n\n'
    else
      TS=$(grep "^=== ask-${ROLE}.sh @ " "$LATEST" 2>/dev/null | tail -1 | awk '{print $4}')
      BUF+="  ${BOLD}Started:${RESET} ${TS:-unknown}"$'\n\n'

      # Query/Focus body
      if [ "$ROLE" = "gemini" ]; then
        BODY=$(awk '/^=== QUERY ===$/{flag=1; next} /^=== /{flag=0} flag' "$LATEST" 2>/dev/null)
      else
        BODY=$(awk '/^=== FOCUS ===$/{flag=1; next} /^=== /{flag=0} flag' "$LATEST" 2>/dev/null)
      fi
      BUF+="  ${BOLD}${LABEL}:${RESET}"$'\n'
      if [ -n "$BODY" ]; then
        WRAPPED=$(echo "$BODY" | fold -s -w "$WRAP_W" | head -5)
        while IFS= read -r line; do BUF+="    $line"$'\n'; done <<< "$WRAPPED"
      fi
      BUF+=$'\n'

      # Extract real response (codex echoes its prompt + duplicates final
      # response after "tokens used"; gemini doesn't have such framing).
      RESPONSE=""
      if [ "$ROLE" = "codex" ]; then
        RESPONSE=$(awk '/^tokens used/{flag=1; next} /^=== END /{flag=0} flag' "$LATEST" 2>/dev/null)
      fi
      if [ -z "$RESPONSE" ]; then
        RESPONSE=$(awk '/^=== RESPONSE ===$/{flag=1; next} /^=== END /{flag=0} flag' "$LATEST" 2>/dev/null)
      fi

      # Status (END marker = done)
      DONE=0; RC=""
      if grep -q '^=== END ' "$LATEST" 2>/dev/null; then
        DONE=1
        RC=$(grep '^=== END ' "$LATEST" | tail -1 | sed 's/.*rc=\([0-9]*\).*/\1/')
        if [ "$RC" = "0" ]; then
          BUF+="  ${BOLD}Status:${RESET} ${GREEN}✓ done${RESET}"$'\n\n'
        else
          BUF+="  ${BOLD}Status:${RESET} ${RED}✗ failed (rc=$RC)${RESET}"$'\n\n'
        fi
      else
        BUF+="  ${BOLD}Status:${RESET} ${YELLOW}⏳ running...${RESET}"$'\n\n'
      fi

      # ── Role-specific summary ────────────────────────────────────────────
      if [ "$ROLE" = "gemini" ] && [ "$DONE" = "1" ]; then
        # Lead: first paragraph of answer (skip Gemini CLI preamble lines).
        # Noise sources to filter:
        #   - "Warning:" lines (TERM/truecolor warnings) + their indented continuations
        #   - "Ripgrep is not available." / "Falling back to GrepTool."
        LEAD=$(echo "$RESPONSE" | awk '
          BEGIN { started=0; skip_block=0 }
          /^Warning:|^Ripgrep|^Falling back/ { skip_block=1; next }
          skip_block && /^[[:space:]]/    { next }
          skip_block && /^[^[:space:]]/   { skip_block=0 }
          skip_block                       { next }
          /^[^[:space:]]/ {
            if (!started) started=1
            if (started) print
          }
          started && /^$/ { exit }
        ')
        if [ -n "$LEAD" ]; then
          BUF+="  ${BOLD}Answer (lead):${RESET}"$'\n'
          WRAPPED=$(echo "$LEAD" | fold -s -w "$WRAP_W" | head -8)
          while IFS= read -r line; do
            BUF+="    ${line}"$'\n'
          done <<< "$WRAPPED"
          BUF+=$'\n'
        fi

        # Source count (URLs cited)
        SRC_COUNT=$(echo "$RESPONSE" | grep -cE 'https?://' || true)
        SRC_COUNT=${SRC_COUNT//[^0-9]/}; SRC_COUNT=${SRC_COUNT:-0}
        BUF+="  ${BOLD}Sources cited:${RESET} ${SRC_COUNT}"$'\n\n'

      elif [ "$ROLE" = "codex" ] && [ "$DONE" = "1" ]; then
        # Verdict bar
        VERDICT_LINE=$(echo "$RESPONSE" | grep -A 1 '^## Verdict' 2>/dev/null | tail -1 | sed 's/^[[:space:]]*//')
        if [ -n "$VERDICT_LINE" ]; then
          VERB=$(echo "$VERDICT_LINE" | awk '{print $1}')
          case "$VERB" in
            SHIP)      VC="$GREEN" ;;
            NEEDS-FIX) VC="$RED" ;;
            DISCUSS)   VC="$YELLOW" ;;
            *)         VC="$CYAN" ;;
          esac
          VWRAP=$(echo "$VERDICT_LINE" | fold -s -w $((WRAP_W - 8)))
          FIRST=1
          while IFS= read -r line; do
            if [ "$FIRST" = "1" ]; then
              BUF+="  ${VC}┃${RESET} ${BOLD}Verdict:${RESET} $line"$'\n'
              FIRST=0
            else
              BUF+="  ${VC}┃${RESET}   $line"$'\n'
            fi
          done <<< "$VWRAP"
          BUF+=$'\n'
        fi

        # Section-aware findings extraction
        SECS=$(echo "$RESPONSE" | awk '
          /^### Blocker/ { sec="bl"; next }
          /^### Major/   { sec="mj"; next }
          /^### Minor/   { sec="mn"; next }
          /^## /         { sec=""; next }
          sec=="bl" && /^- / && tolower($0) !~ /^- none/ { print "BL:" $0; next }
          sec=="mj" && /^- / && tolower($0) !~ /^- none/ { print "MJ:" $0; next }
          sec=="mn" && /^- / && tolower($0) !~ /^- none/ { print "MN:" $0; next }
        ' 2>/dev/null)
        BL=$(echo "$SECS" | grep -c '^BL:' 2>/dev/null || true); BL=${BL//[^0-9]/}; BL=${BL:-0}
        MJ=$(echo "$SECS" | grep -c '^MJ:' 2>/dev/null || true); MJ=${MJ//[^0-9]/}; MJ=${MJ:-0}
        MN=$(echo "$SECS" | grep -c '^MN:' 2>/dev/null || true); MN=${MN//[^0-9]/}; MN=${MN:-0}

        BUF+="  ${BOLD}Findings:${RESET} ${RED}${BL} blocker${RESET} · ${YELLOW}${MJ} major${RESET} · ${DIM}${MN} minor${RESET}"$'\n\n'

        if [ "$BL" -gt 0 ] || [ "$MJ" -gt 0 ]; then
          BUF+="  ${BOLD}${RED}Blockers + Major:${RESET}"$'\n'
          BLMJ=$(echo "$SECS" | grep -E '^(BL|MJ):' | sed 's/^BL://; s/^MJ://')
          WRAPPED=$(echo "$BLMJ" | fold -s -w "$WRAP_W")
          while IFS= read -r line; do
            [ -n "$line" ] && BUF+="    $line"$'\n'
          done <<< "$WRAPPED"
          BUF+=$'\n'
        fi
      fi

      REAL=$(readlink "$LATEST" 2>/dev/null || basename "$LATEST")
      BUF+="  ${DIM}log: $REAL${RESET}"$'\n'
    fi

    # Recent runs (up to 3) — appears whether or not LATEST exists, as long as some runs do.
    if [ "$TOTAL" -gt 0 ]; then
      RECENT_TXT=$(format_recent 3)
      if [ -n "$RECENT_TXT" ]; then
        BUF+=$'\n'
        BUF+="  ${BOLD}Recent:${RESET}"$'\n'
        while IFS= read -r line; do
          [ -n "$line" ] && BUF+="$line"$'\n'
        done <<< "$RECENT_TXT"
      fi
    fi

    # Bottom control hint
    BUF+=$'\n'
    BUF+="  ${DIM}controls: ${BOLD}l${RESET}${DIM}=full log · ${BOLD}space${RESET}${DIM}=pause · ${BOLD}q${RESET}${DIM}=quit${RESET}"$'\n'

    # Flicker-free render: only redraw if content changed.
    # Use cursor-home + per-line erase-EOL + erase-to-end-of-screen instead
    # of a full \033[2J clear, so even when redrawing there's no visible blink.
    HASH=$(printf '%s' "$BUF" | cksum 2>/dev/null | awk '{print $1}')
    if [ "$HASH" != "$LAST_HASH" ]; then
      RENDERED="${BUF//$'\n'/$'\033[K\n'}"
      printf '\033[H%s\033[J' "$RENDERED"
      LAST_HASH="$HASH"
    fi
  fi

  # Wait up to 1s for keypress (also serves as the polling cadence).
  KEY=""
  IFS= read -rs -t 1 -n 1 KEY 2>/dev/null || true
  case "$KEY" in
    l)
      # Hand off to less for scrollable full-log view.
      printf '\033[?25h\033[H\033[2J'
      if [ -e "$LATEST" ]; then
        less -R "$LATEST" || true
      else
        echo "(no log yet — waiting for first call)"; sleep 1
      fi
      printf '\033[?25l'
      LAST_HASH=""   # force redraw on return
      ;;
    ' ')
      if [ "$PAUSED" = "0" ]; then
        PAUSED=1
        printf "\n  %s[PAUSED]%s press space to resume — Ctrl-b [ to scroll\n" "$YELLOW" "$RESET"
        # Show cursor while paused so tmux scroll feels normal
        printf '\033[?25h'
      else
        PAUSED=0
        printf '\033[?25l'
        LAST_HASH=""   # force redraw
      fi
      ;;
    q) cleanup ;;
  esac
done
