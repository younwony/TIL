#!/bin/bash
# block-dangerous-ssh.sh
# SSH/cmux 명령에 위험한 패턴이 포함되어 있으면 실행을 차단한다.
# PreToolUse(Bash) 훅으로 동작. stdin으로 JSON을 받는다.

INPUT=$(cat)
CMD=$(echo "$INPUT" | jq -r '.tool_input.command // ""')

# SSH 명령 또는 cmux send 명령에만 적용
if echo "$CMD" | grep -qE '(ssh\s|cmux\s+send)'; then

  DANGEROUS=$(echo "$CMD" | grep -oP \
    '\b(rm|mv|cp|chmod|chown|touch|ln)\b|\b(kill|pkill|killall)\b|\b(reboot|shutdown|halt|poweroff)\b|systemctl\s+(stop|restart|start|enable|disable)\b|\btee\b|\bwget\b|\bscp\b|\brsync\b|\beval\b|\bbash\s+-c\b|\bsh\s+-c\b|\bpython\s+-c\b|git\s+push\s+--force|git\s+reset\s+--hard|\bmkdir\b|\byum\s+(install|remove)\b|\bapt(-get)?\s+(install|remove)\b|\bpip\s+install\b' \
    2>/dev/null | head -1)

  if [ -n "$DANGEROUS" ]; then
    MSG="SSH 명령에 위험한 패턴이 포함되어 있습니다: '${DANGEROUS}'. 읽기 전용 명령만 허용됩니다."
    echo "{\"decision\": \"block\", \"reason\": \"${MSG}\"}"
    exit 0
  fi
fi

exit 0
