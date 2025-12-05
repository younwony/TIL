package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a15658_연산자_끼워넣기_2 {
    static int[] numbers;
    static int[] signs = new int[4];
    static int max = Integer.MIN_VALUE;
    static int min = Integer.MAX_VALUE;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine()); // 2 ~ 11

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        numbers = new int[N];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < signs.length; i++) {
            signs[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        dfs(1, numbers[0], 1);

        System.out.println(max);
        System.out.println(min);

    }

    private static void dfs(int cnt, int sum, int index) {
        if(cnt == numbers.length){
            max = Math.max(max, sum);
            min = Math.min(min, sum);
            return;
        }

        for (int j = 0; j < signs.length; j++) {
            if(signs[j] > 0){
                signs[j]--;
                dfs(cnt + 1, calculate(sum, numbers[index], j), index + 1);
                signs[j]++;
            }
        }

    }

    // +, -, *, /
    private static int calculate(int sum, int number, int sign) {
        switch (sign){
            case 0 : return sum + number;
            case 1 : return sum - number;
            case 2 : return sum * number;
            case 3 : return sum / number;
        }
        return 0;
    }


}
