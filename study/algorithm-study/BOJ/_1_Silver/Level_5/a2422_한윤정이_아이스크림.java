package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class a2422_한윤정이_아이스크림 {

    private static int[] iceCreams = new int[3];
    private static boolean[][] nonIce;
    private static int N;
    private static int cnt;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());

        nonIce = new boolean[N+1][N+1];
        for(int i = 0; i < M; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int a = Integer.parseInt(stringTokenizer.nextToken());
            int b = Integer.parseInt(stringTokenizer.nextToken());
            nonIce[a][b] = true;
            nonIce[b][a] = true;
        }

        dfs(0, 1);

        System.out.println(cnt);
    }

    private static void dfs(int index, int number) {
        if(index == 3){
            isOk();
            return;
        }

        for (int i = number; i <= N - 2 + index; i++) {
            iceCreams[index] = i;
            dfs(index+1, i + 1);
        }
    }

    private static void isOk() {
        for (int i = 0; i < iceCreams.length; i++) {
            for (int j = i+1; j < iceCreams.length; j++) {
                if(nonIce[iceCreams[i]][iceCreams[j]]){
                    return;
                }
            }
        }
        cnt++;
    }
}
