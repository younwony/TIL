# 프로젝트 서버 탐지 & 기동 레퍼런스

Phase 0에서 프로젝트 타입을 자동 탐지하고 서버를 기동할 때 참조하는 레퍼런스.

## 프로젝트 타입별 탐지 및 기동

### Spring Boot (Gradle)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `build.gradle`, `build.gradle.kts` |
| **포트 설정** | `src/main/resources/application.yml` → `server.port` |
| | `src/main/resources/application.properties` → `server.port` |
| **빌드 커맨드** | `./gradlew bootJar` (Linux/Mac) |
| | `gradlew.bat bootJar` (Windows) |
| **JAR 탐지** | `build/libs/*.jar` (plain JAR 제외, `-plain.jar` 무시) |
| **기동 커맨드** | `java -jar build/libs/{app}.jar --server.port={port}` |
| **기본 포트** | 8080 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |
| **준비 완료 신호** | HTTP 200~399 응답 또는 로그에 "Started *Application" 출력 |

**WAR 프로젝트 탐지:**
- `build.gradle`에 `apply plugin: 'war'` 또는 `id 'war'` 존재 시
- 빌드: `./gradlew bootWar` (Windows: `gradlew.bat bootWar`)
- JAR 탐지 → `build/libs/*.war` (`-plain.war` 무시)
- 기동: `java -jar build/libs/{app}.war --server.port={port}`

### Spring Boot (Maven)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `pom.xml` (spring-boot-starter 의존성 포함) |
| **포트 설정** | `src/main/resources/application.yml` → `server.port` |
| | `src/main/resources/application.properties` → `server.port` |
| **빌드 커맨드** | `./mvnw package -DskipTests` (Linux/Mac) |
| | `mvnw.cmd package -DskipTests` (Windows) |
| **JAR 탐지** | `target/*.jar` (original JAR 제외, `*.jar.original` 무시) |
| **기동 커맨드** | `java -jar target/{app}.jar --server.port={port}` |
| **기본 포트** | 8080 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |

**WAR 프로젝트 탐지:**
- `pom.xml`에 `<packaging>war</packaging>` 존재 시
- 빌드: 동일 (`mvnw package -DskipTests`)
- JAR 탐지 → `target/*.war`
- 기동: `java -jar target/{app}.war --server.port={port}`

### Node.js (npm/yarn/pnpm)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `package.json` |
| **포트 설정** | `package.json` → scripts에서 `--port` 플래그 |
| | `.env` → `PORT` 변수 |
| | 소스 코드에서 `listen(PORT)` 패턴 |
| **기동 커맨드 (우선순위)** | 1. `npm run dev` (scripts.dev 존재 시) |
| | 2. `npm start` (scripts.start 존재 시) |
| | 3. `npx next dev` (Next.js) |
| | 4. `npx vite` (Vite) |
| **기본 포트** | 3000 (Express/Next.js), 5173 (Vite) |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |

**패키지 매니저 탐지:**
- `yarn.lock` 존재 → `yarn dev` / `yarn start`
- `pnpm-lock.yaml` 존재 → `pnpm dev` / `pnpm start`
- 그 외 → `npm run dev` / `npm start`

### Python (Django/Flask/FastAPI)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `requirements.txt`, `pyproject.toml`, `Pipfile` |
| **프레임워크 탐지** | `manage.py` → Django |
| | `app.py` + Flask import → Flask |
| | `main.py` + FastAPI import → FastAPI |
| **기동 커맨드** | Django: `python manage.py runserver` |
| | Flask: `flask run` 또는 `python app.py` |
| | FastAPI: `uvicorn main:app --reload` |
| **기본 포트** | 8000 (Django/FastAPI), 5000 (Flask) |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |

### Docker Compose

| 항목 | 값 |
|------|------|
| **탐지 파일** | `docker-compose.yml`, `docker-compose.yaml`, `compose.yml` |
| **포트 설정** | `docker-compose.yml` → services.*.ports |
| **기동 커맨드** | `docker-compose up -d` |
| **기본 포트** | compose 파일의 첫 번째 서비스 포트 매핑 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |

### 정적 HTML

| 항목 | 값 |
|------|------|
| **탐지 조건** | 위 모든 타입에 해당하지 않고 `index.html` 존재 |
| **기동 커맨드** | `python -m http.server 8000` |
| **기본 포트** | 8000 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:8000` |

## Java 프로젝트 빌드 & 기동 절차

Java 프로젝트(Spring Boot)는 반드시 **빌드 → JAR/WAR 기동** 방식을 사용한다.
`bootRun`이나 `spring-boot:run`은 사용하지 않는다.

### 절차

```
1. 빌드 도구 탐지 (Gradle / Maven)
2. WAR 패키징 여부 확인
3. 빌드 실행 (bootJar / bootWar / package)
4. 빌드 산출물(JAR/WAR) 경로 탐지
5. 포트 결정 (설정 파일 → 기본 포트 → 충돌 시 대체 포트)
6. java -jar {artifact} --server.port={port} 으로 기동
7. Health check 폴링
```

### JAR/WAR 탐지 규칙

**Gradle:**
```bash
# JAR 프로젝트
ls build/libs/*.jar | grep -v plain

# WAR 프로젝트
ls build/libs/*.war | grep -v plain
```

**Maven:**
```bash
# JAR 프로젝트
ls target/*.jar | grep -v original

# WAR 프로젝트
ls target/*.war
```

산출물이 여러 개면 **파일 크기가 가장 큰 것**을 선택한다 (fat JAR/WAR).

## 포트 충돌 자동 해결

서버 기동 전 반드시 포트 사용 여부를 확인하고, 충돌 시 대체 포트로 기동한다.

### 포트 충돌 탐지

```bash
# Windows
netstat -ano | findstr :{port} | findstr LISTENING

# Linux/Mac
lsof -i :{port} -t
```

### 대체 포트 전략

```
1. 설정 파일의 포트 확인 → 사용 중이면 2번으로
2. 기본 포트(8080) 확인 → 사용 중이면 3번으로
3. 대체 포트 순서대로 시도:
   - Java: 28080 → 28081 → 28082 → 28083 → 28090
   - Node.js: 3001 → 3002 → 3003 → 4000
   - Python: 8001 → 8002 → 8003
4. 모든 대체 포트 사용 중이면 사용자에게 보고 후 중단
```

### 포트 변경 시 기동 방법

포트 충돌이 감지되면 **설정 파일을 수정하지 않고** 커맨드 라인 인자로 포트를 오버라이드한다:

| 프로젝트 타입 | 포트 오버라이드 방법 |
|-------------|-------------------|
| **Spring Boot** | `java -jar {artifact} --server.port={alt_port}` |
| **Node.js (Next.js)** | `npx next dev -p {alt_port}` |
| **Node.js (Vite)** | `npx vite --port {alt_port}` |
| **Node.js (Express)** | `PORT={alt_port} npm start` |
| **Django** | `python manage.py runserver 0.0.0.0:{alt_port}` |
| **Flask** | `flask run --port {alt_port}` |
| **FastAPI** | `uvicorn main:app --port {alt_port}` |
| **정적 HTML** | `python -m http.server {alt_port}` |

**중요:** 대체 포트로 기동한 경우, 이후 QA 시나리오의 테스트 URL도 해당 포트로 자동 조정한다.

## 포트 탐지 우선순위

1. 사용자가 URL에 포트를 명시한 경우 → 해당 포트 사용
2. 설정 파일에서 포트 읽기 (`application.yml`, `.env`, `package.json` 등)
3. 프로젝트 타입별 기본 포트
4. 위 포트가 사용 중이면 → 대체 포트 자동 선택

## Health Check 폴링 전략

```
최대 대기: 60초
폴링 간격: 3초
최대 시도: 20회

각 시도:
  curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}
  → 200~399 응답 시 성공
  → 그 외 3초 대기 후 재시도

모든 시도 실패 시:
  → 서버 기동 로그 확인 (TaskOutput)
  → 빌드 실패인지, 런타임 에러인지 분류
  → 에러 메시지와 함께 사용자에게 보고
  → 점검 중단
```

## Windows 환경 주의사항

- `./gradlew` → `gradlew.bat` 또는 `bash ./gradlew` 사용
- `./mvnw` → `mvnw.cmd` 또는 `bash ./mvnw` 사용
- `curl`은 Windows 10+ 기본 포함, Git Bash에서도 사용 가능
- 포트 확인: `netstat -ano | findstr :{port}` 사용
- JAR 기동 시 `java` 명령이 PATH에 있는지 확인 (`java -version`)
- `JAVA_HOME` 설정 확인 필요 시: `echo $JAVA_HOME` 또는 `echo %JAVA_HOME%`
