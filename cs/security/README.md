# Security (보안)

시스템과 데이터를 보호하기 위한 보안 기술과 원칙을 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 보안이란                                                   │
│        - 보안의 정의와 CIA Triad                                 │
│            │                                                     │
│            ▼                                                     │
│   [2] 암호화                                                     │
│        - 보안의 기본 기술                                        │
│            │                                                     │
│            ├──────────────────────────┐                         │
│            ▼                          ▼                         │
│   [3] HTTPS/TLS              [3] 인증과 인가                     │
│        - 통신 보안                    - 접근 제어                │
│            │                          │                         │
│            │                          ▼                         │
│            │                 [4] OAuth/JWT                       │
│            │                                                     │
│            └──────────────────────────┘                         │
│                           │                                      │
│                           ▼                                      │
│                    [4] 웹 보안                                   │
│                        - 취약점 방어                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 난이도별 목차

### [1] 정의/기초

보안이 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [보안이란](./what-is-security.md) | 보안의 정의, CIA Triad, 보안 위협 유형 | 25분 |

### [2] 입문

보안 기초를 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [암호화](./cryptography.md) | 대칭키/비대칭키, AES/RSA, 해시, Salt | 보안이란 |

### [3] 중급

암호화 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [HTTPS와 TLS](./https-tls.md) | HTTPS 동작 원리, TLS 핸드셰이크, 인증서 | 암호화, TCP/IP |
| [인증과 인가](./authentication-authorization.md) | 인증 vs 인가, 세션/토큰 기반, RBAC | 암호화 |

### [4] 심화

기본 개념을 모두 익힌 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [OAuth 2.0과 JWT](./oauth-jwt.md) | OAuth 플로우, JWT 구조, Token | 인증과 인가, HTTPS |
| [웹 보안](./web-security.md) | OWASP Top 10, XSS, CSRF, SQL Injection | 인증과 인가, HTTP |

## 전체 목차

### 기초 개념
- [보안이란](./what-is-security.md) - 보안의 정의, CIA Triad, 보안 위협 유형

### 인증 & 인가
- [인증과 인가](./authentication-authorization.md) - 인증 vs 인가, 세션 기반 vs 토큰 기반, 권한 관리(RBAC/ABAC)

### 웹 보안
- [웹 보안](./web-security.md) - OWASP Top 10, XSS, CSRF, SQL Injection, 보안 취약점 및 방어 방법

### 암호화
- [암호화](./cryptography.md) - 대칭키/비대칭키 암호화, AES/RSA, 해시(SHA, bcrypt), Salt

### OAuth & JWT
- [OAuth 2.0과 JWT](./oauth-jwt.md) - OAuth 2.0 인가 플로우, JWT 구조, Access/Refresh Token

### HTTPS & TLS
- [HTTPS와 TLS](./https-tls.md) - HTTPS 동작 원리, TLS 핸드셰이크, 인증서, 공개키 기반 구조(PKI)

## 핵심 개념 요약

### 보안의 3대 요소 (CIA Triad)

- **기밀성 (Confidentiality)**: 인가된 사용자만 정보 접근 가능
- **무결성 (Integrity)**: 정보가 변조되지 않음을 보장
- **가용성 (Availability)**: 필요할 때 정보에 접근 가능

### 인증 vs 인가

- **인증 (Authentication)**: "당신이 누구인가?" - 신원 확인
- **인가 (Authorization)**: "당신이 무엇을 할 수 있는가?" - 권한 확인

## 실무 체크리스트

### 웹 애플리케이션 보안

- [ ] 모든 페이지 HTTPS 적용
- [ ] 비밀번호 bcrypt/Argon2로 해싱 + Salt
- [ ] XSS 방어: 출력 인코딩, CSP 적용
- [ ] CSRF 방어: CSRF 토큰, SameSite 쿠키
- [ ] SQL Injection 방어: Prepared Statement 사용
- [ ] 인증: Access Token(15분) + Refresh Token(7일)

### API 보안

- [ ] JWT 사용 시 HS256 또는 RS256 서명
- [ ] Authorization 헤더로 토큰 전송 (Bearer)
- [ ] Refresh Token은 HttpOnly 쿠키에 저장
- [ ] Rate Limiting 적용 (DDoS 방어)
- [ ] CORS 설정 (허용 도메인 명시)

## 작성 예정

*(모든 예정 문서가 작성 완료되었습니다)*

## 참고 자료

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/)
- [Mozilla Web Security Guidelines](https://infosec.mozilla.org/guidelines/web_security)
