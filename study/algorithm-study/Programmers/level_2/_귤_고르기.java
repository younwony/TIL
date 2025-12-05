package level_2;

import java.util.Arrays;
import java.util.Collections;

public class _귤_고르기 {
    public static void main(String[] args) {
        System.out.println(solution(6, new int[]{1, 3, 2, 5, 4, 5, 2, 100_000}));
    }

    private static int[] tang = new int[10_000_001];

    public static int solution(int k, int[] tangerine) {
        int answer = 0;

        for (int i : tangerine) {
            tang[i]++;
        }

        Arrays.sort(tang);

        for (int i = tang.length - 1; i >= 0; i--) {
            if (tang[i] > 0) {
                k -= tang[i];
                answer++;
                if(k <= 0) break;
            }
        }

        return answer;
    }
}
