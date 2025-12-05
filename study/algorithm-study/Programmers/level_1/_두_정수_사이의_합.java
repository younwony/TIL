package me.wony.level_1;

public class _두_정수_사이의_합 {
    public static void main(String[] args) {
        System.out.println(solution(3, 5));
        System.out.println(solution(3, 3));
        System.out.println(solution(5, 3));
    }

    private static long solution(int a, int b) {
        long answer = 0;
        long start =Math.min(a, b);
        long last = Math.max(a, b);
        /* start ~ last 까지 더하기 */
        for(;start<=last;start++){
            answer+=start;
        }
        return answer;
    }

    private static long bestSolution(int a, int b){
        long answer = 0;

        long start =Math.min(a, b);
        long last = Math.max(a, b);

        /* 등차 수열 공식 사용 !! */
        return (last - start + 1) * (start + last)/2;
    }
}
