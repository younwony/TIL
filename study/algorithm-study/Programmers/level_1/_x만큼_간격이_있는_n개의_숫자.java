package level_1;

public class _x만큼_간격이_있는_n개의_숫자 {

    public static void main(String[] args) {
        System.out.println(solution(2,5));
    }

    private static long[] solution(int x, int n) {
        long[] answer = new long[n];
        long answerNumber = x;
        for (int i = 0; i < n; i++){
            answer[i] = answerNumber;
            answerNumber += x;
        }
        return answer;
    }
}
