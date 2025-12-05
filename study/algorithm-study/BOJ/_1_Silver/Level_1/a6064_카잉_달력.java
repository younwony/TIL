package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a6064_카잉_달력 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int k = Integer.parseInt(bufferedReader.readLine());

        for(int i =0 ; i < k; i++){
            String[] input = bufferedReader.readLine().split(" ");
            int m = Integer.parseInt(input[0]);
            int n = Integer.parseInt(input[1]);
            int x = Integer.parseInt(input[2]);
            int y = Integer.parseInt(input[3]);

            int xa = 0;
            while(true){
                int a = m * xa + x;
                int b = a%n;
                if(b == 0) b = n;

                if(y == b){
                    System.out.println(a);
                    break;
                }
                if(a > m*n || a < 0){
                    System.out.println(-1);
                    break;
                }
                xa++;
            }
        }
    }
}
