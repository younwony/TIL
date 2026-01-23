# SLM (Small Language Model)

> `[2] 입문` · 선수 지식: [LLM 기초](./llm.md)

> `Trend` 2026

> 특정 작업에 최적화된 소형 언어 모델로, LLM 대비 10~30배 효율적인 추론을 제공

`#SLM` `#SmallLanguageModel` `#소형언어모델` `#경량모델` `#LightweightModel` `#Phi` `#Phi3` `#Phi4` `#Mistral` `#Gemma` `#Llama` `#TinyLlama` `#엣지AI` `#EdgeAI` `#온디바이스AI` `#OnDeviceAI` `#파인튜닝` `#FineTuning` `#LoRA` `#QLoRA` `#양자화` `#Quantization` `#INT4` `#INT8` `#GGUF` `#효율성` `#Efficiency` `#토큰` `#Inference` `#임베디드` `#IoT`

## 왜 알아야 하는가?

- **실무**: 클라우드 비용 절감, 엣지 디바이스 배포, 실시간 응답이 필요한 서비스에 필수
- **면접**: "LLM vs SLM 선택 기준", "온디바이스 AI 전략", "모델 경량화 기법" 등 2026년 핫 이슈
- **기반 지식**: AI 에이전트의 효율적인 배포, MLOps, 엣지 컴퓨팅 이해의 기초

## 핵심 개념

- **파라미터 규모**: 수백만~수십억 (LLM은 수백억~수천억)
- **태스크 특화**: 범용성보다 특정 도메인/작업에 최적화
- **경량화 기법**: 양자화(Quantization), 지식 증류(Knowledge Distillation), LoRA

## 쉽게 이해하기

**LLM vs SLM = 종합병원 vs 동네 의원**

| 종합병원 (LLM) | 동네 의원 (SLM) |
|----------------|-----------------|
| 모든 질병 진료 가능 | 감기, 내과 전문 |
| 대기 시간 김 | 빠른 진료 |
| 비용 높음 | 저렴함 |
| 큰 건물, 많은 의료진 | 작은 공간, 소수 의료진 |

동네 의원이 감기 환자를 더 빠르고 저렴하게 치료하듯, SLM은 특정 작업을 LLM보다 효율적으로 처리합니다.

```
일상적인 코드 자동완성 → SLM (빠른 응답)
복잡한 아키텍처 설계 → LLM (깊은 추론)
```

## 상세 설명

### LLM vs SLM 비교

| 구분 | LLM | SLM |
|------|-----|-----|
| **파라미터** | 70B ~ 1.8T | 0.5B ~ 13B |
| **응답 시간** | 수초 ~ 수십초 | 수십ms ~ 수초 |
| **메모리 요구** | 수십GB ~ 수백GB | 수GB 이하 |
| **비용** | $0.01~0.06/1K tokens | $0.0001~0.001/1K tokens |
| **배포 환경** | 클라우드 필수 | 엣지/로컬 가능 |
| **강점** | 복잡한 추론, 범용성 | 속도, 비용, 프라이버시 |

### 대표 SLM 모델 (2026년 기준)

| 모델 | 파라미터 | 개발사 | 특징 |
|------|---------|--------|------|
| **Phi-4** | 14B | Microsoft | 추론 능력 강조, 코딩 특화 |
| **Phi-3 Mini** | 3.8B | Microsoft | 모바일/엣지 배포 최적화 |
| **Gemma 2** | 2B, 9B | Google | 오픈 소스, 다국어 지원 |
| **Llama 3.2** | 1B, 3B | Meta | 온디바이스 특화, 멀티모달 |
| **Mistral 7B** | 7B | Mistral AI | Sliding Window Attention |
| **Qwen 2.5** | 0.5B~7B | Alibaba | 다양한 크기, 코딩 강점 |

### SLM이 부상한 이유

#### 1. 비용 효율성

```
GPT-4 API 비용 (100만 토큰 기준):
- 입력: $30
- 출력: $60

Phi-3 로컬 실행 비용:
- GPU 전기료만 (시간당 ~$0.1)
```

**하루 100만 요청 처리 시:**
- LLM API: ~$90,000/월
- SLM 로컬: ~$3,000/월 (30배 절감)

#### 2. 지연 시간 (Latency)

```
사용자 입력 → 응답

LLM (API 호출):
[입력] → [네트워크] → [큐 대기] → [추론] → [네트워크] → [응답]
         ~50ms        ~100ms      ~2000ms      ~50ms
         총: 2,200ms (2.2초)

SLM (로컬 실행):
[입력] → [추론] → [응답]
          ~100ms
          총: 100ms (0.1초)
```

#### 3. 프라이버시

```
민감 데이터 처리:

LLM API:
[의료 데이터] → 외부 서버 전송 ⚠️ HIPAA 위반 가능

SLM 로컬:
[의료 데이터] → 로컬에서 처리 ✅ 데이터 외부 유출 없음
```

### SLM 최적화 기법

#### 1. 양자화 (Quantization)

모델 가중치를 더 작은 비트로 표현하여 메모리 절감

```
Float32 → Float16 → INT8 → INT4

예: Llama 3.2 3B
- FP16: 6GB
- INT8: 3GB
- INT4: 1.5GB (75% 절감)
```

```python
# bitsandbytes 라이브러리로 4bit 양자화
from transformers import AutoModelForCausalLM, BitsAndBytesConfig

bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype=torch.float16,
    bnb_4bit_quant_type="nf4"
)

model = AutoModelForCausalLM.from_pretrained(
    "microsoft/phi-3-mini-4k-instruct",
    quantization_config=bnb_config,
    device_map="auto"
)
```

#### 2. 지식 증류 (Knowledge Distillation)

큰 모델(Teacher)의 지식을 작은 모델(Student)에게 전달

```
Teacher (GPT-4, 1.8T params)
          │
          │ 지식 증류
          ▼
Student (Phi-3, 3.8B params)
```

**왜 효과적인가?**
- Teacher의 "소프트 레이블"(확률 분포)로 학습
- 단순 정답보다 더 풍부한 정보 전달
- 작은 모델이 큰 모델의 추론 패턴 습득

#### 3. LoRA (Low-Rank Adaptation)

전체 파라미터가 아닌 일부만 파인튜닝

```
기존 파인튜닝:
7B 파라미터 전체 학습 → GPU 메모리 28GB+ 필요

LoRA:
7B 중 0.1% (7M) 파라미터만 학습 → GPU 메모리 8GB로 가능
```

```python
from peft import LoraConfig, get_peft_model

lora_config = LoraConfig(
    r=8,                    # rank
    lora_alpha=32,          # scaling factor
    target_modules=["q_proj", "v_proj"],
    lora_dropout=0.05,
    task_type="CAUSAL_LM"
)

model = get_peft_model(base_model, lora_config)
# 학습 가능 파라미터: 0.1% (7M/7B)
```

### SLM 선택 기준

```
                    복잡한 추론 필요?
                         │
              ┌──────────┴──────────┐
              │                     │
             Yes                    No
              │                     │
              ▼                     ▼
         LLM 사용             실시간 응답 필요?
                                   │
                        ┌──────────┴──────────┐
                        │                     │
                       Yes                    No
                        │                     │
                        ▼                     ▼
                   SLM 로컬             SLM API
                   (Phi-3, Gemma)       (비용 절감)
```

| 시나리오 | 추천 | 이유 |
|----------|------|------|
| IDE 코드 자동완성 | SLM 로컬 | 실시간 응답 필수 |
| 고객 상담 챗봇 | SLM API | 비용 효율 |
| 법률 문서 분석 | LLM | 복잡한 추론 |
| IoT 센서 데이터 분석 | SLM 엣지 | 네트워크 없이 동작 |
| 코드 리뷰 | LLM/SLM 혼합 | 간단한 건 SLM, 복잡한 건 LLM |

## 예제 코드

### 로컬 SLM 실행 (Ollama)

```bash
# Ollama 설치 후
ollama pull phi3:mini

# 실행
ollama run phi3:mini "Python으로 퀵소트 구현해줘"
```

### Python에서 SLM 사용

```python
from transformers import AutoModelForCausalLM, AutoTokenizer
import torch

# Phi-3 Mini 로드
model_id = "microsoft/phi-3-mini-4k-instruct"
tokenizer = AutoTokenizer.from_pretrained(model_id)
model = AutoModelForCausalLM.from_pretrained(
    model_id,
    torch_dtype=torch.float16,
    device_map="auto"
)

# 추론
messages = [
    {"role": "user", "content": "HTTP와 HTTPS의 차이점을 설명해줘"}
]

inputs = tokenizer.apply_chat_template(
    messages,
    return_tensors="pt",
    return_dict=True
).to(model.device)

outputs = model.generate(
    **inputs,
    max_new_tokens=256,
    temperature=0.7,
    do_sample=True
)

response = tokenizer.decode(outputs[0], skip_special_tokens=True)
print(response)
```

### Spring Boot에서 Ollama 연동

```java
@Service
@RequiredArgsConstructor
public class LocalLlmService {

    private final RestTemplate restTemplate;

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public String generate(String prompt) {
        OllamaRequest request = OllamaRequest.builder()
            .model("phi3:mini")
            .prompt(prompt)
            .stream(false)
            .build();

        OllamaResponse response = restTemplate.postForObject(
            OLLAMA_URL,
            request,
            OllamaResponse.class
        );

        return response.getResponse();
    }
}

@Builder
record OllamaRequest(String model, String prompt, boolean stream) {}
record OllamaResponse(String response, long totalDuration) {}
```

### 벤치마크: LLM vs SLM

```python
import time
from transformers import pipeline

# SLM (Phi-3 Mini)
slm_pipe = pipeline("text-generation", model="microsoft/phi-3-mini-4k-instruct")

# 벤치마크
prompt = "Python으로 피보나치 함수를 작성해줘"

start = time.time()
result = slm_pipe(prompt, max_new_tokens=100)
slm_time = time.time() - start

print(f"SLM 응답 시간: {slm_time:.2f}초")
print(f"SLM 메모리 사용: {torch.cuda.memory_allocated() / 1e9:.2f}GB")

# 실제 결과 예시:
# SLM (Phi-3 Mini): 0.8초, 4GB
# LLM (GPT-4 API): 3.2초, API 비용 $0.003
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 10~30배 빠른 응답 | 복잡한 추론 능력 제한 |
| 90% 이상 비용 절감 | 범용성 부족 |
| 로컬/엣지 배포 가능 | 파인튜닝 필요한 경우 많음 |
| 데이터 프라이버시 보장 | 최신 지식 업데이트 어려움 |
| 오프라인 동작 가능 | 멀티모달 지원 제한적 |

## 트러블슈팅

### 사례 1: SLM 품질이 LLM 대비 떨어짐

#### 증상
- 복잡한 질문에 부정확한 답변
- 맥락 이해 부족

#### 원인 분석
- 범용 SLM을 특정 도메인에 사용
- 모델 크기 대비 태스크 복잡도 높음

#### 해결 방법
```python
# 도메인 특화 파인튜닝
from datasets import load_dataset
from trl import SFTTrainer

# 도메인 데이터로 파인튜닝
dataset = load_dataset("my_domain_dataset")

trainer = SFTTrainer(
    model=model,
    train_dataset=dataset,
    peft_config=lora_config,
    max_seq_length=512,
    args=training_args
)
trainer.train()
```

#### 예방 조치
- 태스크 복잡도 평가 후 모델 선택
- 도메인 벤치마크로 사전 테스트
- Hybrid 접근법 (SLM 1차 + LLM 2차)

### 사례 2: 양자화 후 성능 급격히 저하

#### 증상
```
FP16 정확도: 85%
INT4 정확도: 62% (23% 하락)
```

#### 원인 분석
- 과도한 양자화 (INT4)
- 양자화 캘리브레이션 부족

#### 해결 방법
```python
# GPTQ 양자화 (캘리브레이션 데이터 사용)
from auto_gptq import AutoGPTQForCausalLM

# 캘리브레이션 데이터 준비
calibration_dataset = [...]  # 대표성 있는 샘플

model = AutoGPTQForCausalLM.from_pretrained(
    model_path,
    quantize_config=quantize_config,
    calibration_dataset=calibration_dataset
)
```

#### 예방 조치
- INT8부터 시작 후 점진적 양자화
- 벤치마크로 정확도 모니터링
- 중요 레이어는 양자화 제외

## 면접 예상 질문

### Q: SLM과 LLM을 언제 각각 사용해야 하나요?

A: 선택 기준은 **응답 시간, 비용, 추론 복잡도, 프라이버시** 네 가지입니다.

- **SLM 적합**: 실시간 응답 필요(IDE 자동완성), 대량 처리로 비용 민감, 엣지 배포, 민감 데이터 처리
- **LLM 적합**: 복잡한 추론(법률/의료 분석), 범용 대화, 최신 지식 필요, 높은 정확도 필수

실무에서는 Hybrid 접근이 효과적입니다. 단순 질문은 SLM이 처리하고, 복잡한 질문만 LLM으로 라우팅하여 비용과 품질을 최적화합니다.

### Q: SLM을 프로덕션에 배포할 때 고려사항은?

A: 네 가지 핵심 고려사항이 있습니다:

1. **하드웨어**: GPU 메모리(4bit 양자화 시 3B 모델이 ~2GB), 추론 속도 요구사항
2. **양자화 전략**: INT8로 시작, 정확도 모니터링 후 INT4 검토
3. **서빙 인프라**: Ollama, vLLM, TGI 중 트래픽 패턴에 맞는 선택
4. **모니터링**: 응답 품질 메트릭, 지연 시간 P95/P99, 에러율 추적

특히 엣지 배포 시 OTA 업데이트, 폴백 로직(네트워크 복구 시 LLM으로 전환)도 설계해야 합니다.

### Q: 양자화(Quantization)의 원리와 트레이드오프는?

A: 양자화는 모델 가중치를 더 작은 비트로 표현하는 기법입니다.

**원리**: Float32(32bit) → INT8(8bit) 변환 시 메모리 75% 절감

**트레이드오프**:
- FP16: 품질 유지, 메모리 50% 절감
- INT8: 약간의 품질 저하, 메모리 75% 절감
- INT4: 눈에 띄는 품질 저하, 메모리 87.5% 절감

캘리브레이션 데이터로 양자화하면 품질 저하를 최소화할 수 있습니다. GPTQ, AWQ 같은 기법이 대표적입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [LLM 기초](./llm.md) | SLM의 기반 개념 | [1] 정의 |
| [AI Agent란](./ai-agent.md) | SLM을 두뇌로 사용 | [1] 정의 |
| [RAG](./rag.md) | SLM 지식 보완 전략 | [3] 중급 |
| [엣지 컴퓨팅](../system-design/edge-computing.md) | SLM 배포 환경 | [3] 중급 |

## 참고 자료

- [What's next for AI in 2026 - MIT Technology Review](https://www.technologyreview.com/2026/01/05/1130662/whats-next-for-ai-in-2026/)
- [What's next in AI: 7 trends to watch in 2026 - Microsoft](https://news.microsoft.com/source/features/ai/whats-next-in-ai-7-trends-to-watch-in-2026/)
- [Phi-3 Technical Report - Microsoft](https://arxiv.org/abs/2404.14219)
- [Small Language Models Survey - arXiv](https://arxiv.org/abs/2402.08885)
- [GGUF Format - Hugging Face](https://huggingface.co/docs/hub/gguf)
- [Ollama Documentation](https://ollama.ai/docs)
