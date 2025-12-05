package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2644_촌수계산 {

    private static int len = -1;
    private static int b;
    private static boolean[][] field;
    private static boolean[] visited;
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer;
        int n = Integer.parseInt(bufferedReader.readLine());
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int a = Integer.parseInt(stringTokenizer.nextToken());
        b = Integer.parseInt(stringTokenizer.nextToken());

        int m = Integer.parseInt(bufferedReader.readLine());
        field = new boolean[n + 1][n + 1];
        visited = new boolean[n + 1];

        for(int i = 0; i < m; i++){
            stringTokenizer =  new StringTokenizer(bufferedReader.readLine());
            int x = Integer.parseInt(stringTokenizer.nextToken());
            int y = Integer.parseInt(stringTokenizer.nextToken());
            field[x][y] = true;
            field[y][x] = true;
        }

        visited[a] = true;

        dfs(a, 0);

        System.out.println(len);
    }

    private static void dfs(int node, int depth) {
        if(node == b){
            len = depth;
            return;
        }

        for(int i = 0; i < field.length; i++){
            if(field[node][i] && !visited[i]){
                visited[i] = true;
                dfs(i, depth + 1);
            }
        }
    }
}
