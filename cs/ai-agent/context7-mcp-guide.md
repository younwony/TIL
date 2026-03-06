# Context7 MCP 서버 활용 가이드

## 개요

Context7은 **라이브러리/프레임워크의 최신 공식 문서를 AI 컨텍스트에 실시간으로 가져오는 MCP 서버**다. AI 모델의 학습 시점(knowledge cutoff) 한계를 극복하여, 최신 API 변경사항과 정확한 사용법을 기반으로 코드를 생성할 수 있게 한다.

- **제공:** [Upstash](https://upstash.com/)
- **패키지:** `@upstash/context7-mcp` (npm)
- **라이선스:** MIT
- **가격:** 무료 플랜 제공 (rate limit 존재), 유료 플랜으로 확장 가능

---

## 왜 필요한가

### AI 코딩 도구의 한계

```
사용자: "Spring Boot 3.4의 RestClient로 GET 요청 보내는 코드 알려줘"
AI (Context7 없이): 학습 시점 기준의 오래된 API로 답변 → deprecated 메서드 추천 가능
AI (Context7 있음): 최신 공식 문서에서 정확한 API를 조회 → 정확한 코드 생성
```

### 해결하는 문제

| 문제 | Context7의 해결 방식 |
|------|---------------------|
| 모델이 구버전 API를 추천 | 최신 공식 문서를 실시간 조회 |
| deprecated 메서드 사용 | 현재 권장 방식을 문서에서 직접 가져옴 |
| 새로 추가된 기능을 모름 | 라이브러리의 최신 변경사항 반영 |
| 프레임워크별 미묘한 차이 | 버전별 정확한 문서 제공 |

---

## 설치 및 설정

### 사전 준비

- Node.js 18+ 설치 필요 (npx 사용)
- API 키 발급: https://context7.com/dashboard (무료)

### 방법 1: 원격 서버 + API 키 (권장)

```bash
claude mcp add --scope user \
  --header "CONTEXT7_API_KEY: YOUR_API_KEY" \
  --transport http \
  context7 https://mcp.context7.com/mcp
```

- `--scope user`: 모든 프로젝트에서 사용 (생략 시 현재 프로젝트만)
- 원격 서버이므로 npx 실행 불필요, 더 빠름

### 방법 2: 원격 서버 + OAuth (API 키 없이)

```bash
claude mcp add --scope user \
  --transport http \
  context7 https://mcp.context7.com/mcp/oauth
```

설치 후 Claude Code에서 `/mcp` 명령으로 OAuth 인증 진행

### 방법 3: 로컬 서버 + API 키

```bash
claude mcp add --scope user context7 -- npx -y @upstash/context7-mcp --api-key YOUR_API_KEY
```

또는 환경변수 사용:

```bash
# 환경변수 설정 (bash)
export CONTEXT7_API_KEY=YOUR_API_KEY

# 환경변수 설정 (Windows PowerShell)
$env:CONTEXT7_API_KEY = "YOUR_API_KEY"

# API 키 플래그 생략 가능
claude mcp add --scope user context7 -- npx -y @upstash/context7-mcp
```

### 설치 확인

```bash
# MCP 서버 목록 확인
claude mcp list

# 또는 Claude Code 내에서
/mcp
```

### 설정 파일 구조

설정 후 `~/.claude/settings.json`에 추가되는 내용:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp", "--api-key", "YOUR_API_KEY"]
    }
  }
}
```

원격 서버 방식의 경우:

```json
{
  "mcpServers": {
    "context7": {
      "type": "http",
      "url": "https://mcp.context7.com/mcp",
      "headers": {
        "CONTEXT7_API_KEY": "YOUR_API_KEY"
      }
    }
  }
}
```

---

## 제공 도구 (Tools)

### 1. resolve-library-id

라이브러리 이름을 Context7 ID로 변환한다.

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `query` | string | O | 검색할 라이브러리 이름 |

**반환값:** Context7 호환 라이브러리 ID 목록 (예: `/vercel/next.js`, `/spring-projects/spring-boot`)

**사용 예시:**

```
resolve-library-id(query: "spring boot")
→ /spring-projects/spring-boot

resolve-library-id(query: "react")
→ /facebook/react
```

### 2. query-docs

특정 라이브러리의 최신 문서를 검색한다.

| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| `libraryId` | string | O | Context7 라이브러리 ID (resolve-library-id로 얻은 값) |
| `query` | string | O | 검색할 내용/토픽 |

**반환값:** 해당 라이브러리의 최신 공식 문서에서 관련 내용 추출

**사용 예시:**

```
query-docs(libraryId: "/vercel/next.js", query: "app router middleware")
→ Next.js 최신 버전의 App Router 미들웨어 공식 문서 내용 반환
```

---

## 사용 방법

### 기본 사용: 프롬프트에 "use context7" 추가

```
# 프롬프트 끝에 "use context7" 키워드 추가
"Next.js 14 미들웨어 설정 방법 알려줘. use context7"

# 특정 라이브러리 직접 지정
"use library /supabase/supabase 로 인증 구현해줘"
```

### 슬래시 명령어: `/context7:docs`

```bash
# 토픽 기반 검색
/context7:docs react hooks

# 라이브러리 ID 직접 지정
/context7:docs /vercel/next.js app router

# Spring Boot 검색
/context7:docs spring-boot RestClient
```

### 에이전트 모드: 컨텍스트 절약

```
# 별도 에이전트에서 문서 검색 (메인 컨텍스트 오염 방지)
spawn docs-researcher to look up React hooks documentation
```

---

## CLAUDE.md 자동 트리거 설정

프로젝트의 CLAUDE.md에 규칙을 추가하면, "use context7"을 매번 입력하지 않아도 자동으로 최신 문서를 조회한다.

### 예시: 특정 프레임워크에 대해 항상 Context7 사용

```markdown
# CLAUDE.md

## Context7 규칙

- Spring Boot 관련 코드를 작성할 때는 항상 Context7으로 최신 문서를 확인할 것
- 라이브러리 API 사용 시 Context7으로 현재 버전의 정확한 사용법을 조회할 것
- deprecated 경고가 있는 코드를 발견하면 Context7으로 대체 API를 확인할 것
```

### 예시: 스킬(Skill)에서 자동 트리거

```markdown
# .claude/skills/api-lookup.md

---
description: 라이브러리 API 질문 시 자동으로 Context7에서 최신 문서 조회
---

1. resolve-library-id로 라이브러리 ID 확인
2. query-docs로 관련 문서 조회
3. 최신 문서 기반으로 정확한 코드 생성
```

---

## 실전 활용 시나리오

### 시나리오 1: Spring Boot 프로젝트 개발

```
사용자: "Spring Boot 3.4에서 RestClient로 외부 API 호출하는 서비스 만들어줘. use context7"

Claude Code 내부 동작:
1. resolve-library-id("spring boot") → /spring-projects/spring-boot
2. query-docs("/spring-projects/spring-boot", "RestClient HTTP request")
3. 최신 문서 기반으로 정확한 RestClient 코드 생성
```

### 시나리오 2: 새 프레임워크 빠르게 학습

```
사용자: "Hono 프레임워크로 REST API 만들어줘. use context7"

1. resolve-library-id("hono") → /honojs/hono
2. query-docs("/honojs/hono", "REST API routing middleware")
3. Hono 최신 버전의 라우팅, 미들웨어 패턴으로 코드 생성
```

### 시나리오 3: 버전 마이그레이션

```
사용자: "이 코드를 Next.js 15로 마이그레이션해줘. use context7"

1. query-docs("/vercel/next.js", "migration guide v15 breaking changes")
2. 변경된 API, 제거된 기능 확인
3. 정확한 마이그레이션 코드 생성
```

### 시나리오 4: deprecated API 대체

```
사용자: "이 코드에서 deprecated 경고가 뜨는데 수정해줘. use context7"

1. 경고 메시지에서 라이브러리 식별
2. query-docs로 현재 권장 대체 API 조회
3. deprecated 코드를 최신 API로 교체
```

---

## 다른 도구와의 비교

| 항목 | Context7 | 직접 웹 검색 | AI 학습 데이터 |
|------|----------|-------------|---------------|
| 최신성 | 실시간 공식 문서 | 검색 품질에 의존 | 학습 시점까지 |
| 정확성 | 공식 문서 기반 | 블로그/Stack Overflow 혼재 | 학습 데이터 품질에 의존 |
| 속도 | MCP 도구 호출 1회 | 여러 페이지 탐색 필요 | 즉시 (하지만 부정확할 수 있음) |
| 토큰 효율 | 관련 내용만 추출 | 전체 페이지 읽기 | 추가 토큰 불필요 |
| 버전 특정 | 버전별 문서 조회 가능 | 버전 혼재 가능 | 학습 시점의 버전 |

---

## 지원 라이브러리 범위

Context7은 GitHub 기반의 오픈소스 라이브러리를 광범위하게 지원한다.

### 주요 지원 분야

- **프론트엔드:** React, Next.js, Vue, Svelte, Angular, Astro 등
- **백엔드:** Spring Boot, Express, Hono, FastAPI, Django 등
- **데이터베이스:** Prisma, Drizzle, TypeORM, Supabase 등
- **인프라:** Docker, Kubernetes, Terraform 등
- **AI/ML:** LangChain, LlamaIndex, Anthropic SDK 등
- **기타:** Tailwind CSS, Playwright, Vitest 등

### 개인 저장소 추가

공개되지 않은 내부 라이브러리의 문서도 추가할 수 있다 (유료 플랜).

---

## 팁과 베스트 프랙티스

### 효과적인 사용법

1. **구체적인 쿼리 작성:** "react" 보다 "react server components data fetching" 이 더 정확한 결과
2. **라이브러리 ID 직접 지정:** `use library /vercel/next.js`로 모호함 제거
3. **에이전트 모드 활용:** 긴 문서 검색은 별도 에이전트에 위임하여 메인 컨텍스트 보존
4. **CLAUDE.md에 규칙 추가:** 프로젝트에서 주로 쓰는 프레임워크에 대해 자동 조회 설정

### 주의사항

1. **rate limit:** 무료 플랜에는 호출 횟수 제한이 있음. 빈번한 조회 시 유료 플랜 검토
2. **문서 범위:** 모든 라이브러리를 지원하지는 않음. resolve-library-id로 먼저 확인
3. **네트워크 의존:** 오프라인 환경에서는 사용 불가 (로컬 서버도 외부 API 호출 필요)
4. **보안:** API 키를 코드에 직접 포함하지 않도록 환경변수 사용 권장

---

## 문제 해결

### MCP 서버 연결 실패

```bash
# MCP 서버 상태 확인
claude mcp list

# 서버 재시작
claude mcp remove context7
claude mcp add --scope user context7 -- npx -y @upstash/context7-mcp --api-key YOUR_API_KEY
```

### API 키 관련 오류

```bash
# 환경변수 확인
echo $CONTEXT7_API_KEY

# API 키 재발급: https://context7.com/dashboard
```

### 라이브러리를 찾을 수 없는 경우

```
# 1. 정확한 이름으로 다시 검색
resolve-library-id("spring-boot")  # "spring" 보다 구체적으로

# 2. GitHub 경로로 직접 시도
use library /spring-projects/spring-boot
```

---

## 실제 설정 기록

### 이 프로젝트에서의 설정 (2026-03-06)

```bash
# OAuth 방식으로 설정 (API 키 불필요)
claude mcp add --scope user --transport http context7 https://mcp.context7.com/mcp/oauth

# 결과: ~/.claude.json에 등록됨
# Added HTTP MCP server context7 with URL: https://mcp.context7.com/mcp/oauth to user config
```

설정 후 첫 사용 시 Claude Code에서 `/mcp` 명령으로 OAuth 인증을 완료해야 한다.

### 사용 예제: Spring Boot RestClient 문서 조회

```
# Claude Code에서 입력
"Spring Boot 3.4의 RestClient로 GET 요청하는 코드 작성해줘. use context7"

# Context7 내부 동작
1. resolve-library-id(query: "spring boot")
   → libraryId: "/spring-projects/spring-boot"

2. query-docs(libraryId: "/spring-projects/spring-boot", query: "RestClient GET request")
   → Spring Boot 3.4 공식 문서에서 RestClient 관련 내용 반환

3. 최신 문서 기반으로 정확한 코드 생성
```

### 사용 예제: Next.js App Router 마이그레이션

```
# Claude Code에서 입력
"/context7:docs /vercel/next.js app router migration"

# 또는 자연어로
"Next.js 15 App Router 마이그레이션 가이드 확인해줘. use context7"
```

---

## 참고 자료

- 공식 사이트: https://context7.com
- GitHub: https://github.com/upstash/context7
- npm 패키지: https://www.npmjs.com/package/@upstash/context7-mcp
- API 키 발급: https://context7.com/dashboard
- 클라이언트별 설정 가이드: https://context7.com/docs/resources/all-clients
