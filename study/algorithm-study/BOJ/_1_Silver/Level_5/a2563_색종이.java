package _1_Silver.Level_5;

import java.util.Scanner;

public class a2563_색종이 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[][] field = new int[101][101];

        int x,y;

        for(int i = 0; i < n; i++){
            x = scanner.nextInt();
            y = scanner.nextInt();
            for(int j = x; j < x +10; j++){
                if(j > 100){
                    break;
                }
                for(int k = y; k < y + 10; k++){
                    if(k > 100){
                        break;
                    }
                    field[j][k] = 1;
                }
            }
        }
        scanner.close();

        int result = 0;

        for(int i = 0; i < field.length; i++){
            for(int j = 0; j < field[0].length; j++){
                result += field[i][j];
            }
        }

        System.out.println(result);
    }
}
