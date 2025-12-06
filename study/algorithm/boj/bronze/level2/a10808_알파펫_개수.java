package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10808_알파펫_개수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String S = bufferedReader.readLine();

        int[] alphabet = new int[26];

        for (int i = 0; i < S.length(); i++) {
            alphabet[S.charAt(i) - 'a']++;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i : alphabet) {
            stringBuilder
                    .append(i)
                    .append(" ");
        }

        System.out.println(stringBuilder.toString());
    }
}
