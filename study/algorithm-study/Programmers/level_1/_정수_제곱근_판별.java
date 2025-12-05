package level_1;

public class _정수_제곱근_판별 {

    public static void main(String[] args) {
        System.out.println(solution(121));
    }

    private static long solution(long n) {
        long sqrt = (long) Math.sqrt(n);
        return n == Math.pow(sqrt, 2) ? (long) Math.pow(sqrt + 1, 2) : -1;
    }
}
