# 정렬 알고리즘

## 핵심 정리

- 정렬 알고리즘은 데이터를 특정 순서대로 나열하는 알고리즘
- 시간 복잡도와 공간 복잡도를 고려하여 상황에 맞는 알고리즘 선택 필요
- 안정 정렬(Stable Sort): 동일한 값의 상대적 순서가 유지됨

## 비교 정렬 알고리즘

### Bubble Sort (버블 정렬)

인접한 두 요소를 비교하여 정렬하는 가장 단순한 알고리즘

```java
public void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}
```

| 항목 | 복잡도 |
|------|--------|
| 시간 (최선) | O(n) |
| 시간 (평균/최악) | O(n²) |
| 공간 | O(1) |
| 안정성 | 안정 |

### Selection Sort (선택 정렬)

최솟값을 찾아 맨 앞과 교환하는 방식

```java
public void selectionSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        int minIdx = i;
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIdx]) {
                minIdx = j;
            }
        }
        int temp = arr[minIdx];
        arr[minIdx] = arr[i];
        arr[i] = temp;
    }
}
```

| 항목 | 복잡도 |
|------|--------|
| 시간 (모든 경우) | O(n²) |
| 공간 | O(1) |
| 안정성 | 불안정 |

### Insertion Sort (삽입 정렬)

정렬된 부분에 새 요소를 적절한 위치에 삽입하는 방식

```java
public void insertionSort(int[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}
```

| 항목 | 복잡도 |
|------|--------|
| 시간 (최선) | O(n) |
| 시간 (평균/최악) | O(n²) |
| 공간 | O(1) |
| 안정성 | 안정 |

### Merge Sort (병합 정렬)

분할 정복(Divide and Conquer) 방식으로 배열을 나누고 병합하며 정렬

```java
public void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
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

| 항목 | 복잡도 |
|------|--------|
| 시간 (모든 경우) | O(n log n) |
| 공간 | O(n) |
| 안정성 | 안정 |

### Quick Sort (퀵 정렬)

피벗을 기준으로 분할하여 정렬하는 분할 정복 알고리즘

```java
public void quickSort(int[] arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

private int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    int temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;
    return i + 1;
}
```

| 항목 | 복잡도 |
|------|--------|
| 시간 (최선/평균) | O(n log n) |
| 시간 (최악) | O(n²) |
| 공간 | O(log n) |
| 안정성 | 불안정 |

## 정렬 알고리즘 비교

| 알고리즘 | 최선 | 평균 | 최악 | 공간 | 안정성 |
|----------|------|------|------|------|--------|
| Bubble | O(n) | O(n²) | O(n²) | O(1) | O |
| Selection | O(n²) | O(n²) | O(n²) | O(1) | X |
| Insertion | O(n) | O(n²) | O(n²) | O(1) | O |
| Merge | O(n log n) | O(n log n) | O(n log n) | O(n) | O |
| Quick | O(n log n) | O(n log n) | O(n²) | O(log n) | X |

## 면접 예상 질문

1. **Quick Sort와 Merge Sort의 차이점은 무엇인가요?**
   - Quick Sort는 in-place 정렬로 추가 메모리가 적게 필요하지만 최악의 경우 O(n²)
   - Merge Sort는 항상 O(n log n)을 보장하지만 O(n)의 추가 메모리 필요

2. **정렬이 거의 된 배열에 적합한 정렬 알고리즘은?**
   - Insertion Sort가 O(n)에 가까운 성능을 보여 효율적

3. **안정 정렬이 필요한 상황은 언제인가요?**
   - 다중 기준으로 정렬할 때 (예: 이름순 정렬 후 나이순 정렬)
