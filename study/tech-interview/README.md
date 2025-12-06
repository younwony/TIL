# Tech Interview Study

개발자 기술 면접 준비를 위한 학습 정리입니다.

> 원본 레포지토리: https://github.com/younwony/tech-interview

## 개요

이 스터디는 "[연봉 앞자리를 바꾸는] 개발자 기술 면접 노트" (이남희 저, 한빛미디어, 2024)를 기반으로 합니다.

## 목차

### Algorithm
알고리즘 및 자료구조 관련 개념 정리

- [정렬 알고리즘](algorithm/sort.md) - Bubble, Selection, Insertion, Merge, Quick Sort
- [캐싱 알고리즘](algorithm/cache.md) - LRU, LFU Cache
- [로드 밸런싱](algorithm/load-balancer.md) - Round Robin, Least Connections

### Collection
Java Collection Framework 정리

- [List 인터페이스](collection/list.md) - ArrayList, LinkedList
- [Set 인터페이스](collection/set.md) - HashSet, LinkedHashSet, TreeSet
- [Map 인터페이스](collection/map.md) - HashMap, LinkedHashMap, TreeMap, Hashtable
- [자료구조](collection/structure.md) - Stack, Queue, Deque, PriorityQueue

### Design Pattern
디자인 패턴 정리

- [생성 패턴](pattern/creational.md) - Singleton, Factory Method
- [구조 패턴](pattern/structural.md) - Adapter, Decorator, Facade
- [행동 패턴](pattern/behavioral.md) - Strategy, Template Method, Command

### Java 8
Java 8 주요 기능 정리

- [함수형 인터페이스](java8/functional.md) - Consumer, Supplier, Function, Predicate
- [람다 표현식](java8/lambda.md) - Lambda Expression
- [제네릭](java8/generic.md) - Parameterization

## 폴더 구조

```
tech-interview/
├── README.md
├── algorithm/
│   ├── sort.md           # 정렬 알고리즘
│   ├── cache.md          # 캐싱 알고리즘
│   └── load-balancer.md  # 로드 밸런싱
├── collection/
│   ├── list.md           # List 인터페이스
│   ├── set.md            # Set 인터페이스
│   ├── map.md            # Map 인터페이스
│   └── structure.md      # 자료구조
├── pattern/
│   ├── creational.md     # 생성 패턴
│   ├── structural.md     # 구조 패턴
│   └── behavioral.md     # 행동 패턴
└── java8/
    ├── functional.md     # 함수형 인터페이스
    ├── lambda.md         # 람다 표현식
    └── generic.md        # 제네릭
```
