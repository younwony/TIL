package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11057_오르막수 {
    private static int MOD_NUMBER = 10_007;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[][] dp = new int[n + 1][10];
        for(int i = 0; i < 10; i ++){
            dp[1][i] = 1;
        }
        for(int i = 1; i < n; i++){
            for(int j = 0; j < 10; j++){
                for(int k = j; k < 10; k++){
                    dp[i + 1][k] += dp[i][j];
                    dp[i][j] %= MOD_NUMBER;
                }
            }
        }

        int sum = 0;
        for(int i = 0 ; i < 10; i++){
            sum += dp[n][i] % MOD_NUMBER;
        }

        System.out.println(sum % MOD_NUMBER);
    }
}
