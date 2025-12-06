package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;

public class a1766_어두운_굴다리 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        int m = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int[] arrays = new int[m+2];
        arrays[0] = 0;
        arrays[m+1] = n;
        int index = 1;
        while(stringTokenizer.hasMoreTokens()){
            arrays[index++] = Integer.parseInt(stringTokenizer.nextToken());
        }

        int max = Math.max(arrays[1] - arrays[0], arrays[m+1] - arrays[m]);
        for (int i = 2; i < arrays.length - 1; i++) {
            int minus = arrays[i] - arrays[i - 1];
            int interval = minus % 2 == 1 ? minus/2 + 1 : minus/2;
            max = Math.max(max, interval);
        }

        System.out.println(max);
    }
}
