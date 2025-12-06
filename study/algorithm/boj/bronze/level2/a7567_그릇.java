package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a7567_그릇 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        char[] input = bufferedReader.readLine().toCharArray();
        int length = 10;

        for(int i = 1; i < input.length; i++){
            length += input[i-1] == input[i] ? 5 : 10;
        }

        System.out.println(length);
    }
}
