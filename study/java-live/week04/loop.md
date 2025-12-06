# Week 4: 반복문 (Loop)

## 개요

Java의 반복문에 대해 학습합니다.

## For 문

```java
public void loopFor() {
    for (int i = 0; i < 5; i++) {
        System.out.println(i);
    }
}
```

## Enhanced For (For-Each) 문

```java
public void loopForEach() {
    int[] arr = {1, 2, 3, 4, 5};

    for (int num : arr) {
        System.out.println(num);
    }
}
```

**특징:**
- 배열이나 컬렉션 순회에 적합
- 인덱스 접근이 필요 없을 때 사용
- 읽기 전용 순회에 권장

## While 문

```java
public void loopWhile() {
    int i = 0;

    while (i < 5) {
        System.out.println(i);
        i++;
    }
}
```

**특징:**
- 조건을 먼저 검사
- 조건이 false면 한 번도 실행되지 않을 수 있음

## Do-While 문

```java
public void loopDoWhile() {
    int i = 0;

    do {
        System.out.println(i);
        i++;
    } while (i < 5);
}
```

**특징:**
- 조건을 나중에 검사
- 최소 한 번은 실행됨

## break와 continue

### break
```java
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // 반복문 종료
    }
    System.out.println(i);
}
```

### continue
```java
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // 다음 반복으로 건너뛰기
    }
    System.out.println(i);
}
```

## 레이블을 사용한 break/continue

```java
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break outer;  // 외부 반복문까지 종료
        }
        System.out.println("i=" + i + ", j=" + j);
    }
}
```

## 참고

- 원본 코드: [JavaLiveStudy/liveStudy4](https://github.com/younwony/JavaLiveStudy/tree/master/src/com/wony/liveStudy4)
