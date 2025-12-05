package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class a2583_영역구하기 {

    static boolean[][] field;
    static int resultCnt = 0;
    static int fieldCnt = 0;
    static int[] xArray = {-1,1,0,0};
    static int[] yArray = {0,0,-1,1};

    public static void main(String[] args) throws Exception{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nmk = bufferedReader.readLine().split(" ");
        int n = Integer.valueOf(nmk[0]);
        int m = Integer.valueOf(nmk[1]);
        int k = Integer.valueOf(nmk[2]);

        field = new boolean[n][m];

        String[] x1y1x2y2;
        int x1,y1,x2,y2;
        for(int i = 0 ; i <k; i++){
            x1y1x2y2 = bufferedReader.readLine().split(" ");
            x1 = Integer.valueOf(x1y1x2y2[0]);
            y1 = Integer.valueOf(x1y1x2y2[1]);
            x2 = Integer.valueOf(x1y1x2y2[2]);
            y2 = Integer.valueOf(x1y1x2y2[3]);

            for(int x = x1; x < x2; x++){
                for(int y = y1; y < y2; y++){
                    field[y][x] = true;
                }
            }
        }

        ArrayList<Integer> resultArray = new ArrayList<>();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(field[i][j] == false){
                    resultCnt++;
                    fieldCnt = 0;
                    dfs(i,j);
                    resultArray.add(fieldCnt);
                }
            }
        }
        StringBuilder result = new StringBuilder();

        result.append(resultCnt).append("\n");

        Collections.sort(resultArray);

        for(int i : resultArray){
            result.append(i).append(" ");
        }
        System.out.println(result.toString());
    }

    public static void dfs(int x, int y){
        fieldCnt++;
        field[x][y] = true;

        for(int i = 0; i < 4; i++){
            int xn = x + xArray[i];
            int yn = y + yArray[i];
            if(xn < 0 || xn >= field.length || yn < 0 || yn >= field[0].length) continue;
            if(field[xn][yn]) continue;
            dfs(xn, yn);
        }

    }
}
