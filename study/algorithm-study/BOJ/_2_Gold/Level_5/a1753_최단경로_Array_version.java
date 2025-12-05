package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class a1753_최단경로_Array_version {

    /* 다익스트라 배열 방법
    * 속도 2004ms
    * */
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader( System.in));
        String[] VE = bufferedReader.readLine().split(" ");
        int V = Integer.parseInt(VE[0]);
        int E = Integer.parseInt(VE[1]);
        int K = Integer.parseInt(bufferedReader.readLine());

        /* 인접 리스트로 선언, 인접행렬 선언시 메모리 초과*/
        ArrayList<ArrayList<Edge>> field = new ArrayList<>();
        int[] answer = new int[V+1];
        boolean[] check = new boolean[V+1];
        /* 결과값이 담길 배열 맥스로 초기화 */
        Arrays.fill(answer, Integer.MAX_VALUE);

        /* 인접 리스트 초기화 */
        for(int i = 0 ; i <= V; i++){
            field.add(new ArrayList<>());
        }

        /* Input 값 입력 */
        for(int i = 0; i < E; i++){
            String[] UVW = bufferedReader.readLine().split(" ");
            int u = Integer.parseInt(UVW[0]);
            int v = Integer.parseInt(UVW[1]);
            int w = Integer.parseInt(UVW[2]);
            field.get(u).add(new Edge(v,w));
        }
        /* 시작점은 0*/
        answer[K] = 0;
        /* 시작점을 제외한 모든 노드를 방문하기위해 1부터 시작 */
        for(int index = 1; index < field.size(); index++){
            /* 최소 가중치 노드 방문 체크*/
            check[K] = true;
            /* 현재 K 와 연결된 노드 */
            for(Edge edge : field.get(K)){
                /* 방문 안했을 경우 */
                if(!check[edge.V]){
                    /* 이미 입력된 결과값과, 현재 노드에서 연결된 노드까지의 가중치 값중 최소값 입력*/
                    answer[edge.V] = Math.min(answer[edge.V], answer[K] + edge.W);
                }
            }
            /* 최소 가중치를 찾기위한 변수 */
            int wMin = Integer.MAX_VALUE;
            /* 최소 가중치를 가진 노드의 인덱스를 찾기위한 변수 */
            int indexMin = -1;

            /* 현 시점에서 방문하지 않는 노드중 최소 가중치를 갖는 노드 찾기 */
            for(int i = 1; i < answer.length; i++){
                /* 찾을 경우 스왑 */
                if(!check[i] && answer[i] < wMin){
                    wMin = answer[i];
                    indexMin = i;
                }
            }

            /* 최소 가중치를 갖는 값을 못찾을 경우 종료*/
           if(indexMin == -1) break;

           /* 찾았을 경우 K 를 찾은 Index로 치환 */
            K = indexMin;
        }

        StringBuilder result = new StringBuilder();
        for(int i = 1; i < answer.length; i++){
            result.append(answer[i] == Integer.MAX_VALUE ? "INF" : answer[i]).append("\n");
        }
        System.out.println(result.toString());
    }

    static class Edge{
        int V;
        int W;

        public Edge(int v, int w) {
            V = v;
            W = w;
        }
    }
}
