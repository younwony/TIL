package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11660_구간_합_구하기_5 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);

        long[][] dp = new long[n+1][n+1];

        for (int i = 1; i <= n; i++){
            String[] inputN = bufferedReader.readLine().split(" ");
            for(int j = 1; j<= n ; j++){
                int input = Integer.parseInt(inputN[j-1]);
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + input;
            }
        }

        StringBuilder result = new StringBuilder();
        //when
        for(int i = 0; i < m; i++){
            String[] x1y1x2y2 = bufferedReader.readLine().split(" ");
            int x1 = Integer.parseInt(x1y1x2y2[0]);
            int y1 = Integer.parseInt(x1y1x2y2[1]);
            int x2 = Integer.parseInt(x1y1x2y2[2]);
            int y2 = Integer.parseInt(x1y1x2y2[3]);

            long resultValue = dp[x2][y2] - dp[x2][y1-1] - dp[x1-1][y2] + dp[x1-1][y1-1];
            result.append(resultValue).append("\n");
        }

        System.out.println(result.toString());
    }
}
