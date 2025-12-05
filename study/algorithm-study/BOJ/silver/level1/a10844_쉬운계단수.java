package _1_Silver.Level_1;

import java.util.Scanner;

public class a10844_쉬운계단수 {

	static long[][] dp;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int mod = 1000000000;
		dp = new long[101][10];
		
		for(int i = 0; i < 9; i++){
			dp[1][i+1] = 1; 
		}
		
		for(int i = 2; i <= n; i++){
			for(int j = 0 ; j < 10; j++){
				if(j == 0){
					dp[i][j] = dp[i-1][j+1]%mod; 
				}else if(j == 9){
					dp[i][j] = dp[i-1][j-1]%mod;
				}else{
					dp[i][j] = (dp[i-1][j+1] + dp[i-1][j-1])%mod;
				}
			}
		}
		
		long sum = 0;
		
		for(int i = 0 ; i  < 10; i++){
			sum += dp[n][i];
		}
		
		System.out.println(sum%mod);
	}
}
