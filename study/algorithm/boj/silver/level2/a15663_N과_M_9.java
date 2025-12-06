package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a15663_N과_M_9 {
    static int[] inputs;
    static ArrayList<String> outputs = new ArrayList<>();
    static boolean[] visited;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        String[] input = bufferedReader.readLine().split(" ");
        inputs = new int[n];
        for (int i = 0; i < input.length; i++) {
            inputs[i] = Integer.parseInt(input[i]);
        }

        Arrays.sort(inputs);

        visited = new boolean[inputs.length];
        
        dfs(0,m, "");

        for (String output : outputs) {
            System.out.println(output);
        }
    }

    private static void dfs(int depth, int max, String s) {
        if(depth == max){
            if(!outputs.contains(s)) outputs.add(s);
            return;
        }

        for(int i = 0; i < inputs.length; i++){
            if(!visited[i]){
                visited[i] = true;
                dfs(depth + 1, max, s + inputs[i] + " ");
                visited[i] = false;
            }
        }
    }
}
