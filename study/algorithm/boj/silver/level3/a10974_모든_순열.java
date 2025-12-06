package _1_Silver.Level_3;

import java.util.ArrayList;
import java.util.Scanner;

public class a10974_모든_순열 {
    static int N;
    static boolean[] visited;
    static StringBuilder stringBuilder = new StringBuilder();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        visited = new boolean[N];
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        dfs(integerArrayList);
        System.out.println(stringBuilder.toString());
    }

    private static void dfs(ArrayList<Integer> integerArrayList) {
        if(integerArrayList.size() == N){
            for (Integer integer : integerArrayList) {
                stringBuilder
                        .append(integer + 1)
                        .append(" ");
            }
            stringBuilder.append("\n");
        }

        for(int i = 0; i < N; i++){
            if(!visited[i]){
                visited[i] = true;
                integerArrayList.add(i);
                dfs(integerArrayList);
                integerArrayList.remove((Object)i);
                visited[i] = false;
            }
        }
    }
}
