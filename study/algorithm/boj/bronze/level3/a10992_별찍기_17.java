package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10992_별찍기_17 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < n - 1; i++){
            for(int j = 1; j < n - i; j++){
                stringBuilder.append(" ");
            }

            for(int j = 0; j <= 2*i; j++){
                stringBuilder.append(j == 0 || j == 2*i ? "*" : " ");
            }

            stringBuilder.append(System.lineSeparator());
        }

        for(int i = 0; i < 2 * n - 1; i++){
            stringBuilder.append("*");
        }
        System.out.println(stringBuilder);
    }
}
