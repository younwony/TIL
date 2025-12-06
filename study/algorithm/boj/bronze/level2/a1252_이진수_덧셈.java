package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.StringTokenizer;

public class a1252_이진수_덧셈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        String a = stringTokenizer.nextToken();
        String b = stringTokenizer.nextToken();

        System.out.println(calBinary(a,b));
    }

    private static String calBinary(String a, String b) {
        int maxLen = 82;
        char[] result = new char[maxLen];

        a = lpad(a, maxLen);
        b = lpad(b, maxLen);

        int temp = 0;
        for (int i = 81; i >= 0; i--) {
            int aC = a.charAt(i) - '0';
            int bC = b.charAt(i) - '0';
            int sum = aC + bC;

            if(temp == 1){
                sum += 1;
                temp = 0;
            }

            if(sum > 1){
                sum -= 2;
                temp = 1;
            }
            result[i] = (char) (sum + '0');
        }

        int start = 0;

        for(; start < maxLen; start++){
            if(result[start] == '1'){
                break;
            }
        }

        String resultStr = new String(result, start, maxLen - start);
        if(maxLen == start){
            resultStr = "0";
        }
        return resultStr;
    }

    private static String lpad(String str, int maxLen) {
        int padSize = maxLen - str.length();
        StringBuilder result = new StringBuilder(str);
        for(int i = 0; i < padSize; i++){
            result.insert(0, "0");
        }
        return result.toString();
    }
}
