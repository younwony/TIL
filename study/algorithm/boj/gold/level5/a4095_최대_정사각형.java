package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a4095_최대_정사각형 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        StringTokenizer stringTokenizer;
        StringBuilder stringBuilder = new StringBuilder();
        while(!"0 0".equals(line = bufferedReader.readLine())){
            stringTokenizer = new StringTokenizer(line);
            int n = Integer.parseInt(stringTokenizer.nextToken());
            int m = Integer.parseInt(stringTokenizer.nextToken());

            int[][] field = new int[n+1][m+1];
            int max = 0;
            for(int i = 1; i <= n; i++){
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
                for(int j = 1; j <= m; j++){
                    int number = Integer.parseInt(stringTokenizer.nextToken());
                    if(number == 0){
                        field[i][j] = number;
                        continue;
                    }

                    field[i][j] = Math.min(field[i-1][j-1], Math.min(field[i-1][j],field[i][j-1])) + 1;
                    max = Math.max(max,field[i][j]);
                }
            }
            stringBuilder
                    .append(max)
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
