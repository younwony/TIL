package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10162_전자레인지 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());

        int A = 300;
        int B = 60;
        int C = 10;

        int a = T/A;
        T %= A;
        int b = T/B;
        T %= B;
        int c = T/C;
        T %= C;

        System.out.println(T > 0 ? -1 : a + " " + b + " " + c);

    }
}
