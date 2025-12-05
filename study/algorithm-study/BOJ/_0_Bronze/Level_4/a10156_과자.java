/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 11.
 * @사용처
 * @Todo
 */

public class a10156_과자 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 11.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		scanner.close();
		System.out.println((k * n) - m > 0 ? (k * n) - m : 0); 
	}

}
