package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class a14502_연구소 {

    static int[][] field;
    static boolean[][] visited;
    static ArrayList<Point> virus = new ArrayList<>();
    static int[] xArray = new int[]{-1,0,0,1};
    static int[] yArray = new int[]{0,-1,1,0};
    static int max = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        field = new int[n][m];
        visited = new boolean[n][m];

        for(int i = 0; i < n ; i++){
            String[] line = bufferedReader.readLine().split(" ");
            for(int j = 0; j < m; j++){
                field[i][j] = Integer.parseInt(line[j]);
                if(field[i][j] == 2){
                    virus.add(new Point(i,j));
                }
            }
        }

        dfs(0);

        System.out.println(max);

    }

    private static boolean isField(int nextX, int nextY) {
        return nextX >= 0 && nextY >= 0 && nextX < field.length && nextY < field[0].length;
    }

    private static void dfs(int depth) {
        if(depth == 3){
            bfs();
            return;
        }

        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[0].length; j++){
                if(field[i][j] == 0 && !visited[i][j]){
                    visited[i][j] = true;
                    field[i][j] = 3;
                    dfs(depth+1);
                    visited[i][j] = false;
                    field[i][j] = 0;
                }
            }
        }
    }

    private static void bfs() {
        Queue<Point> queue = new LinkedList<>();
        for (Point point : virus) {
            queue.add(new Point(point.x, point.y));
            while(!queue.isEmpty()){
                Point virusPoint = queue.poll();
                for(int i = 0 ; i < 4; i++){
                    int nextX = virusPoint.x + xArray[i];
                    int nextY = virusPoint.y + yArray[i];
                    if(isField(nextX,nextY) && field[nextX][nextY] == 0){
                        field[nextX][nextY] = 2;
                        queue.add(new Point(nextX, nextY));
                    }
                }
            }
        }

        /* 바이러스 영역 다시 안전영역으로 변경 */
        int safeCnt = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if(field[i][j] == 0) safeCnt++;
                if(field[i][j] == 2) field[i][j] = 0;
            }
        }

        max = Math.max(max, safeCnt);

        /* 바이러스 초기화*/
        for (Point point : virus) {
            field[point.x][point.y] = 2;
        }

    }

    static class Point{
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
