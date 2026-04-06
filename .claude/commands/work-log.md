---
description: Work Log - Confluence 작업 문서화
allowed-tools: Bash(git:*), Read, Glob, Grep
---

# Work Log - Confluence 작업 문서화

현재 브랜치에서 작업한 내용을 Confluence에 문서화해줘.
**비개발자도 이해할 수 있도록** 표(Table)와 ASCII 다이어그램을 포함한다.

## 사용법

```
/work-log                              # 기본: 개인 스페이스 > WORK-LOG 페이지 하위에 작성
/work-log --parent <pageId>            # 지정한 페이지 ID 하위에 작성
/work-log --parent "<페이지 제목>"       # 지정한 제목의 페이지 하위에 작성 (개인 스페이스 내 검색)
```

## 설정

| 항목 | 값 |
|------|------|
| **Confluence Cloud ID** | 1d77eaeb-5f74-4e36-8f0c-1d7ffc53faf9 |
| **개인 스페이스 ID** | 1983741954 |
| **개인 스페이스 키** | ~645023757 |
| **홈페이지 ID** | 1983742135 |
| **WORK-LOG 페이지 ID** | 3255435270 |
| **기본 부모 페이지** | "WORK-LOG" (ID: 3255435270, 홈페이지 하위) |

### 페이지 계층 구조
```
개인 스페이스 홈 (1983742135)
└── WORK-LOG (3255435270)        ← 기본 부모 페이지
    ├── TECH-21436               ← 작업 로그 페이지
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
- **어떻게 동작하는지** 흐름을 SVG 다이어그램과 표로 작성

## 3단계: Confluence 페이지 생성

수집한 정보를 바탕으로 Confluence 개인 스페이스에 작업 로그 페이지를 생성한다.

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

(SVG 다이어그램으로 시각화 — Confluence HTML 매크로로 삽입)

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

(SVG 다이어그램으로 레이어별 구조 시각화)

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

> 이 문서는 Claude Code `/work-log` skill로 자동 생성되었습니다.
```

> 관련 없는 섹션은 생략 가능 (예: DB 변경이 없으면 데이터베이스 섹션 생략, 테스트가 없으면 테스트 섹션 생략)

## 4단계: 부모 페이지 결정

### Argument 파싱

`$ARGUMENTS`에서 `--parent` 옵션을 확인한다:

| 케이스 | 입력 | 부모 페이지 결정 |
|--------|------|-----------------|
| **기본** (argument 없음) | `/work-log` | "WORK-LOG" 페이지 (홈페이지 하위, 없으면 자동 생성) |
| **pageId 지정** | `/work-log --parent 1234567890` | 해당 ID의 페이지 |
| **제목 지정** | `/work-log --parent "프로젝트 문서"` | 개인 스페이스에서 해당 제목의 페이지 검색 |

### 기본 동작: WORK-LOG 부모 페이지 사용

1. **기본 parentId**: `3255435270` (WORK-LOG 페이지)
2. curl로 페이지 존재 확인 (아래 6단계 참조)
3. **있으면** → 해당 pageId(3255435270)를 부모로 사용
4. **없으면** (삭제된 경우) → 자동 재생성:
   - spaceId: 1983741954, parentId: 1983742135 (홈페이지)
   - title: "WORK-LOG"
5. 생성된/조회된 pageId를 **parentId**로 사용

### --parent 지정 시 동작

1. **숫자만 입력** (pageId): curl로 페이지 존재 확인
2. **문자열 입력** (제목): curl로 spaceId + title 검색
3. 페이지를 찾지 못하면 **오류 메시지 출력 후 중단** (자동 생성하지 않음)

## 5단계: 미리보기 및 사용자 승인

페이지를 생성하기 **전에**, 작성될 문서의 미리보기를 사용자에게 제시하고 승인을 받는다.

### 미리보기 출력 형식

```
📋 Confluence 작업 로그 미리보기
━━━━━━━━━━━━━━━━━━━━━━━━━━━━

📌 페이지 정보
  - 타이틀: {TECH-XXXXX}
  - 부모 페이지: {부모 페이지명} ({parentId})
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

- 사용자가 승인(`Y`, `yes`, `진행`, `ㅇ`, Enter 등)하면 → 6단계로 진행
- 사용자가 거부하면 → 수정 요청 사항을 반영하여 미리보기를 다시 제시
- 사용자가 특정 섹션 추가/제거를 요청하면 → 반영 후 재확인

## 6단계: curl로 Confluence 페이지 생성

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

**Step 2: 부모 페이지 확인 (기본: WORK-LOG)**
```bash
# WORK-LOG 페이지 존재 확인
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages/3255435270"
```
- 성공하면 → parentId: 3255435270
- 404 응답이면 → WORK-LOG 페이지를 홈페이지(1983742135) 하위에 재생성

**Step 3: 기존 작업 로그 페이지 확인**
```bash
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages?spaceId=1983741954&title={TECH-XXXXX}&limit=1"
```
- `results` 배열이 비어있으면 → 신규 생성
- `results` 배열에 데이터가 있으면 → 업데이트 (pageId, version 추출)

**Step 4a: 신규 페이지 생성**
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
  "parentId": "{4단계에서 결정된 부모 페이지 ID, 기본: 3255435270}",
  "status": "current",
  "title": "{TECH-XXXXX}",
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

**Step 4b: 기존 페이지 업데이트**
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

### 2. SVG 다이어그램 우선 사용
- **처리 흐름**, **시스템 아키텍처**, **데이터 흐름** 등 복잡한 구조는 SVG 다이어그램으로 시각화
- `svg-diagram` 스킬의 템플릿/팔레트 적용 (그라디언트, 드롭섀도우 등)
- SVG는 Confluence HTML 매크로(`ac:structured-macro ac:name="html"`)로 인라인 삽입
- 간단한 트리 구조, 폴더 계층 등은 ASCII 코드 블록 사용 가능 (폴백)

### 3. SVG → Confluence 삽입 방법
Confluence Storage Format에서 SVG를 삽입하려면 HTML 매크로를 사용한다:
```xml
<ac:structured-macro ac:name="html">
  <ac:plain-text-body><![CDATA[
    <div style="text-align:center;">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 400" width="800" height="400">
        <!-- SVG 다이어그램 내용 -->
      </svg>
    </div>
  ]]></ac:plain-text-body>
</ac:structured-macro>
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
- **기본 스페이스는 개인 스페이스** - 특별한 조건이 없으면 개인 스페이스에 작성
- **Jira 상태(Status)는 포함하지 않음** - QA Ready, In Progress 등 상태는 자주 변경되므로 문서에 포함하지 않음
- **복잡한 흐름/아키텍처는 SVG 다이어그램으로 시각화** (svg-diagram 스킬 사용, Confluence HTML 매크로로 삽입)
- **간단한 트리/목록 구조는 ASCII 코드 블록(\`\`\`) 사용 가능**
- 관련 없는 섹션은 생략 가능 (예: DB 변경이 없으면 데이터베이스 섹션 생략)
