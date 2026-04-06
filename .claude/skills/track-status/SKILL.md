---
name: track-status
description: |
  Track 작업 추적 현황을 조회하고 워크플로우 가이드를 제공한다.
  "/track-status", "트랙 상태", "작업 추적", "track 확인", "진행 상태" 요청에 트리거된다.
  같은 Jira 번호에 여러 sub-track이 있으면 목록을 표시하고 선택할 수 있다.
---

# Track Status Skill

`.claude/tracks/` 하위의 Track 상태를 조회하고, 현재 진행 중인 작업의 워크플로우 가이드를 제공한다.

## 실행 방법

```
/track-status                    # 전체 Track 목록 + 상태 요약
/track-status TECH-22386         # 해당 Jira 번호의 모든 sub-track 표시
/track-status TECH-22386-shipment  # 특정 Track 상세 조회
```

---

## 작업 절차

### 1. Track 목록 조회

`.claude/tracks/index.md`를 읽어 전체 Track 목록을 파악한다.

### 2. 인자 분석

- **인자 없음**: 전체 Track 목록 표시 (Active / Completed / Archived)
- **Jira 번호만** (예: `TECH-22386`): 해당 Jira의 모든 sub-track을 필터링하여 표시
- **전체 Track ID** (예: `TECH-22386-shipment`): 해당 Track의 상세 정보 표시

### 3. Jira 번호로 조회 시 — 다중 Track 선택

같은 Jira 번호에 여러 Track이 있으면 AskUserQuestion으로 선택을 요청한다.

```
TECH-22386 관련 Track이 3��� 있습니다:

1. TECH-22386 (completed) — 캠페인 인플루언서 SKU 다중 할당
2. TECH-22386-shipment (in_progress) — V3 캠페인 다중 SKU 출고/배송 관리 보완
3. TECH-22386-shipment-log-sync (in_progress) — V3 배송 로�� 동기화

어떤 Track의 상세 정보를 확인할��요?
```

### 4. Track 상세 조회

선택된 Track의 `metadata.json`을 읽고 다음 정보를 표시한다:

```
## Track: {track_id}

| 항목 | 값 |
|------|------|
| 설명 | {description} |
| 상태 | {status} |
| 브랜치 | {branch} |
| 생성일 | {created_at} |
| Phase | {current_phase} / {total_phases} |
| 상위 Track | {parent_track} |

### 문서 현황
{DOC_DIR 내 파일 목록 + 존재 여부 체크}

### 워크플로우 가이드
현재 단계에 따른 다음 실행 가능한 커맨드 안내:
- WORK-SPEC.md 없음 → `/work-plan` 실행 안���
- WORK-SPEC.md 있고 PLAN.md 없음 → `/work-plan-start` 실행 안내
- PLAN.md 있고 미완료 Phase 있음 → 현재 Phase 진행 안내
- 모든 Phase 완료 → `/self-review` 또는 `/pr` 안내
```

### 5. Track 상태 변경 (선택적)

사용자가 Track 상태를 변경하고 싶으면:
- `in_progress` → `completed`: metadata.json 업데이트 + index.md에서 Active → Completed로 이동
- `completed` → `archived`: metadata.json 업데이트 + index.md에서 Completed → Archived로 이동

---

## 출력 형식

### 전체 목록 모드

```
## Track 현황

### Active (N개)
| Track ID | 설명 | Phase | 생성일 |
|----------|------|-------|--------|
| {id} | {desc} | {phase} | {date} |

### Completed (N개)
| Track ID | 설명 | 완료일 |
|----------|------|--------|
| {id} | {desc} | {date} |

### Archived (N개)
(없음)
```

### 상세 조회 모드

```
## Track: {track_id}
{상세 정보 테이블}
{문서 현황}
{���크플로우 가이드}
```
