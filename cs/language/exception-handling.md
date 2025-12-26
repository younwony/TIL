# Java 예외 처리 (Exception Handling)

> `[2] 입문` · 선수 지식: [프로그래밍 언어란](./what-is-language.md)

> 프로그램 실행 중 발생하는 예외 상황을 처리하는 메커니즘

`#예외처리` `#ExceptionHandling` `#Java` `#Exception` `#Error` `#Throwable` `#try` `#catch` `#finally` `#throw` `#throws` `#CheckedException` `#체크예외` `#UncheckedException` `#언체크예외` `#RuntimeException` `#CustomException` `#커스텀예외` `#예외전파` `#ExceptionPropagation` `#tryWithResources`

## 왜 알아야 하는가?

예외 처리는 안정적인 프로그램의 핵심입니다. 네트워크 오류, 파일 없음, 잘못된 입력 등 예외 상황은 항상 발생합니다. 적절한 예외 처리로 프로그램이 비정상 종료되지 않고 복구하거나 사용자에게 유용한 정보를 제공할 수 있습니다.

## 핵심 개념

- **Checked Exception**: 컴파일러가 처리를 강제하는 예외
- **Unchecked Exception**: 런타임 예외, 처리 강제 안 함
- **try-catch-finally**: 예외 처리 구문
- **throw/throws**: 예외 발생/선언

## 쉽게 이해하기

**예외 처리**를 비상 대응 매뉴얼에 비유할 수 있습니다.

```
일상 업무 (정상 흐름):
  회사 출근 → 업무 → 퇴근

예외 상황 발생:
  출근 중 자동차 고장!

예외 처리 (비상 대응):
  try {
      자동차로 출근();
  } catch (자동차고장Exception e) {
      대중교통으로 출근();  // 대체 방법
  } finally {
      어떻게든 출근;      // 반드시 실행
  }
```

## 상세 설명

### 예외 계층 구조

```
┌─────────────────────────────────────────────────────────────┐
│                    Java 예외 계층                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│                       Throwable                              │
│                          │                                   │
│           ┌──────────────┴──────────────┐                   │
│           │                             │                   │
│         Error                      Exception                │
│      (복구 불가)                    (복구 가능)              │
│           │                             │                   │
│  ├─ OutOfMemoryError        ┌───────────┴───────────┐       │
│  ├─ StackOverflowError      │                       │       │
│  └─ VirtualMachineError  Checked              Unchecked     │
│                          Exception         (RuntimeException)│
│                             │                       │       │
│                    ├─ IOException        ├─ NullPointerException
│                    ├─ SQLException       ├─ IllegalArgumentException
│                    └─ ClassNotFoundException  ├─ IndexOutOfBoundsException
│                                          └─ ArithmeticException
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Checked vs Unchecked Exception

```java
// Checked Exception: 컴파일러가 처리 강제
public void readFile() {
    FileReader fr = new FileReader("file.txt");  // 컴파일 에러!
    // IOException을 처리하거나 throws로 선언해야 함
}

// 해결 1: try-catch로 처리
public void readFile() {
    try {
        FileReader fr = new FileReader("file.txt");
    } catch (FileNotFoundException e) {
        System.out.println("파일을 찾을 수 없습니다");
    }
}

// 해결 2: throws로 위임
public void readFile() throws FileNotFoundException {
    FileReader fr = new FileReader("file.txt");
}

// Unchecked Exception: 처리 강제 안 함
public void divide(int a, int b) {
    int result = a / b;  // ArithmeticException 가능하지만 컴파일 OK
}
```

### try-catch-finally

```java
public void example() {
    FileReader fr = null;
    try {
        fr = new FileReader("file.txt");
        // 파일 읽기 작업
        int data = fr.read();

    } catch (FileNotFoundException e) {
        // 파일이 없는 경우
        System.out.println("파일을 찾을 수 없습니다: " + e.getMessage());

    } catch (IOException e) {
        // 읽기 오류
        System.out.println("읽기 오류: " + e.getMessage());

    } finally {
        // 항상 실행 (리소스 정리)
        if (fr != null) {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// 멀티 캐치 (Java 7+)
try {
    // 작업
} catch (FileNotFoundException | IOException e) {
    // 여러 예외를 한 번에 처리
}
```

### try-with-resources (Java 7+)

```java
// 기존 방식 (번거로움)
FileReader fr = null;
try {
    fr = new FileReader("file.txt");
    // 사용
} finally {
    if (fr != null) {
        try {
            fr.close();
        } catch (IOException e) { }
    }
}

// try-with-resources (간결)
try (FileReader fr = new FileReader("file.txt")) {
    // 사용
}  // 자동으로 close() 호출

// 여러 리소스
try (FileReader fr = new FileReader("input.txt");
     FileWriter fw = new FileWriter("output.txt")) {
    // 사용
}  // 역순으로 close() (fw → fr)

// 조건: AutoCloseable 인터페이스 구현 필요
public interface AutoCloseable {
    void close() throws Exception;
}
```

### throw와 throws

```java
// throw: 예외 발생
public void validateAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("나이는 음수일 수 없습니다");
    }
}

// throws: 예외 선언 (호출자에게 처리 위임)
public void readFile() throws IOException {
    FileReader fr = new FileReader("file.txt");
}

// 체이닝: 예외를 잡아서 다른 예외로 변환
public User findUser(Long id) throws UserNotFoundException {
    try {
        return userRepository.findById(id);
    } catch (SQLException e) {
        throw new UserNotFoundException("사용자를 찾을 수 없습니다", e);
    }
}
```

### 커스텀 예외

```java
// Checked Exception
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Unchecked Exception (권장)
public class InvalidOrderException extends RuntimeException {
    private final String errorCode;

    public InvalidOrderException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

// 사용
public Order createOrder(OrderRequest request) {
    if (request.getItems().isEmpty()) {
        throw new InvalidOrderException("ORD001", "주문 항목이 비어있습니다");
    }
    // ...
}
```

### 예외 처리 Best Practice

```java
// 1. 구체적인 예외 먼저 catch
try {
    // 작업
} catch (FileNotFoundException e) {    // 더 구체적인 예외 먼저
    // 파일 없음 처리
} catch (IOException e) {              // 더 일반적인 예외 나중에
    // 기타 IO 오류 처리
}

// 2. 예외 정보 보존 (원인 체이닝)
try {
    // 작업
} catch (SQLException e) {
    throw new DataAccessException("데이터 조회 실패", e);  // 원인 포함
}

// 3. 빈 catch 블록 피하기
try {
    // 작업
} catch (Exception e) {
    // 나쁜 예: 아무것도 안 함
}

try {
    // 작업
} catch (Exception e) {
    log.error("작업 실패", e);  // 좋은 예: 최소한 로깅
    throw e;
}

// 4. finally에서 예외 발생 주의
try {
    return process();
} finally {
    cleanup();  // 여기서 예외 발생하면 try의 예외가 덮어씌워짐
}

// 5. 예외는 예외적인 상황에만 사용
// 나쁜 예: 흐름 제어에 사용
try {
    while (true) {
        array[i++] = 0;
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // 루프 종료
}

// 좋은 예
for (int i = 0; i < array.length; i++) {
    array[i] = 0;
}
```

### Spring에서의 예외 처리

```java
// @ControllerAdvice를 이용한 전역 예외 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException e) {
        return new ErrorResponse("USER_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidOrder(InvalidOrderException e) {
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception e) {
        log.error("예상치 못한 오류", e);
        return new ErrorResponse("INTERNAL_ERROR", "서버 오류가 발생했습니다");
    }
}
```

## 트레이드오프

| Checked Exception | Unchecked Exception |
|-------------------|---------------------|
| 컴파일러가 처리 강제 | 처리 선택적 |
| 명시적 예외 선언 | 코드 간결 |
| API 계약 명확 | 유연한 설계 |
| throws 체인 복잡 | 예외 누락 가능 |

## 면접 예상 질문

### Q: Checked와 Unchecked Exception의 차이와 선택 기준은?

A: **Checked**: 컴파일러가 처리 강제, 복구 가능한 예외에 사용 (IOException, SQLException). **Unchecked**: RuntimeException 하위, 프로그래밍 오류에 사용 (NullPointer, IllegalArgument). **선택 기준**: (1) 호출자가 복구할 수 있으면 Checked (2) 프로그래머 실수면 Unchecked. **최근 트렌드**: Unchecked 선호 (Spring도 대부분 Unchecked 사용).

### Q: 예외 처리 시 주의할 점은?

A: (1) **빈 catch 금지**: 최소한 로깅 필요. (2) **원인 체이닝**: 새 예외 던질 때 원인 예외 포함 `throw new XxxException(msg, e)`. (3) **구체적 예외 먼저 catch**: 상위 예외가 먼저 오면 도달 불가. (4) **리소스 정리**: try-with-resources 사용. (5) **예외는 예외적 상황에만**: 흐름 제어에 사용 금지.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로그래밍 언어란](./what-is-language.md) | 선수 지식 | [1] 기초 |
| [Spring MVC](../spring/spring-mvc.md) | 예외 처리 활용 | [3] 중급 |

## 참고 자료

- [Java Exceptions - Oracle](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [Effective Java - Item 69-77](https://www.oreilly.com/library/view/effective-java/9780134686097/)
