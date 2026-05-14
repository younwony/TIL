# Agent Brief 양식

AFK 에이전트가 픽업 가능한 작업 명세. 사람 컨텍스트 없이도 끝까지 갈 수 있어야 한다.

> 참조: 이 양식은 WORK-SPEC.md의 "3-1. 변경 인터페이스" 섹션과 호환된다.

## 본문 구조

```markdown
> *이 브리프는 triage 중 AI가 생성했습니다.*

## 무엇을 만드는가 (What to build)

이 작업의 end-to-end 동작을 한 문단으로 설명. 레이어별 구현 X.

## 변경 인터페이스 (Interface changes)

file path가 아니라 **인터페이스 레벨**로 명시:

- ❌ "src/main/java/.../FooService.java:142 라인 수정"
- ✅ "`FooService.create(...)` 시그니처에 `String tenantId` 추가. `FooNotFoundException` 추가. 호출자는 모두 `tenantId`를 전달해야 함."

목록:
- `{ModuleName}.{method}(...)` — 변경 내용 / 추가된 파라미터 / 새 예외
- ...

## 수용 기준 (Acceptance criteria)

- [ ] 기준 1
- [ ] 기준 2
- [ ] 기준 3

## 차단 요소 (Blocked by)

- {다른 이슈 #N — 이게 먼저 끝나야 함}

또는 "차단 요소 없음 — 즉시 시작 가능"

## 컨텍스트 (Context)

- 관련 CONTEXT.md 용어: ...
- 관련 ADR: 0001, 0003
- 도메인 노트: ...
```

## 작성 규칙

1. **file path / line number 금지** (인터페이스 레벨로). 파일은 곧 변하지만 인터페이스는 결정 그 자체.
2. **수용 기준은 동작 기준**. "X가 동작해야 한다" / "Y 에러가 발생하지 않는다" 같은. 구현 디테일이 들어가면 X.
3. **차단 요소 명확히**. 의존성 그래프가 정확해야 vertical slice가 가능.
4. **prototype 산출물이 있다면** — state machine, reducer, schema, type shape — 그대로 본문에 inlin할 수 있다. 단 데모 코드 X, 결정-rich한 부분만.

## AFK 가능성 체크

브리프 작성 후 다음 질문에 모두 Yes여야 `ready-for-agent`:

- [ ] 추가 사람 결정 없이 끝낼 수 있는가?
- [ ] 외부 접근(프로덕션, 비밀번호 등)이 필요한가? (필요하면 ready-for-human)
- [ ] 매뉴얼 테스트가 결정적으로 필요한가? (필요하면 ready-for-human)
- [ ] 인터페이스가 명확히 정의되어 있는가?

하나라도 No면 `ready-for-human` 또는 `needs-info`로 재분류.
