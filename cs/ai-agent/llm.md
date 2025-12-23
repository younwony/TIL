# LLM (Large Language Model)

> `[1] 정의` · 선수 지식: 없음

> 대규모 텍스트 데이터로 학습하여 자연어를 이해하고 생성하는 딥러닝 모델

`#LLM` `#대규모언어모델` `#LargeLanguageModel` `#Transformer` `#GPT` `#Claude` `#Gemini` `#ChatGPT` `#Llama` `#Mistral` `#토큰` `#Token` `#토크나이저` `#Tokenizer` `#딥러닝` `#DeepLearning` `#NLP` `#자연어처리` `#Attention` `#SelfAttention` `#Temperature` `#TopP` `#TopK` `#컨텍스트윈도우` `#ContextWindow` `#파인튜닝` `#FineTuning` `#프롬프트` `#Prompt` `#NextTokenPrediction` `#인퍼런스` `#Inference`

## 왜 알아야 하는가?

- **실무**: 모든 AI Agent의 두뇌 역할, ChatGPT/Claude/Gemini 등 핵심 기술
- **면접**: "Transformer 동작 원리", "토큰이란?", "Temperature 파라미터" 등 필수 질문
- **기반 지식**: AI Agent, Prompt Engineering, RAG 등 모든 LLM 응용 기술의 기초

## 핵심 개념

- **Transformer**: 2017년 등장한 신경망 아키텍처, 모든 현대 LLM의 기반
- **토큰(Token)**: 텍스트를 분할한 최소 단위, LLM의 입출력 기본 단위
- **Next Token Prediction**: 다음에 올 토큰을 확률적으로 예측하는 LLM의 핵심 원리

## 쉽게 이해하기

**LLM = 초거대 자동완성기**

스마트폰 키보드의 자동완성을 생각해보세요. "오늘 점심"을 입력하면 "뭐 먹을까?"를 추천합니다.

LLM도 같은 원리입니다. 단, 훨씬 더 많은 데이터(인터넷 전체)를 학습했고, 훨씬 더 긴 맥락을 이해합니다.

```
입력: "대한민국의 수도는"
LLM 내부: "서울" 확률 95%, "부산" 확률 3%, "대전" 확률 1%...
출력: "서울"
```

| 일반 자동완성 | LLM |
|---------------|-----|
| 직전 몇 단어만 참고 | 수천~수만 토큰 맥락 이해 |
| 빈도 기반 추천 | 의미/문맥 기반 추론 |
| 단순 단어 제안 | 논리적 문장 생성 |

## 상세 설명

### Transformer 아키텍처

2017년 Google의 "Attention is All You Need" 논문에서 처음 제안되었습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                    Transformer 구조                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   ┌─────────────────┐         ┌─────────────────┐          │
│   │     Encoder     │         │     Decoder     │          │
│   │   (입력 이해)    │  ───▶   │   (출력 생성)    │          │
│   └─────────────────┘         └─────────────────┘          │
│                                                              │
│   예: BERT, 임베딩 모델        예: GPT, Claude, Llama        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

#### LLM 유형별 구조

| 유형 | 구조 | 대표 모델 | 용도 |
|------|------|----------|------|
| Encoder-only | 인코더만 사용 | BERT | 텍스트 분류, 임베딩 |
| Decoder-only | 디코더만 사용 | GPT, Claude, Llama | 텍스트 생성 |
| Encoder-Decoder | 둘 다 사용 | T5, BART | 번역, 요약 |

**현재 대부분의 생성형 LLM은 Decoder-only 구조를 사용합니다.**

### Self-Attention (자기 주의) 메커니즘

Transformer의 핵심 혁신입니다. 문장 내 모든 단어 간의 관계를 동시에 계산합니다.

```
문장: "그 고양이가 매트 위에 앉았다. 그것은 피곤해 보였다."

Self-Attention이 하는 일:
"그것" ──────────▶ "고양이" (관계 파악)
   │
   └── "그것이 무엇을 가리키는가?"를 문맥으로 이해
```

**왜 중요한가?**

이전 RNN 방식은 순차적으로 처리해서 긴 문장에서 앞부분을 잊어버렸습니다. Self-Attention은 모든 위치를 동시에 참조하므로 장거리 의존성을 잘 포착합니다.

### 토큰화 (Tokenization)

LLM은 텍스트를 직접 이해하지 못합니다. 토큰이라는 숫자 단위로 변환합니다.

```
입력 텍스트: "Hello, world!"

토큰화 결과:
┌─────────┬─────────┬─────────┬─────────┐
│ "Hello" │   ","   │ " world"│   "!"   │
├─────────┼─────────┼─────────┼─────────┤
│  15496  │   11    │  995    │   0     │
└─────────┴─────────┴─────────┴─────────┘
     ↓         ↓         ↓         ↓
   토큰 ID (숫자로 변환되어 모델에 입력)
```

#### 토큰 특징

| 언어 | 예시 | 토큰 수 |
|------|------|---------|
| 영어 | "Hello world" | 2 토큰 |
| 한국어 | "안녕하세요" | 3-5 토큰 (언어 효율성 낮음) |

**비용과 직결**: API 사용 시 토큰 수로 과금됩니다.

### 임베딩 (Embedding)

토큰을 고차원 벡터로 변환합니다. 의미가 비슷한 단어는 벡터 공간에서 가깝습니다.

```
"왕"  → [0.2, 0.8, 0.1, ...]
"여왕" → [0.3, 0.7, 0.2, ...]  ← 가까움
"사과" → [0.9, 0.1, 0.5, ...]  ← 멂

유명한 예시:
King - Man + Woman ≈ Queen
```

## 동작 원리

### 학습 (Training)

```
┌─────────────────────────────────────────────────────────────┐
│                     Pre-training                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   인터넷 텍스트 (TB 단위)                                    │
│        │                                                     │
│        ▼                                                     │
│   ┌─────────────────────────────────────────┐               │
│   │ Next Token Prediction                    │               │
│   │                                          │               │
│   │ 입력: "The cat sat on the"              │               │
│   │ 정답: "mat"                              │               │
│   │ 예측: "mat" ✓ / "dog" ✗                 │               │
│   └─────────────────────────────────────────┘               │
│        │                                                     │
│        ▼                                                     │
│   수천억 번 반복 (GPU 클러스터에서 수개월)                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                     Fine-tuning                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   RLHF (Reinforcement Learning from Human Feedback)         │
│   - 인간이 좋은 응답/나쁜 응답 평가                          │
│   - 모델이 인간 선호도에 맞게 조정                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### 추론 (Inference)

학습된 모델을 사용하여 응답을 생성하는 과정입니다.

```
사용자 입력: "한국의 수도는?"
     │
     ▼
┌──────────────────┐
│   토큰화         │  "한국", "의", "수도", "는", "?"
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   임베딩 변환     │  각 토큰 → 벡터
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   Transformer    │  Self-Attention으로 문맥 파악
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   다음 토큰 예측  │  "서울" 확률 95%
└────────┬─────────┘
         │
         ▼
   "서울" 출력 → 다시 입력에 추가 → 반복 (Auto-regressive)
```

**Auto-regressive (자기회귀)**: 출력을 다시 입력으로 넣어 다음 토큰을 생성하는 방식

## 예제 코드

### OpenAI API 사용 예시

```python
from openai import OpenAI

client = OpenAI()

response = client.chat.completions.create(
    model="gpt-4",
    messages=[
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": "한국의 수도는?"}
    ],
    temperature=0.7,  # 창의성 조절 (0: 결정적, 1: 창의적)
    max_tokens=100    # 최대 출력 토큰 수
)

print(response.choices[0].message.content)
# 출력: "한국의 수도는 서울입니다."
```

### 주요 파라미터

| 파라미터 | 설명 | 값 범위 |
|----------|------|---------|
| `temperature` | 출력의 무작위성 | 0.0 ~ 2.0 |
| `max_tokens` | 최대 출력 길이 | 1 ~ 컨텍스트 한도 |
| `top_p` | 누적 확률 기반 샘플링 | 0.0 ~ 1.0 |
| `top_k` | 상위 k개 토큰에서 샘플링 | 1 ~ 어휘 크기 |

```python
# Temperature 비교
# temperature=0: 항상 같은 답 (결정적)
# temperature=1: 다양한 답 (창의적)
# temperature=2: 매우 무작위 (비일관적)
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 범용적 언어 이해/생성 | 학습에 막대한 비용 (수백억 원) |
| Few-shot 학습 가능 | 추론 비용도 높음 |
| 다양한 태스크 수행 | 환각(Hallucination) 문제 |
| 긴 문맥 이해 | 실시간 정보 부재 (학습 데이터 기준) |

## 트러블슈팅

### 사례 1: 환각 (Hallucination)

#### 증상
모델이 사실이 아닌 정보를 자신있게 생성

```
Q: "2024년 노벨 물리학상 수상자는?"
A: "홍길동 박사가 수상했습니다." (존재하지 않는 인물)
```

#### 원인 분석
- 학습 데이터에 없는 정보 요청
- 확률적 생성의 본질적 한계
- 모델이 "모른다"고 답하도록 학습되지 않음

#### 해결 방법
```python
# 1. RAG (Retrieval-Augmented Generation) 사용
# 외부 DB에서 관련 문서 검색 후 컨텍스트에 포함

# 2. 프롬프트로 제한
prompt = """
다음 규칙을 따르세요:
1. 확실하지 않으면 "모르겠습니다"라고 답하세요
2. 출처를 명시하세요
"""
```

#### 예방 조치
- 중요 정보는 RAG로 근거 제공
- 팩트 체킹 레이어 추가
- Temperature 낮추기

### 사례 2: 토큰 한도 초과

#### 증상
```
Error: This model's maximum context length is 8192 tokens.
```

#### 원인 분석
입력 + 출력 토큰 합이 모델의 컨텍스트 윈도우 초과

#### 해결 방법
```python
# 1. 입력 요약/축소
# 2. 청킹 (Chunking): 긴 문서를 나눠서 처리
# 3. 더 긴 컨텍스트 모델 사용 (GPT-4 Turbo: 128K)
```

## 면접 예상 질문

### Q: Transformer의 Self-Attention이 RNN보다 좋은 이유는?

A: 세 가지 핵심 장점이 있습니다:

1. **병렬 처리**: RNN은 순차 처리라 느리지만, Transformer는 모든 위치를 동시에 계산
2. **장거리 의존성**: RNN은 긴 문장에서 앞부분 정보를 잃지만, Attention은 모든 위치 직접 참조
3. **학습 효율**: GPU 병렬화로 대규모 데이터 학습 가능

이로 인해 RNN 시대에 불가능했던 수천억 파라미터 모델 학습이 가능해졌습니다.

### Q: Temperature 파라미터는 무엇이고 언제 조절하나요?

A: Temperature는 출력의 무작위성을 조절합니다.

- **낮은 Temperature (0~0.3)**: 결정적, 일관된 답변 → 코드 생성, 팩트 응답
- **높은 Temperature (0.7~1.0)**: 창의적, 다양한 답변 → 스토리 생성, 브레인스토밍

기술적으로는 softmax 함수의 출력 확률 분포를 조절합니다. Temperature가 낮으면 확률 차이가 극대화되어 가장 높은 확률의 토큰만 선택됩니다.

### Q: LLM의 환각(Hallucination) 문제를 어떻게 해결하나요?

A: 완전한 해결은 불가능하지만, 여러 완화 전략이 있습니다:

1. **RAG (Retrieval-Augmented Generation)**: 외부 지식 DB 검색 후 컨텍스트 제공
2. **프롬프트 엔지니어링**: "확실하지 않으면 모른다고 하라" 지시
3. **Temperature 낮추기**: 무작위성 감소
4. **출처 명시 요청**: 근거 없는 답변 걸러내기
5. **팩트 체킹 레이어**: 별도 검증 시스템 구축

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [AI Agent란](./ai-agent.md) | LLM을 두뇌로 사용하는 시스템 | [1] 정의 |
| [MCP](./mcp.md) | LLM에 외부 도구 연결 | [2] 입문 |
| Tool Use (TODO) | LLM의 함수 호출 기능 | [2] 입문 |
| Prompt Engineering (TODO) | LLM 활용 최적화 | [2] 입문 |

## 참고 자료

- [What is LLM? - AWS](https://aws.amazon.com/what-is/large-language-model/)
- [Large Language Models - IBM](https://www.ibm.com/think/topics/large-language-models)
- [Transformer Explainer - Georgia Tech](https://poloclub.github.io/transformer-explainer/)
- [LLMs and Transformers - Google Developers](https://developers.google.com/machine-learning/crash-course/llm/transformers)
- [Decoder-Only Transformers - Cameron R. Wolfe](https://cameronrwolfe.substack.com/p/decoder-only-transformers-the-workhorse)
