package easy;

public class TapeEquilibrium {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{3, 1, 2, 4, 3}));
        System.out.println(solution(new int[]{-3, -1, -2, -4, -3}));
    }

    public static int solution(int[] A) {
        // Implement your solution here
        int result = Integer.MAX_VALUE;

        int totalSum = 0;
        for (int i = 0; i < A.length; i++) {
            totalSum += A[i];
        }

        int leftSum = 0;
        for (int i = 0; i < A.length - 1; i++) {
            leftSum += A[i];
            result = Math.min(result, Math.abs(leftSum - (totalSum - leftSum)));
        }

        return result;
    }
}
