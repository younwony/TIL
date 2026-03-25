---
description: Track 상태 조회 - 현재 진행 중인 작업 추적 현황 + 워크플로우 가이드
allowed-tools: Read, Glob, Grep
---

# Track Status - 작업 추적 현황 조회

`.claude/tracks/` 디렉토리를 스캔하여 Track 상태를 조회하고, 다음 단계를 안내해줘.

## 실행 방법

```
/track-status              # 전체 Track 현황 + 워크플로우 가이드
/track-status {track_id}   # 특정 Track 상세 조회
```

## 1단계: Track 디렉토리 스캔

`.claude/tracks/` 디렉토리 존재 여부를 확인한다.

### Track이 없는 경우 (디렉토리 없음 또는 비어있음)

워크플로우 가이드를 출력한다:

```
## 작업 워크플로우 가이드

현재 진행 중인 Track이 없습니다.

### 전체 흐름

┌────────────┐   ┌─────────────────┐   ┌────────────────────┐   ┌──────────────┐
│ /work-plan  │ → │ /work-plan-start │ → │/test-coverage-check│ → │ /self-review  │
│ 계획 수립   │   │ 구현 실행        │   │ 테스트 커버리지    │   │ + /simplify  │
│ + Track생성 │   │ + Phase 진행     │   │ 점검 + 누락 보완   │   │ 코드 리뷰    │
└────────────┘   └─────────────────┘   └────────────────────┘   └──────┬───────┘
                                                                       ↓
┌────────────┐   ┌──────────┐   ┌──────────────┐   ┌─────────────┐   ┌───────────────┐
│ /work-log  │ ← │   /pr    │ ← │ 사용자 QA 확인│ ← │/browser-debug│ ← │ /qa-scenario  │
│ Confluence │   │ PR 생성  │   │ (수동 검증)   │   │ 브라우저 QA  │   │ QA 시나리오   │
│ 문서화     │   │          │   │              │   │              │   │ 생성          │
└────────────┘   └──────────┘   └──────────────┘   └─────────────┘   └───────────────┘
                       ↑                                                     ↑
                       └── UI 없는 프로젝트(has_ui: false)는 ────────────────┘
                           코드 리뷰 → /pr 직행 (QA 단계 스킵)

### 시작하기

1. 요구사항 파일(req.md)을 준비하세요
2. `/work-plan` 또는 `/work-plan path/to/req.md`로 시작하세요
3. WORK-SPEC.md와 Track이 자동 생성됩니다

### 커맨드 요약

| 단계 | 커맨드 | 설명 |
|------|--------|------|
| 1. 계획 | `/work-plan [path]` | 요구사항 분석 → WORK-SPEC.md + Track 생성 |
| 2. 구현 | `/work-plan-start [path]` | WORK-SPEC.md 기반 코드 구현 |
| 3. 테스트 커버리지 | `/test-coverage-check` | 변경 파일 커버리지 분석 + 누락 테스트 자동 생성 |
| 4. 코드 리뷰 | `/self-review` + `/simplify` | 자체 코드 리뷰 + 코드 품질 개선 |
| 5. QA 시나리오 | `/qa-scenario` | 변경사항 분석 → QA-SCENARIOS.md 생성 |
| 6. 브라우저 QA | `/browser-debug` | Chrome 자동화 QA 실행 |
| 7. (수동 QA) | 사용자 직접 검증 | 실제 환경에서 QA 확인 |
| 8. PR 생성 | `/pr` | 현재 브랜치 PR 자동 생성 |
| 9. 문서화 | `/work-log` | Confluence 작업 문서화 |
| 상태 확인 | `/track-status` | 현재 진행 상황 조회 (지금 이 명령) |
```

---

### Track이 있는 경우

하위의 모든 `metadata.json`을 읽는다.

## 2단계: 진행률 계산

각 Track의 `*_PLAN.md` (예: `4_PLAN.md`)를 읽어 진행률을 계산한다:

```
진행률 = (체크된 항목 [x] 수) / (전체 체크박스 수) × 100%
```

- `[ ]`: 미완료
- `[~]`: 진행 중
- `[x]`: 완료

현재 Phase와 다음 Task도 파악한다:
- 현재 Phase: `[~]` 또는 첫 번째 `[ ]`가 있는 Phase
- 다음 Task: 현재 Phase에서 첫 번째 `[ ]` 항목

## 3단계: 전체 현황 + 다음 단계 안내

### 전체 조회 (`/track-status`)

```
## Track 현황

### Active
| Track ID | 설명 | 타입 | 생성일 | 진행률 | 현재 Phase |
|----------|------|------|--------|--------|-----------|
| {id} | {desc} | {type} | {date} | {N}% ({done}/{total}) | Phase {N}: {name} |

### 다음 단계
{현재 Phase에 따라 안내 메시지 출력 - 아래 규칙 참조}

### Completed
| Track ID | 설명 | 타입 | 생성일 | 완료일 |
|----------|------|------|--------|--------|
| {id} | {desc} | {type} | {date} | {date} |

### 요약
- Active: {N}개
- Completed: {N}개
- Archived: {N}개
```

### 다음 단계 안내 규칙

현재 Phase와 Track 상태에 따라 적절한 안내를 출력한다:

| 현재 상태 | has_ui | 다음 단계 안내 |
|----------|--------|--------------|
| status: `new` | - | → `/work-plan-start`로 구현을 시작하세요 |
| 구현 Phase 진행 중 | - | → 현재 Task: {task명}. `/work-plan-start`를 계속 실행하세요 |
| 구현 완료, 테스트 커버리지 미시작 | - | → 구현이 완료되었습니다. `/test-coverage-check`로 테스트 커버리지를 점검하세요 |
| 테스트 커버리지 완료, 코드 리뷰 미시작 | - | → 테스트 통과. `/self-review`로 코드 리뷰를 진행하세요 |
| 코드 리뷰 완료 | `true` | → 코드 리뷰 완료. `/qa-scenario`로 QA 시나리오를 생성하세요 |
| 코드 리뷰 완료 | `false` | → 코드 리뷰 완료. `/pr`로 PR을 생성하세요 (UI 없는 프로젝트) |
| QA 시나리오 생성 완료 | `true` | → QA 시나리오가 준비되었습니다. `/browser-debug`로 브라우저 QA를 실행하세요 |
| 브라우저 QA 완료, 사용자 QA 대기 중 | `true` | → 수동 QA를 완료한 후 `/pr`로 PR을 생성하세요 |
| PR 생성 완료, 문서화 미완료 | - | → `/work-log`로 Confluence 작업 문서를 작성하세요 |
| 모든 Phase 완료 | - | → 모든 작업이 완료되었습니다! |

> `has_ui` 판별: metadata.json의 `has_ui` 필드를 확인한다. 필드가 없으면 `type`으로 추론한다 (frontend → true, 그 외 → false).

### 상세 조회 (`/track-status {track_id}`)

`$ARGUMENTS`가 있으면 해당 Track의 상세 정보를 출력한다:

```
## Track: {track_id}

### 메타데이터
- 설명: {description}
- 타입: {type}
- 상태: {status}
- 생성일: {created_at}
- WORK-SPEC: {work_spec_path}

### Phase 진행 상황

#### Phase 1: {Phase명} ✅
- [x] Task: {완료된 Task}
- [x] **Checkpoint**: Phase 1 검증 (verified)

#### Phase 2: {Phase명} 🔄
- [x] Task: {완료된 Task}
- [~] Task: {진행 중 Task} ← 현재
- [ ] Task: {미완료 Task}
- [ ] **Checkpoint**: Phase 2 검증

#### Phase 3: {Phase명} ⏳
- [ ] Task: {미완료 Task}
- [ ] **Checkpoint**: Phase 3 검증

### 진행률: {N}% ({done}/{total} tasks)

### 다음 단계
{다음 단계 안내 규칙에 따른 메시지}
```

상태 아이콘:
- ✅: 완료된 Phase
- 🔄: 진행 중인 Phase
- ⏳: 대기 중인 Phase

## 주의사항

- `.claude/tracks/` 디렉토리가 없으면 워크플로우 가이드를 출력
- metadata.json이 깨져있으면 해당 Track은 "⚠️ 메타데이터 오류"로 표시
- plan.md가 없으면 진행률을 "N/A"로 표시
- 다음 단계 안내는 항상 출력한다 (사용자가 다음에 뭘 해야 하는지 명확하게)
