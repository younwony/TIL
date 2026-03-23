# Playwright 테스트 코드 생성 템플릿

Phase 2에서 QA-SCENARIOS.md를 파싱하여 Playwright 테스트 코드를 생성할 때 참조하는 템플릿.

## QA-SCENARIOS.md 파싱 규칙

각 시나리오에서 다음 정보를 추출한다:

| 필드 | 추출 위치 | 예시 |
|------|----------|------|
| 시나리오 ID | `#### S{번호}.` 제목 | S01 |
| 제목 | `####` 다음 텍스트 | 목록 페이지 로드 및 그리드 표시 |
| 우선순위 | `**우선순위**:` | P0 |
| 테스트 URL | `**테스트 URL**:` | /admin/items/page |
| Given | `**Given**:` | 관리자가 로그인한 상태에서 |
| When | `**When**:` | 목록 페이지에 접속하면 |
| Then | `**Then**:` | 그리드에 데이터가 표시되고... |
| 검증 방법 | `**검증 방법**:` | network, javascript, console |
| 검증 코드 | ` ```javascript` 블록 | DOM 검증 코드 |

## 기본 테스트 파일 구조

```typescript
// .claude/playwright-tests/qa-scenarios.spec.ts
import { test, expect, Page } from '@playwright/test';

// 콘솔 에러 수집용
const consoleErrors: string[] = [];

test.describe('QA Scenarios - {branch_name}', () => {

  test.beforeEach(async ({ page }) => {
    // 콘솔 에러 수집 리셋
    consoleErrors.length = 0;
    page.on('console', msg => {
      if (msg.type() === 'error') {
        consoleErrors.push(msg.text());
      }
    });
  });

  // --- P0 시나리오 ---

  test('S01 - {시나리오 제목}', async ({ page }) => {
    // Given: {Given 내용}
    // When: {When 내용}
    await page.goto('{테스트 URL}');

    // Then: {Then 내용}
    // ... 검증 코드
  });

  // --- P1 시나리오 ---
  // ... 반복
});
```

## 검증 방법별 Playwright 코드 패턴

### 1. network 검증

API 호출과 응답을 검증한다.

```typescript
// 패턴: GET /api/items → 200 응답
test('S01 - API 호출 검증', async ({ page }) => {
  const responsePromise = page.waitForResponse(
    resp => resp.url().includes('/api/items') && resp.request().method() === 'GET'
  );
  await page.goto('/admin/items/page');
  const response = await responsePromise;
  expect(response.status()).toBe(200);

  // 응답 body 검증 (필요 시)
  const body = await response.json();
  expect(body).toBeTruthy();
});

// 패턴: POST /api/items → 201 응답 (폼 submit)
test('S03 - 저장 API 검증', async ({ page }) => {
  await page.goto('/admin/items/new');
  // ... 폼 입력 ...

  const responsePromise = page.waitForResponse(
    resp => resp.url().includes('/api/items') && resp.request().method() === 'POST'
  );
  await page.click('button[type="submit"]');
  const response = await responsePromise;
  expect(response.status()).toBeLessThan(400);
});
```

### 2. javascript / DOM 검증

DOM 상태와 요소 존재를 검증한다.

```typescript
// 패턴: 그리드 데이터 존재 확인
test('S01 - 그리드 데이터 표시', async ({ page }) => {
  await page.goto('/admin/items/page');
  await page.waitForSelector('.ag-row', { timeout: 10_000 });

  const rowCount = await page.locator('.ag-row').count();
  expect(rowCount).toBeGreaterThan(0);
});

// 패턴: 특정 텍스트 포함 확인
test('S02 - 제목 표시', async ({ page }) => {
  await page.goto('/admin/items/page');
  await expect(page.locator('h1')).toContainText('목록');
});

// 패턴: 모달 열기 확인
test('S03 - 모달 열기', async ({ page }) => {
  await page.goto('/admin/items/page');
  await page.click('.ag-row:first-child');
  await expect(page.locator('.modal.show')).toBeVisible({ timeout: 5_000 });
});

// 패턴: 검증 코드 블록 재사용 (page.evaluate)
test('S01 - 검증 코드 실행', async ({ page }) => {
  await page.goto('/admin/items/page');
  await page.waitForSelector('.ag-row');

  const result = await page.evaluate(() => {
    const rows = document.querySelectorAll('.ag-row');
    return rows.length > 0;
  });
  expect(result).toBe(true);
});
```

### 3. console 검증

JS 에러가 없는지 확인한다.

```typescript
// 패턴: JS 에러 없음 확인
test('S01 - JS 에러 없음', async ({ page }) => {
  const errors: string[] = [];
  page.on('console', msg => {
    if (msg.type() === 'error') errors.push(msg.text());
  });

  await page.goto('/admin/items/page');
  await page.waitForLoadState('networkidle');

  // 에러 필터링 (무시할 에러 패턴)
  const criticalErrors = errors.filter(e =>
    !e.includes('favicon.ico') &&
    !e.includes('DevTools') &&
    !e.includes('net::ERR_')  // 외부 리소스 에러 무시
  );

  expect(criticalErrors).toHaveLength(0);
});
```

### 4. 복합 검증 (여러 방법 조합)

```typescript
test('S01 - 목록 페이지 로드 및 그리드 표시', async ({ page }) => {
  const errors: string[] = [];
  page.on('console', msg => {
    if (msg.type() === 'error') errors.push(msg.text());
  });

  // network: API 호출 대기
  const apiPromise = page.waitForResponse(
    r => r.url().includes('/api/items') && r.request().method() === 'GET'
  );

  // Given + When: 페이지 접속
  await page.goto('/admin/items/page');
  const apiResp = await apiPromise;

  // Then: network 검증
  expect(apiResp.status()).toBe(200);

  // Then: DOM 검증
  await page.waitForSelector('.ag-row');
  const rowCount = await page.locator('.ag-row').count();
  expect(rowCount).toBeGreaterThan(0);

  // Then: console 검증
  const criticalErrors = errors.filter(e => !e.includes('favicon'));
  expect(criticalErrors).toHaveLength(0);
});
```

## 사용자 액션 변환 패턴

| QA 시나리오 액션 | Playwright 코드 |
|----------------|----------------|
| 페이지 접속 | `await page.goto(url)` |
| 버튼 클릭 | `await page.click('selector')` |
| 텍스트 입력 | `await page.fill('input[name="x"]', 'value')` |
| 셀렉트박스 선택 | `await page.selectOption('select', 'value')` |
| 체크박스 체크 | `await page.check('input[type="checkbox"]')` |
| 행 클릭 | `await page.click('.ag-row:first-child')` |
| 모달 닫기 | `await page.click('.modal .btn-close')` |
| 스크롤 | `await page.evaluate(() => window.scrollTo(0, 999))` |
| 페이지 대기 | `await page.waitForLoadState('networkidle')` |
| 요소 대기 | `await page.waitForSelector('.selector', { timeout: 10_000 })` |

## 로그인 처리

### Form Submit 방식 (가장 일반적)

```typescript
// auth.setup.ts 또는 테스트 beforeAll
test.beforeAll(async ({ browser }) => {
  const page = await browser.newPage();
  await page.goto('/login');
  await page.fill('input[name="username"]', '{username}');
  await page.fill('input[name="password"]', '{password}');
  await page.click('button[type="submit"]');
  await page.waitForURL('**/dashboard**');

  // 세션 저장
  await page.context().storageState({ path: '.claude/playwright-tests/auth.json' });
  await page.close();
});

// 각 테스트에서 세션 재사용
test.use({ storageState: '.claude/playwright-tests/auth.json' });
```

### 쿠키 직접 설정 방식

```typescript
test.beforeEach(async ({ context }) => {
  await context.addCookies([{
    name: 'JSESSIONID',
    value: '{session_value}',
    domain: 'localhost',
    path: '/',
  }]);
});
```

## 결과 JSON 포맷

`npx playwright test --reporter=json` 실행 시 출력되는 JSON 구조:

```json
{
  "suites": [{
    "title": "QA Scenarios - feature/xxx",
    "specs": [{
      "title": "S01 - 목록 페이지 로드",
      "ok": true,
      "tests": [{
        "status": "passed",
        "duration": 1234
      }]
    }, {
      "title": "S03 - 처리 버튼 클릭",
      "ok": false,
      "tests": [{
        "status": "failed",
        "duration": 30000,
        "errors": [{
          "message": "Timeout waiting for selector '.modal.show'"
        }]
      }]
    }]
  }],
  "stats": {
    "expected": 8,
    "unexpected": 1,
    "duration": 45000
  }
}
```

## 파싱 시 주의사항

- 시나리오 ID가 없는 경우 → 순서대로 S01, S02... 자동 부여
- 테스트 URL이 상대 경로면 → `baseURL`과 결합
- 검증 코드가 `console.assert` 사용 시 → `expect`로 변환
- `document.querySelector` → `page.locator`로 변환
- BLOCKED 시나리오 → `test.skip`으로 처리
