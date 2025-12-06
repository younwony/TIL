package _1_Silver.Level_2;

import java.math.BigInteger;
import java.util.Scanner;

public class a2407_조합 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextInt();
        long m = scanner.nextInt();
        long minM = Math.min(n-m, m);

        BigInteger nF = factorial(BigInteger.valueOf(n), minM);
        BigInteger mF = factorial(BigInteger.valueOf(minM), minM);
        System.out.println(nF.divide(mF));
    }

    private static BigInteger factorial(BigInteger n, long minM) {
        BigInteger result = new BigInteger(String.valueOf(n));
        for(int i = 1; i < minM; i++) {
            n = n.subtract(BigInteger.ONE);
            result = result.multiply(n);
        }
        return  result;
    }
}
