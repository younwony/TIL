package _0_Bronze.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2420_사파리월드 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        System.out.println(Math.abs(Long.parseLong(stringTokenizer.nextToken()) - Long.parseLong(stringTokenizer.nextToken())));
    }
}
