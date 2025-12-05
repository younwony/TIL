/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 *
 */
package _0_Bronze.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 * 
 */
public class a14652_나는행복합니다 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		int result = scanner.nextInt();
		scanner.close();
		
		int x,y;
		x = result / m;
		y = result - x*m;
		
		System.out.println(x + " " + y);
	}

}
