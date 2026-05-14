# DB 스키마 조회 가이드 (mysqlsh)

MySQL Shell 8.4를 사용하여 DB 스키마 정보를 조회하는 가이드.
**읽기 전용 쿼리만 허용된다. DML/DDL은 hook에 의해 차단된다.**

---

## 접속 방법

### URI 형식

```bash
mysqlsh --sql -u {user} -p{password} -h {host} -P {port} -D {database} -e "{SQL}"
```

### application.yml에서 접속 정보 추출

Spring Boot 프로젝트의 DB 접속 정보는 보통 다음 위치에 있다:

| 파일 | 키 |
|------|-----|
| `application.yml` | `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` |
| `application.properties` | `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password` |
| `application-local.yml` | 로컬 프로파일 (우선 사용) |

JDBC URL 파싱 예시:
```
jdbc:mysql://localhost:3306/mydb?useSSL=false
→ host: localhost, port: 3306, database: mydb
```

---

## 스키마 조회 쿼리 템플릿

### 테이블 목록

```sql
SHOW TABLES;
```

### 테이블 구조 (컬럼 정보)

```sql
DESCRIBE {table_name};
```

### 상세 컬럼 정보 (타입, 기본값, 코멘트)

```sql
SELECT
    COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE,
    COLUMN_DEFAULT, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = '{database}' AND TABLE_NAME = '{table_name}'
ORDER BY ORDINAL_POSITION;
```

### 인덱스 정보

```sql
SHOW INDEX FROM {table_name};
```

### 외래키 정보

```sql
SELECT
    CONSTRAINT_NAME, COLUMN_NAME,
    REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = '{database}'
  AND TABLE_NAME = '{table_name}'
  AND REFERENCED_TABLE_NAME IS NOT NULL;
```

### 테이블 생성 DDL 확인

```sql
SHOW CREATE TABLE {table_name};
```

### 테이블 통계 (행 수, 크기)

```sql
SELECT
    TABLE_NAME, TABLE_ROWS, DATA_LENGTH, INDEX_LENGTH,
    ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) AS SIZE_MB
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = '{database}'
ORDER BY TABLE_ROWS DESC;
```

---

## 사용 예시

```bash
# 테이블 목록 조회
mysqlsh --sql -u root -proot -h localhost -P 3306 -D mydb -e "SHOW TABLES;"

# 특정 테이블 구조 조회
mysqlsh --sql -u root -proot -h localhost -P 3306 -D mydb -e "DESCRIBE users;"

# 인덱스 조회
mysqlsh --sql -u root -proot -h localhost -P 3306 -D mydb -e "SHOW INDEX FROM users;"

# 외래키 조회
mysqlsh --sql -u root -proot -h localhost -P 3306 -D mydb \
  -e "SELECT CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = 'mydb' AND TABLE_NAME = 'orders' AND REFERENCED_TABLE_NAME IS NOT NULL;"
```

---

## 보안 주의사항

- `block-dangerous-sql.sh` hook이 위험한 SQL을 자동 차단한다
- 허용: `SELECT`, `SHOW`, `DESCRIBE`, `DESC`, `EXPLAIN`
- 차단: `DELETE`, `UPDATE`, `INSERT`, `DROP`, `ALTER`, `TRUNCATE`, `CREATE` 등
- 비밀번호가 포함된 명령은 히스토리에 남을 수 있으므로, 환경변수 사용을 권장한다
