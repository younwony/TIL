package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a4096_팰린드로미터 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder result = new StringBuilder();

        String input;
        while(!"0".equals(input = bufferedReader.readLine())){
            result
                    .append(solved(input))
                    .append("\n");
        }

        System.out.println(result.toString());
    }

    private static int solved(String input) {
        int length = input.length();
        if(isPalindrome(input, length)){
            return 0;
        }

        int number = Integer.parseInt(input);
        int temp = number;

        while(!isPalindrome(input, length)){
            number++;
            input = lpad(number, length);
        }

        return Integer.parseInt(input) - temp;

    }

    private static boolean isPalindrome(String input, int length) {
        for(int i = 0; i < input.length()/2; i++){
            if(input.charAt(i) != input.charAt(length - i - 1)){
                return false;
            }
        }
        return true;
    }

    private static String lpad(int number, int length) {
        String input = String.valueOf(number);
        for(int i = input.length(); i < length; i++){
            input = "0" + input;
        }
        return input;
    }
}
