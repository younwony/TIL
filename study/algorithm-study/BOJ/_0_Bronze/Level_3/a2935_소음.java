package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class a2935_소음 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        BigInteger a = new BigInteger(bufferedReader.readLine());
        String sign = bufferedReader.readLine();
        BigInteger b = new BigInteger(bufferedReader.readLine());

        System.out.println("*".equals(sign) ? a.multiply(b) : a.add(b));
    }
}
