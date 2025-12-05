package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class a10025_게으른_백곰 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int[] field = new int[1000001];
        int[] dp = new int[1000001];
        int[] save = new int[1000001];
        String[] NK = bufferedReader.readLine().split(" ");
        int N = Integer.parseInt(NK[0]);
        int K = Integer.parseInt(NK[1]);
        for(int i = 0 ; i < N; i++){
            String[] gx = bufferedReader.readLine().split(" ");
            field[Integer.parseInt(gx[1])] = Integer.parseInt(gx[0]);
        }

        dp[0] = field[0];
        for(int i = 1; i < dp.length; i++){
            dp[i] = dp[i-1] + field[i];
        }

        for(int i = 0; i < save.length; i++){
            int min = Math.max(0, i - K);
            int max = Math.min(1_000_000, i + K);
            save[i] = dp[max] - dp[min] + field[min];
        }

        Arrays.sort(save);

        System.out.println(save[save.length-1]);
    }
}
