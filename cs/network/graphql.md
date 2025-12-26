# GraphQL

> `[3] 중급` · 선수 지식: [HTTP/HTTPS](./http-https.md), [REST API](./rest-api.md)

> 클라이언트가 필요한 데이터를 정확히 요청할 수 있는 쿼리 언어 및 런타임

`#GraphQL` `#API` `#Query` `#Mutation` `#Subscription` `#Schema` `#Resolver` `#Overfetching` `#Underfetching` `#Facebook`

## 왜 알아야 하는가?

GraphQL은 **REST API의 한계를 극복**하기 위해 Facebook이 개발한 API 쿼리 언어입니다. 클라이언트가 필요한 데이터만 정확히 요청할 수 있어, Over-fetching과 Under-fetching 문제를 해결합니다. GitHub, Shopify 등 대규모 서비스에서 채택하고 있습니다.

- **실무**: 복잡한 데이터 요구사항, 모바일 최적화, BFF 패턴
- **면접**: "REST vs GraphQL", "Over-fetching이란?"
- **기반 지식**: API 설계, 프론트엔드-백엔드 통신

## 핵심 개념

- **Query**: 데이터 조회 (GET)
- **Mutation**: 데이터 변경 (POST/PUT/DELETE)
- **Schema**: 데이터 타입과 관계 정의
- **Resolver**: 각 필드의 데이터를 가져오는 함수

## 쉽게 이해하기

**GraphQL**을 뷔페 vs 메뉴 주문에 비유할 수 있습니다.

```
REST API = 정해진 세트 메뉴
┌────────────────────────────────────────┐
│  GET /users/1                          │
│  → 이름, 이메일, 주소, 전화번호,        │
│    가입일, 프로필사진, 설정... 다 줌    │
│    (필요 없는 것도 포함 = Over-fetching)│
└────────────────────────────────────────┘

GraphQL = 뷔페에서 원하는 것만 담기
┌────────────────────────────────────────┐
│  query { user(id: 1) { name, email } } │
│  → 이름, 이메일만 줌                    │
│    (딱 필요한 것만 = No Over-fetching) │
└────────────────────────────────────────┘
```

## 상세 설명

### REST vs GraphQL

```
REST: 여러 엔드포인트, 고정된 응답
GET /users/1        → User 전체 정보
GET /users/1/posts  → User의 게시물
GET /posts/1/comments → 게시물의 댓글
(3번 요청 필요 = Under-fetching)

GraphQL: 단일 엔드포인트, 유연한 응답
POST /graphql
{
  user(id: 1) {
    name
    posts {
      title
      comments {
        content
      }
    }
  }
}
(1번 요청으로 모두 조회)
```

| 항목 | REST | GraphQL |
|------|------|---------|
| 엔드포인트 | 여러 개 | 단일 (/graphql) |
| 데이터 형태 | 서버가 결정 | 클라이언트가 결정 |
| Over-fetching | 있음 | 없음 |
| Under-fetching | 있음 | 없음 |
| 캐싱 | HTTP 캐시 활용 | 별도 구현 필요 |
| 버전 관리 | /v1, /v2 | 스키마 진화 |

### Schema 정의

```graphql
# 타입 정의
type User {
  id: ID!
  name: String!
  email: String!
  posts: [Post!]!
}

type Post {
  id: ID!
  title: String!
  content: String!
  author: User!
  comments: [Comment!]!
}

type Comment {
  id: ID!
  content: String!
  author: User!
}

# Query 정의 (조회)
type Query {
  user(id: ID!): User
  users: [User!]!
  post(id: ID!): Post
}

# Mutation 정의 (변경)
type Mutation {
  createUser(input: CreateUserInput!): User!
  createPost(input: CreatePostInput!): Post!
}

input CreateUserInput {
  name: String!
  email: String!
}
```

### Query 예시

```graphql
# 클라이언트 요청
query GetUserWithPosts {
  user(id: "1") {
    name
    email
    posts {
      title
      comments {
        content
        author {
          name
        }
      }
    }
  }
}

# 서버 응답
{
  "data": {
    "user": {
      "name": "John",
      "email": "john@example.com",
      "posts": [
        {
          "title": "Hello GraphQL",
          "comments": [
            {
              "content": "Great post!",
              "author": {
                "name": "Jane"
              }
            }
          ]
        }
      ]
    }
  }
}
```

### Mutation 예시

```graphql
# 데이터 생성
mutation CreateUser {
  createUser(input: {
    name: "John"
    email: "john@example.com"
  }) {
    id
    name
  }
}

# 데이터 수정
mutation UpdatePost {
  updatePost(id: "1", input: {
    title: "Updated Title"
  }) {
    id
    title
    updatedAt
  }
}
```

### Resolver 구현 (Java + Spring)

```java
@Controller
public class UserResolver {

    private final UserService userService;
    private final PostService postService;

    // Query resolver
    @QueryMapping
    public User user(@Argument String id) {
        return userService.findById(id);
    }

    @QueryMapping
    public List<User> users() {
        return userService.findAll();
    }

    // Field resolver (User.posts 필드)
    @SchemaMapping(typeName = "User", field = "posts")
    public List<Post> posts(User user) {
        return postService.findByAuthorId(user.getId());
    }

    // Mutation resolver
    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        return userService.create(input);
    }
}
```

### N+1 문제와 DataLoader

```java
// ❌ N+1 문제 발생
// users 10명 조회 → posts 쿼리 10번 추가 실행
@SchemaMapping(typeName = "User", field = "posts")
public List<Post> posts(User user) {
    return postService.findByAuthorId(user.getId());
}

// ✅ DataLoader로 해결 (배치 로딩)
@Configuration
public class DataLoaderConfig {

    @Bean
    public DataLoader<String, List<Post>> postsDataLoader(PostService postService) {
        return DataLoaderFactory.newDataLoader(userIds -> {
            // 한 번에 모든 사용자의 posts 조회
            Map<String, List<Post>> postsByUserId = postService.findByAuthorIds(userIds);
            return CompletableFuture.completedFuture(
                userIds.stream()
                    .map(id -> postsByUserId.getOrDefault(id, List.of()))
                    .toList()
            );
        });
    }
}
```

### Subscription (실시간)

```graphql
# 실시간 구독
subscription OnPostCreated {
  postCreated {
    id
    title
    author {
      name
    }
  }
}
```

```java
@Controller
public class PostSubscription {

    @SubscriptionMapping
    public Flux<Post> postCreated() {
        return postEventPublisher.getPostCreatedFlux();
    }
}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 필요한 데이터만 요청 | 복잡한 쿼리 시 성능 이슈 |
| 단일 요청으로 연관 데이터 조회 | HTTP 캐싱 어려움 |
| 강타입 스키마 | N+1 문제 발생 가능 |
| 버전 관리 불필요 | 학습 곡선 |
| 자동 문서화 | 파일 업로드 복잡 |

### 언제 사용하나?

**적합한 경우**:
- 복잡한 연관 관계의 데이터
- 다양한 클라이언트 (웹, 모바일, IoT)
- 빠르게 변화하는 프론트엔드 요구사항
- BFF (Backend for Frontend) 패턴

**부적합한 경우**:
- 단순 CRUD API
- 파일 업로드 중심
- 강력한 HTTP 캐싱 필요
- 팀이 REST에 익숙하고 단순한 요구사항

## 면접 예상 질문

### Q: REST vs GraphQL 차이점과 선택 기준은?

A: **REST**: 리소스 중심, 여러 엔드포인트, HTTP 캐시 활용 용이. **GraphQL**: 클라이언트 중심, 단일 엔드포인트, 필요한 데이터만 요청. **선택 기준**: (1) 다양한 클라이언트 + 복잡한 데이터 관계 → GraphQL (2) 단순 CRUD + 강력한 캐싱 필요 → REST (3) 모바일 최적화 (대역폭 절약) → GraphQL.

### Q: Over-fetching, Under-fetching이란?

A: **Over-fetching**: 필요 이상의 데이터를 받는 것. REST에서 `/users/1` 호출 시 이름만 필요해도 모든 필드 반환. **Under-fetching**: 필요한 데이터를 위해 여러 번 요청하는 것. 사용자 + 게시물 + 댓글을 위해 3번 요청. **GraphQL 해결법**: 클라이언트가 필요한 필드만 명시하여 1번 요청으로 정확히 필요한 데이터만 받음.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [HTTP/HTTPS](./http-https.md) | 선수 지식 | [3] 중급 |
| [REST API](./rest-api.md) | 비교 대상 | [3] 중급 |
| [gRPC](./grpc.md) | 다른 API 스타일 | [3] 중급 |

## 참고 자료

- [GraphQL Official](https://graphql.org/)
- [How to GraphQL](https://www.howtographql.com/)
- Learning GraphQL - Eve Porcello
