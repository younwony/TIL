/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 * 
 */
public class a11050_이항계수1 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int r = scanner.nextInt();
		scanner.close();
		int a = 1;
		int b = 1;
		
		for(int i = 0; i < r; i++){
			a *= n-i;
		}
		for(int i = 0; i < r; i++){
			b *= r-i;
		}
		
		System.out.println(a/b);
	}

}
