---
description: Work Plan Start - 작업 명세서 기반 실행
allowed-tools: Bash(git:*), Read, Write, Glob, Grep
---

# Work Plan Start - 작업 명세서 기반 실행

WORK-SPEC.md 작업 명세서를 읽고 단계별로 실제 작업을 수행해줘.
반드시 `/work-plan`으로 WORK-SPEC.md를 먼저 생성한 후 사용한다.

## 실행 방법

```
/work-plan-start                       # 프로젝트 루트의 WORK-SPEC.md 사용
/work-plan-start path/to/WORK-SPEC.md  # 지정 경로의 WORK-SPEC.md 사용
```

## 0단계: WORK-SPEC.md 검증

`$ARGUMENTS`가 있으면 해당 경로, 없으면 프로젝트 루트의 `WORK-SPEC.md`를 읽는다.

**검증 항목:**
- WORK-SPEC.md 파일 존재 여부
- 필수 섹션 존재: "요구사항 요약", "작업 단계 (Phase)"
- Phase 목록이 비어있지 않은지 확인

검증 실패 시 `/work-plan`을 먼저 실행하도록 안내한다.

## 1단계: TaskCreate로 작업 목록 생성

WORK-SPEC.md의 "작업 단계 (Phase)" 섹션을 파싱하여 **TaskCreate** 도구로 작업 목록을 생성한다.

### 작업 생성 규칙

1. **Phase별로 그룹화**: 각 Phase를 상위 작업으로, 하위 항목을 세부 작업으로 생성
2. **의존성 설정**: Phase 간 순서 의존성을 `addBlockedBy`로 설정
3. **activeForm 필수**: 모든 작업에 현재 진행형 activeForm 포함

```
예시:
- Task #1: [Phase 1] Entity 클래스 생성 (activeForm: "Entity 클래스 생성 중")
- Task #2: [Phase 1] Service 로직 구현 (activeForm: "Service 로직 구현 중", blockedBy: [#1])
- Task #3: [Phase 2] 단위 테스트 작성 (activeForm: "단위 테스트 작성 중", blockedBy: [#2])
- Task #4: [Phase 3] 문서화 (activeForm: "문서 작성 중", blockedBy: [#3])
- Task #5: [Phase 4] git add + 완료 보고 (activeForm: "마무리 작업 중", blockedBy: [#4])
```

## 2단계: Phase 1 - 핵심 구현

WORK-SPEC.md의 설계에 따라 코드를 구현한다.

### CLAUDE.md 규칙 적용 체크리스트 (Java 프로젝트)

구현 중 다음을 반드시 확인하며 진행한다:

**OOP/클린코드:**
- 메서드 단일 책임 (한 가지 일만)
- 메서드 파라미터 3개 이하
- Early Return으로 중첩 축소
- null 대신 Optional (반환 타입만)

**코드 가독성:**
- 3개 이상 if-else → switch/Enum/Map
- StringUtils.hasText(), CollectionUtils.isEmpty() 활용
- 매직 넘버/문자열 → static final 상수 또는 Enum

**성능:**
- Pattern, ObjectMapper 등 → static final 캐싱
- 중첩 루프 → Map 활용 검색
- 반복문 내 String 덧셈 → StringBuilder
- 반복문 내 DB/API 호출 → Bulk 연산

**데이터 객체:**
- Entity 직접 노출 금지 → RequestDTO/ResponseDTO 분리
- DTO 불변 설계 (record 권장)
- Entity ↔ DTO 변환은 Mapper 또는 정적 팩토리 메서드

**JPA:**
- @ManyToOne, @OneToOne → FetchType.LAZY
- 컬렉션 조회 → Fetch Join 또는 EntityGraph
- 조회 전용 → @Transactional(readOnly = true)

**Lombok:**
- @Data 사용 금지
- @Getter + @NoArgsConstructor(access = PROTECTED) + @Builder 조합

**예외/로깅:**
- System.out.println 금지 → SLF4J
- 구체적 예외 처리 (Exception 잡기 금지)
- Custom Exception으로 비즈니스 예외 전달

**사용하지 않는 코드:**
- 요청된 기능에 필요한 최소한의 코드만 작성
- 기본 생성 메서드(toString, equals 등) 불필요 시 미생성
- 사용처 없는 유틸리티/헬퍼 미생성

## 3단계: Phase 2 - 테스트

### 단위 테스트
- WORK-SPEC.md의 "테스트 전략 > 단위 테스트" 항목을 기반으로 작성
- 우선순위 P1 항목부터 작성
- 핵심 비즈니스 로직 중심

### 통합 테스트
- WORK-SPEC.md의 "테스트 전략 > 통합 테스트" 항목을 기반으로 작성
- API 엔드포인트 검증 (Spring Boot API인 경우)
- 데이터 흐름 검증

### 테스트 실행 및 재시도

테스트 실행 후 실패가 발생하면:

1. **1차 실패**: 에러 분석 → 코드 수정 → 재실행
2. **2차 실패**: 다른 접근 방식으로 수정 → 재실행
3. **3차 실패**: 실패 내역을 사용자에게 보고하고 **AskUserQuestion**으로 진행 방향 질문

```
테스트 실패가 3회 반복되었습니다.

실패 테스트: {테스트명}
에러: {에러 내용}
시도한 수정: {수정 내역}

어떻게 진행할까요?
1. 테스트 스킵 후 계속 진행
2. 테스트 수정 방향 직접 지정
3. 작업 중단
```

## 4단계: Phase 3 - 문서화

CLAUDE.md 규칙에 따라 문서를 생성한다.

### ARCHITECTURE.md
- 시스템 아키텍처
- 데이터 흐름
- 핵심 로직 (ASCII 다이어그램)
- 파일 구조
- 관련 이슈 번호 (req.md에 있는 경우)

### SPEC.md
- 기능 설명
- 사용자 인터페이스 (해당 시)
- 데이터 흐름 (ASCII 다이어그램)
- 핵심 로직
- 관련 이슈 번호

**TIL 문서 프로젝트의 경우**: ARCHITECTURE.md, SPEC.md 대신 해당 문서 자체가 결과물이므로 이 단계를 건너뛴다.

## 5단계: Phase 4 - 마무리

### git add

```bash
git add {변경된 파일들}
git status
```

**주의**: commit은 수행하지 않는다 (사용자 요청 시에만).

### 완료 보고

```
## 작업 완료 보고

### 요약
- 기반 문서: {WORK-SPEC.md 경로}
- 프로젝트 유형: {유형}
- 소요 Phase: {N}단계

### 구현 결과

#### 생성/수정 파일
| 파일 | 작업 | 상태 |
|------|------|------|
| {파일 경로} | {신규/수정} | {완료/부분} |

#### 테스트 결과
- 단위 테스트: {통과}/{전체} 통과
- 통합 테스트: {통과}/{전체} 통과

#### 문서화
- ARCHITECTURE.md: {생성됨/해당 없음}
- SPEC.md: {생성됨/해당 없음}

### Git 상태
- staged 파일: {N}개
- commit 대기 중 (사용자 요청 시 커밋)

### 체크리스트 결과
- 오버엔지니어링 체크: {통과/주의 항목}
- 언더엔지니어링 체크: {통과/주의 항목}
- CLAUDE.md 규칙 준수: {통과/위반 항목}

### 알려진 제한사항 (있는 경우)
- {제한사항 또는 후속 작업 필요 사항}
```

## 프로젝트 유형별 Phase 조정

### TIL 문서 프로젝트

| Phase | 내용 |
|-------|------|
| Phase 1 | 마크다운 문서 작성 (템플릿 준수) |
| Phase 2 | 링크 검증, README 업데이트 |
| Phase 3 | git add + 완료 보고 |

### 알고리즘 프로젝트

| Phase | 내용 |
|-------|------|
| Phase 1 | 알고리즘 구현 |
| Phase 2 | 테스트 케이스 작성 및 실행 |
| Phase 3 | 시간/공간 복잡도 분석 문서화 |
| Phase 4 | git add + 완료 보고 |

### Spring Boot API 프로젝트

| Phase | 내용 |
|-------|------|
| Phase 1 | Entity, DTO, Repository, Service, Controller 구현 |
| Phase 2 | 단위 테스트 + 통합 테스트 |
| Phase 3 | ARCHITECTURE.md + SPEC.md 작성 |
| Phase 4 | git add + 완료 보고 |

## 주의사항

- **WORK-SPEC.md 우선**: WORK-SPEC.md에 명시된 설계를 따른다. 임의로 설계를 변경하지 않는다.
- **단계별 진행**: TaskCreate로 생성한 작업을 순서대로 진행하며, 각 작업 완료 시 TaskUpdate로 상태를 갱신한다.
- **실패 시 보고**: 구현 중 WORK-SPEC.md의 설계가 불가능한 경우 AskUserQuestion으로 사용자에게 대안을 제시한다.
- **commit 금지**: git add까지만 수행한다.
- **기존 패턴 존중**: 프로젝트의 기존 코드 스타일과 패턴을 따른다.
