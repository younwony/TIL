package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class a1916_최소비용_구하기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int m = Integer.parseInt(bufferedReader.readLine());

        ArrayList<Node>[] list = new ArrayList[n+1];
        int[] answer = new int[n+1];
        Arrays.fill(answer, Integer.MAX_VALUE);
        boolean[] visited = new boolean[n+1];

        for(int i = 0 ; i < list.length; i++){
            list[i] = new ArrayList<>();
        }

        for(int i = 0 ; i < m; i ++){
            String[] SEV = bufferedReader.readLine().split(" ");
            int start = Integer.parseInt(SEV[0]);
            int end = Integer.parseInt(SEV[1]);
            int value = Integer.parseInt(SEV[2]);

            list[start].add(new Node(end,value));
        }

        String[] SE = bufferedReader.readLine().split(" ");
        int start = Integer.parseInt(SE[0]);
        int end = Integer.parseInt(SE[1]);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Node(start,0));
        answer[start] = 0;

        while(!priorityQueue.isEmpty()){
            Node node = priorityQueue.poll();

            if(visited[node.V]) continue;
            visited[node.V] = true;

            for (Node next : list[node.V]) {
                if(!visited[next.V]){
                    answer[next.V] = Math.min(answer[node.V] + next.W, answer[next.V]);
                    priorityQueue.add(new Node(next.V, answer[next.V]));
                }
            }
        }

        System.out.println(answer[end]);
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
