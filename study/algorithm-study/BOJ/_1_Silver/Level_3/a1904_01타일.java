package _1_Silver.Level_3;

import java.util.Scanner;

public class a1904_01타일 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		scanner.close();
		
		int[] dp = new int[n+1];
		
		dp[1] = 1;
		dp[2] = 2;
		
		for(int i = 3; i <= n; i++){
			dp[i] = (dp[i-1] + dp[i-2])%15746;
		}
		
		System.out.println(dp[n]);
	}	
}
