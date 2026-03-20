---
description: Track 상태 조회 - 현재 진행 중인 작업 추적 현황
allowed-tools: Read, Glob, Grep
---

# Track Status - 작업 추적 현황 조회

`.claude/tracks/` 디렉토리를 스캔하여 모든 Track의 상태를 테이블로 출력해줘.

## 실행 방법

```
/track-status              # 전체 Track 현황
/track-status {track_id}   # 특정 Track 상세 조회
```

## 1단계: Track 디렉토리 스캔

`.claude/tracks/` 디렉토리가 없으면:
```
Track이 없습니다. `/work-plan`으로 작업 명세서를 먼저 생성하세요.
```

디렉토리가 있으면 하위의 모든 `metadata.json`을 읽는다.

## 2단계: 진행률 계산

각 Track의 `plan.md`를 읽어 진행률을 계산한다:

```
진행률 = (체크된 항목 [x] 수) / (전체 체크박스 수) × 100%
```

- `[ ]`: 미완료
- `[~]`: 진행 중
- `[x]`: 완료

현재 Phase와 다음 Task도 파악한다:
- 현재 Phase: `[~]` 또는 첫 번째 `[ ]`가 있는 Phase
- 다음 Task: 현재 Phase에서 첫 번째 `[ ]` 항목

## 3단계: 전체 현황 출력

### 전체 조회 (`/track-status`)

```
## Track 현황

### Active
| Track ID | 설명 | 타입 | 생성일 | 진행률 | 현재 Phase |
|----------|------|------|--------|--------|-----------|
| {id} | {desc} | {type} | {date} | {N}% ({done}/{total}) | Phase {N}: {name} |

### Completed
| Track ID | 설명 | 타입 | 생성일 | 완료일 |
|----------|------|------|--------|--------|
| {id} | {desc} | {type} | {date} | {date} |

### 요약
- Active: {N}개
- Completed: {N}개
- Archived: {N}개
```

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
```

상태 아이콘:
- ✅: 완료된 Phase
- 🔄: 진행 중인 Phase
- ⏳: 대기 중인 Phase

## 주의사항

- `.claude/tracks/` 디렉토리가 없으면 안내 메시지만 출력
- metadata.json이 깨져있으면 해당 Track은 "⚠️ 메타데이터 오류"로 표시
- plan.md가 없으면 진행률을 "N/A"로 표시
