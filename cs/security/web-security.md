# 웹 보안

> [3] 중급 · 선수 지식: [인증과 인가](./authentication-authorization.md)

> 웹 애플리케이션에서 발생할 수 있는 보안 취약점을 이해하고 방어하는 기술

`#웹보안` `#WebSecurity` `#XSS` `#CrossSiteScripting` `#CSRF` `#CrossSiteRequestForgery` `#SQLInjection` `#SQL삽입` `#OWASP` `#OWASPTop10` `#보안취약점` `#입력검증` `#InputValidation` `#출력인코딩` `#OutputEncoding` `#StoredXSS` `#ReflectedXSS` `#DOMbasedXSS` `#CSRFToken` `#PreparedStatement` `#CSP` `#ContentSecurityPolicy` `#SameSite` `#HttpOnly` `#SSRF` `#BrokenAccessControl` `#방어계층화` `#DefenseInDepth` `#Escaping` `#WAF`

## 왜 알아야 하는가?

- **실무**: 웹 애플리케이션 개발 시 XSS, CSRF, SQL Injection 등의 공격을 방어하지 못하면 개인정보 유출, 데이터 손실, 서비스 마비 등 심각한 보안 사고가 발생합니다. OWASP Top 10은 실제 기업에서 자주 발생하는 취약점 목록입니다.
- **면접**: "XSS와 CSRF의 차이", "SQL Injection 방어 방법", "Prepared Statement가 안전한 이유"는 웹 개발자 면접의 필수 질문입니다. 보안 의식과 안전한 코드 작성 능력을 평가하는 지표입니다.
- **기반 지식**: 입력 검증, 출력 인코딩, 권한 검증 등 보안 코딩의 기본 원칙을 이해하는 기반이 됩니다. 이를 모르면 아무리 좋은 아키텍처라도 취약점이 생깁니다.

## 핵심 개념

- **OWASP Top 10**: 가장 치명적인 웹 애플리케이션 보안 위협 목록
- **XSS (Cross-Site Scripting)**: 악성 스크립트를 웹 페이지에 삽입하여 실행
- **CSRF (Cross-Site Request Forgery)**: 사용자가 의도하지 않은 요청을 강제로 전송
- **SQL Injection**: SQL 쿼리를 조작하여 데이터베이스를 공격
- **방어 계층화 (Defense in Depth)**: 여러 보안 계층을 중첩하여 방어

## 쉽게 이해하기

**웹 보안 공격**을 일상 범죄에 비유할 수 있습니다.

- **XSS**: 식당 설문지에 폭탄을 숨겨두는 것. 다른 손님이 설문지를 열면 폭탄이 터짐. 악의적인 사용자가 게시판에 스크립트를 작성하면, 다른 사용자가 그 게시글을 볼 때 스크립트가 실행됨.

- **CSRF**: 친구가 보낸 것처럼 위조한 편지로 은행 송금을 시키는 것. 사용자가 로그인한 상태에서 악의적인 사이트를 방문하면, 사용자 몰래 송금 요청이 전송됨.

- **SQL Injection**: 주문서에 "아메리카노 1잔; 금고 열어"라고 쓰는 것. 시스템이 주문서 내용을 그대로 실행하면 금고가 열림. 입력값을 검증하지 않으면 `' OR '1'='1` 같은 조작된 SQL이 실행됨.

- **방어 계층화**: 집에 대문, 현관문, 방문 3개의 잠금장치를 두는 것. 하나가 뚫려도 나머지가 막음. 입력 검증, 출력 인코딩, WAF 등 여러 방어 수단을 함께 사용.

## 상세 설명

### OWASP Top 10 (2021)

**왜 OWASP Top 10을 알아야 하는가?**

전 세계 보안 전문가들이 실제로 가장 많이 발생하고 위험한 취약점을 정리한 목록입니다. 이 10가지만 제대로 방어해도 대부분의 웹 공격을 막을 수 있습니다.

1. **Broken Access Control**: 권한 검증 실패
2. **Cryptographic Failures**: 암호화 미적용, 약한 알고리즘
3. **Injection**: SQL, OS, LDAP Injection
4. **Insecure Design**: 보안을 고려하지 않은 설계
5. **Security Misconfiguration**: 잘못된 보안 설정
6. **Vulnerable Components**: 취약한 라이브러리 사용
7. **Authentication Failures**: 인증 로직 취약점
8. **Software and Data Integrity Failures**: 무결성 검증 실패
9. **Logging and Monitoring Failures**: 로깅 부족
10. **Server-Side Request Forgery (SSRF)**: 서버 측 요청 위조

### XSS (Cross-Site Scripting)

**동작 방식**:

공격자가 웹 페이지에 악성 JavaScript를 삽입하면, 다른 사용자가 그 페이지를 볼 때 스크립트가 실행됩니다.

**왜 위험한가?**

사용자의 브라우저에서 스크립트가 실행되므로 쿠키 탈취, 세션 하이재킹, 키로깅, 피싱 페이지 표시 등 모든 것이 가능합니다. 사용자는 정상적인 웹 페이지를 보고 있다고 생각하지만, 실제로는 공격자의 스크립트가 실행되고 있습니다.

**XSS 종류**:

1. **Stored XSS (저장형)**:
   - 악성 스크립트가 DB에 저장됨
   - 예: 게시판 글 작성 시 `<script>alert(document.cookie)</script>` 입력
   - 피해: 해당 게시글을 보는 모든 사용자 감염
   - 왜 가장 위험한가? 한 번 저장되면 지속적으로 피해 발생

2. **Reflected XSS (반사형)**:
   - URL 파라미터에 스크립트를 포함하여 반사
   - 예: `example.com/search?q=<script>...</script>`
   - 피해: URL을 클릭한 사용자만 감염
   - 왜 많이 사용되는가? 피싱 링크로 쉽게 유포 가능

3. **DOM-based XSS**:
   - 클라이언트 측 JavaScript로 DOM 조작 시 발생
   - 서버 응답에는 스크립트가 없지만 브라우저에서 생성
   - 왜 탐지가 어려운가? 서버 로그에 기록되지 않음

**방어 방법**:

```java
// Bad - XSS 취약
out.println("<div>" + userInput + "</div>");

// Good - HTML 이스케이프
out.println("<div>" + StringEscapeUtils.escapeHtml4(userInput) + "</div>");
// <script> -> &lt;script&gt; (텍스트로 표시, 실행 안 됨)
```

**왜 이스케이프가 필요한가?**

`<script>alert(1)</script>`를 그대로 출력하면 브라우저가 HTML 태그로 인식하여 실행합니다. `&lt;script&gt;`로 변환하면 브라우저가 텍스트로 인식하여 화면에 그대로 표시만 합니다.

**추가 방어**:
- **CSP (Content Security Policy)**: 허용된 소스의 스크립트만 실행
- **HttpOnly 쿠키**: JavaScript로 쿠키 접근 차단
- **입력 검증**: 화이트리스트 기반 필터링

### CSRF (Cross-Site Request Forgery)

**동작 방식**:

1. 사용자가 은행 사이트에 로그인 (쿠키에 세션 저장)
2. 악의적인 사이트 방문
3. 악의적인 사이트가 은행 송금 요청을 자동 전송
4. 브라우저가 쿠키를 자동으로 포함하여 요청
5. 은행 서버는 정상 요청으로 인식하여 송금 실행

**왜 위험한가?**

사용자가 로그인한 상태라면, 공격자가 사용자 몰래 계정의 모든 권한을 사용할 수 있습니다. 왜냐하면 브라우저가 쿠키를 자동으로 포함하므로, 서버는 사용자의 정상 요청인지 공격자의 위조 요청인지 구분할 수 없기 때문입니다.

**방어 방법**:

1. **CSRF Token**:
```java
// 서버: 폼 렌더링 시 토큰 생성
String csrfToken = generateToken();
session.setAttribute("csrfToken", csrfToken);

// HTML
<form action="/transfer" method="POST">
    <input type="hidden" name="csrfToken" value="${csrfToken}">
    <input type="text" name="amount">
    <button type="submit">송금</button>
</form>

// 서버: 요청 검증
String sessionToken = (String) session.getAttribute("csrfToken");
String requestToken = request.getParameter("csrfToken");
if (!sessionToken.equals(requestToken)) {
    throw new SecurityException("CSRF attack detected");
}
```

**왜 CSRF 토큰이 효과적인가?**

공격자는 사용자의 세션 쿠키는 이용할 수 있지만, 세션에 저장된 CSRF 토큰 값은 알 수 없습니다. 따라서 위조 요청에 올바른 토큰을 포함할 수 없어 요청이 차단됩니다.

2. **SameSite 쿠키**:
```java
// 쿠키 설정
Cookie cookie = new Cookie("sessionId", sessionId);
cookie.setAttribute("SameSite", "Strict"); // 또는 "Lax"
response.addCookie(cookie);
```

**왜 SameSite가 효과적인가?**

`SameSite=Strict`로 설정하면 다른 도메인에서 온 요청에는 쿠키가 포함되지 않습니다. 악의적인 사이트에서 보낸 요청은 쿠키가 없어 인증 실패합니다.

3. **Referer 검증**: 요청이 자사 도메인에서 왔는지 확인
4. **중요 작업은 재인증**: 비밀번호 재입력 요구

### SQL Injection

**동작 방식**:

```java
// Bad - SQL Injection 취약
String userId = request.getParameter("userId");
String query = "SELECT * FROM users WHERE id = '" + userId + "'";
// 공격자 입력: ' OR '1'='1
// 실행 쿼리: SELECT * FROM users WHERE id = '' OR '1'='1'
// 결과: 모든 사용자 정보 조회
```

**왜 위험한가?**

공격자가 SQL 쿼리를 조작하여 다음을 할 수 있습니다:
- 데이터 조회 (개인정보 유출)
- 데이터 수정/삭제 (데이터 무결성 파괴)
- 관리자 계정으로 로그인 우회
- DB 서버 제어권 탈취 (OS 명령 실행)

**방어 방법**:

1. **Prepared Statement (권장)**:
```java
// Good - SQL Injection 방어
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(query);
pstmt.setString(1, userId); // 자동으로 이스케이프 처리
ResultSet rs = pstmt.executeQuery();
```

**왜 Prepared Statement가 안전한가?**

입력값을 SQL 구문이 아닌 데이터로 처리합니다. `' OR '1'='1`이 입력되어도 문자열 리터럴로 인식되어 `id = "' OR '1'='1'"`로 검색합니다. SQL 문법으로 해석되지 않습니다.

2. **ORM 사용**:
```java
// JPA/Hibernate
userRepository.findById(userId); // 내부적으로 Prepared Statement 사용
```

3. **입력 검증**:
```java
// 화이트리스트 검증
if (!userId.matches("^[a-zA-Z0-9]+$")) {
    throw new IllegalArgumentException("Invalid userId format");
}
```

**왜 입력 검증만으로는 부족한가?**

모든 경우의 수를 예측하기 어렵고, 블랙리스트 방식은 우회 가능합니다. 예를 들어 `'`, `"`, `;`를 필터링해도 `CHAR()` 함수로 우회할 수 있습니다. Prepared Statement는 근본적으로 SQL과 데이터를 분리하므로 안전합니다.

4. **최소 권한 원칙**: DB 계정에 필요한 최소 권한만 부여
   - 애플리케이션 계정은 DROP, CREATE TABLE 권한 불필요
   - 조회만 필요한 기능은 SELECT 권한만 부여

### 기타 주요 공격 및 방어

#### Broken Access Control

**문제**:
```java
// Bad - URL만 알면 다른 사용자 정보 조회 가능
@GetMapping("/users/{userId}")
public User getUser(@PathVariable String userId) {
    return userService.findById(userId);
}
```

**방어**:
```java
// Good - 권한 검증
@GetMapping("/users/{userId}")
public User getUser(@PathVariable String userId, Principal principal) {
    if (!userId.equals(principal.getName()) && !isAdmin(principal)) {
        throw new AccessDeniedException("Access denied");
    }
    return userService.findById(userId);
}
```

**왜 URL만으로는 안전하지 않은가?**

URL을 추측하거나 브루트포스 공격으로 다른 사용자의 정보에 접근할 수 있습니다. 반드시 서버 측에서 권한을 검증해야 합니다.

#### Security Misconfiguration

**권장 (O)**:
- 에러 메시지에 스택 트레이스, 버전 정보 노출 금지
- 불필요한 기능, 포트, 계정 비활성화
- 최신 보안 패치 적용
- HTTPS 강제, HSTS 활성화

**비권장 (X)**:
- 기본 비밀번호 사용 (admin/admin)
- 디버그 모드 운영 환경 활성화
- 불필요한 HTTP 메서드 허용 (TRACE, OPTIONS)

**왜?**

에러 메시지에 `NullPointerException at UserService.java:42`를 노출하면 공격자가 내부 구조를 파악할 수 있습니다. 운영 환경에서는 "일시적인 오류가 발생했습니다" 같은 일반적인 메시지만 표시해야 합니다.

## 트레이드오프

### 보안 vs 사용자 경험

| 보안 강화 | 장점 | 단점 |
|----------|------|------|
| 2FA 강제 | 계정 탈취 방지 | 로그인 시간 증가, 사용자 불편 |
| 짧은 세션 만료 | 세션 하이재킹 피해 최소화 | 자주 재로그인 필요 |
| 강력한 비밀번호 정책 | 무차별 대입 공격 방어 | 비밀번호 잊어버림 증가 |
| CAPTCHA | 봇 공격 방지 | 사용자 불편, 접근성 저하 |

**결론**: 위험도에 따라 차등 적용. 일반 게시판은 느슨하게, 금융 거래는 엄격하게.

### 입력 검증 vs 출력 인코딩

| 방식 | 시점 | 효과 |
|------|------|------|
| 입력 검증 | 데이터 수신 시 | 악의적인 데이터 차단 (1차 방어) |
| 출력 인코딩 | 데이터 출력 시 | 저장된 악성 코드 무력화 (최종 방어) |

**왜 둘 다 필요한가?**

입력 검증만으로는 100% 방어할 수 없습니다. 우회 기법이 계속 발전하고, 레거시 데이터에 이미 악성 코드가 있을 수 있습니다. 출력 인코딩은 마지막 방어선으로, 저장된 모든 데이터를 안전하게 처리합니다.

## 면접 예상 질문

- Q: XSS와 CSRF의 차이는 무엇인가요?
  - A: XSS는 악성 스크립트를 웹 페이지에 삽입하여 **다른 사용자의 브라우저**에서 실행하는 공격이고, CSRF는 사용자가 의도하지 않은 요청을 **서버**로 전송하는 공격입니다. **왜 다른가?** XSS는 클라이언트(브라우저) 측 공격으로 쿠키 탈취, DOM 조작이 목표이고, CSRF는 서버 측 공격으로 송금, 정보 수정 등 상태 변경이 목표입니다. **방어도 다릅니다.** XSS는 출력 인코딩, CSP로 방어하고, CSRF는 CSRF 토큰, SameSite 쿠키로 방어합니다.

- Q: SQL Injection을 방어하는 가장 효과적인 방법은 무엇인가요?
  - A: Prepared Statement를 사용하는 것입니다. **왜냐하면** 입력값을 SQL 구문이 아닌 데이터로 처리하므로, 공격자가 SQL 문법을 삽입해도 문자열 리터럴로만 인식되기 때문입니다. 예를 들어 `' OR '1'='1`이 입력되어도 `id = "' OR '1'='1'"`로 검색할 뿐, WHERE 절 조건으로 해석되지 않습니다. 입력 검증만으로는 모든 우회 기법을 막을 수 없지만, Prepared Statement는 근본적으로 SQL과 데이터를 분리하므로 안전합니다.

- Q: HttpOnly 쿠키를 사용하면 어떤 공격을 방어할 수 있나요?
  - A: XSS 공격으로 인한 쿠키 탈취를 방어할 수 있습니다. **왜냐하면** HttpOnly 플래그가 설정된 쿠키는 JavaScript로 접근할 수 없기 때문입니다. 공격자가 `<script>alert(document.cookie)</script>`를 삽입해도 HttpOnly 쿠키는 읽을 수 없습니다. 단, XSS 자체를 막는 것은 아니므로, 출력 인코딩과 CSP를 함께 사용해야 합니다. 또한 네트워크 도청(Man-in-the-Middle)은 막지 못하므로 HTTPS(Secure 플래그)도 필수입니다.

- Q: CSRF 토큰은 어떻게 동작하며, 왜 효과적인가요?
  - A: 서버가 폼을 렌더링할 때 임의의 토큰을 생성하여 세션에 저장하고, 폼의 hidden 필드에도 포함합니다. 요청이 오면 세션의 토큰과 요청의 토큰을 비교하여 일치하면 허용합니다. **왜 효과적인가?** 공격자는 사용자의 쿠키(세션 ID)는 이용할 수 있지만, 서버 세션에 저장된 CSRF 토큰 값은 알 수 없습니다. Same-Origin Policy로 인해 다른 도메인에서 토큰을 읽을 수 없기 때문입니다. 따라서 위조 요청은 올바른 토큰을 포함할 수 없어 차단됩니다.

- Q: 입력 검증과 출력 인코딩 중 어느 것이 더 중요하며, 왜 둘 다 해야 하나요?
  - A: 출력 인코딩이 더 중요하지만, 둘 다 필수입니다. **왜냐하면** 입력 검증은 우회될 수 있고, 레거시 데이터에 이미 악성 코드가 있을 수 있기 때문입니다. 출력 인코딩은 저장된 모든 데이터를 안전하게 처리하는 최종 방어선입니다. **둘 다 필요한 이유(Defense in Depth)**: 입력 검증은 1차 방어로 명백히 악의적인 데이터를 차단하고(예: DROP TABLE), 출력 인코딩은 2차 방어로 저장된 데이터가 스크립트로 실행되는 것을 막습니다(예: `<script>` → `&lt;script&gt;`). 하나가 실패해도 다른 하나가 방어합니다.

- Q: OWASP Top 10의 1위가 Injection에서 Broken Access Control로 바뀐 이유는 무엇인가요?
  - A: Prepared Statement, ORM의 보편화로 Injection 공격이 감소한 반면, API와 마이크로서비스의 증가로 접근 제어 취약점이 급증했기 때문입니다. **왜 접근 제어가 어려운가?** 모놀리식 시스템은 중앙 인증/인가로 관리가 쉽지만, MSA 환경에서는 각 서비스가 독립적으로 권한을 검증해야 합니다. "URL만 알면 접근 가능"한 API가 많아지면서 권한 검증 누락이 빈번하게 발생합니다. **따라서** 모든 API 엔드포인트에서 명시적으로 권한을 검증하고, API Gateway에서 중앙 인가를 적용해야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [인증과 인가](./authentication-authorization.md) | 세션 하이재킹, 권한 우회 등 인증/인가 관련 공격 (선수 지식) | Beginner |
| [암호화](./cryptography.md) | HTTPS, 비밀번호 해싱 등 암호화 기술로 공격 방어 | Intermediate |
| [HTTPS와 TLS](./https-tls.md) | HTTPS는 중간자 공격, 도청 등 네트워크 계층 공격 방어 | Advanced |
| [OAuth 2.0과 JWT](./oauth-jwt.md) | JWT 저장 위치에 따른 XSS/CSRF 위험 | Advanced |

## 참고 자료

- [OWASP Top 10 - 2021](https://owasp.org/www-project-top-ten/)
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/)
- [Content Security Policy (CSP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP)
- [Same-Site Cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie/SameSite)
