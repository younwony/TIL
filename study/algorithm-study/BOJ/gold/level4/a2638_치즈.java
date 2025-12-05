package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class a2638_치즈 {

    static int[][] field;
    static int[] xArray = {-1,0,0,1};
    static int[] yArray = {0,-1,1,0};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] NM = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(NM[0]);
        int m = Integer.parseInt(NM[1]);

        field = new int[n][m];

        for (int i = 0; i < n; i++) {
            String[] input = bufferedReader.readLine().split(" ");
            for (int j = 0; j < m; j++) {
                field[i][j] = Integer.parseInt(input[j]);
            }
        }

        int answer = 0;
        while(true){
            Set<Point> pointSet = new HashSet<>();
            setCheezField();
            inSet(pointSet);
            if(pointSet.isEmpty()){
                break;
            }
            clearSet(pointSet);
            answer++;
        }
        System.out.println(answer);
    }

    private static void setCheezField() {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(0,0));
        boolean[][] visited = new boolean[field.length][field[0].length];
        visited[0][0] = true;
        while(!queue.isEmpty()){
            Point point = queue.poll();
            for (int i = 0; i < 4; i++) {
                int x = point.x + xArray[i];
                int y = point.y + yArray[i];
                if(isField(x,y) && !visited[x][y] && (field[x][y] == 0 || field[x][y] == 2)){
                    visited[x][y] = true;
                    field[x][y] = 2;
                    queue.add(new Point(x,y));
                }
            }
        }

    }

    /* 치즈 제거 */
    private static void clearSet(Set<Point> pointSet) {
        for (Point point : pointSet) {
            field[point.x][point.y] = 2;
        }
        pointSet.clear();
    }

    private static void inSet(Set<Point> pointSet) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if(field[i][j] == 1 && aroundCheeze(i, j) > 1){
                    pointSet.add(new Point(i,j));
                }
            }
        }
    }

    /* 공기와 맞닿아 있는 필드 개수 */
   private static int aroundCheeze(int i, int j) {
        int cnt = 0;
        for(int k = 0; k < 4; k++){
            int x = i + xArray[k];
            int y = j + yArray[k];
            if(isField(x,y) && field[x][y] == 2){
                cnt++;
            }
        }
        return cnt;
    }

    /* 필드 체크 */
    private static boolean isField(int x, int y) {
        return x >= 0 && y >= 0 && x < field.length && y < field[0].length;
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
