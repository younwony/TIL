package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class a10826_피보나치수_4 {

    public static BigInteger[] fivo;

    static {
        fivo = new BigInteger[10_001];
        setFivo();
    }

    private static void setFivo() {
        fivo[0] = BigInteger.ZERO;
        fivo[1] = BigInteger.ONE;
        for (int i = 2; i < fivo.length; i++) {
            fivo[i] = fivo[i-1].add(fivo[i-2]);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        System.out.println(fivo[n]);

    }
}
