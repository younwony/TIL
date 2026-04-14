---
description: EC2 서버 상태 점검 — /ssh-server-inspect [qa|prod] [서버명] [항목]
argument-hint: "[qa|prod] [batch|elastic|product-engine7|...] [process|memory|disk|log|all]"
---

# SSH 서버 점검

`ssh-server-inspect` 스킬을 사용하여 EC2 서버 상태를 점검한다.

## 인자 파싱

$ARGUMENTS 를 아래 규칙으로 파싱한다.

| 위치 | 값 | 설명 |
|------|-----|------|
| 1번째 | `qa` 또는 `prod` (생략 시 `prod`) | 환경 |
| 2번째 | 서버 별칭 (생략 시 `kglowing-batch`) | 접속 대상 |
| 3번째 | `process\|memory\|disk\|log\|all` (생략 시 `all`) | 수집 항목 |

### 환경별 서버 매핑

**prod 환경:**

| 별칭 | 실제 호스트 | 접속 방식 |
|------|-----------|---------|
| `batch`, `kglowing-batch` | kglowing-batch | 방식 B (go) |
| `elastic`, `kglowing-elastic` | kglowing-elastic | 방식 D (ProxyCommand) |
| `product-engine7`, `pe7` | kglowing-batch | 방식 B (go) |

**qa 환경:**

| 별칭 | 실제 호스트 | 접속 방식 |
|------|-----------|---------|
| `settle`, `qa-settle` | qa-settle | 방식 A (직접) |

## 실행

파싱된 인자를 바탕으로 `ssh-server-inspect` 스킬을 호출한다.
스킬의 `references/servers.md`에서 키 경로와 접속 정보를 읽어 사용한다.

## 사용 예시

```
/ssh-server-inspect                          → prod kglowing-batch all
/ssh-server-inspect prod batch memory        → prod kglowing-batch memory
/ssh-server-inspect prod elastic disk        → prod kglowing-elastic disk
/ssh-server-inspect qa settle log            → qa qa-settle log
/ssh-server-inspect prod pe7 process         → prod kglowing-batch process
```
