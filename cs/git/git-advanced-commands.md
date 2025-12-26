# Git 고급 명령어 (Git Advanced Commands)

> `[3] 중급` · 선수 지식: [Git 기본 개념](./git-basics.md), [Rebase vs Merge](./rebase-vs-merge.md)

> 복잡한 상황을 해결하는 Git 고급 명령어들

`#Git` `#cherrypick` `#체리픽` `#revert` `#reset` `#stash` `#bisect` `#reflog` `#clean` `#blame` `#log` `#diff` `#되돌리기` `#이력조회` `#문제해결` `#Troubleshooting` `#고급명령어`

## 왜 알아야 하는가?

실무에서는 단순한 커밋/푸시를 넘어 복잡한 상황이 발생합니다. 특정 커밋만 가져오기, 커밋 되돌리기, 버그 원인 찾기 등 고급 명령어를 알면 문제를 빠르게 해결할 수 있습니다.

## 핵심 개념

- **cherry-pick**: 특정 커밋만 가져오기
- **revert**: 커밋 되돌리기 (새 커밋)
- **reset**: 히스토리 되돌리기
- **stash**: 임시 저장
- **bisect**: 버그 원인 커밋 찾기

## 쉽게 이해하기

| 명령어 | 비유 |
|--------|------|
| cherry-pick | 다른 가지에서 체리만 따오기 |
| revert | 반대 작업으로 원상복구 |
| reset | 시간을 되돌려 없던 일로 |
| stash | 책상 서랍에 잠시 넣어두기 |
| bisect | 이진 탐색으로 범인 찾기 |

## 상세 설명

### cherry-pick

특정 커밋만 현재 브랜치로 가져오기:

```bash
git cherry-pick <commit-hash>

# 예시
git cherry-pick abc1234

# 여러 커밋
git cherry-pick abc1234 def5678

# 범위 (첫 커밋 제외)
git cherry-pick abc1234..def5678
```

```
Before:
main:    A───B───C
              \
feature:       D───E───F

git checkout main
git cherry-pick E

After:
main:    A───B───C───E'
              \
feature:       D───E───F
```

**사용 시점**: 핫픽스를 다른 브랜치에도 적용, 특정 기능만 가져오기

### revert

커밋을 되돌리는 새 커밋 생성 (안전):

```bash
# 단일 커밋 되돌리기
git revert <commit-hash>

# 여러 커밋 되돌리기
git revert abc1234..def5678

# 머지 커밋 되돌리기
git revert -m 1 <merge-commit-hash>
```

```
Before:
A───B───C

git revert C

After:
A───B───C───C' (C를 취소하는 커밋)
```

**vs reset**: revert는 히스토리 보존, reset은 히스토리 삭제

### reset

커밋을 되돌리기 (히스토리 변경):

```bash
# 커밋만 취소 (변경사항 스테이징 유지)
git reset --soft HEAD~1

# 커밋과 스테이징 취소 (변경사항 유지)
git reset --mixed HEAD~1  # 기본값

# 커밋, 스테이징, 변경사항 모두 삭제
git reset --hard HEAD~1
```

| 옵션 | HEAD | Index(Staging) | Working Dir |
|------|------|---------------|-------------|
| --soft | 이동 | 유지 | 유지 |
| --mixed | 이동 | 초기화 | 유지 |
| --hard | 이동 | 초기화 | 초기화 |

**주의**: `--hard`는 복구 불가! 공유된 브랜치에서 reset 금지!

### stash

작업 임시 저장:

```bash
# 현재 변경사항 저장
git stash

# 메시지와 함께 저장
git stash save "WIP: feature X"

# 스태시 목록 확인
git stash list

# 스태시 적용 (유지)
git stash apply stash@{0}

# 스태시 적용 후 삭제
git stash pop

# 스태시 삭제
git stash drop stash@{0}

# 모든 스태시 삭제
git stash clear
```

**사용 시점**: 브랜치 전환 필요, 긴급 작업 처리

### bisect

이진 탐색으로 버그 원인 커밋 찾기:

```bash
# 시작
git bisect start

# 현재 버전은 버그 있음
git bisect bad

# 이 버전은 정상이었음
git bisect good v1.0.0

# Git이 중간 커밋으로 체크아웃
# 테스트 후 결과 알려주기
git bisect good  # 또는 bad

# 반복하면 원인 커밋 찾음
# 완료 후 원래 상태로
git bisect reset
```

```
100개 커밋 중 버그 원인 찾기:
이진 탐색 → log₂(100) ≈ 7번만에 발견!
```

### reflog

HEAD 이동 기록 (복구용):

```bash
git reflog

# 출력 예시:
abc1234 HEAD@{0}: commit: Add feature
def5678 HEAD@{1}: checkout: moving from main to feature
ghi9012 HEAD@{2}: reset: moving to HEAD~1

# 실수로 reset --hard 했을 때 복구
git reset --hard HEAD@{2}
```

**reflog는 로컬 전용**, 30일간 보관

### blame

줄별 마지막 수정자 확인:

```bash
git blame file.txt

# 출력:
abc1234 (John 2024-01-01) function foo() {
def5678 (Jane 2024-01-02)   return bar;
ghi9012 (John 2024-01-03) }

# 특정 줄 범위만
git blame -L 10,20 file.txt
```

### log 고급

```bash
# 그래프로 보기
git log --oneline --graph --all

# 특정 파일 이력
git log --oneline -- path/to/file

# 검색
git log --grep="bug fix"

# 작성자로 필터
git log --author="John"

# 날짜 범위
git log --since="2024-01-01" --until="2024-12-31"
```

### clean

추적되지 않는 파일 삭제:

```bash
# 삭제될 파일 미리 보기
git clean -n

# 파일 삭제
git clean -f

# 디렉토리도 삭제
git clean -fd

# .gitignore 파일도 삭제
git clean -fdx
```

## 트레이드오프

| 명령어 | 안전성 | 히스토리 |
|--------|--------|---------|
| revert | 안전 | 보존 |
| reset --soft | 중간 | 변경 |
| reset --hard | 위험 | 변경 |
| cherry-pick | 안전 | 중복 가능 |

## 면접 예상 질문

### Q: reset과 revert의 차이는?

A: **reset**: 히스토리를 되돌림 (커밋 삭제). **revert**: 되돌리는 새 커밋 생성 (히스토리 보존). **선택**: 공유된 브랜치 → revert, 로컬 브랜치 → reset. **주의**: reset --hard는 복구 불가능.

### Q: cherry-pick은 언제 사용하나요?

A: (1) **핫픽스 적용**: main 버그 수정을 release 브랜치에도 적용 (2) **기능 선별**: feature 브랜치에서 특정 기능만 가져오기 (3) **실수 복구**: 잘못된 브랜치에 커밋한 것을 올바른 브랜치로 이동. **주의**: 동일 커밋이 여러 브랜치에 중복될 수 있음.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 기본 개념](./git-basics.md) | 선수 지식 | [2] 입문 |
| [Rebase vs Merge](./rebase-vs-merge.md) | 선수 지식 | [3] 중급 |

## 참고 자료

- [Pro Git Book](https://git-scm.com/book/ko/v2)
- [Git Documentation](https://git-scm.com/docs)
