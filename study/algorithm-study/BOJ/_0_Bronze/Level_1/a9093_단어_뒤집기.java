package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9093_단어_뒤집기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();

        StringTokenizer stringTokenizer;

        while(t-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            while(stringTokenizer.hasMoreTokens()){
                result
                        .append(new StringBuilder(stringTokenizer.nextToken())
                                .reverse())
                        .append(" ");
            }
            result.append("\n");
        }

        System.out.println(result.toString());
    }
}
