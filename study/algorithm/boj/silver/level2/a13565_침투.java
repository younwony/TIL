package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class a13565_침투 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int M = Integer.parseInt(stringTokenizer.nextToken());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int[][] field = new int[M][N];
        for (int i = 0; i < M; i++) {
              String[] input = bufferedReader.readLine().split("");
            for (int j = 0; j < N; j++) {
                field[i][j] = Integer.parseInt(input[j]);
            }
        }

        int[] xArray = {0,1,-1,0};
        int[] yArray = {-1,0,0,1};

        Queue<Point> queue = new LinkedList<>();
        for(int i = 0; i < N; i++){
            if(field[0][i] == 0){
                queue.add(new Point(0,i));
                field[0][i] = 1;
            }
        }

        String answer = "NO";
        while(!queue.isEmpty()){
            Point point = queue.poll();

            if(point.x == M-1){
                answer = "YES";
                break;
            }

            for (int i = 0; i < 4; i++) {
                int x = point.x + xArray[i];
                int y = point.y + yArray[i];
                if(isField(x,y,M,N) && field[x][y] == 0){
                    field[x][y] = 1;
                    queue.add(new Point(x,y));
                }
            }
        }

        System.out.println(answer);

    }

    private static boolean isField(int x, int y, int m, int n) {
        return x >= 0 && y >= 0 && x < m && y <n;
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
