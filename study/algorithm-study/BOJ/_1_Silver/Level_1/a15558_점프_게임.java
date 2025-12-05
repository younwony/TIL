package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class a15558_점프_게임 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int k = Integer.parseInt(stringTokenizer.nextToken());

        int[][] lines = new int[2][N];
        boolean[][] visited = new boolean[2][N];

        String[] leftInput = bufferedReader.readLine().split("");
        String[] rightInput = bufferedReader.readLine().split("");

        for(int i = 0; i < N; i++){
            lines[0][i] = Integer.parseInt(leftInput[i]);
            lines[1][i] = Integer.parseInt(rightInput[i]);
        }

        Queue<Person> queue = new LinkedList<>();
        queue.add(new Person(0,0,0));
        visited[0][0] = true;

        boolean result = false;

        while(!queue.isEmpty()){
            Person person = queue.poll();

            int line = person.line;
            int location = person.location;
            int time = person.time;

            int nextLocation;
            // 1
            nextLocation = location + 1;
            if(nextLocation >= N){
                result = true;
                break;
            }else{
                if(!visited[line][nextLocation] && lines[line][nextLocation] == 1){
                    visited[line][nextLocation] = true;
                    queue.add(new Person(line,nextLocation,time+1));
                }
            }

            // 2
            nextLocation = location - 1;
            if(nextLocation > time && nextLocation >= 0){
                if(!visited[line][nextLocation] && lines[line][nextLocation] == 1){
                    visited[line][nextLocation] = true;
                    queue.add(new Person(line,nextLocation,time+1));
                }
            }

            //3
            nextLocation = location + k;
            line = line == 0 ? 1 : 0;
            if(nextLocation >= N){
                result = true;
                break;
            }else{
                if(!visited[line][nextLocation] && lines[line][nextLocation] == 1){
                    visited[line][nextLocation] = true;
                    queue.add(new Person(line,nextLocation,time+1));
                }
            }
        }

        System.out.println(result ? 1 : 0);
    }

    static class Person{
        int line; // 0 => left, 1 => right
        int location;
        int time;

        public Person(int line, int location, int time) {
            this.line = line;
            this.location = location;
            this.time = time;
        }
    }

}
