package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class a11656_접미사_배열 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String S = bufferedReader.readLine();

        ArrayList<String> sList = new ArrayList<>();

        for(int i = 0 ; i < S.length(); i++){
            sList.add(S.substring(i));
        }

        Collections.sort(sList);

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : sList) {
            stringBuilder
                    .append(s)
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
