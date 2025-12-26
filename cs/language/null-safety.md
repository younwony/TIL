# Null Safety (널 안정성)

> `[3] 중급` · 선수 지식: [프로그래밍 언어란](./what-is-language.md), [OOP](../programming/oop.md)

> 널 참조로 인한 오류를 컴파일 타임에 방지하는 타입 시스템 기능

`#NullSafety` `#널안정성` `#Nullable` `#NonNull` `#NullPointerException` `#NPE` `#Optional` `#Elvis연산자` `#SafeCall` `#안전호출` `#Kotlin` `#NullableType` `#NonNullType` `#let` `#also` `#run` `#apply` `#스마트캐스트` `#SmartCast` `#PlaftformType`

## 왜 알아야 하는가?

NPE(NullPointerException)는 가장 흔한 런타임 오류입니다. Kotlin의 Null Safety는 이를 컴파일 타임에 방지합니다. 안전하고 간결한 코드 작성의 핵심이며, Java-Kotlin 혼용 프로젝트에서 필수 지식입니다.

## 핵심 개념

- **Nullable 타입 (?)**: null 가능한 타입
- **Non-null 타입**: null 불가능한 타입
- **Safe Call (?.)**: null이면 호출 스킵
- **Elvis 연산자 (?:)**: null일 때 대체값

## 쉽게 이해하기

**Null Safety**를 택배 배송에 비유할 수 있습니다.

```kotlin
// Java: 집에 아무도 없을 수도 있음 (NPE 위험)
recipient.getAddress().getCity()  // 집 주소가 null이면 터짐!

// Kotlin: 부재중이면 배송 안 함 (안전)
recipient?.address?.city  // 중간에 null이면 전체가 null
```

## 상세 설명

### Nullable vs Non-null

```kotlin
// Non-null: null 불가
var name: String = "John"
name = null  // 컴파일 에러!

// Nullable: null 가능
var name: String? = "John"
name = null  // OK

// 타입 시스템이 구분
String  → 절대 null 아님
String? → null일 수 있음
```

### Safe Call (?.)

```kotlin
val city: String? = user?.address?.city

// 동등한 Java 코드
String city = null;
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        city = address.getCity();
    }
}
```

### Elvis 연산자 (?:)

```kotlin
// null이면 대체값 사용
val name = user?.name ?: "Anonymous"

// 조기 반환
fun process(user: User?) {
    val name = user?.name ?: return  // null이면 함수 종료
    println(name)
}

// 예외 던지기
val name = user?.name ?: throw IllegalArgumentException("User required")
```

### Non-null 단언 (!!)

```kotlin
// null 아님을 단언 (주의: NPE 발생 가능!)
val name: String = user!!.name

// 피해야 할 패턴
val name = user!!.name  // 위험

// 권장 패턴
val name = user?.name ?: throw IllegalStateException("User required")
```

### let을 이용한 null 처리

```kotlin
// null이 아닐 때만 실행
user?.let { user ->
    println(user.name)
    sendEmail(user.email)
}

// 더 간결하게
user?.let {
    println(it.name)  // it으로 참조
}

// 조건부 변환
val upperName = name?.let { it.uppercase() } ?: "DEFAULT"
```

### 스코프 함수 비교

| 함수 | 객체 참조 | 반환값 | 용도 |
|------|---------|-------|------|
| let | it | 람다 결과 | null 처리, 변환 |
| run | this | 람다 결과 | 객체 설정 후 계산 |
| with | this | 람다 결과 | 객체의 여러 메서드 호출 |
| apply | this | 객체 자신 | 객체 초기화 |
| also | it | 객체 자신 | 부수 효과 (로깅) |

```kotlin
// let: null 체크 + 변환
user?.let { sendEmail(it.email) }

// apply: 객체 초기화
val user = User().apply {
    name = "John"
    age = 30
}

// also: 부수 효과
user.also { println("Created: $it") }
```

### 스마트 캐스트 (Smart Cast)

```kotlin
fun process(name: String?) {
    if (name != null) {
        // 이 블록에서 name은 자동으로 String (non-null)
        println(name.length)  // 안전하게 접근
    }
}

// when 표현식에서도 동작
when (value) {
    is String -> println(value.length)  // String으로 스마트 캐스트
    is Int -> println(value + 1)        // Int로 스마트 캐스트
}
```

### Java와의 상호운용

```kotlin
// Java 코드 (nullable 정보 없음)
public class JavaUser {
    public String getName() { return null; }  // null 반환 가능
}

// Kotlin에서 호출
val user = JavaUser()
val name = user.name  // Platform Type: String!

// 안전하게 처리
val safeName: String = user.name ?: "Unknown"

// 주의: Platform Type은 NPE 위험
val length = user.name.length  // 컴파일 OK, 런타임 NPE 가능
```

### Java의 Optional vs Kotlin Null Safety

```java
// Java Optional
Optional<String> name = Optional.ofNullable(getName());
String result = name.orElse("default");
name.ifPresent(System.out::println);
```

```kotlin
// Kotlin Null Safety
val name: String? = getName()
val result = name ?: "default"
name?.let { println(it) }
```

| 항목 | Java Optional | Kotlin Null Safety |
|------|--------------|-------------------|
| 래핑 | 필요 (Optional 객체) | 불필요 (타입 자체) |
| 성능 | 약간의 오버헤드 | 없음 |
| 컬렉션 | 권장 안 함 | 자연스럽게 사용 |
| 필드 | 권장 안 함 | 사용 가능 |

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 컴파일 타임 NPE 방지 | 학습 곡선 |
| 명시적인 null 처리 | Java 상호운용 주의 |
| 간결한 문법 | !! 남용 위험 |

## 면접 예상 질문

### Q: Kotlin의 Null Safety가 Java보다 좋은 이유는?

A: (1) **컴파일 타임 검사**: NPE를 런타임 전에 발견 (2) **타입 시스템 통합**: Optional 래핑 없이 `?` 하나로 표현 (3) **간결한 문법**: `?.`, `?:`, `let` 등으로 간결한 null 처리 (4) **스마트 캐스트**: null 체크 후 자동 타입 변환. **Java Optional 단점**: 래핑 오버헤드, 필드/컬렉션에 권장 안 함.

### Q: !!와 ?:의 차이는?

A: **!!** (Non-null 단언): null이면 NPE 발생, null이 아님을 확신할 때만 사용. **?:** (Elvis 연산자): null이면 대체값 반환, 안전한 폴백 제공. **권장**: `!!` 대신 `?: throw`, `?: return` 사용으로 명시적 처리.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [프로그래밍 언어란](./what-is-language.md) | 선수 지식 | [1] 정의 |
| [Coroutine](./coroutine.md) | Kotlin 고급 | [4] 심화 |

## 참고 자료

- [Kotlin Null Safety - Official](https://kotlinlang.org/docs/null-safety.html)
- Kotlin in Action - Dmitry Jemerov
