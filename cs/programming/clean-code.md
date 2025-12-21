# 클린 코드 (Clean Code)

**난이도**: [3] 중급

## 왜 알아야 하는가?

소프트웨어 개발에서 **코드를 읽는 시간이 작성하는 시간보다 10배 이상** 많습니다. 클린 코드는 단순히 "예쁜 코드"가 아니라, 유지보수 비용을 절감하고 기술 부채를 줄이는 핵심 역량입니다.

- **실무 필수**: 협업 환경에서 다른 개발자가 내 코드를 읽고 이해해야 함
- **버그 감소**: 명확한 코드는 실수를 줄이고 디버깅 시간을 단축
- **리팩토링 기반**: 클린 코드 원칙을 알아야 안전하게 코드를 개선할 수 있음
- **면접 단골**: 코드 리뷰, 리팩토링 경험은 면접에서 자주 질문됨

## 핵심 개념

- **클린 코드**는 읽기 쉽고, 이해하기 쉬우며, 수정하기 쉬운 코드
- "코드는 작성하는 시간보다 읽는 시간이 10배 이상" - Robert C. Martin
- 핵심 원칙: **가독성**, **단순성**, **명확성**, **일관성**
- 클린 코드는 다른 개발자(미래의 나 포함)를 위한 **배려**
- 기술 부채를 줄이고 유지보수 비용을 절감하는 핵심 역량

## 의미 있는 이름 (Meaningful Names)

### 의도를 분명히 밝혀라

```java
// Bad
int d; // 경과 시간 (일)
List<int[]> list1;

// Good
int elapsedTimeInDays;
List<Cell> flaggedCells;
```

### 그릇된 정보를 피하라

```java
// Bad - List가 아닌데 List라는 이름 사용
Map<String, Account> accountList;

// Good
Map<String, Account> accounts;
Map<String, Account> accountMap;
```

### 의미 있게 구분하라

```java
// Bad - 불용어(noise word) 사용
String nameString;
Customer customerObject;
void getActiveAccount();
void getActiveAccountInfo();  // 차이점이 무엇인가?

// Good
String name;
Customer customer;
void getActiveAccount();
```

### 발음하기 쉬운 이름 사용

```java
// Bad
Date genymdhms;  // generation year, month, day, hour, minute, second
int pszqint;

// Good
Date generationTimestamp;
int recordCount;
```

### 검색하기 쉬운 이름 사용

```java
// Bad - 매직 넘버
if (status == 4) { ... }
for (int i = 0; i < 7; i++) { ... }

// Good
private static final int WORK_DAYS_PER_WEEK = 7;
private static final int STATUS_DELETED = 4;

if (status == STATUS_DELETED) { ... }
for (int i = 0; i < WORK_DAYS_PER_WEEK; i++) { ... }
```

### 클래스와 메서드 이름 규칙

```java
// 클래스: 명사 또는 명사구
class Customer { }
class WikiPage { }
class AccountParser { }

// 메서드: 동사 또는 동사구
void postPayment() { }
void deletePage() { }
boolean isPosted() { }  // boolean 반환은 is/has/can 접두사
```

## 함수 (Functions)

### 작게 만들어라

```java
// Bad - 너무 긴 함수
public void processOrder(Order order) {
    // 검증 로직 50줄
    // 재고 확인 30줄
    // 결제 처리 40줄
    // 배송 처리 30줄
    // 알림 발송 20줄
}

// Good - 작은 함수로 분리
public void processOrder(Order order) {
    validateOrder(order);
    checkInventory(order);
    processPayment(order);
    arrangeShipping(order);
    sendNotification(order);
}
```

### 한 가지만 해라 (Single Responsibility)

```java
// Bad - 여러 가지 일을 하는 함수
public void processUserAndSendEmail(User user) {
    // 사용자 검증
    if (user.getName() == null) throw new IllegalArgumentException();

    // DB 저장
    userRepository.save(user);

    // 이메일 발송
    emailService.sendWelcomeEmail(user.getEmail());
}

// Good - 각각의 책임으로 분리
public void registerUser(User user) {
    validateUser(user);
    saveUser(user);
    sendWelcomeEmail(user);
}

private void validateUser(User user) { ... }
private void saveUser(User user) { ... }
private void sendWelcomeEmail(User user) { ... }
```

### 함수 인수는 적게 (이상적으로 0~2개)

```java
// Bad - 너무 많은 인수
public void createUser(String name, String email, String phone,
                       String address, int age, boolean isActive) { ... }

// Good - 객체로 묶기
public void createUser(UserCreateRequest request) { ... }

// 또는 Builder 패턴 사용
User user = User.builder()
    .name("John")
    .email("john@example.com")
    .build();
```

### 플래그 인수는 피하라

```java
// Bad - 플래그 인수 (함수가 두 가지 일을 한다는 증거)
public void render(boolean isSuite) {
    if (isSuite) {
        renderForSuite();
    } else {
        renderForSingleTest();
    }
}

// Good - 명확하게 분리
public void renderForSuite() { ... }
public void renderForSingleTest() { ... }
```

### 부수 효과(Side Effect)를 피하라

```java
// Bad - 숨겨진 부수 효과
public boolean checkPassword(String userName, String password) {
    User user = userRepository.findByName(userName);
    if (user != null && user.getPassword().equals(password)) {
        Session.initialize();  // 부수 효과! 함수 이름에서 예상 불가
        return true;
    }
    return false;
}

// Good - 부수 효과 제거 또는 이름에 명시
public boolean checkPassword(String userName, String password) {
    User user = userRepository.findByName(userName);
    return user != null && user.getPassword().equals(password);
}

public void checkPasswordAndInitSession(String userName, String password) {
    if (checkPassword(userName, password)) {
        Session.initialize();
    }
}
```

### 명령과 조회를 분리하라 (CQS)

```java
// Bad - 명령과 조회가 혼합
public boolean set(String attribute, String value) {
    // 값을 설정하고 성공 여부 반환
}

if (set("username", "john")) { ... }  // 의미가 모호함

// Good - 분리
public void setAttribute(String attribute, String value) { ... }
public boolean attributeExists(String attribute) { ... }

if (attributeExists("username")) {
    setAttribute("username", "john");
}
```

## 주석 (Comments)

### 좋은 주석

```java
// 법적인 주석
// Copyright (C) 2024 by Company. All rights reserved.

// 정보를 제공하는 주석
// kk:mm:ss EEE, MMM dd, yyyy 형식의 날짜를 파싱
Pattern timeMatcher = Pattern.compile("\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*");

// 의도를 설명하는 주석
// 스레드를 대량 생성하여 경쟁 조건을 만들어 테스트
for (int i = 0; i < 25000; i++) {
    Thread thread = new Thread(widgetBuilderThread);
    thread.start();
}

// TODO 주석
// TODO: 현재 임시 구현. 2024년 3분기에 캐시 로직 추가 예정

// 중요성 강조
// trim()은 매우 중요. 문자열 시작의 공백이 다른 문자열로 인식될 수 있음
String listItemContent = match.group(3).trim();
```

### 나쁜 주석

```java
// Bad - 같은 말 반복 (주석이 코드보다 정보가 적음)
// 월을 반환한다
public int getMonth() {
    return month;
}

// Bad - 이력 기록 (버전 관리 시스템 사용)
// 2024-01-15: 기능 추가
// 2024-01-20: 버그 수정
// 2024-02-01: 리팩토링

// Bad - 있으나 마나 한 주석
// 기본 생성자
public AnnualDateRule() { }

// Bad - 주석 처리된 코드 (삭제하라)
// InputStreamResponse response = new InputStreamResponse();
// response.setBody(formatter.getResultStream(), formatter.getByteCount());

// Bad - 닫는 괄호에 다는 주석
try {
    while (...) {
        ...
    } // while
} // try
catch (Exception e) {
} // catch
```

### 주석보다 코드로 표현하라

```java
// Bad
// 직원에게 복지 혜택을 받을 자격이 있는지 검사
if ((employee.flags & HOURLY_FLAG) && (employee.age > 65))

// Good
if (employee.isEligibleForFullBenefits())
```

## 형식 맞추기 (Formatting)

### 적절한 행 길이 유지

```
권장 파일 크기: 200~500줄
권장 행 길이: 80~120자
```

### 수직 거리 (관련 코드는 가까이)

```java
// Good - 관련 있는 코드끼리 모으기
public class ReporterConfig {
    private String className;
    private List<Property> properties;

    // 생성자
    public ReporterConfig() { }

    // className 관련 메서드
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    // properties 관련 메서드
    public List<Property> getProperties() { return properties; }
    public void addProperty(Property property) { properties.add(property); }
}
```

### 변수 선언

```java
// 지역 변수는 사용하는 위치에 최대한 가까이
public int countTestCases() {
    int count = 0;  // 바로 아래에서 사용
    for (Test test : tests) {
        count += test.countTestCases();
    }
    return count;
}

// 인스턴스 변수는 클래스 맨 처음에 선언
public class Calculator {
    private int result;
    private String lastOperation;

    public void add(int value) { ... }
    public void subtract(int value) { ... }
}
```

### 가로 형식 맞추기

```java
// 할당문 - 공백으로 좌우 구분
int lineSize = line.length();

// 함수 인수 - 쉼표 뒤 공백
lineWidthHistogram.addLine(lineSize, lineCount);

// 연산자 우선순위 강조
return b*b - 4*a*c;  // 곱셈이 먼저
return (-b + Math.sqrt(determinant)) / (2*a);
```

## 오류 처리 (Error Handling)

### 예외를 사용하라 (오류 코드 대신)

```java
// Bad - 오류 코드 반환
public int withdraw(int amount) {
    if (balance < amount) return -1;
    if (amount <= 0) return -2;
    balance -= amount;
    return 0;
}

// Good - 예외 사용
public void withdraw(int amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("출금액은 0보다 커야 합니다");
    }
    if (balance < amount) {
        throw new InsufficientBalanceException("잔액이 부족합니다");
    }
    balance -= amount;
}
```

### Try-Catch-Finally 문부터 작성하라

```java
public List<RecordedGrip> retrieveSection(String sectionName) {
    try {
        FileInputStream stream = new FileInputStream(sectionName);
        // 정상 로직
    } catch (FileNotFoundException e) {
        throw new StorageException("파일을 찾을 수 없습니다: " + sectionName, e);
    } finally {
        // 리소스 정리
    }
}
```

### Unchecked Exception을 사용하라

```java
// Checked Exception의 문제점
// - OCP(개방-폐쇄 원칙) 위반: 하위 메서드에서 새 예외 추가 시 상위 모든 메서드 수정 필요
// - 캡슐화 깨짐: 상위 레벨이 하위 레벨 구현 세부사항을 알아야 함

// Checked Exception (피하는 것이 좋음)
public void readFile() throws FileNotFoundException, IOException { }

// Unchecked Exception (권장)
public void readFile() {
    try {
        // ...
    } catch (IOException e) {
        throw new FileReadException("파일 읽기 실패", e);
    }
}
```

### 예외에 의미를 제공하라

```java
// Bad
throw new Exception("Error");

// Good
throw new DeviceResponseException(
    String.format("ACme 장치에서 응답이 없습니다. 포트: %d, 대기시간: %dms",
        portNumber, timeoutMs)
);
```

### null을 반환하지 마라

```java
// Bad - null 반환
public List<Employee> getEmployees() {
    if (/* 직원이 없으면 */) {
        return null;
    }
    // ...
}

// 호출하는 쪽에서 매번 null 체크 필요
List<Employee> employees = getEmployees();
if (employees != null) {
    for (Employee e : employees) { ... }
}

// Good - 빈 컬렉션 반환
public List<Employee> getEmployees() {
    if (/* 직원이 없으면 */) {
        return Collections.emptyList();
    }
    // ...
}

// null 체크 불필요
for (Employee e : getEmployees()) { ... }
```

### Optional 활용 (Java 8+)

```java
// Bad
public User findById(Long id) {
    User user = userRepository.findById(id);
    return user;  // null일 수 있음
}

// Good
public Optional<User> findById(Long id) {
    return Optional.ofNullable(userRepository.findById(id));
}

// 사용
findById(1L)
    .map(User::getName)
    .orElse("Unknown");
```

## 경계 (Boundaries)

### 외부 코드를 래핑하라

```java
// Bad - 외부 API 직접 사용
Map<String, Sensor> sensors = new HashMap<>();
Sensor s = sensors.get(sensorId);

// Good - 래핑하여 캡슐화
public class Sensors {
    private Map<String, Sensor> sensors = new HashMap<>();

    public Sensor getById(String id) {
        return sensors.get(id);
    }

    public void add(String id, Sensor sensor) {
        sensors.put(id, sensor);
    }
}
```

### 학습 테스트 작성

```java
// 외부 라이브러리를 익히기 위한 테스트
@Test
void testLogCreate() {
    Logger logger = Logger.getLogger("MyLogger");
    logger.info("hello");
}

@Test
void testLogAddAppender() {
    Logger logger = Logger.getLogger("MyLogger");
    ConsoleAppender appender = new ConsoleAppender();
    logger.addAppender(appender);
    logger.info("hello");
}
```

## 리팩토링 기법

### Extract Method (메서드 추출)

```java
// Before
public void printOwing() {
    printBanner();

    // 세부 정보 출력
    System.out.println("name: " + name);
    System.out.println("amount: " + getOutstanding());
}

// After
public void printOwing() {
    printBanner();
    printDetails();
}

private void printDetails() {
    System.out.println("name: " + name);
    System.out.println("amount: " + getOutstanding());
}
```

### Replace Conditional with Polymorphism

```java
// Before
public double calculatePay(Employee e) {
    switch (e.type) {
        case COMMISSIONED:
            return calculateCommissionedPay(e);
        case HOURLY:
            return calculateHourlyPay(e);
        case SALARIED:
            return calculateSalariedPay(e);
        default:
            throw new InvalidEmployeeType(e.type);
    }
}

// After
public abstract class Employee {
    public abstract double calculatePay();
}

public class CommissionedEmployee extends Employee {
    @Override
    public double calculatePay() { ... }
}

public class HourlyEmployee extends Employee {
    @Override
    public double calculatePay() { ... }
}
```

### Early Return (Guard Clause)

```java
// Before - 중첩된 조건문
public double getPayAmount() {
    double result;
    if (isDead) {
        result = deadAmount();
    } else {
        if (isSeparated) {
            result = separatedAmount();
        } else {
            if (isRetired) {
                result = retiredAmount();
            } else {
                result = normalPayAmount();
            }
        }
    }
    return result;
}

// After - Early Return
public double getPayAmount() {
    if (isDead) return deadAmount();
    if (isSeparated) return separatedAmount();
    if (isRetired) return retiredAmount();
    return normalPayAmount();
}
```

## 클린 코드 체크리스트

### 네이밍
- [ ] 의도가 분명한 이름인가?
- [ ] 발음하기 쉽고 검색 가능한 이름인가?
- [ ] 매직 넘버/문자열이 상수로 정의되어 있는가?

### 함수
- [ ] 함수가 한 가지 일만 하는가?
- [ ] 함수 인수가 3개 이하인가?
- [ ] 부수 효과가 없는가?
- [ ] 명령과 조회가 분리되어 있는가?

### 주석
- [ ] 코드로 표현할 수 있는 내용을 주석으로 작성하지 않았는가?
- [ ] 주석 처리된 코드가 없는가?
- [ ] TODO 주석에 담당자와 기한이 명시되어 있는가?

### 형식
- [ ] 관련 있는 코드가 수직으로 가까이 있는가?
- [ ] 일관된 들여쓰기와 공백을 사용하는가?
- [ ] 파일과 함수 길이가 적절한가?

### 오류 처리
- [ ] 예외를 사용하여 오류를 처리하는가?
- [ ] null 대신 Optional이나 빈 컬렉션을 반환하는가?
- [ ] 예외 메시지가 충분한 정보를 제공하는가?

## 면접 예상 질문

### Q1. 클린 코드란 무엇이며, 왜 중요한가요?

**모범 답안:**

클린 코드는 읽기 쉽고, 이해하기 쉬우며, 수정하기 쉬운 코드입니다. Robert C. Martin은 "코드를 작성하는 시간보다 읽는 시간이 10배 이상"이라고 말했습니다. 이는 코드의 가독성이 개발 생산성에 직접적인 영향을 미친다는 의미입니다.

클린 코드가 중요한 이유는 크게 세 가지입니다.

첫째, **유지보수 비용 절감**입니다. 소프트웨어 생명주기에서 유지보수가 차지하는 비용이 80% 이상입니다. 읽기 어려운 코드는 수정에 더 많은 시간이 소요되고, 버그 발생 확률도 높아집니다.

둘째, **기술 부채 감소**입니다. 빠른 개발을 위해 품질을 희생하면 단기적으로는 시간을 절약하지만, 장기적으로는 수정과 확장이 점점 어려워지는 기술 부채가 쌓입니다. 클린 코드는 이러한 부채를 미리 방지합니다.

셋째, **팀 협업 효율성 향상**입니다. 명확한 네이밍, 작은 함수, 일관된 형식은 다른 개발자가 코드를 빠르게 이해하고 기여할 수 있게 합니다. 이는 코드 리뷰 시간 단축과 온보딩 비용 감소로 이어집니다.

### Q2. 좋은 함수를 작성하기 위한 원칙들을 설명해주세요.

**모범 답안:**

좋은 함수 작성의 핵심 원칙은 다음과 같습니다.

**첫째, 작게 만들어라.** 함수는 한 화면에 들어올 정도로 작아야 합니다. 20줄 이내를 권장하며, if/else/while 블록은 한 줄이 이상적입니다. 블록 안에서 다른 함수를 호출하는 형태가 됩니다.

**둘째, 한 가지만 해라(SRP).** 함수는 단 하나의 작업만 수행해야 합니다. "한 가지"의 기준은 추상화 수준입니다. 함수 내 모든 문장이 동일한 추상화 수준이어야 합니다.

**셋째, 인수를 적게 하라.** 이상적인 인수 개수는 0개이며, 3개를 넘지 않아야 합니다. 인수가 많으면 객체로 묶거나 Builder 패턴을 사용합니다. 특히 플래그 인수(boolean)는 함수가 두 가지 일을 한다는 증거이므로 피해야 합니다.

**넷째, 부수 효과를 피하라.** 함수가 예상치 못한 상태 변경을 수행하면 안 됩니다. checkPassword() 함수가 세션을 초기화한다면 이는 숨겨진 부수 효과입니다.

**다섯째, 명령과 조회를 분리하라(CQS).** 함수는 상태를 변경하거나(Command), 정보를 반환하거나(Query) 둘 중 하나만 해야 합니다. `set()` 함수가 boolean을 반환하면 혼란을 야기합니다.

### Q3. 주석을 최소화해야 하는 이유와 좋은 주석의 예시를 설명해주세요.

**모범 답안:**

주석을 최소화해야 하는 이유는 **주석은 코드와 달리 유지보수되지 않기 때문**입니다. 코드는 변경되어도 주석은 그대로 남아 잘못된 정보를 전달하게 됩니다. 또한 주석이 필요하다는 것은 코드 자체가 의도를 명확히 표현하지 못한다는 신호입니다.

**나쁜 주석의 예:**
- 같은 말 반복: `// 월을 반환한다` + `getMonth()`
- 주석 처리된 코드: 버전 관리 시스템이 히스토리를 관리하므로 삭제해야 합니다
- 이력 기록: Git 커밋 로그가 이를 대체합니다
- 닫는 괄호 주석: `} // while` - 함수를 작게 만들면 불필요합니다

**좋은 주석의 예:**
1. **법적 주석**: 저작권, 라이선스 정보
2. **의도 설명**: 왜 이런 결정을 했는지 (예: "스레드를 대량 생성하여 경쟁 조건 테스트")
3. **결과 경고**: "이 함수는 매우 느립니다. 배치 처리에서만 사용하세요"
4. **TODO**: 앞으로 할 일 (담당자와 기한 명시)
5. **중요성 강조**: `// trim() 필수. 공백이 있으면 다른 문자열로 인식됨`

가장 좋은 주석은 **주석을 달지 않을 방법을 찾는 것**입니다. `// 직원에게 복지 혜택 자격이 있는지 검사` 대신 `employee.isEligibleForFullBenefits()` 메서드를 만드는 것이 더 좋습니다.

## 연관 문서

### 필수 선행 학습
- [OOP](./oop.md) - 객체지향 기본 개념을 알아야 클린 코드 원칙 이해 가능

### 관련 심화 주제
- [디자인 패턴](./design-pattern.md) - 클린 코드를 구현하는 검증된 설계 방법
- [함수형 프로그래밍](./functional-programming.md) - 불변성, 순수 함수로 클린 코드 작성

### 실무 적용
- [API 설계](./api-design.md) - 클린 코드 원칙을 API 설계에 적용

## 참고 자료

- Robert C. Martin, "Clean Code: A Handbook of Agile Software Craftsmanship"
- Martin Fowler, "Refactoring: Improving the Design of Existing Code"
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
