# AI 팀 셋업 가이드 — Windows + WSL2 + tmux + Claude·Codex·Gemini

> 이 문서는 한 PC에서 **Claude / Codex / Gemini 세 AI가 한 화면(tmux 3분할)에서 협업**하는 환경을 처음부터 끝까지 셋업하는 가이드입니다. 메인 Claude(PM)가 Gemini(리서치)/Codex(리뷰)를 라이브로 디스패치하고, 우측 패널의 dashboard가 호출 카드를 실시간으로 보여줍니다.

## 한 줄 요약

`ai` 한 글자로 아래 화면이 즉시 뜹니다. 같은 셋업이면 어느 프로젝트 폴더에서 실행해도 자동으로 프로젝트별 세션(`agents-<프로젝트명>`)이 분리됩니다.

```
┌─────────────────────────┬──────────────────────────┐
│                         │  🔍 GEMINI · researcher  │
│  Claude (메인 PM)       │  dashboard               │
│  자동 시작              │  ← claude의 gemini 호출  │
│                         │    카드 형태 라이브 표시 │
│                         ├──────────────────────────┤
│                         │  🧐 CODEX · reviewer     │
│                         │  dashboard               │
│                         │  ← claude의 codex 호출   │
│                         │    카드 형태 라이브 표시 │
└─────────────────────────┴──────────────────────────┘
```

## 사전 안내 — 가이드 폴더 위치

이 가이드의 모든 명령은 **이 폴더로 cd 한 상태**를 가정합니다.

```bash
# TIL 저장소를 WSL의 /mnt/c/workspace/intellij/TIL 에 클론했다면:
cd /mnt/c/workspace/intellij/TIL/cs/tool/ai-harness

# 다른 곳이면 본인 경로에 맞춰:
cd /path/to/ai-harness
```

이후 `bash scripts/install-all.sh` 같은 명령을 그대로 실행하면 됩니다.

## 빠른 시작 (이미 셋업 후)

```bash
# WSL Ubuntu에서
cd /mnt/c/projects/myapp    # 프로젝트 디렉토리
ai                          # 자동으로 'agents-myapp' 세션 생성 + 3분할
```

## 전체 셋업 순서

| Phase | 내용 | 소요 |
|-------|------|------|
| 0 | 사전 요구사항 확인 | 1분 |
| 1 | WSL2 + Ubuntu 24.04 설치 | 10분 |
| 2 | tmux 설정 | 2분 |
| 3 | Node.js + AI CLI 3종 설치 | 10분 |
| 4 | AI 인증 (claude/codex/gemini) | 5분 |
| 5 | Claude Code 자산 동기화 (선택) | 3분 |
| 6 | Harness 플러그인 + ai-harness-monitor 스킬 | 5분 |
| 7 | alias 등록 | 1분 |
| 8 | Windows Terminal 기본 프로필 | 3분 |
| 9 | IntelliJ IDEA / VSCode 터미널 통합 | 3분 |
| - | 검증 | 5분 |

**총 소요: 약 50분** (다운로드 시간 포함)

> Phase 2~7은 `scripts/install-all.sh` 한 번으로 자동 처리됩니다.

---

## Phase 0: 사전 요구사항

| 항목 | 요구 |
|------|------|
| OS | Windows 10 빌드 19041+ 또는 Windows 11 |
| 디스크 | 최소 5GB 여유 (WSL Ubuntu + Node + AI CLI) |
| 메모리 | 최소 8GB 권장 |
| 관리자 권한 | WSL 설치 시 필요 |
| 네트워크 | npm/GitHub 다운로드 가능 |
| 계정 | Anthropic / OpenAI / Google 계정 (AI 인증용) |

확인:

```powershell
# PowerShell 관리자
winver                              # Windows 버전 확인
wsl --version                       # WSL 설치 여부
```

---

## Phase 1: WSL2 + Ubuntu 24.04 설치

### 1-1. WSL 설치 (이미 있으면 건너뛰기)

PowerShell **관리자** 권한:

```powershell
wsl --install
```

재부팅 후 자동으로 진행됨.

### 1-2. Ubuntu 24.04 설치

```powershell
wsl --install -d Ubuntu-24.04
```

설치 끝나면 사용자명과 비밀번호 입력 프롬프트가 뜹니다.
- 사용자명: 짧고 영문 (예: `wony9324`)
- 비밀번호: 외울 수 있는 것 (sudo 사용 시 가끔 필요)

### 1-3. 진입 테스트

```powershell
wsl -d Ubuntu-24.04
```

프롬프트가 `사용자명@PC:~$` 모양으로 바뀌면 성공. 빠져나오기: `exit`

---

## Phase 2: tmux 설정

### 2-1. tmux 설치 확인

WSL Ubuntu 안에서:

```bash
tmux -V
```

`tmux 3.4` (또는 그 이상) 나오면 OK. 안 깔려 있으면:

```bash
sudo apt update && sudo apt install -y tmux
```

### 2-2. `~/.tmux.conf` 적용

이 가이드 폴더의 `scripts/tmux.conf`를 사용:

```bash
cp scripts/tmux.conf ~/.tmux.conf
```

내용 확인:

```bash
cat ~/.tmux.conf | head -10
```

`set -g prefix C-a` 등이 보이면 적용됨.

---

## Phase 3: Node.js + AI CLI 설치

### 3-1. nvm 설치

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.1/install.sh | bash
source ~/.bashrc
```

확인:

```bash
nvm --version
# 0.40.1
```

### 3-2. Node.js LTS 설치

```bash
nvm install --lts
node -v
# v22.x 또는 v24.x
```

### 3-3. AI CLI 3종 설치

```bash
npm install -g @anthropic-ai/claude-code @openai/codex @google/gemini-cli
```

확인:

```bash
claude --version    # 2.x.x (Claude Code)
codex --version     # codex-cli 0.x.x
gemini --version    # 0.x.x
```

---

## Phase 4: AI 인증

각 CLI를 한 번씩 실행해서 인증 흐름을 끝냅니다.

### 4-1. Claude Code

```bash
claude
```

브라우저가 자동으로 열림 (WSL이 Windows 브라우저 호출). Anthropic 계정으로 로그인 → 인증 코드 입력. 끝나면 `/exit`로 종료.

### 4-2. Codex

```bash
codex
```

OpenAI 계정 로그인 또는 API key 입력. `Ctrl+C`로 종료.

### 4-3. Gemini

```bash
gemini
```

Google 계정 OAuth → 브라우저에서 로그인. 끝나면 `Ctrl+C`로 종료.

---

## Phase 5: Claude Code 자산 동기화 (선택)

다른 PC에 이미 셋업된 Claude Code 자산(skills, agents, commands 등)이 있으면 가져옵니다.

### 시나리오 A: Windows에 자산이 있고 WSL에 공유

```bash
bash scripts/link-claude-from-windows.sh
```

자동으로 Windows의 `~/.claude/`를 WSL `~/.claude/`로 symlink. 양쪽 자동 동기화.

### 시나리오 B: 팀 git 저장소에서 받기

```bash
git clone <팀의-claude-자산-저장소> ~/.claude-team
ln -s ~/.claude-team/skills ~/.claude/skills
ln -s ~/.claude-team/agents ~/.claude/agents
# ... 등
```

### 시나리오 C: 완전 새 셋업

이 단계 건너뛰고 다음 Phase 진행. 빈 상태에서 Phase 6의 Harness가 일부 자산 생성.

---

## Phase 6: Harness + ai-harness-monitor 스킬

### 6-1. 환경변수 활성화 (에이전트 팀 기능)

```bash
echo 'export CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1' >> ~/.bashrc
source ~/.bashrc
```

### 6-2. Harness 플러그인 설치

claude 실행 후:

```bash
claude --permission-mode bypassPermissions
```

claude 안에서:

```
/plugin marketplace add revfactory/harness
/plugin install harness@harness-marketplace
/reload-plugins
```

설치 확인:

```
/plugins
```

### 6-3. ai-harness-monitor 스킬 배치

영상 패턴(메인 Claude + Gemini/Codex 라이브 dashboard)을 구현하는 자체 스킬.

> `install-all.sh`가 자동 처리하므로 보통 이 단계는 건너뜁니다. 수동으로 다시 해야 할 때만 사용.

**이 가이드 폴더 안의 `ai-harness-monitor-skill/` 폴더를 그대로 복사**:

```bash
# 이 가이드 폴더에서 실행
cp -r ai-harness-monitor-skill ~/.claude/skills/ai-harness-monitor
```

폴더 구조:
```
~/.claude/skills/ai-harness-monitor/
├── SKILL.md
├── roles/
│   ├── researcher.md
│   └── reviewer.md
├── scripts/
│   ├── team-layout.sh
│   ├── dashboard.sh
│   ├── ask-gemini.sh
│   ├── ask-codex.sh
│   ├── ask-both.sh
│   └── route-codex.sh
└── log/                            # 호출 로그 자동 생성
```

> 이 스킬의 원본: `pandas-studio/agent-harness-tutorial` 리포의 `ep_a_demo/.agents-dev/scripts/`를 글로벌 스킬로 채택한 버전.

설치 확인:

```bash
ls ~/.claude/skills/ai-harness-monitor/scripts/
# team-layout.sh, dashboard.sh, ask-gemini.sh, ask-codex.sh, ask-both.sh, route-codex.sh

chmod +x ~/.claude/skills/ai-harness-monitor/scripts/*.sh
```

---

## Phase 7: 세션 스크립트 + alias 등록

### 7-1. 세션 스크립트 복사

```bash
cp scripts/ai-team.sh ~/ai-team.sh
cp scripts/ai-session.sh ~/ai-session.sh
chmod +x ~/ai-team.sh ~/ai-session.sh
```

### 7-2. alias 등록

```bash
cat scripts/bashrc-aliases.sh >> ~/.bashrc
source ~/.bashrc
```

### 7-3. 자동 설치 스크립트 (Phase 2~7 통합)

위 단계들을 한 번에 처리:

```bash
bash scripts/install-all.sh
```

---

## Phase 8: Windows Terminal 기본 프로필 설정

매번 `wsl` 입력 안 하고 새 창에서 즉시 Ubuntu로 진입.

### 8-1. settings.json 열기

`Ctrl + ,` (Windows Terminal 안에서) 또는:

```
%LOCALAPPDATA%\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\LocalState\settings.json
```

### 8-2. Ubuntu GUID 확인

Windows Terminal이 WSL을 인식하면 자동으로 `profiles.list`에 Ubuntu 추가됨:

```json
{
  "guid": "{e6b5e1d2-3022-5f02-b0b3-4a8aedda5bad}",
  "name": "Ubuntu-24.04",
  "source": "Microsoft.WSL"
}
```

GUID는 PC마다 다름. 본인 PC에서 확인 후 사용.

### 8-3. defaultProfile 변경

settings.json 최상위:

```json
"defaultProfile": "{ubuntu-guid-여기}",
```

### 8-4. (선택) 시작 디렉토리, 폰트 등 커스터마이징

해당 Ubuntu 프로필에 추가:

```json
{
  "guid": "{...}",
  "name": "Ubuntu-24.04",
  "source": "Microsoft.WSL",
  "startingDirectory": "//wsl.localhost/Ubuntu-24.04/home/사용자명",
  "colorScheme": "Campbell",
  "font": { "face": "Cascadia Mono", "size": 11 }
}
```

### 8-5. 적용

Windows Terminal 완전 종료 후 재실행. 새 창 열면 자동으로 Ubuntu 진입.

---

## Phase 9: IntelliJ IDEA / VSCode 터미널 통합

IDE의 내장 터미널을 WSL로 설정 → 프로젝트 cwd 자동 인식.

### 9-1. IntelliJ IDEA (또는 PyCharm, WebStorm)

```
File → Settings (Ctrl+Alt+S)
→ Tools → Terminal
→ Shell path:  wsl.exe -d Ubuntu-24.04
→ Apply
```

이후 하단 Terminal 탭 열면 자동으로 Ubuntu + 프로젝트 위치(`/mnt/c/...`)로 cd.

### 9-2. VSCode

```
Settings (Ctrl+,)
→ "Terminal Integrated Default Profile" 검색
→ Linux: Ubuntu-24.04 (WSL) 선택
```

### 9-3. IDE 안에서 ai 실행

1. IDE로 프로젝트 열기 (예: `C:\projects\myapp`)
2. 하단 Terminal 탭 클릭 → 자동으로 `/mnt/c/projects/myapp` 위치
3. `ai` 입력 → 자동으로 `agents-myapp` 세션 생성
4. IDE 안에 3분할 AI 팀 화면 표시

---

## 검증

### 10-1. 환경 점검

```bash
# 새 셸에서
echo $CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS
# 1

claude --version && codex --version && gemini --version
# 셋 다 버전 출력

which ai-team.sh
ls ~/ai-team.sh ~/ai-session.sh
# 둘 다 존재

type ai
# ai is aliased to `bash ~/ai-team.sh`

ls ~/.claude/skills/ai-harness-monitor/scripts/
# team-layout.sh dashboard.sh ask-gemini.sh ask-codex.sh ask-both.sh route-codex.sh
```

### 10-2. 첫 실행

```bash
cd /mnt/c/projects/test-app    # 또는 임의 프로젝트
ai
```

예상 결과:

```
세션: agents-test-app (cwd: /mnt/c/projects/test-app)
```

화면이 3분할로 갈라지고:
- 좌측: claude가 자동 시작
- 우상: GEMINI · researcher dashboard (waiting for first call)
- 우하: CODEX · reviewer dashboard (waiting for first call)

### 10-3. 영상 패턴 동작 확인

claude 패널에서:

```
gemini한테 "1+1?" 물어봐줘
```

→ 우상단 GEMINI dashboard에 카드 형태로 호출 + 응답 라이브 표시되면 성공.

만약 dashboard에 안 뜨면 claude에게:

```
ask-gemini.sh wrapper를 통해 호출해줘
```

---

## 사용 가이드 (일상)

### 명령어 한눈에

| 명령 | 동작 |
|------|------|
| `ai` | 현재 cwd 기반 세션 시작/attach (자동 분리) |
| `aik` | 현재 cwd 세션 종료 |
| `air` | 종료 + 재시작 |
| `ais` | 현재 세션 + 전체 agents-* 목록 |
| `aia` | 현재 cwd 세션 attach |
| `aikall` | 모든 agents-* 일괄 종료 |
| `ai-old` | 레거시 4분할 (인터랙티브 codex/gemini) |

### tmux 단축키 (Prefix = `Ctrl+a`)

| 단축키 | 동작 |
|--------|------|
| `Ctrl+a → 방향키` | 패널 이동 |
| `Ctrl+a → z` | 현재 패널 전체화면 토글 |
| `Ctrl+a → d` | detach (백그라운드, AI 작업 계속됨) |
| `Ctrl+a → [` | 스크롤 모드 (`q`로 종료) |
| `Ctrl+a → r` | tmux.conf reload |

### 일상 워크플로우

```
1. IDE 열기 (IntelliJ / VSCode)
2. 프로젝트 열기 (예: C:\projects\myapp)
3. 하단 Terminal 탭 클릭 → 자동 Ubuntu 진입
4. ai 입력 → agents-myapp 세션 자동 생성
5. claude/gemini/codex와 협업
6. 자리 비울 때: Ctrl+a → d
7. 다시: ai 또는 aia
```

---

## 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `wsl --install` 실패 | Windows 버전 낮음 | Windows 11 권장. 최소 빌드 19041 |
| `wsl -d Ubuntu-24.04` 진입 시 사용자 입력 무한 루프 | 첫 셋업 미완 | PowerShell에서 `wsl --shutdown` 후 재시도 |
| `claude` 인증 후 매번 다시 인증 요구 | `~/.claude/.credentials.json` 권한 문제 | `chmod 600 ~/.claude/.credentials.json` |
| `ai` 명령 안 먹음 | alias 미적용 | `source ~/.bashrc` |
| `ai` 실행 시 "team-layout.sh not found" | ai-harness-monitor 스킬 미설치 | Phase 6-3 확인 |
| 모든 프로젝트가 같은 세션 attach | 세션 이름 동적 분리 미적용 | 최신 `ai-team.sh` + `bashrc-aliases.sh` 적용 |
| `/plugin install` 실패 | 마켓플레이스 이름 오타 | `harness-marketplace` 확인 (`harness` 아님) |
| dashboard에 호출이 안 보임 | claude가 wrapper 안 씀 | claude에게 `ask-gemini.sh` / `ask-codex.sh` 사용하라고 지시 |
| Windows Terminal이 PowerShell만 열림 | defaultProfile 미설정 | Phase 8 재실행 |
| IDE 터미널이 cmd로 열림 | Shell path 미설정 | Phase 9 재실행 |
| 색이 깨짐 / syntax highlight 흐림 | True Color 미적용 | `~/.tmux.conf`의 `terminal-overrides ",xterm-256color:Tc"` 확인 |
| 마우스 스크롤 안 됨 | tmux 마우스 모드 미활성 | `~/.tmux.conf`의 `set -g mouse on` 확인 |
| 세션이 PC 재부팅 시 사라짐 | 정상 동작 (WSL 종료 = tmux 종료) | 영구 보존이 필요하면 `tmux-resurrect` 플러그인 |
| `block-dangerous.sh: $'\r': command not found` 류 hook 에러 | hook 스크립트가 Windows CRLF로 저장됨 | `.gitattributes`의 `*.sh text eol=lf` 확인. 또는 `sed -i 's/\r$//' <hook파일>` |

---

## 파일 구조 참고

```
TIL/cs/tool/ai-harness/                     ← 이 가이드 폴더
├── README.md                               ← 본 문서
├── QUICK-START.md                          ← 명령만 모은 빠른 셋업
├── scripts/                                ← 셋업 스크립트
│   ├── install-all.sh                      ← 한 방 자동 설치
│   ├── ai-team.sh                          ← 영상 패턴 wrapper
│   ├── ai-session.sh                       ← 레거시 4분할
│   ├── bashrc-aliases.sh                   ← .bashrc에 append할 alias
│   ├── tmux.conf                           ← ~/.tmux.conf 템플릿
│   └── link-claude-from-windows.sh         ← Windows .claude → WSL symlink
└── ai-harness-monitor-skill/               ← ~/.claude/skills/로 배치할 스킬
    ├── SKILL.md
    ├── roles/
    │   ├── researcher.md
    │   └── reviewer.md
    └── scripts/
        ├── team-layout.sh
        ├── dashboard.sh
        ├── ask-gemini.sh
        ├── ask-codex.sh
        ├── ask-both.sh
        └── route-codex.sh

설치 후 WSL Ubuntu의 ~ (홈)
├── .tmux.conf                              ← scripts/tmux.conf 가 복사됨
├── .bashrc                                 ← bashrc-aliases.sh 가 append됨
├── ai-team.sh                              ← 영상 패턴 wrapper
├── ai-session.sh                           ← 레거시 4분할
└── .claude/
    └── skills/
        └── ai-harness-monitor/             ← install-all.sh가 자동 복사
```

---

## 자주 묻는 질문

### Q1. macOS/Linux에서도 동일하게 셋업 가능한가?

WSL 단계 빼고 동일. macOS는 Homebrew, Linux는 apt/snap으로 tmux/node 설치. AI CLI는 npm으로 동일.

### Q2. Codex/Gemini가 유료인가?

- Claude Code: Anthropic 계정 필요. 사용량 기반 과금
- Codex: OpenAI 계정 + API key (사용량 기반)
- Gemini CLI: Google 계정. 무료 한도 있음

회사 계정 정책 확인 필요.

### Q3. 영상의 모든 기능을 100% 구현하나?

`ai-harness-monitor` 스킬이 핵심. 이게 없으면 dashboard 라이브 표시가 안 됨. Phase 6-3 참조.

### Q4. 보안상 안전한가?

`--permission-mode bypassPermissions`는 모든 권한 확인을 끕니다. 신뢰된 작업에만 사용. 회사 정책 따라 결정.

### Q5. 여러 IDE 동시 사용 시?

각 IDE의 터미널이 자기 프로젝트 디렉토리에서 `ai` 실행 → 프로젝트별로 자동 세션 분리 (`agents-myapp`, `agents-backend` 등).

### Q6. 집에서 동일 셋업하려면?

집 PC에서:
```bash
# WSL Ubuntu 진입 후
git clone <TIL-repo-url>
cd TIL/cs/tool/ai-harness
bash scripts/install-all.sh
```
인증 흐름(Phase 4)만 다시 거치면 끝. `.gitattributes`가 `*.sh text eol=lf`로 묶어두기 때문에 Windows checkout이라도 셸 스크립트는 LF로 풀립니다.

---

## 참고 자료

| 자료 | 위치 |
|------|------|
| 영상 데모 | `[하네스 엔지니어링] Claude · Codex · Gemini 셋이서 한 작업 — AI Dev Team 데모` (YouTube) |
| Harness GitHub | https://github.com/revfactory/harness |
| 원본 튜토리얼 | pandas-studio/agent-harness-tutorial (ep_a_demo) |

---

## 변경 이력

| 날짜 | 변경 |
|------|------|
| 2026-05-11 | TIL에 가이드 흡수 (cs/tool/ai-harness/). 스크립트 6개 + skill 1개 + .gitattributes 동봉. |
