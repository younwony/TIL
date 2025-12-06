package me.wony.level_1;

import java.util.Arrays;

public class _제일_작은_수_제거하기 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{4,3,2,1}));
        System.out.println(solution(new int[]{10}));
    }
    private static int[] solution(int[] arr) {
        /* 사이즈 1일경우 -1 반환 */
        if(arr.length == 1) return new int[]{-1};

        /* 최소값 지정 */
        int min = Integer.MAX_VALUE;
        /* 최소값 구하기 */
        for (int i = 0; i < arr.length; i++) {
            int number = arr[i];
            min = Math.min(number, min);
        }

        /* output 배열 생성*/
        int[] answer = new int[arr.length-1];

        /* output 배열 인덱스 */
        int answerIndex = 0;

        for (int number : arr) {
            /* 최소값이 아닐 경우 insert */
            if(min != number){
                answer[answerIndex++] = number;
            }
        }

        return answer;
    }
}
