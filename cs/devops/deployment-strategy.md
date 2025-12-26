# Deployment Strategy (배포 전략)

> `[3] 중급` · 선수 지식: [CI/CD](./ci-cd.md), [Docker](../system-design/docker.md)

> 무중단으로 안전하게 새 버전을 배포하기 위한 전략들

`#DeploymentStrategy` `#배포전략` `#BlueGreen` `#Canary` `#RollingUpdate` `#ZeroDowntime` `#무중단배포` `#Rollback`

## 왜 알아야 하는가?

배포 전략은 **서비스 가용성**과 **배포 안전성**을 결정합니다. 잘못된 배포 전략은 서비스 중단, 데이터 손실, 사용자 이탈로 이어집니다. 현대적인 CI/CD 파이프라인에서 필수적인 지식이며, DevOps/SRE 면접의 핵심 주제입니다.

- **실무**: 무중단 배포, 롤백 전략, 리스크 최소화
- **면접**: "Blue-Green과 Canary의 차이는?", "롤백은 어떻게 하나요?"
- **기반 지식**: Kubernetes, 로드밸런싱, 모니터링

## 핵심 개념

- **Zero Downtime**: 서비스 중단 없는 배포
- **Rollback**: 문제 발생 시 이전 버전으로 복구
- **Traffic Shifting**: 새 버전으로 트래픽을 점진적으로 이동

## 쉽게 이해하기

**배포 전략**을 도로 공사에 비유할 수 있습니다.

```
Recreate (재생성) = 도로 전면 통제
┌─────────────────────────────────────┐
│  기존 도로 폐쇄 → 공사 → 재개통     │
│  → 공사 중 통행 불가 (다운타임)     │
└─────────────────────────────────────┘

Blue-Green = 새 도로 먼저 건설
┌─────────────────────────────────────┐
│  새 도로 완공 후 한 번에 전환        │
│  → 기존 도로는 비상용으로 유지       │
└─────────────────────────────────────┘

Canary = 일부 차선만 새 도로로
┌─────────────────────────────────────┐
│  10% 차선만 새 도로 시험 운행        │
│  → 문제 없으면 점진적으로 확대       │
└─────────────────────────────────────┘

Rolling = 한 차선씩 교체
┌─────────────────────────────────────┐
│  1차선 공사 → 2차선 공사 → ...      │
│  → 항상 일부 차선은 통행 가능        │
└─────────────────────────────────────┘
```

## 상세 설명

### 배포 전략 비교

```
┌─────────────────────────────────────────────────────────┐
│                   배포 전략 스펙트럼                      │
├─────────────────────────────────────────────────────────┤
│                                                          │
│   단순/빠름                              복잡/안전       │
│      │                                      │           │
│      ▼                                      ▼           │
│  Recreate → Rolling → Blue-Green → Canary → A/B        │
│                                                          │
│  다운타임↑         다운타임↓          리스크↓           │
│  리소스↓           리소스↑            복잡도↑           │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

### 1. Recreate (재생성)

```
Before:  [v1] [v1] [v1] [v1]
          ↓    ↓    ↓    ↓
Stop:    [--] [--] [--] [--]  ← 전체 중단 (다운타임)
          ↓    ↓    ↓    ↓
After:   [v2] [v2] [v2] [v2]
```

```yaml
# Kubernetes Deployment
spec:
  strategy:
    type: Recreate
```

| 장점 | 단점 |
|------|------|
| 단순한 구현 | 다운타임 발생 |
| 리소스 효율적 | 롤백 시 또 다운타임 |
| 버전 충돌 없음 | 사용자 영향 큼 |

**사용 시점**: 개발 환경, 버전 호환성 문제, 스키마 변경 시

### 2. Rolling Update (롤링 업데이트)

```
Step 1:  [v1] [v1] [v1] [v2]  ← 1개 교체
Step 2:  [v1] [v1] [v2] [v2]  ← 2개 교체
Step 3:  [v1] [v2] [v2] [v2]  ← 3개 교체
Step 4:  [v2] [v2] [v2] [v2]  ← 완료
```

```yaml
# Kubernetes Deployment
spec:
  replicas: 4
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1        # 최대 추가 Pod 수
      maxUnavailable: 1  # 최대 불가용 Pod 수
```

| 장점 | 단점 |
|------|------|
| 무중단 배포 | 롤백이 느림 (다시 롤링) |
| 점진적 배포 | 버전 호환성 필요 (v1↔v2 공존) |
| 리소스 효율적 | 배포 시간 길어짐 |

**사용 시점**: 일반적인 무중단 배포, K8s 기본 전략

### 3. Blue-Green Deployment

```
        ┌─────────────┐
        │ Load        │
        │ Balancer    │
        └─────┬───────┘
              │
        ┌─────┴─────┐
        │           │
   ┌────▼────┐ ┌────────┐
   │  Blue   │ │ Green  │
   │  (v1)   │ │  (v2)  │
   │ ACTIVE  │ │ STANDBY│
   └─────────┘ └────────┘

배포 전: Blue(v1) 활성
배포 후: Green(v2) 활성, Blue 대기
롤백:   Blue 다시 활성 (즉시)
```

```yaml
# Blue 환경
apiVersion: v1
kind: Service
metadata:
  name: my-app
spec:
  selector:
    app: my-app
    version: blue  # 또는 green으로 전환

---
# Blue Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-blue
spec:
  replicas: 4
  selector:
    matchLabels:
      app: my-app
      version: blue
```

```bash
# 트래픽 전환 (Blue → Green)
kubectl patch service my-app -p '{"spec":{"selector":{"version":"green"}}}'

# 롤백 (Green → Blue)
kubectl patch service my-app -p '{"spec":{"selector":{"version":"blue"}}}'
```

| 장점 | 단점 |
|------|------|
| 즉각적인 롤백 | 리소스 2배 필요 |
| 무중단 배포 | 데이터 마이그레이션 복잡 |
| 배포 전 완전한 테스트 | 세션 관리 필요 |

**사용 시점**: 빠른 롤백 필수, 충분한 인프라, 중요 서비스

### 4. Canary Deployment

```
Step 1: 5% 트래픽
┌─────────────────────────────────────┐
│ ████████████████████░ v1 (95%)      │
│ █░░░░░░░░░░░░░░░░░░░ v2 (5%)        │
└─────────────────────────────────────┘

Step 2: 25% 트래픽 (모니터링 후 확대)
┌─────────────────────────────────────┐
│ ███████████████░░░░░ v1 (75%)       │
│ █████░░░░░░░░░░░░░░░ v2 (25%)       │
└─────────────────────────────────────┘

Step 3: 100% 트래픽 (완료)
┌─────────────────────────────────────┐
│ ████████████████████ v2 (100%)      │
└─────────────────────────────────────┘
```

```yaml
# Istio VirtualService로 트래픽 분배
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: my-app
spec:
  hosts:
    - my-app
  http:
    - route:
        - destination:
            host: my-app
            subset: v1
          weight: 95
        - destination:
            host: my-app
            subset: v2
          weight: 5
```

```bash
# 점진적 트래픽 증가
kubectl apply -f canary-5.yaml   # 5%
# 모니터링...
kubectl apply -f canary-25.yaml  # 25%
# 모니터링...
kubectl apply -f canary-100.yaml # 100%
```

| 장점 | 단점 |
|------|------|
| 리스크 최소화 | 구현 복잡 |
| 실사용자 테스트 | 모니터링 필수 |
| 점진적 롤아웃 | 버전 호환성 필요 |

**사용 시점**: 대규모 사용자, 리스크가 높은 변경, A/B 테스트

### 5. A/B Testing (Feature Flag)

```
사용자 그룹별 다른 버전 제공

User A (그룹 1) → v1 (기존 UI)
User B (그룹 2) → v2 (새 UI)

측정: 전환율, 체류 시간, 클릭률 등
```

```java
// Feature Flag 예시
@RestController
public class CheckoutController {

    @GetMapping("/checkout")
    public String checkout(@RequestHeader("User-Id") String userId) {
        if (featureFlag.isEnabled("new-checkout", userId)) {
            return newCheckoutFlow();  // 새 버전
        }
        return oldCheckoutFlow();  // 기존 버전
    }
}
```

### 전략별 비교표

| 전략 | 다운타임 | 롤백 속도 | 리소스 | 복잡도 | 위험도 |
|------|---------|----------|--------|--------|--------|
| Recreate | 있음 | 느림 | 1x | 낮음 | 높음 |
| Rolling | 없음 | 느림 | 1.x | 중간 | 중간 |
| Blue-Green | 없음 | 즉시 | 2x | 중간 | 낮음 |
| Canary | 없음 | 빠름 | 1.x+ | 높음 | 매우 낮음 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 무중단 서비스 유지 | 인프라 복잡도 증가 |
| 빠른 롤백 가능 | 추가 리소스 필요 |
| 점진적 검증 | 모니터링 시스템 필수 |
| 리스크 분산 | 버전 호환성 관리 필요 |

## 면접 예상 질문

### Q: Blue-Green과 Canary의 차이는?

A: **Blue-Green**: 새 버전 환경을 완전히 준비 후 **100% 트래픽을 한 번에 전환**. 롤백이 즉각적(스위치 전환). 리소스 2배 필요. **Canary**: 새 버전에 **5% → 25% → 100%** 처럼 점진적으로 트래픽 증가. 실사용자로 테스트. 문제 시 영향 범위 제한. **선택 기준**: 빠른 롤백 필요 → Blue-Green, 리스크 최소화 → Canary.

### Q: 롤백은 어떻게 구현하나요?

A: (1) **Blue-Green**: 로드밸런서 타겟을 이전 버전으로 전환 (즉시) (2) **Canary**: 새 버전 트래픽을 0%로 조정 (3) **Rolling**: 이전 버전으로 다시 롤링 업데이트 (느림) (4) **Kubernetes**: `kubectl rollout undo deployment/my-app`. **핵심**: 이전 버전 이미지를 유지하고, 데이터 마이그레이션의 롤백 가능 여부도 고려해야 합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [CI/CD](./ci-cd.md) | 선수 지식 | [3] 중급 |
| [Docker](../system-design/docker.md) | 컨테이너 배포 | [3] 중급 |
| [Kubernetes](../system-design/kubernetes.md) | 오케스트레이션 | [3] 중급 |
| [가용성](../system-design/availability.md) | 무중단 배포 목표 | [2] 입문 |

## 참고 자료

- [Kubernetes Deployment Strategies](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)
- Continuous Delivery - Jez Humble
- [Blue-Green Deployments - Martin Fowler](https://martinfowler.com/bliki/BlueGreenDeployment.html)
