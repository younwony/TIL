# Track 워크플로우 6단계 가이드

> 이 파일은 `/track-status` skill이 참조하는 워크플로우 단계별 상세 가이드다.
> 단일 진실 출처: [`.claude/docs/WORKFLOW-GUIDELINE.md`](../../../docs/WORKFLOW-GUIDELINE.md)

## 단계 정의

```
[Step 0]   환경 점검          /setup-til-skills
[Step 0.5] 제품 검증           /product-review (선택)
[Step 1]   작업 명세            /work-plan
[Step 2]   구현                 /work-plan-start
[Step 3]   검증                 /self-review, /qa-scenario
[Step 4]   마무리               /pr, /work-log
[Sticky]   /caveman             모든 단계 토글
```

---

## Step 0 — 환경 점검

### 진입 조건 (다음 중 하나)
- `.claude/docs/setup-state.json` 부재 → 한 번도 setup 안 됨
- `setup-state.json`의 `setup_at` 이 7일 이상 경과
- 현재 Track의 `hard_dependencies` 중 미충족 항목 존재
- 사용자가 명시적으로 "환경 점검" 요청

### 진단 항목
- ATLASSIAN_API_TOKEN 환경변수
- Slack/Jira/Pencil/Figma MCP 연결
- mysqlsh, Docker, gh CLI 설치
- Hooks 실행 권한
- Codex/Gemini CLI (선택)

### 가이드 메시지 양식
```
⚠️ 환경 점검 필요
- ❌ Slack MCP (영향: jira-*, slack-* 등 9개 skill 차단)
- ⏳ setup-state.json 7일 경과 (재실행 권장)

다음: `/setup-til-skills` 실행하세요.
```

### 통과 조건
모든 hard dependency 충족 OR 사용자가 "skip" 선택 → Step 0.5 또는 Step 1로 진행

---

## Step 0.5 — 제품 검증 (선택)

### 진입 조건 (다음 중 하나)
- 요구사항이 모호함 (req.md에 페르소나/사용 시나리오 없음)
- 변경 규모 5파일 이상 예상
- 사용자가 "이거 진짜 필요한가?" 자기 의심 표현
- 사용자 명시 요청

### Grilling 자동 진입 (Step 1.5 내부)
- WORK-SPEC.md/req.md에 페르소나 비어 있음
- 사용자가 "그릴미" / "인터뷰해줘" 요청

### 산출물
`metadata.json`에 다음 필드 기록 (선택):
```json
{
  "product_review_decision": "Go" | "Go(축소)" | "Hold" | "No-Go",
  "grilling_completed": true | false
}
```

### 가이드 메시지
```
이 작업, 정말 만들어야 하나요?
- 변경 규모: ~7파일 예상 → product-review 권장
- 다음: `/product-review` 실행
```

### 판정별 다음 단계
- `Go` → Step 1
- `Go(축소)` → 범위 줄이고 Step 1
- `Hold` → 정보 수집 후 재시도
- `No-Go` → 종료 또는 대안

---

## Step 1 — 작업 명세

### 진입 조건
- Step 0/0.5 통과
- `2_WORK-SPEC.md` 파일 부재 또는 갱신 필요

### 산출물
- `1_REQ-SNAPSHOT.md` (work-plan)
- `2_WORK-SPEC.md` (work-plan)
- `3_FEATURE-CHECKLIST.md` (work-plan)

### 품질 체크 (반드시)

WORK-SPEC.md를 읽어 다음 섹션 존재 여부 확인:

- [ ] **3-1. 변경 인터페이스 (Agent Brief)** 섹션 — Matt Pocock의 AGENT-BRIEF 양식
  - Summary, Current/Desired Behavior, Key Interfaces, Acceptance Criteria, Out of Scope

- [ ] FEATURE-CHECKLIST.md의 사용자/QA 관점 항목 작성됨

### 가이드 메시지
```
✅ Step 1 완료
- 1_REQ-SNAPSHOT.md ✓
- 2_WORK-SPEC.md ✓
  └ 3-1 (Agent Brief) ✅
- 3_FEATURE-CHECKLIST.md ✓

다음: `/work-plan-start` 또는 직접 구현
```

품질 체크 실패 시:
```
⚠️ WORK-SPEC.md 보강 필요
- 3-1 (변경 인터페이스 / Agent Brief) 섹션 누락
- 권장: WORK-SPEC.md를 직접 수정하거나 `/work-plan` 재실행
```

---

## Step 2 — 구현

### 진입 조건
- WORK-SPEC.md 존재
- `4_PLAN.md` 부재 또는 미완료 Phase 존재

### 산출물
- `4_PLAN.md` (work-plan-start, Phase별 진행률)
- `5_ARCHITECTURE.md` (구현 완료)
- `6_SPEC.md` (구현 완료)

### 모드 자동 선택 (work-plan-start)
| 변경 파일 수 | 모드 | Main 역할 |
|------------|------|---------|
| 1~2 | Solo | 단독 처리 |
| 3~4 | Standard | 조율 + 구현 |
| 5+ | Coordinator | 순수 조율 (워커에 위임) |

### 구현 중 도구 안내 (가이드에 항상 표시)
| 상황 | 도구 | 비고 |
|------|------|------|
| 에러 | `debugger` 에이전트 | Phase 1 (재현 loop) 우선. 10가지 loop 옵션 |
| 코드 영역 모름 | `/zoom-out` | CONTEXT.md 어휘로 모듈+호출자 맵 |
| 테스트 누락 | `test-generator` 에이전트 | |
| 코드 스멜 | `code-refactor` 에이전트 | 14가지 스멜 |
| 응답 길어짐 | `/caveman` 토글 | ~75% 토큰 절약 |

---

## Step 3 — 검증

### 진입 조건
- 모든 Phase 완료 OR 사용자가 검증 단계 진입 명시

### 산출물
- `7_SELF-REVIEW.md` (self-review, 4명 에이전트 + Gemini/Codex 병렬)
- `8_QA-SCENARIOS.md` (qa-scenario)

### 추천 명령 순서
1. `/feature-check` — FEATURE-CHECKLIST 코드 레벨 검증
2. `/self-review` — SELF-REVIEW.md 생성
3. `/qa-scenario` — QA-SCENARIOS.md 생성
4. (선택) `/security-audit` — STRIDE+OWASP
5. (선택) `/ai-slop-detect` — 과잉 추상화 탐지
6. (해당 시) `/browser-debug` — Playwright + Chrome 2-Layer

---

## Step 4 — 마무리

### 진입 조건
- Step 3 통과
- 사용자가 PR/공유 요청

### 산출물
- GitHub PR (pr)
- Confluence 페이지 (work-log / work-share)
- metadata.json `status: completed` 갱신

### 추천 명령
1. `/pr` — PR 자동 생성
2. `/work-log` 또는 `/work-share` — Confluence 문서화
3. `/track-status {id}` 후 응답에서 상태를 `completed`로 변경

---

## Sticky — 단계 무관 도구 모드

### `/caveman`
- 어디서든 ON/OFF
- 한 번 ON되면 사용자가 "stop caveman" 또는 "동굴인 그만" 할 때까지 유지
- Auto-clarity 예외: 보안 경고, 비가역 명령, 멀티스텝 시퀀스, 에러 진단 시 자동 해제

### `/zoom-out`
- 단발성. CONTEXT.md 어휘로 한 단계 추상화 맵 출력
- "모르는 코드 영역" 진단 도구

---

## metadata.json 새 필드 (선택, 누락 OK)

기존 Track의 metadata.json은 새 필드 누락 가능. `/track-status`는 누락 시 "정보 없음"으로 graceful 처리.

```json
{
  "product_review_decision": "Go" | "Go(축소)" | "Hold" | "No-Go" | null,
  "grilling_completed": true | false,
  "agent_brief_complete": true | false,
  "hard_dependencies": ["ATLASSIAN_API_TOKEN", "Slack MCP"],
  "tools_used": ["zoom-out", "caveman", "debugger"]
}
```

이 필드들은 모두 optional. 누락 시 표시:
- product_review_decision 없음 → "(미실행)"
- agent_brief_complete 없음 → 파일을 직접 grep으로 검사 ("3-1." 패턴)
- hard_dependencies 없음 → 환경 점검 결과로부터 추론
- tools_used 없음 → 표시 안 함

---

## 참고

- 메인 가이드라인: [`.claude/docs/WORKFLOW-GUIDELINE.md`](../../../docs/WORKFLOW-GUIDELINE.md)
- Hard/Soft 의존성 분류: [`.claude/docs/adr/0001-skill-dependency-classification.md`](../../../docs/adr/0001-skill-dependency-classification.md)
- Matt Pocock 7패턴 분석: [`cs/tool/mattpocock-skills-harness.md`](../../../../cs/tool/mattpocock-skills-harness.md)
