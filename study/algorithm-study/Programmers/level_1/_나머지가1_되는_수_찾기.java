package level_1;

public class _나머지가1_되는_수_찾기 {
    public static void main(String[] args) {
        System.out.println(solution(10));
    }

    public static int solution(int n) {
        int answer = n - 1;

        for (int i = 2; i < n; i++) {
            if (n % i == 1) {
                return i;
            }
        }

        return answer;
    }
}
