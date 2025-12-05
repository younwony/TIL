package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a11256_사탕 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCase = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer;

        StringBuilder result = new StringBuilder();
        while(testCase-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int j = Integer.parseInt(stringTokenizer.nextToken());
            int n = Integer.parseInt(stringTokenizer.nextToken());
            int cnt = 0;

            int[] arrays = new int[n];

            for (int i = 0; i < arrays.length; i++) {
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
                int size = Integer.parseInt(stringTokenizer.nextToken()) * Integer.parseInt(stringTokenizer.nextToken());
                arrays[i] = size;
            }

            Arrays.sort(arrays);

            for (int i = arrays.length - 1; i >= 0 ; i--) {
                if(j <= 0){
                    break;
                }

                j -= arrays[i];
                cnt++;
            }

            result
                    .append(cnt)
                    .append("\n");
        }

        System.out.println(result.toString());
    }
}
