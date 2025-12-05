package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a5671_호텔_발_번호 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int[] dp = new int[5001];
        dp[0] = 1;
        for (int i = 1; i < dp.length; i++) {
            dp[i] = dp[i-1] + isDuplicate(i);
        }

        StringBuilder result = new StringBuilder();
        StringTokenizer stringTokenizer;
        String line;
        while((line = bufferedReader.readLine()) != null && !"".equals(line)){
            stringTokenizer = new StringTokenizer(line);
            int start = Integer.parseInt(stringTokenizer.nextToken());
            int end = Integer.parseInt(stringTokenizer.nextToken());
            int cnt = dp[end] - dp[start] + isDuplicate(start);

            result
                    .append(cnt)
                    .append("\n");
        }

        System.out.println(result.toString());
    }

    private static int isDuplicate(int number) {

        char[] chars = String.valueOf(number).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (int j = i+1; j < chars.length; j++) {
                if(chars[i] == chars[j]){
                    return 0;
                }
            }
        }

        return 1;
    }
}
