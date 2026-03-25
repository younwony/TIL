---
paths:
  - "**/*.java"
  - "**/*.kt"
---

# Java/Kotlin 코드 컨벤션

## 기능 구현 원칙

- 사용하지 않는 기능은 반드시 제거한다 (구현 X)
- 반드시 필요하다고 판단되었을 경우 주석으로 사용처를 남기고 제거하지 않는다
- 기본으로 사용하지 않는 메소드나 기능은 생성하지 않는다
- 요청된 기능에 필요한 최소한의 코드만 작성한다
- 전체적인 구현의 톤앤매너는 프로젝트 구성과 동일하게 진행

## 프론트엔드 (JavaScript/CSS)

### 인라인 스타일 금지
- `element.style.cssText` 또는 인라인 `style` 속성으로 스타일을 직접 지정하지 않는다
- 해당 페이지의 CSS 파일에 클래스를 정의하고, `className`으로 참조한다

## API 설계 원칙

- 기존 API가 새로운 목적에 부합하지 않으면 **새 API를 생성**한다 (기존 API 무리하게 확장 금지)
- 기존 API의 사용 목적을 충분히 파악한 후, 부합하지 않거나 효율성이 떨어지면 새 API를 만든다

## Exception 처리 원칙

- Exception 분기는 **Controller 레이어의 `@ExceptionHandler`에서** 처리한다 (Service에서 분기 X)
- Custom Exception은 `RuntimeException`을 상속하여 정의하고, Controller에서 `@ExceptionHandler`로 처리
- `@ControllerAdvice`는 공통 예외 처리용으로만 사용 (개별 Controller에서 먼저 처리 시도)

## 설계 원칙

- SOLID, 일급 컬렉션(불변), 디자인 패턴(Strategy/Factory/Builder/Template) 적용
- TDD 필수, 매직 넘버/문자열 금지, SRP/DRY 준수

## 객체지향 리팩토링 원칙

### 메소드 구조
- **한 메서드는 한 가지 일만** 수행 (SRP의 메서드 레벨 적용)
- public 메서드에는 **의도만 담고**, 내부 로직은 private 메서드로 분리
  - 예: `return sum(toInts(split(text)));` — public은 흐름만, 각 단계는 private
- 한 메서드에 **들여쓰기(indent) 2단계 이상 금지** → 내부 로직을 private 메서드로 분리
- `else` 사용 금지 → **Early Return 패턴** 적용 (조건 참이면 즉시 반환)
- 메서드 파라미터 **최대 5개**, 초과 시 객체로 묶기
- 메서드 길이 **15줄 이하** 권장 (최대 80줄)

### 클래스 구조
- 클래스 길이 **500줄 이하** 권장 (50줄 이하가 이상적)
- 인스턴스 변수는 **최소화** (서비스/레포지토리 제외)
- 패키지당 파일 **10개 이하** 권장

### 캡슐화
- 원시 값과 문자열은 **의미 있는 객체로 포장** (예: `int age` → `Age` 클래스)
- **일급 컬렉션**: Collection을 감싸는 별도 클래스 생성, 다른 멤버 변수 없이 컬렉션만 보유
  - 도메인 로직을 컬렉션 클래스 안에 캡슐화 (검증, 필터, 집계 등)
  - `remove()`, `clear()` 등 불필요한 메서드 노출 방지 → 불변으로 유지
  - 예: `List<Order>` → `Orders` 클래스, `Orders.totalPrice()` 메서드 제공
- **디미터 법칙**: 한 줄에 점(`.`) 하나만 — `user.getMoney().getValue()` → `user.hasMoney(amount)`
- **Tell, Don't Ask**: getter로 꺼내서 판단하지 말고, 객체에게 행위를 시킨다
  - `order.getShippingInfo().setAddress(new)` → `order.changeShippingInfo(newInfo)`
- setter 사용 금지 → 의미 있는 메서드명으로 상태 변경 (예: `setStatus()` → `activate()`)

### 네이밍
- 축약 금지 — 의도가 분명한 이름 사용 (축약은 여러 책임의 신호)
- `switch` fall-through 금지 — 명시적 `break`/`return` 필수
- 와일드카드 import (`*`) 금지 — 명시적 import 사용
- public API에 **Javadoc 필수**

## 금지 항목 (위반 시 즉시 수정)

- `@Data` → `@Getter` + `@NoArgsConstructor(access = PROTECTED)` + `@Builder`
- `System.out.println()` → `SLF4J` 로거
- `str != null && !str.isEmpty()` → `StringUtils.hasText()`
- `Optional`을 필드/파라미터로 사용 → 반환 타입으로만, `orElseThrow()` 권장
- 매직 넘버/문자열 → `static final` 상수 또는 `Enum`
- `if-else` 3개 이상 → `switch expression` 또는 `Enum`/`Map` 다형성
- 최상위 `Exception` catch → 구체적 예외 처리, Custom Exception 정의
- `Entity` API 직접 노출 → `RequestDTO`/`ResponseDTO` 분리, `record` 권장
- `FetchType.EAGER` → 모든 연관관계 `LAZY` 필수
- 예외 삼키기(swallow exception) 금지

## 성능 위험 지역

- `Pattern`, `ObjectMapper` 등 고비용 객체 → `static final` 캐싱
- 반복문 내 `String +` → `StringBuilder`
- 반복문 내 DB/API 호출 → Bulk 연산
- 컬렉션 조회 → `Fetch Join` 또는 `EntityGraph` (N+1 방지)
- 조회 전용 메소드 → `@Transactional(readOnly = true)`
