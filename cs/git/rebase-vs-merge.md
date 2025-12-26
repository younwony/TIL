# Rebase vs Merge

> `[3] 중급` · 선수 지식: [Git 기본 개념](./git-basics.md)

> 브랜치를 통합하는 두 가지 방법과 각각의 장단점

`#Rebase` `#Merge` `#리베이스` `#머지` `#브랜치통합` `#Git` `#GitHistory` `#히스토리정리` `#InteractiveRebase` `#FastForward` `#3WayMerge` `#MergeCommit` `#LinearHistory` `#선형이력` `#충돌해결` `#ConflictResolution` `#SquashMerge`

## 왜 알아야 하는가?

브랜치 통합은 협업의 핵심입니다. Merge와 Rebase는 각각 장단점이 있어 상황에 맞게 선택해야 합니다. 깔끔한 히스토리 관리는 코드 리뷰, 디버깅, 롤백에 직접적인 영향을 줍니다.

## 핵심 개념

- **Merge**: 두 브랜치를 합치며 머지 커밋 생성
- **Rebase**: 커밋을 다른 브랜치 위로 재배치
- **Fast-Forward**: 머지 커밋 없이 포인터만 이동
- **Squash**: 여러 커밋을 하나로 합침

## 쉽게 이해하기

**Merge**: 두 역사를 합쳐서 새 장(머지 커밋)을 씀
**Rebase**: 역사를 재작성하여 일직선으로 정리

```
책 쓰기:
Merge: A→B→C와 D→E를 합쳐 "A→B→C, D→E가 합쳐졌다" 기록
Rebase: D→E를 C 뒤로 옮겨 A→B→C→D→E로 정리
```

## 상세 설명

### Merge

```
Before:
      main:    A───B───C
                   \
      feature:      D───E

After (git merge feature):
      main:    A───B───C───────M (merge commit)
                   \          /
      feature:      D───E────┘

M = 머지 커밋 (두 브랜치 통합 기록)
```

```bash
git checkout main
git merge feature
```

**특징**:
- 브랜치 히스토리 보존
- 머지 커밋 생성 (비파괴적)
- 충돌 시 한 번만 해결

### Rebase

```
Before:
      main:    A───B───C
                   \
      feature:      D───E

After (git rebase main):
      main:    A───B───C
                       \
      feature:          D'───E' (새로운 커밋)

D, E가 C 위로 재배치됨
```

```bash
git checkout feature
git rebase main
# 이후 main에서 fast-forward merge 가능
git checkout main
git merge feature  # Fast-Forward
```

**특징**:
- 선형적인 히스토리
- 커밋 해시 변경 (SHA가 바뀜)
- 충돌 시 커밋마다 해결 필요

### 비교

| 항목 | Merge | Rebase |
|------|-------|--------|
| 히스토리 | 분기 보존 | 선형 |
| 커밋 해시 | 유지 | 변경 |
| 머지 커밋 | 생성 | 없음 |
| 충돌 해결 | 1회 | 커밋당 |
| 공유 브랜치 | 안전 | 위험 |

### Golden Rule of Rebase

```
⚠️ 절대 공유된 브랜치를 Rebase하지 마세요!

예: main 브랜치를 rebase하면...

개발자 A 로컬:
main: A───B───C'───D' (rebase 후)

개발자 B 로컬:
main: A───B───C───D (원본)

→ 충돌 발생, 중복 커밋
```

**규칙**: 로컬 브랜치만 Rebase, 푸시된 건 Merge

### Interactive Rebase

```bash
git rebase -i HEAD~4

# 에디터에서:
pick   abc1234 Add feature A
squash def5678 Fix typo        # 이전 커밋에 합치기
pick   ghi9012 Add feature B
reword jkl3456 Refactor code   # 메시지 수정
```

| 명령 | 설명 |
|------|------|
| pick | 커밋 유지 |
| reword | 메시지만 수정 |
| edit | 커밋 내용 수정 |
| squash | 이전 커밋에 합치기 (메시지 합침) |
| fixup | 이전 커밋에 합치기 (메시지 버림) |
| drop | 커밋 삭제 |

### 실무 전략

```
권장 워크플로우:

1. feature 개발 중 main 업데이트 반영:
   git checkout feature
   git rebase main  (로컬이므로 OK)

2. PR 준비 시 커밋 정리:
   git rebase -i HEAD~n

3. PR 머지 시:
   - Squash Merge: 깔끔한 main 히스토리
   - Merge Commit: 기능별 히스토리 보존

4. main에서 feature 머지:
   git checkout main
   git merge feature (또는 Squash Merge)
```

### Squash Merge

```bash
git merge --squash feature
git commit -m "Add feature X"

Before:
main:    A───B───C
              \
feature:       D───E───F

After:
main:    A───B───C───X (D+E+F가 X로 합쳐짐)
```

**장점**: main 히스토리 깔끔
**단점**: 세부 커밋 정보 손실

## 트레이드오프

| 전략 | 장점 | 단점 |
|------|------|------|
| Merge | 안전, 히스토리 보존 | 복잡한 그래프 |
| Rebase | 선형 히스토리 | 공유 시 위험, 충돌 여러 번 |
| Squash | 깔끔한 main | 세부 이력 손실 |

## 면접 예상 질문

### Q: Rebase와 Merge의 차이는?

A: **Merge**: 두 브랜치를 합치며 머지 커밋 생성, 히스토리 보존. **Rebase**: 커밋을 다른 베이스 위로 재배치, 선형 히스토리 생성. **선택 기준**: 공유 브랜치 → Merge, 로컬 정리 → Rebase. **주의**: 이미 푸시된 커밋은 Rebase 금지.

### Q: 언제 Rebase를 사용하나요?

A: (1) **로컬 커밋 정리**: PR 전에 커밋 합치기/정리 (2) **main 변경 반영**: feature 브랜치에 main 업데이트 반영 (3) **선형 히스토리**: 깔끔한 히스토리 원할 때. **금지**: 이미 공유(push)된 브랜치, 공개 브랜치(main).

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 기본 개념](./git-basics.md) | 선수 지식 | [2] 입문 |
| [Git 브랜치 전략](./git-branch-strategy.md) | 활용 | [3] 중급 |

## 참고 자료

- [Pro Git - Rebasing](https://git-scm.com/book/en/v2/Git-Branching-Rebasing)
- [Atlassian Git Tutorials](https://www.atlassian.com/git/tutorials/merging-vs-rebasing)
