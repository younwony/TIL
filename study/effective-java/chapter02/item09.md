# Item 9: try-finally보다는 try-with-resources를 사용하라

## 핵심 정리

자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다. InputStream, OutputStream, java.sql.Connection 등이 좋은 예다.

## try-finally의 문제점

```java
// 자원이 하나인 경우
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}

// 자원이 둘 이상인 경우 - 코드가 복잡해진다
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

**문제점:**
- 코드가 너무 지저분해진다
- 예외가 try 블록과 finally 블록 모두에서 발생할 수 있다
- 이 경우 두 번째 예외가 첫 번째 예외를 완전히 집어삼켜 디버깅을 어렵게 만든다

## 해결책: try-with-resources

```java
// 자원이 하나인 경우
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(
            new FileReader(path))) {
        return br.readLine();
    }
}

// 자원이 둘 이상인 경우 - 여전히 깔끔하다
static void copy(String src, String dst) throws IOException {
    try (InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) >= 0)
            out.write(buf, 0, n);
    }
}
```

## try-with-resources와 catch 절

```java
static String firstLineOfFile(String path, String defaultVal) {
    try (BufferedReader br = new BufferedReader(
            new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        return defaultVal;
    }
}
```

## 장점

1. 코드가 더 짧고 분명하다
2. 만들어지는 예외 정보도 훨씬 유용하다
3. 숨겨진 예외들도 스택 추적 내역에 'suppressed' 꼬리표를 달고 출력된다
4. `Throwable.getSuppressed` 메서드를 이용해 프로그램 코드에서 가져올 수도 있다

## 요약

- 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자
- 예외는 없다
- 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다
- try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다

## 참고

- 원본 코드: [effectiveJava/chapter_2/item_9](https://github.com/younwony/effectiveJava/tree/main/src/main/java/dev/wony/effectivejava/chapter_2/item_9)
