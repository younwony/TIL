package level_2;

import java.util.ArrayList;

public class _약수의_개수와_덧셈 {

    public static void main(String[] args) {
        System.out.println(solution(24, 27));
    }

    static int[] arr = new int[1001];

    public static int solution(int left, int right) {
        int answer = 0;

        set();

        for(int i = left; i <= right; i++) {
            answer += (arr[i] % 2 == 0) ? i : -i;
        }

        return answer;
    }

    private static void set() {
        for(int i = 1; i <= 1000; i++) {
            for(int j = 1; j * i <= 1000; j++) {
                arr[i * j]++;
            }
        }
    }
}
