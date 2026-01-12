# 코드 리뷰 도구 (Code Review Tools)

> `[2] 입문` · 선수 지식: [클린 코드](./clean-code.md)

> 코드 품질을 자동으로 검사하고 일관된 스타일을 유지하기 위한 도구 모음

`#코드리뷰` `#CodeReview` `#린터` `#Linter` `#포매터` `#Formatter` `#Prettier` `#ESLint` `#정적분석` `#StaticAnalysis` `#SonarQube` `#Checkstyle` `#PMD` `#SpotBugs` `#Ktlint` `#Detekt` `#Pylint` `#Ruff` `#Black` `#코드품질` `#CodeQuality` `#GitHooks` `#Husky` `#pre-commit` `#CI/CD` `#자동화` `#코드스타일` `#CodeStyle`

## 왜 알아야 하는가?

코드 리뷰 도구는 현대 소프트웨어 개발에서 **필수적인 품질 관리 인프라**입니다.

- **실무**: 팀 전체의 코드 스타일 통일, 잠재적 버그 사전 방지, PR 리뷰 시간 단축
- **면접**: "팀에서 코드 품질을 어떻게 관리하나요?" 질문에 구체적 도구와 경험 답변 필요
- **기반 지식**: CI/CD 파이프라인, DevOps 문화, 협업 프로세스의 기초

## 핵심 개념

- **린터 (Linter)**: 정적 코드 분석으로 버그, 안티패턴, 코드 스멜 검출
- **포매터 (Formatter)**: 코드 스타일(들여쓰기, 줄바꿈, 따옴표 등) 자동 통일
- **정적 분석 도구**: 보안 취약점, 기술 부채, 복잡도 등 심층 분석
- **Git Hooks**: 커밋/푸시 전 자동 검사 실행

## 쉽게 이해하기

코드 리뷰 도구를 **출판 과정**에 비유할 수 있습니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                        출판 과정 비유                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   📝 원고 작성 (코드 작성)                                        │
│        │                                                         │
│        ▼                                                         │
│   🔍 맞춤법 검사기 (린터)                                         │
│        - "오타가 있습니다" → "console.log 금지 규칙 위반"         │
│        - "문법 오류입니다" → "변수가 선언되지 않았습니다"          │
│        │                                                         │
│        ▼                                                         │
│   📐 편집 스타일 가이드 (포매터)                                   │
│        - 문단 들여쓰기 통일 → 코드 들여쓰기 통일                  │
│        - 문장 부호 규칙 → 세미콜론, 따옴표 규칙                   │
│        │                                                         │
│        ▼                                                         │
│   📊 품질 심사 (정적 분석)                                        │
│        - 전체 내용의 일관성 → 코드 중복, 복잡도                   │
│        - 민감한 내용 검토 → 보안 취약점                           │
│        │                                                         │
│        ▼                                                         │
│   ✅ 최종 출판 (배포)                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 린터 (Linter)

**정적 코드 분석 도구**로, 코드를 실행하지 않고 잠재적 오류와 안티패턴을 검출합니다.

#### 언어별 린터

| 언어 | 도구 | 특징 |
|------|------|------|
| JavaScript/TypeScript | **ESLint** | 가장 널리 사용, 플러그인 생태계 풍부 |
| Python | **Pylint**, **Flake8**, **Ruff** | Ruff가 최신이며 가장 빠름 (Rust 기반) |
| Java | **Checkstyle**, **PMD**, **SpotBugs** | Checkstyle=스타일, SpotBugs=버그 |
| Kotlin | **Ktlint**, **Detekt** | Ktlint=스타일, Detekt=코드 스멜 |
| Go | **golangci-lint** | 여러 린터 통합 |

#### ESLint 예시

```javascript
// .eslintrc.js
module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  rules: {
    'no-console': 'warn',           // console.log 경고
    'no-unused-vars': 'error',      // 미사용 변수 에러
    'eqeqeq': ['error', 'always'],  // === 사용 강제
  },
};
```

```bash
# 실행
npx eslint src/**/*.js --fix
```

**왜 린터를 사용하는가?**
- 코드 리뷰에서 스타일 지적 대신 **로직에 집중** 가능
- 팀 전체 **일관된 코드 품질** 유지
- **잠재적 버그** 사전 발견 (undefined 참조, 타입 오류 등)

---

### 포매터 (Formatter)

**코드 스타일을 자동으로 통일**하는 도구입니다. 린터와 달리 **로직이 아닌 형식만** 다룹니다.

#### 언어별 포매터

| 도구 | 지원 언어 | 특징 |
|------|----------|------|
| **Prettier** | JS, TS, CSS, HTML, JSON, MD | 가장 인기, Opinionated (설정 최소화) |
| **Black** | Python | "The Uncompromising Formatter" |
| **google-java-format** | Java | Google 스타일 가이드 준수 |
| **ktfmt** | Kotlin | Kotlin 공식 포매터 |
| **gofmt** | Go | Go 기본 내장, 표준 |

#### Prettier 예시

```json
// .prettierrc
{
  "semi": true,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "es5",
  "printWidth": 100
}
```

```bash
# 실행
npx prettier --write "src/**/*.{js,ts,css}"
```

**왜 포매터를 사용하는가?**
- **토론 종결**: 탭 vs 스페이스, 세미콜론 논쟁 불필요
- **자동 수정**: 저장 시 자동 포맷팅으로 개발 생산성 향상
- **Git diff 최소화**: 불필요한 스타일 변경 커밋 방지

---

### 린터 vs 포매터

| 구분 | 린터 (Linter) | 포매터 (Formatter) |
|------|--------------|-------------------|
| **목적** | 코드 품질, 버그 검출 | 코드 스타일 통일 |
| **검사 범위** | 로직 오류, 미사용 변수, 보안 취약점 | 들여쓰기, 줄바꿈, 따옴표 |
| **자동 수정** | 일부만 가능 | 전체 자동 수정 |
| **예시** | "console.log 금지", "any 타입 금지" | "세미콜론 추가", "작은따옴표 사용" |

```
┌─────────────────────────────────────────────────────────────────┐
│                    린터 vs 포매터 역할 분담                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   소스 코드                                                       │
│       │                                                          │
│       ├──────────────────────────────────────┐                  │
│       │                                      │                  │
│       ▼                                      ▼                  │
│   ┌─────────────────┐              ┌─────────────────┐          │
│   │    포매터        │              │     린터         │          │
│   │  (Formatter)    │              │   (Linter)      │          │
│   ├─────────────────┤              ├─────────────────┤          │
│   │ • 들여쓰기       │              │ • 미사용 변수    │          │
│   │ • 줄바꿈         │              │ • 타입 오류      │          │
│   │ • 따옴표 스타일  │              │ • 보안 취약점    │          │
│   │ • 공백           │              │ • 안티패턴       │          │
│   └─────────────────┘              └─────────────────┘          │
│       │                                      │                  │
│       │   100% 자동 수정                      │   일부 자동 수정  │
│       │                                      │                  │
│       └──────────────────────────────────────┘                  │
│                           │                                      │
│                           ▼                                      │
│                    품질 보장된 코드                               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### 정적 분석 도구 (Static Analysis)

린터보다 **더 심층적인 분석**을 수행합니다. 보안 취약점, 기술 부채, 코드 복잡도 등을 측정합니다.

#### 주요 도구

| 도구 | 특징 | 용도 |
|------|------|------|
| **SonarQube** | 가장 널리 사용, 다양한 언어 지원 | 코드 품질, 보안, 기술 부채 |
| **CodeClimate** | GitHub 연동 우수, 유지보수성 점수 | 중복 코드, 복잡도 |
| **Codacy** | 자동 코드 리뷰, 다양한 린터 통합 | PR 자동 리뷰 |
| **Snyk** | 보안 특화 | 의존성 취약점 |
| **Semgrep** | 커스텀 규칙 작성 용이 | 보안, 코드 패턴 |

#### SonarQube 품질 게이트 예시

```yaml
# sonar-project.properties
sonar.projectKey=my-project
sonar.sources=src
sonar.tests=test

# 품질 게이트 조건
# - 코드 커버리지 80% 이상
# - 중복 코드 3% 이하
# - 버그 0개
# - 보안 취약점 0개
```

**왜 정적 분석을 사용하는가?**
- **기술 부채 가시화**: 코드 품질 점수로 객관적 측정
- **보안 취약점 검출**: OWASP Top 10 등 알려진 취약점 사전 발견
- **품질 게이트**: CI/CD에서 품질 기준 미달 시 배포 차단

---

### 코드 리뷰 플랫폼

사람이 직접 수행하는 코드 리뷰를 지원하는 플랫폼입니다.

| 플랫폼 | 특징 |
|--------|------|
| **GitHub PR Review** | 인라인 코멘트, Suggestion, CODEOWNERS |
| **GitLab MR Review** | 머지 리퀘스트, 코드 소유자 자동 지정 |
| **Gerrit** | Google 개발, 세밀한 권한 관리 |
| **Crucible** | Atlassian 제품, Jira 연동 |

---

### AI 기반 코드 리뷰

최근 AI를 활용한 자동 코드 리뷰 도구가 급부상하고 있습니다.

| 도구 | 특징 |
|------|------|
| **GitHub Copilot** | AI 코드 제안 및 리뷰 |
| **CodeRabbit** | PR 자동 리뷰, 요약 생성 |
| **Sourcery** | Python 리팩토링 제안 |
| **Amazon CodeGuru** | AWS 통합, 성능 이슈 검출 |
| **Qodo (Codium)** | 테스트 코드 자동 생성 |

---

### Git Hooks

**커밋/푸시 전에 자동으로 검사**를 실행하여 문제 있는 코드가 저장소에 들어가는 것을 방지합니다.

#### Git Hook 도구

| 도구 | 언어 | 특징 |
|------|------|------|
| **Husky** | Node.js | npm 프로젝트용, 가장 인기 |
| **pre-commit** | Python | 다양한 언어 지원, YAML 설정 |
| **lefthook** | Go | 빠른 속도, 병렬 실행 |

#### Husky + lint-staged 예시

```json
// package.json
{
  "scripts": {
    "lint": "eslint src --fix",
    "format": "prettier --write src"
  },
  "husky": {
    "hooks": {
      "pre-commit": "lint-staged"
    }
  },
  "lint-staged": {
    "*.{js,ts}": ["eslint --fix", "prettier --write"],
    "*.{css,scss}": ["prettier --write"]
  }
}
```

#### pre-commit 예시 (Python)

```yaml
# .pre-commit-config.yaml
repos:
  - repo: https://github.com/astral-sh/ruff-pre-commit
    rev: v0.1.6
    hooks:
      - id: ruff
        args: [--fix]
  - repo: https://github.com/psf/black
    rev: 23.11.0
    hooks:
      - id: black
```

```bash
# 설치 및 실행
pip install pre-commit
pre-commit install
```

**왜 Git Hooks를 사용하는가?**
- **Shift Left**: 문제를 최대한 빨리 발견 (커밋 시점)
- **CI 비용 절감**: 서버에서 실패하기 전에 로컬에서 검출
- **강제성**: 규칙을 우회하기 어렵게 만듦

---

## 권장 도구 조합

### JavaScript/TypeScript 프로젝트

```
ESLint (린터) + Prettier (포매터) + Husky + lint-staged
```

```json
// package.json
{
  "devDependencies": {
    "eslint": "^8.0.0",
    "prettier": "^3.0.0",
    "husky": "^8.0.0",
    "lint-staged": "^15.0.0"
  }
}
```

### Python 프로젝트

```
Ruff (린터) + Black (포매터) + pre-commit
```

```yaml
# pyproject.toml
[tool.ruff]
line-length = 100
select = ["E", "F", "W", "I"]

[tool.black]
line-length = 100
```

### Java 프로젝트

```
Checkstyle + SpotBugs + google-java-format + pre-commit
```

```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.0</version>
</plugin>
```

### Kotlin 프로젝트

```
Detekt (린터) + ktfmt (포매터) + pre-commit
```

```kotlin
// build.gradle.kts
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}

detekt {
    config.setFrom("$projectDir/config/detekt.yml")
}
```

---

## CI/CD 통합

```yaml
# .github/workflows/ci.yml
name: CI

on: [push, pull_request]

jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
      - run: npm ci
      - run: npm run lint
      - run: npm run format:check

  sonarqube:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: sonarsource/sonarqube-scan-action@master
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 코드 품질 일관성 보장 | 초기 설정 비용 |
| 리뷰 시간 단축 | 규칙 충돌 가능성 (린터 vs 포매터) |
| 버그 사전 예방 | 과도한 규칙은 생산성 저하 |
| 신규 팀원 온보딩 용이 | 팀 합의 필요 |

## 트러블슈팅

### 사례 1: ESLint와 Prettier 규칙 충돌

#### 증상
```
error: Delete `·` (prettier/prettier)
error: Insert `;` (prettier/prettier)
```

ESLint와 Prettier가 같은 파일에서 서로 다른 수정을 요구

#### 원인 분석
ESLint에도 스타일 규칙이 있어 Prettier와 충돌

#### 해결 방법
```bash
npm install eslint-config-prettier --save-dev
```

```javascript
// .eslintrc.js
module.exports = {
  extends: [
    'eslint:recommended',
    'prettier'  // 반드시 마지막에 추가
  ],
};
```

#### 예방 조치
- ESLint는 로직 검사만, Prettier는 스타일만 담당하도록 역할 분리
- `eslint-config-prettier`로 충돌 규칙 자동 비활성화

---

### 사례 2: pre-commit hook이 너무 느림

#### 증상
```
커밋할 때마다 30초 이상 대기
```

#### 원인 분석
전체 파일을 검사하거나, 무거운 검사를 모두 실행

#### 해결 방법
```json
// lint-staged만 사용 (변경된 파일만 검사)
{
  "lint-staged": {
    "*.{js,ts}": ["eslint --fix", "prettier --write"]
  }
}
```

#### 예방 조치
- **lint-staged** 사용: 변경된 파일만 검사
- 무거운 검사(SonarQube, 테스트)는 CI에서 실행
- 로컬에서는 빠른 검사만 수행

## 면접 예상 질문

### Q: 린터와 포매터의 차이점은 무엇인가요?

A: **린터**는 코드의 **논리적 문제**(미사용 변수, 타입 오류, 보안 취약점)를 검출하고, **포매터**는 코드의 **형식적 스타일**(들여쓰기, 줄바꿈, 따옴표)을 통일합니다. 린터는 "이 코드가 올바른가?"를, 포매터는 "이 코드가 일관된 스타일인가?"를 검사합니다.

### Q: 코드 품질을 위해 어떤 도구를 사용해 보셨나요?

A: JavaScript 프로젝트에서 **ESLint**로 코드 규칙을 강제하고, **Prettier**로 스타일을 통일했습니다. **Husky + lint-staged**로 커밋 전 자동 검사를 설정했고, CI에서는 **SonarQube**로 기술 부채와 보안 취약점을 모니터링했습니다. 이를 통해 PR 리뷰에서 스타일 지적 대신 로직에 집중할 수 있었고, 코드 품질 점수가 A등급을 유지했습니다.

### Q: Git Hooks를 왜 사용하나요?

A: **Shift Left** 원칙을 적용하기 위해서입니다. 문제를 CI 서버가 아닌 개발자의 로컬 환경에서 최대한 빨리 발견하면, 피드백 루프가 짧아지고 수정 비용이 줄어듭니다. 또한 팀 전체가 동일한 검사를 거치도록 강제할 수 있어 코드 품질 일관성을 보장합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [클린 코드](./clean-code.md) | 코드 품질의 기준 | 중급 |
| [TDD](./tdd.md) | 테스트를 통한 품질 보장 | 중급 |
| [리팩토링](./refactoring.md) | 코드 개선 기법 | 중급 |

## 참고 자료

- [ESLint 공식 문서](https://eslint.org/)
- [Prettier 공식 문서](https://prettier.io/)
- [SonarQube 공식 문서](https://docs.sonarsource.com/sonarqube/)
- [Husky 공식 문서](https://typicode.github.io/husky/)
- [pre-commit 공식 문서](https://pre-commit.com/)
