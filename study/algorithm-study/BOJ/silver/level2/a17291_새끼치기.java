package _1_Silver.Level_2;

import java.util.Scanner;

public class a17291_새끼치기 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        int[] dp = new int[21];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 4;
        dp[4] = 7;
        for (int i = 5; i < N+1; i++) {
            dp[i] = dp[i-1]*2;
            if(i % 2 == 0){
                dp[i] -= dp[i-4] + dp[i-5];
            }
        }
        System.out.println(dp[N]);
    }

}
