---
description: 현재 브랜치의 변경사항을 분석하여 PR을 자동 생성합니다.
allowed-tools: Bash(git:*), Bash(gh:*), Read, Glob, Grep
---

# PR 자동 생성

현재 브랜치의 변경사항을 분석하여 GitHub Pull Request를 생성해줘.

## Branch 결정

### Compare Branch (비교 기준)

diff 및 로그 비교에 사용할 브랜치를 결정한다:

1. `git rev-parse --verify main 2>/dev/null` → 존재하면 `main` 사용
2. `git rev-parse --verify master 2>/dev/null` → 존재하면 `master` 사용
3. 위 브랜치가 모두 존재하지 않으면 AskUserQuestion으로 직접 입력받는다
   - "비교 기준 브랜치를 찾을 수 없습니다 (main, master 모두 없음). 비교할 브랜치를 입력해주세요."
4. 결정된 브랜치를 `{COMPARE_BRANCH}`로 사용

### PR Base Branch (PR 대상)

PR을 생성할 대상 브랜치를 결정한다:

1. 프로젝트 CLAUDE.md에서 `PR_BASE_BRANCH` 설정을 찾는다
   - 예: `PR_BASE_BRANCH: main-review`
   - 설정이 있으면 해당 브랜치를 사용
2. 설정이 없으면 다음 순서로 로컬 브랜치 존재 여부를 확인:
   - `git rev-parse --verify main-review 2>/dev/null` → 존재하면 `main-review` 사용
   - `git rev-parse --verify master-review 2>/dev/null` → 존재하면 `master-review` 사용
3. 위 브랜치가 모두 존재하지 않으면 AskUserQuestion으로 PR base branch를 직접 입력받는다
   - "PR base branch를 찾을 수 없습니다 (main-review, master-review 모두 없음). PR base branch를 입력해주세요."
4. 결정된 브랜치를 `{PR_BASE_BRANCH}`로 사용

## 사전 확인

1. 현재 브랜치가 main 또는 base branch가 아닌지 확인
2. main 또는 base branch면 "PR은 작업 브랜치에서 생성해야 합니다."라고 안내하고 중단

## 분석 단계

다음 명령어들을 **병렬로** 실행하여 정보를 수집:

1. `git branch --show-current` - 현재 브랜치명
2. `git log {COMPARE_BRANCH}..HEAD --oneline` - base branch 이후 커밋 목록
3. `git diff {COMPARE_BRANCH}...HEAD --stat` - 변경된 파일 통계
4. `git diff {COMPARE_BRANCH}...HEAD` - 전체 diff 내용
5. `git status` - 현재 작업 상태 (커밋되지 않은 변경 확인)

## 커밋되지 않은 변경 처리

- 스테이징되지 않은 변경이 있으면 사용자에게 경고
- "커밋되지 않은 변경사항이 있습니다. 먼저 커밋한 후 PR을 생성하시겠습니까?"라고 AskUserQuestion으로 확인

## PR 작성 규칙

### PR 제목
- 현재 브랜치명을 그대로 PR 제목으로 사용한다
- 예: 브랜치가 `TECH-21964-be-kglowing-tiktok-logging-개선`이면 제목도 `TECH-21964-be-kglowing-tiktok-logging-개선`

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

## 리뷰어 랜덤 선정

PR 본문 확인이 완료되면, 리뷰어를 랜덤으로 2명 선정한다.

### 1. 리뷰어 후보 목록 수집

```bash
# 저장소 collaborator 목록 조회 (본인 제외)
gh api repos/{owner}/{repo}/collaborators --jq ".[].login" | grep -v "$(git config user.name)"
```

- PR 작성자 본인은 후보에서 **제외**
- 글로벌 CLAUDE.md의 `PR_REVIEWER_EXCLUDE` 설정에 포함된 계정은 후보에서 **제외**
  - 현재 제외 목록: `temcolabs`, `happyfridaycode`
  - `/pr` 실행 시 인자로 추가 제외 대상을 전달할 수 있음 (예: `/pr reviewer 제외: userA`)
- 후보가 2명 미만이면 가능한 인원 전체를 리뷰어로 지정

### 2. 랜덤 선정 및 결과 출력

후보 목록에서 **2명을 랜덤으로 선정**하고, 선정 과정을 다음 형식으로 출력:

```
## 리뷰어 랜덤 선정 결과

- **전체 목록**: user1, user2, user3, ...
- **제외 목록**: excludedUser1, excludedUser2 (사유: PR_REVIEWER_EXCLUDE 설정)
- **후보 목록**: user1, user2, user3, ...
- **총 후보 수**: N명
- **선정 수**: 2명
- **선정된 리뷰어**: ✅ userA, ✅ userB
- **선정 방식**: 후보 목록에서 무작위 추출 (중복 없음)
```

### 3. 사용자 확인 (PR 생성 전 필수)

PR 생성 전에 반드시 AskUserQuestion으로 리뷰어를 확인받는다:
- "선정된 리뷰어: userA, userB. 이 리뷰어로 PR을 생성할까요?"
- 옵션: "확인", "다시 뽑기", "직접 지정"
- "다시 뽑기" 선택 시 2단계로 돌아가 재선정
- "직접 지정" 선택 시 사용자가 리뷰어 핸들을 직접 입력
- **리뷰어 확인 없이 PR을 생성하지 않는다**

## PR 생성

```bash
# 리모트에 푸시 (아직 안 되어 있다면)
git push -u origin $(git branch --show-current)

# PR 생성 (base: {PR_BASE_BRANCH}, 리뷰어 포함)
gh pr create --base {PR_BASE_BRANCH} --title "{제목}" --body "{본문}" --reviewer "{리뷰어1},{리뷰어2}"
```

## 완료 후 안내

PR 생성 완료 시 다음 정보를 출력:
- PR URL
- PR 번호
- 변경 파일 수, 추가/삭제 라인 수
- 리뷰어: 선정된 리뷰어 목록
