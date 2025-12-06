package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a19947_투자의_귀재_배주형 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int H = Integer.parseInt(stringTokenizer.nextToken());
        int Y = Integer.parseInt(stringTokenizer.nextToken());

        int[] dp = new int[16];
        dp[0] = H;
        for (int i = 0; i < 11; i++) {
            dp[i+1] = Math.max(dp[i+1], dp[i] + dp[i]*5/100);
            dp[i+3] = Math.max(dp[i+3], dp[i] + dp[i]*20/100);
            dp[i+5] = Math.max(dp[i+5], dp[i] + dp[i]*35/100);
        }

        System.out.print(dp[Y]);
    }
}