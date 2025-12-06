package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a11052_카드_구매하기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int[] cards = new int[n+1];
        int[] dp = new int[n+1];
        for(int i = 1; i <= n; i++){
            cards[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int i = 1; i <= n; i++) {
            dp[i] = cards[i];
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], dp[j] + dp[i - j]);
            }
        }

        System.out.println(dp[n]);
    }

}
