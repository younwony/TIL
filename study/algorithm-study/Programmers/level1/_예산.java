package me.wony.level_1;

import java.util.Arrays;

public class _예산 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,3,2,5,4}, 9));
        System.out.println(solution(new int[]{2,2,3,3}, 10));
    }

    private static int solution(int[] d, int budget) {
        int answer = 0;
        Arrays.sort(d);

        for (int i : d) {
            budget -= i;
            answer++;
            if(budget < 0 ) {
                answer--;
                break;
            }
        }
        return answer;
    }
}
