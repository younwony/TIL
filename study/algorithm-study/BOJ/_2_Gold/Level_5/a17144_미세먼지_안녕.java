package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class a17144_미세먼지_안녕 {

    static int[][] field;
    static HashSet<dust> dustSet = new HashSet<>();
    static int[] xArray = new int[]{0,-1,0,1};
    static int[] yArray = new int[]{1,0,-1,0};
    static int[] airTop = new int[2];
    static int[] airBot = new int[2];
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] RCT = bufferedReader.readLine().split(" ");
        int R = Integer.parseInt(RCT[0]);
        int C = Integer.parseInt(RCT[1]);
        int T = Integer.parseInt(RCT[2]);

        field = new int[R][C];

        for(int i = 0 ; i < R; i++){
            String[] input = bufferedReader.readLine().split(" ");
            for(int j = 0; j < C; j++){
                field[i][j] = Integer.parseInt(input[j]);
                if(field[i][j] == -1){
                    if(airTop[0] == 0){
                        airTop[0] = i;
                        airTop[1] = j;
                    }else{
                        airBot[0] = i;
                        airBot[1] = j;
                    }
                }
            }
        }

        for(int i = 0; i < T; i++){
            getDustSet();
            multiplyDust();
            airClear(airTop[0], airTop[1], 1);
            airClear(airBot[0], airBot[1],-1);
        }
        int sum = 0;
        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[0].length; j++){
                if(field[i][j] > 0) sum += field[i][j];
            }
        }
        System.out.println(sum);
    }

    private static void airClear(int x, int y ,int sign) {
        int pre = 0;
        int index = 0;
        while(true){
            x += xArray[index] * sign;
            y += yArray[index];
            if(isField(x, y)){
                if(field[x][y] == -1) return;
                int temp = field[x][y];
                field[x][y] = pre;
                pre = temp;
            }else{
                x -= xArray[index] * sign;
                y -= yArray[index];
                index++;
                if(index == 4) index = 0;
            }
        }
    }

    private static void multiplyDust() {
        for (dust dust : dustSet) {
            int plusDust = dust.V/5;
            int plusCnt = 0;
            for(int i = 0; i < 4; i++){
                int nextX = dust.X + xArray[i];
                int nextY = dust.Y + yArray[i];

                if(isField(nextX,nextY) && field[nextX][nextY] != -1){
                    plusCnt++;
                    field[nextX][nextY] += plusDust;
                }
            }
            field[dust.X][dust.Y] -= plusDust * plusCnt;
        }
    }

    private static boolean isField(int nextX, int nextY) {
        return nextX >= 0 && nextY >= 0 && nextX < field.length && nextY < field[0].length;
    }

    private static void getDustSet() {
        dustSet.clear();
        for(int i = 0 ; i < field.length; i++){
            for(int j = 0; j < field[0].length; j++){
                if(field[i][j] > 0 ){
                    dustSet.add(new dust(i,j,field[i][j]));
                }
            }
        }
    }

    static class dust{
        int X;
        int Y;
        int V;

        public dust(int x, int y, int v) {
            X = x;
            Y = y;
            V = v;
        }
    }
}
