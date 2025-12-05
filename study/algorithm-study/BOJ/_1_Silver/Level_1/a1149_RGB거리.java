package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class a1149_RGB거리 {
	
	public static void main(String[] args) throws Exception{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int n =  Integer.valueOf(bufferedReader.readLine());
		int[][] dp = new int[1001][3];
		int[][] rgb = new int[1001][3];
		String[] rgbInput;
		
		for(int i = 0; i < n; i++){
			rgbInput = bufferedReader.readLine().split(" ");
			rgb[i][0] = Integer.valueOf(rgbInput[0]);
			rgb[i][1] = Integer.valueOf(rgbInput[1]);
			rgb[i][2] = Integer.valueOf(rgbInput[2]);
		}
		
		dp[0][0] = rgb[0][0];
		dp[0][1] = rgb[0][1];
		dp[0][2] = rgb[0][2];
		
		for(int i = 1; i <= n; i++){
			dp[i][0] = Math.min(dp[i-1][1], dp[i-1][2]) + rgb[i][0];
			dp[i][1] = Math.min(dp[i-1][0], dp[i-1][2]) + rgb[i][1];
			dp[i][2] = Math.min(dp[i-1][0], dp[i-1][1]) + rgb[i][2];
		}
		
		System.out.println(Math.min(Math.min(dp[n][0], dp[n][1]), dp[n][2]));
	}

}
