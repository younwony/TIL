---
description: 변경사항 분석 → 영향도 매트릭스 → BDD QA 시나리오 문서 생성 (qa-scenario 스킬 실행)
allowed-tools: Bash, Read, Glob, Grep, Edit, Write, Agent, AskUserQuestion, EnterPlanMode, ExitPlanMode
---

# QA Scenario Generator

현재 브랜치의 변경사항을 분석하여 BDD(Given-When-Then) QA 시나리오 문서를 생성합니다.

$ARGUMENTS

## 실행

`qa-scenario` 스킬을 호출하여 실행한다.
이 커맨드는 스킬의 트리거 역할만 수행하며, 실제 로직은 스킬에 정의되어 있다.

스킬 워크플로우:
1. Git 변경사항 수집
2. **유저 인터뷰** (변경 파일 3개+ 일 때) — 진입점·사용자 역할·비즈니스 목적·리스크를 추정값과 함께 확인
   - 같은 Track 내에 `QA-CONTEXT.md` 캐시가 있으면 자동 재사용
   - 갱신하려면 `/qa-scenario refresh` 또는 "인터뷰 다시 받자"
3. 영향 범위 분석 (Explore 에이전트, 사용자 컨텍스트 반영)
4. 시나리오 초안 작성 (BDD + 인라인 SVG)
5. 사용자 리뷰 & 승인
6. `{DOC_DIR}/8_QA-SCENARIOS.html` 생성
7. Track 연동 및 완료 보고
