/**
 * @작성자 wony
 * @작성일 2019. 11. 5.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 5.
 * @사용처 수찾기
 * 
 */
public class a1920_수찾기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 5.
	 * @사용처 BST
	 * @Todo 
	 * BST 재귀와 반복문을 구현하여 해당 알고리즘을 해결
	 * 재귀가 메모리와 시간적으로 우수한 퍼포먼스를 가졋다.
	 * 단. 동일 IO 를 사용하지 않고 여러 미흡한 부분이 있기에 해당 결과가 정확한지 동일한 조건하에 재 설계가 필요하다.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*재귀함수 이진탐색트리(BST)*/
		RecursionBST();
		
		/*반복문 이진탐색트리(BST)*/
		LoopBST();
	}
	
	static int[] Array;
	static int left;
	static int right;
	static int middle;
	static boolean searchCK;
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 5.
	 * @사용처 BST 재귀버전
	 * @Todo
	 * Memory	: 61828 KB
	 * Time		: 600 ms
	 */
	public static void RecursionBST(){
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		int n,m;
		String[] nInput,mOutput;
		
		try {
			n = Integer.parseInt(bufferedReader.readLine());
			nInput = bufferedReader.readLine().split(" ");
			m = Integer.parseInt(bufferedReader.readLine());
			mOutput = bufferedReader.readLine().split(" ");


			/**
			 * 비교 기반 정렬에선 정수의 경우 두 수를 연산 한번으로 비교할 수 있지만,
			 *
			 * 문자열의 경우 최악에 모든 문자에 대해 비교를 하게 됩니다.
			 *
			 * 정수의 정렬 시간복잡도를 O(NlgN) 이라 한다면
			 *
			 * 문자열은 문자열의 길이가 더해져 O(NlgNM) 이라 할 수 있습니다.
			 * 출처 - https://www.acmicpc.net/board/view/34668
			 */
			Array = new int[n];
			for(int i = 0; i < n; i++){
				Array[i] = Integer.parseInt(nInput[i]);
			}
			
			Arrays.sort(Array);
			
			for(String data : mOutput){
				left = 0;
				right = n-1;
				middle = right/2;
				if(BinarySearch(Integer.parseInt(data)))
					bufferedWriter.write(1 + "\n");
				else
					bufferedWriter.write(0 + "\n");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null) {
					bufferedWriter.flush(); 
					bufferedReader.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static boolean BinarySearch(int data){
		
		if(right - left < 0){
			searchCK =  false;
			return searchCK;
		}
		
		middle = (left+right)/2;
		
		if(data == Array[middle])
			searchCK =  true;
		else if(data > Array[middle]){
			left = middle+1;
			BinarySearch(data);
		}
		else if(data < Array[middle]){
			right = middle-1;
			BinarySearch(data);
		}
		
		return searchCK;
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 5.
	 * @사용처 BST 반복문 버전
	 * @Todo
	 * Memory	: 181328 KB
	 * Time		: 1540 ms
	 */
	public static void LoopBST(){
		Scanner scanner = new Scanner(System.in);
		
		int inputCount = scanner.nextInt();
		
		int[] inputArray = new int[inputCount];
		
		for(int i = 0; i < inputCount; i++)
			inputArray[i] = scanner.nextInt();
		
		int checkNumCount = scanner.nextInt();
		
		
		Arrays.sort(inputArray);
		
		int left, right, middle;
		
		int data, searchData;
		
		for(int i = 0; i < checkNumCount; i++){
			left = 0;
			right = inputCount - 1 ;
			searchData = 0;
			
			data = scanner.nextInt();
			
			while(right >= left){
				middle = (left + right)/2;
				if(data == inputArray[middle]){
					searchData = 1;
					break;
				}else if(data > inputArray[middle]){
					left = middle + 1;
				}else
					right = middle - 1;
			}
			System.out.println(searchData);
		}
		
		scanner.close();
	}

}
