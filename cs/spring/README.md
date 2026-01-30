# Spring Framework

Spring Framework 관련 CS 학습 문서입니다.

## 개요

Spring은 Java 엔터프라이즈 애플리케이션 개발을 위한 포괄적인 프레임워크입니다. DI/IoC를 핵심으로 웹, 데이터, 보안 등 다양한 기능을 제공합니다.

## 목차

### [3] 중급

| 문서 | 설명 | 선수 지식 |
|------|------|----------|
| [Spring DI/IoC](./spring-di-ioc.md) | 의존성 주입과 제어의 역전 | OOP, 리플렉션 |
| [Spring AOP](./spring-aop.md) | 관점 지향 프로그래밍 | DI/IoC, 프록시 패턴 |
| [트랜잭션 관리](./spring-transaction.md) | 선언적 트랜잭션 관리 | 트랜잭션, AOP |
| [Spring MVC](./spring-mvc.md) | 웹 애플리케이션 프레임워크 | DI/IoC, HTTP |
| [Spring Security](./spring-security.md) | 인증과 인가 프레임워크 | Spring MVC, 보안 |
| [@Scheduled 스케줄링](./spring-scheduled.md) | 태스크 스케줄링 (fixedRate, cron 등) | DI/IoC, AOP |

## 학습 순서

```
Spring DI/IoC (핵심)
      │
      ├── Spring AOP
      │       │
      │       └── 트랜잭션 관리
      │
      ├── Spring MVC
      │       │
      │       └── Spring Security
      │
      └── @Scheduled (스케줄링)
```

## 핵심 개념

### DI/IoC
- 객체 생성과 의존성 관리를 Spring Container에 위임
- 느슨한 결합, 테스트 용이성

### AOP
- 횡단 관심사(로깅, 트랜잭션, 보안)를 분리
- 핵심 비즈니스 로직에 집중

### Spring MVC
- Model-View-Controller 패턴
- REST API 개발의 표준

### Spring Security
- 인증(Authentication)과 인가(Authorization)
- 다양한 인증 방식 지원 (Form, JWT, OAuth2)
