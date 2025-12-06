package _0_Bronze.Level_1;

import java.util.Scanner;

public class a24416_알고리즘_수업_피보나치_수1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] dp = new int[41];
        dp[1] = 1;
        dp[2] = 1;
        for(int i = 3; i < 41; i++){
            dp[i] = dp[i - 2] + dp[i - 1];
        }

        for(int i = 1; i < 41; i++){
            dp[i] += dp[i - 1];
        }


        System.out.print(dp[n - 2] + 1 + " " + (n - 2));
    }
}
