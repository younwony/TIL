package _1_Silver.Level_3;

import java.util.Scanner;

public class a11727_2xN_타일링_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] dp = new int[1001];
        dp[1] = 1;
        dp[2] = 3;
        for(int i = 3; i<= n; i++){
            dp[i] = (dp[i-2]*2 + dp[i-1])%10007;
        }
        System.out.println(dp[n]);
    }
}
