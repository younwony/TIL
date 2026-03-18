# 프로젝트 CLAUDE.md 추가 내용

> 이 파일의 내용을 기존 회사 프로젝트의 CLAUDE.md에 병합하세요.
> 이미 존재하는 규칙과 중복되지 않도록 확인 후 추가합니다.

---

## 코드 작성 규칙

- TDD 필수, 매직 넘버/문자열 금지, SRP/DRY 준수
- 상세 규칙 참조: [CODE-RULES.md](./.claude/docs/CODE-RULES.md)

## Claude Code Skills

### Skills (자동 트리거)

| 스킬 | 설명 |
|------|------|
| `smart-session` | 세션 시작→작업 실행→세션 종료 3단계 워크플로우 오케스트레이터 |
| `mermaid-diagram` | Mermaid CLI 20가지 다이어그램 (플로우차트, 시퀀스, ER, 클래스 등) |
| `svg-diagram` | 정밀 레이아웃 SVG (패킷 구조, 계층 다이어그램, 네트워크 토폴로지) |
| `3ai-plan` | Claude+Gemini+Codex 3-AI 교차 검토로 플랜 구체화 |

> description이 트리거 역할을 하므로 별도 트리거 키워드 불필요
