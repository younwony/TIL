/**
 * @작성자 wony
 * @작성일 2020. 3. 15.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 15.
 * @사용처
 * @Todo
 */

public class a10886_cute {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 15.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int[] countArray = new int[2];
		
		for(int i = 0 ; i < testCount; i++) {
			countArray[scanner.nextInt()]++;
		}
		
		if(countArray[0] > countArray[1]) {
			System.out.println("Junhee is not cute!");
		}else {
			System.out.println("Junhee is cute!");
			
		}
	}

}
