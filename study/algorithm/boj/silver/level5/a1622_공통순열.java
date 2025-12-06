package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;

public class a1622_공통순열 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String a;
        String b;

        StringBuilder result = new StringBuilder();

        while((a = bufferedReader.readLine()) != null && !"".equals(a) && (b = bufferedReader.readLine()) != null && !"".equals(b)){
            result
                    .append(solved(a.toCharArray(),b.toCharArray()))
                    .append("\n");
        }

        System.out.println(result.toString());
    }

    private static String solved(char[] a, char[] b) {

        String result = "";
        int[] aAlphabet = new int[26];
        int[] bAlphabet = new int[26];

        for (char c : a) {
            aAlphabet[c - 'a']++;
        }

        for (char c : b) {
            bAlphabet[c - 'a']++;
        }

        for(int i = 0; i < 26; i++){
            int count = Math.min(aAlphabet[i], bAlphabet[i]);
            while(count-- > 0){
                result += (char) (i + 'a');
            }
        }

        return result;
    }
}
