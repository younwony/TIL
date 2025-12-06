package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a10026_적록색약 {
    static int[] xArray = new int[]{-1,0,0,1};
    static int[] yArray = new int[]{0,-1,1,0};
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        String[][] field = new String[n][n];
        String[][] rgField = new String[n][n];
        for(int i = 0; i < n; i++){
            String strLine = bufferedReader.readLine();
            field[i] = strLine.split("");
            rgField[i] = strLine.replace("R", "G").split("");
        }

        int rgbCnt = 0;

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if("".equals(field[i][j])) continue;
                bfs(i,j,field);
                rgbCnt++;
            }
        }

        int rgCnt = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if("".equals(rgField[i][j])) continue;
                bfs(i,j,rgField);
                rgCnt++;
            }
        }

        System.out.println(rgbCnt + " " + rgCnt);
    }

    private static void bfs(int i, int j, String[][] field) {
        String fieldStr = field[i][j];
        field[i][j] = "";
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));
        while(!queue.isEmpty()){
            Point poll = queue.poll();
            int x = poll.x;
            int y = poll.y;
            for(int index = 0; index < 4; index++){
                int nextX = x + xArray[index];
                int nextY = y + yArray[index];
                if(isField(nextX,nextY,field) && fieldStr.equals(field[nextX][nextY])){
                    field[nextX][nextY] = "";
                    queue.add(new Point(nextX, nextY));
                }
            }
        }
    }

    private static boolean isField(int nextI, int nextJ, String[][] field) {
        return nextI >= 0 && nextJ >= 0 && nextI < field.length && nextJ < field[0].length;
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
