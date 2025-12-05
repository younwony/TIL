package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class a16236_아기상어 {
    /*
    상어 시작 크기 2
    크기보다 작은 물고기만 섭취가능
    같으면 섭취 불가능하지만 지나가기 가능
    클경우 지나가기도 불가능

    이동 시간 칸당 1초
    현재 크기만큼 물고기 먹을경우 크기 증가

    먹을 수 있는 물고기가 1마리일경우 물고기 먹으러 간다
    1마리 이상일 경우 거리 최솟값으로 이동
    거리 같을 경우 가장 상단
    상단 역시 같을 경우 가장 좌측으로 이동

    */

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[][] field = new int[n][n];
        int[] xArray = new int[]{-1,0,0,1};
        int[] yArray = new int[]{0,-1,1,0};
        int sharkX = 0;
        int sharkY = 0;
        int sharkWeight = 2;
        int eat = 0;
        int time = 0;
        for(int i = 0 ; i < n; i++){
            String[] inputField = bufferedReader.readLine().split(" ");
            for(int j = 0 ;j < n; j++){
                field[i][j] = Integer.parseInt(inputField[j]);
                if(field[i][j] == 9){
                    sharkX = i;
                    sharkY = j;
                    field[i][j] = 0;
                }
            }
        }


        Main :
        while(true){
            PriorityQueue<Fish> fishQue = new PriorityQueue<>(new Fish());
            Queue<Fish> moveQue = new LinkedList<>();
            boolean[][] visited = new boolean[n][n];
            visited[sharkX][sharkY] = true;
            field[sharkX][sharkY] = 0;
            moveQue.add(new Fish(sharkX, sharkY, 0));

            /* 물고기 찾을 때까지 */
            while(!moveQue.isEmpty()){
                Fish shark = moveQue.poll();

                for(int i = 0; i < 4; i++){
                    int nextX = shark.x + xArray[i];
                    int nextY = shark.y + yArray[i];

                    /* 필드값 벗어낫을 경우 제외 */
                    if(isNonField(nextX,nextY,n)) continue;
                    if(visited[nextX][nextY]) continue;

                    /* 물고기 크기*/
                    int fish = field[nextX][nextY];

                    /* 먹을 수 있는 물고기일 경우 FishQue 삽입*/
                    if(fish < sharkWeight && fish != 0){
                        visited[nextX][nextY] = true;
                        fishQue.add(new Fish(nextX, nextY, shark.distance + 1));
                    }
                    /* 이동 가능한 물고기일 경우 moveQue */
                    if(fish == 0 || fish == sharkWeight){
                        visited[nextX][nextY] = true;
                        moveQue.add(new Fish(nextX, nextY, shark.distance + 1 ));
                    }
                }
            }

            if(fishQue.isEmpty()) break ;

            /* 물고기 찾을 경우 */
            Fish nextFish = fishQue.poll();
            sharkX = nextFish.x;
            sharkY = nextFish.y;
            eat++;
            if(sharkWeight == eat){
                sharkWeight++;
                eat = 0;
            }
            time += nextFish.distance;
//            System.out.println("좌표 : " + sharkX + "," + sharkY + "\t 물고기 크기 : " + field[sharkX][sharkY] + " : 현재 무게 : " + sharkWeight + ", 먹은 물고기양 : " + eat + ", 현재 시간 : " + time);
        }

        System.out.println(time);

    }

    private static boolean isNonField(int nextX, int nextY, int n) {
        return nextX < 0 || nextY < 0 || nextX == n || nextY == n;
    }

    static class Fish implements Comparator<Fish>{
        int x;
        int y;
        int distance;

        public Fish() {
        }

        public Fish(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        @Override
        public int compare(Fish o1, Fish o2) {
            int dis = o1.distance - o2.distance;
            if(dis == 0){
                int x = o1.x - o2.x;
                if(x == 0) return o1.y - o2.y;
                return x;
            }
            return dis;
        }
    }
}
