package _0_Bronze.Level_2;

import java.util.Scanner;

public class a14697_방_배정하기 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int A = scanner.nextInt();
        int B = scanner.nextInt();
        int C = scanner.nextInt();
        int N = scanner.nextInt();

        int result = 0;

        loop :
        for (int i = 0; i < 51; i++) {
            for (int j = 0; j < 51; j++) {
                for (int k = 0; k < 51; k++) {
                    if(A * i + B * j + C * k == N){
                        result = 1;
                        break loop;
                    }
                }
            }
        }

        System.out.println(result);
    }
}
