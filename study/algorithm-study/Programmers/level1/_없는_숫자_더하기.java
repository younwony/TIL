package level_1;

public class _없는_숫자_더하기 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,2,3,4, 6,7,8,0}));
        System.out.println(solution(new int[]{5,8,4,0,6,7,9}));
    }

    public static int solution(int[] numbers) {
        int answer = 0;

        boolean[] isNumbers = new boolean[10];

        for (int number : numbers) {
            isNumbers[number] = true;
        }

        for (int i = 0; i < isNumbers.length; i++) {
            if(!isNumbers[i]) answer += i;

        }

        return answer;
    }
}
