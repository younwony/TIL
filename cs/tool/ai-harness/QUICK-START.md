# QUICK START — 명령만 죽 모은 빠른 셋업

> 이 문서는 Windows + WSL2 환경에서 **Claude / Codex / Gemini 3개 AI를 한 화면(tmux 분할)에서 동시에 운영**하는 환경을 5분 안에 셋업하는 빠른 가이드입니다.
> 설명 거의 없이 명령만. 막히면 [README.md](README.md) 참조.
> Windows 11 + 관리자 권한 PowerShell 필요.

**모든 bash 명령은 이 가이드 폴더에서 실행하세요.**

```bash
# WSL Ubuntu 진입 후, TIL 저장소를 clone 했다면:
cd /mnt/c/workspace/intellij/TIL/cs/tool/ai-harness

# 또는 임의 위치에 풀었다면:
cd /path/to/ai-harness
```

## 1. WSL2 + Ubuntu 24.04 설치 (PowerShell 관리자)

```powershell
wsl --install -d Ubuntu-24.04
```

재부팅 → 사용자명/비밀번호 입력 → 다음 단계.

## 2. WSL Ubuntu 진입

```powershell
wsl -d Ubuntu-24.04
```

또는 Windows Terminal에서 새 탭 → Ubuntu 선택.

## 3. 자동 셋업 스크립트 실행

이 가이드 폴더로 cd한 상태에서:

```bash
bash scripts/install-all.sh
```

이 스크립트가 자동 처리:
- `~/.tmux.conf` 적용
- nvm + Node.js LTS 설치
- claude / codex / gemini npm 패키지 설치
- `~/ai-team.sh`, `~/ai-session.sh` 설치
- `~/.bashrc`에 alias + 환경변수 추가
- `ai-harness-monitor-skill/` 폴더를 `~/.claude/skills/ai-harness-monitor/`로 자동 복사

## 4. AI 인증 (각 1회씩)

```bash
source ~/.bashrc          # 새 alias 적용

# 차례로 (각각 인증 흐름 따라가기)
claude                    # Anthropic 로그인
# /exit 후
codex                     # OpenAI 로그인
# Ctrl+C 후
gemini                    # Google 로그인
# Ctrl+C
```

## 5. (선택) Windows의 Claude 자산 동기화

이미 Windows에 `~/.claude/` 자산(skills, agents, commands 등)이 있다면:

```bash
bash scripts/link-claude-from-windows.sh
```

## 6. Harness 플러그인 (선택)

```bash
claude --permission-mode bypassPermissions
```

claude 안에서:
```
/plugin marketplace add revfactory/harness
/plugin install harness@harness-marketplace
/reload-plugins
/exit
```

## 7. ai-harness-monitor 스킬 (install-all.sh가 자동 처리)

`install-all.sh`가 `~/.claude/skills/ai-harness-monitor/`에 자동 배치합니다.
수동으로 다시 해야 한다면:

```bash
cp -r ai-harness-monitor-skill ~/.claude/skills/ai-harness-monitor
chmod +x ~/.claude/skills/ai-harness-monitor/scripts/*.sh
```

폴더 구조:

```
~/.claude/skills/ai-harness-monitor/
  ├── SKILL.md
  ├── roles/
  └── scripts/
      ├── team-layout.sh
      ├── dashboard.sh
      ├── ask-gemini.sh
      ├── ask-codex.sh
      ├── ask-both.sh
      └── route-codex.sh
```

## 8. Windows Terminal 기본 프로필 → Ubuntu

`%LOCALAPPDATA%\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\LocalState\settings.json`

`defaultProfile`을 Ubuntu GUID로 변경. (정확한 GUID는 자기 PC의 settings.json에서 확인)

## 9. IntelliJ IDEA 터미널 설정

```
Settings → Tools → Terminal → Shell path:  wsl.exe -d Ubuntu-24.04
```

VSCode는:
```
Settings → "Terminal Integrated Default Profile" → Ubuntu-24.04 (WSL)
```

## 10. 첫 실행

```bash
cd /mnt/c/projects/myapp    # 프로젝트로 이동
ai                          # 3분할 세션 자동 생성
```

## 명령 치트시트

```
ai          시작/attach (현재 cwd 기반 세션)
aik         현재 세션 종료
air         재시작
ais         상태 확인
aia         attach
aikall      모든 agents-* 일괄 종료
ai-old      레거시 4분할 (인터랙티브)

Ctrl+a → d     detach
Ctrl+a → z     패널 전체화면 토글
Ctrl+a → 방향키 패널 이동
Ctrl+a → [     스크롤 모드 (q로 종료)
```

## 막히면

- [README.md의 트러블슈팅 섹션](README.md#트러블슈팅)

## 다음 PC에서 동일 셋업 한 줄로

이 가이드 폴더가 git 저장소에 있으면(TIL 기준 `cs/tool/ai-harness/`):

```bash
# 새 PC: WSL Ubuntu 진입 후
git clone <TIL-repo-url>
cd <TIL>/cs/tool/ai-harness
bash scripts/install-all.sh
```

그게 다입니다. 인증 흐름(Phase 4)만 새로 거치면 됩니다.
