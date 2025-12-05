package me.wony.level_2;

public class _멀쩡한_사각형 {

    public static void main(String[] args) {
        System.out.println(solution(8, 12));
    }

    private static long solution(int w, int h) {
        long answer = 0;

        /* 대각선의 기울기를 구해 1 ~ w-1 까지 나늣셈을이용해 정수의 부분(정사각형)만을 구한다. */
        for(int i = 1; i < w; i++){
            answer += (long)i * (long)h / (long)w;
        }

        return answer*2;
    }
}
