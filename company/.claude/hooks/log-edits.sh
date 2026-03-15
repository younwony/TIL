#!/bin/bash
#
# 마크다운 파일 편집 시 로그를 기록하는 Hook
# PostToolUse에서 Edit|Write Tool 완료 후 실행됩니다.
#

LOG_FILE="$HOME/.claude/edit-log.txt"

# stdin에서 JSON 읽기
INPUT=$(cat)

# file_path 추출 (jq 사용)
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // ""')
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // ""')

# 마크다운 파일이 아니면 종료
if [[ ! "$FILE_PATH" =~ \.(md|mdx)$ ]]; then
    exit 0
fi

# 로그 디렉토리 생성
mkdir -p "$(dirname "$LOG_FILE")"

# 타임스탬프와 함께 로그 기록
TIMESTAMP=$(date "+%Y-%m-%d %H:%M:%S")
echo "[$TIMESTAMP] $TOOL_NAME: $FILE_PATH" >> "$LOG_FILE"

exit 0
