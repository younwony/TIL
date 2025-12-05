package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a18111_마인크래프트 {
    public static void main(String[] args) throws IOException {
        //given
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( System.in));
        String[] nmb = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nmb[0]);
        int m = Integer.parseInt(nmb[1]);
        int b = Integer.parseInt(nmb[2]);
        int[][] field = new int[n][m];
        int start = Integer.MAX_VALUE;
        int end = 0;
        for(int i = 0 ; i < n; i++){
            String[] nField = bufferedReader.readLine().split(" ");
            for(int j = 0; j < m; j++){
                field[i][j] = Integer.parseInt(nField[j]);
                start = Math.min(start, field[i][j]);
                end = Math.max(end, field[i][j]);
            }
        }

        //when
        int time = Integer.MAX_VALUE;
        int value = 0;
        for(;start <= end; start++){
            if(isBlock(field, start, b)){
                int blockTime = getBlockTime(field, start);
                if(blockTime < time) time = blockTime;
                if(blockTime == time) value = Math.max(value, start);
            }
        }
        time = getBlockTime(field, value);
        System.out.println(time + " " + value);
    }

    private static int getBlockTime(int[][] field, int val) {
        int result = 0 ;
        for (int[] nField : field) {
            for (int value : nField) {
                int setTime = val - value;
                if(setTime > 0) result += setTime;
                if(setTime < 0) result += (setTime*-2);
            }
        }
        return result;
    }

    private static boolean isBlock(int[][] field, int val, int b) {
        for (int[] nField : field) {
            for (int value : nField) {
                if(value == val) continue;
                b -= val - value;
            }
        }
        return b >= 0;
    }
}
