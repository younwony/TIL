# CLAUDE.md

이 문서는 Claude Code가 TIL 저장소에서 작업할 때 참고하는 설정 및 규칙입니다.

## 저장소 개요

- **저장소 유형:** TIL (Today I Learned) - 학습 내용 정리
- **주요 언어:** Markdown, Java, Kotlin
- **구조:** CS 지식(마크다운) + Study(프로젝트 단위)

## Git 설정

### 기본 규칙

- **모든 작업에서 git add까지만 진행** (commit은 사용자 요청 시에만)
- 새로 생성/수정/삭제된 파일 모두 `git add`만 수행
- 커밋 메시지는 사용자가 명시적으로 요청할 때만 작성

### 커밋 메시지 형식

```
<type>: <subject>

<body> (선택)
```

| Type | 설명 | 예시 |
|------|------|------|
| `docs` | 문서 추가/수정 | `docs: Effective Java Item 1 추가` |
| `feat` | 새로운 기능/내용 | `feat: 알고리즘 문제 풀이 추가` |
| `fix` | 오류 수정 | `fix: 오타 수정` |
| `refactor` | 구조 변경 | `refactor: 폴더 구조 정리` |
| `chore` | 기타 작업 | `chore: gitignore 수정` |

## 폴더 구조 규칙

```
TIL/
├── README.md              # 저장소 소개
├── CONVENTION.md          # 컨벤션 가이드
├── CLAUDE.md              # Claude 설정 (이 문서)
├── cs/                    # CS 지식 (마크다운 중심)
│   └── {category}/        # kebab-case
└── study/                 # 스터디 (프로젝트 단위)
    └── {study-name}/      # kebab-case
```

## 네이밍 컨벤션

### 폴더명

| 규칙 | 올바른 예 | 잘못된 예 |
|------|----------|----------|
| kebab-case 사용 | `effective-java` | `effectiveJava`, `Effective_Java` |
| 영문 사용 | `clean-coder` | `클린코더` |
| 단수형 사용 | `algorithm` | `algorithms` |

### 파일명

| 타입 | 규칙 | 예시 |
|------|------|------|
| 마크다운 | kebab-case | `item01.md`, `part01-professionalism.md` |
| Java 클래스 | PascalCase | `NutritionFacts.java` |
| 알고리즘 | `a{번호}_{문제명}.java` | `a1000_더하기.java` |

### 챕터/번호 형식

| 타입 | 형식 | 예시 |
|------|------|------|
| 챕터 | `chapter{00}` | `chapter02` |
| 주차 | `week{00}` | `week04` |
| 파트 | `part{00}` | `part01` |
| 아이템 | `item{00}` | `item01` |

## 마크다운 작성 규칙

### README.md 필수 요소

```markdown
# {Study Name} Study

{한 줄 설명}

> 원본 레포지토리: {URL} (있는 경우)

## 목차

### {Chapter/Part 제목}
- [{아이템 제목}]({상대경로})
```

### 개별 문서 필수 요소

```markdown
# {제목}

## 핵심 정리

{핵심 내용 요약}

## 본문

{상세 내용}

## 예제 코드 (있는 경우)

```java
// 코드
```
```

### 스타일 규칙

- H1은 파일당 1개만 사용
- 코드 블록에 언어 명시 (```java, ```bash 등)
- 링크는 상대 경로 사용
- 리스트는 하이픈(`-`) 사용

## 코드 작성 규칙

### Java

| 요소 | 규칙 |
|------|------|
| 클래스명 | PascalCase |
| 메서드/변수명 | camelCase |
| 상수명 | UPPER_SNAKE_CASE |
| 패키지명 | 소문자 |

### 일반 원칙

- 매직 넘버/문자열 사용 금지 → 상수로 정의
- 단일 책임 원칙(SRP) 준수
- 중복 코드 제거 (DRY 원칙)
- 명확하고 의미 있는 네이밍

## 작업 체크리스트

### 새 스터디 추가 시

- [ ] 폴더명: kebab-case 영문
- [ ] README.md 템플릿 준수
- [ ] 상위 README.md에 링크 추가
- [ ] 원본 레포지토리 링크 포함 (있는 경우)

### 새 문서 추가 시

- [ ] 파일명 규칙 준수
- [ ] H1 제목 포함
- [ ] 코드 블록에 언어 명시
- [ ] 상대 경로 링크 사용

### 작업 완료 시

- [ ] `git add .` 실행
- [ ] `git status`로 staging 확인
- [ ] commit은 사용자 요청 시에만

## CS 문서 작성 (Daily CS)

사용자가 "오늘의 CS", "CS 정리", 또는 특정 CS 주제를 요청하면 아래 절차를 따릅니다.

### 트리거 키워드

- "오늘의 CS: {주제}"
- "CS 정리: {주제}"
- "{주제} 정리해줘" (CS 관련 주제인 경우)
- "최근 이슈 CS" - 최신 기술 트렌드 주제 정리

### 작업 절차

1. **카테고리 판단**: 주제에 맞는 카테고리 선택
   - `network/` - 네트워크, HTTP, TCP/IP, DNS 등
   - `os/` - 프로세스, 메모리, 스케줄링 등
   - `db/` - 인덱스, 트랜잭션, SQL, NoSQL 등
   - `algorithm/` - 정렬, 탐색, DP, 그래프 등
   - `system-design/` - 확장성, 캐싱, MSA 등
   - `git/` - 버전 관리, 브랜치 전략, Git 내부 동작
   - `language/` - Java, Kotlin 등 언어별 심화 주제
   - `programming/` - OOP, API 설계, 디자인 패턴 등
   - `trend/` - AI Agent, MCP, LLM 등 최신 기술 트렌드

2. **파일 생성**: `cs/{category}/{topic}.md`
   - 파일명: kebab-case (예: `tcp-handshake.md`)

3. **내용 작성**: CS-GUIDE.md 템플릿 준수
   - 핵심 개념 (3-5개 불릿포인트)
   - 상세 설명 (소제목으로 구분)
   - 동작 원리 (해당 시)
   - 예제 코드 (해당 시)
   - 면접 예상 질문 (2-3개)
        - 상세하게

4. **README 업데이트**: `cs/{category}/README.md`에 링크 추가

5. **git add**: 생성/수정된 파일 staging

### 문서 품질 기준

| 항목 | 기준 |
|------|------|
| 정확성 | 공식 문서, 신뢰할 수 있는 출처 기반 |
| 간결성 | 핵심에 집중, 불필요한 내용 배제 |
| 실용성 | 면접 대비 + 실무 적용 가능 |
| 일관성 | 동일한 템플릿과 스타일 유지 |

### 예시

요청: "오늘의 CS: TCP 3-way handshake"

작업:
```
1. cs/network/tcp-handshake.md 생성
2. cs/network/README.md 업데이트
3. git add cs/network/
```

### 최근 이슈 CS (Trend CS)

"최근 이슈 CS" 요청 시 아래 절차를 따릅니다.

#### 작업 절차

1. **웹 검색**: 최신 기술 트렌드 조사
   - 최근 3-6개월 내 주목받는 기술/개념
   - 개발자 커뮤니티에서 화제가 되는 주제
   - 예: AI Agent, MCP, RAG, Vector DB, LLM Fine-tuning 등

2. **주제 선정**: 가장 관련성 높은 주제 1개 선택
   - 실무 적용 가능성
   - 개발자 면접 출제 가능성
   - 기술 트렌드 지속성

3. **문서 작성**: `cs/trend/{topic}.md`
   - 기본 템플릿 + 추가 섹션:
     - 등장 배경 / 왜 주목받는가
     - 기존 기술과의 비교
     - 실제 활용 사례
     - 향후 전망

4. **README 업데이트**: `cs/trend/README.md`에 링크 추가

5. **git add**: 생성/수정된 파일 staging

#### 예시 주제

| 분야 | 주제 예시 |
|------|----------|
| AI/ML | AI Agent, MCP, RAG, Vector DB, LLM Fine-tuning |
| 인프라 | Kubernetes Operators, eBPF, Service Mesh |
| 개발 도구 | Copilot, Cursor, Claude Code |
| 아키텍처 | Event Sourcing, CQRS, Serverless |

## 참고 문서

- [CONVENTION.md](./CONVENTION.md): 상세 컨벤션 가이드
- [cs/CS-GUIDE.md](./cs/CS-GUIDE.md): CS 문서 작성 가이드
- [study/README.md](./study/README.md): 스터디 목록
