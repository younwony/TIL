# Git 내부 동작 원리

> `[4] 심화` · 선수 지식: [Git 기본 개념](./git-basics.md)

> Git은 내부적으로 키-값 저장소 형태로 동작하며, 모든 데이터를 객체로 저장하고 SHA-1 해시로 식별한다.

`#Git내부` `#GitInternals` `#SHA1` `#해시` `#Hash` `#blob` `#tree` `#commit객체` `#CommitObject` `#reflog` `#packfile` `#GitObjects` `#index` `#StagingArea` `#HEAD` `#refs` `#브랜치포인터` `#태그` `#Tag` `#ContentAddressable` `#객체저장소` `#ObjectDatabase` `#gc` `#GarbageCollection` `#fsck` `#무결성` `#분산저장소` `#압축` `#Delta`

## 왜 알아야 하는가?

- **실무**: Git 문제 해결(손상된 커밋, reflog 복구, 객체 정리 등)을 위해서는 내부 동작을 알아야 합니다. "왜 이 브랜치가 이렇게 크지?", "삭제한 커밋 복구 가능한가?"같은 문제는 내부 구조를 모르면 해결할 수 없습니다.
- **면접**: "Git에서 브랜치란 무엇인가요?", "커밋은 어떻게 저장되나요?" 같은 심화 질문은 내부 구조 이해를 요구합니다. 단순 명령어 사용을 넘어 깊은 이해를 평가하는 지표입니다.
- **기반 지식**: Git의 효율성(왜 빠른가, 왜 공간을 적게 차지하는가), 데이터 무결성, 분산 시스템 동작 원리 등을 이해하는 기반이 됩니다. 이를 모르면 "왜 Git을 사용하는가?"에 대한 근본적인 답을 할 수 없습니다.

## 핵심 개념

- **Content-Addressable Storage**: 내용 기반 주소 지정 방식으로 데이터 저장
- **SHA-1 해시**: 40자리 16진수로 모든 객체를 고유하게 식별
- **Git 객체**: Blob, Tree, Commit, Tag 네 가지 타입의 객체
- **참조(Refs)**: 브랜치, 태그 등 커밋을 가리키는 포인터
- **.git 디렉토리**: Git 저장소의 모든 메타데이터와 객체 데이터베이스

## 쉽게 이해하기

**Git 내부 동작**을 도서관의 책 관리 시스템에 비유할 수 있습니다.

### Git 객체 = 도서관 카탈로그 시스템

| Git 객체 | 도서관 비유 |
|----------|------------|
| **Blob** | 책의 내용 (페이지들) |
| **Tree** | 책장 목록 (어떤 책이 어디에) |
| **Commit** | 도서관 상태 사진 + 설명 |
| **SHA-1 해시** | 바코드 번호 (고유 식별자) |

**왜 이런 구조인가?**
같은 내용의 책은 한 권만 보관하고, 여러 책장에서 참조합니다. (공간 절약!)

### 객체 저장 방식 = 창고 정리

```
책 "Hello World"를 보관:
1. 내용으로 바코드 생성: SHA-1("Hello World") = 8d0e41234f...
2. 창고에 저장: .git/objects/8d/0e41234f...
3. 똑같은 책이 또 오면? 이미 있으니 참조만 추가
```

**왜 내용으로 해시를 만드나?**
- 같은 파일은 자동으로 중복 제거
- 파일 변경 감지가 빠름 (해시만 비교)
- 데이터 무결성 보장 (해시가 다르면 손상됨)

## 상세 설명

### .git 디렉토리 구조

```
.git/
├── HEAD              # 현재 체크아웃된 브랜치 참조
├── config            # 저장소 설정
├── description       # GitWeb용 설명
├── hooks/            # 클라이언트/서버 훅 스크립트
├── info/             # .gitignore와 유사한 exclude 파일
├── objects/          # 모든 Git 객체 저장소
│   ├── pack/         # 압축된 객체 팩 파일
│   └── info/
├── refs/             # 브랜치, 태그 등 참조
│   ├── heads/        # 로컬 브랜치
│   ├── remotes/      # 원격 브랜치
│   └── tags/         # 태그
├── index             # Staging Area 정보
└── logs/             # 참조 변경 이력 (reflog)
```

### Git 객체 타입

#### 1. Blob (Binary Large Object)

파일의 내용을 저장하는 객체이다. 파일명이나 권한 정보는 포함하지 않는다.

```
┌─────────────────────────────┐
│           Blob              │
├─────────────────────────────┤
│ • 파일 내용만 저장           │
│ • 파일명, 경로 정보 없음     │
│ • 동일 내용 = 동일 해시      │
└─────────────────────────────┘
```

```bash
# Blob 객체 생성 (내부 동작)
echo "Hello, Git" | git hash-object -w --stdin
# 출력: 예) 8d0e41234f...

# Blob 내용 확인
git cat-file -p 8d0e41234f
# 출력: Hello, Git

# Blob 타입 확인
git cat-file -t 8d0e41234f
# 출력: blob
```

#### 2. Tree

디렉토리 구조를 표현하는 객체이다. Blob과 다른 Tree에 대한 참조를 포함한다.

```
┌─────────────────────────────────────────────┐
│                    Tree                      │
├─────────────────────────────────────────────┤
│ 100644 blob a1b2c3... README.md             │
│ 100644 blob d4e5f6... index.js              │
│ 040000 tree g7h8i9... src                   │
└─────────────────────────────────────────────┘
         │                      │
         ▼                      ▼
    ┌─────────┐          ┌─────────────────┐
    │  Blob   │          │      Tree       │
    │README.md│          │      (src)      │
    └─────────┘          ├─────────────────┤
                         │ 100644 blob ... │
                         │ App.js          │
                         └─────────────────┘
```

파일 모드:
- `100644`: 일반 파일
- `100755`: 실행 파일
- `120000`: 심볼릭 링크
- `040000`: 디렉토리 (Tree)

```bash
# Tree 객체 확인
git cat-file -p main^{tree}
# 출력:
# 100644 blob a1b2c3d4... README.md
# 040000 tree e5f6g7h8... src
```

#### 3. Commit

스냅샷(Tree)과 메타데이터를 저장하는 객체이다.

```
┌─────────────────────────────────────────────┐
│                   Commit                     │
├─────────────────────────────────────────────┤
│ tree      a1b2c3d4e5f6...                   │
│ parent    9i8h7g6f5e4d... (첫 커밋은 없음)  │
│ author    John <john@email.com> 1234567890  │
│ committer John <john@email.com> 1234567890  │
│                                             │
│ Commit message here                         │
└─────────────────────────────────────────────┘
         │
         ▼ (tree 참조)
    ┌─────────┐
    │  Tree   │
    │ (root)  │
    └─────────┘
```

```bash
# Commit 객체 확인
git cat-file -p HEAD
# 출력:
# tree 4b825dc642cb6eb9a060e54bf8d69288fbee4904
# parent 8d0e41234f5678...
# author John Doe <john@example.com> 1699000000 +0900
# committer John Doe <john@example.com> 1699000000 +0900
#
# Initial commit
```

#### 4. Tag (Annotated)

특정 커밋에 대한 영구적인 참조와 메타데이터를 저장한다.

```bash
# 태그 객체 생성
git tag -a v1.0 -m "Version 1.0 release"

# 태그 객체 확인
git cat-file -p v1.0
# 출력:
# object d4e5f6g7h8i9...
# type commit
# tag v1.0
# tagger John Doe <john@example.com> 1699000000 +0900
#
# Version 1.0 release
```

### 객체 저장 방식

Git은 객체를 압축하여 `.git/objects` 디렉토리에 저장한다.

```
SHA-1: 8d0e41234f5678901234567890abcdef12345678
저장 위치: .git/objects/8d/0e41234f5678901234567890abcdef12345678
                       ↑↑ ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                    처음 2자리가 디렉토리명, 나머지가 파일명
```

#### Pack 파일

객체가 많아지면 Git은 이를 Pack 파일로 압축한다.

```bash
# 수동으로 pack 파일 생성
git gc

# pack 파일 확인
ls .git/objects/pack/
# 출력:
# pack-1234567890abcdef.idx  # 인덱스 파일
# pack-1234567890abcdef.pack # 압축된 객체들
```

### 참조 (References)

브랜치와 태그는 단순히 커밋을 가리키는 포인터이다.

```
.git/refs/
├── heads/
│   ├── main      # 내용: d4e5f6g7h8i9... (커밋 해시)
│   └── feature   # 내용: a1b2c3d4e5f6...
├── remotes/
│   └── origin/
│       └── main  # 내용: d4e5f6g7h8i9...
└── tags/
    └── v1.0      # 내용: 태그 객체 해시 또는 커밋 해시
```

```bash
# 브랜치가 가리키는 커밋 확인
cat .git/refs/heads/main
# 출력: d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0

# HEAD 확인
cat .git/HEAD
# 출력: ref: refs/heads/main

# Detached HEAD 상태
git checkout d4e5f6g
cat .git/HEAD
# 출력: d4e5f6g7h8i9... (직접 커밋 해시를 가리킴)
```

### 커밋 생성 과정

```
1. git add file.txt
   ┌─────────────────────────────────────────┐
   │ file.txt 내용 → Blob 객체 생성          │
   │ index(Staging Area) 업데이트            │
   └─────────────────────────────────────────┘

2. git commit -m "message"
   ┌─────────────────────────────────────────┐
   │ index → Tree 객체 생성                   │
   │ Tree + parent + 메타데이터 → Commit 생성 │
   │ HEAD가 가리키는 브랜치 → 새 커밋으로 이동 │
   └─────────────────────────────────────────┘
```

### Index (Staging Area)

`.git/index` 파일은 바이너리 형식으로 스테이징 정보를 저장한다.

```bash
# Index 내용 확인
git ls-files --stage
# 출력:
# 100644 a1b2c3d4e5f6... 0    README.md
# 100644 g7h8i9j0k1l2... 0    src/App.js

# Index와 HEAD 비교
git diff --cached

# Index와 Working Directory 비교
git diff
```

### Reflog

참조의 변경 이력을 기록한다. 실수로 삭제한 커밋도 복구할 수 있다.

```bash
# reflog 확인
git reflog
# 출력:
# d4e5f6g HEAD@{0}: commit: Add feature
# a1b2c3d HEAD@{1}: checkout: moving from feature to main
# 9i8h7g6 HEAD@{2}: commit: Fix bug

# 삭제된 커밋 복구
git checkout HEAD@{2}
# 또는
git reset --hard HEAD@{2}
```

### 데이터 무결성

Git은 모든 객체를 SHA-1 해시로 식별하여 데이터 무결성을 보장한다.

```bash
# 저장소 무결성 검사
git fsck
# 출력:
# Checking object directories: 100% (256/256), done.
# Checking objects: 100% (1234/1234), done.

# dangling 객체 확인 (참조되지 않는 객체)
git fsck --unreachable
```

## 면접 예상 질문

- Q: Git에서 브랜치란 내부적으로 무엇인가요?
  - A: Git 브랜치는 내부적으로 특정 커밋을 가리키는 40바이트 파일입니다. `.git/refs/heads/` 디렉토리에 브랜치명으로 파일이 생성되고, 그 안에 커밋 해시가 저장됩니다. 이 때문에 브랜치 생성/삭제가 매우 가볍고 빠릅니다. **왜 이렇게 답해야 하나요?** SVN 같은 VCS는 브랜치를 전체 디렉토리 복사로 구현하여 무겁지만, Git은 포인터만 관리하므로 브랜치 수천 개를 만들어도 성능에 영향이 없습니다. 이것이 Git이 브랜치 전략을 적극 활용할 수 있는 이유입니다.

- Q: Git이 파일 변경을 추적하는 방식과 다른 VCS(예: SVN)와의 차이점은?
  - A: Git은 파일의 변경사항(delta)이 아닌 전체 스냅샷을 저장합니다. 동일한 파일은 같은 해시를 가지므로 중복 저장되지 않습니다. SVN은 변경사항만 저장하는 델타 방식을 사용합니다. Git의 방식은 브랜치 전환, 히스토리 탐색이 빠르고, 오프라인에서도 모든 작업이 가능합니다. **왜 이렇게 답해야 하나요?** 스냅샷 방식은 언뜻 비효율적으로 보이지만, 콘텐츠 해싱으로 중복을 제거하고 Pack 파일로 압축하여 실제로는 공간 효율적입니다. 또한 각 커밋이 완전한 상태를 가져 손상에 강하고 병렬 작업에 유리합니다.

- Q: git gc는 무엇을 하며, 언제 실행되나요?
  - A: `git gc`(Garbage Collection)는 느슨한 객체들을 Pack 파일로 압축하고, 참조되지 않는 객체를 정리합니다. Git은 자동으로 적절한 시점에 실행하며(약 7000개의 느슨한 객체, 또는 50개 이상의 팩 파일), 수동으로도 실행할 수 있습니다. 저장소 크기를 줄이고 성능을 향상시킵니다. **왜 이렇게 답해야 하나요?** 개별 객체 파일은 I/O 오버헤드가 크므로, delta 압축된 Pack 파일로 변환하면 저장 공간이 90% 이상 줄고 네트워크 전송 속도도 빨라집니다. 대규모 저장소에서는 필수적인 최적화입니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [Git 기본 개념](./git-basics.md) | Git 내부 동작의 기초가 되는 개념 (선수 지식) | Beginner |
| [Git 브랜치 전략](./git-branch-strategy.md) | 브랜치의 내부 구조 이해를 통한 전략 활용 | Intermediate |

## 참고 자료

- [Pro Git Book - Git Internals](https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain)
- [Git 공식 문서 - Git Objects](https://git-scm.com/book/en/v2/Git-Internals-Git-Objects)
- [Git 공식 문서 - Git References](https://git-scm.com/book/en/v2/Git-Internals-Git-References)
