# TIL Claude Code Skills / Commands / Agents 카탈로그

> 이 문서는 `CLAUDE.md`에서 분리한 카탈로그 참조 자료입니다.
> 매 세션마다 컨텍스트에 올릴 필요는 없으며, Skill/Command/Agent 호출은 시스템 프롬프트의 available skills 목록과 Agent 도구 description으로 이미 노출됩니다.
> 어떤 항목이 있는지 한눈에 보고 싶을 때만 Read로 열어 확인하세요.

## 디렉토리 구조 (ADR 0003)

skills/는 Matt Pocock 패턴의 6-bucket 구조:

```
.claude/skills/
├── engineering/    ← 일일 코드 작업 (공개)
├── productivity/   ← 일일 워크플로우 (공개)
├── misc/           ← 가끔 사용 (공개)
├── personal/       ← TIL 고유 / 회사 특화 (노출 X)
├── in-progress/    ← 드래프트 (노출 X)
└── deprecated/     ← 폐기 (노출 X)
```

상세: [`.claude/skills/README.md`](../skills/README.md)

## Atomic Skills (Matt Pocock 패턴, 직접 호출 가능)

| 스킬 | 호출 | 설명 |
|------|------|------|
| `grill-with-docs` | `/grill-with-docs` | 계획을 도메인 모델에 대조 + CONTEXT.md/ADR 인라인 갱신 |
| `triage` | `/triage` | 이슈 5-state machine + AGENT-BRIEF로 AFK 준비 |
| `improve-codebase-architecture` | "아키텍처 개선" | deletion test + shallow→deep module 리팩터링 제안 |
| `to-prd` | `/to-prd` | 대화 컨텍스트 → PRD/WORK-SPEC 양식 |
| `to-issues` | `/to-issues` | vertical slice (tracer bullet) 이슈 분해 |
| `prototype` | `/prototype` | 일회성 prototype (terminal app 또는 UI 변형) |

> `/work-plan` 과 `/work-plan-start` 는 위 atomic skill의 wrapper다 (ADR 0002 참조). Track에 들어가지 않는 즉흥 작업이면 atomic skill을 직접 호출하라.

## Skills (자동 트리거)

| 스킬 | 트리거 키워드 | 설명 |
|------|--------------|------|
| `cs-guide-writer` | "오늘의 CS", "CS 정리", "{주제} 정리해줘" | CS 학습 문서 작성 |
| `cs-sync` | "CS 동기화", "가이드 반영" | CS 문서 템플릿 동기화 |
| `cs-link-sync` | "링크 동기화", "깨진 링크 수정", "링크 체크" | CS 문서 링크 검증 및 수정 |
| `svg-diagram` | - | SVG 코드 다이어그램 생성 (기본, 우선 사용) |
| `mermaid-diagram` | - | Mermaid CLI 다이어그램 생성 (빠른 프로토타이핑용) |
| `3ai-plan` | "3AI 플랜", "3AI 협업" | 3-AI(Claude+Gemini+Codex) 협업 플랜 생성 |
| `pencil-screen` | "새 화면 디자인", "스크린 추가", "화면 만들어줘" | Pencil MCP로 새 화면(Desktop+Mobile) 디자인 |
| `pencil-update` | "디자인 수정", "화면 변경", "레이아웃 조정" | Pencil MCP 기존 화면 수정 |
| `pencil-to-code` | "코드 생성", "디자인 적용", "코드로 변환" | Pencil 디자인을 HTML/CSS/JS 코드로 변환 |
| `smart-session` | "스마트 세션", "세션 시작", "세션 정리" | 치트시트 기반 통합 워크플로우 (컨텍스트 진단→WAT 실행→상태 보존) |
| `test-coverage-check` | "테스트 커버리지", "커버리지 체크", "테스트 점검" | 변경 파일 커버리지 분석 + 누락 테스트 자동 생성 |
| `browser-debug` | "브라우저 QA", "웹 QA", "크롬 디버깅", "Playwright QA" | 2-Layer QA: Playwright 1차 검증 → FAIL건만 Chrome 정밀 디버깅 |
| `browser-debug-chrome` | `/browser-debug-chrome`, "Chrome-only QA" | Chrome MCP만 사용하는 레거시 QA (백업) |
| `qa-scenario` | "QA 시나리오", "테스트 시나리오", "변경 영향 분석", "QA 문서" | 변경사항 분석 → 영향도 매트릭스 → BDD QA 시나리오 문서 생성 |
| `feature-check` | "기능 체크", "기능 검증", "구현 확인", "체크리스트 검증" | FEATURE-CHECKLIST.md 기반 코드 레벨 기능 구현 자동 검증 |
| `skill-rebuild` | "스킬 재구성", "스킬 개선", "스킬 리빌드" | 기존 스킬 개선 시 전체 파이프라인(evals, 벤치마크, description 최적화) 강제 |
| `ai-slop-detect` | "AI slop", "코드 과잉", "오버엔지니어링 체크", "불필요한 코드" | AI 생성 코드 품질 감지 (과잉 추상화, YAGNI, 미사용 코드) |
| `weekly-retro` | "주간 회고", "이번 주 뭐했지", "작업 돌아보기" | git 기반 주간 회고 자동 생성 (학습/패턴/개선점) |
| `security-audit` | "보안 감사", "STRIDE 분석", "위협 모델링", "취약점 분석" | STRIDE + OWASP Top 10 통합 보안 감사 |
| `product-review` | "제품 검증", "이거 필요해?", "기능 검증", "오피스아워" | 기능 구현 전 제품 검증 (6질문 + 4확장모드) |
| `sync-global` | "설정 동기화", "글로벌 싱크", "스킬 동기화" | 글로벌 Claude 설정을 프로젝트와 동기화 (push/pull/status) |

## Commands (슬래시 명령)

| 커맨드 | 호출 | 설명 |
|--------|------|------|
| `today` | `/today` | 오늘 작성/수정한 문서 목록 확인 |
| `handoff` | `/handoff` | 작업 상태를 HANDOFF.md로 저장 |
| `pr` | `/pr` | 현재 브랜치 분석 후 PR 자동 생성 |
| `review-pr` | `/review-pr {PR번호}` | PR 코드 리뷰 및 개선 제안 |
| `self-review` | `/self-review` | PR 생성 전 자체 리뷰 및 SELF-REVIEW.md 생성 |
| `api-doc` | `/api-doc {대상}` | API 문서 생성 |
| `work-log` | `/work-log`, `/work-log --parent <pageId\|제목>` | 현재 브랜치 작업 내용 Confluence 문서화 (기본: WORK-LOG 하위) |
| `work-share` | `/work-share` | 현재 브랜치 작업 내용 공유 페이지 하위에 Confluence 문서화 |
| `work-plan` | `/work-plan [path]` | req.md 기반 WORK-SPEC.md 작업 명세서 + FEATURE-CHECKLIST.md 생성 |
| `work-plan-start` | `/work-plan-start [path]` | WORK-SPEC.md 기반 실제 작업 수행 |
| `release-notes-kr` | `/release-notes-kr [버전/범위]` | Claude Code 릴리스 노트 한글 요약 정리 |
| `slack-to-jira` | `/slack-to-jira {Slack URL}` | Slack 스레드 읽어 Jira 이슈 자동 생성 |
| `jira-report` | `/jira-report [#채널]` | 현재 스프린트 현황을 Slack 채널에 공유 |
| `slack-to-confluence` | `/slack-to-confluence {Slack URL}` | Slack 스레드를 Confluence 페이지로 변환 |
| `meeting-notes` | `/meeting-notes {Slack URL}` | Slack 스레드를 회의록으로 변환 (Confluence 저장) |
| `slack-digest` | `/slack-digest [#채널]` | 특정 채널의 최근 대화 요약 |
| `standup-summary` | `/standup-summary [#채널]` | 스탠드업 채널 일일 요약 |
| `slack-remind` | `/slack-remind [대상] [시간] [메시지]` | 특정 시간에 Slack 메시지 예약 발송 |
| `sprint-start-notify` | `/sprint-start-notify [#채널]` | 스프린트 시작 시 팀 채널에 할당 이슈 공유 |
| `jira-notify` | `/jira-notify {이슈키} [#채널]` | Jira 이슈 상태를 Slack 채널에 알림 |
| `browser-debug` | `/browser-debug` | Playwright + Chrome 2-Layer 브라우저 QA |
| `browser-debug-chrome` | `/browser-debug-chrome` | Chrome MCP만 사용하는 레거시 QA (백업) |
| `figma-read` | `/figma-read {Figma URL}` | Figma URL 디자인을 figma-team MCP로 읽기 |
| `qa-scenario` | `/qa-scenario` | 변경사항 분석 → 영향도 매트릭스 → BDD QA 시나리오 문서 생성 |
| `team-review` | `/team-review` | 4명의 전문 리뷰어 에이전트 팀으로 병렬 코드 리뷰 |
| `feature-check` | `/feature-check` | FEATURE-CHECKLIST.md 기반 코드 레벨 기능 구현 자동 검증 |
| `skill-rebuild` | `/skill-rebuild {스킬명}` | 기존 스킬 재구성 (전체 파이프라인 강제) |
| `ai-slop-detect` | `/ai-slop-detect` | AI 생성 코드 품질 감지 (과잉 추상화, YAGNI 위반) |
| `weekly-retro` | `/weekly-retro` | git 기반 주간 회고 자동 생성 |
| `security-audit` | `/security-audit` | STRIDE + OWASP 통합 보안 감사 |
| `product-review` | `/product-review` | 기능 구현 전 제품 검증 (6질문 + 4확장모드) |
| `track-status` | `/track-status [track_id]` | Track 작업 추적 현황 조회 |
| `sync-global` | `/sync-global push\|pull\|status` | 글로벌 Claude 설정을 프로젝트와 동기화 |
| `grill-with-docs` | `/grill-with-docs` | 도메인 모델 대조 grilling + CONTEXT.md/ADR 인라인 갱신 |
| `triage` | `/triage [#이슈]` | 이슈 5-state machine 분류 + AGENT-BRIEF 작성 |
| `improve-codebase-architecture` | `/improve-codebase-architecture [경로]` | shallow→deep module 리팩터링 기회 탐색 |
| `to-prd` | `/to-prd` | 현재 대화 → PRD/WORK-SPEC 양식 합성 |
| `to-issues` | `/to-issues [#이슈\|path]` | 계획/PRD → vertical slice (tracer bullet) 이슈 분해 |
| `prototype` | `/prototype logic\|ui "주제"` | 설계 검증용 throwaway 프로토타입 |

## Agents (Sub-agents)

독립 컨텍스트에서 자율적으로 작업을 수행하는 전문 에이전트입니다.
Claude가 작업 유형에 따라 자동으로 위임하거나, 명시적으로 요청할 수 있습니다.

| 에이전트 | 트리거 키워드 | 모델 | 설명 |
|----------|--------------|------|------|
| `test-generator` | "테스트 생성", "테스트 작성" | Sonnet | 단위/통합 테스트 자동 생성 + 실행 + 수정 |
| `code-refactor` | "리팩토링", "코드 스멜" | Sonnet | CLAUDE.md 규칙 기반 코드 분석 + 개선 |
| `debugger` | "디버깅", "에러 분석" | Sonnet | 스택 트레이스 추적 + 원인 분석 + 수정 |
| `jira-updater` | "Jira 업데이트", "이슈 상태" | Haiku | 브랜치 기반 이슈 감지 + 상태 전환 + 코멘트 |
| `review-performance` | "성능 리뷰" | Sonnet | N+1 쿼리, 고비용 객체, 컬렉션 최적화, I/O 병목 분석 |
| `review-security` | "보안 리뷰" | Sonnet | OWASP Top 10, 인증/인가, 민감정보 노출, 입력 검증 |
| `review-test-coverage` | "테스트 리뷰" | Sonnet | 테스트 존재 여부, 커버리지, 누락 시나리오, 테스트 품질 |
| `review-convention` | "컨벤션 리뷰" | Sonnet | CLAUDE.md 규칙, 클린 코드, SOLID, 네이밍, 가독성 |
| `cs-diagram-generator` | "다이어그램 생성" | Sonnet | CS 문서용 SVG/Mermaid 다이어그램 생성 |
| `cs-index-manager` | "인덱스 업데이트" | Haiku | CS 문서 README.md 인덱스 업데이트 |

> 가이드: [AGENT-GUIDE.md](../../AGENT-GUIDE.md)

## 관련 동작 규칙

카탈로그가 아닌 **언제 어떻게 디스패치하는가**의 규칙은 `CLAUDE.md`에 잔류합니다:

- "일반 요청 병렬 에이전트 디스패치" — 변경 파일 수에 따른 디스패치 전략
- "에이전트 에러 처리 (Withhold-then-Recover)" — 실패 시 자동 복구 절차
- "팀 에이전트 워크플로우" — `/work-plan-start`의 Solo/Standard/Coordinator 모드


## Matt Pocock 4가지 실패 모드 매핑

| # | 실패 모드 | 대응 자산 |
|---|----------|----------|
| 1 | Alignment | `product-review`, `grill-with-docs`, `to-prd` |
| 2 | Verbosity | `.claude/CONTEXT.md`, `zoom-out`, `caveman` |
| 3 | Doesn't Work | `test-coverage-check`, `debugger` (5-phase), `feature-check`, `ai-slop-detect` |
| 4 | Ball of Mud | `improve-codebase-architecture`, `zoom-out`, `code-refactor` |

## ADR 관련

- [ADR 0001](./adr/0001-skill-dependency-classification.md) — Skill Hard/Soft Dependency 분류
- [ADR 0002](./adr/0002-track-as-skill-container.md) — Track as Skill Container
- [ADR 0003](./adr/0003-bucket-structure.md) — Skills 6-Bucket Structure

## docs/agents/ (atomic skill 입력 설정)

- [issue-tracker.md](./agents/issue-tracker.md) — Jira API 호출 패턴
- [triage-labels.md](./agents/triage-labels.md) — canonical role → Jira 라벨 매핑
- [domain.md](./agents/domain.md) — 도메인 문서 위치 + skill 읽기 규칙
