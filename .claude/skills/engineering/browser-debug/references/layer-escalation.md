# Layer 에스컬레이션 로직 가이드

Phase 3 실행 후 FAIL 시나리오를 Layer 2(Chrome)로 에스컬레이션할 때 참조하는 가이드.

## 에스컬레이션 의사결정 트리

```
Playwright 테스트 결과 (results.json)
│
├─ 모든 시나리오 PASS
│  └─ → Phase 5(결과 보고)로 직행. Chrome 호출 없음.
│
├─ FAIL 시나리오 존재
│  ├─ 에러 유형 분석
│  │  ├─ Timeout → 페이지 로드/렌더링 문제 가능성
│  │  ├─ Assertion Failed → DOM 상태 불일치
│  │  ├─ Navigation Error → URL/라우팅 문제
│  │  └─ Script Error → JS 실행 에러
│  │
│  └─ → Phase 4(Layer 2 Chrome 디버깅)으로 진행
│
└─ SKIPPED 시나리오 (test.skip)
   └─ → 결과에 ⏭️ SKIP 기록. 에스컬레이션 불필요.
```

## 에스컬레이션 데이터 수집

### results.json에서 FAIL 시나리오 추출

```
1. results.json 파싱
2. specs[].ok === false 인 항목 필터링
3. 각 FAIL 항목에서 추출:
   - 시나리오 ID (title에서 "S{번호}" 추출)
   - 에러 메시지 (tests[].errors[].message)
   - 실패 스크린샷 경로 (있으면)
   - 소요 시간 (timeout 여부 판별)
```

### FAIL 데이터 정리 (Layer 2 전달용)

각 FAIL 시나리오별로 다음 정보를 정리한다:

```
시나리오: S03 - 상세 모달 열기 및 처리
에러: Timeout 30000ms exceeded waiting for selector '.modal.show'
에러 유형: Timeout
테스트 URL: /admin/brand-missings/page
스크린샷: .claude/playwright-results/screenshots/S03-failure.png
추정 원인: 모달 DOM이 생성되지 않거나, 클래스명이 다름
Chrome 집중 포인트: javascript_tool로 모달 DOM 존재 확인, read_console_messages로 JS 에러 확인
```

## 에러 유형별 Chrome 디버깅 전략

### 1. Timeout (가장 흔함)

**원인 추정**: 요소가 렌더링되지 않음, 셀렉터 불일치, 서버 응답 지연

**Chrome 디버깅 순서:**
1. `navigate` → 해당 페이지 이동
2. `read_network_requests` → API 응답 시간/상태 확인
3. `javascript_tool` → 해당 셀렉터 존재 여부 확인
   ```javascript
   document.querySelectorAll('.modal.show').length
   ```
4. `read_console_messages` → JS 에러 확인
5. `screenshot` → 현재 화면 상태 시각적 확인

### 2. Assertion Failed

**원인 추정**: DOM 상태가 기대와 다름, API 응답 데이터 불일치

**Chrome 디버깅 순서:**
1. `navigate` → 해당 페이지 이동
2. `javascript_tool` → assertion 대상 값 직접 확인
   ```javascript
   // 예: 그리드 행 수 확인
   document.querySelectorAll('.ag-row').length
   ```
3. `read_network_requests` → API 응답 데이터 확인
4. `screenshot` → 실제 렌더링 상태 확인

### 3. Navigation Error

**원인 추정**: URL 라우팅 설정 오류, 서버 404/500

**Chrome 디버깅 순서:**
1. `navigate` → 해당 URL 직접 이동
2. `read_network_requests` → HTTP 상태코드 확인
3. `read_console_messages` → 라우팅 관련 에러 확인
4. `javascript_tool` → 현재 URL 확인
   ```javascript
   window.location.href
   ```

### 4. Script Error

**원인 추정**: JS 런타임 에러, 변수 미정의, API 응답 구조 변경

**Chrome 디버깅 순서:**
1. `navigate` → 해당 페이지 이동
2. `read_console_messages` → **최우선** JS 에러 메시지 확인
3. `javascript_tool` → 에러 발생 지점 주변 변수 확인
4. `read_network_requests` → API 응답 구조 확인

## Layer 2 실행 후 재검증

### 수정 후 재검증 절차

```
1. 코드 수정 완료
2. 서버 재기동 필요 여부 판단:
   - JS/CSS 변경 → 재기동 불필요
   - Java/설정 변경 → 서버 재기동
3. Playwright 테스트 재실행 (해당 시나리오만):
   npx playwright test --grep "S{번호}" --reporter=json
4. 재검증 결과:
   - PASS → 🔧 FIX→PASS 기록
   - FAIL → 추가 디버깅 또는 사용자 보고
```

### Playwright로 재검증하는 이유

Chrome으로 수정 확인 후에도 **반드시 Playwright로 재검증**한다:
- Chrome에서 시각적으로 PASS처럼 보여도 자동화 검증에서 FAIL할 수 있음
- Playwright 테스트가 PASS해야 회귀 방지 보장
- 재검증은 단일 시나리오만 실행하므로 빠름 (수 초)

## 에스컬레이션 제한

### Chrome으로 에스컬레이션하지 않는 경우

1. **SKIP된 시나리오**: 선행 조건 미충족. 코드 문제가 아님.
2. **환경 문제**: Playwright 설치 실패, 브라우저 바이너리 없음 등 → 사용자 안내
3. **서버 다운**: Health check 실패 → 서버 재기동 시도 → 여전히 실패 시 사용자 안내

### 최대 에스컬레이션 수

- FAIL 시나리오가 **5개 이상**이면: 먼저 P0 FAIL만 Chrome으로 디버깅
- P0 수정 후 Playwright 전체 재실행 → 나머지 FAIL 재확인
- 연쇄 실패가 P0 문제에서 기인하는 경우가 많으므로 이 전략이 효율적

## QA-SCENARIOS.md 결과 표기

| Layer | 결과 | 표기 |
|-------|------|------|
| Layer 1 (Playwright) | PASS | ✅ PASS |
| Layer 1 (Playwright) | FAIL → Layer 2 에스컬레이션 | 🔄 Layer 2 진행 중 |
| Layer 2 (Chrome) | 수정 후 PASS | 🔧 FIX→PASS |
| Layer 2 (Chrome) | 여전히 FAIL | ❌ FAIL (원인: ...) |
| - | SKIP | ⏭️ SKIP (사유: ...) |
| - | BLOCKED | ⚠️ BLOCKED (사유: ...) |
