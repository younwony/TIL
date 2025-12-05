package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a5596_시험_점수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(Math.max(sum(bufferedReader.readLine()), sum(bufferedReader.readLine())));
    }

    private static int sum(String line) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        int sum = 0;
        while(stringTokenizer.hasMoreTokens()){
            sum += Integer.parseInt(stringTokenizer.nextToken());
        }
        return sum;
    }
}
