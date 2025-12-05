package _0_Bronze.Level_1;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class a1236_성지키기 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        String[][] field = new String[n][m];
        scanner.nextLine();

        for(int i = 0; i < n; i++){
            field[i] = scanner.nextLine().split("");
        }

        scanner.close();

        Set<Integer> xSet = new HashSet<>();
        Set<Integer> ySet = new HashSet<>();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if("X".equals(field[i][j])){
                    xSet.add(i);
                    ySet.add(j);
                }
            }
        }

        int xCount = n - xSet.size();
        int yCount = m - ySet.size();

        System.out.println(xCount > yCount ? xCount : yCount);
    }
}