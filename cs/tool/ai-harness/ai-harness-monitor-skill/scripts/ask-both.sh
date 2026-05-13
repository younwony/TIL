#!/usr/bin/env bash
# ask-both.sh — call Gemini and Codex in parallel for cross-checking.
#
# Workflow:
#   1. fork ask-gemini.sh and ask-codex.sh in parallel (stdout captured to tmp files)
#   2. wait for both
#   3. print labeled blocks: GEMINI section, then CODEX section, then DONE summary
#
# Each side keeps its own dashboard card (right-top = gemini, right-bottom = codex);
# both cards light up together.
#
# Usage:
#   ask-both.sh "question or focus"
#   ask-both.sh --raw "1+1은?"          # bypass researcher/reviewer persona on both sides
#
# Modes:
#   default — gemini = researcher persona, codex = reviewer persona.
#             Best for code review cross-check (e.g. "review src/foo.rs").
#   --raw   — both raw. Best for ad-hoc factual cross-check (e.g. "1+1", "tokio version?").
#
# Exit: 0 if at least one side succeeds. 1 if both fail.
#
# 글로벌 스킬 ai-harness-monitor. 작성: 2026-05-11.
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

RAW=0
while [ "$#" -gt 0 ]; do
  case "$1" in
    --raw) RAW=1; shift ;;
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
  echo "usage: $0 [--raw] \"question or focus\"" >&2
  exit 2
fi

QUERY="$1"
TMPDIR_B="$(mktemp -d)"
trap 'rm -rf "$TMPDIR_B"' EXIT

GEMINI_OUT="$TMPDIR_B/gemini.out"
CODEX_OUT="$TMPDIR_B/codex.out"

START_TS="$(date +%s)"

if [ "$RAW" = "1" ]; then
  echo "[ask-both] dispatching gemini + codex in parallel (raw mode)" >&2
  "$SCRIPT_DIR/ask-gemini.sh" --raw "$QUERY" >"$GEMINI_OUT" 2>&1 &
  G_PID=$!
  "$SCRIPT_DIR/ask-codex.sh" --raw "$QUERY" >"$CODEX_OUT" 2>&1 &
  C_PID=$!
else
  echo "[ask-both] dispatching gemini (researcher) + codex (reviewer) in parallel" >&2
  "$SCRIPT_DIR/ask-gemini.sh" "$QUERY" >"$GEMINI_OUT" 2>&1 &
  G_PID=$!
  "$SCRIPT_DIR/ask-codex.sh" "$QUERY" >"$CODEX_OUT" 2>&1 &
  C_PID=$!
fi

# wait returns the rc of the named PID under bash; capture each separately.
wait "$G_PID"; G_RC=$?
wait "$C_PID"; C_RC=$?

END_TS="$(date +%s)"
ELAPSED=$((END_TS - START_TS))

printf '\n=== GEMINI (rc=%d) ===\n' "$G_RC"
cat "$GEMINI_OUT"
printf '\n=== CODEX (rc=%d) ===\n' "$C_RC"
cat "$CODEX_OUT"
printf '\n=== DONE — gemini rc=%d, codex rc=%d, elapsed=%ds ===\n' "$G_RC" "$C_RC" "$ELAPSED"

if [ "$G_RC" -ne 0 ] && [ "$C_RC" -ne 0 ]; then
  exit 1
fi
exit 0
