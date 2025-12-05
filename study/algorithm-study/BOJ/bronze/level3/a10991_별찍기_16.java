package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *   *
 *  * *
 * * * *
 */
public class a10991_별찍기_16 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringBuilder output = new StringBuilder();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n - i - 1; j++){
                output.append(" ");
            }
            for(int j = 0; j <= 2 * i; j++){
                output.append(j % 2 == 0 ? "*" : " ");
            }
            output.append(System.lineSeparator());
        }

        System.out.println(output);
    }
}
