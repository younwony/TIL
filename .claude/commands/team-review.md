---
description: 4명의 전문 리뷰어 에이전트 팀으로 현재 브랜치를 병렬 코드 리뷰합니다.
allowed-tools: Bash(git:*), Bash(gh:*), Bash(gemini:*), Bash(codex:*), Bash(where:*), Read, Write, Glob, Grep, Task
---

# 팀 코드 리뷰

4명의 전문 리뷰어 에이전트를 **병렬로** 실행하여 현재 브랜치의 변경사항을 다관점 코드 리뷰하고, 결과를 TEAM-REVIEW.md로 통합한다.

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
2. `where codex`으로 Codex CLI 설치 확인
3. 설치된 외부 CLI로 크로스 리뷰 병렬 실행 (기존 self-review의 4~5단계와 동일)
4. 미설치 시 해당 크로스 리뷰 건너뜀

### 3단계: 결과 통합 → TEAM-REVIEW.md 생성

4명의 리뷰 결과 + 크로스 리뷰 결과를 통합하여 프로젝트 루트에 `TEAM-REVIEW.md`를 생성한다:

```markdown
# Team Code Review

> 브랜치: `{브랜치명}` | 리뷰 일시: YYYY-MM-DD HH:MM
> 리뷰 방식: Agent Team (4명 병렬) + Cross Review

## 변경 개요

- **커밋 수**: N개
- **변경 파일 수**: N개
- **추가/삭제**: +N / -N

### 커밋 히스토리

| 커밋 | 메시지 |
|------|--------|

### 변경 파일

| 파일 | 추가 | 삭제 | 설명 |
|------|------|------|------|

## 종합 리뷰 요약

| 관점 | 리뷰어 | 상태 | 요약 |
|------|--------|------|------|
| 성능 | review-performance | ✅/⚠️/❌ | 한 줄 요약 |
| 보안 | review-security | ✅/⚠️/❌ | 한 줄 요약 |
| 테스트 | review-test-coverage | ✅/⚠️/❌ | 한 줄 요약 |
| 컨벤션 | review-convention | ✅/⚠️/❌ | 한 줄 요약 |

### 심각도 집계

| 심각도 | 성능 | 보안 | 테스트 | 컨벤션 | 합계 |
|--------|------|------|--------|--------|------|
| 🔴 높음/치명 | N | N | N | N | **N** |
| ⚠️ 중간 | N | N | N | N | **N** |
| 💡 참고 | N | N | N | N | **N** |

## 성능 리뷰

{review-performance 에이전트의 전체 결과}

## 보안 리뷰

{review-security 에이전트의 전체 결과}

## 테스트 커버리지 리뷰

{review-test-coverage 에이전트의 전체 결과}

## 컨벤션/가독성/유지보수성 리뷰

{review-convention 에이전트의 전체 결과}

## Gemini 크로스 리뷰

> Reviewed by: Gemini (via Gemini CLI)
> 미설치/실패 시 이 섹션 생략

{Gemini 리뷰 결과}

## Codex 크로스 리뷰

> Reviewed by: Codex (via Codex CLI)
> 미설치/실패 시 이 섹션 생략

{Codex 리뷰 결과}

## 개선 제안 (통합)

4명의 리뷰어 + 크로스 리뷰 결과를 종합하여 우선순위별로 정리:

### 필수 수정 (❌)
{모든 리뷰어의 🔴 높음/치명 이슈를 파일별로 통합}

### 권장 수정 (⚠️)
{모든 리뷰어의 ⚠️ 중간 이슈를 파일별로 통합}

### 참고 (💡)
{모든 리뷰어의 💡 참고 이슈를 파일별로 통합}

## 결론

{모든 리뷰 결과를 종합한 최종 의견. PR 생성 가능 여부 판단}
{🔴 이슈가 1건이라도 있으면 "PR 생성 전 수정 필요", 없으면 "PR 생성 가능"}
```

### 4단계: git add + 다음 액션 선택

```bash
git add TEAM-REVIEW.md
```

AskUserQuestion으로 다음 액션을 선택:

- **PR 생성 진행**: `/pr` 스킬을 이어서 실행
- **개선 사항 직접 수정**: 리뷰에서 발견된 문제를 바로 수정
- **특정 관점 상세 분석**: 특정 리뷰어에게 추가 분석 요청
- **종료**: 팀 리뷰 종료
