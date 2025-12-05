package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1992_쿼드트리 {

    static int[][] field;
    static StringBuilder stringBuilder = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        field = new int[n][n];

        for(int i = 0; i < n; i++){
            String[] inputField = bufferedReader.readLine().split("");
            for (int j = 0; j < n; j++) {
                 field[i][j] = Integer.parseInt(inputField[j]);
            }
        }

        quad(0, 0, n,n);
        System.out.println(stringBuilder.toString());
    }

    private static void quad(int startX, int startY, int endX, int endY) {
        int quadData = field[startX][startY];
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if(quadData != field[x][y]){
                    int midX = (startX+endX)/2;
                    int midY = (startY+endY)/2;
                    stringBuilder.append("(");
                    quad(startX,startY,midX,midY);
                    quad(startX,midY,midX,endY);
                    quad(midX,startY,endX,midY);
                    quad(midX,midY,endX,endY);
                    stringBuilder.append(")");
                    return;
                }
            }
        }
        stringBuilder.append(quadData);
    }
}
