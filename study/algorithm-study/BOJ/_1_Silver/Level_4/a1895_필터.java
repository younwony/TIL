package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a1895_필터 {
    private static int[][] field;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int r = Integer.parseInt(stringTokenizer.nextToken());
        int c = Integer.parseInt(stringTokenizer.nextToken());

        field = new int[r][c];

        for (int i = 0; i < r; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < c; j++) {
                field[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        int T = Integer.parseInt(bufferedReader.readLine());
        int result = 0;
        //when

        for(int i = 0; i <= r -3; i++){
            for(int j = 0; j <= c -3; j++){
                if(getMiddle(i,j) >= T){
                    result++;
                }
            }
        }
        System.out.println(result);
    }

    private static Integer getMiddle(int i, int j) {
        int[] middles = new int[9];
        int index = 0;
        for(int x = i; x < i + 3; x++){
            for(int y = j; y < j + 3; y++){
                middles[index++] = field[x][y];
            }
        }
        Arrays.sort(middles);
        return middles[4];
    }
}
