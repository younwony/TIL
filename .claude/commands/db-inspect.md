---
description: mysqlsh로 read-only DB 조회 (SELECT/SHOW/DESCRIBE/EXPLAIN만 허용)
allowed-tools: Bash, Read
argument-hint: "<SQL 쿼리>"
---

# DB Inspect

$ARGUMENTS

쿼리를 mysqlsh로 실행한다. 접속 정보는 `.claude/skills/prod-db-inspect/references/connection.md`에서 읽어 그대로 사용.

쓰기 명령(DELETE/UPDATE/INSERT/REPLACE/DROP/ALTER/TRUNCATE/RENAME/CREATE/GRANT/REVOKE/LOAD DATA/INTO OUTFILE/INTO DUMPFILE/SET @@GLOBAL/FLUSH/KILL/CALL)은 글로벌 PreToolUse hook `~/.claude/hooks/block-dangerous-sql.sh`가 Bash 단에서 자동 차단한다. 별도 사전 검증 로직 불필요.

대용량 테이블 전체 스캔이 예상되면 `LIMIT N` 추가.

결과는 마크다운 테이블로 정리. 에러는 원문 그대로 전달.
