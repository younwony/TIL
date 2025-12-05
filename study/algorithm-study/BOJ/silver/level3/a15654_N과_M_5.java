package _1_Silver.Level_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class a15654_N과_M_5 {
    static ArrayList<String> strings = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] input = new int[n];
        boolean[] visited = new boolean[n];
        for(int i = 0; i < n; i++){
            input[i] = scanner.nextInt();
        }

        Arrays.sort(input);
        for(int i = 0; i < n; i++){
            visited[i] = true;
            dfs(input[i] + " ", 1, m, input, visited);
            visited[i] = false;
        }

        for (String string : strings) {
            System.out.println(string);
        }
    }

    private static void dfs(String s, int depth, int max, int[] input, boolean[] visited) {
        if(depth == max){
            strings.add(s);
            return;
        }

        for(int i = 0; i < input.length; i++){
            if(!visited[i]){
                visited[i] = true;
                dfs(s + input[i] + " ", depth+1, max, input, visited);
                visited[i] = false;
            }
        }
    }
}
