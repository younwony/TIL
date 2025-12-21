# Green Software Engineering

> `[4] 심화` · `Trend` 2025 · 선수 지식: [확장성](./scalability.md), 시스템 아키텍처

> 탄소 효율적이고 탄소 인지적인 지속 가능한 소프트웨어를 설계하고 구축하는 원칙과 방법론

## 왜 알아야 하는가?

- **실무**: EU AI Act (2025년 8월 시행) 등 규제 강화. ESG 경영의 핵심 지표
- **면접**: 트렌드 감각과 지속 가능한 시스템 설계 역량 평가
- **기반 지식**: 에너지 효율적 코드 = 비용 효율적 코드. 성능 최적화와 직결

## 핵심 개념

- **탄소 효율성(Carbon Efficiency)**: 소프트웨어가 최소한의 탄소 배출로 동일한 작업을 수행하도록 설계
- **탄소 인지(Carbon Awareness)**: 전력 그리드의 탄소 강도에 따라 작업량을 조절
- **SCI (Software Carbon Intensity)**: ISO/IEC 21031:2024 표준으로 채택된 소프트웨어 탄소 강도 측정 지표

## 쉽게 이해하기

**전기 요금이 시간대별로 다른 것처럼, 탄소 배출량도 다르다.**

- 낮에 태양광이 풍부할 때 전력을 사용하면 탄소 배출이 적다
- 밤에 화석 연료 발전에 의존할 때는 같은 전력이라도 탄소 배출이 많다

Green Software는 이런 차이를 인지하고:
1. 가능하면 **청정 에너지가 풍부한 시간**에 작업을 실행
2. 동일한 결과를 **더 적은 에너지**로 달성
3. 하드웨어 교체 주기를 늘려 **제조 과정의 탄소 배출**도 줄임

## 상세 설명

### SCI 공식

```
SCI = ((E × I) + M) / R
```

| 요소 | 설명 | 예시 |
|------|------|------|
| **E** (Energy) | 소프트웨어 운영에 소비된 총 에너지 (kWh) | 서버 전력 소비량 |
| **I** (Intensity) | 에너지원의 탄소 강도 (gCO₂/kWh) | 석탄 820, 태양광 41 |
| **M** (Embodied) | 하드웨어 제조 과정의 내재 탄소 | 서버 제조 시 발생한 CO₂ |
| **R** (Rate) | 측정 단위 (기능 단위) | API 호출당, 사용자당, 트랜잭션당 |

**왜 이 공식인가?**
- 단순 총량이 아닌 **비율(Rate)**로 측정하여 서비스 규모와 무관하게 비교 가능
- 운영 탄소(E×I)와 내재 탄소(M)를 모두 고려하여 전체 라이프사이클 평가

### 세 가지 핵심 원칙

#### 1. 에너지 효율성 (Energy Efficiency)

**같은 작업을 더 적은 에너지로 수행**

```java
// Bad: O(n²) - 에너지 낭비
public List<User> findDuplicates(List<User> users) {
    List<User> duplicates = new ArrayList<>();
    for (int i = 0; i < users.size(); i++) {
        for (int j = i + 1; j < users.size(); j++) {
            if (users.get(i).equals(users.get(j))) {
                duplicates.add(users.get(i));
            }
        }
    }
    return duplicates;
}

// Good: O(n) - 에너지 효율적
public List<User> findDuplicates(List<User> users) {
    Set<User> seen = new HashSet<>();
    return users.stream()
        .filter(user -> !seen.add(user))
        .collect(Collectors.toList());
}
```

**최적화 포인트:**
- 효율적인 알고리즘 선택 (90%까지 컴퓨팅 시간 단축 가능)
- 불필요한 데이터 전송 제거
- 스마트 캐싱으로 중복 연산 방지

#### 2. 탄소 인지 (Carbon Awareness)

**청정 에너지가 풍부할 때 더 많은 작업 수행**

```java
@Service
public class CarbonAwareScheduler {

    private final CarbonIntensityClient carbonClient;

    public void scheduleJob(Runnable job, JobPriority priority) {
        double currentIntensity = carbonClient.getCurrentIntensity();

        if (priority == JobPriority.LOW && currentIntensity > THRESHOLD_HIGH) {
            // 탄소 강도가 높으면 낮은 우선순위 작업 지연
            scheduleForLowCarbonWindow(job);
        } else {
            executeNow(job);
        }
    }

    private void scheduleForLowCarbonWindow(Runnable job) {
        // 태양광 발전이 활발한 시간대(낮)에 예약
        LocalTime optimalTime = carbonClient.getNextLowCarbonWindow();
        scheduler.schedule(job, optimalTime);
    }
}
```

**적용 사례:**
- CI/CD 빌드를 저탄소 시간대에 스케줄링
- 배치 작업을 재생에너지 비율이 높은 지역/시간에 실행
- 비긴급 작업의 demand shifting

#### 3. 하드웨어 효율성 (Hardware Efficiency)

**하드웨어 수명을 늘려 내재 탄소 배분**

- 서버 제조 과정에서 이미 상당한 탄소가 배출됨
- 4년 사용 vs 8년 사용 시, 연간 내재 탄소가 절반으로 감소
- 클라우드 활용으로 하드웨어 utilization 극대화

### 역할별 적용 방안

| 역할 | Green Software 적용 |
|------|---------------------|
| **개발자** | 에너지 효율적 코드, 불필요한 연산 제거 |
| **AI/ML 엔지니어** | 모델 최적화, 사전 학습 모델 활용, 양자화 |
| **DevOps** | 탄소 인지 파이프라인, 저탄소 시간대 빌드 |
| **아키텍트** | Serverless 아키텍처, 이벤트 드리븐 설계 |
| **프로덕트** | 에코 모드 기능, 다크 모드 기본값 |

## 동작 원리

### Carbon-Aware Workload 스케줄링

```
시간대별 탄소 강도 (gCO₂/kWh)

     800 ├─────────────────────────────────────┤
         │    ▓▓▓▓                    ▓▓▓▓    │
     600 │   ▓▓▓▓▓▓                  ▓▓▓▓▓▓   │
         │  ▓▓▓▓▓▓▓▓              ▓▓▓▓▓▓▓▓▓▓  │
     400 │ ▓▓▓▓▓▓▓▓▓▓            ▓▓▓▓▓▓▓▓▓▓▓▓ │
         │▓▓▓▓▓▓▓▓▓▓▓▓          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓│
     200 │            ░░░░░░░░░░              │
         │           ░░░░░░░░░░░░             │
       0 └─────────────────────────────────────┘
          0  2  4  6  8  10 12 14 16 18 20 22 24

          ▓ = 높은 탄소 강도 (화석 연료 의존)
          ░ = 낮은 탄소 강도 (태양광 풍부)

     → 비긴급 배치 작업은 10~16시에 스케줄링!
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 운영 비용 절감 (에너지 = 비용) | 초기 측정/모니터링 인프라 구축 필요 |
| ESG 규제 대응 | 실시간 처리가 필요한 작업에는 적용 어려움 |
| 브랜드 이미지 향상 | 탄소 강도 데이터 획득의 어려움 |
| ISO 인증 획득 가능 | 개발자 인식 전환 필요 |

## 실무 적용 체크리스트

### 즉시 적용 가능

- [ ] 효율적인 알고리즘으로 교체
- [ ] 불필요한 API 호출/데이터 전송 제거
- [ ] 캐싱 전략 최적화
- [ ] 다크 모드 지원 (OLED 디스플레이 에너지 절감)

### 중기 적용

- [ ] SCI 측정 파이프라인 구축
- [ ] CI/CD를 저탄소 시간대에 스케줄링
- [ ] Carbon-aware SDK 도입 (GSF 제공)
- [ ] 서버리스/이벤트 드리븐 아키텍처 전환

### 장기 전략

- [ ] 멀티 리전 배포 시 탄소 강도 고려
- [ ] ISO/IEC 21031 인증 획득
- [ ] Green Software Maturity Matrix 자가 진단

## 면접 예상 질문

### Q: Green Software Engineering이란 무엇이고, 왜 중요해졌나요?

A: 탄소 효율적인 소프트웨어를 설계하고 구축하는 방법론입니다. 데이터센터의 탄소 배출량이 이미 항공 산업을 넘어섰고, EU AI Act(2025년 8월 시행)와 같은 규제가 강화되면서 **비용과 탄소가 수렴**하는 시대가 되었습니다. 에너지 효율적인 코드는 곧 비용 효율적인 코드이며, ESG 경영의 핵심 지표가 되고 있습니다.

### Q: SCI(Software Carbon Intensity)는 어떻게 계산하나요?

A: `SCI = ((E × I) + M) / R` 공식으로 계산합니다. E는 에너지 소비량, I는 탄소 강도, M은 하드웨어 내재 탄소, R은 기능 단위입니다. 핵심은 **총량이 아닌 비율**로 측정하여 서비스 규모와 무관하게 비교할 수 있다는 점입니다.

### Q: 개발자로서 당장 적용할 수 있는 Green Software 원칙은?

A: 세 가지를 제안합니다:
1. **알고리즘 최적화**: O(n²) → O(n)으로 개선하면 90%까지 에너지 절감 가능
2. **불필요한 데이터 전송 제거**: 캐싱, 배치 요청, 필요한 필드만 조회
3. **탄소 인지 스케줄링**: 비긴급 배치 작업을 저탄소 시간대(낮)에 실행

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [서버리스 컴퓨팅](./serverless.md) | 사용한 만큼만 과금 = 에너지 효율 | 중급 |
| [확장성](./scalability.md) | 효율적인 스케일링으로 에너지 절감 | 입문 |
| [캐싱](./caching.md) | DB 부하 감소로 에너지 절감 | 중급 |

## 참고 자료

- [Green Software Foundation](https://greensoftware.foundation/)
- [SCI Specification - ISO/IEC 21031:2024](https://sci.greensoftware.foundation/)
- [Green Software Foundation GitHub](https://github.com/Green-Software-Foundation/sci)
- [Green Software Development in 2025](https://www.techtalent.ro/green-software-development-in-2025-cutting-costs-and-carbon-in-the-digital-era/)
- [Why Software Carbon Intensity Matters](https://www.opensourcerers.org/2024/12/16/why-software-carbon-intensity-matters-an-introduction-to-the-sci-framework/)
