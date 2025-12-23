# Load Balancing

**난이도: [4] 심화**

> 여러 서버로 트래픽을 분산하여 가용성과 성능을 향상시키는 기술

`#로드밸런싱` `#LoadBalancing` `#L4` `#L7` `#Nginx` `#HAProxy` `#AWSELB` `#ALB` `#NLB` `#라운드로빈` `#RoundRobin` `#고가용성` `#HighAvailability` `#LeastConnection` `#IPHash` `#ConsistentHashing` `#헬스체크` `#HealthCheck` `#StickySession` `#SessionPersistence` `#DSR` `#DirectServerReturn` `#TLS종료` `#Failover` `#VRRP` `#Keepalived` `#WeightedRoundRobin` `#SPOF` `#수평확장` `#ScaleOut`

## 왜 알아야 하는가?

- **확장성**: 수평 확장(Scale-out)을 통한 시스템 성능 향상의 핵심 기술
- **고가용성**: 서버 장애 시에도 서비스 중단 없이 운영 가능
- **시스템 설계**: 대규모 트래픽을 처리하는 아키텍처 설계의 필수 요소
- **실무 필수**: AWS ELB, Nginx, HAProxy 등 실제 인프라 구축 시 필수 지식
- **성능 최적화**: 적절한 분산 알고리즘 선택으로 응답 시간 단축 및 리소스 효율 향상

## 핵심 개념

- 로드밸런서는 단일 진입점으로 트래픽을 받아 여러 서버로 분산
- L4 vs L7: 전송 계층(포트) vs 애플리케이션 계층(URL, 헤더) 기반 분산
- 분산 알고리즘: Round Robin, Least Connection, IP Hash 등 상황에 맞는 선택 필요
- 헬스체크: 장애 서버를 자동 감지하고 트래픽 분산에서 제외
- 세션 유지 (Session Persistence): 같은 클라이언트를 동일 서버로 라우팅

## 쉽게 이해하기

**로드밸런싱은 은행 번호표 시스템**에 비유할 수 있습니다.

은행에 여러 창구(서버)가 있고, 입구의 번호표 기계(로드밸런서)가 고객을 빈 창구로 안내합니다. 만약 창구가 1개뿐이면 대기 시간이 길어지지만, 10개 창구로 분산하면 대기 시간이 1/10로 줄어듭니다.

예를 들어, 점심시간에 고객이 몰려도(트래픽 급증) 창구를 늘리면 대응할 수 있습니다. 또한 한 창구가 고장나도(서버 장애) 다른 창구로 안내하면 되므로 은행 전체가 멈추지 않습니다. 이것이 바로 로드밸런싱이 제공하는 확장성과 가용성입니다.

번호표 기계가 고장나면? 모든 창구가 정상이어도 은행 업무가 마비됩니다. 따라서 로드밸런서 자체도 이중화(Active-Standby)하여 SPOF (Single Point of Failure)를 제거해야 합니다.

## 상세 설명

### L4 vs L7 로드밸런싱

| 비교 항목 | L4 (Transport Layer) | L7 (Application Layer) |
|----------|---------------------|----------------------|
| 분산 기준 | IP 주소, 포트 번호 | URL, HTTP 헤더, 쿠키 |
| OSI 계층 | 전송 계층 (TCP/UDP) | 애플리케이션 계층 (HTTP) |
| 속도 | 빠름 | 상대적으로 느림 |
| 유연성 | 제한적 | 높음 (콘텐츠 기반 라우팅) |
| 암호화 | TCP 연결만 처리 | TLS 종료 가능 |
| 예시 | AWS NLB, HAProxy TCP | AWS ALB, Nginx, HAProxy HTTP |

#### L4 로드밸런싱

**동작 방식**: TCP/UDP 패킷의 IP와 포트만 보고 분산

```
Client --[TCP SYN]--> L4 LB --[TCP SYN]--> Server A
                              |
                              +--[TCP SYN]--> Server B
```

**왜 빠른가?**

L4는 패킷의 헤더만 보고 포워딩합니다. HTTP 바디를 파싱할 필요가 없어서 CPU 사용량이 적고, 레이턴시가 낮습니다. 단순히 "이 IP:Port는 서버 A로"라는 매핑만 하면 됩니다.

**언제 사용하나?**

- 단순히 트래픽을 여러 서버로 분산만 하면 되는 경우
- 초당 수십만 건 이상의 높은 처리량이 필요한 경우
- DB 서버처럼 HTTP가 아닌 프로토콜을 사용하는 경우
- TLS 종료를 각 백엔드 서버에서 처리하는 경우

#### L7 로드밸런싱

**동작 방식**: HTTP 요청 내용(URL, 헤더 등)을 파싱하여 분산

```
Client -- GET /api/users --> L7 LB --> API Server
       -- GET /static/*  --> L7 LB --> Static Server
       -- Cookie: premium --> L7 LB --> Premium Server
```

**왜 느린가?**

L7은 HTTP 요청을 완전히 파싱해야 합니다:
1. TCP 연결 수립
2. TLS 핸드셰이크 (HTTPS인 경우)
3. HTTP 요청 전체 읽기
4. URL, 헤더 파싱
5. 분산 규칙 적용
6. 백엔드 서버로 새로운 HTTP 요청 생성

이 과정에서 CPU와 메모리를 더 사용하고, 지연 시간이 증가합니다.

**언제 사용하나?**

- **URL 기반 라우팅**: `/api/*`는 API 서버로, `/static/*`는 CDN으로
- **호스트 기반 라우팅**: `api.example.com`과 `web.example.com`을 다른 서버로
- **HTTP 헤더 기반**: User-Agent를 보고 모바일과 데스크톱을 다른 서버로
- **TLS 종료**: 로드밸런서에서 TLS를 종료하고 백엔드는 HTTP로 통신 (관리 편의성)
- **콘텐츠 캐싱**: 자주 요청되는 정적 콘텐츠를 로드밸런서에서 캐싱

**왜 TLS 종료를 로드밸런서에서 하나?**

- **인증서 관리 간편**: 백엔드 서버마다 인증서 설치 불필요, 로드밸런서 1곳에서만 관리
- **성능**: 백엔드 서버는 TLS 암호화/복호화 부담에서 해방
- **보안**: 내부 네트워크는 HTTP로 통신해도 안전 (VPC 내부라고 가정)

단, 내부 네트워크도 암호화가 필요하면 End-to-End TLS를 사용해야 합니다.

### 분산 알고리즘

#### 1. Round Robin (라운드 로빈)

**동작 방식**: 서버를 순서대로 선택

```
Request 1 → Server A
Request 2 → Server B
Request 3 → Server C
Request 4 → Server A (다시 처음으로)
```

**장점**:
- 구현이 가장 간단
- 서버 성능이 동일하면 균등하게 분산

**단점**:
- 각 요청의 처리 시간이 다를 수 있음 (긴 요청과 짧은 요청이 섞여있으면 불균형)
- 서버 성능이 다르면 부적합

**언제 사용하나?**

모든 요청의 처리 시간이 비슷하고, 모든 서버의 성능이 동일한 경우. 예를 들어 정적 파일 서빙처럼 단순한 작업에 적합합니다.

**Weighted Round Robin**:
성능이 다른 서버에 가중치를 부여할 수 있습니다.

```
Server A (weight=3): 요청의 60% 처리
Server B (weight=2): 요청의 40% 처리
```

#### 2. Least Connection (최소 연결)

**동작 방식**: 현재 연결 수가 가장 적은 서버를 선택

```
Server A: 10 connections
Server B: 5 connections  ← 선택
Server C: 8 connections
```

**왜 이렇게 하는가?**

요청의 처리 시간이 천차만별일 때, Round Robin은 불균형을 만듭니다. 예를 들어:
- Server A: 긴 요청 10개 처리 중 (과부하)
- Server B: 짧은 요청 10개 처리 중 (여유)

연결 수만 보면 같지만 실제 부하는 다릅니다. Least Connection은 "처리 중인 연결이 적다 = 여유롭다"로 판단하여 분산합니다.

**언제 사용하나?**

- 긴 연결(Long-lived connection)이 많은 경우: WebSocket, 데이터베이스 연결 풀
- 요청 처리 시간이 크게 다른 경우: 일부는 1초, 일부는 10초

**한계**:
연결 수가 적어도 CPU를 많이 쓰는 작업이면 실제로는 바쁠 수 있습니다. 예를 들어 이미지 인코딩은 연결은 1개지만 CPU를 100% 사용할 수 있습니다.

#### 3. IP Hash

**동작 방식**: 클라이언트 IP를 해시하여 항상 같은 서버로 라우팅

```
hash(192.168.1.10) % 3 = 1 → Server B
hash(192.168.1.20) % 3 = 2 → Server C
hash(192.168.1.10) % 3 = 1 → Server B (동일)
```

**왜 이렇게 하는가?**

세션 정보가 각 서버의 메모리에 저장되어 있다면, 같은 사용자가 다른 서버로 가면 로그인 정보가 없어집니다. IP Hash를 사용하면 동일 클라이언트는 항상 같은 서버로 가서 세션 유지가 됩니다.

**언제 사용하나?**

- Sticky Session이 필요한 경우 (세션 저장소가 각 서버 로컬 메모리)
- 서버별로 캐시를 유지하는 경우 (동일 데이터를 같은 서버에서 처리하면 캐시 히트율 향상)

**단점**:
- 서버 추가/제거 시 해시값이 바뀌어 세션이 깨질 수 있음
- 특정 IP에서 요청이 몰리면 특정 서버에 부하 집중

**해결책: Consistent Hashing**

일반 해시는 서버 추가 시 대부분의 매핑이 바뀌지만, Consistent Hashing은 일부만 재배치됩니다:
- 3대 → 4대 증설: 약 25%만 재매핑 (1/4만 영향)
- 일반 해시: 거의 100% 재매핑

#### 4. 기타 알고리즘

| 알고리즘 | 설명 | 사용 시점 |
|---------|------|----------|
| Random | 무작위 선택 | 간단하고 빠름, 서버 수가 많으면 자연스럽게 균등 분산 |
| Least Response Time | 응답 시간이 가장 빠른 서버 선택 | 서버 성능 차이가 크거나 네트워크 지연이 다를 때 |
| Resource-based | CPU, 메모리 사용률 기반 선택 | 실제 부하를 고려한 정교한 분산 필요 시 |

### 헬스체크

**목적**: 장애 서버를 자동으로 감지하고 트래픽에서 제외

#### 헬스체크 방식

**1. TCP 헬스체크**
```
L4 LB → TCP SYN → Server
        TCP SYN-ACK ← Server
결과: 포트가 열려있으면 정상
```

**장점**: 빠르고 간단
**단점**: 프로세스가 죽었어도 포트가 열려있으면 정상으로 판단

**2. HTTP 헬스체크**
```
L7 LB → GET /health → Server
        200 OK ← Server (또는 503 Service Unavailable)
```

**장점**: 애플리케이션 수준의 정상 여부 확인 가능
**예시**:
```json
GET /health
{
  "status": "UP",
  "database": "UP",
  "redis": "UP"
}
```

데이터베이스 연결이 끊어졌으면 `"status": "DOWN"`을 반환하여 트래픽을 받지 않도록 할 수 있습니다.

**3. 커스텀 헬스체크**
```python
@app.route('/health')
def health_check():
    if db.is_connected() and redis.is_connected() and disk_usage() < 90:
        return 'OK', 200
    return 'Unhealthy', 503
```

**왜 단순 200 OK가 아니라 의존성까지 확인하나?**

서버 프로세스는 살아있어도 데이터베이스 연결이 끊어졌다면, 모든 요청이 실패합니다. 이런 서버로 트래픽을 보내면 사용자에게 에러만 돌려줍니다. 따라서 핵심 의존성까지 확인해야 진짜 "정상"입니다.

#### 헬스체크 파라미터

```yaml
healthcheck:
  interval: 10s        # 10초마다 체크
  timeout: 3s          # 3초 내에 응답 없으면 실패
  retries: 3           # 3번 연속 실패 시 제외
  start_period: 30s    # 서버 시작 후 30초간은 헬스체크 유예
```

**왜 3번 연속 실패 시 제외하나?**

일시적인 네트워크 지연으로 1번 실패할 수 있습니다. 즉시 제외하면 정상 서버도 불필요하게 제외되는 False Positive가 발생합니다. 3번 연속 실패해야 제외하면 안정성이 높아집니다.

**왜 start_period가 필요한가?**

서버가 시작될 때 데이터베이스 커넥션 풀 생성, 캐시 워밍업 등 초기화 작업이 필요합니다. 이 시간 동안은 헬스체크가 실패할 수 있으므로, 유예 기간을 두어야 합니다.

### 세션 유지 (Session Persistence)

**문제**: 세션 정보가 서버 로컬 메모리에 있으면, 다른 서버로 요청이 가면 로그인 풀림

#### 해결 방법

**1. Sticky Session (Client Affinity)**

로드밸런서가 쿠키로 서버를 추적:
```
1. Client → LB → Server A (쿠키: server=A 설정)
2. Client → LB (쿠키: server=A 포함) → Server A
```

**장점**: 세션 저장소 불필요
**단점**:
- 서버 장애 시 세션 손실
- 부하 분산 불균형 (특정 서버에 긴 세션 몰릴 수 있음)

**2. 세션 클러스터링**

여러 서버가 세션을 복제:
```
Server A: session_123 생성
         ↓ (복제)
Server B: session_123 사본
Server C: session_123 사본
```

**장점**: 서버 장애에도 세션 유지
**단점**: 네트워크 오버헤드, 복잡성 증가

**3. 중앙 세션 저장소 (권장)**

Redis, Memcached에 세션 저장:
```
Client → Server A → Redis (session_123 저장)
Client → Server B → Redis (session_123 조회)
```

**왜 이 방법이 권장되나?**

- 모든 서버가 동일한 세션 정보 접근
- 서버 추가/제거에도 세션 유지
- 서버가 Stateless해져서 수평 확장 용이

**트레이드오프**: Redis 장애 시 모든 세션 손실 위험 → Redis도 이중화 필요

## 동작 원리

### 로드밸런싱 방식

#### 1. DSR (Direct Server Return)

```
Client
  ↓ Request
Load Balancer
  ↓
Server → Response → Client (로드밸런서 경유 안 함)
```

**왜 이렇게 하는가?**

응답 트래픽이 요청보다 훨씬 큽니다 (예: 동영상 다운로드). 응답까지 로드밸런서를 거치면 로드밸런서가 병목이 됩니다. DSR은 응답을 직접 전송하여 로드밸런서 부하를 줄입니다.

**단점**: L4에서만 가능, L7은 HTTP 파싱이 필요해서 DSR 불가

#### 2. Proxy 방식 (일반적)

```
Client
  ↓ Request
Load Balancer
  ↓
Server
  ↓ Response
Load Balancer
  ↓
Client
```

L7 로드밸런서는 대부분 이 방식을 사용합니다.

## 트레이드오프

| 방식 | 장점 | 단점 |
|------|------|------|
| L4 | 빠른 속도, 높은 처리량 | 제한적인 라우팅 (IP/포트만) |
| L7 | 유연한 라우팅 (URL, 헤더), TLS 종료 | 느린 속도, 높은 CPU 사용 |
| Round Robin | 구현 간단, 균등 분산 | 요청 시간 차이 고려 안 함 |
| Least Connection | 긴 연결에 효과적 | 실제 부하 반영 안 됨 |
| IP Hash | 세션 유지 용이 | 불균형 가능, 확장성 제한 |
| Sticky Session | 간단함, 세션 저장소 불필요 | 장애 시 세션 손실, 불균형 |
| 중앙 세션 저장소 | 확장성, 안정성 | Redis 의존, 네트워크 지연 |

## 면접 예상 질문

- Q: L4와 L7 로드밸런서의 차이점은 무엇인가요?
  - A: L4는 전송 계층에서 IP 주소와 포트 번호만 보고 분산하므로 빠릅니다. 패킷 헤더만 보고 포워딩하면 되기 때문에 CPU 사용량이 적고 초당 수십만 건 이상의 높은 처리량을 제공합니다. 반면 L7은 애플리케이션 계층에서 HTTP 요청 내용을 파싱하여 URL, 헤더, 쿠키 등을 기반으로 분산합니다. TLS 종료, 콘텐츠 캐싱, URL 기반 라우팅이 가능하지만 파싱 과정에서 CPU를 더 사용하여 상대적으로 느립니다. 따라서 단순 분산은 L4, 복잡한 라우팅이나 TLS 종료가 필요하면 L7을 사용합니다.

- Q: Round Robin과 Least Connection 알고리즘은 어떤 상황에 각각 적합한가요?
  - A: Round Robin은 모든 요청의 처리 시간이 비슷하고 서버 성능이 동일할 때 적합합니다. 정적 파일 서빙처럼 단순한 작업에 사용합니다. 하지만 긴 요청과 짧은 요청이 섞여있으면 불균형이 발생합니다. 예를 들어 Server A는 10초 걸리는 요청 10개를 처리 중이고, Server B는 1초 걸리는 요청 10개를 처리 중이면 연결 수는 같지만 실제 부하는 10배 차이가 납니다. Least Connection은 이런 상황에서 현재 연결 수가 적은 서버로 보내므로 더 공평하게 분산됩니다. 따라서 WebSocket처럼 긴 연결이 많거나 요청 처리 시간이 천차만별일 때 Least Connection을 사용합니다.

- Q: 헬스체크는 왜 필요하며, 어떻게 구현하나요?
  - A: 헬스체크는 장애 서버를 자동으로 감지하고 트래픽 분산에서 제외하기 위해 필요합니다. 만약 헬스체크가 없으면 로드밸런서는 죽은 서버에도 트래픽을 보내서 사용자에게 에러를 돌려줍니다. L4는 TCP 포트가 열려있는지 확인하고, L7은 HTTP 엔드포인트(예: GET /health)를 호출하여 200 OK를 받는지 확인합니다. 더 정교하게는 /health 엔드포인트에서 데이터베이스, Redis 등 핵심 의존성까지 확인합니다. 왜냐하면 서버 프로세스는 살아있어도 DB 연결이 끊어졌다면 모든 요청이 실패하기 때문입니다. 보통 10초마다 체크하고, 3번 연속 실패 시 제외하는 방식으로 설정하여 일시적인 네트워크 지연으로 인한 오탐을 방지합니다.

- Q: 세션 유지를 위해 Sticky Session과 중앙 세션 저장소 중 어떤 것을 선택해야 하나요?
  - A: 대부분의 경우 중앙 세션 저장소(Redis, Memcached)를 권장합니다. Sticky Session은 쿠키로 같은 서버로 라우팅하여 세션 저장소가 불필요하지만, 서버 장애 시 세션이 손실되고 특정 서버에 긴 세션이 몰려 부하 불균형이 발생할 수 있습니다. 반면 중앙 세션 저장소는 모든 서버가 Redis에서 세션을 조회하므로 어느 서버로 가도 세션이 유지되고, 서버를 자유롭게 추가/제거할 수 있어 수평 확장이 용이합니다. 또한 서버가 Stateless해져서 관리가 쉬워집니다. 단, Redis 자체가 SPOF가 될 수 있으므로 Redis도 Master-Slave 구조로 이중화해야 합니다. Sticky Session은 소규모 시스템이나 세션 손실이 치명적이지 않은 경우(예: 장바구니)에만 사용합니다.

- Q: 로드밸런서 자체가 SPOF가 될 수 있는데, 어떻게 해결하나요?
  - A: 로드밸런서도 이중화(Active-Standby 또는 Active-Active)하고, VRRP(Virtual Router Redundancy Protocol)나 Keepalived를 사용하여 장애 시 자동으로 Failover합니다. Active-Standby 방식에서는 평소에 Active 로드밸런서가 모든 트래픽을 처리하고, Standby는 대기합니다. Active가 죽으면 VIP (Virtual IP)가 Standby로 이동하여 즉시 트래픽을 받습니다. Active-Active 방식에서는 두 로드밸런서가 DNS Round Robin이나 Anycast로 트래픽을 나눠 받습니다. 클라우드 환경에서는 AWS ELB, GCP Load Balancer처럼 managed 서비스를 사용하면 자동으로 이중화되므로 SPOF 걱정이 없습니다. 이들은 여러 가용 영역(AZ)에 걸쳐 분산 배치되어 한 데이터센터가 통째로 장애나도 서비스가 유지됩니다.

- Q: IP Hash 방식에서 서버를 추가하면 세션이 깨지는데, 어떻게 해결하나요?
  - A: Consistent Hashing을 사용하면 서버 추가 시 영향받는 세션을 최소화할 수 있습니다. 일반 해시는 `hash(ip) % 서버수`로 계산하므로, 서버 3대에서 4대로 늘리면 분모가 바뀌어 거의 모든 매핑이 변경됩니다. 반면 Consistent Hashing은 해시 링 구조를 사용하여 서버를 추가해도 인접한 일부 키만 재매핑됩니다. 예를 들어 3대에서 4대로 늘리면 약 25%(1/4)만 영향받고 나머지 75%는 그대로 유지됩니다. 또한 가상 노드(Virtual Node)를 사용하면 서버별 부하를 균등하게 분산할 수 있습니다. 하지만 근본적으로 세션을 서버 로컬에 저장하는 것이 문제이므로, 중앙 세션 저장소(Redis)를 사용하면 서버 추가/제거와 무관하게 세션이 유지됩니다.

## 연관 문서

- [HTTP/HTTPS](./http-https.md) - L7 로드밸런서는 HTTP 헤더와 URL 기반 라우팅
- [TCP/IP](./tcp-ip.md) - L4 로드밸런서는 TCP/UDP 포트 기반 분산
- [DNS](./dns.md) - DNS 라운드 로빈을 통한 부하 분산
- [CDN](./cdn.md) - CDN 엣지 서버 간 트래픽 분산에 로드밸런싱 활용

## 참고 자료

- AWS Elastic Load Balancing Documentation
- Nginx Load Balancing Documentation
- HAProxy Configuration Manual
- "The Art of Scalability" - Martin L. Abbott, Michael T. Fisher
