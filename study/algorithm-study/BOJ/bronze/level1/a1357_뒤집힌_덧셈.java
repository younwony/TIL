package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1357_뒤집힌_덧셈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        String a = new StringBuilder(stringTokenizer.nextToken()).reverse().toString();
        String b = new StringBuilder(stringTokenizer.nextToken()).reverse().toString();
        String result = new StringBuilder(String.valueOf(Integer.parseInt(a) + Integer.parseInt(b))).reverse().toString();

        System.out.println(Integer.parseInt(result));
    }
}
