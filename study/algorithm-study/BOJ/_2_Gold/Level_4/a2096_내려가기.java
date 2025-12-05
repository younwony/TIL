package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2096_내려가기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[][][] dp = new int[n+1][2][3];
        dp[0][0][0] = 0;
        dp[0][0][1] = 0;
        dp[0][0][2] = 0;
        dp[0][1][0] = 0;
        dp[0][1][1] = 0;
        dp[0][1][2] = 0;
        for(int i = 1; i <= n; i++){
            String[] input = bufferedReader.readLine().split(" ");
            int one = Integer.parseInt(input[0]);
            int two = Integer.parseInt(input[1]);
            int three = Integer.parseInt(input[2]);
            dp[i][0][0] = Math.max(dp[i-1][0][0], dp[i-1][0][1]) + one;
            dp[i][0][1] = Math.max(dp[i-1][0][0], Math.max(dp[i-1][0][1], dp[i-1][0][2])) + two;
            dp[i][0][2] = Math.max(dp[i-1][0][1], dp[i-1][0][2]) + three;
            dp[i][1][0] = Math.min(dp[i-1][1][0], dp[i-1][1][1]) + one;
            dp[i][1][1] = Math.min(dp[i-1][1][0], Math.min(dp[i-1][1][1], dp[i-1][1][2])) + two;
            dp[i][1][2] = Math.min(dp[i-1][1][1], dp[i-1][1][2]) + three;
        }
        System.out.println(Math.max(dp[n][0][0],Math.max(dp[n][0][1], dp[n][0][2])) + " " + Math.min(dp[n][1][0],Math.min(dp[n][1][1], dp[n][1][2])));
    }
}
