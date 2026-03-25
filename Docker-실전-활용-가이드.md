# Docker 실전 활용 가이드

## Docker란?

- 컨테이너 기반 **프로세스 격리** 기술 (가상머신이 아님)
- 호스트 OS 위에서 격리된 환경을 만들어 앱을 실행
- CPU/RAM은 호스트 스펙을 넘을 수 없지만, 그 외 소프트웨어 환경은 완전히 동일하게 구성 가능

---

## 주요 활용 사례

### 1. 개발용 인프라 (DB, Redis 등)

로컬에 직접 설치 없이 컨테이너로 실행. 안 쓰면 지우면 끝.

```bash
# MySQL
docker run -d --name mydb -e MYSQL_ROOT_PASSWORD=1234 -p 3306:3306 mysql:8.0

# Redis
docker run -d --name myredis -p 6379:6379 redis

# MongoDB
docker run -d -p 27017:27017 mongo
```

**이점:**
- PC에 서비스가 상시 실행되지 않음 (필요할 때만 up/down)
- DB 꼬이면 컨테이너 지우고 새로 만들면 5초만에 복구
- 버전 전환 자유로움 (`mysql:5.7` ↔ `mysql:8.0`)
- 팀원 온보딩 시 `docker compose up -d` 한 번이면 끝

---

### 2. 운영 환경 재현 (이슈 디버깅)

EC2 운영 환경을 로컬 Docker로 복제하여 이슈 재현.

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

**동일하게 맞출 수 있는 것:**
| 항목 | 가능 여부 |
|------|----------|
| OS (Amazon Linux, Ubuntu 등) | O |
| 런타임 버전 (Java, Node, Python) | O |
| 패키지/라이브러리 버전 | O |
| 환경변수, 설정 파일 | O |
| DB, Redis 등 미들웨어 버전 | O |
| **CPU/RAM 스펙** | **X (호스트 한계)** |

> PC 스펙이 운영보다 낮을 때만 못 맞춤. 높거나 같으면 `--memory=2g` 등으로 제한 가능.

---

### 3. 이미지 기반 배포 (개발 → 운영 동일 환경)

한번 만든 이미지를 개발/테스트/운영에서 그대로 사용.

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

> "내 PC에서는 되는데 운영에서 안 돼요" 원천 차단

---

### 4. AWS 서비스 로컬 체험 (LocalStack)

S3, SQS, DynamoDB, Lambda 등을 비용 0원으로 로컬에서 테스트.

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

> 실제 AWS와 API가 동일. `--endpoint-url`만 빼면 바로 운영 적용 가능.

**기술 검토 워크플로우:**
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

설치 과정 없이 `docker run` 한 줄이면 실행.

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

---

### 6. 기타 활용

- **여러 프로젝트 환경 격리**: A 프로젝트(MySQL 5.7), B 프로젝트(MySQL 8.0) 동시 실행
- **DB 마이그레이션 사전 검증**: 운영 스키마 덤프 → 로컬 컨테이너에서 마이그레이션 테스트
- **크론잡/배치**: `docker run --rm`으로 실행 후 자동 삭제
- **여러 버전 동시 테스트**: Java 17 vs 21 등 버전별 비교

---

## 기본 명령어

```bash
# 컨테이너 실행
docker run -d -p 8080:80 nginx

# 실행 중인 컨테이너 확인
docker ps

# 모든 컨테이너 확인
docker ps -a

# 컨테이너 중지/시작/삭제
docker stop <컨테이너ID>
docker start <컨테이너ID>
docker rm <컨테이너ID>

# 이미지 목록/다운로드
docker images
docker pull ubuntu

# 컨테이너 안으로 접속
docker exec -it <컨테이너ID> bash
```

## Docker Compose (여러 컨테이너 한번에)

```yaml
# docker-compose.yml
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
docker compose up -d    # 시작
docker compose down     # 종료
docker compose down -v  # 종료 + 데이터 초기화
```

---

## 핵심 정리

| 예전 | Docker |
|------|--------|
| 로컬에 MySQL 직접 설치 → PC 지저분 | 컨테이너로 격리, 안 쓰면 삭제 |
| AWS에 직접 구성 → 비용 발생 | LocalStack으로 무료 테스트 |
| "내 PC에서는 되는데..." | 동일 이미지로 개발~운영 통일 |
| 새 기술 설치 가이드 3페이지 | `docker run` 한 줄 |
| 환경 꼬이면 반나절 삽질 | 컨테이너 지우고 새로 만들기 5초 |
