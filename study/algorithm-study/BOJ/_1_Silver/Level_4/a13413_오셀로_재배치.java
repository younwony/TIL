package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a13413_오셀로_재배치 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(bufferedReader.readLine());

        StringBuilder stringBuilder = new StringBuilder();
        while(T-- > 0){
            int N = Integer.parseInt(bufferedReader.readLine());
            char[] start = bufferedReader.readLine().toCharArray();
            char[] target = bufferedReader.readLine().toCharArray();
            stringBuilder
                    .append(getEqualsCnt(start,target, N))
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    private static int getEqualsCnt(char[] start, char[] target, int N) {
        int black = 0;
        int white = 0;

        for (int i = 0; i < N; i++) {
            char s = start[i];
            char t = target[i];

            if(s != t){
                if(s == 'W'){
                    white++;
                }else{
                    black++;
                }
            }
        }

        return Math.max(black,white);
    }
}
