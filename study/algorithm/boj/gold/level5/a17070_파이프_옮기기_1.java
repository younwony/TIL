package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a17070_파이프_옮기기_1 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        boolean[][] field = new boolean[n+1][n+1];
        int[][][] dp = new int[n+1][n+1][3];
        for(int i = 0; i < n; i++){
            String[] input = bufferedReader.readLine().split(" ");
            for(int j = 0; j < n; j++){
                field[i][j] = "0".equals(input[j]);
            }
        }

        // 0 가로, 1 세로, 2 대각선

        dp[0][1][0] = 1;

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(field[i][j+1]){
                    dp[i][j+1][0] += dp[i][j][0] + dp[i][j][2];
                }
                if(field[i+1][j]){
                    dp[i+1][j][1] += dp[i][j][1] + dp[i][j][2];
                }
                if(field[i+1][j+1] && field[i+1][j] && field[i][j+1]){
                    dp[i+1][j+1][2] += dp[i][j][0] + dp[i][j][1] + dp[i][j][2];
                }
            }
        }

        System.out.println(dp[n-1][n-1][0] + dp[n-1][n-1][1] + dp[n-1][n-1][2]);

    }
}
