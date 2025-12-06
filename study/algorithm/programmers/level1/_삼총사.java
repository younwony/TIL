package level_1;

public class _삼총사 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{3,0,4,2,8}));
        System.out.println(solution(new int[]{1, 3, 1}));
        System.out.println(solution(new int[]{1, 1, 2, 3}));
    }

    // H - index
    public static int solution(int[] number){
        int answer = 0;
        int len = number.length;

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                for (int k = j + 1; k < len; k++) {
                    if (number[i] + number[j] + number[k] == 0) {
                        answer++;
                    }
                }
            }
        }

        return answer;
    }
}
