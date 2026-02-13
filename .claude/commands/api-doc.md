# API 문서 생성

지정된 API 엔드포인트 또는 서비스에 대한 문서를 아래 템플릿에 맞춰 생성합니다.

## 입력 정보
$ARGUMENTS

---

## 템플릿

### 1. 개요 (Overview)
```markdown
## API 개요

| 항목 | 내용 |
|------|------|
| API 명 | [API 이름] |
| 버전 | v1.0.0 |
| Base URL | `https://api.example.com/v1` |
| 인증 방식 | Bearer Token / API Key / OAuth 2.0 |
| 콘텐츠 타입 | application/json |
```

### 2. 엔드포인트 (Endpoints)
```markdown
## 엔드포인트

### [기능명]

**`[METHOD] /path/to/resource`**

#### 설명
[API 기능에 대한 간단한 설명]

#### 요청 (Request)

**Headers**
| 헤더 | 필수 | 설명 |
|------|------|------|
| Authorization | O | Bearer {token} |
| Content-Type | O | application/json |

**Path Parameters**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| id | Long | O | 리소스 고유 ID |

**Query Parameters**
| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|----------|------|------|--------|------|
| page | Integer | X | 0 | 페이지 번호 |
| size | Integer | X | 20 | 페이지 크기 |

**Request Body**
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| name | String | O | 이름 (최대 100자) |
| email | String | O | 이메일 주소 |

**예시**
```json
{
  "name": "홍길동",
  "email": "hong@example.com"
}
```

#### 응답 (Response)

**성공 (200 OK)**
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 생성된 리소스 ID |
| name | String | 이름 |
| createdAt | DateTime | 생성 일시 (ISO 8601) |

**예시**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "홍길동",
    "createdAt": "2025-01-15T10:30:00Z"
  }
}
```
```

### 3. 에러 코드 (Error Codes)
```markdown
## 에러 코드

### HTTP 상태 코드
| 코드 | 설명 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 (파라미터 오류) |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 409 | 충돌 (중복 데이터) |
| 500 | 서버 내부 오류 |

### 비즈니스 에러 코드
| 코드 | 메시지 | 설명 |
|------|--------|------|
| USER_001 | 사용자를 찾을 수 없습니다 | 존재하지 않는 사용자 ID |
| USER_002 | 이메일이 이미 존재합니다 | 중복 이메일 |

### 에러 응답 형식
```json
{
  "success": false,
  "error": {
    "code": "USER_001",
    "message": "사용자를 찾을 수 없습니다",
    "timestamp": "2025-01-15T10:30:00Z"
  }
}
```
```

### 4. 인증 (Authentication)
```markdown
## 인증

### Bearer Token
```bash
curl -X GET "https://api.example.com/v1/users" \
  -H "Authorization: Bearer {access_token}"
```

### 토큰 발급
**`POST /auth/token`**

```json
{
  "grant_type": "client_credentials",
  "client_id": "your_client_id",
  "client_secret": "your_client_secret"
}
```
```

---

## 작성 규칙

1. **일관성**: 모든 엔드포인트는 동일한 형식으로 문서화
2. **예시 포함**: 요청/응답 예시를 반드시 포함
3. **에러 처리**: 가능한 모든 에러 케이스 문서화
4. **버전 관리**: API 버전 변경 시 변경 이력 기록
5. **한글 우선**: 설명은 한글로 작성, 기술 용어는 영문 허용

---

위 템플릿을 기반으로 요청된 API에 대한 문서를 생성해주세요.
