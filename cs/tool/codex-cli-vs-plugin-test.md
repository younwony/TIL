# Codex CLI vs Plugin 검증 테스트

> 이 문서는 Claude Code에서 Codex를 호출할 때 CLI/Plugin 중 어떤 방식이 사용되는지
> 판정하기 위한 절차와 기준을 정리한 문서입니다.

## 배경

- Claude Code는 Codex를 Plugin(`/codex:rescue`) 또는 CLI(`codex exec -`)로 호출 가능
- Plugin이 설치되어 있으면 Plugin 우선 사용이 원칙
- 이 문서는 현재 환경에서 어떤 방식이 실제로 사용되는지 확인하는 절차를 정의

## 전제조건 확인

- [ ] Codex CLI 설치: 터미널에서 `codex --version` 실행 → 버전 출력 확인
- [ ] Codex Plugin 설치: Plugin 캐시 파일 존재 확인
  - 경로: `~/.claude/plugins/cache/openai-codex/codex/1.0.0/scripts/codex-companion.mjs`
- [ ] API 키 설정: 환경변수 또는 설정 파일 확인

## 판정 기준

### 기능 존재 확인 (설치 여부)

| 방식 | 확인 방법 | 기대 결과 |
|------|----------|----------|
| CLI | 터미널에서 `codex --version` 실행 | 버전 번호 출력 |
| Plugin | Plugin 캐시 파일 존재 확인 | 파일 존재 |

### 실제 사용 방식 판정

| 방식 | 호출 형태 | 출력 특징 |
|------|----------|----------|
| Plugin | `/codex:rescue` 슬래시 커맨드 | Claude Code UI 요소 동반 (진행 바, 상태 메시지) |
| CLI | `echo "test" \| codex exec -` 파이프 실행 | 순수 텍스트 출력 |

## 검증 절차

1. **Plugin 테스트**: `/codex:rescue 간단한 테스트` 실행
   - 성공 증거: 슬래시 커맨드가 인식되고 Codex 응답 수신
   - 실패 증거: 명령어 미인식 또는 에러
2. **CLI 테스트**: `echo "hello" | codex exec -` 실행
   - 성공 증거: Codex가 응답을 텍스트로 반환
   - 실패 증거: command not found 또는 타임아웃
3. **우선순위 판정**: 둘 다 가능할 때 Claude Code가 어떤 것을 먼저 시도하는지 확인
   - CLAUDE.md의 "Plugin 우선" 규칙과 실제 동작 일치 여부

## 검증 결과

| 항목 | 결과 | 증거 |
|------|------|------|
| Plugin 설치 여부 | | |
| Plugin 실행 성공 | | |
| CLI 설치 여부 | | |
| CLI 실행 성공 | | |
| 우선 사용 방식 | | |
| 판정 | | |

### 판정 규칙

- 둘 다 성공 → CLAUDE.md 규칙에 따라 Plugin 우선
- Plugin만 성공 → Plugin 사용
- CLI만 성공 → CLI 사용
- 둘 다 실패 → 판정 불가 (환경 설정 점검 필요)
