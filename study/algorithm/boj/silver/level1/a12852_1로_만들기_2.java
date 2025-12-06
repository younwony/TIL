package _1_Silver.Level_1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a12852_1로_만들기_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] dp = new int[n+1];
        int[] path = new int[n+1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[1] = 0;
        for(int i = 2; i <= n; i++){
            if(i % 3 == 0){
                if(dp[i/3] + 1 < dp[i]){
                    dp[i] = dp[i/3] + 1;
                    path[i] = i/3;
                }
            }
            if(i % 2 == 0){
                if(dp[i/2] + 1 < dp[i]){
                    dp[i] = dp[i/2] + 1;
                    path[i] = i/2;
                }
            }

            if(dp[i-1] + 1 < dp[i]){
                dp[i] = dp[i-1] + 1;
                path[i] = i-1;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(dp[n])
              .append("\n");
        result.append(n).append(" ");
        while(n > 1){
            result.append(path[n]).append(" ");
            n = path[n];
        }
        System.out.println(result.toString());
    }
}
