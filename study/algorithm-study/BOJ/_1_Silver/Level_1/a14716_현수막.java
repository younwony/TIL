package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a14716_현수막 {

    static int n;
    static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        n = Integer.parseInt(nm[0]);
        m = Integer.parseInt(nm[1]);

        boolean[][] field = new boolean[n][m];
        boolean[][] visited = new boolean[n][m];

        Queue<Point> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String[] input = bufferedReader.readLine().split(" ");
            for(int j = 0; j < m; j++){
                field[i][j] = "1".equals(input[j]);
                if(field[i][j]){
                    queue.add(new Point(i,j));
                }
            }
        }

        int[] xArray = {-1,-1,-1,0,0,0,1,1,1};
        int[] yArray = {-1,0,1,-1,0,1,-1,0,1};

        int answer = 0;
        while(!queue.isEmpty()){
            Point point = queue.poll();
            if(visited[point.x][point.y]) continue;
            answer++;
            visited[point.x][point.y] = true;
            Queue<Point> visitQue = new LinkedList<>();
            visitQue.add(point);
            while(!visitQue.isEmpty()){
                Point visit = visitQue.poll();
                for (int i = 0; i < 9; i++){
                    int x = visit.x + xArray[i];
                    int y = visit.y + yArray[i];
                    if(isField(x,y) && field[x][y] && !visited[x][y]){
                        visitQue.add(new Point(x,y));
                        visited[x][y] = true;
                    }
                }
            }
        }

        System.out.println(answer);
    }

    private static boolean isField(int x, int y) {
        return x >= 0 && y >= 0 && x < n && y < m;
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
