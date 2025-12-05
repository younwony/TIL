package easy;

import java.util.HashSet;
import java.util.Set;

public class OddOccurrencesInArray {
    public static void main(String[] args) {
        int[] array = new int[]{9, 3, 9, 3, 9, 7, 9};
        System.out.println(solution(array));
    }

    public static int solution(int[] A) {
        // Implement your solution here
        Set<Integer> set = new HashSet<>();
        for (int i : A) {
            if (set.contains(i)) {
                set.remove(i);
            } else {
                set.add(i);
            }
        }

        return set.iterator().next();
    }
}
