package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1449_수리공_항승 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int L = Integer.parseInt(stringTokenizer.nextToken());

        boolean[] pipes = new boolean[1001];

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for (int i = 0; i < N; i++) {
            int failPipe = Integer.parseInt(stringTokenizer.nextToken());
            pipes[failPipe] = true;
        }

        int result = 0;
        for (int i = 0; i < pipes.length; i++) {
            if(pipes[i]) {
                result++;
                for(int j = 0; j < L; j++){
                    int pipe = i + j;
                    if(pipe >= 1001) break;
                    pipes[pipe] = false;
                }
            }
        }

        System.out.println(result);
    }
}
