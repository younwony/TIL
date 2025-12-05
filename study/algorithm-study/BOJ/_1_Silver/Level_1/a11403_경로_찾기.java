package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11403_경로_찾기 {
    public static void main(String[] args) throws IOException {
        //given
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[][] field = new int[n][n];
        int[][] answer = new int[n][n];
        for (int i = 0; i < n; i++){
            String[] fieldArray = bufferedReader.readLine().split(" ");
            for (int j = 0; j < n; j++) {
                int input = Integer.parseInt(fieldArray[j]);
                field[i][j] = input;
                answer[i][j] = input;
            }
        }

        //when
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(field[i][j] == 1){
                    dfs(i, j, answer, field);
                }
            }
        }

        for (int[] ints : answer) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }

    }

    private static void dfs(int i, int j, int[][] answer, int[][] field) {
        if(i == j ) {
            answer[i][j] = 1;
            return;
        }
        answer[i][j] = 1;
        for(int k = 0; k < answer.length; k++){
            if(field[j][k] == 1 && answer[i][k] == 0){
                dfs(i, k, answer, field);
            }
        }
    }
}
