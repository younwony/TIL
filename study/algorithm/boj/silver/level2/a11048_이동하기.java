package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a11048_이동하기 {
    private static int[] xMove = {0,1};
    private static int[] yMove = {1,0};
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int[][] field = new int[n][m];
        int[][] dp = new int[n][m];
        for(int i = 0; i < n; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for(int j = 0; j < m; j++){
                field[i][j] = Integer.parseInt(stringTokenizer.nextToken());
                dp[i][j] = field[i][j];
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                for(int k = 0; k < xMove.length; k++){
                    int x = i + xMove[k];
                    int y = j + yMove[k];
                    if(x < n && y < m){
                        dp[x][y] = Math.max(dp[x][y], field[x][y] + dp[i][j]);
                    }
                }
            }
        }

        System.out.println(dp[n - 1][m - 1]);
    }
}
