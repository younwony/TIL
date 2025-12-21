# Claude Code Skill

> `[3] 중급` · 선수 지식: [MCP](./mcp.md)

> `Trend` 2025

> AI 에이전트의 기능을 모듈화하여 확장하는 능력 패키징 시스템

## 왜 알아야 하는가?

- **실무**: 반복되는 작업 패턴을 재사용 가능한 모듈로 만들어 생산성 향상
- **면접**: AI 도구를 단순 사용이 아닌 확장/커스터마이징하는 역량 증명
- **기반 지식**: Claude Code 자동화 및 팀 협업의 핵심 구성요소

## 핵심 개념

- **자동 발견 (Model-invoked)**: 사용자가 명시적으로 호출하지 않아도 Claude가 상황에 맞게 자동으로 선택하여 사용
- **모듈화된 전문성**: 특정 도메인의 지식과 워크플로우를 재사용 가능한 단위로 패키징
- **SKILL.md 기반**: 마크다운 파일로 정의하며 YAML frontmatter로 메타데이터 지정
- **도구 접근 제어**: `allowed-tools` 옵션으로 특정 도구만 사용하도록 제한 가능
- **팀 공유 가능**: Git 저장소에 커밋하면 팀원 모두 동일한 Skill 사용 가능

## 쉽게 이해하기

**Skill**을 회사의 **업무 매뉴얼**에 비유할 수 있습니다.

신입사원(Claude)이 입사하면 모든 업무를 처음부터 배워야 합니다. 하지만 회사에 잘 정리된 업무 매뉴얼이 있다면, 특정 상황에서 어떻게 해야 하는지 바로 참고할 수 있습니다.

예를 들어, "고객 환불 요청" 상황이 발생하면:
- 매뉴얼 없이: 매번 선배에게 물어보거나 시행착오를 겪음
- 매뉴얼 있으면: "고객 환불 매뉴얼"을 자동으로 찾아서 절차대로 처리

Skill도 마찬가지입니다. "PDF 파일 처리해줘"라고 요청하면, Claude가 자동으로 `pdf-processing` Skill을 발견하고 그 안의 지침대로 작업합니다.

단, Slash Command가 "이 매뉴얼 실행해"라고 직접 지정하는 것이라면, Skill은 "상황에 맞는 매뉴얼을 알아서 찾아 적용"하는 것입니다.

## 상세 설명

### Skill vs Slash Command

두 기능 모두 Claude의 동작을 확장하지만, 호출 방식이 다릅니다.

| 구분 | Slash Command | Skill |
|------|---------------|-------|
| 호출 방식 | 사용자가 `/review`처럼 직접 호출 | Claude가 자동으로 발견 및 사용 |
| 파일 구조 | 단일 `.md` 파일 | 디렉토리 + `SKILL.md` |
| 지원 파일 | 불가 | 스크립트, 템플릿 등 지원 |
| 적합한 상황 | 같은 프롬프트를 반복 실행 | 상황에 따라 자동 적용 |

**왜 이렇게 구분되어 있는가?**

Slash Command는 "명확한 의도"가 있을 때 사용합니다. 사용자가 정확히 무엇을 원하는지 알고 있으므로 직접 호출합니다. 반면 Skill은 "맥락 기반 자동화"에 적합합니다. Claude가 대화 맥락을 분석하여 적절한 Skill을 선택하므로, 사용자가 매번 명령어를 기억할 필요가 없습니다.

### Skill 저장 위치

| 종류 | 위치 | 용도 |
|------|------|------|
| Personal Skill | `~/.claude/skills/` | 개인 워크플로우, 실험적 기능 |
| Project Skill | `.claude/skills/` | 팀 워크플로우, 프로젝트 특화 기능 |

**왜 두 가지로 나뉘는가?**

- **Personal Skill**: 개인의 작업 스타일에 맞춘 설정입니다. Git에 공유되지 않으므로 실험적인 Skill을 테스트하거나, 개인 선호도에 맞는 워크플로우를 정의할 때 사용합니다.
- **Project Skill**: 팀 전체가 동일한 가이드라인을 따라야 할 때 사용합니다. Git에 커밋되므로 `git pull`만 하면 팀원 모두 동일한 Skill을 사용할 수 있습니다.

### SKILL.md 필수 필드

```yaml
---
name: skill-이름
description: Skill 설명과 사용 시점
---
```

| 필드 | 규칙 | 왜 중요한가? |
|------|------|-------------|
| `name` | 소문자, 숫자, 하이픈만 (최대 64자) | Skill 식별자로 사용, 충돌 방지 |
| `description` | 용도와 사용 시기 포함 (최대 1024자) | Claude가 자동 발견할 때 이 설명을 기반으로 판단 |

**description 작성이 핵심인 이유**

Claude는 `description`을 읽고 "이 Skill을 언제 사용할지" 판단합니다. 따라서 **"Use when..."** 패턴으로 명확하게 사용 시점을 명시해야 합니다.

```yaml
# Bad - 언제 사용할지 불명확
description: 문서 관련 작업을 도와줍니다

# Good - 사용 시점이 명확
description: PDF 파일에서 텍스트를 추출하고 양식을 작성합니다. PDF 파일 작업이나 문서 추출 시 사용하세요.
```

### 도구 접근 제어 (allowed-tools)

특정 도구만 사용하도록 제한할 수 있습니다.

```yaml
---
name: code-reviewer
description: 코드 리뷰를 수행합니다. 코드 리뷰나 PR 검토 시 사용하세요.
allowed-tools: Read, Grep, Glob
---
```

**왜 도구를 제한하는가?**

- **안전성**: 읽기 전용 Skill에서 실수로 파일을 수정하는 것을 방지
- **명확한 범위**: Skill의 역할을 명확히 정의하여 예측 가능한 동작 보장
- **보안**: 민감한 작업에서 불필요한 도구 접근 차단

## 동작 원리

### Skill 자동 발견 프로세스

```
1. 사용자 요청 입력
   └─> "이 PDF에서 텍스트 추출해줘"

2. Claude가 사용 가능한 Skill 목록 확인
   └─> Personal Skills (~/.claude/skills/)
   └─> Project Skills (.claude/skills/)

3. 각 Skill의 description과 요청 매칭
   └─> "pdf-processing" Skill의 description:
       "PDF 파일에서 텍스트를 추출..."
   └─> 매칭됨!

4. Skill 내용 로드 및 적용
   └─> SKILL.md의 지침 따름
   └─> 관련 파일 (scripts/, templates/) 활용

5. 작업 수행 및 결과 반환
```

### 디렉토리 구조

```
.claude/skills/my-skill/
├── SKILL.md        # 필수 - Skill 정의
├── reference.md    # 선택 - 참고 문서
├── examples.md     # 선택 - 예제 모음
└── scripts/        # 선택 - 헬퍼 스크립트
    └── helper.py
```

**왜 디렉토리 구조인가?**

Slash Command는 단일 `.md` 파일이지만, Skill은 디렉토리입니다. 이는 복잡한 워크플로우를 지원하기 위함입니다:
- 긴 참조 문서를 별도 파일로 분리
- 헬퍼 스크립트를 포함
- 템플릿 파일 관리

## 예제 코드

### 기본 Skill 작성

```yaml
# .claude/skills/commit-helper/SKILL.md
---
name: commit-helper
description: git diff를 분석하여 명확한 커밋 메시지를 생성합니다. 커밋 메시지 작성이나 스테이징된 변경사항 검토 시 사용하세요.
---

# 커밋 메시지 도우미

## 사용 방법

1. `git diff --staged` 실행하여 변경사항 확인
2. 변경사항을 분석하여 커밋 메시지 생성:
   - 50자 이내의 요약
   - 필요시 상세 설명 추가
   - 영향받는 컴포넌트 명시

## 모범 사례

- 현재 시제 사용 ("기능 추가" O, "기능 추가했음" X)
- 무엇을, 왜 변경했는지 설명 (어떻게는 생략)
- 관련 이슈 번호 참조

## 출력 예시

```
feat: JWT 인증 미들웨어 추가

- 토큰 검증 미들웨어 구현
- User 스키마에 인증 필드 추가
- 미들웨어 테스트 케이스 15개 작성

Fixes #123
```
```

### 도구 제한 Skill

```yaml
# .claude/skills/code-reviewer/SKILL.md
---
name: code-reviewer
description: 코드 품질과 잠재적 이슈를 검토합니다. 코드 리뷰, PR 검토, 코드 품질 분석 시 사용하세요.
allowed-tools: Read, Grep, Glob
---

# 코드 리뷰어

파일 수정 없이 읽기 전용으로 코드를 리뷰합니다.

## 리뷰 체크리스트

1. 코드 구조 및 조직화
2. 에러 처리 완성도
3. 성능 고려사항
4. 보안 취약점
5. 테스트 커버리지

## 사용 방법

1. Read 도구로 대상 파일 읽기
2. Grep으로 패턴 검색
3. Glob으로 관련 파일 찾기
4. 구체적인 라인 참조와 함께 상세 피드백 제공
```

### 다중 파일 Skill

```
# 폴더 구조
.claude/skills/api-designer/
├── SKILL.md
├── conventions.md
└── templates/
    └── endpoint-template.md
```

**SKILL.md:**
```yaml
---
name: api-designer
description: 팀 컨벤션에 따라 RESTful API를 설계합니다. 새 API 엔드포인트 생성이나 API 설계 검토 시 사용하세요.
---

# API 설계자

## 빠른 참조

네이밍 규칙은 [conventions.md](conventions.md) 참고.
엔드포인트 템플릿은 [templates/](templates/) 참고.

## 사용 방법

1. RESTful 네이밍 컨벤션 준수
2. 적절한 HTTP 메서드 사용
3. 일관된 응답 포맷 설계
4. 에러 처리 명세 포함
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 반복 작업 자동화로 생산성 향상 | description 작성이 부실하면 자동 발견 실패 |
| 팀 전체 일관된 워크플로우 적용 | 복잡한 Skill은 유지보수 비용 증가 |
| 도메인 지식을 코드화하여 공유 | 유사한 Skill 간 충돌 가능성 |
| Git 기반 버전 관리 및 협업 용이 | 초기 설정 학습 곡선 존재 |

## 면접 예상 질문

### Q: Skill과 Slash Command의 차이점은 무엇인가요?

A: 핵심 차이는 **호출 방식**입니다. Slash Command는 사용자가 `/review`처럼 명시적으로 호출해야 합니다. 반면 Skill은 Claude가 대화 맥락을 분석하여 자동으로 적절한 Skill을 선택합니다.

**왜 이런 구분이 필요한가?** Slash Command는 "정확히 이 작업을 해라"라는 명확한 의도가 있을 때 적합합니다. Skill은 "PDF 다뤄줘"처럼 맥락에서 적절한 도구를 자동으로 선택해야 할 때 유용합니다. 따라서 자주 반복하는 명확한 작업은 Slash Command로, 상황에 따라 유연하게 적용해야 하는 워크플로우는 Skill로 구현합니다.

### Q: Skill의 description을 잘 작성하는 방법은?

A: 핵심은 **사용 시점을 명확히 명시**하는 것입니다.

**나쁜 예**: `description: 문서 관련 작업을 도와줍니다` - 언제 사용할지 불명확

**좋은 예**: `description: PDF 파일에서 텍스트와 테이블을 추출하고 양식을 작성합니다. PDF 파일 작업, 문서 추출, 양식 작성 시 사용하세요.`

**왜 이렇게 작성해야 하는가?** Claude는 description을 기반으로 "이 Skill을 사용할 상황인가?"를 판단합니다. 구체적인 키워드(PDF, 양식, 추출)와 사용 시점이 명시되어야 정확한 매칭이 가능합니다. 모호한 설명은 잘못된 Skill 선택이나 아예 Skill을 발견하지 못하는 결과로 이어집니다.

### Q: allowed-tools 옵션은 언제 사용하나요?

A: **안전성과 예측 가능성**이 중요한 상황에서 사용합니다.

대표적인 사용 사례:
- **읽기 전용 Skill**: 코드 리뷰어가 실수로 파일을 수정하면 안 됨 → `allowed-tools: Read, Grep, Glob`
- **보안 민감 작업**: 특정 도구 접근을 원천 차단
- **역할 분리**: 각 Skill의 책임 범위를 명확히 제한

**왜 기본값이 "모든 도구 허용"인가?** 대부분의 Skill은 유연성이 필요합니다. 제한은 필요한 경우에만 명시적으로 설정하는 것이 설계 철학입니다. 과도한 제한은 Skill의 유용성을 떨어뜨립니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [MCP](./mcp.md) | 선수 지식 - AI 에이전트 기초 | [2] 입문 |
| [Slash Command](./claude-code-slash-command.md) | 관련 개념 - 명시적 호출 방식 | [3] 중급 |
| [Hook](./claude-code-hook.md) | 관련 개념 - 이벤트 기반 제어 | [3] 중급 |

## 참고 자료

- [Claude Code Skills Documentation](https://docs.anthropic.com/en/docs/claude-code/skills)
- [Claude Code Slash Commands Documentation](https://docs.anthropic.com/en/docs/claude-code/slash-commands)
