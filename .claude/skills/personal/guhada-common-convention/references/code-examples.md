# guhada/kglowing 코드 예시 레퍼런스

이 문서는 guhada-common-convention 스킬의 코드 예시를 모아놓은 레퍼런스입니다.

---

## 1. Entity 설계

### 도메인 Entity

```java
@Entity
@Table(name = "campaign")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Builder
    private Campaign(String title, CampaignStatus status) {
        this.title = title;
        this.status = status;
    }

    // 비즈니스 로직
    public void changeStatus(CampaignStatus newStatus) {
        this.status = newStatus;
    }

    // 정적 팩토리 메서드
    public static Campaign create(String title) {
        return Campaign.builder()
                .title(title)
                .status(CampaignStatus.DRAFT)
                .build();
    }
}
```

### BaseEntity

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

## 2. DTO 설계

### Request DTO

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CampaignCreateRequest {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotNull
    private SeedingType seedingType;

    @Builder
    private CampaignCreateRequest(String title, SeedingType seedingType) {
        this.title = title;
        this.seedingType = seedingType;
    }
}
```

### Response DTO

```java
@Getter
@Builder
public class CampaignResponse {
    private final Long id;
    private final String title;
    private final String status;
    private final LocalDateTime createdAt;
}
```

---

## 3. MapStruct Mapper

```java
@Mapper(componentModel = "spring")
public interface CampaignDtoMapper {

    CampaignResponse toResponse(Campaign entity);

    List<CampaignResponse> toResponseList(List<Campaign> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "DRAFT")
    Campaign toEntity(CampaignCreateRequest request);
}
```

---

## 4. 응답 구조

### 성공 응답

```json
{
    "result": "SUCCESS",
    "resultCode": 200,
    "message": "success",
    "data": { ... }
}
```

### 에러 응답

```json
{
    "result": "BAD_REQUEST",
    "resultCode": 400,
    "message": "유효하지 않은 요청입니다."
}
```

### Controller 응답 생성

```java
@GetMapping("/{id}")
public ResponseEntity<GuhadaApiResponse> getCampaign(@PathVariable Long id) {
    CampaignResponse response = campaignService.findById(id);
    return responseApi(response);  // BaseController 메서드
}
```

---

## 5. 예외 처리

### 도메인 예외 정의

```java
public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(Long id) {
        super("캠페인을 찾을 수 없습니다. ID: " + id);
    }
}
```

### 공통 런타임 예외

```java
throw new GuhadaApiRuntimeException(GuhadaApiResult.BAD_REQUEST, "잘못된 요청입니다.");
```

### 예외 핸들러

```java
@ExceptionHandler(CampaignNotFoundException.class)
public ResponseEntity<GuhadaApiErrorResponse> handleNotFound(CampaignNotFoundException ex) {
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new GuhadaApiErrorResponse(GuhadaApiResult.RESOURCE_NOT_FOUND, ex.getMessage()));
}
```

---

## 6. 테스트 코드

### 단위 테스트

```java
@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;

    @Test
    void findById_존재하는_캠페인_조회_성공() {
        // given
        Campaign campaign = Campaign.create("테스트 캠페인");
        given(campaignRepository.findById(1L)).willReturn(Optional.of(campaign));

        // when
        CampaignResponse result = campaignService.findById(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("테스트 캠페인");
    }
}
```

### 통합 테스트

```java
@SpringBootTest
@Transactional
class CampaignIntegrationTest {

    @Autowired
    private CampaignService campaignService;

    @Test
    void 캠페인_생성_및_조회_통합테스트() {
        // given
        CampaignCreateRequest request = CampaignCreateRequest.builder()
                .title("통합테스트 캠페인")
                .seedingType(SeedingType.UNPAID)
                .build();

        // when
        CampaignResponse created = campaignService.create(request);
        CampaignResponse found = campaignService.findById(created.getId());

        // then
        assertThat(found.getTitle()).isEqualTo("통합테스트 캠페인");
    }
}
```
