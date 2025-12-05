package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10820_문자열_분석 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(System.in));

        String input;
        while((input = bufferedReader.readLine()) != null && !"".equals(input)){
            int[] result = new int[4];
            for(int i = 0; i < input.length(); i++){
                char c = input.charAt(i);

                if( c >= 'a' && c <= 'z'){
                    result[0]++;
                }else if( c >= 'A' && c <= 'Z') {
                    result[1]++;
                }else if(c - '0' >= 0 && c - '0' < 10){
                    result[2]++;
                }else if( c == ' '){
                    result[3]++;
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i : result) {
                stringBuilder
                        .append(i)
                        .append(" ");
            }

            System.out.println(stringBuilder.toString());
        }
    }
}
