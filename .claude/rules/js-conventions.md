---
paths:
  - "**/*.js"
  - "**/*.ts"
  - "**/*.jsx"
  - "**/*.tsx"
  - "**/*.vue"
---

# JavaScript/TypeScript 코드 컨벤션

> JS/TS 파일 생성·수정 시 자동 적용된다.

## 기본 원칙

- 요청된 기능에 필요한 최소한의 코드만 작성한다
- 사용하지 않는 변수, 함수, import는 즉시 제거한다
- 전체적인 구현의 톤앤매너는 프로젝트 기존 코드와 동일하게 진행

## 스타일 & 포맷

- **세미콜론**: 프로젝트 설정(ESLint/Prettier)을 따른다. 설정이 없으면 세미콜론 사용
- **따옴표**: 프로젝트 설정을 따른다. 설정이 없으면 작은따옴표(`'`) 사용
- **들여쓰기**: 프로젝트 설정을 따른다. 설정이 없으면 2칸 스페이스
- **줄 길이**: 120자 이하 권장

## 변수 & 상수

- `var` 사용 금지 → `const` 우선, 재할당 필요 시 `let`
- 매직 넘버/문자열 금지 → 의미 있는 상수로 선언
- 변수명은 camelCase, 상수는 UPPER_SNAKE_CASE
- boolean 변수는 `is`, `has`, `should`, `can` 접두사 사용

## 함수

- 한 함수는 한 가지 일만 수행 (SRP)
- 함수 길이 **20줄 이하** 권장
- 파라미터 **3개 이하** 권장, 초과 시 객체로 묶기
- Early Return 패턴 적용 (`else` 최소화)
- 화살표 함수 우선 사용 (콜백, 간단한 함수)
- `this` 바인딩이 필요한 경우만 `function` 키워드 사용

## 비동기 처리

- `async/await` 우선 사용 (`.then()` 체인 지양)
- 에러 처리: `try/catch`로 명시적 처리, 에러 삼키기 금지
- 병렬 실행 가능한 비동기 작업은 `Promise.all()` / `Promise.allSettled()` 사용
- `await`를 반복문 안에서 순차 실행하지 않기 → 병렬화 또는 Bulk 처리

## 타입 (TypeScript)

- `any` 사용 금지 → `unknown` + 타입 가드, 또는 구체적 타입 정의
- 인터페이스/타입은 의미 있는 이름으로 정의 (I- 접두사 불필요)
- 유니온 타입보다 discriminated union 권장 (복잡한 분기 시)
- `as` 타입 단언 최소화 → 타입 가드 함수 사용
- API 응답, 요청 객체는 별도 타입/인터페이스로 정의

## DOM & 스타일

- 인라인 스타일(`element.style.*`) 금지 → CSS 클래스로 관리
- `innerHTML` 사용 시 XSS 주의 → 사용자 입력은 반드시 이스케이프
- `document.querySelector` 반환값은 null 체크 필수
- 이벤트 리스너는 적절히 해제 (메모리 누수 방지)

## 모듈 & Import

- ES Module (`import/export`) 사용 (CommonJS `require` 지양)
- 와일드카드 import (`import *`) 지양 → 명시적 import
- 사용하지 않는 import 즉시 제거
- import 순서: 외부 라이브러리 → 프로젝트 내부 모듈 → 상대 경로

## 에러 처리

- 에러 삼키기(swallow) 금지 — `catch`에서 최소한 로깅
- Custom Error 클래스 활용 (Error 상속)
- 사용자에게 노출되는 에러 메시지와 내부 로깅 분리
- API 호출 실패 시 적절한 fallback 또는 사용자 알림

## 금지 항목 (위반 시 즉시 수정)

- `var` → `const` / `let`
- `==` / `!=` → `===` / `!==` (엄격한 비교)
- `eval()` 사용 금지 (보안 위험)
- `console.log()` 프로덕션 코드에 남기지 않기 → 로깅 라이브러리 사용
- `innerHTML`에 사용자 입력 직접 삽입 금지 (XSS)
- 콜백 지옥 → `async/await` 또는 Promise 체인으로 리팩토링
- `arguments` 객체 → rest 파라미터(`...args`) 사용
- `new Array()` / `new Object()` → 리터럴(`[]` / `{}`) 사용

## 성능 주의 사항

- 대량 DOM 조작 → `DocumentFragment` 또는 가상 DOM 활용
- 이벤트 핸들러 남발 → 이벤트 위임(Event Delegation) 패턴
- 깊은 복사(`structuredClone`, `JSON.parse(JSON.stringify())`) 남용 주의
- 메모이제이션: 비용 높은 계산은 캐싱 고려 (`useMemo`, `useCallback` 등)
- 번들 크기: 트리 셰이킹 가능한 import 사용, 불필요한 라이브러리 의존 최소화
