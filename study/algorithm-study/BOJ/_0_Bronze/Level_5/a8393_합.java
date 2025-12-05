/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a8393_합 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 !a(a factorial)
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		
		int a;
		a = scanner.nextInt();
		
		int result = 1;
		
		for(; a>1; a--)
			result *= a;
		
		System.out.println(result);
	}

}
