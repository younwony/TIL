# Kubernetes (K8s)

> `[4] 심화` · 선수 지식: [Docker](./docker.md), 컨테이너 개념

> 컨테이너화된 애플리케이션의 배포, 확장, 관리를 자동화하는 오픈소스 컨테이너 오케스트레이션 플랫폼

## 왜 알아야 하는가?

- **실무**: 컨테이너 기반 운영의 사실상 표준. 대부분의 클라우드 환경에서 사용
- **면접**: "K8s Pod와 Service 차이", "배포 전략" 등 DevOps/SRE 필수 질문
- **기반 지식**: 클라우드 네이티브, MSA 운영의 핵심 플랫폼

## 핵심 개념

- **컨테이너 오케스트레이션**: 다수의 컨테이너를 자동으로 배포, 확장, 운영하는 기술
- **선언적 구성(Declarative Configuration)**: 원하는 상태를 정의하면 K8s가 현재 상태를 맞춰줌
- **자가 치유(Self-healing)**: 컨테이너 실패 시 자동 재시작, 노드 장애 시 재스케줄링
- **수평 확장(Horizontal Scaling)**: 부하에 따라 Pod 수를 자동으로 조절
- **서비스 디스커버리와 로드밸런싱**: 컨테이너에 IP 주소와 DNS 이름 부여, 트래픽 분산

## 쉽게 이해하기

**Kubernetes**를 대형 물류 창고 관리 시스템에 비유할 수 있습니다.

### K8s = 자동화된 물류 창고 관리자

100개의 컨테이너(Docker)를 관리해야 한다면?
- 어디에 배치할지, 몇 개를 실행할지, 고장나면 어떻게 할지
- 수동으로 하면 밤새 모니터링해야 합니다

Kubernetes는 이 모든 걸 자동으로 해주는 "창고 관리 시스템"입니다.

### 핵심 개념 비유

| K8s 개념 | 물류 창고 비유 |
|----------|---------------|
| **Cluster** | 전체 물류 창고 단지 |
| **Node** | 개별 창고 건물 |
| **Pod** | 창고 안의 선반 한 칸 (컨테이너가 올라가는 곳) |
| **Deployment** | "A 상품 선반 3개 유지해" 라는 주문서 |
| **Service** | 안내 데스크 (어떤 선반에 뭐가 있는지 안내) |
| **Control Plane** | 중앙 관제 센터 |

### 선언적 구성 = 결과만 말하기

**명령형 (직접 지시)**: "창고 A에 가서 선반 꺼내고, B로 옮기고, 라벨 붙이고..."
**선언형 (결과만 선언)**: "A 상품 선반 3개가 항상 있어야 해"

K8s는 선언형입니다. "웹서버 3개 유지해"라고 말하면, K8s가 알아서:
- 서버가 2개면 → 1개 더 생성
- 서버가 4개면 → 1개 제거
- 서버가 죽으면 → 자동으로 새로 생성

### 자가 치유 = 자동 복구 시스템

창고에서 선반이 망가지면(컨테이너 장애), 관리 시스템이 자동으로:
1. 망가진 선반 발견
2. 새 선반 준비
3. 상품 재배치
4. 고객은 중단 없이 서비스 이용

## Kubernetes 아키텍처

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Control Plane (Master)                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────────┐  │
│  │ API Server  │  │  Scheduler  │  │ Controller  │  │     etcd      │  │
│  │             │  │             │  │   Manager   │  │  (Key-Value)  │  │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └───────────────┘  │
│         │                │                │                              │
└─────────┼────────────────┼────────────────┼──────────────────────────────┘
          │                │                │
          ▼                ▼                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                              Worker Nodes                                │
│  ┌────────────────────────────────────────────────────────────────────┐ │
│  │                            Node 1                                   │ │
│  │  ┌─────────┐  ┌─────────────────────────────────────────────────┐  │ │
│  │  │ kubelet │  │                     Pods                         │  │ │
│  │  └─────────┘  │  ┌─────────────┐  ┌─────────────┐               │  │ │
│  │  ┌─────────┐  │  │ Pod         │  │ Pod         │               │  │ │
│  │  │ kube-   │  │  │ ┌─────────┐ │  │ ┌─────────┐ │               │  │ │
│  │  │ proxy   │  │  │ │Container│ │  │ │Container│ │               │  │ │
│  │  └─────────┘  │  │ └─────────┘ │  │ └─────────┘ │               │  │ │
│  │               │  └─────────────┘  └─────────────┘               │  │ │
│  │               └─────────────────────────────────────────────────┘  │ │
│  └────────────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────────────┐ │
│  │                            Node 2                                   │ │
│  │                              ...                                    │ │
│  └────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

### Control Plane 구성 요소

| 구성 요소 | 역할 |
|-----------|------|
| **API Server** | 모든 요청의 진입점, REST API 제공, 인증/인가 처리 |
| **etcd** | 클러스터 상태를 저장하는 분산 Key-Value 저장소 |
| **Scheduler** | 새 Pod를 어느 노드에 배치할지 결정 |
| **Controller Manager** | 컨트롤러들을 실행 (ReplicaSet, Deployment 등) |

### Worker Node 구성 요소

| 구성 요소 | 역할 |
|-----------|------|
| **kubelet** | 노드에서 컨테이너 실행을 담당하는 에이전트 |
| **kube-proxy** | 네트워크 규칙 관리, 서비스 로드밸런싱 |
| **Container Runtime** | 컨테이너 실행 (containerd, CRI-O) |

---

## 핵심 오브젝트

### Pod

Kubernetes의 최소 배포 단위. 하나 이상의 컨테이너를 포함

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
  labels:
    app: my-app
spec:
  containers:
  - name: app
    image: my-app:1.0
    ports:
    - containerPort: 8080
    resources:
      requests:
        memory: "128Mi"
        cpu: "250m"
      limits:
        memory: "256Mi"
        cpu: "500m"
    livenessProbe:
      httpGet:
        path: /health
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
    readinessProbe:
      httpGet:
        path: /ready
        port: 8080
      initialDelaySeconds: 5
      periodSeconds: 5
```

### Deployment

Pod의 선언적 업데이트와 복제본 관리

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: app
        image: my-app:1.0
        ports:
        - containerPort: 8080
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
```

### Service

Pod 집합에 대한 네트워크 서비스 제공

```
┌─────────────────────────────────────────────────────────────────┐
│                        Service Types                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ClusterIP (기본)         NodePort              LoadBalancer     │
│  ┌─────────────┐         ┌─────────────┐       ┌─────────────┐  │
│  │ 클러스터     │         │  각 노드의   │       │ 클라우드     │  │
│  │ 내부 IP     │         │  포트 노출   │       │ LB 연결     │  │
│  └─────────────┘         └─────────────┘       └─────────────┘  │
│        │                       │                     │           │
│        ▼                       ▼                     ▼           │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │                         Pods                                 ││
│  │   ┌─────┐    ┌─────┐    ┌─────┐                            ││
│  │   │Pod 1│    │Pod 2│    │Pod 3│                            ││
│  │   └─────┘    └─────┘    └─────┘                            ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-app-service
spec:
  type: ClusterIP  # ClusterIP, NodePort, LoadBalancer, ExternalName
  selector:
    app: my-app
  ports:
  - port: 80          # Service 포트
    targetPort: 8080  # Pod 포트
    protocol: TCP
```

| 타입 | 설명 | 사용 사례 |
|------|------|----------|
| **ClusterIP** | 클러스터 내부 IP로만 접근 | 내부 서비스 간 통신 |
| **NodePort** | 각 노드의 고정 포트로 외부 노출 | 개발/테스트 환경 |
| **LoadBalancer** | 클라우드 로드밸런서 자동 프로비저닝 | 프로덕션 외부 노출 |
| **ExternalName** | 외부 DNS 이름으로 매핑 | 외부 서비스 접근 |

### Ingress

HTTP/HTTPS 라우팅과 로드밸런싱

```
┌─────────────────────────────────────────────────────────────────┐
│                           Ingress                                │
│                                                                  │
│   Client Request                                                 │
│        │                                                         │
│        ▼                                                         │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │              Ingress Controller (nginx)                  │   │
│   └─────────────────────────────────────────────────────────┘   │
│        │                                                         │
│        ├── api.example.com  ──────▶  api-service                │
│        │                                                         │
│        ├── web.example.com  ──────▶  web-service                │
│        │                                                         │
│        └── example.com/admin ─────▶  admin-service              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  tls:
  - hosts:
    - example.com
    secretName: tls-secret
  rules:
  - host: api.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: api-service
            port:
              number: 80
  - host: example.com
    http:
      paths:
      - path: /admin
        pathType: Prefix
        backend:
          service:
            name: admin-service
            port:
              number: 80
```

---

## 설정 관리

### ConfigMap

환경별 설정 데이터 관리

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  database_host: "db.example.com"
  log_level: "INFO"
  application.properties: |
    server.port=8080
    spring.profiles.active=production
```

### Secret

민감한 데이터 관리 (Base64 인코딩)

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db-secret
type: Opaque
data:
  username: YWRtaW4=        # base64 encoded
  password: cGFzc3dvcmQxMjM= # base64 encoded
```

```yaml
# Pod에서 ConfigMap/Secret 사용
spec:
  containers:
  - name: app
    image: my-app:1.0
    env:
    - name: DB_HOST
      valueFrom:
        configMapKeyRef:
          name: app-config
          key: database_host
    - name: DB_PASSWORD
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: password
    volumeMounts:
    - name: config-volume
      mountPath: /etc/config
  volumes:
  - name: config-volume
    configMap:
      name: app-config
```

---

## 스토리지

### PersistentVolume (PV) & PersistentVolumeClaim (PVC)

```
┌───────────────────────────────────────────────────────────────┐
│                     Storage Architecture                       │
│                                                                │
│   ┌───────────┐         ┌───────────┐         ┌───────────┐  │
│   │    Pod    │ ──────▶ │    PVC    │ ──────▶ │    PV     │  │
│   │           │  마운트   │  (요청)   │  바인딩   │  (실제)   │  │
│   └───────────┘         └───────────┘         └───────────┘  │
│                                                      │         │
│                                                      ▼         │
│                                              ┌─────────────┐  │
│                                              │   Storage   │  │
│                                              │  (EBS, NFS) │  │
│                                              └─────────────┘  │
└───────────────────────────────────────────────────────────────┘
```

```yaml
# PersistentVolume
apiVersion: v1
kind: PersistentVolume
metadata:
  name: my-pv
spec:
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  storageClassName: standard
  hostPath:
    path: /data/my-pv

---
# PersistentVolumeClaim
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 5Gi
  storageClassName: standard
```

| Access Mode | 설명 |
|-------------|------|
| **ReadWriteOnce (RWO)** | 단일 노드에서 읽기/쓰기 |
| **ReadOnlyMany (ROX)** | 여러 노드에서 읽기 전용 |
| **ReadWriteMany (RWX)** | 여러 노드에서 읽기/쓰기 |

---

## 오토스케일링

### Horizontal Pod Autoscaler (HPA)

CPU/메모리 사용량에 따라 Pod 수 자동 조절

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 300  # 5분 대기 후 스케일다운
```

### Vertical Pod Autoscaler (VPA)

Pod의 리소스 요청값 자동 조정

### Cluster Autoscaler

노드 수 자동 조절 (클라우드 환경)

---

## 배포 전략

### Rolling Update (기본)

```
Time →
┌─────┬─────┬─────┬─────┬─────┐
│ v1  │ v1  │ v1  │     │     │  초기 상태
├─────┼─────┼─────┼─────┼─────┤
│ v1  │ v1  │ v1  │ v2  │     │  새 버전 추가
├─────┼─────┼─────┼─────┼─────┤
│ v1  │ v1  │ v2  │ v2  │     │  구버전 제거, 새버전 추가
├─────┼─────┼─────┼─────┼─────┤
│ v1  │ v2  │ v2  │ v2  │     │  계속 교체
├─────┼─────┼─────┼─────┼─────┤
│ v2  │ v2  │ v2  │     │     │  완료
└─────┴─────┴─────┴─────┴─────┘
```

### Blue-Green Deployment

```yaml
# Blue (현재 버전)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-blue
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: app
        image: my-app:1.0

---
# Green (새 버전)
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-green
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: app
        image: my-app:2.0

---
# Service - selector 변경으로 트래픽 전환
apiVersion: v1
kind: Service
metadata:
  name: my-app
spec:
  selector:
    app: my-app
    version: green  # blue → green으로 변경
```

### Canary Deployment

일부 트래픽만 새 버전으로 라우팅

```yaml
# 기존 버전 90%
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-stable
spec:
  replicas: 9
  template:
    metadata:
      labels:
        app: my-app

---
# 새 버전 10%
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app-canary
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: my-app
```

---

## 주요 kubectl 명령어

```bash
# 클러스터 정보
kubectl cluster-info
kubectl get nodes

# 리소스 조회
kubectl get pods -n <namespace>
kubectl get deployments
kubectl get services
kubectl get all

# 상세 정보
kubectl describe pod <pod-name>
kubectl logs <pod-name> -f
kubectl logs <pod-name> -c <container-name>

# 리소스 생성/수정
kubectl apply -f deployment.yaml
kubectl create -f pod.yaml
kubectl delete -f service.yaml

# 스케일링
kubectl scale deployment my-app --replicas=5

# 롤아웃 관리
kubectl rollout status deployment/my-app
kubectl rollout history deployment/my-app
kubectl rollout undo deployment/my-app

# 디버깅
kubectl exec -it <pod-name> -- /bin/bash
kubectl port-forward <pod-name> 8080:8080
kubectl top pods
kubectl top nodes

# 컨텍스트/네임스페이스
kubectl config get-contexts
kubectl config use-context <context-name>
kubectl create namespace <namespace>
```

---

## Helm

Kubernetes 패키지 매니저

```bash
# 차트 설치
helm install my-release bitnami/nginx

# 차트 업그레이드
helm upgrade my-release bitnami/nginx --set replicaCount=3

# 릴리스 목록
helm list

# 차트 생성
helm create my-chart
```

### Chart 구조

```
my-chart/
├── Chart.yaml          # 차트 메타데이터
├── values.yaml         # 기본 설정값
├── templates/          # 템플릿 파일들
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── ingress.yaml
│   └── _helpers.tpl
└── charts/             # 의존성 차트
```

---

## 모범 사례

### 1. 리소스 제한 설정

```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "250m"
  limits:
    memory: "512Mi"
    cpu: "500m"
```

### 2. 헬스체크 설정

```yaml
livenessProbe:    # 컨테이너 재시작 여부 결정
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:   # 트래픽 수신 여부 결정
  httpGet:
    path: /ready
    port: 8080
  initialDelaySeconds: 5
  periodSeconds: 5
```

### 3. 네임스페이스 분리

```bash
# 환경별 분리
kubectl create namespace development
kubectl create namespace staging
kubectl create namespace production
```

### 4. RBAC 설정

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
  namespace: default
rules:
- apiGroups: [""]
  resources: ["pods"]
  verbs: ["get", "list", "watch"]
```

---

## 면접 예상 질문

- **Q: Pod와 Container의 차이점은 무엇인가요?**
  - A: Container는 단일 애플리케이션 프로세스를 실행하는 격리된 환경이고, Pod는 하나 이상의 Container를 포함하는 K8s의 최소 배포 단위입니다. 같은 Pod 내 Container들은 네트워크 네임스페이스(localhost 통신)와 스토리지를 공유하며, 함께 스케줄링됩니다. **왜 이렇게 답해야 하나요?** 웹 애플리케이션과 로그 수집기를 같은 Pod에 두면 localhost:8080으로 바로 통신하고, 로그 볼륨을 공유할 수 있습니다. 하지만 독립적인 확장이 필요하면 Pod를 분리해야 합니다.

- **Q: Deployment와 StatefulSet의 차이점은 무엇인가요?**
  - A: Deployment는 상태가 없는(Stateless) 애플리케이션용으로 Pod가 교체 가능하고 동일합니다. StatefulSet은 상태가 있는(Stateful) 애플리케이션용으로 각 Pod에 고유한 식별자와 안정적인 네트워크 ID, 영구 스토리지를 제공합니다. DB, Kafka 등에 사용됩니다. **왜 이렇게 답해야 하나요?** Deployment Pod는 web-abc, web-def처럼 랜덤 ID로 언제든 교체 가능하지만, StatefulSet은 mysql-0, mysql-1처럼 순서를 보장하고 재생성 시에도 같은 ID와 PV를 유지하여 데이터 일관성을 보장합니다.

- **Q: Service의 ClusterIP, NodePort, LoadBalancer 차이점은 무엇인가요?**
  - A: ClusterIP는 클러스터 내부에서만 접근 가능한 가상 IP를 제공합니다. NodePort는 모든 노드의 특정 포트(30000-32767)로 외부 접근을 허용합니다. LoadBalancer는 클라우드 환경에서 외부 로드밸런서를 자동으로 프로비저닝하여 외부 트래픽을 분산합니다. **왜 이렇게 답해야 하나요?** ClusterIP는 내부 서비스 간 통신용이고, NodePort는 개발 환경의 간단한 노출용이며, LoadBalancer는 프로덕션에서 고가용성 외부 노출을 위한 것입니다. 실제 프로덕션에서는 Ingress + LoadBalancer를 함께 사용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Docker](./docker.md) | 컨테이너 기술의 기초 | 중급 |
| [확장성](./scalability.md) | HPA를 통한 자동 확장 | 입문 |
| [가용성](./availability.md) | Self-healing, 자동 복구 | 입문 |
| [대규모 시스템 설계](./large-scale-system.md) | 컨테이너 오케스트레이션 아키텍처 | 심화 |

## 참고 자료

- [Kubernetes 공식 문서](https://kubernetes.io/docs/)
- [Kubernetes Patterns (O'Reilly)](https://www.oreilly.com/library/view/kubernetes-patterns/9781492050278/)
- [Helm 공식 문서](https://helm.sh/docs/)
