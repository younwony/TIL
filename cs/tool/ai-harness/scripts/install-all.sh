#!/bin/bash
# install-all.sh — WSL Ubuntu 안에서 한 번에 모든 셋업 실행
#
# 사전 조건:
# - WSL2 Ubuntu 24.04 설치 완료 및 진입 상태
# - tmux 설치됨 (Ubuntu 24.04 기본 포함)
#
# 실행:
#   cd <이 가이드 폴더>          # 예: cd /mnt/c/workspace/intellij/TIL/cs/tool/ai-harness
#   bash scripts/install-all.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "================================================"
echo "  AI 팀 셋업 자동 설치"
echo "================================================"
echo ""

# 0. 사전 체크
echo "[0/7] 사전 체크"
command -v tmux >/dev/null 2>&1 || { echo "ERROR: tmux 설치 필요. sudo apt install tmux"; exit 1; }
echo "  ✓ tmux: $(tmux -V)"
command -v curl >/dev/null 2>&1 || { echo "ERROR: curl 설치 필요"; exit 1; }
echo "  ✓ curl: 있음"
echo ""

# 1. tmux 설정
echo "[1/7] ~/.tmux.conf 설치"
if [ -f "$HOME/.tmux.conf" ]; then
  cp "$HOME/.tmux.conf" "$HOME/.tmux.conf.backup-$(date +%Y%m%d-%H%M%S)"
  echo "  → 기존 .tmux.conf 백업됨"
fi
cp "$SCRIPT_DIR/tmux.conf" "$HOME/.tmux.conf"
echo "  ✓ ~/.tmux.conf 적용"
echo ""

# 2. nvm + Node.js LTS
echo "[2/7] nvm + Node.js LTS 설치"
if [ -d "$HOME/.nvm" ]; then
  echo "  → nvm 이미 설치됨"
else
  curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.1/install.sh | bash
  echo "  ✓ nvm 설치 완료"
fi
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
if ! command -v node >/dev/null 2>&1; then
  nvm install --lts
  echo "  ✓ Node.js LTS 설치"
fi
echo "  ✓ Node: $(node -v)"
echo ""

# 3. AI CLI 3종
echo "[3/7] AI CLI 설치 (claude / codex / gemini)"
PACKAGES_NEEDED=""
command -v claude >/dev/null 2>&1 || PACKAGES_NEEDED="$PACKAGES_NEEDED @anthropic-ai/claude-code"
command -v codex >/dev/null 2>&1 || PACKAGES_NEEDED="$PACKAGES_NEEDED @openai/codex"
command -v gemini >/dev/null 2>&1 || PACKAGES_NEEDED="$PACKAGES_NEEDED @google/gemini-cli"

if [ -n "$PACKAGES_NEEDED" ]; then
  echo "  설치할 패키지: $PACKAGES_NEEDED"
  npm install -g $PACKAGES_NEEDED
fi
echo "  ✓ claude: $(claude --version 2>&1 | head -1)"
echo "  ✓ codex:  $(codex --version 2>&1 | head -1)"
echo "  ✓ gemini: $(gemini --version 2>&1 | head -1)"
echo ""

# 4. ai-team.sh / ai-session.sh
echo "[4/7] 세션 스크립트 설치"
cp "$SCRIPT_DIR/ai-team.sh" "$HOME/ai-team.sh"
cp "$SCRIPT_DIR/ai-session.sh" "$HOME/ai-session.sh"
echo "  ✓ ~/ai-team.sh 설치"
echo "  ✓ ~/ai-session.sh 설치"
echo ""

# 5. ai-harness-monitor 스킬 (가이드 폴더 안에 있으면 자동 복사)
echo "[5/7] ai-harness-monitor 스킬 배치"
SKILL_SRC="$(dirname "$SCRIPT_DIR")/ai-harness-monitor-skill"
SKILL_DST="$HOME/.claude/skills/ai-harness-monitor"
if [ -d "$SKILL_SRC" ]; then
  mkdir -p "$HOME/.claude/skills"
  if [ -e "$SKILL_DST" ] || [ -L "$SKILL_DST" ]; then
    echo "  → 기존 스킬 폴더 발견, 건너뜀: $SKILL_DST"
  else
    cp -r "$SKILL_SRC" "$SKILL_DST"
    chmod +x "$SKILL_DST/scripts/"*.sh 2>/dev/null || true
    echo "  ✓ 스킬 설치: $SKILL_DST"
  fi
else
  echo "  → ai-harness-monitor-skill 폴더 없음 (수동 배치 필요)"
fi
echo ""

# 6. .bashrc alias
echo "[6/7] alias 등록 (~/.bashrc)"
if ! grep -q "ai_session_name" "$HOME/.bashrc"; then
  cat "$SCRIPT_DIR/bashrc-aliases.sh" >> "$HOME/.bashrc"
  echo "  ✓ alias + 환경변수 등록 (ai, aik, air, ais, aia, aikall, ai-old)"
else
  echo "  → 이미 등록됨"
fi
echo ""

# 7. 안내
echo "[7/7] 완료"
echo ""
echo "================================================"
echo "  ✅ 셋업 완료"
echo "================================================"
echo ""
echo "다음 단계:"
echo "  1. 새 셸에서 'source ~/.bashrc' (또는 터미널 재시작)"
echo "  2. 'claude' 실행 → 인증"
echo "  3. 'codex' 실행 → 인증"
echo "  4. 'gemini' 실행 → 인증"
echo "  5. (선택) Harness 플러그인:"
echo "       claude > /plugin marketplace add revfactory/harness"
echo "       claude > /plugin install harness@harness-marketplace"
echo "  6. (선택) ai-harness-monitor 스킬을 ~/.claude/skills/ai-harness-monitor/ 에 배치"
echo "  7. 'ai' 실행 → 3분할 세션 시작"
echo ""
echo "사용법:"
echo "  ai       - 4분할 시작 (또는 attach)"
echo "  aik      - 종료"
echo "  air      - 재시작"
echo "  ais      - 상태"
echo "  aia      - attach"
echo "  aikall   - 모든 agents-* 일괄 종료"
echo "  ai-old   - 레거시 4분할 (인터랙티브 codex/gemini)"
