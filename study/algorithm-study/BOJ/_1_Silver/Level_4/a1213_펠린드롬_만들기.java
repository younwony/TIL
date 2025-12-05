package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1213_펠린드롬_만들기 {
    private static int[] uppers = new int[26];

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        int len = input.length();
        for(int i = 0; i < len; i++){
            uppers[input.charAt(i) - 'A']++;
        }

        String result = "I'm Sorry Hansoo";

        /* 문자가 홀수개인 개수 */
        int oddCnt = getOddCnt();

        /* 홀수일 경우 */
        if(len % 2 == 1){
            /* 홀수 일 경우 문자 한개가 홀수개 일경우만 해당.*/
            if(oddCnt == 1){
                result = getPalindrome(getMiddleChar());
            }
        }

        /* 짝수일 경우 */
        if(len % 2 == 0){
            if(oddCnt == 0) {
                result = getPalindrome(' ');
            }
        }

        System.out.println(result);
    }

    private static String getPalindrome(char middle){
        String start = "";
        String end = "";

        for (int i = 0; i < uppers.length; i++) {
            char str = (char)(i + 'A');
            for (int j = 0; j < uppers[i] / 2; j++) {
                start = start + str;
                end = str + end;
            }
        }

        if(middle != ' '){
            start += middle;
        }

        return start + end;
    }

    private static char getMiddleChar() {
        for (int i = 0; i < uppers.length; i++) {
            if(uppers[i] % 2 == 1){
                return (char) (i + 'A');
            }
        }
        return ' ';
    }

    private static int getOddCnt() {
        int odd = 0;
        for (int upper : uppers) {
            if(upper % 2 == 1){
                odd++;
            }
        }
        return odd;
    }
}
