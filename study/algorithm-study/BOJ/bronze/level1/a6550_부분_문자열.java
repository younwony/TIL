package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a6550_부분_문자열 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        StringBuilder result = new StringBuilder();
        while((line = bufferedReader.readLine()) != null && !"".equals(line)){
            String[] input = line.split(" ");
            String s = input[0];
            String t = input[1];

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                int indexOf = t.indexOf(c);
                if(indexOf == -1){
                    result.append("No").append("\n");
                    break;
                }

                if(i == s.length()-1){
                    result.append("Yes").append("\n");
                    break;
                }
                t = t.substring(indexOf+1);
            }
        }

        System.out.println(result.toString());
    }
}
