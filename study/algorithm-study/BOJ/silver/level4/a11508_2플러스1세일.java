package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class a11508_2플러스1세일 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());

        int[] arrays = new int[N];

        for (int i = 0; i < N; i++) {
            arrays[i] = Integer.parseInt(bufferedReader.readLine());
        }

        Arrays.sort(arrays);

        for (int i = N - 3; i >= 0; i -= 3) {
            arrays[i] = 0;
        }

        System.out.println(Arrays.stream(arrays).sum());
    }
}
