package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class a9461_파도반수열 {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int testCnt = Integer.valueOf(bufferedReader.readLine());
		
		StringBuilder result = new StringBuilder();
		
		long[] dp = new long[101];
		dp[1] = 1;
		dp[2] = 1;
		dp[3] = 1;
		dp[4] = 2;
		dp[5] = 2;
		
		for(int i = 6; i < 101; i++){
			dp[i] = dp[i-1] + dp[i-5];
		}
		
		for(int i = 0; i < testCnt; i++){
			result.append(dp[Integer.valueOf(bufferedReader.readLine())]).append("\n");
		}
		
		System.out.println(result.substring(0, result.length() - 1).toString());
	}

}
