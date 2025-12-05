package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a17484_진우의_달_여행_Small {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int[][] field = new int[n][m];

        for (int i = 0; i < n; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < m; j++) {
                field[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        int[][][] dp = new int[n][m][3];
        for(int i = 0; i < m; i++){
            dp[0][i][0] = field[0][i];
            dp[0][i][1] = field[0][i];
            dp[0][i][2] = field[0][i];
        }

        for(int i = 0; i < n; i++){
            dp[i][0][0] = Integer.MAX_VALUE;
            dp[i][m-1][2] = Integer.MAX_VALUE;
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j][1] = Math.min(dp[i-1][j][0], dp[i-1][j][2]) + field[i][j];
                if(j == 0){
                    dp[i][j][2] = Math.min(dp[i-1][j+1][0], dp[i-1][j+1][1]) + field[i][j];
                }else if(j == m - 1){
                    dp[i][j][0] = Math.min(dp[i-1][j-1][1], dp[i-1][j-1][2]) + field[i][j];
                }else{
                    dp[i][j][0] = Math.min(dp[i-1][j-1][1], dp[i-1][j-1][2]) + field[i][j];
                    dp[i][j][2] = Math.min(dp[i-1][j+1][0], dp[i-1][j+1][1]) + field[i][j];
                }
            }
        }

        int result = Integer.MAX_VALUE;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                 result = Math.min(result, dp[n-1][i][j]);
            }
        }

        System.out.println(result);
    }
}
