package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1991_트리_순회 {

    static StringBuilder prefix = new StringBuilder();
    static StringBuilder infix = new StringBuilder();
    static StringBuilder postfix = new StringBuilder();
    static int[][] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        tree = new int[n+1][2];

        for (int i = 0; i < n; i++) {
            String[] input = bufferedReader.readLine().split(" ");
            int parentNode = input[0].charAt(0) - 64;
            int leftNode = input[1].charAt(0) - 64;
            int rightNode = input[2].charAt(0) - 64;

            if(leftNode != -18) tree[parentNode][0] = leftNode;
            if(rightNode != -18) tree[parentNode][1] = rightNode;
        }

        prefix(1);
        infix(1);
        postfix(1);

        System.out.println(prefix.toString());
        System.out.println(infix.toString());
        System.out.println(postfix.toString());
    }

    /* 후위 순회
    * 하위 트리 모두 방문후 올라가는 순
    *  왼쪽 하위 -> 오른쪽 하위 -> 노드
    * */
    private static void postfix(int node) {
        if(tree[node][0] != 0) postfix(tree[node][0]);
        if(tree[node][1] != 0) postfix(tree[node][1]);
        postfix.append((char)(node+64));
    }

    /* 중위 순회
    *  왼쪽 하위 부터 루트까지 방문
    * 왼쪽 하위 -> 노드 -> 오른쪽 하위
    * */
    private static void infix(int node) {
        if(tree[node][0] != 0) infix(tree[node][0]);
        infix.append((char)(node+64));
        if(tree[node][1] != 0) infix(tree[node][1]);
    }

    /* 전위 순회
    *  현재 노드 부터 루트까지
    * 노드 -> 왼쪽 하위 -> 오른쪽 하위
    * */
    private static void prefix(int node) {
        prefix.append((char)(node+64));
        if(tree[node][0] != 0)prefix(tree[node][0]);
        if(tree[node][1] != 0)prefix(tree[node][1]);
    }
}
