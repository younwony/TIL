package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a14490_백대열 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine(), ":");
        int a = Integer.parseInt(stringTokenizer.nextToken());
        int b = Integer.parseInt(stringTokenizer.nextToken());

        int max = a;
        int min = b;
        if(b > a){
            max = b;
            min = a;
        }
        int gcd = gcd(max,min);

        System.out.println(a/gcd + ":" + b/gcd);
    }

    private static int gcd(int a, int b) {
        if(a % b == 0){
            return b;
        }
        return gcd(b, a % b);
    }
}
