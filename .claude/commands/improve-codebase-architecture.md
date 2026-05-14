---
description: Improve Codebase Architecture - shallow→deep module 리팩터링 기회 탐색
allowed-tools: Read, Edit, Glob, Grep, Bash, Agent
---

# Improve Codebase Architecture

`improve-codebase-architecture` skill을 호출하여 코드베이스의 deepening opportunity(shallow→deep module 리팩터링 기회)를 찾아줘.

도구: deletion test, Module/Interface/Seam/Depth/Leverage/Locality 어휘.
CONTEXT.md의 도메인 어휘 + ADR을 입력으로 사용.

상세 절차: `engineering/improve-codebase-architecture/SKILL.md` 참조.

## 실행 방법

```
/improve-codebase-architecture                   # 코드베이스 전반
/improve-codebase-architecture src/foo/         # 특정 영역
```

## 사용 빈도

Matt Pocock 권장: **며칠에 한 번씩** 코드베이스에 적용. 진흙공이 되기 전에 design care.
