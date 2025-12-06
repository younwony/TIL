/**
 * @작성자 wony
 * @작성일 2019. 11. 29.
 * @사용처
 *
 */
package Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 29.
 * @사용처 조합(Combination)
 * 
 */
public class Combination {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 29.
	 * @사용처 nCr = n-1Cr-1 + n-1Cr
	 * nCr = 특정원소를 포함하고 뽑는 경우 + 특정원소를 제외하고 뽑는 경우
	 * @Todo
	 * @param args
	 * 
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int[] array = new int[]{1,2,3,4,5};
		int n = array.length;
		int r = 2;
		int[] saveArray = new int[r];
		
		Combination(array, saveArray, 0, 0, n, r);
		
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 3.
	 * @사용처
	 * @Todo
	 * @param readArray : 주어진 n개의 배열
	 * @param saveArray : 저장할 r개의 배열
	 * @param readIndex : 주어진 N개의 배열중 읽을 위치를 알려주는 Index
	 * @param saveIndex : 저장할 배열 중 저장할 위치를 알려주는 Index
	 * @param n : n개의 원소
	 * @param r : r개의 선택
	 * 
	 */
	public static void Combination(int[] readArray, int[] saveArray, int readIndex, int saveIndex, int n, int r){
		if(r == 0) printArray(saveArray);		
		else if(n == r){
			/* 
			 * n이 r과 같을경우 
			 * 
			 * r의 개수만큼 현재 index에서 하나씩 뽑아 넣어준다.
			 * */
			for(int i = 0; i < r; i++) saveArray[saveIndex++] = readArray[readIndex++];
			
			printArray(saveArray);
		}
		else{
			//한개의 원소를 저장 후 , nCr = n-1Cr-1 + n-1Cr 실행
			saveArray[saveIndex] = readArray[readIndex];
			// n-1 C r -1
			Combination(readArray, saveArray, readIndex + 1, saveIndex + 1, n - 1, r - 1);
			// n-1 C r
			Combination(readArray, saveArray, readIndex + 1, saveIndex, n - 1, r);
			
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 3.
	 * @사용처 배열 출력
	 * @Todo
	 * @param array
	 * 
	 */
	public static void printArray(int[] array){
		for(int data : array)
			System.out.print(data + " ");
		System.out.println();
	}
}
