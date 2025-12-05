package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2460_지능형_기차2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int nowCnt = 0;
        int max = 0;

        for(int i = 0; i < 10; i++){
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int out = Integer.parseInt(stringTokenizer.nextToken());
            int in = Integer.parseInt(stringTokenizer.nextToken());

            nowCnt -= out;
            nowCnt += in;

            max = Math.max(max, nowCnt);
        }

        System.out.println(max);

    }
}
