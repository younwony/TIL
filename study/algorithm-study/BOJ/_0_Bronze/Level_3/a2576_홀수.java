package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2576_홀수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int sum = 0;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < 7; i++) {
            int num = Integer.parseInt(bufferedReader.readLine());
            if(num % 2 == 1){
                sum += num;
                min = Math.min(min, num);
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
