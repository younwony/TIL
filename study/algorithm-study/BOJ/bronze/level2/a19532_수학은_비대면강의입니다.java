package _0_Bronze.Level_2;

import java.util.Scanner;

public class a19532_수학은_비대면강의입니다 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();
        int e = scanner.nextInt();
        int f = scanner.nextInt();

        loop :
        for (int x = -999; x < 1000; x++) {
            for (int y = -999; y < 1000; y++) {
                if((a*x) + (b*y) == c && (d*x) + (e*y) == f){
                    System.out.println(x + " " + y);
                    break loop;
                }
            }
        }

    }
}
