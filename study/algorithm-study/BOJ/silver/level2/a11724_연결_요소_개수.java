package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a11724_연결_요소_개수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        int[][] field = new int[n+1][n+1];

        for(int i = 0 ; i< n; i++){
            field[i+1][i+1] = 1;
        }

        for(int i = 0; i < m; i++){
            String[] ab = bufferedReader.readLine().split(" ");
            int a = Integer.parseInt(ab[0]);
            int b = Integer.parseInt(ab[1]);
            field[a][b] = 1;
            field[b][a] = 1;
        }
        int answer = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if(field[i][j] == 0) continue;
                answer++;
                dfs(i,j, field);
            }
        }
        System.out.println(answer);
    }

    private static void dfs(int i, int j, int[][] field) {
        field[i][j] = 0;
        for(int k = 0; k < field.length; k++){
            if(field[j][k] == 0) continue;
            dfs(j, k,field );
        }
    }
}
