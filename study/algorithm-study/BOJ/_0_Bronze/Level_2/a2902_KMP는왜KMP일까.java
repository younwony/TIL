/**
 * @작성자 wony
 * @작성일 2020. 3. 14.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 14.
 * @사용처
 * @Todo
 */

public class a2902_KMP는왜KMP일까 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 14.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String[] str = scanner.nextLine().split("-");
		StringBuilder result = new StringBuilder();
		
		for(String data : str) {
			result.append(data.charAt(0));
		}
		
		scanner.close();
		
		System.out.println(result.toString());
	}

}
