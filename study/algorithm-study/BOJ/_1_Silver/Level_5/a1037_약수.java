/**
 * @작성자 wony
 * @작성일 2020. 1. 6.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 6.
 * @사용처
 * 
 */
public class a1037_약수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 6.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int divisorCount = scanner.nextInt();
		int[] factor = new int[divisorCount];
		for(int i = 0; i < divisorCount; i++)
			factor[i] = scanner.nextInt();
		scanner.close();
		
		Arrays.sort(factor);
		
		System.out.println(factor[0] * factor[divisorCount-1]);
	}

}
