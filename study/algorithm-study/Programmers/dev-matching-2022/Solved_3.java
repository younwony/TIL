package dev_matching_2022_2;

import java.util.LinkedList;
import java.util.Queue;

public class Solved_3 {
    public static void main(String[] args) {
        solution(9, 7, new int[][]{{2, 2}, {2, 3}, {2, 5}, {3, 2}, {3, 4}, {3, 5}, {3, 6}, {4, 3}, {4, 6}, {5, 2}, {5, 5}, {6, 2}, {6, 3}, {6, 4}, {6, 6}, {7, 2}, {7, 6}, {8, 3}, {8, 4}, {8, 5}});
        solution(5, 6, new int[][]{{2, 2}, {2, 3}, {2, 4}, {3, 2}, {3, 5}, {4, 3}, {4, 4}});
    }

    private static int[][] field;
    private static int[] xMove = {-1, 0, 0, 1};
    private static int[] yMove = {0, -1, 1, 0};

    // 0 : none, 1 : sea, 2 : land, 3 : pool
    public static int[] solution(int rows, int columns, int[][] lands) {
        int[] answer = new int[2];

        field = new int[rows][columns];

        for (int i = 0; i < lands.length; i++) {
            field[lands[i][0] - 1][lands[i][1] - 1] = 2;
        }

        setSea();

        getMinMax(answer);

        if(answer[0] == Integer.MAX_VALUE){
            answer[0] = -1;
            answer[1] = -1;
        }

        return answer;
    }

    private static void getMinMax(int[] answer) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if(field[i][j] == 0){
                    int poolSize = getPoolSize(i, j);

                    min = Math.min(min, poolSize);
                    max = Math.max(max, poolSize);
                }
            }
        }
        answer[0] = min;
        answer[1] = max;
    }

    private static int getPoolSize(int x, int y) {
        int size = 1;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));
        field[x][y] = 3;
        while(!queue.isEmpty()){
            Point poll = queue.poll();
            for(int i = 0; i < 4; i++){
                int nextX = poll.x + xMove[i];
                int nextY = poll.y + yMove[i];

                if(isField(nextX, nextY) && field[nextX][nextY] == 0){
                    field[nextX][nextY] = 3;
                    size++;
                    queue.add(new Point(nextX, nextY));
                }
            }
        }

        return size;
    }

    private static void setSea() {
        int x = 0;
        int y = 0;

        field[x][y] = 1;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x,y));

        while(!queue.isEmpty()){
            Point poll = queue.poll();
            for(int i = 0; i < 4; i++){
                int nextX = poll.x + xMove[i];
                int nextY = poll.y + yMove[i];

                if(isField(nextX, nextY) && field[nextX][nextY] == 0){
                    field[nextX][nextY] = 1;
                    queue.add(new Point(nextX, nextY));
                }
            }
        }

    }

    private static boolean isField(int x, int y){
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
