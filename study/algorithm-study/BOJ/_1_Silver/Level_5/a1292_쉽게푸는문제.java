package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1292_쉽게푸는문제 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int start = Integer.parseInt(stringTokenizer.nextToken());
        int end = Integer.parseInt(stringTokenizer.nextToken());

        int maxIndex = 1_001;

        int[] dp = new int[maxIndex];
        int dpIndex = 1;

        mainLoop :
        while(true) {
            for (int i = 1; i < maxIndex; i++) {
                for (int j = 0; j < i; j++) {
                    dp[dpIndex] = dp[dpIndex - 1] + i;
                    dpIndex++;

                    if(dpIndex == maxIndex){
                        break mainLoop;
                    }
                }
            }
        }

        System.out.println(dp[end] - dp[start - 1]);
    }

}
