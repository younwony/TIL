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
├── .claude/
│   └── skills/            # Claude Code Skills
├── cs/                    # CS 지식 (마크다운 중심)
│   └── {category}/        # kebab-case
├── cs-web/                # CS 문서 웹 뷰어 (Spring Boot)
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

## Claude Code Skills

사용 가능한 스킬 목록입니다. 키워드로 호출할 수 있습니다.

| 스킬 | 트리거 키워드 | 설명 |
|------|--------------|------|
| `cs-guide-writer` | "오늘의 CS", "CS 정리", "{주제} 정리해줘" | CS 학습 문서 작성 |
| `cs-sync` | "CS 동기화", "가이드 반영" | CS 문서 템플릿 동기화 |
| `cs-link-sync` | "링크 동기화", "깨진 링크 수정", "링크 체크" | CS 문서 링크 검증 및 수정 |

## 참고 문서

- [CONVENTION.md](./CONVENTION.md): 상세 컨벤션 가이드
- [cs/CS-GUIDE.md](./cs/CS-GUIDE.md): CS 문서 작성 가이드
- [study/README.md](./study/README.md): 스터디 목록
- [.claude/skills/](./.claude/skills/): Claude Code Skills (특정 워크플로우)
