/**
 * @작성자 wony
 * @작성일 2020. 1. 27.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;


/**
 * @작성자 wony
 * @작성일 2020. 1. 27.
 * @사용처
 * @Todo
 */

public class a2702_초6수학 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 27.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		for(int i = 0; i < testCase; i++) {
			GCDandLCM(scanner.nextInt(), scanner.nextInt());
		}
		scanner.close();
	}
	
	public static void GCDandLCM(int a, int b) {
		int gcd = GCD(a, b);
		int lcm = (a * b) / gcd;
		System.out.println(lcm + " " + gcd);
	}
	
	public static int GCD(int a, int b) {
		return a == 0 ? b : GCD(b%a, a);
	}
}
