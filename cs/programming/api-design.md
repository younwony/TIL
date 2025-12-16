# API 설계

> 클라이언트와 서버 간 데이터 통신을 위한 인터페이스를 정의하는 원칙과 방법

## 핵심 개념

- **API (Application Programming Interface)**: 소프트웨어 간 상호작용을 위한 계약서
- **REST (Representational State Transfer)**: HTTP 기반의 자원 중심 아키텍처 스타일
- **일관성**: 예측 가능한 URL 패턴, 응답 형식, 에러 처리
- **버전 관리**: 하위 호환성을 유지하면서 API를 발전시키는 전략
- **문서화**: 개발자가 API를 쉽게 이해하고 사용할 수 있도록 설명

## 쉽게 이해하기

**API**를 식당 메뉴판에 비유할 수 있습니다.

손님(클라이언트)은 메뉴판(API 문서)을 보고 주문(요청)합니다.
메뉴판에는 음식 이름(엔드포인트), 가격(파라미터), 설명(응답 형식)이 적혀 있습니다.
손님은 주방(서버) 내부 구조를 몰라도 메뉴판만 보면 주문할 수 있습니다.

예를 들어:
- `GET /menu` → 메뉴 목록 보기
- `POST /orders` → 주문하기
- `GET /orders/123` → 내 주문 상태 확인
- `DELETE /orders/123` → 주문 취소

**왜 API 설계가 중요한가요?**
- 메뉴판이 복잡하면 손님이 주문하기 어려움 (개발자 경험 저하)
- 메뉴 이름이 매번 바뀌면 단골도 헷갈림 (하위 호환성 깨짐)
- 잘못된 주문에 대한 안내가 없으면 손님이 당황함 (에러 처리 부재)

---

## 상세 설명

### REST API 설계 원칙

REST는 HTTP를 활용한 아키텍처 스타일로, 6가지 제약 조건을 따릅니다.

| 원칙 | 설명 | 왜 중요한가? |
|------|------|-------------|
| Client-Server | 클라이언트와 서버 분리 | 독립적 발전 가능 |
| Stateless (무상태) | 서버가 클라이언트 상태를 저장하지 않음 | 수평 확장 용이 |
| Cacheable | 응답을 캐시 가능하게 | 성능 향상 |
| Uniform Interface | 일관된 인터페이스 | 단순성, 가시성 |
| Layered System | 계층화된 시스템 | 보안, 확장성 |
| Code on Demand | 선택적으로 코드 전송 | 확장성 (선택) |

**왜 Stateless인가?**

서버가 클라이언트 상태를 저장하면:
- 특정 서버에 종속됨 → 로드밸런싱 어려움
- 서버 재시작 시 상태 유실
- 서버 메모리 부담 증가

**따라서** 모든 요청은 필요한 정보를 모두 포함해야 합니다 (토큰, 세션 ID 등).

---

### HTTP 메서드

| 메서드 | 용도 | 멱등성 (동일 요청 반복 시 결과 동일) | 안전성 (리소스 변경 없음) | 예시 |
|--------|------|--------|--------|------|
| GET | 리소스 조회 | O | O | `GET /users/1` |
| POST | 리소스 생성 | X | X | `POST /users` |
| PUT | 리소스 전체 수정 | O | X | `PUT /users/1` |
| PATCH | 리소스 부분 수정 | X | X | `PATCH /users/1` |
| DELETE | 리소스 삭제 | O | X | `DELETE /users/1` |

**멱등성(Idempotency)이란?**

같은 요청을 여러 번 보내도 결과가 동일한 속성입니다.

```
# 멱등 (O)
DELETE /users/1  → 첫 번째: 삭제 성공
DELETE /users/1  → 두 번째: 이미 삭제됨 (결과 동일)

# 비멱등 (X)
POST /users      → 첫 번째: user 1 생성
POST /users      → 두 번째: user 2 생성 (결과 다름)
```

**왜 멱등성이 중요한가?**
- 네트워크 오류로 재시도 시 중복 생성 방지
- 클라이언트가 안전하게 재요청 가능

---

### URI 설계

#### 기본 규칙

**권장 (O)**:
```
GET  /users                 # 사용자 목록
GET  /users/123             # 특정 사용자
GET  /users/123/orders      # 특정 사용자의 주문 목록
POST /users/123/orders      # 특정 사용자의 주문 생성
```

**비권장 (X)**:
```
GET  /getUsers              # 동사 사용
GET  /user/123              # 단수형 (컬렉션은 복수형)
GET  /users/123/getOrders   # 동사 중복
POST /users/createUser      # 동사 + HTTP 메서드 중복
```

**왜?**
- URI는 리소스(명사)를 나타내고, 행위는 HTTP 메서드가 표현
- 일관된 패턴으로 예측 가능한 API 제공
- `/users/123`만 봐도 "123번 사용자"임을 알 수 있음

#### 계층 관계 표현

```
# 사용자 → 주문 → 상품
GET /users/123/orders/456/items

# 최대 3depth 권장
# 너무 깊으면 쿼리 파라미터 활용
GET /items?user_id=123&order_id=456
```

**만약 지키지 않으면?**
- 깊은 중첩: URL이 너무 길어지고 복잡해짐
- 결합도 증가: 리소스 간 관계가 URL에 고정됨

---

### HTTP 상태 코드

#### 성공 응답 (2xx)

| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 200 OK | 성공 | GET, PUT, PATCH 성공 |
| 201 Created | 생성됨 | POST로 리소스 생성 |
| 204 No Content | 내용 없음 | DELETE 성공, 응답 본문 없음 |

#### 클라이언트 에러 (4xx)

| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 400 Bad Request | 잘못된 요청 | 유효성 검사 실패 |
| 401 Unauthorized | 인증 필요 | 로그인 필요 |
| 403 Forbidden | 권한 없음 | 인증됐지만 권한 부족 |
| 404 Not Found | 리소스 없음 | 존재하지 않는 리소스 |
| 409 Conflict | 충돌 | 중복 데이터, 상태 충돌 |
| 422 Unprocessable Entity | 처리 불가 | 문법은 맞지만 의미상 오류 |
| 429 Too Many Requests | 요청 과다 | Rate Limit 초과 |

#### 서버 에러 (5xx)

| 코드 | 의미 | 사용 시점 |
|------|------|----------|
| 500 Internal Server Error | 서버 오류 | 예상치 못한 서버 오류 |
| 502 Bad Gateway | 게이트웨이 오류 | 업스트림 서버 (게이트웨이 뒤의 실제 서버) 오류 |
| 503 Service Unavailable | 서비스 불가 | 서버 과부하, 점검 중 |

**401 vs 403**

```
401 Unauthorized: "너 누구야?" (인증 실패)
403 Forbidden: "너인 건 알겠는데, 권한 없어" (인가 실패)
```

---

### 에러 응답 설계

#### 일관된 에러 응답 형식

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "입력값이 유효하지 않습니다.",
    "details": [
      {
        "field": "email",
        "reason": "올바른 이메일 형식이 아닙니다."
      },
      {
        "field": "age",
        "reason": "나이는 0보다 커야 합니다."
      }
    ],
    "timestamp": "2024-01-15T10:30:00Z",
    "path": "/api/v1/users"
  }
}
```

**왜 이 형식인가?**
- `code`: 프로그래밍 방식으로 에러 분류 가능
- `message`: 사용자에게 표시할 메시지
- `details`: 필드별 상세 오류 (폼 유효성 검사에 유용)
- `timestamp`, `path`: 디버깅에 필요한 컨텍스트

**만약 지키지 않으면?**
- 클라이언트가 에러 종류를 파악하기 어려움
- 사용자에게 적절한 피드백 제공 불가
- 디버깅 시 원인 추적 어려움

---

### 버전 관리

#### 버전 관리 전략

| 방식 | 예시 | 장점 | 단점 |
|------|------|------|------|
| URI Path | `/api/v1/users` | 명확함, 캐싱 용이 | URI 오염 |
| Query Parameter | `/api/users?version=1` | 간단함 | 선택적이라 혼란 |
| Header | `Accept: application/vnd.api.v1+json` | URI 깔끔 | 테스트 어려움 |

**권장: URI Path 방식**

```
/api/v1/users  # 버전 1
/api/v2/users  # 버전 2
```

**왜 URI Path인가?**
- 브라우저에서 직접 테스트 가능
- 캐시 키가 URL이므로 버전별 캐싱 용이
- 가장 널리 사용되어 예측 가능

#### 버전 업그레이드 정책

```
1. Breaking Change (기존 클라이언트 코드가 동작하지 않게 만드는 변경) 발생 시에만 메이저 버전 업
2. 구버전은 최소 6개월~1년 유지
3. Deprecation (지원 중단 예고) 헤더로 사전 공지
   Deprecation: true
   Sunset: Sat, 31 Dec 2024 23:59:59 GMT
```

---

### 페이지네이션

#### Offset 기반

```http
GET /users?page=2&size=20

# 응답
{
  "data": [...],
  "pagination": {
    "page": 2,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8
  }
}
```

**장점**: 특정 페이지 바로 이동 가능
**단점**: 데이터 변경 시 중복/누락 발생, 대용량에서 느림

**왜 대용량에서 느린가?**
```sql
-- page 1000, size 20
SELECT * FROM users LIMIT 20 OFFSET 19980;
-- 19,980개를 읽고 버린 후 20개 반환 → 비효율
```

#### Cursor 기반 (권장)

> Cursor: 현재 위치를 가리키는 포인터 (주로 마지막 조회 항목의 ID를 Base64 인코딩)

```http
GET /users?cursor=eyJpZCI6MTAwfQ&size=20

# 응답
{
  "data": [...],
  "pagination": {
    "nextCursor": "eyJpZCI6MTIwfQ",
    "hasNext": true
  }
}
```

**장점**: 데이터 변경에 안전, 대용량에서도 일정한 성능
**단점**: 특정 페이지 바로 이동 불가

**왜 Cursor가 더 효율적인가?**
```sql
-- cursor = id:100, size 20
SELECT * FROM users WHERE id > 100 LIMIT 20;
-- 인덱스 사용 가능, 바로 100 이후부터 탐색
```

---

### 필터링, 정렬, 검색

```http
# 필터링
GET /users?status=active&role=admin

# 정렬
GET /users?sort=created_at:desc,name:asc

# 검색
GET /users?q=john

# 필드 선택 - Sparse Fieldsets (응답에서 원하는 필드만 선택)
GET /users?fields=id,name,email

# 복합 예시
GET /users?status=active&sort=created_at:desc&page=1&size=20
```

**왜 필드 선택이 필요한가?**
- 불필요한 데이터 전송 감소 → 네트워크 비용 절감
- 모바일 환경에서 특히 효과적
- GraphQL의 장점을 REST에서 부분 구현

---

### 요청/응답 설계

#### 요청 본문

```json
// POST /users
{
  "name": "홍길동",
  "email": "hong@example.com",
  "age": 25
}
```

**규칙**:
- JSON 형식 사용 (Content-Type: application/json)
- camelCase 또는 snake_case 일관되게 사용
- 필수/선택 필드 명확히 구분

#### 응답 본문

```json
// 단일 리소스
{
  "data": {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}

// 컬렉션
{
  "data": [
    { "id": 1, "name": "홍길동" },
    { "id": 2, "name": "김철수" }
  ],
  "pagination": {
    "page": 1,
    "size": 20,
    "totalElements": 100
  }
}
```

**왜 `data`로 감싸는가?**
- 메타데이터(pagination, links 등) 추가 공간 확보
- 확장성: 나중에 `meta`, `links` 필드 추가 용이
- 일관된 응답 구조로 클라이언트 파싱 단순화

---

### Rate Limiting (단위 시간당 API 요청 수 제한)

```http
HTTP/1.1 200 OK
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1705312800

# 초과 시
HTTP/1.1 429 Too Many Requests
Retry-After: 60
```

| 알고리즘 | 설명 | 특징 |
|----------|------|------|
| Fixed Window | 고정 시간 단위 카운트 | 구현 간단, 경계 시점 버스트 (순간 폭주) 문제 |
| Sliding Window | 이동 윈도우 카운트 | 더 정교함, 메모리 사용 증가 |
| Token Bucket | 토큰 리필 방식 | 버스트 (순간 폭주) 허용, 평균 속도 제한 |
| Leaky Bucket | 일정 속도 처리 | 균일한 처리, 버스트 불가 |

**왜 Rate Limiting이 필요한가?**
- DDoS (분산 서비스 거부) 공격 방어
- 서버 리소스 보호
- 공정한 API 사용 보장
- 비용 제어 (클라우드 환경)

---

### HATEOAS (선택적)

> HATEOAS: Hypermedia as the Engine of Application State
> 응답에 다음 가능한 액션의 링크를 포함하여 클라이언트가 URL을 하드코딩하지 않아도 되게 하는 방식

```json
{
  "data": {
    "id": 123,
    "name": "홍길동",
    "status": "active"
  },
  "links": {
    "self": "/users/123",
    "orders": "/users/123/orders",
    "deactivate": "/users/123/deactivate"
  }
}
```

**왜 선택적인가?**
- 구현 복잡도 증가
- 대부분의 API에서는 과도한 설계
- 클라이언트가 이미 API 구조를 알고 있는 경우 불필요

---

## 예제 코드

### Spring Boot REST Controller

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<UserResponse> users = userService.findAll(page, size);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long id) {

        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse user = userService.create(request);
        URI location = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(location)
                .body(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse user = userService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 공통 응답 래퍼

```java
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private ApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}
```

### 전역 예외 처리

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            EntityNotFoundException e) {

        ErrorResponse error = ErrorResponse.of(
            "NOT_FOUND",
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new FieldError(f.getField(), f.getDefaultMessage()))
                .toList();

        ErrorResponse error = ErrorResponse.of(
            "VALIDATION_ERROR",
            "입력값이 유효하지 않습니다.",
            fieldErrors
        );
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(error));
    }
}
```

---

## 트레이드오프

### REST vs GraphQL

| 항목 | REST | GraphQL |
|------|------|---------|
| 데이터 가져오기 | 여러 엔드포인트 호출 | 한 번에 필요한 것만 |
| Over-fetching (필요 이상의 데이터 수신) | 발생 가능 | 없음 |
| Under-fetching (필요한 데이터를 위해 여러 번 요청) | 발생 가능 | 없음 |
| 캐싱 | HTTP 캐싱 용이 | 복잡함 |
| 학습 곡선 | 낮음 | 높음 |
| 파일 업로드 | 간단함 | 복잡함 |

**언제 REST?**
- 단순한 CRUD (Create, Read, Update, Delete) 작업
- HTTP 캐싱이 중요한 경우
- 팀이 REST에 익숙한 경우

**언제 GraphQL?**
- 복잡한 연관 데이터 조회
- 모바일 앱 (네트워크 최적화)
- 다양한 클라이언트 요구사항

---

## API 설계 체크리스트

```
□ URI는 명사(리소스)로 표현했는가?
□ HTTP 메서드를 올바르게 사용했는가?
□ 적절한 HTTP 상태 코드를 반환하는가?
□ 에러 응답이 일관된 형식인가?
□ 버전 관리 전략이 있는가?
□ 페이지네이션을 지원하는가?
□ Rate Limiting이 적용되어 있는가?
□ API 문서가 최신 상태인가?
□ 인증/인가가 적절히 구현되어 있는가?
□ HTTPS를 사용하는가?
```

---

## 면접 예상 질문

- **Q: REST API에서 PUT과 PATCH의 차이는 무엇인가요?**
  - A: PUT은 리소스 전체를 교체하고, PATCH는 부분 수정입니다. **왜냐하면** PUT은 멱등성을 보장하기 위해 같은 요청 시 항상 같은 전체 상태가 되어야 하기 때문입니다. 예를 들어 사용자의 이름만 바꾸고 싶을 때, PUT은 모든 필드를 보내야 하고 PATCH는 `{"name": "새이름"}`만 보내면 됩니다. 실무에서는 부분 수정이 많아 PATCH를 더 자주 사용합니다.

- **Q: 401 Unauthorized와 403 Forbidden의 차이는 무엇인가요?**
  - A: 401은 "인증 실패"이고 403은 "인가 실패"입니다. **왜냐하면** 401은 "너 누구야?"(신원 미확인)이고, 403은 "너인 건 알겠는데 권한이 없어"(신원 확인됨, 권한 부족)이기 때문입니다. 401 응답 시 클라이언트는 로그인을 시도하고, 403 응답 시에는 권한 상승을 요청하거나 접근을 포기해야 합니다.

- **Q: 대용량 데이터에서 Offset 페이지네이션보다 Cursor 페이지네이션이 더 좋은 이유는 무엇인가요?**
  - A: Offset 방식은 `OFFSET 10000`일 때 10,000개를 읽고 버린 후 다음 페이지를 반환합니다. **왜냐하면** DB가 정렬된 결과에서 앞의 N개를 건너뛰려면 실제로 그만큼 읽어야 하기 때문입니다. 반면 Cursor 방식은 `WHERE id > last_id LIMIT 20`으로 인덱스를 타고 바로 해당 위치부터 읽습니다. **따라서** 페이지가 깊어질수록 Offset은 느려지고 Cursor는 일정한 성능을 유지합니다. 다만 Cursor는 특정 페이지로 바로 이동할 수 없다는 단점이 있습니다.

---

## 참고 자료

- [Microsoft REST API Guidelines](https://github.com/microsoft/api-guidelines)
- [Google API Design Guide](https://cloud.google.com/apis/design)
- [JSON:API Specification](https://jsonapi.org/)
- [HTTP 상태 코드 - MDN](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)
