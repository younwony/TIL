# Claude Code 릴리스 노트 (한글)

> 정리 일시: 2026-03-19
> 정리 범위: v2.1.43 ~ v2.1.79

---

## Version 2.1.79

### 주요 하이라이트
- `--console` 플래그로 Anthropic Console(API 빌링) 인증 지원
- 시작 시 메모리 사용량 ~18MB 감소
- VSCode에서 `/remote-control` 명령으로 브라우저/폰에서 세션 이어받기

### 새 기능
- **`claude auth login --console`**: Anthropic Console(API 빌링) 인증 전용 플래그 추가
- **턴 소요 시간 표시**: `/config` 메뉴에 "Show turn duration" 토글 추가
- **[VSCode] `/remote-control`**: 세션을 claude.ai/code로 브릿지하여 브라우저나 폰에서 이어서 작업
- **[VSCode] AI 생성 세션 제목**: 첫 메시지 기반으로 세션 탭에 자동 제목 부여

### 개선
- **시작 메모리 최적화**: 모든 시나리오에서 ~18MB 감소
- **비스트리밍 API 폴백 개선**: 2분 타임아웃으로 세션 무한 대기 방지
- **`CLAUDE_CODE_PLUGIN_SEED_DIR`**: 플랫폼 구분자(Unix `:`, Windows `;`)로 여러 시드 디렉토리 지원

### 버그 수정
- `claude -p`가 stdin 없이 서브프로세스로 실행될 때 행(hang)되던 문제 수정
- `-p`(print) 모드에서 Ctrl+C가 작동하지 않던 문제 수정
- `/btw`가 스트리밍 중 실행 시 사이드 질문 대신 메인 에이전트 출력을 반환하던 문제 수정
- `voiceEnabled: true` 설정 시 음성 모드가 시작 시 정상 활성화되지 않던 문제 수정
- `/permissions`에서 좌우 화살표 탭 네비게이션 수정
- `CLAUDE_CODE_DISABLE_TERMINAL_TITLE`이 시작 시 터미널 제목 설정을 막지 못하던 문제 수정
- 커스텀 상태줄이 워크스페이스 신뢰 차단 시 빈 화면을 표시하던 문제 수정
- 엔터프라이즈 사용자가 429 레이트 리밋 에러에서 재시도할 수 없던 문제 수정
- `SessionEnd` 훅이 인터랙티브 `/resume`로 세션 전환 시 발동되지 않던 문제 수정
- **[VSCode]** thinking pill이 응답 완료 후 "Thought for Ns" 대신 "Thinking"을 표시하던 문제 수정
- **[VSCode]** 좌측 사이드바에서 세션 열 때 세션 diff 버튼이 누락되던 문제 수정

---

## Version 2.1.78

### 주요 하이라이트
- `StopFailure` 훅 이벤트 추가 — API 에러 시 훅 발동
- 응답 텍스트가 생성 즉시 줄 단위로 스트리밍
- 보안: 샌드박스 의존성 미설치 시 자동 비활성화 방지 (시작 경고 표시)

### 새 기능
- **`StopFailure` 훅 이벤트**: 턴이 API 에러(레이트 리밋, 인증 실패 등)로 종료될 때 발동
- **`${CLAUDE_PLUGIN_DATA}` 변수**: 플러그인 업데이트 후에도 유지되는 영속 상태 디렉토리; `/plugin uninstall` 시 삭제 전 확인
- **플러그인 에이전트 frontmatter**: `effort`, `maxTurns`, `disallowedTools` 지원
- **`ANTHROPIC_CUSTOM_MODEL_OPTION` 환경변수**: `/model` 피커에 커스텀 모델 항목 추가 (`_NAME`, `_DESCRIPTION` 접미사 변수 지원)

### 개선
- **tmux 터미널 알림**: `set -g allow-passthrough on` 설정 시 iTerm2/Kitty/Ghostty 팝업, 프로그레스바가 외부 터미널까지 전달
- **줄 단위 스트리밍**: 응답 텍스트가 생성 즉시 줄 단위로 표시
- **대용량 세션 재개 성능 개선**: 메모리 사용량 및 시작 시간 최적화

### 버그 수정
- Linux 샌드박스 Bash에서 `git log HEAD`가 "ambiguous argument" 실패하고 `git status`에 스텁 파일이 보이던 문제 수정
- `cc log`와 `--resume`이 대용량 세션(>5MB)에서 대화 기록을 자동 절단하던 문제 수정
- API 에러가 stop 훅을 트리거하고 차단 에러가 모델에 재입력되는 무한 루프 수정
- `deny: ["mcp__servername"]` 권한 규칙이 MCP 서버 도구를 모델 전송 전 제거하지 않던 문제 수정
- `sandbox.filesystem.allowWrite`가 절대 경로에서 작동하지 않던 문제 수정 (이전에는 `//` 접두사 필요)
- `/sandbox` Dependencies 탭이 macOS에서 Linux 필수 조건을 표시하던 문제 수정
- **보안**: `sandbox.enabled: true` 설정 시 의존성 미설치로 샌드박스가 자동 비활성화되던 문제 — 이제 시작 경고 표시
- `bypassPermissions` 모드에서 `.git`, `.claude` 등 보호 디렉토리가 프롬프트 없이 쓰기 가능하던 문제 수정
- ctrl+u가 일반 모드에서 readline kill-line 대신 스크롤하던 문제 수정 (ctrl+u/ctrl+d 반 페이지 스크롤은 트랜스크립트 모드로 이동)
- 음성 모드 modifier-combo push-to-talk 키바인딩(예: ctrl+k)이 즉시 활성화 대신 홀드를 요구하던 문제 수정
- WSL2 + WSLg(Windows 11)에서 음성 모드가 작동하지 않던 문제 수정; WSL1/Win10 사용자에게 명확한 에러 표시
- `--worktree` 플래그가 워크트리 디렉토리에서 스킬과 훅을 로드하지 않던 문제 수정
- `CLAUDE_CODE_DISABLE_GIT_INSTRUCTIONS`와 `includeGitInstructions` 설정이 시스템 프롬프트의 git status 섹션을 억제하지 못하던 문제 수정
- VS Code가 Dock/Spotlight에서 실행될 때 Bash 도구가 Homebrew 등 PATH 의존 바이너리를 찾지 못하던 문제 수정
- truecolor를 지원하지 않는 터미널에서 Claude 오렌지 색상이 흐리게 표시되던 문제 수정
- `ANTHROPIC_BETAS` 환경변수가 Haiku 모델 사용 시 무시되던 문제 수정
- 큐된 프롬프트가 줄바꿈 구분 없이 연결되던 문제 수정
- **[VSCode]** 인증된 상태에서 사이드바 열 때 로그인 화면이 잠깐 보이던 문제 수정
- **[VSCode]** Opus 선택 시 "API Error: Rate limit reached" — 플랜 티어 미확인 구독자에게 1M 컨텍스트 변형을 제공하지 않도록 수정

---

## Version 2.1.77

### 주요 하이라이트
- Claude Opus 4.6 기본 최대 출력 토큰 64k로 증가, 상한 128k
- "Always Allow" 복합 명령 권한 규칙 수정 — 서브커맨드별 저장
- `/fork`가 `/branch`로 이름 변경

### 새 기능
- **`allowRead` 샌드박스 설정**: `denyRead` 영역 내에서 읽기 접근을 다시 허용
- **`/copy N`**: N번째 최근 어시스턴트 응답을 복사

### 개선
- **출력 토큰 한도 증가**: Opus 4.6 기본 64k, Opus 4.6/Sonnet 4.6 상한 128k
- **macOS 시작 속도 ~60ms 향상**: 키체인 자격 증명 병렬 읽기
- **`--resume` 성능**: 포크 많고 대용량 세션에서 최대 45% 빠른 로딩, ~100-150MB 피크 메모리 감소
- **Esc 개선**: 진행 중인 비스트리밍 API 요청 중단
- **`claude plugin validate`**: 스킬, 에이전트, 커맨드 frontmatter + `hooks/hooks.json` YAML 파싱 에러 및 스키마 위반 검사
- **백그라운드 bash 작업**: 출력이 5GB 초과 시 자동 종료 (디스크 가득 참 방지)
- **플랜 수락 시 세션 자동 이름 지정**
- **Agent 도구 변경**: `resume` 파라미터 제거 → `SendMessage({to: agentId})`로 이전 에이전트 재개
- **`SendMessage`**: 중지된 에이전트를 백그라운드에서 자동 재개
- **`/fork` → `/branch` 이름 변경** (`/fork`는 별칭으로 유지)
- **[VSCode]** 플랜 프리뷰 탭 제목이 "Claude's Plan" 대신 플랜 헤딩 사용
- **[VSCode]** option+click 네이티브 선택 미작동 시 `macOptionClickForcesSelection` 설정 안내

### 버그 수정
- "Always Allow"가 복합 bash 명령에 대해 전체 문자열을 하나의 규칙으로 저장하여 재매칭 실패하던 문제 수정 → 서브커맨드별 규칙 저장
- auto-updater가 슬래시 커맨드 오버레이 반복 열기/닫기 시 중복 바이너리 다운로드를 시작하여 수십 GB 메모리를 소모하던 문제 수정
- `--resume`이 메모리 추출 쓰기와 메인 트랜스크립트 간 경쟁 조건으로 최근 대화 기록을 자동 절단하던 문제 수정
- PreToolUse 훅이 `"allow"` 반환 시 `deny` 권한 규칙(엔터프라이즈 관리 설정 포함)을 우회하던 문제 수정
- Write 도구가 CRLF 파일 덮어쓰기 또는 CRLF 디렉토리에서 파일 생성 시 줄 끝을 자동 변환하던 문제 수정
- 긴 세션에서 progress 메시지가 compaction 후에도 남아 메모리 증가하던 문제 수정
- API 비스트리밍 폴백 시 비용 및 토큰 사용량이 추적되지 않던 문제 수정
- `CLAUDE_CODE_DISABLE_EXPERIMENTAL_BETAS`가 베타 도구 스키마 필드를 제거하지 않아 프록시 게이트웨이가 요청을 거부하던 문제 수정
- 시스템 임시 디렉토리 경로에 공백이 포함될 때 Bash 도구가 성공한 명령에 대해 에러를 보고하던 문제 수정
- 붙여넣기 직후 입력 시 붙여넣기가 손실되던 문제 수정
- `/feedback` 텍스트 입력에서 Ctrl+D가 두 번째 누름으로 세션 종료 대신 전방 삭제하던 문제 수정
- 0바이트 이미지 파일 드래그 시 API 에러 수정
- Claude Desktop 세션이 OAuth 대신 터미널 CLI의 API 키를 잘못 사용하던 문제 수정
- `git-subdir` 플러그인이 같은 모노레포의 다른 서브디렉토리에서 플러그인 캐시 충돌하던 문제 수정
- 터미널 UI에서 번호 목록이 렌더링되지 않던 문제 수정
- 오래된 워크트리 정리가 이전 크래시에서 재개된 에이전트 워크트리를 삭제할 수 있던 경쟁 조건 수정
- `/mcp` 등 다이얼로그 열기 시 에이전트 실행 중 입력 데드락 수정
- vim NORMAL 모드에서 Backspace와 Delete 키가 작동하지 않던 문제 수정
- vim 모드 토글 시 상태줄이 업데이트되지 않던 문제 수정
- VS Code/Cursor 등 xterm.js 기반 터미널에서 Cmd+click으로 하이퍼링크가 두 번 열리던 문제 수정
- tmux 기본 설정에서 배경색이 터미널 기본값으로 렌더링되던 문제 수정
- SSH 위 tmux에서 텍스트 선택 시 iTerm2 세션 크래시 수정
- tmux 세션에서 클립보드 복사가 자동 실패하던 문제 수정; 복사 토스트에 `⌘V` 또는 tmux `prefix+]` 중 어느 것으로 붙여넣기할지 표시
- 설정/권한/샌드박스 다이얼로그에서 리스트 탐색 중 `←`/`→`가 실수로 탭을 전환하던 문제 수정
- IDE 통합이 tmux 또는 screen 내에서 자동 연결되지 않던 문제 수정
- CJK 문자가 오른쪽 가장자리에서 잘릴 때 인접 UI 요소에 시각적으로 침범하던 문제 수정
- 리더 종료 시 팀원 패널이 닫히지 않던 문제 수정
- iTerm2 auto 모드가 네이티브 split-pane 팀원을 위해 iTerm2를 감지하지 못하던 문제 수정

---

## Version 2.1.76

### 주요 하이라이트
- MCP 엘리시테이션(elicitation) 지원 — MCP 서버가 작업 중 구조화된 입력을 요청 가능
- `/effort` 슬래시 커맨드 추가
- `worktree.sparsePaths` 설정으로 대규모 모노레포에서 필요한 디렉토리만 체크아웃

### 새 기능
- **MCP 엘리시테이션**: MCP 서버가 인터랙티브 다이얼로그(폼 필드 또는 브라우저 URL)로 구조화된 입력을 요청 가능
- **`Elicitation`/`ElicitationResult` 훅**: 응답이 전송되기 전에 가로채고 오버라이드
- **`-n` / `--name <name>` CLI 플래그**: 시작 시 세션 표시 이름 설정
- **`worktree.sparsePaths` 설정**: `claude --worktree` 시 대규모 모노레포에서 git sparse-checkout으로 필요한 디렉토리만 체크아웃
- **`PostCompact` 훅**: compaction 완료 후 발동
- **`/effort` 슬래시 커맨드**: 모델 effort 레벨 설정
- **세션 품질 설문**: 엔터프라이즈 관리자가 `feedbackSurveyRate` 설정으로 샘플 비율 조정

### 개선
- **`--worktree` 시작 성능 개선**: git refs 직접 읽기, 리모트 브랜치 로컬 가용 시 `git fetch` 생략
- **백그라운드 에이전트 종료 시 부분 결과 보존**
- **모델 폴백 알림 가시성 향상**: verbose 모드가 아니어도 항상 표시, 사람이 읽기 쉬운 모델 이름
- **블록인용 가독성 향상**: 다크 테마에서 dim 대신 이탤릭 + 왼쪽 바
- **오래된 워크트리 자동 정리**: 중단된 병렬 실행 후 남은 워크트리 자동 제거
- **Remote Control 세션 제목**: "Interactive session" 대신 첫 프롬프트 기반
- **`/voice` 언어 표시**: 활성화 시 딕테이션 언어 표시, 미지원 언어 경고
- **`--plugin-dir`**: 하나의 경로만 수용 (서브커맨드 지원), 여러 디렉토리는 반복 사용

### 버그 수정
- deferred 도구(`ToolSearch`로 로드)가 compaction 후 입력 스키마를 잃어 배열/숫자 파라미터가 타입 에러로 거부되던 문제 수정
- 슬래시 커맨드가 "Unknown skill" 표시하던 문제 수정
- 플랜 모드가 이미 수락된 플랜에 대해 재승인을 요청하던 문제 수정
- 권한 다이얼로그나 플랜 에디터 열려 있을 때 음성 모드가 키 입력을 삼키던 문제 수정
- Windows npm 설치에서 `/voice`가 작동하지 않던 문제 수정
- `model:` frontmatter가 있는 스킬을 1M 컨텍스트 세션에서 호출 시 "Context limit reached" 에러 수정
- 비표준 모델 문자열 사용 시 "adaptive thinking is not supported" 에러 수정
- `Bash(cmd:*)` 권한 규칙이 `#`이 포함된 인용 인자와 매칭되지 않던 문제 수정
- Bash 권한 다이얼로그의 "don't ask again"이 파이프/복합 명령에 대해 전체 원시 명령을 표시하던 문제 수정
- 자동 compaction이 연속 실패 후 무한 재시도하던 문제 → 3회 후 서킷 브레이커로 중단
- MCP 재연결 스피너가 성공 후에도 지속되던 문제 수정
- LSP 플러그인이 LSP Manager 초기화 후 마켓플레이스 조정 전 서버를 등록하지 못하던 문제 수정
- SSH 위 tmux에서 클립보드 복사 수정 — 직접 터미널 쓰기와 tmux 클립보드 통합 모두 시도
- `/export`가 전체 파일 경로 대신 파일명만 표시하던 문제 수정
- 텍스트 선택 후 트랜스크립트가 새 메시지로 자동 스크롤되지 않던 문제 수정
- Escape 키로 로그인 방법 선택 화면을 종료할 수 없던 문제 수정
- Remote Control 여러 문제 수정: 유휴 환경 종료 시 세션 자동 사망, 빠른 메시지가 배치 대신 하나씩 큐잉, JWT 갱신 후 재전달
- 장시간 WebSocket 연결 끊김 후 브릿지 세션 복구 실패 수정
- soft-hidden 커맨드의 정확한 이름 입력 시 슬래시 커맨드가 발견되지 않던 문제 수정
- **[VSCode]** 쉼표가 포함된 gitignore 패턴이 @-mention 파일 피커에서 전체 파일 타입을 자동 제외하던 문제 수정

---

## Version 2.1.75

### 주요 하이라이트
- Opus 4.6에 기본 1M 컨텍스트 윈도우 (Max, Team, Enterprise 플랜)
- `/color` 커맨드로 프롬프트바 색상 설정
- 메모리 파일에 최종 수정 타임스탬프 추가

### 새 기능
- **1M 컨텍스트 윈도우 기본 제공**: Opus 4.6, Max/Team/Enterprise 플랜 (이전에는 추가 사용량 필요)
- **`/color` 커맨드**: 세션 프롬프트바 색상 설정 (모든 사용자)
- **세션 이름 표시**: `/rename` 사용 시 프롬프트바에 세션 이름 표시
- **메모리 파일 타임스탬프**: 최종 수정 시각 추가 — 최신/오래된 메모리 구분에 도움
- **훅 소스 표시**: 권한 프롬프트에서 훅의 출처(설정/플러그인/스킬) 표시

### 개선
- **macOS 시작 성능 향상**: 비-MDM 머신에서 불필요한 서브프로세스 생략
- **async 훅 완료 메시지 기본 숨김**: `--verbose` 또는 트랜스크립트 모드에서만 표시

### 버그 수정
- 신규 설치에서 `/voice`를 두 번 토글해야 음성 모드가 활성화되던 문제 수정
- `/model` 또는 Option+P로 모델 전환 후 헤더에 표시된 모델 이름이 업데이트되지 않던 문제 수정
- 첨부 메시지 연산이 undefined 값을 반환할 때 세션 크래시 수정
- Bash 도구가 파이프 명령에서 `!`를 맹글링하던 문제 수정 (예: `jq 'select(.x != .y)'` 정상 작동)
- 관리자가 강제 비활성화한 플러그인이 `/plugin` 설치됨 탭에 표시되던 문제 수정
- thinking 및 `tool_use` 블록의 토큰 추정이 과다 계산되어 조기 compaction이 발생하던 문제 수정
- 손상된 마켓플레이스 설정 경로 처리 수정
- `/resume`이 포크/이어받기 세션 재개 후 세션 이름을 잃어버리던 문제 수정
- Config 탭 방문 후 `/status` 다이얼로그에서 Esc가 닫히지 않던 문제 수정
- 플랜 수락/거부 시 입력 처리 수정
- 에이전트 팀에서 푸터 힌트가 "↓ to expand" 대신 올바른 "shift + ↓ to expand" 표시

### 변경
- **Breaking**: Windows 관리 설정 폴백 경로 `C:\ProgramData\ClaudeCode\managed-settings.json` 제거 → `C:\Program Files\ClaudeCode\managed-settings.json` 사용

---

## Version 2.1.74

### 주요 하이라이트
- `/context` 커맨드에 최적화 제안 추가 — 컨텍스트 과부하 도구, 메모리 비대, 용량 경고 감지
- `autoMemoryDirectory` 설정으로 자동 메모리 저장 디렉토리 변경 가능
- 스트리밍 API 응답 버퍼 메모리 누수 수정

### 새 기능
- **`/context` 최적화 제안**: 컨텍스트 과부하 도구, 메모리 비대, 용량 경고를 감지하고 구체적인 최적화 팁 제공
- **`autoMemoryDirectory` 설정**: 자동 메모리 저장을 위한 커스텀 디렉토리 구성

### 개선
- **[VSCode]** 통합 터미널에서 터미널 인식 가속으로 스크롤 휠 반응성 향상

### 버그 수정
- 스트리밍 API 응답 버퍼가 제너레이터 조기 종료 시 해제되지 않아 Node.js/npm 코드 경로에서 RSS가 무한 증가하던 메모리 누수 수정
- 관리 정책 `ask` 규칙이 사용자 `allow` 규칙이나 스킬 `allowed-tools`에 의해 우회되던 문제 수정
- 에이전트 frontmatter `model:` 필드와 `--agents` JSON 설정에서 전체 모델 ID(예: `claude-opus-4-5`)가 무시되던 문제 수정 — `--model`과 동일한 값 수용
- MCP OAuth 인증이 콜백 포트 사용 중일 때 행되던 문제 수정
- MCP OAuth 갱신 토큰 만료 시 재인증 프롬프트가 나오지 않던 문제 수정 (HTTP 200으로 에러 반환하는 OAuth 서버, 예: Slack)
- macOS 네이티브 바이너리에서 마이크 권한 미부여 시 음성 모드가 자동 실패하던 문제 수정 — `audio-input` entitlement 포함
- `SessionEnd` 훅이 `hook.timeout`과 관계없이 종료 후 1.5초에 종료되던 문제 수정 → `CLAUDE_CODE_SESSIONEND_HOOKS_TIMEOUT_MS`로 설정 가능
- REPL 내 마켓플레이스 플러그인 `/plugin install` 실패 수정
- 마켓플레이스 업데이트가 git 서브모듈을 동기화하지 않던 문제 수정
- 인자가 포함된 알 수 없는 슬래시 커맨드가 입력을 자동 삭제하던 문제 수정 → 경고 표시
- Windows Terminal, conhost, VS Code 통합 터미널에서 히브리어, 아랍어 등 RTL 텍스트가 올바르게 렌더링되지 않던 문제 수정
- Windows에서 잘못된 파일 URI로 LSP 서버가 작동하지 않던 문제 수정
- `--plugin-dir` 변경: 로컬 개발 복사본이 동일 이름의 설치된 마켓플레이스 플러그인을 오버라이드
- **[VSCode]** Untitled 세션의 삭제 버튼이 작동하지 않던 문제 수정

---

## Version 2.1.73

### 주요 하이라이트
- `modelOverrides` 설정으로 모델 피커 항목을 커스텀 프로바이더 모델 ID에 매핑
- 복합 bash 명령의 권한 프롬프트로 인한 멈춤 및 100% CPU 루프 수정
- Bedrock/Vertex/Foundry 기본 Opus 모델이 4.6으로 변경

### 새 기능
- **`modelOverrides` 설정**: 모델 피커 항목을 커스텀 프로바이더 모델 ID(예: Bedrock 추론 프로필 ARN)에 매핑
- **SSL 인증서 에러 안내**: OAuth 로그인 또는 연결 체크 실패 시 (기업 프록시, `NODE_EXTRA_CA_CERTS`) 실행 가능한 가이드 제공

### 개선
- **위쪽 화살표 개선**: Claude 중단 후 중단된 프롬프트를 복원하고 대화를 한 단계에서 되감기
- **IDE 감지 속도 향상**
- **macOS 클립보드 이미지 붙여넣기 성능 개선**
- **`/effort`**: Claude 응답 중에도 작동 (`/model` 동작과 일치)
- **음성 모드**: 빠른 push-to-talk 재누름 시 일시적 연결 실패 자동 재시도
- **Remote Control 스폰 모드 선택 프롬프트 개선**
- **Bedrock/Vertex/Foundry 기본 Opus 모델**: 4.1 → **4.6**으로 변경
- **`/output-style` 커맨드 Deprecated**: `/config` 사용 권장. 출력 스타일은 세션 시작 시 고정 (프롬프트 캐싱 개선)

### 버그 수정
- 복합 bash 명령의 권한 프롬프트로 인한 멈춤 및 100% CPU 루프 수정
- 많은 스킬 파일이 동시에 변경될 때(예: `.claude/skills/` 디렉토리가 큰 레포에서 `git pull`) 데드락 수정
- 같은 프로젝트 디렉토리에서 여러 Claude Code 세션 실행 시 Bash 도구 출력 손실 수정
- `model: opus`/`sonnet`/`haiku` 서브에이전트가 Bedrock/Vertex/Foundry에서 이전 모델 버전으로 자동 다운그레이드되던 문제 수정
- 서브에이전트가 생성한 백그라운드 bash 프로세스가 에이전트 종료 시 정리되지 않던 문제 수정
- `/resume`이 피커에 현재 세션을 표시하던 문제 수정
- `/ide`가 확장 자동 설치 시 `onInstall is not defined` 크래시 수정
- `/loop`가 Bedrock/Vertex/Foundry 및 텔레메트리 비활성화 시 사용 불가하던 문제 수정
- `--resume` 또는 `--continue`로 세션 재개 시 SessionStart 훅이 두 번 발동되던 문제 수정
- JSON 출력 훅이 매 턴마다 no-op system-reminder 메시지를 모델 컨텍스트에 주입하던 문제 수정
- 느린 연결에서 새 녹음과 겹칠 때 음성 모드 세션 손상 수정
- Linux 샌드박스가 "ripgrep (rg) not found"로 시작 실패하던 문제 수정
- Amazon Linux 2 등 glibc 2.26 시스템에서 Linux 네이티브 모듈 로딩 실패 수정
- Remote Control로 이미지 수신 시 "media_type: Field required" API 에러 수정
- Windows에서 Desktop 폴더가 이미 존재할 때 `/heapdump` EEXIST 에러 수정
- **[VSCode]** 프록시 뒤이거나 Bedrock/Vertex에서 Claude 4.5 모델 사용 시 HTTP 400 에러 수정

---

## Version 2.1.72

### 주요 하이라이트
- Effort 레벨 단순화 (low/medium/high, `max` 제거) — 새 심볼 ○ ◐ ●
- `/plan` 커맨드에 설명 인자 추가 — 바로 플랜 모드 진입 및 시작
- 대규모 보안·안정성·메모리 수정 및 권한 매칭 개선

### 새 기능
- **`/plan` 설명 인자 지원**: `/plan fix the auth bug`처럼 설명을 바로 전달하면 플랜 모드 진입 후 즉시 시작
- **`/copy`에 `w` 키 추가**: 포커스된 선택 항목을 클립보드 대신 파일로 직접 저장 (SSH 환경에 유용)
- **`ExitWorktree` 도구 추가**: `EnterWorktree` 세션을 빠져나오는 도구
- **`CLAUDE_CODE_DISABLE_CRON` 환경변수 추가**: 세션 중 예약된 cron 작업을 즉시 중지
- **Bash 자동 승인 허용 목록 확장**: `lsof`, `pgrep`, `tput`, `ss`, `fd`, `fdfind` 추가
- **Agent 도구의 `model` 파라미터 복원**: 호출별 모델 오버라이드 가능

### 개선
- **Effort 레벨 단순화**: low/medium/high 3단계로 변경 (max 제거), 새 심볼 ○ ◐ ●, `/effort auto`로 기본값 리셋
- **`/config` UX 개선**: Escape=취소, Enter=저장 후 닫기, Space=토글
- **위쪽 화살표 히스토리 개선**: 여러 세션 동시 실행 시 현재 세션의 메시지를 우선 표시
- **음성 입력 전사 정확도 향상**: 레포 이름과 일반 개발 용어(regex, OAuth, JSON) 인식 개선
- **Bash 명령 파싱 개선**: 네이티브 모듈로 전환 — 더 빠른 초기화, 메모리 누수 제거
- **번들 크기 ~510KB 감소**
- **CPU 활용 효율 개선**: 긴 세션에서의 CPU 사용량 최적화
- **SDK `query()` 호출의 프롬프트 캐시 무효화 수정**: 입력 토큰 비용 최대 12배 절감

### 버그 수정
- **CLAUDE.md HTML 주석(`<!-- ... -->`)이 자동 주입 시 Claude에게 숨겨지도록 변경** — Read 도구로 읽을 때는 여전히 표시
- **느린 종료 수정**: 백그라운드 작업이나 훅이 느리게 응답할 때 종료가 지연되던 문제 해결
- **에이전트 작업 진행 상태가 "Initializing…"에 멈추던 문제 수정**
- **스킬 훅이 이벤트당 2번 발동되던 문제 수정**
- **음성 모드 관련 여러 문제 수정**: 간헐적 입력 지연, push-to-talk 해제 후 잘못된 "No speech detected" 오류, 제출 후 오래된 전사가 프롬프트에 재표시
- **`--continue`가 `--compact` 이후 가장 최근 지점에서 재개하지 않던 문제 수정**
- **Bash 보안 파싱 엣지 케이스 수정**
- **플러그인 관련 여러 수정**: Windows OneDrive 폴더에서 `EEXIST` 오류, 프로젝트/사용자 스코프 충돌, `~` 리터럴 디렉토리 생성 등
- **피드백 설문이 긴 세션에서 너무 자주 표시되던 문제 수정**
- **`--effort` CLI 플래그가 시작 시 관련 없는 설정 쓰기로 리셋되던 문제 수정**
- **Ctrl+B 백그라운드 쿼리가 `/clear` 후 전사를 잃거나 새 대화를 손상시키던 문제 수정**
- **`/clear`가 백그라운드 에이전트/bash 작업을 종료하던 문제 수정** — 이제 포그라운드 작업만 정리
- **워크트리 격리 문제 수정**: Task 도구 재개 시 cwd 미복원, 백그라운드 작업 알림에 `worktreePath`와 `worktreeBranch` 누락
- **샌드박스 권한 문제 수정**: 특정 파일 쓰기 작업이 프롬프트 없이 허용되던 문제, `/tmp/claude/` 등 허용 디렉토리로의 출력 리다이렉션이 불필요하게 프롬프트되던 문제
- **팀 에이전트가 리더의 모델을 상속하도록 수정**
- **"Always Allow"가 다시 매치되지 않는 권한 규칙을 저장하던 문제 수정**
- **훅 관련 여러 수정**: `transcript_path` 오류, 에이전트 `prompt` 삭제, PostToolUse 차단 사유 중복 표시, async 훅의 stdin 문제 등
- **Desktop/SDK에서 U+2028/U+2029 문자 포함 파일 읽기 시 세션 크래시 수정**
- **병렬 도구 호출에서 Read/WebFetch/Glob 실패가 형제 작업을 취소하던 문제 수정** — 이제 Bash 오류만 전파
- **이미지가 큐잉된 메시지에 표시되지 않고, ↑ 편집 시 이미지가 손실되던 문제 수정**

### 플랫폼
- **Azure DevOps, AWS CodeCommit**: `.git` 접미사 없는 마켓플레이스 git URL 지원

### IDE
- **VSCode**: 통합 터미널의 스크롤 속도가 네이티브 터미널과 일치하도록 수정
- **VSCode**: Shift+Enter가 개행 대신 입력을 제출하던 문제 수정 (이전 키바인딩 사용자)
- **VSCode**: 입력 테두리에 effort 레벨 표시기 추가
- **VSCode**: `vscode://anthropic.claude-code/open` URI 핸들러 추가 — 프로그래밍 방식으로 Claude Code 탭 열기 (선택적 `prompt`, `session` 파라미터)

---

## Version 2.1.71

### 주요 하이라이트
- `/loop` 커맨드 추가 — 반복 간격으로 프롬프트/슬래시 커맨드 실행
- Cron 스케줄링 도구 추가 — 세션 내 반복 프롬프트
- 음성 모드 push-to-talk 키 리바인딩 지원

### 새 기능
- **`/loop` 커맨드 추가**: 반복 간격으로 프롬프트 또는 슬래시 커맨드 실행 (예: `/loop 5m check the deploy`)
- **Cron 스케줄링 도구 추가**: 세션 내 반복 프롬프트 예약
- **`voice:pushToTalk` 키바인딩 추가**: `keybindings.json`에서 음성 활성화 키 변경 가능 (기본값: space), `meta+k` 등 수정자+문자 조합은 타이핑 간섭 없음
- **Bash 자동 승인 허용 목록 확장**: `fmt`, `comm`, `cmp`, `numfmt`, `expr`, `test`, `printf`, `getconf`, `seq`, `tsort`, `pr` 추가

### 개선
- **시작 성능 개선**: 네이티브 이미지 프로세서 로딩을 첫 사용까지 지연
- **브릿지 세션 재연결 개선**: 노트북 슬립 복구 후 수초 내 재연결 (기존 최대 10분 대기)
- **`/plugin uninstall` 개선**: 프로젝트 스코프 플러그인을 `.claude/settings.local.json`에서 비활성화하여 팀원에게 영향 없음
- **플러그인 MCP 서버 중복 제거 개선**: 수동 설정된 서버와 동일한 서버를 건너뛰어 중복 연결 방지
- **`/debug` 업데이트**: 세션 중간에 디버그 로깅 토글 가능 (기본적으로 디버그 로그가 더 이상 작성되지 않음)

### 버그 수정
- **stdin 프리징 수정**: 긴 세션에서 키 입력이 처리되지 않던 문제 (프로세스는 유지)
- **5~8초 시작 프리징 수정**: 음성 모드 활성화 사용자의 CoreAudio 초기화가 시스템 깨어남 후 메인 스레드를 차단하던 문제
- **시작 UI 프리징 수정**: 다수의 claude.ai 프록시 커넥터가 만료된 OAuth 토큰을 동시 갱신할 때 발생
- **포크된 대화(`/fork`)가 같은 플랜 파일을 공유하던 문제 수정** — 한 포크의 플랜 편집이 다른 포크를 덮어쓰기
- **Read 도구의 과대 이미지 컨텍스트 삽입 수정**: 이미지 처리 실패 시 과대 이미지가 컨텍스트에 삽입되어 긴 세션을 깨뜨리던 문제
- **heredoc 커밋 메시지 포함 복합 bash 명령의 거짓 양성 권한 프롬프트 수정**
- **복수 Claude Code 인스턴스 실행 시 플러그인 설치 유실 수정**
- **claude.ai 커넥터가 OAuth 토큰 갱신 후 재연결 실패하던 문제 수정**
- **백그라운드 에이전트 완료 알림에 출력 파일 경로 누락 수정** — 컨텍스트 압축 후 부모 에이전트의 결과 복구가 어렵던 문제
- **Chrome 확장 자동 감지가 로컬 Chrome 없는 머신에서 영구적으로 "not installed"에 멈추던 문제 수정**
- **`--print`가 팀 에이전트 설정 시 영원히 행(hang)하던 문제 수정** — 종료 루프가 장기 실행 `in_process_teammate` 작업을 대기하지 않도록 변경
- **Windows에서 `cd <cwd> && git ...` 권한 프롬프트 수정**: 모델이 mingw 스타일 경로 사용 시 발생

---

## Version 2.1.70

### 주요 하이라이트
- 서드파티 게이트웨이 사용 시 API 400 오류 수정 — 프록시 엔드포인트의 도구 검색 호환성
- Windows/WSL 클립보드 비ASCII 텍스트(CJK, 이모지) 깨짐 수정
- VSCode 활동 바에 세션 목록 아이콘 추가

### 새 기능
- **VSCode 활동 바에 스파크 아이콘 추가**: 모든 Claude Code 세션 목록을 보여주며, 세션을 전체 에디터로 열기 가능
- **VSCode 플랜 전체 마크다운 문서 뷰 추가**: 코멘트 추가 지원
- **VSCode 네이티브 MCP 서버 관리 대화상자 추가**: `/mcp`로 서버 활성화/비활성화, 재연결, OAuth 인증 관리

### 개선
- **압축(Compaction) 시 이미지 보존 개선**: 요약 요청에 이미지를 포함하여 프롬프트 캐시 재사용 가능 — 더 빠르고 저렴한 압축
- **`/rename` 개선**: Claude 처리 중에도 작동 (이전에는 조용히 큐잉됨)
- **프롬프트 입력 리렌더 ~74% 감소**
- **시작 메모리 ~426KB 절감**: 커스텀 CA 인증서 없는 사용자 대상
- **Remote Control `/poll` 빈도 감소**: 연결 상태에서 10분마다 1회 (기존 1~2초) — 서버 부하 ~300배 감소
- **MCP 바이너리 콘텐츠 처리 개선**: PDF, Office 문서, 오디오 등을 올바른 확장자로 디스크에 저장 (기존 raw base64 대신)

### 버그 수정
- **`ANTHROPIC_BASE_URL` 사용 시 API 400 오류 수정**: 도구 검색이 프록시 엔드포인트를 올바르게 감지하고 `tool_reference` 블록 비활성화
- **커스텀 Bedrock 추론 프로필에서 effort 파라미터 미지원 오류 수정**
- **`ToolSearch` 후 빈 모델 응답 수정**: 도구 스키마가 시스템 프롬프트 스타일 태그로 렌더링되어 모델이 조기 중단하던 문제
- **MCP 서버 `instructions` 연결 시 프롬프트 캐시 무효화 수정**
- **느린 SSH 연결에서 Enter가 개행 삽입하던 문제 수정**
- **Windows/WSL 클립보드 비ASCII 텍스트 깨짐 수정**: PowerShell `Set-Clipboard` 사용
- **Windows 시작 시 추가 VS Code 창이 열리던 문제 수정**
- **Windows 네이티브 바이너리에서 음성 모드 실패 수정**: "native audio module could not be loaded"
- **push-to-talk이 `voiceEnabled: true` 설정 시 세션 시작에서 활성화되지 않던 문제 수정**
- **`#NNN` 참조 포함 마크다운 링크가 링크된 URL 대신 현재 레포지토리를 가리키던 문제 수정**
- **레거시 Opus 모델 문자열 고정 시 반복적인 "Model updated to Opus 4.6" 알림 수정**
- **`/security-review` 커맨드가 오래된 git 버전에서 실패하던 문제 수정**
- **`/color` 커맨드에 기본 색상 리셋 추가**: `/color default`, `/color gray`, `/color reset`, `/color none`
- **`AskUserQuestion` 미리보기 대화상자 성능 회귀 수정**: 노트 입력 시 매 키 입력마다 마크다운 렌더링이 재실행되던 문제
- **기능 플래그 디스크 캐시가 초기 시작에서 갱신되지 않아 세션 간 오래된 값이 유지되던 문제 수정**
- **스킬 목록이 `--resume`마다 재주입되던 문제 수정** (~600 토큰 절약)
- **Yoga 기본 메모리 ~16MB 절감**: WASM 사전 로딩 지연

---

## Version 2.1.69

### 주요 하이라이트
- `/claude-api` 스킬 추가 — Claude API 및 Anthropic SDK 애플리케이션 구축 지원
- 음성 STT 10개 신규 언어 지원 (총 20개)
- 대규모 보안·안정성·메모리 수정 및 플러그인/훅 시스템 개선

### 새 기능
- **`/claude-api` 스킬 추가**: Claude API 및 Anthropic SDK로 애플리케이션 구축 지원
- **Ctrl+U로 빈 bash 프롬프트(`!`) 종료**: `escape`와 `backspace`와 동일 동작
- **숫자 키패드 지원**: Claude의 질문 옵션 선택에서 숫자 키패드 사용 가능 (기존 QWERTY 상단 숫자 행만 지원)
- **`/remote-control`에 이름 인자 추가**: `/remote-control My Project` 또는 `--name "My Project"`로 커스텀 세션 제목 설정
- **음성 STT 10개 신규 언어 추가** (총 20개): 러시아어, 폴란드어, 터키어, 네덜란드어, 우크라이나어, 그리스어, 체코어, 덴마크어, 스웨덴어, 노르웨이어
- **Effort 레벨 표시**: 로고와 스피너에 "with low effort" 등 활성 effort 설정 표시
- **`--agent` 사용 시 터미널 제목에 에이전트 이름 표시**
- **`sandbox.enableWeakerNetworkIsolation` 설정 추가** (macOS): MITM 프록시 사용 시 Go 프로그램(`gh`, `gcloud`, `terraform`)의 TLS 인증서 검증 허용
- **`includeGitInstructions` 설정 추가**: 내장 커밋/PR 워크플로우 지침을 시스템 프롬프트에서 제거하는 옵션
- **`/reload-plugins` 커맨드 추가**: 재시작 없이 대기 중인 플러그인 변경사항 활성화
- **`${CLAUDE_SKILL_DIR}` 변수 추가**: 스킬이 SKILL.md 콘텐츠에서 자체 디렉토리 참조 가능
- **`InstructionsLoaded` 훅 이벤트 추가**: CLAUDE.md 또는 `.claude/rules/*.md` 파일 로드 시 발동
- **훅 이벤트에 `agent_id`, `agent_type` 필드 추가**
- **상태 라인 훅 커맨드에 `worktree` 필드 추가**: 이름, 경로, 브랜치, 원본 레포 디렉토리 정보

### 개선
- **MCP 바이너리 콘텐츠 처리 개선**: PDF, Office 문서, 오디오를 올바른 확장자로 디스크에 저장
- **긴 세션 메모리 사용량 개선**: `onSubmit` 안정화로 메시지 업데이트 간 메모리 절약
- **LSP 도구 렌더링 개선**: 전체 파일을 읽지 않도록 변경
- **파일 작업 성능 개선**: 존재 확인 시 파일 내용 읽기 회피 (6개 지점)
- **SDK/CCR 세션 메모리 풋프린트 감소**: stream-json 출력 사용
- **대규모 세션 재개 시 메모리 사용량 감소**: 압축된 히스토리 포함
- **멀티 에이전트 작업 토큰 사용량 감소**: 더 간결한 하위 에이전트 최종 보고서
- **스피너 성능 개선**: 50ms 애니메이션 루프를 셸에서 격리하여 렌더링 및 CPU 오버헤드 감소
- **네이티브 바이너리 UI 렌더링 성능 개선**: React Compiler 적용
- **`--worktree` 시작 개선**: 시작 경로에서 git 서브프로세스 제거
- **macOS 시작 개선**: 관리 설정 확인 시 중복 설정 파일 재로드 제거

### 버그 수정
- **보안 이슈 수정**: 중첩 스킬 탐색이 `node_modules` 같은 gitignored 디렉토리에서 스킬을 로드할 수 있던 문제
- **신뢰 대화상자 수정**: 첫 실행 시 모든 `.mcp.json` 서버가 조용히 활성화되던 문제 → 서버별 승인 대화상자 정상 표시
- **`claude remote-control` 크래시 수정**: npm 설치에서 "bad option: --sdk-url" 오류
- **`--model claude-opus-4-0/4-1`이 현재 버전 대신 deprecated 버전으로 해석되던 문제 수정**
- **macOS 키체인 손상 수정**: 여러 OAuth MCP 서버 사용 시 `security -i` stdin 버퍼 오버플로우
- **`.credentials.json`에서 `subscriptionType` 유실 수정**: "Claude API" 대신 "Claude Pro"/"Claude Max" 정상 표시
- **Linux에서 샌드박스 Bash 명령 후 고스트 dotfile이 작업 디렉토리에 나타나던 문제 수정**
- **Ghostty over SSH에서 Shift+Enter가 `[27;2;13~` 출력하던 문제 수정**
- **stash(Ctrl+S)가 Claude 작업 중 메시지 제출 시 정리되던 문제 수정**
- **Ctrl+O(전사 토글)가 파일 편집이 많은 긴 세션에서 수초간 프리징되던 문제 수정**
- **플랜 모드 피드백 입력에서 멀티라인 텍스트 미지원 수정**: 백슬래시+Enter, Shift+Enter로 개행 삽입 가능
- **대화형 도구(`AskUserQuestion` 등)가 스킬의 allowed-tools에 있을 때 자동 허용되던 문제 수정** — 빈 답변으로 실행되는 권한 프롬프트 우회 방지
- **대용량 미추적 바이너리 파일이 작업 트리에 있을 때 커밋 시 수 GB 메모리 스파이크 수정**
- **긴 세션의 여러 메모리 누수 수정**: React Compiler `memoCache`, REPL 렌더 스코프, 인프로세스 팀메이트, 훅 이벤트 누적 등
- **`--mcp-config`가 손상된 파일을 가리킬 때 행(hang) 수정**
- **많은 스킬/플러그인 설치 시 느린 시작 수정**
- **Windows에서 리터럴 `nul` 파일 생성 수정**: 모델이 CMD 스타일 `2>nul` 리다이렉션을 Git Bash에서 사용할 때

### 변경
- **Sonnet 4.5 Pro/Max/Team Premium 사용자가 Sonnet 4.6으로 자동 마이그레이션**
- **`/resume` 선택기 변경**: 첫 프롬프트 대신 가장 최근 프롬프트를 표시
- **claude.ai MCP 커넥터 실패 시 알림 표시**: 조용히 도구 목록에서 사라지는 대신 알림
- **예시 명령 제안이 Haiku 호출 대신 결정적으로 생성**
- **압축 후 재개 시 프리앰블 요약 생략**

### SDK
- **작업 생성 시 `activeForm` 필드 불필요**: 스피너가 작업 제목으로 폴백

### IDE
- **VSCode**: 압축을 접을 수 있는 "Compacted chat" 카드로 표시
- **VSCode**: 권한 모드 선택기가 `permissions.disableBypassPermissionsMode` 설정 반영
- **VSCode**: RTL 텍스트(아랍어, 히브리어, 페르시아어) 렌더링 반전 수정 (v2.1.63 회귀)

---

## Version 2.1.68

### 주요 하이라이트
- Opus 4.6이 Max/Team 구독자에게 기본 medium effort로 설정
- "ultrathink" 키워드 재도입 — 다음 턴에 high effort 활성화
- Opus 4/4.1 제거 — Opus 4.6으로 자동 이전

### 변경
- **Opus 4.6 기본 medium effort**: Max/Team 구독자 대상. medium effort가 대부분의 작업에 적합한 균형점. `/model`에서 변경 가능
- **"ultrathink" 키워드 재도입**: 다음 턴에 high effort 활성화
- **Opus 4/4.1 퍼스트파티 API에서 제거**: 해당 모델이 고정된 사용자는 자동으로 Opus 4.6으로 이동

---

## Version 2.1.66

### 버그 수정
- **불필요한 오류 로깅 감소**

---

## Version 2.1.63

### 주요 하이라이트
- `/simplify`와 `/batch` 번들 슬래시 커맨드 추가
- 프로젝트 설정 및 자동 메모리가 동일 레포지토리의 git 워크트리 간 공유
- HTTP 훅 추가 — 셸 명령 대신 URL로 JSON POST/수신

### 새 기능
- **`/simplify`와 `/batch` 번들 슬래시 커맨드 추가**
- **HTTP 훅 추가**: 셸 명령 대신 URL로 JSON을 POST하고 JSON을 수신
- **MCP OAuth 인증 시 수동 URL 붙여넣기 폴백 추가**: 자동 localhost 리다이렉트 실패 시 콜백 URL 붙여넣기로 인증 완료
- **`ENABLE_CLAUDEAI_MCP_SERVERS=false` 환경변수 추가**: claude.ai MCP 서버 사용 비활성화
- **`/copy`에 "Always copy full response" 옵션 추가**: 선택 시 코드 블록 선택기를 건너뛰고 전체 응답 복사

### 개선
- **프로젝트 설정 및 자동 메모리가 git 워크트리 간 공유**
- **`/model` 개선**: 슬래시 커맨드 메뉴에 현재 활성 모델 표시
- **`/clear`가 캐시된 스킬을 리셋하도록 수정**: 오래된 스킬 콘텐츠가 새 대화에 잔존하던 문제 해결

### 버그 수정
- **로컬 슬래시 커맨드 출력이 사용자 전송 메시지로 표시되던 문제 수정**: 시스템 메시지로 정상 표시
- **브릿지 폴링 루프 리스너 누수 수정**
- **MCP OAuth 플로우 정리 리스너 누수 수정**
- **훅 설정 메뉴 탐색 시 메모리 누수 수정**
- **자동 승인 시 인터랙티브 권한 핸들러 리스너 누수 수정**
- **파일 카운트 캐시가 glob 무시 패턴을 무시하던 문제 수정**
- **bash 명령 접두사 캐시 메모리 누수 수정**
- **MCP 도구/리소스 캐시 누수 수정**: 서버 재연결 시 발생
- **WebSocket 리스너 누수 수정**: 전송 재연결 시 발생
- **git 루트 감지 캐시 메모리 누수 수정**: 긴 세션에서 무한 증가 가능하던 문제
- **JSON 파싱 캐시 메모리 누수 수정**: 긴 세션에서 무한 증가하던 문제
- **REPL 브릿지 경합 조건 수정**: 초기 연결 플러시 중 새 메시지가 히스토리 메시지와 인터리브되어 순서가 뒤섞이던 문제
- **긴 팀메이트 실행 메모리 누수 수정**: 압축 후에도 모든 메시지가 AppState에 유지되던 문제
- **MCP 서버 fetch 캐시 메모리 누수 수정**: 자주 재연결하는 서버에서 연결 해제 시 캐시 미정리

### IDE
- **VSCode**: 원격 세션이 대화 히스토리에 표시되지 않던 문제 수정
- **VSCode**: 세션 목록에 이름 변경 및 삭제 액션 추가

---

## Version 2.1.62

### 버그 수정
- **프롬프트 제안 캐시 회귀 수정**: 캐시 히트율 감소 문제 해결

---

## Version 2.1.61

### 버그 수정
- **Windows에서 동시 쓰기로 설정 파일 손상되던 문제 수정**

---

## Version 2.1.59

### 주요 하이라이트
- 자동 메모리 (Auto-Memory) 도입 — Claude가 유용한 컨텍스트를 자동 저장, `/memory`로 관리
- `/copy` 커맨드 개선 — 코드 블록 개별 선택 가능

### 새 기능
- **자동 메모리 (Auto-Memory)**: Claude가 유용한 컨텍스트를 자동으로 저장. `/memory`로 관리
- **`/copy` 커맨드 개선**: 코드 블록이 있을 때 인터랙티브 선택기 표시 — 개별 코드 블록 또는 전체 응답 선택 가능

### 개선
- **"Always Allow" 접두사 제안 개선**: 복합 bash 명령에서 서브커맨드별 더 스마트한 접두사 계산
- **짧은 작업 목록 순서 개선**
- **멀티 에이전트 세션 메모리 사용량 개선**: 완료된 하위 에이전트 작업 상태 해제

### 버그 수정
- **MCP OAuth 토큰 갱신 경합 조건 수정**: 여러 Claude Code 인스턴스 동시 실행 시 발생
- **작업 디렉토리 삭제 시 셸 명령 오류 메시지 미표시 수정**
- **설정 파일 손상 수정**: 여러 Claude Code 인스턴스 동시 실행 시 인증 정보가 삭제될 수 있던 문제

---

## Version 2.1.58

### 변경
- **Remote Control을 더 많은 사용자에게 확장**

---

## Version 2.1.56

### IDE
- **VSCode**: "command 'claude-vscode.editor.openLast' not found" 크래시의 또 다른 원인 수정

---

## Version 2.1.55

### 플랫폼
- **Windows**: BashTool이 `EINVAL` 오류로 실패하던 문제 수정

---

## Version 2.1.53

### 주요 하이라이트
- Windows 안정성 대폭 개선 — 여러 크래시 수정
- 대량 에이전트 종료(ctrl+f) 개선

### 버그 수정
- **UI 깜빡임 수정**: 사용자 입력이 제출 후 메시지 렌더링 전 잠깐 사라지던 문제
- **대량 에이전트 종료(ctrl+f) 개선**: 에이전트당 개별 알림 대신 단일 집계 알림, 명령 큐 정상 정리
- **정상 종료 시 Remote Control 사용 시 오래된 세션이 남던 문제 수정**: 해체 네트워크 호출을 병렬화
- **`--worktree`가 첫 실행 시 무시되던 문제 수정**
- **Windows에서 패닉("switch on corrupted value") 수정**
- **Windows에서 다수 프로세스 생성 시 크래시 수정**
- **Linux x64 & Windows x64에서 WebAssembly 인터프리터 크래시 수정**
- **Windows ARM64에서 2분 후 간헐적 크래시 수정**

---

## Version 2.1.52

### IDE
- **VSCode**: Windows에서 확장 크래시 수정 ("command 'claude-vscode.editor.openLast' not found")

---

## Version 2.1.51

### 주요 하이라이트
- `claude remote-control` 서브커맨드 추가 — 모든 사용자에게 로컬 환경 서빙 가능
- BashTool 성능 개선 — 셸 스냅샷 사용 시 로그인 셸 플래그 생략

### 새 기능
- **`claude remote-control` 서브커맨드 추가**: 외부 빌드용으로 모든 사용자에게 로컬 환경 서빙 가능
- **플러그인 npm 소스에서 커스텀 npm 레지스트리 및 특정 버전 고정(pinning) 지원**
- **macOS plist 또는 Windows 레지스트리를 통한 관리 설정 지원**

### 개선
- **BashTool 성능 개선**: 셸 스냅샷 사용 가능 시 로그인 셸(`-l` 플래그) 기본 생략 (기존 `CLAUDE_BASH_NO_LOGIN=true` 필요)
- **50K 문자 초과 도구 결과를 디스크에 저장** (기존 100K) — 컨텍스트 윈도우 사용량 감소, 대화 지속성 향상
- **`/model` 선택기에 사람이 읽기 쉬운 레이블 표시**: "Sonnet 4.5" 등 (기존 raw 모델 ID), 최신 버전 사용 가능 시 업그레이드 힌트
- **플러그인 마켓플레이스 기본 git 타임아웃 30초 → 120초 변경**: `CLAUDE_CODE_PLUGIN_GIT_TIMEOUT_MS`로 설정 가능

### 버그 수정
- **보안 이슈 수정**: `statusLine`과 `fileSuggestion` 훅 커맨드가 인터랙티브 모드에서 워크스페이스 신뢰 수락 없이 실행될 수 있던 문제
- **중복 `control_response` 메시지로 인한 API 400 오류 수정**: WebSocket 재연결에서 중복 어시스턴트 메시지가 대화에 푸시되던 문제
- **슬래시 커맨드 자동완성 크래시 수정**: 플러그인의 SKILL.md 설명이 YAML 배열 등 비문자열 타입일 때 발생
- **SDK에 `CLAUDE_CODE_ACCOUNT_UUID`, `CLAUDE_CODE_USER_EMAIL`, `CLAUDE_CODE_ORGANIZATION_UUID` 환경변수 추가**: 초기 텔레메트리 이벤트에 계정 메타데이터 누락되던 경합 조건 해결

---

## Version 2.1.50 (2026-02-21)

### 주요 하이라이트
- 대규모 메모리 누수(Memory Leak) 수정 — 긴 세션에서의 안정성 대폭 향상
- 에이전트 정의에서 `isolation: worktree` 지원 추가 — 격리된 Git 워크트리에서 에이전트 실행 가능
- Opus 4.6 (Fast Mode)에서 1M 컨텍스트 윈도우 지원

### 새 기능
- **LSP 서버 `startupTimeout` 설정 추가**: LSP 서버 시작 시 타임아웃을 설정 가능
- **`WorktreeCreate` / `WorktreeRemove` 훅 이벤트 추가**: 에이전트 워크트리 격리 시 커스텀 VCS 설정/해제가 가능
- **에이전트 정의에서 `isolation: worktree` 지원**: 에이전트가 선언적으로 격리된 Git 워크트리에서 실행 가능
- **`claude agents` CLI 커맨드 추가**: 설정된 모든 에이전트를 목록으로 확인 가능
- **`CLAUDE_CODE_DISABLE_1M_CONTEXT` 환경변수 추가**: 1M 컨텍스트 윈도우 지원을 비활성화하는 옵션
- **Opus 4.6 (Fast Mode)에서 1M 컨텍스트 윈도우 지원**

### 개선
- **긴 세션 메모리 사용량 개선**: 압축(Compaction) 후 내부 캐시 정리
- **긴 세션 메모리 사용량 개선**: 처리 완료된 대용량 도구 결과를 정리
- **긴 세션 메모리 사용량 개선**: 파일 히스토리 스냅샷에 상한선을 적용하여 무한 메모리 증가 방지
- **헤드리스 모드(`-p` 플래그) 시작 성능 개선**: Yoga WASM 및 UI 컴포넌트 임포트를 지연 로딩

### 버그 수정
- **재개된 세션이 심볼릭 링크 작업 디렉토리에서 보이지 않던 문제 수정** — 세션 저장 경로가 시작 중 다른 시점에 확인되던 문제 해결. SSH 연결 해제 시 세션 데이터 손실도 함께 수정
- **Agent Teams 메모리 누수 수정**: 완료된 팀메이트 작업이 세션 상태에서 가비지 컬렉션되지 않던 문제 해결
- **`CLAUDE_CODE_SIMPLE` 완전 정리 수정**: 스킬, 세션 메모리, 커스텀 에이전트, CLAUDE.md 토큰 카운팅을 완전히 제거하도록 수정
- **`/mcp reconnect` 프리징 수정**: 존재하지 않는 서버 이름을 입력하면 CLI가 멈추던 문제 해결
- **완료된 작업 상태 객체 메모리 누수 수정**: AppState에서 제거되지 않던 문제 해결
- **MCP 도구 검색 버그 수정**: 도구 검색이 활성화되어 있고 프롬프트가 실행 인자로 전달된 경우 MCP 도구가 발견되지 않던 문제 해결
- **LSP 진단 데이터 메모리 누수 수정**: 전달 후 정리되지 않아 긴 세션에서 무한 메모리 증가를 유발하던 문제 해결
- **완료된 작업 출력 메모리 누수 수정**: 많은 작업이 있는 긴 세션에서 메모리 사용량 감소
- **프롬프트 제안 캐시 회귀(Regression) 수정**: 캐시 히트율이 감소하던 문제 해결
- **TaskOutput 메모리 누수 수정**: 정리 후에도 최근 줄이 유지되던 문제 해결
- **CircularBuffer 메모리 누수 수정**: 정리된 항목이 배열에 남아 있던 문제 해결
- **셸 명령 실행 메모리 누수 수정**: `ChildProcess` 및 `AbortController` 참조가 정리 후에도 유지되던 문제 해결

### 변경
- **`CLAUDE_CODE_SIMPLE` 모드 강화**: MCP 도구, 첨부파일, 훅, CLAUDE.md 파일 로딩도 비활성화하여 완전한 최소 경험 제공

### 플랫폼
- **Linux**: glibc 2.30 미만 시스템(예: RHEL 8)에서 네이티브 모듈이 로드되지 않던 문제 수정

### IDE
- **VSCode**: `/extra-usage` 커맨드 지원 추가

---

## Version 2.1.49 (2026-02-20)

### 주요 하이라이트
- **`--worktree` (`-w`) 플래그 추가** — 격리된 Git 워크트리에서 Claude를 실행 가능
- **`ConfigChange` 훅 이벤트 추가** — 엔터프라이즈 보안 감사(Audit) 및 설정 변경 차단 지원
- **Sonnet 4.5 (1M 컨텍스트) → Sonnet 4.6 모델 전환** — Max 플랜에서 Sonnet 4.6이 1M 컨텍스트를 지원

### 새 기능
- **`--worktree` (`-w`) 플래그 추가**: 격리된 Git 워크트리에서 Claude를 시작하는 옵션
- **서브에이전트(Sub-agent) `isolation: "worktree"` 지원**: 임시 Git 워크트리에서 작업 가능
- **에이전트 정의에서 `background: true` 지원**: 항상 백그라운드 작업으로 실행되는 에이전트 정의 가능
- **`ConfigChange` 훅 이벤트 추가**: 세션 중 설정 파일 변경 시 발동하여 엔터프라이즈 보안 감사 및 설정 변경 차단이 가능
- **Ctrl+F 키바인딩으로 백그라운드 에이전트 종료** (2회 확인 방식)
- **플러그인에서 `settings.json` 제공 가능**: 기본 설정을 플러그인과 함께 배포
- **SDK 모델 정보 확장**: `supportsEffort`, `supportedEffortLevels`, `supportsAdaptiveThinking` 필드 추가로 모델 기능 검색 가능

### 개선
- **MCP OAuth 인증 개선**: 단계적 인증(Step-up Auth) 지원 및 탐색 캐싱으로 서버 연결 시 중복 네트워크 요청 감소
- **비대화형 모드(`-p`) 성능 개선**: 시작 시 불필요한 API 호출 생략
- **시작 성능 개선**: MCP 인증 실패 캐싱, 분석용 HTTP 호출 감소, MCP 도구 토큰 카운팅 일괄 처리
- **권한 프롬프트 개선**: 경로 안전성 및 작업 디렉토리 차단 시 제한 이유를 표시
- **파일 미발견 오류 시 수정된 경로를 제안**: 모델이 레포 폴더를 누락했을 때 올바른 경로 제안

### 버그 수정
- **Ctrl+C / ESC 무시 문제 수정**: 백그라운드 에이전트가 실행 중이고 메인 스레드가 유휴 상태일 때 무시되던 문제 해결 — 3초 내 2회 누르면 모든 백그라운드 에이전트 종료
- **플러그인 enable/disable 스코프 자동 감지 수정**: `--scope` 미지정 시 항상 사용자 스코프로 기본 설정되던 문제 해결
- **Verbose 모드 표시 버그 수정**: `/config`에서 토글 시 thinking 블록 표시가 업데이트되지 않던 문제 해결
- **무한 WASM 메모리 증가 수정**: 긴 세션에서 tree-sitter 파서를 주기적으로 리셋하여 해결
- **Yoga 레이아웃 렌더링 문제 수정**: 오래된 yoga 레이아웃 참조로 인한 렌더링 문제 해결
- **Yoga WASM 선형 메모리 무한 증가 수정**: 긴 세션에서 WASM 메모리가 축소되지 않던 문제 해결
- **`disableAllHooks` 관리 설정 계층 수정**: 비관리 설정이 정책에 의해 설정된 관리 훅을 비활성화할 수 없도록 수정
- **`--resume` 세션 선택기 수정**: `/clear`로 시작하는 세션에서 원시 XML 태그가 표시되던 문제 해결
- **프롬프트 제안 캐시 회귀 수정**: 캐시 히트율 감소 문제 해결

### 변경
- **Simple 모드(`CLAUDE_CODE_SIMPLE`)에 파일 편집 도구 추가**: Bash 도구 외에 파일 편집 도구도 포함
- **안전 검사 시 권한 제안 자동 채움**: SDK 사용자가 권한 옵션을 표시 가능
- **Sonnet 4.5 (1M 컨텍스트) 제거**: Max 플랜에서 Sonnet 4.6 모델로 전환 (1M 컨텍스트 지원). `/model`에서 전환 필요

---

## Version 2.1.47

### 주요 하이라이트
- 파일 쓰기 도구(FileWriteTool)의 후행 빈 줄 보존 수정
- Windows 터미널 렌더링 버그 수정
- VSCode 플랜 미리보기 기능 개선

### 새 기능
- 해당 없음

### 개선
- **VSCode 플랜 미리보기 개선**: Claude가 수정할 때 자동 업데이트되고, 플랜이 검토 준비가 되었을 때만 코멘트를 활성화하며, 거부 시에도 미리보기를 열어두어 Claude가 수정 가능
- **메모리 사용량 개선**: 긴 세션에서 API 스트림 버퍼, 에이전트 컨텍스트, 스킬 상태를 사용 후 해제하여 메모리 절약
- **시작 성능 개선**: `SessionStart` 훅 실행을 지연하여 상호작용 가능 시간을 약 500ms 단축
- **`@` 파일 멘션 성능 개선**: 시작 시 인덱스를 미리 준비하고 세션 기반 캐싱과 백그라운드 갱신을 사용하여 파일 제안이 더 빠르게 표시
- **에이전트 작업 완료 후 메모리 사용량 개선**: 작업 완료 후 에이전트 태스크 메시지 기록을 정리
- **긴 에이전트 세션의 메모리 사용량 개선**: 진행 업데이트에서의 O(n²) 메시지 누적을 제거
- **축소된 읽기/검색 결과에서 검색 패턴을 따옴표로 표시**하여 명확성 향상

### 버그 수정
- **파일 쓰기 도구 수정**: `trimEnd()`로 의도적인 후행 빈 줄이 제거되던 문제 해결 — 이제 빈 줄이 정상적으로 보존됨
- **Windows 터미널 렌더링 버그 수정**: `os.EOL`(`\r\n`)로 인해 줄 수가 항상 1로 표시되던 문제 해결
- **Windows에서 마크다운 굵은 글씨/색상 텍스트가 잘못된 문자에 적용되던 문제 수정** — `\r\n` 줄바꿈이 원인
- **압축(Compaction) 실패 수정**: 대화에 많은 PDF 문서가 포함된 경우, 압축 API 전송 시 이미지와 함께 문서 블록을 제거하여 해결
- **Windows에서 MSYS2 또는 Cygwin 셸 사용 시 Bash 도구 출력이 무시되던 문제 수정**
- **Bash 권한 분류기 수정**: 반환된 일치 설명이 실제 입력 규칙과 일치하는지 검증하여, 환각된(hallucinated) 설명이 잘못된 권한 부여를 방지
- **NFS/FUSE 파일 시스템에서 사용자 정의 에이전트가 하나의 파일만 로드되던 문제 수정** — inode가 0을 보고하는 경우
- **플러그인 에이전트 스킬이 전체 이름 대신 짧은 이름으로 참조 시 로드 실패하던 문제 수정**

### 변경
- **`ctrl+f`로 모든 백그라운드 에이전트 종료** (기존 ESC 이중 입력 대신). 이제 ESC로 메인 스레드를 취소해도 백그라운드 에이전트는 계속 실행됨
- **팀메이트 탐색을 Shift+Down만 사용하도록 단순화** (기존 Shift+Up/Down 병행에서 변경)

### 플랫폼
- **Windows**: CWD 추적 임시 파일이 정리되지 않고 무한 누적되던 문제 수정
- **Windows**: Git Bash 터미널에서 Right Alt 키가 `[25~` 이스케이프 시퀀스를 남기던 문제 수정

### IDE
- **VSCode**: 대화 메시지가 `AskUserQuestion` 대화상자가 열려 있는 동안 흐리게 표시되던 문제 수정
- **VSCode**: Git 워크트리에서 원격 URL 확인이 워크트리별 gitdir에서 읽던 문제 수정

---

## Version 2.1.46

### 주요 하이라이트
- macOS에서 터미널 연결 해제 후 고아 프로세스(orphaned CC processes) 수정
- claude.ai MCP 커넥터를 Claude Code에서 사용할 수 있는 지원 추가

### 새 기능
- **claude.ai MCP 커넥터 지원 추가**: claude.ai의 MCP 커넥터를 Claude Code에서 직접 사용 가능

### 버그 수정
- **macOS에서 터미널 연결 해제 시 고아 Claude Code 프로세스가 남던 문제 수정**

---

## Version 2.1.45

### 주요 하이라이트
- **Claude Sonnet 4.6 모델 지원 추가**
- `--add-dir` 디렉토리에서 `enabledPlugins` 및 `extraKnownMarketplaces` 읽기 지원
- 다수의 플랫폼별 버그 수정

### 새 기능
- **Claude Sonnet 4.6 지원 추가**
- **`--add-dir` 디렉토리에서 `enabledPlugins`와 `extraKnownMarketplaces` 읽기 지원**: 추가 디렉토리에서도 플러그인/마켓플레이스 설정 가능
- **`spinnerTipsOverride` 설정 추가**: `tips` 배열로 커스텀 스피너 팁 설정 가능, `excludeDefault: true`로 기본 팁 제외 옵션
- **SDK에 `SDKRateLimitInfo` 및 `SDKRateLimitEvent` 타입 추가**: 사용량, 리셋 시간, 초과 정보 등 속도 제한 상태 업데이트 수신 가능

### 개선
- **시작 성능 개선**: 통계 캐싱을 위한 세션 기록의 즉시 로딩(eager loading)을 제거
- **셸 명령의 대용량 출력에 대한 메모리 사용량 개선** — 명령 출력 크기에 따라 RSS가 무한히 증가하지 않음
- **축소된 읽기/검색 그룹이 활성 중일 때 요약 줄 아래에 현재 처리 중인 파일이나 검색 패턴을 표시**

### 버그 수정
- **Agent Teams 팀메이트가 Bedrock, Vertex, Foundry에서 실패하던 문제 수정**: tmux로 생성된 프로세스에 API 공급자 환경변수 전파
- **macOS에서 샌드박스 "operation not permitted" 오류 수정**: 임시 파일 작성 시 올바른 사용자별 임시 디렉토리 사용
- **Task 도구(백그라운드 에이전트)가 완료 시 `ReferenceError`로 크래시하던 문제 수정**
- **이미지가 입력에 붙여넣기된 상태에서 Enter 시 자동완성 제안이 수락되지 않던 문제 수정**
- **하위 에이전트가 호출한 스킬이 압축 후 메인 세션 컨텍스트에 잘못 표시되던 문제 수정**
- **시작마다 `.claude.json.backup` 파일이 과도하게 누적되던 문제 수정**
- **재시작 없이 설치 직후 플러그인 제공 커맨드, 에이전트, 훅이 즉시 사용 가능하도록 수정**

### IDE
- **VSCode**: 권한 대상 선택(프로젝트/사용자/세션)이 세션 간에 유지되도록 개선

---

## Version 2.1.44

### 주요 하이라이트
- 깊이 중첩된 디렉토리 경로에서 발생하는 `ENAMETOOLONG` 오류 수정
- 인증 갱신 오류 수정

### 버그 수정
- **`ENAMETOOLONG` 오류 수정**: 깊이 중첩된 디렉토리 경로에서 파일명이 너무 길어 발생하던 오류 해결
- **인증 갱신(auth refresh) 오류 수정**

---

## Version 2.1.43

### 주요 하이라이트
- AWS 인증 갱신 무한 대기 문제에 3분 타임아웃 추가
- Vertex/Bedrock에서 구조화된 출력(structured-outputs) 베타 헤더가 무조건 전송되던 문제 수정

### 버그 수정
- **AWS 인증 갱신 무한 대기 수정**: 3분 타임아웃 추가로 무한 대기 방지
- **`.claude/agents/` 디렉토리의 에이전트가 아닌 마크다운 파일에 대한 불필요한 경고 수정**
- **Vertex/Bedrock에서 `structured-outputs` 베타 헤더가 무조건 전송되던 문제 수정**
