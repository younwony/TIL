# Playwright 환경 설정 가이드

Phase 1에서 Playwright 환경을 자동 설정할 때 참조하는 가이드.

## 자동 설치 절차

### 1. Playwright 설치 여부 확인

```bash
npx playwright --version 2>/dev/null
```

- 버전이 출력되면 → 설치 완료, 스킵
- 에러 발생 시 → 설치 진행

### 2. 설치 방법

프로젝트에 `package.json`이 있는 경우:
```bash
npm install -D @playwright/test
npx playwright install chromium
```

프로젝트에 `package.json`이 없는 경우:
```bash
# 임시 디렉토리에서 설치
mkdir -p .claude/playwright-tests
cd .claude/playwright-tests
npm init -y
npm install -D @playwright/test
npx playwright install chromium
cd ../..
```

### 3. 설치 검증

```bash
npx playwright --version
```

## 디렉토리 구조

```
.claude/
├── playwright-tests/          # 자동 생성된 Playwright 테스트 코드
│   ├── playwright.config.ts   # Playwright 설정 (인라인 생성)
│   ├── qa-s01.spec.ts         # 시나리오별 테스트 파일
│   ├── qa-s02.spec.ts
│   └── auth.setup.ts          # 로그인 설정 (필요 시)
├── playwright-results/        # 테스트 실행 결과
│   ├── results.json           # JSON reporter 출력
│   └── screenshots/           # 실패 시 스크린샷
└── ...
```

## Playwright Config (인라인 생성)

```typescript
// .claude/playwright-tests/playwright.config.ts
import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: '.',
  timeout: 30_000,
  expect: { timeout: 10_000 },
  retries: 0,
  workers: 1,  // 순차 실행 (서버 부하 방지)
  reporter: [
    ['json', { outputFile: '../playwright-results/results.json' }],
    ['list']  // 콘솔 출력도 유지
  ],
  use: {
    baseURL: 'http://localhost:{port}',
    headless: true,
    screenshot: 'only-on-failure',
    trace: 'off',
    actionTimeout: 10_000,
    navigationTimeout: 15_000,
  },
  projects: [
    {
      name: 'chromium',
      use: { browserName: 'chromium' },
    },
  ],
});
```

**주의사항:**
- `{port}`는 Phase 1에서 결정된 서버 포트로 치환
- `workers: 1`로 순차 실행 (서버 부하 방지 + 시나리오 간 의존성 처리)
- `retries: 0`으로 설정 (FAIL은 Layer 2로 에스컬레이션하므로 재시도 불필요)

## 기존 Playwright 설정 재사용

프로젝트에 이미 `playwright.config.ts`가 있으면:
1. 해당 config의 `baseURL`, `timeout` 등을 참조
2. `.claude/playwright-tests/playwright.config.ts`에서 해당 값을 반영
3. 기존 config는 수정하지 않음

## Windows 환경 주의사항

- `npx` 대신 `npx.cmd` 사용이 필요할 수 있음 → 먼저 `npx`로 시도, 실패 시 `npx.cmd`
- 경로 구분자: Playwright config 내에서는 `/` 사용 (Node.js가 자동 변환)
- `playwright install chromium` 실행 시 관리자 권한 불필요 (사용자 디렉토리에 설치)
- 방화벽 경고 발생 시 → 사용자에게 안내 후 진행

## 정리

QA 완료 후 `.claude/playwright-tests/`와 `.claude/playwright-results/`는:
- `.claude/` 하위이므로 **git add 대상에서 제외**
- 다음 QA 실행 시 덮어쓰기됨
- 수동 삭제 불필요
