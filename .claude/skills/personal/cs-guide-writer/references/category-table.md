# 카테고리 판단 기준

## 카테고리별 주제 분류표

| 카테고리 | 세부 분류 | 주제 예시 |
|----------|----------|----------|
| `network/` | 프로토콜 | HTTP, HTTPS, TCP/IP, UDP, WebSocket, gRPC |
| | 아키텍처 | REST API, GraphQL, CDN, 로드밸런싱, DNS |
| | 보안 | TLS/SSL, CORS, 방화벽, VPN |
| `os/` | 프로세스/스레드 | 컨텍스트 스위칭, 스케줄링, PCB, TCB |
| | 메모리 | 가상 메모리, 페이징, 세그멘테이션, GC |
| | 동기화 | 뮤텍스, 세마포어, 데드락, 조건 변수 |
| | 파일 시스템 | 파일 디스크립터, I/O 모델, 버퍼/캐시 |
| | IPC | 파이프, 소켓, 공유 메모리, 메시지 큐 |
| `db/` | 기초 | 정규화, 인덱스, 트랜잭션, ACID |
| | 성능 | 쿼리 최적화, 실행 계획, 커넥션 풀, N+1 |
| | 분산 | 샤딩, 복제, 파티셔닝, CAP, PACELC |
| | NoSQL | Redis, MongoDB, Elasticsearch, 캐싱 전략 |
| `algorithm/` | 기초 | 시간/공간 복잡도, Big-O, 점근 표기법 |
| | 자료구조 | Array, LinkedList, Tree, Graph, Hash |
| | 알고리즘 | 정렬, 탐색, DP, 그리디, 백트래킹, 분할정복 |
| `data-structure/` | 선형 | Array, LinkedList, Stack, Queue, Deque |
| | 비선형 | Tree, Graph, Heap, Trie |
| | 해시 | HashMap, HashSet, 충돌 해결 |
| `system-design/` | 확장성 | 수평/수직 확장, 로드밸런싱, Rate Limiting |
| | 분산 시스템 | 분산 락, 합의 알고리즘, 이벤트 소싱, CDC |
| | 아키텍처 | MSA, 이벤트 드리븐, CQRS, DDD, 헥사고날 |
| | 메시징 | Kafka, RabbitMQ, 메시지 큐 패턴, Pub/Sub |
| `git/` | 기초 | 버전 관리, 커밋, 브랜치, 머지 |
| | 전략 | Git Flow, GitHub Flow, Trunk-Based |
| | 내부 | Git 객체, 참조, reflog |
| `language/` | Java | JVM, GC, 동시성, Stream, 메모리 모델 |
| | Kotlin | 코루틴, Null Safety, 확장 함수, DSL |
| | 일반 | 타입 시스템, 컴파일러, 인터프리터 |
| `programming/` | 패러다임 | OOP, 함수형 프로그래밍, 리액티브 |
| | 설계 | SOLID, 디자인 패턴, 클린 아키텍처 |
| | 품질 | TDD, 리팩토링, 코드 리뷰, 정적 분석 |
| | API | REST 설계, API 버저닝, OpenAPI, 문서화 |
| `security/` | 인증/인가 | OAuth 2.0, JWT, Session, RBAC, ABAC |
| | 암호화 | 대칭/비대칭, 해싱, Salt, HTTPS |
| | 취약점 | OWASP Top 10, SQL Injection, XSS, CSRF |
| | 보안 설계 | 제로 트러스트, 시크릿 관리, 보안 감사 |
| `ai-agent/` | 기초 | LLM 동작 원리, 토큰화, 프롬프트 엔지니어링 |
| | 도구 | AI Agent, MCP, Function Calling, Tool Use |
| | 응용 | RAG, 벡터 DB, 임베딩, Fine-tuning |
| `devops/` | CI/CD | 파이프라인, GitHub Actions, GitOps |
| | 컨테이너 | Docker, Kubernetes, Helm, 서비스 메시 |
| | 모니터링 | 로깅, 메트릭, 트레이싱, APM, SRE |
| | 인프라 | IaC, Terraform, 클라우드 서비스 |
| `trend/` | - | 최신 기술 트렌드, 연도별 이슈 |

---

## 옵션 2: 카테고리별 추천 주제

"오늘의 CS" 주제 미지정 시 카테고리를 선택하면 아래 주제를 추천합니다.

| 대분류 | 세부 분야 | 추천 주제 예시 |
|--------|----------|---------------|
| **Network** | 프로토콜 | HTTP/HTTPS, TCP/IP, UDP, WebSocket, gRPC |
| | 아키텍처 | REST API, GraphQL, CDN, 로드밸런싱, DNS |
| | 보안 | TLS/SSL, CORS, 네트워크 보안 |
| **OS** | 프로세스/스레드 | 프로세스 vs 스레드, 컨텍스트 스위칭, 스케줄링 |
| | 메모리 | 가상 메모리, 페이징, 메모리 단편화, GC |
| | 동기화 | 뮤텍스, 세마포어, 데드락, IPC |
| | 파일 시스템 | 파일 디스크립터, I/O 모델, 버퍼/캐시 |
| **DB** | 기초 | 정규화, 인덱스, 트랜잭션, ACID |
| | 성능 | 쿼리 최적화, 실행 계획, 커넥션 풀 |
| | 분산 | 샤딩, 복제, 파티셔닝, CAP 정리 |
| | NoSQL | Redis, MongoDB, Elasticsearch, 캐싱 전략 |
| **Algorithm** | 기초 | 시간/공간 복잡도, Big-O 표기법 |
| | 자료구조 | Array, LinkedList, Tree, Graph, Hash |
| | 알고리즘 | 정렬, 탐색, DP, 그리디, 백트래킹 |
| **System Design** | 확장성 | 수평/수직 확장, 로드밸런싱, Rate Limiting |
| | 분산 시스템 | 분산 락, 합의 알고리즘, 이벤트 소싱, CDC |
| | 아키텍처 | MSA, 이벤트 드리븐, CQRS, DDD |
| | 메시징 | Kafka, RabbitMQ, 메시지 큐 패턴 |
| **Programming** | 패러다임 | OOP, 함수형 프로그래밍, 리액티브 |
| | 설계 | SOLID, 디자인 패턴, 클린 아키텍처 |
| | 품질 | TDD, 리팩토링, 코드 리뷰, 테스트 전략 |
| | API | REST 설계, API 버저닝, 문서화 |
| **Security** | 인증/인가 | OAuth, JWT, Session, RBAC |
| | 암호화 | 대칭/비대칭 암호화, 해싱, Salt |
| | 취약점 | OWASP Top 10, SQL Injection, XSS, CSRF |
| **AI/Agent** | 기초 | LLM 동작 원리, 프롬프트 엔지니어링 |
| | 도구 | AI Agent, MCP, Function Calling |
| | 응용 | RAG, 벡터 DB, 임베딩 |
| **DevOps** | CI/CD | 파이프라인, 자동화, GitOps |
| | 컨테이너 | Docker, Kubernetes, 서비스 메시 |
| | 모니터링 | 로깅, 메트릭, 트레이싱, APM |
| **Language** | Java | JVM, GC, 동시성, Stream API |
| | Kotlin | 코루틴, Null Safety, 확장 함수 |
| | 일반 | 타입 시스템, 메모리 모델, 컴파일러 |
