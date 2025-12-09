# Git 기본 개념

> Git은 분산 버전 관리 시스템(DVCS)으로, 소스 코드의 변경 이력을 추적하고 여러 개발자 간의 협업을 지원한다.

## 핵심 개념

- **분산 버전 관리**: 모든 개발자가 전체 저장소의 복사본을 로컬에 가지고 있어 네트워크 없이도 작업 가능
- **스냅샷 기반**: 파일의 변경 사항을 델타가 아닌 스냅샷으로 저장
- **세 가지 상태**: Working Directory, Staging Area(Index), Repository
- **브랜치**: 독립적인 작업 공간을 생성하여 병렬 개발 지원
- **무결성**: SHA-1 해시를 사용하여 모든 데이터의 무결성 보장

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
  - A: merge는 두 브랜치를 합치면서 병합 커밋을 생성하여 히스토리에 분기점이 남습니다. rebase는 커밋들을 다른 브랜치 위로 재배치하여 선형적인 히스토리를 만듭니다. merge는 히스토리를 보존하고 안전하며, rebase는 깔끔한 히스토리를 만들지만 공유된 브랜치에서는 사용을 피해야 합니다.

- Q: git reset과 git revert의 차이점은 무엇인가요?
  - A: reset은 HEAD를 이전 커밋으로 이동시켜 커밋을 삭제하므로 히스토리가 변경됩니다. revert는 특정 커밋의 변경 사항을 되돌리는 새로운 커밋을 생성하므로 히스토리가 보존됩니다. 공유된 브랜치에서는 revert를 사용해야 합니다.

- Q: Staging Area(Index)의 역할은 무엇인가요?
  - A: Staging Area는 커밋할 변경 사항을 선별적으로 준비하는 중간 영역입니다. 이를 통해 작업 디렉토리의 모든 변경 사항이 아닌, 원하는 변경 사항만 선택적으로 커밋할 수 있어 논리적인 단위로 커밋을 구성할 수 있습니다.

## 참고 자료

- [Git 공식 문서](https://git-scm.com/doc)
- [Pro Git Book](https://git-scm.com/book/ko/v2)
