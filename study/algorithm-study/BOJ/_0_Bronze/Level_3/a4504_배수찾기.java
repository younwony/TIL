/**
 * @작성자 wony
 * @작성일 2020. 2. 11.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 11.
 * @사용처
 * @Todo
 */

public class a4504_배수찾기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 11.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		int m;
		while((m = scanner.nextInt()) != 0) {
			if(m%n == 0) {
				System.out.println(m + " is a multiple of " + n + ".");
			}else {
				System.out.println(m + " is NOT a multiple of " + n + ".");
			}
		}
	}

}
