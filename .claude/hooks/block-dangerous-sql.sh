#!/bin/bash
#
# mysqlsh 위험 SQL 명령 차단 PreToolUse Hook
# Bash 명령 중 mysqlsh 호출을 감지하고, 읽기 전용 SQL만 허용한다.
# exit 0: 허용, exit 2: 차단 (BLOCKED)
#

# stdin에서 JSON 읽기
INPUT=$(cat)

COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // ""')

# 빈 명령이면 통과
if [[ -z "$COMMAND" ]]; then
    exit 0
fi

# mysqlsh 명령이 아니면 통과
if ! echo "$COMMAND" | grep -qE '(mysqlsh|mysql\s)'; then
    exit 0
fi

# 명령 전체를 대문자로 변환하여 SQL 패턴 매칭에 사용
SQL=$(echo "$COMMAND" | tr '[:lower:]' '[:upper:]')

# 차단 패턴 (위험 명령)
BLOCKED_PATTERNS=(
    "DELETE\s|DELETE 명령 (데이터 삭제)"
    "UPDATE\s|UPDATE 명령 (데이터 수정)"
    "\bINSERT\s|INSERT 명령 (데이터 삽입)"
    "\bREPLACE\s|REPLACE 명령 (데이터 교체)"
    "\bDROP\s|DROP 명령 (객체 삭제)"
    "\bALTER\s|ALTER 명령 (스키마 변경)"
    "\bTRUNCATE\s|TRUNCATE 명령 (테이블 전체 삭제)"
    "\bRENAME\s|RENAME 명령 (객체 이름 변경)"
    "\bCREATE\s|CREATE 명령 (객체 생성)"
    "\bGRANT\s|GRANT 명령 (권한 부여)"
    "\bREVOKE\s|REVOKE 명령 (권한 회수)"
    "LOAD\s+DATA|LOAD DATA 명령 (외부 데이터 로드)"
    "INTO\s+OUTFILE|INTO OUTFILE (파일 출력)"
    "INTO\s+DUMPFILE|INTO DUMPFILE (파일 출력)"
    "SET\s+@@GLOBAL|글로벌 변수 변경"
    "\bFLUSH\s|FLUSH 명령 (캐시/로그 초기화)"
    "\bKILL\s|KILL 명령 (프로세스 종료)"
    "\bCALL\s|CALL 명령 (프로시저 호출)"
)

for rule in "${BLOCKED_PATTERNS[@]}"; do
    pattern="${rule%%|*}"
    description="${rule##*|}"

    if echo "$SQL" | grep -qE "$pattern"; then
        echo "⛔ BLOCKED: mysqlsh ${description}"
        echo "  명령어: ${COMMAND}"
        echo "  허용된 명령: SELECT, SHOW, DESCRIBE, DESC, EXPLAIN만 사용 가능합니다."
        exit 2
    fi
done

exit 0
