# 프로젝트 서버 탐지 & 기동 레퍼런스

Phase 0에서 프로젝트 타입을 자동 탐지하고 서버를 기동할 때 참조하는 레퍼런스.

## 프로젝트 타입별 탐지 및 기동

### Spring Boot (Gradle)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `build.gradle`, `build.gradle.kts` |
| **포트 설정** | `src/main/resources/application.yml` → `server.port` |
| | `src/main/resources/application.properties` → `server.port` |
| **기동 커맨드** | `./gradlew bootRun` (Linux/Mac) |
| | `gradlew.bat bootRun` (Windows) |
| **기본 포트** | 8080 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |
| **준비 완료 신호** | HTTP 200~399 응답 또는 로그에 "Started *Application" 출력 |

### Spring Boot (Maven)

| 항목 | 값 |
|------|------|
| **탐지 파일** | `pom.xml` (spring-boot-starter 의존성 포함) |
| **포트 설정** | `src/main/resources/application.yml` → `server.port` |
| | `src/main/resources/application.properties` → `server.port` |
| **기동 커맨드** | `./mvnw spring-boot:run` (Linux/Mac) |
| | `mvnw.cmd spring-boot:run` (Windows) |
| **기본 포트** | 8080 |
| **Health check** | `curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}` |

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

## 포트 탐지 우선순위

1. 사용자가 URL에 포트를 명시한 경우 → 해당 포트 사용
2. 설정 파일에서 포트 읽기 (`application.yml`, `.env`, `package.json` 등)
3. 프로젝트 타입별 기본 포트

## Health Check 폴링 전략

```
최대 대기: 30초
폴링 간격: 3초
최대 시도: 10회

각 시도:
  curl -s -o /dev/null -w "%{http_code}" http://localhost:{port}
  → 200~399 응답 시 성공
  → 그 외 3초 대기 후 재시도

모든 시도 실패 시:
  → 서버 기동 로그 확인 (TaskOutput)
  → 에러 메시지와 함께 사용자에게 보고
  → 점검 중단
```

## Windows 환경 주의사항

- `./gradlew` → `gradlew.bat` 또는 `bash ./gradlew` 사용
- `./mvnw` → `mvnw.cmd` 또는 `bash ./mvnw` 사용
- `curl`은 Windows 10+ 기본 포함, Git Bash에서도 사용 가능
- 포트 확인: `netstat -ano | findstr :{port}` 또는 `curl` 사용
