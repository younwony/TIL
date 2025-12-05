package _1_Silver.Level_3;

import java.util.Scanner;

public class a2579_계단오르기 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[] floor = new int[301];

        for(int i = 0; i< n; i++){
        	floor[i] = scanner.nextInt();
        }
        
        int[][] dp = new int[301][2];
        
        // 2번째 연속 체크 숫자 0 = 직전 연속 x, 1 = 직전 연속 o
        dp[0][0] = floor[0];
        dp[0][1] = 0;
        dp[1][0] = floor[1];
        dp[1][1] = dp[0][0] + floor[1];
        
        for(int i = 2 ; i < n; i++){
        	dp[i][0] = Math.max(dp[i-2][0],dp[i-2][1]) + floor[i];
        	dp[i][1] = dp[i-1][0] + floor[i];
        }
        
        System.out.println(Math.max(dp[n-1][0],dp[n-1][1]));

    }

}
