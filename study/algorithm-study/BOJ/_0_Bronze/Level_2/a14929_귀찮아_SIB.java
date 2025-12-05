package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a14929_귀찮아_SIB {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        int[] numbers = new int[n];
        int[] sums = new int[n];

        int sum = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
            sum += numbers[i];
            sums[i] = sum;
        }

        long result = 0;
        for (int i = 0; i < numbers.length; i++) {
             result += numbers[i] * (sums[n-1] - sums[i]);
        }

        System.out.println(result);
    }
}
