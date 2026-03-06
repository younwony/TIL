# Claude Code + AI Agent 실무 활용 사례 공유

> 팀 발표용 프레젠테이션 원고 - Gamma 또는 Manus에 붙여넣어 슬라이드를 생성하세요

---

## Gamma용 프롬프트

아래 텍스트를 Gamma의 "Paste in" 모드에 붙여넣으세요.

---

## 슬라이드 원고

### 슬라이드 1: 표지

**Claude Code + AI Agent로 일하는 법**

부제: 실무에서 검증한 AI 협업 워크플로우 사례 공유

발표자: 윤원희
날짜: 2026년 3월

---

### 슬라이드 2: 발표 목적

**왜 이 발표를 하는가?**

- AI 코딩 도구를 "채팅용"이 아닌 "업무 파이프라인"으로 사용하는 방법을 공유
- 직접 구축하고 실무에서 사용 중인 워크플로우 사례 소개
- 팀원들이 바로 따라 할 수 있는 실전 가이드 제공

핵심 메시지: AI 에이전트는 "질문-답변" 도구가 아니라 "작업을 위임하는 동료"다

---

### 슬라이드 3: 전체 워크플로우 개요

**내가 구축한 AI 워크플로우 전체 그림**

```
작업 요청 → /work-plan (작업 명세서 생성)
         → /work-plan-start (에이전트 팀이 병렬 구현)
         → /self-review (4명 리뷰어 + Gemini/Codex 크로스 리뷰)
         → /pr (PR 자동 생성)
         → /work-log (Confluence 작업 문서 자동화)
```

하나의 명령어로 설계 → 구현 → 리뷰 → 문서화까지 자동화

---

### 슬라이드 4: 사례 1 - 작업 계획 자동화 (/work-plan)

**요구사항 문서 하나로 작업 명세서 자동 생성**

사용법:
- req.md에 자연어로 요구사항 작성
- `/work-plan` 한 줄 실행
- Claude가 프로젝트 구조를 분석하여 WORK-SPEC.md 자동 생성

자동으로 하는 것들:
- 프로젝트 유형 자동 판별 (Spring Boot, Batch, 프론트엔드 등)
- 파일 변경 목록, API 설계, 데이터 모델, 테스트 전략 포함
- Gemini CLI + Codex CLI로 크로스 체크 (누락/오버엔지니어링 검증)

효과: 요구사항만 작성하면 5분 내에 작업 명세서(WORK-SPEC.md) 완성

---

### 슬라이드 5: 사례 2 - 에이전트 팀 병렬 구현 (/work-plan-start)

**4명의 AI 에이전트가 팀으로 일한다**

Phase 1 (탐색 + 설계) - 병렬 실행:
- Explore 에이전트: 코드베이스 구조 파악, 영향 범위 분석
- Plan 에이전트: 구현 전략 설계, 파일별 변경 계획

Phase 2 (구현 + 테스트) - 병렬 실행:
- Main: 핵심 코드 수정
- test-generator: 단위/통합 테스트 자동 생성

Phase 3 (검증 + 문서화) - 병렬 실행:
- code-refactor: 코드 품질 리뷰
- Main: ARCHITECTURE.md, SPEC.md 작성

핵심: 탐색/설계/구현/테스트/검증을 순차가 아닌 병렬로 실행하여 대기 시간 최소화

---

### 슬라이드 6: 사례 3 - AI 팀 코드 리뷰의 핵심, "팀 에이전트"

**사람 리뷰어처럼 전문 관점별 AI 에이전트가 동시에 리뷰한다**

팀 에이전트란?
- 각각 독립된 컨텍스트를 가진 전문 AI 에이전트 4명
- 하나의 Claude가 모든 관점을 보는 게 아니라, 전문 에이전트가 각자 맡은 관점만 깊이 분석
- 병렬 실행으로 4개 리뷰가 동시에 진행

리뷰 팀 구성:
| 리뷰어 | 관점 | 체크 항목 예시 |
|--------|------|---------------|
| 성능 전문가 | 성능 병목 | N+1 쿼리, 고비용 객체 캐싱 누락, 반복문 내 DB 호출 |
| 보안 전문가 | 보안 취약점 | OWASP Top 10, SQL Injection, 민감정보 노출 |
| 테스트 전문가 | 테스트 품질 | 커버리지 누락, 엣지케이스 미검증, 테스트 격리 |
| 컨벤션 전문가 | 코드 품질 | CLAUDE.md 규칙 위반, 네이밍, SOLID 원칙 |

---

### 슬라이드 7: 리뷰 명령어 3종 비교

**같은 팀 에이전트, 다른 사용 시점과 대상**

| 구분 | /self-review | /team-review | /review-pr {번호} |
|------|-------------|-------------|-------------------|
| 시점 | PR 생성 전 | PR 생성 후 | PR 생성 후 |
| 리뷰 대상 | 내 브랜치 변경사항 | 내 브랜치 변경사항 | **다른 사람의 PR** |
| 누가 쓰나 | 본인 (셀프 점검) | 본인 (팀 리뷰 보조) | **리뷰어** (남의 PR 리뷰) |
| 추가 리뷰어 | + Gemini + Codex | 4명 에이전트만 | + Gemini + Codex |
| 결과물 | SELF-REVIEW.md | TEAM-REVIEW.md | PR 코멘트로 직접 작성 |
| 목적 | "내가 놓친 게 없나?" | "리뷰어 참고 자료" | "리뷰어의 리뷰를 보조" |

실제 워크플로우:
```
[코드 작성자]
  코드 작성 완료
    → /self-review (혼자 사전 검증, 6개 관점)
    → 지적사항 수정
    → /pr (PR 생성)
    → /team-review (팀원 리뷰 시 참고 자료 제공)

[코드 리뷰어]
  PR 리뷰 요청 받음
    → /review-pr 123 (AI가 먼저 분석)
    → AI 리뷰 결과 참고하여 사람이 최종 판단
```

핵심: 작성자도 리뷰어도 각자의 시점에서 AI 팀 에이전트를 활용

---

### 슬라이드 8: 사례 4 - 디자인 자동화 (Pencil MCP)

**코드 한 줄 없이 화면 디자인 생성**

Pencil MCP란?
- .pen 파일 기반 디자인 도구를 Claude Code에서 직접 제어
- 자연어로 "대시보드 화면 만들어줘"라고 요청하면 디자인 생성

워크플로우:
1. "새 화면 디자인해줘" → pencil-screen 스킬 자동 트리거
2. Desktop(1440x900) + Mobile(390x844) 쌍으로 자동 생성
3. 스타일 가이드 자동 적용, 컴포넌트 재사용
4. "코드로 변환해줘" → pencil-to-code로 HTML/CSS/JS 생성

효과: 디자이너 없이도 프로토타입 화면을 즉시 생성하고 코드까지 변환

---

### 슬라이드 9: 사례 5 - 프론트엔드 디자인 생성 (/frontend-design)

**프롬프트 한 줄로 프로덕션급 UI 생성**

/frontend-design 스킬:
- 웹 컴포넌트, 페이지, 애플리케이션을 코드로 직접 생성
- 일반적인 AI 생성 디자인의 "AI 느낌"을 벗어난 창의적이고 세련된 결과물
- HTML/CSS/JS 코드로 바로 실행 가능

활용 예시:
- 랜딩 페이지 프로토타입
- 관리자 대시보드 UI
- 모바일 반응형 컴포넌트

Pencil과의 차이:
- Pencil: 디자인 파일(.pen) 중심 → 코드 변환
- /frontend-design: 처음부터 코드로 직접 생성

---

### 슬라이드 10: 사례 6 - 작업 로그 자동화 (/work-log)

**코드 변경사항을 Confluence 문서로 자동 변환**

/work-log 실행 시 자동으로 하는 것들:
1. git diff 분석 → 변경 내용 파악
2. 코드를 읽고 "무엇을, 왜, 어떻게" 분석
3. 비개발자도 이해할 수 있는 문서 자동 생성
4. Confluence 개인 스페이스에 자동 업로드

문서에 포함되는 내용:
- 한눈에 보기 (이슈번호, 작업일시, 변경 파일 수)
- 배경 및 목적 (비개발자 친화적 설명)
- 처리 흐름 다이어그램 (ASCII 박스 다이어그램)
- 시스템 아키텍처 (레이어별 역할)
- 주요 파일 변경 내역
- 코드 하이라이트

효과: `/work-log` 한 줄로 Confluence 문서가 자동 생성됨

---

### 슬라이드 11: 사례 7 - Slack/Jira/Confluence 연동

**커뮤니케이션과 프로젝트 관리도 자동화**

| 명령어 | 기능 |
|--------|------|
| /slack-to-jira | Slack 스레드를 읽어 Jira 이슈 자동 생성 |
| /jira-report | 현재 스프린트 현황을 Slack 채널에 공유 |
| /meeting-notes | Slack 스레드를 회의록으로 변환 → Confluence 저장 |
| /slack-digest | 특정 채널의 최근 대화 요약 |
| /standup-summary | 스탠드업 채널 일일 요약 |
| /work-share | 작업 내용을 공유 페이지에 Confluence 문서화 |

핵심: 도구 간 컨텍스트 전환 없이 Claude Code에서 모든 것을 처리

---

### 슬라이드 12: 이 모든 것의 핵심 - CLAUDE.md

**AI에게 "우리 팀의 규칙"을 가르치는 설정 파일**

CLAUDE.md에 정의한 것들:
- Git 컨벤션 (커밋 메시지, 브랜치 규칙)
- 코딩 규칙 (Java: SOLID, @Data 금지, LAZY 필수 등)
- 성능 위험 지역 (Pattern 캐싱, N+1 방지 등)
- 보안 원칙 (OWASP Top 10 리뷰 필수)
- Skills/Commands/Agents 정의

효과:
- AI가 생성하는 모든 코드가 팀 컨벤션을 준수
- 새 팀원 온보딩 시 CLAUDE.md만 공유하면 AI가 알아서 규칙 적용
- 코드 리뷰에서 컨벤션 위반이 사전 차단됨

---

### 슬라이드 13: 3-AI 협업 구조

**Claude + Gemini + Codex, 세 AI가 서로 검증한다**

구조:
- Claude Code: 메인 에이전트 (분석, 설계, 구현, 최종 정리)
- Gemini CLI: 크로스 체크 (작업 계획 검증, 코드 리뷰)
- Codex CLI: 크로스 체크 (다른 관점의 검증)

적용 포인트:
- /work-plan: 작업 명세서를 Gemini + Codex가 크로스 체크
- /self-review: 코드 리뷰를 4명 에이전트 + Gemini + Codex가 병렬 수행
- /3ai-plan: 3개 AI의 의견을 종합하여 최적 플랜 도출

왜 3개 AI인가?
- 단일 AI의 편향(bias) 방지
- 각 모델의 강점이 다름 (Claude: 구조화, Gemini: 최신 정보, Codex: 코드 품질)
- 실수를 서로 잡아주는 안전망 역할

---

### 슬라이드 14: 실제 생산성 변화 (Before vs After)

**도입 전후 시간 비교**

| 작업 | Before (수동) | After (AI 자동화) |
|------|-------------|-----------------|
| 작업 명세서 작성 | 요구사항 분석 + 설계 문서 직접 작성 | `/work-plan` → 5분 내 WORK-SPEC.md 생성 |
| 코드 리뷰 | 리뷰어 배정 → 대기 → 피드백 | `/self-review` → 4명 에이전트가 즉시 병렬 리뷰 |
| 작업 문서화 | Confluence에 수동 작성 | `/work-log` → git diff 기반 자동 문서 생성 |
| 프로토타입 UI | 디자인 도구에서 직접 제작 | Pencil or `/frontend-design` → 프롬프트로 생성 |
| PR 생성 | 변경사항 정리 + 설명 작성 | `/pr` → 브랜치 분석 후 자동 생성 |

주의: AI가 100% 완벽한 결과를 주는 것은 아님 → 사람의 검토/수정은 필수

---

### 슬라이드 15: 시작하는 방법

**오늘부터 바로 시작할 수 있는 3단계**

Step 1: Claude Code 설치
- npm install -g @anthropic-ai/claude-code
- 터미널에서 claude 실행

Step 2: CLAUDE.md 작성
- 프로젝트 루트에 CLAUDE.md 생성
- 팀 컨벤션, 코딩 규칙, 금지 사항 정의
- AI가 이 파일을 읽고 모든 작업에 적용

Step 3: Skill/Command 활용
- /pr, /work-log 같은 내장 명령어부터 시작
- 반복 작업이 보이면 커스텀 Skill로 자동화
- 점진적으로 에이전트 팀 워크플로우 구축

핵심: 한 번에 다 하지 말고, 가장 반복적인 작업 하나부터 자동화

---

### 슬라이드 16: Q&A

**질문 & 토론**

참고 자료:
- CLAUDE.md 실제 설정 파일 공유 가능
- 각 Skill/Command 소스 코드 공개

---

## Manus용 프롬프트

아래 텍스트를 Manus에 붙여넣으세요.

```
"Claude Code + AI Agent로 일하는 법"이라는 제목의 팀 공유 발표자료를 만들어줘.
16장 슬라이드, 한국어, 톤은 실무 중심 + 친근한 설명.

내용 요약:
1. 표지: Claude Code + AI Agent로 일하는 법
2. 발표 목적: AI를 "질문-답변"이 아닌 "작업 파이프라인"으로 활용
3. 전체 워크플로우: /work-plan → /work-plan-start → /self-review → /pr → /work-log
4. 작업 계획 자동화: req.md → WORK-SPEC.md 자동 생성, Gemini+Codex 크로스 체크
5. 에이전트 팀 병렬 구현: Explore/Plan/Main/test-generator/code-refactor 5개 에이전트 3-Phase
6. AI 팀 코드 리뷰: 성능/보안/테스트/컨벤션 4명의 독립 에이전트가 각자 전문 관점으로 병렬 리뷰. 하나의 AI가 전부 보는 게 아니라 전문가별로 분리
7. 리뷰 명령어 3종 비교: /self-review(PR 전 셀프 점검, +Gemini/Codex), /team-review(PR 후 리뷰 보조), /review-pr(리뷰어가 남의 PR 분석, +Gemini/Codex). 작성자 워크플로우: 코드→/self-review→수정→/pr→/team-review. 리뷰어 워크플로우: PR 받음→/review-pr 123→AI 결과 참고→최종 판단
8. Pencil MCP 디자인: 자연어로 Desktop+Mobile 화면 디자인 → 코드 변환
9. /frontend-design: 프롬프트로 프로덕션급 UI 코드 직접 생성
10. /work-log: git diff → Confluence 문서 자동 생성 (비개발자 친화적)
11. Slack/Jira/Confluence 연동: /slack-to-jira, /jira-report, /meeting-notes 등
12. CLAUDE.md: AI에게 팀 규칙을 가르치는 설정 파일
13. 3-AI 협업: Claude + Gemini + Codex 크로스 체크 구조
14. Before/After 비교: 각 작업별 수동 vs AI 자동화 방식 비교 (구체적 수치 없이 방식 차이 중심)
15. 시작 방법: Claude Code 설치 → CLAUDE.md 작성 → Skill 활용 3단계
16. Q&A
```
