package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a14916_거스름돈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        int cnt = Integer.MAX_VALUE;

        int fiveMax = n/5;
        for(int i = 0; i <= fiveMax; i++){
            int temp = n - (5 * i);
            if(temp % 2 == 0){
                cnt = Math.min(cnt, i + temp/2);
            }
        }

        System.out.println(cnt == Integer.MAX_VALUE ? -1 : cnt);
    }
}
