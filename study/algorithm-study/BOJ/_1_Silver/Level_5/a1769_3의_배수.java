package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1769_3의_배수 {
    private static int cnt;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        char[] chars = bufferedReader.readLine().toCharArray();

        if(chars.length == 1){
            cnt = -1;
        }

        int x = 0;

        for (char aChar : chars) {
            x += aChar - '0';
        }

        String result = isThree(x);

        System.out.println(cnt);
        System.out.println(result);
    }

    private static String isThree(int x) {
        cnt++;

        if(x < 10){
            return x % 3 == 0 ? "YES" : "NO";
        }

        int sum = 0;
        while(x > 0){
            sum += x % 10;
            x /= 10;
        }

        return isThree(sum);
    }
}
