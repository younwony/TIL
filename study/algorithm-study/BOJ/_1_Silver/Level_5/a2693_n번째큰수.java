/**
 * @작성자 wony
 * @작성일 2020. 1. 25.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 25.
 * @사용처
 * @Todo
 */

public class a2693_n번째큰수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 25.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		int[] inputData = new int[10];
		
		for(int i = 0; i < testCase; i++) {
			for(int j = 0; j < 10; j++) {
				inputData[j] = scanner.nextInt();
			}
			Arrays.sort(inputData);
			System.out.println(inputData[7]);
		}
		scanner.close();
	}

}
