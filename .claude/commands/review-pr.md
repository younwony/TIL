---
description: 특정 PR에 대해 4명의 전문 리뷰어 에이전트 팀 + Gemini/Codex 크로스 리뷰를 병렬 수행합니다.
allowed-tools: Bash(git:*), Bash(gh:*), Bash(gemini:*), Bash(codex:*), Bash(where:*), Read, Write, Glob, Grep, Task
---

# PR 팀 코드 리뷰

지정된 PR에 대해 4명의 전문 리뷰어 에이전트 + Gemini/Codex 크로스 리뷰를 **병렬로** 실행하여 다관점 코드 리뷰를 수행한다.

## 리뷰 팀 구성

| 리뷰어 | 에이전트 | 관점 |
|--------|---------|------|
| 성능 전문가 | `review-performance` | N+1 쿼리, 고비용 객체, 컬렉션 최적화, I/O 병목 |
| 보안 전문가 | `review-security` | OWASP Top 10, 인증/인가, 민감정보 노출, 입력 검증 |
| 테스트 전문가 | `review-test-coverage` | 테스트 존재 여부, 커버리지, 누락 시나리오, 테스트 품질 |
| 컨벤션 전문가 | `review-convention` | CLAUDE.md 규칙, 클린 코드, SOLID, 네이밍, 가독성 |
| Gemini | Gemini CLI | 코드 품질, 보안, 성능, 설계 (선택적) |
| Codex | Codex CLI | 코드 품질, 보안, 성능, 설계 (선택적) |

## PR 번호 확인

- PR 번호: `$ARGUMENTS`
- PR 번호가 없으면 AskUserQuestion으로 "리뷰할 PR 번호를 입력해주세요."라고 요청

## 사전 확인

다음 명령어들을 **병렬로** 실행:

1. `where gemini` 명령으로 Gemini CLI 설치 여부 확인
   - 설치됨: Gemini 크로스 리뷰 활성화
   - 미설치: "Gemini CLI가 설치되지 않아 Gemini 크로스 리뷰를 건너뜁니다" 안내
2. Codex 설치 여부 확인 (Plugin 우선, CLI fallback):
   1. `test -f "$HOME/.claude/plugins/cache/openai-codex/codex/1.0.0/scripts/codex-companion.mjs"` → Plugin 설치됨: `/codex:review` 사용
   2. Plugin 미설치 시 `where codex` → CLI 설치됨: `codex exec -` fallback 사용
   3. 둘 다 미설치: "Codex가 설치되지 않아 Codex 크로스 리뷰를 건너뜁니다" 안내

> Gemini와 Codex 모두 비활성화된 경우: "외부 크로스 리뷰 없이 에이전트 팀 리뷰로 진행합니다" 안내

## 1단계: PR 정보 수집

다음 명령어들을 **병렬로** 실행하여 정보를 수집:

1. `gh pr view $PR_NUMBER --json title,body,author,baseRefName,headRefName,files,additions,deletions,commits,reviews,state` - PR 상세 정보
2. `gh pr diff $PR_NUMBER` - PR diff 내용
3. `gh pr view $PR_NUMBER --json comments` - PR 코멘트
4. `gh pr diff $PR_NUMBER --name-only 2>/dev/null || gh pr view $PR_NUMBER --json files --jq '.files[].path'` - 변경 파일 목록

## 2단계: 전체 개요

수집한 정보를 바탕으로 다음을 정리:

- **PR 목적**: 제목과 본문에서 변경 의도 파악
- **주요 변경 파일**: 변경된 파일 목록과 각 파일의 변경 규모 (추가/삭제 라인)
- **커밋 히스토리**: 커밋 메시지 요약
- **현재 상태**: 리뷰 상태, 코멘트 유무

## 3단계: 6명의 리뷰어 병렬 실행

**하나의 메시지에서 최대 6개의 도구 호출**을 동시에 실행한다.

### 3-1. 4명의 에이전트 팀 (Task 도구)

4개의 Task를 **동시에** 실행한다. 각 에이전트에게 다음 정보를 전달:

- PR 번호: `$PR_NUMBER`
- 변경 파일 목록 (1단계에서 수집)
- PR diff 내용 (1단계에서 수집)

각 에이전트에게 전달할 프롬프트 템플릿:

```
PR #$PR_NUMBER 의 변경사항을 {관점} 관점에서 리뷰해줘.

변경 파일: {파일 목록}

PR diff:
{diff 내용}

에이전트 정의의 작업 흐름과 보고 형식을 따라서 결과를 반환해줘.
```

### 3-2. Gemini 크로스 리뷰 (Bash 도구, 선택적)

> 사전 확인에서 Gemini CLI가 설치된 경우에만 실행.

#### 대용량 diff 처리

1. PR의 additions + deletions 합계로 변경 규모를 확인 (1단계에서 수집한 정보 활용)
2. 총 변경 라인이 **1,000줄 이하**인 경우: 전체 diff를 그대로 전달
3. 총 변경 라인이 **1,000줄 초과**인 경우: stat 요약 + 핵심 파일의 diff만 선별 전달

#### Gemini 리뷰 실행 (**Bash timeout: 300000ms 필수**)

```bash
# 일반적인 경우 (1,000줄 이하)
gh pr diff $PR_NUMBER | gemini -p "다음 PR의 코드 변경사항을 코드 리뷰해줘:

## 리뷰 관점
1. **코드 품질**: 네이밍, SRP, 중복코드, 매직넘버
2. **보안**: OWASP Top 10 취약점, 입력 검증, 민감정보 노출
3. **성능**: N+1 쿼리, 불필요한 반복, 고비용 객체 생성
4. **설계**: SOLID 원칙 준수, 적절한 추상화

## 출력 형식
각 관점별로 발견한 이슈를 심각도(❌ 필수 수정 / ⚠️ 권장 / 💡 참고)와 함께 정리해줘.
한국어로 답변해줘."
```

```bash
# 대용량인 경우 (1,000줄 초과): 주요 파일 diff만 전달
gh pr diff $PR_NUMBER -- {주요파일1} {주요파일2} ... | gemini -p "다음 PR의 코드 변경사항을 코드 리뷰해줘:

## 리뷰 관점
1. **코드 품질**: 네이밍, SRP, 중복코드, 매직넘버
2. **보안**: OWASP Top 10 취약점, 입력 검증, 민감정보 노출
3. **성능**: N+1 쿼리, 불필요한 반복, 고비용 객체 생성
4. **설계**: SOLID 원칙 준수, 적절한 추상화

## 출력 형식
각 관점별로 발견한 이슈를 심각도(❌ 필수 수정 / ⚠️ 권장 / 💡 참고)와 함께 정리해줘.
한국어로 답변해줘."
```

- 실행 실패 시 "Gemini 리뷰 실행에 실패했습니다." 안내 후 계속 진행

### 3-3. Codex 크로스 리뷰 (Codex Plugin, 선택적)

> 사전 확인에서 Codex Plugin이 설치된 경우에만 실행.

#### Codex 리뷰 실행

Codex Plugin의 리뷰를 사용한다. 대용량 diff도 Plugin이 자체적으로 처리하므로 별도 분기가 불필요하다.

**실행 모드 선택** (2단계에서 수집한 additions + deletions 기준):
- 변경 1,000줄 이하: foreground 실행
  ```
  /codex:review --base {baseRefName} --wait
  ```
- 변경 1,000줄 초과: background 실행 (타임아웃 방지)
  ```
  /codex:review --base {baseRefName} --background
  ```
  → 결과 통합 시점에 `/codex:result`로 수집

**실패 시 재시도:**
1. 1차 실패 → `--resume`으로 재시도 (이전 컨텍스트 유지)
   ```
   /codex:rescue --resume 이전 리뷰를 이어서 완료해줘 --wait
   ```
2. 2차 실패 → CLI fallback (Bash, timeout: 300000ms):
   ```bash
   (echo "다음 PR의 코드 변경사항을 코드 리뷰해줘. 코드 품질, 보안, 성능, 설계 관점에서 이슈를 심각도와 함께 한국어로 정리해줘:" && gh pr diff $PR_NUMBER) | codex exec -
   ```
3. 3차 실패 → "Codex 리뷰 실행에 실패했습니다." 안내 후 계속 진행

- **Plugin 미설치 시**: CLI fallback 직행

## 4단계: 결과 통합 → 리뷰 결과 출력

6명의 리뷰 결과를 통합하여 다음 형식으로 출력:

```markdown
# PR Review - #$PR_NUMBER

> PR: `{PR 제목}` | 리뷰 일시: YYYY-MM-DD HH:MM
> 리뷰 방식: Agent Team (4명 병렬) + Cross Review (Gemini/Codex)

## 변경 개요

- **작성자**: {author}
- **브랜치**: {headRefName} → {baseRefName}
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

6명의 리뷰어 결과를 종합하여 우선순위별로 정리:

### 필수 수정 (❌)
{모든 리뷰어의 🔴 높음/치명 이슈를 파일별로 통합}

### 권장 수정 (⚠️)
{모든 리뷰어의 ⚠️ 중간 이슈를 파일별로 통합}

### 참고 (💡)
{모든 리뷰어의 💡 참고 이슈를 파일별로 통합}

## 결론

{모든 리뷰 결과를 종합한 최종 의견}
{🔴 이슈가 1건이라도 있으면 "수정 후 Approve 권장", 없으면 "Approve 가능"}
```

## 5단계: 다음 액션 선택

AskUserQuestion으로 다음 액션을 선택:

- **리뷰 코멘트 작성**: `gh pr review $PR_NUMBER` 명령으로 GitHub에 리뷰 코멘트 등록
- **특정 관점 상세 분석**: 특정 리뷰어에게 추가 분석 요청
- **특정 파일 상세 분석**: 특정 파일을 더 깊이 분석
- **종료**: 리뷰 종료
