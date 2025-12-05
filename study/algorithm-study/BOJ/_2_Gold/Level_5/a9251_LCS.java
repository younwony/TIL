package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a9251_LCS {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String n = bufferedReader.readLine();
        String m = bufferedReader.readLine();

        int[][] dp = new int[n.length()+1][m.length()+1];

        for(int i = 1; i < dp.length; i++){
            char nC = n.charAt(i-1);
            for(int j = 1; j < dp[0].length; j++){
                char mC = m.charAt(j-1);
                /* LCS 중요 로직
                *  1. 비교 대상이 같을 경우 대각선 위의 값 + 1
                *  2. 다를 경우 각 위치에서 i 축 -1 값 과 j축 -1 값의 최대값을 입력한다.
                * */
                dp[i][j] = nC == mC ? dp[i-1][j-1] + 1 : Math.max(dp[i-1][j],dp[i][j-1]);
            }
        }

        System.out.println(dp[n.length()][m.length()]);
    }
}
