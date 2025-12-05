/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 *
 */
package _0_Bronze.Level_5;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 * 
 */
public class a13277_큰수곱셈 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		BigInteger a = scanner.nextBigInteger();
		BigInteger b = scanner.nextBigInteger();
		scanner.close();
		System.out.println(a.multiply(b));

	}

}
