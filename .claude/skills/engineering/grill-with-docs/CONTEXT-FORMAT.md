# CONTEXT.md 작성 형식

`CONTEXT.md`는 프로젝트의 **순수 용어집**(ubiquitous language)이다. spec도, scratchpad도, 구현 결정 저장소도 아니다.

## 구조

```markdown
# {프로젝트 이름} Context — Ubiquitous Language

> 이 파일은 이 저장소의 도메인 용어집이다. 매 세션마다 같은 용어를 다시 설명하지 않도록, 프로젝트 고유 개념만 정의한다.
>
> 일반 프로그래밍 용어(timeout, retry 등)는 포함하지 않는다.

---

## Language

### {도메인 카테고리 1, 예: 작업 추적}

**{용어}**
{한 단어 또는 한 문장 정의}
_Avoid_: {피해야 할 동의어}

### {도메인 카테고리 2}
...

## Relationships

- 한 **A** → N개의 **B**
- **B**는 항상 하나의 **C**를 가진다

## Flagged ambiguities

- **"X"** 가 두 가지를 가리킴: A / B → 항상 풀네임으로 구분
```

## 작성 규칙

1. **한 줄 또는 한 문단으로 정의**. 길어지면 별도 문서로 분리.
2. **`_Avoid_:`** 섹션으로 피해야 할 동의어를 명시. ubiquitous language의 핵심은 "같은 개념엔 한 단어".
3. **구현 디테일 금지**. "Foo는 Bar 테이블에 저장된다" 같은 건 ARCHITECTURE.html로.
4. **새 용어가 등장하면 즉시 추가** — 일괄 배치 금지.
5. **모호한 용어는 Flagged ambiguities 섹션**에 등록. 추후 결정될 때까지 둠.

## Before/After 예시

**Before** (장황):
> "한 강의(course)의 한 섹션(section) 안에 있는 한 레슨(lesson)이 '실재화'(real)된다 — 즉 파일 시스템의 자리를 할당받는다 — 그때 문제가 생긴다"

**After** (단어 1개):
> "materialization cascade에 문제가 있다"

`materialization`을 CONTEXT.md에 한 줄 정의해두면 이 단어 하나가 7단어를 대체한다.

## 갱신 트리거

- `/grill-with-docs` 세션 중 새 용어가 풀릴 때
- `/work-plan` 실행 시 req.md에 반복되는 고유명사 발견 시
- 코드 리뷰 중 "이 변수명 헷갈린다" 피드백
- 새 ADR을 작성하다 정의가 흩어진 용어 발견 시
