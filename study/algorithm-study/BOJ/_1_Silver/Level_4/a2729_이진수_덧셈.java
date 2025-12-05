package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2729_이진수_덧셈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();

        StringTokenizer stringTokenizer;
        while(T-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            String a = stringTokenizer.nextToken();
            String b = stringTokenizer.nextToken();

            result
                    .append(calBinary(a,b))
                    .append("\n");
        }

        System.out.println(result.toString());
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
