package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class a11051_이항_계수_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int k = Integer.parseInt(stringTokenizer.nextToken());

        int mod = 10_007;

        BigInteger top = BigInteger.ONE;
        for(int i = 0; i < k; i++){
            top = top.multiply(BigInteger.valueOf(n - i));
        }

        BigInteger bottom = BigInteger.ONE;
        for(int i = 1; i <= k; i++){
            bottom = bottom.multiply(BigInteger.valueOf(i));
        }

        System.out.println(top.divide(bottom).mod(BigInteger.valueOf(mod)));
    }
}
