package Problem_2021_Intern;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Problem_2 {
    public static void main(String[] args) {
        System.out.println(solution(new String[][]{{"POOOP", "OXXOX", "OPXPX", "OOXOX", "POXXP"}, {"POOPX", "OXPXP", "PXXXO", "OXXXO", "OOOPP"}, {"PXOPX", "OXOXP", "OXPXX", "OXXXP", "POOXX"}, {"OOOXX", "XOOOX", "OOOXX", "OXOOX", "OOOOO"}, {"PXPXP", "XPXPX", "PXPXP", "XPXPX", "PXPXP"}}));
    }

    public static int[] solution(String[][] places) {
        int[] xArray = new int[]{-1,0,0,1};
        int[] yArray = new int[]{0,-1,1,0};
        int[] answer = new int[5];
        for(int index = 0; index < places.length; index++){
            String[] place = places[index];
            //given
            String[][] room = new String[5][5];
            ArrayList<Person> personArrayList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                String[] placeArray = place[i].split("");
                for (int j = 0; j < placeArray.length; j++) {
                    room[i][j] = placeArray[j];
                    if("P".equals(room[i][j])){
                        personArrayList.add(new Person(i,j,0));
                    }
                }
            }

            //when
            when :
            for (Person person : personArrayList) {
                Queue<Person> queue = new LinkedList<>();
                boolean[][] visited = new boolean[5][5];
                visited[person.x][person.y] = true;
                queue.add(person);
                while(!queue.isEmpty()){
                    Person p = queue.poll();
                    for (int i = 0; i < 4; i++) {
                        int nextX = p.x + xArray[i];
                        int nextY = p.y + yArray[i];
                        if(isRoom(nextX,nextY) && !visited[nextX][nextY]){

                            if("P".equals(room[nextX][nextY])) {
                                answer[index] = 1;
                                break when;
                            }

                            if("O".equals(room[nextX][nextY]) && p.men < 1){
                                visited[nextX][nextY] = true;
                                queue.add(new Person(nextX,nextY, p.men+1));
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < answer.length; i++) {
            answer[i] = answer[i] == 0 ? 1 : 0;
        }

        return answer;
    }

    private static boolean isRoom(int nextX, int nextY) {
        return nextX >= 0 && nextY >= 0 && nextX < 5 && nextY < 5;
    }

    static class Person{
        int x;
        int y;
        int men;

        public Person(int x, int y, int men) {
            this.x = x;
            this.y = y;
            this.men = men;
        }
    }
}
