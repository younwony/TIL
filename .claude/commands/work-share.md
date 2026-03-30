---
description: Work Share - 공유용 Confluence 작업 문서화
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Work Share - 공유용 Confluence 작업 문서화

현재 브랜치에서 작업한 내용을 Confluence 공유 페이지 하위에 문서화해줘.
작업 내용과 문서 형식은 `/work-log`와 동일하며, **팀에 공유할 만한 내용**을 빠르게 올리기 위한 커맨드이다.

## 사용법

```
/work-share                            # 공유 페이지(3255664674) 하위에 작성
```

## 설정

| 항목 | 값 |
|------|------|
| **Confluence Cloud ID** | 1d77eaeb-5f74-4e36-8f0c-1d7ffc53faf9 |
| **개인 스페이스 ID** | 1983741954 |
| **개인 스페이스 키** | ~645023757 |
| **홈페이지 ID** | 1983742135 |
| **공유 부모 페이지 ID** | 3255664674 |

### 페이지 계층 구조
```
개인 스페이스 홈 (1983742135)
└── 공유 페이지 (3255664674)     ← 고정 부모 페이지
    ├── TECH-21436               ← 공유 작업 문서
    ├── TECH-21437
    └── ...
```

### 페이지 타이틀 규칙
- **타이틀 형식**: `{TECH-XXXXX}` (Jira 이슈 번호만 사용)
- **예시**: `TECH-21436`
- **이유**: 브랜치명이 변경되어도 동일한 페이지를 수정할 수 있도록 함

## 1단계: 현재 브랜치 정보 수집

다음 명령어들을 **병렬로** 실행하여 정보를 수집한다:

```bash
# 브랜치명
git branch --show-current
```

```bash
# master 이후 커밋 목록
git log master..HEAD --oneline --no-decorate
```

```bash
# 변경된 파일 통계
git diff master..HEAD --stat
```

```bash
# 상세 변경 내용 요약
git diff master..HEAD --stat --summary
```

## 2단계: 코드 분석 및 이해

변경된 주요 파일들을 읽고 분석하여:
- 이 작업이 **무엇을 하는지** 파악
- **왜 필요한지** 배경 설명 작성
- **어떻게 동작하는지** 흐름을 ASCII 다이어그램과 표로 작성

## 3단계: Confluence 페이지 생성

수집한 정보를 바탕으로 공유 페이지 하위에 작업 문서를 생성한다.

### 문서 구조 (템플릿)

```markdown
# {작업 제목}

---

## 한눈에 보기

| 구분 | 내용 |
|------|------|
| **이슈 번호** | {TECH-XXXXX} |
| **작업 일시** | {YYYY-MM-DD} |
| **작업자** | 윤원희 |
| **브랜치** | {branch_name} |
| **변경 파일 수** | {file_count}개 |
| **추가/삭제 라인** | +{additions} / -{deletions} |

---

## 이 작업은 무엇인가요?

### 배경 및 목적
{비개발자도 이해할 수 있는 배경 설명}

### 핵심 용어 설명
| 용어 | 설명 |
|------|------|
| **{용어1}** | {쉬운 설명} |
| **{용어2}** | {쉬운 설명} |

---

## 주요 변경사항

### 1. {기능1 제목}

#### 처리 흐름 다이어그램

(ASCII 박스 다이어그램으로 시각화)

#### 상세 처리 단계

| 순서 | 단계 | 처리 내용 | 관련 파일 |
|:----:|------|----------|-----------|
| 1 | {단계명} | {설명} | `{파일명}` |

#### 왜 필요한가요?

| 기존 문제 | 해결 방법 | 기대 효과 |
|-----------|----------|----------|
| {문제 설명} | {해결 방법} | {효과} |

---

## 시스템 아키텍처

(레이어별 ASCII 다이어그램)

### 레이어별 역할

| 레이어 | 컴포넌트 | 파일 | 역할 |
|:------:|----------|------|------|
| **Controller** | {파일명} | `{경로}` | {역할} |
| **Service** | {파일명} | `{경로}` | {역할} |

---

## 주요 파일 변경 내역

### 신규 생성 파일
| 구분 | 파일 경로 | 역할 |
|------|----------|------|
| {타입} | `{경로}` | {역할} |

### 수정된 파일
| 구분 | 파일 경로 | 변경 내용 |
|------|----------|----------|
| {타입} | `{경로}` | {변경 내용 요약} |

---

## 코드 하이라이트

### 핵심 로직: {기능명}
(코드 스니펫 + 설명)

---

## 관련 링크

| 구분 | 링크 |
|------|------|
| **Jira 이슈** | [TECH-XXXXX](https://temcolabs.atlassian.net/browse/TECH-XXXXX) |
| **PR** | {PR 링크} |

---

> 이 문서는 Claude Code `/work-share` skill로 자동 생성되었습니다.
```

> 관련 없는 섹션은 생략 가능 (예: DB 변경이 없으면 데이터베이스 섹션 생략, 테스트가 없으면 테스트 섹션 생략)

## 4단계: 미리보기 및 사용자 승인

페이지를 생성하기 **전에**, 작성될 문서의 미리보기를 사용자에게 제시하고 승인을 받는다.

### 미리보기 출력 형식

```
📋 Confluence 공유 문서 미리보기
━━━━━━━━━━━━━━━━━━━━━━━━━━━━

📌 페이지 정보
  - 타이틀: {TECH-XXXXX}
  - 부모 페이지: 공유 페이지 (3255664674)
  - 동작: {신규 생성 | 기존 페이지 업데이트}

📝 문서 구성
  1. 한눈에 보기 (이슈 번호, 작업일시, 브랜치, 변경 파일 수)
  2. 이 작업은 무엇인가요? (배경 및 목적, 핵심 용어)
  3. 주요 변경사항 ({변경 기능 목록 요약})
  4. 시스템 아키텍처 (레이어별 역할)
  5. 주요 파일 변경 내역 (신규 {N}개, 수정 {M}개)
  6. 코드 하이라이트
  7. 관련 링크

📊 요약
  - 커밋 수: {N}개
  - 변경 파일: {M}개 (+{additions} / -{deletions})
  - 주요 변경: {핵심 변경사항 1줄 요약}

진행하시겠습니까? (Y/n)
```

### 승인 규칙

- 사용자가 승인(`Y`, `yes`, `진행`, `ㅇ`, Enter 등)하면 → 5단계로 진행
- 사용자가 거부하면 → 수정 요청 사항을 반영하여 미리보기를 다시 제시
- 사용자가 특정 섹션 추가/제거를 요청하면 → 반영 후 재확인

## 5단계: curl로 Confluence 페이지 생성

curl을 사용하여 Atlassian REST API를 직접 호출한다.

### 인증 정보

```
Base URL (Jira): https://temcolabs.atlassian.net/rest/api/3
Base URL (Confluence): https://temcolabs.atlassian.net/wiki/api/v2
인증: Basic Auth (wonhee.youn@temco.io:{API_TOKEN})
```

- API 토큰은 `.claude.json`의 `mcpServers.atlassian.env.ATLASSIAN_API_TOKEN`에서 가져옴

### curl 프로세스

**Step 1: Jira 이슈 조회**
```bash
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{TECH-XXXXX}"
```

**Step 2: 기존 작업 문서 확인**
```bash
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages?spaceId=1983741954&title={TECH-XXXXX}&limit=1"
```
- `results` 배열이 비어있으면 → 신규 생성
- `results` 배열에 데이터가 있으면 → 업데이트 (pageId, version 추출)

**Step 3a: 신규 페이지 생성**
```bash
curl -s -X POST -H "Content-Type: application/json" \
  -u "{EMAIL}:{API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

JSON 구조:
```json
{
  "spaceId": "1983741954",
  "parentId": "3255664674",
  "status": "current",
  "title": "{TECH-XXXXX}",
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

**Step 3b: 기존 페이지 업데이트**
```bash
curl -s -X PUT -H "Content-Type: application/json" \
  -u "{EMAIL}:{API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages/{pageId}"
```

JSON 구조:
```json
{
  "id": "{pageId}",
  "status": "current",
  "title": "{TECH-XXXXX}",
  "version": {
    "number": "{현재버전 + 1}",
    "message": "작업 내용 업데이트"
  },
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

### 주의사항
- API 토큰은 `.claude.json`에서 읽어와 사용 (하드코딩 금지)
- curl 응답에서 `_links.webui`를 추출하여 사용자에게 페이지 URL 제공
- JSON 파일은 `/tmp/confluence_page.json`에 임시 저장 후 전송
- JSON 파일 내 특수문자(따옴표, 꺽쇠 등)는 적절히 이스케이프 처리
- **bodyValue는 반드시 Confluence Storage Format (XML)** 사용 (wiki markup, markdown 사용 금지)

## 문서 작성 원칙

### 1. 비개발자 친화적 작성
- 기술 용어는 반드시 쉬운 설명 추가
- 도메인 지식이 없어도 이해할 수 있도록 배경 설명 포함
- "왜 필요한가요?", "어떻게 동작하나요?" 형식 사용

### 2. ASCII 다이어그램 필수 사용
- **처리 흐름**은 ASCII 박스 다이어그램으로 시각화
- **시스템 아키텍처**는 레이어별 ASCII 다이어그램으로 표현
- **테이블 스키마**는 ASCII 테이블로 표현
- Confluence 코드 블록 내에서 깔끔하게 표시됨

### 3. ASCII 다이어그램 가이드
```
# 박스 그리기
┌─────────┐  ╔═════════╗
│  내용   │  ║  강조   ║
└─────────┘  ╚═════════╝

# 화살표
─▶  (오른쪽)    ◀─  (왼쪽)
│▼  (아래)      ▲│  (위)

# 연결선
─────  (가로선)   │  (세로선)
├──   (T 분기)    └──  (L 코너)
```

### 4. 표(Table) 기반 정보 정리
- 비교 정보는 다열 표로 정리
- 레이어별 역할 설명은 표로 정리
- 변경 전/후, 문제/해결, 기존/개선 구조 활용

### 5. 코드 스니펫 포함
- 핵심 로직은 코드 스니펫으로 포함
- 언어별 syntax highlighting 적용 (java, javascript, sql 등)
- 코드 앞에 설명 주석 추가

## 주의사항

- master 브랜치와 비교하여 작업 내용을 추출한다
- 브랜치명에서 이슈 키(예: TECH-12345)를 자동으로 추출한다
- **페이지 타이틀은 이슈 번호만 사용** (예: `TECH-21436`) - 브랜치명이 변경되어도 동일 페이지 유지
- **기존 페이지가 있으면 업데이트, 없으면 신규 생성** - 중복 페이지 방지
- **부모 페이지는 공유 페이지(3255664674) 고정**
- **Jira 상태(Status)는 포함하지 않음** - QA Ready, In Progress 등 상태는 자주 변경되므로 문서에 포함하지 않음
- **ASCII 다이어그램은 반드시 코드 블록(\`\`\`) 안에 작성하여 Confluence에서 깨지지 않도록 한다**
- 관련 없는 섹션은 생략 가능 (예: DB 변경이 없으면 데이터베이스 섹션 생략)
