---
description: Work Plan - 작업 명세서 생성
allowed-tools: Bash(git:*), Bash(gemini:*), Bash(codex:*), Bash(where:*), Read, Write, Glob, Grep
---

# Work Plan - 작업 명세서 생성

req.md 요구사항 파일을 분석하여 WORK-SPEC.md 작업 명세서를 생성해줘.
생성된 WORK-SPEC.md는 `/work-plan-start`의 입력으로 사용됩니다.

## 실행 방법

```
/work-plan                    # .claude/docs/req.md 사용
/work-plan path/to/req.md    # 지정 경로의 req.md 사용
```

## 1단계: req.md 읽기

`$ARGUMENTS`가 있으면 해당 경로, 없으면 `.claude/docs/req.md`를 읽는다.

```
경로 우선순위:
1. $ARGUMENTS로 전달된 경로
2. .claude/docs/req.md
3. 없으면 AskUserQuestion으로 경로 질문
```

req.md가 존재하지 않으면 **AskUserQuestion**으로 요구사항 파일 경로를 질문한다.

## 2단계: 프로젝트 컨텍스트 분석

req.md를 읽은 후 **프로젝트 유형을 자동 판별**한다.

### 프로젝트 유형 판별 기준

| 유형 | 판별 조건 | 활성화 섹션 |
|------|----------|------------|
| **Spring Boot API** | `build.gradle`/`pom.xml` + `spring-boot-starter-web` | API 설계, 데이터 모델, 예외 처리, JPA |
| **Spring Batch** | `spring-boot-starter-batch` | 배치 흐름, 스케줄러, 청크 설계 |
| **TIL 문서** | `cs/` 디렉토리 존재, 마크다운 중심 | 문서 구조, 카테고리, 템플릿 |
| **알고리즘** | `algorithm/` 디렉토리, 문제 풀이 패턴 | 입출력 정의, 시간복잡도, 테스트케이스 |
| **라이브러리/모듈** | 독립 모듈 구조 | 인터페이스 설계, 의존성, 배포 |
| **프론트엔드** | `package.json` + React/Vue 등 | 컴포넌트 구조, 상태 관리, 라우팅 |

### 분석 항목

다음을 자동으로 파악한다:

1. **기술 스택**: build.gradle/pom.xml/package.json 분석
2. **프로젝트 구조**: 패키지/디렉토리 구조 파악
3. **기존 코드 패턴**: 네이밍 규칙, 아키텍처 패턴, 예외 처리 방식
4. **CLAUDE.md 규칙**: 프로젝트/글로벌 CLAUDE.md에서 코딩 규칙 추출
5. **기존 테스트 패턴**: 테스트 프레임워크, 네이밍, 구조

## 3단계: WORK-SPEC.md 초안 생성

`.claude/docs/`(또는 req.md와 같은 디렉토리)에 `WORK-SPEC.md`를 생성한다.

**핵심 원칙: 해당 없는 섹션은 제거한다. 빈 템플릿을 남기지 않는다.**

### 섹션 선택 규칙

| 프로젝트 유형 | 포함 섹션 | 제외 섹션 |
|-------------|----------|----------|
| Spring Boot API | 1~10 전체 | - |
| Spring Batch | 1,2,3,6,8,9,10 | 4(Entity→Job 설계로 대체), 5(API 없음) |
| TIL 문서 | 1,3,9 | 4,5,6,7,8,10 |
| 알고리즘 | 1,6,8,9 | 2,3,4,5,7 |
| 프론트엔드 | 1,2,3,6,8,9,10 | 4(→상태 설계), 7(→에러 바운더리) |

## 4단계: 외부 AI 크로스 체크

WORK-SPEC.md 초안 생성 후, Gemini CLI와 Codex CLI를 활용하여 작업 계획을 크로스 체크한다.

### 사전 확인

다음을 **병렬로** 실행하여 CLI 설치 여부를 확인한다:

```bash
where gemini
```

```bash
where codex
```

| 결과 | 처리 |
|------|------|
| gemini 설치됨 | Gemini 크로스 체크 활성화 |
| gemini 미설치 | "Gemini CLI가 설치되지 않아 Gemini 크로스 체크를 건너뜁니다" 안내 |
| codex 설치됨 | Codex 크로스 체크 활성화 |
| codex 미설치 | "Codex CLI가 설치되지 않아 Codex 크로스 체크를 건너뜁니다" 안내 |
| 둘 다 미설치 | "외부 크로스 체크 없이 Claude 단독으로 진행합니다" 안내 후 5단계로 이동 |

### Gemini 크로스 체크

> Gemini CLI가 설치된 경우에만 실행. **Bash timeout: 300000ms 필수**
> Codex 크로스 체크와 독립적이므로 **가능한 경우 병렬로 실행**한다.

WORK-SPEC.md와 req.md 내용을 Gemini에게 파이프로 전달하여 검증한다:

```bash
(cat {req.md 경로} && echo -e "\n---\n" && cat {WORK-SPEC.md 경로}) | gemini -p "다음은 요구사항 문서(req.md)와 이를 기반으로 생성한 작업 명세서(WORK-SPEC.md)입니다.

## 검증 관점
1. **요구사항 커버리지**: req.md의 모든 요구사항이 WORK-SPEC.md에 반영되었는가?
2. **기술적 타당성**: 선택한 기술 접근 방식이 적절한가? 더 나은 대안이 있는가?
3. **누락된 고려사항**: 예외 처리, 엣지 케이스, 보안, 성능 관점에서 빠진 것은?
4. **작업 범위**: 오버엔지니어링이나 언더엔지니어링이 있는가?
5. **작업 순서**: Phase 분리와 작업 순서가 합리적인가?

## 출력 형식
각 관점별로 발견한 이슈를 심각도(❌ 필수 반영 / ⚠️ 권장 / 💡 참고)와 함께 정리해줘.
한국어로 답변해줘."
```

- 파일 경로는 req.md와 WORK-SPEC.md의 **실제 경로**를 사용한다
- Gemini CLI 실행이 실패하면 "Gemini 크로스 체크에 실패했습니다." 안내 후 계속 진행

### Codex 크로스 체크

> Codex CLI가 설치된 경우에만 실행. **Bash timeout: 300000ms 필수**
> Gemini 크로스 체크와 독립적이므로 **가능한 경우 병렬로 실행**한다.

WORK-SPEC.md와 req.md를 Codex에게 stdin으로 전달하여 검증한다:

> **주의**: Codex CLI의 `-p` 플래그는 config profile 선택 용도이므로, 프롬프트 전달에 사용하면 안 된다. stdin으로 내용을 전달하여 `codex exec -`로 실행한다.

```bash
(echo "다음은 요구사항 문서(req.md)와 이를 기반으로 생성한 작업 명세서(WORK-SPEC.md)입니다. 요구사항 커버리지, 기술적 타당성, 누락된 고려사항, 작업 범위, 작업 순서 관점에서 이슈를 심각도(❌ 필수 반영 / ⚠️ 권장 / 💡 참고)와 함께 한국어로 정리해줘:" && cat {req.md 경로} && echo -e "\n---\n" && cat {WORK-SPEC.md 경로}) | codex exec -
```

- Codex CLI 실행이 실패하면 "Codex 크로스 체크에 실패했습니다." 안내 후 계속 진행

### 피드백 반영

1. Gemini/Codex 피드백 중 **❌ 필수 반영** 항목은 WORK-SPEC.md에 즉시 반영
2. **⚠️ 권장** 항목은 Claude가 타당성을 판단하여 선택적 반영
3. **💡 참고** 항목은 WORK-SPEC.md 하단에 "크로스 체크 참고사항" 섹션으로 추가
4. 피드백 반영 후 WORK-SPEC.md를 최종 업데이트

### WORK-SPEC.md에 크로스 체크 섹션 추가

피드백 반영 후 WORK-SPEC.md 마지막에 다음 섹션을 추가한다:

```markdown
## 크로스 체크 결과

> 검증일: YYYY-MM-DD HH:MM

### Gemini 피드백 요약
> Gemini CLI가 설치되지 않았거나 실행에 실패한 경우 이 섹션은 생략한다.

| 심각도 | 내용 | 반영 여부 |
|--------|------|----------|
| ❌/⚠️/💡 | {피드백 내용 요약} | ✅ 반영 / ❌ 미반영 (사유) |

### Codex 피드백 요약
> Codex CLI가 설치되지 않았거나 실행에 실패한 경우 이 섹션은 생략한다.

| 심각도 | 내용 | 반영 여부 |
|--------|------|----------|
| ❌/⚠️/💡 | {피드백 내용 요약} | ✅ 반영 / ❌ 미반영 (사유) |
```

> 크로스 체크 결과 섹션은 프로젝트 유형과 무관하게, 외부 AI 크로스 체크가 실행된 경우 항상 포함한다.

## 5단계: Track 디렉토리 생성

WORK-SPEC.md 생성 후, 작업 추적을 위한 Track 디렉토리를 생성한다.

### Track ID 생성 규칙

```
형식: {shortname}_{YYYYMMDD}
- shortname: 요구사항에서 핵심 키워드를 kebab-case로 추출 (2~4단어)
- YYYYMMDD: 생성일자
- 예시: user-auth_20260320, batch-retry_20260320
```

### 생성 파일

`.claude/tracks/{track_id}/` 디렉토리에 다음 파일을 생성한다:

#### spec.md
```markdown
# Spec: {Track 설명}

> Track ID: {track_id}
> 생성일: {YYYY-MM-DD}
> 기반 문서: {req.md 경로}

## 요구사항 요약
{WORK-SPEC.md의 "요구사항 요약" 섹션 내용}

## 기능 요구사항
{WORK-SPEC.md의 기능 요구사항 체크리스트}
```

#### plan.md
```markdown
# Plan: {Track 설명}

> Track ID: {track_id}
> 상태: new

## Phase 1: {Phase명}
- [ ] Task: {Task 설명}
  - [ ] Sub-task: {상세}
- [ ] **Checkpoint**: Phase 1 검증

## Phase 2: {Phase명}
- [ ] Task: {Task 설명}
- [ ] **Checkpoint**: Phase 2 검증

## Phase N: 마무리
- [ ] git add
- [ ] 완료 보고
- [ ] **Checkpoint**: 최종 검증
```

> Phase 구조는 WORK-SPEC.md의 "작업 단계 (Phase)" 섹션을 기반으로 생성한다.
> 각 Phase 마지막에 **Checkpoint** Task를 자동 삽입한다.

#### metadata.json
```json
{
  "track_id": "{track_id}",
  "description": "{Track 설명}",
  "type": "{프로젝트 유형: spring-boot-api | spring-batch | til-docs | algorithm | frontend | library}",
  "status": "new",
  "created_at": "{ISO 8601 형식}",
  "work_spec_path": "{WORK-SPEC.md 경로}",
  "total_phases": {N},
  "current_phase": 0
}
```

### WORK-SPEC.md에 Track 참조 추가

WORK-SPEC.md 상단 메타데이터에 Track ID를 추가한다:

```markdown
> Track ID: {track_id}
```

### index.md 업데이트

`.claude/tracks/index.md`가 없으면 생성하고, 있으면 업데이트한다:

```markdown
# Tracks

## Active
- [ ] [{Track 설명}](./{track_id}/) - {YYYY-MM-DD} 생성

## Completed
(없음)

## Archived
(없음)
```

## 6단계: 사용자 확인

WORK-SPEC.md 및 Track 생성 후 **EnterPlanMode**로 전환하여 구현 계획을 제안한다.

사용자가 WORK-SPEC.md 내용을 검토하고 승인하면 `/work-plan-start`로 실행할 수 있다.

## 완료 후 결과 보고

```
## Work Plan 생성 완료

### 분석 결과
- 프로젝트 유형: {유형}
- 기술 스택: {스택 요약}
- 기존 패턴: {패턴 요약}

### 크로스 체크
- Gemini: {✅ 완료 / ⏭️ 건너뜀 (사유)}
- Codex: {✅ 완료 / ⏭️ 건너뜀 (사유)}
- 필수 반영 항목: {N}개 (모두 반영 완료)
- 권장 반영 항목: {N}개 중 {M}개 반영

### 생성 파일
- WORK-SPEC.md ({파일 위치})
- Track: {track_id} ({.claude/tracks/{track_id}/ 경로})

### 작업 규모
- 신규 파일: {N}개
- 수정 파일: {N}개
- 테스트 파일: {N}개
- 예상 Phase: {N}단계

### 다음 단계
`/work-plan-start` 로 작업을 시작하세요.
WORK-SPEC.md를 수정한 후 시작해도 됩니다.
```

## WORK-SPEC.md 템플릿

```markdown
# 작업 명세서 (WORK-SPEC)

> 생성일: {날짜}
> 기반 문서: {req.md 경로}
> 프로젝트 유형: {판별된 유형}

## 1. 요구사항 요약

### 목표
{req.md에서 추출한 핵심 목표 - 1~3문장}

### 기능 요구사항
- [ ] {기능 1}
- [ ] {기능 2}
- [ ] {기능 N}

### 비기능 요구사항 (해당 시)
- {성능, 보안, 확장성 등}

## 2. 기술 스택 및 제약사항

### 기술 스택
| 구분 | 기술 | 버전 |
|------|------|------|
| {구분} | {기술} | {버전} |

### 제약사항
- {프로젝트 CLAUDE.md 규칙에서 추출}
- {기존 코드 패턴 준수 사항}

## 3. 파일 변경 목록

### 신규 생성
| 파일 경로 | 역할 | 비고 |
|----------|------|------|
| {경로} | {역할} | {비고} |

### 수정
| 파일 경로 | 변경 내용 | 비고 |
|----------|----------|------|
| {경로} | {변경 내용} | {비고} |

## 4. 데이터 모델 (해당 시)

### Entity 설계
{ERD 또는 테이블 구조}

### DTO 설계
{요청/응답 DTO 구조}

## 5. API 설계 (해당 시)

| Method | Endpoint | 설명 | Request | Response |
|--------|----------|------|---------|----------|
| {METHOD} | {URI} | {설명} | {Request DTO} | {Response DTO} |

## 6. 핵심 로직 설계

### {로직 1 이름}
{로직 설명 - 플로우차트 또는 의사 코드}

## 7. 예외 처리 전략 (해당 시)

| 예외 상황 | 예외 클래스 | HTTP 상태 | 메시지 |
|----------|-----------|----------|--------|
| {상황} | {클래스} | {코드} | {메시지} |

## 8. 테스트 전략

### 단위 테스트
| 대상 | 테스트 항목 | 우선순위 |
|------|-----------|---------|
| {클래스/메서드} | {테스트 내용} | {P1/P2/P3} |

### 통합 테스트
| 시나리오 | 테스트 항목 | 우선순위 |
|---------|-----------|---------|
| {시나리오} | {테스트 내용} | {P1/P2/P3} |

## 9. 작업 단계 (Phase)

### Phase 1: 핵심 구현
- [ ] {작업 1}
- [ ] {작업 2}

### Phase 2: 테스트
- [ ] {단위 테스트}
- [ ] {통합 테스트}

### Phase 3: 문서화
- [ ] ARCHITECTURE.md 작성
- [ ] SPEC.md 작성

### Phase 4: 마무리
- [ ] git add
- [ ] 완료 보고

## 10. 엔지니어링 체크리스트

### 오버엔지니어링 방지
- [ ] 요청된 기능에 필요한 최소한의 코드만 작성했는가?
- [ ] 사용하지 않는 메서드/기능을 생성하지 않았는가?
- [ ] 미래 요구사항을 위한 불필요한 추상화를 만들지 않았는가?
- [ ] 한 번만 사용하는 코드를 위한 헬퍼/유틸리티를 만들지 않았는가?

### 언더엔지니어링 방지
- [ ] 입력 검증이 시스템 경계에서 수행되는가?
- [ ] 에러 처리가 적절한가? (예외를 삼키지 않는가?)
- [ ] 테스트가 핵심 경로를 커버하는가?
- [ ] 동시성/성능 이슈가 고려되었는가?

### CLAUDE.md 규칙 준수 (Java 프로젝트)
- [ ] SOLID 원칙 준수
- [ ] 메서드 단일 책임
- [ ] Early Return 사용
- [ ] null 대신 Optional (반환 타입만)
- [ ] 3개 이상 if-else → switch/Enum/Map
- [ ] StringUtils, CollectionUtils 등 유틸리티 활용
- [ ] 매직 넘버 → 상수/Enum 추출
- [ ] 고비용 객체 static final 캐싱
- [ ] 중첩 루프 → Map 활용
- [ ] 반복문 내 String 덧셈 → StringBuilder
- [ ] Entity 직접 노출 금지 → DTO 분리
- [ ] DTO 불변 (record 권장)
- [ ] @Data 지양 → @Getter + @Builder
- [ ] FetchType.LAZY 필수
- [ ] 조회 메서드 @Transactional(readOnly = true)
- [ ] SLF4J 로거 사용 (System.out.println 금지)
- [ ] 구체적 예외 처리 (Exception 금지)
```

## 주의사항

- **req.md 형식 자유**: req.md는 정해진 형식이 없다. 자연어, 목록, 표 등 어떤 형식이든 분석한다.
- **기존 코드 존중**: 새 코드는 기존 프로젝트의 패턴과 스타일을 따른다.
- **빈 섹션 금지**: 해당 없는 섹션은 템플릿에서 제거한다.
- **WORK-SPEC.md 위치**: req.md와 같은 디렉토리에 생성한다 (기본: `.claude/docs/`).
- **수정 가능**: 사용자가 WORK-SPEC.md를 직접 수정한 후 `/work-plan-start`를 실행할 수 있다.
- **크로스 체크 실패 시**: 외부 AI 크로스 체크가 실패해도 Claude 단독으로 생성한 WORK-SPEC.md는 유효하다.
