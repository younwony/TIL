# Week 9: 예외 처리 (Exception Handling)

## 개요

Java 예외 처리에 대해 학습합니다.

## 예외 계층 구조

```
Throwable
├── Error (복구 불가능한 오류)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── RuntimeException (Unchecked Exception)
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   └── IllegalArgumentException
    └── IOException, SQLException 등 (Checked Exception)
```

## try-catch-finally

```java
public void readFile() {
    FileReader reader = null;
    try {
        reader = new FileReader("file.txt");
        // 파일 읽기 로직
    } catch (FileNotFoundException e) {
        System.out.println("파일을 찾을 수 없습니다: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("읽기 오류: " + e.getMessage());
    } finally {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## try-with-resources (Java 7+)

```java
public void readFile() {
    try (FileReader reader = new FileReader("file.txt");
         BufferedReader br = new BufferedReader(reader)) {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.out.println("오류: " + e.getMessage());
    }
}
```

## 커스텀 예외 클래스

```java
public class CustomException extends Exception {
    private int errorCode;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
```

## 예외 던지기 (throw, throws)

```java
public class Study {
    // 메서드에서 예외를 던질 수 있음을 선언
    public void validateAge(int age) throws CustomException {
        if (age < 0) {
            throw new CustomException("나이는 음수일 수 없습니다", 1001);
        }
        if (age > 150) {
            throw new CustomException("유효하지 않은 나이입니다", 1002);
        }
    }
}
```

## 예외 처리 패턴

### 예외 전환 (Exception Translation)

```java
public void processData() throws ServiceException {
    try {
        // 데이터 처리
    } catch (SQLException e) {
        throw new ServiceException("데이터 처리 실패", e);
    }
}
```

### 예외 연결 (Exception Chaining)

```java
try {
    // 코드
} catch (IOException e) {
    throw new CustomException("처리 실패", e);  // 원인 예외 전달
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy9_예외처리](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy9_%EC%97%90%EC%99%B8%EC%B2%98%EB%A6%AC)
