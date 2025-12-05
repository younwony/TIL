package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2960_에라토스테네스의_체 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int K = Integer.parseInt(stringTokenizer.nextToken());

        boolean[] filed = new boolean[N+1];

        for(int i = 2; i <= N; i++){
            for(int j = 1; i * j <= N; j++){
                if(!filed[i * j]) {
                    filed[i * j] = true;
                    if (--K == 0) {
                        System.out.println(i * j);
                        return;
                    }
                }
            }
        }
    }
}
