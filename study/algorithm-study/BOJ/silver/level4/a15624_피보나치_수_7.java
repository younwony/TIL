package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a15624_피보나치_수_7 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[] fibo = new int[1_000_001];
        int mod = 1_000_000_007;
        fibo[1] = 1;

        for(int i = 2; i <= n; i++){
            fibo[i] = (fibo[i - 2] + fibo[i - 1]) % mod;
        }

        System.out.println(fibo[n]);
    }
}
