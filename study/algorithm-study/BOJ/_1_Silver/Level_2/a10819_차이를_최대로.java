package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a10819_차이를_최대로 {
    static int[] NArray;
    static boolean[] visited;
    static int[] result;
    static int max;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        NArray = new int[N];
        result = new int[N];
        visited = new boolean[N];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for(int i = 0; i < NArray.length; i++){
            NArray[i] = Integer.parseInt(stringTokenizer.nextToken());
        }
        dfs(0);

        System.out.println(max);
    }

    private static void dfs(int depth){
        if(depth == NArray.length){
            isMax();
            return;
        }

        for(int i = 0; i < NArray.length; i++){
            if(!visited[i]){
                visited[i] = true;
                result[depth] = NArray[i];
                dfs(depth+1);
                visited[i] = false;
            }
        }

    }

    private static void isMax() {
        int sum = 0;
        for (int i = 0; i < result.length-1; i++) {
            sum += Math.abs(result[i] - result[i+1]);
        }
        max = Math.max(max, sum);
    }
}
