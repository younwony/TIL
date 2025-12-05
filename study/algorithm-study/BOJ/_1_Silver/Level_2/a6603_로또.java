/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처 로또
 * 
 */
public class a6603_로또 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 28.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int[] rottoArray;
		int[] dataArray;
		while(true){
			int testCount = scanner.nextInt();
			if(testCount == 0) break;
			dataArray = new int[testCount];
			rottoArray = new int[6]; // r개의 개수만큼 배열 선언
			for(int i = 0; i < testCount; i++)
				dataArray[i] = scanner.nextInt();
			Combination(dataArray, rottoArray, 0, 0, testCount, 6);
			System.out.println();
		}
		
		scanner.close();
	}
	
	public static void Combination(int[] dataArray, int[] rottoArray, int dataIndex, int rottoIndex, int n , int r){
		
		if(r == 0)printArray(rottoArray);
		else if( r == n){
			for(int i = 0; i < r; i++) rottoArray[rottoIndex++] = dataArray[dataIndex++]; 
			printArray(rottoArray);
		}
		else{
			rottoArray[rottoIndex] = dataArray[dataIndex];
			Combination(dataArray, rottoArray, dataIndex + 1, rottoIndex + 1, n - 1, r - 1);
			Combination(dataArray, rottoArray, dataIndex + 1, rottoIndex, n - 1, r);
		}
	}
	
	public static void printArray(int[] array){
		for(int data : array)
			System.out.print(data + " ");
		System.out.println();
	}

}
