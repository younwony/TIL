package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9084_동전 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCnt = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();
        for(int i = 0 ; i < testCnt; i++){
            int coinKind = Integer.parseInt(bufferedReader.readLine());
            int[] coins = new int[coinKind];
            StringTokenizer coin = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < coins.length; j++) {
                coins[j] = Integer.parseInt(coin.nextToken());
            }

            int price = Integer.parseInt(bufferedReader.readLine());
            int[] dp = new int[20001];
            dp[0] = 1;

            for (int c : coins) {
                for (int j = c; j < dp.length; j++) {
                    dp[j] += dp[j-c];
                }
            }

            result.append(dp[price]).append("\n");
        }

        System.out.println(result.toString());
    }
}
