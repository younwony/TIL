package _0_Bronze.Level_5;

import java.math.BigInteger;
import java.util.Scanner;

public class a1271_엄청난부자2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] mn = scanner.nextLine().split(" ");
        BigInteger m = new BigInteger(mn[0]);
        BigInteger n = new BigInteger(mn[1]);
        System.out.println(m.divide(n));
        System.out.println(m.mod(n));
    }
}
