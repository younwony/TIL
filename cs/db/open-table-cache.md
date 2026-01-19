# open_table_cache

> `[3] 중급` · 선수 지식: [파일 디스크립터](../os/file-descriptor.md), [MySQL 인덱스](./mysql-index.md)

> MySQL이 열어둔 테이블 핸들(파일 디스크립터)을 캐싱하여 테이블 오픈 오버헤드를 줄이는 시스템 변수

`#open_table_cache` `#테이블캐시` `#TableCache` `#MySQL` `#MariaDB` `#파일디스크립터` `#FileDescriptor` `#table_open_cache` `#table_definition_cache` `#table_open_cache_instances` `#Opened_tables` `#테이블핸들` `#TableHandle` `#성능튜닝` `#MySQLTuning` `#메타데이터` `#frm파일` `#ibd파일` `#InnoDB` `#MyISAM` `#커넥션` `#동시접속` `#캐시히트` `#CacheHit` `#flush_tables` `#open_files_limit` `#ulimit` `#DBA` `#운영` `#모니터링`

## 왜 알아야 하는가?

MySQL에서 쿼리를 실행하려면 먼저 테이블을 "열어야" 합니다. 이 과정은 파일 시스템 접근이 필요하며, 동시 접속이 많은 환경에서는 심각한 병목이 될 수 있습니다.

- **실무**: 동시 접속이 많은 서비스에서 `Opened_tables` 수치가 급증하면 성능 저하의 원인이 됩니다
- **면접**: MySQL 성능 튜닝, 커넥션 관리와 연계된 질문에서 자주 등장합니다
- **기반 지식**: 파일 디스크립터, OS 자원 관리와 DB의 관계를 이해하는 핵심 개념입니다

## 핵심 개념

- **테이블 핸들**: 테이블을 열 때 생성되는 파일 디스크립터와 메타데이터의 조합
- **open_table_cache**: 열린 테이블 핸들을 저장하는 캐시 공간
- **커넥션별 테이블 오픈**: 같은 테이블이라도 커넥션마다 별도로 열어야 함

## 쉽게 이해하기

**도서관 비유**

도서관에서 책을 읽으려면:
1. 서고에서 책을 찾아온다 (파일 시스템 접근)
2. 열람석에 책을 펼쳐놓는다 (테이블 오픈)
3. 읽고 나면 다시 서고에 반납한다 (테이블 클로즈)

매번 서고를 왕복하면 시간이 오래 걸립니다.

**open_table_cache**는 자주 읽는 책을 열람석 근처 **임시 보관대**에 두는 것과 같습니다:
- 다른 사람이 같은 책을 요청하면 서고까지 가지 않고 바로 제공
- 보관대가 가득 차면 오래된 책부터 서고로 반납
- 보관대 크기가 작으면 자주 서고를 왕복해야 함

## 상세 설명

### 테이블을 여는 과정

MySQL에서 쿼리가 테이블에 접근할 때:

```
1. 테이블 정의(메타데이터) 확인
2. 테이블 파일 오픈 (파일 디스크립터 할당)
3. 테이블 핸들 생성
4. 쿼리 실행
5. 테이블 핸들 해제 (캐시에 반환 또는 클로즈)
```

**왜 이렇게 하는가?**

MySQL은 테이블당 여러 파일을 사용합니다:
- `.frm`: 테이블 구조 정의 (MySQL 8.0 이전)
- `.ibd`: InnoDB 데이터/인덱스 파일
- `.MYD`, `.MYI`: MyISAM 데이터/인덱스 파일

파일을 열 때마다 OS의 `open()` 시스템 콜이 발생하며, 이는 비용이 큰 작업입니다.

### open_table_cache의 역할

```
┌─────────────────────────────────────────────────────────────┐
│                      MySQL Server                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Connection 1 ──┐                                            │
│  Connection 2 ──┼──▶ ┌─────────────────────────────┐        │
│  Connection 3 ──┤    │     open_table_cache        │        │
│  Connection N ──┘    │  ┌───────┬───────┬───────┐  │        │
│                      │  │users  │orders │products│ │        │
│                      │  │handle │handle │handle  │ │        │
│                      │  └───────┴───────┴───────┘  │        │
│                      └──────────────┬──────────────┘        │
│                                     │                        │
│                                     ▼                        │
│                      ┌─────────────────────────────┐        │
│                      │    파일 시스템 (OS)          │        │
│                      │  .ibd, .frm, .MYD, .MYI     │        │
│                      └─────────────────────────────┘        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 주요 시스템 변수

| 변수 | 설명 | 기본값 |
|------|------|--------|
| `table_open_cache` | 열린 테이블 핸들 캐시 크기 | 4000 (MySQL 8.0) |
| `table_open_cache_instances` | 캐시 파티션 수 (락 경합 감소) | 16 |
| `table_definition_cache` | 테이블 정의(.frm) 캐시 크기 | 자동 계산 |
| `open_files_limit` | OS에서 허용하는 최대 파일 수 | 시스템 설정 |

**왜 이렇게 하는가?**

- `table_open_cache_instances`: 단일 캐시는 락 경합이 발생하므로 여러 파티션으로 분리
- `table_definition_cache`: 테이블 구조 정보는 공유 가능하므로 별도 캐시

### table_open_cache_instances 상세

`table_open_cache`를 **여러 개의 독립적인 파티션으로 분할**하는 설정입니다.

#### 단일 캐시의 문제점

```
┌─────────────────────────────────────────────────────────────┐
│            table_open_cache_instances = 1 (단일)             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   Connection 1 ──┐                                           │
│   Connection 2 ──┼──▶  ┌─────────────────────┐              │
│   Connection 3 ──┤     │  table_open_cache   │              │
│   Connection 4 ──┤     │                     │              │
│   Connection 5 ──┘     │   🔒 단일 Mutex     │ ◀── 병목!    │
│         ⋮              │                     │              │
│   Connection N ──────▶ └─────────────────────┘              │
│                                                              │
│   ⚠️ 모든 커넥션이 하나의 락을 두고 경쟁                      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

테이블을 열거나 닫을 때마다 캐시에 접근해야 하는데, 단일 뮤텍스로 보호되면 **동시에 한 커넥션만** 캐시 작업이 가능합니다.

#### 파티션 분할로 해결

```
┌─────────────────────────────────────────────────────────────┐
│            table_open_cache_instances = 4 (분할)             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   Connection 1 ──▶ ┌──────────┐                             │
│   Connection 5 ──▶ │ 파티션 0 │ 🔒 Mutex 0                  │
│   Connection 9 ──▶ └──────────┘                             │
│                                                              │
│   Connection 2 ──▶ ┌──────────┐                             │
│   Connection 6 ──▶ │ 파티션 1 │ 🔒 Mutex 1                  │
│   Connection 10 ─▶ └──────────┘                             │
│                                                              │
│   Connection 3 ──▶ ┌──────────┐                             │
│   Connection 7 ──▶ │ 파티션 2 │ 🔒 Mutex 2                  │
│   Connection 11 ─▶ └──────────┘                             │
│                                                              │
│   Connection 4 ──▶ ┌──────────┐                             │
│   Connection 8 ──▶ │ 파티션 3 │ 🔒 Mutex 3                  │
│   Connection 12 ─▶ └──────────┘                             │
│                                                              │
│   ✅ 4개의 커넥션이 동시에 캐시 작업 가능                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

#### 파티션 할당 방식

```
파티션 번호 = thread_id % table_open_cache_instances
```

```
예: instances = 4일 때

Thread ID 101 → 101 % 4 = 1 → 파티션 1
Thread ID 102 → 102 % 4 = 2 → 파티션 2
Thread ID 103 → 103 % 4 = 3 → 파티션 3
Thread ID 104 → 104 % 4 = 0 → 파티션 0
```

#### 각 파티션의 크기

```
파티션별 캐시 크기 = table_open_cache / table_open_cache_instances
```

```
예: table_open_cache = 8000, instances = 16

각 파티션 크기 = 8000 / 16 = 500개 테이블 핸들
```

#### 효과가 큰 경우 vs 적은 경우

| 효과가 큰 경우 | 효과가 적은 경우 |
|---------------|-----------------|
| 동시 접속 100개 이상 | 동시 접속 10개 미만 |
| 짧은 쿼리 대량 실행 (OLTP) | 긴 쿼리 위주 (OLAP) |
| CPU 코어가 많음 | 단일 코어 환경 |

#### 권장 설정

| 환경 | 동시 접속 | 권장 instances |
|------|----------|----------------|
| 소규모 | < 50 | 4 |
| 중규모 | 50 ~ 200 | 8 |
| 대규모 | 200 ~ 500 | 16 |
| 초대규모 | 500+ | 16 ~ 32 |

### 캐시 크기 계산

```
필요한 테이블 핸들 수 ≈ max_connections × 평균 테이블 사용 수
```

예시:
- `max_connections` = 500
- 평균적으로 쿼리당 3개 테이블 조인
- 필요량 ≈ 500 × 3 = 1,500 (최소)
- 여유를 두고 `table_open_cache = 2000~4000` 권장

## 동작 원리

```
┌──────────────────────────────────────────────────────────────┐
│                    테이블 오픈 과정                            │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│   쿼리 요청                                                    │
│       │                                                       │
│       ▼                                                       │
│   ┌───────────────────┐                                      │
│   │ open_table_cache  │                                      │
│   │ 에서 핸들 검색     │                                      │
│   └─────────┬─────────┘                                      │
│             │                                                 │
│       ┌─────┴─────┐                                          │
│       │           │                                          │
│    Cache Hit   Cache Miss                                    │
│       │           │                                          │
│       ▼           ▼                                          │
│   핸들 재사용   테이블 오픈                                    │
│       │           │                                          │
│       │           ├──▶ 캐시 공간 있음 → 캐시에 추가           │
│       │           │                                          │
│       │           └──▶ 캐시 가득 참 → LRU로 오래된 핸들 제거  │
│       │                              → 새 핸들 추가           │
│       │                                                       │
│       └───────────────────────────────────────────┐          │
│                                                   ▼          │
│                                              쿼리 실행        │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

## 예제 코드

### 현재 상태 확인

```sql
-- 테이블 캐시 설정 확인
SHOW VARIABLES LIKE 'table_open_cache%';
+----------------------------+-------+
| Variable_name              | Value |
+----------------------------+-------+
| table_open_cache           | 4000  |
| table_open_cache_instances | 16    |
+----------------------------+-------+

-- 테이블 정의 캐시 확인
SHOW VARIABLES LIKE 'table_definition_cache';
+------------------------+-------+
| Variable_name          | Value |
+------------------------+-------+
| table_definition_cache | 2000  |
+------------------------+-------+

-- OS 파일 제한 확인
SHOW VARIABLES LIKE 'open_files_limit';
+------------------+-------+
| Variable_name    | Value |
+------------------+-------+
| open_files_limit | 65535 |
+------------------+-------+
```

### 캐시 효율 모니터링

```sql
-- 테이블 오픈 관련 상태 확인
SHOW GLOBAL STATUS LIKE 'Open%tables%';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Open_tables   | 1234  |  -- 현재 열린 테이블 수
| Opened_tables | 56789 |  -- 누적 테이블 오픈 횟수 (서버 시작 이후)
+---------------+-------+

-- 캐시 미스율 계산
-- Opened_tables가 빠르게 증가하면 캐시 크기 부족
```

### 캐시 효율 분석 쿼리

```sql
-- 캐시 히트율 확인 (높을수록 좋음)
SELECT
    @@table_open_cache AS cache_size,
    (SELECT VARIABLE_VALUE FROM performance_schema.global_status
     WHERE VARIABLE_NAME = 'Open_tables') AS open_tables,
    (SELECT VARIABLE_VALUE FROM performance_schema.global_status
     WHERE VARIABLE_NAME = 'Opened_tables') AS opened_tables,
    ROUND(
        (1 - (SELECT VARIABLE_VALUE FROM performance_schema.global_status
              WHERE VARIABLE_NAME = 'Opened_tables') /
             GREATEST((SELECT VARIABLE_VALUE FROM performance_schema.global_status
                       WHERE VARIABLE_NAME = 'Open_tables'), 1)) * 100, 2
    ) AS estimated_efficiency;
```

### 설정 변경

```sql
-- 동적으로 변경 (재시작 불필요)
SET GLOBAL table_open_cache = 8000;
SET GLOBAL table_open_cache_instances = 16;

-- 영구 설정 (my.cnf 또는 my.ini)
-- [mysqld]
-- table_open_cache = 8000
-- table_open_cache_instances = 16
```

### 캐시 초기화

```sql
-- 모든 테이블 캐시 비우기 (운영 중 주의)
FLUSH TABLES;

-- 특정 테이블만 캐시에서 제거
FLUSH TABLES users, orders;
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 테이블 오픈 오버헤드 감소 | 메모리 사용량 증가 |
| 파일 디스크립터 재사용 | 너무 크면 메모리 낭비 |
| 락 경합 감소 (instances 사용 시) | 설정 변경 시 FLUSH 필요할 수 있음 |

## 트러블슈팅

### 사례 1: Opened_tables가 지속적으로 증가

#### 증상

```sql
-- 1분 간격으로 확인했을 때 Opened_tables가 계속 증가
SHOW GLOBAL STATUS LIKE 'Opened_tables';
-- 10:00 → 50000
-- 10:01 → 50500
-- 10:02 → 51100
```

#### 원인 분석

- `table_open_cache`가 너무 작아서 캐시 미스 발생
- 동시 접속 수 × 쿼리당 테이블 수 > 캐시 크기

#### 해결 방법

```sql
-- 1. 현재 설정 확인
SHOW VARIABLES LIKE 'table_open_cache';

-- 2. 동시 접속 수 확인
SHOW STATUS LIKE 'Max_used_connections';

-- 3. 캐시 크기 증가
SET GLOBAL table_open_cache = 8000;

-- 4. 효과 확인 (Opened_tables 증가 속도 감소해야 함)
```

#### 예방 조치

```sql
-- 모니터링 쿼리 (1분 간격으로 실행)
SELECT
    NOW() as check_time,
    VARIABLE_VALUE as opened_tables
FROM performance_schema.global_status
WHERE VARIABLE_NAME = 'Opened_tables';

-- 알람 설정: 분당 증가량이 100 이상이면 경고
```

### 사례 2: "Too many open files" 에러

#### 증상

```
[ERROR] Could not open file './database/table.ibd': Too many open files
```

#### 원인 분석

- `table_open_cache` + 기타 파일 > OS의 `open_files_limit`
- MySQL이 열 수 있는 파일 수 초과

#### 해결 방법

```shell
# 1. OS 레벨 파일 제한 확인
ulimit -n

# 2. MySQL의 파일 제한 확인
mysql> SHOW VARIABLES LIKE 'open_files_limit';

# 3. /etc/security/limits.conf 수정
mysql soft nofile 65535
mysql hard nofile 65535

# 4. systemd 서비스 파일 수정 (필요 시)
# /etc/systemd/system/mysqld.service.d/limits.conf
# [Service]
# LimitNOFILE=65535

# 5. MySQL 재시작
sudo systemctl restart mysqld
```

#### 예방 조치

```
table_open_cache + table_definition_cache + 버퍼 ≤ open_files_limit × 0.8
```

### 사례 3: 테이블 캐시 락 경합

#### 증상

```sql
-- 대기 이벤트에서 table cache 관련 대기 발견
SELECT * FROM performance_schema.events_waits_summary_global_by_event_name
WHERE EVENT_NAME LIKE '%table_cache%';
```

#### 원인 분석

- `table_open_cache_instances = 1`인 경우 단일 뮤텍스에서 경합

#### 해결 방법

```sql
-- 인스턴스 수 증가 (CPU 코어 수에 맞춤)
SET GLOBAL table_open_cache_instances = 16;
```

## 면접 예상 질문

### Q: open_table_cache와 table_definition_cache의 차이는 무엇인가요?

A: `open_table_cache`는 **테이블 핸들**(파일 디스크립터 + 메타데이터)을 캐싱하며, 커넥션마다 별도로 필요합니다. 반면 `table_definition_cache`는 **테이블 정의 정보**(.frm 내용)만 캐싱하며, 모든 커넥션이 공유합니다. 테이블 정의는 한 번 읽으면 변경되지 않지만, 핸들은 커넥션별로 독립적인 파일 포인터가 필요하기 때문입니다.

### Q: Opened_tables 값이 계속 증가하면 어떤 문제가 있는 것인가요?

A: `Opened_tables`가 빠르게 증가하면 테이블 캐시 미스가 자주 발생한다는 의미입니다. 매번 파일을 새로 열어야 하므로 시스템 콜 오버헤드가 증가하고, 응답 시간이 느려집니다. `table_open_cache` 값을 늘려서 해결할 수 있으며, 적정 값은 `max_connections × 평균 조인 테이블 수`를 기준으로 산정합니다.

### Q: table_open_cache_instances는 왜 필요한가요?

A: 단일 테이블 캐시는 하나의 뮤텍스로 보호되므로, 동시 접속이 많으면 락 경합이 발생합니다. `table_open_cache_instances`를 늘리면 캐시를 여러 파티션으로 분리하여 락 경합을 줄일 수 있습니다. 일반적으로 8~16 정도가 적절하며, CPU 코어 수를 고려하여 설정합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [파일 디스크립터](../os/file-descriptor.md) | 선수 지식 - OS 레벨 파일 핸들 | Intermediate |
| [MySQL 인덱스](./mysql-index.md) | 선수 지식 - 테이블 구조 이해 | Advanced |
| [실행 계획](./execution-plan.md) | 관련 개념 - 쿼리 성능 분석 | Advanced |
| [커넥션 풀](../system-design/connection-pool.md) | 관련 개념 - 커넥션 관리 | Intermediate |

## 참고 자료

- [MySQL 8.0 Reference Manual - table_open_cache](https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_table_open_cache)
- [MySQL 8.0 Reference Manual - How MySQL Opens and Closes Tables](https://dev.mysql.com/doc/refman/8.0/en/table-cache.html)
- [Percona - MySQL Table Cache](https://www.percona.com/blog/mysql-table-cache/)
