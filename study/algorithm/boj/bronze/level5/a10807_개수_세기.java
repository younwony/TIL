package _0_Bronze.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a10807_개수_세기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[] numbers = new int[201];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int question = Integer.parseInt(bufferedReader.readLine());

        while(stringTokenizer.hasMoreTokens()){
            numbers[Integer.parseInt(stringTokenizer.nextToken()) + 100]++;
        }

        System.out.println(numbers[question + 100]);
    }
}
