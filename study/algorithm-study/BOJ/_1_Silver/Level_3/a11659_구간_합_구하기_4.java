package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11659_구간_합_구하기_4 {
    public static void main(String[] args) throws IOException {
        //given
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        int[] numArray = new int[n];
        int[] dp = new int[n+1];
        dp[0] = 0;
        String[] inputNum = bufferedReader.readLine().split(" ");
        for(int i = 0; i < n; i++){
            numArray[i] = Integer.parseInt(inputNum[i]);
            dp[i+1] = dp[i] + numArray[i];
        }

        //when
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < m; i++){
            String[] atoz = bufferedReader.readLine().split(" ");
            int a = Integer.parseInt(atoz[0]) - 1;
            int z = Integer.parseInt(atoz[1]);

            stringBuilder.append(dp[z] - dp[a]).append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

}
