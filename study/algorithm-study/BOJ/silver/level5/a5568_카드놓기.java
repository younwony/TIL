package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class a5568_카드놓기 {

    static Set<String> set = new HashSet<>();
    static int[] arrays;
    static boolean[] visited;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        int k = Integer.parseInt(bufferedReader.readLine());

        arrays = new int[n];
        visited = new boolean[n];
        for (int i = 0; i < arrays.length; i++) {
            arrays[i] = Integer.parseInt(bufferedReader.readLine());
        }

        dfs(k, "");

        System.out.println(set.size());

    }

    private static void dfs(int k, String s) {
        if(k == 0){
            set.add(s);
            return;
        }

        for (int i = 0; i < arrays.length; i++) {
            if(!visited[i]){
                visited[i] = true;
                dfs(k-1, s + arrays[i]);
                visited[i] = false;
            }
        }
    }
}
