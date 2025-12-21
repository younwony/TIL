# OAuth 2.0과 JWT

> [4] 심화 · 선수 지식: [인증과 인가](./authentication-authorization.md), [암호화](./cryptography.md)

> OAuth 2.0은 제3자 애플리케이션이 사용자 리소스에 안전하게 접근하도록 허용하는 인가 프레임워크이며, JWT는 정보를 안전하게 전송하기 위한 토큰 포맷

## 왜 알아야 하는가?

- **실무**: OAuth는 "구글 로그인", "카카오 로그인" 같은 소셜 로그인 구현의 표준이고, JWT는 MSA 환경에서 토큰 기반 인증의 사실상 표준입니다. 대부분의 현대 웹/모바일 애플리케이션은 이 두 기술을 사용합니다.
- **면접**: "OAuth와 JWT의 차이", "Authorization Code Flow 과정", "JWT를 무효화하는 방법", "Access Token과 Refresh Token을 나누는 이유"는 백엔드 개발자 면접의 고급 질문입니다. API 설계 능력을 평가하는 지표입니다.
- **기반 지식**: 토큰 기반 인증, MSA 환경의 인증/인가, API 보안의 기초가 됩니다. OAuth와 JWT를 이해해야 OpenID Connect, SAML, API Gateway 등 고급 주제로 확장할 수 있습니다.

## 핵심 개념

- **OAuth 2.0**: 인가(Authorization) 프레임워크로, 비밀번호 공유 없이 리소스 접근 권한 위임
- **인가 플로우**: 상황에 따라 다른 토큰 발급 방식 (Authorization Code, Implicit, Client Credentials 등)
- **JWT (JSON Web Token)**: JSON 기반의 자체 포함형(Self-contained) 토큰
- **Access Token**: 리소스 접근에 사용하는 단기 토큰 (15분~1시간)
- **Refresh Token**: Access Token 갱신에 사용하는 장기 토큰 (7일~30일)

## 쉽게 이해하기

**OAuth 2.0**을 호텔 키 카드에 비유할 수 있습니다.

당신이 호텔에 투숙하면 프론트에서 키 카드를 받습니다. 이 키 카드로:
- **자신의 방**: 무제한 출입 가능 (Resource Owner = 사용자)
- **피트니스센터**: 투숙 기간 동안만 이용 가능 (Client = 제3자 앱)
- **직원 전용 구역**: 접근 불가 (Scope = 권한 범위)

프론트 데스크가 **인가 서버(Authorization Server)**이고, 키 카드가 **Access Token**입니다. 키 카드를 잃어버려도 비밀번호(마스터키)는 안전하고, 프론트에서 즉시 무효화할 수 있습니다.

**JWT**는 신분증 + 자격증이 적힌 카드입니다.

일반 출입증은 번호만 적혀 있어, 경비원이 매번 중앙 DB를 조회해야 합니다. 반면 JWT는 "홍길동, 소프트웨어팀, READ/WRITE 권한"이 카드에 직접 적혀 있어, 경비원이 카드만 보고 판단할 수 있습니다. 단, 한 번 발급하면 수정할 수 없으므로 유효기간을 짧게 설정합니다.

## 상세 설명

### OAuth 2.0 개념

**왜 OAuth 2.0이 필요한가?**

예를 들어 사진 인쇄 서비스가 사용자의 Google 포토에 접근해야 한다면:
- **문제 1**: 사용자가 Google 비밀번호를 인쇄 서비스에 알려줘야 함 (보안 위험)
- **문제 2**: 인쇄 서비스가 Google의 모든 기능에 접근 가능 (과도한 권한)
- **문제 3**: 비밀번호 변경 시 인쇄 서비스 재설정 필요

**OAuth 2.0 해결책**:
- 비밀번호 공유 없이 **제한된 권한**만 위임
- 사용자가 언제든지 권한 철회 가능
- 토큰 만료 시 자동 차단

### OAuth 2.0 역할

1. **Resource Owner (리소스 소유자)**: 사용자
2. **Client (클라이언트)**: 제3자 애플리케이션 (예: 사진 인쇄 서비스)
3. **Authorization Server (인가 서버)**: 토큰 발급 서버 (예: Google OAuth)
4. **Resource Server (리소스 서버)**: 실제 데이터를 가진 서버 (예: Google 포토 API)

### Authorization Code Flow (가장 안전)

**언제 사용하는가?**
- 서버 사이드 웹 애플리케이션
- 클라이언트 시크릿을 안전하게 저장 가능

**동작 순서**:

```
1. 사용자 → Client: "Google 포토 연동하기" 클릭

2. Client → 사용자: 인가 서버로 리다이렉트
   https://accounts.google.com/oauth/authorize?
     client_id=ABC123&
     redirect_uri=https://printapp.com/callback&
     response_type=code&
     scope=photos.read

3. 사용자 → Authorization Server: 로그인 및 권한 승인

4. Authorization Server → 사용자: Authorization Code 발급
   (리다이렉트) https://printapp.com/callback?code=XYZ789

5. Client → Authorization Server: Code를 Access Token으로 교환
   POST /token
   {
     "grant_type": "authorization_code",
     "code": "XYZ789",
     "client_id": "ABC123",
     "client_secret": "SECRET",
     "redirect_uri": "https://printapp.com/callback"
   }

6. Authorization Server → Client: Access Token + Refresh Token 발급
   {
     "access_token": "ya29.a0AfH6...",
     "refresh_token": "1//0gL8...",
     "expires_in": 3600,
     "token_type": "Bearer"
   }

7. Client → Resource Server: Access Token으로 리소스 요청
   GET /photos
   Authorization: Bearer ya29.a0AfH6...

8. Resource Server → Client: 사진 목록 반환
```

**왜 Authorization Code를 거치는가?**

Access Token을 바로 브라우저로 전달하면 URL에 노출되어 위험합니다(브라우저 히스토리, Referer 헤더). Authorization Code는 일회용이고, 서버에서만 Token으로 교환하므로 안전합니다.

**왜 client_secret이 필요한가?**

Authorization Code가 탈취되어도, client_secret 없이는 Token을 발급받을 수 없습니다. 2단계 검증으로 보안을 강화합니다.

### Implicit Flow (보안 취약, 더 이상 권장 안 함)

**언제 사용했었나?**
- SPA(Single Page Application)에서 사용
- 서버 없이 브라우저만으로 동작

**왜 더 이상 사용하지 않는가?**

Access Token이 URL Fragment(`#token=...`)로 전달되어 브라우저 히스토리, 확장 프로그램 등에 노출됩니다. 현재는 **Authorization Code + PKCE**를 SPA에서도 사용합니다.

### Client Credentials Flow

**언제 사용하는가?**
- 서버 간 통신 (Machine-to-Machine)
- 사용자 없이 클라이언트 자체의 권한으로 접근
- 예: 백엔드 서버가 외부 API 호출

**동작 순서**:

```
Client → Authorization Server: Client ID + Secret으로 토큰 요청
POST /token
{
  "grant_type": "client_credentials",
  "client_id": "ABC123",
  "client_secret": "SECRET",
  "scope": "api.read"
}

Authorization Server → Client: Access Token 발급
{
  "access_token": "2YotnFZF...",
  "expires_in": 3600,
  "token_type": "Bearer"
}
```

**왜 Refresh Token이 없는가?**

사용자가 없으므로 만료 시 재로그인이 필요 없습니다. Access Token 만료 후 client_credentials로 새로운 토큰을 즉시 발급받으면 됩니다.

### PKCE (Proof Key for Code Exchange)

**왜 필요한가?**

모바일 앱이나 SPA는 client_secret을 안전하게 저장할 수 없습니다. 앱을 디컴파일하거나 브라우저 개발자 도구로 추출 가능합니다.

**동작 방식**:

1. 클라이언트가 무작위 `code_verifier` 생성
2. `code_challenge = SHA256(code_verifier)` 계산
3. 인가 요청 시 `code_challenge` 전송
4. Token 교환 시 `code_verifier` 전송
5. 서버가 `SHA256(code_verifier) == code_challenge` 검증

**왜 안전한가?**

공격자가 Authorization Code를 가로채도, `code_verifier`를 모르면 토큰을 발급받을 수 없습니다. `code_challenge`(해시 값)에서 `code_verifier`를 역산할 수 없기 때문입니다.

### JWT (JSON Web Token)

**구조**: `Header.Payload.Signature` (Base64URL 인코딩)

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9  ← Header
.eyJzdWIiOiIxMjM0IiwibmFtZSI6IkpvaG4iLCJpYXQiOjE2MTYyMzkwMjJ9  ← Payload
.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c  ← Signature
```

**1. Header**:
```json
{
  "alg": "HS256",  // 서명 알고리즘
  "typ": "JWT"     // 토큰 타입
}
```

**2. Payload**:
```json
{
  "sub": "1234567890",      // Subject (사용자 ID)
  "name": "John Doe",       // 커스텀 클레임
  "iat": 1516239022,        // Issued At (발급 시간)
  "exp": 1516242622,        // Expiration (만료 시간)
  "iss": "https://auth.example.com",  // Issuer (발급자)
  "aud": "https://api.example.com",   // Audience (수신자)
  "scope": "read write"     // 권한
}
```

**3. Signature**:
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

**왜 JWT를 사용하는가?**

**자체 포함형(Self-contained)**: 토큰 자체에 사용자 정보와 권한이 포함되어 있어, 서버가 DB를 조회하지 않고 토큰만으로 검증 가능합니다. 세션 기반은 매 요청마다 DB에서 세션을 조회해야 하지만, JWT는 서명만 검증하면 됩니다.

**Stateless**: 서버가 토큰 상태를 저장하지 않으므로 수평 확장이 쉽습니다. 어떤 서버가 요청을 받아도 독립적으로 검증 가능합니다.

**JWT 검증 과정**:

```java
import io.jsonwebtoken.*;

public class JWTExample {
    private static final String SECRET_KEY = "mySecretKey";

    // JWT 생성
    public static String createJWT(String userId, String name) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + 3600000); // 1시간 후

        return Jwts.builder()
                .setSubject(userId)
                .claim("name", name)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT 검증
    public static Claims validateJWT(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired");
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid signature");
        }
    }
}
```

**왜 서명이 필요한가?**

Payload는 Base64 인코딩만 되어 있어 누구나 읽을 수 있습니다. 공격자가 Payload를 수정(예: `"role": "user"` → `"role": "admin"`)하면 권한 상승 공격이 가능합니다. 서명은 Header와 Payload를 Secret Key로 HMAC 해싱한 값이므로, Payload가 변조되면 서명 검증이 실패합니다.

### Access Token vs Refresh Token

**왜 두 가지 토큰이 필요한가?**

| 시나리오 | Access Token만 사용 | Access + Refresh Token |
|---------|---------------------|------------------------|
| 유효기간 짧게 (15분) | 15분마다 재로그인 (사용자 불편) | Access Token 만료 시 자동 갱신 (UX 좋음) |
| 유효기간 길게 (7일) | 탈취 시 7일간 악용 가능 (보안 취약) | Access Token 탈취해도 15분만 유효 (보안 좋음) |

**동작 방식**:

```
1. 로그인 성공
   → Access Token (15분) + Refresh Token (7일) 발급

2. API 요청
   → Authorization: Bearer {Access Token}

3. Access Token 만료 (15분 후)
   → 401 Unauthorized 응답

4. 클라이언트 자동 처리
   POST /refresh
   {
     "refresh_token": "..."
   }
   → 새로운 Access Token 발급

5. 재시도
   → Authorization: Bearer {새로운 Access Token}
```

**왜 Refresh Token은 길게 설정하는가?**

사용자가 앱을 사용 중이면 Access Token이 자동으로 갱신되므로, 7일 동안 재로그인 없이 사용할 수 있습니다. 7일 동안 미사용 시에만 재로그인이 필요합니다.

**Refresh Token 보안**:
- HttpOnly 쿠키에 저장 (XSS 방어)
- DB에 저장하여 무효화 가능 (강제 로그아웃)
- Refresh Token Rotation: 갱신 시 기존 토큰 무효화

**왜 Refresh Token Rotation이 필요한가?**

Refresh Token이 탈취되면 7일간 악용 가능합니다. Rotation을 적용하면 갱신 시마다 새로운 Refresh Token을 발급하고 기존 토큰을 무효화합니다. 공격자가 탈취한 토큰을 사용하면, 정상 사용자의 갱신 요청이 실패하여 탈취를 즉시 감지할 수 있습니다.

## 트레이드오프

### JWT vs 세션

| 구분 | JWT | 세션 |
|------|-----|------|
| 서버 저장소 | 불필요 (Stateless) | 필요 (Redis, DB) |
| 수평 확장 | 쉬움 (독립적 검증) | 어려움 (세션 공유 필요) |
| 토큰 무효화 | 어려움 (만료 전까지 유효) | 쉬움 (즉시 삭제 가능) |
| 크기 | 큼 (Payload 포함) | 작음 (Session ID만) |
| 보안 | XSS 취약 (localStorage 사용 시) | CSRF 취약 (쿠키 사용) |

**결론**:
- JWT: MSA, 모바일 API, 수평 확장 중요 시
- 세션: 모놀리식, 강력한 세션 제어 필요 시

### HS256 (대칭키) vs RS256 (비대칭키)

| 구분 | HS256 (HMAC-SHA256) | RS256 (RSA-SHA256) |
|------|---------------------|---------------------|
| 키 타입 | 대칭키 (Secret Key) | 비대칭키 (Public/Private Key) |
| 서명 | Secret Key로 서명 | Private Key로 서명 |
| 검증 | Secret Key로 검증 | Public Key로 검증 |
| 속도 | 빠름 | 느림 |
| 키 배포 | 어려움 (Secret 공유 위험) | 쉬움 (Public Key 공개 가능) |

**언제 RS256을 사용하는가?**

여러 서비스가 JWT를 검증해야 할 때. 인가 서버만 Private Key를 가지고, 각 마이크로서비스는 Public Key로 검증합니다. Secret Key를 공유할 필요가 없어 안전합니다.

## 면접 예상 질문

- Q: OAuth 2.0과 JWT의 차이는 무엇인가요?
  - A: OAuth 2.0은 **인가 프레임워크**(권한 위임 방법)이고, JWT는 **토큰 포맷**(정보 전송 형식)입니다. **왜 혼동되는가?** OAuth 2.0의 Access Token으로 JWT를 자주 사용하기 때문입니다. 하지만 OAuth 2.0은 토큰 포맷을 강제하지 않으며, Opaque Token(랜덤 문자열)을 사용할 수도 있습니다. **차이점**: OAuth 2.0은 "어떻게 권한을 위임할 것인가?"를 정의하고(Authorization Code Flow 등), JWT는 "토큰에 어떤 정보를 어떻게 담을 것인가?"를 정의합니다.

- Q: Authorization Code Flow에서 왜 Code를 거쳐서 Token을 발급하나요? 바로 Token을 주면 안 되나요?
  - A: Access Token을 바로 브라우저로 전달하면 URL에 노출되어 위험하기 때문입니다. **왜 위험한가?** 브라우저 히스토리, 서버 로그, Referer 헤더에 URL이 기록되므로 토큰이 유출될 수 있습니다. Authorization Code는 일회용이고, 서버 측에서만 Token으로 교환하므로 안전합니다. **추가 보안**: client_secret 검증으로 2단계 인증을 제공합니다. Code가 탈취되어도 client_secret 없이는 Token을 발급받을 수 없습니다.

- Q: JWT를 사용하면 서버에서 토큰을 무효화할 수 없다는 단점이 있습니다. 어떻게 해결하나요?
  - A: **1) 짧은 만료 시간**: Access Token을 15분으로 설정하여 탈취 시 피해 최소화. Refresh Token으로 자동 갱신하여 UX 유지. **2) Refresh Token을 DB에 저장**: Refresh Token은 DB에 저장하여 강제 로그아웃 시 삭제. Access Token은 15분 후 자동 만료. **3) Token Blacklist**: 무효화할 토큰 ID를 Redis에 저장. 요청마다 블랙리스트 확인. 단, Stateless 장점 상실. **4) 버전 관리**: 사용자 DB에 `token_version` 컬럼 추가. JWT에 버전 포함. 강제 로그아웃 시 버전 증가. **트레이드오프**: 완전 무효화 vs Stateless. 일반적으로 1+2번 조합 사용.

- Q: Access Token과 Refresh Token을 나누는 이유는 무엇인가요?
  - A: **보안(짧은 유효기간)과 사용자 경험(재로그인 불필요)을 모두 달성하기 위해서**입니다. Access Token만 사용하면 딜레마가 발생합니다. 유효기간을 짧게(15분) 설정하면 탈취 피해는 줄지만 사용자가 자주 재로그인해야 하고, 길게(7일) 설정하면 편하지만 탈취 시 7일간 악용 가능합니다. **해결**: Access Token(15분)은 API 요청에 사용하고, Refresh Token(7일)은 Access Token 갱신에만 사용합니다. Access Token 탈취 시 15분만 유효하고, Refresh Token은 HttpOnly 쿠키에 저장하여 XSS 공격으로부터 보호합니다.

- Q: JWT를 localStorage에 저장하면 안 되는 이유는 무엇인가요?
  - A: XSS(Cross-Site Scripting) 공격에 취약하기 때문입니다. **왜?** localStorage는 JavaScript로 접근 가능하므로, 악성 스크립트가 `localStorage.getItem('token')`으로 토큰을 탈취할 수 있습니다. **권장**: HttpOnly 쿠키에 저장. HttpOnly 플래그가 설정된 쿠키는 JavaScript로 접근할 수 없어 XSS 공격으로부터 안전합니다. **CSRF 방어**: SameSite=Strict/Lax 설정 또는 CSRF 토큰 사용. **트레이드오프**: 쿠키는 CSRF에 취약하지만 방어 가능하고, localStorage는 XSS 방어가 어렵습니다. 따라서 HttpOnly + SameSite 쿠키가 더 안전합니다.

- Q: PKCE는 무엇이며, 왜 필요한가요?
  - A: PKCE(Proof Key for Code Exchange)는 모바일 앱이나 SPA에서 Authorization Code Flow를 안전하게 사용하기 위한 확장입니다. **왜 필요한가?** 모바일 앱이나 SPA는 client_secret을 안전하게 저장할 수 없습니다. 앱을 디컴파일하거나 브라우저 개발자 도구로 추출 가능합니다. **동작**: 클라이언트가 무작위 `code_verifier`를 생성하고, `code_challenge = SHA256(code_verifier)`를 인가 요청에 포함합니다. Token 교환 시 `code_verifier`를 전송하면 서버가 해시 값을 비교하여 검증합니다. **왜 안전한가?** 공격자가 Authorization Code를 가로채도 `code_verifier`를 모르면 토큰을 발급받을 수 없고, 해시 값에서 원본을 역산할 수 없기 때문입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [인증과 인가](./authentication-authorization.md) | OAuth는 인가 프레임워크, JWT는 토큰 기반 인증 구현 (선수 지식) | Beginner |
| [암호화](./cryptography.md) | JWT 서명에 HMAC/RSA 사용, PKCE에 SHA256 해시 사용 (선수 지식) | Intermediate |
| [HTTPS와 TLS](./https-tls.md) | OAuth 토큰과 JWT는 HTTPS를 통해 안전하게 전송 | Advanced |
| [웹 보안](./web-security.md) | JWT를 localStorage에 저장 시 XSS 위험, 쿠키 사용 시 CSRF 대응 필요 | Intermediate |

## 참고 자료

- [OAuth 2.0 RFC 6749](https://datatracker.ietf.org/doc/html/rfc6749)
- [JWT RFC 7519](https://datatracker.ietf.org/doc/html/rfc7519)
- [PKCE RFC 7636](https://datatracker.ietf.org/doc/html/rfc7636)
- [OAuth 2.0 Security Best Practices](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics)
- [jwt.io - JWT Debugger](https://jwt.io/)
