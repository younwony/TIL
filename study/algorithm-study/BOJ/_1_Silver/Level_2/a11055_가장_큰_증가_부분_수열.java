package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a11055_가장_큰_증가_부분_수열 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        int[] numbers = new int[N];
        int[] dp = new int[N];

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        int max = 0;

        for (int i = 0; i < dp.length; i++) {
            dp[i] = numbers[i];
            for (int j = 0; j < i; j++) {
                if(numbers[j] < numbers[i] && dp[i] < dp[j] + numbers[i]){
                    dp[i] = dp[j] + numbers[i];
                }
            }
            max = Math.max(max,dp[i]);
        }

        System.out.println(max);
    }
}
