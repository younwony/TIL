package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class a7562_나이트의_이동 {

    static int[] xArray = {-1,-2,-2,-1,1,2,2,1};
    static int[] yArray = {-2,-1,1,2,-2,-1,1,2};
    static int[][] field;
    static boolean[][] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCase = Integer.parseInt(bufferedReader.readLine());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < testCase; i++) {
            int len = Integer.parseInt(bufferedReader.readLine());
            field = new int[len][len];
            visited = new boolean[len][len];
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int startX = Integer.parseInt(stringTokenizer.nextToken());
            int startY = Integer.parseInt(stringTokenizer.nextToken());

            stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int endX = Integer.parseInt(stringTokenizer.nextToken());
            int endY = Integer.parseInt(stringTokenizer.nextToken());
            
            int result = solve(startX, startY, endX, endY);
            stringBuilder
                    .append(result)
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    private static int solve(int startX, int startY, int endX, int endY) {
        int result = 0;

        Queue<Chess> queue = new LinkedList<>();
        queue.add(new Chess(startX,startY,0));
        visited[startX][startY] = true;
        while(!queue.isEmpty()){
            Chess chess = queue.poll();
            if(chess.x == endX && chess.y == endY){
                result = chess.cnt;
                break;
            }

            for(int i = 0 ; i < 8; i++){
                int x = chess.x + xArray[i];
                int y = chess.y + yArray[i];
                if(isField(x,y) && !visited[x][y]){
                    visited[x][y] = true;
                    queue.add(new Chess(x,y, chess.cnt+1));
                }
            }
        }

        return result;
    }

    private static boolean isField(int x, int y) {
        return x >= 0 && y >= 0 && x < field.length && y < field.length;
    }

    static class Chess{
        int x;
        int y;
        int cnt;

        public Chess(int x, int y, int cnt) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
        }
    }
}
