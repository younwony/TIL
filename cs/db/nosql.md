# NoSQL

> 관계형 데이터베이스(RDBMS)가 아닌 다양한 형태의 데이터 저장 및 검색을 위한 데이터베이스 시스템. "Not Only SQL"의 약자

> `[4] 심화` · 선수 지식: [SQL](./sql.md), [Transaction](./transaction.md), [Normalization](./normalization.md)

## 왜 알아야 하는가?

현대 웹 서비스는 RDBMS만으로 모든 요구사항을 충족하기 어렵습니다. 세션 저장은 Redis, 로그 수집은 Cassandra, 추천 시스템은 Neo4j처럼 각 상황에 맞는 NoSQL을 선택해야 합니다. 하지만 NoSQL을 잘못 선택하면 오히려 복잡도만 증가하고 성능도 저하됩니다. 백엔드 개발자라면 RDBMS와 NoSQL의 트레이드오프, CAP 정리, 각 NoSQL 유형의 특징을 이해하고, 언제 어떤 데이터베이스를 사용해야 하는지 판단할 수 있어야 합니다. 이는 확장 가능하고 효율적인 시스템 아키텍처를 설계하는 핵심 역량입니다.

## 핵심 개념

- **Document DB**: JSON/BSON 형태로 문서 저장 (MongoDB, CouchDB)
- **Key-Value DB**: 키-값 쌍으로 데이터 저장 (Redis, DynamoDB)
- **Column-Family DB**: 컬럼 기반 저장으로 대용량 데이터 처리 (Cassandra, HBase)
- **Graph DB**: 노드와 간선으로 관계 중심 데이터 저장 (Neo4j, ArangoDB)
- **CAP 정리**: 일관성(Consistency), 가용성(Availability), 분할 내성(Partition Tolerance) 중 2개만 선택 가능

## 쉽게 이해하기

**NoSQL**을 다양한 형태의 수납 방식에 비유할 수 있습니다.

**관계형 DB (RDBMS)**는 엑셀 표와 같습니다:
- 행과 열이 명확히 정해져 있음
- 모든 행이 같은 구조를 가짐
- 규칙적이고 정형화됨
- 예: 학생 명부 (학번, 이름, 학과가 모든 학생에게 동일)

**NoSQL**은 다양한 수납 방식입니다:

**1. Document DB (MongoDB)** = 파일 캐비닛
- 각 문서(파일)는 독립적이고 구조가 다를 수 있음
- "사원 A의 파일"에는 이름, 부서, 연봉이 있고
- "사원 B의 파일"에는 이름, 부서, 연봉, 자격증, 프로젝트 목록이 있음 (유연함)

**2. Key-Value DB (Redis)** = 사물함
- 번호(Key)만 알면 즉시 내용물(Value) 꺼내기
- "1001번 사물함" → 책
- "1002번 사물함" → 운동화
- 매우 빠르지만, "빨간 운동화는 몇 번 사물함?" 같은 검색은 불가

**3. Column-Family DB (Cassandra)** = 책장 (수직 정리)
- 일반 표는 "행"으로 저장 (학생1: 이름, 나이, 학과)
- Column-Family는 "열"로 저장 ("이름" 컬럼: 철수, 영희, 민수...)
- 특정 컬럼만 조회할 때 매우 빠름

**4. Graph DB (Neo4j)** = 관계도 (인맥 지도)
- "철수는 영희의 친구이고, 영희는 민수의 동료이다"
- 관계를 따라가며 탐색: "철수의 친구의 친구는?"
- SNS, 추천 시스템에 적합

**CAP 정리**는 "빠름, 정확함, 저렴함 중 2개만 선택"과 같습니다:
- **일관성(C)**: 모든 사용자가 같은 데이터를 봄 (정확함)
- **가용성(A)**: 항상 응답 가능 (빠름)
- **분할 내성(P)**: 서버 간 통신 실패해도 작동 (안정적)
- 네트워크 분할(P)은 불가피하므로, 실제로는 **C와 A 중 선택**

## 상세 설명

### NoSQL이 필요한 이유

**RDBMS의 한계**:

1. **스키마 고정**: 컬럼 추가/변경 시 전체 테이블 ALTER 필요
2. **수평 확장 어려움**: Sharding 복잡, JOIN 성능 저하
3. **대용량 데이터**: 수억-수조 건 처리에 부적합
4. **유연성 부족**: 비정형 데이터(JSON, XML) 저장 어려움

**NoSQL의 장점**:

1. **스키마 유연**: 문서마다 다른 구조 허용
2. **수평 확장 쉬움**: Sharding 기본 지원
3. **대용량 처리**: 분산 저장 및 처리
4. **고성능**: 특정 워크로드에 최적화

**왜 "Not Only SQL"인가?**

RDBMS를 대체하는 것이 아니라, **특정 상황에 더 적합한 선택지**를 제공하기 위함입니다.

### 1. Document DB (문서형)

**대표**: MongoDB, CouchDB

**데이터 구조**: JSON/BSON 형태의 문서

```json
// 사용자 문서
{
  "_id": "user123",
  "name": "김철수",
  "email": "kim@example.com",
  "age": 30,
  "address": {
    "city": "서울",
    "zipcode": "12345"
  },
  "orders": [
    {"orderId": "o1", "amount": 50000},
    {"orderId": "o2", "amount": 30000}
  ]
}

// 다른 사용자 문서 (구조가 달라도 됨)
{
  "_id": "user456",
  "name": "이영희",
  "email": "lee@example.com",
  "phone": "010-1234-5678",  // 추가 필드
  "skills": ["Java", "Python"]  // 추가 필드
}
```

**특징**:

1. **스키마 유연**: 각 문서가 다른 필드를 가질 수 있음
2. **중첩 구조**: 객체 안에 객체, 배열 포함 가능
3. **조인 불필요**: 관련 데이터를 한 문서에 저장

**장점**:

- 빠른 개발: 스키마 변경 없이 필드 추가 가능
- 읽기 성능: JOIN 없이 한 번에 모든 데이터 조회
- 직관적: JSON 구조로 애플리케이션 객체와 유사

**단점**:

- 중복 데이터: 정규화 불가, 같은 데이터가 여러 문서에 중복
- 트랜잭션 제한: 문서 단위 트랜잭션만 보장 (다중 문서는 제한적)
- 데이터 일관성: 중복된 데이터 동기화 복잡

**언제 사용하나?**

- 콘텐츠 관리 시스템 (CMS): 블로그, 뉴스 기사
- 사용자 프로필: 각 사용자마다 다른 속성
- 카탈로그: 상품 정보 (상품마다 다른 속성)
- 로그 저장: 다양한 형태의 로그

**왜 이 상황에서 사용하나?**

스키마가 자주 변경되거나, 각 문서마다 구조가 달라야 할 때 RDBMS로는 매번 ALTER TABLE이 필요하지만, Document DB는 바로 저장 가능합니다.

**예시: 전자상품 vs 의류**

```json
// 노트북 (전자상품)
{
  "productId": "p1",
  "category": "electronics",
  "name": "맥북 프로",
  "cpu": "M2",
  "ram": "16GB",
  "storage": "512GB"
}

// 티셔츠 (의류)
{
  "productId": "p2",
  "category": "clothing",
  "name": "면 티셔츠",
  "size": "L",
  "color": "blue",
  "material": "cotton"
}
```

RDBMS라면 cpu, ram, size, color 모든 컬럼을 만들고 NULL로 채워야 하지만, Document DB는 필요한 필드만 저장합니다.

### 2. Key-Value DB (키-값)

**대표**: Redis, DynamoDB, Memcached

**데이터 구조**: 키와 값의 쌍

```
Key: "user:1001:session"
Value: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

Key: "product:5001:stock"
Value: "125"

Key: "cache:homepage"
Value: "<html>...</html>"
```

**특징**:

1. **단순함**: Key로만 접근 (빠름)
2. **인메모리**: 주로 RAM에 저장 (매우 빠름)
3. **TTL 지원**: 자동 만료 시간 설정

**장점**:

- 초고속 읽기/쓰기: O(1) 시간 복잡도
- 간단한 API: GET, SET, DELETE
- 확장성: 수평 확장 쉬움

**단점**:

- 쿼리 불가: Key로만 검색 가능 (Value로 검색 불가)
- 복잡한 데이터 구조 부적합
- 메모리 의존: 대용량 데이터 저장 비용 높음

**언제 사용하나?**

- **세션 저장**: 사용자 로그인 세션
- **캐시**: DB 조회 결과 캐싱
- **실시간 카운터**: 조회수, 좋아요 수
- **Rate Limiting**: API 호출 횟수 제한
- **임시 데이터**: OTP, 비밀번호 재설정 토큰

**왜 이 상황에서 사용하나?**

빠른 응답 속도가 필요하고, 단순한 키 기반 접근만 필요할 때. **예를 들어**, 세션은 "세션ID"만 알면 되므로 Key-Value가 최적입니다.

**Redis 고급 기능**:

```redis
# String
SET user:1001:name "김철수"
GET user:1001:name

# List (채팅 메시지, 최근 기록)
LPUSH chat:room1 "안녕하세요"
LRANGE chat:room1 0 9  # 최근 10개

# Set (중복 제거 - 태그, 좋아요한 사람)
SADD post:100:likes user:1001
SISMEMBER post:100:likes user:1001  # 좋아요 여부 확인

# Sorted Set (랭킹)
ZADD leaderboard 9500 user:1001
ZRANGE leaderboard 0 9 WITHSCORES  # 상위 10명

# Hash (사용자 정보)
HSET user:1001 name "김철수" age 30
HGETALL user:1001
```

### 3. Column-Family DB (컬럼형)

**대표**: Cassandra, HBase, Google Bigtable

**데이터 구조**: Row Key + Column Family + Column

```
Row Key: "user1001"
Column Family: "profile"
  - name: "김철수"
  - age: 30
  - email: "kim@example.com"

Column Family: "stats"
  - loginCount: 150
  - lastLogin: "2024-01-01"
```

**RDBMS vs Column-Family 저장 방식**:

**RDBMS (행 기반)**:
```
[Row 1: id=1, name=김철수, age=30]
[Row 2: id=2, name=이영희, age=25]
```

**Column-Family (열 기반)**:
```
[id: 1, 2, 3, ...]
[name: 김철수, 이영희, 박민수, ...]
[age: 30, 25, 28, ...]
```

**왜 컬럼 기반인가?**

특정 컬럼만 조회할 때 해당 컬럼만 읽으면 되므로 I/O 최소화. **예를 들어**, "모든 사용자의 나이"를 조회하면 age 컬럼만 읽습니다.

**특징**:

1. **컬럼 압축**: 같은 타입의 데이터가 연속 저장 → 압축률 높음
2. **선택적 읽기**: 필요한 컬럼만 읽기
3. **수평 확장**: 데이터를 여러 노드에 분산

**장점**:

- 분석 쿼리 빠름: 특정 컬럼만 집계할 때 효율적
- 대용량 처리: 수억-수조 건 데이터
- 쓰기 성능: 로그 형태로 추가만 함 (빠름)

**단점**:

- 읽기 최적화: 행 전체 조회는 느림
- 쿼리 제한: SQL처럼 복잡한 쿼리 어려움
- 운영 복잡: 분산 시스템 관리 어려움

**언제 사용하나?**

- **시계열 데이터**: IoT 센서 데이터, 로그
- **분석 워크로드**: 특정 컬럼 집계 (매출, 조회수)
- **대용량 데이터**: 수억-수조 건
- **쓰기 집중**: 로그 수집, 이벤트 추적

**왜 이 상황에서 사용하나?**

"모든 센서의 온도 평균"처럼 특정 컬럼만 조회/집계하는 경우, 행 기반 DB는 모든 행을 읽어야 하지만 컬럼 기반 DB는 온도 컬럼만 읽으므로 10배 이상 빠릅니다.

**Cassandra 예시**:

```sql
-- Column Family 정의
CREATE TABLE user_activity (
    user_id UUID,
    timestamp TIMESTAMP,
    page_view TEXT,
    PRIMARY KEY (user_id, timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);

-- 특정 사용자의 최근 활동 조회 (빠름)
SELECT * FROM user_activity WHERE user_id = ? LIMIT 10;
```

### 4. Graph DB (그래프형)

**대표**: Neo4j, ArangoDB, JanusGraph

**데이터 구조**: 노드(Node) + 간선(Edge) + 속성(Property)

```
노드: (User {name: "김철수", age: 30})
노드: (User {name: "이영희", age: 25})
간선: (김철수)-[:FRIEND]->(이영희)
간선: (김철수)-[:LIKES]->(Product {name: "노트북"})
```

**RDBMS vs Graph DB**:

**RDBMS**: 친구 관계 조회

```sql
-- 김철수의 친구
SELECT u2.* FROM users u1
JOIN friendships f ON u1.id = f.user1_id
JOIN users u2 ON f.user2_id = u2.id
WHERE u1.name = '김철수';

-- 김철수의 친구의 친구 (2단계)
SELECT DISTINCT u3.* FROM users u1
JOIN friendships f1 ON u1.id = f1.user1_id
JOIN users u2 ON f1.user2_id = u2.id
JOIN friendships f2 ON u2.id = f2.user1_id
JOIN users u3 ON f2.user2_id = u3.id
WHERE u1.name = '김철수' AND u3.id != u1.id;

-- 3단계, 4단계... 계속 JOIN 추가 (복잡하고 느림)
```

**Graph DB**: Cypher 쿼리 (Neo4j)

```cypher
// 김철수의 친구
MATCH (u:User {name: "김철수"})-[:FRIEND]->(friend)
RETURN friend;

// 김철수의 친구의 친구 (2단계)
MATCH (u:User {name: "김철수"})-[:FRIEND*2]->(friend_of_friend)
RETURN friend_of_friend;

// 김철수와 이영희 사이의 최단 경로
MATCH path = shortestPath(
  (u1:User {name: "김철수"})-[:FRIEND*]-(u2:User {name: "이영희"})
)
RETURN path;
```

**왜 Graph DB가 빠른가?**

관계를 따라가는 것이 O(1)입니다. 노드에 연결된 간선의 포인터를 저장하므로, JOIN 없이 바로 이동합니다.

**특징**:

1. **관계 중심**: 노드보다 관계가 중요
2. **패턴 매칭**: 복잡한 관계 패턴 검색
3. **그래프 알고리즘**: 최단 경로, 중심성, 커뮤니티 탐지

**장점**:

- 관계 쿼리 빠름: JOIN 없이 관계 탐색
- 직관적: 그래프로 시각화 가능
- 유연성: 새로운 관계 타입 추가 쉬움

**단점**:

- 집계 쿼리 느림: 전체 통계, SUM, AVG 등
- 샤딩 어려움: 관계가 분산되면 성능 저하
- 러닝 커브: Cypher 같은 쿼리 언어 학습 필요

**언제 사용하나?**

- **소셜 네트워크**: 친구 관계, 팔로우
- **추천 시스템**: "이 상품을 본 사람들이 본 다른 상품"
- **지식 그래프**: 위키백과, 온톨로지
- **사기 탐지**: 복잡한 관계 패턴 분석
- **네트워크 분석**: 도로망, 통신망

**왜 이 상황에서 사용하나?**

"친구의 친구의 친구"처럼 다단계 관계 탐색이 필요할 때, RDBMS는 JOIN이 기하급수적으로 증가하지만 Graph DB는 선형적으로 증가합니다.

**추천 시스템 예시**:

```cypher
// "노트북"을 본 사람들이 본 다른 상품 추천
MATCH (u:User)-[:VIEWED]->(p1:Product {name: "노트북"}),
      (u)-[:VIEWED]->(p2:Product)
WHERE p2.name != "노트북"
RETURN p2.name, COUNT(u) AS viewCount
ORDER BY viewCount DESC
LIMIT 10;
```

### CAP 정리 (CAP Theorem)

**정의**: 분산 시스템에서 다음 3가지 중 2개만 보장 가능

1. **Consistency (일관성)**: 모든 노드가 같은 데이터를 봄
2. **Availability (가용성)**: 모든 요청이 응답을 받음 (성공 또는 실패)
3. **Partition Tolerance (분할 내성)**: 네트워크 분할 발생해도 시스템 작동

**왜 3개 모두 불가능한가?**

네트워크 분할(노드 간 통신 실패)이 발생하면, **일관성과 가용성 중 선택**해야 합니다.

**시나리오**: 2개 노드 (A, B)에 데이터 복제

```
초기 상태: A = 10, B = 10

사용자 1 → A에 쓰기: A = 20
네트워크 분할 발생! (A와 B가 통신 불가)
사용자 2 → B에 읽기 요청
```

**선택 1: 일관성 우선 (CP)**

- B는 A의 최신 값을 확인할 수 없으므로 **에러 반환** (가용성 포기)
- 일관성 보장: 항상 최신 데이터만 반환
- 예: HBase, MongoDB (기본 설정)

**선택 2: 가용성 우선 (AP)**

- B는 자신의 값 10을 반환 (오래된 값)
- 가용성 보장: 항상 응답
- 일관성 포기: 사용자 2는 오래된 값(10)을 봄
- 예: Cassandra, DynamoDB

**왜 P(분할 내성)는 필수인가?**

네트워크 분할은 불가피합니다 (케이블 끊김, 라우터 장애 등). **따라서** 실제로는 **CP vs AP 선택**입니다.

**CAP 분류**:

| 유형 | 특징 | 예시 | 사용처 |
|-----|------|------|--------|
| **CP** | 일관성 + 분할 내성 | HBase, MongoDB, Redis Cluster | 금융, 재고 |
| **AP** | 가용성 + 분할 내성 | Cassandra, DynamoDB, CouchDB | SNS, 로그 |
| **CA** | 일관성 + 가용성 (분산 불가) | RDBMS (단일 노드) | 전통적 DB |

**예시: 전자상거래 장바구니**

**CP 선택 (일관성 우선)**:
- 장점: 재고가 정확함 (품절 상품 구매 불가)
- 단점: 네트워크 장애 시 장바구니 사용 불가

**AP 선택 (가용성 우선)**:
- 장점: 항상 장바구니 사용 가능
- 단점: 품절 상품이 장바구니에 담길 수 있음 (나중에 정리)

**실무 전략**: 최종 일관성 (Eventual Consistency)

네트워크 정상화 후 데이터를 동기화. AP 시스템에서 일시적 불일치를 허용하되, 결국에는 일관성 달성.

### BASE vs ACID

**ACID (RDBMS)**:
- Atomicity, Consistency, Isolation, Durability
- 강한 일관성, 트랜잭션 보장

**BASE (NoSQL)**:
- **BA**sically **A**vailable: 기본적으로 가용
- **S**oft state: 일시적 불일치 허용
- **E**ventual consistency: 최종적 일관성

**왜 BASE를 사용하는가?**

대규모 분산 시스템에서 ACID를 완벽히 보장하면 성능이 크게 저하됩니다. **따라서** 일시적 불일치를 허용하고 높은 가용성과 성능을 얻습니다.

## 트레이드오프

### RDBMS vs NoSQL

| 항목 | RDBMS | NoSQL |
|------|-------|-------|
| 스키마 | 고정 (엄격함) | 유연 (자유로움) |
| 확장 | 수직 확장 (한계 있음) | 수평 확장 (거의 무제한) |
| 트랜잭션 | ACID 완벽 지원 | 제한적 (문서 단위 등) |
| 조인 | 복잡한 조인 가능 | 조인 불가 또는 느림 |
| 일관성 | 강한 일관성 | 최종 일관성 |
| 쿼리 | SQL (표준, 강력함) | DB마다 다름 |
| 사용처 | 금융, ERP, 전통적 앱 | SNS, 빅데이터, 실시간 |

### NoSQL 유형별 비교

| 유형 | 장점 | 단점 | 사용처 |
|-----|------|------|--------|
| **Document** | 스키마 유연, 빠른 개발 | 중복 데이터, 조인 불가 | CMS, 사용자 프로필 |
| **Key-Value** | 초고속, 단순함 | 쿼리 불가, 단순한 데이터만 | 캐시, 세션 |
| **Column-Family** | 대용량 분석 빠름 | 복잡한 쿼리 어려움 | 로그, 시계열 |
| **Graph** | 관계 탐색 빠름 | 집계 느림, 샤딩 어려움 | SNS, 추천 |

## 면접 예상 질문

- Q: NoSQL을 언제 사용하나요?
  - A: (1) 스키마가 자주 변경되거나 문서마다 구조가 다를 때 (2) 대용량 데이터를 수평 확장으로 처리해야 할 때 (3) 빠른 읽기/쓰기가 필요하고 일시적 불일치를 허용할 수 있을 때 (4) 관계 탐색이 중심인 경우. **왜냐하면** RDBMS는 고정 스키마와 수직 확장의 한계가 있고, 복잡한 JOIN은 성능 저하를 일으키기 때문입니다. **예시**: MongoDB는 상품 카탈로그(각 상품마다 다른 속성), Redis는 세션 캐시(초고속), Cassandra는 IoT 센서 데이터(수십억 건), Neo4j는 SNS 친구 추천(관계 탐색)에 적합합니다.

- Q: CAP 정리가 무엇인가요?
  - A: 분산 시스템에서 일관성(Consistency), 가용성(Availability), 분할 내성(Partition Tolerance) 중 2개만 보장할 수 있다는 정리입니다. **왜 3개 모두 불가능한가?** 네트워크 분할 발생 시 노드 간 통신이 불가능하므로, 일관성(최신 데이터만 반환)과 가용성(항상 응답) 중 선택해야 하기 때문입니다. **CP 선택**: 일관성 우선, 네트워크 장애 시 에러 반환 (HBase, MongoDB). **AP 선택**: 가용성 우선, 오래된 데이터라도 반환 (Cassandra, DynamoDB). **실무**: P는 필수이므로 실제로는 C와 A 중 선택. 금융은 CP, SNS는 AP.

- Q: MongoDB와 같은 Document DB를 언제 사용하나요?
  - A: 스키마가 유연해야 하고, 각 문서마다 다른 구조를 가질 수 있을 때 사용합니다. **왜냐하면** RDBMS는 모든 행이 같은 컬럼을 가져야 하므로, 전자상품(CPU, RAM)과 의류(사이즈, 색상)를 같은 테이블에 저장하면 많은 NULL이 발생하지만, Document DB는 필요한 필드만 저장하기 때문입니다. **장점**: (1) 빠른 개발 - ALTER TABLE 없이 필드 추가 (2) 읽기 성능 - JOIN 없이 한 문서에서 모든 정보 조회. **단점**: 데이터 중복, 다중 문서 트랜잭션 제한. **사용처**: CMS, 사용자 프로필, 카탈로그.

- Q: Redis 같은 Key-Value DB는 왜 빠른가요?
  - A: (1) **인메모리**: RAM에 데이터를 저장하므로 디스크 I/O 없음 (2) **단순한 구조**: Key-Value만 저장하므로 복잡한 인덱싱 불필요 (3) **O(1) 접근**: 해시 테이블로 구현되어 즉시 접근. **왜 디스크 DB보다 빠른가?** RAM 접근은 나노초(ns), SSD는 마이크로초(μs), HDD는 밀리초(ms)로 1000배 이상 차이. **사용처**: 세션 저장(로그인 정보), 캐시(DB 조회 결과), 실시간 카운터(조회수, 좋아요), Rate Limiting. **주의**: 휘발성이므로 중요 데이터는 디스크 백업(RDB, AOF) 필요.

- Q: Graph DB는 RDBMS와 무엇이 다른가요?
  - A: Graph DB는 관계를 일급 객체(First-class)로 저장하여 관계 탐색이 매우 빠릅니다. **RDBMS**: 관계를 외래키로 표현하고, JOIN으로 탐색. "친구의 친구의 친구"는 3번 JOIN 필요 (느림). **Graph DB**: 노드에 연결된 간선의 포인터를 저장하여 O(1)로 이동. **예시**: Neo4j에서 `MATCH (u)-[:FRIEND*3]->(f)` 한 줄로 3단계 친구 탐색. **왜 빠른가?** JOIN은 테이블 전체를 스캔하지만, Graph DB는 포인터를 따라가므로 관계 개수에만 비례. **사용처**: SNS(친구 추천), 추천 시스템(협업 필터링), 사기 탐지(복잡한 관계 패턴).

- Q: NoSQL도 트랜잭션을 지원하나요?
  - A: 제한적으로 지원합니다. **Document DB**: 단일 문서 트랜잭션은 보장하지만, 다중 문서 트랜잭션은 제한적 (MongoDB 4.0+에서 추가). **Key-Value**: Redis는 MULTI/EXEC로 단순 트랜잭션 지원. **Column-Family**: Cassandra는 경량 트랜잭션(LWT) 지원하지만 느림. **왜 제한적인가?** NoSQL은 수평 확장을 위해 데이터를 여러 노드에 분산 저장하므로, 여러 노드에 걸친 트랜잭션은 2PC(Two-Phase Commit)가 필요하여 성능 저하가 큽니다. **대안**: (1) 단일 문서/행에 모든 정보 포함 (2) 애플리케이션 레벨에서 보상 트랜잭션 (3) 최종 일관성 허용.

## 연관 문서

- [SQL](./sql.md): RDBMS와 NoSQL의 비교
- [Transaction](./transaction.md): NoSQL의 트랜잭션과 CAP 정리
- [Normalization](./normalization.md): NoSQL의 반정규화 전략
- [Redis Caching](./redis-caching.md): Redis를 활용한 캐싱 전략

## 참고 자료

- [MongoDB 공식 문서](https://www.mongodb.com/docs/)
- [Redis 공식 문서](https://redis.io/docs/)
- [Cassandra 공식 문서](https://cassandra.apache.org/doc/)
- [Neo4j 공식 문서](https://neo4j.com/docs/)
- [CAP Theorem - Martin Kleppmann](https://martin.kleppmann.com/2015/05/11/please-stop-calling-databases-cp-or-ap.html)
- [Designing Data-Intensive Applications - Martin Kleppmann](https://dataintensive.net/)
