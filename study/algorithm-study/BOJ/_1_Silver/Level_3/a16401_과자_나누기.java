package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a16401_과자_나누기 {
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int M = Integer.parseInt(stringTokenizer.nextToken());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int[] snacks = new int[N];
        for (int i = 0; i < snacks.length; i++) {
            snacks[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        Arrays.sort(snacks);

        int low = 1;
        int high = snacks[N-1];
        int max = 0;
        int snackSize = 0;


        while(low <= high){
            snackSize = low + high >> 1;

            if(getSnackCnt(snacks, snackSize) >= M){
                max = snackSize;
                low = snackSize + 1;
            }else{
                high = snackSize - 1;
            }
        }

        System.out.println(max);

    }

    private static int getSnackCnt(int[] snacks, int snackSize) {
        int cnt = 0;
        for (int snack : snacks) {
            cnt += snack/ snackSize;
        }
        return cnt;
    }
}
