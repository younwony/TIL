package _0_Bronze.Level_4;

import java.util.Scanner;

public class a1297_TV크기 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        double z = Math.sqrt(x*x + y*y);

        double nRatio = n/z;

        System.out.println((int)(x * nRatio));
        System.out.println((int)(y * nRatio));
    }
}
