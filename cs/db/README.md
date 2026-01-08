# Database

데이터베이스 관련 학습 내용을 정리합니다.

## 학습 로드맵

```
┌─────────────────────────────────────────────────────────────────┐
│                        학습 순서                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   [1] 데이터베이스란                                             │
│        - 데이터베이스의 정의와 종류                              │
│            │                                                     │
│            ▼                                                     │
│   [2] SQL                                                        │
│        - 데이터 조작 언어                                        │
│            │                                                     │
│            ├──────────────┬──────────────┐                      │
│            ▼              ▼              ▼                      │
│   [3] Index        [3] Transaction  [3] Normalization           │
│        - 성능 최적화와 데이터 무결성                             │
│            │              │              │                      │
│            └──────────────┴──────────────┘                      │
│                           │                                      │
│            ┌──────────────┼──────────────┐                      │
│            ▼              ▼              ▼                      │
│   [4] MySQL Index   [4] JPA        [4] NoSQL                    │
│        - 심화 및 ORM                   │                        │
│            │                           ▼                        │
│            ▼               [3] Elasticsearch                    │
│   [3] Redis Caching             - 역인덱스, 분산 검색            │
│        - 캐싱 전략, 무효화, 일관성          │                    │
│                                         ▼                       │
│                             [4] ES 풀 색인 전략                  │
│                                  - Lucene 세그먼트, Alias 스왑   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 난이도별 목차

### [1] 정의/기초

데이터베이스가 무엇인지부터 시작하세요.

| 문서 | 설명 | 예상 시간 |
|------|------|----------|
| [데이터베이스란](./what-is-database.md) | DB의 정의, RDBMS vs NoSQL, 기본 용어 | 25분 |

### [2] 입문

데이터베이스 기초를 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [SQL](./sql.md) | SQL 기본, SELECT/JOIN/서브쿼리 | 데이터베이스란 |

### [3] 중급

SQL 기본 개념을 이해한 후 학습하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Index](./index.md) | B-Tree/B+Tree, 클러스터드/논클러스터드 | SQL, 자료구조 기초 |
| [Transaction](./transaction.md) | ACID, 격리 수준, 락, MVCC | SQL |
| [Normalization](./normalization.md) | 정규화(1NF~3NF), 이상현상, 반정규화 | SQL |
| [LATERAL JOIN](./lateral-join.md) | 상관 조인, Top-N per Group, CROSS/OUTER APPLY | SQL |
| [Redis Caching](./redis-caching.md) | 캐싱 전략, 무효화, 일관성, 스탬피드 | NoSQL |
| [Elasticsearch](./elasticsearch.md) | 역인덱스, 전문검색, 분산 검색엔진 | NoSQL, Index |

### [4] 심화

기본 개념을 모두 익힌 후 도전하세요.

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [MySQL 인덱스](./mysql-index.md) | B+Tree, 클러스터드/세컨더리 인덱스 | Index |
| [JPA](./jpa.md) | 영속성 컨텍스트, N+1 문제, 지연 로딩 | SQL, Transaction, OOP |
| [NoSQL](./nosql.md) | Document/Key-Value/Column/Graph, CAP | SQL, 시스템 설계 기초 |
| [ES 풀 색인 전략](./elasticsearch-reindexing.md) | 덮어쓰기 vs Alias 스왑, Lucene 세그먼트 | NoSQL, Index |
| [샤딩](./sharding.md) | 수평 분할, 샤드 키 선택, 리샤딩 | 데이터베이스란, Index, Transaction |
| [레플리케이션](./replication.md) | Master-Slave, 동기/비동기 복제, Failover | 데이터베이스란, Transaction |
| [실행 계획](./execution-plan.md) | EXPLAIN, 쿼리 분석, 최적화 | SQL, Index |

## 전체 목차

### ORM
- [JPA](./jpa.md) - 영속성 컨텍스트, N+1 문제, 지연 로딩, Entity 생명주기

### MySQL
- [MySQL 인덱스](./mysql-index.md) - B+Tree 인덱스, 클러스터드/세컨더리 인덱스, 복합 인덱스

### Redis
- [Redis Caching](./redis-caching.md) - 캐싱 전략(Cache-Aside, Write-Through, Write-Behind), 캐시 무효화, 일관성

### Elasticsearch
- [Elasticsearch](./elasticsearch.md) - 역인덱스, 전문검색, Analyzer, 분산 검색엔진
- [ES 풀 색인 전략](./elasticsearch-reindexing.md) - 덮어쓰기 vs Alias 스왑, Lucene 세그먼트, 디스크 용량 피크

### 기초
- [데이터베이스란](./what-is-database.md) - DB의 정의, RDBMS vs NoSQL, DBMS, 기본 용어

### 일반
- [SQL](./sql.md) - SQL 기본, SELECT/JOIN/서브쿼리, 실행 계획, 쿼리 최적화
- [Index](./index.md) - 인덱스 개념, B-Tree/B+Tree, 클러스터드/논클러스터드, 커버링 인덱스
- [Transaction](./transaction.md) - 트랜잭션, ACID, 격리 수준, 락(공유락/배타락), MVCC
- [Normalization](./normalization.md) - 정규화(1NF~3NF), 이상현상, 반정규화
- [LATERAL JOIN](./lateral-join.md) - 상관 조인, Top-N per Group, CROSS/OUTER APPLY
- [NoSQL](./nosql.md) - NoSQL 유형(Document/Key-Value/Column/Graph), CAP 정리

### 분산 데이터베이스
- [샤딩](./sharding.md) - 수평 분할, 샤드 키, 해시/범위 샤딩, 리샤딩
- [레플리케이션](./replication.md) - Master-Slave, 동기/비동기 복제, Failover

### 성능 분석
- [실행 계획](./execution-plan.md) - EXPLAIN, 쿼리 최적화, 조인 알고리즘

## 작성 예정

*(모든 예정 문서가 작성 완료되었습니다)*
