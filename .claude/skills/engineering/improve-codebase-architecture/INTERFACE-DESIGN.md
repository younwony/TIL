# Interface Design

deepened 모듈의 대안 인터페이스를 탐색할 때 사용하는 가이드.

## 진행 방식

후보가 선정되고 grilling이 어느 지점까지 갔을 때, 사용자에게 묻기:

> "이 deepened 모듈의 인터페이스를 몇 가지 다른 모양으로 그려볼까요? 결정 전에 비교해보면 좋습니다."

같은 책임에 대해 **2~3개의 인터페이스 변형**을 그려서 비교. 각 변형은:

1. 함수/메서드 시그니처
2. 타입
3. 에러 모드
4. 호출 순서 (필요 시 sequence diagram)
5. config / 환경 의존성

## 변형 차원

다음 차원으로 변형을 만들어본다:

### 동기 vs 비동기
- 동기 인터페이스 (Promise/Future 미반환)
- 비동기 인터페이스 (Promise/Future 반환)
- 양쪽 다 지원하는 인터페이스 (보통 안티 패턴)

### 단일 호출 vs 멀티 스텝
- 한 번 호출로 끝 (`do(arg)`)
- 명시적 라이프사이클 (`init()` → `step()` → `commit()`)

### 상태 보유 vs 비보유
- pure function (인풋만으로 결정됨)
- 상태를 가진 객체 (mutable / immutable)

### Throw vs Result type
- 에러를 throw
- 명시적 Result<Ok, Err> 반환

### Builder vs 단일 생성자
- `new Foo(a, b, c, d, e)`
- `Foo.builder().withA(a).withB(b).build()`

## 비교 기준

각 변형에 대해 점수 매기지 말고 **trade-off**를 명시:

- **Caller 부담** — 호출자가 알아야 할 것이 얼마나 많은가
- **유연성** — 새 요구가 왔을 때 인터페이스를 깨뜨려야 하나
- **테스트 용이성** — 어떤 테스트가 쉽고 어떤 게 어려운가
- **에러 처리** — 에러 발생 시 caller가 무엇을 해야 하나
- **확장성** — adapter를 추가할 수 있나

## ADR로 기록할 시점

인터페이스를 골랐다면 다음 중 하나에 해당하면 ADR:

- 골랐던 변형이 명백하지 않다 (real trade-off)
- 미래에 다른 인터페이스로 마음 바꾸기 어렵다 (hard to reverse)
- 코드만 봐서는 왜 이 모양인지 모를 것 같다 (surprising without context)

3개 모두 충족 시 [ADR-FORMAT.md](../grill-with-docs/ADR-FORMAT.md)로 기록.

## 안티 패턴

- **모든 변형을 다 지원하는 인터페이스**. "일단 다 가능하게 해두자"는 leverage를 잃는 길.
- **caller 1개를 기준으로만 그리기**. 두 번째 adapter를 가정하고 그릴 것. Adapter 1개 = 가설 seam.
- **타입만 보고 인터페이스가 작아 보이는 것**. 불변식, 에러 모드, 라이프사이클을 빼먹으면 진짜 인터페이스는 더 큼.
