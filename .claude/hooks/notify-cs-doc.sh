#!/bin/bash
#
# CS 문서 생성/수정 시 알림을 표시하는 Hook
# PostToolUse에서 Edit|Write Tool 완료 후 실행됩니다.
#

# stdin에서 JSON 읽기
INPUT=$(cat)

# file_path, tool_name 추출
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // ""')
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // ""')

# cs/ 디렉토리의 마크다운 파일이 아니면 종료
if [[ ! "$FILE_PATH" =~ /cs/.+\.md$ ]]; then
    exit 0
fi

# 파일명과 카테고리 추출
FILENAME=$(basename "$FILE_PATH" .md)
CATEGORY=$(echo "$FILE_PATH" | grep -oP '/cs/\K[^/]+')

# 액션 결정
if [[ "$TOOL_NAME" == "Write" ]]; then
    ACTION="추가"
else
    ACTION="수정"
fi

# 알림 메시지 출력 (Claude Code 대화에 표시됨)
echo "📝 CS 문서 ${ACTION}: [${CATEGORY}] ${FILENAME}.md"

exit 0
