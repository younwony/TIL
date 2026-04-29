#!/bin/bash
#
# 위험한 Bash 명령 실행을 사전에 차단하는 PreToolUse Hook (글로벌)
# exit 0: 허용, exit 2: 차단 (BLOCKED)
#

# stdin에서 JSON 읽기
INPUT=$(cat)

COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // ""')

# 빈 명령이면 통과
if [[ -z "$COMMAND" ]]; then
    exit 0
fi

# 위험 패턴 목록 (패턴|설명)
RULES=(
    "rm -rf /|루트 디렉토리 삭제"
    "rm -rf ~|홈 디렉토리 삭제"
    "git push.*--force.*main|main 브랜치 force push"
    "git push.*--force.*master|master 브랜치 force push"
    "git push.*-f.*main|main 브랜치 force push"
    "git push.*-f.*master|master 브랜치 force push"
    "git reset --hard|커밋 이력 강제 초기화"
    "git checkout \.|모든 변경사항 폐기"
    "git restore \.|모든 변경사항 폐기"
    "git clean -fd|추적되지 않는 파일 강제 삭제"
    "git branch -D|브랜치 강제 삭제"
    "drop table|테이블 삭제"
    "drop database|데이터베이스 삭제"
    "truncate table|테이블 데이터 전체 삭제"
    "curl.*\|.*sh$|원격 스크립트 파이프 실행"
    "curl.*\|.*bash$|원격 스크립트 파이프 실행"
    "wget.*\|.*sh$|원격 스크립트 파이프 실행"
    "chmod -R 777|전체 권한 개방"
    "dd if=|디스크 직접 쓰기"
    "mkfs\.|파일시스템 포맷"
    "--no-verify|Git Hook 우회"
    "git\s+add\s+.*\.claude|.claude/ 디렉토리 git add 금지 (CLAUDE.md 규칙)"
    "git\s+add\s+-A|--all 추가는 .claude/ 포함 위험. 개별 파일 명시 권장"
)

for rule in "${RULES[@]}"; do
    pattern="${rule%%|*}"
    description="${rule##*|}"

    if echo "$COMMAND" | grep -iqE "$pattern"; then
        echo "⛔ BLOCKED: ${description}"
        echo "  명령어: ${COMMAND}"
        echo "  패턴: ${pattern}"
        exit 2
    fi
done

exit 0
