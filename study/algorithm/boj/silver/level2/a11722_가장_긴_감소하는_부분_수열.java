package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class a11722_가장_긴_감소하는_부분_수열 {
    /**
     * dp 에 덮어 씌우기
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int[] numbers = new int[n];
        int[] dp = new int[n];
        for(int i = 0; i < n; i++){
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
            for(int j = 0; j < n; j++){
                if(dp[j] <= numbers[i]){
                    dp[j] = numbers[i];
                    break;
                }
            }
        }

        int len = n;
        for(int i = 0; i < n; i++){
            if(dp[i] == 0){
                len = i;
                break;
            }
        }
        System.out.println(len);
    }

    /**
     * Solve 1
     * 앞선 dp 전부 검색
     */
    /*public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int[] numbers = new int[n];
        int[] dp = new int[n];
        for(int i = n - 1; i >= 0; i--){
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        int maxLen = 0;

        for(int i = 0 ; i < n; i++){
            for(int j = 0; j < i; j++){
                if(numbers[j] < numbers[i]){
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    maxLen = Math.max(maxLen, dp[i]);
                }
            }
        }

        System.out.println(maxLen + 1);

    }*/
}

