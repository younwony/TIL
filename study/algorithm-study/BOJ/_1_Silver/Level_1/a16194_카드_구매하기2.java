package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a16194_카드_구매하기2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());

        int[] cards = new int[N + 1];
        int[] dp = new int[N + 1];

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for (int i = 1; i < cards.length; i++) {
            cards[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int i = 1; i < dp.length; i++) {
            int now = cards[i];
            for (int j = 1; j < i; j++) {
                now = Math.min(now, dp[j] + dp[i - j]);
            }
            dp[i] = now;
        }

        System.out.println(dp[N]);

    }

}
