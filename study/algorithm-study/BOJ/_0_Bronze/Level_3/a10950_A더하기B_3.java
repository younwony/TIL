/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a10950_A더하기B_3 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 A+B
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCaseCount,a,b;
		
		testCaseCount = scanner.nextInt();
		
		int[] result = new int[testCaseCount];
		
		for(int i = 0; i < result.length; i++){
			a = scanner.nextInt();
			b = scanner.nextInt();
			result[i] = a+b;
		}
		for(int i : result)
			System.out.println(i);
	}

}
