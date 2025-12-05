package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a3085_사탕_게임 {
    private static String[][] field;
    private static int N;
    private static int MAX;
    private static int[] xArray = {-1,0,0,1};
    private static int[] yArray = {0,-1,1,0};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(bufferedReader.readLine());
        field = new String[N][N];

        for (int i = 0; i < N; i++) {
            field[i] = bufferedReader.readLine().split("");
        }

        solved();

        System.out.println(MAX);
    }

    private static void solved() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                changeField(i,j);
            }
        }
    }

    private static void changeField(int x, int y) {

        for (int i = 0; i < 4; i++) {
            int nextX = x + xArray[i];
            int nextY = y + yArray[i];

            if(isField(nextX,nextY)){
                swapField(x, y, nextX, nextY);

                compareLenFiled(x, y);

                swapField(x, y, nextX, nextY);
            }
        }

    }

    private static void swapField(int x, int y, int nextX, int nextY) {
        String temp = field[x][y];
        field[x][y] = field[nextX][nextY];
        field[nextX][nextY] = temp;
    }

    private static void compareLenFiled(int x, int y) {
        int len = 1;

        String pre = field[x][0];
        for(int i = 1; i < N; i++){
            if(pre.equals(field[x][i])){
                len++;
            }else{
                MAX = Math.max(MAX, len);
                len = 1;
            }
            pre = field[x][i];
        }

        MAX = Math.max(MAX, len);

        len = 1;
        pre = field[0][y];
        for(int i = 1; i < N; i++){
            if(pre.equals(field[i][y])){
                len++;
            }else{
                MAX = Math.max(MAX, len);
                len = 1;
            }
            pre = field[i][y];
        }

        MAX = Math.max(MAX, len);
    }

    private static boolean isField(int nextX, int nextY) {
        return nextX >= 0 && nextY >= 0 && nextX < N && nextY < N;
    }
}
