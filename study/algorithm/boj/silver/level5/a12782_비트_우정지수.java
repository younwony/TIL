package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a12782_비트_우정지수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCnt = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();
        StringTokenizer stringTokenizer;
        while(testCnt-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            String a = stringTokenizer.nextToken();
            String b = stringTokenizer.nextToken();

            int[] aCnt = new int[2];
            for (int i = 0; i < a.length(); i++) {
                if(a.charAt(i) != b.charAt(i)){
                    aCnt[a.charAt(i) - '0']++;
                }
            }
            result
                    .append(Math.max(aCnt[0],aCnt[1]))
                    .append("\n");
        }

        System.out.println(result.toString());
    }

}
