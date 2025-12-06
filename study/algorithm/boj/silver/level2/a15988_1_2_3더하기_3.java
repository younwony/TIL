package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a15988_1_2_3더하기_3 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int MOD = 1_000_000_009;
        int T = Integer.parseInt(bufferedReader.readLine());

        int[] dp = new int[1_000_001];

        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 4;

        for (int i = 4; i < dp.length; i++) {
            dp[i] = ((dp[i-3] + dp[i-2])%MOD + dp[i-1]) % MOD;
        }

        StringBuilder stringBuilder = new StringBuilder();
        while(T-- > 0){
            stringBuilder
                    .append(dp[Integer.parseInt(bufferedReader.readLine())])
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
