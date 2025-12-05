/**
 * @작성자 wony
 * @작성일 2020. 3. 2.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 2.
 * @사용처
 * @Todo
 */

public class a2417_정수제곱근 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 2.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		long number = scanner.nextLong();
		
		long result = (long)Math.sqrt(number);
		
		if(result*result != number) {result++;}
		
		System.out.println(result);
	}

}
