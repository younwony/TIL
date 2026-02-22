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
  desktop=I(document, {type: "frame", width: 1440, height: 900, placeholder: true, name: "화면명 - Desktop", fill: "#FAF8F5", x: ..., y: ...}),
  mobile=I(document, {type: "frame", width: 390, height: 844, placeholder: true, name: "화면명 - Mobile", fill: "#FAF8F5", layout: "vertical", x: ..., y: ...})
])
```

- 두 프레임 모두 `placeholder: true` 필수
- Mobile은 `layout: "vertical"` 기본
- 배경색 `#FAF8F5` 설정

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

## 프로젝트 컴포넌트 ID 매핑

### 레이아웃 컴포넌트

| 컴포넌트 | Pencil ID | 설명 |
|----------|-----------|------|
| Sidebar | `d5ZTS` | 280px, 다크 배경(#18181b), 로고+메뉴+프로필 |
| Sidebar Section Title | `h12kK` | 섹션 라벨 (예: "메뉴") |
| Sidebar Item Active | `dOLzc` | 활성 네비 아이템 |
| Sidebar Item Default | `X6nwq` | 비활성 네비 아이템 |

### 데이터 표시 컴포넌트

| 컴포넌트 | Pencil ID | 설명 |
|----------|-----------|------|
| Card | `ERkuB` | 기본 카드 (Header/Content/Actions) |
| Card Image | `ksvfk` | 이미지 포함 카드 |
| Progress Bar | `W4YFH` | 진행률 바 (sage green) |
| Label Success | `7KC5U` | 초록 배지 |
| Label Orange | `L8Rgv` | 주황 배지 |

### 입력 컴포넌트

| 컴포넌트 | Pencil ID | 설명 |
|----------|-----------|------|
| Input Group | `gKpi4` | 라벨 + 입력필드 (pill) |
| Select Group | `XhJWF` | 라벨 + 셀렉트 (pill) |
| Textarea Group | `QFzE8` | 라벨 + 텍스트에어리어 |
| Search Box | `T5yK2` | 검색 입력 |

### 액션 컴포넌트

| 컴포넌트 | Pencil ID | 설명 |
|----------|-----------|------|
| Button Primary | `ZETEA` | 주요 버튼 (sage green) |
| Button Outline | `4x7RU` | 외곽선 버튼 |
| Button Secondary | `U83R7` | 보조 버튼 |
| Tabs | `Kbr4h` | 탭 컨테이너 (pill) |
| Tab Active | `KbyBJ` | 활성 탭 |
| Tab Inactive | `BdBJJ` | 비활성 탭 |
| Checkbox Default | `Wxq1C` | 기본 체크박스 |
| Checkbox Checked | `r91nP` | 체크된 체크박스 |

## 프로젝트 디자인 토큰

| 토큰 | 값 | CSS 변수 |
|------|----|---------|
| 배경 | `#FAF8F5` | `--color-bg` |
| Sage Green | `#7C9082` | `--color-sage` |
| Terracotta | `#D4845E` | `--color-terracotta` |
| 텍스트 | `#2D2D2D` | `--color-text` |
| 뮤트 텍스트 | `#8A8A8A` | `--color-text-muted` |
| 테두리 | `#E8E4DF` | `--color-border` |
| 밝은 테두리 | `#F0EDE8` | `--color-border-light` |
| 사이드바 | `#18181b` | `--color-sidebar` |
| 카드 | `#FFFFFF` | `--color-surface` |
| 디스플레이 폰트 | Fraunces | `--font-display` |
| 본문 폰트 | Inter | `--font-body` |

## 핵심 규칙

- **항상 Desktop + Mobile 쌍** 생성
- **placeholder: true** 작업 중 필수, 완료 후 해제
- **최대 25 ops/call** - 복잡한 화면은 여러 번 호출
- **fill 속성 필수** - 텍스트에 fill 없으면 안 보임
- **fill_container** - 자식 크기는 하드코딩 대신 동적 크기 사용
- **기존 컴포넌트 활용** - 새로 만들기보다 ref + descendants

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
