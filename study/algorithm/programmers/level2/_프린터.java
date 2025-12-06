package me.wony.level_2;

import java.util.LinkedList;
import java.util.Queue;

public class _프린터 {

    public static void main(String[] args) {
        System.out.println(solution(new int[]{2,1,3,2}, 2));
        System.out.println(solution(new int[]{1,1,9,1,1,1}, 0));
    }

    private static int solution(int[] priorities, int location) {
        int answer = 0;

        Queue<Prior> queue = new LinkedList<>();

        for (int i = 0; i < priorities.length; i++) {
            queue.add(new Prior(i, priorities[i]));
        }

        while(true){
            Prior prior = queue.poll();
            int index = prior.getIndex();
            int priority = prior.getPriority();

            if(isPriority(priority, queue)) {
                queue.add(prior);
                continue;
            } else answer++;

            if(index == location) break;
        }

        return answer;
    }

    private static boolean isPriority(int priority, Queue<Prior> queue) {
        for (Prior prior : queue) {
            if(prior.getPriority() > priority) return true;
        }
        return false;
    }

    static class Prior{
        int index;
        int priority;

        public Prior(int index, int priority) {
            this.index = index;
            this.priority = priority;
        }

        public int getIndex() {
            return index;
        }

        public int getPriority() {
            return priority;
        }
    }
}
