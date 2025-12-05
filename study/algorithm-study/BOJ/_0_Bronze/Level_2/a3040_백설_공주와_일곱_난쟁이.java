package _0_Bronze.Level_2;

import java.util.Scanner;

public class a3040_백설_공주와_일곱_난쟁이 {
    static int[] dwarfs = new int[9];
    static boolean[] visited = new boolean[9];
    static StringBuilder stringBuilder = new StringBuilder();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < dwarfs.length; i++) {
            dwarfs[i] = scanner.nextInt();
        }
        dfs(0);
        System.out.println(stringBuilder.toString());
    }

    private static void dfs(int i) {
        if(i == 7){
            int sum = 0;
            for (int j = 0; j < dwarfs.length; j++) {
                if(visited[j]){
                    sum += dwarfs[j];
                }
            }
            if(sum == 100){
                for (int j = 0; j < visited.length; j++) {
                    if(visited[j]){
                        stringBuilder
                                .append(dwarfs[j])
                                .append("\n");
                    }
                }
            }
            return;
        }

        for (int j = 0; j < dwarfs.length; j++) {
            if(!visited[j]) {
                visited[j] = true;
                dfs(i+1);
                if(!stringBuilder.toString().isEmpty()){
                    return;
                }
                visited[j] = false;
            }
        }
    }
}
