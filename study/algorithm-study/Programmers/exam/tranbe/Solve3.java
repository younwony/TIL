package exam.tranbe;

import java.util.Arrays;

public class Solve3 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{2, 2, 1}));
        System.out.println(solution(new int[]{3,2,1,2}));
    }

    private static int[] solution(int[] grade) {
        int[] answer = new int[grade.length];

        for (int i = 0; i < grade.length; i++) {
            answer[i] = grade[i];
        }

        Arrays.sort(grade);

        for (int i = 0; i < answer.length; i++) {
            int rank = 1;
            for (int j = grade.length - 1; j >= 0; j--) {
                if(answer[i] >= grade[j]){
                    break;
                }
                rank++;
            }
            answer[i] = rank;
        }

        return answer;
    }
}
