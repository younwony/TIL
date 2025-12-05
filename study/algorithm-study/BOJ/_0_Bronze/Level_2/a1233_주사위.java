package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1233_주사위 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int S1 = Integer.parseInt(stringTokenizer.nextToken());
        int S2 = Integer.parseInt(stringTokenizer.nextToken());
        int S3 = Integer.parseInt(stringTokenizer.nextToken());

        int[] sums = new int[S1 + S2 + S3 + 1];

        for(int i = 1; i <= S1; i++){
            for(int j = 1; j <= S2; j++) {
                for(int k = 1; k <= S3; k++) {
                    sums[i+j+k]++;
                }
            }
        }

        int min = 0;
        int index = -1;
        for(int i = 3; i < sums.length; i++){
            if(sums[i] > min){
                min = sums[i];
                index = i;
            }
        }

        System.out.println(index);
    }
}
