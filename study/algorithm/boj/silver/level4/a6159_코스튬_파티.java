package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a6159_코스튬_파티 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int S = Integer.parseInt(stringTokenizer.nextToken());

        int[] cows = new int[N];

        for (int i = 0; i < cows.length; i++) {
            cows[i] = Integer.parseInt(bufferedReader.readLine());
        }

        int cnt = 0;

        for (int i = 0; i < cows.length; i++) {
            for (int j = i + 1; j < cows.length; j++) {
                if(cows[i] + cows[j] <= S){
                    cnt++;
                }
            }
        }

        System.out.println(cnt);
    }
}
