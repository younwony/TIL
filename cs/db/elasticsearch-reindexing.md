# Elasticsearch 풀 색인 전략: 덮어쓰기 vs Alias 스왑

> `[4] 심화` · 선수 지식: [NoSQL](./nosql.md), [Index](./index.md)

> Elasticsearch에서 전체 데이터를 다시 색인할 때 선택할 수 있는 두 가지 전략과, 각 방식의 디스크 용량 특성을 Lucene 내부 구조 관점에서 비교 분석

`#Elasticsearch` `#ES` `#엘라스틱서치` `#풀색인` `#FullReindex` `#Reindexing` `#재색인` `#Lucene` `#루씬` `#Segment` `#세그먼트` `#Alias` `#별칭` `#IndexSwap` `#인덱스스왑` `#ZeroDowntime` `#무중단배포` `#SoftDelete` `#삭제마킹` `#Merge` `#머지` `#ForceMerge` `#ImmutableSegment` `#불변세그먼트` `#디스크용량` `#DiskUsage` `#검색엔진` `#SearchEngine` `#NoSQL`

## 왜 알아야 하는가?

Elasticsearch를 운영하다 보면 매핑 변경, 분석기 교체, 데이터 정제 등의 이유로 **전체 색인(Full Reindexing)**이 필요한 상황이 자주 발생합니다.

- **실무**: "덮어쓰기 했더니 디스크가 터졌다"는 장애 사례가 빈번함
- **면접**: Lucene 세그먼트 구조와 불변성에 대한 이해도를 확인하는 질문
- **기반 지식**: 검색 엔진 내부 동작 원리를 이해해야 올바른 운영 전략 수립 가능

## 핵심 개념

- **덮어쓰기(In-place Update)**: 기존 인덱스에 동일 ID로 문서를 다시 색인
- **Alias 스왑**: 새 인덱스 생성 후 Alias를 교체하는 방식
- **Lucene 세그먼트**: 검색을 위한 불변(Immutable) 데이터 단위
- **Soft Delete**: 삭제 시 실제로 지우지 않고 마킹만 하는 방식
- **Merge**: 여러 세그먼트를 하나로 합치면서 삭제된 문서를 제거하는 과정

## 쉽게 이해하기

### 덮어쓰기 = 도서관 책 교체

도서관에서 **모든 책을 최신판으로 교체**한다고 가정합니다.

**덮어쓰기 방식**:
1. 기존 책을 서가에서 빼지 않음 (삭제 불가)
2. 대신 "폐기 예정" 스티커만 붙임 (삭제 마킹)
3. 새 책을 서가의 빈 공간에 추가
4. 결과: **기존 책 + 새 책**이 동시에 존재 → 공간 2배 필요
5. 나중에 정리 작업(Merge) 시 폐기 예정 책 제거

**Alias 스왑 방식**:
1. 새로운 서가(인덱스)를 통째로 준비
2. 새 서가에 최신 책만 배치
3. 안내 표지판(Alias)을 새 서가로 변경
4. 기존 서가는 확인 후 폐기
5. 결과: 서가 2개가 필요하지만, **용량 예측 가능**

### 왜 덮어쓰기가 더 위험한가?

```
덮어쓰기:
┌─────────────────────────────────────────────────┐
│  기존 데이터(삭제 마킹) + 새 데이터 + 머지 중간 파일  │
│              최대 2.5~3배 용량 피크!              │
└─────────────────────────────────────────────────┘

Alias 스왑:
┌─────────────────────────────────────────────────┐
│     기존 인덱스 + 새 인덱스 (명확히 2배)           │
│           용량 예측 가능, 통제 가능               │
└─────────────────────────────────────────────────┘
```

## 상세 설명

### Lucene 세그먼트 구조 이해

Elasticsearch는 내부적으로 **Apache Lucene**을 사용합니다. Lucene의 핵심은 **세그먼트(Segment)**입니다.

```
Elasticsearch Index
└── Shard (Lucene Index)
    ├── Segment 0 (불변)
    │   ├── .cfs (복합 파일)
    │   ├── .si  (세그먼트 정보)
    │   └── .liv (삭제 마킹 - 구버전: .del)
    ├── Segment 1 (불변)
    ├── Segment 2 (불변)
    └── ...
```

**세그먼트의 특징**:

| 특성 | 설명 |
|------|------|
| **불변성(Immutable)** | 한번 생성된 세그먼트는 수정 불가 |
| **추가 전용(Append-only)** | 새 문서는 항상 새 세그먼트에 기록 |
| **삭제 = 마킹** | 삭제 시 .liv 파일에 "삭제됨" 표시만 |
| **업데이트 = 삭제 + 추가** | 기존 문서 삭제 마킹 + 새 문서 추가 |

**왜 불변으로 설계했는가?**

1. **동시성 안전**: 락(Lock) 없이 읽기 가능
2. **캐시 효율**: OS 파일 시스템 캐시 활용 극대화
3. **검색 성능**: 세그먼트별 인덱스 구조 최적화
4. **복구 용이**: 불변 파일이므로 손상 시 복구 단순

### 덮어쓰기 시 디스크 용량 피크 발생 원리

**시나리오**: 100GB 인덱스의 모든 문서를 동일 ID로 재색인

#### Step 1: 재색인 시작

```
기존 세그먼트 (100GB)
├── Segment A: doc1, doc2, doc3...
├── Segment B: doc101, doc102...
└── Segment C: doc201, doc202...

새 세그먼트 생성 시작
└── Segment D: doc1(new), doc2(new)... (점점 증가)
```

#### Step 2: 삭제 마킹 (Soft Delete)

동일 ID 문서가 들어오면 **기존 문서는 삭제 마킹**됩니다.

```
기존 세그먼트 (여전히 100GB 차지!)
├── Segment A: [del]doc1, [del]doc2, [del]doc3...
├── Segment B: [del]doc101, [del]doc102...
└── Segment C: [del]doc201, [del]doc202...

+ 새 세그먼트 (100GB 추가)
└── Segment D: doc1(new), doc2(new), doc3(new)...
```

**이 시점 디스크 용량: ~200GB** (기존 100GB + 신규 100GB)

**왜 기존 데이터가 삭제되지 않는가?**

세그먼트는 **불변**이므로 물리적 삭제가 불가능합니다. `.liv` 파일에 비트마스크로 삭제 여부만 기록합니다.

```
.liv 파일 구조 (비트마스크)
┌─────────────────────────────────┐
│ doc0: 0 (삭제됨)                │
│ doc1: 0 (삭제됨)                │
│ doc2: 1 (유효)                  │
│ doc3: 0 (삭제됨)                │
│ ...                             │
└─────────────────────────────────┘
```

#### Step 3: Merge 프로세스 (가장 위험한 구간)

Elasticsearch는 백그라운드에서 **세그먼트 머지**를 수행합니다.

```
머지 전:
├── Segment A (30GB, 90% 삭제 마킹)
├── Segment B (30GB, 90% 삭제 마킹)
├── Segment C (40GB, 90% 삭제 마킹)
└── Segment D (100GB, 신규 데이터)
총: 200GB

머지 중: (!)
├── Segment A (30GB) ─┐
├── Segment B (30GB) ─┼─→ Segment E (100GB, 작성 중)
├── Segment C (40GB) ─┘
└── Segment D (100GB)
총: 300GB (피크!)

머지 후:
├── Segment E (100GB, 유효 데이터만)
└── Segment D (100GB)
총: 200GB → 이후 D+E 머지 시 최종 100GB
```

**디스크 용량 변화 그래프**:

```
용량
300GB ┤           *** (머지 피크)
      │         **   **
200GB ┤       **       **
      │     **           **
100GB ┤ ****               ****
      │
      └────────────────────────── 시간
        시작   재색인    머지    완료
```

#### 용량 피크의 수학적 분석

| 단계 | 용량 | 설명 |
|------|------|------|
| 초기 | N | 원본 데이터 |
| 재색인 중 | ~2N | 원본(삭제마킹) + 신규 |
| 머지 피크 | ~2.5N~3N | 원본 + 신규 + 머지 중간 파일 |
| 머지 완료 | ~2N | 머지된 세그먼트 + 미처리 세그먼트 |
| 최종 | ~N | 모든 머지 완료 후 |

**왜 3배까지 치솟는가?**

1. **기존 데이터**: 삭제 마킹만 되고 물리적으로 존재 (N)
2. **신규 데이터**: 새 세그먼트에 기록 (N)
3. **머지 중간 파일**: 새 세그먼트 작성 중 (최대 N)
4. 머지가 완료되어야 기존 세그먼트 삭제 가능

### Alias 스왑 방식

**프로세스**:

```
1. 새 인덱스 생성
   products_v2 (빈 상태)

2. 새 인덱스에 색인
   products_v2 ← 전체 데이터 색인

3. Alias 스왑 (Atomic)
   products_alias: products_v1 → products_v2

4. 기존 인덱스 삭제
   DELETE products_v1
```

**Alias 스왑 API**:

```json
POST /_aliases
{
  "actions": [
    { "remove": { "index": "products_v1", "alias": "products" } },
    { "add":    { "index": "products_v2", "alias": "products" } }
  ]
}
```

**특징**:
- **Atomic**: 두 동작이 원자적으로 수행 (중간 상태 없음)
- **Zero Downtime**: 애플리케이션은 Alias만 바라보므로 무중단
- **Rollback 용이**: 문제 시 Alias를 다시 v1으로 변경

**디스크 용량 변화**:

```
용량
200GB ┤ **********************
      │
100GB ┤                       ****
      │
      └────────────────────────── 시간
        v1존재   v2색인    v1삭제
```

- 최대 용량: 정확히 2N (예측 가능)
- 머지 피크 없음: 새 인덱스는 최적화된 상태로 시작

## 동작 원리

### 덮어쓰기 상세 프로세스

```
┌─────────────────────────────────────────────────────────────┐
│                    덮어쓰기 프로세스                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. 문서 도착 (doc_id: 123, version: 2)                     │
│     │                                                       │
│     ▼                                                       │
│  2. 기존 문서 검색 (doc_id: 123, version: 1)                │
│     │                                                       │
│     ├─→ 있음: Segment A의 .liv 파일에 삭제 마킹             │
│     │                                                       │
│     ▼                                                       │
│  3. 새 문서를 In-Memory Buffer에 추가                       │
│     │                                                       │
│     ▼                                                       │
│  4. Refresh (기본 1초): Buffer → 새 Segment                 │
│     │                                                       │
│     ▼                                                       │
│  5. Flush: Translog → Disk (영구 저장)                      │
│     │                                                       │
│     ▼                                                       │
│  6. Merge (비동기): 여러 Segment → 하나로 병합              │
│     └─→ 이때 삭제 마킹된 문서 물리적 제거                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Alias 스왑 상세 프로세스

```
┌─────────────────────────────────────────────────────────────┐
│                   Alias 스왑 프로세스                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  [현재 상태]                                                │
│  Application → products (alias) → products_v1 (index)      │
│                                                             │
│  1. 새 인덱스 생성                                          │
│     PUT /products_v2 (매핑, 설정 포함)                      │
│     │                                                       │
│     ▼                                                       │
│  2. 새 인덱스에 색인 (Bulk API 권장)                        │
│     POST /products_v2/_bulk                                 │
│     │                                                       │
│     ▼                                                       │
│  3. 색인 완료 확인                                          │
│     GET /products_v2/_count                                 │
│     │                                                       │
│     ▼                                                       │
│  4. Alias 원자적 스왑                                       │
│     POST /_aliases { remove + add }                         │
│     │                                                       │
│     ▼                                                       │
│  5. 기존 인덱스 삭제 (선택적, 확인 후)                      │
│     DELETE /products_v1                                     │
│                                                             │
│  [최종 상태]                                                │
│  Application → products (alias) → products_v2 (index)      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 예제 코드

### 덮어쓰기 방식 (권장하지 않음)

```java
// 기존 인덱스에 직접 색인 (같은 ID로 덮어쓰기)
public void reindexInPlace(List<Product> products) {
    BulkRequest bulkRequest = new BulkRequest();

    for (Product product : products) {
        IndexRequest request = new IndexRequest("products")
            .id(product.getId())  // 동일 ID → 덮어쓰기
            .source(objectMapper.writeValueAsString(product), XContentType.JSON);
        bulkRequest.add(request);
    }

    // 문제: 기존 문서는 삭제 마킹만 됨 → 디스크 용량 증가
    client.bulk(bulkRequest, RequestOptions.DEFAULT);
}
```

### Alias 스왑 방식 (권장)

```java
public class ElasticsearchReindexService {

    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    /**
     * Alias 스왑을 통한 무중단 풀 색인
     */
    public void reindexWithAliasSwap(String aliasName, List<Product> products)
            throws IOException {

        // 1. 현재 Alias가 가리키는 인덱스 확인
        String currentIndex = getCurrentIndexFromAlias(aliasName);
        String newIndex = generateNewIndexName(aliasName);

        // 2. 새 인덱스 생성 (매핑 복사)
        createNewIndex(newIndex, currentIndex);

        // 3. 새 인덱스에 벌크 색인
        bulkIndexToNewIndex(newIndex, products);

        // 4. 색인 완료 확인
        refreshAndVerify(newIndex, products.size());

        // 5. Alias 원자적 스왑
        swapAlias(aliasName, currentIndex, newIndex);

        // 6. 기존 인덱스 삭제 (선택적)
        deleteOldIndex(currentIndex);
    }

    private String generateNewIndexName(String aliasName) {
        return aliasName + "_" + System.currentTimeMillis();
    }

    private void createNewIndex(String newIndex, String sourceIndex)
            throws IOException {
        // 기존 인덱스의 매핑과 설정 복사
        GetIndexRequest getRequest = new GetIndexRequest(sourceIndex);
        GetIndexResponse response = client.indices().get(getRequest, RequestOptions.DEFAULT);

        Map<String, Object> settings = response.getSettings().get(sourceIndex).getAsStructuredMap();
        Map<String, Object> mappings = response.getMappings().get(sourceIndex).getSourceAsMap();

        // 새 인덱스 생성
        CreateIndexRequest createRequest = new CreateIndexRequest(newIndex);
        createRequest.settings(filterSettings(settings));
        createRequest.mapping(mappings);

        client.indices().create(createRequest, RequestOptions.DEFAULT);
    }

    private void bulkIndexToNewIndex(String indexName, List<Product> products)
            throws IOException {
        int batchSize = 1000;

        for (int i = 0; i < products.size(); i += batchSize) {
            List<Product> batch = products.subList(
                i, Math.min(i + batchSize, products.size()));

            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.NONE);

            for (Product product : batch) {
                IndexRequest request = new IndexRequest(indexName)
                    .id(product.getId())
                    .source(objectMapper.writeValueAsString(product), XContentType.JSON);
                bulkRequest.add(request);
            }

            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.hasFailures()) {
                throw new RuntimeException("Bulk indexing failed: " +
                    response.buildFailureMessage());
            }
        }
    }

    private void swapAlias(String aliasName, String oldIndex, String newIndex)
            throws IOException {
        // 원자적 Alias 스왑
        IndicesAliasesRequest request = new IndicesAliasesRequest();

        // 기존 인덱스에서 Alias 제거
        IndicesAliasesRequest.AliasActions removeAction =
            new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.REMOVE)
                .index(oldIndex)
                .alias(aliasName);

        // 새 인덱스에 Alias 추가
        IndicesAliasesRequest.AliasActions addAction =
            new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.ADD)
                .index(newIndex)
                .alias(aliasName);

        request.addAliasAction(removeAction);
        request.addAliasAction(addAction);

        // 두 동작이 원자적으로 수행됨
        client.indices().updateAliases(request, RequestOptions.DEFAULT);
    }
}
```

## 트레이드오프

| 구분 | 덮어쓰기 (In-place) | Alias 스왑 |
|------|---------------------|------------|
| **디스크 피크** | 2.5~3배 (예측 어려움) | 2배 (예측 가능) |
| **운영 복잡도** | 단순 (한 인덱스만 관리) | 중간 (인덱스 버전 관리 필요) |
| **무중단 여부** | △ (머지 중 성능 저하) | ○ (완전 무중단) |
| **롤백 용이성** | ✗ (이전 상태 복구 어려움) | ○ (Alias만 변경) |
| **머지 부하** | 높음 (삭제 마킹 다수) | 낮음 (깨끗한 인덱스) |
| **색인 중 검색** | 섞인 결과 (신/구 혼재) | 일관된 결과 (스왑 전까지) |
| **적합한 상황** | 소규모, 증분 업데이트 | 대규모, 전체 재색인 |

### 덮어쓰기가 적합한 경우

- 일부 문서만 업데이트 (전체의 10% 미만)
- 디스크 공간이 충분함
- 실시간 반영이 필요함

### Alias 스왑이 적합한 경우

- 전체 또는 대부분의 문서 재색인
- 디스크 공간이 제한적
- 무중단 배포 필수
- 롤백 가능해야 함

## 트러블슈팅

### 사례 1: 덮어쓰기 중 디스크 풀(Disk Full)

#### 증상

```
[ERROR] ClusterBlockException: blocked by: [FORBIDDEN/12/index read-only /
allow delete (api)];
```

#### 원인 분석

1. 풀 색인 시작 (100GB 인덱스)
2. 삭제 마킹된 기존 데이터 + 신규 데이터 = 200GB
3. 머지 프로세스 시작 → 추가 50~100GB 필요
4. 디스크 사용률 95% 초과 → ES가 인덱스를 읽기 전용으로 전환

#### 해결 방법

```bash
# 1. 읽기 전용 해제
PUT /products/_settings
{
  "index.blocks.read_only_allow_delete": null
}

# 2. 불필요한 데이터 삭제 또는 디스크 추가

# 3. 향후: Alias 스왑 방식으로 전환
```

#### 예방 조치

- 풀 색인 전 디스크 여유 공간 3배 확보
- `cluster.routing.allocation.disk.watermark` 설정 확인
- 모니터링 알람 설정 (디스크 70% 초과 시)

### 사례 2: 머지 중 검색 성능 저하

#### 증상

- 검색 레이턴시 2~5배 증가
- CPU 사용률 급증

#### 원인 분석

머지 프로세스가 I/O와 CPU를 집중적으로 사용하여 검색 성능에 영향

#### 해결 방법

```json
// 머지 스로틀링 설정
PUT /_cluster/settings
{
  "persistent": {
    "indices.store.throttle.max_bytes_per_sec": "50mb"
  }
}

// 피크 시간 외에 Force Merge 수행
POST /products/_forcemerge?max_num_segments=1&only_expunge_deletes=true
```

## 면접 예상 질문

### Q: Elasticsearch에서 문서를 업데이트하면 내부적으로 어떤 일이 일어나나요?

A: Elasticsearch는 Lucene 기반이며, Lucene 세그먼트는 **불변(Immutable)**입니다. 따라서 업데이트는 실제로 **삭제 + 추가** 두 단계로 처리됩니다.

1. 기존 문서가 있는 세그먼트의 `.liv` 파일에 **삭제 마킹**
2. 새 문서를 **새 세그먼트**에 추가

**왜 불변으로 설계했는가?**
- 동시성: 락 없이 읽기 가능
- 캐시: OS 파일 시스템 캐시 효율 극대화
- 성능: 세그먼트별 최적화된 검색 구조

**결과적으로** 업데이트가 많으면 삭제 마킹된 "죽은" 문서가 누적되어 디스크 사용량이 증가하고, Merge가 필요해집니다.

### Q: 풀 색인 시 덮어쓰기 방식의 문제점은 무엇인가요?

A: 덮어쓰기 방식은 **디스크 용량 피크**와 **검색 품질 저하**라는 두 가지 주요 문제가 있습니다.

**디스크 용량 피크**:
1. 기존 문서: 삭제 마킹만 되고 물리적으로 존재 (N)
2. 신규 문서: 새 세그먼트에 기록 (N)
3. 머지 중간 파일: 최대 N 추가
4. 결과: 최대 **2.5~3배** 용량 필요

**검색 품질 저하**:
- 색인 중 신/구 문서가 섞여 결과에 노출
- 머지 중 I/O 부하로 검색 지연 증가

**대안**: Alias 스왑 방식은 용량이 정확히 2배로 예측 가능하고, 스왑 전까지 검색 결과가 일관됩니다.

### Q: Force Merge는 언제 사용해야 하나요?

A: Force Merge는 세그먼트를 강제로 병합하여 삭제 마킹된 문서를 물리적으로 제거합니다.

**사용 시점**:
- 더 이상 쓰기가 없는 인덱스 (예: 시계열 데이터의 과거 인덱스)
- 풀 색인 완료 후 정리
- 검색 성능 최적화 필요 시

**주의사항**:
- I/O 집중적 → 피크 시간 피해서 실행
- `max_num_segments=1`은 모든 세그먼트를 하나로 → 매우 오래 걸림
- 쓰기가 계속되는 인덱스에서는 비권장

```bash
# 삭제된 문서만 정리 (권장)
POST /products/_forcemerge?only_expunge_deletes=true

# 세그먼트 수 제한 (주의: 오래 걸림)
POST /products/_forcemerge?max_num_segments=5
```

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [NoSQL](./nosql.md) | Elasticsearch의 NoSQL 특성 | [4] 심화 |
| [Index](./index.md) | 인덱스의 기본 개념 | [3] 중급 |
| [분산 트랜잭션](../system-design/distributed-transaction.md) | ES 클러스터 분산 처리 | [4] 심화 |

## 참고 자료

- [Elasticsearch 공식 문서 - Index Aliases](https://www.elastic.co/guide/en/elasticsearch/reference/current/aliases.html)
- [Elasticsearch 공식 문서 - Force Merge](https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-forcemerge.html)
- [Lucene 세그먼트 구조 - Apache Lucene](https://lucene.apache.org/core/9_0_0/core/org/apache/lucene/codecs/lucene90/package-summary.html)
- [Elasticsearch: The Definitive Guide - Index Management](https://www.elastic.co/guide/en/elasticsearch/guide/current/index-management.html)
