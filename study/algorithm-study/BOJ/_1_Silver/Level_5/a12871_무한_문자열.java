package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a12871_무한_문자열 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String s = bufferedReader.readLine();
        String t = bufferedReader.readLine();

        int result = 1;

        int sLen = s.length();
        int tLen = t.length();
        for(int i = 0; i < sLen * tLen; i++) {
            if(s.charAt(i % sLen) != t.charAt(i % tLen)){
                result = 0;
                break;
            }
        }

        System.out.println(result);

    }
}
