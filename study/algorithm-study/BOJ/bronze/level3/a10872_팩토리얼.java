/**
 * @작성자 wony
 * @작성일 2019. 10. 20.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 20.
 * @사용처
 * @Todo
 */

public class a10872_팩토리얼 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 20.
	 * @사용처 factorial
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int facNum = scanner.nextInt();
		int result = 1;
		while(facNum>0) {
			result *= facNum;
			facNum--;
		}
		
		System.out.println(result);
	}

}
