package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2630_색종이_만들기 {
    static int[][] field;
    static int[] answer = new int[2];
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        field = new int[n][n];

        for(int i = 0; i < n; i++){
            String[] inputStr = bufferedReader.readLine().split( " ");
            for(int j = 0; j < n; j++){
                field[i][j] = Integer.parseInt(inputStr[j]);
            }
        }

        paper(0,0,n,n);

        for (int paper : answer) {
            System.out.println(paper);
        }
    }

    private static void paper(int startX, int startY, int endX, int endY) {
        int paper = field[startX][startY];

        for(int x = startX; x < endX; x++){
            for(int y = startY; y < endY; y++){
                if(paper != field[x][y]){
                    int midX = (endX + startX)/2;
                    int midY = (startY + endY)/2;
                    paper(startX,startY,midX,midY);
                    paper(midX,startY,endX,midY);
                    paper(startX,midY,midX,endY);
                    paper(midX,midY,endX,endY);
                    return;
                }
            }
        }

        answer[paper]++;
    }
}
