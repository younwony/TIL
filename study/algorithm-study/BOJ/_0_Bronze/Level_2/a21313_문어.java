package _0_Bronze.Level_2;

import java.util.Scanner;

public class a21313_문어 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();
        StringBuilder stringBuilder = new StringBuilder();
        boolean isOdd = N % 2 == 1;
        int temp = isOdd ? N - 1 : N;
        for(int i = 0 ; i < temp; i++){
            stringBuilder
                    .append(i % 2 == 0 ? 1 : 2)
                    .append(" ");
        }

        if(isOdd){
            stringBuilder
                    .append(3)
                    .append(" ");
        }

        System.out.println(stringBuilder.toString());


    }
}
