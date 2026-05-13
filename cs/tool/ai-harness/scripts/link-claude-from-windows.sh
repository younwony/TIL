#!/bin/bash
# link-claude-from-windows.sh
#
# Windows의 ~/.claude/ 자산(skills, agents, commands, rules, CLAUDE.md)을
# WSL의 ~/.claude/ 로 심볼릭 링크합니다. 양쪽 자동 동기화.
#
# 사용 시나리오:
# - Windows에서 이미 Claude Code 자산 보유 (skills, agents, commands 등)
# - WSL에서도 같은 자산 사용하고 싶음
#
# 주의: WSL에서 새로 받은 인증, 세션, settings.json은 의도적으로 보존됩니다.
#       (OS별 분리되어야 정상)

set -e

# Windows 사용자명 자동 감지
WIN_USER=$(cmd.exe /c "echo %USERNAME%" 2>/dev/null | tr -d '\r\n' || echo "$USER")
WIN_CLAUDE="/mnt/c/Users/$WIN_USER/.claude"
WSL_CLAUDE="$HOME/.claude"

# 사용자 입력으로 override 가능
if [ -n "$1" ]; then
  WIN_CLAUDE="$1"
fi

if [ ! -d "$WIN_CLAUDE" ]; then
  echo "ERROR: Windows .claude 디렉토리 없음: $WIN_CLAUDE"
  echo ""
  echo "사용법:"
  echo "  $0                            # 자동 감지"
  echo "  $0 /mnt/c/Users/이름/.claude   # 수동 지정"
  exit 1
fi

echo "Windows .claude: $WIN_CLAUDE"
echo "WSL .claude:     $WSL_CLAUDE"
echo ""

# 백업
BACKUP="$HOME/.claude.backup-$(date +%Y%m%d-%H%M%S)"
echo "1. 현재 ~/.claude 백업..."
mkdir -p "$WSL_CLAUDE"
cp -r "$WSL_CLAUDE" "$BACKUP"
echo "   → $BACKUP"
echo ""

# Symlink 대상 (OS 비종속 자산만)
# 제외: settings.json, .credentials.json, sessions/, projects/, cache/, hooks/(OS-specific), plugins/
ITEMS=(
  "skills"
  "agents"
  "commands"
  "rules"
  "CLAUDE.md"
  "statusline.sh"
)

echo "2. 심볼릭 링크 생성:"
for item in "${ITEMS[@]}"; do
  src="$WIN_CLAUDE/$item"
  dst="$WSL_CLAUDE/$item"

  if [ ! -e "$src" ]; then
    echo "   - 건너뛰기: $item (Windows에 없음)"
    continue
  fi

  if [ -e "$dst" ] || [ -L "$dst" ]; then
    rm -rf "$dst"
  fi

  ln -s "$src" "$dst"
  echo "   ✓ $item → $src"
done

echo ""
echo "3. 결과 확인:"
ls -la "$WSL_CLAUDE/" | grep -E "skills|agents|commands|rules|CLAUDE.md|statusline.sh" || true

echo ""
echo "✅ 완료. 문제 시 복원:"
echo "   rm -rf $WSL_CLAUDE"
echo "   mv $BACKUP $WSL_CLAUDE"
