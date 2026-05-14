# Architecture Language

본 skill이 제안에서 **정확히** 사용하는 아키텍처 어휘. "component", "service", "boundary" 등으로 drift 금지.

> 출처: John Ousterhout, *A Philosophy of Software Design* + Matt Pocock의 적용.

## 어휘

### Module
인터페이스 + 구현이 있는 것. 함수, 클래스, 패키지, 슬라이스 모두 module. 단위 크기 무관.

### Interface
caller가 모듈을 쓰기 위해 알아야 할 **모든 것**. 타입 시그니처만이 아님:

- 타입
- 불변식 (invariants)
- 에러 모드
- 호출 순서 / 라이프사이클
- config / 환경 의존성

좋은 인터페이스 = 위 항목들이 모두 작고 명확.

### Implementation
모듈 안 코드. caller가 알 필요 없는 모든 것.

### Depth
인터페이스의 leverage 비율.

- **Deep** = 작은 인터페이스 뒤에 큰 행동. 고 leverage.
- **Shallow** = 인터페이스가 구현만큼 복잡. caller가 모듈을 통해 얻는 게 거의 없음.

목표는 **deep**.

### Seam
인터페이스가 사는 곳. 행동을 in-place 코드 수정 없이 바꿀 수 있는 지점.

**"boundary" 대신 이 단어 사용.** boundary는 도메인 경계도 의미해서 모호.

### Adapter
seam에서 인터페이스를 만족하는 구체.

### Leverage
depth로 **caller**가 얻는 것. 작은 호출 하나로 큰 일이 일어남.

### Locality
depth로 **maintainer**가 얻는 것. 변화/버그/지식이 한 곳에 모임. 한 모듈을 이해하면 그 부분 전체가 이해됨.

## 핵심 원칙

### 1. Deletion test

shallow 의심 시 적용:

> 이 모듈을 지운다고 상상하라. 복잡도가 사라지면 pass-through였다 (= shallow, 제거 대상). 복잡도가 N개 caller에 다시 나타나면 그건 제 몫을 했다 (= deep, 유지).

### 2. 인터페이스가 테스트 표면이다

좋은 테스트는 인터페이스를 통해 동작을 검증한다. 구현 디테일을 mock하지 말 것. 구현이 바뀌어도 테스트가 살아남아야 한다.

### 3. Adapter 1개 = 가설 seam. Adapter 2개 = 진짜 seam.

추상화는 한 곳에서만 쓰면 의심하라. "유연성을 위해" 만든 인터페이스가 실제로 한 구현만 가진다면 그건 leverage가 없는 추상화.

### 4. Reduce information leakage

같은 정보가 여러 모듈에 흩어지면 shallow의 신호. 한 모듈만 알고 있어야 할 디테일이 다른 모듈로 새어 나간다면 인터페이스가 잘못 그어져 있다.

### 5. Pass-through 모듈 회피

caller로부터 받은 데이터를 그대로 다른 모듈에 넘기기만 하는 모듈은 거의 항상 shallow. 합치는 게 낫다.

## 안티 패턴

- **Manager / Helper / Util** 이름의 모듈 — 책임이 불분명하다는 신호
- **테스트성을 위해 추출된 pure function** — locality 없음. 실제 버그는 호출 컨텍스트에 있는데 테스트는 호출을 못 봄.
- **여러 caller에 의해 다르게 해석되는 인터페이스** — 인터페이스가 모호함
- **에러 핸들링이 caller마다 다른 모듈** — 에러 모드가 인터페이스의 일부인데 빠짐

## 사용 시 검증 질문

후보 제시 전 모두 만족하는지 자체 체크:

- [ ] 도메인 용어는 CONTEXT.md vocabulary?
- [ ] 아키텍처 용어는 본 LANGUAGE.md vocabulary?
- [ ] Deletion test 적용? (shallow 후보 모두)
- [ ] ADR 충돌 확인?
- [ ] locality + leverage로 이익 표현?
- [ ] "테스트가 어떻게 개선될지" 명시?
