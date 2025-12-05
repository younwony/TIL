package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2670_연속부분최대곱 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());

        double[] dp = new double[N];
        dp[0] = Double.parseDouble(bufferedReader.readLine());
        double max = dp[0];
        for (int i = 1; i < dp.length; i++) {
            double input = Double.parseDouble(bufferedReader.readLine());
            dp[i] = Math.max(dp[i-1] * input, input);
            max = Math.max(max, dp[i]);
        }

        System.out.println(String.format("%.3f", max));

    }
}
