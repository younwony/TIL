# Repository Intelligence

> `[3] 중급` · 선수 지식: [Vibe Coding](./vibe-coding.md), [LLM 기초](./llm.md)

> `Trend` 2026

> AI가 코드베이스 전체의 구조, 관계, 히스토리를 이해하여 맥락에 맞는 코드 제안을 제공하는 기술

`#RepositoryIntelligence` `#저장소지능` `#코드베이스이해` `#CodebaseUnderstanding` `#AI코딩` `#AICoding` `#GitHubCopilot` `#Cursor` `#Sourcegraph` `#Cody` `#Zencoder` `#코드인덱싱` `#CodeIndexing` `#컨텍스트인식` `#ContextAware` `#코드관계분석` `#BreakingChangeDetection` `#PR자동화` `#VibeCoding` `#SuperAgent` `#Repomix` `#전체코드이해` `#아키텍처인식` `#컨벤션학습` `#커밋히스토리분석` `#면접AI` `#코드리뷰AI`

## 왜 알아야 하는가?

**2026년 GitHub이 선언한 AI 코딩의 새로운 패러다임입니다.**

- **실무**: 2025년 GitHub에서 5억 1,870만 개의 PR이 병합됨(전년 대비 29% 증가). Repository Intelligence 도구 사용 팀은 PR 병합 속도 50% 향상, 리드 타임 55% 단축
- **면접**: "AI 코딩 도구의 발전 방향"을 묻는 질문에서 단순 자동완성 → 코드베이스 전체 이해로의 진화 설명 가능
- **기반 지식**: Vibe Coding의 다음 단계로, AI가 "왜" 코드가 이렇게 작성되었는지까지 이해하는 수준으로 발전

## 핵심 개념

- **전체 코드베이스 인덱싱**: 프로젝트를 열면 AI가 모든 파일, 관계, 의존성을 색인화
- **커밋 히스토리 분석**: 코드가 "왜" 변경되었는지 이해 (단순 diff가 아닌 의도 파악)
- **아키텍처 인식**: 프로젝트의 구조와 패턴을 학습하여 일관된 코드 제안
- **크로스 파일 영향 분석**: 한 파일의 변경이 다른 파일에 미치는 영향을 사전 감지

## 쉽게 이해하기

**책을 읽는 비서의 비유**

기존 AI 코딩 도구는 **책의 한 페이지만 읽는 비서**와 같습니다:
- 현재 열린 파일만 봄
- 프로젝트 전체 맥락을 모름
- "이 함수를 어디서 호출하는지" 모름

Repository Intelligence는 **책 전체를 읽고 줄거리까지 기억하는 비서**입니다:
- 모든 파일의 관계를 파악
- 코드 변경 히스토리를 이해
- "이 함수를 수정하면 저쪽이 깨진다"는 것을 미리 앎

## 상세 설명

### 기존 AI 코딩 도구의 한계

```
┌─────────────────────────────────────────────────┐
│              기존 AI 코딩 도구                    │
├─────────────────────────────────────────────────┤
│  ┌─────────┐                                    │
│  │ 현재    │  ← AI가 보는 범위 (단일 파일)       │
│  │ 파일    │                                    │
│  └─────────┘                                    │
│                                                 │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐         │
│  │ 다른    │  │ 다른    │  │ 다른    │  ← 보이지 않음 │
│  │ 파일1   │  │ 파일2   │  │ 파일3   │         │
│  └─────────┘  └─────────┘  └─────────┘         │
└─────────────────────────────────────────────────┘
```

**문제점:**
- 프로젝트에 없는 라이브러리 버전의 함수를 제안
- 팀의 코딩 컨벤션을 무시한 스타일 제안
- 다른 파일에 영향을 주는 변경을 감지 못함

### Repository Intelligence의 동작 방식

```
┌─────────────────────────────────────────────────────────┐
│              Repository Intelligence                     │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌───────────────────────────────────────────────────┐  │
│  │                  전체 코드베이스                    │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐           │  │
│  │  │ 파일1   │←→│ 파일2   │←→│ 파일3   │           │  │
│  │  └────┬────┘  └────┬────┘  └────┬────┘           │  │
│  │       │            │            │                 │  │
│  │       └────────────┴────────────┘                 │  │
│  │                    ↓                              │  │
│  │            ┌──────────────┐                       │  │
│  │            │ 의존성 그래프 │                       │  │
│  │            └──────────────┘                       │  │
│  └───────────────────────────────────────────────────┘  │
│                         ↓                                │
│  ┌───────────────────────────────────────────────────┐  │
│  │               Git 히스토리 분석                    │  │
│  │  commit abc: "인증 로직 분리" → 왜? 보안 강화     │  │
│  │  commit def: "캐시 추가" → 왜? 성능 개선          │  │
│  └───────────────────────────────────────────────────┘  │
│                         ↓                                │
│  ┌───────────────────────────────────────────────────┐  │
│  │               컨벤션 패턴 학습                     │  │
│  │  - 네이밍: camelCase                              │  │
│  │  - 에러 처리: Either 패턴                         │  │
│  │  - 테스트: given-when-then                        │  │
│  └───────────────────────────────────────────────────┘  │
│                         ↓                                │
│           맥락에 맞는 정확한 코드 제안                    │
└─────────────────────────────────────────────────────────┘
```

### 핵심 기능

#### 1. 스마트 제안 (Context-Aware Suggestions)

```java
// 기존 AI: 일반적인 로깅 라이브러리 제안
import org.slf4j.Logger;

// Repository Intelligence: 프로젝트에서 실제 사용하는 방식 제안
import com.company.common.logging.CustomLogger; // 팀 커스텀 로거
```

**왜 이렇게 하는가?**
프로젝트 전체를 스캔하여 실제로 사용되는 라이브러리, 패턴, 컨벤션을 학습하기 때문입니다.

#### 2. 조기 오류 감지 (Breaking Change Detection)

```java
// UserService.java에서 메서드 시그니처 변경
public User findById(Long id) { ... }
// ↓
public Optional<User> findById(Long id) { ... }

// Repository Intelligence가 자동 감지:
// ⚠️ 이 변경은 다음 파일들에 영향을 줍니다:
//   - OrderService.java:45 (User user = userService.findById(id))
//   - ReportService.java:78 (User user = userService.findById(id))
//   - AdminController.java:23 (User user = userService.findById(id))
```

#### 3. 아키텍처 정렬 코드 생성

```java
// 프로젝트가 헥사고날 아키텍처를 사용하는 경우
// Repository Intelligence는 자동으로 해당 패턴을 따르는 코드 생성

// ❌ 기존 AI: 단순한 서비스 클래스 생성
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository repository;
}

// ✅ Repository Intelligence: 프로젝트 아키텍처에 맞는 코드
// Port (인터페이스)
public interface PaymentUseCase {
    PaymentResult process(PaymentCommand command);
}

// Adapter (구현체)
@Component
public class PaymentService implements PaymentUseCase {
    private final PaymentPort paymentPort;

    public PaymentService(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }
}
```

## 대표 도구 비교

| 도구 | 특징 | 인덱싱 방식 | 적합 대상 |
|------|------|------------|----------|
| **Cursor** | 전체 코드베이스 이해 최초 구현 | 로컬 Shadow Workspace | 개인/소규모 팀 |
| **Sourcegraph Cody** | 엔터프라이즈 수준 코드 인텔리전스 | 서버 기반 인덱싱 | 대규모 조직 |
| **Zencoder** | 유연한 LLM 백엔드 선택 | 클라우드/로컬 하이브리드 | 개인 개발자 |
| **GitHub Copilot** | 리포지토리 인텔리전스 업데이트 예정 | GitHub 통합 | GitHub 사용자 |

### Cursor의 인덱싱 방식

```
┌─────────────────────────────────────────┐
│           Cursor 작동 방식               │
├─────────────────────────────────────────┤
│  1. 프로젝트 열기                        │
│         ↓                               │
│  2. Shadow Workspace 생성               │
│         ↓                               │
│  3. 모든 파일 로컬 인덱싱                │
│     - 파일 간 관계 매핑                  │
│     - 심볼 테이블 구축                   │
│     - 의존성 그래프 생성                 │
│         ↓                               │
│  4. @ 참조로 특정 파일 컨텍스트 추가     │
│     예: @src/auth/login.ts              │
│         ↓                               │
│  5. 다중 LLM 백엔드 지원                 │
│     (OpenAI, Anthropic, Gemini, xAI)    │
└─────────────────────────────────────────┘
```

## 관련 도구: Repomix

전체 저장소를 AI 친화적인 단일 파일로 패키징하는 도구입니다.

```bash
# 설치 없이 바로 실행
npx repomix

# 출력: repomix-output.txt
# → Claude, ChatGPT, Gemini 등에 붙여넣기 가능
```

**활용 시나리오:**
- 기존 프로젝트를 새로운 AI에게 설명할 때
- 코드 리뷰를 AI에게 요청할 때
- 아키텍처 분석을 의뢰할 때

## 통계로 보는 효과

| 지표 | 수치 | 출처 |
|------|------|------|
| 2025년 GitHub PR 병합 수 | 5억 1,870만 개 (전년 대비 +29%) | GitHub |
| Repository Intelligence 사용 팀 PR 병합 속도 | 50% 향상 | GitHub |
| 리드 타임 단축 | 55% | GitHub |
| AI 도구를 첫 주에 사용하는 신규 개발자 | 80% | GitHub |
| AI 도구 일일 사용 개발자 비율 | 51% | Stack Overflow |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 맥락에 맞는 정확한 코드 제안 | 대규모 코드베이스 초기 인덱싱 시간 |
| Breaking Change 사전 감지 | 로컬 리소스 (메모리, CPU) 사용 증가 |
| 팀 컨벤션 자동 학습 | 프라이버시 우려 (클라우드 인덱싱 시) |
| PR 리뷰 시간 대폭 단축 | 도구 의존성 증가 가능성 |

## 면접 예상 질문

### Q: Repository Intelligence와 기존 AI 코딩 도구의 차이점은?

A: 기존 AI 코딩 도구는 **현재 열린 파일만 보는 단일 파일 컨텍스트**를 가집니다. 반면 Repository Intelligence는 **전체 코드베이스를 인덱싱**하여 파일 간 관계, 커밋 히스토리, 팀 컨벤션까지 이해합니다.

비유하자면, 기존 도구는 "책의 한 페이지만 읽는 비서"이고, Repository Intelligence는 "책 전체를 읽고 줄거리까지 기억하는 비서"입니다. 이 차이로 인해 Breaking Change 사전 감지, 아키텍처 정렬 코드 생성 등이 가능해집니다.

### Q: Repository Intelligence 도입 시 고려해야 할 점은?

A: 세 가지를 고려해야 합니다:

1. **초기 인덱싱 비용**: 대규모 코드베이스는 첫 인덱싱에 시간이 걸림
2. **프라이버시**: 클라우드 기반 도구 사용 시 코드가 외부로 전송될 수 있음 (Cursor는 로컬 인덱싱으로 이 문제 해결)
3. **의존성 관리**: AI 도구에 과도하게 의존하면 개발자의 코드 이해력이 저하될 수 있음

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Vibe Coding](./vibe-coding.md) | AI 협업 코딩의 기반 개념 | 중급 |
| [LLM 기초](./llm.md) | Repository Intelligence의 기반 기술 | 기초 |
| [Context Engineering](./context-engineering.md) | 컨텍스트 설계의 심화 버전 | 심화 |
| [AI Agent란](./ai-agent.md) | 자율적 AI 시스템의 기본 개념 | 기초 |

## 참고 자료

- [Repository Intelligence: AI That Groks Your Codebase (byteiota)](https://byteiota.com/repository-intelligence-ai-codebase-understanding-2026/)
- [What's next in AI: 7 trends to watch in 2026 (Microsoft)](https://news.microsoft.com/source/features/ai/whats-next-in-ai-7-trends-to-watch-in-2026/)
- [5 Key Trends Shaping Agentic Development in 2026 (The New Stack)](https://thenewstack.io/5-key-trends-shaping-agentic-development-in-2026/)
- [Repomix - GitHub](https://github.com/yamadashy/repomix)
