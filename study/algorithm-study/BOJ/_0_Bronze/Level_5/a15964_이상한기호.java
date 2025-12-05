/**
 * @작성자 wony
 * @작성일 2019. 12. 16.
 * @사용처
 *
 */
package _0_Bronze.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 16.
 * @사용처
 * 
 */
public class a15964_이상한기호 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 16.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		long a = scanner.nextInt();
		long b = scanner.nextInt();
		scanner.close();
		System.out.println((a+b)*(a-b));
	}

}
