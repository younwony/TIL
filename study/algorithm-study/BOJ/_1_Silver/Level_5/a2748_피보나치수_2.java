/**
 * @작성자 wony
 * @작성일 2020. 1. 1.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 1.
 * @사용처
 * @Todo
 */

public class a2748_피보나치수_2 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 1.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		
		int[] memFivo = new int[91];
		memFivo[0] = 0;
		memFivo[1] = 1;
		for(int i = 2; i <= n; i++) {
			memFivo[i] = memFivo[i-1] + memFivo[i-2]; 
		}
		
		System.out.println(memFivo[n]);
	}

}
