package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a10164_격자상의_경로 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());
        int K = Integer.parseInt(stringTokenizer.nextToken());

        int[][] dp = new int[N+1][M+1];
        dp[0][1] = 1;

        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }

        int cnt = 0;
        if(K > 0){
            int kX = (K-1)/M + 1;
            int kY = (K-1)%M + 1;
            cnt = dp[kX][kY] * dp[N - kX + 1][M - kY + 1];
        }

        if(K == 0){
            cnt = dp[N][M];
        }

        System.out.println(cnt);

    }

}
