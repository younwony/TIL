# Git 기본 개념

> `[2] 입문` · 선수 지식: 없음

> Git은 분산 버전 관리 시스템(DVCS)으로, 소스 코드의 변경 이력을 추적하고 여러 개발자 간의 협업을 지원한다.

## 왜 알아야 하는가?

- **실무**: 모든 개발 프로젝트는 Git으로 버전 관리를 합니다. Git을 모르면 팀 협업이 불가능하고, 코드 히스토리 추적과 롤백도 할 수 없습니다. "어제 코드가 왜 안 되지?"라는 상황에서 Git이 없으면 해결 방법이 없습니다.
- **면접**: 거의 모든 기술 면접에서 "Git의 merge와 rebase 차이", "브랜치 전략" 등을 질문합니다. Git은 개발자의 기본 소양으로 간주되어, Git을 모르면 협업 능력을 의심받습니다.
- **기반 지식**: Git 브랜치 전략, CI/CD, 협업 워크플로우 등 모든 개발 프로세스의 기반이 됩니다. Git을 모르면 GitHub Flow, Git Flow 같은 브랜치 전략을 이해할 수 없습니다.

## 핵심 개념

- **분산 버전 관리**: 모든 개발자가 전체 저장소의 복사본을 로컬에 가지고 있어 네트워크 없이도 작업 가능
- **스냅샷 기반**: 파일의 변경 사항을 델타가 아닌 스냅샷으로 저장
- **세 가지 상태**: Working Directory, Staging Area(Index), Repository
- **브랜치**: 독립적인 작업 공간을 생성하여 병렬 개발 지원
- **무결성**: SHA-1 해시를 사용하여 모든 데이터의 무결성 보장

## 쉽게 이해하기

**Git**을 게임의 "세이브/로드" 시스템에 비유할 수 있습니다.

게임을 하다가 중요한 보스전 앞에서 저장하고, 여러 번 도전하다 안 되면 불러오기 하죠. Git도 마찬가지입니다.

| 게임 개념 | Git 개념 |
|----------|----------|
| 세이브 파일 | Commit (저장된 상태) |
| 세이브 포인트 이름 | Branch (작업 버전) |
| 여러 엔딩 루트 | 여러 브랜치로 병렬 작업 |
| 불러오기 | Checkout (과거로 이동) |
| 다른 세이브 파일과 합치기 | Merge |

**왜 Git이 필요한가?**
- "작업_최종.doc", "작업_최종_진짜최종.doc" 같은 파일명 지옥에서 벗어남
- "어제 코드가 더 나았는데..." 할 때 되돌리기 가능
- 팀원과 동시에 작업해도 코드가 섞이지 않음

## 상세 설명

### Git의 세 가지 영역

```
Working Directory    →    Staging Area    →    Repository
   (작업 디렉토리)           (스테이징)            (저장소)
        │                      │                    │
        │      git add         │    git commit      │
        ├─────────────────────►├───────────────────►│
        │                      │                    │
        │◄─────────────────────┴────────────────────┤
        │              git checkout                 │
```

| 영역 | 설명 | 상태 |
|------|------|------|
| Working Directory | 실제 파일이 존재하는 작업 공간 | Modified |
| Staging Area | 커밋할 파일들이 대기하는 공간 | Staged |
| Repository | 커밋된 스냅샷이 저장되는 공간 | Committed |

### 기본 명령어

#### 저장소 초기화 및 복제

```bash
# 새 저장소 초기화
git init

# 원격 저장소 복제
git clone <url>
```

#### 변경 사항 관리

```bash
# 상태 확인
git status

# 스테이징에 추가
git add <file>
git add .  # 모든 변경 사항

# 커밋
git commit -m "commit message"

# 커밋 히스토리 확인
git log
git log --oneline --graph
```

#### 브랜치 관리

```bash
# 브랜치 목록 확인
git branch

# 브랜치 생성
git branch <branch-name>

# 브랜치 전환
git checkout <branch-name>
git switch <branch-name>  # Git 2.23+

# 브랜치 생성 및 전환
git checkout -b <branch-name>
git switch -c <branch-name>  # Git 2.23+

# 브랜치 병합
git merge <branch-name>

# 브랜치 삭제
git branch -d <branch-name>
```

#### 원격 저장소 관리

```bash
# 원격 저장소 확인
git remote -v

# 원격 저장소 추가
git remote add origin <url>

# 변경 사항 푸시
git push origin <branch-name>

# 변경 사항 가져오기
git fetch origin
git pull origin <branch-name>  # fetch + merge
```

### Git 파일 상태 라이프사이클

```
Untracked    Unmodified    Modified    Staged
    │             │            │          │
    │   git add   │            │          │
    ├────────────────────────────────────►│
    │             │            │          │
    │             │  파일 수정  │          │
    │             ├───────────►│          │
    │             │            │          │
    │             │            │ git add  │
    │             │            ├─────────►│
    │             │            │          │
    │             │◄───────────┴──────────┤
    │             │       git commit      │
    │             │            │          │
    │◄────────────┤            │          │
    │  파일 삭제   │            │          │
```

### Merge vs Rebase

| 특성 | Merge | Rebase |
|------|-------|--------|
| 히스토리 | 분기점 유지 (non-linear) | 선형 히스토리 |
| 커밋 | 머지 커밋 생성 | 커밋 재작성 |
| 충돌 해결 | 한 번에 해결 | 커밋마다 해결 |
| 사용 시점 | 공개 브랜치 병합 | 로컬 브랜치 정리 |
| 안전성 | 원본 커밋 보존 | 히스토리 변경 (주의 필요) |

**왜?**
Merge는 실제 작업 흐름을 보존하여 누가 언제 무엇을 했는지 추적이 쉽습니다. Rebase는 히스토리를 깔끔하게 만들지만, 커밋 해시가 바뀌어 공유된 브랜치에서 사용 시 충돌이 발생합니다.

**권장 (O) / 비권장 (X)**
- (O) 기능 브랜치를 main에 합칠 때: merge
- (O) 로컬 커밋을 정리할 때: rebase
- (X) 이미 push한 브랜치를 rebase
- (X) 여러 명이 작업 중인 브랜치를 rebase

**만약 지키지 않으면?**
Push된 브랜치를 rebase하면 다른 팀원의 브랜치와 히스토리가 달라져 충돌이 발생하고, force push로 인해 다른 사람의 작업이 덮어씌워질 수 있습니다.

```bash
# Merge
git checkout main
git merge feature

# Rebase
git checkout feature
git rebase main
```

### Reset vs Revert

| 명령어 | 동작 | 히스토리 | 사용 시점 |
|--------|------|----------|-----------|
| `reset` | 커밋 삭제 | 변경됨 | 로컬에서만 |
| `revert` | 반대 커밋 생성 | 보존됨 | 공유된 브랜치 |

```bash
# Reset (커밋 삭제)
git reset --soft HEAD~1   # 커밋만 취소, 변경사항 staged 유지
git reset --mixed HEAD~1  # 커밋 취소, 변경사항 unstaged (기본값)
git reset --hard HEAD~1   # 커밋 취소, 변경사항도 삭제

# Revert (새 커밋으로 취소)
git revert <commit-hash>
```

### Stash

작업 중인 변경 사항을 임시로 저장하고 나중에 복원할 수 있다.

```bash
# 현재 변경 사항 임시 저장
git stash
git stash save "message"

# stash 목록 확인
git stash list

# stash 적용
git stash apply        # 최근 stash 적용 (stash 유지)
git stash pop          # 최근 stash 적용 후 삭제
git stash apply stash@{n}  # 특정 stash 적용

# stash 삭제
git stash drop stash@{n}
git stash clear  # 모든 stash 삭제
```

## 면접 예상 질문

- Q: Git의 merge와 rebase의 차이점은 무엇인가요?
  - A: merge는 두 브랜치를 합치면서 병합 커밋을 생성하여 히스토리에 분기점이 남습니다. rebase는 커밋들을 다른 브랜치 위로 재배치하여 선형적인 히스토리를 만듭니다. **왜 이렇게 답해야 하나요?** merge는 실제 작업 흐름을 보존하여 협업 시 추적이 쉽고 안전합니다. rebase는 히스토리가 깔끔하지만 커밋 해시가 변경되어 공유 브랜치에서는 충돌 위험이 있습니다. 따라서 공개 브랜치는 merge, 로컬 정리는 rebase를 사용합니다.

- Q: git reset과 git revert의 차이점은 무엇인가요?
  - A: reset은 HEAD를 이전 커밋으로 이동시켜 커밋을 삭제하므로 히스토리가 변경됩니다. revert는 특정 커밋의 변경 사항을 되돌리는 새로운 커밋을 생성하므로 히스토리가 보존됩니다. **왜 이렇게 답해야 하나요?** 공유 브랜치에서 reset을 사용하면 다른 개발자가 가진 커밋이 사라져 히스토리 불일치가 발생합니다. revert는 새 커밋을 추가하므로 안전하게 변경을 취소할 수 있습니다.

- Q: Staging Area(Index)의 역할은 무엇인가요?
  - A: Staging Area는 커밋할 변경 사항을 선별적으로 준비하는 중간 영역입니다. 이를 통해 작업 디렉토리의 모든 변경 사항이 아닌, 원하는 변경 사항만 선택적으로 커밋할 수 있어 논리적인 단위로 커밋을 구성할 수 있습니다. **왜 이렇게 답해야 하나요?** 한 파일에서 여러 기능을 수정했을 때, 기능별로 나눠서 커밋하면 코드 리뷰와 롤백이 쉬워집니다. Git의 3단계 구조 덕분에 이런 세밀한 버전 관리가 가능합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 브랜치 전략](./git-branch-strategy.md) | Git 기본을 활용한 팀 협업 워크플로우 | Intermediate |
| [Git 내부 동작 원리](./git-internals.md) | Git이 내부적으로 어떻게 동작하는지 심화 학습 | Advanced |

## 참고 자료

- [Git 공식 문서](https://git-scm.com/doc)
- [Pro Git Book](https://git-scm.com/book/ko/v2)
