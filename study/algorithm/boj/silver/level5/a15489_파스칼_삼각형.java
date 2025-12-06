package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a15489_파스칼_삼각형 {
    static int[][] field = new int[31][31];
    public static void main(String[] args) throws IOException {
        setField();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int R = Integer.parseInt(stringTokenizer.nextToken());
        int C = Integer.parseInt(stringTokenizer.nextToken());
        int W = Integer.parseInt(stringTokenizer.nextToken());

        int sum = 0;
        int index = 1;
        for(int i = R-1; i < R + W - 1; i++){
            for(int j = 0; j < index; j++){
                int nextJ = C - 1 + j;
                sum += field[i][nextJ];
            }
            index++;
        }

        System.out.println(sum);


    }

    private static void setField() {
        for (int i = 0; i < field.length; i++) {
            field[i][0] = 1;
            field[i][i] = 1;
        }

        for (int i = 2; i < field.length; i++) {
            for (int j = 1; j < i; j++) {
                field[i][j] = field[i-1][j-1] + field[i-1][j];
            }
        }
    }
}
