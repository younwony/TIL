# Git 브랜치 전략

> `[3] 중급` · 선수 지식: [Git 기본 개념](./git-basics.md)

> 브랜치 전략은 팀이 Git 브랜치를 어떻게 생성, 관리, 병합할지 정의하는 규칙으로, 효율적인 협업과 안정적인 배포를 위해 필수적이다.

`#브랜치전략` `#BranchStrategy` `#GitFlow` `#GitHubFlow` `#TrunkBased` `#TrunkBasedDevelopment` `#GitLabFlow` `#feature브랜치` `#FeatureBranch` `#릴리스` `#Release` `#hotfix` `#핫픽스` `#develop` `#main` `#master` `#머지전략` `#MergeStrategy` `#CICD` `#지속적배포` `#PullRequest` `#PR` `#코드리뷰` `#CodeReview` `#버전관리` `#협업워크플로우` `#릴리스브랜치` `#배포` `#Deployment`

## 왜 알아야 하는가?

- **실무**: 팀 협업에서 브랜치 전략이 없으면 코드 충돌, 배포 실패, 롤백 어려움 등 혼란이 발생합니다. "어느 브랜치에서 작업하지?", "언제 merge하지?"에 대한 명확한 규칙이 없으면 팀 생산성이 급격히 떨어집니다.
- **면접**: "프로젝트에서 어떤 브랜치 전략을 사용했나요?", "Git Flow와 GitHub Flow의 차이"는 거의 모든 면접에서 나오는 질문입니다. 실제 협업 경험을 평가하는 중요한 지표입니다.
- **기반 지식**: CI/CD 파이프라인, 릴리스 관리, 코드 리뷰 프로세스 등 모든 개발 워크플로우의 기반이 됩니다. 브랜치 전략을 모르면 자동 배포 설정, 환경별 분리 등을 이해할 수 없습니다.

## 핵심 개념

- **브랜치 전략**: 팀 내 브랜치 생성, 네이밍, 병합 규칙을 정의한 워크플로우
- **Git Flow**: 복잡한 릴리스 주기를 가진 프로젝트에 적합한 브랜치 모델
- **GitHub Flow**: 지속적 배포(CD)에 적합한 단순화된 브랜치 모델
- **Trunk-Based Development**: 메인 브랜치 중심의 빠른 통합 전략
- **Feature Branch**: 기능 단위로 브랜치를 생성하여 독립적으로 개발

## 쉽게 이해하기

**브랜치 전략**을 신문사의 출판 프로세스에 비유할 수 있습니다.

### Git Flow = 잡지 출판사

**정기 발행하는 월간지처럼 릴리스 주기가 명확한 경우**

| 비유 | Git Flow |
|------|----------|
| 편집실 (기사 작성 중) | develop 브랜치 |
| 기자의 원고 | feature 브랜치 |
| 출판 준비실 | release 브랜치 |
| 서점에 진열된 잡지 | main 브랜치 |
| 긴급 정정 공지 | hotfix 브랜치 |

```
기자 A: 스포츠 기사 작성 (feature/sports)
기자 B: 연예 기사 작성 (feature/entertainment)
→ 편집실에 모음 (develop)
→ 3월호 출판 준비 (release/march)
→ 인쇄 및 배포 (main)
→ 오타 발견! 긴급 정정 (hotfix/typo)
```

### GitHub Flow = 온라인 뉴스

**실시간으로 기사를 올리는 온라인 뉴스처럼 빠른 배포가 필요한 경우**

| 비유 | GitHub Flow |
|------|-----------|
| 라이브 웹사이트 | main 브랜치 (항상 배포 가능) |
| 기자의 초안 | feature 브랜치 |
| 데스크 검수 | Pull Request & Review |
| 즉시 게시 | Merge 후 자동 배포 |

**왜 이런 차이가 생겼나?**
- Git Flow: 패키지 소프트웨어처럼 버전 관리가 중요할 때
- GitHub Flow: 웹 서비스처럼 빠른 배포가 중요할 때

## 상세 설명

### Git Flow

Vincent Driessen이 제안한 브랜치 모델로, 명확한 릴리스 주기가 있는 프로젝트에 적합하다.

```
main (master)
  │
  ├──────────────────────────────────────────────► (배포된 코드)
  │         │              │              │
  │         │   release/1.0│              │
  │         │    ┌─────────┤              │
  │         │    │         │              │
  │         │    │    fix──┤              │
  │         │    │         │              │
  │         │    └────────►│              │
  │         │              │              │
develop    │              │              │
  │◄────────┴──────────────┴──────────────┤
  │                                       │
  ├──feature/login──►│                    │
  │                  ├────►│              │
  │                        │              │
  ├──feature/signup────────►│             │
  │                        │              │
  │◄───────────────────────┘              │
  │                                       │
  │           hotfix/critical─────────────┤
  │                                       │
```

#### 브랜치 종류

| 브랜치 | 용도 | 생성 위치 | 병합 대상 |
|--------|------|-----------|-----------|
| `main` | 배포된 코드 | - | - |
| `develop` | 개발 통합 | main | main (릴리스 시) |
| `feature/*` | 새 기능 개발 | develop | develop |
| `release/*` | 릴리스 준비 | develop | main, develop |
| `hotfix/*` | 긴급 버그 수정 | main | main, develop |

#### 사용 명령어

```bash
# Feature 브랜치 시작
git checkout develop
git checkout -b feature/login

# Feature 브랜치 완료
git checkout develop
git merge --no-ff feature/login
git branch -d feature/login

# Release 브랜치 시작
git checkout develop
git checkout -b release/1.0

# Release 완료
git checkout main
git merge --no-ff release/1.0
git tag -a v1.0
git checkout develop
git merge --no-ff release/1.0
git branch -d release/1.0

# Hotfix 시작
git checkout main
git checkout -b hotfix/critical-bug

# Hotfix 완료
git checkout main
git merge --no-ff hotfix/critical-bug
git tag -a v1.0.1
git checkout develop
git merge --no-ff hotfix/critical-bug
git branch -d hotfix/critical-bug
```

#### 장단점

| 장점 | 단점 |
|------|------|
| 명확한 브랜치 역할 | 복잡한 구조 |
| 릴리스 버전 관리 용이 | 브랜치 전환 빈번 |
| 병렬 개발 지원 | 지속적 배포에 부적합 |
| hotfix 프로세스 명확 | 학습 곡선 존재 |

### GitHub Flow

GitHub에서 제안한 단순화된 브랜치 전략으로, 지속적 배포 환경에 적합하다.

```
main ──●────●────●────●────●────●────●──► (항상 배포 가능)
       │    ▲    │    ▲    │    ▲    │
       │    │    │    │    │    │    │
       └─●──┘    └─●──┘    └●─●─┘    │
         │         │        │ │      │
      feature   feature  feature     │
        PR        PR       PR        │
```

#### 워크플로우

1. `main` 브랜치는 항상 배포 가능한 상태 유지
2. 새 작업은 `main`에서 브랜치 생성
3. 로컬에서 커밋하고 원격에 주기적으로 push
4. Pull Request 생성하여 코드 리뷰 요청
5. 리뷰 후 `main`에 병합
6. 병합 후 즉시 배포

```bash
# 새 브랜치 생성
git checkout main
git pull origin main
git checkout -b feature/new-feature

# 작업 후 push
git add .
git commit -m "Add new feature"
git push origin feature/new-feature

# GitHub에서 PR 생성 후 리뷰
# 병합 후 로컬 정리
git checkout main
git pull origin main
git branch -d feature/new-feature
```

#### 장단점

| 장점 | 단점 |
|------|------|
| 단순하고 이해하기 쉬움 | 릴리스 버전 관리 어려움 |
| 지속적 배포에 적합 | 복잡한 릴리스 주기에 부적합 |
| 빠른 피드백 루프 | 대규모 팀에서 충돌 가능성 |
| 코드 리뷰 강제 | 롤백 전략 필요 |

### Trunk-Based Development

모든 개발자가 하나의 메인 브랜치(trunk)에 자주 통합하는 전략이다.

```
main (trunk)
  │
  ●────●────●────●────●────●────●────●──►
  │    │    │    │    │    │    │    │
  │   ┌┴┐  ┌┴┐   │   ┌┴┐   │   ┌┴┐   │
  │   │A│  │B│   │   │C│   │   │D│   │
  │   └┬┘  └┬┘   │   └┬┘   │   └┬┘   │
  │    │    │    │    │    │    │    │
       └────┴────┴────┴────┴────┴────┘
         짧은 수명의 feature 브랜치
            (1-2일 이내 병합)
```

#### 핵심 원칙

- Feature 브랜치는 1-2일 이내 병합
- 작은 단위로 자주 커밋
- Feature Flag로 미완성 기능 숨김
- 강력한 CI/CD 파이프라인 필수

```bash
# 짧은 수명의 브랜치
git checkout main
git checkout -b short-lived-feature

# 작업 완료 후 빠르게 병합 (1-2일 이내)
git checkout main
git pull
git merge short-lived-feature
git push origin main
```

#### 장단점

| 장점 | 단점 |
|------|------|
| 머지 지옥 방지 | 강력한 CI/CD 필수 |
| 빠른 피드백 | Feature Flag 관리 필요 |
| 코드 충돌 최소화 | 미완성 코드가 main에 존재 |
| 지속적 통합 촉진 | 팀 역량 필요 |

### GitLab Flow

GitHub Flow와 Git Flow의 중간 형태로, 환경별 브랜치를 추가한 전략이다.

```
production ──●────────────●────────────●──► (운영)
             ▲            ▲            ▲
             │            │            │
staging ─────●────●───────●────●───────●──► (스테이징)
             ▲    ▲       ▲    ▲       ▲
             │    │       │    │       │
main ────────●────●───────●────●───────●──► (개발)
             ▲    ▲       ▲    ▲       ▲
             │    │       │    │       │
          feature branches
```

## 브랜치 전략 비교

| 특성 | Git Flow | GitHub Flow | Trunk-Based | GitLab Flow |
|------|----------|-------------|-------------|-------------|
| 복잡도 | 높음 | 낮음 | 낮음 | 중간 |
| 릴리스 주기 | 정기적 | 지속적 | 지속적 | 유연 |
| 브랜치 수 | 많음 | 적음 | 최소 | 중간 |
| 환경 분리 | 릴리스 브랜치 | 없음 | 없음 | 환경별 브랜치 |
| 적합 팀 규모 | 대규모 | 소규모~중규모 | 숙련된 팀 | 중규모 |
| CI/CD 요구 | 중간 | 높음 | 매우 높음 | 높음 |

## 브랜치 네이밍 컨벤션

```bash
# Feature 브랜치
feature/user-authentication
feature/JIRA-123-payment-integration

# Bugfix 브랜치
bugfix/login-validation-error
bugfix/JIRA-456-null-pointer

# Hotfix 브랜치
hotfix/security-vulnerability
hotfix/v1.2.1-critical-fix

# Release 브랜치
release/1.0.0
release/2023-Q4
```

## 면접 예상 질문

- Q: Git Flow와 GitHub Flow의 차이점은 무엇이고, 각각 어떤 상황에 적합한가요?
  - A: Git Flow는 develop, release, hotfix 등 여러 브랜치를 사용하여 복잡한 릴리스 주기를 관리합니다. 정기 릴리스가 있는 대규모 프로젝트에 적합합니다. GitHub Flow는 main 브랜치와 feature 브랜치만 사용하는 단순한 구조로, 지속적 배포(CD) 환경에서 빠른 개발 주기를 가진 프로젝트에 적합합니다. **왜 이렇게 답해야 하나요?** Git Flow의 복잡한 구조는 릴리스 버전 관리와 병렬 개발이 필요한 패키지 소프트웨어에서는 유용하지만, 웹 서비스처럼 빠른 배포가 중요한 경우 오히려 병목이 됩니다. 프로젝트 특성에 맞는 전략을 선택해야 합니다.

- Q: Trunk-Based Development의 장점과 이를 적용하기 위한 전제 조건은 무엇인가요?
  - A: 장점은 머지 충돌 최소화, 빠른 피드백, 지속적 통합 촉진입니다. 전제 조건으로는 강력한 CI/CD 파이프라인, 자동화된 테스트, Feature Flag 시스템, 그리고 팀원들의 높은 역량과 협업 문화가 필요합니다. **왜 이렇게 답해야 하나요?** 브랜치 수명이 짧아야 하므로 자동화된 테스트로 빠르게 검증하고, Feature Flag로 미완성 기능을 숨겨야 합니다. 인프라 없이 도입하면 main 브랜치가 불안정해져 오히려 역효과가 발생합니다.

- Q: Feature 브랜치 전략에서 장기간 유지되는 브랜치의 문제점과 해결 방법은?
  - A: 장기 브랜치는 main과의 차이가 커져 머지 충돌이 많아지고, 통합 테스트가 어려워집니다. 해결 방법으로는 작은 단위로 작업 분할, main을 주기적으로 feature 브랜치에 병합(또는 rebase), 그리고 Feature Flag를 활용하여 미완성 기능을 숨기고 빠르게 병합하는 것이 있습니다. **왜 이렇게 답해야 하나요?** 장기 브랜치는 "통합 지옥(Integration Hell)"을 유발합니다. 2주 이상 격리된 브랜치는 코드 베이스와 동떨어져 병합 시 대규모 충돌이 발생하고, 다른 기능과의 통합 문제를 늦게 발견하게 됩니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 기본 개념](./git-basics.md) | 브랜치 전략의 기초가 되는 Git 기본 개념 (선수 지식) | Beginner |
| [Git 내부 동작 원리](./git-internals.md) | 브랜치가 내부적으로 어떻게 구현되는지 이해 | Advanced |

## 참고 자료

- [A successful Git branching model (Git Flow)](https://nvie.com/posts/a-successful-git-branching-model/)
- [GitHub Flow](https://docs.github.com/en/get-started/quickstart/github-flow)
- [Trunk-Based Development](https://trunkbaseddevelopment.com/)
- [GitLab Flow](https://docs.gitlab.com/ee/topics/gitlab_flow.html)
