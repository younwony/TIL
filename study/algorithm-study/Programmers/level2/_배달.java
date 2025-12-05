package level_2;

import java.util.LinkedList;
import java.util.Queue;

public class _배달 {

    /**
     * 1 번부터 시작
     * n개의 마을 중 k 시간 이하로 배달 가능한 마을만 주문
     */
    public static void main(String[] args) {
        System.out.println(solution(5, new int[][]{{1,2,1},{2,3,3},{5,2,2},{1,4,2},{5,3,1},{5,4,2}},3));
        System.out.println(solution(6, new int[][]{{1,2,1},{1,3,2},{2,3,2},{3,4,3},{3,5,2},{3,5,3},{5,6,1}},4));
    }
    
    private static int solution(int N, int[][] road, int K) {
        //given
        int answer = 0;
        /* 인접 행렬 필드 생성 */
        int[][] field = new int[N+1][N+1];
        for (int i = 0; i < road.length; i++) {
            int[] roads = road[i];
            int start = roads[0];
            int end = roads[1];
            int hour = roads[2];
            /* a -> b로 가는길이 여러개 일수 있기 때문에 최소 시간의 거리만 저장*/
            if(field[start][end] > 0)  field[start][end] = Math.min(field[start][end], hour);
            if(field[start][end] == 0) field[start][end] = hour;
            if(field[end][start] > 0)  field[end][start] = Math.min(field[end][start], hour);
            if(field[end][start] == 0) field[end][start] = hour;
        }

        int dp[] = new int[N+1];
        /* 거리의 최소값 로직 사용을 위해 각각 마을까지의 시간 값을 최대로 저장*/
        for (int i = 0; i < dp.length; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[1] = 0;
        Queue<Integer> queue = new LinkedList<>();
        /* 시작점은 1부터 */
        queue.add(1);
        while(!queue.isEmpty()){
            int start = queue.poll();
            for(int i = 1; i <= N; i++){
                int hour = field[start][i];
                /* 이전의 시작점부터 현재 마을 위치까지 다리가 있는지 체크*/
                if(hour == 0 || i == start) continue;
                /* 다리가 있다면 현재 시작점까지의 시간(dp[start]) + 시작점 -> 현재 마을까지 걸린시간(hour) 더하기 */
                hour += dp[start];
                /* 이미 dp에 저장된 값보다 작으면 패스*/
                if(dp[i] <= hour) continue;
                /* 최솟 값일 경우 dp에 값을 넣고, Queue에도 값을 넣는다. */
                dp[i] = hour;
                queue.add(i);
            }
        }

        for (int i : dp) {
            if(i <= K) answer++;
        }

        return answer;
    }

    static class Home {
        int start;
        int end;
        int hour;

        public Home(int start, int end, int hour) {
            this.start = start;
            this.end = end;
            this.hour = hour;
        }
    }
}
