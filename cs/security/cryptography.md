# 암호화

> [3] 중급 · 선수 지식: [인증과 인가](./authentication-authorization.md)

> 데이터를 제3자가 읽을 수 없도록 변환하여 기밀성, 무결성, 인증을 보장하는 기술

## 왜 알아야 하는가?

- **실무**: HTTPS 통신, 비밀번호 저장, 데이터베이스 암호화, 파일 보호 등 모든 보안 구현의 기초입니다. 잘못된 암호화 방식을 선택하면(예: MD5로 비밀번호 저장) 보안 사고로 이어집니다.
- **면접**: "대칭키와 비대칭키의 차이", "왜 비밀번호는 bcrypt로 저장하나요", "HTTPS는 어떻게 안전한가요"는 백엔드/보안 면접의 필수 질문입니다. 암호화 알고리즘의 동작 원리와 사용 시나리오를 이해해야 답변할 수 있습니다.
- **기반 지식**: HTTPS/TLS, OAuth, JWT, 블록체인 등 대부분의 보안 기술은 암호화를 기반으로 합니다. 암호화 원리를 모르면 이런 기술들의 작동 방식을 이해할 수 없습니다.

## 핵심 개념

- **대칭키 암호화**: 암호화와 복호화에 동일한 키 사용 (AES, ChaCha20)
- **비대칭키 암호화**: 공개키로 암호화, 개인키로 복호화 (RSA, ECC)
- **해시 함수**: 임의 길이 데이터를 고정 길이 다이제스트로 변환, 복호화 불가 (SHA-256, bcrypt)
- **Salt**: 해시에 무작위 값을 추가하여 레인보우 테이블 공격 방어
- **키 교환**: 안전하지 않은 채널에서 암호화 키를 공유하는 방법 (Diffie-Hellman)

## 쉽게 이해하기

**암호화**를 자물쇠에 비유할 수 있습니다.

- **대칭키 암호화**: 집 현관문의 일반 자물쇠. 열쇠 1개로 잠그고 풀기. 빠르고 간단하지만, 열쇠를 상대방에게 안전하게 전달하는 것이 문제. 열쇠를 분실하면 다른 사람도 문을 열 수 있음.

- **비대칭키 암호화**: 우편함. 누구나 편지를 넣을 수 있지만(공개키), 열쇠는 주인만 가지고 있음(개인키). 공개키를 공개해도 안전하지만, 계산이 복잡해서 느림.

- **해시**: 고기를 갈아서 햄버거 패티로 만드는 것. 패티를 보고 원래 고기 조각을 복원할 수 없음. 항상 같은 고기를 갈면 같은 패티가 나옴. 비밀번호를 저장할 때 원본 대신 해시 값만 저장.

- **Salt**: 고기를 갈 때 비밀 양념을 추가하는 것. 같은 고기라도 양념이 다르면 맛(해시 값)이 달라짐. 같은 비밀번호라도 Salt가 다르면 다른 해시 값 생성.

## 상세 설명

### 대칭키 암호화

**동작 방식**:
- 암호화: `암호문 = Encrypt(평문, 키)`
- 복호화: `평문 = Decrypt(암호문, 키)`
- 같은 키를 사용

**왜 대칭키 암호화를 사용하는가?**

비대칭키 암호화보다 수백 배 빠릅니다. 왜냐하면 XOR, 치환(Substitution), 전치(Permutation) 같은 단순한 연산만 사용하기 때문입니다. 대용량 데이터 암호화(파일, 디스크, 네트워크 트래픽)는 속도가 중요하므로 대칭키를 사용합니다.

**주요 알고리즘**:

1. **AES (Advanced Encryption Standard)**:
   - 키 길이: 128, 192, 256비트
   - 블록 크기: 128비트
   - 현재 표준, 가장 널리 사용

```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

// AES-GCM 암호화 (권장)
public class AESExample {
    public static byte[] encrypt(String plaintext, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128-bit 인증 태그
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(plaintext.getBytes());
    }

    public static String decrypt(byte[] ciphertext, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return new String(cipher.doFinal(ciphertext));
    }
}
```

**왜 AES-GCM을 사용하는가?**

GCM(Galois/Counter Mode)은 암호화와 동시에 인증(AEAD: Authenticated Encryption with Associated Data)을 제공합니다. 암호문이 변조되면 복호화 시 에러가 발생하여 무결성을 보장합니다. CBC 모드는 Padding Oracle Attack에 취약하지만, GCM은 안전합니다.

2. **ChaCha20-Poly1305**:
   - AES보다 소프트웨어 구현 성능이 좋음
   - 모바일, IoT 디바이스에 적합

**대칭키 암호화의 문제점**:

**키 배포 문제**: 송신자와 수신자가 같은 키를 공유해야 하는데, 키를 안전하게 전달하는 것이 어렵습니다. 평문으로 키를 보내면 도청당하고, 암호화해서 보내려면 또 다른 키가 필요합니다(순환 문제).

**해결**: 비대칭키로 대칭키를 암호화하여 전달. 또는 Diffie-Hellman 키 교환 프로토콜 사용.

### 비대칭키 암호화

**동작 방식**:
- 키 쌍 생성: `(공개키, 개인키)`
- 암호화: `암호문 = Encrypt(평문, 공개키)`
- 복호화: `평문 = Decrypt(암호문, 개인키)`
- 공개키는 공개, 개인키는 비밀 유지

**왜 비대칭키 암호화를 사용하는가?**

키 배포 문제를 해결합니다. 공개키는 누구나 알아도 안전하므로, 평문으로 전달해도 됩니다. 수신자의 공개키로 암호화하면 개인키를 가진 수신자만 복호화할 수 있습니다.

**주요 알고리즘**:

1. **RSA (Rivest-Shamir-Adleman)**:
   - 키 길이: 2048, 3072, 4096비트 (2048 권장)
   - 용도: 암호화, 전자서명

```java
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSAExample {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048); // 키 길이
        return generator.generateKeyPair();
    }

    public static byte[] encrypt(String plaintext, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext.getBytes());
    }

    public static String decrypt(byte[] ciphertext, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(ciphertext));
    }
}
```

**왜 OAEP 패딩을 사용하는가?**

PKCS#1 v1.5 패딩은 Chosen Ciphertext Attack에 취약합니다. OAEP(Optimal Asymmetric Encryption Padding)는 무작위성을 추가하여 같은 평문을 암호화해도 매번 다른 암호문이 생성되고, 공격을 방어합니다.

2. **ECC (Elliptic Curve Cryptography)**:
   - 키 길이: 256비트 (RSA 3072비트와 동일한 보안 강도)
   - 더 짧은 키로 동일한 보안 수준
   - 모바일, 블록체인에 적합

**비대칭키 암호화의 한계**:

**느린 속도**: RSA는 AES보다 100~1000배 느립니다. 왜냐하면 큰 소수의 거듭제곱 연산이 필요하기 때문입니다.

**해결**: **하이브리드 암호화**
1. 임의의 대칭키(AES) 생성
2. 대칭키로 대용량 데이터 암호화 (빠름)
3. 비대칭키(RSA)로 대칭키 암호화 (작은 데이터)
4. 암호화된 데이터 + 암호화된 대칭키 전송

이것이 HTTPS/TLS가 동작하는 방식입니다.

### 해시 함수

**특징**:
- **일방향**: 해시 값에서 원본 복원 불가
- **결정론적**: 같은 입력은 항상 같은 해시 값
- **고정 길이**: 입력 크기와 무관하게 고정된 길이 출력
- **충돌 저항성**: 같은 해시 값을 가진 서로 다른 입력 찾기 어려움
- **눈사태 효과**: 입력이 1비트만 바뀌어도 해시 값 50% 변화

**왜 해시를 사용하는가?**

비밀번호를 평문으로 저장하면 DB 유출 시 모든 계정이 탈취됩니다. 해시 값만 저장하면 해시 값을 알아도 원본 비밀번호를 알 수 없습니다. 로그인 시 입력된 비밀번호를 해시하여 DB의 해시 값과 비교합니다.

**주요 알고리즘**:

1. **SHA-256 (Secure Hash Algorithm)**:
   - 출력: 256비트 (64자리 16진수)
   - 용도: 파일 무결성 검증, 블록체인

```java
import java.security.MessageDigest;

public class SHA256Example {
    public static String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());

        // 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
```

**왜 SHA-1이 아닌 SHA-256을 사용하는가?**

SHA-1은 충돌이 발견되어 더 이상 안전하지 않습니다(2017년 Google SHAttered 공격). SHA-256은 현재 안전한 것으로 알려져 있습니다.

2. **bcrypt**:
   - 비밀번호 해싱 전용
   - 느린 속도 (의도적)
   - Salt 자동 생성

```java
import org.mindrot.jbcrypt.BCrypt;

public class BcryptExample {
    // 비밀번호 해싱
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // work factor 12
    }

    // 비밀번호 검증
    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
```

**왜 SHA-256이 아닌 bcrypt를 사용하는가?**

SHA-256은 너무 빠릅니다. GPU로 초당 수십억 번 해시를 계산할 수 있어 무차별 대입 공격에 취약합니다. bcrypt는 의도적으로 느리게 설계되어(work factor로 조절), 공격자가 많은 비밀번호를 시도하기 어렵습니다.

**대안**: Argon2 (최신, 메모리 사용량도 조절 가능)

### Salt

**왜 Salt가 필요한가?**

같은 비밀번호는 같은 해시 값을 가집니다. 공격자가 자주 사용되는 비밀번호의 해시 값을 미리 계산해둔 테이블(레인보우 테이블)을 만들면, DB를 탈취했을 때 즉시 비밀번호를 찾을 수 있습니다.

**동작 방식**:
1. 사용자별로 무작위 Salt 생성
2. `해시값 = Hash(비밀번호 + Salt)`
3. `Salt + 해시값`을 DB에 저장
4. 로그인 시: DB에서 Salt 조회 → `Hash(입력 비밀번호 + Salt)` 계산 → DB 해시값과 비교

**왜 효과적인가?**

같은 비밀번호 `1234`라도:
- 사용자 A: Salt=`abc123` → Hash(`1234abc123`) = `x1y2z3...`
- 사용자 B: Salt=`xyz789` → Hash(`1234xyz789`) = `a9b8c7...`

서로 다른 해시 값이 저장되므로, 레인보우 테이블이 무용지물이 됩니다. 공격자는 각 사용자별로 새로운 레인보우 테이블을 만들어야 하므로 현실적으로 불가능합니다.

**Salt의 조건**:
- 충분한 길이 (최소 16바이트)
- 암호학적으로 안전한 난수 생성 (`SecureRandom`)
- 사용자별로 고유 (재사용 금지)
- DB에 평문으로 저장 (비밀 아님)

```java
import java.security.SecureRandom;

public class SaltExample {
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128비트
        random.nextBytes(salt);
        return salt;
    }
}
```

**왜 Salt는 비밀이 아닌가?**

Salt의 목적은 레인보우 테이블을 무력화하는 것이지, 해시 값 자체를 숨기는 것이 아닙니다. Salt가 공개되어도 공격자는 각 사용자별로 무차별 대입 공격을 해야 하므로 시간이 기하급수적으로 증가합니다.

### 키 교환 (Diffie-Hellman)

**문제**: 도청 가능한 채널에서 어떻게 비밀 키를 공유하는가?

**Diffie-Hellman 동작 방식**:
1. Alice와 Bob이 공개 값 `g`, `p` 합의 (소수)
2. Alice: 비밀 값 `a` 선택 → `A = g^a mod p` 계산 → Bob에게 전송
3. Bob: 비밀 값 `b` 선택 → `B = g^b mod p` 계산 → Alice에게 전송
4. Alice: `s = B^a mod p` 계산
5. Bob: `s = A^b mod p` 계산
6. 둘 다 같은 `s` 값을 얻음 (`s = g^(ab) mod p`)

**왜 안전한가?**

공격자는 `A`, `B`, `g`, `p`를 모두 알지만, `a`와 `b`를 모릅니다. `A`에서 `a`를 계산하는 것(이산 로그 문제)은 현재 컴퓨터로 수백 년이 걸립니다.

## 트레이드오프

### 대칭키 vs 비대칭키

| 구분 | 대칭키 | 비대칭키 |
|------|--------|----------|
| 속도 | 매우 빠름 | 느림 (100~1000배) |
| 키 관리 | 어려움 (안전한 공유 필요) | 쉬움 (공개키 공개 가능) |
| 키 길이 | 짧음 (128~256비트) | 길음 (2048~4096비트) |
| 용도 | 대용량 데이터 암호화 | 키 교환, 전자서명 |
| 확장성 | 나쁨 (N명이면 N(N-1)/2개 키) | 좋음 (N명이면 2N개 키) |

**결론**: 하이브리드 사용. 비대칭키로 대칭키 전달, 대칭키로 데이터 암호화.

### SHA-256 vs bcrypt (비밀번호)

| 구분 | SHA-256 | bcrypt |
|------|---------|--------|
| 속도 | 매우 빠름 (초당 수십억 해시) | 느림 (의도적, 조절 가능) |
| Salt | 수동 구현 필요 | 자동 생성 |
| 무차별 대입 저항성 | 약함 | 강함 |
| 용도 | 파일 무결성, 블록체인 | 비밀번호 해싱 |

**결론**: 비밀번호는 bcrypt/Argon2, 파일 무결성은 SHA-256.

## 면접 예상 질문

- Q: 대칭키와 비대칭키 암호화의 차이는 무엇이며, 각각 언제 사용하나요?
  - A: 대칭키는 암호화와 복호화에 같은 키를 사용하고, 비대칭키는 공개키와 개인키 쌍을 사용합니다. **왜 둘 다 필요한가?** 대칭키는 빠르지만 키 배포가 어렵고, 비대칭키는 키 배포가 쉽지만 느립니다. **따라서** HTTPS에서는 하이브리드 방식을 사용합니다. RSA로 AES 대칭키를 암호화하여 전달하고(핸드셰이크), 실제 데이터는 AES로 암호화합니다(통신). 이렇게 하면 보안(비대칭키)과 성능(대칭키)을 모두 챙길 수 있습니다.

- Q: 비밀번호를 저장할 때 왜 SHA-256이 아닌 bcrypt를 사용해야 하나요?
  - A: SHA-256은 너무 빠르기 때문입니다. **왜 빠른 것이 문제인가?** GPU로 초당 수십억 번 해시를 계산할 수 있어, 공격자가 무차별 대입 공격으로 짧은 시간에 많은 비밀번호를 시도할 수 있습니다. bcrypt는 의도적으로 느리게 설계되어(work factor로 속도 조절), 공격자가 많은 시도를 하기 어렵습니다. 예를 들어 SHA-256은 1초에 10억 번 시도 가능하지만, bcrypt(work factor 12)는 1초에 수백 번만 가능합니다. 또한 bcrypt는 Salt를 자동으로 생성하고 해시 값에 포함하여 관리가 간편합니다.

- Q: Salt는 무엇이며, 왜 필요한가요?
  - A: Salt는 비밀번호 해싱 시 추가하는 무작위 값입니다. **왜 필요한가?** 같은 비밀번호는 같은 해시 값을 가지므로, 공격자가 자주 사용되는 비밀번호의 해시 값을 미리 계산한 레인보우 테이블을 만들 수 있습니다. Salt를 추가하면 같은 비밀번호라도 사용자별로 다른 해시 값이 생성되어, 레인보우 테이블이 무용지물이 됩니다. **Salt는 비밀이 아닙니다.** DB에 평문으로 저장해도 되는 이유는, Salt의 목적이 레인보우 테이블을 무력화하는 것이지 해시 값을 숨기는 것이 아니기 때문입니다. Salt가 공개되어도 공격자는 각 사용자별로 무차별 대입 공격을 해야 하므로 시간이 기하급수적으로 증가합니다.

- Q: HTTPS에서 왜 대칭키와 비대칭키를 함께 사용하나요?
  - A: 비대칭키(RSA)만 사용하면 너무 느리고, 대칭키(AES)만 사용하면 키 배포가 어렵기 때문입니다. **하이브리드 방식**: TLS 핸드셰이크에서 서버의 공개키(인증서)로 클라이언트가 생성한 임의의 대칭키를 암호화하여 전달합니다. 이후 실제 데이터는 이 대칭키로 암호화합니다. **왜 효과적인가?** 비대칭키는 작은 데이터(대칭키)만 암호화하므로 속도 문제가 없고, 대칭키는 안전하게 전달되었으므로 키 배포 문제가 없습니다. 이렇게 보안과 성능을 모두 달성합니다.

- Q: AES-CBC와 AES-GCM의 차이는 무엇이며, 왜 GCM을 권장하나요?
  - A: CBC(Cipher Block Chaining)는 암호화만 제공하고, GCM(Galois/Counter Mode)은 암호화와 인증을 함께 제공합니다(AEAD). **왜 인증이 필요한가?** 암호화는 기밀성만 보장하므로, 공격자가 암호문을 변조해도 탐지할 수 없습니다. Padding Oracle Attack처럼 암호문을 조작하여 평문 정보를 유출할 수 있습니다. GCM은 인증 태그를 생성하여 암호문이 변조되면 복호화 시 에러가 발생하므로 무결성을 보장합니다. **따라서** 최신 시스템에서는 AES-GCM, ChaCha20-Poly1305 같은 AEAD 암호를 권장합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [인증과 인가](./authentication-authorization.md) | 비밀번호 해싱, 토큰 서명에 암호화 기술 활용 (선수 지식) | Beginner |
| [HTTPS와 TLS](./https-tls.md) | TLS 핸드셰이크에서 대칭키/비대칭키 암호화 사용 | Advanced |
| [OAuth 2.0과 JWT](./oauth-jwt.md) | JWT 서명에 HMAC/RSA 암호화 알고리즘 사용 | Advanced |
| [웹 보안](./web-security.md) | 암호화는 보안의 기술적 구현 수단 | Intermediate |

## 참고 자료

- [NIST Cryptographic Standards](https://csrc.nist.gov/projects/cryptographic-standards-and-guidelines)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [RFC 5869 - HKDF (HMAC-based Key Derivation Function)](https://datatracker.ietf.org/doc/html/rfc5869)
- [Serious Cryptography by Jean-Philippe Aumasson](https://nostarch.com/seriouscrypto)
