package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a1389_케빈_베이컨의_6단계_법칙 {
    static boolean[][] field;
    static int[] persons;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);

        field = new boolean[n][n];
        persons = new int[n];
        for(int i = 0; i < m; i++){
            String[] input = bufferedReader.readLine().split(" ");
            int personA = Integer.parseInt(input[0]) - 1;
            int personB = Integer.parseInt(input[1]) - 1;
            field[personA][personB] = true;
            field[personB][personA] = true;
        }

        for(int i = 0 ; i < n; i++){
            boolean[] visited = new boolean[n];
            visited[i] = true;
            persons[i] = bfs(i,visited);
        }

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < persons.length; i++) {
             min = Math.min(min,persons[i]);
        }
        for (int i = 0; i < persons.length; i++) {
            if(min == persons[i]) {
                System.out.println(i + 1);
                break;
            }
        }
    }

    private static int bfs(int person, boolean[] visited) {
        Queue<Person> queue = new LinkedList<>();
        int sum = 0;
        int depth = 1;
        for(int i = 0 ; i < field.length; i++){
            if(field[person][i] && !visited[i]){
                visited[i] = true;
                queue.add(new Person(i, depth));
                sum += depth;
            }
        }

        while(!queue.isEmpty()){
            Person nextPerson = queue.poll();
            int nextPersonIndex = nextPerson.person;
            int nextDepth = nextPerson.depth + 1;
            for(int i = 0; i < field.length; i++){
                if(field[nextPersonIndex][i] && !visited[i]){
                    visited[i] = true;
                    queue.add(new Person(i, nextDepth));
                    sum += nextDepth;
                }
            }
        }

        return sum;
    }

    static class Person{
        int person;
        int depth;

        public Person(int person, int depth) {
            this.person = person;
            this.depth = depth;
        }
    }
}
