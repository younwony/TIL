package Problem_2021_Intern;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Problem_4 {
    public static void main(String[] args) {

        System.out.println(solution(3, 1, 3, new int[][]{{1,2,2},{3,2,3}}, new int[]{2}));
        System.out.println(solution(4, 1, 4, new int[][]{{1,2,1},{3,2,1},{2,4,1}}, new int[]{2,3}));
    }

    public static int solution(int n, int start, int end, int[][] roads, int[] traps) {
        int answer = 0;
        HashMap<Integer, Boolean> trapMap = new HashMap<>();
        for (int trap : traps) {
            trapMap.put(trap, false);
        }
        int[][] field = new int[n+1][n+1];
        for (int[] road : roads) {
            int time = field[road[0]][road[1]];
            if(time > 0) time = Math.min(time, road[2]);
            else if(time == 0) time = road[2];
            field[road[0]][road[1]] = time;
        }

        Queue<Person> queue = new LinkedList<>();
        queue.add(new Person(start, 0, (HashMap<Integer, Boolean>) trapMap.clone()));

        while(!queue.isEmpty()){
            Person person = queue.poll();
            int point = person.point;
            int time = person.time;
            HashMap<Integer, Boolean> trap = person.trap;

            /* 현재 지점에 트랩일 경우 트랩 체크*/
            boolean isTrap = trapMap.containsKey(point);
            /* 방향이 바뀌었는지 체크 */
            boolean moveChange = false;
            if(isTrap) moveChange = trap.get(point);

            for (int i = 1; i < n+1; i++) {
                /* 방향이 바뀌었으면 point로 향하는 경로가 이동할 수 있는 경로*/
                int pointToTime = moveChange ? field[i][point] : field[point][i];

                /* 가는 길이 있을 때*/
                if(pointToTime > 0){

                    /* point가 지점에 도달할 수 있으면 종료 */
                    if(end == i){
                        return time + pointToTime;
                    }
                    /* 다음지점에 저장할 트랩 */
                    HashMap<Integer, Boolean> cloneTrap = (HashMap<Integer, Boolean>) trap.clone();

                    /* 다음 지점이 트랩일경우 트랩 발동 */
                    if(trapMap.containsKey(i)){
                        cloneTrap.put(i, !cloneTrap.get(i));
                    }

                    /* 다음 지점 삽입*/
                    queue.add(new Person(i, time + pointToTime, cloneTrap));
                }
            }
        }

        return answer;
    }

    static class Person{
        int point;
        int time;
        HashMap<Integer,Boolean> trap;

        public Person(int point, int time, HashMap<Integer, Boolean> trap) {
            this.point = point;
            this.time = time;
            this.trap = trap;
        }
    }

}
