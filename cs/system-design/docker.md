# Docker

> 애플리케이션을 컨테이너라는 격리된 환경에서 실행할 수 있게 해주는 오픈소스 컨테이너 플랫폼

## 핵심 개념

- **컨테이너(Container)**: 애플리케이션과 의존성을 패키징하여 격리된 환경에서 실행하는 경량 가상화 기술
- **이미지(Image)**: 컨테이너를 생성하기 위한 읽기 전용 템플릿, 레이어 구조로 구성
- **Dockerfile**: 이미지를 빌드하기 위한 명령어 스크립트
- **레지스트리(Registry)**: 이미지를 저장하고 배포하는 저장소 (Docker Hub, ECR, GCR 등)
- **오케스트레이션**: 여러 컨테이너의 배포, 확장, 관리를 자동화 (Kubernetes, Docker Swarm)

## 쉽게 이해하기

**Docker**를 이삿짐 포장과 컨테이너 운송에 비유할 수 있습니다.

### 컨테이너 = 이삿짐 박스

이사할 때 물건을 그냥 옮기면 깨지거나 잃어버리기 쉽습니다. 박스에 잘 포장해서 옮기면 안전하죠.

**개발 환경 문제**: "내 컴퓨터에서는 되는데..." 문제
- 개발자 A: Java 11, MySQL 8.0
- 개발자 B: Java 17, MySQL 5.7
- 서버: Java 8, MySQL 8.0
- 같은 코드인데 다르게 동작!

**Docker 해결**: 애플리케이션 + 필요한 모든 것을 박스(컨테이너)에 담아서 어디서든 똑같이 실행

### 이미지 vs 컨테이너 = 레시피 vs 요리

| 비유 | Docker 개념 |
|------|-------------|
| 레시피 (설계도) | Image - 무엇이 들어가는지 정의 |
| 완성된 요리 | Container - 실제로 실행 중인 것 |
| 요리책 | Docker Hub - 레시피 저장소 |

같은 레시피(이미지)로 여러 개의 요리(컨테이너)를 만들 수 있습니다.

### 컨테이너 vs 가상머신 = 아파트 vs 단독주택

| 비유 | 컨테이너 | 가상머신 |
|------|---------|----------|
| 주거 형태 | 아파트 (건물 공유) | 단독주택 (독립) |
| 공유 자원 | 커널(운영체제 핵심) 공유 | 각자 OS 설치 |
| 크기 | 작음 (MB 단위) | 큼 (GB 단위) |
| 시작 시간 | 빠름 (초 단위) | 느림 (분 단위) |
| 격리 수준 | 프로세스 수준 | 완전한 OS 격리 |

아파트는 건물 기반시설(수도, 전기)을 공유하면서도 각 가구가 독립적으로 생활합니다. 컨테이너도 OS 커널을 공유하면서 각 앱이 독립적으로 실행됩니다.

## 컨테이너 vs 가상머신(VM)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    컨테이너 (Container)                              │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────┐ ┌─────────┐ ┌─────────┐                               │
│  │  App A  │ │  App B  │ │  App C  │                               │
│  ├─────────┤ ├─────────┤ ├─────────┤                               │
│  │ Bins/   │ │ Bins/   │ │ Bins/   │                               │
│  │ Libs    │ │ Libs    │ │ Libs    │                               │
│  └─────────┘ └─────────┘ └─────────┘                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Docker Engine                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                      Host OS                                 │   │
│  └─────────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                     Infrastructure                           │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    가상머신 (Virtual Machine)                        │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐       │
│  │     App A       │ │     App B       │ │     App C       │       │
│  ├─────────────────┤ ├─────────────────┤ ├─────────────────┤       │
│  │   Bins/Libs     │ │   Bins/Libs     │ │   Bins/Libs     │       │
│  ├─────────────────┤ ├─────────────────┤ ├─────────────────┤       │
│  │   Guest OS      │ │   Guest OS      │ │   Guest OS      │       │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                      Hypervisor                              │   │
│  └─────────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                     Infrastructure                           │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

| 특성 | 컨테이너 | 가상머신 |
|------|---------|----------|
| **부팅 시간** | 초 단위 | 분 단위 |
| **크기** | MB 단위 | GB 단위 |
| **격리 수준** | 프로세스 수준 | 완전한 OS 격리 |
| **성능** | 네이티브에 가까움 | 오버헤드 존재 |
| **이식성** | 매우 높음 | 높음 |
| **자원 효율** | 높음 (커널 공유) | 낮음 (각 OS 필요) |

---

## Docker 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                         Docker Client                            │
│                    (docker build, run, pull)                     │
└─────────────────────────┬───────────────────────────────────────┘
                          │ REST API
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                         Docker Daemon                            │
│                          (dockerd)                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Images     │  │  Containers  │  │   Networks   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│  ┌──────────────┐  ┌──────────────┐                             │
│  │   Volumes    │  │   Plugins    │                             │
│  └──────────────┘  └──────────────┘                             │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Registry                                  │
│                  (Docker Hub, ECR, GCR)                          │
└─────────────────────────────────────────────────────────────────┘
```

### 핵심 구성 요소

- **Docker Client**: 사용자가 Docker와 상호작용하는 CLI 도구
- **Docker Daemon**: 컨테이너 빌드, 실행, 배포를 담당하는 백그라운드 서비스
- **Docker Registry**: 이미지를 저장하고 배포하는 중앙 저장소

---

## Docker 이미지

### 이미지 레이어 구조

```
┌─────────────────────────────────────┐
│         Application Layer            │  ← 애플리케이션 코드
├─────────────────────────────────────┤
│         Dependencies Layer           │  ← npm, pip 패키지 등
├─────────────────────────────────────┤
│         Runtime Layer                │  ← Node.js, Python 등
├─────────────────────────────────────┤
│         Base Image Layer             │  ← Ubuntu, Alpine 등
└─────────────────────────────────────┘
```

- 각 레이어는 읽기 전용이며 캐시됨
- 컨테이너 실행 시 최상단에 쓰기 가능한 레이어 추가
- 레이어 공유로 저장 공간과 빌드 시간 절약

### 주요 명령어

```bash
# 이미지 관련
docker images                    # 이미지 목록 조회
docker pull nginx:latest         # 이미지 다운로드
docker build -t myapp:1.0 .      # 이미지 빌드
docker push myrepo/myapp:1.0     # 이미지 업로드
docker rmi myapp:1.0             # 이미지 삭제

# 컨테이너 관련
docker ps                        # 실행 중인 컨테이너 목록
docker ps -a                     # 전체 컨테이너 목록
docker run -d -p 8080:80 nginx   # 컨테이너 실행
docker stop <container_id>       # 컨테이너 중지
docker rm <container_id>         # 컨테이너 삭제
docker logs <container_id>       # 로그 확인
docker exec -it <container_id> bash  # 컨테이너 접속
```

---

## Dockerfile

### 기본 구조

```dockerfile
# 베이스 이미지 지정
FROM openjdk:17-slim

# 메타데이터 설정
LABEL maintainer="developer@example.com"
LABEL version="1.0"

# 환경변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 작업 디렉토리 설정
WORKDIR /app

# 의존성 파일 복사 (캐시 활용)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew build -x test --no-daemon

# 실행할 JAR 파일 복사
RUN cp build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 컨테이너 실행 명령어
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
```

### 주요 Dockerfile 명령어

| 명령어 | 설명 | 예시 |
|--------|------|------|
| `FROM` | 베이스 이미지 지정 | `FROM node:18-alpine` |
| `COPY` | 파일/디렉토리 복사 | `COPY package*.json ./` |
| `ADD` | COPY + URL/압축 해제 지원 | `ADD app.tar.gz /app` |
| `RUN` | 빌드 시 명령어 실행 | `RUN npm install` |
| `CMD` | 컨테이너 실행 시 기본 명령어 | `CMD ["npm", "start"]` |
| `ENTRYPOINT` | 컨테이너 실행 시 고정 명령어 | `ENTRYPOINT ["java", "-jar"]` |
| `ENV` | 환경변수 설정 | `ENV NODE_ENV=production` |
| `EXPOSE` | 포트 문서화 | `EXPOSE 3000` |
| `WORKDIR` | 작업 디렉토리 설정 | `WORKDIR /app` |
| `VOLUME` | 볼륨 마운트 포인트 | `VOLUME ["/data"]` |
| `ARG` | 빌드 시 변수 | `ARG VERSION=1.0` |
| `USER` | 실행 사용자 지정 | `USER appuser` |

### 멀티스테이지 빌드

빌드 환경과 실행 환경을 분리하여 이미지 크기 최소화:

```dockerfile
# 빌드 스테이지
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# 실행 스테이지
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Docker Compose

여러 컨테이너를 정의하고 실행하기 위한 도구

### docker-compose.yml 예시

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=local
      - DATABASE_URL=jdbc:postgresql://db:5432/mydb
    depends_on:
      db:
        condition: service_healthy
      redis:
        condition: service_started
    networks:
      - backend
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped

  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - backend
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U user -d mydb"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - backend
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data

networks:
  backend:
    driver: bridge

volumes:
  postgres_data:
  redis_data:
```

### Docker Compose 명령어

```bash
docker-compose up -d             # 모든 서비스 백그라운드 실행
docker-compose down              # 모든 서비스 중지 및 삭제
docker-compose ps                # 서비스 상태 확인
docker-compose logs -f app       # 특정 서비스 로그 확인
docker-compose build             # 이미지 빌드
docker-compose restart app       # 특정 서비스 재시작
docker-compose exec app bash     # 서비스 컨테이너 접속
```

---

## Docker 네트워크

### 네트워크 드라이버

| 드라이버 | 설명 | 사용 사례 |
|----------|------|----------|
| **bridge** | 기본 네트워크, 같은 호스트 내 컨테이너 통신 | 단일 호스트 애플리케이션 |
| **host** | 호스트 네트워크 직접 사용 | 네트워크 성능이 중요한 경우 |
| **overlay** | 여러 호스트 간 컨테이너 통신 | Docker Swarm, 분산 환경 |
| **none** | 네트워크 비활성화 | 완전한 네트워크 격리 |

```bash
# 네트워크 생성
docker network create --driver bridge my-network

# 컨테이너를 네트워크에 연결
docker run -d --network my-network --name app myapp

# 네트워크 목록 조회
docker network ls

# 네트워크 상세 정보
docker network inspect my-network
```

---

## Docker 볼륨

데이터 영속성을 위한 저장소 관리

### 볼륨 타입

```
┌───────────────────────────────────────────────────────────────────┐
│                        Volume Types                                │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. Named Volume (권장)                                            │
│     docker run -v mydata:/app/data myapp                          │
│     └── Docker가 관리하는 볼륨                                      │
│                                                                    │
│  2. Bind Mount                                                     │
│     docker run -v /host/path:/container/path myapp                │
│     └── 호스트 디렉토리 직접 마운트                                  │
│                                                                    │
│  3. tmpfs Mount                                                    │
│     docker run --tmpfs /app/temp myapp                            │
│     └── 메모리에 임시 저장                                          │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

```bash
# 볼륨 생성
docker volume create my-volume

# 볼륨과 함께 컨테이너 실행
docker run -d -v my-volume:/app/data myapp

# 볼륨 목록 조회
docker volume ls

# 볼륨 삭제
docker volume rm my-volume

# 사용하지 않는 볼륨 정리
docker volume prune
```

---

## Docker 모범 사례

### 1. 이미지 최적화

```dockerfile
# 경량 베이스 이미지 사용
FROM node:18-alpine    # (O) ~50MB
# FROM node:18         # (X) ~350MB

# .dockerignore 활용
# .dockerignore 파일:
# node_modules
# .git
# *.log
# Dockerfile
```

### 2. 레이어 캐시 활용

```dockerfile
# 변경이 적은 레이어를 먼저 배치
COPY package*.json ./
RUN npm ci --only=production    # 의존성은 자주 안 바뀜

COPY . .                        # 소스코드는 자주 바뀜
```

### 3. 보안 강화

```dockerfile
# root가 아닌 사용자로 실행
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# 비밀정보는 빌드 인자로 전달하지 않음
# (X) ARG DB_PASSWORD
# (O) 환경변수나 시크릿 관리 도구 사용
```

### 4. 헬스체크 설정

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1
```

---

## 면접 예상 질문

- **Q: 컨테이너와 가상머신의 차이점은 무엇인가요?**
  - A: 가상머신은 Hypervisor 위에 각각의 Guest OS를 실행하여 완전한 격리를 제공하지만 무겁습니다. 컨테이너는 Host OS의 커널을 공유하고 프로세스 수준의 격리를 제공하여 가볍고 빠릅니다. 컨테이너는 초 단위로 시작되고 MB 단위 크기인 반면, VM은 분 단위 시작과 GB 단위 크기입니다.

- **Q: Docker 이미지와 컨테이너의 차이는 무엇인가요?**
  - A: 이미지는 애플리케이션을 실행하기 위한 파일 시스템과 설정을 포함하는 읽기 전용 템플릿입니다. 컨테이너는 이미지를 기반으로 생성된 실행 가능한 인스턴스로, 이미지 위에 쓰기 가능한 레이어가 추가됩니다. 클래스와 객체의 관계와 유사합니다.

- **Q: 멀티스테이지 빌드란 무엇이고 왜 사용하나요?**
  - A: 멀티스테이지 빌드는 하나의 Dockerfile에서 여러 FROM 명령을 사용하여 빌드 단계와 실행 단계를 분리하는 기법입니다. 빌드에 필요한 도구(컴파일러, 빌드 도구 등)를 최종 이미지에서 제외하여 이미지 크기를 줄이고 보안을 강화할 수 있습니다.

## 참고 자료

- [Docker 공식 문서](https://docs.docker.com/)
- [Dockerfile 모범 사례](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
- [Docker Compose 문서](https://docs.docker.com/compose/)
