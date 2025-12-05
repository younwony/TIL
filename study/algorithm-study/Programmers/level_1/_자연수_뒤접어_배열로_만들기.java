package level_1;

public class _자연수_뒤접어_배열로_만들기 {

    public static void main(String[] args) {

        System.out.println(solution(12345));
    }

    private static int[] solution(long n) {
        String[] nStr = String.valueOf(n).split("");
        int[] answer = new int[nStr.length];
        for (int i = 0; i < nStr.length; i++) {
            int reversN = Integer.valueOf(nStr[i]);
            answer[nStr.length - 1 - i] = reversN;
        }
        return answer;
    }
}
