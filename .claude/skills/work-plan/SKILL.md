---
name: work-plan
description: |
  req.md 요구사항 파일을 분석하여 WORK-SPEC.md 작업 명세서를 생성한다. Gemini/Codex 크로스 체크로 계획의 품질을 검증한다.
  "/work-plan", "작업 계획", "구현 계획 세우기", "작업 명세서", "WORK-SPEC 생성", "req.md 분석", "작업 분석", "요구사항 분석해줘", "작업 계획 세워줘", "스펙 정리", "설계해줘" 요청에 트리거된다.
  생성된 WORK-SPEC.md는 `/work-plan-start` 스킬의 입력으로 사용된다.
  req.md가 없어도 사용자가 구두로 요구사항을 설명하면 그것을 기반으로 WORK-SPEC.md를 생성할 수 있다.
---

# Work Plan Skill

req.md를 분석하여 WORK-SPEC.md 작업 명세서를 생성한다.
구현 전에 "무엇을, 어떤 순서로, 어떤 규칙으로" 만들지 확정하기 위함이다.

## 왜 이 스킬인가?

- **구현 전 설계 확정**: 코드를 쓰기 전에 파일 목록, API, 데이터 모델, Phase를 확정하여 임의 변경을 방지
- **크로스 체크 내장**: Gemini/Codex가 요구사항 누락, 오버엔지니어링, 기술 타당성을 검증
- **팀 워크플로우 연계**: 생성된 WORK-SPEC.md가 `/work-plan-start`의 입력이 되어 에이전트 병렬 디스패치 가능

---

## 실행 방법

```
/work-plan              # 프로젝트 루트의 req.md 사용
/work-plan path/to/req.md  # 지정 경로의 req.md 사용
```

---

## 작업 절차

### 1. req.md 읽기

`$ARGUMENTS`가 있으면 해당 경로, 없으면 프로젝트 루트의 `req.md`를 읽는다.

```
경로 우선순위:
1. $ARGUMENTS로 전달된 경로
2. 현재 프로젝트 루트의 req.md
3. 없으면 사용자에게 경로 질문
```

req.md 형식은 자유다. 자연어, 목록, 표 등 어떤 형식이든 분석한다.

### 2. 프로젝트 컨텍스트 분석

req.md를 읽은 후 프로젝트 유형을 자동 판별한다. 기존 코드의 패턴과 스타일을 파악해야 새 코드가 이질적이지 않다.

**판별 기준:**

| 유형 | 판별 조건 |
|------|----------|
| Spring Boot API | `build.gradle`/`pom.xml` + `spring-boot-starter-web` |
| Spring Batch | `spring-boot-starter-batch` |
| TIL 문서 | `cs/` 디렉토리 존재, 마크다운 중심 |
| 알고리즘 | `algorithm/` 디렉토리, 문제 풀이 패턴 |
| 라이브러리/모듈 | 독립 모듈 구조 |
| 프론트엔드 | `package.json` + React/Vue 등 |

**분석 항목:**

1. **기술 스택** - build.gradle / pom.xml / package.json 분석
2. **프로젝트 구조** - 패키지/디렉토리 구조 파악
3. **기존 코드 패턴** - 네이밍 규칙, 아키텍처 패턴, 예외 처리 방식
4. **CLAUDE.md 규칙** - 프로젝트/글로벌 CLAUDE.md에서 코딩 규칙 추출
5. **기존 테스트 패턴** - 테스트 프레임워크, 네이밍, 구조
6. **DB 스키마 탐색** (Spring Boot API / Spring Batch 유형만)

#### 2-6. DB 스키마 탐색

프로젝트 유형이 **Spring Boot API** 또는 **Spring Batch**인 경우, DB 테이블/인덱스 구조를 직접 조회하여 데이터 모델 설계에 반영한다.

**Read 도구로 `references/db-schema-guide.md`를 읽어서 mysqlsh 사용법과 쿼리 템플릿을 참조한다.**

**절차:**

1. **접속 정보 추출**: `application.yml`, `application-local.yml`, `application.properties` 중 하나에서 `spring.datasource.*` 키를 찾는다.
2. **접속 가능 여부 확인**: mysqlsh 설치 여부와 DB 접속 가능 여부를 확인한다.
   ```bash
   where mysqlsh  # 설치 확인
   mysqlsh --sql -u {user} -p{password} -h {host} -P {port} -D {database} -e "SELECT 1;"  # 접속 확인
   ```
3. **스키마 조회**: 접속 가능하면 아래 순서로 조회한다.
   - `SHOW TABLES;` - 전체 테이블 목록
   - `DESCRIBE {table};` - req.md에서 언급된 테이블의 구조
   - `SHOW INDEX FROM {table};` - 관련 테이블의 인덱스
   - 외래키 관계 (INFORMATION_SCHEMA.KEY_COLUMN_USAGE)
4. **결과 활용**: 조회된 스키마 정보를 WORK-SPEC.md의 "데이터 모델" 섹션에 반영한다.

**예외 처리:**
- mysqlsh 미설치 → "mysqlsh 미설치로 DB 스키마 조회를 건너뜁니다" 안내 후 계속
- 접속 정보 없음 → 사용자에게 DB 접속 정보를 질문 (host, port, database, user, password)
- 접속 실패 → "DB 접속 실패로 스키마 조회를 건너뜁니다" 안내 후 계속
- **보안**: `block-dangerous-sql.sh` hook이 읽기 전용 쿼리만 허용한다. DML/DDL은 자동 차단된다.

### 3. WORK-SPEC.md 초안 생성

**Read 도구로 `references/workspec-template.md`를 읽어서 템플릿과 섹션 선택 규칙을 참조한다.**

`{DOC_DIR}/1_WORK-SPEC.md`에 생성한다. (`{DOC_DIR}`은 글로벌 CLAUDE.md의 워크플로우 문서 저장 경로 규칙을 따른다.)

**핵심 원칙:**
- 해당 없는 섹션은 제거한다. 빈 템플릿을 남기지 않는다.
- 새 코드는 기존 프로젝트의 패턴과 스타일을 따른다.

### 4. 외부 AI 크로스 체크

WORK-SPEC.md 초안 생성 후, Gemini/Codex CLI로 작업 계획을 크로스 체크한다.
단독 검증은 맹점이 생기기 쉬우므로, 다른 AI의 시선으로 누락과 오류를 잡는다.

#### 4-1. 사전 확인

다음을 **병렬로** 확인한다:

```bash
where gemini    # 설치 여부 확인
where codex     # 설치 여부 확인
```

- 미설치 CLI는 해당 크로스 체크를 건너뛴다 (안내 메시지 출력).
- 둘 다 미설치면 "외부 크로스 체크 없이 Claude 단독으로 진행합니다" 안내 후 계속.

#### 4-2. 크로스 체크 실행

Gemini와 Codex는 독립적이므로 **가능한 경우 병렬 실행**한다.

**Gemini** (Bash timeout: 300000ms 필수):

```bash
(cat {req.md 경로} && echo -e "\n---\n" && cat {WORK-SPEC.md 경로}) | gemini -p "다음은 요구사항 문서(req.md)와 이를 기반으로 생성한 작업 명세서(WORK-SPEC.md)입니다.

## 검증 관점
1. **요구사항 커버리지**: req.md의 모든 요구사항이 WORK-SPEC.md에 반영되었는가?
2. **기술적 타당성**: 선택한 기술 접근 방식이 적절한가? 더 나은 대안이 있는가?
3. **누락된 고려사항**: 예외 처리, 엣지 케이스, 보안, 성능 관점에서 빠진 것은?
4. **작업 범위**: 오버엔지니어링이나 언더엔지니어링이 있는가?
5. **작업 순서**: Phase 분리와 작업 순서가 합리적인가?

## 출력 형식
각 관점별로 발견한 이슈를 심각도(필수 반영 / 권장 / 참고)와 함께 정리해줘.
한국어로 답변해줘."
```

**Codex (Plugin 우선):**

Codex Plugin이 설치된 경우 `/codex:rescue`로 자연어 위임한다:

```
/codex:rescue 다음 WORK-SPEC.md를 검증해줘. 요구사항 커버리지, 기술적 타당성, 누락된 고려사항, 작업 범위, 작업 순서 관점에서 이슈를 심각도와 함께 한국어로 정리해줘. --wait
```

**Plugin 미설치 시 fallback** (Bash, timeout: 300000ms):

```bash
(echo "다음은 요구사항 문서(req.md)와 이를 기반으로 생성한 작업 명세서(WORK-SPEC.md)입니다. 요구사항 커버리지, 기술적 타당성, 누락된 고려사항, 작업 범위, 작업 순서 관점에서 이슈를 심각도와 함께 한국어로 정리해줘:" && cat {req.md 경로} && echo -e "\n---\n" && cat {WORK-SPEC.md 경로}) | codex exec -
```

실행 실패 시 안내 메시지만 출력하고 계속 진행한다.

#### 4-3. 피드백 반영

| 심각도 | 처리 방식 |
|--------|----------|
| **필수 반영** | WORK-SPEC.md에 즉시 반영 |
| **권장** | Claude가 타당성 판단 후 선택적 반영 |
| **참고** | WORK-SPEC.md 하단 "크로스 체크 결과" 섹션에 기록 |

피드백 반영 후 WORK-SPEC.md를 최종 업데이트하고, 크로스 체크 결과 섹션을 추가한다.
크로스 체크 결과 섹션의 형식은 `references/workspec-template.md`를 참조한다.

### 5. 사용자 확인

WORK-SPEC.md 생성 후 **Plan Mode로 전환**하여 결과를 보고한다.
사용자가 검토/수정 후 `/work-plan-start`로 실행할 수 있다.

---

## 결과 보고 형식

```
## Work Plan 생성 완료

### 분석 결과
- 프로젝트 유형: {유형}
- 기술 스택: {스택 요약}
- 기존 패턴: {패턴 요약}

### 크로스 체크
- Gemini: {완료 / 건너뜀 (사유)}
- Codex: {완료 / 건너뜀 (사유)}
- 필수 반영 항목: {N}개 (모두 반영 완료)
- 권장 반영 항목: {N}개 중 {M}개 반영

### 생성 파일
- WORK-SPEC.md ({파일 위치})

### 작업 규모
- 신규 파일: {N}개
- 수정 파일: {N}개
- 테스트 파일: {N}개
- 예상 Phase: {N}단계

### 다음 단계
`/work-plan-start` 로 작업을 시작하세요.
WORK-SPEC.md를 수정한 후 시작해도 됩니다.
```

---

## 주의사항

- **WORK-SPEC.md 위치**: req.md와 같은 디렉토리에 생성한다 (기본: 프로젝트 루트).
- **수정 가능**: 사용자가 WORK-SPEC.md를 직접 수정한 후 `/work-plan-start`를 실행할 수 있다.
- **크로스 체크 실패 허용**: 외부 AI 크로스 체크가 실패해도 Claude 단독 생성 WORK-SPEC.md는 유효하다.
