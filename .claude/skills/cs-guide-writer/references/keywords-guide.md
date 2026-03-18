# 키워드 작성 가이드

문서 상단에 검색용 키워드를 작성합니다. 나중에 검색 시 해당 문서를 쉽게 찾을 수 있도록 합니다.

## 키워드 작성 원칙

1. **최대 30개**까지 작성 가능 (내용에 맞게 충분히 작성)
2. **한글/영문** 모두 포함 (검색 편의)
3. **약어와 전체 명칭** 모두 포함
4. **관련 기술/도구/서비스/프레임워크** 포함
5. **동의어/유사어/대체 용어** 포함
6. **사용 사례/시나리오** 관련 키워드 포함
7. **면접 키워드** 포함 (자주 나오는 질문 관련)

---

## 키워드 분류 체계

| 분류 | 설명 | 예시 |
|------|------|------|
| **핵심 개념** | 주제의 핵심 용어 (한글/영문) | `#트랜잭션` `#Transaction` |
| **약어/전체명** | 약어와 풀네임 | `#MSA` `#마이크로서비스` `#Microservices` |
| **관련 기술** | 함께 사용되는 기술/도구 | `#Spring` `#JPA` `#Hibernate` |
| **하위 개념** | 세부 개념/종류 | `#ReadCommitted` `#Serializable` |
| **사용 사례** | 실제 적용 시나리오 | `#분산락` `#동시성제어` `#데드락` |
| **비교 대상** | 대안/경쟁 기술 | `#MySQL` `#PostgreSQL` `#Oracle` |
| **면접 키워드** | 면접 빈출 주제 | `#ACID` `#격리수준` `#락` |

---

## 카테고리별 키워드 예시 (확장)

| 카테고리 | 주제 | 키워드 (최대 30개) |
|----------|------|-------------------|
| **AI/Agent** | AI Agent | `#AI에이전트` `#AIAgent` `#LLM` `#대규모언어모델` `#Claude` `#GPT` `#Gemini` `#ChatGPT` `#Anthropic` `#OpenAI` `#Google` `#자율AI` `#Agentic` `#ToolUse` `#FunctionCalling` `#ReAct` `#CoT` `#ChainOfThought` `#프롬프트` `#RAG` `#벡터DB` `#임베딩` `#파인튜닝` `#토큰` `#컨텍스트윈도우` |
| **Network** | WebSocket | `#웹소켓` `#WebSocket` `#WS` `#WSS` `#실시간통신` `#양방향통신` `#FullDuplex` `#Socket.io` `#STOMP` `#SockJS` `#채팅` `#푸시알림` `#게임서버` `#Polling` `#LongPolling` `#SSE` `#ServerSentEvents` `#HTTP업그레이드` `#핸드셰이크` `#연결유지` `#Stateful` `#Spring` `#Netty` |
| **DB** | 트랜잭션 | `#트랜잭션` `#Transaction` `#ACID` `#원자성` `#Atomicity` `#일관성` `#Consistency` `#격리성` `#Isolation` `#지속성` `#Durability` `#커밋` `#Commit` `#롤백` `#Rollback` `#격리수준` `#IsolationLevel` `#ReadUncommitted` `#ReadCommitted` `#RepeatableRead` `#Serializable` `#락` `#Lock` `#데드락` `#Deadlock` `#동시성` `#MVCC` |
| **OS** | 프로세스 | `#프로세스` `#Process` `#스레드` `#Thread` `#PCB` `#ProcessControlBlock` `#컨텍스트스위칭` `#ContextSwitch` `#스케줄링` `#Scheduling` `#fork` `#exec` `#IPC` `#프로세스간통신` `#공유메모리` `#파이프` `#소켓` `#메시지큐` `#좀비프로세스` `#고아프로세스` `#멀티프로세스` `#멀티스레드` |
| **System Design** | MSA | `#MSA` `#마이크로서비스` `#Microservices` `#분산시스템` `#DistributedSystem` `#API게이트웨이` `#APIGateway` `#서비스메시` `#ServiceMesh` `#Istio` `#Envoy` `#서비스디스커버리` `#로드밸런싱` `#서킷브레이커` `#Resilience4j` `#Saga패턴` `#CQRS` `#이벤트소싱` `#EventDriven` `#Kafka` `#RabbitMQ` `#Docker` `#Kubernetes` `#컨테이너` `#오케스트레이션` |
| **Programming** | OOP | `#OOP` `#객체지향` `#ObjectOriented` `#캡슐화` `#Encapsulation` `#상속` `#Inheritance` `#다형성` `#Polymorphism` `#추상화` `#Abstraction` `#SOLID` `#SRP` `#OCP` `#LSP` `#ISP` `#DIP` `#클래스` `#Class` `#인터페이스` `#Interface` `#추상클래스` `#오버라이딩` `#오버로딩` `#디자인패턴` `#의존성주입` `#DI` |
| **Security** | JWT | `#JWT` `#JsonWebToken` `#토큰` `#Token` `#인증` `#Authentication` `#인가` `#Authorization` `#AccessToken` `#RefreshToken` `#Bearer` `#Claim` `#Payload` `#Header` `#Signature` `#HS256` `#RS256` `#만료시간` `#토큰탈취` `#토큰무효화` `#블랙리스트` `#OAuth` `#OIDC` `#세션` `#Stateless` |

---

## 키워드 포맷

```markdown
`#핵심한글` `#CoreEnglish` `#약어` `#관련기술1` `#관련기술2` `#하위개념1` `#하위개념2` `#사용사례` `#비교대상` `#면접키워드`
```

---

## 키워드 개수 가이드

| 문서 복잡도 | 권장 키워드 수 |
|------------|---------------|
| 기초 개념 (정의) | 10~15개 |
| 중급 개념 | 15~20개 |
| 심화 개념 | 20~25개 |
| 복합 주제 | 25~30개 |
