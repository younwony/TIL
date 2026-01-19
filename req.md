# Mermaid 활용한 이미지 생성 분석

## 개요

Mermaid 다이어그램을 이미지(SVG/PNG/PDF)로 생성하는 두 가지 방법을 분석합니다.

| 방법 | 설명 | 사용 시점 |
|------|------|----------|
| **Mermaid CLI** | 직접 명령어로 변환 | 로컬 빌드, CI/CD 파이프라인 |
| **Mermaid MCP** | Claude와 연동하여 AI가 다이어그램 생성 | AI 기반 문서 작성 |

---

## 1. Mermaid CLI

> 공식 문서: https://github.com/mermaid-js/mermaid-cli

### 설치 방법

```bash
# 전역 설치 (권장)
npm install -g @mermaid-js/mermaid-cli

# npx로 바로 실행 (설치 없이)
npx -p @mermaid-js/mermaid-cli mmdc -h

# Docker 사용
docker pull minlag/mermaid-cli
```

### 기본 사용법

```bash
# 기본 변환 (SVG)
mmdc -i input.mmd -o output.svg

# PNG로 변환
mmdc -i input.mmd -o output.png

# PDF로 변환
mmdc -i input.mmd -o output.pdf
```

### 주요 옵션

| 옵션 | 설명 | 예시 |
|------|------|------|
| `-i, --input` | 입력 파일 (`.mmd`) | `-i diagram.mmd` |
| `-o, --output` | 출력 파일 | `-o diagram.svg` |
| `-t, --theme` | 테마 (default, forest, dark, neutral) | `-t dark` |
| `-b, --backgroundColor` | 배경색 | `-b transparent` |
| `-w, --width` | 너비 (px) | `-w 800` |
| `-H, --height` | 높이 (px) | `-H 600` |
| `-s, --scale` | 스케일 | `-s 2` |
| `--configFile` | Mermaid 설정 파일 | `--configFile config.json` |
| `--cssFile` | 커스텀 CSS | `--cssFile custom.css` |

### 실전 예제

```bash
# 다크 테마 + 투명 배경 PNG
mmdc -i flowchart.mmd -o flowchart.png -t dark -b transparent

# 고해상도 이미지 (2배 스케일)
mmdc -i sequence.mmd -o sequence.png -s 2

# stdin 입력 사용
cat diagram.mmd | mmdc --input - -o output.svg

# 마크다운 내 mermaid 블록 자동 변환
mmdc -i readme.template.md -o readme.md
```

### 프로젝트 내 활용 (TIL/cs 문서)

```bash
# images 디렉토리에 SVG 생성
mkdir -p cs/{category}/images
mmdc -i cs/{category}/images/diagram.mmd \
     -o cs/{category}/images/diagram.svg \
     -b transparent
```

---

## 2. Mermaid MCP Server

> Claude와 연동하여 자연어로 다이어그램 생성

### 추천 MCP 서버 비교

| 서버 | 특징 | 설치 방법 |
|------|------|----------|
| **[@peng-shawn/mermaid-mcp-server](https://github.com/peng-shawn/mermaid-mcp-server)** | PNG/SVG 변환, Smithery 지원 | `npx @smithery/cli install` |
| **[claude-mermaid](https://github.com/veelenga/claude-mermaid)** | 라이브 리로드, 브라우저 미리보기 | `npm install -g` |
| **[mcp-mermaid](https://mcpservers.org/servers/hustcc/mcp-mermaid)** | base64/svg/file 출력, 검증 기능 | `npx mcp-mermaid` |
| **[Sailor](https://github.com/aj-geddes/sailor)** | 웹 UI + MCP 통합 | Python 기반 |

### 설치 방법 1: @peng-shawn/mermaid-mcp-server (권장)

**Smithery로 자동 설치:**
```bash
npx -y @smithery/cli install @peng-shawn/mermaid-mcp-server --client claude
```

**수동 설정 (claude_desktop_config.json):**
```json
{
  "mcpServers": {
    "mermaid": {
      "command": "npx",
      "args": ["-y", "@peng-shawn/mermaid-mcp-server"]
    }
  }
}
```

### 설치 방법 2: claude-mermaid (라이브 프리뷰)

```bash
git clone https://github.com/veelenga/claude-mermaid.git
cd claude-mermaid
npm install && npm run build && npm install -g .
```

**특징:**
- 브라우저에서 실시간 미리보기
- 편집 시 자동 리로드
- SVG/PNG/PDF 저장
- Pan & Zoom 인터랙티브 뷰

### 설치 방법 3: mcp-mermaid (간단 설정)

**Claude Desktop 설정:**
```json
{
  "mcpServers": {
    "mcp-mermaid": {
      "command": "npx",
      "args": ["-y", "mcp-mermaid"]
    }
  }
}
```

**출력 타입:**
- `base64` - 이미지 데이터
- `svg` - SVG 코드
- `file` - PNG 파일 자동 저장

---

## 3. 비교 및 선택 가이드

| 기준 | CLI | MCP |
|------|-----|-----|
| **자동화** | CI/CD, 스크립트 | AI 대화 기반 |
| **편집** | 직접 .mmd 파일 수정 | 자연어로 요청 |
| **미리보기** | 없음 (별도 도구 필요) | 라이브 프리뷰 가능 |
| **배치 처리** | 우수 | 제한적 |
| **유연성** | 높음 | 중간 |

### 권장 사용 시나리오

1. **CS 문서 작성 시 (TIL 프로젝트)**
   - `mermaid-diagram` 스킬과 CLI 조합 사용
   - `.mmd` 파일 작성 → `mmdc`로 SVG 변환 → git add

2. **AI 기반 다이어그램 생성**
   - MCP 서버 설치 후 Claude에게 다이어그램 요청
   - "시퀀스 다이어그램으로 로그인 플로우 그려줘"

3. **CI/CD 파이프라인**
   - CLI로 마크다운 내 mermaid 블록 자동 변환
   - 빌드 시 이미지 생성

---

## 4. 다음 단계

- [ ] Mermaid CLI 전역 설치 확인
- [ ] MCP 서버 선택 및 설치
- [ ] Claude Desktop/Claude Code 설정 테스트
- [ ] 기존 `mermaid-diagram` 스킬과 통합 검토

---

## 참고 자료

- [Mermaid 공식 문서](https://mermaid.js.org/intro/)
- [Mermaid CLI GitHub](https://github.com/mermaid-js/mermaid-cli)
- [@peng-shawn/mermaid-mcp-server](https://github.com/peng-shawn/mermaid-mcp-server)
- [claude-mermaid](https://github.com/veelenga/claude-mermaid)
- [mcp-mermaid](https://mcpservers.org/servers/hustcc/mcp-mermaid)
- [Sailor](https://github.com/aj-geddes/sailor)
