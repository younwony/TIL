# curl 폴백 프로세스

MCP 도구(`mcp__atlassian__*`)가 네트워크 오류를 반환할 때 curl로 직접 Atlassian REST API를 호출하는 절차입니다.

---

## 폴백 전환 기준

MCP 도구 호출 시 `Network error occurred`, `timeout`, `connection refused` 등의 오류가 **2회 이상** 발생하면 curl 폴백으로 전환합니다. 1회는 일시적 오류일 수 있으므로 재시도합니다.

---

## 인증 정보

```
Base URL (Jira): https://temcolabs.atlassian.net/rest/api/3
Base URL (Confluence): https://temcolabs.atlassian.net/wiki/api/v2
인증: Basic Auth (wonhee.youn@temco.io:{API_TOKEN})
```

API 토큰은 `.claude.json`의 `mcpServers.atlassian.env.ATLASSIAN_API_TOKEN`에서 읽어옵니다. 하드코딩 금지.

---

## Step 1: Jira 이슈 조회

```bash
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/rest/api/3/issue/{TECH-XXXXX}"
```

---

## Step 2: 부모 페이지 확인 (기본: WORK-LOG)

```bash
# WORK-LOG 페이지 존재 확인
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages/3255435270"
```

- 성공 -> parentId: 3255435270
- 404 응답 -> WORK-LOG 페이지를 홈페이지(1983742135) 하위에 재생성

---

## Step 3: 기존 작업 로그 페이지 확인

```bash
curl -s -u "{EMAIL}:{API_TOKEN}" \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages?spaceId=1983741954&title={TECH-XXXXX}&limit=1"
```

- `results` 배열이 비어있으면 -> 신규 생성 (Step 4a)
- `results` 배열에 데이터가 있으면 -> 업데이트 (Step 4b, pageId와 version 추출)

---

## Step 4a: 신규 페이지 생성

```bash
curl -s -X POST -H "Content-Type: application/json" \
  -u "{EMAIL}:{API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages"
```

JSON 구조:
```json
{
  "spaceId": "1983741954",
  "parentId": "{부모 페이지 ID, 기본: 3255435270}",
  "status": "current",
  "title": "{TECH-XXXXX}",
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

---

## Step 4b: 기존 페이지 업데이트

```bash
curl -s -X PUT -H "Content-Type: application/json" \
  -u "{EMAIL}:{API_TOKEN}" \
  -d @/tmp/confluence_page.json \
  "https://temcolabs.atlassian.net/wiki/api/v2/pages/{pageId}"
```

JSON 구조:
```json
{
  "id": "{pageId}",
  "status": "current",
  "title": "{TECH-XXXXX}",
  "version": {
    "number": "{현재버전 + 1}",
    "message": "작업 내용 업데이트"
  },
  "body": {
    "representation": "storage",
    "value": "{Confluence Storage Format HTML}"
  }
}
```

---

## 주의사항

- JSON 파일은 `/tmp/confluence_page.json`에 임시 저장 후 전송합니다. JSON 내 특수문자(따옴표, 꺽쇠 등)를 적절히 이스케이프하세요.
- bodyValue는 **Confluence Storage Format (XML)** 으로 작성합니다. wiki markup이나 markdown은 사용하지 마세요.
- curl 응답에서 `_links.webui`를 추출하여 사용자에게 페이지 URL을 제공합니다.
