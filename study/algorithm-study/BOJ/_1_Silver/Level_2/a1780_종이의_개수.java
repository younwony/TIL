package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1780_종이의_개수 {
    static int[][] field;
    static int[] answer = new int[3];
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        field = new int[n][n];

        for(int i = 0 ; i < n; i++){
            String[] inputField = bufferedReader.readLine().split(" ");
            for(int j = 0 ; j < n; j++){
                field[i][j] = Integer.parseInt(inputField[j]);
            }
        }
        
        paper(0,0,n,n);

        for (int i : answer) {
            System.out.println(i);
        }

    }

    private static void paper(int startX, int startY, int endX, int endY) {
        int paper = field[startX][startY];

        for(int x = startX; x < endX; x++){
            for(int y = startY; y < endY; y++){
                if(paper != field[x][y]){
                    divPaper(startX, startY, endX, endY);
                    return;
                }
            }
        }

        answer[paper+1]++;
    }

    private static void divPaper(int startX, int startY, int endX, int endY) {
        int oneThirdX = startX + (endX - startX)/3;
        int twoThirdX = startX + (endX - startX)*2/3;
        int oneThirdY = startY + (endY - startY)/3;
        int twoThirdY = startY + (endY - startY)*2/3;
        paper(startX, startY,oneThirdX,oneThirdY);
        paper(startX,oneThirdY,oneThirdX,twoThirdY);
        paper(startX,twoThirdY,oneThirdX, endY);
        paper(oneThirdX, startY,twoThirdX,oneThirdY);
        paper(oneThirdX,oneThirdY,twoThirdX,twoThirdY);
        paper(oneThirdX,twoThirdY,twoThirdX, endY);
        paper(twoThirdX, startY, endX,oneThirdY);
        paper(twoThirdX,oneThirdY, endX,twoThirdY);
        paper(twoThirdX,twoThirdY, endX, endY);
    }
}
