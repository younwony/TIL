# 파일 디스크립터 (File Descriptor)

> `Intermediate` 중급 · 선수 지식: [프로세스와 스레드](./process-vs-thread.md)

> 프로세스가 파일, 소켓, 파이프 등 I/O 자원에 접근하기 위해 운영체제로부터 할당받는 정수 식별자이다.

## 핵심 개념

- **파일 디스크립터(FD)**: 열린 파일을 가리키는 음이 아닌 정수 핸들
- **표준 FD**: 0(stdin), 1(stdout), 2(stderr)는 프로세스 시작 시 자동 할당
- **"Everything is a file"**: Unix 철학 - 파일, 소켓, 파이프, 디바이스 모두 FD로 추상화
- **FD 테이블**: 각 프로세스가 보유한 열린 파일 목록 (프로세스별 독립)
- **FD 누수**: close() 없이 FD를 계속 열면 자원 고갈 발생

## 쉽게 이해하기

**파일 디스크립터**를 도서관의 대출 시스템에 비유할 수 있습니다.

### 도서관 = 운영체제

도서관(OS)에는 수많은 책(파일)이 있습니다. 책을 읽으려면 대출 카드(FD)가 필요합니다.

### 대출 카드 = 파일 디스크립터

- **대출**: `open()` - 책을 빌리면 대출 번호(FD)를 받음
- **읽기/쓰기**: `read()/write()` - 대출 번호로 책에 접근
- **반납**: `close()` - 대출 번호 반환, 다른 사람이 사용 가능

### 대출 한도 = FD 제한

한 사람(프로세스)이 빌릴 수 있는 책의 수에는 한도가 있습니다. 반납 없이 계속 빌리면 한도 초과로 더 이상 대출할 수 없습니다.

| 비유 | 실제 |
|------|------|
| 도서관 | 운영체제 |
| 책 | 파일, 소켓, 파이프 |
| 대출 카드 번호 | 파일 디스크립터 (정수) |
| 대출 | open() 시스템 콜 |
| 반납 | close() 시스템 콜 |
| 대출 한도 | ulimit, FD 최대 개수 |

**왜 번호(정수)를 사용하나?**
- 커널 내부의 복잡한 구조체를 직접 노출하지 않음 (보안, 추상화)
- 정수 연산은 빠르고 효율적
- 프로세스 간 격리 (다른 프로세스의 FD 3과 내 FD 3은 다른 파일)

---

## 상세 설명

### 표준 파일 디스크립터

프로세스 시작 시 자동으로 3개의 FD가 열림:

```
┌─────┬──────────┬─────────────────────────────┐
│ FD  │ 이름     │ 설명                        │
├─────┼──────────┼─────────────────────────────┤
│  0  │ stdin    │ 표준 입력 (키보드)           │
│  1  │ stdout   │ 표준 출력 (화면)             │
│  2  │ stderr   │ 표준 에러 (화면, 버퍼 없음)   │
└─────┴──────────┴─────────────────────────────┘
```

**왜 stdout과 stderr이 분리되어 있나?**
- stdout: 정상 출력, 버퍼링 됨 (성능 최적화)
- stderr: 에러 출력, 버퍼링 없음 (즉시 출력되어 디버깅 용이)
- 리다이렉션 시 분리 가능: `program > output.log 2> error.log`

### 파일 디스크립터 테이블 구조

```
┌────────────────────────────────────────────────────────────────┐
│                         커널 영역                              │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│   ┌──────────────┐     ┌──────────────┐     ┌──────────────┐  │
│   │ Open File    │     │ Open File    │     │ Open File    │  │
│   │ Table Entry  │     │ Table Entry  │     │ Table Entry  │  │
│   │ ─────────────│     │ ─────────────│     │ ─────────────│  │
│   │ offset: 100  │     │ offset: 0    │     │ offset: 50   │  │
│   │ flags: R     │     │ flags: RW    │     │ flags: W     │  │
│   │ inode ptr ───┼──┐  │ inode ptr ───┼──┐  │ inode ptr ───┼──┤
│   └──────────────┘  │  └──────────────┘  │  └──────────────┘  │
│                     │                    │                    │
│                     ▼                    ▼                    │
│              ┌───────────┐        ┌───────────┐               │
│              │   Inode   │        │   Inode   │               │
│              │ (file.txt)│        │ (data.db) │               │
│              └───────────┘        └───────────┘               │
│                                                                │
└────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────────────────────────┐
│                       사용자 영역                              │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│   Process A                          Process B                 │
│   ┌─────────────────┐               ┌─────────────────┐       │
│   │ FD Table        │               │ FD Table        │       │
│   │ ───────────────│               │ ───────────────│       │
│   │ 0 → stdin       │               │ 0 → stdin       │       │
│   │ 1 → stdout      │               │ 1 → stdout      │       │
│   │ 2 → stderr      │               │ 2 → stderr      │       │
│   │ 3 → file.txt ───┼───────────┐   │ 3 → data.db ────┼───┐   │
│   │ 4 → data.db ────┼───────┐   │   │ 4 → socket ─────┼─┐ │   │
│   └─────────────────┘       │   │   └─────────────────┘ │ │   │
│                             │   │                       │ │   │
└─────────────────────────────┼───┼───────────────────────┼─┼───┘
                              │   │                       │ │
                              │   └───────────────────────┼─┘
                              │                           │
                              └───────────────────────────┘
```

**3단계 구조의 이유:**

1. **프로세스 FD 테이블** (per-process)
   - 각 프로세스가 독립적으로 보유
   - FD 번호 → Open File Table 포인터

2. **Open File Table** (system-wide)
   - 파일의 현재 오프셋, 접근 모드 저장
   - 여러 프로세스가 같은 파일을 다른 오프셋으로 읽기 가능

3. **Inode Table** (system-wide)
   - 실제 파일 메타데이터 (크기, 권한, 블록 위치)
   - 같은 파일을 여러 번 open해도 inode는 하나

**왜 이렇게 복잡한가?**
- **fork() 지원**: 자식 프로세스가 부모의 FD를 상속하되 독립적 오프셋 가능
- **공유와 독립의 균형**: 필요에 따라 오프셋 공유/독립 선택 가능
- **효율성**: inode 중복 저장 방지

---

## 동작 원리

### open() 시스템 콜

```
┌─────────────────────────────────────────────────────────────┐
│                    open("/tmp/file.txt", O_RDWR)            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. 커널이 해당 파일의 inode 탐색                             │
│    - 파일 시스템에서 경로를 따라 inode 찾기                  │
│    - 권한 확인 (읽기/쓰기 가능한지)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Open File Table에 새 엔트리 생성                         │
│    - offset = 0 (파일 시작)                                 │
│    - flags = O_RDWR                                        │
│    - inode 포인터 설정                                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. 프로세스 FD 테이블에서 가장 작은 미사용 FD 할당           │
│    - 보통 3부터 시작 (0,1,2는 표준 FD)                      │
│    - FD 3 → Open File Table 엔트리 연결                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 4. FD 번호(3) 반환                                          │
└─────────────────────────────────────────────────────────────┘
```

### close() 시스템 콜

```
┌─────────────────────────────────────────────────────────────┐
│                        close(3)                             │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. 프로세스 FD 테이블에서 FD 3 제거                         │
│    - FD 3은 다시 사용 가능                                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Open File Table 엔트리 참조 카운트 감소                  │
│    - 참조 카운트 0이면 엔트리 삭제                          │
│    - fork()로 공유 중이면 다른 프로세스가 여전히 참조 가능   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. Inode 참조 카운트도 감소                                 │
│    - 참조 카운트 0이면 파일 시스템 자원 해제                │
└─────────────────────────────────────────────────────────────┘
```

**왜 참조 카운트를 사용하나?**
- fork() 시 부모/자식이 같은 Open File Table 공유
- 한쪽에서 close()해도 다른 쪽은 계속 사용 가능
- 모든 참조가 사라져야 실제로 자원 해제

---

## 리다이렉션과 dup2()

### 표준 출력 리다이렉션

```bash
# stdout을 파일로 리다이렉션
$ program > output.txt
```

내부 동작:

```c
int fd = open("output.txt", O_WRONLY | O_CREAT | O_TRUNC);
dup2(fd, 1);   // fd를 FD 1(stdout)에 복제
close(fd);     // 원래 fd는 닫음

// 이제 FD 1은 output.txt를 가리킴
// printf()는 FD 1로 출력 → output.txt에 기록
```

```
Before dup2():                    After dup2():
┌───────────────┐                ┌───────────────┐
│ FD Table      │                │ FD Table      │
│ ─────────────│                │ ─────────────│
│ 0 → stdin     │                │ 0 → stdin     │
│ 1 → terminal  │  ──dup2()──▶  │ 1 → output.txt│
│ 2 → stderr    │                │ 2 → stderr    │
│ 3 → output.txt│                │ (3 closed)    │
└───────────────┘                └───────────────┘
```

**왜 dup2()를 사용하나?**
- 기존 FD 번호를 유지하면서 타깃 변경 가능
- 프로그램 코드 수정 없이 I/O 대상 변경
- 쉘의 리다이렉션, 파이프 구현의 핵심

### 파이프 (Pipe)

```bash
$ ls -la | grep "txt"
```

내부 구조:

```
┌─────────────────┐        ┌─────────────────┐
│    ls -la       │        │  grep "txt"     │
│                 │        │                 │
│ stdout(FD 1) ───┼───────▶│ stdin(FD 0)     │
│   (write end)   │  PIPE  │   (read end)    │
└─────────────────┘        └─────────────────┘
```

```c
int pipefd[2];
pipe(pipefd);  // pipefd[0]: 읽기, pipefd[1]: 쓰기

if (fork() == 0) {
    // 자식 (ls)
    close(pipefd[0]);      // 읽기 끝 닫음
    dup2(pipefd[1], 1);    // stdout을 파이프 쓰기로
    exec("ls", "-la");
} else {
    // 부모 (grep)
    close(pipefd[1]);      // 쓰기 끝 닫음
    dup2(pipefd[0], 0);    // stdin을 파이프 읽기로
    exec("grep", "txt");
}
```

**왜 파이프도 FD인가?**
- Unix "Everything is a file" 철학
- 프로그램 입장에서 파이프/파일/소켓 구분 없이 read/write 가능
- 조합 가능성: 어떤 프로그램이든 파이프로 연결 가능

---

## 파일 디스크립터 제한

### 확인 및 설정

```bash
# 현재 프로세스의 FD 제한 확인
$ ulimit -n
1024

# soft limit 변경 (일시적)
$ ulimit -n 65536

# hard limit 확인
$ ulimit -Hn
1048576

# /etc/security/limits.conf (영구 설정)
* soft nofile 65536
* hard nofile 1048576
```

### 시스템 전체 제한

```bash
# 시스템 전체 최대 FD 수
$ cat /proc/sys/fs/file-max
9223372036854775807

# 현재 사용 중인 FD 수
$ cat /proc/sys/fs/file-nr
1234    0    9223372036854775807
# 할당된 FD / 사용 가능 FD / 최대 FD
```

**왜 제한이 필요한가?**
- 메모리 보호: FD마다 커널 메모리 사용
- DoS 방지: 악의적 프로그램이 FD를 무한 생성하는 것 방지
- 시스템 안정성: 자원 고갈로 인한 시스템 전체 영향 방지

---

## FD 누수 (File Descriptor Leak)

### 문제 패턴

**비권장 (X): close() 누락**

```java
// FD 누수 발생
public void readFile(String path) throws IOException {
    FileInputStream fis = new FileInputStream(path);
    byte[] data = new byte[1024];
    fis.read(data);
    // close() 호출 안 함 → FD 누수!
}
```

**왜 문제인가?**
- 메서드 호출마다 FD 1개씩 누적
- ulimit 도달 시 "Too many open files" 에러
- 서버 애플리케이션에서 치명적 (장시간 운영 시)

### 권장 패턴

**권장 (O): try-with-resources**

```java
// Java 7+: AutoCloseable 자동 close
public void readFile(String path) throws IOException {
    try (FileInputStream fis = new FileInputStream(path)) {
        byte[] data = new byte[1024];
        fis.read(data);
    }  // 자동으로 fis.close() 호출
}
```

**권장 (O): finally 블록**

```java
// Java 7 이전
public void readFile(String path) throws IOException {
    FileInputStream fis = null;
    try {
        fis = new FileInputStream(path);
        byte[] data = new byte[1024];
        fis.read(data);
    } finally {
        if (fis != null) {
            fis.close();  // 예외 발생해도 반드시 실행
        }
    }
}
```

### FD 누수 디버깅

```bash
# 특정 프로세스의 열린 FD 목록
$ ls -la /proc/<PID>/fd

# 또는 lsof 사용
$ lsof -p <PID>

# FD 수 카운트
$ ls /proc/<PID>/fd | wc -l
```

---

## 예제 코드

### C - 기본 파일 작업

```c
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

int main() {
    // 파일 열기 (FD 반환)
    int fd = open("test.txt", O_RDWR | O_CREAT, 0644);
    if (fd == -1) {
        perror("open failed");
        return 1;
    }

    printf("Opened file with FD: %d\n", fd);  // 보통 3

    // 쓰기
    const char* msg = "Hello, FD!\n";
    write(fd, msg, strlen(msg));

    // 오프셋 이동 (파일 시작으로)
    lseek(fd, 0, SEEK_SET);

    // 읽기
    char buf[128];
    ssize_t n = read(fd, buf, sizeof(buf) - 1);
    buf[n] = '\0';
    printf("Read: %s", buf);

    // 파일 닫기 (FD 반환)
    close(fd);

    return 0;
}
```

### Java - 저수준 FD 접근

```java
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

public class FileDescriptorExample {
    public static void main(String[] args) throws Exception {
        // 표준 FD 확인
        System.out.println("stdin FD: " + getFdNumber(FileDescriptor.in));   // 0
        System.out.println("stdout FD: " + getFdNumber(FileDescriptor.out)); // 1
        System.out.println("stderr FD: " + getFdNumber(FileDescriptor.err)); // 2

        // 파일 열기
        try (FileInputStream fis = new FileInputStream("test.txt")) {
            FileDescriptor fd = fis.getFD();
            System.out.println("File FD: " + getFdNumber(fd));  // 보통 3 이상
        }
    }

    // 리플렉션으로 FD 번호 추출 (디버깅 용도)
    private static int getFdNumber(FileDescriptor fd) throws Exception {
        Field field = FileDescriptor.class.getDeclaredField("fd");
        field.setAccessible(true);
        return field.getInt(fd);
    }
}
```

### 소켓과 FD

```java
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFdExample {
    public static void main(String[] args) throws Exception {
        // 서버 소켓도 FD 할당받음
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server listening...");

            // 클라이언트 연결마다 새 FD
            try (Socket client = server.accept()) {
                System.out.println("Client connected");
                // client도 FD를 가짐
            }  // close() → FD 반환
        }
    }
}
```

---

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 통일된 I/O 인터페이스 (파일, 소켓, 파이프) | FD 누수 시 자원 고갈 |
| 정수 기반으로 빠른 접근 | 제한된 개수 (ulimit) |
| 프로세스 간 격리 | 3단계 테이블 구조의 복잡성 |
| 리다이렉션/파이프 구현 용이 | fork() 시 의도치 않은 공유 가능 |

---

## 면접 예상 질문

### Q: 파일 디스크립터란 무엇인가요?

**A:** 프로세스가 파일, 소켓, 파이프 등 I/O 자원에 접근하기 위해 운영체제로부터 할당받는 음이 아닌 정수입니다. 커널 내부의 복잡한 자료구조를 추상화하여 프로세스에게 단순한 핸들로 제공합니다.

**왜 정수인가?**
- 보안: 커널 주소를 직접 노출하지 않음
- 효율: 정수 연산과 배열 인덱싱이 빠름
- 추상화: 파일/소켓/파이프 구분 없이 동일한 read/write 인터페이스

---

### Q: 0, 1, 2번 FD의 의미는?

**A:**
- 0: stdin (표준 입력) - 기본적으로 키보드
- 1: stdout (표준 출력) - 기본적으로 화면, 버퍼링 됨
- 2: stderr (표준 에러) - 기본적으로 화면, 버퍼링 없음

**왜 stdout과 stderr이 분리되어 있나?**
- 정상 출력과 에러 출력을 별도로 리다이렉션 가능
- stderr은 버퍼링 없이 즉시 출력되어 디버깅에 유리
- 예: `program > output.txt 2> error.txt`

---

### Q: "Too many open files" 에러의 원인과 해결책은?

**A:** 프로세스가 ulimit 제한(기본 1024)을 초과하여 FD를 열려고 할 때 발생합니다.

**원인:**
- FD 누수: close() 없이 계속 open()
- 동시 연결 과다: 웹서버에서 많은 클라이언트 연결

**해결책:**
1. **코드 수정**: try-with-resources로 자동 close() 보장
2. **ulimit 증가**: `ulimit -n 65536`
3. **모니터링**: `lsof -p <PID>`로 열린 파일 추적
4. **커넥션 풀링**: DB/HTTP 연결 재사용

---

### Q: fork() 후 FD는 어떻게 되나요?

**A:** 자식 프로세스는 부모의 FD 테이블을 복사받아 같은 Open File Table 엔트리를 공유합니다.

```
fork() 전:                    fork() 후:
┌─────────────┐               ┌─────────────┐    ┌─────────────┐
│ Parent      │               │ Parent      │    │ Child       │
│ FD 3 ───────┼──┐            │ FD 3 ───────┼──┐ │ FD 3 ───────┼──┐
└─────────────┘  │            └─────────────┘  │ └─────────────┘  │
                 │                             │                  │
                 ▼                             └────────┬─────────┘
          ┌───────────┐                                 ▼
          │ Open File │                          ┌───────────┐
          │ Table     │                          │ Open File │
          │ offset: 0 │                          │ Table     │
          └───────────┘                          │ offset: 0 │
                                                 │ (공유!)   │
                                                 └───────────┘
```

**의미:**
- 오프셋이 공유됨: 부모가 100바이트 읽으면 자식도 100부터 시작
- close()는 독립: 한쪽에서 close()해도 다른 쪽은 계속 사용 가능
- 파이프 구현의 핵심: fork() 후 불필요한 끝(read/write)을 close()

---

### Q: 소켓도 파일 디스크립터인가요?

**A:** 네. Unix의 "Everything is a file" 철학에 따라 소켓도 FD로 추상화됩니다.

**장점:**
- 동일한 read/write 시스템 콜 사용
- select/poll/epoll로 파일과 소켓 함께 모니터링 가능
- 리다이렉션 가능: 네트워크 데이터를 파일처럼 다룸

```c
int sockfd = socket(AF_INET, SOCK_STREAM, 0);  // FD 반환
// 파일처럼 read/write 가능
read(sockfd, buffer, size);
write(sockfd, data, len);
close(sockfd);  // FD 반환
```

---

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로세스와 스레드](./process-vs-thread.md) | FD 테이블은 프로세스별로 관리됨 (선수 지식) | Beginner |
| [파일시스템](./file-system.md) | FD가 inode를 참조하는 구조 | Intermediate |
| [메모리 관리](./memory-management.md) | 페이지 캐시와 파일 I/O의 관계 | Advanced |

## 참고 자료

- Advanced Programming in the UNIX Environment (W. Richard Stevens)
- The Linux Programming Interface (Michael Kerrisk)
- [Linux man pages - open(2)](https://man7.org/linux/man-pages/man2/open.2.html)
- [Linux man pages - dup2(2)](https://man7.org/linux/man-pages/man2/dup.2.html)
