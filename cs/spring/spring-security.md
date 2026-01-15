# Spring Security

> `[3] 중급` · 선수 지식: [Spring MVC](./spring-mvc.md), [인증과 인가](../security/authentication-authorization.md)

> 인증(Authentication)과 인가(Authorization)를 처리하는 Spring 보안 프레임워크

`#SpringSecurity` `#스프링시큐리티` `#인증` `#Authentication` `#인가` `#Authorization` `#Filter` `#필터` `#SecurityFilterChain` `#UserDetails` `#PasswordEncoder` `#BCrypt` `#JWT` `#OAuth2` `#CSRF` `#CORS` `#SessionManagement` `#세션관리` `#RememberMe` `#SecurityContext` `#Principal` `#GrantedAuthority` `#Role` `#권한` `#AccessDenied` `#FormLogin` `#BasicAuth`

## 왜 알아야 하는가?

보안은 모든 애플리케이션의 필수 요소입니다. Spring Security는 인증, 인가, 세션 관리, CSRF/CORS 등 보안의 모든 측면을 다룹니다. 복잡하지만 한 번 이해하면 견고한 보안 시스템을 구축할 수 있습니다.

## 핵심 개념

- **Authentication (인증)**: "너 누구야?" - 사용자 신원 확인
- **Authorization (인가)**: "뭘 할 수 있어?" - 권한 확인
- **SecurityFilterChain**: 보안 필터 체인
- **SecurityContext**: 인증 정보 저장소

## 쉽게 이해하기

**Spring Security**를 건물 보안에 비유할 수 있습니다.

```
┌─────────────────────────────────────────────────────────────┐
│                    건물 보안 비유                            │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  방문자 (Request)                                            │
│    │                                                         │
│    ▼                                                         │
│  ┌──────────────────────────────────────────┐               │
│  │ 1. 정문 (Filter Chain)                    │               │
│  │    - 기본 검사 (CORS, CSRF)              │               │
│  └──────────────────────────────────────────┘               │
│    │                                                         │
│    ▼                                                         │
│  ┌──────────────────────────────────────────┐               │
│  │ 2. 신분증 확인 (Authentication)           │               │
│  │    "사원증 보여주세요"                    │               │
│  │    → ID/PW, JWT, OAuth 등                │               │
│  └──────────────────────────────────────────┘               │
│    │                                                         │
│    ▼                                                         │
│  ┌──────────────────────────────────────────┐               │
│  │ 3. 출입 권한 확인 (Authorization)         │               │
│  │    "이 층에 들어갈 수 있나요?"            │               │
│  │    → ROLE_ADMIN, ROLE_USER 등            │               │
│  └──────────────────────────────────────────┘               │
│    │                                                         │
│    ▼                                                         │
│  사무실 (Controller) 입장                                    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## 상세 설명

### 기본 설정 (Spring Security 6.x)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 설정
            .csrf(csrf -> csrf.disable())  // REST API는 보통 비활성화

            // 요청별 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )

            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/api/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )

            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            // 세션 관리
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
            );

        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Filter Chain 구조

```
┌─────────────────────────────────────────────────────────────┐
│             Security Filter Chain 순서                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Request                                                     │
│    │                                                         │
│    ▼                                                         │
│  1. SecurityContextPersistenceFilter                        │
│     SecurityContext 로드/저장                               │
│    │                                                         │
│    ▼                                                         │
│  2. CsrfFilter                                              │
│     CSRF 토큰 검증                                          │
│    │                                                         │
│    ▼                                                         │
│  3. LogoutFilter                                            │
│     로그아웃 처리                                            │
│    │                                                         │
│    ▼                                                         │
│  4. UsernamePasswordAuthenticationFilter                    │
│     폼 로그인 처리                                          │
│    │                                                         │
│    ▼                                                         │
│  5. BasicAuthenticationFilter                               │
│     HTTP Basic 인증                                         │
│    │                                                         │
│    ▼                                                         │
│  6. BearerTokenAuthenticationFilter (JWT)                   │
│     JWT 토큰 인증                                           │
│    │                                                         │
│    ▼                                                         │
│  7. ExceptionTranslationFilter                              │
│     인증/인가 예외 처리                                      │
│    │                                                         │
│    ▼                                                         │
│  8. FilterSecurityInterceptor                               │
│     최종 인가 결정                                          │
│    │                                                         │
│    ▼                                                         │
│  Controller                                                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### UserDetailsService 구현

```java
// 사용자 정보 로드
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())  // 이미 암호화된 비밀번호
            .roles(user.getRoles().toArray(new String[0]))
            .build();
    }
}

// 커스텀 UserDetails
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getUsername(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return !user.isLocked(); }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.isEnabled(); }

    // 추가 메서드
    public Long getUserId() { return user.getId(); }
}
```

### JWT 인증 설정

```java
// JWT 설정
@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}

// JWT 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}

// JWT Provider
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.get("roles");

        List<GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
```

### 메서드 레벨 보안

```java
@Configuration
@EnableMethodSecurity  // @PreAuthorize 등 활성화
public class MethodSecurityConfig { }

@Service
public class OrderService {

    // 특정 역할만 접근
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long orderId) { }

    // 표현식 사용
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public Order getOrder(Long userId, Long orderId) { }

    // 반환값 필터링
    @PostAuthorize("returnObject.userId == authentication.principal.id")
    public Order findOrder(Long orderId) { }

    // 컬렉션 필터링
    @PostFilter("filterObject.userId == authentication.principal.id")
    public List<Order> findAllOrders() { }

    // 파라미터 필터링
    @PreFilter("filterObject.userId == authentication.principal.id")
    public void deleteOrders(List<Order> orders) { }

    // 복잡한 조건
    @PreAuthorize("@orderSecurityService.canAccess(#orderId, authentication)")
    public Order getSecureOrder(Long orderId) { }
}

// 커스텀 보안 서비스
@Service("orderSecurityService")
public class OrderSecurityService {
    public boolean canAccess(Long orderId, Authentication auth) {
        // 복잡한 권한 로직
        return true;
    }
}
```

### 인증 정보 접근

```java
@RestController
public class UserController {

    // 1. Principal 파라미터
    @GetMapping("/me")
    public UserResponse getMe(Principal principal) {
        String username = principal.getName();
        return userService.findByUsername(username);
    }

    // 2. @AuthenticationPrincipal
    @GetMapping("/profile")
    public UserResponse getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername());
    }

    // 커스텀 UserDetails
    @GetMapping("/profile2")
    public UserResponse getProfile2(@AuthenticationPrincipal CustomUserDetails user) {
        return userService.findById(user.getUserId());
    }

    // 3. SecurityContextHolder 직접 접근
    @GetMapping("/info")
    public UserResponse getInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUsername(username);
    }
}
```

### CORS 설정

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}

// SecurityConfig에서 사용
http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
```

### 예외 처리

```java
// 인증 실패 처리
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"인증이 필요합니다\"}");
    }
}

// 인가 실패 처리
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"접근 권한이 없습니다\"}");
    }
}

// 설정
http.exceptionHandling(ex -> ex
    .authenticationEntryPoint(customAuthenticationEntryPoint)
    .accessDeniedHandler(customAccessDeniedHandler)
);
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 포괄적인 보안 기능 | 학습 곡선 높음 |
| 유연한 커스터마이징 | 설정 복잡도 |
| Spring 생태계 통합 | 과도한 기본 설정 |
| 검증된 보안 구현 | 디버깅 어려움 |

## 면접 예상 질문

### Q: Authentication과 Authorization의 차이는?

A: **Authentication (인증)**: "누구인가?" 확인. ID/PW, JWT, OAuth 등으로 신원 검증. **Authorization (인가)**: "무엇을 할 수 있는가?" 확인. Role, Permission 기반 권한 검사. **순서**: 인증 → 인가. **Spring Security**: UsernamePasswordAuthenticationFilter(인증) → FilterSecurityInterceptor(인가).

### Q: Spring Security의 세션 관리 전략은?

A: (1) **ALWAYS**: 항상 세션 생성 (2) **IF_REQUIRED**: 필요 시 생성 (기본값) (3) **NEVER**: 생성 안 함, 있으면 사용 (4) **STATELESS**: 세션 미사용 (JWT에 적합). **동시 세션 제어**: maximumSessions으로 동시 로그인 수 제한 가능.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Spring MVC](./spring-mvc.md) | 선수 지식 | [3] 중급 |
| [인증과 인가](../security/authentication-authorization.md) | 개념 | [3] 중급 |
| [OAuth 2.0과 JWT](../security/oauth-jwt.md) | 토큰 인증/소셜 로그인 | [4] 심화 |

## 참고 자료

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [Baeldung - Spring Security](https://www.baeldung.com/security-spring)
