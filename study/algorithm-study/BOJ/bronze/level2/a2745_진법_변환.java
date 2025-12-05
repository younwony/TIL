package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2745_진법_변환 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        String number = stringTokenizer.nextToken();
        int base = Integer.parseInt(stringTokenizer.nextToken());
        System.out.println(Integer.parseInt(number, base));
    }
}
