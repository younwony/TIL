# Git Hooks

> `[3] 중급` · 선수 지식: [Git 기본 개념](./git-basics.md)

> Git 이벤트 발생 시 자동으로 실행되는 스크립트

`#GitHooks` `#Git훅` `#pre-commit` `#commit-msg` `#pre-push` `#post-merge` `#자동화` `#Automation` `#린트` `#Lint` `#포맷팅` `#Formatting` `#CI` `#Husky` `#lint-staged` `#커밋메시지` `#CommitMessage` `#코드품질` `#CodeQuality`

## 왜 알아야 하는가?

Git Hooks는 커밋/푸시 시 자동으로 코드 품질을 검증합니다. 잘못된 코드가 저장소에 들어가는 것을 방지하고, 일관된 커밋 메시지를 강제합니다. CI/CD 이전 단계에서 문제를 조기에 발견합니다.

## 핵심 개념

- **클라이언트 훅**: 로컬에서 실행 (pre-commit, commit-msg)
- **서버 훅**: 원격 저장소에서 실행 (pre-receive, post-receive)
- **트리거**: 특정 Git 이벤트 발생 시 자동 실행
- **종료 코드**: 0이면 계속, 그 외는 중단

## 쉽게 이해하기

**Git Hooks**를 공항 보안 검색에 비유할 수 있습니다.

- **pre-commit**: 탑승 전 보안 검색 (금지 물품 확인)
- **commit-msg**: 탑승권 확인 (올바른 형식인지)
- **pre-push**: 출국 심사 (최종 확인)

통과하지 못하면 탑승(커밋/푸시) 불가!

## 상세 설명

### 훅 종류와 타이밍

```
워크플로우:

git add → [pre-commit] → [commit-msg] → git commit
              ↓              ↓
         린트, 테스트    메시지 검증

git push → [pre-push] → 원격 저장소 → [pre-receive] → [post-receive]
              ↓                           ↓              ↓
         로컬 최종 확인             서버 검증     알림/배포
```

### 주요 클라이언트 훅

| 훅 | 실행 시점 | 용도 |
|---|---------|------|
| pre-commit | 커밋 메시지 입력 전 | 린트, 포맷팅, 테스트 |
| prepare-commit-msg | 에디터 열기 전 | 메시지 템플릿 |
| commit-msg | 메시지 입력 후 | 메시지 형식 검증 |
| post-commit | 커밋 완료 후 | 알림 |
| pre-push | 푸시 전 | 테스트, 빌드 확인 |
| post-merge | 머지 완료 후 | 의존성 설치 |

### 훅 설정

```bash
# 훅 파일 위치
.git/hooks/pre-commit
.git/hooks/commit-msg
# 등등...

# 훅 파일 생성 (실행 권한 필요!)
chmod +x .git/hooks/pre-commit
```

### pre-commit 예시

```bash
#!/bin/sh
# .git/hooks/pre-commit

echo "Running pre-commit hooks..."

# 1. 린트 검사
npm run lint
if [ $? -ne 0 ]; then
    echo "❌ Lint failed. Commit aborted."
    exit 1
fi

# 2. 테스트 실행
npm run test
if [ $? -ne 0 ]; then
    echo "❌ Tests failed. Commit aborted."
    exit 1
fi

echo "✅ All checks passed!"
exit 0
```

### commit-msg 예시

```bash
#!/bin/sh
# .git/hooks/commit-msg

COMMIT_MSG_FILE=$1
COMMIT_MSG=$(cat $COMMIT_MSG_FILE)

# Conventional Commits 형식 검증
PATTERN="^(feat|fix|docs|style|refactor|test|chore)(\(.+\))?: .{1,50}"

if ! echo "$COMMIT_MSG" | grep -qE "$PATTERN"; then
    echo "❌ Invalid commit message format!"
    echo "Expected: type(scope): subject"
    echo "Example: feat(auth): add login feature"
    exit 1
fi

echo "✅ Commit message format is valid!"
exit 0
```

### Husky (Node.js 프로젝트)

```bash
# 설치
npm install husky --save-dev
npx husky install

# pre-commit 훅 추가
npx husky add .husky/pre-commit "npm run lint"
npx husky add .husky/pre-commit "npm test"

# commit-msg 훅 추가
npx husky add .husky/commit-msg 'npx --no -- commitlint --edit "$1"'
```

```json
// package.json
{
  "scripts": {
    "prepare": "husky install"
  }
}
```

### lint-staged (스테이징된 파일만)

```bash
npm install lint-staged --save-dev
```

```json
// package.json
{
  "lint-staged": {
    "*.{js,ts}": [
      "eslint --fix",
      "prettier --write"
    ],
    "*.{json,md}": [
      "prettier --write"
    ]
  }
}
```

```bash
# .husky/pre-commit
npx lint-staged
```

### 훅 공유 (.husky 폴더)

```
프로젝트/
├── .husky/
│   ├── _/
│   │   └── husky.sh
│   ├── pre-commit
│   └── commit-msg
├── package.json
└── ...
```

Git에 `.husky/` 폴더가 커밋되어 팀 전체에 적용

### 훅 우회 (비상시)

```bash
# 훅 건너뛰기 (권장하지 않음)
git commit --no-verify -m "Emergency fix"
git push --no-verify
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 자동화된 품질 검증 | 커밋 시간 증가 |
| 일관성 강제 | 초기 설정 필요 |
| CI 이전 문제 발견 | 우회 가능 |

## 면접 예상 질문

### Q: Git Hooks를 사용하는 이유는?

A: (1) **자동 품질 검증**: 커밋 전 린트, 테스트 자동 실행 (2) **일관성**: 커밋 메시지 형식 강제 (3) **조기 발견**: CI 전에 문제 발견 (4) **팀 표준화**: 공유 가능한 훅으로 팀 전체 적용. **도구**: Husky + lint-staged가 Node.js 프로젝트 표준.

### Q: pre-commit과 pre-push의 차이는?

A: **pre-commit**: 커밋 직전 실행, 빠른 검사 (린트, 포맷팅). **pre-push**: 푸시 직전 실행, 더 무거운 검사 (전체 테스트, 빌드). **전략**: pre-commit은 빠르게 자주, pre-push는 철저하게.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 기본 개념](./git-basics.md) | 선수 지식 | [2] 입문 |
| [CI/CD 자동화](../automation/cicd.md) | 연계 | [3] 중급 |

## 참고 자료

- [Git Hooks - Pro Git](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)
- [Husky Documentation](https://typicode.github.io/husky/)
