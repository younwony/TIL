# DB 접속 정보

## Read-Only Slave (guhada 운영)

| 항목 | 값 |
|------|------|
| 호스트 | `guhada-prod-slave.cytgq1fzvvht.ap-northeast-2.rds.amazonaws.com` |
| 포트 | `3306` |
| 유저 | `tech_ai_agent` |
| 비밀번호 | `Tsjdwi311!` (사용자 직접 입력) |
| 권한 | read-only (SELECT / SHOW / DESCRIBE / EXPLAIN) |
| 용도 | 운영 점검 · 장애 진단 · 데이터 확인 |

## mysqlsh 실행 기본 형태

### 단일 쿼리 (`-e` 사용)

```bash
mysqlsh --sql \
  --uri 'tech_ai_agent:Tsjdwi311!@guhada-prod-slave.cytgq1fzvvht.ap-northeast-2.rds.amazonaws.com:3306' \
  -e "SELECT 1"
```

### 여러 줄 쿼리 (heredoc)

```bash
mysqlsh --sql \
  --uri 'tech_ai_agent:Tsjdwi311!@guhada-prod-slave.cytgq1fzvvht.ap-northeast-2.rds.amazonaws.com:3306' <<'SQL'
SELECT id, email, created_at
FROM users
WHERE created_at >= CURDATE() - INTERVAL 1 DAY
ORDER BY id DESC
LIMIT 10;
SQL
```

### 결과 포맷 옵션

| 플래그 | 용도 |
|--------|------|
| `--table` | 컬럼 정렬 테이블 (기본, 사람 확인용) |
| `--json` | JSON 출력 (파싱용) |
| `--result-format=tabbed` | 탭 구분 (grep/awk 파이프용) |

예시:
```bash
mysqlsh --sql --table --uri '...' -e "SELECT ..."
```

## 비밀번호 내 특수문자 주의

비밀번호에 `!`가 포함된 경우 bash 히스토리 확장 문자로 해석될 수 있다. 반드시 **작은따옴표**로 감싼다.

```bash
# 올바름
--uri 'tech_ai_agent:PASS!WORD@host:3306'

# 잘못됨 (bash가 !WORD를 히스토리로 해석 시도)
--uri "tech_ai_agent:PASS!WORD@host:3306"
```

## 추가 DB 등록 시

향후 스테이징/마스터 등이 필요하면 이 문서 아래에 동일 포맷으로 추가하고, SKILL.md의 "접속 정보" 섹션은 이 파일을 계속 참조한다.
