# Test Double (테스트 대역)

> `[3] 중급` · 선수 지식: [TDD](./tdd.md), [OOP](./oop.md)

> 테스트에서 실제 의존 객체를 대신하는 가짜 객체의 총칭

`#TestDouble` `#테스트대역` `#Mock` `#Stub` `#Spy` `#Fake` `#Dummy` `#Mocking` `#UnitTest` `#Mockito`

## 왜 알아야 하는가?

Test Double은 **단위 테스트의 핵심 기법**입니다. 외부 시스템(DB, API, 파일)에 의존하지 않고 빠르고 안정적인 테스트를 작성할 수 있게 해줍니다. 모든 테스트 프레임워크(Mockito, Jest 등)가 이 개념을 기반으로 합니다.

- **실무**: 외부 의존성 격리, 빠른 피드백 루프
- **면접**: "Mock과 Stub의 차이는?", "언제 Mocking을 사용하나요?"
- **기반 지식**: TDD, 단위 테스트, 통합 테스트

## 핵심 개념

- **Test Double**: 테스트를 위해 실제 객체를 대신하는 모든 종류의 가짜 객체
- **Dummy**: 사용되지 않지만 파라미터 채우기용
- **Stub**: 미리 정의된 값을 반환
- **Spy**: 실제 객체를 감시하며 호출 기록
- **Mock**: 행위를 검증하는 가짜 객체
- **Fake**: 실제 동작하는 간소화된 구현

## 쉽게 이해하기

**Test Double**을 영화 촬영의 대역에 비유할 수 있습니다.

```
실제 촬영 (실제 환경)
┌──────────────────────────────────────────┐
│  주연 배우 + 실제 장소 + 실제 소품        │
│  → 비용 높음, 시간 많이 걸림              │
│  → 날씨, 사고 등 외부 요인에 영향          │
└──────────────────────────────────────────┘

스턴트/대역 활용 (테스트 환경)
┌──────────────────────────────────────────┐
│  스턴트맨 (Mock) + 세트장 (Stub)          │
│  → 통제된 환경에서 빠르게 촬영            │
│  → 위험한 장면도 안전하게 테스트          │
└──────────────────────────────────────────┘
```

## 상세 설명

### Test Double 종류

```
┌─────────────────────────────────────────────────────────┐
│                    Test Double 종류                      │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  Dummy    [파라미터 채우기]                              │
│    │      실제로 사용되지 않음                           │
│    ▼                                                     │
│  Stub     [상태 검증]                                    │
│    │      미리 정의된 값 반환                            │
│    ▼                                                     │
│  Spy      [실제 객체 + 기록]                             │
│    │      실제 동작 + 호출 추적                          │
│    ▼                                                     │
│  Mock     [행위 검증]                                    │
│    │      기대한 대로 호출됐는지 검증                    │
│    ▼                                                     │
│  Fake     [간소화된 실제 구현]                           │
│           실제 동작하지만 운영용 아님                    │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

### 1. Dummy

```java
// Dummy: 실제로 사용되지 않지만 파라미터로 필요한 객체
public class DummyLogger implements Logger {
    @Override
    public void log(String message) {
        // 아무것도 안 함 - 테스트에서 로깅이 필요 없을 때
    }
}

@Test
void createUser_shouldWork() {
    UserService service = new UserService(
        new UserRepository(),
        new DummyLogger()  // 테스트에서 로깅은 필요 없음
    );
    // ...
}
```

### 2. Stub

```java
// Stub: 미리 정의된 값을 반환
public class StubUserRepository implements UserRepository {
    @Override
    public User findById(Long id) {
        return new User(1L, "TestUser", "test@test.com");
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;  // 항상 false 반환
    }
}

@Test
void findUser_shouldReturnUser() {
    UserService service = new UserService(new StubUserRepository());

    User user = service.findUser(1L);

    // 상태 검증 (State Verification)
    assertThat(user.getName()).isEqualTo("TestUser");
}

// Mockito로 Stub 만들기
@Test
void findUser_withMockito() {
    UserRepository repository = mock(UserRepository.class);
    when(repository.findById(1L))
        .thenReturn(new User(1L, "TestUser", "test@test.com"));

    UserService service = new UserService(repository);
    User user = service.findUser(1L);

    assertThat(user.getName()).isEqualTo("TestUser");
}
```

### 3. Mock

```java
// Mock: 호출 여부와 방식을 검증
@Test
void createUser_shouldSendWelcomeEmail() {
    // Given
    EmailService emailService = mock(EmailService.class);
    UserRepository repository = mock(UserRepository.class);
    UserService service = new UserService(repository, emailService);

    // When
    service.createUser(new CreateUserRequest("John", "john@test.com"));

    // Then - 행위 검증 (Behavior Verification)
    verify(emailService).sendWelcomeEmail("john@test.com");
    verify(emailService, times(1)).sendWelcomeEmail(anyString());
    verify(emailService, never()).sendErrorEmail(anyString());
}

// 호출 순서 검증
@Test
void processOrder_shouldCallInOrder() {
    PaymentService payment = mock(PaymentService.class);
    InventoryService inventory = mock(InventoryService.class);

    OrderService orderService = new OrderService(payment, inventory);
    orderService.processOrder(order);

    InOrder inOrder = inOrder(inventory, payment);
    inOrder.verify(inventory).reserve(order);
    inOrder.verify(payment).charge(order);
}
```

### 4. Spy

```java
// Spy: 실제 객체를 감싸서 일부만 Stub
@Test
void spy_example() {
    List<String> list = new ArrayList<>();
    List<String> spyList = spy(list);

    // 실제 메서드 호출
    spyList.add("one");
    spyList.add("two");

    // 실제 동작 확인
    assertThat(spyList.size()).isEqualTo(2);

    // 특정 메서드만 Stub
    doReturn(100).when(spyList).size();
    assertThat(spyList.size()).isEqualTo(100);

    // 호출 여부 검증
    verify(spyList).add("one");
}

// 실제 서비스의 일부만 Stub
@Test
void spy_partialMock() {
    UserService realService = new UserService(repository);
    UserService spyService = spy(realService);

    // validateUser만 Stub
    doReturn(true).when(spyService).validateUser(any());

    // 나머지는 실제 로직 실행
    spyService.createUser(request);
}
```

### 5. Fake

```java
// Fake: 실제 동작하지만 간소화된 구현
public class FakeUserRepository implements UserRepository {
    private Map<Long, User> storage = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void delete(User user) {
        storage.remove(user.getId());
    }

    // 실제 DB 대신 HashMap 사용
    // 운영에서는 사용 안 하지만 실제로 동작함
}

@Test
void userCRUD_withFake() {
    UserRepository repository = new FakeUserRepository();
    UserService service = new UserService(repository);

    // Create
    User created = service.createUser("John");
    assertThat(created.getId()).isNotNull();

    // Read
    User found = service.findUser(created.getId());
    assertThat(found.getName()).isEqualTo("John");

    // Delete
    service.deleteUser(created.getId());
    assertThat(service.findUser(created.getId())).isNull();
}
```

### Stub vs Mock

```java
// Stub: 상태 검증 (State Verification)
// "결과가 이것인가?"
@Test
void stub_stateVerification() {
    UserRepository stubRepo = mock(UserRepository.class);
    when(stubRepo.findById(1L)).thenReturn(new User(1L, "John"));

    UserService service = new UserService(stubRepo);
    User user = service.findUser(1L);

    // 결과(상태) 검증
    assertThat(user.getName()).isEqualTo("John");
}

// Mock: 행위 검증 (Behavior Verification)
// "이 메서드가 호출됐는가?"
@Test
void mock_behaviorVerification() {
    EmailService mockEmail = mock(EmailService.class);

    UserService service = new UserService(repository, mockEmail);
    service.createUser(request);

    // 호출(행위) 검증
    verify(mockEmail).sendWelcomeEmail(anyString());
}
```

### 언제 무엇을 사용하나?

| Test Double | 사용 시점 |
|-------------|----------|
| **Dummy** | 파라미터가 필요하지만 실제로 사용되지 않을 때 |
| **Stub** | 외부 시스템의 응답을 고정하고 싶을 때 |
| **Mock** | 특정 메서드가 호출됐는지 검증하고 싶을 때 |
| **Spy** | 실제 객체의 일부만 변경하고 싶을 때 |
| **Fake** | 실제 동작이 필요하지만 가벼운 구현이 필요할 때 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 빠른 테스트 실행 | 실제 환경과의 차이 |
| 외부 의존성 제거 | 과도한 Mocking은 테스트 취약 |
| 격리된 단위 테스트 | 구현 세부사항에 의존할 수 있음 |
| 엣지 케이스 테스트 용이 | Fake 구현 유지보수 비용 |

### 주의사항

```java
// ❌ 안티패턴: 과도한 Mocking
@Test
void tooMuchMocking() {
    // 모든 것을 Mock하면 실제 동작을 테스트하지 않음
    Service mock = mock(Service.class);
    when(mock.process(any())).thenReturn(result);
    // mock을 만들고 mock을 검증... 의미 없음
}

// ❌ 안티패턴: 구현 세부사항 검증
@Test
void verifyingImplementation() {
    service.createUser(request);
    // 내부 메서드 호출 순서까지 검증 → 리팩토링에 취약
    verify(repository).beginTransaction();
    verify(repository).save(any());
    verify(repository).commit();
}

// ✅ 좋은 예: 행위의 결과 검증
@Test
void verifyingBehavior() {
    service.createUser(request);
    // 비즈니스 관점에서 중요한 행위만 검증
    verify(emailService).sendWelcomeEmail(request.getEmail());
    assertThat(repository.findByEmail(request.getEmail())).isPresent();
}
```

## 면접 예상 질문

### Q: Mock과 Stub의 차이는?

A: **Stub**은 미리 정의된 값을 반환하여 **상태를 검증**합니다. "이 입력에 이 결과가 나오는가?" **Mock**은 메서드 호출 여부와 방식을 검증하여 **행위를 검증**합니다. "이 메서드가 호출됐는가?" 예: Stub은 "결제 API가 성공을 반환한다고 가정", Mock은 "결제 API가 1번 호출됐는지 검증".

### Q: Mocking을 언제 사용하나요?

A: (1) **외부 시스템 의존성 제거**: DB, 외부 API, 파일 시스템 등 (2) **느린 작업 대체**: 네트워크 호출, I/O 작업 (3) **비결정적 요소 제어**: 현재 시간, 랜덤 값 (4) **엣지 케이스 테스트**: 예외 상황, 타임아웃. **주의**: 과도한 Mocking은 실제 동작을 테스트하지 않게 되므로, 통합 테스트와 균형이 필요합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [TDD](./tdd.md) | 선수 지식 | [3] 중급 |
| [OOP](./oop.md) | 의존성 주입 | [2] 입문 |
| [클린 코드](./clean-code.md) | 테스트 가능한 설계 | [3] 중급 |

## 참고 자료

- xUnit Test Patterns - Gerard Meszaros
- [Mocks Aren't Stubs - Martin Fowler](https://martinfowler.com/articles/mocksArentStubs.html)
- [Mockito Documentation](https://site.mockito.org/)
