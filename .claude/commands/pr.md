---
description: 현재 브랜치의 변경사항을 분석하여 PR을 자동 생성합니다.
allowed-tools: Bash(git:*), Bash(gh:*), Read, Glob, Grep
---

# PR 자동 생성

현재 브랜치의 변경사항을 분석하여 GitHub Pull Request를 생성해줘.

## 사전 확인

1. 현재 브랜치가 main이 아닌지 확인
2. main이면 "PR은 main이 아닌 브랜치에서 생성해야 합니다."라고 안내하고 중단

## 분석 단계

다음 명령어들을 **병렬로** 실행하여 정보를 수집:

1. `git branch --show-current` - 현재 브랜치명
2. `git log main..HEAD --oneline` - main 이후 커밋 목록
3. `git diff main...HEAD --stat` - 변경된 파일 통계
4. `git diff main...HEAD` - 전체 diff 내용
5. `git status` - 현재 작업 상태 (커밋되지 않은 변경 확인)

## 커밋되지 않은 변경 처리

- 스테이징되지 않은 변경이 있으면 사용자에게 경고
- "커밋되지 않은 변경사항이 있습니다. 먼저 커밋한 후 PR을 생성하시겠습니까?"라고 AskUserQuestion으로 확인

## PR 작성 규칙

### PR 제목
- 70자 이내
- 커밋 메시지 컨벤션 따름: `<type>: <subject>`
- 여러 커밋이면 가장 핵심적인 변경을 요약

### PR 본문 (pull_request_template.md 형식 준수)

```
### 변경 내용을 설명해 주세요. (자유롭게 기술해 주세요.)

{변경 내용 상세 분석 - 커밋별로 정리}

- **변경 파일**: {변경된 파일 목록과 각 파일의 변경 요약}
- **주요 변경사항**: {핵심 변경 내용을 bullet point로}

### (Optional) 리뷰어에게 남기실 말씀을 써 주세요.

{코드 리뷰 시 참고할 사항 - 특별히 봐달라는 부분, 고민한 점 등}
```

## PR 생성 전 확인

1. 작성된 PR 제목과 본문을 사용자에게 **미리 보여주기**
2. AskUserQuestion으로 확인:
   - "이대로 PR을 생성할까요?"
   - 옵션: "생성", "제목 수정", "본문 수정", "취소"
3. 수정 요청 시 해당 부분만 수정 후 다시 확인

## PR 생성

```bash
# 리모트에 푸시 (아직 안 되어 있다면)
git push -u origin $(git branch --show-current)

# PR 생성
gh pr create --title "{제목}" --body "{본문}"
```

## 완료 후 안내

PR 생성 완료 시 다음 정보를 출력:
- PR URL
- PR 번호
- 변경 파일 수, 추가/삭제 라인 수
