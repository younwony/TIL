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

# TIL 저장소 예외 사전 판정 (.claude/ git add 관련 룰만 우회)
# TIL은 글로벌 설정 공유 채널로 사용되어 .claude/ git add 허용 (TIL/CLAUDE.md 정책).
TIL_PATTERN='workspace[/\\]intellij[/\\]TIL'
CWD=$(echo "$INPUT" | jq -r '.cwd // ""')
IN_TIL=0
if [[ "$SKIP_CLAUDE_DIR_BLOCK" == "1" ]]; then IN_TIL=1; fi
if echo "$COMMAND" | grep -qiE "$TIL_PATTERN"; then IN_TIL=1; fi
if [[ -n "$CWD" ]] && echo "$CWD" | grep -qiE "$TIL_PATTERN"; then IN_TIL=1; fi
if pwd 2>/dev/null | grep -qiE "$TIL_PATTERN"; then IN_TIL=1; fi

for rule in "${RULES[@]}"; do
    pattern="${rule%%|*}"
    description="${rule##*|}"

    if echo "$COMMAND" | grep -iqE "$pattern"; then
        # TIL 안에서는 .claude/ git add 관련 룰만 통과. rm -rf, force push 등은 그대로 차단.
        if [[ "$IN_TIL" == "1" ]] && { [[ "$pattern" == *'\.claude'* ]] || [[ "$pattern" == *'add\s+-A'* ]]; }; then
            continue
        fi
        echo "⛔ BLOCKED: ${description}"
        echo "  명령어: ${COMMAND}"
        echo "  패턴: ${pattern}"
        if [[ "$pattern" == *'\.claude'* ]] || [[ "$pattern" == *'add\s+-A'* ]]; then
            echo "  TIL 저장소(workspace/intellij/TIL/)에서는 자동 허용됨" >&2
            echo "  그 외 응급 우회: SKIP_CLAUDE_DIR_BLOCK=1 git add ..." >&2
        fi
        exit 2
    fi
done

exit 0
