package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class a2587_대표값2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int[] number = new int[5];
        int sum = 0;
        for (int i = 0; i < number.length; i++) {
            number[i] = Integer.parseInt(bufferedReader.readLine());
            sum += number[i];
        }

        Arrays.sort(number);

        System.out.println(sum/5);
        System.out.println(number[2]);

    }
}
