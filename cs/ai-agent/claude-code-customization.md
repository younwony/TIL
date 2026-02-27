# Claude Code 커스터마이징 마스터 가이드

> `[3] 중급` · 선수 지식: [Claude Code Workflow](./claude-code-workflow.md), [Claude Code 설정 체계](./claude-code-settings.md)

> `Trend` 2026

> GeekNews 커뮤니티에서 검증된 Claude Code 창시자/파워유저의 커스터마이징 실전 팁

`#ClaudeCode` `#Customization` `#커스터마이징` `#BorisChern` `#CLAUDE.md` `#Worktree` `#병렬세션` `#설정최적화` `#Hook` `#Skill` `#MCP` `#SubAgent` `#PlanMode` `#피드백루프` `#FeedbackLoop` `#환경변수` `#settings.json` `#Statusline` `#Keybinding` `#OutputStyle` `#Sandbox` `#Agent` `#Anthropic`

## 왜 알아야 하는가?

- **실무**: Claude Code의 기본값만으로도 충분하지만, 커스터마이징을 통해 **생산성 2~3배 향상** 가능
- **면접**: AI 도구를 단순 사용이 아닌 **체계적으로 커스터마이징하고 팀에 공유**하는 역량 입증
- **기반 지식**: 37개 설정 항목과 84개 환경변수를 이해하면 Claude Code의 전체 동작 원리를 파악

## 핵심 개념

- **12가지 커스터마이징 영역**: 터미널, 모델, 플러그인, 에이전트, 권한, 샌드박스, 상태바, 키바인딩, 훅, 출력 스타일, 스피너, 설정/환경변수
- **병렬 worktree 워크플로우**: 3~5개 git worktree를 동시 운영하며 각각 독립 Claude 세션 실행
- **검증 피드백 루프**: 모든 변경 후 반드시 검증 방법을 제공하여 최종 결과물 품질 보장
- **계획과 실행의 분리**: Research → Plan → Annotation → Todo → Implementation → Feedback 6단계

## 쉽게 이해하기

**Claude Code 커스터마이징**을 **자동차 튜닝**에 비유할 수 있습니다.

- **기본 설정(출고 상태)** = 누구나 바로 운전할 수 있는 표준 세팅
- **CLAUDE.md** = 운전 매뉴얼 (이 차에서는 이렇게 운전하세요)
- **settings.json** = 엔진 제어 유닛(ECU) 세팅 (기술적 미세 조정)
- **Hook** = 블랙박스/자동 브레이크 (위험 상황 자동 개입)
- **병렬 worktree** = 여러 대의 차를 동시에 운전 (각자 다른 목적지)
- **피드백 루프** = 매 코너마다 라인 체크 (실수하면 즉시 교정)
- **커스텀 에이전트** = 전문 운전자 교대 (짐 운반은 트럭, 경주는 F1)

핵심은 **"뛰어난 기본값 + 높은 커스터마이징 자유도"** 를 동시에 갖추도록 설계되었다는 점입니다.

## 상세 설명

### 1. 12가지 커스터마이징 영역

Claude Code 창시자가 직접 공개한 12가지 커스터마이징 포인트입니다.

#### 1.1 터미널 설정

```bash
# 라이트/다크 모드 전환
/config

# Shift+Enter 줄바꿈 활성화
/terminal-setup

# Vim 모드 활성화
/vim
```

**권장 터미널**: Ghostty (동기화 렌더링, 24비트 컬러, 유니코드 지원)

**추가 팁**:
- iTerm2 알림 활성화 또는 커스텀 알림 훅 사용
- tmux로 worktree당 하나의 색상 코드 탭 유지
- 음성 받아쓰기 활용 (타이핑의 3배 속도, 더 상세한 프롬프트 가능)

#### 1.2 Effort 레벨 조정

```bash
# 모델/성능 수준 선택
/model
```

| 레벨 | 용도 | 특징 |
|------|------|------|
| Low | 빠른 응답 | 간단한 질문, 코드 설명 |
| Medium | 균형 | 일반적인 코드 작성 |
| High | 고지능 | 복잡한 설계, 디버깅 |

> **창시자 Boris Cherny**: 모든 작업에 **Opus + High** 사용. "크고 느리지만 조정이 적게 필요하고 도구 활용 능력이 뛰어나 결과적으로 더 빠르다."

#### 1.3 플러그인/MCP/스킬 설치

```bash
# 플러그인 설치
/plugin

# settings.json을 Git에 체크인하여 팀과 공유
git add .claude/settings.json
```

**Skills vs MCP 구분**:
- **Skills**: 에이전트가 원시 환경(바이너리, 스크립트, 문서)에 접근하는 **스크립팅 레이어**
- **MCP**: 비대한 API 대신 몇 가지 강력한 고수준 도구를 제공하는 **인증/보안 게이트웨이**

#### 1.4 커스텀 에이전트 생성

```
.claude/agents/
├── code-simplifier.md    # 코드 단순화 전문
├── verify-app.md         # 앱 검증 전문
└── analytics-engineer.md # 데이터 분석 전문
```

각 에이전트에 이름, 색상, 도구 세트, 권한 모드를 지정합니다.

```bash
# 에이전트 목록 확인 및 시작
/agents

# 기본 에이전트 설정
claude --agent code-simplifier
```

#### 1.5 권한 사전 승인 (와일드카드)

```bash
# 허용/차단 목록 관리
/permissions
```

와일드카드 문법으로 팀 정책을 settings.json에 체크인합니다.

```json
{
  "permissions": {
    "allow": [
      "Bash(bun run *)",
      "Bash(npm test *)",
      "Read(*)"
    ],
    "deny": [
      "Bash(rm -rf *)",
      "Bash(git push --force *)"
    ]
  }
}
```

#### 1.6 샌드박싱

```bash
# 샌드박스 활성화
/sandbox
```

- 파일 격리와 네트워크 격리 모두 지원
- 신뢰할 수 없는 코드 실행 시 필수
- Windows 지원은 추후 예정

#### 1.7 상태 표시줄

```bash
# 상태 표시줄 자동 생성
/statusline
```

모델명, 디렉터리, 남은 컨텍스트, 비용, git 브랜치 등을 자유롭게 구성합니다. 팀원마다 다른 설정 사용이 가능합니다.

#### 1.8 키바인딩 커스터마이징

```bash
# 모든 키 바인딩 재매핑
/keybindings
```

설정이 실시간 리로드되어 즉시 확인이 가능합니다.

#### 1.9 훅 설정

Claude 라이프사이클에 결정론적으로 개입하는 셸 스크립트입니다.

```
PreToolUse    → 도구 실행 전 검사/차단
PostToolUse   → 도구 실행 후 처리/로깅
```

**실전 활용 예시**:
- Slack/Opus로 권한 요청 자동 라우팅
- 코드 포매팅 자동 처리로 CI 오류 방지
- git commit 시점에서 테스트 통과 강제

```bash
# PreToolUse Hook으로 커밋 차단 예시
# /tmp/agent-pre-commit-pass 파일 확인
# 파일 없으면 커밋 차단 → 테스트 통과까지 강제
```

#### 1.10 출력 스타일 설정

```bash
/config
```

| 스타일 | 설명 | 용도 |
|--------|------|------|
| explanatory | 프레임워크와 패턴 설명 | 코드 리뷰, 학습 |
| learning | 코드 변경 과정을 코칭 형태로 안내 | 주니어 개발자, 자기 학습 |
| custom | 직접 정의한 출력 형식 | 팀별 특화 |

#### 1.11 스피너 동사 커스터마이징

기본 동사 목록("Thinking...", "Writing...")에 자신만의 동사를 추가하거나 교체할 수 있습니다. settings.json에 체크인하여 팀과 공유합니다.

#### 1.12 전체 설정 규모

| 항목 | 수량 | 관리 방법 |
|------|------|----------|
| 설정 항목 | 37개 | settings.json |
| 환경 변수 | 84개 | settings.json의 "env" 필드 또는 셸 |
| 설정 범위 | 4단계 | 코드베이스 → 서브폴더 → 개인 → 엔터프라이즈 |

**주요 환경 변수**:

```bash
HTTPS_PROXY / HTTP_PROXY     # 원시 트래픽 검사
MCP_TOOL_TIMEOUT              # MCP 도구 타임아웃 (기본값 증가 권장)
BASH_MAX_TIMEOUT_MS           # Bash 최대 타임아웃 (보수적 기본값 증가)
ANTHROPIC_API_KEY             # 엔터프라이즈 API 키 (사용량 기반 가격)
```

### 2. 창시자 Boris Cherny 워크플로우

Claude Code를 만든 Boris Cherny의 실제 일상 워크플로우입니다.

#### 2.1 5개 병렬 worktree + shell alias

```bash
# shell alias 예시
alias za='cd ~/project/worktree-a && claude'
alias zb='cd ~/project/worktree-b && claude'
alias zc='cd ~/project/worktree-c && claude'
alias zd='cd ~/project/worktree-d && claude'
alias ze='cd ~/project/worktree-e && claude'
```

- 터미널에서 5개의 Claude를 병렬 실행
- 웹에서 추가 5~10개를 동시 운영
- 각 탭에 번호를 붙이고 시스템 알림으로 입력 필요 시점 파악
- 로그 확인/BigQuery 실행 전용 **analysis worktree** 별도 운영

#### 2.2 Plan Mode → Auto-accept 전환

```
1. Plan Mode로 시작 → 계획에 에너지 투자
2. 계획 확정 후 Auto-accept 모드로 전환
3. 작업 실패 시 즉시 Plan Mode로 복귀하여 재계획
4. 두 번째 Claude를 "스태프 엔지니어" 역할로 계획 검토
```

> **핵심**: 대부분 Plan 모드로 시작해 계획을 다듬은 후 auto-accept 모드로 전환한다.

#### 2.3 CLAUDE.md 지속 관리

```
규칙: Claude가 잘못된 행동을 할 때마다 해당 내용을 CLAUDE.md에 추가
```

- 팀 전체가 공유하는 단일 문서를 Git에 체크인
- 코드 리뷰 시 동료 PR에 `@.claude`를 태그해 규칙 추가
- 로그 분석으로 공통 실패 패턴 발견 → CLAUDE.md 지속 개선
- 지속적 개선으로 실수율이 **측정 가능하게 감소**

**CLAUDE.md 작성 안티패턴**:

| 안티패턴 | 이유 | 대안 |
|----------|------|------|
| `@`-파일 과다 문서화 | 매 실행마다 전체 파일이 컨텍스트에 임베딩 | 핵심 도구만 10개 불릿으로 |
| 부정형 제약 | "절대 사용 금지"는 효과 낮음 | "대신 이렇게 사용" |
| 13KB 초과 | 컨텍스트 비대화 | 13KB~25KB 규모로 유지 |
| 복잡한 CLI 직접 기술 | 이해도 저하 | bash 래퍼로 추상화 |

#### 2.4 검증 피드백 루프 (가장 중요)

```
Claude에게 작업을 검증할 방법 제공
→ 최종 결과물 품질이 2~3배 향상
```

**검증 방법 예시**:
- bash 명령 실행 (빌드, 린트, 타입 체크)
- 테스트 스위트 실행
- 브라우저/시뮬레이터 테스트
- 지속적 타입 체크 수행

### 3. 계획과 실행의 분리 (6단계)

"계획 승인 전에는 Claude에게 코드를 쓰게 하지 않는다"는 철학입니다.

#### 3.1 Research (리서치)

```
지시: "코드베이스를 깊이 있게, 세부적으로 분석하라"
출력: research.md
목적: 캐싱 누락, ORM 규칙 미반영, 중복 로직 같은 구조적 오류 사전 차단
```

#### 3.2 Planning (계획 수립)

```
지시: "실제 코드 스니펫, 수정 파일 경로, 트레이드오프를 포함한 계획 작성"
출력: plan.md
팁: 오픈소스 참조 구현을 함께 제공하면 계획 품질이 크게 향상
```

#### 3.3 Annotation Cycle (주석 순환)

```
지시: "아직 구현하지 말고(Don't implement yet) 주석만 반영하라"
반복: 1~6회
```

작성자가 plan.md에 직접 인라인 주석을 추가합니다:
- 잘못된 가정 수정
- 접근법 거부
- 도메인 지식 주입

> **"Don't implement yet"** 명령이 조기 실행을 방지하는 핵심 안전장치

#### 3.4 Todo List 생성

구현 전 세부 작업 목록을 추가하여 진행 상황을 추적합니다. 장시간 세션에서도 상태를 파악할 수 있습니다.

#### 3.5 Implementation (구현)

모든 결정 확정 후 표준화된 프롬프트로 실행합니다:

```
- "모든 태스크 완료까지 멈추지 말 것"
- "불필요한 주석 금지"
- "any/unknown 타입 금지"
- "지속적 타입체크 수행"
```

이 단계는 계획을 그대로 실행하는 **기계적 단계**입니다. 창의적 판단은 이미 계획 단계에서 완료된 상태입니다.

#### 3.6 Feedback (피드백)

작성자가 **감독자 역할**로 전환합니다:
- 짧고 명확한 피드백: "이 함수 누락됨", "admin 앱으로 이동"
- 잘못된 방향 시 `git revert` 후 범위 축소 재시도

### 4. 실전 팁 정리

#### 4.1 CLAUDE.md 작성 팁

**권장 구조**:
- 엔지니어 30% 이상이 사용하는 도구만 문서화
- 각 도구마다 10개 불릿으로 80% 사용 사례 집중
- 13KB~25KB 규모로 유지 (모노레포 기준)

**지속 관리 원칙**:
- Claude가 잘못된 행동을 할 때마다 규칙 추가
- 하루 1회 이상 반복 작업은 Skill/Command로 변환
- 매번 수정할 때마다 "CLAUDE.md를 업데이트해서 같은 실수를 반복하지 않도록 지시"

#### 4.2 컨텍스트 관리

**200K 토큰 윈도우 활용법**:

| 전략 | 설명 |
|------|------|
| `/compact` 회피 | 자동 압축은 오류 발생 가능성 높음 |
| `/clear` + `/catchup` 조합 | 상태 초기화 후 git 브랜치의 모든 변경 파일 읽기 |
| Document & Clear | 대규모 작업 시 진행상황을 `.md`에 기록 후 새 세션 시작 |
| 한 번의 긴 세션 | 리서치부터 구현까지 연속 수행하여 맥락 축적 |

#### 4.3 서브에이전트 활용

```
요청에 "use subagents" 추가
→ 개별 작업 위임
→ 메인 에이전트의 컨텍스트 윈도우 보존
```

**Task() vs 커스텀 Subagent**:
- 저자는 `Task()` 기능을 선호
- 모든 핵심 컨텍스트를 CLAUDE.md에 배치
- 메인 에이전트가 자체 복사본에 작업 위임 시점 결정
- **"Master-Clone" 아키텍처**로 동적 오케스트레이션

#### 4.4 데이터 분석 자동화

- Claude에 `bq` CLI로 실시간 메트릭 분석 요청
- 코드베이스에 BigQuery skill 체크인해 팀 전체가 활용
- SQL을 6개월 이상 직접 작성하지 않을 수 있는 수준

#### 4.5 자동화 대상 선별

```
하루 1회 이상 반복 작업 → Skill/Command로 변환
```

**예시**:
- `/techdebt`: 중복 코드 제거
- `/catchup`: git 변경 파일 읽기
- `/pr`: 코드 정리 및 PR 준비
- 7일치 Slack, GDrive, Asana, GitHub 동기화 커맨드

> **주의**: 복잡한 커스텀 명령 리스트는 안티패턴. 에이전트는 자연어로 충분히 유용합니다.

#### 4.6 버그 수정 위임

- Slack MCP 활성화 후 버그 스레드 붙여넣고 "fix" 명령
- "Go fix the failing CI tests" 같은 고수준 지시
- docker logs를 분산 시스템 트러블슈팅에 활용

### 5. 핵심 설계 원칙 요약

| 원칙 | 설명 |
|------|------|
| **운전석 유지** | Claude에게 완전한 자율권을 주지 않음, 최종 결정은 항상 사람 |
| **문서 중심 협업** | 마크다운 파일이 공유 상태로 작동, 명확한 의사소통 |
| **검증 필수** | 결과물을 직접 실행/테스트할 방법을 항상 제공 |
| **점진적 자율성** | Plan Mode → 검토 → Auto-accept 순서로 자율성 확대 |
| **누적 학습** | 실수를 규칙으로 전환하여 CLAUDE.md에 축적 |

## 된다 / 안 된다

### 된다

- settings.json을 Git에 체크인하면 팀 전체가 동일한 설정을 공유한다
- 3~5개 worktree를 동시 운영하면 독립적인 기능 개발을 병렬로 처리한다
- 커스텀 에이전트(.claude/agents/)를 만들면 반복 작업을 자동화한다
- PreToolUse Hook으로 위험한 명령(rm -rf, force push)을 사전 차단한다
- `/config`에서 출력 스타일을 learning으로 설정하면 학습 모드로 전환된다
- 환경 변수로 MCP 타임아웃, Bash 타임아웃을 조정하면 긴 작업도 안정적으로 처리된다

### 안 된다

- `/compact`에 의존하면 자동 압축 과정에서 중요한 컨텍스트가 손실될 수 있다
- `@`-파일을 과다하게 참조하면 매 실행마다 전체 파일이 컨텍스트에 임베딩되어 비대화된다
- CLAUDE.md에 부정형 제약("절대 금지")만 쓰면 실제 효과가 낮다
- Plan 단계 없이 바로 구현을 시작하면 잘못된 방향으로 대량의 코드가 생성될 위험이 있다
- 커스텀 명령을 과도하게 만들면 관리 부담이 증가하고 자연어 지시가 더 효과적인 경우가 많다

## 언제 사용하나?

### 개인 개발자

- 반복 작업이 하루 1회 이상 → Skill/Command로 자동화
- 복잡한 기능 구현 → 6단계 계획/실행 분리 워크플로우
- 새 프로젝트 시작 → CLAUDE.md 초기 설정 + settings.json 구성

### 팀 리드

- 팀 표준 설정 → settings.json Git 체크인 + 권한 정책 설정
- 코드 품질 강화 → PreToolUse Hook으로 커밋 전 테스트 강제
- 온보딩 가속 → 출력 스타일 "learning" + 커스텀 에이전트

### 데이터/분석 작업

- BigQuery skill 체크인으로 팀 전체 SQL 자동화
- analytics-engineer 스타일 에이전트로 dbt 작업 처리

## 주의사항 / 트레이드오프

| 항목 | 주의점 |
|------|--------|
| 병렬 worktree | Git 충돌 가능성 증가, merge 전략 필요 |
| Auto-accept | 검증 없이 사용하면 위험, Plan Mode로 충분히 검토 후 전환 |
| CLAUDE.md 크기 | 25KB 초과 시 컨텍스트 효율 저하, 핵심만 유지 |
| Hook 복잡도 | 과도한 Hook은 실행 속도 저하, 필수적인 것만 설정 |
| 환경 변수 | 84개 전부를 설정할 필요 없음, 필요한 것만 선별 |
| 샌드박스 | Windows 미지원, 보안이 중요한 작업에서는 대안 필요 |

## 면접 질문

### Q1. Claude Code의 CLAUDE.md를 효과적으로 관리하는 전략은?

**모범 답변**: CLAUDE.md는 "에이전트의 헌법"으로, 팀 전체가 공유하는 단일 문서를 Git에 체크인하여 관리합니다. 핵심 전략은 세 가지입니다. 첫째, **누적 학습** — Claude가 잘못된 행동을 할 때마다 해당 규칙을 추가하여 같은 실수를 방지합니다. 둘째, **적정 크기 유지** — 13KB~25KB 범위로 유지하며, 엔지니어 30% 이상이 사용하는 도구만 문서화합니다. 셋째, **긍정형 지시** — "절대 사용 금지"보다 "대신 이렇게 사용"이 더 효과적입니다.

### Q2. AI 에이전트에게 복잡한 작업을 위임할 때 품질을 보장하는 방법은?

**모범 답변**: **검증 피드백 루프**가 핵심입니다. Claude Code 창시자에 따르면 검증 방법을 제공하면 최종 결과물 품질이 2~3배 향상됩니다. 구체적으로 6단계 워크플로우(Research → Plan → Annotation → Todo → Implementation → Feedback)를 적용하며, "Don't implement yet" 명령으로 조기 실행을 방지합니다. 계획 단계에서 실제 코드 스니펫, 수정 파일 경로, 트레이드오프를 포함한 상세 계획을 수립한 후에만 구현을 시작합니다.

### Q3. AI 코딩 도구의 병렬 세션을 효율적으로 관리하는 방법은?

**모범 답변**: Git worktree를 활용하여 3~5개의 독립 작업 공간을 만들고, 각각에 Claude 세션을 실행합니다. shell alias(za, zb, zc)로 빠르게 전환하며, tmux로 작업별 색상 코드 탭을 유지합니다. 핵심은 각 worktree가 **완전히 독립적인 브랜치**에서 작업하므로 충돌 없이 기능 개발, 버그 수정, 리팩토링을 동시에 진행할 수 있다는 점입니다. 로그 분석용 전용 worktree를 별도로 운영하는 것도 효과적입니다.

## 참고 자료

### GeekNews 소스 (6개)

- [Claude Code 창시자의 커스터마이징 팁 12가지](https://news.hada.io/topic?id=26649)
- [Claude Code 완전 가이드: 70가지 파워 팁](https://news.hada.io/topic?id=26526)
- [Claude Code 창시자 Boris Cherny의 사용법](https://news.hada.io/topic?id=25570)
- [Claude Code 활용: 계획과 실행의 분리](https://news.hada.io/topic?id=26907)
- [Claude Code의 모든 기능 활용법](https://news.hada.io/topic?id=24099)
- [Claude Code 창시자 실전 사용 팁](https://news.hada.io/topic?id=26330)

### 관련 문서

- [Claude Code Workflow](./claude-code-workflow.md) - 병렬 세션, Plan 모드, 최적화 전략
- [Claude Code 실전 가이드](./claude-code-guide.md) - 70가지 팁 핵심 정리
- [Claude Code 설정 체계](./claude-code-settings.md) - 다계층 설정 구조
- [Claude Code Hook](./claude-code-hook.md) - 생명주기 자동 실행 셸 명령어
- [Claude Code Skill](./claude-code-skill.md) - 기능 모듈화, 능력 패키징
- [Claude Code Sub Agent](./claude-code-sub-agent.md) - 독립적 작업 위임, 병렬 실행
