# HTTP/HTTPS

**난이도: [3] 중급**

> 웹에서 클라이언트와 서버 간 데이터를 주고받기 위한 프로토콜

## 왜 알아야 하는가?

- **웹의 기초**: 모든 웹 애플리케이션의 핵심 통신 프로토콜
- **RESTful API 설계**: HTTP 메서드와 상태 코드를 올바르게 사용해야 표준적인 API 설계 가능
- **성능 최적화**: HTTP/2, HTTP/3의 특징을 이해해야 웹 성능 개선 가능
- **보안 필수**: HTTPS, TLS 핸드셰이크, 보안 헤더 등 보안 지식 필수
- **캐싱 전략**: 올바른 캐시 헤더 설정으로 서버 부하 감소 및 사용자 경험 향상

## 핵심 개념

- **HTTP(HyperText Transfer Protocol)**: 웹에서 하이퍼텍스트 문서를 전송하기 위한 애플리케이션 계층 프로토콜
- **HTTPS(HTTP Secure)**: HTTP에 SSL/TLS 암호화를 적용하여 보안을 강화한 프로토콜
- **무상태(Stateless)**: 각 요청은 독립적이며 서버는 이전 요청 정보를 저장하지 않음
- **비연결성(Connectionless)**: HTTP/1.0 기준 요청-응답 후 연결 종료 (Keep-Alive로 개선)
- **요청-응답 모델**: 클라이언트가 요청을 보내면 서버가 응답하는 단방향 통신 구조

## 쉽게 이해하기

**HTTP와 HTTPS**를 편지와 등기우편에 비유할 수 있습니다.

### HTTP = 일반 엽서

엽서에 내용을 적어서 보내면 누구나 내용을 볼 수 있습니다. 배달원도, 옆 사람도 읽을 수 있죠. 빠르고 간단하지만 비밀이 보장되지 않습니다.

### HTTPS = 봉인된 등기우편

편지를 봉투에 넣고 자물쇠로 잠근 뒤, 받는 사람만 열 수 있는 열쇠를 함께 보냅니다. 중간에 누가 훔쳐봐도 내용을 알 수 없고, 받는 사람이 맞는지 확인도 됩니다.

| 비유 | HTTP | HTTPS |
|------|------|-------|
| 전달 방식 | 엽서 (내용 노출) | 봉인된 편지 (암호화) |
| 중간에서 볼 수 있나? | O (도청 가능) | X (암호화됨) |
| 받는 사람 확인 | X | O (인증서로 확인) |
| 속도 | 빠름 | 약간 느림 (암호화 시간) |

### SSL/TLS 핸드셰이크 = 비밀 악수

```
나: "안녕, 비밀 대화하고 싶어" (Client Hello)
상대: "좋아, 내 신분증이야" (Server Hello + 인증서)
나: "신분증 확인했어. 이 비밀번호로 대화하자" (암호화된 키 전송)
둘: "이제부터 이 비밀번호로 대화!" (암호화 통신 시작)
```

**왜 이 과정이 필요한가요?**
- **신분 확인**: "진짜 네이버인지, 가짜 사이트인지" 인증서로 확인
- **키 교환**: 암호화에 쓸 비밀번호를 안전하게 공유
- **암호화 협상**: 둘 다 지원하는 가장 안전한 방식 선택

### 무상태(Stateless) = 기억력 없는 점원

매번 방문할 때마다 점원이 "누구세요?"라고 묻습니다. 그래서 "저 단골이에요"라는 이름표(쿠키/세션)를 달고 다니는 겁니다.

**왜 무상태로 설계했나요?**
- **확장성**: 어느 서버가 받아도 처리 가능 (로드밸런싱 용이)
- **단순성**: 서버가 상태를 관리할 필요 없음
- **장애 격리**: 한 요청 실패가 다른 요청에 영향 없음

---

## 상세 설명

### HTTP 메서드

| 메서드 | 설명 | 멱등성 | 안전성 | 왜 이 특성인가? |
|--------|------|--------|--------|----------------|
| GET | 리소스 조회 | O | O | 조회만 하므로 서버 상태 불변 |
| POST | 리소스 생성, 데이터 처리 | X | X | 매번 새 리소스 생성 가능 |
| PUT | 리소스 전체 수정 (없으면 생성) | O | X | 같은 데이터로 여러 번 해도 결과 동일 |
| PATCH | 리소스 일부 수정 | X | X | 증분 연산 가능 (count += 1) |
| DELETE | 리소스 삭제 | O | X | 이미 없으면 또 삭제해도 없음 |
| HEAD | GET과 동일하나 본문 없이 헤더만 응답 | O | O | 메타데이터만 확인 |
| OPTIONS | 지원 메서드 확인 (CORS preflight) | O | O | 서버 상태 변경 없이 정보 조회 |

**멱등성(Idempotency)**: 동일한 요청을 여러 번 보내도 결과가 같음

**왜 멱등성이 중요한가?**
- 네트워크 실패 시 **재시도 가능** 여부 결정
- GET, PUT, DELETE: 재시도해도 안전
- POST: 재시도하면 중복 생성 가능 → 결제 중복 등 문제 발생

### 문제가 되는 패턴

**비권장 (X): GET으로 상태 변경**

```http
GET /api/users/1/delete  ← 위험!
```

**왜 문제인가?**
- 브라우저가 GET을 캐싱 → 예상치 못한 삭제 발생
- 크롤러가 링크 따라가다가 데이터 삭제
- 히스토리 뒤로가기만 해도 삭제 실행

**권장 (O): 적절한 메서드 사용**

```http
DELETE /api/users/1
```

**만약 지키지 않으면?**
- 검색 엔진 크롤러가 사이트 데이터 전부 삭제
- 브라우저 prefetch로 의도치 않은 동작 실행
- RESTful API 표준 위반으로 협업 혼란

---

### HTTP 상태 코드

| 범위 | 의미 | 주요 코드 | 왜 이 범위인가? |
|------|------|----------|----------------|
| 1xx | 정보 응답 | 100 Continue | 아직 처리 중, 계속 진행하세요 |
| 2xx | 성공 | 200 OK, 201 Created | 요청이 정상 처리됨 |
| 3xx | 리다이렉션 | 301, 302, 304 | 다른 곳으로 가세요 |
| 4xx | 클라이언트 오류 | 400, 401, 403, 404 | 요청이 잘못됨 (클라이언트 책임) |
| 5xx | 서버 오류 | 500, 502, 503 | 서버가 처리 실패 (서버 책임) |

**자주 혼동하는 상태 코드:**

| 상태 코드 | 의미 | 언제 사용? |
|----------|------|-----------|
| 401 Unauthorized | 인증 필요 | 로그인 안 했을 때 |
| 403 Forbidden | 권한 없음 | 로그인했지만 접근 권한 없을 때 |
| 404 Not Found | 리소스 없음 | URL이 잘못되었거나 삭제됨 |
| 400 Bad Request | 잘못된 요청 | 파라미터 오류, 문법 오류 |

**만약 상태 코드를 잘못 사용하면?**
- 401/403 혼용: 클라이언트가 로그인 시도할지, 권한 요청할지 혼란
- 모든 오류에 500: 클라이언트 잘못인지, 서버 잘못인지 구분 불가
- 성공인데 200 아닌 코드: 클라이언트 라이브러리 오동작

---

### HTTP 버전별 특징

#### HTTP/1.0

```
[요청 1] ─── TCP 연결 ─── [응답 1] ─── 연결 종료
[요청 2] ─── TCP 연결 ─── [응답 2] ─── 연결 종료
```

**왜 비효율적인가?**
- 매 요청마다 TCP 3-way handshake (RTT 1회)
- 페이지당 수십 개 리소스 → 수십 번 연결 수립

#### HTTP/1.1

```
[연결 수립]
[요청 1] ─── [응답 1]
[요청 2] ─── [응답 2]  ← Keep-Alive로 연결 재사용
[요청 3] ─── [응답 3]
[연결 종료]
```

**개선점:**
- **Keep-Alive**: 연결 재사용으로 성능 향상
- **파이프라이닝**: 응답을 기다리지 않고 여러 요청 전송

**왜 아직도 문제인가?**
- **HOL(Head-of-Line) Blocking**: 첫 응답 늦으면 뒤 요청 다 대기
- 해결책: 도메인 샤딩 (여러 도메인으로 분산) - 근본 해결 아님

#### HTTP/2

```
하나의 연결에서:
┌─────────────────────────────┐
│  Stream 1: HTML 요청/응답     │
│  Stream 2: CSS 요청/응답      │ ← 동시 처리 (멀티플렉싱)
│  Stream 3: JS 요청/응답       │
└─────────────────────────────┘
```

**왜 HTTP/2가 빠른가?**

| 기능 | HTTP/1.1 | HTTP/2 | 개선 효과 |
|------|---------|--------|----------|
| 요청 처리 | 순차적 | 멀티플렉싱 | HOL Blocking 해결 |
| 헤더 크기 | 매번 전체 전송 | HPACK 압축 | 헤더 크기 85% 감소 |
| 리소스 전송 | 요청해야 전송 | 서버 푸시 | 왕복 시간 절약 |
| 전송 형식 | 텍스트 | 바이너리 | 파싱 효율 증가 |

#### HTTP/3

```
UDP 기반 QUIC:
┌─────────────────────────────┐
│  Stream 1: 패킷 손실 발생     │ ← Stream 1만 재전송
│  Stream 2: 정상 처리         │ ← 영향 없이 계속 진행
│  Stream 3: 정상 처리         │
└─────────────────────────────┘
```

**왜 UDP인가?**
- TCP는 커널 레벨 → 프로토콜 수정 어려움
- UDP는 사용자 레벨에서 제어 가능 → 빠른 개선
- 0-RTT 연결: 이전 연결 정보로 즉시 데이터 전송

**만약 HTTP/3를 사용하지 않으면?**
- 모바일에서 WiFi ↔ LTE 전환 시 연결 끊김 (HTTP/3는 유지)
- TCP HOL Blocking으로 패킷 하나 손실 시 전체 지연

---

### HTTPS 동작 원리

```
┌──────────┐                          ┌──────────┐
│  Client  │                          │  Server  │
└────┬─────┘                          └────┬─────┘
     │                                      │
     │  1. Client Hello (지원 암호화 목록)    │
     │─────────────────────────────────────>│
     │                                      │
     │  2. Server Hello (선택된 암호화)       │
     │  3. 서버 인증서 전송                   │
     │<─────────────────────────────────────│
     │                                      │
     │  4. 인증서 검증 (CA 확인)              │
     │                                      │
     │  5. Pre-Master Secret 전송           │
     │     (서버 공개키로 암호화)             │
     │─────────────────────────────────────>│
     │                                      │
     │  6. 세션 키 생성 (양측 동일)           │
     │                                      │
     │  7. 암호화된 통신 시작                 │
     │<────────────────────────────────────>│
```

**왜 이 순서인가?**

1. **Client Hello**: "나는 이런 암호화 할 수 있어" - 협상 시작
2. **Server Hello**: "그중 이걸로 하자" - 암호화 방식 결정
3. **인증서 전송**: "내가 진짜 이 서버야" - 신원 증명
4. **인증서 검증**: "진짜 맞나 CA한테 확인" - 신뢰 검증
5. **Pre-Master Secret**: 비대칭 암호화로 안전하게 키 교환
6. **세션 키 생성**: 대칭 암호화용 키 (실제 통신에 사용)
7. **암호화 통신**: 대칭 암호화로 빠르게 데이터 전송

**왜 비대칭 + 대칭 암호화를 함께 쓰는가?**
- **비대칭 암호화**: 안전하지만 느림 → 키 교환에만 사용
- **대칭 암호화**: 빠름 → 실제 데이터 암호화에 사용
- 둘의 장점을 조합

---

### SSL/TLS 버전

| 버전 | 상태 | 왜? |
|------|------|-----|
| SSL 2.0/3.0 | 폐기 | POODLE, DROWN 등 심각한 취약점 |
| TLS 1.0/1.1 | 폐기 | BEAST, CRIME 등 취약점, 2020년 지원 종료 |
| TLS 1.2 | 사용 | 현재 가장 널리 사용, 안전 |
| TLS 1.3 | 권장 | 핸드셰이크 1-RTT, 더 강력한 암호화 |

**권장 (O): TLS 1.2 이상 사용**

```nginx
# Nginx 설정 예시
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256;
```

**비권장 (X): 하위 호환성 이유로 TLS 1.0 유지**

**만약 지키지 않으면?**
- PCI DSS 컴플라이언스 실패 (결제 서비스 불가)
- 브라우저에서 "안전하지 않음" 경고
- 중간자 공격(MITM)에 취약

---

### HTTP vs HTTPS 비교

| 항목 | HTTP | HTTPS | 왜 차이가 나는가? |
|------|------|-------|------------------|
| 포트 | 80 | 443 | 표준 포트 구분 |
| 암호화 | X | O (SSL/TLS) | 보안 계층 유무 |
| 인증서 | 불필요 | 필요 | 서버 신원 확인 |
| 속도 | 빠름 | 약간 느림 | 암호화/복호화 오버헤드 |
| SEO | 불리 | 유리 | 구글이 HTTPS 가산점 부여 |
| 보안 | 데이터 평문 전송 | 데이터 암호화 전송 | 도청/변조 방지 |

**권장 (O): 모든 트래픽 HTTPS**

```nginx
# HTTP → HTTPS 리다이렉트
server {
    listen 80;
    return 301 https://$host$request_uri;
}
```

**만약 HTTP를 사용하면?**
- **도청**: 공공 WiFi에서 비밀번호 탈취
- **변조**: ISP가 광고 주입 가능
- **피싱**: 가짜 사이트 구분 불가
- **SEO 불이익**: 검색 순위 하락

---

### 주요 HTTP 헤더

#### 보안 관련 헤더

**권장 (O): 보안 헤더 설정**

```http
# XSS 방지
Content-Security-Policy: default-src 'self'

# 클릭재킹 방지
X-Frame-Options: DENY

# MIME 스니핑 방지
X-Content-Type-Options: nosniff

# HTTPS 강제
Strict-Transport-Security: max-age=31536000; includeSubDomains
```

**왜 필요한가?**

| 헤더 | 방어 대상 | 공격 예시 |
|------|----------|----------|
| CSP | XSS | 악성 스크립트 삽입 |
| X-Frame-Options | 클릭재킹 | 투명 iframe으로 클릭 유도 |
| X-Content-Type-Options | MIME 스니핑 | JS를 이미지로 위장 |
| HSTS | 다운그레이드 | HTTPS를 HTTP로 변경 |

**만약 설정하지 않으면?**
- XSS로 세션 탈취
- 클릭재킹으로 의도치 않은 동작 실행
- SSL Stripping 공격에 노출

#### 캐싱 관련 헤더

| 헤더 | 설명 | 언제 사용? |
|------|------|-----------|
| Cache-Control | 캐시 동작 제어 | 대부분의 캐싱 제어 |
| ETag | 리소스 버전 식별자 | 변경 감지 |
| Last-Modified | 마지막 수정 시간 | 시간 기반 검증 |
| If-None-Match | ETag 비교 | 조건부 요청 |

**권장 (O): 적절한 캐시 전략**

```http
# 정적 리소스 (JS, CSS, 이미지)
Cache-Control: public, max-age=31536000, immutable

# API 응답
Cache-Control: private, no-cache

# 민감한 데이터
Cache-Control: no-store
```

**왜 중요한가?**
- 캐시 없음: 매번 서버 요청 → 느림, 서버 부하
- 캐시 과다: 업데이트 반영 안 됨 → 구버전 노출

---

## 트레이드오프

### HTTP 버전 선택

| 기준 | HTTP/1.1 | HTTP/2 | HTTP/3 |
|------|---------|--------|--------|
| 호환성 | ✅ 최고 | ✅ 대부분 | ⚠️ 일부 |
| 성능 | ❌ HOL Blocking | ✅ 멀티플렉싱 | ✅ 최고 |
| 구현 난이도 | ✅ 쉬움 | ⚠️ 보통 | ❌ 어려움 |
| 인프라 지원 | ✅ 완벽 | ✅ 대부분 | ⚠️ 제한적 |

### 캐시 전략

| 전략 | 장점 | 단점 | 적합한 경우 |
|------|------|------|-----------|
| no-store | 항상 최신 | 매번 요청 | 민감한 데이터 |
| no-cache | 검증 후 캐시 | 요청 필요 | 자주 변경되는 데이터 |
| max-age | 빠른 로딩 | 업데이트 지연 | 정적 리소스 |
| immutable | 재검증 안 함 | 변경 불가 | 버전된 리소스 |

---

## 예제 코드

### Java - HttpURLConnection

```java
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpExample {

    private static final int TIMEOUT_MS = 5000;
    private static final String REQUEST_METHOD_GET = "GET";

    public String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod(REQUEST_METHOD_GET);
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            }
            throw new RuntimeException("HTTP 요청 실패: " + responseCode);
        } finally {
            connection.disconnect();
        }
    }

    private String readResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
}
```

### Java - WebClient (권장, 비동기)

```java
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientExample {

    private final WebClient webClient;

    public WebClientExample(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // 동기 호출
    public <T> T getSync(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType)
                .block();  // 블로킹
    }

    // 비동기 호출
    public <T> Mono<T> getAsync(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType);  // 논블로킹
    }
}
```

**왜 WebClient를 권장하는가?**
- RestTemplate: 동기 블로킹 → 스레드 대기 낭비
- WebClient: 비동기 논블로킹 → 적은 스레드로 높은 처리량
- Spring 5.0부터 RestTemplate 유지보수 모드

---

## 면접 예상 질문

### Q: HTTP와 HTTPS의 차이점은 무엇인가요?

**A:** HTTP는 데이터를 평문으로 전송하고, HTTPS는 SSL/TLS 암호화를 적용합니다.

**왜 HTTPS를 사용해야 하는가?**

| 위협 | HTTP | HTTPS |
|------|------|-------|
| 도청 | ❌ 노출 | ✅ 암호화 |
| 변조 | ❌ 가능 | ✅ 무결성 검증 |
| 위장 | ❌ 구분 불가 | ✅ 인증서 검증 |

**만약 HTTP를 사용하면?**
- 공공 WiFi에서 로그인 정보 탈취
- ISP가 페이지에 광고 삽입
- 피싱 사이트와 구분 불가
- 구글 SEO 페널티

---

### Q: HTTP/1.1과 HTTP/2의 차이점은 무엇인가요?

**A:** 가장 큰 차이는 **멀티플렉싱**입니다.

| 기능 | HTTP/1.1 | HTTP/2 |
|------|---------|--------|
| 요청 처리 | 순차적 (HOL Blocking) | 병렬 (멀티플렉싱) |
| 헤더 | 매번 전체 텍스트 | HPACK 압축 |
| 전송 형식 | 텍스트 | 바이너리 |
| 서버 푸시 | ❌ | ✅ |

**왜 멀티플렉싱이 중요한가?**
- HTTP/1.1: 요청 A 응답이 늦으면 B, C도 대기 (HOL Blocking)
- HTTP/2: A가 늦어도 B, C는 독립적으로 처리
- 결과: 페이지 로딩 속도 50% 이상 개선 가능

---

### Q: HTTPS의 SSL/TLS 핸드셰이크 과정을 설명해주세요.

**A:**

1. **Client Hello**: 클라이언트가 지원하는 암호화 방식 목록 전송
2. **Server Hello**: 서버가 암호화 방식 선택 및 인증서 전송
3. **인증서 검증**: 클라이언트가 CA를 통해 인증서 유효성 확인
4. **키 교환**: Pre-Master Secret을 서버 공개키로 암호화하여 전송
5. **세션 키 생성**: 양측이 동일한 대칭키 생성
6. **암호화 통신**: 대칭키로 실제 데이터 암호화 전송

**왜 비대칭 + 대칭 암호화를 함께 쓰는가?**

| 암호화 방식 | 특징 | 용도 |
|------------|------|------|
| 비대칭 (RSA) | 안전하지만 느림 | 키 교환 |
| 대칭 (AES) | 빠르지만 키 공유 어려움 | 데이터 암호화 |

비대칭으로 안전하게 키를 공유하고, 대칭으로 빠르게 데이터를 암호화합니다.

---

### Q: GET과 POST의 차이점은 무엇인가요?

**A:**

| 특성 | GET | POST |
|------|-----|------|
| 용도 | 리소스 조회 | 리소스 생성/처리 |
| 데이터 위치 | URL (쿼리스트링) | Body |
| 캐싱 | ✅ 가능 | ❌ 일반적으로 안 함 |
| 멱등성 | ✅ | ❌ |
| 북마크 | ✅ 가능 | ❌ |
| 데이터 크기 | URL 길이 제한 | 제한 없음 |

**왜 구분해서 사용해야 하는가?**

| 잘못 사용 | 문제 |
|----------|------|
| GET으로 삭제 | 크롤러가 데이터 삭제 |
| GET으로 비밀번호 전송 | URL에 노출, 브라우저 기록 |
| POST로 조회 | 캐싱 불가, 북마크 불가 |

---

### Q: HTTP 캐싱은 어떻게 동작하나요?

**A:** 두 가지 검증 메커니즘이 있습니다.

**1. ETag (강한 검증)**
```http
# 최초 응답
ETag: "abc123"

# 재요청
If-None-Match: "abc123"

# 변경 없으면
304 Not Modified (본문 없음)
```

**2. Last-Modified (약한 검증)**
```http
# 최초 응답
Last-Modified: Wed, 15 Nov 2023 12:00:00 GMT

# 재요청
If-Modified-Since: Wed, 15 Nov 2023 12:00:00 GMT

# 변경 없으면
304 Not Modified
```

**왜 304를 사용하는가?**
- 본문 전송 없이 "변경 없음" 알림
- 네트워크 대역폭 절약
- 빠른 응답 (캐시 사용)

---

## 연관 문서

- [TCP/IP](./tcp-ip.md) - HTTP는 TCP 위에서 동작하며 신뢰성 있는 전송 보장
- [DNS](./dns.md) - HTTP 통신 전 DNS 조회로 서버 IP 주소 확인
- [WebSocket](./websocket.md) - HTTP 기반으로 시작하여 양방향 통신으로 업그레이드
- [CDN](./cdn.md) - HTTP 캐싱 메커니즘을 활용한 콘텐츠 전송 최적화
- [Load Balancing](./load-balancing.md) - L7 로드밸런서는 HTTP 내용 기반 라우팅

## 참고 자료

- [MDN Web Docs - HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP)
- [RFC 7231 - HTTP/1.1 Semantics and Content](https://tools.ietf.org/html/rfc7231)
- [RFC 7540 - HTTP/2](https://tools.ietf.org/html/rfc7540)
- [RFC 9114 - HTTP/3](https://www.rfc-editor.org/rfc/rfc9114)
- High Performance Browser Networking (Ilya Grigorik)
