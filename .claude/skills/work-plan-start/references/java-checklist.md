# Java 프로젝트 CLAUDE.md 규칙 체크리스트

구현 중 아래 항목을 반드시 확인하며 진행한다.

## OOP / 클린코드

- [ ] 메서드 단일 책임 (한 가지 일만)
- [ ] 메서드 파라미터 3개 이하
- [ ] Early Return으로 중첩 축소
- [ ] null 대신 Optional (반환 타입으로만, `orElseThrow()` 권장)

## 코드 가독성

- [ ] 3개 이상 if-else → switch expression / Enum / Map 다형성
- [ ] `StringUtils.hasText()`, `CollectionUtils.isEmpty()` 활용
- [ ] 매직 넘버/문자열 → `static final` 상수 또는 Enum

## 성능

- [ ] `Pattern`, `ObjectMapper` 등 고비용 객체 → `static final` 캐싱
- [ ] 중첩 루프 → Map 활용 검색
- [ ] 반복문 내 String 덧셈 → StringBuilder
- [ ] 반복문 내 DB/API 호출 → Bulk 연산

## 데이터 객체

- [ ] Entity 직접 노출 금지 → RequestDTO / ResponseDTO 분리
- [ ] DTO 불변 설계 (`record` 권장)
- [ ] Entity ↔ DTO 변환은 Mapper 또는 정적 팩토리 메서드

## JPA

- [ ] `@ManyToOne`, `@OneToOne` → `FetchType.LAZY`
- [ ] 컬렉션 조회 → Fetch Join 또는 EntityGraph (N+1 방지)
- [ ] 조회 전용 → `@Transactional(readOnly = true)`

## Lombok

- [ ] `@Data` 사용 금지
- [ ] `@Getter` + `@NoArgsConstructor(access = PROTECTED)` + `@Builder` 조합

## 예외 / 로깅

- [ ] `System.out.println` 금지 → SLF4J
- [ ] 구체적 예외 처리 (최상위 `Exception` catch 금지)
- [ ] Custom Exception으로 비즈니스 예외 전달
- [ ] 예외 삼키기(swallow) 금지

## 최소 코드 원칙

- [ ] 요청된 기능에 필요한 최소한의 코드만 작성
- [ ] 사용처 없는 toString, equals, 유틸리티/헬퍼 미생성
- [ ] 기본 생성 메서드 불필요 시 미생성
