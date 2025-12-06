package _0_Bronze.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a3003_킹_퀸_룩_비숍_나이트_폰 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int[] chess = {1, 1, 2, 2, 2, 8};
        StringBuilder stringBuilder = new StringBuilder();

        for (int i : chess) {
            stringBuilder.append(i - Integer.parseInt(stringTokenizer.nextToken()) + " ");
        }

        System.out.println(stringBuilder);
    }
}
