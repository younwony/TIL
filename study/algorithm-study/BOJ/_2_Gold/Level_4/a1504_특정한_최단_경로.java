package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class a1504_특정한_최단_경로 {

    static int N;
    static ArrayList<Node>[] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] NE = bufferedReader.readLine().split(" ");
        N = Integer.parseInt(NE[0]);
        int E = Integer.parseInt(NE[1]);
        tree = new ArrayList[N];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new ArrayList<>();
        }

        for(int i = 0 ; i < E; i++){
            String[] abc = bufferedReader.readLine().split(" ");
            int a = Integer.parseInt(abc[0]) - 1;
            int b = Integer.parseInt(abc[1]) - 1;
            int c = Integer.parseInt(abc[2]);
            tree[a].add(new Node(b, c));
            tree[b].add(new Node(a, c));
        }

        String[] v1v2 = bufferedReader.readLine().split(" ");
        int v1 = Integer.parseInt(v1v2[0]) - 1;
        int v2 = Integer.parseInt(v1v2[1]) - 1;

        long v1Min = getValue(0, v1) + getValue(v1,v2) + getValue(v2,N-1);
        long v2Min = getValue(0, v2) + getValue(v2,v1) + getValue(v1,N-1);
        long result = Math.min(v1Min, v2Min);
        System.out.println(result < 0 ? -1 : result);

    }

    private static long getValue(int start, int V) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        boolean[] visited = new boolean[N];
        int[] valueArray = new int[N];
        Arrays.fill(valueArray,Integer.MAX_VALUE);
        valueArray[start] = 0;
        pq.add(new Node(start,valueArray[start]));
        while(!pq.isEmpty()){
            Node node = pq.poll();
            if(visited[node.V]) continue;
            visited[node.V] = true;

            for(Node next : tree[node.V]){
                if(!visited[next.V]){
                    valueArray[next.V] = Math.min(valueArray[next.V], valueArray[node.V] + next.W);
                    pq.add(new Node(next.V, valueArray[next.V]));
                }
            }
        }
        return valueArray[V] == Integer.MAX_VALUE ? -Integer.MIN_VALUE : valueArray[V];
    }

    static class Node implements Comparable<Node>{
        int V;
        int W;

        public Node(int v, int w) {
            V = v;
            W = w;
        }

        @Override
        public int compareTo(Node o) {
            return this.W - o.W;
        }
    }
}
