package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class a17086_아기상어_2 {

    static int[] xArray = {-1,-1,-1,0,0,0,1,1,1};
    static int[] yArray = {-1,0,1,-1,0,1,-1,0,1};
    static int max = 0;
    static int[][] field;

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());
        field = new int[N][M];

        for (int i = 0; i < N; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < M; j++) {
                field[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if(field[i][j] == 0){
                    bfs(i, j);
                }
            }
        }

        System.out.println(max);
    }

    private static void bfs(int i, int j) {

        boolean[][] visited = new boolean[field.length][field[0].length];
        Queue<Point> points = new LinkedList<>();

        points.add(new Point(i,j,0));
        visited[i][j] = true;

        loop :
        while(!points.isEmpty()){
            Point point = points.poll();

            for (int k = 0; k < xArray.length; k++) {
                int x = point.x + xArray[k];
                int y = point.y + yArray[k];
                int cnt = point.cnt + 1;

                if(isField(x,y) && !visited[x][y]){
                    if(field[x][y] == 1){
                        max = Math.max(max, cnt);
                        break loop;
                    }
                    visited[x][y] = true;
                    points.add(new Point(x,y, cnt));
                }

            }
        }

    }

    private static boolean isField(int x, int y) {
        return x >= 0 && y >= 0 && x < field.length && y < field[0].length;
    }

    static class Point{
        int x;
        int y;
        int cnt;

        public Point(int x, int y, int cnt) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
        }
    }
}
