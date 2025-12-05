package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a14500_테트로미노 {
    static int[][] field;
    static boolean[][] visited;
    static int max;
    static int[] xArray = new int[]{-1,0,0,1};
    static int[] yArray = new int[]{0,-1,1,0};
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        field = new int[n][m];
        visited = new boolean[n][m];
        max = 0;

        for(int i = 0 ; i < n; i++){
            String[] mField = bufferedReader.readLine().split(" ");
            for(int j = 0 ; j < m; j++){
                field[i][j] = Integer.parseInt(mField[j]);
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                visited[i][j] = true;
                dfs(i,j,1, field[i][j]);
                centerTech(i,j);
                visited[i][j] = false;
            }
        }

        System.out.println(max);
    }

    private static void centerTech(int i, int j) {
        MainLoop :
        for(int k = 0 ; k < 4; k ++) {
            int sum = field[i][j];
            for (int index = 0; index < 4; index++) {
                if(index == k) continue;
                int nextI = i + xArray[index];
                int nextJ = j + yArray[index];

                if (!isField(nextI, nextJ)) continue MainLoop;
                sum += field[nextI][nextJ];
            }
            max = Math.max(sum, max);
        }
    }

    private static void dfs(int i, int j, int depth, int sum) {
        if(depth == 4){
            max = Math.max(max, sum);
            return;
        }

        for(int index = 0; index < 4; index++){
            int nextI = i + xArray[index];
            int nextJ = j + yArray[index];
            if(isField(nextI,nextJ)){
                if(visited[nextI][nextJ]) continue;
                visited[nextI][nextJ] = true;
                dfs(nextI, nextJ, depth+1, sum+field[nextI][nextJ]);
                visited[nextI][nextJ] = false;
            }
        }

    }

    private static boolean isField(int nextI, int nextJ) {
        return nextI >= 0 && nextJ >= 0 && nextI < field.length && nextJ < field[0].length;
    }
}
