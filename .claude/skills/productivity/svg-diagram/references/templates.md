# SVG 다이어그램 템플릿 참조

## 1. 그라디언트 정의 템플릿

```xml
<defs>
  <linearGradient id="nodeBlue" x1="0%" y1="0%" x2="0%" y2="100%">
    <stop offset="0%" style="stop-color:#3498DB"/>
    <stop offset="100%" style="stop-color:#2980B9"/>
  </linearGradient>
  <linearGradient id="nodeGreen" x1="0%" y1="0%" x2="0%" y2="100%">
    <stop offset="0%" style="stop-color:#2ECC71"/>
    <stop offset="100%" style="stop-color:#27AE60"/>
  </linearGradient>
  <linearGradient id="nodeRed" x1="0%" y1="0%" x2="0%" y2="100%">
    <stop offset="0%" style="stop-color:#E74C3C"/>
    <stop offset="100%" style="stop-color:#C0392B"/>
  </linearGradient>
  <linearGradient id="nodePurple" x1="0%" y1="0%" x2="0%" y2="100%">
    <stop offset="0%" style="stop-color:#9B59B6"/>
    <stop offset="100%" style="stop-color:#8E44AD"/>
  </linearGradient>
  <linearGradient id="nodeOrange" x1="0%" y1="0%" x2="0%" y2="100%">
    <stop offset="0%" style="stop-color:#E67E22"/>
    <stop offset="100%" style="stop-color:#D35400"/>
  </linearGradient>
</defs>
```

## 2. 화살표 마커 정의

```xml
<defs>
  <marker id="arrowhead" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
    <polygon points="0 0, 10 3.5, 0 7" fill="#7F8C8D"/>
  </marker>
  <marker id="arrowRed" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
    <polygon points="0 0, 10 3.5, 0 7" fill="#E74C3C"/>
  </marker>
  <marker id="arrowGreen" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
    <polygon points="0 0, 10 3.5, 0 7" fill="#27AE60"/>
  </marker>
</defs>
```

## 3. 요소 스타일 규칙

### 원형 노드 (트리, 그래프)

```xml
<circle cx="150" cy="50" r="18" fill="url(#nodeBlue)"/>
<text x="150" y="55" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="14" font-weight="bold">1</text>
```

### 사각형 박스 (프레임, 블록)

```xml
<rect x="20" y="30" width="120" height="35" rx="5" fill="url(#nodeBlue)"/>
<text x="80" y="53" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="12" font-weight="bold">텍스트</text>
```

### 연결선

```xml
<!-- 일반 선 -->
<line x1="50" y1="50" x2="150" y2="50" stroke="#7F8C8D" stroke-width="2"/>

<!-- 화살표 선 -->
<line x1="50" y1="50" x2="150" y2="50" stroke="#7F8C8D" stroke-width="2" marker-end="url(#arrowhead)"/>

<!-- 점선 -->
<line x1="50" y1="50" x2="150" y2="50" stroke="#BDC3C7" stroke-width="2" stroke-dasharray="5,5"/>
```

### 텍스트

```xml
<!-- 타이틀 -->
<text x="200" y="25" text-anchor="middle" fill="#2C3E50" font-family="Arial, sans-serif" font-size="14" font-weight="bold">제목</text>

<!-- 부제 -->
<text x="200" y="40" text-anchor="middle" fill="#7F8C8D" font-family="Arial, sans-serif" font-size="10">부제목</text>

<!-- 레이블 -->
<text x="100" y="80" fill="#7F8C8D" font-family="Arial, sans-serif" font-size="9">레이블</text>
```

---

## 4. 다이어그램 유형별 템플릿

### 4-1. 트리 다이어그램

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 300 200">
  <defs>
    <linearGradient id="nodeBlue" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#3498DB"/>
      <stop offset="100%" style="stop-color:#2980B9"/>
    </linearGradient>
  </defs>

  <!-- 간선 먼저 그리기 -->
  <line x1="150" y1="35" x2="80" y2="85" stroke="#7F8C8D" stroke-width="2"/>
  <line x1="150" y1="35" x2="220" y2="85" stroke="#7F8C8D" stroke-width="2"/>

  <!-- 노드 그리기 -->
  <circle cx="150" cy="25" r="18" fill="url(#nodeBlue)"/>
  <text x="150" y="30" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="14" font-weight="bold">1</text>
</svg>
```

### 4-2. 계층 테이블 (OSI 스타일)

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 200">
  <defs>
    <linearGradient id="layer1" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#E74C3C"/>
      <stop offset="100%" style="stop-color:#C0392B"/>
    </linearGradient>
  </defs>

  <!-- 타이틀 -->
  <text x="250" y="20" text-anchor="middle" fill="#2C3E50" font-family="Arial, sans-serif" font-size="14" font-weight="bold">계층 구조</text>

  <!-- 행 -->
  <rect x="20" y="35" width="40" height="25" fill="url(#layer1)" rx="3"/>
  <text x="40" y="52" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="11" font-weight="bold">1</text>
</svg>
```

### 4-3. 시퀀스/핸드셰이크 다이어그램

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 500 300">
  <defs>
    <linearGradient id="clientGrad" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#3498DB"/>
      <stop offset="100%" style="stop-color:#2980B9"/>
    </linearGradient>
    <marker id="arrowR" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
      <polygon points="0 0, 10 3.5, 0 7" fill="#E74C3C"/>
    </marker>
  </defs>

  <!-- 엔티티 -->
  <rect x="80" y="40" width="80" height="30" fill="url(#clientGrad)" rx="5"/>
  <text x="120" y="60" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="12" font-weight="bold">Client</text>

  <!-- 세로선 -->
  <line x1="120" y1="70" x2="120" y2="280" stroke="#3498DB" stroke-width="3"/>

  <!-- 메시지 화살표 -->
  <line x1="120" y1="100" x2="370" y2="100" stroke="#E74C3C" stroke-width="2" marker-end="url(#arrowR)"/>
  <rect x="200" y="85" width="100" height="22" fill="#E74C3C" rx="3"/>
  <text x="250" y="100" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="10" font-weight="bold">SYN</text>
</svg>
```

### 4-4. 아키텍처/시스템 구성도

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 600 400">
  <defs>
    <linearGradient id="lbGrad" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#E74C3C"/>
      <stop offset="100%" style="stop-color:#C0392B"/>
    </linearGradient>
    <marker id="arrow" markerWidth="10" markerHeight="7" refX="9" refY="3.5" orient="auto">
      <polygon points="0 0, 10 3.5, 0 7" fill="#7F8C8D"/>
    </marker>
  </defs>

  <!-- Load Balancer -->
  <rect x="200" y="50" width="200" height="40" fill="url(#lbGrad)" rx="5"/>
  <text x="300" y="75" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="12" font-weight="bold">Load Balancer</text>

  <!-- 화살표 -->
  <line x1="300" y1="90" x2="300" y2="130" stroke="#7F8C8D" stroke-width="2" marker-end="url(#arrow)"/>
</svg>
```

### 4-5. 상태 다이어그램 (서킷브레이커 스타일)

```xml
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 450 280">
  <defs>
    <linearGradient id="closedGrad" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#2ECC71"/>
      <stop offset="100%" style="stop-color:#27AE60"/>
    </linearGradient>
    <linearGradient id="openGrad" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" style="stop-color:#E74C3C"/>
      <stop offset="100%" style="stop-color:#C0392B"/>
    </linearGradient>
  </defs>

  <!-- 상태 원 -->
  <circle cx="100" cy="100" r="45" fill="url(#closedGrad)"/>
  <text x="100" y="105" text-anchor="middle" fill="white" font-family="Arial, sans-serif" font-size="11" font-weight="bold">Closed</text>

  <!-- 전이 화살표 (곡선) -->
  <path d="M 145 85 Q 225 40 305 85" fill="none" stroke="#E74C3C" stroke-width="2"/>
</svg>
```
