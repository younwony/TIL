package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class a17413_단어_뒤집기_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferedReader.readLine();
        ArrayList<String> sArray = split(s);

        StringBuilder stringBuilder = new StringBuilder();

        for (String str : sArray) {
            stringBuilder.append(str);
        }

        System.out.println(stringBuilder.toString());
    }

    private static ArrayList<String> split(String s) {

        char[] chars = s.toCharArray();
        boolean leftBracket = false;
        boolean rightBracket = false;

        ArrayList<String> strings = new ArrayList<>();

        int preIndex = 0;
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if(!leftBracket && !rightBracket){
                if(aChar == ' ') {
                    String str = new StringBuilder(new String(chars, preIndex, i - preIndex))
                            .reverse()
                            .toString();
                    strings.add(str + " ");
                    preIndex = i + 1;
                }
                if(i == chars.length - 1){
                    String str = new StringBuilder(new String(chars, preIndex, chars.length - preIndex))
                            .reverse()
                            .toString();
                    strings.add(str);
                }
            }

            if(aChar == '<'){
                String str = new StringBuilder(new String(chars, preIndex, i - preIndex))
                        .reverse()
                        .toString();
                strings.add(str);
                leftBracket = true;
                preIndex = i;
            }

            if(aChar == '>'){
                strings.add(new String(chars, preIndex, i - preIndex + 1));
                preIndex = i+1;
                leftBracket = false;
                rightBracket = false;
            }
        }

        return strings;
    }
}
