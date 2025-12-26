# TDD (테스트 주도 개발)

> `[3] 중급` · 선수 지식: [OOP](./oop.md), [클린 코드](./clean-code.md)

> 테스트를 먼저 작성하고, 테스트를 통과하는 코드를 작성하는 개발 방법론

`#TDD` `#TestDrivenDevelopment` `#테스트주도개발` `#RedGreenRefactor` `#단위테스트` `#UnitTest` `#테스트코드` `#TestCode` `#리팩토링` `#Refactoring` `#JUnit` `#Mockito` `#테스트커버리지` `#TestCoverage` `#애자일` `#Agile` `#XP` `#ExtremeProgramming` `#Assert` `#Given-When-Then` `#AAA패턴`

## 왜 알아야 하는가?

TDD는 코드 품질과 설계를 개선하는 강력한 방법론입니다. 버그를 조기에 발견하고, 리팩토링을 안전하게 하며, 문서 역할을 합니다. 많은 기업에서 TDD 경험을 요구하며, 면접에서도 자주 출제됩니다.

## 핵심 개념

- **Red-Green-Refactor**: TDD의 3단계 사이클
- **단위 테스트**: 작은 단위(함수/메서드)를 검증
- **테스트 더블**: Mock, Stub, Fake 등 대역 객체
- **테스트 커버리지**: 테스트가 검증하는 코드 비율

## 쉽게 이해하기

**TDD**를 건물 설계에 비유할 수 있습니다.

일반 개발: 건물 완성 후 안전 검사
TDD: 안전 기준 정의 → 기준 통과하도록 설계 → 개선

- **Red**: "이 층은 1톤을 견뎌야 한다" (요구사항 = 실패하는 테스트)
- **Green**: 1톤 견디는 최소 설계 (테스트 통과)
- **Refactor**: 더 효율적인 구조로 개선 (테스트 유지하며 개선)

## 상세 설명

### Red-Green-Refactor 사이클

```
     ┌──────────────────────────────────────────────┐
     │                                              │
     │    ┌─────────┐                               │
     │    │   Red   │ ◄── 실패하는 테스트 작성       │
     │    └────┬────┘                               │
     │         │                                    │
     │         ▼                                    │
     │    ┌─────────┐                               │
     │    │  Green  │ ◄── 테스트 통과하는 코드 작성  │
     │    └────┬────┘                               │
     │         │                                    │
     │         ▼                                    │
     │    ┌──────────┐                              │
     └────┤ Refactor │ ◄── 코드 개선 (테스트 유지)   │
          └──────────┘                              │
```

### TDD 예시: 계산기

**1단계 (Red): 실패하는 테스트 작성**

```java
@Test
void 두_수를_더한다() {
    Calculator calc = new Calculator();
    int result = calc.add(2, 3);
    assertEquals(5, result);  // 아직 구현 안 됨 → 실패
}
```

**2단계 (Green): 테스트 통과하는 최소 코드**

```java
class Calculator {
    int add(int a, int b) {
        return 5;  // 하드코딩으로 일단 통과
    }
}
```

**다른 케이스 추가 (Red)**

```java
@Test
void 다른_두_수를_더한다() {
    Calculator calc = new Calculator();
    assertEquals(10, calc.add(4, 6));  // 실패
}
```

**일반화 (Green + Refactor)**

```java
class Calculator {
    int add(int a, int b) {
        return a + b;  // 일반화
    }
}
```

### 테스트 구조 패턴

**AAA (Arrange-Act-Assert)**:

```java
@Test
void 재고가_있으면_주문_성공() {
    // Arrange (준비)
    Product product = new Product("A", 10);
    OrderService service = new OrderService();

    // Act (실행)
    Order order = service.createOrder(product, 5);

    // Assert (검증)
    assertNotNull(order);
    assertEquals(5, product.getStock());
}
```

**Given-When-Then** (BDD 스타일):

```java
@Test
void 재고가_있으면_주문_성공() {
    // Given
    Product product = new Product("A", 10);
    OrderService service = new OrderService();

    // When
    Order order = service.createOrder(product, 5);

    // Then
    assertNotNull(order);
    assertEquals(5, product.getStock());
}
```

### 테스트 더블 (Test Double)

| 종류 | 설명 | 사용 시점 |
|------|------|----------|
| Dummy | 파라미터 채우기용 | 실제 사용 안 함 |
| Stub | 미리 정해진 값 반환 | 특정 응답 필요 |
| Spy | 호출 기록 | 호출 여부 확인 |
| Mock | 기대 행동 검증 | 상호작용 검증 |
| Fake | 간단한 구현체 | 실제 구현 대체 |

**Mockito 예시**:

```java
@Test
void 이메일_전송_성공() {
    // Mock 생성
    EmailService emailMock = mock(EmailService.class);
    UserService userService = new UserService(emailMock);

    // Stub 설정
    when(emailMock.send(anyString())).thenReturn(true);

    // 실행
    boolean result = userService.register("test@test.com");

    // Mock 검증
    verify(emailMock).send("test@test.com");
    assertTrue(result);
}
```

### 좋은 테스트의 특징 (FIRST)

| 원칙 | 의미 |
|------|------|
| **F**ast | 빠르게 실행 |
| **I**ndependent | 테스트 간 독립적 |
| **R**epeatable | 반복 실행해도 같은 결과 |
| **S**elf-validating | 자동 판정 (성공/실패) |
| **T**imely | 적시에 작성 (코드 전) |

### TDD의 장단점

| 장점 | 단점 |
|------|------|
| 버그 조기 발견 | 초기 개발 시간 증가 |
| 리팩토링 안전망 | 학습 곡선 |
| 설계 개선 유도 | 테스트 유지보수 비용 |
| 문서 역할 | 레거시 적용 어려움 |

## 트레이드오프

| 항목 | 고려 사항 |
|------|----------|
| 커버리지 목표 | 100%가 항상 좋은 건 아님 |
| 테스트 범위 | 단위 vs 통합 vs E2E 균형 |
| Mock 사용 | 과도한 Mock은 구현에 결합 |

## 면접 예상 질문

### Q: TDD의 장점은 무엇인가요?

A: (1) **버그 조기 발견**: 코드 작성 전 테스트로 요구사항 명확화 (2) **리팩토링 안전망**: 테스트가 있으면 안심하고 개선 가능 (3) **설계 개선**: 테스트하기 쉬운 코드 = 좋은 설계 (의존성 주입, 단일 책임) (4) **문서 역할**: 테스트가 사용법을 보여줌. **핵심**: TDD는 테스트보다 설계 기법에 가깝습니다.

### Q: Mock과 Stub의 차이는?

A: **Stub**은 미리 정해진 응답을 반환하여 테스트 환경을 구성합니다 (상태 검증). **Mock**은 특정 메서드가 호출되었는지, 어떤 파라미터로 호출되었는지를 검증합니다 (행동 검증). **선택 기준**: 결과 값이 중요하면 Stub, 상호작용이 중요하면 Mock.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [클린 코드](./clean-code.md) | 코드 품질 | [3] 중급 |
| [리팩토링](./refactoring.md) | 코드 개선 | [3] 중급 |
| [OOP](./oop.md) | 선수 지식 | [2] 입문 |

## 참고 자료

- Test-Driven Development: By Example - Kent Beck
- Clean Code - Robert C. Martin
