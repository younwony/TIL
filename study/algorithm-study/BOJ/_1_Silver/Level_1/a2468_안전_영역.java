package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

public class a2468_안전_영역 {
    /**
     * 어떤 지역의 높이 정보가 주어졌을 때, 장마철에 물에 잠기지 않는 안전한 영역의 최대 개수를 계산하는 프로그램을 작성하시오.
     *
     * Input
     * 첫째 줄에는 어떤 지역을 나타내는 2차원 배열의 행과 열의 개수를 나타내는 수 N이 입력된다. N은 2 이상 100 이하의 정수이다.
     * 둘째 줄부터 N개의 각 줄에는 2차원 배열의 첫 번째 행부터 N번째 행까지 순서대로 한 행씩 높이 정보가 입력된다.
     * 각 줄에는 각 행의 첫 번째 열부터 N번째 열까지 N개의 높이 정보를 나타내는 자연수가 빈 칸을 사이에 두고 입력된다. 높이는 1이상 100 이하의 정수이다.
     *
     * Output
     * 첫째 줄에 장마철에 물에 잠기지 않는 안전한 영역의 최대 개수를 출력한다.
     *
     * Input ex
     * 5
     * 6 8 2 6 2
     * 3 2 3 4 6
     * 6 7 3 3 2
     * 7 2 5 3 6
     * 8 9 5 2 7
     *
     * Output ex
     *  5
     *
     *  Min 부터 Max 까지 bfs 탐색
     * @param args
     */

    private static int[][] field;
    private static boolean[][] visited;
    private static int[] xMove = {0,0,-1,1};
    private static int[] yMove = {-1,1,0,0};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        field = new int[n][n];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < n; j++) {
                field[i][j] = Integer.parseInt(stringTokenizer.nextToken());
                min = Math.min(min, field[i][j]);
                max = Math.max(max, field[i][j]);
            }
        }

        int maxArea = 1;
        for(; min < max; min++){
            visited = new boolean[n][n];
            raindrop(min);
            int area = getArea();
            maxArea = Math.max(maxArea, area);
        }

        System.out.println(maxArea);
    }

    private static int getArea() {
        int area = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if(!visited[i][j]){
                    area++;
                    visited[i][j] = true;
                    setArea(i, j);
                }
            }
        }
        return area;
    }

    private static void setArea(int i, int j) {
        for (int k = 0; k < 4; k++) {
            int nextI = i + xMove[k];
            int nextJ = j + yMove[k];
            if(nextI >= 0 && nextJ >= 0 && nextI < field.length && nextJ < field.length && !visited[nextI][nextJ]){
                visited[nextI][nextJ] = true;
                setArea(nextI, nextJ);
            }
        }
    }

    private static void raindrop(int min) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if(field[i][j] <= min) {
                    field[i][j] = 0;
                    visited[i][j] = true;
                }
            }
        }
    }
}
