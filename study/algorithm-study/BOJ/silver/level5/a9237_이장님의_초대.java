package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a9237_이장님의_초대 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        int[] trees = new int[n];

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for(int i = 0; i < n; i++){
            trees[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        Arrays.sort(trees);

        int max = 0;
        for(int i = 0; i < n; i++){
            max = Math.max(trees[n - i - 1] + 1 + i, max);
        }

        System.out.println(max + 1);
    }
}
