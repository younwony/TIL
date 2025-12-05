/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a2292_벌집 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		int x = 1;
		while(3*x*(x-1)+1 < n)x++;
		System.out.println(x);
	}

}
