# /track-status 워크플로우 재구성 — Matt Pocock 패턴 통합

> 이 문서는 이번 대화에서 추가한 9가지 요소(CONTEXT.md, ADR 0001, product-review grilling, debugger 5-phase, /setup-til-skills, .claude 보호 hook, /zoom-out, /caveman, WORK-SPEC AGENT-BRIEF)를 `/track-status` 워크플로우에 어떻게 통합할지 분석한 설계 문서다.

---

## 1. 현재 `/track-status` 워크플로우 분석

### 핵심 구조 (113줄 SKILL.md 요약)

```
.claude/tracks/
├── index.md (Active / Completed / Archived 분류)
└── {track_id}/
    ├── metadata.json   (track_id, description, type, status, branch,
    │                    work_spec_path, current_phase, total_phases, ...)
    ├── 1_REQ-SNAPSHOT.md   (work-plan)
    ├── 2_WORK-SPEC.md      (work-plan)
    ├── 3_FEATURE-CHECKLIST.md (work-plan)
    ├── 4_PLAN.md           (work-plan-start)
    ├── 5_ARCHITECTURE.md   (구현 완료)
    ├── 6_SPEC.md           (구현 완료)
    ├── 7_SELF-REVIEW.md    (self-review)
    └── 8_QA-SCENARIOS.md   (qa-scenario)
```

### 현재 워크플로우 가이드 분기

| 상태 | 안내 |
|------|------|
| WORK-SPEC.md 없음 | `/work-plan` 실행 |
| WORK-SPEC.md 있고 PLAN.md 없음 | `/work-plan-start` |
| PLAN.md 있고 미완료 Phase 있음 | 현재 Phase 진행 |
| 모든 Phase 완료 | `/self-review` 또는 `/pr` |

### 현재의 한계

1. **워크플로우 시작 전 환경 점검이 없음** — Hard dependency 미충족이면 한참 가다가 막힘
2. **product-review 단계가 워크플로우 외부에 있음** — "왜 만드는가" 검증을 건너뛰기 쉬움
3. **WORK-SPEC.md 품질 체크 없음** — 단순히 "있다/없다"만. AGENT-BRIEF 섹션 누락 등은 못 잡음
4. **도구(zoom-out, caveman, debugger 5-phase)가 가이드에 등장 안 함** — 사용자가 존재를 잊기 쉬움
5. **인프라 문서(CONTEXT.md, ADR)에 대한 가시성 없음** — 만들어 놓고도 활용 안 됨

---

## 2. 9가지 추가 요소의 워크플로우 위치 매핑

| # | 요소 | 분류 | 워크플로우 위치 |
|---|------|------|----------------|
| 1 | `.claude/CONTEXT.md` | **인프라** | Step 0 (환경 점검) — 존재/갱신 필요 알림 |
| 2 | `.claude/docs/adr/0001-...` | **인프라** | Step 0 — Hard dependency 분류표의 source of truth |
| 3 | `product-review` grilling | **워크플로우 단계** | Step 0.5 (work-plan 이전) — 신규 단계 |
| 4 | `debugger` 5-phase + 10-loop | **도구** | Step 2 구현 중, 에러 발생 시 안내 |
| 5 | `/setup-til-skills` | **워크플로우 단계** | Step 0 (가장 처음) — 신규 단계 |
| 6 | `block-dangerous` hook | **인프라** | 워크플로우 무관 — 백그라운드 강제 |
| 7 | `/zoom-out` | **도구** | Step 1/2 탐색 중, 코드 영역 모를 때 |
| 8 | `/caveman` | **도구 모드** | 모든 단계, 사용자 토글 |
| 9 | WORK-SPEC AGENT-BRIEF (3-1 섹션) | **품질 체크** | Step 1 종료 시 검증 항목 |

---

## 3. 재구성 후 워크플로우

### 새 단계 흐름

```
[Step 0]  환경 점검         ← 신규
   ├─ /setup-til-skills (Hard dep 충족도 확인)
   ├─ CONTEXT.md / ADR 0001 존재 확인
   └─ ✅ 모두 OK → 다음 단계

[Step 0.5] 제품 검증         ← 신규 (선택)
   ├─ /product-review
   │     └─ 요구사항 모호 → Step 1.5 grilling 진입
   └─ Go / Go(축소) / Hold / No-Go 판정

[Step 1]  작업 명세         ← 기존
   ├─ /work-plan → REQ-SNAPSHOT, WORK-SPEC, FEATURE-CHECKLIST
   └─ ✅ WORK-SPEC.md 3-1 (AGENT-BRIEF) 섹션 작성됐는가?

[Step 2]  구현              ← 기존 + 도구 안내
   ├─ /work-plan-start → PLAN, ARCHITECTURE, SPEC
   ├─ 🔧 에러 → debugger (Phase 1 재현 loop 우선)
   └─ 🔧 코드 영역 모르겠음 → /zoom-out

[Step 3]  검증              ← 기존
   ├─ /feature-check
   ├─ /self-review → SELF-REVIEW
   ├─ /qa-scenario → QA-SCENARIOS
   └─ /browser-debug

[Step 4]  마무리            ← 기존
   ├─ /pr
   └─ /work-log

[Sticky]  /caveman           ← 어떤 단계에서든 토글
```

### 핵심 변화

- **Step 0 신규**: 작업 시작 전 환경 점검. 가장 흔한 "한참 가다가 막힘" 제거
- **Step 0.5 신규**: 제품 검증 단계 명시. 지금까지 product-review가 워크플로우 외부였음
- **Step 1 보강**: WORK-SPEC.md 품질 체크에 AGENT-BRIEF 섹션 추가
- **Step 2 보강**: 에러/탐색 도구를 가이드에 명시
- **Sticky 도구**: caveman은 단계 무관, 항상 토글 가능

---

## 4. metadata.json 스키마 확장

### 현재
```json
{
  "track_id", "description", "type", "has_ui",
  "status", "created_at", "work_spec_path",
  "branch", "parent_track",
  "total_phases", "current_phase"
}
```

### 추가 후보 필드
```json
{
  // 기존 필드 유지
  "setup_check_at": "2026-04-29T15:30:00+09:00",
       // /setup-til-skills 마지막 실행 시각. 1주 지나면 /track-status가 재실행 권장
  "product_review_decision": "Go" | "Go(축소)" | "Hold" | "No-Go" | null,
       // /product-review 판정 결과. null이면 product-review 미실행
  "grilling_completed": true | false,
       // grilling Step 1.5 진행 여부 (요구사항 모호 → 인터뷰 완료)
  "agent_brief_complete": true | false,
       // WORK-SPEC.md 3-1 (변경 인터페이스 Agent Brief) 섹션 작성 여부
  "hard_dependencies": ["ATLASSIAN_API_TOKEN", "Slack MCP", "mysqlsh"],
       // 이 Track이 요구하는 ADR 0001 분류상의 hard dependency 목록
  "tools_used": ["zoom-out", "caveman", "debugger"]
       // 이번 Track 진행 중 사용한 도구 (post-hoc 기록, 리트로용)
}
```

각 필드는 **선택적**(optional). 누락되면 `/track-status`가 "정보 없음"으로 표시.

---

## 5. `/track-status` 출력 양식 재설계

### Before (현재)
```markdown
## Track: {track_id}

| 항목 | 값 |
|------|------|
| 설명 | ... |
| 상태 | ... |
| 브랜치 | ... |
| Phase | 2/5 |

### 문서 현황
- [x] 1_REQ-SNAPSHOT.md
- [x] 2_WORK-SPEC.md
- [ ] 3_FEATURE-CHECKLIST.md

### 워크플로우 가이드
다음 단계: /work-plan-start 실행
```

### After (재구성)
```markdown
## Track: {track_id}

| 항목 | 값 |
|------|------|
| 설명 | ... |
| 상태 | in_progress |
| 브랜치 | feature/foo |
| 제품 검증 | Go (2026-04-29) |
| Phase | 2/5 |

### 환경 점검 (Hard Dependency)
> ADR 0001 분류 기준. 미충족 시 /setup-til-skills 권장.
- ✅ ATLASSIAN_API_TOKEN
- ❌ Slack MCP → /jira-* 등 차단됨
- ✅ Codex CLI
- (마지막 setup: 2026-04-22, 1주 경과 → 재실행 권장)

### 인프라 문서
- ✅ .claude/CONTEXT.md (도메인 용어집)
- ✅ .claude/docs/adr/0001-skill-dependency-classification.md
- ⚠️ .claude/out-of-scope/ (없음 — 거절 이력 누적 시 권장)

### 워크플로우 단계
[✓] Step 0: 환경 점검 (setup OK, 1주 전)
[✓] Step 0.5: 제품 검증 (Go)
[✓] Step 1: 작업 명세
    └─ ✅ WORK-SPEC 3-1 (Agent Brief) 작성됨
    └─ ✅ FEATURE-CHECKLIST 생성됨
[▶] Step 2: 구현 ← 현재 위치 (Phase 2/5)
[ ] Step 3: 검증
[ ] Step 4: 마무리

### 문서 현황 (번호별)
- [x] 1_REQ-SNAPSHOT.md
- [x] 2_WORK-SPEC.md (3-1 Agent Brief ✅)
- [x] 3_FEATURE-CHECKLIST.md
- [▶] 4_PLAN.md (Phase 2/5 진행 중)
- [ ] 5_ARCHITECTURE.md
- [ ] 6_SPEC.md
- [ ] 7_SELF-REVIEW.md
- [ ] 8_QA-SCENARIOS.md

### 다음 추천 명령
1. **/work-plan-start** — Phase 3 진행
   └ 에러 발생 시: debugger (Phase 1 재현 loop 우선)
   └ 코드 영역 모를 때: /zoom-out

### 사용 가능한 도구 모드
- `/caveman` — 토큰 75% 절약 모드 (단계 무관 토글)
- `/zoom-out` — 한 단계 추상화 맵
- debugger 에이전트 — 5-phase 진단 (재현→최소화→가설→계측→fix)
```

---

## 6. 재구성 실행 계획 (총 ~3시간)

### Phase A: SKILL.md 보강 (1시간)

**대상**: `~/.claude/skills/track-status/SKILL.md` (+ 로컬)

**변경**:
1. "## 작업 절차" 의 단계 4 (Track 상세 조회)에서 표시 항목 확장
2. "환경 점검" 서브섹션 신규 추가 — `setup-state.json` 읽어 Hard dep 충족도 표시
3. "인프라 문서" 서브섹션 신규 추가 — CONTEXT.md, ADR 0001 존재 확인
4. "워크플로우 단계" 출력 양식 추가 (Step 0 ~ Step 4 + Sticky)
5. "다음 추천 명령" 섹션 확장 — debugger / zoom-out / caveman 힌트 포함

**파일 수정 분량**: 기존 113줄 → 약 200줄 예상

### Phase B: metadata.json 스키마 확장 (30분)

**변경**:
1. `/work-plan` 생성 시 `product_review_decision`, `grilling_completed`, `agent_brief_complete` 필드 자동 추가
2. `/setup-til-skills` 실행 시 `setup_check_at` 갱신 (Track별이 아닌 전역이라 `.claude/docs/setup-state.json`에 저장하는 게 맞을 수도)
3. 기존 Track의 metadata.json은 새 필드 누락 → "정보 없음" 표시 (마이그레이션 강제 X)

**고려사항**: `setup_check_at`는 Track 단위가 아닌 환경 단위라 `.claude/docs/setup-state.json` 에 두는 게 더 자연스러움. metadata.json은 Track 자체 정보만.

### Phase C: 시각적 개선 — ASCII 워크플로우 다이어그램 (30분)

**변경**: `/track-status` 출력에 위 4번의 "워크플로우 단계" 다이어그램 항상 표시. 현재 위치 `[▶]` 마커.

```
[✓] Step 0: 환경 점검
[✓] Step 0.5: 제품 검증
[✓] Step 1: 작업 명세
[▶] Step 2: 구현 (Phase 2/5)
[ ] Step 3: 검증
[ ] Step 4: 마무리
```

### Phase D: setup-til-skills와 연계 (30분)

**변경**:
1. `/setup-til-skills` SKILL.md 의 산출물에 `.claude/docs/setup-state.json` 명시 (이미 있음)
2. `/track-status` Step 0이 이 파일을 읽음
3. 1주 이상 지났으면 "재실행 권장" 안내
4. Hard dep 미충족 + Track이 그 dep을 요구하면 빨간색 경고

### Phase E: 기존 Track 마이그레이션 (15분)

**변경**: 기존 Track 1개 (`claude-usage-optimization`)의 metadata.json 에 새 필드 추가 (선택).
실제로는 새 필드는 모두 optional이라 마이그레이션 강제 X.

---

## 7. 우선순위 매트릭스

| 변경 | 효과 | 난이도 | 우선순위 |
|------|------|--------|---------|
| 워크플로우 다이어그램 출력 | 매우 높음 (현재 위치 시각화) | Low (30분) | 🔴 1순위 |
| 환경 점검 섹션 추가 | 높음 (한참 가다 막힘 방지) | Low (30분) | 🔴 2순위 |
| product_review_decision 필드 + 표시 | 높음 ("왜 만드는가" 가시화) | Low (30분) | 🟡 3순위 |
| AGENT-BRIEF 섹션 검증 | 중간 (품질 게이트) | Medium (40분) | 🟡 4순위 |
| 도구 힌트 (zoom-out/caveman/debugger) | 중간 (도구 발견성) | Low (15분) | 🟢 5순위 |
| 인프라 문서 체크 | 낮음 (이미 있으면 표시만) | Low (15분) | 🟢 6순위 |
| metadata.json 신규 필드 마이그레이션 | 낮음 (optional이라 강제 X) | Low (15분) | 🟢 7순위 |

---

## 8. 위험 요소

1. **SKILL.md 비대화** — 113줄 → 200줄 됨. 개별 항목을 reference 파일로 분리해서 progressive disclosure 적용 권장 (Matt Pocock의 write-a-skill 패턴).
2. **기존 Track 호환성** — 새 metadata.json 필드는 모두 optional이라 BC OK. `/track-status` 가 누락 필드를 "정보 없음"으로 처리해야 함.
3. **setup-state.json 누락 시** — 첫 실행 시 파일이 없을 수 있음. `/track-status`가 graceful degrade 해야 함 ("setup 안내" 표시).
4. **product-review 미실행 Track** — 기존 Track은 product_review_decision 필드가 없음. 누락이 정상.

---

## 9. 결론

이번에 추가한 9가지 요소 중 **6가지(/setup-til-skills, /product-review grilling, AGENT-BRIEF, debugger 5-phase, /zoom-out, /caveman)** 가 `/track-status` 워크플로우에 자연스럽게 끼어든다. 나머지 3가지(CONTEXT.md, ADR 0001, .claude 보호 hook)는 워크플로우 외부 인프라지만, **존재 여부 표시**만 추가하면 충분하다.

**가장 큰 효과**:
- 워크플로우 다이어그램 (Step 0~4) 시각화 → 현재 위치 즉시 파악
- 환경 점검 단계 추가 → "한참 가다가 막힘" 시나리오 제거
- product-review를 워크플로우 단계로 정식 편입 → "왜 만드는가" 검증이 더 이상 옵션이 아님

총 작업 시간 **3시간** 정도. Phase A~D를 병렬/순차로 진행하면 한 세션에 끝낼 수 있다.

---

## 참고

- 원본 분석: [`mattpocock-skills-application.md`](./mattpocock-skills-application.md)
- 가이드: [cs/tool/mattpocock-skills-harness.md](../../cs/tool/mattpocock-skills-harness.md)
- 작성일: 2026-04-29
