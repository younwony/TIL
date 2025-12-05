package level_3;

import java.util.LinkedList;
import java.util.Queue;

public class _네트워크 {
    public static void main(String[] args) {

        System.out.println(solution(3, new int[][]{{1,1,0}, {1,1,0}, {0,0,1}}));
        System.out.println(solution(3, new int[][]{{1,1,0}, {1,1,1}, {0,1,1}}));
    }

    public static int solution(int n, int[][] computers) {
        int answer = 0;

        for (int i = 0; i < computers.length; i++) {
            for (int j = i; j < computers[0].length; j++) {
                if(computers[i][j] == 1){
                    answer++;
                    bfs(computers, i);
                }
            }
        }

        return answer;
    }

    private static void bfs(int[][] computers, int i) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(i);
        while(!queue.isEmpty()){
            Integer poll = queue.poll();
            for(int j = 0; j < computers.length; j++){
                if(computers[poll][j] == 1){
                    computers[poll][j] = 0;
                    queue.add(j);
                }
            }
        }
    }
}
