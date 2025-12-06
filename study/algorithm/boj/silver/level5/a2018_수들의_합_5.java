package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2018_수들의_합_5 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        int cnt = 0;

        int maxNumber = n / 2 + 1;
        for(int i = 1; i <= maxNumber; i++){
            int sum = 0;
            for(int j = i; j <= maxNumber; j++){
                sum += j;
                if(sum == n){
                    cnt++;
                    break;
                }

                if(sum > n){
                    break;
                }
            }
        }

        System.out.println(cnt + 1);
    }
}
