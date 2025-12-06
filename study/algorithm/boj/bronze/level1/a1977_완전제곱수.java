package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class a1977_완전제곱수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int m = Integer.parseInt(bufferedReader.readLine());
        int n = Integer.parseInt(bufferedReader.readLine());

        final int MIN = 1;
        final int MAX = 100_000_000;

        boolean[] pows = new boolean[MAX + 1];
        for(int i = MIN; Math.pow(i,2) < MAX; i++){
            pows[(int) Math.pow(i,2)] = true;
        }

        int min = Integer.MAX_VALUE;
        int sum = 0;
        for(int i = m; i <= n; i++){
            if(pows[i]){
                if(min == Integer.MAX_VALUE){
                    min = i;
                }
                sum += i;
            }
        }

        if(sum == 0){
            System.out.println(-1);
        }else{
            System.out.println(sum);
            System.out.println(min);
        }
    }
}
