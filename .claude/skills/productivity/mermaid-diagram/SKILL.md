---
name: mermaid-diagram
description: |
  Mermaid CLI를 사용하여 다이어그램을 생성합니다. 플로우차트, 시퀀스, ER, 클래스, 상태, 마인드맵, 간트, Git 그래프 등 20가지 유형을 지원합니다.
  "다이어그램 그려줘", "흐름도 만들어줘", "시퀀스 다이어그램", "ER 다이어그램", "상태 머신", "구조 시각화" 같은 요청뿐 아니라, 코드 흐름·API 호출·DB 관계를 설명하는 문맥에서도 자동 트리거됩니다.
  SVG 직접 생성(svg-diagram)보다 Mermaid를 먼저 고려하세요 — 자동 레이아웃으로 빠르게 결과를 얻을 수 있습니다. 정교한 픽셀 제어가 필요할 때만 svg-diagram을 사용하세요.
---

# Mermaid CLI 다이어그램 스킬

## 왜 Mermaid인가?

- **텍스트 기반**: 코드처럼 작성하고 Git으로 버전을 추적할 수 있다
- **자동 레이아웃**: 노드 좌표를 손으로 계산할 필요가 없다
- **빠른 피드백**: `.mmd` 파일 한 줄 수정으로 전체 다이어그램이 재생성된다
- **20가지 유형**: 플로우차트부터 C4 아키텍처까지 대부분의 상황을 커버한다

SVG를 직접 생성하는 방식(svg-diagram)은 픽셀 단위 레이아웃이나 커스텀 아이콘이 필요할 때만 선택한다.

---

## 언제 사용하는가?

| 상황 | 선택 |
|------|------|
| 플로우차트, 시퀀스, ER, 클래스, 상태, 간트, 마인드맵 등 표준 다이어그램 | Mermaid |
| 픽셀 단위 위치 제어, 커스텀 아이콘, 복잡한 그라디언트 | SVG 직접 생성 |

---

## 사전 요구사항

```bash
# Mermaid CLI 설치
npm install -g @mermaid-js/mermaid-cli

# 설치 확인
mmdc --version
```

---

## 파일 구조

```
cs/{category}/
├── {document}.md                    # 마크다운 문서
└── images/
    ├── {diagram-name}.mmd           # Mermaid 소스 (선택)
    └── {diagram-name}.svg           # 생성된 SVG
```

---

## 작업 절차

1. **코드 작성**: `.mmd` 파일 또는 마크다운 코드 블록에 Mermaid 문법 작성
2. **SVG 변환**: `mmdc -i diagram.mmd -o diagram.svg`
3. **파일 저장**: `cs/{category}/images/{diagram-name}.svg`
4. **마크다운에서 참조**: `![설명](./images/{diagram-name}.svg)`
5. **git add 실행**

CLI 옵션과 스타일링 상세는 [references/cli-usage.md](./references/cli-usage.md)를 Read 도구로 읽어서 참조하세요.

---

## 용도별 다이어그램 선택 가이드

| 용도 | 추천 다이어그램 |
|------|----------------|
| 로직/흐름 설명 | Flowchart |
| API/통신 흐름 | Sequence |
| 시스템 구조 | Architecture / C4 / Block |
| 개념 정리 | Mindmap |
| 클래스 관계 | Class Diagram |
| 상태 변화 | State Diagram |
| DB 스키마 | ER Diagram |
| 일정/타임라인 | Gantt / Timeline |
| 데이터 분포 | Pie Chart |
| Git 브랜치 | GitGraph |
| 사용자 경험 | User Journey |
| 기술 평가 | Quadrant Chart |
| 수치 데이터 | XY Chart |
| 작업 관리 | Kanban |
| 데이터 흐름 | Sankey |
| 요구사항 | Requirement |
| 패킷 구조 | Packet |

---

## 문법 레퍼런스

20가지 유형별 문법과 예제는 [references/syntax-guide.md](./references/syntax-guide.md)를 Read 도구로 읽어서 참조하세요.

---

## CLI 사용법

기본 변환, 설정 파일, 테마, 스타일링은 [references/cli-usage.md](./references/cli-usage.md)를 Read 도구로 읽어서 참조하세요.

---

## 주의사항

1. **파일명**: kebab-case 사용 (`network-flow.svg`, `order-state.svg`)
2. **위치**: `cs/{category}/images/` 폴더에 저장
3. **한글**: 일부 환경에서 폰트 렌더링 문제가 발생할 수 있으므로 테스트 필요
4. **복잡한 레이아웃**: 자동 배치가 원하는 결과가 아니면 svg-diagram 고려
5. **CLI 미설치 시**: svg-diagram 스킬로 대체

---

## 참고 자료

- [Mermaid 공식 문서](https://mermaid.js.org/)
- [Mermaid Live Editor](https://mermaid.live/)
- [Mermaid CS 문서](../../cs/automation/mermaid.md)
