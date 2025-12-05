package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2740_행렬_곱셈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int[][] a = new int[Integer.parseInt(stringTokenizer.nextToken())][Integer.parseInt(stringTokenizer.nextToken())];
        for (int i = 0; i < a.length; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int[][] b = new int[Integer.parseInt(stringTokenizer.nextToken())][Integer.parseInt(stringTokenizer.nextToken())];
        for (int i = 0; i < b.length; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        int sum = 0;
        for (int i = 0; i < a.length; i++) {
            for(int j = 0; j < b[0].length; j++){
                sum = 0;
                for(int k = 0; k < a[0].length; k++){
                    sum += a[i][k] * b[k][j];
                }
                stringBuilder
                        .append(sum)
                        .append(" ");
            }
            stringBuilder
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
