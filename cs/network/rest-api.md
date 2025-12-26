# REST API

> `[3] 중급` · 선수 지식: [HTTP/HTTPS](./http-https.md)

> HTTP를 기반으로 자원을 표현하고 조작하는 아키텍처 스타일

`#REST` `#RESTful` `#RESTAPI` `#API` `#HTTP` `#자원` `#Resource` `#표현` `#Representation` `#상태전이` `#StateTransfer` `#무상태` `#Stateless` `#CRUD` `#GET` `#POST` `#PUT` `#PATCH` `#DELETE` `#URI` `#엔드포인트` `#Endpoint` `#HTTP상태코드` `#StatusCode` `#HATEOAS` `#Richardson성숙도` `#API설계`

## 왜 알아야 하는가?

REST API는 현대 웹 서비스의 표준입니다. 프론트엔드-백엔드 통신, 마이크로서비스 간 통신, 외부 서비스 연동 모두 REST API를 사용합니다. 좋은 REST API를 설계하면 이해하기 쉽고, 유지보수가 용이하며, 확장성이 높은 시스템을 만들 수 있습니다.

## 핵심 개념

- **REST (Representational State Transfer)**: 자원을 URI로 표현하고 HTTP 메서드로 조작하는 아키텍처 스타일
- **자원 (Resource)**: API가 다루는 대상 (사용자, 주문, 상품 등)
- **표현 (Representation)**: 자원의 상태를 JSON, XML 등으로 표현
- **무상태 (Stateless)**: 각 요청이 독립적, 서버가 클라이언트 상태 저장 안 함
- **URI**: 자원을 식별하는 주소

## 쉽게 이해하기

**REST API**를 도서관 사서에 비유할 수 있습니다.

- **자원 (Resource)**: 책
- **URI**: 책의 위치 (서가 번호)
- **HTTP 메서드**: 사서에게 하는 요청
  - GET: "이 책 보여주세요"
  - POST: "새 책 등록해주세요"
  - PUT: "이 책 정보 전체 수정해주세요"
  - DELETE: "이 책 폐기해주세요"
- **상태 코드**: 사서의 응답
  - 200: "여기 있습니다"
  - 404: "그 책 없어요"
  - 500: "시스템 오류입니다"

## 상세 설명

### REST 6가지 제약 조건

| 제약 조건 | 설명 |
|----------|------|
| **Client-Server** | 클라이언트와 서버 역할 분리 |
| **Stateless** | 각 요청이 독립적, 서버가 상태 저장 안 함 |
| **Cacheable** | 응답은 캐시 가능 여부 명시 |
| **Uniform Interface** | 일관된 인터페이스 (URI, HTTP 메서드) |
| **Layered System** | 계층화 가능 (로드밸런서, 프록시 등) |
| **Code on Demand** | (선택) 서버가 클라이언트에 코드 전송 가능 |

### URI 설계 원칙

#### 좋은 URI 설계

```
# 자원은 명사, 복수형 사용
GET /users              # 사용자 목록
GET /users/123          # 특정 사용자
GET /users/123/orders   # 특정 사용자의 주문 목록

# 계층 관계 표현
GET /departments/10/employees

# 쿼리 파라미터는 필터링/정렬/페이징
GET /users?status=active&sort=name&page=1&size=20
```

#### 나쁜 URI 설계

```
# Bad: 동사 사용
GET /getUsers
GET /createUser
POST /deleteUser/123

# Bad: 단수형
GET /user/123

# Bad: 행위를 URI에 포함
POST /users/123/delete
GET /users/123/update
```

### HTTP 메서드와 CRUD

| 메서드 | CRUD | 설명 | 멱등성 | 안전성 |
|--------|------|------|--------|--------|
| GET | Read | 자원 조회 | O | O |
| POST | Create | 자원 생성 | X | X |
| PUT | Update | 자원 전체 수정 | O | X |
| PATCH | Update | 자원 부분 수정 | X | X |
| DELETE | Delete | 자원 삭제 | O | X |

**멱등성**: 같은 요청을 여러 번 해도 결과가 같음
**안전성**: 서버 상태를 변경하지 않음

```
# 멱등성 예시
DELETE /users/123  # 첫 번째: 삭제됨
DELETE /users/123  # 두 번째: 이미 없음 (결과 동일)

POST /users        # 첫 번째: 새 사용자 생성
POST /users        # 두 번째: 또 다른 사용자 생성 (멱등 X)
```

### PUT vs PATCH

```json
// 원본 데이터
{
  "id": 1,
  "name": "John",
  "email": "john@example.com",
  "age": 30
}

// PUT /users/1 - 전체 교체
// 요청에 없는 필드는 null로 됨
{
  "name": "Jane",
  "email": "jane@example.com"
}
// 결과: age가 null이 될 수 있음

// PATCH /users/1 - 부분 수정
{
  "name": "Jane"
}
// 결과: name만 변경, 나머지 유지
```

### HTTP 상태 코드

#### 2xx: 성공

| 코드 | 이름 | 설명 | 사용 |
|------|------|------|------|
| 200 | OK | 요청 성공 | GET, PUT, PATCH |
| 201 | Created | 자원 생성됨 | POST |
| 204 | No Content | 성공, 응답 본문 없음 | DELETE |

#### 4xx: 클라이언트 오류

| 코드 | 이름 | 설명 |
|------|------|------|
| 400 | Bad Request | 잘못된 요청 (유효성 실패) |
| 401 | Unauthorized | 인증 필요 |
| 403 | Forbidden | 권한 없음 |
| 404 | Not Found | 자원 없음 |
| 409 | Conflict | 충돌 (중복 등) |
| 422 | Unprocessable Entity | 문법은 맞지만 처리 불가 |

#### 5xx: 서버 오류

| 코드 | 이름 | 설명 |
|------|------|------|
| 500 | Internal Server Error | 서버 내부 오류 |
| 502 | Bad Gateway | 게이트웨이 오류 |
| 503 | Service Unavailable | 서비스 일시 중단 |

### 응답 형식

```json
// 성공 응답
{
  "data": {
    "id": 123,
    "name": "John",
    "email": "john@example.com"
  }
}

// 목록 응답 (페이징 포함)
{
  "data": [
    {"id": 1, "name": "John"},
    {"id": 2, "name": "Jane"}
  ],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}

// 오류 응답
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "이메일 형식이 올바르지 않습니다.",
    "details": [
      {"field": "email", "message": "유효한 이메일을 입력하세요."}
    ]
  }
}
```

### Richardson 성숙도 모델

REST API의 성숙도를 4단계로 측정:

```
Level 3: HATEOAS (Hypermedia)
         - 링크를 통한 상태 전이

Level 2: HTTP 메서드 활용
         - GET, POST, PUT, DELETE 구분
         - 적절한 상태 코드

Level 1: 자원 (Resource)
         - URI로 자원 구분

Level 0: HTTP 사용
         - 단순히 HTTP 프로토콜만 사용
```

**Level 3 예시 (HATEOAS)**:
```json
{
  "id": 123,
  "name": "John",
  "_links": {
    "self": {"href": "/users/123"},
    "orders": {"href": "/users/123/orders"},
    "delete": {"href": "/users/123", "method": "DELETE"}
  }
}
```

### API 버저닝

```
# 1. URI 버저닝 (권장)
GET /v1/users
GET /v2/users

# 2. 쿼리 파라미터
GET /users?version=1

# 3. 헤더 (Accept)
GET /users
Accept: application/vnd.myapi.v1+json
```

### 실제 API 설계 예시

```
# 사용자 API
GET    /users                 # 목록 조회
GET    /users/{id}            # 단일 조회
POST   /users                 # 생성
PUT    /users/{id}            # 전체 수정
PATCH  /users/{id}            # 부분 수정
DELETE /users/{id}            # 삭제

# 주문 API (사용자 하위 자원)
GET    /users/{userId}/orders         # 특정 사용자의 주문 목록
GET    /users/{userId}/orders/{id}    # 특정 주문 조회
POST   /users/{userId}/orders         # 주문 생성

# 검색 API
GET    /users/search?name=john&status=active

# 행위 API (동사 허용되는 예외)
POST   /users/{id}/activate           # 활성화
POST   /orders/{id}/cancel            # 취소
```

## 트레이드오프

| 항목 | REST | GraphQL | gRPC |
|------|------|---------|------|
| 학습 곡선 | 낮음 | 중간 | 높음 |
| 유연성 | 중간 | 높음 | 낮음 |
| 성능 | 중간 | 중간 | 높음 |
| 캐싱 | 쉬움 | 어려움 | 어려움 |
| 적합 | 공개 API | 복잡한 쿼리 | 마이크로서비스 |

## 면접 예상 질문

### Q: REST란 무엇인가요?

A: REST는 **자원을 URI로 표현하고 HTTP 메서드로 조작하는 아키텍처 스타일**입니다. 6가지 제약 조건(Client-Server, Stateless, Cacheable, Uniform Interface, Layered System, Code on Demand)을 따릅니다. **핵심 원칙**: (1) URI는 자원을 명사로 표현 (2) HTTP 메서드로 행위 구분 (GET/POST/PUT/DELETE) (3) 무상태로 각 요청 독립적 처리. **왜 중요한가?** 일관된 인터페이스로 이해하기 쉽고, 캐싱과 확장이 용이합니다.

### Q: PUT과 PATCH의 차이는?

A: **PUT**은 자원 전체를 교체하고, **PATCH**는 일부만 수정합니다. PUT은 요청에 없는 필드가 null이 될 수 있어 모든 필드를 보내야 합니다. PATCH는 변경할 필드만 보내면 됩니다. **멱등성**: PUT은 멱등(같은 요청 반복해도 결과 동일), PATCH는 구현에 따라 다릅니다. **사용 권장**: 실무에서는 부분 수정이 많아 PATCH를 더 자주 사용합니다.

### Q: REST API에서 HTTP 상태 코드를 어떻게 사용하나요?

A: **2xx**는 성공(200 OK, 201 Created, 204 No Content), **4xx**는 클라이언트 오류(400 Bad Request, 401 Unauthorized, 404 Not Found), **5xx**는 서버 오류입니다. **왜 중요한가?** 상태 코드만으로 요청 결과를 파악할 수 있어 클라이언트가 적절히 대응할 수 있습니다. **예시**: 201은 POST 성공 시, 204는 DELETE 성공 시, 409는 중복 데이터 충돌 시 사용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [HTTP/HTTPS](./http-https.md) | 선수 지식 | [3] 중급 |
| [API 설계](../programming/api-design.md) | 설계 원칙 | [3] 중급 |

## 참고 자료

- [REST API Tutorial](https://restfulapi.net/)
- [HTTP API Design Guide](https://github.com/interagent/http-api-design)
- [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)
