package easy;

public class PermMissingElem {

    public static void main(String[] args) {
        int[] A = {2, 3, 1, 5};
        System.out.println(solution(A));
    }

    public static int solution(int[] A) {
        boolean[] check = new boolean[A.length + 1];
        for (int i = 0; i < A.length; i++) {
            check[A[i] - 1] = true;
        }

        for (int i = 0; i < check.length; i++) {
            if (!check[i]) {
                return i + 1;
            }
        }

        return 0;
    }
}
