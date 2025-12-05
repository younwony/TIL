package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1373_2진수_8진수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();

        StringBuilder result = new StringBuilder();

        int len = input.length();

        if(len % 3 == 2){
            input = "0" + input;
        }else if(len % 3 == 1){
            input = "00" + input;
        }

        for(int i = 0 ; i < input.length(); i += 3){
            int digit = (input.charAt(i) - '0') * 4
                    + (input.charAt(i + 1) - '0') * 2
                    + (input.charAt(i + 2) - '0') * 1;

            result.append(digit);
        }

        System.out.println(result.toString());
    }
}
