# Java 직렬화 (Serialization)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](../what-is-language.md)

> 객체를 바이트 스트림으로 변환하여 저장하거나 전송할 수 있게 하는 기능

`#직렬화` `#Serialization` `#역직렬화` `#Deserialization` `#Java` `#Serializable` `#transient` `#serialVersionUID` `#ObjectOutputStream` `#ObjectInputStream` `#바이트스트림` `#ByteStream` `#영속화` `#Persistence` `#JSON` `#Jackson` `#Gson` `#네트워크전송` `#캐시` `#세션` `#RMI` `#Externalizable`

## 왜 알아야 하는가?

객체를 파일에 저장하거나 네트워크로 전송하려면 직렬화가 필요합니다. 세션 저장, 캐시, 메시지 큐(Kafka) 등에서 사용됩니다. 하지만 보안 취약점과 버전 호환성 문제가 있어 JSON, Protocol Buffers 같은 대안이 권장됩니다.

## 핵심 개념

- **직렬화 (Serialization)**: 객체 → 바이트 스트림
- **역직렬화 (Deserialization)**: 바이트 스트림 → 객체
- **Serializable**: 직렬화 가능 마커 인터페이스
- **transient**: 직렬화 제외 키워드
- **serialVersionUID**: 버전 호환성 검증 ID

## 쉽게 이해하기

**직렬화**를 이사짐 포장에 비유할 수 있습니다.

```
직렬화 = 이사짐 포장

┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  객체 (가구, 가전)                                           │
│       ↓ 직렬화 (포장)                                        │
│  바이트 스트림 (포장된 박스)                                  │
│       ↓ 전송/저장 (트럭 이동)                                │
│  바이트 스트림                                               │
│       ↓ 역직렬화 (개봉)                                      │
│  객체 복원 (새 집에 가구 배치)                               │
│                                                              │
└─────────────────────────────────────────────────────────────┘

주의: 포장 명세서(serialVersionUID)가 다르면 개봉 불가!
```

## 상세 설명

### 기본 직렬화

```java
// Serializable 인터페이스 구현
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private transient String password;  // 직렬화 제외

    // 생성자, getter, setter
}

// 직렬화 (객체 → 바이트)
User user = new User("Alice", 25, "secret123");

try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("user.ser"))) {
    oos.writeObject(user);
}

// 역직렬화 (바이트 → 객체)
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("user.ser"))) {
    User loaded = (User) ois.readObject();
    System.out.println(loaded.getName());     // Alice
    System.out.println(loaded.getPassword()); // null (transient)
}
```

### serialVersionUID

```java
public class User implements Serializable {
    // 명시적 선언 (권장)
    private static final long serialVersionUID = 1L;

    private String name;
}

// serialVersionUID가 다르면 역직렬화 실패
// InvalidClassException: local class incompatible:
// stream classdesc serialVersionUID = 1,
// local class serialVersionUID = 2

// 자동 생성 (비권장)
// IDE나 serialver 도구로 생성
// $ serialver User
// User: static final long serialVersionUID = 123456789L;
```

### transient 키워드

```java
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private double balance;

    // 직렬화에서 제외할 필드
    private transient String password;      // 보안 데이터
    private transient Connection connection; // 리소스
    private transient Logger logger;         // 유틸리티

    // 역직렬화 후 transient 필드 초기화
    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.logger = LoggerFactory.getLogger(Account.class);
    }
}
```

### 커스텀 직렬화

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String encryptedPassword;

    // 커스텀 직렬화
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();  // 기본 필드 직렬화
        // 추가 데이터 쓰기
        oos.writeObject(encrypt(encryptedPassword));
    }

    // 커스텀 역직렬화
    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();  // 기본 필드 역직렬화
        // 추가 데이터 읽기
        String encrypted = (String) ois.readObject();
        this.encryptedPassword = decrypt(encrypted);
    }

    // 역직렬화 후 검증
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("스트림 데이터 필요");
    }
}
```

### Externalizable 인터페이스

```java
// 완전한 제어가 필요할 때 (성능 최적화)
public class User implements Externalizable {
    private String name;
    private int age;

    // 기본 생성자 필수!
    public User() { }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.name = in.readUTF();
        this.age = in.readInt();
    }
}
```

### 직렬화 보안 문제

```java
// 보안 취약점: 악의적인 바이트 스트림으로 공격 가능

// 1. ObjectInputFilter (Java 9+)
ObjectInputStream ois = new ObjectInputStream(fis);
ois.setObjectInputFilter(filterInfo -> {
    Class<?> clazz = filterInfo.serialClass();
    if (clazz != null) {
        // 허용된 클래스만
        if (clazz == User.class || clazz == String.class) {
            return ObjectInputFilter.Status.ALLOWED;
        }
        return ObjectInputFilter.Status.REJECTED;
    }
    return ObjectInputFilter.Status.UNDECIDED;
});

// 2. 역직렬화 대신 JSON 사용 (권장)
ObjectMapper mapper = new ObjectMapper();
String json = mapper.writeValueAsString(user);
User loaded = mapper.readValue(json, User.class);
```

### 대안: JSON 직렬화

```java
// Jackson
ObjectMapper mapper = new ObjectMapper();

// 직렬화
String json = mapper.writeValueAsString(user);
// {"name":"Alice","age":25}

// 역직렬화
User loaded = mapper.readValue(json, User.class);

// Gson
Gson gson = new Gson();

String json = gson.toJson(user);
User loaded = gson.fromJson(json, User.class);
```

### 직렬화 vs JSON 비교

| 항목 | Java 직렬화 | JSON |
|------|------------|------|
| 형식 | 바이너리 | 텍스트 |
| 크기 | 작음 | 큼 |
| 속도 | 빠름 | 느림 |
| 가독성 | 없음 | 높음 |
| 언어 호환 | Java만 | 언어 무관 |
| 보안 | 취약 | 상대적 안전 |
| 버전 관리 | 어려움 | 유연함 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 객체 상태 완벽 보존 | 보안 취약점 |
| Java 네이티브 | 버전 호환성 문제 |
| 참조 관계 유지 | Java 전용 |
| 기본 제공 | 클래스 구조 의존 |

## 면접 예상 질문

### Q: Java 직렬화의 문제점과 대안은?

A: **문제점**: (1) **보안**: 역직렬화 공격 취약 (2) **호환성**: 클래스 변경 시 serialVersionUID 불일치 (3) **Java 종속**: 다른 언어와 통신 불가 (4) **크기**: 메타데이터로 인한 오버헤드. **대안**: JSON (Jackson, Gson), Protocol Buffers, Avro. **Effective Java**: "직렬화의 대안을 찾으라" (Item 85).

### Q: serialVersionUID를 명시해야 하는 이유는?

A: 명시하지 않으면 컴파일러가 클래스 구조 기반으로 자동 생성. 클래스 수정 시 UID가 바뀌어 기존 직렬화 데이터 역직렬화 실패. **명시적 선언 이점**: (1) 호환 가능한 변경에서 역직렬화 성공 (2) 의도적 비호환 시 UID 변경으로 명확한 실패. **규칙**: 처음부터 선언, 변경 시 신중히.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로그래밍 언어란](../what-is-language.md) | 선수 지식 | [1] 기초 |
| [Redis Caching](../../db/redis-caching.md) | 캐시 직렬화 | [3] 중급 |

## 참고 자료

- [Java Object Serialization - Oracle](https://docs.oracle.com/javase/8/docs/technotes/guides/serialization/)
- [Effective Java - Item 85-90](https://www.oreilly.com/library/view/effective-java/9780134686097/)
