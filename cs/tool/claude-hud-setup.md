# Claude HUD 설정 가이드

> `[2] 입문` · 선수 지식: [Claude Code StatusLine](./claude-code-statusline.md)

> 한 줄 정의: Claude Code 플러그인 기반의 실시간 HUD(Head-Up Display)로, 컨텍스트 사용률, 도구 활동, 에이전트 상태, 할 일 진행률을 터미널 하단에 표시하는 상태 표시줄

`#ClaudeHUD` `#ClaudeCode` `#Plugin` `#StatusLine` `#HUD` `#컨텍스트` `#터미널` `#개발도구` `#DX` `#DeveloperExperience` `#플러그인` `#실시간모니터링`

## 왜 알아야 하는가?

- **실무**: 컨텍스트 윈도우 소진을 사전에 감지하여 대화 리셋 타이밍을 최적화하고, 도구/에이전트 실행 상황을 실시간으로 파악하여 작업 효율을 높임
- **기반 지식**: Claude Code의 StatusLine API, 플러그인 시스템, stdin/stdout 기반 데이터 흐름 등 CLI 도구 확장 방식을 이해하는 기반이 됨
- **커스터마이징**: 프리셋(Full/Essential/Minimal)과 세부 옵션으로 개인 워크플로우에 최적화된 정보 표시 환경을 구성할 수 있음

## 핵심 개념

- Claude HUD는 Claude Code의 **플러그인 시스템**으로 설치하며, 네이티브 StatusLine API 위에서 동작
- 약 **300ms 간격**으로 Claude Code가 JSON 데이터를 stdin으로 전달하고, HUD가 파싱하여 표시
- 컨텍스트 사용률을 **색상 바**(초록 → 노랑 → 빨강)로 시각화하여 한눈에 상태 파악 가능
- 트랜스크립트(Transcript) JSONL 파일을 파싱하여 도구/에이전트/할 일 정보를 추출
- `config.json`으로 표시 항목, 레이아웃, Git 상태 등을 세밀하게 커스터마이징

## 쉽게 이해하기

Claude HUD는 **자동차 계기판**과 같다.

운전 중 속도계, 연료 게이지, 엔진 경고등을 항상 볼 수 있듯이, Claude HUD는 현재 모델, 컨텍스트 잔량, 실행 중인 도구, 에이전트 상태를 터미널 하단에 항상 표시한다.

기존 StatusLine이 수동으로 셸 스크립트를 작성하는 **DIY 계기판**이었다면, Claude HUD는 **기성품 디지털 계기판**으로 설치만 하면 바로 사용할 수 있다.

```
┌─────────────────────────────────────────────────────────────────┐
│  Claude Code 대화 영역                                          │
│                                                                 │
│  > 사용자 입력...                                                │
│  Claude 응답...                                                 │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│  [Opus | Max] │ my-project git:(main*)                          │ ← 라인 1: 모델/프로젝트
│  Context █████░░░░░ 45% │ Usage ██░░░░░░░░ 25% (1h 30m / 5h)   │ ← 라인 2: 컨텍스트/사용량
│  ◐ Edit: auth.ts | ✓ Read ×3 | ✓ Grep ×2                       │ ← 라인 3: 도구 활동 (선택)
│  ◐ explore [haiku]: Finding auth code (2m 15s)                  │ ← 라인 4: 에이전트 (선택)
│  ▸ Fix authentication bug (2/5)                                 │ ← 라인 5: 할 일 (선택)
└─────────────────────────────────────────────────────────────────┘
```

## 상세 설명

### StatusLine vs Claude HUD 비교

| 항목 | StatusLine (수동) | Claude HUD (플러그인) |
|------|------------------|----------------------|
| 설치 방식 | `settings.json` 직접 편집 | `/plugin install` 명령 |
| 설정 방식 | bash 스크립트 직접 작성 | `/claude-hud:configure` 대화형 설정 |
| 표시 정보 | 사용자가 직접 구현한 항목만 | 모델, 컨텍스트, 도구, 에이전트, 할 일 등 기본 제공 |
| 도구/에이전트 추적 | 직접 구현 필요 | 트랜스크립트 자동 파싱 |
| 사용량 제한 표시 | 불가 | Pro/Max/Team 구독 시 자동 표시 |
| 커스터마이징 | 무제한 (코드 직접 작성) | config.json 옵션 범위 내 |
| 의존성 | `jq`, bash | Node.js 18+ |

**왜 HUD를 선택하는가?**

직접 셸 스크립트를 작성할 필요 없이, 검증된 플러그인으로 풍부한 정보를 즉시 표시할 수 있다. 특히 도구 활동 추적, 에이전트 모니터링, 할 일 진행률 같은 기능은 직접 구현하기 어렵다.

### 설치 방법

#### 사전 요구사항

| 항목 | 요구사항 |
|------|---------|
| Claude Code | v1.0.80 이상 |
| Node.js | 18 이상 (또는 Bun) |

```bash
# 버전 확인
claude --version
node --version
```

#### Step 1: 마켓플레이스 추가

```
/plugin marketplace add jarrodwatts/claude-hud
```

Claude Code 플러그인 마켓플레이스에 `jarrodwatts/claude-hud` 저장소를 등록한다.

#### Step 2: 플러그인 설치

```
/plugin install claude-hud
```

> **Linux 사용자 주의**: `/tmp`이 별도 파일시스템(tmpfs)인 경우 `EXDEV: cross-device link not permitted` 에러가 발생할 수 있다.
>
> ```bash
> # 해결: TMPDIR을 홈 디렉토리로 변경 후 실행
> mkdir -p ~/.cache/tmp && TMPDIR=~/.cache/tmp claude
> ```
> 이후 해당 세션에서 `/plugin install claude-hud` 실행.

#### Step 3: StatusLine 설정

```
/claude-hud:setup
```

이 명령은 `~/.claude/settings.json`에 statusLine 설정을 자동으로 추가한다. 설정 후 즉시 적용되며 재시작이 필요 없다.

**왜 별도 setup 단계가 필요한가?**

플러그인 설치와 statusLine 설정은 분리되어 있다. `plugin.json`에는 메타데이터만 포함되고, statusLine 명령어는 `settings.json`에 등록해야 Claude Code가 인식하기 때문이다.

### 설정 구조 (동작 원리)

```
Claude Code → stdin JSON → claude-hud → stdout → 터미널 표시
           ↘ transcript JSONL (도구, 에이전트, 할 일)
```

#### 데이터 소스

| 소스 | 데이터 | 정확도 |
|------|--------|--------|
| stdin JSON | 모델명, 컨텍스트 토큰, 윈도우 크기 | 네이티브 (정확) |
| Transcript JSONL | 도구 사용, 에이전트 상태, 할 일 목록 | 파싱 기반 |
| 설정 파일 | MCP 서버 수, 훅 수, CLAUDE.md 규칙 수 | 파일 읽기 |
| OAuth API | 사용량 제한 (Pro/Max/Team) | API 호출 (60초 캐시) |

### 커스터마이징

#### 대화형 설정

```
/claude-hud:configure
```

대화형 플로우로 안내되며, 직접 파일을 편집할 필요가 없다:

1. **프리셋 선택**: Full / Essential / Minimal
2. **개별 요소 토글**: 항목별 on/off
3. **미리보기**: 저장 전 결과 확인

#### 프리셋

| 프리셋 | 표시 항목 | 추천 사용자 |
|--------|----------|------------|
| **Full** | 모든 항목 (도구, 에이전트, 할 일, Git, 사용량, 시간) | 모든 정보를 한눈에 보고 싶은 사용자 |
| **Essential** | 활동 라인 + Git 상태, 최소 정보 | 핵심 정보만 원하는 사용자 |
| **Minimal** | 모델명 + 컨텍스트 바만 | 깔끔한 화면을 선호하는 사용자 |

#### 수동 설정 (config.json)

설정 파일 위치:

```
~/.claude/plugins/claude-hud/config.json
```

##### 전체 옵션 레퍼런스

**레이아웃 및 프로젝트**

| 옵션 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `lineLayout` | string | `expanded` | `expanded` (멀티라인) 또는 `compact` (단일 라인) |
| `pathLevels` | 1-3 | 1 | 프로젝트 경로에 표시할 디렉토리 깊이 |

**Git 상태**

| 옵션 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `gitStatus.enabled` | boolean | true | Git 브랜치 표시 |
| `gitStatus.showDirty` | boolean | true | 커밋되지 않은 변경 시 `*` 표시 |
| `gitStatus.showAheadBehind` | boolean | false | 리모트 대비 `↑N ↓N` 표시 |
| `gitStatus.showFileStats` | boolean | false | 파일 변경 카운트 `!M +A ✘D ?U` 표시 |

**표시 요소**

| 옵션 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `display.showModel` | boolean | true | 모델명 `[Opus]` 표시 |
| `display.showContextBar` | boolean | true | 시각적 컨텍스트 바 `████░░░░░░` 표시 |
| `display.contextValue` | string | `percent` | 컨텍스트 표시 형식 (`percent` 또는 `tokens`) |
| `display.showConfigCounts` | boolean | false | CLAUDE.md, 규칙, MCP, 훅 카운트 표시 |
| `display.showDuration` | boolean | false | 세션 지속 시간 표시 |
| `display.showSpeed` | boolean | false | 출력 토큰 속도 표시 |
| `display.showUsage` | boolean | true | 사용량 제한 표시 (Pro/Max/Team) |
| `display.showTokenBreakdown` | boolean | true | 85% 이상 시 토큰 상세 표시 |
| `display.showTools` | boolean | false | 도구 활동 라인 표시 |
| `display.showAgents` | boolean | false | 에이전트 활동 라인 표시 |
| `display.showTodos` | boolean | false | 할 일 진행률 라인 표시 |

##### 설정 예시

```json
{
  "lineLayout": "expanded",
  "pathLevels": 2,
  "gitStatus": {
    "enabled": true,
    "showDirty": true,
    "showAheadBehind": true,
    "showFileStats": true
  },
  "display": {
    "showTools": true,
    "showAgents": true,
    "showTodos": true,
    "showConfigCounts": true,
    "showDuration": true
  }
}
```

### 컨텍스트 바 색상 임계값

| 범위 | 색상 | 의미 | 동작 |
|------|------|------|------|
| 0-70% | 초록 | 정상 | 기본 표시 |
| 70-85% | 노랑 | 경고 | 주의 필요 |
| 85%+ | 빨강 | 위험 | 토큰 상세 분석 표시 |

**왜 이 임계값인가?**

컨텍스트 윈도우가 85%를 넘으면 Claude Code가 자동 압축(compression)을 수행하기 시작한다. 이전 대화가 요약되면서 정보 손실이 발생할 수 있으므로, 이 시점을 빨간색으로 경고하여 새 대화 시작을 고려하도록 유도한다.

### 표시 예시

**기본 (2줄)**:
```
[Opus | Max] │ my-project git:(main*)
Context █████░░░░░ 45% │ Usage ██░░░░░░░░ 25% (1h 30m / 5h)
```

**경로 깊이별**:
```
pathLevels: 1 → [Opus] │ my-project git:(main)
pathLevels: 2 → [Opus] │ apps/my-project git:(main)
pathLevels: 3 → [Opus] │ dev/apps/my-project git:(main)
```

**Git 상태별**:
```
변경 없음    → git:(main)
변경 있음    → git:(main*)
ahead/behind → git:(main ↑2 ↓1)
파일 통계    → git:(main* !3 +1 ?2)
```
- `!` = 수정된 파일, `+` = 추가/스테이징, `✘` = 삭제, `?` = 추적되지 않는 파일

**Full 프리셋 (모든 항목)**:
```
[Opus | Max] │ my-project git:(main* !3 +1 ?2)
Context █████░░░░░ 45% │ Usage ██░░░░░░░░ 25% (1h 30m / 5h)
◐ Edit: auth.ts | ✓ Read ×3 | ✓ Grep ×2
◐ explore [haiku]: Finding auth code (2m 15s)
▸ Fix authentication bug (2/5)
```

### Usage (사용량 제한) 표시

Pro/Max/Team 구독자에게 기본 활성화되며, 5시간/7일 사용량 제한을 시각화한다.

```
Context █████░░░░░ 45% │ Usage ██░░░░░░░░ 25% (1h 30m / 5h) | ██████████ 85% (2d / 7d)
```

| 조건 | 표시 |
|------|------|
| API 사용자 | 표시 안 됨 (종량제이므로) |
| Pro/Max/Team | 5시간 사용량 바 표시 |
| 7일 사용량 ≥ 80% (기본) | 7일 사용량 바 추가 표시 |
| AWS Bedrock | `Bedrock` 표시, 사용량 숨김 |

## 트러블슈팅

### 사례 1: StatusLine이 표시되지 않음

#### 증상

플러그인 설치 후에도 터미널 하단에 아무것도 표시되지 않음.

#### 원인 분석

- 플러그인이 설치되지 않았거나 statusLine이 `settings.json`에 등록되지 않음
- Claude Code 버전이 v1.0.80 미만

#### 해결 방법

```bash
# 1. Claude Code 버전 확인
claude --version

# 2. 플러그인 목록 확인
# Claude Code 내에서:
/plugin list

# 3. 재설치
/plugin marketplace add jarrodwatts/claude-hud
/plugin install claude-hud
/claude-hud:setup
```

### 사례 2: `[claude-hud] Initializing...` 메시지가 계속 표시됨

#### 증상

HUD가 초기화 메시지에서 넘어가지 않음.

#### 원인 분석

첫 호출 시 stdin 데이터가 아직 전달되지 않은 상태. 정상 동작이며 짧은 시간 후 자동으로 해결된다.

#### 해결 방법

잠시 대기. 지속되면 Claude Code를 재시작한다.

### 사례 3: 도구/에이전트/할 일 라인이 보이지 않음

#### 증상

모델과 컨텍스트 바는 표시되지만 도구 활동 등이 표시되지 않음.

#### 원인 분석

해당 라인은 **기본적으로 비활성화**되어 있다. 또한 실제 활동이 없으면 표시되지 않는다.

#### 해결 방법

```
# 대화형 설정으로 활성화
/claude-hud:configure

# 또는 수동으로 config.json 편집
```

```json
{
  "display": {
    "showTools": true,
    "showAgents": true,
    "showTodos": true
  }
}
```

활성화 후에도 해당 활동이 발생해야 라인이 표시된다.

### 사례 4: 설정 변경이 반영되지 않음

#### 증상

`config.json`을 수정했지만 HUD에 변화가 없음.

#### 원인 분석

JSON 문법 에러가 있으면 자동으로 기본값으로 폴백(fallback)된다.

#### 해결 방법

```bash
# JSON 문법 검증
cat ~/.claude/plugins/claude-hud/config.json | jq .

# 에러 발생 시 config.json 삭제 후 재설정
rm ~/.claude/plugins/claude-hud/config.json
# Claude Code 내에서:
/claude-hud:configure
```

### 사례 5: Linux에서 설치 실패 (EXDEV 에러)

#### 증상

```
EXDEV: cross-device link not permitted
```

#### 원인 분석

Linux에서 `/tmp`이 별도 파일시스템(tmpfs)으로 마운트되어 있어, 크로스 디바이스 링크가 차단됨.

#### 해결 방법

```bash
mkdir -p ~/.cache/tmp && TMPDIR=~/.cache/tmp claude
```

이 세션에서 `/plugin install claude-hud` 실행.

#### 예방 조치

`.bashrc` 또는 `.zshrc`에 추가:

```bash
export TMPDIR=~/.cache/tmp
```

## 면접 예상 질문

### Q: Claude Code의 StatusLine API는 어떻게 동작하는가?

A: Claude Code는 `~/.claude/settings.json`의 `statusLine.command`에 정의된 셸 명령어를 약 300ms 간격으로 실행한다. 실행 시 현재 모델, 컨텍스트 윈도우 사용량, 트랜스크립트 경로 등의 정보를 JSON 형태로 stdin에 전달하고, 명령어의 stdout 출력을 터미널 하단에 렌더링한다. 이 구조는 **파이프라인 패턴**으로, 데이터 생산자(Claude Code)와 소비자(statusLine 명령어)가 stdin/stdout으로 느슨하게 결합되어 있어 플러그인 교체가 용이하다.

### Q: 플러그인 시스템과 직접 StatusLine 스크립트의 트레이드오프는?

A: 직접 스크립트는 완전한 자유도를 제공하지만, 도구 활동 추적이나 사용량 API 연동 같은 복잡한 기능 구현이 어렵다. 플러그인은 이런 기능을 기본 제공하지만 커스터마이징이 `config.json` 옵션 범위로 제한된다. 대부분의 사용자에게는 플러그인의 편의성이 스크립트의 자유도보다 가치 있으며, 고급 사용자는 플러그인 소스를 포크하여 두 장점을 결합할 수 있다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Claude Code StatusLine 설정 가이드](./claude-code-statusline.md) | StatusLine API 기초, 수동 설정 방식 | 입문 |
| [Claude Code 릴리스 노트](./claude-code-release-notes.md) | Claude Code 기능 업데이트 | 입문 |

## 참고 자료

- [Claude HUD GitHub 저장소](https://github.com/jarrodwatts/claude-hud)
- [Claude Code 공식 문서 - Plugins](https://docs.anthropic.com/en/docs/claude-code)
- [Claude Code StatusLine API](https://docs.anthropic.com/en/docs/claude-code)
