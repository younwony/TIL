# 트라이 (Trie)

> `[4] 심화` · 선수 지식: [트리](./tree.md)

> 문자열을 효율적으로 저장하고 검색하기 위한 트리 기반 자료구조

`#트라이` `#Trie` `#접두사트리` `#PrefixTree` `#문자열검색` `#StringSearch` `#자동완성` `#Autocomplete` `#사전` `#Dictionary` `#접두사검색` `#PrefixSearch` `#래딕스트리` `#RadixTree` `#압축트라이` `#검색엔진` `#SearchEngine` `#IP라우팅` `#IPRouting` `#문자열매칭` `#LCP` `#LongestCommonPrefix`

## 왜 알아야 하는가?

트라이는 문자열 검색에 특화된 자료구조입니다. 자동완성, 사전 검색, IP 라우팅 등에 사용됩니다. 코딩 테스트에서 문자열 문제, 특히 접두사 관련 문제에 자주 출제됩니다.

## 핵심 개념

- **노드**: 문자 하나를 저장
- **루트**: 빈 문자열 (시작점)
- **경로**: 루트에서 노드까지 = 문자열
- **종료 표시**: 단어의 끝을 표시하는 플래그

## 쉽게 이해하기

**트라이**를 전화번호부 색인에 비유할 수 있습니다.

"김", "김철", "김철수", "박", "박영희"를 찾으려면:
1. 첫 글자 "김"으로 시작하는 섹션으로 이동
2. 그 안에서 "철"로 시작하는 부분으로
3. 그 안에서 "수"를 찾음

각 글자가 다음 글자로 가는 경로가 되는 트리 구조입니다.

## 상세 설명

### 트라이 구조

```
"app", "apple", "api", "bat" 저장

          root
         /    \
        a      b
       /        \
      p          a
     / \          \
    p   i*         t*
   /
  l
 /
e*

* = 단어의 끝 (isEnd = true)
```

### 트라이 노드 구조

```java
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}
```

또는 고정 크기 배열 (알파벳 소문자만):

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
}
```

### 주요 연산

**1. 삽입 (Insert)**

```java
void insert(String word) {
    TrieNode current = root;
    for (char c : word.toCharArray()) {
        current.children.putIfAbsent(c, new TrieNode());
        current = current.children.get(c);
    }
    current.isEndOfWord = true;
}
```

**2. 검색 (Search)**

```java
boolean search(String word) {
    TrieNode node = searchNode(word);
    return node != null && node.isEndOfWord;
}

private TrieNode searchNode(String word) {
    TrieNode current = root;
    for (char c : word.toCharArray()) {
        if (!current.children.containsKey(c)) {
            return null;
        }
        current = current.children.get(c);
    }
    return current;
}
```

**3. 접두사 검색 (Starts With)**

```java
boolean startsWith(String prefix) {
    return searchNode(prefix) != null;
}
```

### 트라이 vs 다른 자료구조

| 연산 | 트라이 | 해시 맵 | BST |
|------|-------|--------|-----|
| 삽입 | O(m) | O(m) | O(m log n) |
| 검색 | O(m) | O(m) | O(m log n) |
| 접두사 검색 | O(m) | O(n*m) | O(m log n) |
| 정렬된 순회 | O(n) | 불가 | O(n) |

*m = 문자열 길이, n = 저장된 문자열 수*

**트라이의 장점**: 접두사 검색이 O(m)으로 매우 빠름

### 자동완성 구현

```java
List<String> autocomplete(String prefix) {
    List<String> results = new ArrayList<>();
    TrieNode node = searchNode(prefix);

    if (node == null) return results;

    dfs(node, new StringBuilder(prefix), results);
    return results;
}

void dfs(TrieNode node, StringBuilder current, List<String> results) {
    if (node.isEndOfWord) {
        results.add(current.toString());
    }

    for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
        current.append(entry.getKey());
        dfs(entry.getValue(), current, results);
        current.deleteCharAt(current.length() - 1);
    }
}
```

### 압축 트라이 (Radix Tree)

공간 효율을 위해 단일 자식 노드를 합침:

```
일반 트라이:           압축 트라이:
    root                 root
      |                    |
      r                   rom
      |                  /   \
      o                 an    ulus
      |
      m
     / \
    a   u
    |   |
    n   l
        |
        u
        |
        s
```

## 트레이드오프

| 장점 | 단점 |
|------|------|
| 접두사 검색 O(m) | 메모리 사용량 많음 |
| 사전순 정렬 자연스럽게 | 해시 테이블보다 단순 검색 느림 |
| 최장 공통 접두사 찾기 용이 | 구현 복잡 |

## 면접 예상 질문

### Q: 트라이를 사용하면 좋은 상황은?

A: (1) **자동완성**: 접두사로 시작하는 모든 단어 빠르게 찾기 (2) **사전 검색**: 단어 존재 여부 확인 (3) **IP 라우팅**: 가장 긴 접두사 매칭 (4) **문자열 집합 연산**: 공통 접두사 찾기. **핵심**: 접두사 기반 검색이 필요할 때 트라이가 효율적입니다.

### Q: 트라이의 공간 복잡도를 줄이는 방법은?

A: (1) **압축 트라이 (Radix Tree)**: 단일 자식 노드들을 하나로 합침 (2) **Map 대신 배열**: 문자 집합이 제한적일 때 (3) **비트 트라이**: 비트 단위로 저장 (4) **Double-Array Trie**: 배열 두 개로 압축 저장.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [트리](./tree.md) | 선수 지식 | [3] 중급 |
| [해시 테이블](./hash-table.md) | 비교 자료구조 | [3] 중급 |

## 참고 자료

- Introduction to Algorithms (CLRS)
- [Trie - Wikipedia](https://en.wikipedia.org/wiki/Trie)
