/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a1427_소트인사이드 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String[] number = scanner.nextLine().split("");
		scanner.close();
		Arrays.sort(number,Collections.reverseOrder());
		StringBuilder result = new StringBuilder();
		for(String str : number)
			result.append(str);
		System.out.println(result.toString());
	}

}
