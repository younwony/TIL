# DTO-Entity 변환 설계

> `[4] 심화` · 선수 지식: Clean Architecture, SOLID 원칙

> DTO와 Entity 간 변환은 DTO가 Entity를 알고, Entity는 DTO를 모르는 단방향 의존성을 유지해야 한다.

## 왜 알아야 하는가?

- **실무**: 대부분의 프로젝트가 잘못된 의존성 방향으로 설계됨. 리팩토링 시 큰 비용 발생
- **면접**: "Clean Architecture", "DIP 적용 사례" 질문의 실전 예시
- **기반 지식**: 도메인 주도 설계(DDD), 헥사고날 아키텍처의 핵심 원칙

## 핵심 개념

- **의존성 방향**: DTO → Entity (단방향), Entity는 DTO를 알면 안 됨
- **DIP 준수**: 안정적인 도메인 계층이 불안정한 응용 계층에 의존하면 안 됨
- **도메인 순수성**: Entity는 비즈니스 로직만 포함, 외부 계층(DTO, Controller)을 모름
- **변환 책임**: DTO가 `toEntity()`, `from(entity)` 메서드로 변환 담당
- **테스트 용이성**: Entity를 독립적으로 테스트 가능

## 쉽게 이해하기

**DTO-Entity 변환**을 건물 구조에 비유할 수 있습니다.

Entity는 건물의 **기초 공사**이고, DTO는 **인테리어**입니다.
기초가 인테리어에 의존하면 인테리어를 바꿀 때마다 기초 공사를 다시 해야 합니다.
반대로 인테리어가 기초 위에 올라가는 구조라면, 인테리어는 자유롭게 변경할 수 있습니다.

예를 들어, 집을 리모델링할 때:
- 벽지, 가구(DTO)를 바꿔도 → 기초, 기둥(Entity)은 그대로
- 기초를 바꾸면 → 집 전체를 다시 지어야 함 (대공사)

**왜 이 방향이어야 하나요?**
- API 응답 형식(DTO)은 자주 바뀜 (클라이언트 요구, 버전 업)
- 핵심 비즈니스 로직(Entity)은 안정적이어야 함
- 불안정한 것(DTO)이 안정적인 것(Entity)을 알아야 변경 영향 최소화

---

## 상세 설명

### 문제가 되는 패턴

**비권장 (X)**: Entity가 DTO를 알고 있음 (의존성 역방향)

```java
// Entity가 DTO를 알고 있음 (잘못된 패턴)
public class ApifyApi extends BaseEntity {

    public static ApifyApi create(ApifyApiDto dto) {  // DTO에 의존
        return ApifyApi.builder()
                .type(dto.getType())
                .operation(dto.getOperation())
                .build();
    }
}
```

```
+------------------+          +------------------+
|   ApifyApiDto    |  <----   |    ApifyApi      |
|   (응용 계층)     |          |   (도메인 계층)   |
+------------------+          +------------------+
        ^                              |
        |______________________________|
              Entity가 DTO에 의존 (역방향) ❌
```

### 왜 문제인가?

#### 1. DIP(의존성 역전 원칙) 위반

```
[외부/변경 잦음]  Controller → DTO → Service → Entity → Repository  [내부/안정적]
                 ─────────────────────────────────────────────────>
                              의존성은 안쪽(안정적인 방향)으로 흘러야 함
```

**왜?**
- Entity: 시스템의 **핵심 도메인 모델**로 가장 안정적이어야 함
- DTO: API 스펙, 화면 요구사항에 따라 **자주 변경**됨
- Entity가 DTO에 의존 → **불안정한 것에 안정적인 것이 의존** → 기초가 흔들림

**만약 지키지 않으면?**
- API 스펙 변경할 때마다 Entity도 수정해야 함
- 도메인 로직 테스트에 DTO 의존성이 끼어듦
- 다른 프로젝트에서 Entity만 재사용 불가

---

#### 2. 컴파일 의존성 문제

```java
// DTO에 새 필드 추가 (API 응답용)
public class ApifyApiDto {
    private String newFieldForApi;  // 추가
}

// Entity는 변경하지 않았는데...
public class ApifyApi {
    public static ApifyApi create(ApifyApiDto dto) {  // ApifyApiDto 참조
        // ...
    }
}
// → ApifyApi.java도 재컴파일됨 (변경하지 않았는데!)
```

**왜 문제인가?**
- 빌드 시간 증가 (연쇄적 재컴파일)
- CI/CD 비효율 (변경하지 않은 모듈까지 재빌드/재배포)
- 바이너리 호환성 깨짐 (라이브러리로 배포 시)

---

#### 3. 도메인 순수성 침해

```java
public class ApifyApi extends BaseEntity {

    // 도메인 로직 (순수) ✅
    public boolean isCompleted() {
        return this.status == ApifyApiStatus.COMPLETED;
    }

    // 외부 계층 침투 (오염) ❌
    public static ApifyApi create(ApifyApiDto dto) { ... }
    public static ApifyApi fromRequest(ApifyApiRequest request) { ... }
    public static ApifyApi fromExcel(ExcelRow row) { ... }
    // → DTO, Request, Excel 등 외부 표현을 Entity가 알게 됨
}
```

**왜 문제인가?**
- **SRP 위반**: Entity가 변환 로직까지 담당 (책임 2개)
- **변경 사유 증가**: API 변경, Excel 포맷 변경 시 Entity 수정 필요
- **재사용성 저하**: Entity가 DTO에 의존하므로 단독 사용 불가

---

#### 4. 테스트 복잡성 증가

```java
// 문제 있는 패턴: Entity 테스트에 DTO가 필요
@Test
void testCreateEntity() {
    // DTO를 먼저 만들어야 Entity를 테스트할 수 있음 ❌
    ApifyApiDto dto = ApifyApiDto.builder()
            .type(ApifyApiType.TIKTOK_SHOP_CREATOR)
            .operation(ApifyApiOperation.DEFAULT)
            .build();

    ApifyApi entity = ApifyApi.create(dto);
    assertThat(entity.getType()).isEqualTo(ApifyApiType.TIKTOK_SHOP_CREATOR);
}
```

**왜 문제인가?**
- 테스트 격리 실패 (Entity 테스트에 DTO 의존)
- DTO가 복잡하면 Mock 필요
- 테스트 실패 시 원인 불명확 (DTO 문제? Entity 문제?)

---

#### 5. 순환 의존성 위험

```java
// Entity가 DTO를 알고
public class ApifyApi {
    public static ApifyApi create(ApifyApiDto dto) { ... }
}

// DTO가 Entity를 알면
public class ApifyApiDto {
    public static ApifyApiDto from(ApifyApi entity) { ... }
}

// → 순환 의존 발생! ApifyApi ←→ ApifyApiDto
```

**왜 문제인가?**
- 컴파일 순서 문제 (어느 것을 먼저?)
- 모듈 분리 불가 (두 클래스가 항상 함께)
- 변경 영향이 양방향으로 전파

---

### 권장 설계

**권장 (O)**: DTO가 Entity를 알고 변환 담당

```
+------------------+          +------------------+
|   ApifyApiDto    |  ---->   |    ApifyApi      |
|   (응용 계층)     |          |   (도메인 계층)   |
+------------------+          +------------------+
        |                              ^
        |______________________________|
              DTO가 Entity에 의존 (정방향) ✅
```

```java
// Entity는 DTO를 모름 (순수한 도메인)
public class ApifyApi extends BaseEntity {
    // DTO 관련 코드 없음
    // 비즈니스 도메인 로직만 포함

    public boolean isCompleted() {
        return this.status == ApifyApiStatus.COMPLETED;
    }
}

// DTO가 Entity를 알고 변환 담당
public class ApifyApiDto {

    // Entity -> DTO (조회 시)
    public static ApifyApiDto from(ApifyApi entity) {
        return ApifyApiDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .build();
    }

    // DTO -> Entity (저장 시)
    public ApifyApi toEntity() {
        return ApifyApi.builder()
                .type(this.type)
                .operation(this.operation)
                .build();
    }
}
```

**왜 이렇게 하는가?**
1. **단방향 의존성**: DTO → Entity (순환 의존 차단)
2. **도메인 순수성**: Entity는 외부 계층을 모름
3. **컴파일 최적화**: DTO 변경이 Entity에 영향 없음
4. **테스트 용이**: Entity를 독립적으로 테스트 가능

---

## 동작 원리

### 계층 구조와 의존성 방향

```
+------------------+
|   Controller     |  ← 요청/응답 처리
+------------------+
         |
         v
+------------------+
|   DTO (Request/  |  ← 데이터 전송 객체
|   Response)      |    Entity를 알고 변환 담당
+------------------+
         |
         v
+------------------+
|   Service        |  ← 비즈니스 로직
+------------------+
         |
         v
+------------------+
|   Entity         |  ← 도메인 모델
|   (Domain)       |    외부 계층을 모름 (순수)
+------------------+
         |
         v
+------------------+
|   Repository     |  ← 영속성 처리
+------------------+
```

### Clean Architecture 관점

```
+------------------------------------------+
|              Frameworks & Drivers         |  ← 가장 바깥 (변경 잦음)
|  +------------------------------------+  |
|  |           Interface Adapters       |  |  ← DTO, Controller
|  |  +------------------------------+  |  |
|  |  |      Application Business    |  |  |  ← Service, Use Case
|  |  |  +------------------------+  |  |  |
|  |  |  |   Enterprise Business  |  |  |  |  ← Entity (가장 안쪽, 안정적)
|  |  |  +------------------------+  |  |  |
|  |  +------------------------------+  |  |
|  +------------------------------------+  |
+------------------------------------------+

의존성 방향: 바깥 → 안쪽 (단방향)
Entity는 바깥 계층을 절대 알면 안 됨!
```

---

## 예제 코드

### 개선된 테스트 코드

```java
// 좋은 패턴: Entity만으로 테스트 가능
@Test
void testCreateEntity() {
    // Entity를 직접 생성하여 테스트 ✅
    ApifyApi entity = ApifyApi.builder()
            .type(ApifyApiType.TIKTOK_SHOP_CREATOR)
            .operation(ApifyApiOperation.DEFAULT)
            .status(ApifyApiStatus.WAIT)
            .build();

    assertThat(entity.getType()).isEqualTo(ApifyApiType.TIKTOK_SHOP_CREATOR);
    assertThat(entity.isCompleted()).isFalse();
}

// DTO 변환 테스트 (별도)
@Test
void testDtoConversion() {
    ApifyApi entity = createTestEntity();

    ApifyApiDto dto = ApifyApiDto.from(entity);

    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getType()).isEqualTo(entity.getType());
}
```

### 계층별 알 수 있는 것

| 계층 | 알 수 있는 것 | 알면 안 되는 것 |
|------|--------------|----------------|
| DTO | Entity, Enum | - |
| Entity | Enum, 다른 Entity | DTO, Controller, Request |
| Service | Entity, DTO, Repository | Controller |
| Controller | DTO, Service | Entity 직접 노출 |

---

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 도메인 순수성 유지 | DTO 클래스에 변환 로직 집중 |
| 컴파일 의존성 감소 | 변환 메서드 중복 가능성 |
| 테스트 격리 용이 | DTO 수가 많으면 관리 복잡 |
| 모듈 분리 가능 | 초기 설계 시간 필요 |

**대안: Mapper 클래스 분리**

변환 로직이 복잡하면 별도의 Mapper 클래스로 분리:

```java
@Component
public class ApifyApiMapper {

    public ApifyApiDto toDto(ApifyApi entity) {
        // 복잡한 변환 로직
    }

    public ApifyApi toEntity(ApifyApiDto dto) {
        // 복잡한 변환 로직
    }
}
```

**MapStruct 사용 시:**
```java
@Mapper(componentModel = "spring")
public interface ApifyApiMapper {
    ApifyApiDto toDto(ApifyApi entity);
    ApifyApi toEntity(ApifyApiDto dto);
}
```

---

## 면접 예상 질문

### Q: Entity에서 DTO로 변환하는 메서드를 만들면 안 되는 이유는?

**A:** DIP(의존성 역전 원칙) 위반입니다.

**왜 문제인가?**
- Entity는 도메인 계층으로 가장 **안정적**이어야 함
- DTO는 응용 계층으로 API 스펙 변경에 따라 **자주 바뀜**
- Entity가 DTO에 의존하면 불안정한 것에 안정적인 것이 의존

**만약 지키지 않으면?**
- DTO 변경 시 Entity까지 재컴파일
- Entity 단위 테스트에 DTO Mock 필요
- 다른 프로젝트에서 Entity만 재사용 불가

---

### Q: DTO와 Entity 변환은 어디서 해야 하나요?

**A:** 3가지 방법이 있고, 상황에 따라 선택합니다.

| 방법 | 장점 | 단점 | 적합한 경우 |
|------|------|------|-------------|
| DTO 내부 | 단순, 명확 | DTO가 비대해질 수 있음 | 변환 로직 단순할 때 |
| Mapper 클래스 | 책임 분리, 테스트 용이 | 클래스 증가 | 변환 로직 복잡할 때 |
| MapStruct | 자동 생성, 성능 좋음 | 학습 필요 | 대규모 프로젝트 |

**핵심은 의존성 방향**: 어디서 하든 DTO→Entity 방향을 유지해야 합니다.

---

### Q: Clean Architecture에서 의존성 방향이 중요한 이유는?

**A:** 변경의 파급 효과를 최소화하기 위해서입니다.

**왜 바깥→안쪽인가?**
- 바깥 계층(UI, API)은 요구사항 변경으로 **자주 바뀜**
- 안쪽 계층(도메인)은 핵심 비즈니스로 **안정적**
- 안쪽이 바깥을 모르면 바깥이 바뀌어도 안쪽은 영향 없음

**비유:**
- 스마트폰 케이스(바깥)는 자주 바꿔도 됨
- 스마트폰 본체(안쪽)는 케이스에 상관없이 동작
- 본체가 특정 케이스에 의존하면? 케이스 바꿀 때 본체도 바꿔야 함

---

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [대규모 시스템 설계](./large-scale-system.md) | 유지보수성을 고려한 설계 | 심화 |

## 참고 자료

- Clean Architecture - Robert C. Martin
- SOLID 원칙 (DIP: 의존성 역전 원칙)
- Hexagonal Architecture (Ports and Adapters)
- 만들면서 배우는 클린 아키텍처 - Tom Hombergs
