/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a3053_택시기하학 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int r = scanner.nextInt();
		scanner.close();
		System.out.println(String.format("%.6f",r * r * Math.PI));
		System.out.println(String.format("%.6f",r * r * 2.0));
	}

}
