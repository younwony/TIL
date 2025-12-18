# Security (보안)

시스템과 데이터를 보호하기 위한 보안 기술과 원칙을 정리합니다.

## 목차

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

## 학습 순서 추천

1. **기초**: 암호화 → HTTPS/TLS
   - 암호화 기본 개념을 이해한 후 HTTPS가 어떻게 암호화를 활용하는지 학습

2. **인증/인가**: 인증과 인가 → OAuth/JWT
   - 인증/인가 개념을 이해한 후 OAuth 2.0과 JWT 구현 방식 학습

3. **실전**: 웹 보안
   - 전체적인 보안 개념을 익힌 후 실제 공격 사례와 방어 방법 학습

## 핵심 개념 요약

### 보안의 3대 요소 (CIA Triad)

- **기밀성 (Confidentiality)**: 인가된 사용자만 정보 접근 가능
- **무결성 (Integrity)**: 정보가 변조되지 않음을 보장
- **가용성 (Availability)**: 필요할 때 정보에 접근 가능

### 인증 vs 인가

- **인증 (Authentication)**: "당신이 누구인가?" - 신원 확인
- **인가 (Authorization)**: "당신이 무엇을 할 수 있는가?" - 권한 확인

### 암호화 방식

- **대칭키**: 암호화/복호화 키 동일, 빠름, 키 배포 어려움
- **비대칭키**: 공개키/개인키 쌍, 느림, 키 배포 쉬움
- **해시**: 일방향 변환, 복호화 불가, 무결성 검증

### 주요 프로토콜

- **OAuth 2.0**: 인가 프레임워크, 권한 위임
- **JWT**: 자체 포함형 토큰, Stateless
- **TLS/SSL**: 전송 계층 보안, HTTPS의 기반

## 실무 체크리스트

### 웹 애플리케이션 보안

- [ ] 모든 페이지 HTTPS 적용
- [ ] 비밀번호 bcrypt/Argon2로 해싱 + Salt
- [ ] XSS 방어: 출력 인코딩, CSP 적용
- [ ] CSRF 방어: CSRF 토큰, SameSite 쿠키
- [ ] SQL Injection 방어: Prepared Statement 사용
- [ ] 인증: Access Token(15분) + Refresh Token(7일)
- [ ] 쿠키: HttpOnly, Secure, SameSite 플래그 설정
- [ ] 입력 검증: 화이트리스트 기반 필터링

### API 보안

- [ ] JWT 사용 시 HS256 또는 RS256 서명
- [ ] Authorization 헤더로 토큰 전송 (Bearer)
- [ ] Refresh Token은 HttpOnly 쿠키에 저장
- [ ] Rate Limiting 적용 (DDoS 방어)
- [ ] CORS 설정 (허용 도메인 명시)
- [ ] 민감한 정보 로그에 기록 금지

### 인증서 관리

- [ ] CA 서명 인증서 사용 (자체 서명 금지)
- [ ] TLS 1.2 이상 사용 (TLS 1.3 권장)
- [ ] 강력한 암호 스위트 선택 (Forward Secrecy 지원)
- [ ] 인증서 만료 전 갱신 (자동화 권장)
- [ ] HSTS 활성화 (HTTPS 강제)

## 참고 자료

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/)
- [Mozilla Web Security Guidelines](https://infosec.mozilla.org/guidelines/web_security)
- [Let's Encrypt](https://letsencrypt.org/)
- [jwt.io](https://jwt.io/)
