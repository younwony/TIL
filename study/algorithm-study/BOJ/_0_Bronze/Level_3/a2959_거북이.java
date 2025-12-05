/**
 * @작성자 wony
 * @작성일 2020. 5. 2.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_3;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 5. 2.
 * @사용처
 * @Todo
 */

public class a2959_거북이 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 2.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int[] nArray = new int[4];
		for(int i = 0; i < 4; i++) {
			nArray[i] = scanner.nextInt();
		}
		Arrays.sort(nArray);
		System.out.println(nArray[0] * nArray[2]);
	}

}
