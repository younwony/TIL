# HTTP/HTTPS

> 웹에서 클라이언트와 서버 간 데이터를 주고받기 위한 프로토콜

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

### 무상태(Stateless) = 기억력 없는 점원

매번 방문할 때마다 점원이 "누구세요?"라고 묻습니다. 그래서 "저 단골이에요"라는 이름표(쿠키/세션)를 달고 다니는 겁니다.

## 상세 설명

### HTTP 메서드

| 메서드 | 설명 | 멱등성 | 안전성 |
|--------|------|--------|--------|
| GET | 리소스 조회 | O | O |
| POST | 리소스 생성, 데이터 처리 | X | X |
| PUT | 리소스 전체 수정 (없으면 생성) | O | X |
| PATCH | 리소스 일부 수정 | X | X |
| DELETE | 리소스 삭제 | O | X |
| HEAD | GET과 동일하나 본문 없이 헤더만 응답 | O | O |
| OPTIONS | 지원 메서드 확인 (CORS preflight) | O | O |

**멱등성(Idempotency)**: 동일한 요청을 여러 번 보내도 결과가 같음

### HTTP 상태 코드

| 범위 | 의미 | 주요 코드 |
|------|------|----------|
| 1xx | 정보 응답 | 100 Continue, 101 Switching Protocols |
| 2xx | 성공 | 200 OK, 201 Created, 204 No Content |
| 3xx | 리다이렉션 | 301 Moved Permanently, 302 Found, 304 Not Modified |
| 4xx | 클라이언트 오류 | 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found |
| 5xx | 서버 오류 | 500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable |

### HTTP 버전별 특징

#### HTTP/1.0
- 요청마다 새로운 TCP 연결 수립
- 한 연결에 하나의 요청-응답만 처리

#### HTTP/1.1
- **Keep-Alive**: 연결 재사용으로 성능 향상
- **파이프라이닝**: 응답을 기다리지 않고 여러 요청 전송 (HOL Blocking 문제 존재)
- **Host 헤더 필수**: 가상 호스팅 지원
- **청크 전송 인코딩**: 대용량 데이터 스트리밍

#### HTTP/2
- **멀티플렉싱**: 하나의 연결에서 여러 요청-응답 동시 처리
- **헤더 압축**: HPACK 알고리즘으로 헤더 크기 감소
- **서버 푸시**: 클라이언트 요청 없이 리소스 전송
- **스트림 우선순위**: 중요한 리소스 먼저 전송
- **바이너리 프레이밍**: 텍스트 대신 바이너리로 전송

#### HTTP/3
- **QUIC 프로토콜**: UDP 기반으로 연결 수립 시간 단축
- **0-RTT 연결**: 이전 연결 정보로 즉시 데이터 전송
- **독립적 스트림**: 패킷 손실 시 다른 스트림에 영향 없음
- **연결 마이그레이션**: IP 변경 시에도 연결 유지

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

### SSL/TLS 버전

| 버전 | 상태 | 비고 |
|------|------|------|
| SSL 2.0/3.0 | 폐기 | 보안 취약점 발견 |
| TLS 1.0/1.1 | 폐기 | 2020년 주요 브라우저 지원 종료 |
| TLS 1.2 | 사용 | 현재 가장 널리 사용 |
| TLS 1.3 | 권장 | 핸드셰이크 간소화, 보안 강화 |

### HTTP vs HTTPS 비교

| 항목 | HTTP | HTTPS |
|------|------|-------|
| 포트 | 80 | 443 |
| 암호화 | X | O (SSL/TLS) |
| 인증서 | 불필요 | 필요 |
| 속도 | 빠름 | 약간 느림 (암호화 오버헤드) |
| SEO | 불리 | 유리 (구글 가산점) |
| 보안 | 데이터 평문 전송 | 데이터 암호화 전송 |

### 주요 HTTP 헤더

#### 요청 헤더
```http
GET /api/users HTTP/1.1
Host: example.com
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json
Cache-Control: no-cache
User-Agent: Mozilla/5.0
```

#### 응답 헤더
```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 1234
Cache-Control: max-age=3600
Set-Cookie: session=abc123; HttpOnly; Secure
Access-Control-Allow-Origin: *
```

### 캐싱 관련 헤더

| 헤더 | 설명 |
|------|------|
| Cache-Control | 캐시 동작 제어 (max-age, no-cache, no-store) |
| ETag | 리소스 버전 식별자 |
| Last-Modified | 마지막 수정 시간 |
| If-None-Match | ETag 비교로 조건부 요청 |
| If-Modified-Since | 수정 시간 비교로 조건부 요청 |

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

### Java - Spring RestTemplate

```java
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestTemplateExample {

    private final RestTemplate restTemplate;

    public RestTemplateExample(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    public <T> T post(String url, Object request, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, headers);

        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            responseType
        );

        return response.getBody();
    }
}
```

## 면접 예상 질문

- **Q: HTTP와 HTTPS의 차이점은 무엇인가요?**
  - A: HTTP는 데이터를 평문으로 전송하여 도청, 변조 위험이 있습니다. HTTPS는 SSL/TLS 암호화를 적용하여 데이터를 암호화하고, 서버 인증서를 통해 신뢰성을 보장합니다. 기본 포트도 HTTP는 80, HTTPS는 443으로 다릅니다.

- **Q: HTTP/1.1과 HTTP/2의 차이점은 무엇인가요?**
  - A: HTTP/1.1은 요청당 하나의 응답만 처리 가능하여 HOL(Head-of-Line) Blocking 문제가 있습니다. HTTP/2는 멀티플렉싱으로 하나의 연결에서 여러 요청을 동시 처리하고, HPACK으로 헤더를 압축하며, 서버 푸시 기능을 지원합니다.

- **Q: HTTPS의 SSL/TLS 핸드셰이크 과정을 설명해주세요.**
  - A: 1) 클라이언트가 지원하는 암호화 방식 목록 전송, 2) 서버가 암호화 방식 선택 및 인증서 전송, 3) 클라이언트가 인증서 검증 후 Pre-Master Secret을 서버 공개키로 암호화하여 전송, 4) 양측이 동일한 세션 키를 생성하여 이후 통신에 사용합니다.

- **Q: GET과 POST의 차이점은 무엇인가요?**
  - A: GET은 리소스 조회에 사용하며 데이터를 URL에 포함합니다. 멱등성과 안전성을 보장하고 캐싱이 가능합니다. POST는 리소스 생성이나 데이터 처리에 사용하며 데이터를 본문에 포함합니다. 멱등성을 보장하지 않으며 일반적으로 캐싱되지 않습니다.

## 참고 자료

- [MDN Web Docs - HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP)
- [RFC 7231 - HTTP/1.1 Semantics and Content](https://tools.ietf.org/html/rfc7231)
- [RFC 7540 - HTTP/2](https://tools.ietf.org/html/rfc7540)
- [RFC 9114 - HTTP/3](https://www.rfc-editor.org/rfc/rfc9114)
