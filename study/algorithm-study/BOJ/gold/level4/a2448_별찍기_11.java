package _2_Gold.Level_4;

import java.util.Scanner;

public class a2448_별찍기_11 {
    static char[][] field;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int end = 2*n-1;
        field = new char[n][end];
        draw(0, 0, n);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < field.length; i++) {
            for(int j = 0; j < field[0].length; j++){
                result.append(field[i][j] == '\0' ? " " : field[i][j]);
            }
            result.append("\n");
        }
        System.out.println(result.toString());

    }

    private static void draw(int startX, int startY, int n) {
        if(n == 3){
            star(startX, startY);
            return;
        }
        int nextN = n/2;
        draw(startX,startY+nextN,nextN);
        draw(startX + nextN,startY,nextN);
        draw(startX + nextN,startY + n,nextN);
    }

    private static void star(int x, int y) {
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y+5; j++) {
                if(i == x && j == y+2){
                    field[i][j] = '*';
                }else if(i == x+1 && (j == y+1 || j == y+3)){
                    field[i][j] = '*';
                }else if(i == x+2){
                    field[i][j] = '*';
                }
            }
        }
    }
}
