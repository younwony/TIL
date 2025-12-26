# 리팩토링 (Refactoring)

> `[3] 중급` · 선수 지식: [클린 코드](./clean-code.md), [OOP](./oop.md)

> 외부 동작을 유지하면서 내부 구조를 개선하는 코드 수정 기법

`#리팩토링` `#Refactoring` `#코드스멜` `#CodeSmell` `#클린코드` `#CleanCode` `#추출` `#Extract` `#인라인` `#Inline` `#이동` `#Move` `#이름변경` `#Rename` `#레거시코드` `#LegacyCode` `#기술부채` `#TechnicalDebt` `#SOLID` `#중복제거` `#DuplicateCode` `#긴메서드` `#LongMethod` `#IDE리팩토링` `#IntelliJ` `#MartinFowler`

## 왜 알아야 하는가?

리팩토링은 코드 품질을 지속적으로 개선하는 핵심 기술입니다. 기술 부채를 관리하고, 가독성과 유지보수성을 높입니다. TDD에서 Refactor 단계의 핵심이며, 시니어 개발자의 필수 역량입니다.

## 핵심 개념

- **리팩토링**: 기능 변경 없이 코드 구조 개선
- **코드 스멜**: 리팩토링이 필요한 징후
- **테스트 커버리지**: 리팩토링의 안전망
- **작은 단계**: 점진적으로 개선

## 쉽게 이해하기

**리팩토링**을 집 리모델링에 비유할 수 있습니다.

- 외부 기능(살기)은 유지하면서 내부 구조(배관, 배선)를 개선
- 한 번에 전체를 뜯지 않고 방 하나씩 작업
- 작업 전에 설계도(테스트)를 확인

**나쁜 예**: 살면서 전체 배관 교체 (기능 손상 위험)
**좋은 예**: 화장실 배관 교체 → 테스트 → 주방 배관 교체

## 상세 설명

### 코드 스멜 (Code Smell)

| 스멜 | 증상 | 해결 기법 |
|------|------|----------|
| 중복 코드 | 비슷한 코드 반복 | 메서드 추출, 상속/조합 |
| 긴 메서드 | 20줄 이상 | 메서드 추출 |
| 긴 파라미터 목록 | 4개 이상 | 객체로 묶기 |
| 전역 데이터 | 어디서든 수정 가능 | 캡슐화 |
| 기능 편애 | 다른 클래스 데이터만 사용 | 메서드 이동 |
| 데이터 뭉치 | 항상 함께 다니는 데이터 | 클래스 추출 |
| 기본형 집착 | 원시값으로 표현 | 객체로 래핑 |
| Switch문 | 여러 조건 분기 | 다형성으로 대체 |
| 임시 필드 | 특정 상황에서만 값 있음 | 클래스 추출 |
| 죽은 코드 | 사용되지 않는 코드 | 삭제 |

### 리팩토링 기법

#### 1. 메서드 추출 (Extract Method)

```java
// Before
void printOwing() {
    // 배너 출력
    System.out.println("*****************");
    System.out.println("*** 고객 청구서 ***");
    System.out.println("*****************");

    // 상세 내용 출력
    System.out.println("고객명: " + name);
    System.out.println("채무액: " + amount);
}

// After
void printOwing() {
    printBanner();
    printDetails();
}

void printBanner() {
    System.out.println("*****************");
    System.out.println("*** 고객 청구서 ***");
    System.out.println("*****************");
}

void printDetails() {
    System.out.println("고객명: " + name);
    System.out.println("채무액: " + amount);
}
```

#### 2. 변수 인라인 (Inline Variable)

```java
// Before
int basePrice = order.getBasePrice();
return basePrice > 1000;

// After
return order.getBasePrice() > 1000;
```

#### 3. 임시 변수를 쿼리로 교체 (Replace Temp with Query)

```java
// Before
double getPrice() {
    double basePrice = quantity * itemPrice;
    if (basePrice > 1000) {
        return basePrice * 0.95;
    }
    return basePrice * 0.98;
}

// After
double getPrice() {
    if (basePrice() > 1000) {
        return basePrice() * 0.95;
    }
    return basePrice() * 0.98;
}

double basePrice() {
    return quantity * itemPrice;
}
```

#### 4. 조건문 분해 (Decompose Conditional)

```java
// Before
if (date.before(SUMMER_START) || date.after(SUMMER_END)) {
    charge = quantity * winterRate + winterServiceCharge;
} else {
    charge = quantity * summerRate;
}

// After
if (isWinter(date)) {
    charge = winterCharge(quantity);
} else {
    charge = summerCharge(quantity);
}

boolean isWinter(Date date) {
    return date.before(SUMMER_START) || date.after(SUMMER_END);
}
```

#### 5. 다형성으로 조건문 대체 (Replace Conditional with Polymorphism)

```java
// Before
double getSpeed() {
    switch (type) {
        case EUROPEAN: return getBaseSpeed();
        case AFRICAN: return getBaseSpeed() - getLoadFactor();
        case NORWEGIAN_BLUE: return isNailed ? 0 : getBaseSpeed();
    }
}

// After
abstract class Bird {
    abstract double getSpeed();
}

class European extends Bird {
    double getSpeed() { return getBaseSpeed(); }
}

class African extends Bird {
    double getSpeed() { return getBaseSpeed() - getLoadFactor(); }
}

class NorwegianBlue extends Bird {
    double getSpeed() { return isNailed ? 0 : getBaseSpeed(); }
}
```

#### 6. 객체로 값 교체 (Replace Primitive with Object)

```java
// Before
String phoneNumber;

// After
class PhoneNumber {
    private String areaCode;
    private String number;

    String getFormatted() {
        return areaCode + "-" + number;
    }

    boolean isValid() {
        // 검증 로직
    }
}
```

### 리팩토링 워크플로우

```
┌─────────────────────────────────────────────────────────────┐
│                    리팩토링 사이클                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   1. 테스트 확인 (Green)                                     │
│        ↓                                                     │
│   2. 작은 변경 (한 가지 리팩토링)                             │
│        ↓                                                     │
│   3. 테스트 실행                                             │
│        ↓                                                     │
│   4. 실패? → 되돌리기                                        │
│      성공? → 커밋 → 1번으로                                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘

핵심: 한 번에 하나씩, 작은 단위로
```

### IDE 리팩토링 기능 (IntelliJ)

| 단축키 | 기능 |
|--------|------|
| Ctrl+Alt+M | 메서드 추출 |
| Ctrl+Alt+V | 변수 추출 |
| Ctrl+Alt+C | 상수 추출 |
| Ctrl+Alt+P | 파라미터 추출 |
| Shift+F6 | 이름 변경 |
| F6 | 이동 |
| Ctrl+Alt+N | 인라인 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 코드 가독성 향상 | 시간 투자 필요 |
| 유지보수 용이 | 테스트 필수 |
| 버그 발견 기회 | 학습 곡선 |
| 설계 개선 | 과도한 추상화 위험 |

## 면접 예상 질문

### Q: 리팩토링을 언제 해야 하나요?

A: (1) **코드 리뷰 후**: 피드백 반영 시 (2) **기능 추가 전**: 새 기능을 위한 기반 정리 (3) **버그 수정 시**: 원인 파악 중 발견한 문제 (4) **이해하기 어려울 때**: 읽으면서 정리. **규칙 of Three**: 비슷한 코드가 3번 나오면 리팩토링. **주의**: 기능 개발과 리팩토링은 분리해서 커밋.

### Q: 레거시 코드를 리팩토링할 때 주의점은?

A: (1) **테스트 먼저**: 테스트 없으면 Characterization 테스트 작성 (2) **작은 단계**: 한 번에 하나씩, 커밋 자주 (3) **안전한 변경부터**: 이름 변경, 메서드 추출 등 (4) **과감한 변경 자제**: 전체 재작성은 대부분 실패. **핵심**: 작동하는 코드를 깨뜨리지 않으면서 점진적으로 개선.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [클린 코드](./clean-code.md) | 목표 상태 | [3] 중급 |
| [TDD](./tdd.md) | 안전망 | [3] 중급 |
| [디자인 패턴](./design-pattern.md) | 리팩토링 방향 | [3] 중급 |

## 참고 자료

- Refactoring: Improving the Design of Existing Code - Martin Fowler
- Working Effectively with Legacy Code - Michael Feathers
