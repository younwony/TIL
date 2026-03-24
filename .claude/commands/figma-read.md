---
description: Figma URL의 디자인을 figma-team MCP로 읽어옵니다.
allowed-tools: mcp__figma-team__get_figma_data, mcp__figma-team__download_figma_images, Read, Write, Glob
---

# Figma 디자인 읽기

사용자가 제공한 Figma URL에서 디자인 데이터를 읽어와 분석한다.

## 필수 규칙

- **반드시 `mcp__figma-team__*` MCP 도구만 사용** (`mcp__claude_ai_Figma__*`는 개인계정이므로 사용 금지)
- figma-team MCP 실패 시에만 claude_ai_Figma를 폴백으로 고려

## URL 파싱

Figma URL에서 fileKey와 nodeId를 추출한다:
- `figma.com/design/:fileKey/:fileName?node-id=:nodeId` → nodeId의 `-`를 `:`로 변환
- `figma.com/design/:fileKey/branch/:branchKey/:fileName` → branchKey를 fileKey로 사용
- `figma.com/file/:fileKey/...` → fileKey 추출

## 실행 절차

1. 사용자가 제공한 Figma URL에서 fileKey와 nodeId를 파싱
2. `mcp__figma-team__get_figma_data`로 디자인 데이터 조회
3. 필요 시 `mcp__figma-team__download_figma_images`로 이미지 다운로드
4. 디자인 구조, 컴포넌트, 스타일 정보를 분석하여 요약 제공

## 입력

$ARGUMENTS

- Figma URL을 인자로 전달 (예: `/figma-read https://www.figma.com/design/xxx/...`)
- URL이 없으면 사용자에게 요청