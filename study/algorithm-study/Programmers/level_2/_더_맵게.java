package me.wony.level_2;

import java.util.*;

public class _더_맵게 {

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 1, 1}, 4)); //2
        System.out.println(solution(new int[]{1,2,3,9,10,12}, 7));
        System.out.println(solution(new int[]{1, 1, 1}, 4)); //2
        System.out.println(solution(new int[]{10, 10, 10, 10, 10}, 100)); //4
        System.out.println(solution(new int[]{1, 2, 3, 9, 10, 12}, 7)); //2
        System.out.println(solution(new int[]{0, 2, 3, 9, 10, 12}, 7)); //2
        System.out.println(solution(new int[]{0, 0, 3, 9, 10, 12}, 7)); //3
        System.out.println(solution(new int[]{0, 0, 0, 0}, 7)); //-1
        System.out.println(solution(new int[]{0, 0, 3, 9, 10, 12}, 7000)); //-1
        System.out.println(solution(new int[]{0, 0, 3, 9, 10, 12}, 0)); //0
        System.out.println(solution(new int[]{0, 0, 3, 9, 10, 12}, 1)); //2
        System.out.println(solution(new int[]{0, 0}, 0)); //0
        System.out.println(solution(new int[]{0, 0}, 1)); //-1
        System.out.println(solution(new int[]{1, 0}, 1)); //1
        System.out.println(priorQueueSolution(new int[]{0, 0, 3, 9, 10, 12}, 7000)); //-1
    }

    private static int solution(int[] scoville, int K) {
        int answer = 0;
        for (int i = 0; i < scoville.length -1; i++) {
            /* 오름차순 정렬 */
            Arrays.sort(scoville);
            /* 현재 인덱스 값(peek) 가 주어진 K 이상인지 체크, 이상일 경우 break; */
            if(scoville[i] >= K) break;

            /* scoville 섞기*/
            scoville[i+1] = scoville[i] + scoville[i+1]*2;
            /* scoville 현재 인덱스 -1값 주기 */
            scoville[i] = -1;
            /* 섞었으니 결과 ++ */
            answer++;
        }

        /* 마지막 스코빌이 주어진 K보다 작으면 실패로 -1 리턴*/
        if(scoville[scoville.length-1] < K)  return -1;
        return answer;
    }

    private static int priorQueueSolution(int[] scoville, int K){
        int answer = 0;
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int scov : scoville) {
            priorityQueue.offer(scov);
        }

        while(!priorityQueue.isEmpty() && priorityQueue.peek() < K){
            try {
                Integer scovOne = priorityQueue.poll();
                Integer scovTwo = priorityQueue.poll();
                priorityQueue.offer(scovOne + scovTwo*2);
                answer++;
            } catch (NullPointerException e){
                answer = -1;
                break;
            }
        }

        return answer;
    }
}
