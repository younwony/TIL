package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class a9934_완전_이진_트리 {
    static String[] binaryTree;
    static boolean[] visitied;
    static ArrayList<Integer>[] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int K = Integer.parseInt(bufferedReader.readLine());
        binaryTree = bufferedReader.readLine().split(" ");
        visitied = new boolean[binaryTree.length];
        tree = new ArrayList[K];

        for(int i = 0 ; i < K; i++){
            tree[i] = new ArrayList<>();
        }

        dfs(0, binaryTree.length, 0);

        StringBuilder result = new StringBuilder();
        for (ArrayList<Integer> nodeList : tree) {
            for (Integer node : nodeList) {
                result.append(node).append(" ");
            }
            result.append("\n");
        }

        System.out.println(result.toString());
    }

    private static void dfs(int start, int end, int level) {
        int node = (start + end)/2;
        if(node == binaryTree.length || visitied[node]) return;
        visitied[node] = true;
        tree[level].add(Integer.valueOf(binaryTree[node]));
        dfs(start, node-1, level+1);
        dfs(node+1, end, level+1);
    }

    static class Point{
        int start;
        int end;

        public Point(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
