package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11170_0의_개수 {

    static int[] dp = new int[1000001];
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        setDp();

        int testCase = Integer.parseInt(bufferedReader.readLine());

        StringBuilder result = new StringBuilder();
        for(int i = 0; i < testCase; i++){
            String[] NM = bufferedReader.readLine().split(" ");
            int N = Integer.parseInt(NM[0]);
            int M = Integer.parseInt(NM[1]);
            result.append(dp[M] - dp[N] + isZero(N)).append("\n");
        }

        System.out.println(result.toString());
    }

    private static void setDp() {
        dp[0] = 1;
        for(int i = 1; i < dp.length; i++){
            dp[i] = dp[i-1] + isZero(i);
        }
    }

    private static int isZero(int i) {
        String str = String.valueOf(i);

        return str.length() - str.replace("0","").length();
    }
}
