package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11931_수_정렬하기_4 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        final int MAX_ABS = 1_000_000;
        boolean[] numbers = new boolean[MAX_ABS * 2 + 1];

        int n = Integer.parseInt(bufferedReader.readLine());

        while(n-- > 0){
            int index = Integer.parseInt(bufferedReader.readLine()) + MAX_ABS;
            numbers[index] = true;
        }

        StringBuilder result = new StringBuilder();

        for (int i = numbers.length - 1; i >= 0; i--) {
            if(numbers[i]){
                result
                        .append(i - MAX_ABS)
                        .append("\n");
            }
        }

        System.out.println(result.toString());

    }
}
