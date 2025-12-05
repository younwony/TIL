package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a20300_서강근육맨 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        long[] machines = new long[N];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < machines.length; i++) {
            machines[i] = Long.parseLong(stringTokenizer.nextToken());
        }
        Arrays.sort(machines);
        long max = 0;

        for (int i = 0; i < machines.length/2; i++) {
            int minus = machines.length % 2 == 0 ? 1 : 2;
            int j = machines.length - i - minus;
            if(i == j) continue;
            long sum = machines[i] + machines[j];
            max = Math.max(sum,max);
        }

        System.out.println(max);
    }
}
