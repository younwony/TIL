package _2_Gold.Level_4;

import java.util.*;

public class a13549_숨바꼭질_3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        boolean[] visited = new boolean[100001];
        PriorityQueue<Person> queue = new PriorityQueue<>();
        queue.add(new Person(N, 0));
        int min = Integer.MAX_VALUE;
        while(!queue.isEmpty()){
            Person person = queue.poll();

            if(visited[person.v]) continue;
            visited[person.v] = true;

            if(person.v == K){
                min = person.time;
                break;
            }

            int plus = person.v + 1;
            int minus = person.v - 1;
            int pow = person.v * 2;

            if(isRange(pow) && !visited[pow]){
                queue.add(new Person(pow, person.time));
            }
            if(isRange(plus) && !visited[plus]){
                queue.add(new Person(plus, person.time + 1));
            }
            if(isRange(minus) && !visited[minus]){
                queue.add(new Person(minus, person.time + 1));
            }
        }

        System.out.println(min);
    }

    private static boolean isRange(int plus) {
        return plus >= 0 && plus <= 100000;
    }

    static class Person implements Comparable<Person> {
        int v;
        int time;

        public Person(int v, int time) {
            this.v = v;
            this.time = time;
        }

        @Override
        public int compareTo(Person o) {
            return this.time - o.time;
        }
    }
}
