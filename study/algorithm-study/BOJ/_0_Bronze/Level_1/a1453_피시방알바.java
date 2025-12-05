/**
 * @작성자 wony
 * @작성일 2020. 5. 5.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 5. 5.
 * @사용처
 * @Todo
 */

public class a1453_피시방알바 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 5.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		boolean[] isEmpty = new boolean[101];
		
		int number;
		
		int count = 0;
		
		for(int i = 0; i< testCase; i++) {
			number = scanner.nextInt();
			if(!isEmpty[number -1]) {
				isEmpty[number -1] = true;
			}else {
				count++;
			}
		}
		System.out.println(count);
	}

}
