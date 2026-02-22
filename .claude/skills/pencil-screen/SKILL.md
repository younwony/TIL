---
name: pencil-screen
description: Pencil MCP로 새 화면(Desktop+Mobile)을 디자인할 때 사용
---

# Pencil 새 화면 디자인

## 개요

Pencil MCP 도구를 사용하여 .pen 파일에 새 화면을 디자인한다.
항상 Desktop(1440x900) + Mobile(390x844) 쌍으로 생성한다.

## 사용법

### Step 1: 에디터 상태 확인

```
get_editor_state(include_schema: false)
```

- 현재 열린 .pen 파일 확인
- 사용 가능한 reusable components 목록 확인

### Step 2: 스타일 가이드 확인

```
get_style_guide_tags()
get_style_guide(tags: [...관련_태그])
```

- 디자인 방향 결정에 필요한 경우만 호출

### Step 3: 컴포넌트 구조 파악

```
batch_get(nodeIds: [사용할_컴포넌트_ID], readDepth: 3)
```

- 사용할 컴포넌트의 내부 구조를 파악
- descendants override에 필요한 자식 ID 확인

### Step 4: 빈 공간 찾기

```
find_empty_space_on_canvas(direction: "below", width: 1980, height: 900)
```

- Desktop(1440) + gap(150) + Mobile(390) = 1980px 이상 확보

### Step 5: 프레임 생성 (placeholder)

```
batch_design([
  desktop=I(document, {type: "frame", width: 1440, height: 900, placeholder: true, name: "화면명 - Desktop", x: ..., y: ...}),
  mobile=I(document, {type: "frame", width: 390, height: 844, placeholder: true, name: "화면명 - Mobile", layout: "vertical", x: ..., y: ...})
])
```

- 두 프레임 모두 `placeholder: true` 필수
- Mobile은 `layout: "vertical"` 기본

### Step 6: 화면 구성

- Desktop → Mobile 순서로 작업
- 섹션별로 batch_design 호출 (최대 25 ops/call)
- 기존 컴포넌트는 ref로 재사용, descendants로 커스터마이즈

### Step 7: 검증

```
get_screenshot(nodeId: desktop_id)
get_screenshot(nodeId: mobile_id)
```

- 시각적 검증 후 필요시 수정
- 완료 후 각 프레임에서 `placeholder: false` 해제

```
batch_design([
  U(desktop_id, {placeholder: false}),
  U(mobile_id, {placeholder: false})
])
```

## 입출력

| 입력 | 출력 |
|------|------|
| 화면 이름, 요구사항 | Desktop + Mobile 프레임 쌍 |
| 사용할 컴포넌트 지정 | .pen 파일에 새 화면 추가 |

## 핵심 규칙

- **항상 Desktop + Mobile 쌍** 생성
- **placeholder: true** 작업 중 필수, 완료 후 해제
- **최대 25 ops/call** - 복잡한 화면은 여러 번 호출
- **fill 속성 필수** - 텍스트에 fill 없으면 안 보임
- **fill_container** - 자식 크기는 하드코딩 대신 동적 크기 사용
- **기존 컴포넌트 활용** - 새로 만들기보다 ref + descendants

## 프로젝트 디자인 토큰 (땅뽀 여행)

| 토큰 | 값 |
|------|---|
| 배경 | #FAF8F5 |
| Sage Green | #7C9082 |
| Terracotta | #D4845E |
| 텍스트 | #2D2D2D |
| 뮤트 | #8A8A8A |
| 테두리 | #E8E4DF |
| 사이드바 | #18181b |
| 디스플레이 폰트 | Fraunces |
| 본문 폰트 | Inter |

## 체크리스트

- [ ] Desktop(1440x900) 프레임 생성
- [ ] Mobile(390x844) 프레임 생성
- [ ] placeholder 설정 → 작업 → 해제
- [ ] 스크린샷으로 시각 검증
- [ ] 텍스트에 fill 속성 설정
- [ ] 기존 컴포넌트 ref 활용

## 관련 스킬

- `pencil-update`: 기존 화면 수정
- `pencil-to-code`: 디자인을 코드로 변환
