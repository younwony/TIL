package level_2;

import java.util.HashSet;
import java.util.Set;

public class _연속_부분_수열_합의_개수 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{7, 9, 1, 1, 4}));
    }

    public static int solution(int[] elements) {
        Set<Integer> set = new HashSet<>();

        int length = elements.length;
        for (int i = 1; i <= length; i++) {
            for (int j = 0; j < length + i; j++) {
                int sum = 0;
                for(int k = 0; k < i; k++){
                    int index = (j + k) % length;
                    sum += elements[index];
                }
                set.add(sum);
            }
        }

        return set.size();
    }
}
