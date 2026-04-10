# Advisor Strategy — Claude 모델 비용 효율화 전략

> `[2] 활용` · 선수 지식: 없음

> 한 줄 정의: 작은 모델(Executor)이 작업을 주도하고, 판단이 어려운 시점에만 큰 모델(Advisor)에게 자문을 구하는 이중 모델 아키텍처다.

`#AdvisorStrategy` `#ClaudeAPI` `#Executor` `#Advisor` `#비용최적화` `#모델협업` `#Opus` `#Sonnet` `#Haiku` `#멀티모델` `#AgentWorkflow` `#advisor_20260301` `#SWEbench` `#BrowseComp` `#AIArchitecture` `#CostReduction` `#LLMOrchestration` `#ClaudeCode` `#AI도구` `#워크플로우최적화`

---

## 왜 알아야 하는가?

### 실무 관점
- Opus를 모든 작업에 사용하면 비용이 크게 증가한다. Advisor Strategy는 **Opus 수준의 판단 품질을 유지하면서 비용을 최대 85%까지 절감**한다
- Claude API의 `advisor_20260301` 네이티브 도구로 **단일 Messages API 요청** 안에서 Executor ↔ Advisor 전환이 가능하다
- `max_uses` 파라미터로 Advisor 호출 횟수를 제한해 비용 예측이 쉬워진다

### 기반 지식 관점
- 단순히 "모델 크기를 고르는" 것에서 **"작업 흐름에 따라 모델 역할을 분리"**하는 사고 전환
- 에이전트 워크플로우(Agent Workflow) 설계의 핵심 패턴으로, 다른 LLM 조합에도 동일하게 응용 가능

### 커스터마이징 관점
- `max_uses` 조정만으로 품질-비용 트레이드오프를 컨트롤할 수 있다
- 기존 도구(웹 검색, 코드 실행 등)와 함께 동작하므로 **현재 스킬/에이전트 워크플로우에 점진적으로 적용**할 수 있다

---

## 핵심 개념

### 구조

```
Executor (실행자)              Advisor (조언자)
   Haiku / Sonnet       ←→       Opus
   - 작업 주도                - 어려운 판단만 담당
   - 도구 호출                - 도구 호출 없음
   - 결과 반복 처리            - 계획/지침만 반환 (400~700 토큰)
```

Executor는 작업의 흐름을 처음부터 끝까지 이끈다. 스스로 해결할 수 없는 결정에 도달했을 때만 Advisor를 호출한다. Advisor는 실행하지 않고 방향만 제시한다.

### 성능 및 비용 실측 결과

| 조합 | 벤치마크 | 성능 변화 | 비용 변화 |
|------|---------|---------|---------|
| Sonnet + Opus advisor | SWE-bench Multilingual | +2.7%p 향상 | -11.9% |
| Haiku + Opus advisor | BrowseComp | 독립 실행 대비 **2배 이상** 향상 | Sonnet 단독 대비 **-85%** |

비용이 줄면서 성능도 오르는 구간이 존재한다는 점이 핵심이다.

---

## 쉽게 이해하기

> **팀 프로젝트 비유**: 주니어 개발자(Executor)가 코딩을 주도하고, 막히는 설계 결정이 있을 때만 시니어(Advisor)에게 짧게 물어보는 구조다. 시니어는 직접 코딩하지 않고 방향만 제시한다. 결과적으로 시니어의 시간(비용)은 줄이면서 시니어급 판단 품질은 유지된다.

---

## 구현 방법

### advisor_20260301 도구 사용

```python
import anthropic

client = anthropic.Anthropic()

response = client.messages.create(
    model="claude-sonnet-4-6",  # Executor 모델
    max_tokens=8096,
    tools=[
        {
            "type": "advisor_20260301",
            "advisor_model": "claude-opus-4-6",  # Advisor 모델
            "max_uses": 3  # Advisor 호출 횟수 제한 (비용 제어)
        },
        # 기존 도구들 (웹 검색, 코드 실행 등)
    ],
    messages=[{"role": "user", "content": "작업 내용..."}]
)
```

### 주요 파라미터

| 파라미터 | 설명 | 권장값 |
|---------|------|-------|
| `advisor_model` | Advisor로 사용할 모델 | `claude-opus-4-6` |
| `max_uses` | 전체 요청에서 Advisor 호출 최대 횟수 | 복잡한 작업: 5~10, 단순: 2~3 |

### 비용 투명성

- Advisor 호출 토큰이 **응답에 별도 표시**된다
- 사용된 Advisor 토큰은 Executor 토큰과 분리 청구된다
- 이를 통해 워크플로우별 Advisor 의존도를 측정하고 최적화할 수 있다

---

## 적용 가이드: 스킬/에이전트별 분석

### 🔴 HIGH — 즉시 적용 가치 큼

| 스킬/커맨드 | Executor 역할 (Haiku/Sonnet) | Advisor 역할 (Opus) | 예상 효과 |
|------------|---------------------------|-------------------|---------|
| `/work-plan-start` | 파일 수정, git add, 에이전트 디스패치 | **모드 선택** (Solo/Standard/Coordinator), 구현 막혔을 때 방향 결정 | 비용 ↓ + 설계 품질 ↑ |
| `/work-plan` | req.md 읽기, 파일 탐색, 문서 작성 | **복잡도 판단** (Light/Full), Phase 설계, 피드백 반영 여부 결정 | 명세 품질 ↑ |
| `/review-pr` + `/team-review` | 4명 리뷰어(Haiku)가 각 관점 검토 | **종합 판단**, 심각도 분류, 최종 리뷰 요약 | 비용 -50%+ |
| `/self-review` | 파일 탐색, diff 분석 | **리뷰 판단**, 개선 우선순위 결정 | 리뷰 품질 ↑ |
| `debugger` agent | 스택 트레이스 파싱, 파일 탐색 | **근본 원인 추론**, 수정 방향 결정 | 디버깅 정확도 ↑ |
| `/security-audit` | 파일 탐색, OWASP 체크리스트 실행 | **STRIDE 위협 모델링**, 심각도 판단 | 위협 판단 품질 ↑ |

### 🟡 MEDIUM — 판단 로직 비중 중간

| 스킬/커맨드 | Executor 역할 | Advisor 역할 | 예상 효과 |
|------------|-------------|------------|---------|
| `/qa-scenario` | 영향 파일 탐색, BDD 문서 작성 | 영향도 판단, 우선순위 결정 | 시나리오 완성도 ↑ |
| `cs-guide-writer` | 문서 읽기/작성, 인덱스 갱신 | 개념 설명 품질 검토, 구조 개선 | 학습 문서 품질 ↑ |
| `/weekly-retro` | git log 분석, 커밋 파싱 | 패턴 인사이트, 개선점 도출 | 회고 깊이 ↑ |
| `ai-slop-detect` | 코드 패턴 탐지, 파일 순회 | 오버엔지니어링 최종 판단 | 판단 정확도 ↑ |
| `/product-review` | 6가지 질문 실행 | 종합 제품 판단, 구현 여부 결정 | 의사결정 질 ↑ |
| `test-coverage-check` | 커버리지 데이터 수집 | 누락 테스트 우선순위 판단 | 테스트 전략 ↑ |

### 🟢 LOW — 단순 실행 위주 (적용 불필요)

- `docker-up/down/status/logs` — 명령 실행이 전부
- `sync-global` — 파일 복사/비교만
- `/today`, `/handoff` — 단순 조회/파일 생성
- `pencil-*`, `figma-read` — MCP 호출 위주
- Slack/Jira/Confluence 커맨드 — API 호출 + 포맷팅만

---

## 적용 우선순위

```
1순위  /work-plan-start   가장 복잡한 작업, 가장 긴 실행 시간, 효과 최대
2순위  /review-pr         4명 리뷰어 Haiku화 → 즉각적인 비용 절감
3순위  /work-plan         Phase 설계 판단이 핵심 가치
4순위  debugger agent     탐색(반복) / 판단(복잡) 분리가 가장 명확한 구조
5순위  /security-audit    Opus의 위협 모델링 판단 품질이 결과를 크게 좌우
```

---

## 주의사항

- **Advisor 토큰도 과금된다**: Advisor가 짧게 응답(400~700 토큰)해도 Opus 요금이 적용된다. `max_uses`로 상한을 반드시 설정할 것
- **Advisor는 도구를 호출하지 않는다**: 파일 읽기, 웹 검색 등 도구 실행이 필요한 작업은 Executor에게 위임해야 한다
- **컨텍스트 공유**: Executor와 Advisor는 동일 컨텍스트를 공유한다. 불필요한 정보를 미리 제거하면 Advisor 응답 품질이 올라간다

---

## 면접 예상 질문

**Q. Advisor Strategy에서 Advisor가 도구를 호출하지 않는 이유는?**
> Advisor의 역할은 판단과 계획이다. 실행은 Executor의 책임 영역이므로, Advisor가 도구를 직접 호출하면 두 모델의 역할 경계가 무너진다. 또한 Advisor가 도구를 호출하면 비용이 급증한다.

**Q. max_uses를 너무 낮게 설정하면 어떤 문제가 생기나?**
> Executor가 어려운 판단에 직면했을 때 Advisor를 호출하지 못하고 단독으로 결정하게 된다. 결과적으로 Advisor를 쓰지 않는 것과 같아지므로, 작업 복잡도에 맞는 값을 설정해야 한다.

**Q. Haiku + Opus 조합이 Sonnet 단독보다 비용이 낮은 이유는?**
> Haiku는 Sonnet보다 훨씬 저렴하며, 반복 실행(파일 탐색, 도구 호출, 결과 파싱)의 대부분을 Haiku가 담당한다. Opus는 짧은 판단 응답만 생성하므로 실제 Opus 토큰 사용량이 매우 적다. 두 모델의 가격 차이가 크기 때문에 전체 비용이 Sonnet 단독보다 낮아질 수 있다.

---

## 연관 문서

| 관계 | 문서 | 설명 |
|------|------|------|
| 관련 | [Codex Plugin for Claude Code](./codex-plugin-claude-code.md) | Multi-AI 협업 패턴 (Claude + Codex) |
| 관련 | [Claude Code 릴리스 노트](./claude-code-release-notes.md) | advisor_20260301 도구가 추가된 버전 확인 |
