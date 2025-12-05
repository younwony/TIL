package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a3151_합이_0 {
    static int cnt;
    static int[] arrays;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine().trim());
        arrays = new int[N];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        dfs(0, 0, 0);

        System.out.println(cnt);
    }

    private static void dfs(int index, int depth, int sum) {
        if(depth == 3){
            if(sum == 0){
                cnt++;
            }
            return;
        }
        for (int i = index; i < arrays.length; i++) {
            dfs(i+1, depth+1, sum+arrays[i]);
        }
    }
}
