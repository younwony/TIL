# Domain Docs — TIL 환경

`improve-codebase-architecture`, `diagnose`, `tdd` 같은 skill이 도메인 언어와 결정을 읽는 위치 정의.

## 컨텍스트 구조

**Single-context** (단일 컨텍스트).

```
TIL/
├── .claude/
│   ├── CONTEXT.md                          ← ubiquitous language (단일)
│   └── docs/
│       └── adr/
│           ├── 0001-skill-dependency-classification.md
│           ├── 0002-track-as-skill-container.md
│           └── 0003-bucket-structure.md
└── src/  (또는 cs/, study/, cs-web/)
```

TIL은 모노레포가 아니다. 모든 영역(cs/, study/, cs-web/)이 하나의 도메인 컨텍스트를 공유한다.

## CONTEXT.md 위치

`.claude/CONTEXT.md` (글로벌 1개).

TIL 고유 용어 (Track, WORK-SPEC, Phase, Sub-track, FEATURE-CHECKLIST 등)를 정의한다.

용어 추가/수정은 `/grill-with-docs` 세션 또는 직접 편집. 양식: `engineering/grill-with-docs/CONTEXT-FORMAT.md`.

## ADR 위치

`.claude/docs/adr/`.

현재 ADR 목록:
- `0001-skill-dependency-classification.md` — Hard/Soft 의존성 분리
- `0002-track-as-skill-container.md` — Track을 atomic skill 컨테이너로 재정의
- `0003-bucket-structure.md` — skills/ 6-bucket 구조

새 ADR 작성 시:
1. 다음 번호 부여 (0004, 0005, ...)
2. 양식: `engineering/grill-with-docs/ADR-FORMAT.md`
3. 작성 시점에 Status = "Accepted"

## Skill 읽기 규칙

`improve-codebase-architecture`, `diagnose`, `tdd`, `to-prd`, `to-issues`, `triage`, `grill-with-docs`는 다음 순서로 도메인 정보를 읽는다:

1. **CONTEXT.md** — 사용할 어휘 결정
2. **건드릴 영역의 ADR** — 재논의하지 말 결정 인지
3. **코드** — 실제 구현 확인

## CONTEXT.md / ADR 갱신 권한

- **read**: 모든 skill
- **write**: `/grill-with-docs`, `/improve-codebase-architecture` (인라인 갱신 권한)
- **새 ADR 작성**: 위 두 skill + 사용자가 직접
- **삭제**: 사용자만 (사람 판단 필요)

## 관련

- [issue-tracker.md](./issue-tracker.md) — Jira 호출 패턴
- [triage-labels.md](./triage-labels.md) — 라벨 매핑
- `.claude/CONTEXT.md` — ubiquitous language
- `.claude/docs/adr/` — 결정 기록
