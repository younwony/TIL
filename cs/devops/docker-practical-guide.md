# Docker 실전 활용 가이드

> `[2] 입문` · 선수 지식: [Docker](../system-design/docker.md)

> Docker를 실무 개발 환경에서 활용하는 6가지 핵심 패턴과 워크플로우

`#Docker` `#도커` `#DockerCompose` `#컨테이너` `#Container` `#개발환경` `#DevEnvironment` `#LocalStack` `#로컬스택` `#개발인프라` `#환경재현` `#이미지배포` `#ImageDeploy` `#기술체험` `#PoC` `#MySQL` `#Redis` `#MongoDB` `#Kafka` `#Elasticsearch` `#Grafana` `#Jenkins` `#환경격리` `#마이그레이션` `#docker-compose.yml` `#Dockerfile` `#환경변수` `#볼륨` `#포트매핑` `#온보딩`

## 왜 알아야 하는가?

- **실무**: Docker를 "설치했다"만으로는 부족하다. 개발 인프라 구성, 운영 환경 재현, 기술 검증까지 **일상적으로 활용하는 패턴**을 알아야 생산성이 올라간다
- **면접**: "Docker를 어떻게 활용했나요?"라는 질문에 `docker run` 수준이 아닌, 구체적인 활용 사례를 답해야 한다
- **기반 지식**: CI/CD, Kubernetes, 클라우드 마이그레이션 등 모든 DevOps 워크플로우의 출발점이다

## 핵심 개념

- **개발용 인프라**: DB, Redis, MongoDB 등을 컨테이너로 실행하여 로컬 환경을 깔끔하게 유지
- **운영 환경 재현**: EC2 등 운영 서버와 동일한 환경을 로컬에서 구성하여 이슈를 디버깅
- **이미지 기반 배포**: 한 번 빌드한 이미지를 개발/테스트/운영에서 그대로 사용
- **AWS 로컬 체험**: LocalStack으로 S3, SQS, DynamoDB 등을 비용 없이 테스트
- **기술 즉시 체험**: Kafka, ELK, Grafana 등을 설치 과정 없이 `docker run` 한 줄로 실행
- **환경 격리**: 프로젝트별 서로 다른 버전의 미들웨어를 충돌 없이 동시 운영

## 쉽게 이해하기

Docker 활용을 **주방 비유**로 이해해보자.

### 설치형 개발 = 자기 집 주방

집 주방에 오븐, 믹서기, 에어프라이어를 전부 사들이면 공간도 차지하고, 안 쓸 때도 자리를 먹고, 고장나면 수리해야 한다.

### Docker 활용 = 공유 주방(키친 스튜디오)

필요한 장비만 빌려 쓰고, 다 쓰면 반납하면 끝이다.
- **개발 인프라** = 필요할 때 오븐 빌려 쓰기 (MySQL 컨테이너)
- **환경 재현** = 유명 셰프의 주방을 똑같이 세팅해서 레시피 테스트
- **LocalStack** = 고급 장비를 무료 체험판으로 써보기
- **기술 체험** = 새 장비 데모 체험 후 구매 결정

## 상세 설명

### 1. 개발용 인프라 (DB, Redis 등)

로컬에 직접 설치하지 않고 컨테이너로 실행한다. 안 쓰면 지우면 끝.

```bash
# MySQL
docker run -d --name mydb -e MYSQL_ROOT_PASSWORD=1234 -p 3306:3306 mysql:8.0

# Redis
docker run -d --name myredis -p 6379:6379 redis

# MongoDB
docker run -d -p 27017:27017 mongo
```

**왜 이렇게 하는가?**

| 직접 설치 | Docker |
|-----------|--------|
| PC에 서비스가 상시 실행 | 필요할 때만 up/down |
| DB 꼬이면 재설치 30분+ | 컨테이너 삭제 후 재생성 5초 |
| 버전 전환이 번거로움 | `mysql:5.7` ↔ `mysql:8.0` 자유 전환 |
| 팀원 온보딩에 설치 가이드 3페이지 | `docker compose up -d` 한 번이면 끝 |

---

### 2. 운영 환경 재현 (이슈 디버깅)

EC2 운영 환경을 로컬 Docker로 복제하여 이슈를 재현한다.

**EC2에서 수집할 정보:**

```bash
cat /etc/os-release       # OS 버전
java -version             # 런타임 버전
env                       # 환경변수
dpkg -l                   # 설치된 패키지 (Ubuntu)
```

**Dockerfile로 재현:**

```dockerfile
FROM amazonlinux:2023
RUN yum install -y java-17-amazon-corretto
ENV SPRING_PROFILES_ACTIVE=prod
COPY app.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
```

**왜 이렇게 하는가?**

운영에서만 발생하는 버그는 환경 차이가 원인인 경우가 많다. Docker로 OS, 런타임, 패키지 버전을 동일하게 맞추면 로컬에서 재현할 수 있다.

**동일하게 맞출 수 있는 것:**

| 항목 | 가능 여부 |
|------|----------|
| OS (Amazon Linux, Ubuntu 등) | O |
| 런타임 버전 (Java, Node, Python) | O |
| 패키지/라이브러리 버전 | O |
| 환경변수, 설정 파일 | O |
| DB, Redis 등 미들웨어 버전 | O |
| **CPU/RAM 스펙** | **X (호스트 한계)** |

> PC 스펙이 운영보다 낮을 때만 못 맞춘다. 같거나 높으면 `--memory=2g` 등으로 제한 가능.

---

### 3. 이미지 기반 배포 (개발 → 운영 동일 환경)

한 번 만든 이미지를 개발/테스트/운영에서 그대로 사용한다.

```bash
# 1. 개발 PC에서 이미지 빌드
docker build -t myapp:1.0 .

# 2. 레지스트리에 푸시
docker push myapp:1.0

# 3. 운영 EC2에서 그대로 실행
docker pull myapp:1.0
docker run -d myapp:1.0
```

**환경별로 다른 값은 환경변수로 분리:**

```bash
# 개발
docker run -e DB_HOST=localhost -e PROFILE=dev myapp:1.0

# 운영
docker run -e DB_HOST=prod-db.rds.amazonaws.com -e PROFILE=prod myapp:1.0
```

**왜 이렇게 하는가?**

"내 PC에서는 되는데 운영에서 안 돼요"를 원천 차단한다. 이미지가 동일하므로 바이너리, 의존성, 설정 파일이 모두 같고, 환경별 차이는 환경변수로만 제어한다.

---

### 4. AWS 서비스 로컬 체험 (LocalStack)

S3, SQS, DynamoDB, Lambda 등을 비용 0원으로 로컬에서 테스트한다.

```bash
# LocalStack 실행
docker run -d -p 4566:4566 localstack/localstack

# S3 버킷 생성
aws --endpoint-url=http://localhost:4566 s3 mb s3://my-bucket

# SQS 큐 생성
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name my-queue

# DynamoDB 테이블 생성
aws --endpoint-url=http://localhost:4566 dynamodb create-table ...
```

**왜 이렇게 하는가?**

실제 AWS와 API가 동일하다. `--endpoint-url`만 빼면 바로 운영에 적용할 수 있다. 기술 검토 워크플로우로 활용하면 효과적이다:

```
1. Docker로 체험/비교 (Kafka vs SQS 등)
2. Docker로 개발 + 테스트
3. 검증 완료 후 AWS에 동일하게 구성
   - Kafka → Amazon MSK
   - SQS → AWS SQS
   - 코드 변경 거의 없음
```

---

### 5. 안 써본 기술 즉시 체험

설치 과정 없이 `docker run` 한 줄이면 실행된다.

```bash
# Kafka
docker run -d confluentinc/cp-kafka

# Elasticsearch + Kibana
docker run -d -p 9200:9200 elasticsearch:8.12.0
docker run -d -p 5601:5601 kibana:8.12.0

# RabbitMQ (관리 콘솔 포함)
docker run -d -p 15672:15672 rabbitmq:management

# Grafana (모니터링 대시보드)
docker run -d -p 3000:3000 grafana/grafana

# Jenkins (CI/CD)
docker run -d -p 8080:8080 jenkins/jenkins

# WordPress
docker run -d -p 80:80 wordpress
```

**왜 이렇게 하는가?**

새 기술 도입을 검토할 때 설치 가이드 3페이지를 읽고 환경을 세팅하는 대신, 30초 만에 실행하고 직접 써볼 수 있다. 마음에 안 들면 컨테이너를 삭제하면 PC에 흔적이 남지 않는다.

---

### 6. 기타 활용

| 사례 | 설명 | 예시 |
|------|------|------|
| **프로젝트별 환경 격리** | 서로 다른 버전 동시 실행 | A 프로젝트(MySQL 5.7) + B 프로젝트(MySQL 8.0) |
| **DB 마이그레이션 검증** | 운영 스키마 덤프 → 로컬 테스트 | 마이그레이션 스크립트 사전 검증 |
| **크론잡/배치** | 실행 후 자동 삭제 | `docker run --rm myapp batch-job` |
| **버전 비교 테스트** | 여러 런타임 버전 동시 비교 | Java 17 vs 21 성능 비교 |

## 동작 원리

![Docker 실전 활용 사례](./images/docker-practical-usecases.svg)

## Docker Compose 실전 예시

여러 컨테이너를 한 번에 관리하는 `docker-compose.yml` 예시:

```yaml
services:
  app:
    build: .
    ports: ["8080:8080"]
    environment:
      DB_HOST: mysql
    depends_on:
      - mysql
      - redis

  mysql:
    image: mysql:8.0
    ports: ["3306:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: "1234"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7
    ports: ["6379:6379"]

volumes:
  mysql_data:
```

```bash
docker compose up -d    # 전체 시작
docker compose down     # 전체 종료
docker compose down -v  # 종료 + 데이터 초기화
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 환경 구성이 빠르고 재현 가능 | Docker 자체 학습 비용 |
| PC를 깔끔하게 유지 | 디스크 사용량 증가 (이미지 캐시) |
| 팀 전체가 동일 환경 공유 | Windows/Mac은 Docker Desktop 필요 (리소스 소비) |
| 버전 전환이 자유로움 | 네트워크/볼륨 설정 복잡도 |
| 운영 환경 재현 가능 | CPU/RAM은 호스트 스펙을 넘을 수 없음 |

## 트러블슈팅

### 사례 1: 포트 충돌

#### 증상

```
Error: Bind for 0.0.0.0:3306 failed: port is already allocated
```

#### 원인 분석

호스트의 3306 포트를 다른 프로세스(로컬 MySQL 등)가 이미 점유하고 있다.

#### 해결 방법

```bash
# 방법 1: 다른 포트로 매핑
docker run -d -p 3307:3306 mysql:8.0

# 방법 2: 기존 프로세스 확인 후 중지
# Linux/Mac
lsof -i :3306
# Windows
netstat -ano | findstr :3306
```

#### 예방 조치

`docker-compose.yml`에서 프로젝트별로 포트 대역을 분리한다 (예: A 프로젝트 33060, B 프로젝트 33061).

---

### 사례 2: 볼륨 데이터 유실

#### 증상

컨테이너를 재생성했더니 DB 데이터가 전부 사라졌다.

#### 원인 분석

Named Volume 없이 컨테이너를 실행하면, 컨테이너 삭제 시 내부 데이터도 함께 삭제된다.

#### 해결 방법

```yaml
# docker-compose.yml에서 Named Volume 사용
services:
  mysql:
    image: mysql:8.0
    volumes:
      - mysql_data:/var/lib/mysql  # Named Volume

volumes:
  mysql_data:  # Docker가 관리하는 영속 볼륨
```

#### 예방 조치

개발 데이터라도 보존이 필요하면 반드시 Named Volume을 설정한다. 의도적 초기화는 `docker compose down -v`로 명시적으로 수행한다.

---

### 사례 3: 이미지 캐시로 디스크 부족

#### 증상

```
No space left on device
```

#### 원인 분석

오래된 이미지, 중지된 컨테이너, 미사용 볼륨이 누적되어 디스크를 점유한다.

#### 해결 방법

```bash
# 미사용 리소스 일괄 정리
docker system prune -a

# 개별 정리
docker image prune -a    # 미사용 이미지
docker volume prune      # 미사용 볼륨
docker container prune   # 중지된 컨테이너
```

#### 예방 조치

주기적으로 `docker system df`로 디스크 사용량을 확인한다.

## 면접 예상 질문

### Q: Docker를 실무에서 어떻게 활용했나요?

A: 개발 환경에서 MySQL, Redis 등을 컨테이너로 실행하여 로컬 PC를 깔끔하게 유지하고, `docker-compose.yml`로 팀 전체가 동일한 개발 환경을 공유했습니다. 운영에서만 발생하는 버그는 운영 서버와 동일한 OS, 런타임, 패키지 버전의 컨테이너를 만들어 로컬에서 재현했습니다. **왜 이렇게 했나요?** 직접 설치 방식은 팀원마다 환경이 달라지고, 온보딩에 시간이 많이 걸리기 때문입니다. Docker Compose 파일 하나로 환경 구성을 코드화하면 이런 문제를 해결할 수 있습니다.

### Q: "내 PC에서는 되는데 서버에서 안 돼요" 문제를 어떻게 해결하나요?

A: Docker 이미지 기반 배포로 해결합니다. 개발 PC에서 빌드한 이미지를 레지스트리에 푸시하고, 운영 서버에서 동일한 이미지를 풀해서 실행합니다. 환경별로 다른 값(DB 호스트, 프로파일 등)은 환경변수로 분리합니다. **왜 이렇게 하나요?** 이미지가 동일하면 바이너리, 의존성, 설정 파일이 모두 같으므로 환경 차이로 인한 버그를 원천 차단할 수 있습니다.

### Q: LocalStack이 뭔가요? 왜 사용하나요?

A: LocalStack은 AWS 서비스(S3, SQS, DynamoDB 등)를 로컬 Docker 컨테이너에서 에뮬레이션하는 도구입니다. 실제 AWS와 API가 동일하므로 `--endpoint-url`만 변경하면 로컬 테스트 코드를 그대로 운영에 적용할 수 있습니다. **왜 사용하나요?** AWS 리소스를 직접 생성하면 비용이 발생하고, 테스트 환경 관리가 복잡해집니다. LocalStack으로 비용 0원으로 개발/테스트하고, 검증이 완료되면 실제 AWS에 동일하게 구성합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Docker](../system-design/docker.md) | 선수 지식 - Docker 이론 (아키텍처, Dockerfile, 네트워크, 볼륨) | 중급 |
| [CI/CD](./ci-cd.md) | Docker 이미지 빌드/배포를 자동화하는 파이프라인 | 입문 |
| [모니터링](./monitoring.md) | Grafana, ELK 등 모니터링 도구를 Docker로 실행 | 중급 |
| [IaC](./iac.md) | Docker Compose를 넘어 인프라를 코드로 관리 | 중급 |
| [배포 전략](./deployment-strategy.md) | Docker 이미지 기반 Blue-Green, Canary 배포 | 중급 |

## 참고 자료

- [Docker 공식 문서](https://docs.docker.com/)
- [Docker Compose 문서](https://docs.docker.com/compose/)
- [LocalStack 공식 문서](https://docs.localstack.cloud/)
- [Docker Hub](https://hub.docker.com/)
