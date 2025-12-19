# gRPC

> `[3] 중급` · 선수 지식: [HTTP/HTTPS](./http-https.md), [TCP/IP](./tcp-ip.md)

> Google이 개발한 HTTP/2 기반의 고성능 원격 프로시저 호출(RPC) 프레임워크

## 왜 알아야 하는가?

- **실무**: MSA 환경에서 서비스 간 통신의 표준으로 자리잡음. Kubernetes, Envoy 등 클라우드 네이티브 도구들이 gRPC를 기본 지원
- **면접**: REST vs gRPC 비교, HTTP/2 특성, Protocol Buffers 직렬화 관련 질문 빈출
- **기반 지식**: 서비스 메시, API Gateway, 분산 시스템 설계의 핵심 통신 기술

## 핵심 개념

- **Protocol Buffers (protobuf)**: 인터페이스 정의 언어(IDL)이자 직렬화 포맷
- **HTTP/2 기반**: 멀티플렉싱, 헤더 압축, 양방향 스트리밍 지원
- **4가지 통신 패턴**: Unary, Server Streaming, Client Streaming, Bidirectional Streaming
- **코드 생성**: `.proto` 파일에서 클라이언트/서버 코드 자동 생성

## 쉽게 이해하기

**레스토랑 주문 시스템으로 비유**

- **REST API**: 종이 메뉴판에 글로 주문 → 웨이터가 읽고 해석 → 주방에 전달 (텍스트 기반, 해석 필요)
- **gRPC**: 주문 버튼이 있는 태블릿 → 버튼 누르면 바로 주방 모니터에 표시 (바이너리, 즉시 처리)

```
REST (JSON)                    gRPC (Protocol Buffers)
┌─────────────────┐            ┌─────────────────┐
│ {                │            │ 08 01 12 05     │
│   "id": 1,      │     vs     │ 48 65 6c 6c 6f  │
│   "name": "Hi"  │            │                 │
│ }               │            │ (바이너리)       │
└─────────────────┘            └─────────────────┘
     ~50 bytes                      ~12 bytes
```

## 상세 설명

### gRPC 아키텍처

```
┌──────────────────────────────────────────────────────────────┐
│                        gRPC 아키텍처                          │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│   Client                              Server                  │
│  ┌─────────────┐                    ┌─────────────┐          │
│  │ Application │                    │ Application │          │
│  └──────┬──────┘                    └──────▲──────┘          │
│         │                                  │                  │
│  ┌──────▼──────┐                    ┌──────┴──────┐          │
│  │ Generated   │                    │ Generated   │          │
│  │ Client Stub │                    │ Server Stub │          │
│  └──────┬──────┘                    └──────▲──────┘          │
│         │                                  │                  │
│  ┌──────▼──────┐                    ┌──────┴──────┐          │
│  │   gRPC      │◄──── HTTP/2 ──────►│   gRPC      │          │
│  │   Channel   │   (TLS optional)   │   Server    │          │
│  └─────────────┘                    └─────────────┘          │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

**왜 이렇게 구성하는가?**
- Stub(스텁)이 직렬화/역직렬화 처리 → 개발자는 비즈니스 로직에만 집중
- HTTP/2 채널이 연결 관리 → 효율적인 리소스 사용

### Protocol Buffers

```protobuf
// user.proto
syntax = "proto3";

package user;

service UserService {
  rpc GetUser (UserRequest) returns (UserResponse);
  rpc ListUsers (Empty) returns (stream UserResponse);
}

message UserRequest {
  int32 id = 1;        // 필드 번호 (바이너리 인코딩에 사용)
}

message UserResponse {
  int32 id = 1;
  string name = 2;
  string email = 3;
}

message Empty {}
```

**왜 Protocol Buffers인가?**
- **스키마 강제**: 컴파일 타임에 타입 오류 검출
- **하위 호환성**: 필드 번호로 버전 관리, 새 필드 추가해도 기존 클라이언트 동작
- **작은 페이로드**: JSON 대비 3~10배 작은 크기

### 4가지 통신 패턴

```
┌────────────────────────────────────────────────────────────────┐
│                    gRPC 통신 패턴                               │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Unary (단일 요청-응답)                                      │
│     Client ──── Request ────► Server                           │
│     Client ◄─── Response ──── Server                           │
│     예: 사용자 조회, 로그인                                     │
│                                                                 │
│  2. Server Streaming (서버 스트리밍)                            │
│     Client ──── Request ────► Server                           │
│     Client ◄─── Response 1 ── Server                           │
│     Client ◄─── Response 2 ── Server                           │
│     Client ◄─── Response N ── Server                           │
│     예: 실시간 주식 시세, 로그 스트리밍                         │
│                                                                 │
│  3. Client Streaming (클라이언트 스트리밍)                      │
│     Client ──── Request 1 ───► Server                          │
│     Client ──── Request 2 ───► Server                          │
│     Client ──── Request N ───► Server                          │
│     Client ◄─── Response ───── Server                          │
│     예: 파일 업로드, 배치 데이터 전송                           │
│                                                                 │
│  4. Bidirectional Streaming (양방향 스트리밍)                   │
│     Client ◄───► Request/Response ◄───► Server                 │
│     예: 채팅, 실시간 게임                                       │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### HTTP/2의 이점

| HTTP/1.1 | HTTP/2 (gRPC) |
|----------|---------------|
| 텍스트 기반 | 바이너리 프레이밍 |
| 연결당 1요청 | 멀티플렉싱 (다중 요청) |
| 헤더 반복 전송 | HPACK 헤더 압축 |
| 단방향 | 양방향 스트리밍 |

**왜 HTTP/2가 gRPC에 적합한가?**
- 하나의 TCP 연결로 여러 RPC 호출 동시 처리 → 연결 오버헤드 감소
- 서버 푸시로 스트리밍 구현 용이

## 예제 코드

### Proto 파일 정의

```protobuf
// greeting.proto
syntax = "proto3";

option java_package = "com.example.grpc";

service GreetingService {
  rpc SayHello (HelloRequest) returns (HelloResponse);
}

message HelloRequest {
  string name = 1;
}

message HelloResponse {
  string message = 1;
}
```

### Java Server 구현

```java
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = "Hello, " + request.getName() + "!";

        HelloResponse response = HelloResponse.newBuilder()
                .setMessage(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```

### Java Client 구현

```java
public class GreetingClient {
    private final GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub;

    public GreetingClient(ManagedChannel channel) {
        blockingStub = GreetingServiceGrpc.newBlockingStub(channel);
    }

    public String sayHello(String name) {
        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        HelloResponse response = blockingStub.sayHello(request);
        return response.getMessage();
    }
}
```

## 트레이드오프

### gRPC vs REST

| 항목 | gRPC | REST |
|------|------|------|
| **프로토콜** | HTTP/2 | HTTP/1.1 or 2 |
| **페이로드** | Protocol Buffers (바이너리) | JSON/XML (텍스트) |
| **성능** | 빠름 (직렬화, 압축) | 상대적으로 느림 |
| **브라우저 지원** | 제한적 (grpc-web 필요) | 완벽 지원 |
| **디버깅** | 어려움 (바이너리) | 쉬움 (사람이 읽을 수 있음) |
| **스키마** | 필수 (.proto) | 선택 (OpenAPI) |
| **스트리밍** | 네이티브 지원 | 제한적 |

### 언제 gRPC를 선택하는가?

```
┌─────────────────────────────────────────────────────────┐
│                    선택 가이드                           │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  gRPC 선택                      REST 선택                │
│  ───────────                    ─────────                │
│  ✓ MSA 서비스 간 통신           ✓ Public API             │
│  ✓ 저지연이 중요한 경우         ✓ 브라우저 클라이언트     │
│  ✓ 양방향 스트리밍 필요         ✓ 간단한 CRUD 작업       │
│  ✓ 다양한 언어 간 통신          ✓ 캐싱이 중요한 경우     │
│  ✓ 강타입 계약 필요             ✓ 빠른 프로토타이핑      │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

## 동작 원리

### gRPC 호출 흐름

```
┌────────────────────────────────────────────────────────────────┐
│                     gRPC 호출 흐름                              │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 클라이언트가 Stub 메서드 호출                               │
│     │                                                           │
│     ▼                                                           │
│  2. Request 객체 → Protocol Buffers 직렬화 (바이너리)          │
│     │                                                           │
│     ▼                                                           │
│  3. HTTP/2 프레임으로 래핑                                      │
│     │  - HEADERS 프레임: 메타데이터, 경로 (/package.Service/Method)
│     │  - DATA 프레임: 직렬화된 메시지                           │
│     ▼                                                           │
│  4. TLS 암호화 (선택) → 네트워크 전송                          │
│     │                                                           │
│     ▼                                                           │
│  5. 서버에서 역직렬화 → 핸들러 호출                            │
│     │                                                           │
│     ▼                                                           │
│  6. 응답 직렬화 → HTTP/2 프레임 → 클라이언트 전송              │
│     │                                                           │
│     ▼                                                           │
│  7. 클라이언트에서 역직렬화 → 결과 반환                        │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 면접 예상 질문

### Q: gRPC가 REST보다 빠른 이유는?

A: 세 가지 핵심 요인이 있습니다.

1. **직렬화 효율**: Protocol Buffers는 바이너리 포맷으로 JSON 대비 3~10배 작은 페이로드. 파싱 속도도 빠름
2. **HTTP/2 멀티플렉싱**: 하나의 TCP 연결에서 여러 요청을 동시 처리. HTTP/1.1의 Head-of-Line Blocking 문제 해결
3. **헤더 압축**: HPACK으로 반복되는 헤더를 압축. 작은 요청에서 특히 효과적

**왜 이게 중요한가?** MSA에서 서비스 간 호출이 수백 번 발생할 때, 각 호출의 지연시간 감소가 전체 응답시간에 누적되어 큰 차이를 만듭니다.

### Q: gRPC에서 에러 처리는 어떻게 하는가?

A: gRPC는 표준화된 상태 코드를 사용합니다.

```java
// 서버에서 에러 반환
responseObserver.onError(Status.NOT_FOUND
    .withDescription("User not found: " + userId)
    .asRuntimeException());

// 클라이언트에서 에러 처리
try {
    response = blockingStub.getUser(request);
} catch (StatusRuntimeException e) {
    if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
        // 처리
    }
}
```

주요 상태 코드: `OK`, `CANCELLED`, `INVALID_ARGUMENT`, `NOT_FOUND`, `PERMISSION_DENIED`, `INTERNAL`, `UNAVAILABLE`

### Q: gRPC의 로드밸런싱은 어떻게 동작하는가?

A: 두 가지 방식이 있습니다.

1. **Proxy 기반**: L7 로드밸런서(Envoy, nginx)가 gRPC 트래픽 분산
2. **Client-side**: 클라이언트가 직접 서버 목록을 알고 분산 (라운드로빈, 가중치 등)

Kubernetes 환경에서는 Service Mesh(Istio)와 함께 사용하여 투명한 로드밸런싱을 구현합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [HTTP/HTTPS](./http-https.md) | HTTP/2 이해를 위한 선수 지식 | 중급 |
| [TCP/IP](./tcp-ip.md) | 전송 계층 이해 | 입문 |
| [WebSocket](./websocket.md) | 실시간 통신 비교 대상 | 중급 |
| [Load Balancing](./load-balancing.md) | gRPC 로드밸런싱 심화 | 심화 |

## 참고 자료

- [gRPC 공식 문서](https://grpc.io/docs/)
- [Protocol Buffers 가이드](https://developers.google.com/protocol-buffers)
- [gRPC vs REST 성능 비교](https://blog.dreamfactory.com/grpc-vs-rest-how-does-grpc-compare-with-traditional-rest-apis/)
