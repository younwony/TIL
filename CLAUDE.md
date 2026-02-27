# CLAUDE.md

이 문서는 Claude Code가 TIL 저장소에서 작업할 때 참고하는 설정 및 규칙입니다.

## 저장소 개요

- **저장소 유형:** TIL (Today I Learned) - 학습 내용 정리
- **주요 언어:** Markdown, Java, Kotlin
- **구조:** CS 지식(마크다운) + Study(프로젝트 단위)

## Git 설정

### 기본 규칙

- **모든 작업에서 git add까지만 진행** (commit은 사용자 요청 시에만)
- 새로 생성/수정/삭제된 파일 모두 `git add`만 수행
- 커밋 메시지는 사용자가 명시적으로 요청할 때만 작성

### PR 설정

PR_BASE_BRANCH: main-review

### 커밋 메시지 형식

```
<type>: <subject>

<body> (선택)
```

| Type | 설명 | 예시 |
|------|------|------|
| `docs` | 문서 추가/수정 | `docs: Effective Java Item 1 추가` |
| `feat` | 새로운 기능/내용 | `feat: 알고리즘 문제 풀이 추가` |
| `fix` | 오류 수정 | `fix: 오타 수정` |
| `refactor` | 구조 변경 | `refactor: 폴더 구조 정리` |
| `chore` | 기타 작업 | `chore: gitignore 수정` |

## 폴더 구조 규칙

```
TIL/
├── README.md              # 저장소 소개
├── CONVENTION.md          # 컨벤션 가이드
├── CLAUDE.md              # Claude 설정 (이 문서)
├── .claude/
│   └── skills/            # Claude Code Skills
├── cs/                    # CS 지식 (마크다운 중심)
│   └── {category}/        # kebab-case
├── cs-web/                # CS 문서 웹 뷰어 (Spring Boot)
└── study/                 # 스터디 (프로젝트 단위)
    └── {study-name}/      # kebab-case
```

## 네이밍 컨벤션

### 폴더명

| 규칙 | 올바른 예 | 잘못된 예 |
|------|----------|----------|
| kebab-case 사용 | `effective-java` | `effectiveJava`, `Effective_Java` |
| 영문 사용 | `clean-coder` | `클린코더` |
| 단수형 사용 | `algorithm` | `algorithms` |

### 파일명

| 타입 | 규칙 | 예시 |
|------|------|------|
| 마크다운 | kebab-case | `item01.md`, `part01-professionalism.md` |
| Java 클래스 | PascalCase | `NutritionFacts.java` |
| 알고리즘 | `a{번호}_{문제명}.java` | `a1000_더하기.java` |

### 챕터/번호 형식

| 타입 | 형식 | 예시 |
|------|------|------|
| 챕터 | `chapter{00}` | `chapter02` |
| 주차 | `week{00}` | `week04` |
| 파트 | `part{00}` | `part01` |
| 아이템 | `item{00}` | `item01` |

## 마크다운 작성 규칙

### README.md 필수 요소

```markdown
# {Study Name} Study

{한 줄 설명}

> 원본 레포지토리: {URL} (있는 경우)

## 목차

### {Chapter/Part 제목}
- [{아이템 제목}]({상대경로})
```

### 개별 문서 필수 요소

```markdown
# {제목}

## 핵심 정리

{핵심 내용 요약}

## 본문

{상세 내용}

## 예제 코드 (있는 경우)

```java
// 코드
```
```

### 스타일 규칙

- H1은 파일당 1개만 사용
- 코드 블록에 언어 명시 (```java, ```bash 등)
- 링크는 상대 경로 사용
- 리스트는 하이픈(`-`) 사용

## 코드 작성 규칙

### Java

| 요소 | 규칙 |
|------|------|
| 클래스명 | PascalCase |
| 메서드/변수명 | camelCase |
| 상수명 | UPPER_SNAKE_CASE |
| 패키지명 | 소문자 |

### 일반 원칙

- 매직 넘버/문자열 사용 금지 → 상수로 정의
- 단일 책임 원칙(SRP) 준수
- 중복 코드 제거 (DRY 원칙)
- 명확하고 의미 있는 네이밍

## 작업 체크리스트

### 새 스터디 추가 시

- [ ] 폴더명: kebab-case 영문
- [ ] README.md 템플릿 준수
- [ ] 상위 README.md에 링크 추가
- [ ] 원본 레포지토리 링크 포함 (있는 경우)

### 새 문서 추가 시

- [ ] 파일명 규칙 준수
- [ ] H1 제목 포함
- [ ] 코드 블록에 언어 명시
- [ ] 상대 경로 링크 사용

### 작업 완료 시

- [ ] `git add .` 실행
- [ ] `git status`로 staging 확인
- [ ] commit은 사용자 요청 시에만

## Claude Code Skills & Commands

사용 가능한 스킬 및 커맨드 목록입니다.

### Skills (자동 트리거)

| 스킬 | 트리거 키워드 | 설명 |
|------|--------------|------|
| `cs-guide-writer` | "오늘의 CS", "CS 정리", "{주제} 정리해줘" | CS 학습 문서 작성 |
| `cs-sync` | "CS 동기화", "가이드 반영" | CS 문서 템플릿 동기화 |
| `cs-link-sync` | "링크 동기화", "깨진 링크 수정", "링크 체크" | CS 문서 링크 검증 및 수정 |
| `mermaid-diagram` | - | Mermaid CLI 다이어그램 생성 |
| `svg-diagram` | - | SVG 코드 다이어그램 생성 |
| `3ai-plan` | "3AI 플랜", "3AI 협업" | 3-AI(Claude+Gemini+Codex) 협업 플랜 생성 |
| `pencil-screen` | "새 화면 디자인", "스크린 추가", "화면 만들어줘" | Pencil MCP로 새 화면(Desktop+Mobile) 디자인 |
| `pencil-update` | "디자인 수정", "화면 변경", "레이아웃 조정" | Pencil MCP 기존 화면 수정 |
| `pencil-to-code` | "코드 생성", "디자인 적용", "코드로 변환" | Pencil 디자인을 HTML/CSS/JS 코드로 변환 |

### Commands (슬래시 명령)

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
| `work-plan` | `/work-plan [path]` | req.md 기반 WORK-SPEC.md 작업 명세서 생성 |
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
| `debug-chrome` | `/debug-chrome` | 현재 브랜치 변경사항 분석 후 Claude in Chrome 디버깅 시작 |
| `team-review` | `/team-review` | 4명의 전문 리뷰어 에이전트 팀으로 병렬 코드 리뷰 |

### Agents (Sub-agents)

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

> 가이드: [AGENT-GUIDE.md](./AGENT-GUIDE.md)

### 팀 에이전트 워크플로우

`/work-plan-start` 실행 시 아래 Phase별로 에이전트를 병렬 디스패치하여 작업한다.

#### Phase 구성

```
Phase 1: 탐색 + 설계 (병렬, 결과 대기)
├─ [Explore]  코드베이스 구조 파악, 영향 범위 분석
└─ [Plan]     구현 전략 설계, 파일별 변경 계획

Phase 2: 구현 + 테스트 (병렬)
├─ [Main]            핵심 코드 수정 (Phase 1 결과 기반)
└─ [test-generator]  테스트 자동 생성 (background, Phase 1 결과 기반)

Phase 3: 검증 + 문서화 (병렬)
├─ [code-refactor]   코드 품질 리뷰 (background)
└─ [Main]            ARCHITECTURE.md, SPEC.md 작성
```

#### 디스패치 규칙

- **Phase 1**은 foreground 실행 (결과가 Phase 2의 입력)
- **Phase 2**의 test-generator는 background 실행 (Main과 병렬)
- **Phase 3**의 code-refactor는 background 실행
- Phase 간 의존성이 있으므로 Phase 순서는 반드시 순차 실행
- 각 Phase 내 에이전트는 최대한 병렬 실행
- 에이전트 실패 시 Main이 해당 작업을 직접 수행

#### 에이전트별 입력

| Phase | 에이전트 | 입력 |
|-------|----------|------|
| 1 | Explore | WORK-SPEC.md의 변경 대상 파일 목록 + 키워드 |
| 1 | Plan | WORK-SPEC.md 전체 내용 |
| 2 | Main | Phase 1 Explore/Plan 결과 |
| 2 | test-generator | Phase 1 Plan 결과 + 변경된 소스 파일 경로 |
| 3 | code-refactor | Phase 2에서 변경된 파일 목록 |
| 3 | Main | Phase 2 완료된 코드 기반 |

#### 적용 조건

- WORK-SPEC.md가 존재할 때만 팀 워크플로우 적용
- 단순 작업 (파일 1~2개 수정)은 Main 단독 처리
- 에이전트 디스패치 여부는 작업 복잡도에 따라 자체 판단

## AI 코딩 보안

AI 코딩 도구 사용 시 팀 전체가 지켜야 할 보안 원칙입니다.

### 신뢰할 수 없는 소스 주의
- `git clone` 전 저장소의 CLAUDE.md, .cursorrules, AGENTS.md 등 컨텍스트 파일 확인
- 알 수 없는 출처의 MCP 서버 연결 금지
- 외부 PR/이슈의 코드를 그대로 실행하지 않음

### AI 생성 코드 보안 리뷰
- AI 생성 코드도 반드시 OWASP Top 10 관점 보안 리뷰
- 특히 외부 입력 처리, 인증/인가, 파일 I/O, SQL 쿼리 주의
- API 키, 시크릿, 자격 증명이 코드에 포함되지 않았는지 확인

### 권한 최소화
- AI에게 필요 최소한의 파일/디렉토리 접근만 허용
- 프로덕션 환경 접근 금지
- `.env`, `credentials.json` 등 민감 파일 커밋 방지

## 언어 설정

- **응답 언어:** 모든 대답과 설명은 한국어로 진행합니다.
- **문서 작성:** 한글로 작성하며, 기술 용어는 영문 표기와 병행합니다.
- **코드 설명:** 코드 내 주석과 설명도 한글로 작성합니다.

## 참고 문서

- [CONVENTION.md](./CONVENTION.md): 상세 컨벤션 가이드
- [cs/CS-GUIDE.md](./cs/CS-GUIDE.md): CS 문서 작성 가이드
- [study/README.md](./study/README.md): 스터디 목록
- [.claude/skills/](./.claude/skills/): Claude Code Skills (특정 워크플로우)
