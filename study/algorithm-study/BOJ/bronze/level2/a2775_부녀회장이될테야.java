/**
 * @작성자 wony
 * @작성일 2019. 12. 24.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 24.
 * @사용처
 * @Todo
 */

public class a2775_부녀회장이될테야 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 24.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int k,n;
		
		for(int i = 0; i < testCount; i++)
			System.out.println(recurI(scanner.nextInt(),scanner.nextInt()));
		
		scanner.close();
	}
	
	public static int recurI(int k, int n) {
		if(k == 0) return n;
		if(n == 1) return 1;
		
		return recurI(k-1, n) + recurI(k, n-1);
	}

}
