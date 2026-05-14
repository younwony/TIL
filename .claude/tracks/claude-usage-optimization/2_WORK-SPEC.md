# Claude Code 사용량 최적화 — 작업 명세서

> 이 문서는 `/usage` 점검 결과를 바탕으로 로컬+글로벌 스킬·에이전트에서 비용을 절약할 수 있는 부분을 분석하고 적용한 작업 명세서입니다.

## 1. 요구사항 요약

- 서브에이전트 과다 호출(주간 84%)과 긴 컨텍스트(45%)가 주요 비용 원인
- 로컬(`.claude/`) + 글로벌(`~/.claude/`) 스킬·에이전트를 점검하여 절약 가능한 부분 적용

## 3. 변경 파일 목록

| # | 파일 (로컬) | 글로벌 | 변경 내용 |
|---|------------|--------|---------|
| 1 | `.claude/agents/design-reviewer.md` | `~/.claude/agents/design-reviewer.md` | `model: opus` → `model: sonnet` |
| 2 | `.claude/agents/cs-diagram-generator.md` | `~/.claude/agents/cs-diagram-generator.md` | `model: sonnet` → `model: haiku` |
| 3 | `.claude/skills/work-plan/SKILL.md` | `~/.claude/skills/work-plan/SKILL.md` | Step 3.7 design-reviewer opt-in (`--design-review` 플래그) |
| 4 | `.claude/agents/test-generator.md` | `~/.claude/agents/test-generator.md` | Advisor 조건 OR → AND 강화 |
| 5 | `.claude/agents/code-refactor.md` | `~/.claude/agents/code-refactor.md` | Advisor 조건 OR → AND 강화 |

## 9. 작업 단계

### Phase 1 (완료): 에이전트 모델 수정
- [x] design-reviewer: `opus` → `sonnet` (로컬 + 글로벌)
- [x] cs-diagram-generator: `sonnet` → `haiku` (로컬 + 글로벌)

### Phase 2 (완료): 스킬 플로우 수정
- [x] work-plan SKILL.md: Step 3.7 opt-in화, `--design-review` 플래그 추가 (로컬 + 글로벌)

### Phase 3 (완료): Advisor 트리거 조건 강화
- [x] test-generator: Advisor 조건 OR → AND 조합 (로컬 + 글로벌)
- [x] code-refactor: Advisor 조건 OR → AND 조합 (로컬 + 글로벌)
