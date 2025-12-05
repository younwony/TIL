package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a18311_왕복 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        long k = Long.parseLong(stringTokenizer.nextToken());

        int[] fields = new int[n];

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < fields.length; i++) {
            fields[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        System.out.println(solved(k,fields));

    }

    private static int solved(long k, int[] fields) {

        int index = 0;

        for (; index < fields.length; index++) {
            k -= fields[index];
            if(k < 0){
                return index+1;
            }
        }

        index--;

        for (; index >= 0; index--) {
            k -= fields[index];
            if(k < 0){
                return index+1;
            }
        }

        return 1;
    }
}
