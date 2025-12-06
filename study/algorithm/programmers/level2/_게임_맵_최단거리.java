package level_2;

import java.util.LinkedList;
import java.util.Queue;

public class _게임_맵_최단거리 {

    public static void main(String[] args) {
        System.out.println(solution(new int[][]{{1,0,1,1,1},{1,0,1,0,1},{1,0,1,1,1},{1,1,1,0,1},{0,0,0,0,1}}));
        System.out.println(solution(new int[][]{{1,0,1,1,1},{1,0,1,0,1},{1,0,1,1,1},{1,1,1,0,0},{0,0,0,0,1}}));
    }


    private static int solution(int[][] maps) {
        //given
        int answer = Integer.MAX_VALUE;
        int endCnt = 0;
        boolean[][] visited = new boolean[maps.length][maps[0].length];
        int[] xArray = {1,0,0,-1};
        int[] yArray = {0,1,-1,0};
        Queue<Location> queue = new LinkedList<>();
        queue.add(new Location(0,0,1));
        visited[0][0] = true;

        //when
        while(!queue.isEmpty()){
            Location now = queue.poll();
            int x = now.x;
            int y = now.y;
            int cnt = now.cnt;
            if(x == maps.length-1 && y == maps[0].length-1){
                endCnt++;
                answer = Math.min(answer, cnt);
            }
            for(int i = 0; i < 4; i++){
                int nextX = x+xArray[i];
                int nextY = y+yArray[i];
                if(isNonFiled(nextX,nextY, maps.length, maps[0].length)) continue;
                if(visited[nextX][nextY]) continue;
                if(maps[nextX][nextY] == 0) continue;
                visited[nextX][nextY] = true;
                queue.add(new Location(nextX, nextY, cnt+1));
            }
        }

        return endCnt == 0 ? -1 : answer;
    }

    private static boolean isNonFiled(int nextX, int nextY, int maxX, int maxY ) {
        if(nextX < 0) return true;
        if(nextY < 0) return true;
        if(nextX > maxX -1) return true;
        if(nextY > maxY -1) return true;

        return false;
    }

    static class Location{
        int x;
        int y;
        int cnt;

        public Location(int x, int y, int cnt) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
        }
    }
}
