package _2_Gold.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a1238_파티 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] NMX = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(NMX[0]);
        int m = Integer.parseInt(NMX[1]);
        int x = Integer.parseInt(NMX[2]) -1;
        ArrayList<Node>[] tree = new ArrayList[n];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            String[] SET = bufferedReader.readLine().split(" ");
            int s = Integer.parseInt(SET[0]) - 1;
            int e = Integer.parseInt(SET[1]) - 1;
            int t = Integer.parseInt(SET[2]);
            tree[s].add(new Node(e,t));
        }

        int[][] result = new int[n][n];
        for(int i = 0 ; i < n; i++){
            Arrays.fill(result[i],Integer.MAX_VALUE);
            boolean[] visitied = new boolean[n];
            //when
            PriorityQueue<Node> pq = new PriorityQueue<>();
            result[i][i] = 0;
            pq.add(new Node(i,0));
            while(!pq.isEmpty()){
                Node node = pq.poll();
                if(visitied[node.V]) continue;
                visitied[node.V] = true;

                for(Node next : tree[node.V]){
                    if(!visitied[next.V]){
                        result[i][next.V] = Math.min(result[i][next.V], result[i][node.V] + next.T);
                        pq.add(new Node(next.V, result[i][next.V]));
                    }
                }
            }
        }

        int max = 0;
        for(int i = 0 ; i < n ; i ++){
            int person = result[x][i] + result[i][x];
            max = Math.max(max,person);
        }

        System.out.println(max);
    }

    static class Node implements Comparable<Node>{

        int V;
        int T;

        public Node(int v, int t) {
            V = v;
            T = t;
        }

        @Override
        public int compareTo(Node o) {
            return this.T - o.T;
        }
    }
}
