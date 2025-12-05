/**
 * @작성자 wony
 * @작성일 2019. 11. 5.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 5.
 * @사용처 수 정렬하기
 * 
 */
public class a2750_수정렬하기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 5.
	 * @사용처 수 정렬하기 - > Arrays.sort 이용
	 * @Todo 
	 * 1 <= testCount <= 1,000
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int[] testArray = new int[testCount];
		
		for(int i = 0; i < testCount; i++){
			testArray[i] = scanner.nextInt();
		}
		
		scanner.close();
		
		Arrays.sort(testArray);
		
		for(int i : testArray)
			System.out.println(i);
	}

}
