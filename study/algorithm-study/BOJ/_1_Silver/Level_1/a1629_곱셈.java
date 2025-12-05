package _1_Silver.Level_1;

import java.util.Scanner;

public class a1629_곱셈 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();

        System.out.println(power(a%c,b,c));
    }

    private static long power(int a, int b, int c) {
        if(b == 1) return a;

        long temp = power(a,b/2,c);

        if(b % 2 == 0){
            return temp * temp % c;
        }else{
            return temp * temp % c * a % c;
        }
    }
}
