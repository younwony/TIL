package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a15665_N과_M_11 {

    static int M;
    static int[] numbers, result;
    static StringBuilder stringBuilder = new StringBuilder();
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        M = Integer.parseInt(stringTokenizer.nextToken());
        numbers = new int[N];
        result = new int[M];
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        Arrays.sort(numbers);

        dfs(0);

        System.out.println(stringBuilder.toString());
    }

    private static void dfs(int cnt) {
        if(cnt == M){
            for (int i : result) {
                stringBuilder
                        .append(i)
                        .append(" ");
            }
            stringBuilder.append("\n");
            return;
        }

        int before = 0;
        for (int i = 0; i < numbers.length; i++) {
            if(before == numbers[i]) {
                continue;
            }
            result[cnt] = numbers[i];
            dfs( cnt + 1);
            before = numbers[i];
        }
    }
}
