# 정렬 알고리즘 (Sorting Algorithms)

> `[3] 중급` · 선수 지식: [시간 복잡도](./time-complexity.md), [재귀](../data-structure/recursion.md)

> 데이터를 특정 순서(오름차순/내림차순)로 배열하는 알고리즘

## 왜 알아야 하는가?

- **실무**: 데이터베이스 ORDER BY, 검색 결과 정렬, 로그 분석 등 거의 모든 애플리케이션에서 사용됩니다. 효율적인 정렬은 후속 작업(이진 탐색, 중복 제거 등)의 성능을 크게 향상시킵니다.
- **면접**: 퀵 정렬, 병합 정렬은 코딩 테스트 단골 주제이며, 안정성, 시간/공간 복잡도를 묻는 질문이 자주 출제됩니다. 알고리즘 선택 기준을 평가하는 핵심 주제입니다.
- **기반 지식**: 분할 정복(퀵, 병합), 재귀, 시간 복잡도 분석의 대표 예시입니다. 안정성, In-place 등 알고리즘 특성 이해의 기반이 됩니다.

## 핵심 개념

- **비교 기반 정렬**: 원소 간 비교를 통해 정렬 (버블, 선택, 삽입, 퀵, 병합, 힙)
- **비비교 기반 정렬**: 특정 조건에서 비교 없이 정렬 (계수, 기수, 버킷)
- **안정성 (Stability)**: 동일한 값의 상대적 순서가 유지되는지 여부
- **시간 복잡도**: 최선/평균/최악 케이스에 따라 성능이 다름
- **공간 복잡도**: 추가 메모리 사용량 (In-place vs Out-of-place)

## 쉽게 이해하기

**정렬 알고리즘**을 카드 정리하기에 비유할 수 있습니다.

손에 든 카드를 번호 순서대로 정리하는 방법은 여러 가지입니다:

- **버블 정렬**: 옆 사람과 키를 비교하며 한 줄로 서기. 키 큰 사람이 계속 뒤로 이동
- **선택 정렬**: 매번 제일 작은 카드를 찾아 맨 앞에 놓기
- **삽입 정렬**: 카드를 한 장씩 뽑아 정렬된 부분의 적절한 위치에 끼워넣기
- **병합 정렬**: 카드를 절반씩 나눠 각각 정렬한 후, 두 묶음을 비교하며 합치기
- **퀵 정렬**: 기준 카드(피벗)를 정하고, 작은 것은 왼쪽, 큰 것은 오른쪽으로 분류 후 재귀적으로 반복

예를 들어, 책장의 책을 정리할 때:
- 책이 10권이면 삽입 정렬처럼 하나씩 적절한 위치에 꽂기
- 책이 1000권이면 퀵 정렬이나 병합 정렬처럼 나눠서 정렬 후 합치기

카드 수가 적으면 간단한 방법이 빠르지만, 많으면 효율적인 알고리즘이 필요합니다.

## 상세 설명

### 1. 단순 정렬 알고리즘 (Simple Sorts)

#### 버블 정렬 (Bubble Sort)

인접한 두 원소를 비교하며 교환을 반복하여 큰 값이 뒤로 이동하는 정렬 방식입니다.

**왜 이렇게 하는가?**

가장 직관적인 정렬 방법입니다. 매번 가장 큰 원소가 "물거품처럼" 위로 떠오르듯 뒤로 이동하기 때문에 버블 정렬이라 부릅니다. 한 번의 순회(pass)가 끝나면 가장 큰 원소가 확정된 위치에 놓입니다.

```java
public void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        boolean swapped = false;
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 인접 원소 교환
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }
        // 최적화: 교환이 없으면 이미 정렬됨
        if (!swapped) break;
    }
}
```

**특징**:
- 안정 정렬 (Stable): 동일한 값의 순서 유지
- In-place: 추가 메모리 O(1)
- 시간 복잡도: O(n²) - 실무에서 거의 사용하지 않음

#### 선택 정렬 (Selection Sort)

매번 최솟값을 찾아 맨 앞과 교환하는 정렬 방식입니다.

**왜 이렇게 하는가?**

매 단계마다 남은 원소 중 가장 작은 것을 "선택"하여 정렬된 부분의 끝에 추가합니다. 교환 횟수가 최대 n-1번으로 제한되어 교환 비용이 큰 경우 버블 정렬보다 유리합니다.

```java
public void selectionSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        int minIdx = i;
        // 남은 부분에서 최솟값 찾기
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIdx]) {
                minIdx = j;
            }
        }
        // 최솟값을 현재 위치와 교환
        int temp = arr[i];
        arr[i] = arr[minIdx];
        arr[minIdx] = temp;
    }
}
```

**특징**:
- 불안정 정렬 (Unstable): 교환으로 인해 동일 값의 순서가 바뀔 수 있음
- 교환 횟수가 적어 교환 비용이 큰 경우 유리

#### 삽입 정렬 (Insertion Sort)

정렬된 부분에 새 원소를 적절한 위치에 삽입하는 정렬 방식입니다.

**왜 이렇게 하는가?**

카드 게임에서 카드를 정리하는 방식과 동일합니다. 이미 정렬된 배열에 새 원소를 삽입할 때 효율적입니다. 거의 정렬된 데이터에서는 O(n)에 가까운 성능을 보입니다.

```java
public void insertionSort(int[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;

        // key보다 큰 원소들을 한 칸씩 뒤로 이동
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}
```

**특징**:
- 안정 정렬 (Stable)
- 거의 정렬된 데이터에 최적: O(n)
- 작은 데이터셋(n < 50)에서 효율적 - 실무에서 퀵 정렬과 함께 사용

**왜 작은 데이터셋에서 효율적인가?**

오버헤드가 적고 캐시 지역성이 좋아 상수 계수가 작습니다. 퀵 정렬도 부분 배열 크기가 작아지면 삽입 정렬로 전환합니다.

### 2. 효율적인 정렬 알고리즘 (Efficient Sorts)

#### 퀵 정렬 (Quick Sort)

피벗(기준값)을 선택하고 작은 값은 왼쪽, 큰 값은 오른쪽으로 분할한 후 재귀적으로 정렬하는 방식입니다.

**왜 이렇게 하는가?**

분할 정복(Divide and Conquer) 전략을 사용합니다. 피벗을 기준으로 한 번 분할하면 피벗은 최종 위치가 확정되고, 양쪽을 독립적으로 정렬할 수 있습니다. 평균적으로 가장 빠른 정렬 알고리즘입니다.

```java
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);  // 왼쪽 부분 정렬
        quickSort(arr, pi + 1, high); // 오른쪽 부분 정렬
    }
}

private int partition(int[] arr, int low, int high) {
    int pivot = arr[high]; // 피벗을 마지막 원소로 선택
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}
```

**특징**:
- 불안정 정렬
- In-place (추가 메모리: 재귀 스택 O(log n))
- 평균 O(n log n), 최악 O(n²) - 피벗 선택이 중요

**왜 최악의 경우 O(n²)인가?**

이미 정렬된 배열에서 항상 최소/최대값을 피벗으로 선택하면 한쪽으로만 분할되어 O(n²)이 됩니다. 이를 방지하기 위해 랜덤 피벗, 중앙값 피벗(Median of Three) 등을 사용합니다.

**왜 실무에서 가장 많이 사용되나?**

- 평균적으로 가장 빠름 (상수 계수가 작음)
- 캐시 지역성이 좋음 (인접 메모리 접근)
- In-place로 메모리 효율적

#### 병합 정렬 (Merge Sort)

배열을 절반씩 나눠 각각 정렬한 후 병합하는 정렬 방식입니다.

**왜 이렇게 하는가?**

분할 정복 전략을 사용하되, 피벗 선택 없이 무조건 절반으로 나눕니다. 두 개의 정렬된 배열을 병합하는 것은 O(n)이므로, 전체 시간 복잡도가 항상 O(n log n)으로 보장됩니다.

```java
public void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);      // 왼쪽 절반 정렬
        mergeSort(arr, mid + 1, right); // 오른쪽 절반 정렬
        merge(arr, left, mid, right);   // 병합
    }
}

private void merge(int[] arr, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    int[] L = new int[n1];
    int[] R = new int[n2];

    System.arraycopy(arr, left, L, 0, n1);
    System.arraycopy(arr, mid + 1, R, 0, n2);

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arr[k++] = L[i++];
        } else {
            arr[k++] = R[j++];
        }
    }

    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];
}
```

**특징**:
- 안정 정렬 (Stable)
- 항상 O(n log n) 보장 - 피벗 선택 문제 없음
- 추가 메모리 O(n) 필요 (Out-of-place)

**언제 사용하나?**

- 최악의 경우에도 성능 보장이 필요할 때
- 안정 정렬이 필요할 때
- 링크드 리스트 정렬 (추가 메모리 불필요)
- 외부 정렬 (대용량 파일 정렬)

#### 힙 정렬 (Heap Sort)

최대 힙(Max Heap) 자료구조를 이용하여 정렬하는 방식입니다.

**왜 이렇게 하는가?**

최대 힙에서는 루트가 항상 최댓값입니다. 루트를 배열 끝과 교환하고 힙 크기를 줄인 후 heapify를 반복하면 정렬됩니다.

```java
public void heapSort(int[] arr) {
    int n = arr.length;

    // 최대 힙 구성
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(arr, n, i);
    }

    // 하나씩 추출하며 정렬
    for (int i = n - 1; i > 0; i--) {
        swap(arr, 0, i); // 최댓값을 끝으로 이동
        heapify(arr, i, 0); // 남은 부분 힙 재구성
    }
}

private void heapify(int[] arr, int n, int i) {
    int largest = i;
    int left = 2 * i + 1;
    int right = 2 * i + 2;

    if (left < n && arr[left] > arr[largest])
        largest = left;
    if (right < n && arr[right] > arr[largest])
        largest = right;

    if (largest != i) {
        swap(arr, i, largest);
        heapify(arr, n, largest);
    }
}
```

**특징**:
- 불안정 정렬
- In-place, O(n log n) 보장
- 병합 정렬보다 메모리 효율적이지만 캐시 지역성이 나쁨

**왜 퀵 정렬보다 느린가?**

힙 정렬은 힙 구조를 유지하기 위해 비인접 메모리에 자주 접근하여 캐시 미스가 많습니다. 퀵 정렬은 인접 메모리를 주로 접근하여 캐시 효율이 좋습니다.

### 3. 비비교 정렬 알고리즘 (Non-Comparison Sorts)

#### 계수 정렬 (Counting Sort)

각 원소의 등장 횟수를 세어 정렬하는 방식입니다.

**왜 이렇게 하는가?**

값의 범위가 제한적일 때, 비교 없이 O(n)에 정렬할 수 있습니다. 각 값이 몇 번 등장하는지 카운트 배열에 저장하고, 이를 기반으로 출력 배열을 구성합니다.

```java
public void countingSort(int[] arr) {
    int n = arr.length;
    int max = Arrays.stream(arr).max().getAsInt();

    int[] count = new int[max + 1];
    int[] output = new int[n];

    // 등장 횟수 카운트
    for (int num : arr) {
        count[num]++;
    }

    // 누적 합으로 변환 (위치 계산용)
    for (int i = 1; i <= max; i++) {
        count[i] += count[i - 1];
    }

    // 뒤에서부터 출력 배열 구성 (안정 정렬 보장)
    for (int i = n - 1; i >= 0; i--) {
        output[count[arr[i]] - 1] = arr[i];
        count[arr[i]]--;
    }

    System.arraycopy(output, 0, arr, 0, n);
}
```

**특징**:
- 안정 정렬
- 시간 복잡도: O(n + k) (k는 값의 범위)
- 공간 복잡도: O(n + k)

**언제 사용하나?**

- 값의 범위가 n에 비해 크지 않을 때 (예: 나이, 시험 점수)
- 음수가 없거나 오프셋으로 처리 가능할 때

**만약 범위가 크면?**

k가 너무 크면 (예: 0 ~ 1,000,000) 메모리 낭비가 심합니다. 이 경우 기수 정렬을 사용합니다.

#### 기수 정렬 (Radix Sort)

자릿수별로 정렬을 반복하는 방식입니다.

**왜 이렇게 하는가?**

큰 숫자도 각 자릿수는 0~9로 제한됩니다. 낮은 자릿수부터 높은 자릿수까지 계수 정렬을 반복하면 전체가 정렬됩니다.

```java
public void radixSort(int[] arr) {
    int max = Arrays.stream(arr).max().getAsInt();

    // 1의 자리, 10의 자리, ... 순으로 정렬
    for (int exp = 1; max / exp > 0; exp *= 10) {
        countingSortByDigit(arr, exp);
    }
}

private void countingSortByDigit(int[] arr, int exp) {
    int n = arr.length;
    int[] output = new int[n];
    int[] count = new int[10]; // 0~9

    for (int num : arr) {
        count[(num / exp) % 10]++;
    }

    for (int i = 1; i < 10; i++) {
        count[i] += count[i - 1];
    }

    for (int i = n - 1; i >= 0; i--) {
        int digit = (arr[i] / exp) % 10;
        output[count[digit] - 1] = arr[i];
        count[digit]--;
    }

    System.arraycopy(output, 0, arr, 0, n);
}
```

**특징**:
- 안정 정렬
- 시간 복잡도: O(d × (n + k)) (d는 최대 자릿수, k는 기수)
- 10진수: O(d × n), d = log₁₀(max)

**언제 사용하나?**

- 정수, 문자열 등 자릿수가 있는 데이터
- 값의 범위는 크지만 자릿수가 적을 때

### 4. 안정성 (Stability)

**안정 정렬**이란 동일한 값의 상대적 순서가 정렬 후에도 유지되는 것입니다.

**왜 중요한가?**

다중 정렬 시 이전 정렬 결과를 유지해야 합니다.

예: 학생을 성적순으로 정렬한 후, 같은 성적끼리는 이름순으로 정렬
```
[{90, Alice}, {85, Bob}, {90, Charlie}]
→ 성적순 정렬 (안정): [{90, Alice}, {90, Charlie}, {85, Bob}]
→ 이름순 정렬 (안정): 90점 내에서 Alice, Charlie 순서 유지
```

**권장 (O)**: 안정 정렬 사용 - 병합 정렬, 삽입 정렬, 계수 정렬
**비권장 (X)**: 불안정 정렬 - 퀵 정렬, 선택 정렬, 힙 정렬

**왜?**

데이터베이스의 ORDER BY 절이 여러 개일 때, 안정 정렬이 아니면 이전 정렬 기준이 무시됩니다.

**퀵 정렬을 안정적으로 만들 수 있나?**

가능하지만 추가 메모리 O(n)이 필요하여 퀵 정렬의 장점(In-place)이 사라집니다.

## 시간/공간 복잡도

| 알고리즘 | 최선 | 평균 | 최악 | 공간 | 안정성 |
|---------|------|------|------|------|--------|
| **버블 정렬** | O(n) | O(n²) | O(n²) | O(1) | O |
| **선택 정렬** | O(n²) | O(n²) | O(n²) | O(1) | X |
| **삽입 정렬** | O(n) | O(n²) | O(n²) | O(1) | O |
| **퀵 정렬** | O(n log n) | O(n log n) | O(n²) | O(log n) | X |
| **병합 정렬** | O(n log n) | O(n log n) | O(n log n) | O(n) | O |
| **힙 정렬** | O(n log n) | O(n log n) | O(n log n) | O(1) | X |
| **계수 정렬** | O(n + k) | O(n + k) | O(n + k) | O(n + k) | O |
| **기수 정렬** | O(d(n + k)) | O(d(n + k)) | O(d(n + k)) | O(n + k) | O |

**왜 비교 기반 정렬은 최소 O(n log n)인가?**

비교 기반 정렬의 결정 트리 높이는 최소 log₂(n!)이고, 스털링 근사에 의해 Ω(n log n)입니다. 즉, n개 원소의 모든 순열을 구분하려면 최소 n log n번의 비교가 필요합니다.

## 알고리즘 선택 가이드

| 상황 | 추천 알고리즘 | 이유 |
|------|-------------|------|
| 작은 데이터 (n < 50) | 삽입 정렬 | 오버헤드가 적고 캐시 효율적 |
| 평균적으로 빠른 정렬 | 퀵 정렬 | 평균 O(n log n), 캐시 지역성 우수 |
| 최악의 경우 보장 | 병합 정렬, 힙 정렬 | 항상 O(n log n) |
| 안정 정렬 필요 | 병합 정렬 | 안정성 + O(n log n) 보장 |
| 메모리 제약 | 힙 정렬 | In-place + O(n log n) 보장 |
| 거의 정렬된 데이터 | 삽입 정렬 | O(n)에 가까운 성능 |
| 정수, 범위 제한 | 계수 정렬, 기수 정렬 | O(n) 가능 |
| 링크드 리스트 | 병합 정렬 | 추가 메모리 불필요 |

**왜 Java의 Arrays.sort()는 여러 알고리즘을 섞어 사용하나?**

- 기본형 배열: Dual-Pivot Quick Sort (평균 성능 우수)
- 객체 배열: TimSort (병합 + 삽입 정렬, 안정 정렬)
- 작은 부분 배열: 삽입 정렬로 전환 (오버헤드 감소)

상황에 맞는 최적 알고리즘을 조합하여 실용적 성능을 극대화합니다.

## 면접 예상 질문

- Q: 퀵 정렬과 병합 정렬의 차이는?
  - A: 둘 다 O(n log n) 분할 정복 알고리즘이지만, 퀵 정렬은 피벗 선택에 따라 최악 O(n²)이고 In-place입니다. 병합 정렬은 항상 O(n log n)이 보장되지만 O(n) 추가 메모리가 필요합니다. **왜냐하면** 퀵 정렬은 분할 시 피벗 기준으로 나누므로 한쪽으로 치우칠 수 있고, 병합 정렬은 무조건 절반씩 나눠 균형이 보장되지만 병합 시 임시 배열이 필요하기 때문입니다. 실무에서는 평균 성능이 좋은 퀵 정렬을 주로 사용하되, 최악 케이스 방지를 위해 랜덤 피벗이나 IntroSort(퀵+힙 정렬 조합)를 사용합니다.

- Q: 안정 정렬이 왜 필요한가요?
  - A: 다중 기준 정렬 시 이전 정렬 결과를 보존하기 위해서입니다. **예를 들어** 학생을 먼저 이름순으로 정렬한 후, 성적순으로 정렬할 때 안정 정렬을 사용하면 같은 성적 내에서 이름순이 유지됩니다. 데이터베이스의 ORDER BY 절이 여러 개일 때, 불안정 정렬을 사용하면 뒷 기준만 적용되고 앞 기준은 무시됩니다. **따라서** 엑셀, 데이터베이스, UI 테이블 정렬 등에서는 안정 정렬이 필수입니다.

- Q: 왜 계수 정렬은 O(n)인데 항상 사용하지 않나요?
  - A: 값의 범위(k)가 제한적일 때만 효율적이기 때문입니다. **왜냐하면** 계수 정렬은 O(n + k) 시간과 공간을 사용하므로, k가 n보다 훨씬 크면 오히려 비효율적입니다. **예를 들어** 10개의 숫자를 정렬하는데 범위가 0~1,000,000이면 100만 크기의 배열을 생성해야 합니다. **또한** 음수, 실수, 문자열 등 값의 범위를 정의하기 어려운 데이터에는 사용할 수 없습니다. **따라서** 나이(0~150), 시험 점수(0~100) 등 범위가 작고 명확한 경우에만 사용합니다.

- Q: 실무에서 가장 많이 사용되는 정렬 알고리즘은?
  - A: 퀵 정렬과 병합 정렬의 변형입니다. **구체적으로** Java의 Arrays.sort()는 기본형 배열에 Dual-Pivot Quick Sort를, 객체 배열에 TimSort(병합+삽입)를 사용합니다. Python의 sorted()도 TimSort를 사용합니다. **왜냐하면** 실제 데이터는 부분적으로 정렬된 경우가 많아, 이를 감지하여 최적화하는 적응형 알고리즘이 평균적으로 가장 빠르기 때문입니다. **추가로** 작은 부분 배열(보통 < 10)에서는 삽입 정렬로 전환하여 재귀 오버헤드를 줄입니다.

- Q: 링크드 리스트를 정렬할 때 어떤 알고리즘이 좋나요?
  - A: 병합 정렬이 가장 적합합니다. **왜냐하면** 링크드 리스트는 인덱스 접근이 O(n)이므로 퀵 정렬의 피벗 선택과 분할이 비효율적이지만, 병합 정렬은 순차 접근만으로 동작하고 포인터 재연결로 병합할 수 있어 추가 메모리도 불필요하기 때문입니다. 배열의 병합 정렬은 O(n) 추가 메모리가 필요하지만, 링크드 리스트는 포인터만 변경하면 되어 O(1) 공간으로 가능합니다. **따라서** LinkedList를 정렬할 때는 Collections.sort()가 내부적으로 병합 정렬을 사용합니다.

## 연관 문서

| 문서 | 연관성 | 난이도 |
|------|--------|--------|
| [시간 복잡도](./time-complexity.md) | 선수 지식: 정렬 알고리즘 성능 비교 기반 | [2] 입문 |
| [재귀](../data-structure/recursion.md) | 선수 지식: 퀵 정렬, 병합 정렬 구현 기반 | [2] 입문 |
| [탐색](./search.md) | 관련 개념: 정렬 후 이진 탐색 | [3] 중급 |
| [그래프](./graph.md) | 관련 개념: 크루스칼 MST의 간선 정렬 | [4] 심화 |
| [자료구조](../data-structure/README.md) | 선수 지식: 힙 정렬의 힙 자료구조 | [2] 입문 |

## 참고 자료

- Introduction to Algorithms (CLRS) - 7장(Quicksort), 8장(Sorting in Linear Time)
- Algorithms 4th Edition (Robert Sedgewick) - 2.1~2.5 Sorting
- [VisuAlgo](https://visualgo.net/en/sorting) - 정렬 알고리즘 시각화
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/) - 복잡도 요약
