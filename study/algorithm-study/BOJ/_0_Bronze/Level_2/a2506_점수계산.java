package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2506_점수계산 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        bufferedReader.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int sum = 0;
        int preCnt = 0;

        while(stringTokenizer.hasMoreTokens()){
            preCnt = "1".equals(stringTokenizer.nextToken()) ? preCnt + 1 : 0;
            sum += preCnt;
        }

        System.out.println(sum);
    }
}
