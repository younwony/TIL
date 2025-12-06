package level_2;

public class _n2배열_자르기 {
    public static void main(String[] args) {
        for (int i : solution(3, 2, 5)) {
            System.out.println("i = " + i);
        }
        for (int i : solution(4, 7, 14)) {
            System.out.println("i = " + i);
        }
        for (int i : solution((int) Math.pow(10, 7), 7, 14)) {
            System.out.println("i = " + i);
        }
    }

    public static int[] solution(int n, long left, long right) {
        int size = (int) (right - left + 1);
        int[] answer = new int[size];

        for (int i = 0; i < size; i++) {
            int x = (int) (left / n);
            int y = (int) (left % n);
            answer[i] = Math.max(x, y) + 1;
            left++;
        }

        return answer;
    }
}
