---
description: 4명의 전문 리뷰어 에이전트 팀으로 현재 브랜치를 병렬 코드 리뷰하고 TEAM-REVIEW.html을 생성합니다.
allowed-tools: Bash(git:*), Bash(gh:*), Bash(gemini:*), Bash(codex:*), Bash(where:*), Bash(test:*), Bash(node:*), Read, Write, Glob, Grep, Task, Skill(codex:*)
---

# 팀 코드 리뷰

4명의 전문 리뷰어 에이전트를 **병렬로** 실행하여 현재 브랜치의 변경사항을 다관점 코드 리뷰하고, 결과를 `TEAM-REVIEW.html`로 통합한다.

> **Gemini/Codex 보조 크로스 체크는 `gemini-check` / `codex-check` 하네스로 위임한다.**
> 본 커맨드는 4명의 리뷰 에이전트(performance/security/test-coverage/convention) 호출이 주력이다. Gemini/Codex 추가 검증을 사용할 때는 두 하네스를 통한 Agent 위임 호출을 사용한다.
> Codex Plugin Skill(`/codex:review`, `/codex:rescue` 등)은 사용하지 않는다 — codex-check 하네스가 CLAUDE.md 정책을 강제한다.
> 아래 본문의 Gemini/Codex 직접 호출 섹션은 호환성을 위해 남겨두지만 신규 실행은 위임 패턴을 사용한다.

## 리뷰 팀 구성

| 리뷰어 | 에이전트 | 관점 |
|--------|---------|------|
| 성능 전문가 | `review-performance` | N+1 쿼리, 고비용 객체, 컬렉션 최적화, I/O 병목 |
| 보안 전문가 | `review-security` | OWASP Top 10, 인증/인가, 민감정보 노출, 입력 검증 |
| 테스트 전문가 | `review-test-coverage` | 테스트 존재 여부, 커버리지, 누락 시나리오, 테스트 품질 |
| 컨벤션 전문가 | `review-convention` | CLAUDE.md 규칙, 클린 코드, SOLID, 네이밍, 가독성 |

## Compare Branch 결정

1. `git rev-parse --verify main 2>/dev/null` → 존재하면 `main` 사용
2. `git rev-parse --verify master 2>/dev/null` → 존재하면 `master` 사용
3. 둘 다 없으면 AskUserQuestion으로 비교 기준 브랜치를 입력받는다

## 사전 확인

1. 현재 브랜치가 main/master가 아닌지 확인 (작업 브랜치에서만 실행)
2. `git diff {COMPARE_BRANCH}...HEAD --stat`으로 변경 규모 확인
3. 변경 파일이 없으면 "리뷰할 변경사항이 없습니다." 안내 후 중단

## 실행 절차

### 1단계: 4명의 리뷰어 에이전트 병렬 실행

4개의 Task를 **동시에** 실행한다. 각 에이전트에게 다음 정보를 전달:

- 비교 브랜치: `{COMPARE_BRANCH}`
- 변경 파일 목록: `git diff {COMPARE_BRANCH}...HEAD --name-only` 결과

각 에이전트에게 전달할 프롬프트 템플릿:

```
현재 브랜치의 변경사항을 {관점} 관점에서 리뷰해줘.
비교 브랜치: {COMPARE_BRANCH}
변경 파일: {파일 목록}

에이전트 정의의 작업 흐름과 보고 형식을 따라서 결과를 반환해줘.
```

4개 에이전트를 **하나의 메시지에서 4개의 Task 도구 호출**로 병렬 실행한다.

### 2단계: Gemini/Codex 크로스 리뷰 (선택적)

에이전트 4명이 작업하는 동안:

1. `where gemini`으로 Gemini CLI 설치 확인
2. Codex 설치 여부 확인: `test -f "$HOME/.claude/plugins/cache/openai-codex/codex/1.0.0/scripts/codex-companion.mjs"` (Plugin 우선, CLI fallback)
3. 설치된 외부 도구로 크로스 리뷰 병렬 실행:
   - Gemini: `git diff {COMPARE_BRANCH}...HEAD | gemini -p "..."` (기존 방식)
   - Codex (Plugin): `/codex:review --base {COMPARE_BRANCH} --background` → 결과 통합 시 `/codex:result`로 수집. 실패 시 `/codex:rescue --resume` 재시도 1회 → CLI fallback
   - Codex (fallback): `codex review --base {COMPARE_BRANCH}` (Bash, timeout: 240000ms). **절대 `codex exec -` 사용 금지.**
4. 미설치 시 해당 크로스 리뷰 건너뜀

### 3단계: 결과 통합 → TEAM-REVIEW.html 생성

4명의 리뷰 결과 + 크로스 리뷰 결과를 통합하여 `TEAM-REVIEW.html`을 생성한다.

산출 문서는 html-doc 스킬 규칙을 따라 자체 완결 HTML로 작성한다. template.html을 skeleton으로 쓰고, 리뷰어별 지적사항은 `<details>` collapsible과 상태 배지로, **심각도 분포는 components.html의 막대 차트 SVG로** 시각화한다.

HTML 구조는 다음 섹션 구성으로 작성한다:

- **`<header>`**: 브랜치명, 리뷰 일시, 리뷰 방식 (Agent Team 4명 병렬 + Cross Review)
- **변경 개요 `<section>`**: 커밋 수·변경 파일 수·추가/삭제 라인, 커밋 히스토리 `<table>`, 변경 파일 `<table>`
- **종합 리뷰 요약 `<section>`**: 관점별 상태 배지(badge-ok/badge-warn/badge-err)를 포함한 `<table>`, 심각도 집계 비교 `<table>`, **심각도 분포 막대 차트 SVG (components.html 컴포넌트 8번)**
- **리뷰어별 `<section>`** (성능 / 보안 / 테스트 / 컨벤션 / Gemini 크로스 / Codex 크로스):
  - 미설치/실패한 외부 리뷰어는 섹션 생략
  - 각 지적사항은 `<details><summary>심각도 배지 + 이슈 제목</summary>...</details>` collapsible로 표현
- **개선 제안 통합 `<section>`**: 필수 수정(badge-err) / 권장 수정(badge-warn) / 참고(badge-ok) 3개 subsection, 파일별로 이슈 통합
- **결론 `<section>`**: 최종 의견, PR 생성 가능 여부 (🔴 이슈 1건이라도 있으면 "PR 생성 전 수정 필요", 없으면 "PR 생성 가능")

### 4단계: git add + 다음 액션 선택

```bash
git add TEAM-REVIEW.html
```

AskUserQuestion으로 다음 액션을 선택:

- **PR 생성 진행**: `/pr` 스킬을 이어서 실행
- **개선 사항 직접 수정**: 리뷰에서 발견된 문제를 바로 수정
- **특정 관점 상세 분석**: 특정 리뷰어에게 추가 분석 요청
- **종료**: 팀 리뷰 종료
