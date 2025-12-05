package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class a1753_최단경로 {
    /* 다익스트라 우선순위 큐 방법
    * 속도 908ms
    * */
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader( System.in));
        String[] VE = bufferedReader.readLine().split(" ");
        int V = Integer.parseInt(VE[0]);
        int E = Integer.parseInt(VE[1]);
        int K = Integer.parseInt(bufferedReader.readLine());

        /* 인접 리스트(가중치) 로 선언, 인접 행렬로 선언시 메모리 초과 발생 */
        ArrayList<Edge>[] field = new ArrayList[V+1];
        /* 초기화 */
        for(int i = 0 ; i <= V; i++){
            field[i] = new ArrayList<>();
        }

        /* Input 입력 */
        for(int i = 0; i < E; i++){
            String[] UVW = bufferedReader.readLine().split(" ");
            int u = Integer.parseInt(UVW[0]);
            int v = Integer.parseInt(UVW[1]);
            int w = Integer.parseInt(UVW[2]);
            field[u].add(new Edge(v,w));
        }

        /* 우선순위 큐 선언, 자동으로 Edge에 implement된 Comparable로 정렬 된다.*/
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        /* 방문 체크용 */
        boolean[] check = new boolean[V+1];
        /* 최종으로 거리의 합이 담겨질 배열 */
        int[] answer = new int[V+1];
        /* 최소값을 구하기위해, 최대값으로 초기화 */
        Arrays.fill(answer,Integer.MAX_VALUE);
        /* 시작점은 0이므로, 0으로 초기화*/
        answer[K] = 0;
        /* 큐에 입력 */
        priorityQueue.add(new Edge(K, 0));


        /* 가중치에 따라서 정렬된 큐이기 때문에 같은 노드가 들어와도 가중치가 적은 노드가 먼저 poll 되게 된다.
        *  먼저 poll된 노드는 방문 체크를 통해 이후에 poll이 되어도 가중치 로직을 타지 않게된다.
        * */
        while(!priorityQueue.isEmpty()){
            Edge edge = priorityQueue.poll();

            /* 이미 방문한 노드일경우 패스*/
            if(check[edge.V]) continue;
            /* 아닐 경우 방문 체크 */
            check[edge.V] = true;

            /* 현재 노드와 연결된 노드 */
            for (Edge next : field[edge.V]) {
                /* 현재 노드 + 연결된 노드 가중치 가 결과값 배열에 입력된 값 보다 작을경우*/
                if(answer[edge.V] + next.W < answer[next.V]){
                    /* 결과값 배열에 현재 노드 + 연결된 노드 가중치 삽입*/
                    answer[next.V] = answer[edge.V] + next.W;
                    /* 우선순위 큐에 해당 노드 가중치와 입력 */
                    priorityQueue.add(new Edge(next.V,answer[next.V]));
                }
            }

        }

        StringBuilder result = new StringBuilder();
        for(int i = 1; i < answer.length; i++){
            result.append(answer[i] == Integer.MAX_VALUE ? "INF" : answer[i]).append("\n");
        }
        System.out.println(result.toString());
    }

    /* 우선순위 정렬을 위해 Comparable 사용 (Comparator 오류 발생)*/
    static class Edge implements Comparable<Edge> {
        int V;
        int W;

        public Edge(int v, int w) {
            V = v;
            W = w;
        }

        /* 현재 거리합의 최소값, 오름차순*/
        @Override
        public int compareTo(Edge o) {
            return this.W - o.W;
        }
    }
}
