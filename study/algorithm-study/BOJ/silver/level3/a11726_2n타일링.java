/**
 * @작성자 wony
 * @작성일 2020. 1. 29.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 29.
 * @사용처
 * @Todo
 */

public class a11726_2n타일링 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 29.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	static int[] dp;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		dp = new int[n+1];
		dp[0] = 1;
		dp[1] = 1;
		
		dp(n);
		
		System.out.println(dp[n]);
		scanner.close();
	}
	
	public static void dp(int n) {
		for(int i = 2; i <= n; i++) {
			dp[i] = (dp[i-1] + dp[i-2])%10007;
		}
	}

}
