# WebSocket

> 클라이언트와 서버 간 지속적인 양방향 통신을 위한 프로토콜

## 핵심 개념

- **Full-Duplex 통신**: 클라이언트와 서버가 동시에 데이터를 주고받을 수 있음
- **Persistent Connection**: 한 번 연결하면 명시적으로 종료할 때까지 유지
- **Low Latency**: HTTP의 요청-응답 오버헤드 없이 실시간 데이터 전송

## 쉽게 이해하기

**전화 통화 vs 편지**

- **HTTP**: 편지를 보내고 답장을 기다림 (요청마다 새 연결)
- **WebSocket**: 전화 연결 후 자유롭게 대화 (연결 유지, 양방향)

```
HTTP (Polling)                    WebSocket
┌────────┐    요청    ┌────────┐   ┌────────┐  연결 유지  ┌────────┐
│ Client │ ────────→ │ Server │   │ Client │ ←────────→ │ Server │
│        │ ←──────── │        │   │        │            │        │
└────────┘    응답    └────────┘   └────────┘            └────────┘
    ↓                                  ↑
 매번 새 연결                      한 번 연결, 계속 통신
```

## 상세 설명

### HTTP vs WebSocket

| 특성 | HTTP | WebSocket |
|------|------|-----------|
| 통신 방향 | 단방향 (요청-응답) | 양방향 (Full-Duplex) |
| 연결 유지 | 요청마다 새 연결 | 지속 연결 |
| 오버헤드 | 매 요청마다 헤더 전송 | 초기 핸드셰이크 후 최소 오버헤드 |
| 서버 푸시 | 불가능 (Polling 필요) | 가능 |
| 포트 | 80 (HTTP), 443 (HTTPS) | 80 (WS), 443 (WSS) |

**왜 WebSocket이 필요한가?**

HTTP는 클라이언트가 먼저 요청해야만 서버가 응답할 수 있습니다. 실시간 채팅, 주식 시세, 게임처럼 서버에서 즉시 데이터를 보내야 하는 경우 HTTP Polling은 비효율적입니다.

### WebSocket 핸드셰이크

```
Client → Server (HTTP Upgrade 요청)
─────────────────────────────────────
GET /chat HTTP/1.1
Host: server.example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Sec-WebSocket-Version: 13

Server → Client (101 Switching Protocols)
─────────────────────────────────────
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
```

**왜 HTTP로 시작하는가?**
- 기존 인프라(프록시, 방화벽)와의 호환성
- 포트 80/443을 그대로 사용 가능
- 점진적 업그레이드 방식

### 연결 상태

```
┌─────────────────────────────────────────────────────────┐
│                    WebSocket 생명주기                     │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  CONNECTING ──→ OPEN ──→ CLOSING ──→ CLOSED            │
│       │           │          │          │               │
│   핸드셰이크    메시지 전송   종료 요청    연결 종료         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 실시간 통신 방식 비교

#### 1. Polling

```java
// 주기적으로 서버에 요청
@Scheduled(fixedRate = 1000)
public void pollMessages() {
    List<Message> messages = restTemplate.getForObject("/api/messages", List.class);
    // 새 메시지가 없어도 계속 요청
}
```

- 단순하지만 비효율적 (불필요한 요청 발생)
- 실시간성 떨어짐 (폴링 간격만큼 지연)

#### 2. Long Polling

```java
// 서버가 새 데이터 있을 때까지 응답 보류
@GetMapping("/messages")
public DeferredResult<List<Message>> getMessages() {
    DeferredResult<List<Message>> result = new DeferredResult<>(30000L);
    messageQueue.addListener(result::setResult);
    return result;
}
```

- Polling보다 효율적
- 여전히 연결 재수립 오버헤드 존재

#### 3. Server-Sent Events (SSE)

```java
// 서버 → 클라이언트 단방향 스트림
@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Message> streamMessages() {
    return messageService.getMessageStream();
}
```

- 서버 → 클라이언트 단방향만 가능
- HTTP 기반으로 구현 간단

#### 4. WebSocket

```java
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 모든 연결된 클라이언트에게 브로드캐스트
        sessions.forEach(s -> {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                // 에러 처리
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
```

- 양방향 실시간 통신
- 낮은 지연시간, 적은 오버헤드

### Spring WebSocket 설정

```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler(), "/ws/chat")
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler();
    }
}
```

### STOMP over WebSocket

복잡한 메시징 시나리오에는 STOMP(Simple Text Oriented Messaging Protocol) 사용:

```java
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .withSockJS();  // WebSocket 미지원 브라우저 폴백
    }
}

@Controller
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) {
        return message;
    }
}
```

**왜 STOMP를 사용하는가?**
- 메시지 라우팅 표준화 (pub/sub 패턴)
- 메시지 브로커(RabbitMQ, ActiveMQ) 연동 용이
- SockJS 폴백으로 호환성 확보

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 실시간 양방향 통신 | Stateful 연결로 수평 확장 복잡 |
| 낮은 지연시간 | 연결 관리 오버헤드 |
| 적은 네트워크 오버헤드 | 일부 프록시/방화벽 호환성 이슈 |
| 서버 푸시 가능 | HTTP/2 Server Push와 역할 중복 |

## 확장성 고려사항

### 문제: Stateful 연결

WebSocket은 상태를 유지하므로 로드밸런서 뒤에 여러 서버가 있으면 문제 발생:

```
사용자 A ──→ Server 1 (연결 유지)
사용자 B ──→ Server 2 (연결 유지)

→ A가 B에게 메시지 보내려면?
```

### 해결책

1. **Sticky Session**: 같은 사용자는 같은 서버로 라우팅
2. **Message Broker**: Redis Pub/Sub, Kafka로 서버 간 메시지 전파
3. **Shared State**: Redis에 세션 정보 저장

```java
// Redis Pub/Sub을 통한 서버 간 메시지 전파
@Service
public class RedisMessageRelay {

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void subscribe() {
        redisTemplate.getConnectionFactory().getConnection()
            .subscribe((message, pattern) -> {
                messagingTemplate.convertAndSend("/topic/chat",
                    new String(message.getBody()));
            }, "chat-channel".getBytes());
    }

    public void publish(String message) {
        redisTemplate.convertAndSend("chat-channel", message);
    }
}
```

## 사용 사례

| 사용 사례 | 이유 |
|----------|------|
| 실시간 채팅 | 양방향 즉시 메시지 전달 |
| 주식/암호화폐 시세 | 고빈도 업데이트, 낮은 지연 |
| 온라인 게임 | 실시간 상태 동기화 |
| 협업 도구 (Google Docs) | 동시 편집 동기화 |
| IoT 대시보드 | 센서 데이터 실시간 모니터링 |

## 면접 예상 질문

### Q: WebSocket과 HTTP의 차이점은 무엇인가요?

A: HTTP는 **요청-응답 기반 단방향 통신**으로 클라이언트가 먼저 요청해야 서버가 응답합니다. WebSocket은 **지속적인 양방향 통신**으로 한 번 연결하면 클라이언트와 서버 모두 자유롭게 메시지를 보낼 수 있습니다. 실시간 채팅처럼 서버가 먼저 데이터를 보내야 하는 경우 WebSocket이 적합합니다.

### Q: WebSocket 대신 HTTP Polling을 사용하면 안 되나요?

A: 가능하지만 비효율적입니다. Polling은 새 데이터가 없어도 계속 요청하므로 **불필요한 네트워크 트래픽과 서버 부하**가 발생합니다. 또한 폴링 간격만큼 지연이 생겨 **실시간성이 떨어집니다**. WebSocket은 새 데이터가 있을 때만 전송하므로 효율적입니다.

### Q: WebSocket의 확장성 문제와 해결책은?

A: WebSocket은 **Stateful 연결**이므로 수평 확장 시 서버 간 세션 공유 문제가 생깁니다. 해결책으로:
1. **Sticky Session**: 같은 사용자를 같은 서버로 라우팅
2. **Message Broker**: Redis Pub/Sub이나 Kafka로 서버 간 메시지 전파
3. **연결 상태 외부화**: Redis에 세션 정보 저장

대규모 서비스에서는 보통 Redis Pub/Sub과 STOMP를 조합하여 사용합니다.

## 참고 자료

- [RFC 6455 - The WebSocket Protocol](https://datatracker.ietf.org/doc/html/rfc6455)
- [Spring WebSocket Documentation](https://docs.spring.io/spring-framework/reference/web/websocket.html)
- [MDN WebSocket API](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket)
