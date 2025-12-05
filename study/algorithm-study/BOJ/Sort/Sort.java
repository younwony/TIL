/**
 * @작성자 wony
 * @작성일 2019. 11. 4.
 * @사용처
 *
 */
package Sort;

/**
 * @작성자 wony
 * @작성일 2019. 11. 4.
 * @사용처 정렬 클래스
 * 
 */
public class Sort{

	int[] Array;
	int temp;
	
	public Sort(int[] Array){
		this.Array = Array;
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 4.
	 * @사용처 선택정렬
	 * @Todo
	 * 1. 주어진 리스트 중 최솟값을 찾는다.
	 * 2. 최솟값과 맨앞에 위치한 값과 교체한다.
	 * 3. 최솟값 인덱스의 위치를 제외한 나머지 리스트를 반복한다.
	 * 
	 * 시간 복잡도 = O(n^2)
	 */
	public void SelectSort(){
		int min;
		
		for(int i = 0; i < Array.length; i++){
			min = i;
			for(int j = i; j < Array.length; j++){
				if(Array[j] < Array[min])
					min = j;
			}
			temp = Array[min];
			Array[min] = Array[i];
			Array[i] = temp;
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 4.
	 * @사용처 삽입정렬
	 * @Todo
	 * 1. 배열의 모든 요소를 앞에서부터 정렬된 배열부분과 비교하여, 자신의 위치를 찾아 삽입하는 정렬
	 * 2. 저장공간이 별도로 필요없는 in-place 정렬
	 * 
	 * 시간 복잡도 = O(n^2)
	 */
	public void InsertSort(){
		for(int i =1; i< Array.length; i++){
			temp = Array[i];
			int preTempIndex = i-1;
			while(preTempIndex >= 0 && Array[preTempIndex] > temp){
				Array[preTempIndex+1] = Array[preTempIndex];
				preTempIndex--;
			}
			Array[preTempIndex+1] = temp;
		}
	};
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 8.
	 * @사용처 버블정렬
	 * @Todo
	 * 
	 * 1. 맨 뒤에서부터 정렬 값을 채워나가는 형식
	 * 
	 * 시간 복잡도 = O(n^2)
	 */
	public void BubbleSort(){
		for(int j = Array.length-1; j > 0; j--){
			for(int i = 0; i < j; i++){
				if(Array[i] > Array[i+1]){
					temp = Array[i];
					Array[i] = Array[i+1];
					Array[i+1] = temp;
				}
			}
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 8.
	 * @사용처 병합정렬
	 * @Todo
	 * 
	 */
	public void MergeSort(){
		
		// 분할되어 정렬된 데이터를 저장할 Array 생성
		int[] array = new int[Array.length];
		
		for(int i = 0; i < array.length; i++)
			array[i] = Array[i];
		
		RecursionMerge(array, 0, array.length-1);
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 7.
	 * @사용처 Merge - 분할
	 * @Todo
	 * @param array
	 * @param left
	 * @param right
	 * 
	 */
	public void RecursionMerge(int[] array, int left, int right){
		
		// 2분할 지점
		int middle = (left+right)/2;
		// Array가 데이터를 하나가질때까지 쪼갠다.
		if(left < right){
			RecursionMerge(array, left, middle); // 좌측
			RecursionMerge(array, middle + 1, right); // 우측
			CompareArray(array, left, middle, right); // 결합부분
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 7.
	 * @사용처 Merge - 정복, 결합
	 * @Todo
	 * @param array
	 * @param left
	 * @param middle
	 * @param right
	 * 
	 */
	public void CompareArray(int[] array, int left, int middle, int right){
		
		int i = left; // 좌측 아이템 시작점
		int j = middle + 1; // 우측 아이템 시작점
		int k = left; // 아이템 넣을 index
		// 좌측 배열이 끝나거나 우측배열이 끝나기전 까지
		while(i <= middle && j <= right){
			if(Array[i] < Array[j]){
				array[k] = Array[i];
				i++;
			}
			else if(Array[j] <= Array[i]){
				array[k] = Array[j];
				j++;
			}
			k++;
		}
		
		// 우측배열이 먼저 끝낫을 경우 좌측 배열 남은 데이터 삽입
		while(i <= middle){
			array[k] = Array[i];
			k++;
			i++;
		}
		
		// 좌측배열이 먼저 끝낫을 경우 우측배열 남은 데이터 삽입
		while(j <= right){
			array[k] = Array[j];
			k++;
			j++;
		}
		
		// 정렬된 데이터 실제 배열에 삽입
		for(; left<=right; left++)
			Array[left] = array[left];
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 8.
	 * @사용처 퀵 정렬
	 * @Todo
	 * 
	 */
	public void QuickSort(){
		RecursionQuick(Array, 0, Array.length-1);
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 8.
	 * @사용처 퀵정렬 - 실행부분
	 * @Todo
	 * 
	 *  1. 현재 는 피봇값을 맨 처음의 인덱스로 설정하였다.
	 *  2. 좀더 좋은 효율을 갖기위해 랜덤 피봇을 설정해 보자!(또는 우측 피봇, 중간 피봇으로 구형해보기)
	 * 
	 * @param array
	 * @param left
	 * @param right
	 * 
	 */
	public void RecursionQuick(int[] array, int left, int right){
		
		if(left >= right)		// 정렬할 배열이 하나일경우 리턴
			return;
		
		int pivot = left; 		// 피봇값지정 맨앞 의 인덱스로 설정
		int start = left+1;		// 시작점 지정 피봇값 + 1(큰값 찾기)
		int end = right;		// 끝점 지정(작은값 찾기)
		
		while(start <= end){										// 시작점이 끝점보다 작거나 같을 경우 진행
			while(Array[start] <= Array[pivot] && start < right){ //큰값을 찾기위해 start 인덱스가 가르키는 데이터가  pivot 데이터 보다 작을경우 계속 이동, 배열의 범위를 넘어가지않기위해 끝지점까지  조건 지정
				start++;
			}
			while(Array[end] >= Array[pivot] && end > left){ // 작은 값을 찾기위해 end 인덱스가 가르키는 데이터가 pivot 데이터 보다 클 경우 계속 이동, 배열의 범위를 넘어가지 않기 위해 시작 지점까지 조건 지정
				end--;
			}
			if(start < end){				//작은 값과 큰값을 찾은 경우 스왑
				temp = Array[start];
				Array[start] = Array[end];
				Array[end] = temp;
			}else{							//찾지못하고 시작점과 끝점이 교차된경우 끝점과 피봇값 스왑
				temp = Array[end];
				Array[end] = Array[pivot];
				Array[pivot] = temp;
				break;
			}
		}
		
		RecursionQuick(array, left, end-1);		//정렬 완료후 남은 왼쪽 배열 재귀 진행
		RecursionQuick(array, end+1, right);	//정렬 완료후 남은 오른쪽 배열 재귀 진행
		
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 1.
	 * @사용처 힙정렬
	 * @Todo
	 * 
	 * 1. 최대힙을 만든후 첫인덱스를 마지막으로 보내면서 오름차순 정렬생성
	 *  
	 */
	public void HeapSort() {
		
		Heapify(Array, 0);
		
		int lastIndex;
		for(int i = 0; i < Array.length; i++) {
			lastIndex = Array.length - 1 - i;
			temp = Array[lastIndex];
			Array[lastIndex] = Array[0];
			Array[0] = temp;
			Heapify(Array, i+1);
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 1.
	 * @사용처 힙정렬 - 최대힙만들기
	 * @param array
	 * @param lastIndex
	 * @Todo
	 */
	public void Heapify(int[] array, int lastIndex) {
		int node;
		int pNode;
		int temp;
		
		for(int i = array.length - 1 - lastIndex; i >= 0; i--) {
			node = i;
			pNode = (i-1)/2; //부모노드
			if(pNode >= 0 && array[node] > array[pNode]) {
				temp = array[node];
				array[node] = array[pNode];
				array[pNode] = temp;
			}
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 4.
	 * @사용처 int Array 출력
	 * @Todo
	 * @param Array
	 * 
	 */
	public void Print(){
		for(int data : Array)
			System.out.print(data + " ");
	}
}
