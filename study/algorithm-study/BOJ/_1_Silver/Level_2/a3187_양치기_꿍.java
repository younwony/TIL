package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class a3187_양치기_꿍 {

    static String[][] field;
    static boolean[][] visited;
    static int[] xArray = {-1,0,0,1};
    static int[] yArray = {0,-1,1,0};
    static int[] vk = new int[2];

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] RC  = bufferedReader.readLine().split(" ");
        int R = Integer.parseInt(RC[0]);
        int C = Integer.parseInt(RC[1]);

        field = new String[R][C];
        visited = new boolean[R][C];

        ArrayList<Point> wolves = new ArrayList<>();

        for (int i = 0; i < R; i++) {
            field[i] = bufferedReader.readLine().split("");
            for (int j = 0; j < C; j++) {
                if("v".equals(field[i][j])){
                    wolves.add(new Point(i,j));
                    vk[0]++;
                }
                if("k".equals(field[i][j])){
                    vk[1]++;
                }
            }
        }

        //when
        for (Point wolf : wolves) {
            if(visited[wolf.x][wolf.y]) continue;
            visited[wolf.x][wolf.y] = true;
            Queue<Point> queue = new LinkedList<>();
            queue.add(wolf);

            int v = 1;
            int k = 0;

            while(!queue.isEmpty()){
                Point point = queue.poll();

                for (int i = 0; i < 4; i++) {
                    int x = point.x + xArray[i];
                    int y = point.y + yArray[i];
                    if(isNonField(x,y) || visited[x][y]) continue;
                    visited[x][y] = true;
                    if("v".equals(field[x][y])){
                        v++;
                        queue.add(new Point(x,y));
                    }
                    if("k".equals(field[x][y])){
                        k++;
                        queue.add(new Point(x,y));
                    }
                    if(".".equals(field[x][y])){
                        queue.add(new Point(x,y));
                    }
                }
            }

            if(v >= k){
                vk[1] -= k;
            }else{
                vk[0] -= v;
            }
        }

        System.out.println(vk[1] + " " + vk[0]);


    }

    private static boolean isNonField(int x, int y) {
        return x < 0 || y < 0 || x == field.length || y == field[0].length;
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
