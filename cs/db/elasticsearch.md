# 검색엔진과 Elasticsearch

> `[3] 중급` · 선수 지식: [NoSQL](./nosql.md), [Index](./index.md)

> 대용량 데이터에서 빠른 전문 검색(Full-text Search)을 제공하는 분산 검색 및 분석 엔진

`#Elasticsearch` `#엘라스틱서치` `#ES` `#ELK` `#ElasticStack` `#검색엔진` `#SearchEngine` `#FullTextSearch` `#전문검색` `#역인덱스` `#InvertedIndex` `#Lucene` `#루씬` `#형태소분석` `#Tokenizer` `#Analyzer` `#Nori` `#노리` `#Shard` `#샤드` `#Replica` `#레플리카` `#DocumentDB` `#JSON` `#RESTful` `#Kibana` `#Logstash` `#분산시스템` `#실시간검색` `#로그분석`

## 왜 알아야 하는가?

현대 서비스에서 검색은 핵심 기능입니다. 쇼핑몰 상품 검색, 블로그 포스트 검색, 로그 분석 등 어디서나 필요합니다. 하지만 RDBMS의 `LIKE '%검색어%'`는 인덱스를 사용하지 못해 대용량 데이터에서 매우 느립니다. Elasticsearch는 역인덱스(Inverted Index) 구조로 수억 건의 문서에서도 밀리초 단위 검색을 제공합니다.

- **실무**: 상품 검색, 로그 분석, 모니터링, 추천 시스템 구축에 필수
- **면접**: "전문 검색을 어떻게 구현하나요?", "RDBMS LIKE와의 차이는?" 등 자주 출제
- **기반 지식**: 분산 시스템, 샤딩, 복제 개념 이해의 기반

## 핵심 개념

- **역인덱스 (Inverted Index)**: 단어 → 문서 ID 매핑으로 빠른 검색 지원
- **Analyzer**: 텍스트를 토큰으로 분리하는 전처리 파이프라인
- **샤드 (Shard)**: 인덱스를 분할하여 분산 저장하는 단위
- **레플리카 (Replica)**: 샤드의 복제본으로 고가용성과 읽기 성능 향상
- **Document**: JSON 형태로 저장되는 데이터의 기본 단위

## 쉽게 이해하기

**검색엔진**을 도서관 색인 시스템에 비유할 수 있습니다.

**RDBMS의 LIKE 검색** = 모든 책을 한 권씩 넘기며 찾기
- "자바"라는 단어가 포함된 책을 찾으려면 도서관의 모든 책을 열어봐야 함
- 책이 100만 권이면 100만 번 확인 → 매우 느림

**Elasticsearch의 역인덱스** = 도서관 뒤편의 색인 카드
- "자바" 카드를 펼치면 "자바"가 언급된 모든 책 번호가 적혀 있음
- 한 번만 찾으면 됨 → 매우 빠름

```
색인 카드 (역인덱스):
┌─────────┬────────────────────────┐
│  단어   │      책 번호 목록       │
├─────────┼────────────────────────┤
│ 자바    │ 책#1, 책#5, 책#42      │
│ 스프링  │ 책#3, 책#5, 책#99      │
│ 데이터  │ 책#1, 책#7, 책#42      │
└─────────┴────────────────────────┘

"자바"와 "스프링" 모두 포함된 책 = 책#5 (교집합)
```

**왜 역인덱스인가?**
일반 인덱스는 "책#1 → 자바, 데이터, ..." (문서 → 단어)이지만,
역인덱스는 "자바 → 책#1, 책#5, ..." (단어 → 문서)로 **역방향**입니다.

## 상세 설명

### 검색엔진이란?

**정의**: 대량의 데이터에서 원하는 정보를 빠르게 찾아주는 시스템

**검색의 종류**:

| 종류 | 설명 | 예시 |
|------|------|------|
| **정확 매칭** (Exact Match) | 정확히 일치하는 값 검색 | `WHERE id = 1001` |
| **범위 검색** (Range) | 특정 범위 내 값 검색 | `WHERE price BETWEEN 1000 AND 5000` |
| **전문 검색** (Full-text) | 텍스트 내용 검색 | "자바 스프링 입문" 포함된 문서 |
| **유사도 검색** (Fuzzy) | 오타 허용 검색 | "Elasticserch" → "Elasticsearch" |

**RDBMS로 전문 검색이 어려운 이유**:

```sql
-- LIKE 검색: 인덱스 사용 불가 (Full Table Scan)
SELECT * FROM products WHERE name LIKE '%자바%';

-- 100만 건 테이블에서 1초 이상 소요
-- 동시 사용자 100명이면 DB 과부하
```

**왜 LIKE '%검색어%'가 느린가?**
- B-Tree 인덱스는 **접두사(prefix) 검색만 지원**
- `LIKE '자바%'`는 인덱스 사용 가능 (접두사)
- `LIKE '%자바%'`는 인덱스 사용 불가 (중간, 접미사)
- 따라서 테이블 전체를 순차 스캔해야 함

### Elasticsearch란?

**정의**: Apache Lucene 기반의 분산 검색 및 분석 엔진

**핵심 특징**:

1. **RESTful API**: HTTP 요청으로 모든 작업 수행
2. **분산 시스템**: 수평 확장으로 대용량 처리
3. **Near Real-time**: 인덱싱 후 약 1초 내 검색 가능
4. **스키마리스**: JSON 문서 저장, 동적 매핑

**Elastic Stack (ELK)**:

```
┌─────────────────────────────────────────────────────────┐
│                      Kibana                              │
│        (시각화, 대시보드, 모니터링)                       │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                  Elasticsearch                           │
│           (검색, 저장, 분석 엔진)                         │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│           Logstash / Beats                               │
│        (데이터 수집, 파이프라인)                          │
└─────────────────────────────────────────────────────────┘
```

### 역인덱스 (Inverted Index)

**동작 원리**:

**1단계: 문서 저장**

```json
{"id": 1, "title": "자바 프로그래밍 입문"}
{"id": 2, "title": "스프링 부트 시작하기"}
{"id": 3, "title": "자바 스프링 웹 개발"}
```

**2단계: 토큰화 (Tokenization)**

```
문서 1: [자바, 프로그래밍, 입문]
문서 2: [스프링, 부트, 시작하기]
문서 3: [자바, 스프링, 웹, 개발]
```

**3단계: 역인덱스 생성**

```
┌────────────┬─────────────────────────────────┐
│   Term     │   Posting List (문서 ID, 위치)   │
├────────────┼─────────────────────────────────┤
│ 개발       │ [(3, pos:4)]                    │
│ 부트       │ [(2, pos:2)]                    │
│ 스프링     │ [(2, pos:1), (3, pos:2)]        │
│ 시작하기   │ [(2, pos:3)]                    │
│ 웹         │ [(3, pos:3)]                    │
│ 입문       │ [(1, pos:3)]                    │
│ 자바       │ [(1, pos:1), (3, pos:1)]        │
│ 프로그래밍 │ [(1, pos:2)]                    │
└────────────┴─────────────────────────────────┘
```

**검색 과정**:

```
검색어: "자바 스프링"

1. "자바" 조회 → 문서 1, 3
2. "스프링" 조회 → 문서 2, 3
3. AND 연산 → 문서 3 (교집합)
4. 결과 반환: 문서 3
```

**왜 빠른가?**
- Term을 정렬된 상태로 저장 → 이진 검색 O(log N)
- Posting List에서 문서 ID로 바로 접근
- 100만 건도 밀리초 단위 검색

### Analyzer (분석기)

**정의**: 텍스트를 검색 가능한 토큰으로 변환하는 파이프라인

**구성 요소**:

```
입력 텍스트 → Character Filter → Tokenizer → Token Filter → 토큰
```

| 구성 요소 | 역할 | 예시 |
|----------|------|------|
| **Character Filter** | 문자 변환 | HTML 태그 제거, 특수문자 변환 |
| **Tokenizer** | 토큰 분리 | 공백, 구두점 기준 분리 |
| **Token Filter** | 토큰 가공 | 소문자 변환, 불용어 제거, 동의어 |

**분석 예시**:

```
입력: "The Quick Brown FOX!"

Character Filter: "The Quick Brown FOX!" (변화 없음)
Tokenizer:        [The, Quick, Brown, FOX]
Token Filter:
  - lowercase:    [the, quick, brown, fox]
  - stop words:   [quick, brown, fox]  ("the" 제거)

최종 토큰: [quick, brown, fox]
```

**한글 형태소 분석기 (Nori)**:

한글은 공백만으로 분리하면 안 됩니다:
- "삼성전자가" → "삼성전자", "가" (조사 분리)
- "아름다운" → "아름답", "ㄴ" (어간, 어미 분리)

```
입력: "삼성전자가 반도체를 생산한다"

Standard Analyzer (공백 분리):
  [삼성전자가, 반도체를, 생산한다]
  → "삼성전자" 검색 시 매칭 안 됨!

Nori Analyzer (형태소 분석):
  [삼성전자, 반도체, 생산]
  → "삼성전자" 검색 시 매칭 성공!
```

**왜 형태소 분석이 필요한가?**
- "달리다", "달린다", "달렸다" → 모두 "달리" 로 정규화
- 어간 기준으로 검색하면 활용형 모두 매칭

### Elasticsearch 아키텍처

**클러스터 구조**:

```
┌─────────────────────── Cluster ───────────────────────┐
│                                                        │
│  ┌─────────────────┐  ┌─────────────────┐              │
│  │     Node 1      │  │     Node 2      │              │
│  │   (Master)      │  │    (Data)       │              │
│  │ ┌─────┐ ┌─────┐ │  │ ┌─────┐ ┌─────┐ │              │
│  │ │ P0  │ │ P1  │ │  │ │ R0  │ │ R1  │ │              │
│  │ └─────┘ └─────┘ │  │ └─────┘ └─────┘ │              │
│  └─────────────────┘  └─────────────────┘              │
│                                                        │
│  ┌─────────────────┐                                   │
│  │     Node 3      │   P = Primary Shard               │
│  │    (Data)       │   R = Replica Shard               │
│  │ ┌─────┐ ┌─────┐ │                                   │
│  │ │ P2  │ │ R2  │ │                                   │
│  │ └─────┘ └─────┘ │                                   │
│  └─────────────────┘                                   │
└────────────────────────────────────────────────────────┘
```

**용어 정리**:

| 용어 | 설명 |
|------|------|
| **Cluster** | 하나 이상의 노드로 구성된 집합 |
| **Node** | Elasticsearch 프로세스 (서버 1대 = 1노드 권장) |
| **Index** | 문서의 논리적 그룹 (RDBMS의 테이블과 유사) |
| **Shard** | 인덱스를 분할한 조각 (Lucene 인덱스) |
| **Primary Shard** | 원본 데이터 샤드 |
| **Replica Shard** | Primary의 복제본 (다른 노드에 배치) |

**샤딩이 필요한 이유**:

1. **데이터 분산**: 1TB 인덱스 → 10개 샤드 × 100GB
2. **병렬 처리**: 10개 샤드에서 동시 검색 → 10배 빠름
3. **수평 확장**: 노드 추가로 용량 증가

**레플리카가 필요한 이유**:

1. **고가용성**: Primary 노드 장애 시 Replica가 승격
2. **읽기 부하 분산**: 검색 요청을 여러 Replica에서 처리

### 기본 CRUD 연산

**인덱스 생성**:

```bash
PUT /products
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "name": { "type": "text", "analyzer": "nori" },
      "price": { "type": "integer" },
      "category": { "type": "keyword" }
    }
  }
}
```

**문서 인덱싱 (Create)**:

```bash
POST /products/_doc/1
{
  "name": "삼성 갤럭시 S24",
  "price": 1200000,
  "category": "스마트폰"
}
```

**문서 조회 (Read)**:

```bash
GET /products/_doc/1
```

**문서 수정 (Update)**:

```bash
POST /products/_update/1
{
  "doc": {
    "price": 1100000
  }
}
```

**문서 삭제 (Delete)**:

```bash
DELETE /products/_doc/1
```

### 검색 쿼리

**Match Query (전문 검색)**:

```bash
GET /products/_search
{
  "query": {
    "match": {
      "name": "갤럭시 스마트폰"
    }
  }
}
```
- 분석기를 통해 토큰화 후 검색
- "갤럭시" OR "스마트폰" 매칭

**Term Query (정확 매칭)**:

```bash
GET /products/_search
{
  "query": {
    "term": {
      "category": "스마트폰"
    }
  }
}
```
- 분석 없이 정확히 일치하는 값 검색
- keyword 타입에 사용

**Bool Query (복합 조건)**:

```bash
GET /products/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "name": "갤럭시" } }
      ],
      "filter": [
        { "range": { "price": { "gte": 500000, "lte": 1500000 } } }
      ],
      "must_not": [
        { "term": { "category": "태블릿" } }
      ]
    }
  }
}
```

| 조건 | 설명 | 스코어 영향 |
|------|------|------------|
| **must** | 반드시 만족해야 함 | O |
| **should** | 만족하면 스코어 증가 | O |
| **filter** | 반드시 만족 (필터링) | X |
| **must_not** | 반드시 불만족 | X |

**왜 filter와 must를 구분하는가?**
- `must`: 스코어 계산 포함 (느림)
- `filter`: 스코어 계산 제외 + 캐싱 (빠름)
- 범위 조건, 정확 매칭은 `filter` 권장

### text vs keyword

| 타입 | 분석 여부 | 용도 | 예시 |
|------|----------|------|------|
| **text** | O (토큰화) | 전문 검색 | 상품명, 설명, 본문 |
| **keyword** | X (그대로 저장) | 정확 매칭, 정렬, 집계 | 카테고리, 태그, 이메일 |

```json
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "fields": {
          "keyword": { "type": "keyword" }
        }
      }
    }
  }
}
```
- `title`: 전문 검색용 (match query)
- `title.keyword`: 정렬/집계용 (term query)

## 동작 원리

### 인덱싱 과정

```
1. 클라이언트 → 문서 전송

2. Coordinating Node
   └── 문서 ID 해싱 → 대상 샤드 결정
       (routing = hash(_id) % number_of_shards)

3. Primary Shard
   ├── 문서 저장 (In-memory Buffer)
   ├── Translog 기록 (장애 복구용)
   └── Replica에 복제 요청

4. Replica Shard
   └── 동일하게 저장

5. Refresh (기본 1초)
   └── In-memory Buffer → Segment (검색 가능)

6. Flush (주기적)
   └── Segment → Disk (영구 저장)
```

**Near Real-time의 의미**:
- 문서 저장 후 바로 검색 안 됨
- Refresh(기본 1초) 후 검색 가능
- `?refresh=true` 옵션으로 즉시 반영 가능 (성능 저하)

### 검색 과정

```
1. 클라이언트 → 검색 쿼리

2. Coordinating Node
   ├── 쿼리를 모든 샤드에 전송 (Query Phase)
   │   └── 각 샤드: 매칭 문서 ID + 스코어 반환
   │
   ├── 결과 병합 및 상위 N개 선정
   │
   └── 해당 샤드에 문서 요청 (Fetch Phase)
       └── 실제 문서 내용 반환

3. 클라이언트 ← 최종 결과
```

**Scatter-Gather 패턴**:
- 모든 샤드에 분산 요청(Scatter)
- 결과를 한 곳에서 수집(Gather)

## 예제 코드

### Spring Data Elasticsearch

```java
@Document(indexName = "products")
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
}
```

```java
@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    // 메서드 이름 기반 쿼리
    List<Product> findByName(String name);

    List<Product> findByPriceBetween(Integer min, Integer max);

    // @Query 어노테이션
    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}], \"filter\": [{\"term\": {\"category\": \"?1\"}}]}}")
    List<Product> searchByNameAndCategory(String name, String category);
}
```

```java
@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchHits<Product> search(String keyword, String category,
                                       Integer minPrice, Integer maxPrice) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 검색어 조건 (must)
        if (StringUtils.hasText(keyword)) {
            boolQuery.must(QueryBuilders.matchQuery("name", keyword));
        }

        // 필터 조건 (filter - 캐싱됨)
        if (StringUtils.hasText(category)) {
            boolQuery.filter(QueryBuilders.termQuery("category", category));
        }

        if (minPrice != null || maxPrice != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            if (minPrice != null) rangeQuery.gte(minPrice);
            if (maxPrice != null) rangeQuery.lte(maxPrice);
            boolQuery.filter(rangeQuery);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery)
            .withSort(SortBuilders.scoreSort())  // 스코어 순 정렬
            .withPageable(PageRequest.of(0, 20))
            .build();

        return elasticsearchOperations.search(searchQuery, Product.class);
    }
}
```

## 트레이드오프

### RDBMS vs Elasticsearch

| 항목 | RDBMS | Elasticsearch |
|------|-------|---------------|
| **전문 검색** | LIKE (느림) | 역인덱스 (빠름) |
| **트랜잭션** | ACID 완벽 지원 | 제한적 |
| **JOIN** | 강력한 JOIN 지원 | 기본 미지원 (Nested, Parent-Child) |
| **실시간성** | 즉시 반영 | Near Real-time (1초 지연) |
| **일관성** | 강한 일관성 | 최종 일관성 |
| **스키마** | 엄격한 스키마 | 유연한 스키마 |
| **용도** | 트랜잭션 처리 (OLTP) | 검색/분석 (OLAP) |

### 샤드 수 결정

| 샤드 수 | 장점 | 단점 |
|---------|------|------|
| **적음** | 오버헤드 감소, 관리 용이 | 확장성 제한 |
| **많음** | 병렬 처리 향상 | 오버헤드 증가, 검색 성능 저하 |

**권장 사항**:
- 샤드 1개당 10~50GB 권장
- 노드당 샤드 수 = 힙 메모리(GB) × 20 이하
- Primary 샤드 수는 변경 불가 → 초기 설계 중요

## 트러블슈팅

### 사례 1: 검색 결과 누락 (한글 검색)

#### 증상

"삼성전자" 검색 시 "삼성전자가", "삼성전자의" 문서가 검색되지 않음

#### 원인 분석

Standard Analyzer 사용 시 한글 조사가 분리되지 않음:
- 저장: "삼성전자가" → ["삼성전자가"]
- 검색: "삼성전자" → ["삼성전자"]
- 매칭 실패

#### 해결 방법

**Nori 한글 형태소 분석기 설정**:

```bash
PUT /products
{
  "settings": {
    "analysis": {
      "analyzer": {
        "korean": {
          "type": "custom",
          "tokenizer": "nori_tokenizer",
          "filter": ["lowercase", "nori_part_of_speech"]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": { "type": "text", "analyzer": "korean" }
    }
  }
}
```

#### 예방 조치

- 한글 데이터 인덱스 생성 시 반드시 Nori 분석기 설정
- `_analyze` API로 분석 결과 확인

```bash
GET /products/_analyze
{
  "analyzer": "korean",
  "text": "삼성전자가 반도체를 생산한다"
}
```

### 사례 2: 클러스터 상태 Yellow/Red

#### 증상

```bash
GET /_cluster/health

{"status": "yellow", "unassigned_shards": 5}
```

#### 원인 분석

**Yellow**: Replica 샤드 미할당
- 단일 노드에서 Replica를 같은 노드에 배치 불가
- Replica 설정이 노드 수보다 많음

**Red**: Primary 샤드 미할당
- 노드 장애로 Primary 샤드 유실
- 디스크 공간 부족

#### 해결 방법

**Yellow 해결 (개발 환경)**:

```bash
PUT /products/_settings
{
  "index": {
    "number_of_replicas": 0
  }
}
```

**Red 해결**:

```bash
# 미할당 샤드 확인
GET /_cluster/allocation/explain

# 디스크 공간 확인 후 정리
# 노드 추가 또는 인덱스 삭제
```

#### 예방 조치

- 운영 환경: 최소 3개 노드
- 디스크 사용률 모니터링 (85% 이하 유지)
- Replica 수 = 노드 수 - 1 이하

### 사례 3: 검색 속도 저하

#### 증상

평소 50ms 응답 → 2초 이상 소요

#### 원인 분석

1. **세그먼트 과다**: Refresh마다 세그먼트 생성 → 수백 개 누적
2. **힙 메모리 부족**: GC 빈번 발생
3. **무거운 쿼리**: Wildcard, Regex, Script 등

#### 해결 방법

**Force Merge (세그먼트 병합)**:

```bash
POST /products/_forcemerge?max_num_segments=1
```

**쿼리 최적화**:

```bash
# Bad: 와일드카드 검색 (느림)
{ "query": { "wildcard": { "name": "*스마트*" } } }

# Good: Match 검색 (빠름)
{ "query": { "match": { "name": "스마트" } } }
```

**필터 캐싱 활용**:

```bash
# 자주 사용하는 조건은 filter로 이동
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "category": "스마트폰" } },
        { "range": { "price": { "gte": 100000 } } }
      ]
    }
  }
}
```

#### 예방 조치

- 주기적 Force Merge (새벽 배치)
- 힙 메모리 모니터링 (노드 RAM의 50% 이하)
- Slow Query 로그 활성화

## 면접 예상 질문

### Q: RDBMS의 LIKE 검색과 Elasticsearch의 차이는?

A: **LIKE '%검색어%'**는 인덱스를 사용하지 못해 전체 테이블을 순차 스캔합니다. **왜냐하면** B-Tree 인덱스는 접두사(prefix) 매칭만 지원하기 때문입니다. 반면 **Elasticsearch**는 역인덱스(Inverted Index) 구조로 단어 → 문서 매핑을 미리 만들어두어, 검색 시 해당 단어의 Posting List만 조회하면 됩니다. 100만 건 테이블에서 LIKE는 100만 번 비교가 필요하지만, Elasticsearch는 이진 검색으로 log N 번만 조회합니다. **따라서** 대용량 전문 검색에서는 Elasticsearch가 수백 배 빠릅니다.

### Q: 역인덱스(Inverted Index)가 무엇인가요?

A: **역인덱스**는 단어를 키로, 해당 단어가 포함된 문서 목록을 값으로 저장하는 구조입니다. 일반 인덱스가 "문서 → 단어"라면, 역인덱스는 "단어 → 문서"로 **역방향**입니다. **예시**: "자바" → [문서1, 문서3], "스프링" → [문서2, 문서3]으로 저장하면, "자바 AND 스프링" 검색 시 두 리스트의 교집합(문서3)을 구하면 됩니다. **왜 빠른가?** Term이 정렬되어 있어 이진 검색이 가능하고, Posting List에서 문서 ID로 직접 접근하므로 O(log N + K) (K는 결과 수)입니다.

### Q: text 타입과 keyword 타입의 차이는?

A: **text**는 분석기(Analyzer)를 통해 토큰화되어 저장되고, **keyword**는 분석 없이 원본 그대로 저장됩니다. **text**: "삼성 갤럭시" → ["삼성", "갤럭시"]로 분리되어 부분 검색 가능. **keyword**: "삼성 갤럭시" 그대로 저장되어 정확히 일치해야 검색됨. **사용처**: text는 상품명, 본문 등 전문 검색용, keyword는 카테고리, 태그, 이메일 등 정확 매칭/정렬/집계용. **실무 팁**: 하나의 필드를 text와 keyword 모두로 매핑(Multi-fields)하여 유연하게 사용.

### Q: 샤드와 레플리카의 역할은?

A: **샤드(Shard)**는 인덱스를 분할한 조각으로, 데이터 분산 저장과 병렬 처리를 가능하게 합니다. **왜 필요한가?** 1TB 인덱스를 10개 샤드로 분할하면 10개 노드에 분산 저장하고, 검색 시 10개 샤드에서 동시에 처리하여 10배 빠릅니다. **레플리카(Replica)**는 샤드의 복제본으로, 고가용성과 읽기 부하 분산을 제공합니다. Primary 노드 장애 시 Replica가 승격하고, 검색 요청을 여러 Replica에서 처리하여 부하를 분산합니다. **주의**: Primary 샤드 수는 생성 후 변경 불가하므로 초기 설계가 중요합니다.

### Q: Elasticsearch는 실시간인가요?

A: **Near Real-time**입니다. 문서 인덱싱 후 약 1초(기본 refresh_interval) 후에 검색 가능합니다. **왜 즉시 검색이 안 되는가?** 성능을 위해 문서를 In-memory Buffer에 모아두었다가 주기적으로 Segment로 변환하기 때문입니다. Segment가 생성되어야 검색 가능합니다. **즉시 반영이 필요하면?** `?refresh=true` 옵션을 사용할 수 있지만, 빈번한 Refresh는 성능 저하와 세그먼트 과다를 유발합니다. **실무**: 대부분의 검색 서비스에서 1초 지연은 허용 가능하므로 기본값 유지를 권장합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [NoSQL](./nosql.md) | 선수 지식 - 분산 DB 개념 | Intermediate |
| [Index](./index.md) | 선수 지식 - B-Tree와 인덱스 | Intermediate |
| [ES 풀 색인 전략](./elasticsearch-reindexing.md) | 후속 학습 - Alias 스왑, 재색인 | Advanced |

## 참고 자료

- [Elasticsearch 공식 문서](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Elasticsearch: The Definitive Guide](https://www.elastic.co/guide/en/elasticsearch/guide/current/index.html)
- [Nori 형태소 분석기 문서](https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-nori.html)
- [Lucene 공식 문서](https://lucene.apache.org/core/)
- [Designing Data-Intensive Applications - Martin Kleppmann](https://dataintensive.net/)
