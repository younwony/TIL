package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10988_팰린드롬인지_확인하기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder stringBuilder = new StringBuilder(bufferedReader.readLine());
        System.out.println(stringBuilder.toString().equals(stringBuilder.reverse().toString()) ? 1 : 0);
    }
}
