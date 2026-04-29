---
description: SQL 쿼리 튜닝 — EXPLAIN 기반 실행 계획 분석 및 개선안 제시
allowed-tools: Bash, Read
argument-hint: "<튜닝할 SELECT 쿼리>"
---

# DB Tune

$ARGUMENTS

위 쿼리의 실행 계획을 분석해 튜닝 포인트를 제시한다. 접속 정보는 `.claude/skills/prod-db-inspect/references/connection.md` 사용 (db-inspect와 동일 DB).

## 동작

1. 입력 쿼리 앞에 `EXPLAIN` 붙여 전통 포맷 조회 → 요약 테이블
2. `EXPLAIN FORMAT=JSON` 로 cost/row 상세 조회
3. 필요 시 `SHOW INDEX FROM <테이블>` 로 현재 인덱스 확인
4. 아래 관점으로 진단 + 개선안 제시

## 진단 체크리스트

| 항목 | 경고 조건 | 의미 |
|------|---------|------|
| `type` | `ALL`, `index` | 풀 스캔 — 인덱스 미사용 |
| `rows` | 수만~수십만+ | 스캔 대상 과다 |
| `key` | `NULL` | 인덱스 미적용 |
| `Extra` | `Using filesort` | 정렬용 임시 메모리 사용 |
| `Extra` | `Using temporary` | 임시 테이블 생성 (GROUP BY/DISTINCT) |
| `Extra` | `Using where; Using index` | 커버링 인덱스 (좋음) |
| JSON cost | `query_cost` 높음 | 전반적 비용 과다 |

## 개선안 카테고리

- **인덱스 추가**: WHERE·JOIN·ORDER BY 컬럼에 복합/단일 인덱스 제안
- **쿼리 재작성**: 서브쿼리 → JOIN, OR → UNION, LIKE '%x%' → 전문검색
- **JOIN 순서 변경**: 작은 테이블 먼저, STRAIGHT_JOIN 힌트 검토
- **LIMIT/페이징**: 커서 기반 페이징 권장 (offset 회피)
- **커버링 인덱스**: SELECT 컬럼을 인덱스에 포함

## 안전장치

- EXPLAIN / EXPLAIN ANALYZE / SHOW INDEX 만 실행. 원본 쿼리는 **실행하지 않는다** (플랜만 본다).
- 쓰기 명령은 글로벌 hook `block-dangerous-sql.sh`가 자동 차단.
- **인덱스 추가·테이블 변경은 제안만**. 실제 ALTER/CREATE INDEX는 사용자가 DBA 승인 후 master에 직접 수행.

## 결과 형식

```
## 실행 계획 (EXPLAIN)
(요약 테이블)

## 주요 지표
- type: ... / rows: ... / key: ...

## 진단
- 🔴 풀 스캔 발생 (type=ALL, rows=N)
- 🟡 filesort 사용

## 개선안
1. `CREATE INDEX idx_... ON table(col1, col2)` 제안
2. WHERE 조건 순서 변경: ...
3. ...

## 재측정 방법
제안 인덱스 적용 후 동일 쿼리를 `/db-tune` 으로 다시 실행하여 rows/cost 비교.
```
