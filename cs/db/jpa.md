# JPA (Java Persistence API)

> Java 애플리케이션에서 관계형 데이터베이스를 객체지향적으로 다루기 위한 ORM 표준 명세

> `[4] 심화` · 선수 지식: [SQL](./sql.md), [Transaction](./transaction.md)

## 왜 알아야 하는가?

JPA는 Java/Spring 백엔드 개발에서 사실상 표준 ORM 기술입니다. SQL을 직접 작성하는 JDBC 방식에 비해 생산성이 10배 이상 향상되고, 객체지향적인 코드 작성이 가능합니다. 하지만 내부 동작 원리를 이해하지 못하면 N+1 문제, 영속성 컨텍스트 관련 버그, 성능 저하 등의 문제가 발생합니다. 실무에서 JPA를 제대로 활용하려면 영속성 컨텍스트, 지연 로딩, JPQL, 트랜잭션 관리 등의 핵심 개념을 반드시 이해해야 합니다.

## 핵심 개념

- **ORM (Object-Relational Mapping)**: 객체와 관계형 DB 테이블을 매핑하여 SQL 없이 객체로 DB 조작
- **영속성 컨텍스트 (Persistence Context)**: Entity를 관리하는 1차 캐시 + 변경 감지 환경
- **Entity**: DB 테이블과 매핑되는 Java 객체
- **JPQL**: 객체 지향 쿼리 언어 (테이블이 아닌 Entity 대상으로 쿼리)
- **지연 로딩 (Lazy Loading)**: 연관 Entity를 실제 사용 시점에 조회하여 성능 최적화

## 쉽게 이해하기

**JPA**를 비서에 비유할 수 있습니다.

개발자가 "고객 정보 가져와"라고 말하면, 비서(JPA)가 알아서 DB에서 찾아오고, "고객 이름 바꿔"라고 하면 비서가 알아서 DB에 반영합니다.
개발자는 SQL을 직접 작성하지 않고 객체만 다루면 됩니다.

예를 들어, 회원 정보를 수정할 때:
- **JDBC 방식**: `UPDATE member SET name = '홍길동' WHERE id = 1` SQL 직접 작성
- **JPA 방식**: `member.setName("홍길동")` 객체 수정만 하면 JPA가 알아서 UPDATE

비서가 "이 고객 정보는 최근에 봤으니 다시 DB 안 가도 돼요"라고 캐싱해주는 것이 **영속성 컨텍스트**입니다.

## 상세 설명

### ORM과 JPA의 관계

```
ORM (개념) ← JPA (표준 명세) ← Hibernate (구현체)
```

| 구분 | 설명 |
|------|------|
| ORM | 객체-관계 매핑 개념/패러다임 |
| JPA | Java 진영의 ORM 표준 인터페이스 (javax.persistence) |
| Hibernate | JPA 구현체 중 하나 (가장 널리 사용) |
| Spring Data JPA | JPA를 더 쉽게 사용하게 해주는 Spring 모듈 |

**왜 JPA를 사용하는가?**

1. **생산성**: CRUD용 SQL을 직접 작성하지 않아도 됨
2. **유지보수성**: 컬럼 추가 시 Entity 필드만 추가하면 됨 (SQL 수정 불필요)
3. **패러다임 불일치 해결**: 상속, 연관관계 등 객체 모델과 RDB 모델의 차이를 JPA가 해결
4. **DB 벤더 독립성**: JPQL로 작성하면 MySQL, Oracle 등 DB가 바뀌어도 코드 변경 불필요

### 영속성 컨텍스트 (Persistence Context)

영속성 컨텍스트는 **Entity를 영구 저장하는 환경**으로, EntityManager를 통해 접근합니다.

```
┌─────────────────────────────────────────────┐
│           영속성 컨텍스트                      │
│  ┌─────────────────────────────────────┐    │
│  │          1차 캐시                    │    │
│  │  ┌─────────┬─────────────────────┐  │    │
│  │  │   @Id   │      Entity         │  │    │
│  │  ├─────────┼─────────────────────┤  │    │
│  │  │  "1"    │   Member(id=1)      │  │    │
│  │  │  "2"    │   Member(id=2)      │  │    │
│  │  └─────────┴─────────────────────┘  │    │
│  └─────────────────────────────────────┘    │
│                                             │
│  ┌─────────────────────────────────────┐    │
│  │      쓰기 지연 SQL 저장소              │    │
│  │  INSERT INTO member VALUES (1,...)   │    │
│  │  UPDATE member SET name=... WHERE... │    │
│  └─────────────────────────────────────┘    │
└─────────────────────────────────────────────┘
                    ↓ flush()
              ┌─────────────┐
              │   Database  │
              └─────────────┘
```

#### 영속성 컨텍스트의 이점

| 기능 | 설명 | 왜 좋은가? |
|------|------|-----------|
| 1차 캐시 | 조회 시 캐시 먼저 확인 | 동일 트랜잭션 내 반복 조회 시 DB 접근 불필요 |
| 동일성 보장 | 같은 Entity 조회 시 같은 인스턴스 반환 | `==` 비교 가능, 메모리 절약 |
| 쓰기 지연 | INSERT/UPDATE SQL을 모았다가 한번에 전송 | 네트워크 왕복 최소화 |
| 변경 감지 | Entity 변경 시 자동으로 UPDATE SQL 생성 | 명시적 update() 호출 불필요 |
| 지연 로딩 | 연관 Entity를 실제 사용 시점에 조회 | 불필요한 조인 방지 |

### Entity 생명주기

```
                    persist()
    비영속(new) ────────────────→ 영속(managed)
       ↑                              │
       │                              │ detach() / clear() / close()
       │ merge()                      ↓
       └──────────────────────── 준영속(detached)
                                      │
                                      │ remove()
                                      ↓
                                 삭제(removed)
```

| 상태 | 설명 | 영속성 컨텍스트와 관계 |
|------|------|----------------------|
| 비영속 (new/transient) | 순수 Java 객체 상태 | 관계 없음 |
| 영속 (managed) | 영속성 컨텍스트에 저장된 상태 | 관리됨 |
| 준영속 (detached) | 영속성 컨텍스트에서 분리된 상태 | 관리 안 됨 |
| 삭제 (removed) | 삭제 예정 상태 | 삭제 예정 |

```java
// 비영속 - 순수 객체 상태
Member member = new Member();
member.setName("홍길동");

// 영속 - 영속성 컨텍스트에 저장
em.persist(member);

// 준영속 - 영속성 컨텍스트에서 분리
em.detach(member);

// 삭제 - DB에서 삭제
em.remove(member);
```

**왜 이렇게 상태를 나누는가?**

- 영속 상태일 때만 변경 감지, 지연 로딩 등의 기능이 동작함
- 준영속 상태는 트랜잭션 밖에서 Entity를 안전하게 사용할 때 활용
- 상태 전이를 이해해야 "왜 지연 로딩이 안 되지?"와 같은 문제 해결 가능

### 연관관계 매핑

#### 연관관계 종류

| 매핑 | 예시 | 설명 |
|------|------|------|
| @ManyToOne | 주문 → 회원 | 다대일: 여러 주문이 한 회원에 속함 |
| @OneToMany | 회원 → 주문들 | 일대다: 한 회원이 여러 주문을 가짐 |
| @OneToOne | 회원 → 프로필 | 일대일: 한 회원이 하나의 프로필 |
| @ManyToMany | 학생 ↔ 강의 | 다대다: 여러 학생이 여러 강의 수강 |

#### 연관관계 주인 (Owner)

**왜 연관관계 주인이 필요한가?**

양방향 연관관계에서 두 Entity 모두 서로를 참조합니다. DB에서는 외래키 하나로 양쪽 조인이 가능하지만, 객체는 각자 참조를 가집니다.
**둘 중 하나**를 연관관계의 주인으로 정해서 외래키를 관리해야 합니다.

```java
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "member")  // 연관관계 주인 아님 (읽기 전용)
    private List<Order> orders = new ArrayList<>();
}

@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")  // 연관관계 주인 (외래키 관리)
    private Member member;
}
```

**권장 (O)**: 외래키가 있는 쪽을 연관관계 주인으로 설정
**비권장 (X)**: 외래키가 없는 쪽을 주인으로 설정

**왜?**
- 외래키가 있는 테이블의 Entity가 주인이면 INSERT/UPDATE SQL이 직관적
- 외래키가 없는 쪽이 주인이면, 다른 테이블에 UPDATE 쿼리가 추가 발생 (성능 저하)

### 지연 로딩 vs 즉시 로딩

```java
// 즉시 로딩 (EAGER) - 권장하지 않음
@ManyToOne(fetch = FetchType.EAGER)
private Team team;

// 지연 로딩 (LAZY) - 권장
@ManyToOne(fetch = FetchType.LAZY)
private Team team;
```

| 로딩 방식 | 시점 | 특징 |
|----------|------|------|
| 즉시 로딩 (EAGER) | Entity 조회 시 연관 Entity도 함께 조회 | JOIN 발생, N+1 문제 야기 가능 |
| 지연 로딩 (LAZY) | 연관 Entity 실제 사용 시점에 조회 | 프록시 객체 사용, 필요할 때만 쿼리 |

**권장 (O)**: 모든 연관관계에 지연 로딩 사용
**비권장 (X)**: @ManyToOne, @OneToOne에 즉시 로딩 사용

**왜?**
- 즉시 로딩은 예상치 못한 SQL 발생 (개발자가 쿼리 예측 어려움)
- JPQL에서 N+1 문제 발생의 주요 원인
- 실무에서는 지연 로딩 + 필요시 fetch join으로 해결

### N+1 문제

**N+1 문제란?**

1개의 쿼리로 N개의 결과를 가져온 후, 각 결과마다 추가로 1개씩 총 N개의 쿼리가 더 발생하는 문제

```java
// 회원 10명 조회 - 쿼리 1번
List<Member> members = em.createQuery("select m from Member m", Member.class)
                         .getResultList();

// 각 회원의 팀 접근 시 - 쿼리 10번 추가 발생!
for (Member member : members) {
    System.out.println(member.getTeam().getName());  // LAZY인데도 N+1 발생
}
// 총 11번 쿼리 (1 + 10)
```

**왜 발생하는가?**
- JPQL은 SQL로 번역될 때 연관관계를 고려하지 않음
- `select m from Member m`은 `SELECT * FROM member`로 변환
- 이후 member.getTeam() 호출 시 각각 SELECT 쿼리 발생

#### N+1 해결 방법

**1. Fetch Join**

```java
// Fetch Join으로 한 번에 조회
List<Member> members = em.createQuery(
    "select m from Member m join fetch m.team", Member.class)
    .getResultList();
// 쿼리 1번만 발생 (JOIN으로 team도 함께 조회)
```

**2. @EntityGraph**

```java
@EntityGraph(attributePaths = {"team"})
@Query("select m from Member m")
List<Member> findAllWithTeam();
```

**3. Batch Size 설정**

```java
// application.yml
spring.jpa.properties.hibernate.default_batch_fetch_size: 100

// 또는 Entity에 직접
@BatchSize(size = 100)
@OneToMany(mappedBy = "member")
private List<Order> orders;
```

| 해결 방법 | 장점 | 단점 |
|----------|------|------|
| Fetch Join | 한 번의 쿼리로 해결, 직관적 | 페이징 불가 (컬렉션 fetch join 시) |
| @EntityGraph | 어노테이션으로 간편 적용 | 복잡한 조건 처리 어려움 |
| Batch Size | 설정만으로 적용, 페이징 가능 | IN 쿼리 개수만큼은 쿼리 발생 |

### JPQL vs QueryDSL vs Native Query

| 구분 | 특징 | 사용 시점 |
|------|------|----------|
| JPQL | 문자열 기반, Entity 대상 쿼리 | 간단한 쿼리 |
| QueryDSL | 타입 안전, 자동완성 지원 | 복잡한 동적 쿼리 |
| Native Query | 순수 SQL | 특정 DB 기능, 복잡한 통계 쿼리 |

```java
// JPQL
String jpql = "select m from Member m where m.age > :age";
List<Member> members = em.createQuery(jpql, Member.class)
                         .setParameter("age", 20)
                         .getResultList();

// QueryDSL (타입 안전)
List<Member> members = queryFactory
    .selectFrom(member)
    .where(member.age.gt(20))
    .fetch();
```

**왜 QueryDSL을 사용하는가?**
- 컴파일 시점 문법 오류 발견 (JPQL은 런타임에 발견)
- IDE 자동완성 지원
- 동적 쿼리 작성이 편리함

## 동작 원리

### 변경 감지 (Dirty Checking) 동작 방식

```
1. 트랜잭션 시작
2. Entity 조회 → 영속성 컨텍스트의 1차 캐시에 저장 + 스냅샷 저장
3. Entity 수정 (setter 호출)
4. 트랜잭션 커밋 시점
   ├─ flush() 호출
   ├─ Entity와 스냅샷 비교
   ├─ 변경된 Entity가 있으면 UPDATE SQL 생성
   └─ DB에 SQL 전송
5. 트랜잭션 커밋
```

```java
@Transactional
public void updateMember(Long id, String newName) {
    Member member = em.find(Member.class, id);  // 영속 상태
    member.setName(newName);  // 변경
    // em.update(member) 같은 코드가 필요 없음!
}  // 트랜잭션 커밋 시점에 자동으로 UPDATE SQL 실행
```

### 프록시와 지연 로딩 동작 방식

```
em.find(Member.class, 1L)
  → DB에서 바로 조회
  → 실제 Entity 반환

em.getReference(Member.class, 1L)  // 또는 LAZY 로딩
  → DB 조회 안 함
  → 프록시 객체 반환 (껍데기)

프록시.getName()  // 실제 사용 시점
  → 영속성 컨텍스트에 초기화 요청
  → DB 조회 (SELECT 쿼리 발생)
  → 실제 Entity 생성 후 프록시가 참조
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 생산성 향상 (SQL 작성 감소) | 학습 곡선이 높음 |
| 객체지향적 설계 가능 | 복잡한 쿼리는 성능 튜닝 어려움 |
| DB 벤더 독립성 | 내부 동작 이해 없이 사용 시 장애 유발 |
| 유지보수성 향상 | 캐시 전략 등 추가 고려 필요 |

**결론**: 단순 CRUD 위주의 애플리케이션에서는 생산성이 높지만, 복잡한 통계/집계 쿼리는 Native Query나 별도 기술(MyBatis 등) 병행 권장

## 주의사항

- **준영속 상태에서 지연 로딩 불가**
  - 왜 문제인가: 트랜잭션 밖에서 LAZY 로딩 시 `LazyInitializationException` 발생
  - 해결 방법: OSIV(Open Session In View) 또는 DTO 변환 후 반환

- **양방향 연관관계 무한 루프**
  - 왜 문제인가: toString(), JSON 변환 시 서로 호출하며 StackOverflow
  - 해결 방법: @JsonIgnore, DTO 사용, Lombok @ToString(exclude) 사용

- **@ManyToMany 사용 지양**
  - 왜 문제인가: 중간 테이블에 추가 컬럼 불가, 쿼리 예측 어려움
  - 해결 방법: 중간 Entity를 직접 만들어 @ManyToOne 두 개로 풀어서 구현

## 예제 코드

### Entity 설계 예시

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    // 생성 메서드
    public static Member createMember(String name, Team team) {
        Member member = new Member();
        member.name = name;
        member.changeTeam(team);
        return member;
    }
}

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Member> members = new ArrayList<>();
}
```

### Repository 예시 (Spring Data JPA)

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 메서드 이름으로 쿼리 생성
    List<Member> findByName(String name);

    // JPQL
    @Query("select m from Member m where m.age > :age")
    List<Member> findByAgeGreaterThan(@Param("age") int age);

    // Fetch Join으로 N+1 해결
    @Query("select m from Member m join fetch m.team")
    List<Member> findAllWithTeam();

    // EntityGraph
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
}
```

## 면접 예상 질문

- Q: JPA의 영속성 컨텍스트란 무엇이고, 어떤 이점이 있나요?
  - A: 영속성 컨텍스트는 Entity를 관리하는 논리적 공간입니다. **왜냐하면** 1차 캐시를 통해 동일 트랜잭션 내 반복 조회 시 DB 접근을 줄이고, 변경 감지(Dirty Checking)로 Entity 수정 시 자동으로 UPDATE SQL을 생성하며, 쓰기 지연으로 SQL을 모았다가 한 번에 전송하여 네트워크 비용을 줄일 수 있기 때문입니다. **따라서** 개발 생산성과 성능 모두 향상됩니다.

- Q: N+1 문제가 무엇이고 어떻게 해결하나요?
  - A: N+1 문제는 1개의 쿼리로 N개의 결과를 가져온 후, 연관 Entity 조회를 위해 N개의 추가 쿼리가 발생하는 문제입니다. **왜냐하면** JPQL은 연관관계를 고려하지 않고 SQL로 번역되기 때문입니다. 해결 방법으로는 (1) Fetch Join으로 한 번에 조회, (2) @EntityGraph 사용, (3) Batch Size 설정이 있습니다. **실무에서는** 지연 로딩을 기본으로 하고 필요한 곳에서만 Fetch Join이나 EntityGraph를 적용합니다.

- Q: 즉시 로딩(EAGER)과 지연 로딩(LAZY)의 차이와 권장 방식은?
  - A: 즉시 로딩은 Entity 조회 시 연관 Entity도 함께 조회하고, 지연 로딩은 실제 사용 시점에 조회합니다. **실무에서는 지연 로딩을 권장**합니다. **왜냐하면** 즉시 로딩은 예상치 못한 SQL이 발생하여 성능 문제를 야기하고, 특히 JPQL 사용 시 N+1 문제의 주요 원인이 되기 때문입니다. **따라서** 모든 연관관계에 LAZY를 적용하고, 필요할 때 Fetch Join으로 한 번에 조회하는 것이 좋습니다.

- Q: 변경 감지(Dirty Checking)는 어떻게 동작하나요?
  - A: 영속성 컨텍스트에 Entity가 저장될 때 최초 상태를 스냅샷으로 저장합니다. 트랜잭션 커밋 시점에 flush()가 호출되면 현재 Entity와 스냅샷을 비교하여 변경된 부분을 찾고, UPDATE SQL을 생성하여 DB에 반영합니다. **왜 이렇게 하는가?** 개발자가 명시적으로 update()를 호출하지 않아도 되어 객체지향적 코드 작성이 가능하고, 변경된 필드만 UPDATE하므로 효율적입니다.

## 연관 문서

- [Transaction](./transaction.md): JPA 트랜잭션과 영속성 컨텍스트의 관계
- [SQL](./sql.md): JPQL과 SQL의 차이
- [Index](./index.md): JPA 성능 최적화를 위한 인덱스 설계
- [Normalization](./normalization.md): Entity 설계와 정규화

## 참고 자료

- [JPA 공식 명세 (JSR 338)](https://jcp.org/en/jsr/detail?id=338)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/)
- [김영한 - 자바 ORM 표준 JPA 프로그래밍](https://www.yes24.com/Product/Goods/19040233)
