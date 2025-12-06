package easy;

public class CyclicRotation {
    public static void main(String[] args) {
        int[] solution = solution(new int[]{3, 8, 9, 7, 6}, 3);
        for (int i : solution) {
            System.out.println(i);
        }

        int[] solution2 = solution(new int[]{1, 2, 3, 4}, 4);
        for (int i : solution2) {
            System.out.println(i);
        }
    }
    public static int[] solution(int[] A, int K) {
        // Implement your solution here
        int len = A.length;
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[(K + i) % len] = A[i];
        }
        return result;
    }
}
