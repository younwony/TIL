package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a3613_Java_vs_Cpp {
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        char[] inputs = bufferedReader.readLine().toCharArray();
        int length = inputs.length;
        boolean isJava = true;
        boolean isCpp = true;

        /* 첫 글자, 마지막글자 '_' 일 경우 or 첫 글자 대문자 인경우 에러 */
        if(inputs[0] == '_' || inputs[length - 1] == '_' || isUpper(inputs[0])){
            System.out.println("Error!");
            return;
        }

        for (int i = 0; i < length; i++) {
            /* Cpp 일 경우 */
            if(inputs[i] == '_'){
                /* 연속 '_' 일 경우 에러 */
                if(i > 1 && inputs[i - 1] == '_'){
                    System.out.println("Error!");
                    return;
                }

                isJava = false;

            /* '_' 없고 대문자 일경우 Java */
            }else if(isUpper(inputs[i])){
                isCpp = false;
            }
        }

        /* Cpp 와 Java 같이 쓰일 경우 에러 */
        if(!isCpp && !isJava){
            System.out.println("Error!");
            return;
        }

        /* 모두 소문자일 경우 그대로 출력 */
        if(isCpp && isJava){
            System.out.println(new String(inputs));
            return;
        }

        StringBuilder result = new StringBuilder();

        if(isJava){
            for(char c : inputs){
                //대문자를 빼고 '_'와 소문자를 넣는다.
                if (isUpper(c)) {
                    result.append('_').append((char) (c + 32));
                } else {
                    result.append(c);
                }
            }
        }

        if(isCpp){
            for (int i = 0; i < inputs.length; i++) {
                //'_'를 빼고 대문자를 넣는다.
                if (inputs[i] == '_' && i < inputs.length - 1) {
                    result.append((char) (inputs[++i] - 32));
                } else {
                    result.append(inputs[i]);
                }
            }
        }

        System.out.println(result.toString());
    }

    private static boolean isUpper(char input) {
        return 'A' <= input && input <= 'Z';
    }
}
